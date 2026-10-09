# Highlighting round 1: audit and drafts

Assignment: `assignments/highlighting-round-1.md` (commit `b86e65bde79`). Claim: `claims/highlighting-round-1.md` (commit `63128a22a5f`). Group reports: `reports/highlighting-round-1-g1.md` (SOLR-2681 draft, SOLR-2632 audit) and `reports/highlighting-round-1-g2.md` (SOLR-3704 and SOLR-4540 drafts, SOLR-16885 audit). Drafts: `pr-drafts/highlighting/`.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Two subagents did the audits and drafts. The lead checked the live tips, the drafts' open markers and em dashes, and wrote this report. No PR was opened, no comment was posted, no Jira item was touched, no branch was edited, and no build, Gradle run or test was run.

## Per-ticket verdicts

| Ticket | Live head (checked) | Verdict | Open point |
|---|---|---|---|
| SOLR-2681 | `4cb25b1691b9` | Drafted, held | The receipt's count of 36 of 36 does not match the source: 31 `@Test` methods at this head and 30 at base. The draft carries an internal delete-before-posting note. The changelog title says "nested inside a function query", but the code handles only a top-level `query(...)`. Narrow the title. Limits says "on request"; the planned-submission rule needs a stated plan. |
| SOLR-3704 | `de63d4e5d5d1` | Drafted, draftable on the gate record | The receipt names no verification date, so the draft omits one. The ledger review notes the assignment names were not found, so Limits come from the diff: the docValues-only Date branch and date unique keys are untested. Owner to confirm those Limits. |
| SOLR-4540 | `62c06439fb06` | Drafted, not postable | One OPEN line: the base-code result for `testFieldAbsentFromDocIsSkipped` is not recorded in the receipt and must come from the run log. The changelog title claims a slowdown fix, but no timing exists in the record. Fix the title. |
| SOLR-2632 | `1d7018f3fd7b` | Audit only; owner decision | The branch matches the record. It adds a root `SOLR-2632-TESTING.md` (stale, must not ship), a changelog that claims a behavior change, and two unwrap hunks that are inert if the receipt is right. Its test does not use dismax, so it does not cover the ticket's scenario. See options below. |
| SOLR-16885 | `2b9b80119a92` | Audit only; owner decision | The branch and its note match the receipt. The note is unrun. A premise run must show the `IndexOutOfBoundsException` on base with the branch test. The Solr-side workaround changes behavior for every term-vector field without positions, which the ticket itself calls unlikely to be a Solr fix. `SOLR-16885-TESTING.md` must not ship. |

## SOLR-2632 options, stated plainly

- **(a)** A test-only pin PR. It drops the production hunks, the changelog, and the root note, and needs a fresh run.
- **(b)** A Jira comment and no PR. This needs your go-ahead, because it is public.
- **(c)** Keep the branch as received and do not submit it.

## Owner decisions

1. **SOLR-2632 disposition:** (a), (b) or (c).
2. **SOLR-16885:** whether a main-side premise run should be funded to show the base failure, and whether a Solr-side change is wanted at all, given that it changes behavior for every term-vector field without positions.
3. **SOLR-2681:** reconcile the count, 36 of 36 in the receipt against 31 `@Test` methods at head, before the draft can be posted. Narrow the changelog title. Replace "on request" with a planned-submission sentence, or remove the nested-forms limit.
4. **SOLR-4540:** fill the OPEN line from the run log, and fix the changelog title's slowdown claim.
5. **SOLR-3704:** confirm the Limits, which come from the diff, since the ledger review notes were not found.

## Holds carried in the drafts

- `pr-drafts/highlighting/SOLR-2681.md` contains an internal note at the top, marked for deletion before posting. It must be removed before the draft is used.
- `pr-drafts/highlighting/SOLR-4540.md` contains the OPEN line at its Proof. It must be filled before the draft is used.

## Not done

No PR was opened, commented on, or changed. No submit branch was edited, and no Jira item was touched. No build, Gradle run or test was run.
