# Pool assignment: SolrCloud PR drafts, patch and audit, round 3

Capability tags: `review`, `draft`. Intended host: windows (review coordinator). Staffing: four subagents, one per group below. Claim: `claims/pool-solrcloud-pr-audit-round-3.md`.

## Why this round

Nick asked for the SolrCloud drafts to be patched and audited. Round 2 applied the critical review's fixes. Round 2's decisions are in `material/solrcloud-round-2-decisions.md`, and two branches changed since then (SOLR-9155 at `29bf373828994c622ac478a968d6cff4f4c45fa5`, SOLR-16437 at `a23f770e4b9587acbd14fcdbaae2047bd5c727c4`). This round checks every draft against its current facts.

## Rules

- Read `pr-formula.md` and `material/solrcloud-round-2-decisions.md` first.
- Edit only your assigned drafts in `pr-drafts/solrcloud/`. Do not edit receipts, gates, reports, claims, material, branches, or other drafts.
- Live heads: check each branch with `git -C C:/Users/shaninna/dev/Solr-issues/source ls-remote origin refs/heads/<branch>`. A draft's cited head must match the receipt's head, and the receipt's head must match the live tip. A head that differs is a blocker. Do not change the Proof's numbers to fit it.
- Citations: for each link in the draft, check that the path exists at the SHA in the link (`git -C C:/Users/shaninna/dev/Solr-issues/source show <sha>:<path>`), and that the line range covers the code the sentence describes. A wrong range is a text error. Fix it and say so.
- Proof: counts and dates come only from the receipt or a gate file. A bracketed placeholder means the Proof is not final. Leave the bracket in place and name it as a blocker.
- JIRA fit: fetch the ticket read-only with `C:/Users/shaninna/AppData/Local/Temp/2/claude/C--Users-shaninna-dev-Solr-issues/1a4dfad7-722c-4e59-ab2e-961faf1441e1/scratchpad/jira-read.ps1 -Keys SOLR-NNNNN` (read its header first). Never print a token. Never write to JIRA.
- Formula: section order (header, Jira link, What happens today, What this change does, Proof, A choice to check only for a live alternative, Limits, Changelog, AI assistance). A bold one-line summary opens each body section. Plain language. No internal process words (claim, probe, harness, round, receipt, gate, vm1, vm2, draft, scratch, pool, export). No dashes. AI header and footer present.
- Length: visible characters at or under 3,500 (link URLs and markup removed).
- Do not run builds, tests, or gates. Do not commit or push. The coordinator commits.

## Output (each subagent)

Return under 700 words:

1. For each draft: verdict READY, HOLD or NOT READY. READY means the text is final and the only remaining step is Nick's approval to open. HOLD names the item that holds it. NOT READY names what is missing.
2. For each draft: the edits you made, one line each, and the visible length after the edit.
3. Each citation error you found and fixed, with the link and the correct range.
4. Blockers that need a gate, a branch change, or a decision for Nick. Name the ticket and the exact item.
5. Any fact that contradicts a receipt or the JIRA text. Quote both.

## Groups

### Group A: SOLR-5813, SOLR-11288, SOLR-11479, SOLR-12651, SOLR-12991
### Group B: SOLR-13186, SOLR-13369, SOLR-14919, SOLR-15035, SOLR-15106
### Group C: SOLR-15386, SOLR-15674, SOLR-15863, SOLR-16013, SOLR-16437
### Group D: SOLR-17292, SOLR-17680, SOLR-17733, SOLR-9155

## Known state to check against, not to accept blindly

- SOLR-9155: the head is `29bf373828994c622ac478a968d6cff4f4c45fa5`. The draft's Proof is bracketed until the re-gate in `gates/SOLR-9155-logging-regate.md` lands. The Choice asks maintainers about the declared route. Limits names flow control and the rejoin flag.
- SOLR-14919: processor-only. The replication failure is in Limits. The follow-up ticket draft is `material/solr-14919-replication-ticket-draft.md`, not filed.
- SOLR-16437: the head is `a23f770e4b9587acbd14fcdbaae2047bd5c727c4`. The changelog title says "not in that shard". The Proof's nightly counts are at `f72a9edd0a8b`, the head they were recorded at.
- SOLR-15674 and SOLR-12651: their history scrubs are queued on vm2 (`gates/SOLR-15674-history-scrub.md`, `gates/SOLR-12651-history-scrub.md`). Their Proofs cannot cite a rewritten head until the scrub lands.
