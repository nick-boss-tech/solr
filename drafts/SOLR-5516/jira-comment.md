We reran ChaosMonkeyNothingIsSafeTest against Solr main at 498a09aea9 with the nightly test group enabled. It passed on the first run.

One passing run does not show the problem is gone, because the failure is intermittent on slow machines. The 2.5-second sleep before the replacement leader syncs is still the only mitigation on main (ShardLeaderElectionContext.java, around line 209). We are leaving this open until it can be reproduced or shown fixed by a change to the sync path.

If you still see this:

- Reopen with the Solr version, the test seed and the failing log, so the run can be repeated with that input.
