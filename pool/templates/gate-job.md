# Gate job: <TICKET> (<short title>)

Status: OPEN

- Branch: <fork branch>. Head: `<40-character sha>` (fork tip, confirmed by ls-remote on <YYYY-MM-DD>). If the tip has moved at claim time, stop and record the new tip here.
- Prior gated head: `<sha, or none>`. Merge-base with upstream/main: `<sha>`.
- Intended host: <host>. Any capable Linux host may claim if the intended host is busy (WORKFLOW.md).
- Capability tags: gate.
- Module: `<gradle module, for example :solr:core>`. Focused class: `<fully qualified test class>`. Seed: `<hex seed>`.
- Diff scope against `<base sha>`: <files, with insertions and deletions>. Anything else in the tree is out of scope.
- Why: <one paragraph: what changed since the last receipt, and why this gate is owed>.

Links: Ticket or parent: <ticket>. Category: <one of the 14 in pool/work-map.md>. Siblings: <branches on the same surface, or none known>. Supersedes: <gate job or head, or none>.

## What to run

Use the host's standard gate runner. Do not run the build tool directly. Check the worktree out at the head first.

- Step 0: changelog YAML parse of `<changelog path>`.
- Step 1: tidy. If tidy changes any file, apply the tidy output, record its patch and the tidied tree sha in this job, and continue from step 2. WORKFLOW.md, Gate repairs.
- Step 2: Error Prone compile: `<tasks>`.
- Step 3, proof leg: <what is reverted to base, and what stays at head>. Expected: <exact failure, by content>. Any other failure shape stops the job and follows the failure-route decision.
- Step 4, focused head run: <clean first, then the focused class at the seed>. Count from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
- Step 5: module check `<task> -x test`.

Failures follow the rows below and WORKFLOW.md, Gate repairs. A failed retry hands the job back.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| <name> | decided: <answer> | none | <Nick, or the lane that decided> |
| <name> | default: <what happens> | if <condition>, <action>; otherwise nothing | <owner> |

No row may leave the choice to the claimant.

## On green

1. Write `receipts/<TICKET>.md` with a new top section: head, gate date and host, log path, step results, the proof-leg result (test name and failure text), and the focused counts from the fresh XML. Record every attempt, its repair commits and files, and the gated head.
2. <Public text edits, if any: the target text, the receipt it cites, and the read-back check.>
3. Mark this job DONE and the claim DONE in the same push as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed
- [ ] step 0 changelog parse
- [ ] step 1 tidy clean, or repaired and recorded
- [ ] step 2 Error Prone compile
- [ ] step 3 proof leg, expected failure by content
- [ ] step 4 focused head run, counted from fresh XML
- [ ] step 5 module check
- [ ] receipt written
- [ ] public edits read back, if any
- [ ] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks, plus any repair commits (WORKFLOW.md, Gate repairs).
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
