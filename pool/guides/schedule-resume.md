# Guide: pausing a session until a later time (schedule_resume)

Status: reference, for every pool host.

## What it is

`mcp__user-input__schedule_resume` wakes this same conversation at a time you choose, with all of its context. Use it when you are waiting on something external: a Gradle run in the queue, a live Solr check, a review, a push that is still in a hook, or a reply from another host.

Do not use `schedule_task` for this. `schedule_task` starts a new session that does not have the conversation, so it loses what you were doing.

## How to use it

1. Check what is still running and where its result will be written, so the note can point to it.
2. Call `schedule_resume` with:
   - `wakeTime`: a natural-language time, such as `in 25 minutes` or `tomorrow 9am`. Interpret it in the UTC clock the session uses.
   - `note`: a message to your future self. Say what is pending, the paths or pids to check, what each result means, what to do if it passed, what to do if it failed, and what you must not do (for example, "do not post", "do not push").
3. End your turn. Do not poll in a loop while you wait.

## Rules

- One wake per session. A new `schedule_resume` replaces the pending one.
- A wake fires once. If the thing you waited for is still not done, check and schedule another wake.
- A wake can fire early. The user can wake the session before the time you set, so the note must still make sense if you wake early and the job is only half done.
- The note is the only memory of your reasoning. Write the facts you need, not just "check the result".
- Use a short wait (about 15 to 25 minutes) for a Gradle run. A full gate runs about 40 minutes.

## Practical notes

- A foreground shell command times out after 2 minutes by default. For a longer wait, set `timeout`, but prefer a wake over a long sleep.
- Long jobs belong in the queue (`/workspace/gates/queue/tick.sh`), not in a shell loop. The queue starts the next job when the current one ends.
- Stop a process you started by PID only. Do not use `pkill -f` with a pattern: it can match your own shell.

## Example note

    SOLR-NNNN gate, round 2. Queued 14:20Z (pid 1234). Result file
    /workspace/gates/logs/vm2/NNNN-result.txt. Check GATE END, the HEAD line
    counts (expect 22 of 22), and legB. If green, write the receipt on
    coordination (new top section, Nick Shanin author, no trailers). If a test
    failed, record the test name and the failure shape and stop. Do not
    relaunch. Do not push. Do not force push.
