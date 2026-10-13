# Public record check, round 7: offer comment on PR #5065

Read-only. Nothing was posted, edited, or changed on GitHub, in the repository, or in JIRA.

## The posted comment

- Comment `6139135892` on apache/solr PR #5065, created 2026-10-13 05:45:50 UTC (2026-10-12 22:45 PDT). Edited 2 minutes 40 seconds later, at 05:48:30 UTC. The author association is CONTRIBUTOR, from the account `nick-boss-tech`.
- Our receipt (`receipts/SOLR-18010.md`, commit `9242ce0f17a`) says it was "posted 2026-10-12 with Nick's approval of the exact text", and that "the returned body was verified against the sent text". The receipt gives the Pacific date only, and it does not store the sent body.
- The posted text differs from the draft in `reports/solr-18010-overlap-comparison-round-1.md`:
  - "We" became "I", in two places.
  - Backticks were removed from `persistConf`.
  - One sentence was added: "I have put the version with both elements up as a draft in #5118 so you can see them worked out."
- The API cannot show the text before the edit. The receipt's claim that the returned body matched the sent text cannot be checked from local files.
- No process words appear in the posted body. "draft" is the only term near that list, and it describes a public draft PR.

## Consequences

- The comment is public. It now points to draft PR #5118. If #5118 is closed, that pointer is left dangling, so its keep-or-close decision now affects a public comment.
- The round 1 recommendation was to combine with PR #5065 and not open a competing PR. The comment offers the two elements and also points to our draft. That is a mix of the combine and open routes, and the round 1 report does not record it.

## PR #5065: current state

- Open, not a draft, not merged. Head `337ccdd2592a`, unchanged. Mergeable, with mergeable state "unstable".
- Last human activity is still 2026-10-09 12:09 UTC. The offer comment and its edit are the only activity since.
- Reviews: one approval from a non-member, "LGTM.", 2026-10-09 11:31 UTC. No change requests. The open question ("other places in standalone that might have similar issues") has no author reply.
- Checks on `337ccdd`: "Run Solr Tests using Crave.io resources" failed, with no test named. The other checks passed.
- The reviewer's "till monday" date, 2026-10-12, has passed with no maintainer action.

## Dates in local records

- Receipts for #5118 and for the comment say 2026-10-12. Those are Pacific dates. In UTC, both are 2026-10-13: #5118 at 05:41 and the comment at 05:45. The round 6 report uses UTC and matches the API.
- Receipt dates should be stored in UTC, with the Pacific date in parentheses.

## Decisions for Nick

1. **What was approved.** The receipt says "the exact text", but the posted text differs from the round 1 draft. Please confirm the final text is what you approved, and whether you edited the comment within the first 3 minutes, as the API suggests. The sent body should be stored in the receipt.
2. **PR #5118.** A public comment now points to it. Keep it as a draft, or close it? Closing it would leave the comment pointing at nothing.
3. **Next step on #5065.** The comment asks the author whether to adopt either element. No reply has come. Nothing needs to happen until the author answers or the maintainer acts.

## Not done

- No comment, PR, edit, or JIRA write.
- No build, test, or gate run.
