# Gate job: SOLR-18552 review pass 1 (launcher MMap permit default, main line)

Status: NEARLY (partial regate on vm5; the bats launcher leg is handed to vm1 as the named piece)

- Branch: solr-18552-submit. Head: `cb32d0d9661`-full `cb32d0d966159edb237d581318af9bf96071c0d1` (ls-remote 2026-10-20T14:08Z, unchanged at claim).
- Prior gated head: `a5406c9fe531161df7b424b2a55143489b95166e` (receipt, PR #5181 earlier head). Merge-base with upstream branch_9x: `3511867c3e`.
- Intended host: vm5 for syntax and changelog legs; vm1 for the bats launcher leg (only vm1 carries the bats tag, hosts/vm1.md).
- Capability tags: gate (vm5); bats (vm1).
- Module: none (shell and cmd only). Focused class: none. Seed: none.
- Diff scope against `3511867c3e`: `solr/bin/solr`, `solr/bin/solr.cmd`, `changelog/unreleased/SOLR-18552-mmap-arena-permits.yml`. Nothing else.
- Why: pass 1 of the review round for this unit.

## Checks

- [x] head confirmed by ls-remote, unchanged since the claim
- [x] diff scope confirmed (three files)
- [x] step 0 changelog YAML parse: keys authors, links, title, type; type `changed` (uv run --with pyyaml)
- [ ] step 1 tidy: not applicable (no Java or Gradle)
- [x] shell syntax: `bash -n solr/bin/solr` ok at the head
- [ ] launcher behavior: the node started with the branch's bin/solr has the property on its JVM; the control launcher does not. NOT RUN on vm5 (needs a distribution build and the bats tag). Named piece for vm1.
- [ ] bats launcher tests: NOT RUN on vm5 (bats tag is vm1 only). Named piece for vm1.

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-18552-submit.md`.
- Receipt section in `receipts/SOLR-18552.md`, top section, marked partial.
