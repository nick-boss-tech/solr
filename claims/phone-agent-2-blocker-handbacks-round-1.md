# Claim: phone-agent-2-blocker-handbacks-round-1

- Claimant host: phone-agent-2, matching `hosts/phone-agent-2.md`.
- Status: ACTIVE; completing the SOLR-17840 slice only.
- Job or assignment: `assignments/phone-agent-2-blocker-handbacks-round-1.md`.
- Slice: finish and validate the SOLR-17840 timeout fix on current Apache main, preserving the unchanged LTR regression and reporting exact evidence.
- Capability tags used: review, implementation, gate.
- Started: 2026-10-23T01:38:17Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat, and then takeable over under WORKFLOW.md, Claims. Attempts: 1. Idempotency key: SOLR-17840@21bf1dbb6ea2365cb3be7caab74ea853af74dd0d.
- Links: Ticket: SOLR-17840. Category: Search and query components, secondary Test quality. Siblings: SOLR-17631, SOLR-18539 and SOLR-18541; the receipt says the latter two use different QueryComponent paths. Supersedes: `claims/phone-agent-2-blocker-patches-round-2.md` for this revalidation only; the completed claim remains terminal.
- Initial evidence: current fork candidate `21bf1dbb6ea2365cb3be7caab74ea853af74dd0d`, with GREEN receipt `receipts/SOLR-17840.md`; its recorded base is `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`. Current Apache main is `5b2ac4dea21a7c86e31e2ba0cea58c262dc558c8`. None of the touched files changed between the recorded base and that Apache main. Nick's fork `origin/main` is an older, separate ref at `498a09aea97aac86c2cc7ac114113b946d249942`.
- Decision ladder: Rung 5, owner choice. The base options were Nick's fork `origin/main` at `498a09aea97aac86c2cc7ac114113b946d249942` and Apache `upstream/main` at `5b2ac4dea21a7c86e31e2ba0cea58c262dc558c8`. Nick said, "probably upstream Solr for the base" on 2026-10-22. Use Apache `upstream/main` at `5b2ac4dea21a7c86e31e2ba0cea58c262dc558c8` for this gate and submission branch; do not change the base without fresh owner direction.
- Plan: inspect the two existing fix commits against current Apache main, prepare the submission branch using `solr-17840-submit`, run the preserved focused regression and required full module gate on its exact head, then report the disposition and evidence. Keep the existing candidate branch unchanged.
- Constraints: no Jira or GitHub posts/comments, no PR opening or ready flip, no test weakening, no force push. Builds serialize under the shared lock and disk/inode guard; no gate-green claim without evidence from the current head.

Heartbeat: 2026-10-23T01:38:17Z
