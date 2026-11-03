# PR 5282 reply posted after explicit owner authorization

Status: REPLY POSTED AND READ BACK; IMPLEMENTATION PROOF STILL PENDING.

- Comment: https://github.com/apache/solr/pull/5282#issuecomment-6611035441 .
- Posted: `2026-11-03T17:34:02Z`, author `nick-boss-tech`.
- PR/fork head checked separately: `200ff5e5c6c214212f72f37485487f3092e05541`, unchanged before posting and after read-back.
- Original head used for the pinned diff: `6ee3c9b6370e6e1e3b4da4afcf968a0806cc747f`.
- Evidence snapshot: coordination-ui `5df312a3330141379a67d7def307efc3f1f98f7f`, `receipts/PR-5282-menu-refresh-round-1.md`.
- Authority: Nick explicitly released the earlier publication hold and instructed posting, then pointed to the newly posted receipt. This post uses that explicit permission, not the standing clean-result exception.

## Actual result reconciled before publication

The receipt explicitly says NOT GREEN. It covers successful JavaScript syntax checks, full tidy with a clean tracked tree, and `:solr:webapp:check -x test` at the exact PR head, with JDK 21/Gradle 9.7.0. The new Selenium suite compiled and discovered nine tests, but all nine were skipped with the Selenium group disabled; the host lacked a usable Chrome installation. The positive browser suites, both fail-before overlays and root check did not run.

The owner was told of this discrepancy before posting. The reply reports those exact limits rather than claiming that the full required proof passed. The upstream Admin UI Tests run at this head was separately checked and reported `action_required`: https://github.com/apache/solr/actions/runs/41680303677 . No tests, checks, formatting repair, source edit or push occurred on Windows during this posting task.

## Publication checks

The live discussion contained only Eric's original substantive comment, with no equivalent reply, immediately before posting. The reply was drafted locally, checked against the current head/discussion, and posted once with the absolute-path `--body-file` form. Separate live PR read-back confirmed exactly one authored comment matching the draft byte-for-byte, unchanged head, no other new comment and no new review. A separate comments API read confirmed the stored body, author, UTC creation time and URL.

The public text answers Eric's alternatives and nearby-issue question, explains selection retention, obsolete callback rejection and alias view synchronization, links the pinned diff, and puts partial verification plus its limitations in a collapsed details block. It does not claim that model deletion automatically triggers ng-change on every page load.

Local session files: `research/scratchpad/pr-5282-reply-20261103/` (draft, before/after JSON, posted URL and verification helper). The earlier hold recorded in `reports/pr-5282-description-update-comment-hold-20261103.md` was released by Nick for this reply. The required Linux proof remains incomplete; the original gate stays OPEN. No PR body edit, JIRA comment, approval, merge or status conversion was part of this publication.
