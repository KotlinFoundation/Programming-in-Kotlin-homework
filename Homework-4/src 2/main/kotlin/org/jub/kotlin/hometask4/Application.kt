package org.jub.kotlin.hometask4

interface Application : Runnable {
    /**
     * Wait for your application to stop.
     * You might need this, you might not, that's fine.
     */
    fun waitToFinish() {}

    companion object {
        /**
         * Creates a new Application, which writes results to the file at the given path.
         * You have to think of what type the `tasks` should be yourself.
         *
         * @param resultsFile path to a file in which results whould be stored
         * @param tasks List of available tasks.
         */
        fun create(resultsFile: String, tasks: Any?): Application = TODO()
    }
}
