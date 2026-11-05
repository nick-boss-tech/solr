# SOLR-18624 assertion repair, round 2

Status: Implemented, Proof Pending.

## Revisions and authority

- Owner direction: "Try to patch this", following the full source review of the published test branch. This authorizes the scoped repair and ordinary fork push; it is a new slice after the completed publication-only assignment.
- Claim: `claims/solr-18624-assertion-repair-round-2-windows.md` (posted and verified at board commit `dbe1a647abcd735843d9c082cbc22167d0100e55`).
- Fork branch: `nick-boss-tech/solr:solr-18624-flaky-multiword-synonyms`.
- Parent H0: `c610e6d05db5700835a123de54da61137478e48f`.
- Upstream base and merge base B: `07ccc73cd24e26e59da169ca35b3fd5f64c35986`.
- Follow-up head and separate remote read-back: `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`.
- Commit: `SOLR-18624: Preserve phrase slop and function boost assertions`.
- Follow-up diff against H0: one file, 36 additions and 6 deletions. Cumulative diff against B: one file, 57 additions and 11 deletions.
- Only changed path: `solr/core/src/test/org/apache/solr/search/TestMultiWordSynonyms.java`.
- No production code, schema or fixture change. Test-only changes are exempt from a changelog fragment under `dev-docs/changelog.adoc:127-128`.

## Repair

1. Both baseline requests still use a deterministic secondary sort, and now require strictly descending scores across IDs 13,12,11,10. Thus sorting by the boost field cannot conceal a missing additive `bf` contribution. Their failure messages describe the score prerequisite.
2. A new slop-zero request requires equal scores for documents 10 and 13: reversing `bar foo` must not receive an exact-phrase boost.
3. The slop-two `bar foo` request again requires document 10 to win, with its score strictly greater than document 13. This score check precedes the query-shape checks. Both `bar foo` and `tropical cyclone foo` phrase paths must carry slop 2.
4. The `tropical cyclone foo` pf2 request checks all four IDs without an order requirement, and specifically checks the boosted `tropical cyclone` synonym bigram and `cyclone foo` bigram with slop 2. A comment explains why the first result is not constrained.

The existing parser expectation in `TestMultiWordSynonyms` and the precise parsed-query assertions in `TestExtendedDismaxParser.testPfPs` supply the patterns. No new helper or fixture was needed.

## Review correction and limits

The first review's Close verdict was too strong. The subsequent review identified that broad `contains('~2')` checks can miss a dropped synonym path or bigram, and that secondary field sorting can hide absent function boosting. These are coverage findings, not established production defects. The source repair addresses both; runtime discrimination still belongs to Linux.

The source trace explains the suspect tie: pf2 shingles the raw words of `tropical cyclone foo` into `tropical cyclone` and `cyclone foo` before synonym analysis. All four fixture documents match the former through `bar`; none matches the latter. Random merge policy can reverse tied document order. The issue lacks the original seed and complete failing request, so this explanation must be distinguished from a reproduced original failure.

## Verification and handoff

- Static diff review and `git diff --check`: passed before staging and again across the committed H0-to-H1 diff.
- Worktree clean after commit: confirmed with a separate `git status --short` call (no output).
- Plain fork push and separate GitHub ref read-back: succeeded, both at `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`.
- Windows ran no Gradle, formatter, compilation, tests or mutations.
- Exact-head Linux job: `gates/solr-18624-assertion-repair-round-2.md`; receipt reserved for its executor at `receipts/SOLR-18624-assertion-repair-round-2.md`.
- The job requires focused repetition, full class and neighboring method, a deterministic query diagnostic, three independent compiling mutations with unchanged acceptance tests, tidy, Error Prone and root check. Posted work does not establish a Linux claim or a passing result.

No PR, JIRA comment or public review was submitted. This report and the gate deliver the implementation claim; validation remains pending until the complete exact-head receipt is available.
