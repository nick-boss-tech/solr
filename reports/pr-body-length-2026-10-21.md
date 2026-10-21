# PR body length survey and first trim (vm3, 2026-10-21)

Guide (pr-formula.md): the whole description about one screenful, roughly under 3,500 characters unless the ticket is unusually complex. Length here is visible characters: markdown link targets dropped, backticks and emphasis marks removed, counted from the AI header to the end.

## Survey

87 open PRs from nick-boss-tech. 37 are over 3,500 visible characters. Table: `/workspace/tmp/len/table.txt` at survey time; the list below is the over-guide set, largest first.

| PR | Visible | Draft or ready |
| --- | --- | --- |
| 5017 | 10,697 | ready |
| 5030 | 9,710 | ready |
| 4968 | 7,415 | ready |
| 5169 | 6,735 | ready |
| 5031 | 6,397 | ready |
| 4969 | 6,056 | ready |
| 5118 | 6,026 | draft (kept long on purpose: wide blast radius, owner asked for detail) |
| 5000 | 5,865 | ready |
| 5101 | 5,466 | ready |
| 5002 | 5,366 | ready |
| 5134 | 5,170 | ready, trimmed in this pass to 3,485 |
| 5135 | 5,125 | ready |
| 5075 | 5,012 | ready |
| 5088 | 4,976 | ready |
| 5028 | 4,947 | ready |
| 5076 | 4,909 | ready |
| 5016 | 4,876 | ready |
| 5011 | 4,798 | ready |
| 5003 | 4,742 | ready |
| 5096 | 4,723 | ready |
| 5029 | 4,487 | ready |
| 5098 | 4,473 | ready |
| 5010 | 4,347 | ready |
| 5159 | 4,190 | ready |
| 5100 | 4,146 | ready |
| 5077 | 4,143 | ready |
| 5138 | 4,079 | ready |
| 5156 | 3,996 | ready |
| 4998 | 3,979 | ready |
| 5089 | 3,952 | ready |
| 5081 | 3,940 | ready |
| 5116 | 3,867 | ready |
| 5093 | 3,772 | ready |
| 5145 | 3,704 | ready |
| 5141 | 3,667 | ready |
| 5180 | 3,644 | ready |
| 5154 | 3,507 | ready |

## First trim: PR 5134 (SOLR-6831)

5,170 to 3,485 visible characters. Live body edited 2026-10-21 (read-back matches the draft).

What went:
- The behavior change was stated in Change, Proof and Limits. It now sits once, under the change, and Limits lost its copy.
- The refinement-stop sentence was repeated in Proof and Limits. Proof keeps the test outcomes.
- The test-hook note and the "passes with and without" tests are one bullet each, not a paragraph each.
- The AI block was replaced by the one-line footer the formula asks for.

What stayed: every SHA, every line anchor, every test name, the reverted-head run (7 tests, 1 failure), the fail-before outcome for the omitted-answer test, the 2026-10-14 verification at `24bd15ebb84e`, and all limits that a reviewer would ask about.

One wording change to check: "a partial or omitted refinement answer makes the coordinator's response partial" widens the live "partial marker" sentence to omitted answers. This matches the reading of `haveCompleteResults` and `refinePivotFacets` at `5c1124bdef` in `pr-drafts/2026-10-16-review/PR-5134-reconcile-5c1124bd.md`. No new run was made for it.

## Not yet trimmed

The other 36 over-guide PRs are unchanged. Redundancy is usually the same claim in the section summary, the Change, and Limits. The method above is the template: keep every number, SHA, anchor and test name, cut the repeats, and reword to the formula's one-line footer. PR 5118 stays long on purpose (see the table).

## Nested highlighting follow-up (PR 5104, SOLR-2681)

- Branch `solr-2681-nested-submit` exists at `cec42ef4f0`. No PR exists for it (checked through the API, state all).
- The 5104 Limits sentence "A follow-up pull request is planned for the nested forms" is supported by that branch.
- Opening the nested PR still needs Nick's approval. The ticket question (attach to SOLR-2681, or file a new follow-up) is still open in `pr-drafts/highlighting-nested/ticket-draft.md`.
- No live change to 5104.
