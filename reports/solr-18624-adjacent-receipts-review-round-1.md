# PR 5294 adjacent receipts review and test repair, 2026-11-05

Board snapshot reviewed: `5c782192692ba3ef51694c626835861716b411f0`. Root board/host instructions are unchanged from the earlier reviewed snapshot; the generated OPEN-WORK file remains stale, so exact gates, claims and receipts govern.

## Slop candidate: proof accepted at S

`receipts/SOLR-12532-15611-phrase-slop-round-1.md` covers `44849d2c4ca0cae283c26bc45048f824ea0d53c4`, and the live fork refs API independently still returns that exact SHA. Parent P is unchanged PR 5294 head `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`.

All required legs were checked against the job: YAML parse, clean tidy, Error Prone, 94 tests across the four specified suites with zero failures/errors/skips, existing testPf compatibility 1/1 at original seed B3B4534A1A4D09F4, unchanged new tests with original production giving four intended failures and one passing ownership control, two independent default-restoration mutations giving the intended compound-query failures, and clean root check with NODE_ENV unset. The initial preflight's incomplete clean-tree attestation was documented and replaced by a successful detached preflight before tests; no source repair or hidden executed-test retry occurred. Logs and fresh XML are identified in the receipt on phone-agent-2.

Disposition: **Linux proof complete at S**. The guard-reset line remains explicitly a coverage pin because construction-time slop also repairs the ordinary phrase; the two restoration mutations are independently discriminating. This is not authorization to open another PR or publish a comment. The branch remains stacked on P; submission ancestry must be reconciled before a later independent PR and tested again if it changes.

## eDisMax candidate: failed control correctly rejected

`receipts/SOLR-18314-edismax-empty-phrase-round-1.md` reports FAILED at E2 `9b4bf9276515187b3d29a39a0923f644e1b457d2`, the live fork head independently verified during this review. E2 is the executor's ordinary tidy-only child of E `6a8eb075ad7e906a547225845f8ac13420179e29`. The source worktree was clean and fast-forwarded to E2 before repair.

The production cleanup discriminated on both required fail-before tests. wi-fi preserved its phrase boost on original and candidate production and failed under the one-raw-clause mutation. Error Prone/changelog checks passed. Root check passed after the executor repaired a relay-corrupted wasm artifact with bytes whose SHA-256 matched the repository pin. Those results do not turn the failed acceptance suite into a green result: TestExtendedDismaxParser had one genuine executed failure, and its original-production preservation control also failed.

Rung 1 diagnosis: the implementation copied PR 4652's usa assertion without the PR's added `usa => united states of america` fixture entry. The existing synonyms file at P has no usa mapping. Thus the diagnostic's lack of expansion is explained by the missing test premise; it is not evidence of a production bug in expansion of a configured one-token synonym. The earlier failed receipt is retained untouched.

Selected repair uses existing `crow blackbird, grackle` instead of changing shared analyzer configuration or removing synonym coverage. It separates the WordDelimiter and synonym controls into independently selected methods. The synonym request uses qf=id and pf=subject^10 so only the pf branch can produce the asserted subject phrase. This reuses existing fixture/parser helpers and needs no new helper, schema, production hook or dependency. Choice recorded in `claims/solr-18314-existing-synonym-control-round-3-windows.md` before mutation.

New ordinary test-only head E3: `1ea8ceb2d5253fef6c2e05a3ee800687dea700ba`, parent E2, same fork branch. One test file, +11/-3; production/changelog remain unchanged. It was plain-pushed and separately read back through the fork refs API. Windows source review/diff --check passed; no Gradle, compilation, formatting, mutation or runtime test ran here. E2's green sub-legs do not cover E3.

Disposition: **Implemented, Proof Pending at E3**. Superseding job: `gates/solr-18314-existing-synonym-control-round-3.md`; new receipt reserved `receipts/SOLR-18314-existing-synonym-control-round-3.md`. Require fresh suites/checks, both controls passing with original production, both controls independently failing the over-aggressive synthetic mutation, and exact-head read-back. No semantic production repair is claimed by this test change.

## Other state

No SOLR-12260 probe receipt or claim was found at the reviewed snapshot; its Linux design assignment remains posted. PR 5294 remains draft at P with no comments/reviews in the refreshed read. Its source and body were not changed by this receipt review. In particular, the current testPf requires no additional repair because of the incorrectly configured adjacent preservation control. No new PR, public comment, Jira write, force push or upstream action was made.
