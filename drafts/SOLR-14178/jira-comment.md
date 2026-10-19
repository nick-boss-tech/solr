We checked this against Solr main at commit 498a09aea97 and could not yet measure the output size.

What we checked:

- The five test classes named in the report are present on current main, and no commit names this ticket.

How we tried to reproduce it:

- Not run. The check needs the output size of each named test from a test run.

What this means:

- We have not yet measured whether these tests still write large amounts of output on current main. The ticket remains open until that is done.

If you still see this:

- Please send the test run output sizes for the named tests on a current build. Please reopen with that information.
