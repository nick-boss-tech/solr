# Gate job: SOLR-18552 review pass 3 (launcher MMap permit default, main line)

Status: NEARLY (partial regate on vm5; the launcher and bats leg is with vm1 as gates/SOLR-18552-launcher-bats.md)

- Branch: solr-18552-submit. Head: `cb32d0d966159edb237d581318af9bf96071c0d1` (ls-remote 2026-10-20T14:15Z, unchanged since pass 1).
- Prior gated head: `cb32d0d966159edb237d581318af9bf96071c0d1` (passes 1 and 2, partial). Earlier light gate by vm1 at `a5406c9fe531161df7b424b2a55143489b95166e` (claims/pool-vm1-18552-light-gate.md).
- Diff scope against `3511867c3e`: `solr/bin/solr`, `solr/bin/solr.cmd`, `changelog/unreleased/SOLR-18552-mmap-arena-permits.yml`.

## Checks

- [x] head confirmed by ls-remote, unchanged across passes
- [x] diff scope (three files)
- [x] changelog YAML parse (same head)
- [x] `bash -n solr/bin/solr` (same head)
- [x] override order, bash and cmd (pass 2)
- [x] property name and default verified by vm1 on a5406c9fe5 against the lucene-core 9.12.3 jar (earlier head, cited)
- [ ] launcher behavior on the head: NOT RUN on vm5; named for vm1
- [ ] bats launcher tests on the head: NOT RUN on vm5; named for vm1

## Deliverable

- Pass 3 result in `reports/newdev-critblock-review-round-1/solr-18552-submit.md`.
- Receipt section in `receipts/SOLR-18552.md`, pass 3, partial.
