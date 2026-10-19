We checked this against Solr main at commit 498a09aea97 and could not finish the review yet.

What we checked:

- The fix for the specific deadlock in SlotAcc initialization (SOLR-16165) is on main.
- The build's error-prone configuration includes a check for class initialization deadlocks, but it is turned off there with a note to decide whether it is useful.

How we tried to reproduce it:

- Not attempted. The deadlock depends on the order in which classes load across threads, and a single test run does not show it.

What this means:

- The general review asked for in this ticket is not finished. We have not listed the remaining static initializers that reference subclasses, and we have not decided whether to enable the build check.

If you still see this:

- A stack trace from a class-loading deadlock on a current release, or the list of sites a check reports, would let us move forward. Please reopen with that information.
