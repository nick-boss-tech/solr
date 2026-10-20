# Gate job: SOLR-18552 review pass 2 (launcher MMap permit default, main line)

Status: NEARLY (partial regate on vm5; the bats launcher leg is with vm1 as gates/SOLR-18552-launcher-bats.md)

- Branch: solr-18552-submit. Head: `cb32d0d966159edb237d581318af9bf96071c0d1` (ls-remote 2026-10-20T14:12Z, unchanged since pass 1).
- Prior gated head: `cb32d0d966159edb237d581318af9bf96071c0d1` (pass 1 partial). Merge-base with upstream branch_9x: `3511867c3e`.
- Intended host: vm5 for the static legs; vm1 for the bats launcher leg.
- Capability tags: gate (vm5); bats (vm1).
- Diff scope against `3511867c3e`: `solr/bin/solr`, `solr/bin/solr.cmd`, `changelog/unreleased/SOLR-18552-mmap-arena-permits.yml`.

## Checks

- [x] head confirmed by ls-remote, unchanged
- [x] diff scope (three files)
- [x] changelog YAML parse (pass 1, same head)
- [x] `bash -n solr/bin/solr` (pass 1, same head)
- [x] override order: bash and cmd both place `SCRIPT_SOLR_OPTS` before `SOLR_OPTS`, so a user `-D` wins (read from the head)
- [x] cmd quoting matches the file's existing `set "...%VAR% ..."` form (read from the head)
- [ ] launcher behavior and bats: NOT RUN on vm5. Named piece for vm1.

## Deliverable

- Pass 2 result in `reports/newdev-critblock-review-round-1/solr-18552-submit.md`.
- Receipt section in `receipts/SOLR-18552.md`, pass 2, partial.
