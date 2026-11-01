# State verification: SOLR-18600 HTTP client binary compatibility restoration

Verified 2026-11-01 about 03:30 MST, overnight run. Records only; no PR
opening, no Jira post, no build run.

## Live head

- Branch `solr-18600-http-abi-restore-submit` on the fork is at
  `c40665af8d6bd23c85fef565f93de5004a7b2ca4`, confirmed by
  `git ls-remote fork` this run. It matches the board receipt
  (`receipts/SOLR-18600.md` on coordination-ui) and the local worktree
  `~/workspace/solr-worktrees/18600-restore`, which is clean at that head.
- Base is `origin/branch_10x` at `a336bf871b2c`. Upstream branch_10x has
  since moved to `a5c8060f00` (DOAP changes for release 10.1.0), 26 commits
  ahead of the branch base. The branch base is still an ancestor of the
  upstream tip, so a rebase would be a fast-forward-style replay, not a
  conflict-prone merge, but it still needs doing before opening.
- Diff at the head: 16 files, 1067 insertions, 601 deletions. The bulk is
  the restored `HttpSolrClientBase` (405 lines) and
  `HttpSolrClientBuilderBase` (289 lines), the moved implementation out of
  `HttpSolrClient`, two new binary-compat guard tests, a changelog
  fragment, and a 10.2 upgrade-notes entry.

## Jira state

- SOLR-18600: Open, no resolution, reporter Nick Shanin, unassigned,
  labels `binary-compatibility` and `solrj`, priority Major, last updated
  2026-10-27. No PR exists on apache/solr from nick-boss-tech for it.
  Related ticket SOLR-17968 is the investigation source.

## Gate state, from the receipt

Gate evidence stands at the pushed head, on this VM, tree identical to
the head:

- Changelog YAML parse OK; tidy rc=0 with the tree unchanged afterwards.
- Compiles with `-Pvalidation.errorprone=true`: solrj, solrj-jetty, core
  main and test, solrj-zookeeper main and test compile tasks, all rc=0.
- `:solr:solrj:check -x test` and `:solr:solrj-jetty:check -x test` rc=0
  (spotless, forbiddenapis, ecj lint included).
- Focused suites at seed `18600C0FFEE18600`, fresh JUnit XML:
  HttpSolrClientBinaryCompatTest 4/4, HttpJettySolrClientBinaryCompatTest
  2/2, HttpJdkSolrClientTest 40/40, HttpJettySolrClientTest 49/49,
  CloudHttp2SolrClientBuilderTest 14/14, CloudSolrClientCacheTest 6/6,
  ConcurrentUpdateJdkSolrClientTest 11/11, plus LBSolrClientTest and
  LBSolrClientRetryUnsentTest green in the same run.
- Proof pair, the decisive leg: consumer bytes compiled against released
  10.0.0 fail on published 10.1.0 with `NoSuchMethodError` (exit 1) and
  pass on this branch's built solrj jar (exit 0, `CONSUMER OK`); the
  withHttpClient control consumer also passes.

## Limits carried from the receipt

- The full `:solr:core:test` suite was not run; core main and test sources
  compile and the focused SolrJ suites above are the gate's test evidence.
- A japicmp closure over the whole artifact was not run; the descriptor
  guard tests cover the HTTP client cluster named in the ticket.
- The fix targets branch_10x (the 10.2 line). The same break exists in the
  10.1.0 release only; a 10.1.1 backport or a main port is a separate step
  on Nick's call.

## What opening would need

1. Nick's explicit go for the opening itself. Approving this record does
   not approve opening.
2. A live recheck of the PR cap. The recorded non-draft open set was
   exactly 7 as of 2026-10-29 evening (#5192, #5098, #5079, #5016, #4968,
   #5266, #5265), so a new open PR needs a freed slot; otherwise it goes
   up as a draft with the one plain sentence that draft status is due to
   the cap and the work is complete and ready for review.
3. A rebase onto current branch_10x (26 commits of drift) and a re-gate at
   the new head, since the gate evidence above is at the old head. If the
   rebase is a clean replay with no content change, the re-gate is still
   the gate rule for a moved head, not an optional step.
4. A PR description following pr-formula.md, stating plainly what ran, at
   which head, what passed, and that the new test fails without the fix
   (the proof pair above). No internal process vocabulary in public text.
5. No Jira comment is needed to open; the ticket already describes the
   break. The 10.1.1 backport and main port decisions stay separate and
   are not part of the opening.
