# SolrCloud PR drafts: patch and audit, round 3

Verdict count: 9 READY, 8 HOLD, 2 NOT READY. Nothing has been opened, posted or merged. Each READY draft is final text, and only Nick's approval to open remains.

Basis: the four audit subagents read `assignments/pool-solrcloud-pr-audit-round-3.md`. Each checked its drafts against the live fork heads (ls-remote), the receipts, the JIRA text (read only), and the code at the cited SHAs. Every citation's path and line range was checked. The lead then verified the four edits that change a claim about code: SOLR-17680 (field check moves out of the command), SOLR-12651 (async copy failure does not throw), SOLR-9155 (log line restored), and SOLR-14919 (the RecoveryStrategy line). The 12651 edit is correct: a failed copy in async mode is reported only in the async status, and the TODO in `waitForAsyncCallsToComplete` says so. The 14919 wording was corrected to the RecoveryStrategy line, after the lead found its replication ticket draft had the wrong file.

## Verdicts

| Ticket | Verdict | What holds it, or what is left |
|---|---|---|
| SOLR-5813 | READY | None. |
| SOLR-11288 | HOLD | Base run at `cbf0c678f277` (`gates/SOLR-11288-base-run.md`). The Proof's base sentence rests on the prior head; no base log is on disk. |
| SOLR-11479 | READY | Landing order after SOLR-15035 is the coordinator's plan. Nick confirms the order. See the identity item below. |
| SOLR-12651 | HOLD | Trailers on three commits (`gates/SOLR-12651-history-scrub.md`), then base run at the rewritten head. The changelog title says "unless the restore runs asynchronously", which overstates the code. Fix it in the same scrub. |
| SOLR-12991 | READY | None. |
| SOLR-13186 | READY | None. The base-failure date is the receipt's ledger date. Accepted, since the receipt records it. |
| SOLR-13369 | HOLD | Dated focused run at `dfa0db5bdf96` (`gates/SOLR-13369-dated-run.md`). |
| SOLR-14919 | NOT READY | Processor-only. The receipt covers `84e7bcaeb57`; the links are at `c1fa37e33c1`; the live tip is `8f1f4ef91e79`. The gate at `8f1f4ef91e79` stopped at the focused-head leg and was not relaunched (`gates/SOLR-14919-processor-only-gate.md`). |
| SOLR-15035 | HOLD | Base-code run and dated banked-patch run (`gates/SOLR-15035-base-run.md`). |
| SOLR-15106 | HOLD | Premise compile text and dated head run (`gates/SOLR-15106-premise-base.md`, claimed by vm2). |
| SOLR-15386 | HOLD | Dated run at `ca8cb61ee957` (`gates/SOLR-15386-dated-run.md`). The "recorded 2026-10-07" date is a takeover-log date. |
| SOLR-15674 | HOLD | Scrub, re-gate, and re-point all links and the changelog (`gates/SOLR-15674-history-scrub.md`). |
| SOLR-15863 | READY | None. Counts match the receipt at `78dff1d52f6c`. |
| SOLR-16013 | READY | None after one fix: the Proof now reads "(run 2026-10-12)" in place of a claim about `966bb4c4eb44`. |
| SOLR-16437 | HOLD | Changelog-only gate at `a23f770e4b9` (`gates/SOLR-16437-changelog-regate.md`). Decision for Nick: run the nightly group first (`gates/SOLR-16437-nightly-run.md`), or keep the skip disclosure in Limits. |
| SOLR-17292 | READY | None. |
| SOLR-17680 | READY | None. |
| SOLR-17733 | READY | None. |
| SOLR-9155 | NOT READY | Gate at `29bf373828994c622ac478a968d6cff4f4c45fa5` (`gates/SOLR-9155-logging-regate.md`). The counts stay at `dbd88c84ff2` until then. Visible length is about 3,880 with the bracket; cut 380 characters once counts land. |

## Edits made this round

- SOLR-9155: Proof states that the test passed at the last gated head `dbd88c84ff2`, and that it does not compile on the merge-base. "Not changed" replaces "Behavior change" for the interrupt flag, which the base code also leaves cleared. The ops-logging Limits line now says the generic catch keeps its ERROR line, and it names the avoid-log-and-rethrow item.
- SOLR-14919: "Any other non-boolean value" replaces "Any other value". The upstream description now names the RecoveryStrategy line, not a test.
- SOLR-13369: the test comment names shard2 and shard3. The ticket has no hash-range reading.
- SOLR-16013: the Proof no longer claims the revert run used `966bb4c4eb44`.
- SOLR-17680: the bold line and the Choice say the field check moves out of the command, as the branch diff shows. The Proof says the green run at `f4b8ce83365` still stands for this tree.
- SOLR-12651: the async exemption is now accurate, and the DEBUG bullet says one ERROR line is added for the cleanup.
- `material/solr-14919-replication-ticket-draft.md`: the upstream description matches the RecoveryStrategy diff.

## Decisions for Nick

1. **SOLR-11479 commit identity.** Commit `360dca60fcfa` is authored as `253869336+nick-boss-tech@users.noreply.github.com`. The other commits and the ICLA identity use `nick.boss.us@gmail.com`. Confirm the identity. A fix means a history rewrite of `solr-11479-submit`, which needs a lease push and a re-gate.
2. **SOLR-16437 nightly run.** Run the nightly group before posting, or post with the skip disclosure in Limits.
3. **SOLR-9155 log-and-rethrow Limits line.** Confirm the wording.
4. **SOLR-17680 v2 aliases.** The receipt says the v2 endpoint with two routers starts working too, but the draft says "should". Make it firmer, or leave the hedge.
5. **SOLR-5813 Choice.** The sentence "It would also change what `collection=` returns" is vague. Your call on wording.
6. **jira-read token file.** The read helper reads `C:\Users\shaninna\Downloads\jira_pat2.env`. AGENTS.md names `C:\Users\shaninna\dev\jira_token.env`. Confirm which file is intended. The token was not printed.

## Gates now queued on vm2 (all OPEN unless noted)

- `gates/SOLR-9155-logging-regate.md`, `gates/SOLR-16437-changelog-regate.md`
- `gates/SOLR-11288-base-run.md`, `gates/SOLR-12651-history-scrub.md`, `gates/SOLR-12651-base-run.md`
- `gates/SOLR-13369-dated-run.md`, `gates/SOLR-15035-base-run.md`, `gates/SOLR-15386-dated-run.md`
- `gates/SOLR-15674-history-scrub.md`, `gates/SOLR-16437-nightly-run.md`
- `gates/SOLR-14919-processor-only-gate.md` (stopped at the focused-head leg; not relaunched)
- `gates/SOLR-15106-premise-base.md` (claimed)

## Not done

- No PR opened, no comment, no JIRA write, no email.
- No build, test or gate run on this host.
- No branch or receipt edited in this round.
