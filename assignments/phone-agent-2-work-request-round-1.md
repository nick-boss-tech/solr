# Assignment: phone-agent-2-work-request-round-1 (open work that matches your tags)

- Status: OPEN. Intended host: phone-agent-2 (tags `review`, `draft`, `records`, `gate`, `npm`). Posted by vm2, 2026-10-24, at the owner's direction.
- Why: the pool has been idle on most hosts for a while. Several open items match the tags this host lists, and the Ref Guide build can only run on a host with `npm`. Nothing here asks you to take restricted work.

## Items, in the order I would take them

1. **Ref Guide build for the SOLR-17988 docs branch (npm, the one that only you can run).** Assignment `assignments/solr-17988-docs-refguide-build-round-1.md`. Head `16d3005293d3c6a047277bc586b30e05b6b6ef11` on `solr-17988-docs`. Blocks PR1 for the SOLR-17988 upgrade notes.
2. **PR 5211 gate, Linux part (gate, settling-run).** Assignment `assignments/pr-5211-test-review-implementation-and-gate-round-1.md`. Read its intended-host line first; it names the capability it needs.
3. **SOLR-17988 stacked sysprops PR (review, implementation).** Assignment `assignments/solr-17988-stacked-sysprops-round-1.md`. It starts after the Ref Guide build in item 1 passes, so only pick it up then.
4. **New UI assessment, round 2 (review).** Assignment `assignments/new-ui-assessment-round-2.md`. Tickets 17572, 17585, 17598, 17657, 17898, 18178, 18191 and 18214 are already claimed or done. Check the claims before taking anything; I expect nothing is left.
5. **UI volunteer staged-ask draft for SOLR-17572 (review).** Assignment `assignments/ui-17572-design-plan-draft-round-1.md`. Drafting only, nothing posted.
6. **PR follow-ups (draft, records).** Assignment `assignments/pr-followups-round-1.md`. Item 3 is held for the owner's go; item 1 waits on the SOLR-17988 PR. Other items may be open.

## Rules

- Claim each item as its own claim file under `claims/` before you start. One live claim per host.
- Check the tip before claiming: `git fetch origin coordination-ui`, then look for an existing claim.
- Commit as Nick Shanin with no trailers. No em dashes in doc text.
- Nothing is posted to GitHub or Jira, and no PR is opened, without the owner's go.

## If you have no capacity

Set this status to HANDED with the reason in the file. Then the next host can take it.
