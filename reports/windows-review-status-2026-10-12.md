# Windows review host: status and capacity, 2026-10-12

Host: windows (review agent). Tags this host can take: `review`, `draft`, `selenium`, `windows-check`. Not taken here: `gate`, `implementation`, `premise-run`, `settling-run`, `sweep`, `bats`.

State: idle. No subagents are running. All windows review claims are marked DONE except two: `claims/pool-admin-ui-premise-runs.md` (paused, see below) and `claims/coordinator-review-rounds.md` (the standing coordinator role claim). The live tip at the time of writing is `260751d40b5`.

## Completed this session, pushed to the branch

- **Open review fixes lane, round 1** (`181818be5a9`): 29 replacements on 27 lines across 13 files; 11 items excluded to the main side.
- **Review re-check, round 4** (`f19f5fc012b`, `6d148810720`): not converged as checked. Two SOLR-7022 run claims were corrected because no receipt supported them.
- **Draft fidelity, round 3** (`36037409bfd`): SOLR-18368 and SOLR-9396 confirmed, 0 drift.
- **gradlew ticket, round 4** (`60436b1d169`): SATISFIED. The ticket text is final for the consolidated email. Nothing was filed or sent.
- **SOLR-9865 packaging input** (`091ec5790c5`): the state of the overlapping rollback PR, for the packaging call.
- **Convergence, round 1** (`b258f6ead72`): core admin 18 of 21 not converged; replication 8 of 9; SolrCloud 12 of 18; SOLR-17539 body findings (1 to 3 settled, 4 and 5 hold).
- **Convergence, round 2** (`5395f73b93f`): core admin 7 of 18 not converged; replication 4 of 8; SolrCloud 3 of 12.
- **Convergence, round 3** (`1a6cc1908f5`): core admin 3 of 3 still open (nothing landed); replication 2 of 2 still open; SolrCloud 3 of 3 converged, with one record item (SC-R3-01) open.
- **Category convergence, round 1** (`da75babdeae`): build and docs 0 of 8 converged; CLI 0 of 9; configsets 2 of 4; schema 0 of 8; security 1 of 5; SolrJ 3 of 19 (notes only); streaming 0 of 6.
- **CombinedQueryComponent ticket review, round 3** (report on the branch): still open pending main-side settlement.
- **CombinedQueryComponent ticket review, round 4** (`260751d40b5`): still open. Two blocking labels (draft lines 50 and 56) and two should-fix labels (lines 20 and 56), plus a comma splice in the gate job at line 31. Items 2 and 3 confirmed.

## Waiting on the main side

1. **SOLR-17539 live body:** apply the replacement text for findings 4 and 5, and the "What this change does" sentence (`reports/solr-17539-body-findings.md`).
2. **Core admin:** SOLR-4502 fragment retitle (branch side), SOLR-12916 fragment retitle (branch side), and SOLR-8576 alias fix (its gate job is still OPEN and unclaimed).
3. **Replication:** the SOLR-8430 draft pass. The gate is green at `0d64c35c5a`, so RB-R1-01, RB-R2-01 and RB-R3-01 to 03 can now be applied. SOLR-9091 needs the fragment retitle (RB-R1-04), then RB-R2-02.
4. **SolrCloud:** record items SC-R3-01 (per-ticket gate entries for SOLR-11479 and 12651), SC-R3-02 (precision on SOLR-17680 line 20) and SC-R3-03 (round 1 response corrections). None holds a draft.
5. **Category round 1:** 18 blocking items and about 125 should-fix items across seven categories. Each category report carries the exact replacement text. Blocking items by category: build and docs 2 (SOLR-16322 hold, SOLR-17842 claim); CLI 1 (SOLR-10667 needs the owner's `solrclient/` ruling); schema 3 (SOLR-15357 twice, SOLR-18134); security 4 (SOLR-11678 three, including a branch mapping, and SOLR-12161, all on HOLD until packaged heads are pushed); SolrJ 5 (SOLR-8536, SOLR-10364 and SOLR-11356 on HOLD, and SOLR-18341); streaming 3 (SOLR-10322 cause, SOLR-14231 twice).
6. **CombinedQueryComponent round 4:** apply A1 and A2 (blocking), A3 and A4, and B1. The round can then close as SATISFIED.
7. **Owner and main-side decisions on record:** the SOLR-9865 packaging call (PR #5100 read in `material/solr-9865-packaging-input.md`); the branch-history co-author trailer on the SOLR-15805 branch, which blocks opening that branch; the SOLR-9598 owner hold; the SOLR-10667 `solrclient/` exclusion; the SOLR-6430 changelog drop; the SOLR-10627 and other length calls.
8. **Admin UI premise runs (selenium):** paused. The claim needs Nick to type the Selenium commands in this session. The test-selection investigation is in `reports/admin-ui-test-selection.md`.

## Capacity and what I can take next

- I can run up to five or six read-only subagents at once within the cap, and I'm staffing the next review assignment as soon as it's pushed under `assignments/` with a claim path.
- Ready now, pending main-side replacements: round 2 re-checks of the seven categories. These are about 14 slices, which I can run in two waves.
- Available if wanted: a draft fix lane (`draft` tag) for the mechanical replacement texts. Name the drafts and say whether to use the texts in the round reports. Owner-decision items stay with the main side.
- `windows-check` work: any check that runs on this host without Gradle or builds.
- Not available: gate, implementation, premise-run, settling-run, sweep or bats work. Selenium runs happen only when Nick types the commands.

## Limits on this host

- Gate logs, JUnit XML and the VM1 probe source are not on this host, so several Proofs are checked against receipts only.
- The schema round read receipts at the local `origin/pr-prepare` ref, not the live receipts tip.
- Jira and GitHub reads are limited to the wrapper. Ticket quotes come from `research/jira-context/`.

To give me work, push the assignment under `assignments/` with its claim path, and name the host and staffing. I'll claim it before starting.
