# Stand-down: UBI context gate on the listener scaffold

Status: HANDED (record-only; old gate withdrawn, replacement is an implementation-and-gate assignment)

- Source: Nick's follow-up direction to put the UBI recording fix on PR 2452, with permission to rebuild the UBI changes on main if needed, instead of offering the PR 5238 scaffold.
- This record supersedes the OPEN execution request in `gates/pr-5238-ubi-context-round-1.md`. Do not claim or start that old gate at `09343928e7d2ba5cb5e7def1d0a550337b67a7d2`.
- Replacement: `assignments/pr-2452-ubi-context-rework-and-gate-round-1.md`. Its assignee must produce a new exact-head gate after reworking the fix onto the real UBI feature.
- The board snapshot inspected was `b4234669ee2e86825d64535c78d1c1e277b7deda`; it contained the completed Windows implementation claim/report and the old OPEN job, with no Linux UBI-context claim or receipt found. This is not a cancellation of the earlier, already green test-only UBI contract proof at `c2c387064a83def34cece4163eccded28318caeb`.
- Preserve `nick-boss-tech/solr:solr-11994-ubi-context-submit` at `09343928e7d2ba5cb5e7def1d0a550337b67a7d2` as source material. Do not delete, force-push, rewrite, or run the old scaffold solely to satisfy its retired job.
- If a host has already begun an unrecorded old run, preserve its logs and report its disposition, do not launch a duplicate, and stop at a safe boundary. Any old result remains evidence for that scaffold only; it cannot certify a new PR 2452-based candidate.
- The original gate, claim, and report remain unchanged under the add-only workflow. A generated OPEN-WORK index may lag this withdrawal; this explicit superseding direction governs.
