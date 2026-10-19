We looked at this against Solr main at commit 498a09aea9 (October 7, 2026). The test class is not excluded from the per-replica state mode, so it runs in that mode on some runs, chosen at random. An earlier run of the test class passed, but that run did not record which mode it used, so it does not show whether the failure with that mode is gone.

What we checked:

- How the test framework chooses the per-replica state mode for each test class.
- The test class setup, which does not exclude that mode.

How we tried to reproduce it:

- One earlier run of the test class passed. We have not yet run it with the per-replica state mode forced on, over several seeds.

What this means:

- The failure needs the per-replica state mode forced on to show up reliably. We are leaving the ticket open until that run is done.

If you still see this:

- Please send the Solr version, the test seed, and the failing output (the assertion and stack trace) from a run with the per-replica state mode on. With those, we can check the failure directly.
