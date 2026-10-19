We checked this against Solr main at commit 498a09aea97 and could not confirm the failure yet.

What we checked:

- The test is still marked as a known issue (AwaitsFix) on main, so it is skipped in normal runs.
- The earlier change that marked it as a bad apple was not a fix, and the thread-leak fix from SOLR-16046 is on main.

How we tried to reproduce it:

- We ran the test eight times with the known-issue group enabled. All eight passed. Eight passes are not enough to rule out a failure rate near 10 percent.

What this means:

- We have not confirmed whether the failure still happens on current main. A longer run is needed to measure the rate.

If you still see this:

- A failing run's log or stack trace from a current release, with the Solr version, would help. Please reopen with that information.
