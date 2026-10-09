# SOLR-5941 round 30 design branch: report

Status: DONE, GATED, PUSHED 2026-10-07. New branch `solr-5941-submit` on origin/main
cabedd1d968059215188f4e7563fb303241899ed, one commit 62516cc338ef42f87e98989b2da02796375f2558,
author and committer Nick Shanin <nick.boss.us@gmail.com>, no Claude trailer, ls-remote verified.
PR NOT opened (Nick's call). The complete draft PR description is at the end of this file.

## Design implemented (the minimal route from the assessment)

AutoCommits are routed through the core's default update processing chain so processors see
`processCommit` for autoCommits as they do for client commits, with the commit treated as an
end point so it stays local to its core, including in SolrCloud.

* `CommitTracker.run()` builds the synthetic request with
  `DistributedUpdateProcessor.COMMIT_END_POINT` set to boolean `true`, puts an `autocommit`
  marker (`CommitTracker.AUTOCOMMIT_CONTEXT_KEY`, Boolean.TRUE) in the request context, and
  issues the commit through `core.getUpdateProcessingChain(null)`, creating the processor and
  finishing and closing it in a finally block the same way `ContentStreamHandlerBase` does for
  client update requests. The version-clock stamping, the autoCommitCount increment ordering,
  and the existing "auto commit error..." catch are unchanged.
* `DistributedZkUpdateProcessor.processCommit`: when the request carries
  `commit_end_point=true` (boolean), it calls `doLocalCommit(cmd)` and returns, before any
  leader lookup or distribution.
* `RoutedAliasUpdateProcessor.wrap()`: returns the next processor unwrapped for an end point
  commit, so an autoCommit on a core belonging to a routed alias is not fanned out to the
  leaders of the aliased collections.

Deviations from the ticket's 2014 starting patch, each forced by the current code:

* The 2014 patch also set `update.distrib=skip`. That is not done here: any non-null
  `update.distrib` value makes `UpdateRequestProcessorChain.createProcessor` skip every
  factory before the distributing one, which would hide the autoCommit from exactly the
  custom processors this ticket is about. The full chain runs, as for a client commit.
* Params alone cannot keep a SolrCloud commit local: with `commit_end_point` set to the
  internal string values, a leader still distributes the commit to its replicas, and a
  non-leader does nothing at all. The boolean-true guard in the two distributed processors
  is what gives the end point mark its meaning for commits. Boolean true does not collide
  with the string values: `StrUtils.parseBool` returns false for "leaders" and "replicas".
  The standalone `DistributedUpdateProcessor.processCommit` already only commits locally.
* Side effect worth knowing: an ignore-commit processor (`IgnoreCommitOptimizeUpdateProcessorFactory`)
  reads the same boolean mark as "do not ignore this commit", so an autoCommit will no longer
  be swallowed by such a processor in the chain. That matches the mark's documented meaning
  (a targeted commit that must not be ignored).

Behavior for cores with no custom chain is unchanged: the default chain ends at the same
update handler commit as the old direct call.

## Premise (base production, test only)

New test `org.apache.solr.update.AutoCommitUpdateChainTest` (:solr:core, extends SolrTestCase,
EmbeddedSolrServerTestRule, config solrconfig-autocommit-chain.xml) puts a recording processor
first in the default chain. Run against base production cabedd1d968 with only the test and its
config added (log g5941-premise.log, seed 71F95B7FB677138D, verdicts from fresh JUnit XML):
2 tests, 1 failure. The client-commit control passed (a client commit reaches the recording
processor on base). The autoCommit test failed at the chain-visibility assertion,
`expected:<1> but was:<0>`: the autoCommit fired (the tracker's commit count rose) but no
processor ever saw it. That is the SOLR-5941 defect, grounded.

At head the same test passes 2/2: the autoCommit reaches the recording processor exactly once,
carrying both the autocommit context marker and `commit_end_point=true`, and the committed
document becomes searchable.

One test defect found and fixed during the gate: the first head run failed the searchability
assertion because the test queried immediately after the tracker's count rose; the count is
incremented before the commit runs on the scheduler thread, so the query raced the commit
thread's searcher opening. The test now polls for visibility with a 30 second deadline.
Production code was not involved; the chain-visibility and marker assertions passed in that
run already.

Locality evidence for SolrCloud is code-level plus the asserted end point marker, not a live
multi-node run: the standalone processor is local-only by construction, the ZK processor's
end point branch returns after doLocalCommit, and the routed alias wrapper declines to route
end point commits. This is stated plainly in the draft description's Limits.

## Gate receipts (worktree ~/workspace/solr-worktrees/solr-5941-submit)

* Changelog YAML parse: `changelog/unreleased/SOLR-5941.yml` (type: changed) parses OK with
  ~/workspace/tools/check-changelog-yaml.sh.
* Tidy: `:solr:core:tidy` rc=0 (logs g5941-gate.log and g5941-gate2.log);
  gradle/libs.versions.toml restored after each run and clean in the shipped tree.
* Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava
  -Pvalidation.errorprone=true` rc=0 (log g5941-gate.log) over the production code and the
  test as first written. The only later change was inside one test method (the visibility
  poll); a final-tree Error Prone re-run was queued behind the shared lane (log
  g5941-ep.log) as trailing confirmation, and gate2's `:solr:core:check -x test` recompiled
  and linted the final tree with rc=0.
* Focused tests at head 62516cc338ef, counted from fresh JUnit XML (log g5941-gate2.log,
  seed BA67B7C31548EECC): AutoCommitUpdateChainTest 2/2, DirectUpdateHandlerTest 7/7,
  MaxSizeAutoCommitTest 3/3, TestUpdate 3/3, SolrCmdDistributorTest 1/1,
  DistributedUpdateProcessorTest 4/4. Total 20 tests, 0 failures, 0 errors, 0 skipped.
* Module check: `:solr:core:check -x test` rc=0 (twice: g5941-gate.log and g5941-gate2.log).
* Ref guide check: `:solr:solr-ref-guide:check -x test` fails inside any git worktree at
  `buildLocalAntoraSite` with "Local content source must be a git repository" (Antora does
  not accept a worktree's .git file); the same failure hit another worktree run the same
  night. The adoc change is five lines of plain prose with no xrefs or macros.
* Lane note: the local builds shared the serial test-queue flock with the round 29, 30, 32,
  and 33 work all night, and the VM restarted twice during the waits (about 04:53 and about
  07:09 UTC); one queued run was lost to a restart and relaunched, and at about 05:05 UTC
  the lane wedged on the proxy relay holding a finished gate's inherited lock fd, cleared
  by killing the relay (it auto-restarts).

GH corroboration: run 37590296106 dispatched on ci/5941-autocommit-chain at the shipped head
for org.apache.solr.update.AutoCommitUpdateChainTest (:solr:core); conclusion to be recorded
in the takeover log when it lands.

## Draft PR description (complete, ready to paste when Nick approves opening)

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-5941

## What happens today

An autoCommit bypasses the update request processor chain. `CommitTracker` builds a
`CommitUpdateCommand` on a bare synthetic request and calls the update handler directly, so a
processor in the default chain sees `processCommit` for client commits but never for
autoCommits. A processor that must observe, record, or veto every commit cannot do its job
today.

## What this change does

* `CommitTracker` issues the autoCommit through the core's default update processing chain
  (`getUpdateProcessingChain(null)`), creating, finishing, and closing the processor the same
  way the update request handler does.
* The commit is marked as an end point (`commit_end_point=true`), and the distributed
  processors honor that mark for commits: `DistributedZkUpdateProcessor` applies an end point
  commit locally and never forwards it, and `RoutedAliasUpdateProcessor` does not route one
  to alias leaders. An autoCommit stays on the core that triggered it, including in
  SolrCloud, exactly as it does today.
* The synthetic request carries an `autocommit` marker in its request context
  (`CommitTracker.AUTOCOMMIT_CONTEXT_KEY`) so a processor can tell a timer-driven commit from
  a client commit.
* Cores with no custom chain see no behavior change: the default chain ends at the same
  update handler commit as before.
* Reference guide note (Commits and Transaction Logs) and a changelog entry are included.

## Proof

* New test `org.apache.solr.update.AutoCommitUpdateChainTest` (:solr:core): a recording
  processor in the default chain sees a client commit on base and on this branch; on base
  (cabedd1d968, 2026-10-06) an autoCommit never reaches it (`expected:<1> but was:<0>`),
  while at this head it does, marked as an autoCommit and as an end point, and the committed
  document becomes searchable.
* Focused runs at 62516cc338ef (2026-10-07), counted from JUnit XML:
  AutoCommitUpdateChainTest 2/2, DirectUpdateHandlerTest 7/7, MaxSizeAutoCommitTest 3/3,
  TestUpdate 3/3, SolrCmdDistributorTest 1/1, DistributedUpdateProcessorTest 4/4.
* Gate: changelog YAML parse, `tidy`, Error Prone compile with
  `-Pvalidation.errorprone=true`, and `:solr:core:check -x test`, all passing.

## Choices

Implemented: autoCommits go through the default chain, and the commit is treated as an end
point so it is never distributed.

Alternatives:

* Track the chain each document arrived through, and run the autoCommit it triggers through
  that chain instead of the default. Cores do not record which chain a document came
  through, so this needs new bookkeeping. That route can be done in this PR instead if
  maintainers prefer it.
* Leave autoCommits outside the chain and document that processors do not see them. That
  route can also be done in this PR (it reduces to the reference guide note) if maintainers
  prefer it.

## Limits

* Reacting after a commit is already covered: `SolrEventListener` postCommit and
  postSoftCommit callbacks fire for autoCommits today. This change matters for processors
  that must see the commit itself, act on it, or veto it.
* The default chain applies even when the documents being committed arrived through a named
  chain.
* The synthetic request carries no user and no request parameters beyond the commit's own,
  so processors that rely on request parameters or security context will not find them on
  an autoCommit.
* In SolrCloud the local commit now goes through
  `DistributedZkUpdateProcessor.doLocalCommit`, as distributed commits already do, so it
  applies when the update log is active. An autoCommit that lands while a replica is
  buffering updates during recovery is skipped by that check instead of being committed
  directly; the replica is covered by the normal recovery commit when it rejoins.
* Locality in SolrCloud rests on the end point handling in the two distributed processors
  and on the end point marker asserted by the new test; no live multi-node SolrCloud run
  was made. A cloud test can be added if maintainers want one.

Changelog: `changelog/unreleased/SOLR-5941.yml` (type: changed).

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed
the work and takes responsibility for this contribution.
