# Pool assignment: Replication and backup convergence, round 2

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: 1. Claim path: claims/pool-replication-backup-convergence-round-2.md.

## Purpose

Round 1 (`reports/replication-backup-convergence-round-1.md`) returned NOT CONVERGED: 8 of the 9 drafts were NOT CONVERGED, with 2 blocking, 11 should-fix and 10 note findings. The main side has applied the holding findings and written its adjudication in `reports/replication-backup-convergence-round-1-response.md`, including two receipt corrections the round flagged. This round is a confirmation round: it re-checks only the drafts round 1 marked NOT CONVERGED, verifies each disposition in the response against the live draft text, and settles the items whose state may have moved since.

## Scope

- The 8 drafts round 1 marked NOT CONVERGED: SOLR-8430, 9091, 9382, 9598, 9865, 11650, 12085, 12246, all in `pr-drafts/replication-backup/`.
- SOLR-17287, which round 1 marked CONVERGED, is out of scope as a draft; its receipt correction is a main-side record item and is not re-audited here.
- Settled decisions recorded in `material/replication-backup-round-1-answers.md` are not reopened, including the landing orders (RestoreCore 9865, then 17287, then 9091; IndexFetcher 12246, then 12085, then 11650, with 6711 last; 9598 before 12651). Gate state is settled by the receipt in `receipts/` at the exact live tip.

## What to check

1. For every finding listed against an in-scope draft in the round 1 register, read the live draft and confirm the response's disposition: applied (the replacement text is present, word for word in substance), branch-side (the item is recorded in the response with its replacement text), or standing. Flag any disposition the live text does not match.
2. SOLR-8430, RB-R1-01 (the open blocking finding): re-check the state. Read the live tip of `solr-8430-submit` with `git ls-remote origin`. If the limiter cap has landed and a focused re-run at the new head is recorded, judge the draft against the register's post-cap text: the first Limits bullet replaced, the "map holds at most 64 rate values" sentence present, links and Proof at the new head. If the tip is unchanged, confirm the finding stays open and the draft stays held.
3. Branch-side items RB-R1-03 (SOLR-8430 changelog title), RB-R1-04 (SOLR-9091 changelog title) and the SOLR-12085 changelog title: check whether the fragment commits have landed on those branches. If one has, check the fragment title against the replacement recorded in the response (for SOLR-12085, against the round 1 status item) and that the draft's links and Proof point at the new head. If none has landed, record the state and move on.
4. SOLR-9865: confirm the draft carries the third route in its Choice, the prescribed Limits sentence, and the OWNER NOTE hold. Judge the text only; whether the packaging call has been made is reported as state, not decided here. If the write-back has been dropped at packaging since the response, check What this change does and the Proof against the packaged head instead.
5. SOLR-11650: check the Proof against the corrected receipts/SOLR-11650.md (the merge-base is `14c7aac0d151`). If the owed title, javadoc and test-comment commits have landed, check the draft's citations and Proof head against the new tip; if not, confirm the hold state.
6. SOLR-9598: confirm the OWNER NOTE now records the adopted landing order (9598 before SOLR-12651) and that the two should-fix replacements are in the text. The draft's remaining hold items are owner state, reported, not judged.
7. Fresh eyes, narrowly: a claim in an in-scope draft that contradicts its receipt or the branch code and that neither round 1 nor the response records is a new finding. Anything else (style, length, settled decisions, the recorded landing orders) is out of scope.

## Findings register

Carried findings keep their round 1 IDs (RB-R1-nn) and are marked CONFIRMED, STILL OPEN, or SUPERSEDED with the evidence. New findings get IDs RB-R2-nn with draft, grade, the exact text at issue, the evidence, and exact replacement text for blocking and should-fix grades.

## Deliverable

`reports/replication-backup-convergence-round-2.md`, with:

- Counts up front: drafts re-checked, dispositions confirmed, dispositions disputed, items still open.
- A verdict per in-scope draft: CONVERGED or NOT CONVERGED.
- The register.
- A convergence line: CONVERGED for a draft only when no blocking or should-fix finding is open against it.

## Limits

Read-only apart from the claim and the report. No draft edit this round; the main side applies any further replacement text. No PR, comment, review, branch edit, live PR text edit, build, test or gate. Nothing posted or filed.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
