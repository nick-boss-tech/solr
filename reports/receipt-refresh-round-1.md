# Receipt refresh round 1: round roll-up

Claim: `claims/receipt-refresh-round-1.md` (commit `fdafefeab92`). Per-part reports: `reports/receipt-refresh-round-1-r1.md` through `-r4.md`.

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Four read-only subagents checked the refreshed receipts against the branches. No build, Gradle run, or test was run. No submit branch, live PR, JIRA item, or comment was touched. Nothing was posted.

## Heads

Each live head matches the gated head its refreshed receipt names. Four heads moved since the earlier audits, and their verdicts below use the live head:

- 16570: `974c44f9608` to `9d3466101a2`
- 7120: `6a434a7fc3d` to `4664014a919`
- 2681: `4cb25b1691b` to `a3b1ea7994d`
- 4540: `62c06439fb0` to `180b6e8a7d3`

## Per-ticket verdicts

| Ticket | Verdict | Draft | Live head | What changed and what blocks it |
|---|---|---|---|---|
| SOLR-10403 | Hold; no draft | none | `d8e03755cb5` | The refreshed receipt proves the one-hop helper. The ticket's own case, `currency(price,'KRW')` with a USD default, still rounds to whole cents between two conversions (2556 on the branch, 2544 on base, stored 2555). Owner: keep the one-hop change with a Limits line, or carry the unrounded value through both conversions |
| SOLR-11391 | Hold | `pr-drafts/query-parsing/SOLR-11391.md` (HOLD draft) | `b0f15a22856` | The refreshed receipt proves a 500 to 400 change on an unknown join method. The Jira ticket is about GraphTermsCollector performance, so it cannot carry this change. The changelog links block names SOLR-11391. Owner: a new Jira key, or drop the branch |
| SOLR-16570 | Hold; not draftable | none | `9d3466101a2` | The refreshed receipt's test and count check out. But the ticket describes an empty-index NPE (`nullPolicy=expand` with `hint=top_fc`), which SOLR-16611 already fixed on base. The branch fixes a different NPE on docValues fields with `uninvertible=false`. Owner must pick the ticket before any draft |
| SOLR-8088 | Hold; not draftable | none | `567efa9b78c` | The receipt's count and single base failure check out. But the check fires only for `multiValued` fields. The reporter's `TextField` uninverts to `SORTED_SET_BINARY`, so a single-valued `TextField` still hits the same error. The changelog title overstates the scope: numeric `multiValued` fields already fail with a 400 on base |
| SOLR-3923 | Hold; no draft | none | `723f61ee4ab` | The guard in `ExtendedDismaxQParser.java` lines 310-317 sits inside `isBareWord()`. Signed lone parens such as `+(` and `-)` still enter the pf phrase text. The premise run (39 of 39, `testPfPs` fails on base) settles the premise, but the guard fix needs a new gate |
| SOLR-7120 | Draftable | `pr-drafts/edismax/SOLR-7120.md` | `4664014a919` | The handoff note is gone at the new head. Count 40 of 40 and the base failure check out. The changelog title says "bare RuntimeException", but base wraps the cause, so its text is in the response. Replacement is in part r3, finding 5 |
| SOLR-2681 (highlighting) | Accept after fixes | `pr-drafts/highlighting/SOLR-2681.md` (main-side draft) | `a3b1ea7994d` | The code, test, links, and changelog match the head. Fixes: the bold summary at line 13 drops "top-level"; add "verified 2026-10-09" to the Proof date; remove the "follow-up submission is planned" Limits sentence; link the Changelog line |
| SOLR-4540 (highlighting) | Not postable as written | `pr-drafts/highlighting/SOLR-4540.md` (main-side draft) | `180b6e8a7d3` | The Proof bullet at line 25 says a GitHub Actions run passed "at this head", but the receipt says it ran at an earlier head. Replace with "At head `180b6e8a7d3`, FastVectorHighlighterTest passes 3 of 3; verified 2026-10-09." Also: remove the process comment at line 1; fix the "today" link at line 10 (`L641` is post-change; base is `L636`); fix the internal wording at line 24 |

## Corrections to earlier verdicts

- **SOLR-10403.** The configsets audit called it audit only with no gate. It now has a gate-green receipt at the same head, but the ticket's own case is still not met. Hold.
- **SOLR-11391.** The query-parsing audit held it because of the ticket mismatch. The refreshed receipt does not change that. Hold.
- **SOLR-16570.** The query-parsing audit said not submittable. The refreshed receipt is gate green at the new head, but the ticket-linkage problem is new information. Hold.
- **SOLR-3923.** The eDisMax audit said not ready for a draft, because the premise was unproven and the guard missed signed parens. The premise is now settled by the run. The guard still misses signed parens. Hold.
- **SOLR-7120.** The eDisMax audit said sound in shape, with the handoff note to remove first. The note is removed at the new head, and a draft is now written. Draftable.
- **SOLR-8088.** The search-components audit said audit only, with no gate. The refreshed receipt is gate green, but the check misses the reporter's own case. Hold.

## Owner decisions

1. SOLR-10403: (a) keep the one-hop helper, state the between-hops rounding in Limits, and hold; or (b) carry the unrounded value through both conversions, round once, and add a `currency()` test. Only (b) meets the ticket, and it widens query-time changes.
2. SOLR-11391: a new Jira key for the 400 change, or drop the branch.
3. SOLR-16570: which Jira ticket this change addresses (the ticket as written is fixed by SOLR-16611 on base).
4. SOLR-8088: widen the check to single-valued `TextField`, or narrow the ticket and the title.
5. SOLR-3923: approve a branch fix to the guard for signed lone parens, followed by a new gate.
6. SOLR-4540: whether to add a choice section for the bad-builder behavior change (proposed wording is in the highlighting part r4 report).

## Draft fixes before posting

- `SOLR-7120.md`: replace the changelog title's "bare RuntimeException" wording.
- `SOLR-11391.md`: the HOLD block must come out before any posting, and the Jira key must be set first.
- `pr-drafts/highlighting/SOLR-2681.md` and `SOLR-4540.md`: the fixes listed in the table above.

## Not done

No build, test, Gradle run, `gh` write call, fetch, commit, or post. Gate logs named in the refreshed receipts are not on disk, so counts are receipt-only. Live JIRA was not queried. The subagents' reports are the source for the findings above.
