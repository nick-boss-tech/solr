# Gate job: SOLR-12608 changelog title correction and gate at the landed head

- Branch: solr-12608-submit. Head: d1dd8a1f9f0aa17c6e34f8c5e40cb9768a83aea2 (live tip, verified by `git ls-remote` on 2026-10-13; receipt `receipts/SOLR-12608.md`, GATE GREEN at this head).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: DONE. Landed head c16d49d8417396a396c0bcedbe4305d406f98deb (fast-forward from d1dd8a1f9f0aa17c6e34f8c5e40cb9768a83aea2), title is the G2 replacement wording, gate GREEN 2026-10-14T04:26Z. Receipt refreshed in `receipts/SOLR-12608.md`. Posted 2026-10-13 by the main side (openings census, `reports/openings-census-round-1.md`).
- Capability tags: gate.
- Job type: branch commit under the patch loop, then the full gate steps at the landed head. The claiming host makes the title commit and pushes it (the patching agent pushes), then gates the head it created.
- Why owed: the query-parsing review (finding G2, decision D3 in `reports/query-parsing-review-summary.md`) found the branch changelog title claims a memory benefit the record does not show ("avoids the excessive memory use seen with edismax queries"). The draft's Limits already says the change claims no memory benefit, so the title and the draft disagree until the title is corrected on the branch. `reports/branch-review-verdicts-landing-round-1.md` records this as the item that keeps the ticket from opening.
- The correction: take the replacement wording from the G2 gate request in the query-parsing review record (`reports/query-parsing-review-q2.md` and `reports/query-parsing-review-summary.md`). If the exact replacement wording cannot be found there, write a title that states only what the change does (repeated wildcards in a wildcard query are collapsed before the query is built) with no memory claim, and record that choice in this file.
- Modules: `:solr:core`.
- Focused classes at the head: `org.apache.solr.search.TestSolrQueryParser` (38 tests at the current head, including `testRepeatedWildcardsAreCollapsed`; the receipt counts 86 focused tests across the gate run at `d1dd8a1f9f0`, so run the class set the receipt names if it names more).
- Seed: `12608C0FFEE12608`.
- Base: the merge-base of the landed head with `upstream/main`, confirmed with `git merge-base` at claim time and recorded here.
- Gate steps:
  1. Confirm the head with `git ls-remote`; record it here. Apply the title correction to `changelog/unreleased/` fragment for SOLR-12608 on `solr-12608-submit` as one commit (author Nick Shanin, no trailers), and push it plainly to the fork.
  2. Changelog YAML parse of the corrected fragment at the landed head.
  3. Tidy on the landed tree; the tree must be clean afterward (restore `gradle/libs.versions.toml` if tidy touches it). If tidy changes anything else, stop and record it here.
  4. Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
  5. Focused run of `TestSolrQueryParser` with `-Ptests.seed=12608C0FFEE12608`; count from fresh JUnit XML.
  6. Module check: `:solr:core:check -x test`.
- On completion: this file gets the landed head, the gate results and a DONE mark. The gating host refreshes `receipts/SOLR-12608.md` with the landed head, the counts, the seed and the date. The review side then confirms the draft against the landed title; the fail-before sentence in the draft rests on `g12608-premise.log`, which is not on disk, and stays flagged for the main side per disagreement D-1 in the verdicts landing report (not this job's to settle).

## Step checklist

- [x] head confirmed by ls-remote and recorded here
- [x] title correction committed on solr-12608-submit and pushed; landed head recorded here
- [x] changelog YAML parse at the landed head
- [x] tidy clean on the landed tree
- [x] Error Prone compile passes
- [x] focused TestSolrQueryParser run counted from fresh JUnit XML, seed 12608C0FFEE12608
- [x] module check `:solr:core:check -x test` passes
- [x] receipt refreshed in receipts/SOLR-12608.md; job DONE
