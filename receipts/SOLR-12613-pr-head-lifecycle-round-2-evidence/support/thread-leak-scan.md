# Thread-leak signal scan

The fresh picker, four paired-neighbor, and combined fail-before logs/XML were searched for explicit leak reports (`thread leak`, `leaked thread`, orphaned/still-running threads, and thread-termination failures). No explicit thread-leak message was found. Normal `com.carrotsearch.randomizedtesting.ThreadLeakControl` stack frames in failure traces are framework frames, not a leak report.
