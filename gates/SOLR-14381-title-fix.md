# Gate job: SOLR-14381 changelog title correction (title-only commit)

- Branch: solr-14381-submit. Head: a28b3f672cb4daa7cc32833082b0f499f41e3a85 (live tip, verified by `git ls-remote origin refs/heads/solr-14381-submit` on 2026-10-14; the gated head in receipts/SOLR-14381.md is the same SHA).
- Status: DONE. Title commit 7caf8d33d3bf29d38e3f16c38a01282982329e90 pushed to solr-14381-submit, gate green (receipts/SOLR-14381.md). Posted 2026-10-14 by the Windows review agent from the census follow-up round for search components (assignments/pool-census-followup-search-components-wording-round-1.md, item SOLR-14381 step S4). No claim is filed by the poster; the claiming host files its claim under the WORKFLOW.md claim rules before it starts.
- Intended host: vm2 (Linux gate lane). Windows does not run this job and does not claim build work.
- Capability tags: gate.
- Job type: branch-side changelog title correction on a gated branch. One title-only commit on top of the head, then the gate evidence check at the new head.
- Draft status: pr-drafts/search-components/SOLR-14381.md is HELD on this job (step S4). It is not SATISFIED. Its other steps (bold summaries on A choice, Proof and Limits) were applied on 2026-10-14.
- Why: the title in changelog/unreleased/SOLR-14381.yml is cut off mid-sentence. It ends at "...SolrJ GroupCommand getMatches and getNGroups now return long and Long", runs about 381 characters, and has no closing sentence. The draft claims the whole change: grouping counts are long values, shards send an Integer whenever a count fits, and the SolrJ getters change return type.
- The fix: in changelog/unreleased/SOLR-14381.yml at the head above, replace the text of the `title:` line with exactly the following (one line, plain scalar, no quotes needed because it has no colon followed by a space and no space followed by #):

  Distributed grouped counts are now long values, so grouped searches over more than Integer.MAX_VALUE matching documents no longer overflow. Shards send an Integer whenever a count fits, so an older coordinator keeps working for those counts. SolrJ GroupCommand getMatches() now returns long and getNGroups() returns Long.

  Keep the `type: fixed`, the authors block and the links block unchanged.
- Commit: one new changelog-only commit on top of a28b3f672cb4. Do not amend or rewrite history. Push plainly (fast-forward) to solr-14381-submit after the checks below are green.
- Delta check: `git diff --stat a28b3f672cb4daa7cc32833082b0f499f41e3a85 HEAD` must list only `changelog/unreleased/SOLR-14381.yml`. Stop and record here if anything else differs.
- Proof shape: none owed for code. No Java file changes. The evidence in receipts/SOLR-14381.md at a28b3f672cb4 carries over to the title-only commit. If the delta touches anything else, the gate in receipts/SOLR-14381.md is owed again.
- Gate steps: changelog YAML parse of the edited fragment with a real YAML parser (the `title` value must parse to the replacement text exactly, with no colon or quoting error); changelog lint; tidy with a clean tree afterward (restore gradle/libs.versions.toml if touched); module check `:solr:core:check -x test`, as in the SOLR-5011 title-correction precedent.
- On green: push plainly to solr-14381-submit and verify by `git ls-remote`. Refresh receipts/SOLR-14381.md with a title-correction section that names the new head, the steps, and the replacement title, and state that the code is unchanged from a28b3f672cb4. Mark this job DONE and the claim DONE. The review side then re-points the Changelog link in pr-drafts/search-components/SOLR-14381.md to the new head and re-reads the draft before marking it SATISFIED.
- On failure: record the failing step and message in this file. Do not relaunch unchanged. A gate that ran tests and failed goes to the lane coordinator first.

## Step checklist

- [x] title line replaced in changelog/unreleased/SOLR-14381.yml with the replacement text; delta against a28b3f672cb4 is that fragment only
- [x] changelog YAML parse with a real parser
- [x] changelog lint
- [x] tidy on a clean tree
- [x] module check :solr:core:check -x test
- [x] push fast-forward to solr-14381-submit, verified by ls-remote
- [x] receipts/SOLR-14381.md refreshed; this job DONE; claim DONE
