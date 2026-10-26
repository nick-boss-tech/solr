# Assignment: Eric Pugh stream stock, round 1

Status: OPEN
Posted by: phone agent, 2026-10-26, at Nick Shanin's direction (stock the 2026-10-27 active comment stream; Eric Pugh's tickets are the theme).
Capability tags: review, draft

## Purpose

The 2026-10-26 active stream went entirely to Christos Malliaridis's UI tickets (10 of 10). Tomorrow's stream is stocked from Eric Pugh's tickets. Five Eric drafts already exist and are not part of this assignment: SOLR-18549, SOLR-18546, SOLR-17931, SOLR-18439, SOLR-18500. This assignment produces the missing evidence units so their drafts can join the stock.

Four of these units were assigned on 2026-10-24 (warm-ticket evidence units round 2 units 1 and 2; warm-and-bench units 3 and 4) and have produced no report and no draft on this board. This assignment restates them as claimable units; a claim here supersedes the silent earlier assignment for that unit.

## Units (one claim per unit)

1. **SOLR-18553 plurality audit.** Eric's ticket on pluralization handling. Audit the current code against the ticket's claim, with file and line citations at a pinned main SHA. Deliverable: `reports/solr-18553-plurality-audit-round-1.md` and, if the audit settles an open point, a draft at `jira/jira-comment-SOLR-18553-draft.md`; otherwise a no-draft verdict with the reason in the report.
2. **SOLR-18449 test-sysprops audit.** Eric's ticket on system properties in tests. Same shape: audit at a pinned SHA, report `reports/solr-18449-test-sysprops-audit-round-1.md`, draft at `jira/jira-comment-SOLR-18449-draft.md` if warranted.
3. **SOLR-18369 BulkV2 equivalence audit.** Eric's ticket on BulkV2 API equivalence. Audit the v2 bulk path against the v1 behavior the ticket names, citations pinned. Report `reports/solr-18369-bulkv2-equivalence-audit-round-1.md`, draft at `jira/jira-comment-SOLR-18369-draft.md` if warranted.
4. **SOLR-18460 CoreAdmin v2 test-port scoping.** Eric's ticket on porting CoreAdmin tests to the v2 API. Scope which tests port cleanly and which do not, with reasons. Report `reports/solr-18460-coreadmin-v2-test-port-scoping-round-1.md`, draft at `jira/jira-comment-SOLR-18460-draft.md` if warranted.
5. **Fresh Eric scan.** SOLR tickets Eric Pugh reported, or commented on, updated since 2026-10-24, excluding every ticket named above and the five existing drafts. Pick up to the three strongest where a code trace or reproduction can settle an open point tonight. Per ticket: a short report section in `reports/eric-fresh-scan-round-1.md` and a draft at `jira/jira-comment-SOLR-<KEY>-draft.md`, or a no-draft verdict with reason.

## Rules

- Drafts only. Nothing posts to Jira or GitHub from this assignment; every draft needs Nick Shanin's own go later.
- Drafts follow the staged shape in `comment-formula.md`: evidence first with citations pinned to a main SHA, one clear question, Jira wiki markup, closing footer `🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_`, and a real mention of Eric (`[~epugh]`) where the draft addresses him.
- Evidence means a code trace or a run, quoted in the report. A draft whose unit produced neither is a no-draft verdict, not a weaker draft.
- Claims are one file per unit in `claims/`, naming the unit number.
