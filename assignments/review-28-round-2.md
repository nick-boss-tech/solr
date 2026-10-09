# Assignment: review round 2 for the 28 update-processing PRs (verification of the corrections)

Claim first: add `claims/review-28-round-2.md`, then work. Reports go in `reports/review-28-round-2*.md` on this branch. Read-only toward the drafts, the receipts, the branches, and the PRs: report findings, do not edit any of them.

## Background

Round 1 (`reports/review-opened-28.md`) found 26 fix-first and 2 owner-call among the 28 drafts. The corrections round (`reports/review-corrections-28.md`) edited all 28 drafts; the owner ruled the open calls (answers in `material/review-opened-28-answers.md`, including the ratified confirmations and the plain-language ruling); the plain-language pass (commit `78ce00d90af`) rewrote Proof phrasing without changing any fact; the corrected drafts were then applied to the 28 live PR bodies on apache/solr and verified to match. A verification of the plain-language pass (`reports/review-plain-language-28.md`) checked facts preservation and raised six new findings, listed in part 2 below.

This round verifies the corrections as a whole. It is not a fresh audit: do not re-open branches, decisions, or claims that round 1 accepted and this round's checklist does not name.

## Part 1: finding-by-finding verification

For every accepted finding in `reports/review-opened-28.md` and every edit recorded in `reports/review-corrections-28.md`, check the final draft in `pr-drafts/update-processing/` and state one of: applied as specified; applied differently (say how); missing. Pay particular attention to:

- The bold one-line summary opening every Limits section.
- The planned follow-up sentences the owner ratified (22 drafts carry one; SOLR-5065, 12705, 14718, 14262, 7022 deliberately carry none).
- The scoped claims: SOLR-6045 limited to the merger path with the factory path named in Limits; SOLR-5887 limited to the covered paths with the ClassificationUpdateProcessor bypass named in Limits.
- SOLR-5505: the always-on `[core]` format kept, changelog type `changed`, and the new Choices section posing the opt-in alternative.
- SOLR-7504 and SOLR-12705: Proof and head references at the null-fix head `2fe06bfd917f` and its interaction with 12705's scope.
- The six corrected titles (SOLR-5754, 13943, 16673, 5505, 12864, 12245): each title against the change it names.
- No internal process vocabulary anywhere in any draft, all sections, not only Proof: gate, receipt, ledger, "JUnit XML", "rc=", takeover, "live tip" (see part 2, item 5). The ordinary verb "delegate" in SOLR-5939 is not a hit.

## Part 2: verify the six dispositions

The six findings from `reports/review-plain-language-28.md` have been dispositioned by the main side (recorded at the end of `material/review-opened-28-answers.md`): five fixed directly in the drafts and the SOLR-7022 receipt, one (the SOLR-13696 pre-fix heads) accepted under the SOLR-16673 addendum precedent. Verify each landed as recorded, and that no public claim exceeds what the corrected records support.

## Part 3: verdict

Per PR: ready to flip, or the specific remaining item. One roll-up report. Any proposed draft change is listed as exact replacement text for the owner's ratification; nothing is applied in this round.
