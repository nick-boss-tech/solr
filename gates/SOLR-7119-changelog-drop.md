# Gate job: SOLR-7119, drop the changelog fragment (branch packaging, then changelog lint)

- Branch: solr-7119-submit. Gated head: 4c652c93b99c0eed496d70a69043e8bcd9760e21 (live tip, verified by ls-remote 2026-10-14; matches receipts/SOLR-7119.md and the docs-shaped gate at that head).
- Status: DONE. Packaged head af336af5ab8aa03b95cc6ecf0fde565ef305ca12; lint rc=0; see receipts/SOLR-7119.md.
- Intended host: any capable Linux host (vm2 or vm1; VM3 is down per material/coordinator-handoff.md). Capability tag: `gate`.
- Windows does not claim, build, push or commit this job.

## Why

- Owner decision 14 in reports/build-docs-misc-round-1.md (SOLR-7119: docs only; drop the changelog, recommended) is adopted under the decision default (material/coordinator-handoff.md, "Deciding scope questions"). Its test-scope half (docs only, as written, recommended in reports/build-docs-misc-round-1-g4.md) is what the branch already is. The changelog half is the step below.
- The same convention was applied to SOLR-6430, whose fragment drop gated green. The ref-guide-only tickets SOLR-5821 and SOLR-16914 ship no fragment.
- The branch still carries `changelog/unreleased/SOLR-7119-interval-facet-ex-doc.yml` at 4c652c93. The draft pr-drafts/build-docs/SOLR-7119.md cites that fragment at the gated head, so the draft's Changelog line comes out when this job is DONE.

## Steps

1. Verify `git ls-remote origin refs/heads/solr-7119-submit` equals 4c652c93b99c0eed496d70a69043e8bcd9760e21. If it does not, stop and record the new tip; do not adopt it silently.
2. On a worktree of that head, delete only `changelog/unreleased/SOLR-7119-interval-facet-ex-doc.yml` in one packaging commit. Author Nick Shanin, no co-author trailer. No other file changes; `solr/solr-ref-guide/modules/query-guide/pages/faceting.adoc` stays as it is.
3. Run `./gradlew --no-daemon --console=plain logchangeLint` at the new head. Record the exit code and the validator result. Expect the fragment count to drop by one.
4. The documentation build is not rerun. The only change is the deleted fragment, and the page is byte-identical to 4c652c93, whose `buildLocalSite` result (69 tasks, exit 0, rendered page checked) is in receipts/SOLR-7119.md. Say so in the receipt.
5. Push fast-forward to solr-7119-submit (no force). Verify `git ls-remote` shows the new head.
6. Update receipts/SOLR-7119.md: the new packaged head, the lint result, and the note that the docs build is inherited from 4c652c93. Mark this job DONE and the claim DONE.

## On completion

- The reviewer re-reads pr-drafts/build-docs/SOLR-7119.md against the new head: remove the Changelog line, and confirm the Proof and the Limits still match the receipt.
- Nothing here opens, flips, posts, comments on or edits a PR.
