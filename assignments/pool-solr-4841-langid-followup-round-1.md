# Assignment: build the SOLR-4841 follow-up test (language detection through a custom identifier), round 1

Status: SATISFIED (review rounds 2 and 3 done by vm5 2026-10-18T19:45:04ZZ, reports/SOLR-4841-followup-round-1.md; the mutation run in that report shows `expected:<[sv]> but was:<[]>`). Remaining steps are public actions for Nick: file the ticket and approve the PR. Claim: claims/pool-vm5-solr-4841-review-rounds-2-3.md, 2026-10-18T19:12:15Z). Test built and gated by vm4 at bf1b01d0dc; the review rounds are the open part.

Capability tags: [implementation, review]. Intended host: Windows (review agent). Claim under claims/ before starting, per WORKFLOW.md. The gate itself runs on a Linux host through the gate job this assignment directs you to file; no builds run on Windows.

## Owner directive

Nick, 2026-10-14: the merged PR for SOLR-4841 promised this follow-up and it was never executed. Build it now. The review agent builds the test and runs the review rounds on it; gating and further review by other agents proceed in parallel with the submission work. Run this assignment to SATISFIED: a findings-only round does not discharge it.

## Background

- The SOLR-4841 change merged on 2026-10-10 (merge commit `a99ff066f4` on main, "SOLR-4841: DetectedLanguage constructor should be public"). It made the `DetectedLanguage` constructor public so a custom `LanguageIdentifierUpdateProcessor` living outside the processor package can build its own detection results.
- The merged PR's description promises, verbatim: "A follow-up submission is planned for a test that runs language detection through a custom identifier." The promise is recorded in `reports/merged-pr-followup-promises-round-1.md` (round 2 section), state OPEN.
- What merged instead is `solr/modules/langid/src/test/org/apache/solr/update/processor/custom/CustomLanguageIdentifierTest.java`. It defines a `FixedLanguageIdentifier` subclass whose `detectLanguage` returns a fixed result, but its only test method constructs a `DetectedLanguage` and checks the two getters. Nothing calls `detectLanguage` and no document is processed; the class comment says so itself ("compiling it is the main check"). The merged PR's own Limits state: "It does not run language detection."
- The SOLR-4841 ticket is still open. No fork branch carries a detection-running test. This assignment builds it.

## The test to build

Design a test that runs language detection end to end through a custom identifier, in the `custom` package next to the merged test (module `:solr:modules:langid`):

1. Start from the merged change: read `LanguageIdentifierUpdateProcessor` (the abstract base), `DetectedLanguage`, `LangIdParams`, and the merged `CustomLanguageIdentifierTest`. The custom identifier under test is a `LanguageIdentifierUpdateProcessor` subclass defined outside the processor package, as the merged test's `FixedLanguageIdentifier` is.
2. The run must go through the processing path, not around it: a document carrying text in the configured input fields goes through the processor (`processAdd` on the custom processor, or a chain containing it), the base class machinery calls the subclass's `detectLanguage`, and the detected language is written to the document's language field. Assert on that field's value on the processed document. Calling `detectLanguage` directly and asserting on its return value does not satisfy this assignment: that is the merged test's gap repeated.
3. The custom identifier's detection should be content-dependent (for example, it inspects the text it is given and returns different languages for different inputs), so the assertions discriminate: at least two inputs producing two different asserted language field values. A constant-result identifier with an assertion that would pass for any detected value is a review finding.
4. Follow the standing test rules: the new test class extends `SolrTestCase` (as the merged test does) or `SolrCloudTestCase`; never `SolrTestCaseJ4`, and do not copy the J4-based factory test-case harness pattern. If the design seems to need that harness, raise it as a review point instead of extending J4. If Mockito is used, `assumeWorkingMockito` follows the base-class rule in the hardening record.
5. Keep the change test-only: no production edits. If the end-to-end run exposes a real production defect, stop and record it in the deliverable rather than fixing production inside this branch; scope questions go in the deliverable for the main side.

## Pre-fix check (part of the design, recorded honestly)

The test must be shown to depend on the SOLR-4841 change:

- Against the parent of the merged commit `a99ff066f4` on main, `DetectedLanguage`'s constructor is not public, so a test in the `custom` package that builds detection results does not compile there. That does-not-compile outcome, with the compiler error quoted, is an acceptable proof leg and the expected one.
- If the test as designed also has a leg that compiles against the base, that leg must fail there for the premise reason (the detection path cannot run), checked by failure content, not count alone.
- Record whichever legs apply in the gate job file (below) so the gating host runs them; do not claim a leg that was not run.

## Branch and commits

- New branch `solr-4841-langid-followup-submit` off current main (fetch first; record the base SHA in the deliverable). Commit as the work goes, identity Nick Shanin <nick.boss.us@gmail.com>, no trailers, no "Claude" anywhere, no em dashes.
- Push the branch to the fork (`fork` remote, never `origin`) when the test is written, and again after each review round that changes it. If you patch it, you push it.

## Review rounds (patch loop, on your own work)

- Round 1 is your self-review of the design and assertions against the points above, recorded in the deliverable.
- At least two further review rounds follow, each reviewing the current branch head: assertions actually discriminate (point 3), the run truly goes through `processAdd` (point 2), the pre-fix legs are honestly stated, and the test follows the base-class and style rules. Apply the patches or wording changes each round's findings call for and re-review the patched head.
- Rounds continue until a SATISFIED verdict on the test's design and assertions comes back. Record each round's verdict and what it changed in the deliverable.

## Gate job (file it; a Linux host runs it)

File `gates/SOLR-4841-langid-followup.md` for a Linux host (any capable Linux host may claim; vm2 coordinates the gate backlog), naming:

- Branch `solr-4841-langid-followup-submit` and its head at filing time, base = current main at branch creation, module `:solr:modules:langid`.
- Changelog decision: NO new changelog fragment. This is a test-only follow-up for an existing ticket whose production change already shipped with its own fragment in the merged PR; test-only changes are changelog-exempt (dev-docs/changelog.adoc section 4). The gate's changelog step verifies the branch adds no fragment and has nothing to parse.
- Steps, in the standard shape: tidy on the final tree (restore `gradle/libs.versions.toml` afterward; a gate that passes on an untidied tree is invalid), Error Prone compile (`-Pvalidation.errorprone=true`), the pre-fix proof legs recorded above (does-not-compile against the parent of `a99ff066f4`, quoted error; plus any compiling leg, by failure content), the focused test class runs at the head counted from fresh JUnit XML, and the module check (`:solr:modules:langid:check -x test`). Use `~/workspace/tools/solr-gradle.sh` on vm1, never `./gradlew`; one Gradle build at a time per host under the test-queue flock.
- On green: the gating host writes `receipts/SOLR-4841-followup.md` per pool protocol, marks the gate job and its claim DONE, and notes the branch is ready to join an openings slate.

## What this assignment does not authorize

- No PR is opened by this assignment. When the branch is gated green and the review rounds are SATISFIED, it joins an openings slate for Nick's approval like the other prepared branches, and the main side opens it on his word.
- No comments are posted anywhere by the review side. The eventual PR description follows `pr-formula.md` (bold one-line summary opening every section, Proof only from the gate record, plain language, no internal process vocabulary, no "we", no em dashes); drafting it is welcome in the deliverable, posting is not part of this assignment.

## Deliverable

`reports/solr-4841-langid-followup-round-1.md`: the design in brief (how the document flows through the custom identifier and what is asserted), the branch name and base SHA, the head after each round, each round's verdict and what it changed, the pre-fix legs as recorded for the gate job, the gate job file name, and the final state (SATISFIED or the named surviving hold).

## Amendment (2026-10-14): the follow-up opens under a new ticket of its own

Jan Høydahl directed on the SOLR-4841 ticket that this follow-up get a NEW ticket of its own (reported by Nick, 2026-10-14). The build proceeds exactly as assigned above: branch `solr-4841-langid-followup-submit`, the gate job for a Linux host, and the self-review rounds to SATISFIED are unchanged, and the work is still described against SOLR-4841 in the deliverable while it is being built. What changes is the packaging target:

- The eventual PR opens under the NEW ticket's number, once Nick files that ticket. No opening happens before the ticket exists.
- At packaging time, the draft's title, its citations, and any changelog references are re-pointed from SOLR-4841 to the new ticket number. (The gate job's changelog decision already stands: the change is test-only and adds no fragment, so there is no fragment to re-point unless packaging decides otherwise under the new ticket.)
- The "What this assignment does not authorize" section stands in full: no PR is opened by this assignment, and the opening still needs Nick's approval on an openings slate, now under the new ticket.
