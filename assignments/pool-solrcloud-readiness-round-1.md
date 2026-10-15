# Pool assignment: SolrCloud readiness for opening PRs, round 1

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: five read-only subagents, four drafts each. Claim: `claims/pool-solrcloud-readiness-round-1.md`.

## Purpose

The coordinator has said the openings pass can begin, and SolrCloud is the next PR category. `reports/solrcloud-openings-slate.md` (written 2026-10-10) lists what can open and what is held, but several of its items have moved since: heads have changed, receipts have been refreshed, and some branch commits have landed. This round rebuilds the readiness picture for the 19 drafts in `pr-drafts/solrcloud/`, against the current fork tips and receipts. It does not open anything. Opening stays with Nick.

## Checks, per draft

1. Tip and head. The fork tip of the ticket's `solr-<n>-submit` branch (git ls-remote, read only) equals the head the draft names in its Proof. Report equal or not.
2. Receipt. Read `receipts/<TICKET>.md`. State the gated head, the gate date, and whether the gated head equals the current tip. Quote the line.
3. Slate item. Find the draft's entry in the slate (the "Can open", "Held" or "Decisions" section). Say whether the item is still true, citing the receipt or the draft line that settles it, or whether it has been overtaken. Quote the evidence.
4. Citations and counts. Every GitHub link in the draft resolves at the tip (git cat-file -e), with its line range inside the file. Each Proof count and date matches the receipt body. Do not use takeover-log or ledger citations as the source for a count or date.
5. Readiness. Give one verdict:
   - READY TO OPEN: the tip equals the gated head, the receipt is green, the citations and counts hold, and any slate item is settled.
   - HOLD: name the branch commit, re-gate or record that is still owed.
   - DECISION: name the owner decision still open, quoting the slate's recommendation.
   - NOT FOR OPENING: name the reason from the slate or the receipt.
   Add the process-word check: list any line in the draft that contains gate, gated, receipt, claim, job, vm1 or vm2, with its line number, and any dash.

## Rules

- Read-only. No build, no test, no Gradle, no gh write, no push, no edit to any file in the repository. The notes go to your final report only.
- Proof comes only from the named receipt body and the object store. Takeover logs and ledgers are not sources for a count or a date.
- No em dashes or en dashes in your output.

## Deliverable

`reports/solrcloud-readiness-round-1.md`: a table of the 19 drafts with the verdict, the tip, the gated head, and the open item, and an opening order for the READY drafts.
