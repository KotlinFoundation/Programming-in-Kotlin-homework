package org.jub.kotlin.hometask4

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Timeout
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal object ApplicationTest {
    private const val resultsFile = "results.txt"
    private var baos: ByteArrayOutputStream = ByteArrayOutputStream()
    private var helpMsg = "no message"

    @JvmStatic
    @BeforeAll
    fun setHelpMessage() {
        setUp()
        val app = Application.create(resultsFile, tasks)
        val commands = listOf("help")
        setSystemIn(commands.toInput())
        app.run()
        Thread.sleep(1.secs)
        helpMsg = baos.toString(Charset.defaultCharset())
        setDown()
    }

    private fun setSystemIn(input: String) = System.setIn(input.byteInputStream())

    private fun List<String>.toInput() = joinToString(System.lineSeparator())

    private val Int.secs: Long
        get() = this * 1000L

    @BeforeEach
    fun setUp() {
        baos.reset()
        val ps = PrintStream(baos, true, StandardCharsets.UTF_8.name())
        System.setOut(ps)
        File(resultsFile).writer().write("")
    }

    @AfterEach
    fun setDown() {
        System.setOut(System.out)
        System.setIn(System.`in`)
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    fun limitThreads() {
        val tasks = listOf(Callable<Int> { Thread.sleep(5.secs); 666 })
        val app = Application.create(resultsFile, tasks)

        val commands = List(7) { "task long 0" }
        setSystemIn(commands.toInput())

        app.run()
        Thread.sleep(7.secs)

        val outputs = File(resultsFile).readLines().size
        assertTrue(outputs <= 6, "You cannot finish 6+ tasks 5 secs each in 7 secs")
        assertTrue(outputs >= 2, "It looks like tasks are not performed in parallel")
        app.waitToFinish()
    }

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    fun finishGrace() {
        val tasks = listOf(Callable<Int> { Thread.sleep(5.secs); 666 })
        val app = Application.create(resultsFile, tasks)

        val commands = List(6) { "task keep 0" } + "finish grace" + "task drop 0"
        setSystemIn(commands.toInput())

        app.run()
        Thread.sleep(20.secs)

        val outputs = File(resultsFile).readLines().size
        assertEquals(6, outputs)
        app.waitToFinish()
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    fun finishForce() {
        val tasks = listOf(Callable<Int> { Thread.sleep(3.secs); 666 }, Callable<Int> { Thread.sleep(1.secs); 23 })
        val app = Application.create(resultsFile, tasks)

        val commands = List(2) { "task keep 1" } + List(7) { "task maybeDrop$it 0" } + "finish force"
        setSystemIn(commands.toInput())

        app.run()
        Thread.sleep(10.secs)
        app.waitToFinish()

        val outputs = File(resultsFile).readLines().size
        assertTrue(outputs <= 6, "Due to force finish app should have dropped at least some tasks")
        assertTrue(outputs >= 2, "Instant tasks should have been definitely completed")
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    fun outputFormat() {
        val tasks = listOf(Callable<String> { "Hello" }, Callable<Int> { Thread.sleep(2.secs); 23 })
        val app = Application.create(resultsFile, tasks)

        val commands = listOf("task world 0", "task number1 1", "task number2 1", "get")
        setSystemIn(commands.toInput())

        app.run()
        Thread.sleep(5.secs)
        app.waitToFinish()

        val outputs = File(resultsFile).readLines()
        assertEquals("world: Hello", outputs[0])
        assertContains(outputs, "number1: 23")
        assertContains(outputs, "number2: 23")
        assertTrue(baos.toString(Charset.defaultCharset()).isNotEmpty())
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    fun exceptions() {
        val tasks = listOf(Callable<String> { "Still Alive" }, Callable<Int> { throw Exception("Disgusting") })
        val app = Application.create(resultsFile, tasks)

        val commands = listOf("task iam 0", "task burn1 1", "task burn2 1", "task nevertheless 0", "task heh 0")
        setSystemIn(commands.toInput())

        app.run()
        Thread.sleep(1.secs)
        app.waitToFinish()

        val outputs = File(resultsFile).readLines()
        assertContains(outputs, "iam: Still Alive")
        assertContains(outputs, "nevertheless: Still Alive")
        assertContains(outputs, "heh: Still Alive")
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    fun help() {
        val tasks = listOf(Callable<Int> { 42 })
        val app = Application.create(resultsFile, tasks)
        val commands = listOf("help")
        setSystemIn(commands.toInput())

        app.run()
        Thread.sleep(1.secs)
        app.waitToFinish()

        val consoleOutput = baos.toString(Charset.defaultCharset())
        assertTrue(consoleOutput.contains("task"))
        assertTrue(consoleOutput.contains("finish"))
        assertTrue(consoleOutput.contains("get"))
        assertEquals(helpMsg, consoleOutput)
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    fun input() {
        val tasks = listOf(Callable<Int> { Thread.sleep(1.secs); 42 })
        val app = Application.create(resultsFile, tasks)

        val commands =
            listOf("rubbish", "finish wrong", "task ok1 0", "task heh hah", "task 1 2 3", "task ok2 0", "task n 42")
        setSystemIn(commands.toInput())

        app.run()
        Thread.sleep(3.secs)
        app.waitToFinish()

        val outputs = File(resultsFile).readLines()
        assertEquals(2, outputs.size)
        assertContains(outputs, "ok1: 42")
        assertContains(outputs, "ok2: 42")
    }

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    fun doNotBlockInput() {
        val tasks = listOf(Callable<String> { "Hello, world!" })
        val app = Application.create(resultsFile, tasks)

        val size = 100

        val commands = List(size) { "task hundred 0" } + listOf("help", "get", "finish grace")
        setSystemIn(commands.toInput())

        app.run()
        Thread.sleep(1.secs)
        assertTrue(baos.toString(Charset.defaultCharset()).isNotEmpty())
        Thread.sleep(10.secs)
        assertContains(baos.toString(Charset.defaultCharset()), "Hello, world! [hundred]")

        val outputs = File(resultsFile).readLines()
        assertEquals(size, outputs.size)

        app.waitToFinish()
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    fun doNotBlockInput2() {
        val tasks = listOf(Callable<String> { Thread.sleep(3.secs); "should not happen again" })
        val app = Application.create(resultsFile, tasks)

        val commands = List(64) { "task this 0" } + listOf("help", "finish force")
        setSystemIn(commands.toInput())

        app.run()
        Thread.sleep(1.secs)
        assertEquals(helpMsg, baos.toString(Charset.defaultCharset()))

        app.waitToFinish()
    }

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    fun simple() {
        val tasks = listOf(Callable<String> { Thread.sleep(7.secs); "7" }, Callable<String> { Thread.sleep(3.secs); "3" })
        val app = Application.create(resultsFile, tasks)

        val commands = listOf("task seven 0", "task three1 1", "task three2 1", "wake up", "finish grace")
        setSystemIn(commands.toInput())

        app.run()
        Thread.sleep(10.secs)
        assertTrue(baos.toString(Charset.defaultCharset()).isNotEmpty())

        val outputs = File(resultsFile).readLines()
        assertEquals(3, outputs.size)
        val concatOutputs = outputs.joinToString(" ")
        assertContains(concatOutputs, "seven: 7")
        assertContains(concatOutputs, "three1: 3")
        assertContains(concatOutputs, "three2: 3")

        app.waitToFinish()
    }

    @Test
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    fun twoAtATime() {
        val tasks = listOf(Callable<String> { Thread.sleep(1.secs); "I have to stop Noah" })
        val resultsFile2 = "results2.txt"
        val app1 = Application.create(resultsFile, tasks)
        val app2 = Application.create(resultsFile2, tasks)

        val commands = List(10) { "task sec 0" } + "finish grace"
        setSystemIn(commands.toInput())
        app1.run()
        Thread.sleep(2.secs)
        setSystemIn(commands.toInput())
        app2.run()
        Thread.sleep(10.secs)

        val outputs1 = File(resultsFile).readLines()
        val outputs2 = File(resultsFile2).readLines()
        assertEquals(10, outputs1.size)
        assertEquals("sec: I have to stop Noah", outputs1.first())
        assertEquals(10, outputs2.size)
        assertEquals("sec: I have to stop Noah", outputs2.first())

        app1.waitToFinish()
        app2.waitToFinish()
    }

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    fun lotsOfWork() {
        val tasks = listOf(
            Callable { "▲▼▲▼▲▼" },
            Callable { 1899 },
            Callable { throw Exception("Alfred") },
            Callable { "Maura" to 1011 }
        )

        val size = 10_000

        val commands = List(size) { "task #$it ${it % tasks.size}" } + "finish grace"
        setSystemIn(commands.toInput())
        val app = Application.create(resultsFile, tasks)
        app.run()
        app.waitToFinish()

        val outputs1 = File(resultsFile).readLines()

        assertEquals(size * 3 / 4, outputs1.size)
    }
}
