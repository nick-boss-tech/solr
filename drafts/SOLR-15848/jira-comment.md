We checked this against Solr main at commit 498a09aea97 and could not close it from here.

What we checked:

- The fix commits from the 8.11 release branch are not on main. The main branch has no copy of the 8.11 branch in this repository.
- The test class named in one of the commits (ScheduledTriggerIntegrationTest) was removed from main when the autoscaling framework was removed in 2020.
- Three of the other tests changed by the first commit still exist on main, and none of them is marked as a known failure there.

How we tried to reproduce it:

- Not attempted. Checking whether those three tests are stable needs a run of each on main.

What this means:

- Whether the 8.11 branch still needs these markers is a maintenance decision for the Solr developers. Whether the three remaining tests are stable on main has not been checked.

If you still see this:

- If the 8.11 branch is still maintained, please say so and we will check the markers there. If a test on main is unstable, please send the test name and a failing log from a current build.
