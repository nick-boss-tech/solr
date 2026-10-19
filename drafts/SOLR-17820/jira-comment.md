We checked a Solr code snapshot from October 7, 2026 (commit 498a09aea9). In that code, the shard health value is computed before the step that marks replicas on dead nodes as down, so a replica still listed as active on a node that is no longer live is counted as active, and the shard can report GREEN. This is fixed by commit 7e3b175021 (SOLR-17820, #5046), which is on main and in the 10.x and 10.1 branches.

What we checked:

- ClusterStatus.java: the health calculation, the collection status step, and the live-node check, with the order in which they run.
- Commit 7e3b175021 and the test it adds to ClusterStatusTest.

How we tried to reproduce it:

- We did not run a reproduction in this pass. The conclusion comes from reading the code path.

What this means:

- The example in the report, with one replica in state "down", gives YELLOW in the code we checked. The GREEN case is a replica that is still marked active on a node that is not live, which the fix addresses.

If you still see this:

- Please send the CLUSTERSTATUS output for the affected shard, the live node list, and the Solr version. With those, we can check which path the report took.
