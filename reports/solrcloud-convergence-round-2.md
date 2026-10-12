# SolrCloud convergence, round 2

Verdict: **NOT CONVERGED.** Nine of the twelve re-checked drafts converge. Three do not: SOLR-15035, 16437 and 17680. The round 1 blocking item (SC-R1-21, SOLR-15106) is closed.

Scope: `assignments/pool-solrcloud-convergence-round-2.md`, checked against `reports/solrcloud-convergence-round-1-response.md`. Two read-only subagents covered the twelve drafts, and two limited re-checks were run (SOLR-13186 and SOLR-15674). Every live head was checked with `git ls-remote origin refs/heads/solr-<n>-submit`. Each draft's named head matches its live tip, so no draft is on hold. Merge-bases were checked against the local `upstream/main`. The drafts were not edited.

## Verdict per draft

- **SOLR-9155: CONVERGED.** SC-R1-01 to 03 confirmed. Head `9f08d033023`.
- **SOLR-11479: CONVERGED.** SC-R1-01, 10 confirmed. Head `360dca60fcfa`. The answers still say "No gate recorded" (SC-R2-02).
- **SOLR-12651: CONVERGED.** SC-R1-02, 06 confirmed; SC-R1-05 standing (branch title). SC-R2-03 (note): 3,543 characters against the 3,500 guide, with no recorded decision.
- **SOLR-14919: CONVERGED.** SC-R1-26, 33, 34, 35 confirmed or standing as recorded.
- **SOLR-15035: NOT CONVERGED.** SC-R1-31 (should-fix): the Proof claims a base failure that the receipt does not record. Carried SC-R1-27, 28 (judged, sentence stands), 29 confirmed.
- **SOLR-15106: CONVERGED.** SC-R1-21 (blocking) is closed: the bold line and bullet now say the base code cannot compile the test, and the accessor link matches the code. SC-R1-22, 23 confirmed.
- **SOLR-15386: CONVERGED.** SC-R1-24, 25, 30 confirmed. SC-R1-37 confirmed with the correction in SC-R2-25. SC-R2-24 (length note) standing.
- **SOLR-15863: CONVERGED.** SC-R1-41 confirmed (the date is recorded, not verified). SC-R1-49 is a branch-side record (the changelog title still starts lowercase).
- **SOLR-16437: NOT CONVERGED.** SC-R2-21 (should-fix): the Limits sentence names "DELETEREPLICAPROP and the other APIs the ticket mentions", and the ticket names only ADDREPLICAPROP; its closing sentence says "and other APIs". The judged item SC-R1-51 is overstated; see below. SC-R1-42, 50, 52 confirmed. SC-R2-23 (note): the date form.
- **SOLR-17292: CONVERGED.** SC-R1-43, 53 confirmed.
- **SOLR-17680: NOT CONVERGED.** SC-R2-22 (should-fix): the error-text list covers only time aliases. A category routed alias without `router.field` also changes its error text, and line 31's "The field check moved out of the command" is true only for time aliases. SC-R1-44, 54, 55 confirmed.
- **SOLR-17733: CONVERGED.** SC-R1-45, 46, 56 confirmed.

## Limited re-checks

- **SOLR-13186 and the date-form pick (SC-R1-30): PASS.** The pick was applied as recorded: 13186 "recorded 2026-10-06"; 9155 "recorded 2026-10-06"; 15106 "recorded 2026-10-07"; 15386 "recorded 2026-10-07"; 15035 "recorded 2026-10-03"; 15863 "recorded 2026-10-08". No draft the answers prescribe as "verified" changed; 15674 keeps "verified 2026-10-03".
- **SOLR-15674 (SC-R1-47, 48 and the version-reset sentence): PASS.** Both applied. "A new znode starts that version over at 0." appears once in 15674 and once in `pr-drafts/configsets/SOLR-15478.md`, identical. The 15478 draft stays out of the verdict. SC-R2-26 (note): its landing-order sentence is missing, because SC-R1-48 applied only to 15674.

## Judged items

- **SC-R1-28 (SOLR-15035, "The ticket summary describes this difference."):** the sentence stands against the summary the response quotes from live Jira ("core.properties different when using ADDREPLICA .vs. when the replica created with CREATE"). Caveat: no Jira packet for SOLR-15035 is on disk, so the outcome rests on the recorded quote.
- **SC-R1-51 (SOLR-16437, "the other APIs the ticket mentions"):** the phrase is overstated. The ticket's closing sentence says "(and other APIs)" and names none, and the draft's own example, DELETEREPLICAPROP, is not in the ticket. Apply SC-R2-21. "other APIs that take the same inputs" is closer, but "the same inputs" is also an inference.
- **SC-R1-21 (SOLR-15106, blocking):** closed, as above.

## Register

Carried findings keep their round 1 IDs. Replacement text is given for each open should-fix item.

**SC-R1-31 | SOLR-15035 | should-fix (carried, not applied).** The response says the Proof "already states the receipt's outcome for both legs". The receipt records only the ADDREPLICA-alone run and no base-code run, so the bold line and bullet 1 claim a base failure that is not recorded. Replacement, line 19: "**The new test fails with the ADDREPLICA change alone, and passes with the full change.**" Replacement in line 21: replace "On the base code it fails, because the new core has no numShards entry in core.properties. The ADDREPLICA change alone also fails, for the reason in the next bullet." with "On the base code, ADDREPLICA sends no shard count, so the new core has no numShards entry in core.properties. The ADDREPLICA change alone fails, for the reason in the next bullet." The code basis is that `AddReplicaCmd` and `paramToProp` have no `NUM_SHARDS` at the merge-base.

**SC-R2-21 | SOLR-16437 | should-fix.** Line 33: "Only ADDREPLICAPROP is covered. DELETEREPLICAPROP and the other APIs the ticket mentions are not changed." Replacement: "**Only ADDREPLICAPROP is covered. The ticket's closing sentence suggests the same check for other APIs. DELETEREPLICAPROP and those APIs are not changed.**" Record note: the answers' line 167 says "the other APIs the ticket names"; it should say "the ticket's closing sentence suggests".

**SC-R2-22 | SOLR-17680 | should-fix.** The error-text list (line 19) covers only time aliases. At the head, `CreateAliasCmd` checks only `router.name` (line 120) and falls through to the generic "Not all required params were supplied. Missing params" check (lines 140 to 146). `CategoryRoutedAlias` does not check `router.field` in its constructor (lines 74 to 82), although its required list includes it (lines 55 and 56). Replacements: after line 19 add "- A category routed alias without `router.field` now fails with "Not all required params were supplied. Missing params: router.field". Before, it failed with the same "A routed alias requires these params" message as a time alias." Replace line 31 with "**The up-front check for `router.field` moved out of the command.**" In the "Before" sentence of line 19, replace the bracketed list with "followed by the two parameter names", because the order comes from `Set.of` iteration and can vary per JVM start.

**SC-R2-01 | response record (SC-R1-37) | note.** The response says the titles "are not recoverable from the round 1 parts on this branch". Commit `0e287a38159` added "Suggested PR title" lines in owner-notes headers for SOLR-9155, 15106, 13186 and 15386, and `cafceac3230` removed them. They are recoverable with `git show 0e287a38159:pr-drafts/solrcloud/<ID>.md`. The decision that drafts in this set carry no title line stands.

**SC-R2-02 | answers record | note.** `material/solrcloud-round-1-answers.md` still records SOLR-11479 as "No gate recorded" and SOLR-12651 as not gated. `receipts/SOLR-11479.md` records green at `360dca60fcfa`, and `receipts/SOLR-12651.md` records green at `f3131d1ee846`. The drafts match the receipts. Move both to the gated-green list.

**SC-R2-03 | SOLR-12651 | note.** The draft is 3,543 characters against the 3,500 guide. The response records no decision. Options: trim (for example, line 29 "Options: delete the new collection on failure (implemented),"), or record the acceptance.

**SC-R2-23 | SOLR-16437 | note.** Line 23 "verified 2026-10-10" could read "recorded 2026-10-10". The receipt says the date is in the ledger and takeover log. The answers prescribe no form for this draft.

**SC-R2-24 | length | note.** Character counts for 15386, 15863, 16437, 17292, 17680 and 17733 are about 4,490, 4,060, 5,050, 4,910, 4,510 and 4,270, against the 3,500 guide. The answers accept length only for 15674. Record a reason per draft, or trim.

**SC-R2-25 | record note (SC-R1-37) | note.** The response says "the four drafts the note names". The round 1 note names three (13186, 15106 and 15386). The titles were in history for four drafts (9155, 13186, 15106 and 15386). The disposition stands: the PR title follows the changelog title.

**SC-R2-26 | SOLR-15478 (outside the verdict) | note.** The 15478 draft states no landing order. Suggested for its owner: "This branch is meant to land after SOLR-15674."

## Not checked

- Jira packets for SOLR-14919 and SOLR-15035 (none on disk).
- Gate logs, which are not on disk.
- The master receipts ledger, the takeover log and security material.

## Convergence line

NOT CONVERGED. Three drafts hold open should-fix items: SOLR-15035 (SC-R1-31), 16437 (SC-R2-21) and 17680 (SC-R2-22).
