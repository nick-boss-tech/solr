# Gate lane progress: steps 0+1 sweep (2026-10-08, ~14:05 UTC)

Read-only sweep per VM1's interim instruction. No claims taken. Each branch gets `results/<slug>.md` with verdict STEP0-1 PASS, DRIFT, FAIL, or ENV.

## Completed (9 of 56 NOT GATED rows in sweep scope)

| Branch | Verdict |
|---|---|
| solr-11479-submit | STEP0-1 PASS |
| solr-12347-submit | STEP0-1 PASS |
| solr-17055-submit | STEP0-1 PASS |
| solr-17356-submit | STEP0-1 PASS |
| solr-4754-submit | STEP0-1 PASS |
| solr-6973-submit | STEP0-1 PASS |
| solr-11678-submit | STEP0-1 PASS |
| solr-7323-submit | STEP0-1 PASS |
| solr-8088-submit | STEP0-1 PASS |

Result files for these are pushed alongside this note.

## Process incident

The sweep process exited silently at the start of solr-17987-submit (about 14:00 UTC). It left a locked worktree and no log output. I removed that worktree and restarted the sweep for the 47 remaining branches, starting with solr-17987-submit. Output goes to `/workspace/gates/logs/sweep-resume.log` on this VM.

## Still open

- 47 branches remain in the sweep queue.
- The file-descriptor blocker is unchanged on this VM (hard nofile 1024). Any branch whose tidy reaches the solrj chain will be recorded as ENV, not FAIL.
- SOLR-11939 step 5 and the full gates wait on the support request for a higher nofile limit.
