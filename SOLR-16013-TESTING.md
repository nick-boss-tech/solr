# SOLR-16013 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

On node shutdown, `ZkController.close()` released the ZK election node and
closed the `Overseer` in parallel (two `customThreadPool.execute` tasks), so
a new overseer could be elected and re-read the overseer queue *before* the
old overseer finished draining in-flight tasks — one command processed
twice. Concrete symptom from the ticket: N ADDREPLICA commands turning into
N+1 CREATE core commands during k8s rolling restarts.

Fix, exactly the ordering the maintainers converged on in the ticket
(Hostetter asked why the closes aren't serialized; Bialecki agreed the
order is wrong):

- `solr/core/src/java/org/apache/solr/cloud/ZkController.java` — the two
  closes are now a single pool task that closes the `Overseer` first
  (drains in-flight queue tasks) and only then closes
  `overseerElector.getContext()` (releases the election node so a new
  overseer is elected). Still runs on the close thread pool; the rest of the
  shutdown sequence is untouched.
- `solr/core/src/java/org/apache/solr/cloud/OverseerElectionContext.java`
  — `close()` now calls `overseer.close()` *before* setting `isClosed =
  true`, so `runLeaderProcess` can't observe "closed" while the overseer is
  still processing. Lock analysis: `close()` already called
  `overseer.close()` while holding the context monitor, and
  `Overseer.close()` (synchronized on the Overseer, not the context) never
  calls back into the election context — no new lock ordering.

Caveat (David Smiley on the ticket): this race is specific to the
centralized overseer; the distributed collection API mode in Solr 9+
(SOLR-15146) doesn't have it.

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

## Test ideas (not written)

This is a shutdown-timing race; a deterministic unit test is hard.

- Integration (timing-sensitive, document flakiness): mini SolrCloud
  cluster, submit ADDREPLICA, kill the overseer node mid-processing, assert
  no duplicate core/replica is created.
- At minimum, verify the new close ordering by code inspection.
- Existing overseer / ZkController shutdown test suites for regressions.

## Patch limits and follow-ups

- **Not compiled or tested.**
- The thread pool introduced "to make tests run faster" (per Joel
  Bernstein's comment) is retained — only the two overseer-related closes
  were serialized into one task.
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
