# Main-side responses, 2026-10-15

Dispositions from the main side on items the review lane raised and cannot settle on its own: the eDisMax history items, the proof-shape audit's SOLR-12998 proposal, the SOLR-15035 changelog-parse question, and a count discrepancy in the proof-shape audit. Each item states the disposition and the record it rests on. Nothing here authorizes a PR opening, a ready flip, or a comment post.

## (a) eDisMax history items O3 to O6

- **O4, O5, O6: DECLINED.** The owner's standing ruling of 2026-10-14 governs: do not rewrite a branch's history for cosmetic commit-message defects; keep what we have, because the final PR is what matters, not the commit subjects. The history rewrites these items propose are cosmetic in that sense, so they do not proceed, on any of the branches they name.
- **O3: narrowed to trailer removal only.** Three SOLR-12092 commits carry `Co-Authored-By` trailers naming Claude. Removing those trailers is not cosmetic and is not covered by the decline: the standing rule is that no "Claude" appears in commit authors, committers, or trailers on anything pushed. Trailer removal on those three commits may proceed as its own job when one is filed. The squash O3 also proposed is not directed, and no other part of O3 is authorized by this response.

## (b) Proof-shape audit: the SOLR-12998 shape-A proposal is not ratified

- The main side does not ratify the audit's shape-A proof proposal for SOLR-12998. The held-items ruling of 2026-10-13 stands: no new proof run for SOLR-12998, and the PR body edited to match the decided proof shape (`material/held-items-decisions-2026-10-13.md`). That ruling stands unless the owner overrules it.
- The shape-A proposal is recorded as pending the owner. No lane should run the shape-A proof, and no record should describe SOLR-12998's proof as awaiting it; the branch's proof state is the 2026-10-13 ruling's shape.

## (c) SOLR-15035: the changelog-parse question (flag F3)

- Flag F3 is being closed on the main side: a parse check of the branch's changelog fragment at the settled head (`12d7491e82cd`) is run main-side, and its result is recorded with the branch's records when it lands.
- Readiness is governed by the first-rung settlement, not by F3: `reports/solrcloud-hold-first-rung-round-1.md` settled SOLR-15035 TO SATISFIED on the landed base and head-production runs. The parse check closes the flag; it does not reopen the settlement, and no lane needs to hold work on it.

## (d) Proof-shape audit: the "26" figure

- Noted, not blocking: the audit's "26" figure does not reconcile with its own 22-ticket table. The coordinator reconciles the claim text so the report's summary and its table agree. Items that rest on the table itself are unaffected; items that cite the "26" figure should cite the table's count once reconciled.
