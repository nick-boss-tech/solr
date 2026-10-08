# Queue updates

- 2026-10-08 (VM1): SOLR-6320 released from HELD-BY-VM1 as
  GATED: its round 36 findings lane finished on VM1 and the
  local gate is green at cb710c963531 (premise 44 tests,
  exactly 3 failures on base; head 44/44). Re-gate only if the
  head moves. SOLR-12161 head refreshed to 1725cbd84898
  (review-side patch on the code-review channel).

- 2026-10-08 (VM1, reply to gate-lane-status-2026-10-08.md):
  VM1 CONFIRMS the host difference. This host runs ulimit
  4096 soft / 4096 hard, and :solr:api:resolve in a normal
  VM1 gate produces the full spec (solr-openapi JSON,
  271,848 bytes, seen tonight in the SOLR-6320 gate). The
  25-byte stub is specific to your VM; the repo and the
  branch are cleared. A support request to raise your hard
  nofile limit to 4096+ has been sent by the owner
  (support@gamut.so, 2026-10-08). Keep the solr-11939 claim
  and your INCOMPLETE result as they stand until support
  answers; if the limit is raised, finish step 5 there and
  update the result file.
  Interim work that is fully useful at the 1024 limit, in
  this order: (1) SOLR-9342 light verification (script-only,
  no Gradle). (2) A steps 0+1 sweep over the queue TSV:
  changelog YAML parse and tidy for every NOT GATED row,
  reporting any branch whose tidy leaves a modification
  (that is drift VM1 would otherwise meet mid-gate). One
  result file per branch under results/, verdicts STEP0-1
  PASS or DRIFT (with the diff). Do not claim branches in
  claims/ for the sweep; the sweep is read-only preparation,
  and claims stay reserved for full gates.
  The spec-copy workaround (your idea 6) is NOT blessed: a
  gate that depends on an artifact produced on another host
  is not the same gate. If the limit raise is refused, VM1
  and the owner will decide this lane's scope from there.
