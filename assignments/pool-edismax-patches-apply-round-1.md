# Assignment: eDisMax draft patches P1 to P6, application round 1

Capability tags: [implementation, gate]. Intended host: either Linux host (vm1 or vm2); Windows never claims build work. Posted by the main side, 2026-10-15.

Claim first: before starting, file a claim under `claims/` naming this assignment (for example `claims/pool-edismax-patches-apply-round-1.md`), per WORKFLOW.md. Heartbeat and staleness follow the pool rules. The six patches are independent per branch; a claimant may take all six or claim per patch, recording which patches its claim covers.

## Grounding

Read before claiming: `reports/edismax-final-review-round-1.md`, sections 5 and 8 in full, and `reports/main-side-responses-2026-10-15.md` for the disposition of the report's history items O3 to O6.

The final review (Windows lane) settled six branch patches, P1 to P6, as draft text in section 5 of the report. None could be committed or pushed from that lane: its worktree creation wrapper was refused by its own permission classifier (blocker O7 in section 8 of the report). The patch texts were read from the live heads and checked against the cited line numbers, but they were not compiled or run through tidy. This assignment applies them from a Linux host.

Branch tips, pinned in the report and re-verified by ls-remote at posting time (2026-10-15):

- SOLR-3243: `solr-3243-submit` at `1db99c13662d8f827511a5d3a51763d7e1e2ee4c`
- SOLR-3729: `solr-3729-submit` at `0fe7e503945c1a169f76a019392d68bdf4261eb7`
- SOLR-6009: `solr-6009-submit` at `a41bb034a1f47c08a8f8ea6ebbe46e6d443cbb6f`
- SOLR-6320: `solr-6320-submit` at `cb710c963531c4f32166e6e0b28c45e54b164a3f`
- SOLR-12092: `solr-12092-submit` at `ca9573dabd385a38711bd60633973ae46b7a552e`
- SOLR-14913: `solr-14913-submit` at `b80221f46d3ce5889c510b97a97d8748a2ea4c7c`

## Scope

Work the patches in the report's order, P1 to P6. For each patch:

1. Verify the branch tip by ls-remote at claim time against the pinned tip above. If the tip has moved, stop that patch: record the new tip and the skip in the claim file and in `receipts/` for that ticket, and move on to the next patch. Do not apply a patch to a moved tip and do not force anything.
2. Apply the patch text exactly as written in section 5 of the report: the named files, the named anchors, the replacement text verbatim. If a patch does not apply cleanly at its pinned tip, record the failure and skip that patch; do not hand-adapt the text silently.
3. Before committing, re-flow any javadoc or Java comment lines to 100 columns the way google-java-format does (the report's section 5 note: the texts were not run through tidy, and the gate fails the run if tidy changes a file).
4. Commit under the Nick Shanin identity, no trailers, no em dashes in the subject or body. One commit per patch is the expected shape.
5. Push fast-forward to the fork branch. Never force. These are the owner's own branches; pushes carry standing authorization.
6. Post the gate job named under that patch in section 5 as a file under `gates/`, carrying the branch, the new head, the module, the focused test classes, the seed convention, the proof shape from section 5, and a step checklist, per WORKFLOW.md. Running the gate follows the normal claim rules; the gate's own host finishes it and writes the receipt.

Per patch, from section 5:

- **P1, SOLR-3243** (changelog title replacement; new test `testNegatedUnfieldedOpenRangeExcludesEveryDocument` in `TestExtendedDismaxParser`). Gate job: changelog parse; tidy; Error Prone compile; focused `TestExtendedDismaxParser` (expect 43 of 43); proof leg with the parser reverted to base, expected failure is the new test with `numFound` above 0; module check.
- **P2, SOLR-3729** (comments only: the `isStandaloneMatchAll` javadoc paragraph and the three-line test comment). Gate job: changelog parse; tidy; Error Prone compile; focused `TestExtendedDismaxParser` (expect 41 of 41); module check. No proof leg.
- **P3, SOLR-6009** (changelog folded title only). Gate job: changelog parse; focused `TestExtendedDismaxParser` (expect 41 of 41); module check, per the title-only handling named in section 5.
- **P4, SOLR-6320** (test file comments only: two javadoc replacements and the deletion of the two-line TODO comment). Gate job: tidy; Error Prone compile; focused `TestExtendedDismaxParser` (expect 44 of 44); module check. No proof leg. The gate also confirms the test count is unchanged.
- **P5, SOLR-12092** (changelog folded title; one sentence added to `solr/solr-ref-guide/modules/query-guide/pages/edismax-query-parser.adoc`). This is a full gate, not docs-only: the `.adoc` change does not qualify as docs-only under the 2026-10-12 rule. Gate job: changelog parse; tidy; Error Prone compile; focused `TestManagedStopFilterFactory` (5 of 5) and `TestExtendedDismaxParser` (39 of 39) plus the other seven classes in the receipt's focused run; module check. Two record steps ride with P5:
  - History item O3 is disposed in `reports/main-side-responses-2026-10-15.md`, narrowed to trailer removal only (O4 to O6 are declined; no history work on SOLR-6009, SOLR-6320, or SOLR-7120 under this assignment). As part of P5's application, remove the `Co-Authored-By` trailers from the commits on `solr-12092-submit` if they are present. At posting time they are present on `cde23f0a9d7`, `b989934d4ea`, and `3a54f4f3fdf` (verified by reading the branch log); re-check at claim time and record what was removed.
  - Remove the HTML review flag comment planted in `pr-drafts/edismax/SOLR-12092.md` (the `<!-- REVIEW FLAG, remove before posting: ... -->` comment on the flagged sentence), and move the draft's Proof and Changelog links to the new head, per the last bullet under P5 in section 5.
- **P6, SOLR-14913** (changelog title replacement; new assertion appended in `testAliasingWithNonSchemaField`, arguments one per line as section 5 shows). Gate job: changelog parse; tidy; Error Prone compile; focused `TestExtendedDismaxParser` (expect 40 of 40 at the new head); proof leg with the parser reverted to base, expected failure is the new assertion; module check.

Build rules for every gate job posted here: take the blocking flock on the test queue; never run two Gradle builds at once. Use `~/workspace/tools/solr-gradle.sh` on vm1, or the claiming host's own wrapper per its host file. Copy `gradle.properties` into every new worktree before building. Test counts stand only on the gate log and fresh JUnit XML.

## Out of scope

- Owner items O1 (SOLR-3729, broad vs narrow match-all set) and O2 (SOLR-6320 finding 1) are not part of this assignment. Both are posed, or poseable, as Choices in their drafts and stay with the drafts. Do not settle them here and do not change code for them.
- SOLR-3962's reconcile follows only after SOLR-3729 lands. It is not part of this assignment; P2 does not start it.
- No PR openings, no PR body edits beyond what P5 names for the draft on this branch, no Jira activity, no comments.

## Stop conditions

- Per patch: tip moved from the pinned value, or patch text does not apply cleanly at the pinned tip. Record the reason, skip that patch, continue with the next.
- Per gate job: a gate that executes tests and fails is recorded in its gate file and is not relaunched by another host on sight (WORKFLOW.md failure rule). The failure is reported, not retried, under this assignment.
- Green path per patch: patch applied at the pinned tip, committed clean (identity, no trailers, no em dashes, tidy-clean tree), pushed fast-forward, gate job posted under `gates/`, and the receipt for that ticket updated to name the new head as awaiting its gate.

## On completion

Mark the claim DONE, naming the outcome per patch: the new head and the gate job file posted, or the recorded skip and its reason. Append the per-patch outcomes to the takeover log if the claim file does not already carry them where the next pass reads them.
