# Host: vm2

Pool Linux host. Capabilities and rules are in `hosts/pool-linux.md`.

- Heartbeat: 2026-10-25T00:21:14Z; warm-ticket evidence units round 1 DONE (receipts/warm-ticket-evidence-units-round-1-vm2.md; all four units published, drafts not posted). Step 02 DONE (receipts/cuvs-lucene-02-submission-package-round-4.md). Live claims with deliverables: review kit (claims/cuvs-lucene-review-kit-round-4-vm2.md). Next: finding more work.

- Heartbeat: 2026-10-25T00:58:39Z; harmonize H1 all 16 batches DONE (receipts/harmonize-h1-all-16-vm2.md; batches material/harmonized-backlog/batches/h1-NNN-rev2.json). Next: H2 QA of verify-first and close-fixed rows, not started.

- Heartbeat: 2026-10-25T01:00:07Z; harmonize H1 rev3 published (receipts/harmonize-h1-rev3-vm2.md). H2 blocked on second-host QA of H1. Next: look for other unclaimed Linux work.

- Heartbeat: 2026-10-25T02:08:12Z; WON'T DO for harmonize batch-claims round 1 and 2 (claims/wontdo-harmonize-h1-batch-claims-round-*-vm2.md). CI red PR RCA published (reports/ci-red-prs-rca-round-1.md): #5081 infrastructure, #5083 and #5069 need settling runs. H2 still blocked on second-host QA. Next: settling runs or critical implementation triage if unclaimed.

- Heartbeat: 2026-10-25T02:19:22Z; critical implementation triage round 1 published (reports/critical-implementation-triage-round-1.md). Settling runs for CI red RCA (#5083 seed A57A22E346C237C6, #5069 seed B94347D2600CC75D) running on the host lock, logs under gates/ci-red-rca/logs (not on board yet). H2 still blocked on second-host QA.

- Heartbeat: 2026-10-25T02:46:41Z; CI red RCA settling runs done (reports/ci-red-prs-rca-round-1-settling.md; #5083 and #5069 pass at recorded seeds on head and main; #5081 re-run not done). Worktrees ci-5083, ci-5069, ci-main removed. Next: OPEN-WORK check for unclaimed Linux work.

- Heartbeat: 2026-10-25T02:50:13Z; CI red RCA settling done (reports/ci-red-prs-rca-round-1-settling.md; worktrees removed). Critical comments batch 02 done (reports/critical-comments-round-1-batch-02.md, ten drafts, not posted). Next: batch 03 if unclaimed.

- Heartbeat: 2026-10-25T02:51:40Z; critical comments batch 03 done (reports/critical-comments-round-1-batch-03.md, eight drafts, two no-draft reasons). Next: batch 04 if unclaimed.

- Heartbeat: 2026-10-25T02:55:58Z; critical comments batch 04 done (reports/critical-comments-round-1-batch-04.md, nine drafts, one no-draft reason). Next: batch 05 if unclaimed.

- Heartbeat: 2026-10-25T03:00:00Z; critical comments batch 05 done (reports/critical-comments-round-1-batch-05.md, eight drafts, two no-draft reasons). Next: batch 06.

- Heartbeat: 2026-10-25T03:04:55Z; critical comments batch 06 done (reports/critical-comments-round-1-batch-06.md, ten drafts). Next: batch 07.

- Heartbeat: 2026-10-25T03:08:52Z; critical comments batch 07 done (reports/critical-comments-round-1-batch-07.md, nine drafts, one already-told). Next: batch 08.

- Heartbeat: 2026-10-25T03:13:22Z; critical comments batch 08 done (reports/critical-comments-round-1-batch-08.md, six drafts, four reason codes). Next: batch 09.

- Heartbeat: 2026-10-25T03:18:40Z; critical comments batches 02 to 09 done (reports/critical-comments-round-1-summary-vm2.md). 63 drafts, 13 no-draft reasons. Batch 01 claimed by phone-agent-2. Next: unclaimed work from OPEN-WORK.md; SOLR-14232 draft outstanding.

- Heartbeat: 2026-10-25T03:52:10Z; harmonize H2 six family drafts published (drafts/jira-comments/*.md, not posted, QA pending); harmonize rev4 published. Gates SOLR-18564 and SOLR-10928 running.

- Heartbeat: 2026-10-25T04:09:35Z; SOLR-18564 exact head independently confirmed (receipts/SOLR-18564-vm2-independent.md; vm6 had already gated it). SOLR-10928 vm2 run stopped as duplicate of vm6 green receipt (claims/solr-10928-live-head-regate-vm2-duplicate.md). Build lock free.

- Heartbeat: 2026-10-25T18:13:07Z; SOLR-17786 round 2 GREEN at fde070281383f113a331a9ec5ccde173b66fac3c (receipts/SOLR-17786.md). Round 1 failed on the forbidden-API check (claims/solr-17786-gate-round-1-two-shard-vm2-verdict-check-failed.md); the ladder repair is claims/solr-17786-forbidden-api-repair-vm2.md. Antora guide: material/antora-ref-guide-gate-guide.md. Public PR submission still needs Nick. Next: wait for owner direction.

- Heartbeat: 2026-10-25T19:34:47Z; posted the reviewed PR 5108 comment (https://github.com/apache/solr/pull/5108#issuecomment-6402197189), owner-approved. Draft: drafts/solr-17786-pr5108-compare-comment-draft-r3.md. Next: wait for reply.

- Heartbeat: 2026-10-25T20:20:10Z; BATS 5225 follow-up: branch nick-boss-tech/solr:solr-5225-bats-followup at 096da6567e3, report reports/bats-5225-accurate-assertion-round-1.md, draft drafts/pr-bats-5225-followup-draft.md (not posted). JDK 21 assertion passes; BATS teardown fails on this host (solr stop returns 1, also on unchanged main). Java 24 leg code-read only. Next: wait for Nick on the PR.

- Heartbeat: 2026-10-26T00:10:59Z; teardown client-interrupt probe round 1 done: verdict incomplete (reports/teardown-client-interrupt-probe-round-1.md, scaffold in reports/teardown-client-interrupt-probe-round-1-scaffold.md; board commit 632b8eaff8e). Without-interrupt arm clean; with-interrupt closer did not return in 30 s, tracker entry remained, closer stack not captured. Next: none claimed; waiting for owner direction on the proposed one-run stack capture.

- Heartbeat: 2026-10-26T00:43:23Z; teardown probe rerun done: verdict incomplete (reports/teardown-client-interrupt-probe-round-1-rerun.md; scaffold v2 reports/teardown-client-interrupt-probe-round-1-rerun-scaffold.md; board commit 06abb40a43a). Interrupted closer parked in ExecutorUtil 60 s executor wait after httpClient stop returned; 30 s cap ended observation. Thread names verified (qtp, h2sc, workers). Next: proposed single 75 s run of the interrupted arm, not claimed.

- Heartbeat: 2026-10-26T01:25:18Z; teardown probe round 2: control invocation stopped on a scaffold failure (evidence path under build/ denied by solr-tests.policy; no close ran). Interrupted arm not run. Report reports/teardown-client-interrupt-probe-round-2.md. Waiting on the assignment author for the invocation-budget decision.

- Heartbeat: 2026-10-26T01:41:36Z; teardown probe round 2 corrected pair done (board commit 3bcf49da9c8; reports/teardown-client-interrupt-probe-round-2-result.md). Interrupted: tracker-only survivor (close threw InterruptedException from Jetty ManagedSelector.doStop at 60 s; executor and selector terminated; tracker retained). Control: clean. Invocation budget exceeded by one control run through a duplicate chain (my error, disclosed in the report).

- Heartbeat: 2026-10-26T01:54:45Z; teardown probe round 2: all driver chains confirmed finished (only a defunct shell remains). Correction published (reports/teardown-client-interrupt-probe-round-2-result-correction.md): only the interrupted arm sets the interrupt. No further runs or production changes pending source review.

- Heartbeat: 2026-10-26T02:41:02Z; external-executor close probe round 1 done (reports/teardown-client-close-external-executor-round-1.md, board commit caadee98de8). Control clean. Interrupted: close threw (InterruptedException at ManagedSelector.doStop:139); tracker retained; one saved raw selector still open at 1 s with an attributed caller-pool worker in ManagedSelector.select. Exactly two invocations; attempt 0 refused by guard before any test. Next: review for the owner decision on where selector cleanup belongs.
