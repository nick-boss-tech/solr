# Draft PR record check, round 6

Read-only. Two draft PRs on apache/solr were checked against their local drafts and receipts. Nothing was changed on GitHub, in the repository, or in JIRA.

## Result

| PR | Ticket | State | Head matches | Body matches draft | Comments and reviews | Action needed |
|---|---|---|---|---|---|---|
| #5117 | SOLR-18541 | Open, draft, last activity at opening | Yes, `d5ed34125db9` | Yes | None | Date text: the draft says 2026-10-12, the API says 2026-10-13 05:13 UTC |
| #5118 | SOLR-18010 | Open, draft, last activity 11 s after opening | Yes, `c3685bb37d9d` | Yes | None, no change requests | Decision for Nick: see below |

## PR #5118 (SOLR-18010): decision needed

- Opened at 2026-10-13 05:41 UTC (API). The receipt says 2026-10-12, which is only correct in Pacific time.
- It was opened after `reports/solr-18010-overlap-comparison-round-1.md`, which recommends combining with PR #5065 and not opening a competing PR. That report is waiting on Nick's approval of a draft comment.
- Overlap: PR #5065 is open on the same ticket with an approval and a stalled review. #5118 is the competing PR the comparison advised against.
- Labels on #5118: `tests` and `cat:security`. Mergeable, with mergeable state "unstable". Checks not queried.
- Recommendation: keep #5118 as a draft, and do not mark it ready, until Nick decides between combine and open. Closing it is a public action and needs Nick's direction.

## PR #5117 (SOLR-18541): date text

- The draft's staging note says "opened 2026-10-12" and the Proof says "Verified 2026-10-12". The API says 2026-10-13 05:13 UTC.
- The body is public and matches the draft, so the date sits in the live PR. Correcting it is an edit to a live PR description, which scheduled rounds do not make. Nick decides whether to edit it.

## Other notes

- #5118's body says "Verified 2026-10-05 at this head". The receipt agrees on the gate date and records a later changelog check on 2026-10-10 that the PR text does not mention. This is a gap, not a conflict.
- #5118's Proof links point at base `14c7aac0d15`. The PR base is `597ed677` on main. I did not check whether `14c7aac0d15` is an ancestor of main.
- The fork repository's `pushed_at` is 2026-10-13 05:42 UTC, after #5118 opened. Nobody checked which branch was pushed. Check the fork's branch refs before any further change to `solr-18010-submit`.

## Not done

- No PR, comment, JIRA write, or email.
- No edit to either PR, its body, or its branch.
- No build, test, or gate run.
- The receipts' test counts are unverified here; the vm1 gate logs are not visible from this host.
