# Search components opened review, round 1, slice 1 (PRs 5130 to 5133)

- Assignment: `assignments/pool-search-components-opened-review-round-1.md`. Claim: `claims/search-components-opened-review-round-1.md`.
- Reviewer: review agent, Windows host. Every PR stays a draft. No ready flips, no PR comments, no pushes, no commits, no Jira edits, no builds, no tests.
- Full before and after text for the body edits: scratchpad `pr5130-body-before.md` and `pr5130-body-after.md`, and the same pairs for 5132 and 5133.

## PR #5130 (SOLR-4374), head `801c62290f79`. SATISFIED after 3 description edits.

- Diff is scoped to the ticket (3 files). The behavior statements were checked against `getDigitLeadingFieldName`, `IndexSchema.getFieldTypeNoEx` and the colon path in `add()`.
- Edits:
  1. Appended to "What happens today": "The link in this paragraph points at the merge-base commit, before this change."
  2. "Behavior change" rewritten. Without a catch-all, a number is read as a field only when the schema has a field with that exact name, or a dynamic pattern such as `1*` that matches it. The same check applies before a colon. The old sentence was wrong for non-catch-all patterns and missed the colon case.
  3. Limits: "The rename path still reads names with the stricter check." became "The source name in a rename still uses the stricter check."
- Length: about 3.9k characters, over the 3,500 guide.

## PR #5131 (SOLR-5394), head `967445622f92`. BLOCKED. Move: Nick.

- Body and proof are otherwise clean. The changelog type is `fixed`. dev-docs defines `changed` as modifying the behavior of existing requests, which fits this change. The slate lists the type as an open owner item, while the row reads SATISFIED.
- Gate job: `gates/SOLR-5394-review-changelog-type.md` (HOLD). It runs only after Nick picks the type. The proposed patch changes `type: fixed` to `type: changed`.

## PR #5132 (SOLR-6207), head `b099a9f1be5a`. SATISFIED after 1 edit.

- Edit: "RequestUtil.java line 182" is now linked at the merge-base. The PR does not touch that file.
- The Proof has no verification date. The owner's advisory ruling of 2026-10-14 covers it, so there is no edit.

## PR #5133 (SOLR-6759), head `42f03eb52376`. SATISFIED after 3 edits.

- Diff is scoped (5 files). `GroupCollector` implements `Collector`, not `DelegatingCollector`, so "no post filter, path unchanged" holds.
- Edits:
  1. Two pre-change links re-pointed from the head to the merge-base, with "at the merge-base" in the link text.
  2. Limits: appended that the reporter's own case (an access-control post filter that sends documents only in its finish step) is not reproduced; the test uses a counting filter instead. The ticket describes an always-empty expand section, which the body did not address.
- Length: about 4.2k characters, over the guide. It was already over before the edits.

## Gate jobs filed this slice

- `gates/SOLR-5394-review-changelog-type.md` (answers the #5131 type finding; HOLD)

## Record disagreements (flagged, not worked around)

- Slate: SOLR-5394 is SATISFIED in the row, while its type is an open owner item.
- Receipt for 6759 says the premise is "loses its last group". The ticket describes an always-empty expand section. The main side should decide whether the receipt is narrower than the ticket.
- Branch history for 6759 includes `62974ef8d18` "add hypothetical-reproduction handoff doc", removed by `42f03eb`. The net diff is clean, but the subject is visible on the PR's Commits tab. Squash or rewrite is the main side's call.
- The claim `claims/search-components-opened-review-round-1.md` was not written by this slice and should be marked DONE by the coordinator.
