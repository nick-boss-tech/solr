# Backlog first-100, Unit 4 report (vm4, round 1)

Status: BLOCKED. 0 of 25 drafts filled. Shortfall named below. No `jira/` drafts were written for this unit, because the family wording does not hold for most of the eleven tickets.
Claim: `claims/backlog-first-100-unit-4.md`. Blocked result: `claims/backlog-first-100-unit-4-blocked.md`.
Board tip at check: `6e04c404c7` (2026-10-26T00:30:35Z UTC). Records read: `material/harmonized-backlog/batches/h1-001..016-rev4.json` (latest revision per batch).
Live checks: Jira REST at `issues.apache.org/jira/rest/api/2`, read on 2026-10-26 UTC.

## Why the unit cannot reach 25

Unit 4 is `closure-duplicate` (8), `ping-stale` (11), and `closure-not-a-bug` 26 to 31 (6). Under the pack rules as written:

| Family | Records | Excluded by rule | Deliverable now | Blocker |
| --- | --- | --- | --- | --- |
| closure-duplicate | 8 | 8 (conflict flag) | 0 | Conflict flag from the superseded Bug rule. Also no surviving-ticket target in any record, and the skip queue the records cite is not on the board. |
| ping-stale | 11 | 0 | 11 | Family wording does not hold (see below). |
| closure-not-a-bug 26 to 31 | 6 | 6 (conflict flag) | 0 | Conflict flag from the superseded Bug rule. |

Shortfall: 14 of 25 under the current rules (7 duplicates plus 6 not-a-bug records are blocked by conflict flags, and one more of the eight duplicates is flagged).

Across the pack, under the rules as written, only the 11 ping-stale records are deliverable. The other families are blocked as follows:

- **Conflict flags on Bug records.** Every closure-not-a-bug record (127) carries `bug in a closure disposition: barred from closure families; flagged for severity screen`. Those flags come from the round-1 "no Bugs" setting that the owner superseded for this pack (`assignments/backlog-first-100-pack-round-1.md`). The pack rule still excludes conflict-flagged records, so the flags block the whole population. The same flag appears on 77 closure-fixed, 67 closure-obsolete and 7 closure-duplicate records. Records are add-only, so I did not edit them; clearing the flags needs a rev5 of the batch records, written by whoever owns the store.
- **Duplicate targets.** No record names the surviving ticket. The template requires it, and the verdict source (`queue-current.json`) is not on the board.
- **Source files.** The records cite `queue-current.json` and `material/jira-issues-csv/solr-jira-metadata-2026-10-18-1792393324869.csv`. Neither is on `coordination-ui`. The ping-stale template takes its date and reporter from the CSV, so I took them from live Jira instead (same field, `updated` and `reporter`).

## The ping-stale wording does not hold

The ping-stale template says: "A review of the ticket text found no steps that could be tested, so it has stayed unconfirmed." I read each description. That sentence is false for several of the eleven:

| Key | What the ticket text contains | Template sentence holds? |
| --- | --- | --- |
| SOLR-6678 | Reload loop reproduction steps (about 1k reloads) and a heap dump | No. 2015 comment by Vitaliy Zhovtyuk: "cannot reproduce" |
| SOLR-7947 | Node restart and stack trace | Partly. Scenario given, no confirmed repro |
| SOLR-9492 | A split-shard API call and its response | No. 2018 comment by Jason Gerlowski: not reproduced |
| SOLR-10706 | A SolrJ query example that shows the encoding difference | No |
| SOLR-11776 | A broken-pipe scenario, no steps | Yes |
| SOLR-13853 | An upgrade scenario, no steps | Yes |
| SOLR-15571 | A query with specific data | Partly |
| SOLR-16062 | A cross-environment LTR comparison, no steps | Yes |
| SOLR-16792 | A stack trace from a corrupt index. Eric Pugh asked in 2025 for steps and got none | Partly. The ping itself fits |
| SOLR-17376 | A code snippet that builds the client, and a leak description | No |
| SOLR-17747 | Schema and merge config, and an optimize that does not run | No |

Proposed sentence, accurate for all eleven because no comment records a confirmed reproduction on a supported release:

> [~{REPORTER}] I am checking in on this ticket. Its last activity was on {DATE}. The ticket does not record a confirmed reproduction on a supported Solr release. If it still happens on a supported release, a reply with the version and steps will keep it open.
>
> 🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

The family file is not approved text, so the change is for the owner or main side to accept. The wording above is the basis for the eleven drafts, once accepted.

## Live check, the eleven ping-stale keys (2026-10-26 UTC)

All eleven: Open, Bug, Major, no resolution, no PR link in comments, description, or remote links, no comment by Nick Shanin, and not reported by him. The SOLR-6678 issue links (SOLR-9117, SOLR-14940) are closed references, not PRs. The SOLR-17376 remote link is a Jetty issue, not a PR.

| Key | Reporter (Jira username) | Last updated (live) | Comments | Notes |
| --- | --- | --- | --- | --- |
| SOLR-6678 | alexey.serba | 2020-10-16 | 4 | 2015 "cannot reproduce" comment |
| SOLR-7947 | forest_soup | 2015-08-20 | 1 | |
| SOLR-9492 | shalin | 2020-07-22 | 2 | 2018 "not reproduced" comment |
| SOLR-10706 | backslash47 | 2019-06-08 | 0 | |
| SOLR-11776 | caomanhdat | 2019-06-08 | 0 | |
| SOLR-13853 | thefoolwhocodes | 2020-01-21 | 0 | |
| SOLR-15571 | kennyKnecht | 2021-09-28 | 0 | |
| SOLR-16062 | shailesh.randive | 2022-05-12 | 0 | |
| SOLR-16792 | aminadinari19 | 2025-11-29 | 1 | Eric Pugh asked for steps (assignee) |
| SOLR-17376 | veita | 2025-03-24 | 4 | Comments by David Smiley and Michael Gibney |
| SOLR-17747 | knraji | 2025-04-29 | 0 | |

Raw Jira JSON for each key is kept on the VM under `/workspace/tmp/unit4/raw/`.

## Decisions needed

1. **Conflict flags.** Clear the superseded Bug-barred flags in a rev5 of the batch records, or keep them. Clearing unlocks up to 125 closure-not-a-bug, 77 closure-fixed (plus its fixing-version check), 67 closure-obsolete and 7 closure-duplicate records, subject to the other pack checks.
2. **Ping-stale wording.** Accept or amend the proposed sentence. Then the eleven drafts can be filled.
3. **Duplicate targets.** Provide the skip-queue file, or the surviving tickets, so the seven duplicates can be filled.
4. **Metadata source.** Accept live Jira as the source for reporter and last-updated, or provide the metadata CSV.

## Not done

- No drafts written; nothing posted to Jira or GitHub.
- No edits to rev4 records, the pack files, the family files, or other hosts' claims.
- Spot-QA is a separate claim for a second host and was not started.
