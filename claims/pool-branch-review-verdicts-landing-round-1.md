# Claim: land the search-components and query-parsing review verdicts, round 1

Status: CLOSED 2026-10-17 (batch close approved by Nick; no heartbeat within 2 hours; listed in reports/claims-stale-batch-close-2026-10-17.md)

- Claimed by: windows (review coordinator), Claude Code on this host, working for Nick Shanin
- Capability tags: review (writes the patches and drafts)
- Assignment: `assignments/pool-branch-review-verdicts-landing-round-1.md`. Standing authority: `material/coordinator-handoff.md`, "Standing authority confirmed 2026-10-13", and the owner's direction on 2026-10-13 to allow pushes to fork branches and edits to live PRs.
- Verdict tips: search components `289d5d3ad5fe34b21852a4187657b03796bbc5e7`; query parsing `03fbf23793f0ab23d46dce01709a3afec01a2fe6` (both checked by ls-remote on 2026-10-13).
- Scope taken this round: step 1 (land the ten verdict reports byte-identical, with provenance) and step 3 for the query-parsing NOT SATISFIED drafts, wording settlements to the drafts only. Live body edits and fork pushes are not made in this round; they follow once each draft's wording is settled and re-read.
- Still to take in later rounds: the search-components verdict drafts, the query-parsing step 4 ready lists, and the patch-loop items that need a gate.
- Limits: no PR opened, closed or flipped; no comment posted; no build, test or Gradle on this host.
- Heartbeat: 2026-10-13T23:30Z (claim taken).
- Status: PARTLY DONE 2026-10-13T23:50Z. Step 1 and step 2 done: the ten verdict reports landed byte-identical, and `reports/branch-review-verdicts-landing-round-1.md` carries the provenance, the accounting, the flags and one line per draft. Step 3 started for the three query-parsing OPEN (gate) drafts (SOLR-6014, SOLR-12608, SOLR-17796), with holds recorded. Not yet worked: the 23 review-tip edits, the search-components OPEN (gate) item, and step 4 (ready lists). Live body edits and fork pushes not made; they wait on draft review.
