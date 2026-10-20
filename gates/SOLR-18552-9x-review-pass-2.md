# Gate job: SOLR-18552 9.x review pass 2 (launcher default, 9.x line)

Status: NEARLY (partial regate on vm5; the launcher and bats legs for a 9.x distribution are for vm1)

- Branch: solr-18552-sharedarena-9x. Head: `cd7cc695fb09fc8adba211992a85b5f76380b110` (ls-remote 2026-10-20T14:18Z, unchanged since pass 1).
- Prior gated head: none on this line (pass 1 partial, same head).
- Intended host: vm5 for static legs; vm1 for the 9.x launcher and bats legs.
- Diff scope against `f4f2507177`: `solr/bin/solr` (+1), `pr-draft/SOLR-18552.md` (+22, draft).

## Checks

- [x] head confirmed by ls-remote, unchanged
- [x] diff scope (one launcher line and a draft file)
- [x] `bash -n solr/bin/solr` (pass 1, same head)
- [x] override order, bash (read at the line)
- [x] draft's gate evidence read against the tip: it is an 11.0 SNAPSHOT run, not 9.x (finding recorded)
- [ ] changelog parse: no file on this line (finding recorded, owner)
- [ ] `solr.cmd` default: not on this line (owner question posted)
- [ ] 9.x launcher behavior and bats: NOT RUN on vm5; for vm1 on a 9.x distribution

## Deliverable

- Pass 2 result in `reports/newdev-critblock-review-round-1/solr-18552-sharedarena-9x.md`.
