# Core admin answers round 1, part c1 (items 1 and 2): done for 11 drafts

Result: items 1 and 2 applied to 11 drafts. Five INTERNAL blocks removed, twelve bold openers added. The Limits bullets were not trimmed (see Not done).

## Changes

Item 1: INTERNAL blocks removed. In each file the old last lines were a blank line and one `<!-- INTERNAL, remove before posting. ... -->` comment. The new last line is the `AI assistance` paragraph. Each file also gained a final newline, because the old file ended with no newline after the comment.

- `SOLR-6438.md`: removed. The block held head `8c77988d5917`, the two handoff-doc commit subjects (`6dc9151aaa1`, `8c77988d591`), and the note that the owner decides on the Choice section.
- `SOLR-8275.md`: removed. The block held head `e52e10fa50a3` and "no hold, no Choice owed".
- `SOLR-8576.md`: removed. The block held HOLD, the alias assertion fix (CollectionsAPISolrJTest.java L1188), the run owed at the new head, and the two handoff commits.
- `SOLR-16725.md`: removed. The block held head `be1838ef8ccf`, the rule that the maxShardsPerNode Limits line stays in the post, the raw CREATE request in the test (no stated reason), and the ClusterStatus.java L337-L340 description.
- `SOLR-17731.md`: removed. The block held HOLD until the two license headers are fixed, the two Jersey comments are reworded, and ListAliasesAPITest runs at the new head, plus the router owner call and the two handoff commits.

Item 2: bold openers added. Each is a new line placed directly under the heading, with a blank line on each side. The bullets under it are unchanged.

- `SOLR-4989.md`, Limits. Old: `## Limits` then `- show=all with a document id ...`. New: the same heading, then `**The distributed path was read but not run, and a document id still returns \`BAD_REQUEST\`.**`, then the bullet.
- `SOLR-13246.md`, Limits. New opener: `**One public method is added, and other log lines that print a searcher keep their output.**`
- `SOLR-12007.md`, Limits. New opener: `**Timing on large index directories is not measured, and only a mock factory is tested.**`
- `SOLR-17297.md`, Limits. New opener: `**Module SPIs are not fixed when a shared lib is present, and only shared lib SPIs are tested.**`
- `SOLR-17377.md`, Limits. New opener: `**Only a shared lib class is tested, and the allowed classes do not change.**`
- `SOLR-6438.md`, Limits. New opener: `**The v1 action has no test, and clients that sent both sources must now send one.**`
- `SOLR-8275.md`, Limits. New opener: `**Two message variants share this code path, and neither has a test.**`
- `SOLR-8576.md`, Limits. New opener: `**The limits are narrow: name collisions only, and the checks match error text.**`
- `SOLR-16725.md`, Limits. New opener: `**The maxShardsPerNode key is not changed, and only the CLUSTERSTATUS output changes.**`
- `SOLR-17731.md`, Limits. New opener: `**Other overlapping v2 paths were not searched, and the alias response keeps its name field.**`
- `SOLR-15024.md`, Choice. Old: `## A choice to check` then `Options considered: ...`. New: the same heading, then `**The two options are unique object keys or an ordered list, and this change uses the list.**`, then the same paragraph.
- `SOLR-15024.md`, Limits. New opener: `**Token filters keep the class name as their key, and the Admin UI was read but not run.**`

## Not done

- The Limits bullets were not trimmed. The formula asks that the summary carry the claim once and the body carry only evidence. Some bullets now repeat their opener. Item 2 asked only for the openers, so trimming was left out. This is a call for the lead.
- Items 3 to 8 are part c2's work. Nothing from them was done here.
- The INTERNAL text is gone from the drafts. For SOLR-8576 and SOLR-17731 the drafts no longer say HOLD. Until c2 adds the owed notes (item 5), those two drafts read as ready. Do not post from them on the draft text alone. The holds are still recorded in the answers file and the branch corrections list.
- Outside items 1 and 2, nothing was changed. Two existing bold Choice openers run to two sentences: SOLR-12007 and SOLR-17297. The formula asks for one line. This is noted for the draft-fix pass.
- Concurrency: the c2 pass edited SOLR-8576, SOLR-15024, SOLR-17297, SOLR-17377, and SOLR-17731 while this part ran. I used exact-string edits only, never whole-file writes. The final check found all twelve openers and all five removals still in place. Those files also carry c2's changes, which I did not review or revert.
- No commits, pushes, stashes, or checkouts. No DISCUSS item decided. No branch, live PR, or receipt touched. No builds, tests, or gh calls.

## Checks

- Em and en dashes in the drafts folder after the edits: 0 in all 16 drafts.
- INTERNAL blocks left in the folder: 0.
- Openers: 12 present. Each Limits section in the eleven assigned drafts now opens with a bold line, and the Choice section in SOLR-15024 does too.
- Internal process words (receipt, gate, audit, round, report, handoff, owner, hold, premise, owed, tidy, harden, pipeline, queue, banked, discuss, lane, fix, note): none in the new openers. The only hits in the folder are the ordinary word "fix" in existing Choice questions in SOLR-17297 and SOLR-17731, which I did not change.
- Line endings: every edited draft is CRLF on every line, with no mixed endings.
- Proof numbers and head references: not checked here. That is part c2's item 8.
