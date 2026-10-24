# Assignment: PR 5204 picocli review, narrow implementation and Linux BATS proof

Status: OPEN

- Posted: 2026-10-24, at Nick Shanin's explicit request in the Windows review chat: "submit the gate for this review (and/or implementation) as an assignment to vm4."
- Intended host: vm4, explicitly selected by Nick for this assignment. Other hosts must not duplicate it; a later handback can release it.
- Capability tags: initial phase `review, implementation, records, draft`; executable proof phase `gate, bats` on Linux, after the BATS prerequisite below.
- Authority: reproduce and settle the review finding; implement the small correction on Nick's fork if confirmed; push and prove that exact head; produce a review-comment draft. No public PR/JIRA comment, PR creation, foreign-branch push, upstream write, or force push is authorized.
- Target: https://github.com/apache/solr/pull/5204, SOLR-18566, author jaykay12 / Jalaz Kumar.
- Reviewed PR head: `7eb288a6f5777770d4472fc2c550b66e0d1cec12`.
- PR base and merge base: `79b23df58daac14b92cc35f23b8b81f72098b4ba`.
- Foreign branch: `jaykay12/solr:SOLR-18566-picocli-bats-fixed`.
- Proposed Nick fork branch: `solr-18566-pr5204-cli-review`. Confirm absent before creating; if present, inspect ownership and reuse only if it belongs to this assignment.
- Board snapshot used to prepare this assignment: `1ea03b601b9a5560086d487a2e622b4a137ed08d`. No SOLR-18566 receipt, assignment, gate, or live claim was found there. The only ticket mention was a historical JIRA scan entry.

## Bootstrap, capability prerequisite and ownership

Read the live OPEN-WORK.md, WORKFLOW.md, START-HERE.md, UI-PLAYBOOK.md, pool/decision-map.md, material/decision-ladder.md, comment-formula.md, pool/owner-complaints-2026-10-19.md, hosts/vm4.md and hosts/pool-linux.md. Observe add-only claims, reports and gate jobs; do not hand-edit indexes. Take this explicit owner assignment rather than treating its age or the general pool intake defaults as a reason to discard it.

At the preparation snapshot vm4 inherits Linux implementation/gate capabilities but its own host record does not list `bats`. Claim the initial review/setup/implementation slice with those existing tags. Establish the BATS prerequisites on vm4 using the repository's downloadBats task and the normal distribution/toolchain setup, verifying Bash, JDK, Python, download access, and ability to start/stop local Solr processes. Reversible dependency setup is within this assignment. Record actual versions and the verified `bats` capability in vm4's own host file before claiming any executable BATS proof phase. Do not silently assume the missing capability or claim that BATS tests ran when only setup ran. If a dependency cannot be provided, publish a specific environment report and hand back the execution phase with the unmet prerequisite; do not report green.

Use the host build lock and resource guards in hosts/pool-linux.md. One Gradle build per host; run no parallel script suites sharing a distribution or Solr home. Keep a heartbeat in vm4's own host file while active. Claims record phase, host, start UTC, input SHAs, decision-ladder rung, raw log/report paths, and named deliverables. Check the live board for competing claims and any existing receipt before each phase. First pushed claim wins.

Read the live PR, all inline review comments, and SOLR-18566. The reviewed SHA is a fixed input, not a floating branch. If upstream moved, inspect the delta and record how it affects this assignment before choosing any replacement proof input. Never silently gate a new head or imply old evidence covers it. Keep synthetic proof work separate from the clean final worktree.

## Review finding and evidence

The new helper at [bats_helper.bash lines 121-126](https://github.com/apache/solr/blob/7eb288a6f5777770d4472fc2c550b66e0d1cec12/solr/packaging/test/bats_helper.bash#L121-L126) tests `[ -n "${SOLR_PICOCLI:-}" ]`. Literal `false` therefore chooses the picocli expected text. In contrast, [bin/solr lines 830-835](https://github.com/apache/solr/blob/7eb288a6f5777770d4472fc2c550b66e0d1cec12/solr/bin/solr#L830-L835) delegates start/stop help to Java only for the literal value `true`; otherwise it prints the legacy shell help. SolrCLI.main uses EnvUtils.getPropertyAsBool("solr.picocli", false).

configureBatsTask sets SOLR_PICOCLI only when usePicocli is true (build.gradle lines 341-343). Thus integrationTests inherits an explicitly exported false value. The changed helper then expects picocli output from the legacy parser. Example: test_help.bats expects lowercase `usage: bin/solr start` instead of the shell's legacy `Usage: solr start`; test_post.bats selects the wrong missing-option message. test_help.bats line 56 also skips legacy restart help for an explicit false because it tests presence rather than true.

This was a P2 source-derived harness finding. No production correctness or security defect was identified, and no tests ran during the Windows review. Do not elevate source reasoning to reproduced evidence until the probe executes.

The five existing inline comments are Eric Pugh's, in two review summaries:

- https://github.com/apache/solr/pull/5204#discussion_r4358544790 (shorten AuthTool comment).
- https://github.com/apache/solr/pull/5204#discussion_r4358551047 (TODO wording).
- https://github.com/apache/solr/pull/5204#discussion_r4358568411 (why retain the indirectly supplied include-file option).
- https://github.com/apache/solr/pull/5204#discussion_r4358578833 (retaining both parser test paths).
- https://github.com/apache/solr/pull/5204#discussion_r4358591161 (removing the option from the guide).

Do not repeat those as new discoveries. SOLR_INCLUDE is an existing environment input, read/sourced by the shell and forwarded as --solr-include-file to Java; it is not a new JVM system-property API introduced by this PR. Hiding the Java option leaves its parser behavior intact. Do not redesign this interface to settle an open maintainer discussion.

PR #5190 changes the default and documents false as the fallback. It was still open at review time. This assignment targets #5204's actual head, not an assumed merge of #5190. The unchanged default CI sequence tests unset/true, which does not cover the explicit false case.

## Phase R: discriminating reproduction and review settlement

Fetch apache/solr refs/pull/5204/head into an isolated worktree pinned to the reviewed SHA. Do not use a dirty shared source checkout, a stale ticket branch, or branch-work.ps1 new (which assumes an existing submit branch).

After capability setup and a separate proof claim, run the targeted help suite on the unmodified reviewed head with SOLR_PICOCLI=false in the actual BATS child environment. Expected: assertion failures attributable to wrong legacy/picocli expected wording, not compilation, distribution startup, missing tooling, credentials, or port errors. Capture the observed output and test names. The restart-help case being skipped under false is a separate coverage symptom.

Task shape on Linux: `env SOLR_PICOCLI=false ./gradlew :solr:packaging:integrationTests --tests test_help.bats --rerun-tasks`. Add test_post.bats to a separately recorded probe if needed to confirm the second call site. Use the host's supported runner and toolchain arguments. This command is not permission to bypass the local build lock or guards.

The original PR head may contain unrelated BATS failures; classify them separately. If the expected mismatch does not reproduce, investigate environment/selection and correct or retract the review claim with the actual evidence. Do not patch merely to validate the original opinion. Decisions can normally settle at ladder rung 1 (code/history) or 2 (probe).

## Phase I: minimal implementation, only after confirmation

Implement on Nick's fork branch rooted at the exact reviewed PR head, preserving the contributor's commits. Use Nick Shanin as the new commit author, no attribution trailers, and a normal non-forced fork push.

Expected delta:

- solr/packaging/test/bats_helper.bash: select picocli only when the value is explicitly true, consistent with the shell launcher.
- solr/packaging/test/test_help.bats: apply the same predicate to the picocli-only restart skip, so false retains legacy coverage.
- Optional bounded hygiene: remove trailing whitespace/blank EOF line in changelog/unreleased/SOLR-18566-picocli-bats-fixed.yml and refresh the stale setup comment in test_basic_auth.bats, which still describes directly passing the include-file option. Preserve the contributor's changelog attribution and content; no second changelog fragment is needed for this test-only suggestion.

Do not change Java production behavior, default parser selection, auth permissions, include-file API design, shared workflow timing, unrelated existing test defects, or the contributor's live PR body. Keep additional proposed improvements as separately named follow-ups unless needed to complete this proof. A relevant existing test that fails for an unrelated reason needs a recorded diagnosis and decision before changes or reruns.

Add the smallest lasting regression protection practical for explicit false selection, using the existing BATS harness. Do not let tests redefine the variable such that every requested false-mode run silently becomes true or unset. Document how the delivered check exercises the false branch rather than only normal unset/true coverage.

Review the incremental diff, commit, push to nick-boss-tech/solr and independently read back the remote full SHA. Report the expected file list, author, actual incremental compare URL from reviewed head to new head, and any proof limitations. Keep state Implemented, Proof Pending until the final phase passes.

Deliver `reports/pr-5204-cli-review-implementation-vm4-round-1.md`. Then add an executable immutable job at `gates/SOLR-18566-pr5204-cli-review-<actual-head12>.md` with the actual branch and full pushed SHA. Do not create a runnable final-head job containing a placeholder. A separate claim covers the final gate, which vm4 may run under its serialized lock. Never move an already claimed gate's head; post a superseding job for a new SHA.

## Phase G: final-head Linux gate requirements

Use fresh isolated test output for every leg and prevent Gradle up-to-date results from standing in for execution. BatsTask does not explicitly declare parser environment as a custom @Input; force the relevant task execution, for example with --rerun-tasks, when changing mode. Preserve each leg's JUnit report and raw log before the next task clears its output directory. Record actual child environment and effective parser, commands, UTC start/end, exit code, test/pass/fail/skip counts, report timestamps, worktree HEAD, host, JDK/Python/BATS versions and log paths. BATS has no JUnit randomized-tests seed; record seed as not applicable rather than inventing one.

Required proof:

1. **Original-head negative control:** the unmodified reviewed-head false-mode help failure from phase R. Retain the exact observed assertions. A setup/build failure does not count.
2. **Patched false-mode focused leg:** final pushed SHA, SOLR_PICOCLI=false, integrationTests with test_help.bats and test_post.bats. Expected pass, with restart help executing rather than being skipped for picocli. Include an explicit executed check of the false-selector regression protection.
3. **Default legacy full suite:** same final SHA, SOLR_PICOCLI unset in the process and no include-file override enabling it, `:solr:packaging:integrationTests`. Expected all selected non-nightly tests pass; state all skips and why.
4. **Picocli full suite:** same final SHA, `:solr:packaging:integrationTestsPicocli`, which sets true. Expected all selected non-nightly tests pass, with the intentionally unimplemented restart-help skip accounted for. Do not silently return to the old curated suite or drop failing files.
5. **Formatting and module checks:** detached tidy preflight, `tidy`, `:solr:packaging:check -x test`, and `git diff --check` of the incremental delta. No Java compile proof is requested for this shell-test-only delta. If implementation scope requires Java changes, record why and supersede the scope/job before adding core compile or test proof.

The full-suite task names are module-qualified to avoid accidentally selecting unrelated tasks. Confirm those names against the pinned build before execution. Do not run nightly-tagged tests, Docker builds, the independent ui/ build, or a repository-wide check as part of this assignment. If an existing selected failure blocks a full-suite result, diagnose it and report the limitation rather than call the gate green or rerun unchanged until it passes.

Formatting fixes change the gate input: commit and push any required repair, publish a superseding pinned job, and obtain final proof at that SHA. Proof at an earlier tree is not proof at the delivered head. The original-head expected failure remains valid for its original input and need not be needlessly repeated.

Deliver `reports/pr-5204-cli-review-gate-vm4-round-1.md` and a gating-host-owned `receipts/SOLR-18566.md` only after checking for a current owner. Never overwrite another host's receipt; use a phase-specific receipt if necessary. A failed or incomplete leg gets a failure/incomplete report, not a green receipt. Include the incremental compare URL, original and final full SHAs, final tree status and remote read-back.

## Review-comment draft and closeout

After proof, reread the live PR and discussion and add `reports/pr-5204-review-comment-draft-vm4-round-1.md` with ready-to-review GitHub Markdown following comment-formula.md: the AI disclosure header, @jaykay12, the observed false-mode failure and narrow fix, actual results at exact SHAs, and any limits. This mechanical diff may be linked in the footer under the formula's small-diff exception. Do not claim the five existing review threads were answered or resolved. No internal gate/receipt/ladder language, no em/en dashes, no checklist. Keep requested clarification about include-file visibility separate from the demonstrated selector defect.

The body typo (test_stream listed twice instead of test_help), the old workflow "curated suite" comment, and remaining include-file discussion may be mentioned as minor notes; no live body edit, comment, review submission, JIRA write, PR creation, or foreign push is authorized. The final report states what landed on Nick's fork, what ran, what remains open, and the concrete draft location.

This assignment concerns #5204 only. It carries no authority to read, transfer, publish, run, or resume the separately held #5214 security packet.
