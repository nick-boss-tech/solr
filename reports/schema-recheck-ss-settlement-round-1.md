# Schema re-check, SS settlement (SS-01 to SS-04), round 1

Verdict: **SS-01, SS-02 and SS-04 are CLOSED. SS-03 is still OPEN.** The main side's settlement located SS-03 in the wrong file. The stale citation is in the schema round 2 record itself, at line 57, not in the round 1 response. That record is this lane's own file, and the one-line fix is named below.

Scope: the main side's settlement commit `87249be89fc` and the records it edited (`reports/schema-convergence-round-2-response.md`, `reports/schema-convergence-round-1-response.md`), checked against `reports/schema-recheck-round2-settlement-round-1.md` and the live SOLR-15358 draft `pr-drafts/schema-analysis/SOLR-15358.md`. The claim is `claims/pool-recheck-schema-ss-settlement.md`. Two read-only subagents. Nothing was edited, posted, built or tested.

## Closed

- **SS-01 (round 2 response, the SCH-R2-06 paragraph, line 17): CLOSED.** The clause "RC-S1, RC-S3 and RC-S5 are settled; SCH-R1-16 and the two owner holds on SOLR-15358 remain open." is present word for word. The superseded sentence appears only inside the dated correction note that quotes it, which is the file's correction convention.
- **SS-02 (round 1 response, SCH-R1-14 entry): CLOSED.** The citation reads "lines 19 and 33". The live draft matches: line 19 carries "1.6 and later", and line 33 carries "returned as stored values by default on schema version 1.6 and later". Line 31 is the "A choice to check" heading.
- **SS-04 (round 2 response, opening paragraph, line 3): CLOSED.** It now reads "six items (SCH-R2-01 to SCH-R2-06)". The "All five items held" clause is correct, because the settlement acted on five items.
- **Dashes.** The two edited records contain no em dash or en dash.

## Open

**SS-03 | `reports/schema-convergence-round-2.md` line 57 | should-fix, citation.** The SCH-R2-04 entry reads "The live line 41 has the register text, 'could carry the derived currency values into the document it sends back'." The live landing-order sentence is at line 43 of `pr-drafts/schema-analysis/SOLR-15358.md`, as the round 2 response's own SCH-R2-04 correction records at its line 13. The round 2 response does not edit line 57, so the stale citation stands.
Replacement for "The live line 41 has the register text": "The live line 43 has the register text".

My earlier SS-03 entry said the stale citation was in the round 1 response. That was wrong, and the main side's check was right to find no such citation there. The fix belongs in this round 2 record.

## Notes (not blocking)

- The settlement record (`reports/schema-recheck-round2-settlement-round-1-response.md`) quotes "line 41" while reporting the SS-03 check. That is a quotation, not a live citation, so it needs no change.
- `reports/schema-convergence-round-1.md` line 24 says "Line 41 says 'No test covers PointType...'". That refers to the SOLR-15357 draft and is unrelated.

## Not checked

- The SOLR-14199 draft and receipt updates that landed in `e600e4b945b` and later. They belong to the SOLR-14199 record job, not to this settlement.

## Convergence line

SS-01, SS-02 and SS-04 are CLOSED. SS-03 is OPEN until line 57 of the schema round 2 record reads "live line 43". Once that line is corrected, the schema round 2 settlement has no open item in the reports. The SOLR-14199 draft pass and the SOLR-15358 holds remain open as before. Nothing was edited, posted, built or tested in this round.
