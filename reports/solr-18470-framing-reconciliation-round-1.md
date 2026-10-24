# SOLR-18470 framing reconciliation, round 1

Claim: `claims/solr-18470-framing-reconciliation-round-1.md` (vm6). Assignment: `assignments/solr-18470-framing-reconciliation-round-1.md`. Analysis and drafting only. Nothing was posted or edited on JIRA or GitHub, and no PR was touched.

Sources, read on 2026-10-24: the posted comment 18131208 on SOLR-18470 (Jira REST, public read); SOLR-18446 and SOLR-18470 fields (Jira REST, public read); PR apache/solr #5202 description, files, reviews, and review comments (GitHub API, read only, head `1c0a942ff86`).

Limit: Smiley's dev-list reply (Gmail 1a16c83cfc2ffa84) is not on the branch and I could not open it. The analysis below uses the assignment's summary of it. Quotes from that reply are the assignment's words, not the email's.

## 1. The framing gap

What the ticket says. SOLR-18446 is a Blocker with fix version `main(11.0)`, reporter Smiley, titled "Remove 'collection' param routing support in HttpSolrCall". SOLR-18470 is Major, no fix version, reporter Smiley, titled "Limit collection handlers' scope to URL path". Smiley's frame, from the assignment: a Blocker with a fix version blocks that release. His concern is that dropping the `collection` parameter cannot be backported, and it is aimed at Solr 11.

What PR #5202 does. It implements SOLR-18446 with a narrowing, not a removal. The URL path decides the collection on both the v1 and V2 entry points. The `collection` parameter can only narrow the set the path names. Requests with no collection in the path now return 404. The PR keeps the parameter. The Solr 11 upgrade note (`major-changes-in-solr-11.adoc`, +7 lines) records the change as "can no longer override the path", not as a removal.

Pugh's review asked the question directly: "it feels to me like the `collection` parameter is no longer needed?" and "are we deprecating the 'collection' parameter and changing it's behavior?" Nick's reply on the PR (2026-10-23, 23:13) says it is "neither dropped nor deprecated", and that the parameter is optional and still useful for narrowing. Pugh's review state is COMMENTED, not approved. His comment says "I think I need @dsmiley to weigh in."

What the posted comment (18131208) says. "#5202 covers SOLR-18446 only: the `collection` parameter can no longer override the path on distributed requests. It deliberately does not attempt this ticket." It then gives a plan for path scope on `SolrQueryRequest`, with collection handlers returning 400 when they cannot honor the scope, and "parameters such as `shards` may narrow the path scope, never expand it". It asks three questions and says Nick will line up a reviewer before starting work.

Where a reader holding Smiley's frame diverges from the comment:
- The ticket he filed is to remove the parameter's routing role. The PR and the comment keep the parameter as a narrowing tool. The comment's wording, "can no longer override the path", describes half of the removal and does not say that the parameter survives. A reader with his frame would take the comment to mean the removal is being redesigned as a narrowing, which is the Solr 11 decision the Blocker exists to force.
- The comment reads as a plan for path-scope work with three design questions. Smiley's frame says the scope question is the release-blocking conversation for Solr 11, not a work-now item. So the questions ask him to settle a Solr 11 decision now.
- "Line up a reviewer before starting work" reads, to a reader who has just declined review asks, as a request for review of #5202 and of the plan. He named #5202 as one PR too many on his pile.

What a reader with his frame would reasonably take the comment to ask: whether he will review a path-scope redesign that keeps the `collection` parameter as a narrowing tool, and settle the Solr 11 scope now.

## 2. What his reply demands of each artifact

- PR #5202: nothing new is asked by the reply beyond the review he has declined for now. The PR does what its ticket asks for a narrowing, and the open question on it is the Solr 11 one: removal or narrowing. That question belongs to the 18446 Blocker, not to the PR's merge.
- The 18470 plan: its scope question is the Solr 11 release-blocking conversation. It is not a work-now item. Nothing in the reply asks for the plan's first slice to be built or reviewed.
- Nick's selection posture: the factory rule is to solicit reviewers before work. The posted comment already follows that rule. The gap is that the reviewer it asks for is the one who declined review. The posture needs no change; the next step needs a reviewer who has said yes.

## 3. Delivery vehicle

Recommendation: a new short comment on SOLR-18470, not an edit of 18131208.

Reasons:
- Smiley is a watcher on SOLR-18470. An edit re-notifies him, and his reply already shows that a notification on this thread lands badly. A new comment is a single notification with the correction in it.
- The posted comment is part of the record of what he read. Editing it rewrites that record. A new comment keeps the record and corrects the framing next to it.
- The comment's facts are correct. It links the right PR, and "covers SOLR-18446 only" is true. The problem is framing, and framing is corrected by saying so, not by changing the old text.

What would have to be true for an edit to be the better vehicle: the posted text would have to state a fact that is wrong and that other readers would act on, such as a wrong PR link or a wrong status. None of that holds here. The comment has no factual error.

## 4. Side finding (not in the draft)

PR #5202's changelog entry is `type: fixed`, while the same PR adds a Solr 11 upgrade note for a behavior change that is not a fix. The two should agree. This is for the PR's owner and the release notes. It does not change the framing above.

## Draft

`jira/jira-comment-SOLR-18470-followup-draft.md`: a short new comment in Jira wiki markup. It acknowledges the review pile and the reviewer rule without arguing with Smiley's email. It states that PR #5202 does only the narrowing step and that removal or deprecation for Solr 11 is the project's call. It says what Nick will not do next on 18470 (no further work without a reviewer who said yes). It asks Smiley what shape of plan, if any, he would want to hear about before Solr 11 work is real. It offers no new scope, sets no deadline, and does not apologize beyond the facts. It ends with the Jira footer.

Research only; no build or test was run.
