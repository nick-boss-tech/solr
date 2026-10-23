# Failed Blocker patches: both validated

Status: DONE. Nick's requested follow-up repairs and validation are complete for SOLR-17840 and SOLR-18446. Both fork branches are green at the heads below. No PR was opened and no Jira or GitHub comment was posted.

| Ticket | Verified fork branch and head | Result |
| --- | --- | --- |
| SOLR-17840 | `solr-17840-preserve-partial-results-phone-agent-2`, `21bf1dbb6ea2365cb3be7caab74ea853af74dd0d` | New fetch-scope patch pushed; 15 LTR and 13 core focused tests pass; Error Prone and core/LTR module checks pass |
| SOLR-18446 | `solr-18446-path-routing-phone-agent-2`, `8bb60e6f61abc9827fb58dcae1863cabb33f9796` | Existing import correction verified; 13 focused tests pass; Error Prone and core module checks pass |

## LTR: preserved hits now survive response fetching

[Commit 21bf1dbb6e](https://github.com/nick-boss-tech/solr/commit/21bf1dbb6ea2365cb3be7caab74ea853af74dd0d) completes the second demonstrated timeout boundary. The already-collected documents can finish fetching even after the request limit has tripped, but only for partial results allowed and only inside the selected-document fetch. The limit remains latched and is restored before transformers or subqueries. This is not request-wide timeout disabling.

The original four-document/order/score/timing regression is unchanged. Removing only this new fetch scope reproduces the doc-values ExitingReaderException and missing response; restoring the head gives all 15 LTR tests green. Seven new deterministic scope tests and six existing core limit tests also pass, none skipped. Both core and LTR production/tests compile with Error Prone. Module checks exclude the full test suites; they do not imply all core tests ran.

[Receipt](../receipts/SOLR-17840.md) and [phase record](../gates/SOLR-17840-phase-phone-agent-2-patches-round-2.md). Logs and separate XML copies: `/workspace/phone-agent-2-logs/gate-17840-patches-round-2/`. Seed `1BB02D3762B56611`.

## Routing: import-fixed candidate now verified

No further production change was necessary after [8bb60e6f61](https://github.com/nick-boss-tech/solr/commit/8bb60e6f61abc9827fb58dcae1863cabb33f9796). The baseline implementation fails the exact new path-precedence regression, selecting collection2 where collection1 is required. Restored head passes HttpSolrCallCloudTest (4 tests) and AliasIntegrationTest (9 tests), none skipped, plus Error Prone and core module checks.

The proof runner initially expected the wrong JUnit assertion display format. Parsed XML established the exact intended mismatch; validation continued from the head step without repeating completed builds or changing code. Both runner states are archived. Independent review found no confirmed production defect; direct actual-alias narrowing and disjoint-parameter cases remain non-blocking coverage limits, not claimed new coverage.

[Receipt](../receipts/SOLR-18446.md) and [phase record](../gates/SOLR-18446-phase-phone-agent-2-patches-round-2.md). Logs and XML: `/workspace/phone-agent-2-logs/gate-18446-patches-round-2/`. Seed `18446`.

## Closeout

[New claim](../claims/phone-agent-2-blocker-patches-round-2.md) supersedes the two implementation handbacks only. The original DONE claim and prior failed gate records were not edited. Prior receipt evidence remains below each new verified section. Source worktrees are clean. All builds were serial under the shared lock and disk/inode guard; final disk use 34 percent and inode use 41 percent.

Both patches are available on the fork for owner review. Green validation is not an upstream merge or a Jira status change. SOLR-17844 and the other classifications from round 1 were not reopened.
