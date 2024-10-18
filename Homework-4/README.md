# Task 4. Non-blocking UI simulator

Console application with non-blocking input that writes its results to a file.

## User input
- `task NAME X`: execute task `X`, name it `NAME` and write the result to the results file
- `get`: output the last result and its name to the console
- `finish grace`: stop accepting new tasks, finish all _pending_ tasks, and stop the application
- `finish force`: stop the application without waiting for _pending_ tasks to finish
- `clean`: clean the results file
- `help`: output guidelines to the console

## Details

There is a list of predefined thread-blocking tasks. `X` in the `task` command is expected to be an Int, index of the desired task to be performed. Each task is `Callable<T>`, which blocks the execution thread for 300–5,000 ms. The `T.toString()` method is implemented so that `T` is represented as a single line of text. Performing a task means getting the result (`T`) of the task and writing it to the results file, which is provided to the application at the start. `NAME` in `task` should not have whitespaces.

Records in file must look like this: `NAME: T`. Outputs in the console must look like this: `T [NAME]`. Errors must not be written to the results file. You can write them to a separate file or print to the console.

The `get` command should read the representation of the last result and output it together with the name that the result/task was given.

Application should not use more than 6 threads. Console input should always be available (never blocked), except when `finish` has been called.

Application should start in 1.5 seconds.

If the input stream closes (you get null), the application should finish. It is up to you to decide whether it should finish gracefully or not.

**For the instructor: How to grade an assignment:** 
A student can get 7 points in total. It does not matter what they use: threads or coroutines. 4 points for correct implementation of the task (it works well, passes all tests, etc.) and 3 points for correctly choosing the concurrent data structures and thread-safe work with them.

## Verification

Run tests locally:`./gradlew test`;

Run Detekt locally: `./gradlew detektCheckAll`;

Run Diktat locally: `./gradlew diktatCheckAll`;

Run the whole Github workflow locally: `./gradlew githubWorkflow`.
