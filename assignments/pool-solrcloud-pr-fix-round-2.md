# Pool assignment: SolrCloud PR drafts, fix pass, round 2

Status: DONE

Capability tags: `review`, `draft`. Intended host: windows (review coordinator). Staffing: four subagents, one per group below. Claim: `claims/pool-solrcloud-pr-fix-round-2.md`.

## Why this round

`reports/solrcloud-pr-critical-review-round-1.md` rated all nineteen drafts not submittable: thirteen HOLD and six NOT READY. This round applies the fixes that the drafts can take now, and writes the gate needs for the rest. The goal is a set of drafts that can be posted once Nick approves each opening.

## Rules for every draft

- Read `pr-formula.md` first. Section order: header, Jira link, What happens today, What this change does, Proof, A choice to check (only for a live alternative), Limits, Changelog, AI assistance. Every body section opens with a bold one-line summary.
- Edit only your assigned drafts in `pr-drafts/solrcloud/`. Do not edit receipts, gates, reports, claims, or any other draft.
- Proof numbers come only from the receipt in `receipts/` or a gate file on `pr-prepare`. Never from memory. Every count carries its head and its verification date. If the receipt does not carry the number you need, do not invent one: leave the draft's Proof as the receipt records it and list the gap in your report as a gate need.
- Verify the head. The head the draft cites must equal the receipt's gated head, and the receipt's head must equal the live fork tip. Check the live tip read-only with `git -C C:/Users/shaninna/dev/Solr-issues/source ls-remote origin refs/heads/<branch>`. If they differ, do not change the Proof's numbers. Name the gap in your report.
- JIRA fit. Fetch the ticket read-only: `C:/Users/shaninna/AppData/Local/Temp/2/claude/C--Users-shaninna-dev-Solr-issues/1a4dfad7-722c-4e59-ab2e-961faf1441e1/scratchpad/jira-read.ps1 -Keys SOLR-NNNNN` (read its header first). Read the output file it writes. Never print a token or open the token file. Never write to JIRA.
- Choices. A Choice appears only when a maintainer could plausibly pick the other route. Adopt the recommendation recorded in `material/solrcloud-round-1-answers.md` or in the critical review when one exists. Where none is recorded, take the harder, more thorough option, and say so in your report. Each Choice ends with a pointed question. It states the cost of the alternative.
- Limits name what is not covered, including every JIRA request the draft does not answer. Name them plainly. Do not offer follow-up work unless a Limits line already requires it.
- Plain language: short sentences, one idea each, no internal process words (claim, probe, harness, round, receipt, gate, vm1, vm2, draft, scratch, export, pool). No em dashes or en dashes.
- Length: aim for 3,500 visible characters or fewer for the whole description. If a draft runs over, cut repeated claims first, then examples. Keep every number and citation.
- Citations are real links. Code the change produces links the branch head SHA. Pre-change symptoms link the merge-base, and the text says so.
- AI header at the top, and the AI assistance footer, on every draft.
- Do not run Gradle, builds, tests, or gates. Do not push, commit, or post. The coordinator commits.

## Output (each subagent)

Return under 600 words:

1. For each draft: changed or unchanged; one line per change made; visible-character count after the edit.
2. Blockers that text alone cannot clear, each with: the ticket, the missing fact, and what would settle it (a gate, a branch change, or a decision for Nick).
3. Gate needs, each as: ticket, head, what to run, and why.
4. Any receipt or JIRA fact that contradicted the critical review. Quote it.

## Per-ticket fixes

Each entry gives the critical review's finding. Apply what the draft can take now. Record the rest as a blocker or gate need.

### SOLR-5813 (HOLD)
- Finding: the draft defaults the empty name where the title asks for a clean failure. The reporter asked for a failure in SolrCloud mode.
- Fix: keep the adopted default (the answers file records it). Name the reporter's failure request in Limits or the Choice, and state the cost of the failure route. Trim to length.

### SOLR-11288 (NOT READY)
- Finding: the Proof cites the old head `cd094c3c623`. The fail-before is not recorded at the live head.
- Fix: cite the receipt's live-tip head and its counts. If the receipt has no fail-before at that head, record it as a gate need. JIRA gap: the reporter's failure is a comma inside a value, and escaping is not added. Add a Limits line that says so. Trim to length (about 4,265 now).

### SOLR-11479 (HOLD)
- Finding: the draft lands after SOLR-15035, with no link or state. No verification date.
- Fix: state the landing order: after SOLR-15035, per the answers file. Add the verification date and head from the receipt. JIRA: the multi-replica 400 is outside the ticket. Add that as a Limits line.

### SOLR-12651 (NOT READY)
- Finding: the Proof count belongs to the prior head `f3131d1ee846`, not the live head `404f13b501c9`.
- Fix: use the receipt's count at `404f13b501c9`, with its date. If the receipt does not record that head's counts, record a gate need. JIRA: the reporter asked for DEBUG-level copy logs. Add a Limits line. The changelog title overstates the cleanup; the answers file gives the corrected wording (part p2, finding 9). Check whether the branch already carries it, read-only.

### SOLR-12991 (HOLD)
- Finding: the Choice omits INFO, which the commenter suggests.
- Fix: add INFO to the Choice as a third option, with its cost. Keep WARN as the implemented route.

### SOLR-13186 (HOLD)
- Finding: the receipt now has the dated run (2026-10-13, one test, no failures, at the live head). The draft's Proof does not carry it.
- Fix: add that run to the Proof with its date and head. Keep the Limits line that says the test covers only the skipped-start path.

### SOLR-13369 (HOLD)
- Finding: the seed narration is process and should go. The cause should be labelled as the reporter's reading. No verification date. The reporter asked test-or-router, and the draft changes the test.
- Fix: remove the seed narration. Label the cause as the reporter's reading. Add the verification date and head. Add a Limits or Choice line that answers test-or-router. Length is about 2,908 now, so there is room.

### SOLR-14919 (NOT READY)
- Finding: the new head is `8f1f4ef91e79` (a javadoc rewrap only). Gate `c1fa37e33c1` failed Spotless. Links and the Proof cite `c1fa` and `84e7bcaeb57`, which still has the recovery marker. The ticket is about replication; the draft fixes forwarded commits only.
- Fix: the Proof and the links move to `8f1f4ef91e79` only after the gate receipt for that head exists. Until then, record the re-gate as a gate need (the job file `gates/SOLR-14919-processor-only-gate.md` is claimed and running). Add a Limits line: the replication failure in the ticket is not covered. Trim (about 4,068).

### SOLR-15035 (HOLD)
- Finding: there is no Choice on numShards (all slices or active only, or drop it). Failing-run counts are missing. Consistency is delivered; the reporter's numShards question has no choice.
- Fix: write the numShards Choice. Read the code at the live head to name the implemented route, then state the alternative and its cost. The answers file says the split case stays a Limits bullet. The Proof placeholder (the observed base failure line) cannot be quoted from records on disk. Reword the Proof to the receipt's recorded outcome, as the answers file allows.

### SOLR-15106 (NOT READY)
- Finding: the draft keeps every `return` and resigns leadership instead. The base compile message is not recorded. The title asks that the thread not return. The ZooKeeper outage is not reproduced.
- Fix: the Choice states the title's request and the implemented route, with the cost. Add a Limits line that the ZooKeeper outage is not reproduced. The base compile message needs the premise run (`gates/SOLR-15106-premise-base.md`); record it as a gate need. Trim (about 4,076).

### SOLR-15386 (HOLD)
- Finding: no verification date. The Choice's first alternative gives no cost. The restart symptom matches. A commenter's broader idea is not in Limits.
- Fix: add the verification date and head from the receipt. Add the cost to the first alternative. Add the commenter's broader idea to Limits, named.

### SOLR-15674 (NOT READY)
- Finding: the branch history carries a `Co-Authored-By` trailer on `9b51865402d`. The scrub is a gate job (`gates/SOLR-15674-history-scrub.md`, open). The reporter's delete-and-recreate sequence is not run. The draft is about 4,986 characters.
- Fix: the Proof cites the scrubbed head only after the re-gate. Until then, record the scrub and re-gate as a gate need. Add a Limits line for the delete-and-recreate sequence, not run. Trim hard (the draft is about 40% over the guide).

### SOLR-15863 (HOLD)
- Finding: the Proof is at `f381fd8d4dd`, and the tip is `78dff1d52f6c`, one changelog commit past it. The Proof counts 7 test methods, and the source shows 6.
- Fix: check the receipt's current head. If the receipt now records the new head (`c073a59a22f` records the green at the new head), align the Proof to it, and check the method count in the source at that head. The answers file also owes a lower-case changelog title fix on the branch; check whether the branch carries it, and record it as a gate need if not.

### SOLR-16013 (HOLD)
- Finding: no timeout on the Overseer wait. The close order is not described. The serial close suggestion has no Choice. The receipt gate is at the older head `e1bd21fd11a`; the live tip `ba26b702891` is not gated.
- Fix: describe the close order. Add the serial-close Choice, with its cost. Add the Overseer-wait timeout to Limits. The Proof names the gated head and the date, and says the live tip is not yet gated. Record the live-tip gate as a gate need (`gates/SOLR-16013.md`).

### SOLR-16437 (HOLD)
- Finding: the wrong-shard 400 path is untested. Thirty-nine nightly tests are skipped, and the draft does not say so. The misspelled-replica 200 matches the ticket. About 5,092 characters.
- Fix: add Limits lines for the untested wrong-shard 400 path and for the thirty-nine skipped nightly tests. Take the count from the receipt, with its date and head. Trim hard.

### SOLR-17292 (HOLD)
- Finding: the verification date is missing. The node-down message is now sent when the persist throws. That behavior change has no Choice. The reporter's "throw the relevant exception" is answered.
- Fix: add the date and head. State the node-down behavior change in What this change does, as the answers file adopted (no Choice section). Confirm the receipt covers the in-branch remedy at the live head; if not, record the re-gate as a gate need. Trim (about 4,885).

### SOLR-17680 (HOLD)
- Finding: the Proof names one head. The re-gate is open. "Error text changes" describes text clients never see (checked at CollectionsHandler). The v2 "starts working" claim rests on a test that checks only the message.
- Fix: the Proof names the live head and its receipt. Remove the "error text changes" sentence, or state it as an internal change. Qualify the v2 claim to what the test checks, and add the rest to Limits. Record the re-gate as a gate need if the receipt is not at the live head (`gates/SOLR-17680-message-regate.md`).

### SOLR-17733 (HOLD)
- Finding: the dated run at the head is still open (`gates/SOLR-17733-dated-run.md`). A local-delete failure now returns success with the file left on disk. The sync complaint is answered only as "not reproduced". The reporter asked for a 501 or an explanation.
- Fix: state the local-delete behavior change plainly. Add a Choice on the 501 route against the success route, with its cost, if the code supports the alternative. Answer the reporter's ask in Limits. Record the dated run as a gate need.

### SOLR-9155 (NOT READY)
- Finding: the Proof is a bracket. "1 of 1 at dbd88c84ff2" is the old wrapping-route count. The declared-route gate has not run. The reporter asked for better ops logging, and the draft removes the only ERROR log without answering him. The gate file records GATE END NOT GREEN at `3b52bcfd2eb` (the premise re-run is queued). The receipt's "GATE GREEN" line conflicts with that; quote both.
- Fix: the Proof stays a bracket until the premise re-run and the receipt exist. Record that as a gate need. Answer the ops-logging request in Limits or the Choice. Trim (about 5,272).

## Done when

- Every assigned draft has its text fixes applied and its visible length reported.
- Every blocker and gate need is named in your report.
- No draft invents a count, a head, or a date.
