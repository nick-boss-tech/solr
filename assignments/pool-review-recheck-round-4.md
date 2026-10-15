# Pool assignment: review re-check, round 4 (changed passages from the open fixes lane)

Status: DONE

Capability tags: `review` (read-only). Intended host: windows. Staffing: 2, split by slice, read-only subagents. Claim path: `claims/pool-review-recheck-round-4.md` (slice-level claims by slice are fine).

## Purpose

Update-processing convergence round 3 closed NOT CONVERGED, and draft fidelity round 2, the gradlew round 3 and the CombinedQueryComponent round 2 records closed open. The open fixes lane (`reports/review-open-fixes-lane-round-1.md`, pushed at `181818be5a9`) applied their replacements. This round checks only the passages that lane changed, so those records can close. It does not reopen settled items and does not sweep the drafts again.

## Scope and the finding bar

Check each changed passage listed below against its record and against the code or record it cites. Report each as CONFIRMED, or as FINDING with the exact fix.

A finding is raised only when:

- (a) the passage is wrong against the record it replaces or the code or record it cites;
- (b) the passage contradicts another line in the same draft or Limits; or
- (c) a change the record requests is not on the page.

Style, length, link form and label wording are not findings in this round.

The diff to check is the lane's own commit range: `git -C <worktree> diff 19e916d0972 181818be5a9 -- <paths>`.

### Excluded (not checked here)

- SOLR-6973 line 17 (blocking; owner confirmation pending).
- Length items (SOLR-5941, SOLR-6973).
- SOLR-5941 line 41; SOLR-16655 line 25; SOLR-12705 lines 19, 28 and 39 (none changed).
- CombinedQueryComponent Symptom bullet 3 (line 22) and the Fix options passages (lines 62 to 68).

## Slice R4-A: update-processing drafts (`pr-drafts/update-processing/`)

Check these passages.

- **SOLR-12705 line 17:** "Three existing processors change output." Check the three processors named in the same bullet.
- **SOLR-7022 line 24** ("the formatting check") and **line 26**, last sentence (merge-base claim, 2026-10-06, `DirectUpdateHandler2CommitWaitTest` 1 of 1). Check against `reports/update-processing-convergence-round-3.md` and the cited run.
- **SOLR-14718 line 24:** "At head `29c09959791a`, SolrCmdDistributorTest passes 1 of 1 (verified 2026-10-07)." Check the count and head against the record.
- **SOLR-16673 line 23:** the parse clause is removed; "Before the fix, at `d5c19e64ba1b`, the new test fails; the gate record gives PASS only, and the observed failure is not recorded." Check that the receipt or gate record says PASS only and does not record the failure.
- **SOLR-3657 line 25** (merge-base `97d973814336`, branch's `DocumentBuilderTest.java` applied, 17 tests, 2 failures). Also check **bold line 22** ("On base production code, the run without the fix has 2 failures.") for contradiction with line 25 under (b). This is the only bold line checked here.
- **SOLR-5065 line 25** (merge-base `0cc328310f8f`, branch's `DefaultSchemaSuggesterTest.java` added; the class is new on this branch). Confirm the class is absent at the merge-base.
- **SOLR-5941 line 35** (bold, "By reading the base code"), **line 38** (`CommitTracker.java` line 280 at `cabedd1d968`, on the merge-base), and **Limits bold line 45** ("three SolrCloud commit paths"). For line 38, confirm the cited line says what the sentence says. For Limits, confirm the Proof names three SolrCloud classes and that "commit paths" describes them.
- **SOLR-6973 line 7** (bold, "With `overwriteDupes` and a `fields` list, ..."), **line 9** (the new link to `SignatureUpdateProcessorFactory.java` lines 169 to 178 at `cabedd1d968`; confirm the lines hold the loop the sentence describes), **line 23** (gate 2026-10-08, head `fa5b59ba07b4`; "Reading the base code ... base lines 199 to 203" sets `signatureField` and `updateTerm`), **line 25** (the deleted clause; confirm no remaining sentence still claims the core module checks), and **Limits line 37** (processor output only).
- **SOLR-7504 line 25** (production code before the fix at `22b77196e662`, with this branch's test file added; 1 failure in 30), **line 27** ("the formatting check leaves a clean tree"), and **line 38** (combined run of SOLR-7504 at `22b77196e662` with SOLR-12705, 2026-10-09, before the null fix). Check line 38 against the record it cites, and check that no other line in the draft says the combined run was at a different head.

## Slice R4-B: the other drafts and material

- **`pr-drafts/security/SOLR-18368.md` line 1:** "decides under which key the PR opens". Check against the SOLR-18368 entry in `reports/draft-fidelity-confirmation-round-2.md`. Confirm the lead-in asks no question the record has closed.
- **`pr-drafts/search-components/SOLR-9396.md` lines 26 and 28:** both now name `$row.field` and `${row.field}`. Check against `reports/draft-fidelity-confirmation-round-2.md` (SOLR-9396 entry) and `reports/draft-fidelity-searchcomponents-edismax-s10.md` lines 51 and 52, and against the scan at `SubQueryAugmenterFactory.java` line 307 (`\$\{?row\.`) at `a5ab2eda4e6`. Also check Limits bullets 1 and 2 (lines 34 and 35) for contradiction with the new Choice wording under (b). **Answer this question in the report:** is the lane's composed Choice wording accurate to the code, and does the record support it?
- **`material/gradlew-ticket-draft.md` line 5** (source note; "executed 2026-10-11" removed). Check that no other sentence in the source note states a run date the record does not support.
- **`material/gradlew-ticket-draft.md` line 51** (the argument loop "runs once over `-version` without converting it; its `cygpath` call at line 231 is not reached"). Check against the gradlew round 3 record (item 4) and its trace files (`trace-fix.txt` lines 56 to 60; `trace-repro.txt` lines 60 to 64; locate them with Glob if they are not at those paths). Check the cited lines 225, 222 to 242 and 231 at the cited snapshot. Markup (backticks against Jira `{{ }}`) is not a finding.
- **`material/combinedquerycomponent-ticket-draft.md` line 20** (both real-exit sentences labelled "(from code reading, not run)"), **line 48** ("whose question is posed in Fix options": check that Fix options does pose the question), **line 54** (the clause "which is why this was not visible" removed: check the sentence still reads and is not contradicted), and **line 56** (label). Check that no other passage states the real-exit products as run results. **Answer this question in the report:** is a second label on line 20 needed, or a duplicate?

## Method

Read-only. Use Read, Grep, Glob and read-only git only (`git -C <worktree> show`, `cat-file -t`, `diff`, `log`). If a cited object is absent from the local repository, record "not checked: object absent" with the SHA. Do not fetch, build, test, run a gate, run Gradle, or edit any file. Each subagent reports its results as text, with the lines it checked. The coordinator writes the report.

## Deliverable

`reports/review-recheck-round-4.md`, with the verdict first: CONVERGED (no findings across both slices) or NOT CONVERGED (findings listed). Then one line per changed passage: CONFIRMED, FINDING (file, line, the defect in one sentence, and the exact replacement), or NOT CHECKED (with the reason). Include the two answers requested above.

Mark the claim DONE in the same push as the deliverable.

## Limits

No PR, comment, live PR body, branch, receipt or Jira edit. Nothing is posted or filed. The private records stay with the main agent. Commit as Nick Shanin with no trailers and no em dashes. No PR numbers in file names or commit messages. The SOLR-6973 blocking item and the excluded items stay with the main side.
