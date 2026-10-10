# Pool assignment: review-confidence round 2 (verification of finished drafts, live bodies, and three unverified premises)

Capability tags: `review`, `draft`. Staffing: up to 4 (slices in parallel; Slice A may split by category). Claim path: claims/pool-review-confidence-round-2.md (slice-level claims allowed, for example claims/pool-review-confidence-round-2-a.md).

Background: the category rounds are done and their answers are adopted. The drafts written from those answers have had one writing pass and no independent verification pass. This round is that verification pass: it re-checks finished work against the decisions already recorded and the gate receipts, at the branches' live tips. It opens no new audits and takes no decisions. Anything that would change a claim, a number, or a decision is flagged in the report for the main agent, never settled inside this round. Intended host: the review agent (all three slices are reading and draft work; no builds).

## Slice A: draft verification against the adopted answers

Categories and their adopted answers files:

- SolrCloud: material/solrcloud-round-1-answers.md, drafts in pr-drafts/solrcloud/
- Core admin: material/core-admin-round-1-answers.md, drafts in pr-drafts/core-admin/
- Replication and backup: material/replication-backup-round-1-answers.md, drafts in pr-drafts/replication-backup/
- Highlighting: material/highlighting-round-1-answers.md, drafts in pr-drafts/highlighting/
- Spellcheck round 5: material/spellcheck-round-5-answers.md, drafts in pr-drafts/spellcheck/
- Suggester round 4: material/suggester-round-4-decisions.md and material/suggester-round-4-answers.md, drafts in pr-drafts/suggester/

For every draft in those folders, verify all four of these:

1. Decision fidelity. The draft matches the adopted answers: its title, every Choice it poses, and every Limits line are the ones the answers record. Where an answers file records a DISCUSS item still awaiting Nick, the draft must pose the call as recorded; a draft that silently takes the call fails this check.
2. Proof numbers. Every number in the draft's Proof section matches the ticket's receipts/<TICKET>.md. Check the receipt against the branch's live tip (ls-remote at claim time): if the branch has moved past the receipt's head, the draft is flagged as written against a moved branch, not silently passed.
3. Citations. Every citation link points at the draft's branch head SHA, and the cited lines show what the surrounding text claims. A link at a stale head, or a line range that does not contain the claimed code, fails this check.
4. Plain language. The draft follows pr-formula.md and carries no internal process vocabulary: no gate, receipt, or ledger wording, no run identifiers, no internal labels for proof steps. Proof is stated plainly: what ran, at which head, what passed, and that the new test fails without the fix.

Fixing rights: wording defects and citation defects (wrong head, wrong line range) may be fixed directly in the draft file, with each edit named in the report. Anything that would change a claim, a Proof number, a Choice, a Limits line, or a title's meaning is flagged, not edited.

Deliverable additions: reports/review-confidence-round-2.md, one verdict per draft: PASS, FIXED (with the edit named), or FLAGGED (with the issue stated exactly).

## Slice B: live body consistency for the 28 update-processing PRs

The reference texts are the drafts in pr-drafts/update-processing/. The live bodies were applied from those drafts and corrected once already; since then three branches changed under their bodies. Re-verify each of the 28 live bodies against its branch's current head and gate state, and give the three changed branches a close read:

- SOLR-5887: test-only fix fa60337f88b landed after the body was applied and was re-gated green. Check every claim in the body against the new head, including anything the body says about the tests the fix touched.
- SOLR-13943: the changelog fragment was restored, head ca443f6f680. Confirm the body's changelog and scope statements still describe the branch exactly.
- SOLR-11483: a false Limits bullet about an unclosed UpdateLog was already removed from the body. Confirm nothing else in the body depends on the removed claim (no Proof line, Choice, or other Limits line that assumes it).

Read the live bodies through the GitHub API. If a body cannot be read from this host, flag that ticket as unread rather than guessing from the draft.

Deliverable additions: a section in reports/review-confidence-round-2.md, one line per ticket: CONSISTENT, or the exact drift (the body text, the branch fact it no longer matches, and where the two diverge). Body edits are not made under this assignment; drift is reported for the main agent, which holds the standing authorization for body edits.

## Slice C: premise re-checks by code reading for three NO GATE branches

Three branches carry receipts marked NO GATE with premises that were never verified by a run, and no other assignment covers them. For each, code-read the premise against current main (the branch named in the ticket's receipt is the change under review) and return one of two verdicts: the premise holds, with a run-ready premise spec; or the premise is dead on current main, with the code evidence.

- SOLR-11678 (receipt receipts/SOLR-11678.md): its own testing note admits the reproduction was guessed and never run. This ticket's audit home is the Security round, which recorded that admission; the premise itself has never been checked.
- SOLR-9852 (receipt receipts/SOLR-9852.md): Streaming round 1 recorded NO GATE, audit only.
- SOLR-10882 (receipt receipts/SOLR-10882.md): Streaming round 1 recorded NO GATE, audit only.

A run-ready premise spec names: the module and the exact test class or reproduction steps, the input or scenario that triggers the premise, the failure or behavior the run must show on current main, and the branch behavior that would refute it. Precise enough that the gate backlog can execute it without further reading. No runs are made under this assignment; the spec is the deliverable.

Deliverable additions: a section in reports/review-confidence-round-2.md with one verdict per ticket and, where the premise holds, the full premise-run spec.

## Rules

WORKFLOW.md binds: claim before work, the subagent cap across hosts, heartbeat while a claim is active, and claims marked DONE in the same push as the deliverables. One report file serves all three slices; slice claimants coordinate through their claim files so the report lands once, complete. No builds or test runs under this assignment. Decision slates, answers passes, and PR openings stay with the main agent, as do edits to live PR bodies.
