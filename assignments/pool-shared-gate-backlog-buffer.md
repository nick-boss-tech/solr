# Pool assignment: shared gate backlog buffer (either Linux host)

Capability tags: `gate`, `premise-run`, `bats`. Staffing: one job at a time per host. Claim path: claims/pool-vm2-gate-backlog-<ticket>.md (the established per-ticket claim files).

This file makes the buffer explicit: the round 2 gate jobs below are written up in gates/<ticket>.md and are unclaimed. Either Linux host (vm1 or vm2) may claim any of them under the WORKFLOW.md claim rules; the round 2 assignment named vm2 as the intended host, and the owner has since directed both Linux hosts to draw from the same backlog (2026-10-10). Round 1 jobs and claimed jobs are not part of this buffer.

Unclaimed at the time of writing: SOLR-10667 (gates/SOLR-10667.md), SOLR-12347, SOLR-11678, SOLR-12161, SOLR-6430, SOLR-7119, SOLR-11700, SOLR-17356, SOLR-16322, SOLR-17722. Check claims/ before starting: a ticket with a live claim is taken, whatever this list says.

Added 2026-10-10 (buffer top-up, first gates for branches whose premises already held on vm2 premise runs): SOLR-5262 (gates/SOLR-5262.md), SOLR-13705 (gates/SOLR-13705.md), SOLR-10390 (gates/SOLR-10390.md, capability tag `bats`). Each is a first full gate; the premise evidence is recorded in the job file and in the ticket's receipt. Several tickets in the list above have since been claimed or gated; claims/ governs.

Added 2026-10-11 (gate queue deepening; most carry an implementation step from the adopted answers before the gate): SOLR-6711 (gates/SOLR-6711.md, implementation plus first gate: shared lock and write order), SOLR-17708 (gates/SOLR-17708.md, changelog title correction plus top-up gate), SOLR-11479 (gates/SOLR-11479.md, recorded corrections plus first gate), SOLR-8628 (gates/SOLR-8628-premise.md, premise run only; a first gate follows only if the premise holds), SOLR-8430 (gates/SOLR-8430.md, capped limiter replacement plus re-gate), SOLR-5589 (gates/SOLR-5589.md, disable-rule fix plus first gate), SOLR-17612 (gates/SOLR-17612.md, multi-filter math fix plus re-gate), SOLR-4399 (gates/SOLR-4399.md, fail at build not init, plus re-gate), SOLR-1877 (gates/SOLR-1877.md, reload-close race fix plus re-gate), SOLR-10641 (gates/SOLR-10641.md, tidy fold-in plus first gate; passing pin, proof inconclusive by construction). Each job file carries its own steps and proof shape; claims/ governs.

Added 2026-10-11 (owner decision, the hard thing): SOLR-13097 (gates/SOLR-13097.md, full rebase onto current main plus the full gate at the rebased head; claimable by vm1 or vm2). This job is unlike the others in this list: the branch has a live PR with changes requested, the rebase rewrites its history, and the job file carries a force-with-lease rule pinned to the fork tip read at claim time. Read the job file and reports/solr-13097-conflict-analysis.md in full before claiming.

Added 2026-10-11 (queue restock, vm2 gate backlog coordinator; every jobless candidate in the receipts was either retired, parked, held, blocked on an owner call, or already gated): SOLR-3865 (gates/SOLR-3865.md, packaging plus first gate), SOLR-4754 (gates/SOLR-4754.md, packaging plus first gate, compile-level proof), SOLR-8554 (gates/SOLR-8554.md, premise run first, then first gate only if the premise holds). The queue order for vm2 is the list order above, skipping blocked jobs.

Rules: each job follows its gates/ file exactly (premise run first where the file requires one; a premise that does not hold is recorded as NO GATE by finding with run evidence). The gating host writes the ticket's receipt on green, per WORKFLOW.md. One gate per host at a time.

## Clarification, 2026-10-11: these jobs are claimable end to end

Added after vm2's queue monitor reported vm2 idle at 2026-10-11 18:31Z, 18:51Z and 19:00Z while eight jobs sat unclaimed here, read as belonging to other assignments, waiting on a branch fix or a decision, or outside the round order. That reading is the blocker this section removes; nothing else in this file or in the job files changes.

- The eight jobs: SOLR-10641, SOLR-13097, SOLR-17612, SOLR-1877, SOLR-4399, SOLR-5589, SOLR-6711, SOLR-8430.
- One claim covers the whole job, the implementation step and the gate together. The implementation step in each job file (the fix, the rebase, the tidy fold-in, the packaging commit) is part of the claim, not work another host or the main side does first. The claiming host writes the fix, pushes its own commits, and then runs the gate, in the one claim.
- No decision is outstanding on any of the eight. Each job's fix comes from an answers file already adopted under the decision default, and the job file cites the entry it implements. The coordinator authorization in material/coordinator-handoff.md covers implementing them: VM2 as gate backlog coordinator may implement changes identified in review through the full hardening gate and push its own patches, and vm1 holds the same pool rights under WORKFLOW.md.
- These jobs are not scoped to the round 1 or round 2 backlog assignments, and they do not belong to the category assignments whose answers produced their fixes. The shared buffer is a queue of its own for both Linux hosts; vm2 sequences and claims from it as gate backlog coordinator.
- SOLR-13097 is included on the same terms. The owner's decision (the full rebase, then the full gate) is recorded in the job file itself, so no separate go-ahead is needed before claiming. The force-with-lease rule in that job file, pinned to the fork tip read at claim time, is the safeguard and stays exactly as written.
- Claim and heartbeat mechanics are unchanged: claim first at claims/pool-vm2-gate-backlog-<ticket>.md, hourly heartbeats, the 2-hour staleness rule, one gate per host at a time.

Added 2026-10-11 (queue restock, vm2 gate backlog coordinator, after SOLR-3865 and SOLR-4754 were claimed): SOLR-10234 (gates/SOLR-10234.md, premise run, Lucene name check, packaging, first gate; the branch is a hypothetical reproduction) and SOLR-11939 (gates/SOLR-11939.md, rebase onto current main with force-with-lease pinned to the fork tip read at claim time, premise check, packaging, docs-shaped first gate; the branch is based on an older main than the standard merge-base). Both are claimable by vm1 or vm2; claims/ governs.

Added 2026-10-11 (gate queue supply, main side): gates/SOLR-5011-narrowing.md (implement the shared-schema analysis's recommended narrowing on the gated SOLR-5011 branch, with its proving test and a full gate; the SOLR-5011 draft unholds on green), gates/SOLR-17731.md (the adopted Core admin round 1 fixes, the owed ListAliasesAPITest run, re-gate), gates/SOLR-16437.md (message-only rewrite dropping the hypothetical-test commit body, force push, re-gate), gates/SOLR-17372-premise.md (premise run for the retire round's salvage verdict, at the seed the ticket itself reports; verdict CONFIRMED or NOT SUPPORTED), and gates/SOLR-15358-proof-record.md (re-run the proof leg and name the base run's failing tests in the receipt; record only, the head does not move). All five claimable by vm1 or vm2 end to end; claims/ governs as always. Correction to the 2026-10-10 list in this file: gates/SOLR-10390.md is DONE, GATE GREEN at the packaged head, so it is no longer supply.

Added 2026-10-11 (gate queue supply, second pass, main side): gates/SOLR-4502-title-fix.md (the CA-R1-01 changelog title correction on the gated branch, re-gate at the new head, plain push, draft link re-point), gates/SOLR-12916-title-fix.md (the CA-R1-34 title correction, same shape), gates/SOLR-9091-title-fix.md (the RB-R1-04 title correction, same shape), gates/SOLR-12085-title-fix.md (the adopted g2 finding 14 title correction, same shape), gates/SOLR-14199-record.md (record runs only: name the 37th test in TestSolrQueryParser's counted set from fresh JUnit XML, and record the SchemaVersionSpecificBehaviorTest run the draft's HOLD names; no branch commit), gates/SOLR-8009-record.md (record run only: reconcile the recorded 10 tests against the head file's 9 declared methods, naming the tenth or correcting the count), gates/SOLR-16267-def-check.md (record only: whether Lucene's DefFunction consults exists(), read in 9.12.3 and 10.4.0, verdict in the receipt), and gates/SOLR-15615-fallback-trace.md (investigation: trace the MLT fallback path end to end, with the authorization verdict recorded in the receipt either way). All eight claimable by vm1 or vm2 end to end; claims/ governs as always. Note on SOLR-8430: no new job and no amendment; gates/SOLR-8430.md already carries the RB-R1-03 fragment retitle as its implementation step 2 alongside the cap, under vm2's live claim, with the implementation pushed and the gate running.

Added 2026-10-12 (queue restock, vm2 gate backlog coordinator): SOLR-8576-alias-fix (gates/SOLR-8576-alias-fix.md, the alias assertion fix in the new SOLR-8576 test and its re-gate; posted by the main side and not previously listed here). Claimable by vm1 or vm2; claims/ governs. Restock note: SOLR-17731 and SOLR-16437 gated green on vm2 today and are DONE; SOLR-15003 already has a live-tip receipt, so it needs no job.

Added 2026-10-12 (queue restock, vm2 gate backlog coordinator): SOLR-16013 (gates/SOLR-16013.md, re-gate at the live tip ba26b70289, focused OverseerCloseOrderingTest; the gated head e1bd21fd11 is older) and SOLR-13136 (gates/SOLR-13136.md, consistency re-gate at the live tip 486b387755 with focused CreateShardCmdTest and CreateShardConstructionTest; the live PR is not touched). Both claimable by vm1 or vm2; claims/ governs.

## Restock, 2026-10-12 (vm2 gate backlog coordinator)

Added after the SOLR-16013 claim left one unclaimed job in the buffer. Each is a re-gate of a branch whose receipt names an older gated head, checked against the fork tip by ls-remote; none duplicates a gated head at its live tip.

- SOLR-13943 (gates/SOLR-13943.md): re-gate at ca443f6f68; the tip adds a changelog restore over cc155cf68e.
- SOLR-15805 (gates/SOLR-15805.md): re-gate at 6d78603d82; same tree as the receipt's 3432f950f0, re-run so the receipt names the live commit.

Not stocked: SOLR-10252 (docs only, held on the round 5 owner decision).

## Current supply, 2026-10-13 (main side census)

The lists above are the queue's history and are stale as a supply list: most jobs named there are DONE. The verified OPEN, unclaimed, claimable gate jobs at the 2026-10-13 pr-prepare tip are:

- gates/SOLR-16437-changelog-regate.md (full re-gate at `a23f770e4b9`, receipt refresh, draft re-point; amended 2026-10-13)
- gates/SOLR-11479-identity-carry.md (carry-over check at `6f739cfbbd38`)
- gates/SOLR-15035-base-run.md
- gates/SOLR-11288-base-run.md
- gates/SOLR-12651-history-scrub.md
- gates/SOLR-15674-history-scrub.md
- gates/SOLR-18391-changelog-wording.md
- gates/SOLR-5011-narrowing.md
- gates/SOLR-6014-searchtime-premise.md
- gates/SOLR-6430-fragment-drop.md
- gates/combinedquerycomponent-real-exit-premise.md
- gates/pr-drift-round-1-run-records.md (per-item claims allowed)

Also OPEN and assigned to vm2 by Nick (2026-10-13), claimed by vm2 before running: gates/SOLR-16437-nightly-run.md and gates/SOLR-17680-v2-two-router-test.md. The same census, with one-line descriptions, is in material/coordinator-handoff.md under "Build queue loaded for vm2, 2026-10-13 (main side)". Claims/ governs as always: a ticket with a live claim is taken, whatever any list says.

## Restock, 2026-10-13 (vm2 gate backlog coordinator, 10:39Z)

Added after SOLR-5011 went DONE and the buffer fell to two unclaimed jobs against a target of three. Each is a re-gate at a live tip with no green gate recorded there; the audit checked receipts against fork tips and excluded parked, blocked and claimed tickets.

- SOLR-18368-tip-regate (gates/SOLR-18368-tip-regate.md): the live tip a7ec9a1b65 drops the pin test the last green gate (1acaced80b2) covered; re-gate at the tip, record the count the tip actually gives.
- SOLR-17539-tip-regate (gates/SOLR-17539-tip-regate.md): docs-only commit f9d201a278 past the gated head; docs-shaped re-gate at the tip.

Audit candidates not stocked, with the reason: SOLR-15823 (stacked on a superseded PR head; needs a main-side decision), SOLR-10322 (changelog-title-only commit past the gated head; module not yet checked), SOLR-14913 (comment-only commit past the gated tree), SOLR-7267 (packaging removal past the gated tree), SOLR-11475 (history rewrite with the tree identical to the gated head; receipt refresh only, no gate). The next restock pass checks SOLR-10322 and SOLR-11475 first.

## Restock, 2026-10-13 (vm2 gate backlog coordinator, 10:58Z, second pass)

The audit candidates from the 10:39Z pass were checked against receipts and fork tips (ls-remote, same session). Stocked: SOLR-10322-tip-regate (gates/SOLR-10322-tip-regate.md, changelog-title commit 80ce9d7a3c8 past the gated head 0523a10639dd; docs-shaped module check) and SOLR-14913-tip-regate (gates/SOLR-14913-tip-regate.md, comment-only commit b80221f46d3 past the gated tree 51fe1087fe; top-up tidy, compile, focused class, module check). Not stocked, with the reason: SOLR-11475 (the receipt already names the new head 229947201fd7; the tree is identical to the gated tree 3223028c, so no gate is owed); SOLR-7267 (the tip removes only the SOLR-7267-TESTING.md handoff doc past the gated tree c3d8cb47d2; packaging removal, carried by the receipt); SOLR-15823 (stacked on a superseded PR head; main-side decision).

Unclaimed gate jobs in the buffer after this pass, claimable by vm1 or vm2: SOLR-18368-tip-regate (claimed by vm2 at 10:47Z, running), SOLR-17539-tip-regate, SOLR-10322-tip-regate, SOLR-14913-tip-regate. Target of 3 unclaimed met once SOLR-18368 is taken. Blocked, not in supply: SOLR-17680-v2-two-router-test (owner decision, see its "Blocked at claim" section).

## Restock, 2026-10-13 (vm2 gate backlog coordinator, 12:00Z)

Unclaimed after the 11:17Z claims by the other vm2 pass (SOLR-10322, SOLR-14913), the SOLR-17539 and SOLR-18368 completions, and the reclaim of SOLR-17680 v2 (settled by the main side in commit cf450d8324): the buffer had no unclaimed gate job. Stocked: SOLR-5011-title-fix (gates/SOLR-5011-title-fix.md, the changelog title correction the narrowing receipt owes, then a docs-shaped re-gate at the new head). Still short of the target of 3; the next pass audits receipts for further first, re-gate or top-up candidates.

Added 2026-10-13 (buffer top-up, main side; both join the current supply above): gates/SOLR-18280-nightly-record-run.md (record run only: TestReplicationHandler at the live tip `0da92abd9e96` with the nightly group on, so the live PR's run claims get a recorded count or named corrections) and gates/SOLR-18178-base-leg-run.md (base-leg record run: the receipt's focused set against merge-base production in a scratch tree, with the two branch-added configset test files reverted in that tree only). Both OPEN, unclaimed, claimable by vm1 or vm2 end to end; claims/ governs as always.

## Round 3 top-up, 2026-10-13 (main side, 15:30Z)

The unclaimed gate supply was short of the buffer target of 3, and two vm2 gate claims had stopped heartbeating (SOLR-12505 since 01:12Z, SOLR-14919 since 04:22Z). The order and the holds are in `assignments/pool-vm2-gate-backlog-round-3.md`:

1. SOLR-12505, proof leg only, per `gates/SOLR-12505-fetchstream-fix-amendment-2.md`.
2. SOLR-14919, re-run at `8f1f4ef91e`, per the main-side re-run decision in its gate file.
3. SOLR-17292, dated run of record, `gates/SOLR-17292-run-date.md` (new).

Holds stay out of the supply: SOLR-18280, SOLR-10667, SOLR-12347, SOLR-18532. Claims in `claims/` govern. Updated 2026-10-13: SOLR-16013 (`gates/SOLR-16013-interrupt-fix.md`) and SOLR-7394 (`gates/SOLR-7394-changelog-title.md`) are now open jobs in supply; SOLR-12998 is decided and is not a hold (see `material/held-items-decisions-2026-10-13.md`).

## Restock, 2026-10-13 (vm2 gate backlog coordinator, 16:55Z)

After the SOLR-16013 head failure (parked, with its fixture patch assigned in assignments/pool-vm2-16013-head-failure-fixture-patch.md) and the SOLR-7394 changelog check (DONE), the buffer had no unclaimed gate job. Stocked from existing job files that have no claim and no DONE line:

- SOLR-18010-replace-semantics-premise (gates/SOLR-18010-replace-semantics-premise.md): premise run, no push. CLAIMED by vm2 at 2b5ad801c9 (claims/pool-vm2-gate-backlog-18010-replace-semantics.md).
- SOLR-18541-score-only-exit-input (gates/SOLR-18541-score-only-exit-input.md): premise run on the merge-base, no push. Unclaimed. Overlap noted: the vm1 real-exit premise (claims/pool-vm1-combinedquerycomponent-real-exit-premise.md) answered related questions on the same seed; the claimant reads that record first and records any overlap in the job.

Not stocked, with the reason: the three review-r1 patch jobs (gates/SOLR-18010-review-r1-patch.md, SOLR-18539-review-r1-patch.md, SOLR-18541-review-r1-patch.md) are awaiting the owner-patch round 3 (claims/pool-new-prs-owner-patch-round-3.md, ACTIVE), which moves the SOLR-18541 and SOLR-18010 submit branches and posts its own gate jobs at the new heads. Gating the review-r1 heads now would gate superseded commits. queryComponent-real-exit-shape-premise is DONE by vm1 (claims/pool-vm1-combinedquerycomponent-real-exit-premise.md), so its job file is stale.

## Restock pass, 2026-10-13 (vm2 gate backlog coordinator, 2026-10-13T18:49Z)

Unclaimed gate jobs in the buffer: 0 against a target of 3. Audit: all 328 fork `solr-*-submit` tips (ls-remote) were checked against receipts/ and gates/, and each DONE gate head against its fork tip. Stocked: none. Considered and not stocked, with the reason:

- SOLR-18391 `solr-18391-submit` at 3000eeede7 (PR #4997 draft branch): tidy, compile and check recorded 2026-10-04, no focused tests, no gate at this head. A main-side call is needed on whether that branch is still wanted; the live SOLR-18391 gate is at 327ca7b722 (solr-18391-graceful-create-submit).
- Suggester branches 9227, 9968, 10937, 11844, 14171, 17215, 17393 and 9637: gated or held in the main-side ledger (test-receipts-63-branches.md), outside this buffer.
- SOLR-10252 (held); SOLR-15823 (main-side decision); SOLR-18541-review-r1-patch and SOLR-18010 G3 (waiting on the review patch round); SOLR-16013-interrupt-fix (waiting on the review patch and Nick's go-ahead).
- SOLR-18541-score-only-exit-input is claimed by vm1 (claims/pool-vm1-gate-18541-score-only-exit-input.md).

Closed on verification in this pass: SOLR-12998 (the 2026-10-10 decision cleared the proof-leg block; GREEN at 62a17a116b5) and queryComponent-real-exit-shape-premise (superseded by the vm1 real-exit run). The shortfall is recorded here and in reports/vm2-queue-monitor.md for the main side.

## Restock pass, 2026-10-13 19:57Z (vm2 gate backlog coordinator)

Unclaimed gate jobs in the buffer: 0 against a target of 3. Checked: every vm2 gate claim under claims/ (none live; SOLR-18010 review round 1 leg G3 DONE 19:52Z), SOLR-12505 (DONE, GREEN at 1a1875d95f), and every fork ref that no receipt names (ls-remote, 729 refs). The ungated submit tips are all held, parked, decided, or outside this buffer: SOLR-10252 (held), SOLR-16013 (parked, fixture patch waits for Nick's go-ahead), and the suggester branches 9227, 9637, 9968, 10937, 11844, 14171, 17215, 17393 (main-side ledger).

Stocked: none. Not stocked, with the reason:
- SOLR-18541-score-only-exit-input: vm1 holds claims/pool-vm1-gate-18541-score-only-exit-input.md, last heartbeat 17:10Z and not DONE. Not taken over: a gate takeover needs the log state read on vm1, which vm2 cannot read. vm1 to confirm or release.
- pr-drift round 1 records: parked for the main agent on items (b) and (c).

## Restock pass, 2026-10-13T20:03Z (vm2 gate backlog coordinator)

Unclaimed gate jobs in the buffer: 0 against a target of 3. Gate files re-read against the 2026-10-13 build queue: every job on that list is DONE in its gate file, so there is no open job to claim. Stocked: none. Held, not claimable by vm2: SOLR-18541-review-r1-patch and SOLR-18541-score-only-exit-input (vm1 claims at 17:10Z with no heartbeat since; the log is on vm1, so the takeover check cannot be made from vm2; vm1 to confirm or release); pr-drift round 1 records (parked for the main agent); SOLR-16013-interrupt-fix (push waits for Nick's go-ahead); SOLR-18010 review round 1 patch (review coordinator owns the test-premise patch).
