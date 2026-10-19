# Skip record: SOLR-17224

- Ticket: SOLR-17224, "Getting NPE in apache SOLR 8.11.2 while doing atomic update using add-distinct from java based application". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2024-04-03, updated 2024-04-28, no fix version. Two comments: a cross-reference to SOLR-17120 (cpoerschke, 2024-04-03) and a note that the stack trace points at line 466 of `AtomicUpdateDocumentMerger.java` in 8.11.2 (puneet.sharma, 2024-04-03).
- Verdict: close-fixed. Unchanged from the earlier record.
- Draft branch: solr-17224-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 13:48-13:49 UTC.

## Ticket reading

On 8.11.2, an atomic update with an `add-distinct` operator on a multivalued docValues field throws an NPE inside the Solr server. It works locally and fails in production. The reporter's update adds `promotionType` with `add-distinct` to a document that already exists. The stack trace points at `AtomicUpdateDocumentMerger.java` line 466 in 8.11.2.

## Code reading

- `solr/core/src/java/org/apache/solr/update/processor/AtomicUpdateDocumentMerger.java:545-553` at the pinned head: `doAddDistinct` reads `existingField.getValues()` into `original` at line 549, then at lines 551-553 replaces a null `original` with a new list. The null check is the change that stops the NPE: `getValues()` can return null for an existing field that holds no values, and the old code then called `original.size()` on it.
- Pre-fix code, from commit a1ebd0433c: `Collection<Object> original = existingField != null ? existingField.getValues() : new ArrayList<>();`, with no null check after it.

## History

- `a1ebd0433c`, 2025-07-30, "SOLR-17721 - null pointer fix in the doAddDistinct method (#3436)": changes this exact line and adds an `AtomicUpdatesTest` case (23 lines). Ancestor of the pinned head (`git merge-base --is-ancestor`: yes, checked).
- The commit subject cites SOLR-17721, not SOLR-17224. The queue note names SOLR-17721 as the duplicate. The link is by code path and by that note; the commit does not name this ticket.
- No commit on the pinned head names SOLR-17224 (evidence tool: no hits).

## Reproduction attempts

Not attempted in this pass (review-only, no builds). The fixing commit carries its own `AtomicUpdatesTest` case, which is the check to run on the pinned head if someone wants a test-level confirmation.

## Gap analysis

The reported NPE and the fix's null check are on the same statement, and the null check removes the failing path. The ticket's stack trace is from 8.11.2 line 466. That file has changed since 8.11.2, so the line numbers are not compared here. The fix matches the mechanism the ticket describes: an existing field whose values are null, combined with `add-distinct`. That match is by code reading, not by the ticket's exact data.

## Verdict justification

close-fixed meets the rubric's minimum: the fixing commit is `a1ebd0433c`, it is an ancestor of the pinned head (checked), and the changed code is present at the head (`AtomicUpdateDocumentMerger.java:549-553`). Linkage to this ticket is by code path and by the duplicate note, which the rubric allows when the subject does not cite the ticket.

## Limits

This record does not reproduce the reporter's NPE on 8.11.2 and does not run the fixing commit's test. It does not establish which data state the reporter had. It does not change the JIRA status. Nothing is posted to JIRA or GitHub.
