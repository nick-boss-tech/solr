# Assignment: PR 2452 UBI context fix, rework and Linux gate

Status: OPEN

## Authorization and ownership

- Tags: implementation, gate, review, records, draft.
- Intended host: any available capable Linux host with the branch's JDK/toolchain and build dependencies, one gate at a time under its host build lock. This is implementation followed by proof, not a request to gate an already prepared PR 2452 head.
- Source: Nick explicitly asked to post an assignment to rework the candidate on top of PR 2452, or take PR 2452's changes on top of main, and gate the result.
- Supersedes execution of `gates/pr-5238-ubi-context-round-1.md`; see `gates/pr-5238-ubi-context-round-1-standdown.md`. Preserve the old branch and records. The earlier green test-only listener contract proof is separate and remains unchanged.
- Before claiming, refresh the live board and check for an existing claim or deliverables for this exact slice. First claim wins. Claim the implementation slice with the selected base, intended branch, UTC start, host, tags and deliverables. Complete it through a pushed candidate and Linux proof, or an evidence-backed report explaining a concrete remaining blocker.

## Starting evidence

Verified from live GitHub before posting:

- Target PR: `https://github.com/apache/solr/pull/2452`, OPEN and draft, Eric Pugh (`epugh`), title User Behavior Insights implementation for Apache Solr.
- Actual contributor branch: `epugh/solr:ubi`, head H `6402b4b528d3ec8daa134772abc84532380e3f7c`.
- PR base reported by GitHub: main at `1869a60e2e3fa6361822a471bbc0fa7dbb7bf5cb`. Pin the actual merge-base when reconstructing the PR's changes; do not assume that the reported base tip is the merge-base.
- Source candidate on Nick's fork: `solr-11994-ubi-context-submit` at F0 `09343928e7d2ba5cb5e7def1d0a550337b67a7d2`.
- F0's parent I0 is `3a362dcbcb036a4cfe9a6df413057d90666cd070`, which imports only the real UBIComponent.java and LoggingStream.java from H, byte-identical at I0. I0's parent is the listener example C `c2c387064a83def34cece4163eccded28318caeb`.
- **Only I0..F0 is the candidate repair to port:** a fresh StreamContext per recording plus `solr/core/src/test/org/apache/solr/handler/component/UBIRecordingContextTest.java`. Do not port I0's imported-file commit, the listener hook, SearchRequestListenerUbiTest, or any other PR 5238-only change.
- Current claim and analysis source: `claims/pr-5238-ubi-context-windows-round-1.md` and `reports/pr-5238-ubi-context-windows-round-1.md`.

No code in F0 has been compiled or tested on Windows. No runtime proof of the suspected race exists yet. The live PR body has no JIRA key; SOLR-11994 belongs to the listener work. Do not invent a UBI ticket or reuse that ticket in new commit messages. A new fork branch such as `ubi-pr2452-context-submit` identifies this no-JIRA PR follow-up; if a real matching UBI issue is established, record its key and use it instead. Never push to epugh/solr or apache/solr.

## Choose and prepare the contribution base

**Preferred: actual PR 2452 head.** Refresh the live PR head/discussion and fetch it. If it is still H, create a clean branch at H and cherry-pick only F0, or apply I0..F0 as a scoped commit. This should leave only the UBI context repair and its regression test relative to H. If the author moved the head, examine the actual change first, verify the race still exists, and record the new pinned head before porting. Do not overwrite an author's fix or silently use stale H. Inspect the full UBI feature already present there, including its default component registration and existing fixtures; adapt the test registration if needed so both overlapped requests use the same intended component instance and no duplicate registration masks behavior.

**Allowed fallback: UBI feature on pinned main.** Use this only when a concrete API/build compatibility issue makes the old PR base unsuitable; age alone is not a reason. Pin current upstream main M and the PR merge-base, replay the full intended UBI feature delta onto M on a new fork branch, and commit it as an unfixed baseline U. Keep required compatibility repairs separate and documented, and make the same environment/compatibility changes available to both sides of the comparison. Preserve the shared-context defect in U; then apply the narrow repair/test as F. Do not replace the feature with just the two-file scaffold. Do not import the listener hook to make the test compile. Separate main transport, feature compatibility and the race repair so an author can review/take the latter independently. Record conflicts and resolutions. If finishing the draft would require broader feature design, report that boundary instead of silently expanding this race fix into completion of UBI.

Use the decision ladder and document the chosen path, reason and exact revision table in an add-only report. A main-based result proves that transported feature at U/F, not the unchanged contributor head H. Offer wording and Git commands must reflect that distinction.

## Invariant and test requirements

The real draft shares one StreamContext per UBIComponent. recordQuery puts a request's ubi-query into that object; UBIQueryStream retains its reference and reads the key later. A pauses after assigning the context; B replaces the value and completes; A can then record B while returning A's own query_id and results. A concurrent map alone does not solve the single shared payload slot.

The proposed fix gives every recording its own StreamContext/maps, copies the read-only initialization entries, and preserves the container-owned SolrClientCache and core ownership. Review the actual implementation before accepting it. It does not need a production test hook, a lock around recording I/O, asynchronous ingestion, or a listener migration.

The existing five-test candidate uses the real UBIComponent's protected getTuple seam only for a bounded latch pause, then delegates to actual UBIQueryStream/LoggingStream execution. It validates the real JSONL output against successful search responses: concurrent local, concurrent forced-distributed, sequential local/distributed, generated identity, and disabled recording. It reuses cloud-minimal and ConfigRequest. Its distributed case sets shortCircuit=false and requires shards.info on a single shard; it does not claim multi-shard merging coverage. Adapt to the actual target's APIs and reuse its existing UBI fixtures/helpers when preferable, without weakening the identity/user_query/doc_ids checks, replacing the real recorder with a fake, or using sleeps. Ensure each side uses the same final test file and that both queries hit one component instance.

## Publish an exact gate, then run the matched pair

Commit and normal-push the new fork branch in this session and independently verify its remote head. Preserve both old fork branches. Author/committer is Nick Shanin, no attribution trailer, no PR number in commit messages. Post an add-only `gates/pr-2452-ubi-context-round-1.md` naming the selected unfixed baseline B, final candidate F, branch, exact test file/blob, JDK/runner/log paths, tests, seeds, formatting and checks below. Perform detached preflight before the gate claim under WORKFLOW. No Windows execution is requested.

Required Linux legs, with separate fresh logs, exit codes, test outputs and XML copies:

1. **Fail before:** B is H (or the refreshed actual PR head) on the preferred path, or U on the main fallback. Overlay only the exact final regression test and any explicitly recorded shared test-harness prerequisites. Do not overlay the fixed UBIComponent. Run `:solr:core:test --tests org.apache.solr.handler.component.UBIRecordingContextTest -Ptests.seed=11994C01` (or the documented final class name if integrated into an existing suite). Expected candidate shape is five executed tests: the three controls pass; both overlap cases fail at the recorded identity comparison, expected `[query-B, query-A]`, actual `[query-B, query-B]`, after both successful response identities/results were verified. Capture the wrong JSONL records and assertion stacks. Compile errors, skipped cases, timeout, absent/malformed log output, wrong handler, or a different failure are not proof of the race. If the baseline passes, investigate and report non-reproduction rather than claiming a regression was demonstrated.
2. **Pass after:** full regression suite at F, seeds `11994C01` and `11994C02`, zero skips/failures/errors. The entire final suite must execute, with actual counts recorded.
3. **Real UBI neighbors:** at the same F, run the actual PR's `org.apache.solr.handler.component.UBIComponentTest` and `org.apache.solr.handler.LoggingStreamTest`; verify their paths/names first. Also run TestSolrConfigHandler and TestSubQueryTransformerDistrib, seed `11994C01`, after confirming presence on the selected tree. Investigate any feature-baseline failure and distinguish it from a repair regression. Do not omit an existing UBI suite merely because the old scaffold lacked it. The retired job's listener suites are not requirements for this UBI-only contribution, and must not be transplanted to satisfy the old job.
4. **Formatting/checks:** clean tidy at the exact offered head, clean diff check, root `check -x test`. Unset NODE_ENV for the root check if required by the established host setup and report the actual execution condition. Use the selected tree's wrapper and supported serialized runner/toolchain; do not substitute a different tree's build files merely to obtain green.
5. Verify the final remote head and clean positive worktree; review the final baseline-to-fix and author-head-to-candidate diffs. State exactly what the author would take, especially on the main fallback.

Formatting, compilation and bounded test/setup repair are authorized. Preserve and investigate the first executed failure before any retry. If the head or final test changes, push/verify it and add a superseding exact-head gate; obtain fresh required proof using the final test on both sides. No stale F0 or listener-example result may certify the new head. No weakening of the intended negative criterion without an evidence-backed written reassessment.

## Deliverables and public boundary

- New pushed fork branch with the verified final SHA and clean scoped diff.
- Exact gate record and an add-only `reports/pr-2452-ubi-context-rework-and-gate-round-1.md` (or named superseding round) with chosen base, source paths/lines, conflicts/repairs, all proof results, toolchain/commands, log/XML paths, limitations and ancestry.
- New gate-owned `receipts/PR-2452-ubi-context.md`. Do not overwrite either PR 5238 receipt or edit the retired job/claim/report.
- A local or add-only board comment draft offering the narrow fix to **PR 2452**, with pinned diff and verified take-in commands. State that this addresses recording-context isolation, not readiness of the full draft. On the preferred path, verify ancestry against the refreshed live author head before suggesting a fast-forward merge. On the main fallback, explain the transport and provide a reviewed way to take just the repair rather than presenting it as a simple fast-forward of the existing PR.

Keep the status Implemented, Proof Pending until all required legs pass. Return the result and draft for review. This assignment does not authorize a public comment, PR creation/body edit, JIRA write, contributor-branch push, rebase/force-push of an open PR, approval or merge. It does not request a listener migration, background ingestion, multi-shard correctness project, performance claim, redaction work, or general completion of the UBI feature.
