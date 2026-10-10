# Claim: Search components round 1, sub-batch 3 (doc transformers, response writers, loaders and handlers)

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: `assignments/search-components-3.md` (commit `77b6b51a7fc`). Seventeen tickets: SOLR-4374, 7390, 7498, 8003, 8240, 9148, 9396, 9864, 10424, 11153, 11364, 12543, 13245, 14678, 15041, 16155, 18356. Audit first; drafts under `pr-drafts/search-components/` for the draftable tickets.

## Heads checked live on 2026-10-09

`git ls-remote`, then a read-only fetch. Every named head matches its live head.

| Ticket | Branch | Live head | Named in the assignment | Result |
|---|---|---|---|---|
| 4374 | `solr-4374-submit` | `801c62290f79` | `801c62290f7` | matches |
| 7390 | `solr-7390-submit` | `7463dd006a78` | `7463dd006a7` | matches |
| 7498 | `solr-7498-submit` | `2050d8e447a7` | `2050d8e447a` | matches |
| 8003 | `solr-8003-submit` | `f1c99a449617` | `f1c99a44961` | matches; no gate, audit only |
| 8240 | `solr-8240-submit` | `fad7a1dd8e23` | `fad7a1dd8e2` | matches |
| 9148 | `solr-9148-submit` | `30f0d7a42d50` | `30f0d7a42d5` | matches |
| 9396 | `solr-9396-submit` | `a5ab2eda4e6a` | `a5ab2eda4e6` | matches |
| 9864 | `solr-9864-submit` | `b5826e466b9a` | `b5826e466b9` | matches |
| 10424 | `solr-10424-submit` | `e629ab8bb293` | `e629ab8bb29` | matches |
| 11153 | `solr-11153-submit` | `093df0d65bdc` | `093df0d65bd` | matches |
| 11364 | `solr-11364-submit` | `562d3e7e685a` | `562d3e7e685` | matches |
| 12543 | `solr-12543-submit` | `88d236db6b7d` | `88d236db6b7` | matches |
| 13245 | `solr-13245-submit` | `16e62ab65425` | `16e62ab6542` | matches |
| 14678 | `solr-14678-submit` | `5d94e6cf3981` | `5d94e6cf398` | matches |
| 15041 | `solr-15041-submit` | `55fca0a7c243` | `55fca0a7c24` | matches |
| 16155 | `solr-16155-submit` | `0881de1ed68b` | `0881de1ed68` | matches; flagged submission, audit only |
| 18356 | `solr-18356-submit` | `c179cd35713e` | `c179cd35713` | matches; retired, audit only |

## Shared rules for every part

- Read only, except the report file and the drafts for your part. No commit, push, checkout, reset, merge, stash, or fetch of anything new. `git show`, `git log`, `git diff`, `git merge-tree`, `git rev-parse`, `git cat-file -e`, `git grep`, `git ls-tree`, and `git merge-base` are fine. Trial merges with `git merge-tree --write-tree` are fine if they write no ref.
- No builds, no Gradle, no tests, no test runs of any kind.
- `gh pr view <number> --repo apache/solr --json 'title,body,headRefOid,mergeable,mergeStateStatus,state'` (one quoted field list) and `gh pr checks` are the only `gh` calls allowed, and only for a live-PR consistency pass. Nothing that writes.
- Post nothing anywhere. No PR, no comment, no JIRA. No new Jira ticket unless someone asks.
- Proof numbers come only from the receipt named for the ticket. Gate logs are often not on disk; say so.
- Drafts follow `pr-formula.md`: the AI header as line 1, the Jira link line, a bold one-line summary in each section, "What happens today", "What this change does", "Proof", "A choice to check" (only with a live alternative and a pointed question), "Limits", the Changelog line with a link, and the AI assistance footer. Length guide about 3,500 characters. Each draft names the head it was written against in its Proof. Proof states what ran, at which head, what passed, and that the new test fails without the fix, when a receipt supports that. A Proof marked inconclusive, pin, or classified is stated as such.
- Public text carries no internal process vocabulary (gate, receipt, ledger, handoff, top-up, takeover, audit, rc=0, JUnit XML, pre-fix proof as a label, "submission" as a workspace word, internal log names, "premise run").
- Plain words and short sentences. No em dash and no en dash in anything you write.
- Lucene version claims: check against both the 9.x and 10.x lines wherever a draft names Lucene behavior, and say which line you checked.
- A moved tip, or a diff that differs from the receipt, is flagged, not silently adopted.
- Every finding: file and line, evidence (SHA and line, receipt line, or command output), and exact replacement wording. Mark FIX or NOTE. If you cannot check something, say so under "Not checked". Do not guess.

## Parts

Six subagents. Reports go to `reports/search-components-3-<part>.md`. Drafts go to `pr-drafts/search-components/`.

**Part w1: SolrReturnFields pair (SOLR-4374 and 7390).** Both change `SolrReturnFields.java` and share `ReturnFieldsTest`. State the landing order, and check the hunks and the test file for overlap. 7390's gate ran at a received head whose code content is recorded as identical to the shipped head; check that claim against the diff before any Proof says it. Draft the draftable ones.

**Part w2: DocTransformer machinery (SOLR-9396 and 14678).** 9396 (subquery transformer) and 14678 (child doc transformer) both sit on the DocTransformer machinery. 14678 also changes `DocTransformer.java` and `DocTransformers.java`; check 9396's assumptions against those changed base classes. 14678's per-document versus request-global framing is an owner item in its record; present it as a Choice. Draft the draftable ones.

**Part w3: mapUniqueKeyOnly pair (SOLR-8240 and 10424).** Both work the same `mapUniqueKeyOnly` behavior from two sides: 8240 makes JsonLoader honor the field mapping params; 10424 drops `mapUniqueKeyOnly` from the techproducts `params.json`. State how the two changes compose. 10424's record is re-proven after a conf/conf test defect was fixed. Its draft's Proof must match the fixed test's record, not the original shipped claim. 8240's one-line `df` correction is noted in its receipt. Draft the draftable ones.

**Part w4: Flagged and export-adjacent (SOLR-11364 and 16155).** 11364 (`useDocValuesAsStored`) is gated: a focused run of 1 of 1 at the tip, with the wider 58-test battery at the earlier head, both stated in its receipt. The rebase-before-PR housekeeping is an owner item in the record. 16155 has a gate by a hybrid run, but a submission flag: upstream PR #1151 covers this ticket with broader scope, and the main record's verdict is no PR of its own. Audit only; do not draft 16155. Note the interaction between 11364's `SolrDocumentFetcher` path and 16155's `DocumentBuilder` neighborhood, without auditing 16155 past its flagged state.

**Part w5: Gated singles and audit-only (SOLR-7498, 9148, 9864, 8003, 18356).** 7498: focused tests 10 total across three classes; the receipt names the PR notes (existing `null` values in indexes; a metadata-level test). 9148: `TestSQLHandler` 35 of 35; the proof rests on the previous head's gate, and the live-tip run is a docs-only smoke, both stated in the receipt. Do not let the draft suggest the live tip has a full gate. 9864: focused tests 27 of 27. Draft the three draftable ones. 8003 has no gate and premise unverified; it adds only a handoff doc. Audit only. 18356 is retired as obsolete at `c179cd35713`: upstream already landed the identical removal. Audit only; the only question is the branch deletion, which is the owner's call.

**Part w6: Writers, export and daemon (SOLR-11153, 12543, 13245, 15041).** 11153: focused tests 33 total; the proof is recorded against the converted test text. 12543: `TestExportHandlerHttpStatus` 3 of 3 and `TestExportWriter` 19 of 19; the fix is partial, and the draft must say so. 13245: `DaemonStreamApiTest` 2 of 2; the scope question (node-local versus collection-wide) is the owner's call, posed as a Choice. 15041: `TestCSVLoader` 8 of 8; its record states the proof only as PASS with no premise count, so no draft may cite premise counts. Draft the draftable ones.

## Deliverables

1. `reports/search-components-3-w1.md` through `-w6.md`. The lead writes `reports/search-components-3.md`, with per-ticket verdicts (draftable, held with reason, audit-only result for the flagged and retired tickets), the interaction results, and the owner decisions.
2. Drafts in `pr-drafts/search-components/` for the draftable tickets, each naming its head.

Not in scope: opening PRs, posting comments, editing branches or live PR descriptions, builds, Gradle, and test runs.
