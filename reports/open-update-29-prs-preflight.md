# Open the update-29 draft PRs: preflight

Assignment: `assignments/open-update-29-prs.md` (commit `b719d4fc1e9`). Claim: `claims/open-update-29-prs-preflight.md` (commit `d0f0470962b`).

This report is the read-only preflight the assignment asks for, before any pull request is opened. **No pull request was opened, commented on, or changed.** The assignment says its authorization was given in the main chat. This session cannot verify that, and the standing instruction for this scheduled check is no PRs and nothing posted publicly. The opening step waits for the user's explicit go-ahead in this session.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Two subagents checked receipts against live tips for 14 tickets each. The lead checked SOLR-11483 and read existing pull requests once with a read-only listing.

## Result

Rule: a ticket opens only if the live tip of `solr-<ticket>-submit` equals the gated head in its receipt.

**Openable: 24.** The live tip matches the receipt head (full or prefix) and a draft exists.

3657, 5065, 5505, 5887, 5939, 5941, 6045, 6065, 6973, 7504, 11475, 11483, 12245, 12703, 12864, 12705, 13265, 13696, 13943, 14262, 14718, 16356, 16655, 16910

**Skipped by the rule: 4.** The live tip is not the receipt head.

| Ticket | Receipt head | Live tip | Why |
|---|---|---|---|
| 4841 | `f8850ffd421` | `e4c878627108` | One changelog-only commit past the receipt. Not re-gated in the receipt. |
| 5754 | `7fbe0128d8b0` | `46b919e2d4e8` | One commit that removes the root `SOLR-5754-TESTING.md`. Not re-gated; the receipt records no pre-fix step. |
| 7022 | `6a233ab2fdb` | `db357868610b` | Two text-only commits past the receipt (changelog title and a Javadoc comment). Not re-gated. |
| 16673 | `d5c19e64ba1b` | `d7170b12f312` | One changelog-title commit past the receipt. The receipt head is its ancestor. Not re-gated. |

The rulings material says these four were verified the same day, with top-up verifications recorded. The receipts in the workspace still name the earlier heads, so the rule cannot pass them as they stand. The main side should refresh those four receipts, as `receipts/README.md` says it does, or confirm that the top-up records cover the live tips.

## Checks made

- Receipt head against live tip for all 28 tickets. Short receipt heads were resolved to full SHAs locally.
- A draft exists for each of the 28. SOLR-18505 is not in the list, as the assignment says.
- Existing pull requests: one read-only listing of pull requests authored by the fork against apache/solr, limit 200. None is open or merged for any of the 28 update branches. SOLR-18505 is already open (#5028), and the assignment excludes it.
- No receipt says NO GATE, and none lacks a gated-head line.

## Points for the owner before opening

1. **Authorization.** Open the 24 as draft pull requests on apache/solr, in the assignment's order? This is public and visible to upstream maintainers.
2. **Pull request titles.** The assignment says the title and body come from each draft "exactly as written". The drafts have no separate title field. Only SOLR-16673 has a labelled "Title:" line. Each opening needs a title source, or the owner should name the title for each ticket.
3. **The four skipped tickets.** Refresh their receipts to the live tips, or open them after the main side confirms the top-up.
4. **Draft text.** Each draft opens with the AI-text header line and a JIRA link. The assignment says not to change the text. The header line and footer are in the drafts as the workspace conventions require.

## Not done

No pull request was opened, commented on, or changed. No submit branch was edited. No build, Gradle run, or test was run.
