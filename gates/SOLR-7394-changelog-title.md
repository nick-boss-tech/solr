# Gate job: SOLR-7394 changelog title (changelog-only check)

- Branch: solr-7394-recovery. Head: `a92a0d89b20` (pushed 2026-10-13, fast-forward from `9857d9ee802`).
- Change: `changelog/unreleased/SOLR-7394.yml`, title only. It now names the restore of the pre-recovery term, using the wording recorded in `material/solrcloud-round-1-answers.md` (line 265) and already applied to the live PR title. Decision record: `material/held-items-decisions-2026-10-13.md`.
- Intended host: any Linux host. Windows never claims build work.
- Status: DONE 2026-10-13T16:50Z by vm2 (gate backlog coordinator). Changelog check GREEN at a92a0d89b20 (GATE END 2026-10-13T16:49:30Z; logs/vm2/queue-g7394-changelog-title-gate-20261013T164930Z.out). Receipt: receipts/SOLR-7394.md, section 'Changelog check at a92a0d89b20'. Claim: claims/pool-vm2-gate-backlog-7394-changelog-title.md.

## Steps

1. Parse `changelog/unreleased/SOLR-7394.yml` at the head as YAML. The title is a single plain scalar with no colon or quote. Record the parse result.
2. `git diff 9857d9ee802 a92a0d89b20 --stat` must show only that changelog file.
3. No Gradle run is needed for a changelog-only change. Say so in the receipt; no code test is claimed at the new head.

## On green

- Refresh `receipts/SOLR-7394.md` so it names `a92a0d89b20` as the live tip, with the code proof left at `9857d9ee802` and labelled as such.
- Mark this job and its claim DONE.
