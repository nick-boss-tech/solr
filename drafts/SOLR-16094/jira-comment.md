We checked this against Solr main at commit 498a09aea97 and could not reproduce it.

What we checked:

- SolrCore.reload takes the core's reload lock before it does any work. The reporter's hang lies between that lock and the log line that announces the new core, so the cause is in that block or in the code it calls.
- The test has no known-issue marker on main, so it runs in the normal suite.

How we tried to reproduce it:

- Not attempted on macOS. The failure is reported only there, and our check runs on Linux.

What this means:

- We have not identified which call in that block hangs. A thread dump taken while the test is hung would show it.

If you still see this:

- A thread dump from a hung run on macOS, with the Solr version and the Java version, would let us find the blocking call. Please reopen with that information.
