# Draft fidelity confirmation, round 2: main-side response

Round 2 (`reports/draft-fidelity-confirmation-round-2.md`) confirmed five of the seven drafts and found two drifts. Both fixes are applied and verified at the current tip. They landed in commit `181818be5a` ("Apply the open review fixes lane, round 1, to the drafts", windows review agent), which took them from the round 2 record before this lane ran; this response verifies each fix against the record it must match and records the outcome. No draft was edited by this lane, and the five CONFIRMED drafts were not touched.

## 1. `security/SOLR-18368.md`: APPLIED

Verified against `receipts/SOLR-18368.md` line 9: not opening at all is foreclosed by the still-an-issue verdict (`reports/solr-18368-still-an-issue.md`) and Nick's resolved-ticket rule of 2026-10-11, the branch joins the openings list, and only the key question is settled when Nick approves the opening.

- Line 1 (banner), before: "Do not post until the owner decides whether a PR opens and under which key".
- Line 1 (banner), after: "Do not post until the owner decides under which key the PR opens".
- Options (a) and (b) stand as written, and the note still says to remove it after deciding. Option (c) was already gone.
- Recorded flag, carried from the round 2 report and not a drift: the receipt's owner-call paragraph names carrying the SOLR-18368 key or opening as a follow-up reference; banner option (a), no key with the upstream PR cited in the body, is a shape the receipt does not name. The options stand as written; the owner confirms (a) before opening. Round 3 does not re-open this flag.

## 2. `search-components/SOLR-9396.md`: APPLIED

Verified against the head source and the record: at head `a5ab2eda4e6`, `SubQueryAugmenterFactory.java` line 307 compiles the pattern `\$\{?row\.`, which scans both spellings, and `reports/draft-fidelity-searchcomponents-edismax-s10.md` lines 51 and 52 name both spellings. Line 15 and the Limits bullets already named both; the Choice now agrees with them and with the code.

- Line 26 (Choice bold summary), before: "The transformer reads `$row.` names from the subquery's own parameters, not from every field of the request."
- Line 26, after: "The transformer reads `$row.field` and `${row.field}` names from the subquery's own parameters, not from every field of the request."
- Line 28, before: "scan the subquery's parameter values for `$row.field` references (implemented)".
- Line 28, after: "scan the subquery's parameter values for `$row.field` and `${row.field}` references (implemented)".

## Next step

`assignments/pool-draft-fidelity-confirmation-round-3.md` re-checks these two drafts only, per-draft CONFIRMED or DRIFT.
