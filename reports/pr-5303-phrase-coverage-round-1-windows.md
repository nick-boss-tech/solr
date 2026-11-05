# PR 5303 phrase-boost test-only coverage implementation, round 1

Status: **Implemented, Proof Pending**.

| Revision | Full SHA |
| --- | --- |
| Ian's PR 5303 contributor parent H | `e7f66ef3220f412bc3a54b566ca78ce4a906715e` |
| PR base/merge base B | `1fc38d1e40cb12512aba2b1990b22b04111dad2d` |
| Offered additive coverage F | `256612b7f85dc831f9332d51182258d9594fcc1f` |

Owner authorization: Nick explicitly requested building the test-only additions and handing them to a gate. The board snapshot checked was `efcffaefd4e41969baf131de5cbbd813d9c0b237`; no competing claim/job for PR 5303 or SOLR-18625 was present. Claim: `claims/pr-5303-phrase-coverage-round-1-windows.md`, posted and verified at board commit `bc809e7f651e05e324b60787df2fe2f093b8f6c6`.

Fork branch `nick-boss-tech/solr:pr-5303-phrase-coverage` preserves Ian's ancestry and contains one ordinary Nick Shanin commit. Only outbound file is `solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java`, +66/-22. The commit was plain-pushed and independently read back through the fork refs API at F. The change is intended for the existing PR, not a competing cleanup implementation. No production/changelog/schema/synonym/helper/dependency or PR 5294 source changes were made.

## Correctness and scope

The existing testNoEmptyPhraseBoostClause now covers its original plain-token case explicitly in both sow modes. Fielded-only input has its own test method so a negative-control failure in the plain-token case cannot hide the fielded case. Match-all and wi-fi preservation are split into methods and cover both modes. A dedicated grackle/crow-blackbird control checks the existing synonym graph with qf=id and pf=subject^10, isolating the subject phrase to pf. There are five independently executed methods replacing one combined method, a net increase of four methods.

This is additional coverage for Ian's already-correct production fix; both H's original suite and F's enlarged suite are expected to pass. Do not call a passing original suite evidence that the new assertions are ineffective. Conversely, old E3 proof does not establish F: it had another parent and different tests.

## Architecture, maintenance and reuse

Decision ladder rung 1 selects existing TestExtendedDismaxParser helpers, schema and the already-configured `crow blackbird, grackle` rule; no new analyzer or fixture is needed. The earlier missing usa mapping is not copied. The contributor's assertion intent is retained while scenario separation permits independent XML outcomes and targeted mutation proof. This implements the claim's scope; the fielded case was separated further during source review to keep all three regression outcomes independent.

No new public API, extension point or security boundary is introduced. Production/changelog files remain inherited, byte-identical to H; Nick's only outbound file is the test. No user documentation or separate changelog entry is warranted for this optional coverage delta.

## Verification and handoff

Windows source review and diff --check passed. No Gradle, compilation, formatter, tests or synthetic mutations ran here. All new/revised assertions are pins until the new Linux runs discriminate them. Exact-head job: `gates/pr-5303-phrase-coverage-round-1.md`. Receipt target: `receipts/PR-5303-phrase-coverage-round-1.md`.

The gate requests original H and F suites, root tidy/Error Prone/root check at F, three independent existing-bug regression failures with pre-PR production, both analyzer-preservation failures under a one-clause shortcut, and false-mode-specific mutations to ensure early true-mode failures do not masquerade as proof of both modes. The zero-clause guard is not claimed independently allocation/performance-proven. No benchmark claim is made.

The stable neighboring MultiWordSynonyms query-shape method is selected instead of that class's full suite, because its unrelated testPf flake is the subject of unmerged PR 5294. This coverage patch must not import the PR 5294 test repair into Ian's branch. Any newly encountered baseline failure must be diagnosed rather than silently retried or absorbed into the patch.

Before any later public offer, refresh Ian's PR head/state/discussion and require the exact offered head's complete receipt, matching ancestry and no remaining concern; a moved or merged PR requires reconciliation. Nothing was posted to the public PR or JIRA, no new PR was opened, and Ian's branch was not changed. This report and gate are fork coordination records only.
