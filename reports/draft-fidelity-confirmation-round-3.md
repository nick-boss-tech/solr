# Draft fidelity confirmation, round 3

Verdict: **2 CONFIRMED, 0 DRIFT.** Both round 2 drifts are fixed in place. The applied text matches the round 2 response, and each passage reads coherently with its section and its cited record.

Scope: the two drafts named in `assignments/pool-draft-fidelity-confirmation-round-3.md`, read in place. Read first: `reports/draft-fidelity-confirmation-round-2-response.md`. No draft, receipt or report was edited.

## Per draft

### 1. `pr-drafts/security/SOLR-18368.md`: CONFIRMED

- **(a) Applied text present.** Line 1 reads "Do not post until the owner decides under which key the PR opens: (a) no key, with the upstream PR cited in the body (delete the Jira line below); (b) a follow-up reference to SOLR-18368 (keep the Jira line). Remove this note after deciding." That is the response's "after" text, verbatim.
- **(b) Coherent.** The banner asks the key question. `receipts/SOLR-18368.md` line 9 says the key is settled when Nick approves the opening, and that not opening at all is foreclosed. The banner no longer offers "not opening at all", so it agrees with the receipt. Options (a) and (b) are unchanged, as the assignment requires.
- **(c) No claim beyond the receipt.** The banner poses a question to the owner and states no fact. The option (a) flag is the one the response records. Round 3 does not reopen it.

### 2. `pr-drafts/search-components/SOLR-9396.md`: CONFIRMED

- **(a) Applied text present.**
  - Line 26 reads "The transformer reads `$row.field` and `${row.field}` names from the subquery's own parameters, not from every field of the request." Verbatim match to the response.
  - Line 28 reads "scan the subquery's parameter values for `$row.field` and `${row.field}` references (implemented)." Verbatim match to the response.
- **Head pattern checked against the object.** `git show a5ab2eda4e6:solr/core/src/java/org/apache/solr/response/transform/SubQueryAugmenterFactory.java`, lines 305 to 309, holds `Pattern.compile("\\$\\{?row\\.([\\w.\\-]*\\w)")` at line 307. The `\{?` branch matches both spellings, so the Choice is accurate to the head.
- **Record checked.** `reports/draft-fidelity-searchcomponents-edismax-s10.md` lines 51 and 52 name both spellings, as the response says.
- **(b) Coherent.** The bold summary (line 26) and the body (line 28) agree. Limits bullets 1 and 2 (lines 34 and 35) already name both spellings and are unchanged.
- **(c) No claim beyond the source.** Each statement in the checked passages is the scan's behavior at the head. The draft does not say the transformer uses the `${row.field}` field.

## Observation (not a drift; no change requested)

Read alone, the bold summary at line 26 could suggest that `${row.field}` names are used as row references. Limits bullet 2 (line 35) says the scan matches that spelling but the query parser never treats it as a row reference, so the field read is unused. The two lines agree, because "reads" in line 26 means the scan matches the names. If the owner wants the bold line to stop inviting the other reading, the choice is the owner's. Nothing is requested in this round.

## Relation to round 4

The same two passages are in `reports/review-recheck-round-4.md` (slice R4-B). This report is the record for them under the fidelity assignment, and round 4 cites it.
