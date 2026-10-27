# SOLR-18325 Jira proposal posted and reviewed, Windows round 1

Status: DONE. One authorized comment posted; separate content read-back and rendered-markup review passed. No correction or second comment was needed.

- Jira: [SOLR-18325 comment 18133265](https://issues.apache.org/jira/browse/SOLR-18325?focusedCommentId=18133265&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-18133265).
- Author: Nick Shanin. Created and last updated: `2026-10-27T21:39:47.227Z`, equivalent to October 27, 2026 at 17:39:47.227 EDT.
- Authorization: Nick's direct instruction in this chat, "OK I think you could post the comment and give it a review pass after posted", after agreeing the staged proposal. This was specific authorization for the Jira comment, not the standing PR follow-up exception.
- Claim: `claims/solr-18325-jira-comment-windows-round-1.md`, published at `7441ccdbcf38801f279b5e55cad37b3b7724ce8b`.
- Pre-post board snapshot: `13abb7ad5eccc28fefa080e81de90f87c9a64156`. Live discussion checked twice immediately before posting: only comments `18108711` and `18108756`, with no equivalent proposal. Posting used the Apache Jira connector once.
- Current fork source independently verified at `8bfca8edd30acf11bdf9d6a733312e97f99ec78a`. Proof records re-read: evidence closeout and binary-attribution receipts. No code or tests changed.

## Post-publication review

A fresh connector issue read confirms the new comment is present under Nick Shanin and the issue remains Open. A separate read-only REST GET of comment 18133265 with `expand=renderedBody` returned the stored body, author, timestamps and rendered HTML. Stored text matches the local outbound text exactly after CRLF normalization and removal of its trailing file newline. It has 2,878 characters, approximately 2,652 with URLs removed, one closing question, and no em/en dashes or internal process terms.

Jira's rendered HTML confirms the Summary label is bold at the top, `epugh` resolves to Eric Pugh, all four bullets are a proper list, code spans and version emphasis render, the three explicit links retain their intended targets, and the AI disclosure appears as a footer. The evidence report blob was verified at the pinned board revision; SOLR-17968 was re-read through Jira, and PR 3829 was verified as the merged refactor.

Content review found no correction needed:

- Proposes finishing the client change for 10.x only if it is acceptable independently. Server and other modules are still to be implemented/tested for possible 10.x inclusion; no requirement to defer them to 11 is claimed. Solr 11 is offered as maintainer preference.
- Reports the successful tested consumer, resolver and 15-test results without claiming that all core tests pass or that the candidate is release-ready.
- Discloses the mapper field's actual type break despite the existing internal marker. Reports the public/protected comparison against the pre-Jackson base, not universal binary compatibility.
- Keeps the old binary failure visible and attributes the observed timeout linkage failure to the prior HTTP refactor on both base and candidate. Links the original refactor issue and PR, not an invented new defect ticket.
- Distinguishes tested Jackson 3.2.1 from the ticket's requested 3.1.x and notes that a representative Boot 4 application remains unverified.
- Asks for direction, without requesting a review commitment, opening a PR, closing the ticket, changing a release target, or waiving any proof requirement.

## Records and next state

Local outbound draft: `research/SOLR-18325-jira-comment-draft.txt`, SHA-256 `efc7580110fb0dab0d4665be650fd2d592d01aa953fd268d44b7e7a62135f1f2`. The actual stored body and rendered HTML are preserved in local `research/SOLR-18325-jira-comment-posted-18133265.json`.

The daily ledger update appends one owner-directed staged-proposal row under `receipts/comment-ledger/2026-10-27.md`, based on reviewed blob `89a3c8fecce39be657ac2f29fa492cc6e53a828f`. Five records existed before this post, none for SOLR-18325; this post is below the cap even if conservatively counted toward the active stream. Shared `jira/COMMENTS.md` remains with its designated owner; this report supplies the posted URL and disposition for that index.

Next state: await maintainer engagement on this proposal. Existing source head and INCOMPLETE binary compatibility result remain unchanged. The separate HTTP investigation assignment remains available; no production restoration, shim, server migration, new gate run or automation change was made here.

## Posted text

```text
*Summary:* A SolrJ-only Jackson 3 prototype on the Solr 10 development branch passes the tested Java 17 consumer and JSON compatibility checks. I propose completing the client change first for 10.x, then assessing whether the server and remaining modules can follow in 10.x. I can target Solr 11 instead if preferred; the server migration has not yet been implemented or tested here.

[~epugh], following up on your question about consumer impact, I investigated a client-first migration, leaving server-side code on Jackson 2.

The [test results and API analysis|https://github.com/nick-boss-tech/solr/blob/23115b3c946bea6cf54155d7f666214741e1de13/reports/solr-18325-evidence-closeout-linux-round-1.md] show:
 * Maven and Gradle resolve the candidate without Jackson 2 core or databind on the tested consumer's runtime path; the shared Jackson annotations remain.
 * A consumer compiled with {{--release 17}} passes query/update/response checks and HTTP operations against a released Solr 10.0.0 server.
 * The 15 focused Jackson tests pass. Deliberately removing the compatibility settings causes the expected failures for JSON defaults, depth/string limits and exception wrapping.
 * The public/protected API comparison against the pre-Jackson base finds the {{JacksonContentWriter.DEFAULT_MAPPER}} type change and three additive helpers. That class is already marked internal, but callers using its mapper field would still need changes. The {{solr-api}} API is unchanged.

Released-binary compatibility remains unresolved: an application compiled against SolrJ 10.0.0 fails at {{HttpJdkSolrClient.Builder.withConnectionTimeout}} on both the unchanged development base and the Jackson prototype. This traces to the earlier HTTP hierarchy refactor in [SOLR-17968|https://issues.apache.org/jira/browse/SOLR-17968] ([PR 3829|https://github.com/apache/solr/pull/3829]). I am investigating that separately; these results do not establish a drop-in replacement for 10.0.0.

The experiment used Jackson *3.2.1*, not the ticket's requested *3.1.x*. The selected version and a representative Spring Boot 4 application still need verification. Full server/module migration is outside this prototype.

If a client-only change is acceptable independently, my recommendation is to finish its remaining checks and submit it for review for 10.x. The expected scope covers dependency declarations, {{JacksonContentWriter}}, {{JacksonDataBindResponseParser}}, response conversion, tests and small core adjustments to keep server-side Jackson 2 independent. The server and remaining modules would then need their own implementation and compatibility testing to establish whether they can also target 10.x.

Is this evidence sufficient to pursue that staged approach for Solr 10, or would you prefer a coordinated migration targeting Solr 11?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
```
