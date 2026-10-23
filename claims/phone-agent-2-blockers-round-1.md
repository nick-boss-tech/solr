# Claim: phone-agent-2-blockers-round-1

- Claimant host: phone-agent-2, matching `hosts/phone-agent-2.md`.
- Status: DONE (13 evidence-backed classifications; one green fix, two explicit implementation handbacks).
- Job or assignment: `assignments/phone-agent-2-blockers-round-1.md`.
- Slice: all 13 named Blockers, evidence-first classification; narrow justified implementations and full gates only where the premise holds.
- Capability tags used: review, draft, records, implementation, gate, premise-run.
- Started: 2026-10-22T23:25:03Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: phone-agent-2-blockers-round-1@38e4a6d9d72d2ce100097622989baa679ee1c824.
- Links: Parent: assignments/phone-agent-2-blockers-round-1.md. Category: cross-category Blocker review. Siblings: existing SOLR-18297 records, read before any duplicate work; SOLR-14200 and SOLR-16155 excluded. Supersedes: none.
- Constraints: no Jira or GitHub posts/comments, no PR openings or ready flips. Git commits as Nick Shanin without trailers. Token is process-scoped only.
- Local paths: coordination worktree `/workspace/phone-agent-2-coordination`; source worktree to be pinned to fresh upstream main. JAVA_HOME `/usr/lib/jvm/java-21-openjdk-amd64`; builds serialized under `/workspace/tools/.gradle-$(hostname -s).lock` with disk guard.
- Decision ladder: rung 1, exact assignment and standing rules apply. Prior finished or live ticket work will be cited, not duplicated.

Heartbeat: 2026-10-22T23:25:03Z

Progress: 2026-10-22T23:36:23Z. Fresh public Jira reviews completed for all 13. Source pinned to upstream main b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb. Narrow candidate branches in preparation for SOLR-17844 and SOLR-18446. SOLR-17840 ignored timeout test enabled only in a disposable premise worktree; run evidence pending. SOLR-18398 explicitly deferred in Jira until closer to 11.0 release; no premature broad API migration. No external posts or PR actions.

Heartbeat: 2026-10-22T23:53:26Z. SOLR-17844 first head gate failed one test after a successful discriminating mutation; returned to implementation, no green claim. SOLR-18446 exact tidy output committed and pushed as 5dcdf0d2bce40a9e83b9d19567beb6785844335f; one permitted full retry will run from step 0. SOLR-17840 enabled test executed once and failed with an LTR timeout response containing no hits; diagnostic boundary probe is being prepared, no speculative patch.

Heartbeat: 2026-10-23T00:12:19Z. SOLR-17844 full fresh gate GREEN at fork-read-back 08ecc90ff7b5505e576985c8dcb514393314ce2b, with 2 passing focused tests, discriminating mutation and module check. SOLR-18446 terminal gate HANDED BACK after failed permitted retry; compiler-named import correction pushed as unverified 8bb60e6f61abc9827fb58dcae1863cabb33f9796, no third attempt. SOLR-17840 diagnostic proves 4 fallback hits are lost only at QueryComponent response exposure; narrow reorder plus unchanged enabled regression pushed as 3999ff0be34bd192c2325b9138b8aa8a7b5bc24d, full gate next.

## Terminal delivery and clearing

Completed: 2026-10-23T00:20:26Z. Deliverable: `reports/phone-agent-2-blockers-round-1.md`, delivered in this same atomic push with this DONE mark and the assignment status. The report is a completed classification round with explicit partial implementation delivery, not a statement that every upstream ticket is resolved.

| Ticket | Terminal round disposition and clearing action |
| --- | --- |
| SOLR-12833 | WON'T DO, shape 1: existing timeout implementation b9a966e5f7 and current code stand; this round's duplicate implementation slot closed, no duplicate claim/branch created. |
| SOLR-16450 | WON'T DO, shape 1: initial fix plus post-reopen 32e0f68a9b stand; duplicate slot closed, no duplicate claim/branch created. |
| SOLR-17463 | DONE classification, NOT-REPRODUCIBLE from available triggering data; full dump and sequence requested in owner-only draft. Not falsely cleared as fixed or structurally terminal. |
| SOLR-17546 | DONE sampled-URL classification, NOT-REPRODUCIBLE in two probes; complete redirect policy remains unverified. |
| SOLR-17840 | HANDED BACK implementation at 3999ff0be34bd192c2325b9138b8aa8a7b5bc24d: response-exposure candidate reveals a second fetch/streaming timeout failure. Full receipt written; no expected-value repair or out-of-scope retry. |
| SOLR-17844 | DONE, GATE GREEN at 08ecc90ff7b5505e576985c8dcb514393314ce2b. Fresh full rework gate and receipt stand; original failed attempt preserved. |
| SOLR-17988 | WON'T DO, shape 1, strictly for the named transitions: published property guidance and 7f635a9f31 stand; duplicate guidance-edit slot closed, no exhaustive audit claimed. |
| SOLR-18240 | WON'T DO, shape 1: published 9.11 section and ce063d9877 stand; duplicate guide-edit slot closed. |
| SOLR-18297 | WON'T DO, shape 1: merged PR #5059/eaf9c86369 and DONE claims/pool-owner-items-solr-18297.md stand; this duplicate upgrade slot closed without reviving/editing any terminal claim. |
| SOLR-18398 | DONE classification, STILL-PRESENT and explicitly deferred in Jira. No premature cross-tree API migration or false WON'T DO clearing. |
| SOLR-18446 | HANDED BACK after permitted retry; subsequent one-line import correction at 8bb60e6f61abc9827fb58dcae1863cabb33f9796 is unverified. Receipt preserves both attempts, no third gate run. |
| SOLR-18486 | DONE non-code classification, STILL-PRESENT release-owned sign-off. Populated beta guide is not approval. |
| SOLR-18501 | DONE non-code classification, STILL-PRESENT PMC IP-clearance process. Draft paperwork is not filing or approval. |

Decision record: rung 1 applies the assignment, Jira's migration timing instruction, and refusal/repair rules; rung 2 runtime boundary evidence justifies only the demonstrated LTR response-exposure candidate; independent Lucene partition evidence justified the separate field-cache implementation rework. No ungrounded production hardening, owner-decision invention, or request to reauthorize an already-covered write. No Jira or GitHub issue/PR posts/comments, PR openings, ready flips, force pushes, or edits to another host's terminal claims.
