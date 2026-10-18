We could not reproduce this on Solr main at commit 498a09aea9, and the seed in the original report does not apply to the current code, so this stays open.

What we checked:

- The 2014 workaround commit b221115ebc is on main. The test TestDistribDocBasedVersion still has the extra commit() call at line 104 of the test file, which avoids the failure as described in the ticket.

How we tried to reproduce it:

- Removed that commit() call in a scratch copy of main at commit 498a09aea9 and ran TestDistribDocBasedVersion six times, each with a new random seed. All six runs passed.
- The seed in the original report was recorded against the 2014 code. The ticket's own comments say a seed only reproduces when the code is unchanged, so that seed was not run on current main.

What this means:

- The failure described here did not appear on current main in these runs. Because the original seed cannot be replayed on current code, we cannot confirm or rule out the underlying cause from this test alone.

If you still see this:

- A failure on current main with the commit() line removed, including the seed and the log. Or the steps to reproduce on the 2014 revision r1574381, which the original description gives. Please reopen the ticket with that information.
