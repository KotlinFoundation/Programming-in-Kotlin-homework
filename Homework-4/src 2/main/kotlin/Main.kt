import org.jub.kotlin.hometask4.Application
import org.jub.kotlin.hometask4.tasks

fun main() {
    val app = Application.create("results.txt", tasks)
    app.run()
    app.waitToFinish()
}
