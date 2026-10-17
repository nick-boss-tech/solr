# Pool assignment: 2026-10-16 review follow-ups, PR text drafts (round 1)

Status: DONE (vm2, 2026-10-17): all seven items drafted under pr-drafts/2026-10-16-review/, index.md lists each item. Nothing applied to a live PR.

- Capability tags: draft.
- Intended host: any host with the draft tag. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: Windows review lane (review-round coordinator), 2026-10-16, at the direction of Nick Shanin.

These items fix PR text found in the 2026-10-16 review round. Write each corrected text as a local draft under `pr-drafts/2026-10-16-review/`. Do not edit any live PR title or body. Proof numbers come only from the receipt or the gate file for that ticket, never from memory. Write in plain language, as `pr-formula.md` requires.

## Items

1. **SOLR-7550 (PR 5139): title and proof text.** The title still describes the broad rule that the branch dropped. The rule now is narrow: a versions-request 500 counts as success only when the error metadata names `SolrCoreInitializationException`. Draft a title that names that rule. The Proof section still says the run is pending. The gate at the current head is green, so cite the counts from the receipt. The choice section also omits an update-loss cost that the review found; add it, stated for the option the PR chose. Start from: `receipts/SOLR-7550.md` on pr-prepare, and branch `solr-7550-submit`. Output: `pr-drafts/2026-10-16-review/PR-5139-text.md`.

2. **SOLR-9396 (PR 5156): body length.** The visible body is 3,998 characters, over the 3,500 guideline. Cut it under 3,500 visible characters without losing the proof and the decision text. Start from: the PR body as stored (read only) and `receipts/SOLR-9396.md`. Output: `pr-drafts/2026-10-16-review/PR-5156-text.md`.

3. **SOLR-12505 (PR 5169): body length.** The visible body is 6,742 characters, against the 3,500 guideline. Draft a body under 3,500 visible characters. The commit trailers are a separate decision (see Decisions). Start from: the PR body as stored (read only) and `receipts/SOLR-12505.md`. Output: `pr-drafts/2026-10-16-review/PR-5169-text.md`.

4. **SOLR-8009 (PR 5141): disclose a new failure.** An unrouted get can now fail when a shard has no live replica. The non-tolerant request takes the SERVICE_UNAVAILABLE error, where the base returned empty. The code is right, but the body does not say so. Add one sentence to the body. The body is 3,681 visible characters, so also trim it under 3,500 in the same draft. Start from: the PR body as stored (read only). Output: `pr-drafts/2026-10-16-review/PR-5141-text.md`.

5. **SOLR-8240 (PR 5145): overstated sentence and a default recommendation.** The body says "three examples now return 400". Only one example changes; correct it. The "Setting JSON Defaults" section recommends the flag as a default, but the stock techproducts sample makes every `f` request fail until SOLR-10424 lands. Apply the Decision on this item. The body is 3,706 visible characters, so trim it under 3,500 in the same draft. Start from: the PR body as stored (read only). Output: `pr-drafts/2026-10-16-review/PR-5145-text.md`.

6. **SOLR-7520 (PR 5138): name the wider change.** The grouped shard path now calls `complete()` on every post filter, not only AnalyticsQuery. The body, the changelog Limits and the tests do not say so. Add it. The body is 4,081 visible characters, so trim it under 3,500 in the same draft. Start from: the PR body as stored (read only). Output: `pr-drafts/2026-10-16-review/PR-5138-text.md`.

7. **SOLR-18368 (PR 5170): JIRA key in the title.** The PR title has no JIRA key, and the review gate requires one at the start of the title. Draft the title as `SOLR-18368: ` followed by the current wording. If the existing `pool-vm1-solr-18368-review-fix-round-1` assignment already changes the title, skip this item and say so in the index. Output: `pr-drafts/2026-10-16-review/PR-5170-text.md`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Live PR edits | decided: drafts only, under `pr-drafts/2026-10-16-review/`. No PATCH of any live title or body, no comment, no state change | none | Nick |
| Item 1 title wording | decided: name the SolrCoreInitializationException rule, since the body and changelog already describe it | none | Nick |
| Item 5 SOLR-10424 dependency | decided: say in the body that the sample needs SOLR-10424 first, and do not recommend the flag as a default until that ticket lands | none | Nick |
| Commit trailers on PR 5169 | default: leave the three Claude co-author trailers as they are | if a history rewrite is wanted, escalate to the main agent; a rewrite is a force-push to an open PR head and needs Nick's go-ahead | Nick |
| Commit messages that cite a missing TESTING.md file (PRs 5137, 5138, 5140, 5144, 5157) | default: leave unchanged, as Nick decided for the same pattern on PR 5158 | if Nick changes his mind, escalate to the main agent | Nick |
| Whether SOLR-7520 closes on PR 5138 | default: the draft does not claim closure | escalate to the main agent | Nick |
| Item 7 overlap | default: skip if the existing round-1 SOLR-18368 fix assignment already changes the title | otherwise draft it | main agent |

No row may leave the choice to the claimant.

## Deliverable

`pr-drafts/2026-10-16-review/`: one draft file per item, named as in its Output line. Each draft starts with its PR number, the head SHA it was written against, the visible character count, and the source of every proof number (receipt or gate file). Then `pr-drafts/2026-10-16-review/index.md` lists each item as drafted or skipped, with the reason.

## Limits

- No PR, comment, JIRA or email. No edit to a live title or body. No force push.
- Commit as Nick Shanin with no trailers. No em dashes or en dashes.
- Proof numbers only from receipts or gate files.
