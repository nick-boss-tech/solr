# PR #5098 direct leader-only routing follow-up: implementation handoff

Status: Implemented, Proof Pending.

- Reviewed PR head O: `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d` on `nick-boss-tech/solr:solr-18530-submit`.
- Pushed final head F: `d822b88b1bd58c197e4ad09c53ffff0920b76190`; the fork ref and live PR head both read back as F.
- Parent: O. This is an ordinary fast-forward commit on the existing PR branch.
- Reviewer comment: [iamsanjay's inline comment](https://github.com/apache/solr/pull/5098#discussion_r4489773547), submitted 2026-11-06 06:44:48 UTC.
- Source change: `solr/test-framework/src/java/org/apache/solr/cloud/AbstractFullDistribZkTestBase.java` adds an overload for the existing cloud-client helper that configures direct updates to shard leaders only. The original overload delegates with that option disabled, retaining its behavior for existing callers.
- Test change: `solr/core/src/test/org/apache/solr/cloud/RecoveryAfterSoftCommitTest.java` opts the post-partition client into both leader preference and direct leader-only routing, then asserts both settings before issuing the adds.
- The source diff is limited to those two Java files. `git diff --check` passed. No Gradle task, compiler, formatter or test ran on Windows.
- Gate: `gates/pr-5098-direct-leader-only-round-1.md`, pinned to F. It compares O/F, runs the focused test at six seeds, requires a compiling omission mutation to fail at the direct-only assertion, restores F and reruns, then checks tidy and root `check -x test`.
- The previous `receipts/SOLR-18530.md` green result covers an earlier head and its changelog-only carryover to O; it does not prove F. The separately assigned RC3 matched-pair work remains out of scope for this PR gate.
- No public reply has been posted. Prepare the concise reply after the new receipt is green at F and the PR head/discussion are refreshed.
