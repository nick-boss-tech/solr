# Assignment: SolrCloud openings slate preparation and missed-work settlement, round 1

- Posted: 2026-10-13 by the main side.
- Capability tags: review, draft. Any host with those tags may claim this under the WORKFLOW.md claim rules (claim file at `claims/pool-openings-slate-and-missed-work-round-1.md` before work).
- No build work. This assignment runs no gate, claims no gate job, and moves no branch. Its deliverables are one report and, where a draft is stale against its receipt, corrected draft files.
- Why this exists: the hourly queue check at 2026-10-13 13:13 MDT read both queues as below target and the local lane as idle. Section 1 is the census taken at the pr-prepare tip `42e383d09f` on 2026-10-13 at about 19:27Z. The queues are not empty; the low counts come from claim files whose status lines lag the work, from two large reviews that live on their own branches, and from claimed work whose heartbeats sit inside claim files rather than in the buffer list. Sections 2 and 3 are the work this assignment adds: the SolrCloud openings slate the earlier batches pointed at but nobody prepared, and the settlement checks for three tickets the batches left between them.

## 1. The queues are not empty (census pointer, 2026-10-13 about 19:27Z)

Counts from the files on pr-prepare at tip `42e383d09f`: 198 files under `assignments/`, 399 files under `claims/`, 136 files under `gates/`.

In-flight or freshly claimed work, named so no host reads the unclaimed counts as idleness:

- vm1 (main side host): one chained runner holds four claims pushed 2026-10-13T17:10Z: the SOLR-18541 review patch gate (gate GREEN at `a93f9f6a997e` recorded by the 12:13 MDT queue pass; the claim file still reads CLAIMED), the QueryComponent real-exit shape premise (first run stopped on a probe compile defect; fixture fixed and the run relaunched 2026-10-13T18:41Z), the SOLR-18010 permissions legs G1 and G2 (DONE at `2b5ad801c9e8`), and the SOLR-18541 score-only exit input (premise DONE, crash path documented). Claims: `claims/pool-vm1-gate-18541-review-r1-patch.md`, `claims/pool-vm1-querycomponent-real-exit-shape-premise.md`, `claims/pool-vm1-gate-18010-permissions-fail-before.md`, `claims/pool-vm1-gate-18541-score-only-exit-input.md`.
- vm2 (gate backlog coordinator): the SOLR-18539 review patch gate ended 2026-10-13T18:05:35Z at `fbf7610644d1` with both fail-before legs failing as the job designed; it stands GATE ENDED, awaiting a main-agent decision, not relaunched (claim `claims/pool-vm2-gate-backlog-18539-review-r1.md`, last entry 18:15Z). The SOLR-18010 corrected fail-before at the parent baseline `2b5ad801c9e8` is re-claimed (claim `claims/pool-vm2-gate-backlog-18010-review-r1.md`, 18:19Z). The SOLR-16013 fixture patch is claimed (claim `claims/pool-review-16013-head-failure-fixture-patch.md`, 17:40Z). The pr-drift round 1 run-records claim carries a heartbeat at 18:49Z with items (b), (c) and (d) still owing runs.
- Windows (review coordinator): the SOLR-18010 fail-before route patch is PREPARED at 2026-10-13T19:00Z (claim `claims/pool-review-18010-r1-fail-before-patch.md`); two routes are saved in `gates/SOLR-18010-fail-before-route1.patch` and `gates/SOLR-18010-fail-before-route2.patch`, and the push waits for the owner's go-ahead. Owner patch round 3 (claim heartbeat 14:00Z) and owner-direction round 4 (14:24Z) stand open in their claim files with their deliverables landed. The SolrCloud batch A claim (14:39Z) stands open in its file; its report landed.
- The two fresh-branch reviews are claimed and complete on their own branches, not on pr-prepare: `assignment-search-components-review` carries its claim commit `5e4495bde6` and its verdict commit `289d5d3ad5` (verdicts for the 48 drafts, wording fixes, reports and summary on that branch); `assignment-query-parsing-review` carries its claim commit `9ecdc53f8a` and its verdict commit `03fbf23793` (verdicts for the 18 drafts, on that branch). Any host looking only at pr-prepare sees neither.
- Gate files: 124 of the 136 files under `gates/` carry DONE as a recorded status. Of the rest: SOLR-18541 review patch and score-only files still read OPEN in the gate file while vm1's claims above hold them; SOLR-10667 and SOLR-12347 still carry their 2026-10-11 BLOCKED headers while the vm1 retry verification of 2026-10-13 records both premises DONE and both branches gated green; SOLR-16013 and its interrupt-fix job stand FAILED with the fixture patch claimed above; the two SOLR-12505 amendment files are direction records for a gate that has since recorded GREEN at `1a1875d95f`.

The buffer targets (at least 2 unclaimed review or draft assignments, at least 3 unclaimed gate jobs) are a supply rule, not an idleness test. This assignment and the G3 job posted with it (`gates/SOLR-18010-review-r1-g3.md`) restock both queues with real work.

## 2. SolrCloud openings slate preparation

The SolrCloud READY review batches returned four tickets as ready to open, and a fifth has since cleared its gate. Prepare the owner's approval slate; the openings themselves stay with the owner.

Tickets and starting state (verify each against the live fork tip by ls-remote and against `receipts/<TICKET>.md`; the tips below were read by ls-remote on 2026-10-13 at about 19:27Z and are a starting point, not the verification):

- SOLR-17680: SATISFIED, READY TO OPEN (batch B report; decisions round 5). Live tip `52b641ca1a263589afa4f1ef0ec7b68f53c84565`. The earlier message-only re-gate recorded GREEN at `ce59bbe112d` on a tree identical to its gated head; reconcile which head the opening uses and say so in the slate.
- SOLR-17733: SATISFIED, READY TO OPEN (batch B report). Live tip `636196b7954af6b34efad54bd0cda2330ef8c7a4`.
- SOLR-9155: SATISFIED, READY TO OPEN (batch B report). Live tip `29bf373828994c622ac478a968d6cff4f4c45fa5`. The receipt records GATE GREEN at `dbd88c84ff` and notes the draft is owed one Choice (the interrupt surfaces as a declared InterruptedException rather than a SolrException with the flag restored) and Limits lines (the ticket's flow-control bullet is unaddressed; the reproduction pre-interrupts before a happy-path call). The live tip past the gated head owes its own gate state; establish what covers `29bf37382899` before the slate calls this ticket ready, and name the covering record or the gap.
- SOLR-14919: NOT SATISFIED in batch B pending its gate; the re-gate at `8f1f4ef91e798f736eb91c9625223a654b59794a` is now GATE GREEN on the pr-prepare records (receipt `receipts/SOLR-14919.md`, focused count recorded at that head). Live tip matches at `8f1f4ef91e`. Treat the batch B hold as cleared by the gate and prepare it with the other three, flagging anything else batch B held against it.

For each of the four:

1. Read the draft at `pr-drafts/solrcloud/SOLR-<ticket>.md` against the live fork tip and the receipt. Every head SHA, count, date and claim in the draft must match the receipt at the live tip; list each mismatch and correct the draft where the receipt settles it.
2. List every packaging step still owed before opening: TESTING.md removal if the branch still carries one, changelog fragment title check (the title states the change accurately, and the fragment parses as YAML), the changelog line link at the head SHA, body refresh against the PR formula (sections, bold one-line summary opening each section, plain language, no internal process vocabulary), and any Choice or Limits lines the receipt or the batch reports say the draft owes.
3. Do the draft-side corrections in this assignment (draft files only). Branch-side packaging (TESTING.md removal, changelog commits) is listed as owed, not done here; it belongs to the opening pass on the main side.

Deliverable: `reports/solrcloud-openings-slate-round-1.md`, one section per ticket, each ending with a single line: READY FOR OWNER APPROVAL, or NOT READY with the owed steps named. The report is the slate the owner approves from; it names the exact head each PR would open at.

## 3. Settlement verification: SOLR-15035, SOLR-15106, SOLR-15386

- SOLR-15035: its premise runs are DONE at `12d7491e82cd660ad55063cb85633b7e05e1918c` (base run failed as the premise expects; the banked patch alone passed; claim `claims/pool-vm2-gate-backlog-15035.md` is DONE; live tip matches at `12d7491e82c`). State precisely what its opening still waits on: read the SOLR-15035 draft, its receipt, and the HOLD assignment's settlement record (`assignments/pool-solrcloud-hold-first-rung-round-1.md` and its report), and name each remaining step. Note the dependency that runs the other way: SOLR-11479 is SATISFIED and READY TO OPEN after SOLR-15035 (batch A report), so the slate report says what 15035's settlement unblocks.
- SOLR-15106 (live tip `40b7e5d0efa70d61b74b9fa9ac04beb1ed397637`) and SOLR-15386 (live tip `ca8cb61ee957ed728034a2e6c90532e6082ef3ff`): both stand READY in the decisions round 5 record and sat in neither READY review batch, so no batch report covers them. Verify each draft under `pr-drafts/solrcloud/` against its receipt and the live tip, the same checks as section 2. Close each into the slate report with its own section (READY FOR OWNER APPROVAL or NOT READY with the gap named), or state precisely what is missing for it to join the slate.

## 4. Bounds

- No PR openings, no ready flips, no branch moves, no pushes to any `solr-*-submit` branch, no comment posts. Openings stay with the owner; this assignment prepares the slate he approves.
- Draft corrections under `pr-drafts/` and the slate report under `reports/` are the only edits. If a verification turns up a defect in a branch rather than in its draft, record it in the slate report as an owed step; do not patch the branch under this assignment.
- Follow the PR formula and the plain-language rule for any draft text touched: no internal process vocabulary, no em dashes, and authored text never uses "we" for the owner plus agent.
- Mark the claim DONE in the same push sequence as the slate report, naming the deliverable paths.
