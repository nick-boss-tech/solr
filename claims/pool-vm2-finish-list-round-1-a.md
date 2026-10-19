# Claim: pool-vm2-finish-list-round-1 (takeover of section A, B and C record)

- Claimant host: vm2. Takes over the stale claim `claims/pool-vm2-finish-list-round-1-a.md` under WORKFLOW.md rule 4 (the prior claim was STALE since 2026-10-17). Assignment: `assignments/pool-vm2-finish-list-round-1.md`.
- Started: 2026-10-19 (UTC), recorded at fork tip `2e56ce9990` for SOLR-8628 and `74532d0f76` for SOLR-5394.

## Section A: SOLR-8628, packaged head. CLOSED, no push this round

- Step 1 starting point differed from the assignment. `solr-8628-submit` was already at the packaged head `2e56ce9990b008c00d2d07b2498cf28cdc101a06`, pushed by vm2 on 2026-10-14 (`gates/SOLR-8628-packaging.md` Push section), with parent `5b606dc779d0` and a diff of only `SOLR-8628-TESTING.md` (41 deletions). Verified by ls-remote this round.
- Step 2: already done (fork tip is the packaged head). No push was made.
- Step 3: `pr-prepare` `pr-drafts/core-admin/SOLR-8628.md` already cites `2e56ce9990`; no `5b606dc779` citation remains.
- Step 4: `gates/SOLR-8628-packaging.md` is DONE (vm5 verification, 2026-10-18T15:55Z). Its claim `claims/pool-vm5-gate-backlog-8628-packaging.md` is the closing record.

## Section B: SOLR-5394, push gated head. STOPPED, needs a main-side decision

- `gates/SOLR-5394-push-gated-head.md` is not on `origin/coordination`, so the job named in the assignment does not exist on the branch.
- The fork tip of `solr-5394-submit` is `74532d0f76a6d4`. It is not an ancestor of the gated head `6e4c302c418be91b71305a1fe2f718e96295835f`, so the push would need a force. Not done.

## Section C: SOLR-17055, proof rerun and two follow-up tests. STOPPED, needs a main-side decision

- `gates/SOLR-17055-sort-path-proof-rerun.md` is not on `origin/coordination`. Only `gates/SOLR-17055-sort-path.md` exists, and it names the rerun as a separate job that is not filed.
- Fix commit `2fe0986158391908ac89564090eb97e245788023` exists locally; fork tip `solr-17055-submit` is `2fe0986158391908...`, so the head is already at the fix commit. No gate was run, because the job file is missing.

## Status

Section A closed. Sections B and C stopped for the missing job files and the non-fast-forward push. No force push, no PR, no JIRA or GitHub post.
