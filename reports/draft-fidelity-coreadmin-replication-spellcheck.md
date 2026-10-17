# Draft fidelity review: Core admin, Replication and backup and Spellcheck drafts

Assignment: `assignments/pool-draft-fidelity-coreadmin-replication-spellcheck.md`. Claim: `claims/pool-draft-fidelity-coreadmin-replication-spellcheck.md`. Lead: the windows review agent. Part reports, with the exact replacement text for each item:

- `reports/draft-fidelity-coreadmin-replication-spellcheck-s1.md`: core-admin SOLR-11939, 12007, 13246, 15024, 15805.
- `reports/draft-fidelity-coreadmin-replication-spellcheck-s2.md`: core-admin SOLR-16725, 16849, 17297, 17377, 17708.
- `reports/draft-fidelity-coreadmin-replication-spellcheck-s3.md`: core-admin SOLR-17731, 18010, 4989, 6438, 8275.
- `reports/draft-fidelity-coreadmin-replication-spellcheck-s4.md`: core-admin SOLR-8576, 9750; replication SOLR-11650, 12085, 12246.
- `reports/draft-fidelity-coreadmin-replication-spellcheck-s5.md`: replication SOLR-17287, 8430, 9598, 9865; spellcheck SOLR-3701, 4367.

No draft was edited. Read-only throughout. No build, test, gate or test-queue run. No PR, comment, review, submit-branch, PR-description or Jira write.

## Verdicts

Twenty-six drafts: two are CONSISTENT and twenty-four are DRIFT. Every named head matched the fork's live tip in `ls-remote` at the time of the check.

| Draft | Verdict | Part report |
|---|---|---|
| SOLR-11939 | DRIFT (2 items) | s1 |
| SOLR-12007 | DRIFT (2 items) | s1 |
| SOLR-13246 | DRIFT (3 items) | s1 |
| SOLR-15024 | DRIFT (2 items) | s1 |
| SOLR-15805 | DRIFT (1 item) | s1 |
| SOLR-16725 | CONSISTENT | s2 |
| SOLR-16849 | CONSISTENT | s2 |
| SOLR-17297 | DRIFT (2 items), held for an owner DISCUSS call | s2 |
| SOLR-17377 | DRIFT (1 item) | s2 |
| SOLR-17708 | DRIFT (3 items) | s2 |
| SOLR-17731 | DRIFT (3 items) | s3 |
| SOLR-18010 | DRIFT (2 items) | s3 |
| SOLR-4989 | DRIFT (1 item) | s3 |
| SOLR-6438 | DRIFT (1 item) | s3 |
| SOLR-8275 | DRIFT (1 item) | s3 |
| SOLR-8576 | DRIFT (4 items) | s4 |
| SOLR-9750 | DRIFT (1 item) | s4 |
| SOLR-11650 | DRIFT (6 items) | s4 |
| SOLR-12085 | DRIFT (3 items) | s4 |
| SOLR-12246 | DRIFT (2 items) | s4 |
| SOLR-17287 | DRIFT (2 items) | s5 |
| SOLR-8430 | DRIFT (3 items) | s5 |
| SOLR-9598 | DRIFT (1 item) | s5 |
| SOLR-9865 | DRIFT (1 item) | s5 |
| SOLR-3701 | DRIFT (1 item) | s5 |
| SOLR-4367 | DRIFT (2 items) | s5 |

## What the drift is

- **Citations.** Pre-change symptom links point at the head SHA or at upstream main, where the rule needs the merge-base, with the text saying so. Several drafts also cite head line ranges that shifted after an import. The merge-base for each draft is named in its part report.
- **Changelog titles.** Several branch changelog titles overstate, understate or miss their change: SOLR-11650, 12085, 8430, 3701, and others. These are branch-side fixes. A title fix moves the head, so the links need re-pointing after the edit.
- **Placeholders and internal text.** `[CONFIRM: count]` placeholders (SOLR-9865 and 17287), an internal OWED note (SOLR-8576), and an owner-fill bracket in SOLR-4367's Proof. These must come out before posting.
- **Verified dates.** Several "verified" dates are receipt record dates, not run dates. The replacement wording is "recorded" in the part reports.

## Specific items the owner or the main side must decide

1. **SOLR-8576.** The alias check at the head (line 1188) does not read the alias (round k6 FIX 1). The receipt says PR-ready, but round k6 and the material hold the draft for FIX 1. The owner should reconcile before posting.
2. **SOLR-17297.** The Choice is a narrow-versus-broad scope question, which `pr-formula.md` section 4 excludes. The owner's DISCUSS call on the branch is still open, so the draft stays held.
3. **SOLR-12085 and SOLR-12246.** Each draft says "no replication test ran", which contradicts the draft's own `IndexFetcherPacketProtocolTest` count. The count stands; the sentence goes.
4. **SOLR-8275.** The `RecoveryStrategy` range (lines 959 to 989) runs past the end of the file, which is 945 lines. Round 1 part k6 carries the same bad range, so k6 may need a correction too.
5. **SOLR-15024.** The Jira text was not checked (no packet on disk and no Jira tool in this round). An OWED note is in the draft.
6. **SOLR-16725, 16849, 17708.** Ticket text was not checked against `research/jira-context` for these three. SOLR-17708 also carries an internal OWED note in the draft text, and its branch changelog title contradicts the draft's Limits.
7. **Shared "verified" dates.** The core-admin round's k3 NOTE 16 applies to the whole set: the "verified" dates are record dates from the receipts, not run dates.

## Not done

- No build, test, Gradle, gate or queue run.
- Gate logs, base-failure logs and the takeover log are not on disk. Base-failure claims were checked against receipt text only.
- Several round 1 parts were grep-checked only, because they name none of these tickets: core-admin k1 to k3 and k5; replication g1 and g4; spellcheck parts.
- No live Jira or GitHub state was checked, apart from read-only spot calls that the part reports name.
