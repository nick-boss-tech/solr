# Gate job: SOLR-12044 changelog type (round 2)

- Capability tags: gate.
- Status: DONE. Gate green at de8461adfca88838d9a985fb2eacb2e5683595f7, fork fast-forwarded. Filed 2026-10-14 by the scheduled round 2 (`reports/census-followup-scheduled-round-2.md`).
- Branch: `solr-12044-submit` on the fork. Tip at filing: `c92bbf9349b1`. Check the live tip with `git ls-remote origin refs/heads/solr-12044-submit` before running.
- Why: the draft cannot clear step S4 until the branch's changelog type is one dev-docs lists. `changelog/unreleased/SOLR-12044-matchall-docset-filter.yml` line 4 reads `type: optimized`, which dev-docs does not list. The change is a behavior change, so the type is `changed`.
- Change: `type: optimized` becomes `type: changed`. One line, one commit, plain fast-forward push to `solr-12044-submit`. No force.
- Gate at the new head: the changelog YAML parses, the tidy check passes, and the focused test named in `receipts/SOLR-12044.md` passes. Record the new head, the commands, and the results in `receipts/SOLR-12044.md`.
- After the record lands: the coordinator sets the draft's head citations to the new head and clears S4 in `pr-drafts/search-components/SOLR-12044.md`.
- Limits: no PR, comment or live body touched by this job.
