# Gate job: SOLR-18552 9.x review pass 1 (launcher default, 9.x line)

Status: NEARLY (partial regate on vm5; the launcher and bats legs are for vm1)

- Branch: solr-18552-sharedarena-9x. Head: `cd7cc695fb09fc8adba211992a85b5f76380b110` (ls-remote 2026-10-20T14:15Z).
- Prior gated head: none on this line (the draft cites an 11.0 SNAPSHOT launcher gate, not a head on this branch). Base: upstream branch_9 `f4f2507177`.
- Intended host: vm5 for syntax and diff checks; vm1 for the launcher and bats legs.
- Capability tags: gate (vm5); bats (vm1).
- Diff scope against `f4f2507177`: `solr/bin/solr` (+1), `pr-draft/SOLR-18552.md` (+22, draft).

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope (one launcher line and a draft file)
- [ ] changelog parse: NO changelog file on this line; finding recorded (owner)
- [x] `bash -n solr/bin/solr` at the head
- [x] `git diff --check` clean
- [x] override order: `SCRIPT_SOLR_OPTS` before `SOLR_OPTS` on the java command (read at the line)
- [ ] `solr.cmd` default: not present on this line; owner question (scope)
- [ ] launcher behavior and bats: NOT RUN on vm5; named for vm1

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-18552-sharedarena-9x.md`.
