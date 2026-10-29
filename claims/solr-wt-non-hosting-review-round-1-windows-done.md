# DONE: non-hosting-node wt routing review, Windows round 1

- Assignment: assignments/solr-wt-non-hosting-review-round-1.md.
- Claim: claims/solr-wt-non-hosting-review-round-1-windows.md at 339efc2615cf338a5bb76ff0c7a3bbccf5035138.
- Completion: receipts/solr-wt-non-hosting-review-round-1.md, with questions 1-4, source/evidence corrections, history, fix selection, SOLR-16398 impact and the rung-5 recommendation.
- Search appendix: reports/solr-wt-non-hosting-jira-search-round-1-windows.md.
- Public-action draft: jira/solr-wt-non-hosting-comment-draft-round-1.md, NOT POSTED. Existing SOLR-17241 recommended; no duplicate new-ticket draft.
- Verdict: review COMPLETE; any public comment awaits Nick. No implementation or runtime verification performed on Windows. Use SOLR_PARAMS fallback in a separate shared-filter fix; preserve both hosting and non-hosting tests. Current SOLR-16398 JSON-parser tests are not proved intermittent by default-javabin failures.
- Decision ladder: rungs 1-3 establish current mechanism, existing issue, negotiation intent and actual branch test scope; rung 4 chooses localized filter fallback; rung 5 is one owner question before commenting on JIRA.
- Add-only completion record supersedes the assignment's OPEN status for this claimed review. WORKFLOW.md prohibits workers editing posted assignments; the source assignment is left intact for its owner/index generator to reconcile.
