# Claim: SOLR-18214 implementation, round 2 (owner decision row 2, option A)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: CLAIMED
- Supersedes: `claims/pool-decisions-impl-SOLR-18214.md` (DONE, stopped at verification). That claim is terminal and is not edited.
- Job or assignment: `assignments/pool-vm3-owner-decisions-round-1.md` row 2 (answer A), and `pool/decision-map.md` standing answer on "a decision is needed".
- Slice: the whole ticket, on a new branch `solr-18214-empty-configset` from c8ab71430f.
- Capability tags used: implementation, gate.
- Started: 2026-10-20T04:43:43Z (UTC).
- Idempotency key: SOLR-18214-r2@c8ab71430f.

Heartbeat: 2026-10-20T04:43:43Z

## Decision recorded before any run (ladder)

- The verification failure is not a question for the owner. The map says "cost is effort, do the hard thing"; the stop clause in row 2 was written for a failed verification, and this claim does the work it asks for.
- Rung 3 (stated intent): the ticket says the base is "truly optional" and that from-scratch creation is blocked. Row 2 answer A: an omitted base creates an empty configset; v1 keeps defaulting to `_default`. The intent is that a configset created with no base can hold a core.
- Rung 1 (code): the probe shows an uploaded empty configset has no `solrconfig.xml`, so no core loads (claim above). `CloneConfigSet` and `ConfigSetCmds.createConfigSet` require a base today.
- Rung 4 (choice): the empty configset is kept, with the minimum files a core needs added by the server (`solrconfig.xml` and a managed schema). Their contents are fixed by the probe in this claim, not guessed. v1 is unchanged. Option C (reject) is not taken: it contradicts answer A.
- The probe result and the chosen file set will be recorded here before the gate runs.

## Probe, round 2 (started 2026-10-20T05:31:06Z)

- Probe (uncommitted, in worktree /workspace/worktrees/solr-18214 on solr-18214-empty-configset from c8ab71430f): `EmptyConfigSetProbeTest`, one node. It uploads a configset holding only a generated minimal `solrconfig.xml` (ClassicIndexSchemaFactory, DirectUpdateHandler2 with updateLog, `/select` and `/update` handlers) and a minimal `schema.xml` (`id`, `_version_`, uniqueKey). It creates a collection from it, indexes one document, commits, and asserts one hit.
- Script: /workspace/tools/probe-18214.sh. Seed 18214A001. Logs: gates/logs/vm3/solr-18214-probe-minimal/ (test.log, result-test.txt, done).
- Outcome decides the next step: pass means the generated files are the minimum that loads a core, and the implementation adds them to the server-side create path. Fail means the evidence goes here and the decision goes back to the ladder.

## Probe result and implementation (recorded 2026-10-20T05:52:57Z)

- Probe PASSED: test=0, `EmptyConfigSetProbeTest` 1 test, 0 failures (seed 18214A001, gates/logs/vm3/solr-18214-probe-minimal/). A configset holding only a generated minimal `solrconfig.xml` (ClassicIndexSchemaFactory, DirectUpdateHandler2 with updateLog, /select and /update) and `schema.xml` (id, _version_, uniqueKey) creates a collection, indexes, commits, and returns one hit.
- Rung used: 4 (choice between defensible behaviors, decided from rung 3 intent and the probe). The generated files are the minimum that loads a core. The empty configset of answer A is kept as the base; v1 is unchanged.
- Implementation on `solr-18214-empty-configset` from c8ab71430f (uncommitted at this record; committed after gate): `CloneConfigsetRequestBody.baseConfigSet` drops the `_default` default; `CloneConfigSet` passes `minimalConfigSet=true` when the base is omitted and checks the base only when given; `ConfigSetCmds.createConfigSet` uploads the generated files instead of copying a base and skips base-only checks and property merge for the minimal path; new test `TestCloneConfigSetWithoutBase` (v2 POST /configsets with name only, collection created and queried); changelog `changelog/unreleased/SOLR-18214.yml`.
- Gate: /workspace/tools/gate-solr-18214.sh, logs gates/logs/vm3/solr-18214/ (step 0 changelog, tidy, compile with Error Prone, focused test `TestCloneConfigSetWithoutBase` and `TestConfigSetsAPI` seed 18214A002, check). Receipt to follow in receipts/SOLR-18214.md.
