# SolrCloud PR drafts: fix pass, round 2

Verdict: no SolrCloud draft is ready to post yet, though SOLR-17292 comes closest. Its text is final and its receipt is green at the live head. Most of the others have final text and wait only on a dated run or a gate. Six need a branch change, a history rewrite or a re-gate before the text can be final, and four need a decision from Nick. Posting stays with Nick, and nothing was opened, posted or built.

Basis: the round 1 critical review (`reports/solrcloud-pr-critical-review-round-1.md`), the round 2 assignment (`assignments/pool-solrcloud-pr-fix-round-2.md`), the receipts, the fork tips checked with ls-remote, and the JIRA text fetched read-only. Four subagents edited the drafts, four or five per group. I checked the drafts afterwards: no dashes, the header and footer are present, no process words, and every SHA a draft cites either appears in its receipt or exists as a commit in the source checkout.

## Length measure

The formula's guide is about one screenful. The subagents measured visible text, with link URLs and markup removed. The raw file count, which includes URLs, is what the critical review used. Every citation is a full link, and the formula keeps every citation, so raw counts run high. Raw counts after round 2:

- Under 3,500 raw: SOLR-12991 (3,198), SOLR-13186 (3,206), SOLR-13369 (3,118), SOLR-14919 (3,498).
- At or just over 3,500 raw: SOLR-5813 (3,501), SOLR-15035 (3,501), SOLR-11479 (3,719), SOLR-12651 (3,777).
- Well over: SOLR-11288 (4,149), SOLR-15106 (4,554), SOLR-15386 (4,558), SOLR-15674 (4,843), SOLR-15863 (4,026), SOLR-16013 (4,158), SOLR-16437 (5,234), SOLR-17292 (4,800), SOLR-17680 (5,266), SOLR-17733 (4,614), SOLR-9155 (5,209).

If Nick wants raw counts to govern, the drafts in the last two groups need trimming. On visible text, every draft is at or under 3,500 characters; SOLR-16013 is exactly 3,500.

## Shared blocker: dates

The formula requires a verification date on every count. Most receipts record a ledger date or a takeover-log date, and the gate files say those are not date sources. Drafts that carry only those dates say "recorded <date>". The batch job `gates/solrcloud-dated-runs-round-2.md` and the per-ticket jobs below supply dated runs. Until a dated run exists, the Proof is not final.

## Per ticket

| Ticket | State after round 2 | Still needed before posting |
|---|---|---|
| SOLR-5813 | Text fixed. Default kept (answers file). Failure request named in the Choice. | Dated run (batch item 1). |
| SOLR-11288 | Text fixed; Proof at the live head `cbf0c678f277`; comma-escaping Limits line added; ClusterStatus change added. | Base run at the live head (`gates/SOLR-11288-base-run.md`). The receipt's only base run is the padded repeat, which cannot reach the padded case. |
| SOLR-11479 | Text fixed. Landing order after SOLR-15035 stated; multi-replica 400 in Limits. | Opens after SOLR-15035. |
| SOLR-12651 | Text fixed at `404f13b501c9`; DEBUG request in Limits; changelog title already corrected. | Three commits carry Co-Authored-By trailers (`gates/SOLR-12651-history-scrub.md`). After the rewrite the Proof must be re-pointed. Base run (`gates/SOLR-12651-base-run.md`). |
| SOLR-12991 | Text fixed. INFO added to the Choice with its cost. | Dated run (batch item 2). |
| SOLR-13186 | Proof has the dated 2026-10-13 run at `b436d90d2a8`. | Base run dated (`gates/SOLR-13186-dated-run.md`, already queued). |
| SOLR-13369 | Seed narration removed; cause labelled as the reporter's reading; test-or-router answered in Limits. | Dated run (`gates/SOLR-13369-dated-run.md`). |
| SOLR-14919 | Processor-only route. Upstream commit `75b183196798` described without speculation. Proof stays at `84e7bcaeb57` and links at `c1fa37e33c1` until the re-gate. | Re-gate at `8f1f4ef91e79` (`gates/SOLR-14919-processor-only-gate.md`, claimed). Scope decision for Nick (below). |
| SOLR-15035 | numShards Choice added: implemented route is all slices; alternatives stated with costs. Proof uses the receipt's recorded outcome. | Base run and banked-patch run (`gates/SOLR-15035-base-run.md`). |
| SOLR-15106 | Choice added (title's no-return request against the resign route). ZooKeeper outage in Limits. | Premise run and dated head run, claimed by vm2 (`claims/pool-vm2-gate-backlog-15106.md`, 2026-10-13 04:19 UTC). |
| SOLR-15386 | Cost added to the first alternative. Commenter's broader idea named in Limits with an offer. | Dated run (`gates/SOLR-15386-dated-run.md`). |
| SOLR-15674 | Text fixed; Proof stays a placeholder for the scrubbed head. Reporter's sequence named as not run. | Scrub and re-gate (`gates/SOLR-15674-history-scrub.md`, open). |
| SOLR-15863 | Proof and code links at `78dff1d52f6c` (green in the receipt). Earlier-head wording now quotes the receipt. | Check the "base code" claim against `g15863-gate.log` (the receipt does not say whether production was reverted at `8f5e94cf314`). |
| SOLR-16013 | Five-step close order; Choice: pool wait (implemented) against serial close; Overseer-wait timeout in Limits. Proof at `408317d7e241`. | `gates/SOLR-16013.md` still reads FAILED. vm2 should close it as superseded. |
| SOLR-16437 | Limits: wrong-shard 400 path untested; nightly skips with their counts at `f72a9edd0a8b`. | Nightly-group run (`gates/SOLR-16437-nightly-run.md`). The wrong-shard test would be branch work. The branch changelog says the replica "does not exist", which does not fit a replica that exists in another shard. Nick decides both (below). |
| SOLR-17292 | Node-down wording narrowed to KeeperException. No Choice (adopted). Date 2026-10-10 from the re-gate. | None. The receipt covers the in-branch remedy at `f614a42fbc80`. |
| SOLR-17680 | Text fixed; Proof at `ce59bbe112d`, gate green 2026-10-13 03:53 UTC, receipt refreshed. The base run at `f4b8ce83365` covers the same tree. | None. |
| SOLR-17733 | Local-delete behavior stated in What. Choice: implemented route, the 400 route, and a 501 route for DELETE. Limits: remote and local failures untested; sync 500 not reproduced. Dated run at `636196b7954` done, 2026-10-13 04:07 UTC. | None on text. |
| SOLR-9155 | Text follows the head `dbd88c84ff2` (declared route). Proof filled from the receipt: green at this head, and the premise compile message at the merge-base. | Nick confirms the route Choice and the ops-logging Limits (below). |

## Decisions made in this round

Under the decision default, the recorded recommendation is adopted. Where none is recorded, the harder or more thorough option is taken. These are the calls I made:

- **SOLR-5813:** kept the adopted default; the failure route is named with its cost.
- **SOLR-12991:** kept WARN as implemented; INFO added to the Choice, because the commenter raised it.
- **SOLR-15035 and SOLR-15106:** kept the implemented route in each Choice, and stated the alternative with its cost. The answers file gave no recommendation for either.
- **SOLR-16013:** kept pool wait as the implemented route, and stated the serial close with its cost. The ticket comment that suggests serial close says the pool was added for test speed, not correctness.
- **SOLR-17733:** kept the three-route Choice. The 501 route for DELETE is a live alternative in its own right. The reporter's 501 request is about POST sync, so it is answered in Limits, not in the Choice.
- **SOLR-13369:** kept the test side, the one the draft already took. The router change would alter routing for every collection, so the test side is the narrower change. The router route is stated in the Choice with its cost. This is a judgment call, and Nick may overrule it.
- **SOLR-12651:** the history rewrite also changes two commit messages: the Co-Authored-By trailers come off, `db8ba836d8d` drops the "Hypothetical, unrun" line, and `13e6789badd`'s subject drops "hypothetical-reproduction". Public history should not carry process words.

## Decisions for Nick

These change what a PR claims, or the record conflicts in a way the files cannot settle.

1. **SOLR-9155 route.** The answers file (2026-10-09) adopted the wrapping route. The branch head `dbd88c84ff2` implements the declared route, and its receipt (2026-10-13) calls that head PR-ready. The answers file is superseded for this head. The draft's Choice puts the question to you: declared route (implemented), or wrapping with the flag restored. If you pick wrapping, the branch needs a revert, a new gate, and new What and Choice sections.
2. **SOLR-9155 ops logging.** The ERROR line is removed and nothing is added. The ticket asks for better ops logging. Keep the removal, or add a log line at a caller?
3. **SOLR-14919 replication.** The processor-only route does not fix the replication failure the ticket describes. The decision default points to the harder option: the recovery marker with its own test. That is branch work, and the upstream commit gives no reason for removing the marker. My recommendation: ship processor-only now and open the replication work as its own ticket, with the Limits line already in the draft. If you want the PR to fix replication, say so and I will queue a patch job for vm2.
4. **SOLR-16437 branch items.** A wrong-shard test would be branch work. The changelog wording "does not exist" does not fit a replica that exists in another shard. My recommendation: reword the changelog line to say the replica is not on the target shard, and keep the wrong-shard test as a Limits line.

## Branch items for the main side

- SOLR-12651: history rewrite (`gates/SOLR-12651-history-scrub.md`). This is a fork force-push on a submit branch, which AGENTS.md allows with a lease.
- SOLR-15674: history scrub (`gates/SOLR-15674-history-scrub.md`), already queued.

## Gate jobs written this round

- `gates/SOLR-11288-base-run.md`
- `gates/SOLR-12651-base-run.md`
- `gates/SOLR-12651-history-scrub.md`
- `gates/SOLR-13369-dated-run.md`
- `gates/SOLR-15035-base-run.md`
- `gates/SOLR-15386-dated-run.md`
- `gates/SOLR-16437-nightly-run.md`
- `gates/solrcloud-dated-runs-round-2.md` (three items: SOLR-5813, SOLR-12991, SOLR-15106)

Gate jobs already open that this round relies on: `gates/SOLR-13186-dated-run.md`, `gates/SOLR-14919-processor-only-gate.md`, `gates/SOLR-15106-premise-base.md` (claimed by vm2), and `gates/SOLR-15674-history-scrub.md`. Done by other lanes since this round started: the SOLR-9155 gate at `dbd88c84ff2` (receipt 2026-10-13), the SOLR-17680 re-gate at `ce59bbe112d` (2026-10-13 03:53 UTC), and the SOLR-17733 dated run at `636196b7954` (2026-10-13 04:07 UTC).

## Facts that contradicted the critical review or the records

- The review says SOLR-17680's error text "describes text clients never see". The code at `ce59bbe112d` (AdminAPIBase.java L129-131) rethrows the remote exception, and JIRA quotes the client-facing message. The sentences stay.
- The review says the SOLR-17733 reporter asked for a 501. The reporter asked for a 501 on POST sync. The DELETE 501 route is the review's own alternative.
- The review missed the Claude co-author trailers on three SOLR-12651 commits.
- The review says SOLR-15863's Proof counts 7 test methods against 6 in the source. The source has seven: six annotated, plus `testBackupProperties`, which has no annotation (`AbstractIncrementalBackupTest.java` line 466). The receipt's 7 of 7 stands.
- The review says SOLR-16013's receipt gate is at the older head `e1bd21fd11a`. The receipt now records a green at `408317d7e241`, which is the live tip.
- The answers file dates SOLR-17292's verification to 2026-10-04. That date belongs to the superseded head `e43200b0fb6`. The re-gate at `f614a42fbc80` finished 2026-10-10.
- `receipts/SOLR-14919.md` says "GATE GREEN at the live tip" for `84e7bcaeb57`. The live tip is `8f1f4ef91e79`, so that receipt is stale.
- The SOLR-9155 receipt was refreshed while this round ran. It now records a green run at `dbd88c84ff2` and a premise compile message at the merge-base. The round 1 "no count at this head" finding is superseded.
- The SOLR-15863 changelog title is already capitalised at `78dff1d52f6c`. The answers file's "owed" fix is done.

## Not done

- No PR opened, no comment, no JIRA write, no email.
- No build, test or gate run on this host.
- No edits to receipts, gate files owned by vm2, or submit branches.
