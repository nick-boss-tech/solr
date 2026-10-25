# Gate: SOLR-3479 / PR 5126 implicit highlight-query production fix, round 1

Status: OPEN
Tags: gate, implementation, review, draft

- Intended host: any capable Linux gate VM, one build at a time under its host lock. Windows must not execute Gradle.
- Authority: Nick explicitly requested a production fix, exact-head Linux proof, and a plain-language reply following comment-formula.md. A direct diff against David's PR is explicitly requested. No public comment, JIRA write, PR creation, or foreign-branch push is authorized.
- Ticket/PR: SOLR-3479, https://github.com/apache/solr/pull/5126 (David Smiley).
- Original PR source head: `74b09fb443f060caac3f8ec9701909cec276cfb4`.
- Fix fork branch: `nick-boss-tech/solr:solr-3479-submit`.
- Fix head to gate: `23c159d06bd86180e0bbd4b474517b60b693f926`.
- Module: `:solr:core`.
- Focused class: `org.apache.solr.response.transform.TestHighlightAugmenterFactory`.
- Sensitivity method: `testImplicitQueryUsesHighlightQuery`.
- Additional existing coverage: `org.apache.solr.highlight.TestUnifiedSolrHighlighter`.
- Seed for all test legs: `34795126`.
- Receipt deliverable: `receipts/SOLR-3479-pr5126-highlight-query-round-1.md` (confirmed absent when preparing the job; recheck live ownership before claiming).
- Report deliverable: `reports/pr-5126-highlight-query-fix-gate-round-1.md`.
- Result-based comment draft deliverable: `reports/pr-5126-production-fix-reply-round-1.md`.

## Bootstrap and claim

Read the live tip, OPEN-WORK.md, WORKFLOW.md, START-HERE.md, UI-PLAYBOOK.md, hosts/pool-linux.md and your own host file, pool/decision-map.md, material/decision-ladder.md, pool/owner-complaints-2026-10-19.md. Read comment-formula.md before drafting. Read `claims/pr-5126-highlight-query-fix-windows-round-1.md` and `reports/pr-5126-highlight-query-fix-implementation-round-1.md`. Check claims, receipts and deliverables for this exact fix phase. Q's prior claim is completed reproduction work, not an active production-fix gate. Optional RTG slice R is separate and must not delay this job.

First pushed gate claim wins; use a new add-only claim file. Detached formatting preflight precedes the gate claim per WORKFLOW.md. The claimant records source/fix SHAs, host, JAVA_HOME/version, board snapshot, runner and UTC start time. Take the host build lock and disk/inode guard. Read current runner documentation. Keep phases serialized behind the host lock and heartbeat in the host file, not the immutable claim. Do not edit this gate or another worker's records; completion comes from the receipt/report.

Read the live PR head/discussion and fork head before running. The pinned original source is intentional. If David's PR advanced, inspect overlap and record both heads rather than silently substituting a new baseline. If the fork tip differs from the pinned fix head, inspect its owner/changes and post a superseding exact-head job before testing it. Never move a claimed job's head by editing the original gate file.

## Scope and intended result

The prior Linux Q matrix at `022eb6d7811cd876afde3b84115696cb1c6af50e` demonstrated cats highlighted only in the implicit+bq+hl.weightMatches=false transformer rows. Default true did not reproduce for that fixture. Q's tests assert the difference and are not fixed-behavior regression tests. Do not reuse their green result as proof for this new branch.

This branch starts directly from David's PR head and changes only HighlightAugmenterFactory.java and TestHighlightAugmenterFactory.java. It reuses the main request's QParser for implicit highlighting when the result context belongs to that request/query, takes getHighlightQuery(), and preserves the old context-query fallback when no matching parser is available. Explicit hl.q and transformer-local q remain overrides. No RTG implementation, /export alias fix, reference-guide change, or extra changelog fragment is included; the original contributor's fragment stays unchanged.

The new tests assert correct user-visible behavior: boost-only cats must not become an implicit body snippet, whether the existing component is enabled or disabled; explicit and local fox overrides agree; both weightMatches settings work; eDisMax q.alt and a local main-query parser are honored; deliberate explicit/local cats overrides still highlight cats. The existing committed-document get test stays in the full class as a fallback control. This does not establish support for uncommitted RTG or distributed execution.

## Mandatory discriminating proof

Use clean isolated trees. Keep each leg's raw command line, start/end UTC times, exit code, seed and new JUnit XML. Clear previous results through `:solr:core:cleanTest` before each test leg; do not count stale XML. Check out the exact head for positive proof. Record production and test-overlay hashes separately for the negative source.

1. **Fail before:** detached original PR production at `74b09fb443f060caac3f8ec9701909cec276cfb4`, overlay ONLY the final fix branch's TestHighlightAugmenterFactory.java. Run `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.response.transform.TestHighlightAugmenterFactory.testImplicitQueryUsesHighlightQuery -Dtests.seed=34795126`. Expected: the new assertion fails because doc 2 has a cats body_hl at weightMatches=false. It must compile, execute the target test, and fail for that assertion. A startup/compile failure or unrelated exception is not sensitivity proof. Do not test pre-PR main, which lacks this transformer.
2. **Pass after:** clean exact fix head. Run `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.response.transform.TestHighlightAugmenterFactory -Dtests.seed=34795126`. Every new assertion and the original suite must pass. Publish actual XML test/error/failure/skipped counts. Retain the default-setting qualification from Q; do not imply Q reproduced at defaults.
3. **Neighbor coverage:** same clean fix head. Run `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.highlight.TestUnifiedSolrHighlighter -Dtests.seed=34795126`. Preserve separate log/XML from leg 2.
4. **Format and compile/check:** at that same final clean head, run `tidy`, `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`, and `:solr:core:check -x test` with the supported runner. Record each task/exit. Do not silently drop a check, exclude an unrelated failure, or call a module check a full repository check.

The commands describe task/filter inputs to the serialized pool runner, not permission to start overlapping unguarded Gradle builds. All commands execute on Linux only. A new request that never executes tests follows the documented environment-relaunch rule. A test failure is investigated as a repair, not silently repeated.

If a source/test/format repair is necessary, it is authorized within the two-file scope: review the actual failure, make a narrow commit as Nick Shanin with no attribution trailer, push normally to the same fork branch and read back the remote full SHA. No force push. Add a repair report and a superseding exact-head gate file; rerun the mandatory proof on the final head. Do not issue a green receipt for a head prior to a formatting or code change. Wider production changes require an evidence-based scope decision recorded before the change; do not treat this as authority for RTG or /export work.

## Evidence and receipt

Report original PR SHA, final verified fork SHA, author, incremental file list/diffstat, whether tidy changed files, all leg outcomes, exact commands/seed, runner, JDK/host, raw logs and fresh XML. Publish durable evidence reachable from the board report, not only VM-local absolute paths. Include the expected fail-before assertion and the passing fixed result; distinguish observation from source explanation and failed-versus-skipped proof.

Primary reviewer link is a direct comparison from David's PR branch to Nick's fix branch:

https://github.com/dsmiley/solr/compare/SOLR-3479-highlightDocTransformer...nick-boss-tech:solr-3479-submit

Verify its API before citing: base/merge base must equal the original PR head and its final head must equal the gate receipt. Also include an immutable SHA-to-SHA compare link for the exact tested delta. Keep the separate reproduction branch intact. If David's branch moved, cite the pinned immutable comparison and explain the base difference instead of calling a moving comparison the tested delta.

Only after all required legs pass write the phase receipt, whose final section is a PR-description-style summary of the proposed incremental patch (no new PR is to be opened). The report and receipt must identify the exact final head. A failed leg produces an explicit failure/repair report, not a green receipt.

## Plain-language comment after proof

After green, read the live PR/discussion and comment-formula.md again, then add the named reply draft. It is for Nick's review, not for posting. Open proposed outbound text with `🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*`, address @dsmiley and answer his actual parseHighlightQuery question. File status must say awaiting Nick's review, not assert he has already reviewed the draft.

Lead with measured evidence and the proposed fix. Explain q (which documents match), bq (ranking boost), and hl.q (what to highlight) using the existing fox-title/cats-body fixture in ordinary language. State that the original reproduction required hl.weightMatches=false and did not reproduce at the default true. Explain the helper's hl.q-absent early return and the old context.getQuery fallback; then explain that the patch uses the already-created search parser's highlighting query. Avoid the long Lucene field-remapping explanation unless it is needed to answer a further question.

State what failed before and passed after, using actual counts, exact final commit and named checks. Name the two changed files. Link the direct PR-to-fix diff as the primary evidence; Nick explicitly asked for that link. Additional raw-proof links may follow. Do not imply that the prior diagnostic tests constitute proof of the production fix. If a leg remains incomplete, the draft stays pending and must not say verified or green.

Be generous with useful explanation; brevity is not a reason to leave the same ambiguity. Do not repeat the P2 label as an argument. Address the separate RTG wording question briefly by identifying the PR description's Solution bullet spelling out real-time get, and the JIRA limitation note. State no RTG implementation or runtime test claim.

Close with one question about whether this evidence and narrow fix are sufficient for the query-selection concern. Recommend this limited adjustment; treat uncommitted RTG and the already-tracked #5188 alias issue as separate work. Use no public internal vocabulary such as gate, receipt, ledger or rung; no authored we, em/en dashes or checklist. No public comment, existing-comment edit, JIRA write, review submission, PR opening, or foreign-branch update is authorized. Nick reviews the concrete tested diff and draft first.
