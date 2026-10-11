# Draft fidelity review: Search components and eDisMax drafts

Assignment: `assignments/pool-draft-fidelity-searchcomponents-edismax.md`. Claim: `claims/pool-draft-fidelity-searchcomponents-edismax.md`. Lead: the windows review agent. The set was sliced by number, in twelve slices. The part reports below hold each draft's exact replacement text.

- `reports/draft-fidelity-searchcomponents-edismax-s1.md` to `-s10.md`: search components, in the slices listed in the claim.
- `reports/draft-fidelity-searchcomponents-edismax-s11.md`: eDisMax SOLR-12092, 14913, 2309, 2988, 3243.
- `reports/draft-fidelity-searchcomponents-edismax-s12.md`: eDisMax SOLR-3729, 3962, 4362, 6009, 6320, 7120.

No draft was edited. Read-only throughout. No build, test, gate or test-queue run. No PR, comment, review, submit-branch, PR-description or Jira write.

## Verdicts

Fifty-nine drafts: twelve are CONSISTENT and forty-seven are DRIFT. Every named head matched the fork's live tip in `ls-remote` at the time of the check.

Search components (48):

| Draft | Verdict | Part report |
|---|---|---|
| SOLR-10424 | DRIFT (2 items) | s1 |
| SOLR-10492 | DRIFT (3 items) | s1 |
| SOLR-10694 | DRIFT (3 items) | s1 |
| SOLR-10844 | DRIFT (3 items) | s1 |
| SOLR-11129 | DRIFT (4 items) | s1 |
| SOLR-11153 | DRIFT (3 items) | s2 |
| SOLR-11310 | DRIFT (2 items) | s2 |
| SOLR-11364 | DRIFT (2 items) | s2 |
| SOLR-11470 | DRIFT (1 item) | s2 |
| SOLR-12044 | DRIFT (1 item) | s2 |
| SOLR-12543 | CONSISTENT | s3 |
| SOLR-12556 | DRIFT (4 items) | s3 |
| SOLR-13245 | DRIFT (2 items) | s3 |
| SOLR-13876 | DRIFT (1 item) | s3 |
| SOLR-14381 | DRIFT (2 items) | s3 |
| SOLR-14451 | DRIFT (2 items) | s4 |
| SOLR-14678 | DRIFT (4 items) | s4 |
| SOLR-14931 | CONSISTENT, held (the round fixes change the head) | s4 |
| SOLR-15018 | CONSISTENT | s4 |
| SOLR-15041 | DRIFT (2 items), held until a blank-line fix and its focused test land | s4 |
| SOLR-15319 | DRIFT (2 items) | s5 |
| SOLR-15331 | DRIFT (1 item) | s5 |
| SOLR-15479 | DRIFT (1 item) | s5 |
| SOLR-15895 | DRIFT (1 item) | s5 |
| SOLR-16444 | CONSISTENT | s5 |
| SOLR-17051 | DRIFT (2 items) | s6 |
| SOLR-17155 | CONSISTENT | s6 |
| SOLR-17748 | DRIFT (4 items) | s6 |
| SOLR-17791 | CONSISTENT | s6 |
| SOLR-17976 | DRIFT (1 item) | s6 |
| SOLR-18109 | CONSISTENT | s7 |
| SOLR-4374 | DRIFT (1 item) | s7 |
| SOLR-5394 | DRIFT (2 items) | s7 |
| SOLR-6193 | DRIFT (4 items) | s7 |
| SOLR-6207 | DRIFT (1 item) | s7 |
| SOLR-6831 | DRIFT (2 items) | s8 |
| SOLR-6975 | CONSISTENT | s8 |
| SOLR-7390 | DRIFT (1 item) | s8 |
| SOLR-7498 | DRIFT (2 items) | s8 |
| SOLR-7520 | CONSISTENT | s8 |
| SOLR-7550 | DRIFT (2 items) | s9 |
| SOLR-8009 | DRIFT (2 items) | s9 |
| SOLR-8020 | CONSISTENT | s9 |
| SOLR-8240 | DRIFT (2 items) | s9 |
| SOLR-8939 | DRIFT (1 item) | s9 |
| SOLR-9148 | DRIFT (2 items) | s10 |
| SOLR-9396 | DRIFT (3 items) | s10 |
| SOLR-9864 | DRIFT (1 item) | s10 |

eDisMax (11):

| Draft | Verdict | Part report |
|---|---|---|
| SOLR-12092 | DRIFT (1 item), hold the sentence until the branch fix lands | s11 |
| SOLR-14913 | DRIFT (1 item) | s11 |
| SOLR-2309 | CONSISTENT | s11 |
| SOLR-2988 | DRIFT (1 item) | s11 |
| SOLR-3243 | DRIFT (1 item) | s11 |
| SOLR-3729 | CONSISTENT | s12 |
| SOLR-3962 | DRIFT (1 item) | s12 |
| SOLR-4362 | DRIFT (3 items) | s12 |
| SOLR-6009 | DRIFT (2 items) | s12 |
| SOLR-6320 | DRIFT (1 item) | s12 |
| SOLR-7120 | DRIFT (1 item) | s12 |

## What the drift is

- **Citations.** The most common item. Pre-change symptom code is linked at the head, or at upstream main, where the rule needs the merge-base with "before this change" in the text (SOLR-10424, 10694, 10844, 11153, 11310, 11364, 11470, 12044, 7550, 8240, 14451, 14678, and others).
- **Changelog titles.** Many branch changelog titles are narrower or wider than the code, or they claim something no receipt supports. Examples: SOLR-10844's "empty counts" claim; SOLR-14913's "failing the whole query"; SOLR-3243's omission of the negated range change; SOLR-6193's overclaim on `facet.offset` and `facet.pivot.mincount`; SOLR-7550's "unreleased" wording where the code counts success. These are branch-side fixes. A title fix moves the head, so each affected draft needs its links re-pointed after the owner's edit.
- **Changelog lines.** SOLR-10492, SOLR-11129 and SOLR-8939 have a changelog line that is not a link at the head SHA.
- **Facts the code or receipt does not support.** Examples: SOLR-10492's Limits claim (`facet.query` does not return BAD_REQUEST for a request-level `group.field`; only the local-only case throws); SOLR-2988's "no text field with a keyword tokenizer" (the schema has `keywordtok`); SOLR-14381's "before the boxing change" wording; SOLR-8009's "10 of 10" count, which matches the receipt but the head file has nine test methods.
- **Internal text.** SOLR-11129 still presents the local `facet.offset` double application as shipped, and its HOLD comment must come out. SOLR-12556 keeps a reviewer block and a CONFIRM wording. SOLR-4362's Proof carries process text.

## Holds and open items for the owner or the main side

1. **SOLR-14931 and SOLR-15041.** Consistent or drift, but both hold. SOLR-14931's two round fixes change the head, so it needs a re-point and a new receipt. SOLR-15041 waits for the blank-line fix and its focused test.
2. **SOLR-12092.** The sentence "both state this" is false at the branch head, because neither the changelog title nor reference guide line 110 states the index-side rule. Hold the sentence until the branch fix lands.
4. **SOLR-8009.** The Proof says "10 of 10", which matches its receipt. The head file has nine test methods. Confirm against the gate log, which is not on disk, before posting.
5. **SOLR-9148.** The merge-base (`97d973814`) was computed from the worktree's local `upstream/main`, which may be behind upstream. Confirm the merge-base before the links are re-pointed.
6. **SOLR-16267.** The receipt's head (`8f4b0c6`) differs from the queue's head SHA (`67ffcb2b63f`). Left for the owner.
7. **SOLR-7550 and SOLR-8240.** The changelog title for SOLR-7550 needs a branch fix. SOLR-8240's "planned follow-up pull request" wording needs the owner's agreement before it is posted.
8. **SOLR-10424, 11129 and others.** Several drafts need a branch-side change before the head is final. Those changes are listed item by item in the part reports.
9. **Merge-base checks.** Several merge-bases were computed from local `upstream/main`. The part reports record which ones; re-verify them against the live upstream before posting.

## Not done

- No build, test, Gradle, gate or test-queue run.
- Gate logs, premise logs, JUnit XML and round 36 goal files are not on disk. Proof counts were checked against the receipts only.
- No live Jira or GitHub state was checked, apart from read-only spot calls that the part reports name (for example, PR 4779 and PR 1481).
- Changelog YAML files were read as text, not parsed.
