# Search components sub-batch 3, part w6 (SOLR-11153, 12543, 13245, 15041)

Result: all four heads match the live remote and the claim. Drafts are written for all four, but none is ready to post: 11153 and 15041 need FIX items first, 12543 needs three Claude trailers removed from history, and 13245 waits on the owner's scope call.

Method: read only. `git ls-remote origin` for the four branches; `git show`, `log`, `diff`, `merge-base`; trial merges with `merge-tree --write-tree` (no refs written). Read the receipts, the `research/jira-context` packets, the round-28 reviews, and the workspace test rules from disk. No builds, no tests, no `gh` calls, no posting, no commits.

## Heads and bases

| Ticket | Live head | Base (merge-base with upstream/main) | Receipt base | Trial merge with upstream/main |
|---|---|---|---|---|
| 11153 | 093df0d65bdc (matches) | e2cdb2d7e8ae | not stated | clean |
| 12543 | 88d236db6b7d (matches) | 14c7aac0d151 | 14c7aac0d151 (proof base) | clean |
| 13245 | 16e62ab65425 (matches) | 22a8cfebbbdb | 22a8cfebbbd (proof base) | clean |
| 15041 | 55fca0a7c243 (matches) | b5c71bc5573c | b5c71bc5573c (round-28 review) | clean |

Upstream/main is 8e62c2686882 (2026-10-09 11:00:55 -0400). A path-filtered log shows no upstream commit since each base that touches the files each branch changes. All commits on the four branches are authored by Nick Shanin. The four branches touch disjoint files.

## Findings

1. FIX (15041). A blank line inside a split value loses a line break, and later breaks get the wrong terminator. File: `solr/core/src/java/org/apache/solr/handler/loader/CSVLoaderBase.java`, the break branch of `recordTerminators` at lines 227-235 (head 55fca0a7c2431f0fe335aaf6fb7387c7343a66bd). `split` at line 178 takes the terminator by index. Evidence: the CSVLoader strategy passes `ignoreEmptyLines = true` (lines 302-312, 8th argument). `CSVParserTest` lines 324-328 show that `hello,\r\n\r\nworld,""` parses to two records, so the parser skips the blank line. `recordTerminators` adds one entry per break, so the blank line adds an entry the parser never uses. Read from the code; not run. Expected results: value `x!a\n\nb` gives `a\nb` (should be `a\n\nb`); value `x!a\r\n\nb\r\nc` gives `a\r\nb\nc` (should be `a\r\n\nb\r\nc`). Replacement: in the break branch, when a break comes directly after another break, append it to the last terminator instead of adding a new entry. Track this with a flag that is set after a break and cleared by any other character. Add a focused test for both inputs above. The draft's Limits line names the blank-line case; rewrite the draft after the fix.

2. FIX (12543). Three commits carry `Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>`: `685f2d1ed23`, `b7127494797`, and `5331639cc76`. Evidence: `git log -i --grep=Co-Authored-By 14c7aac0d151..origin/solr-12543-submit`. The user's memory rule says no Claude trailers on commits pushed to the user's repo. Replacement: remove the trailer line from those three commit bodies. This rewrites a pushed fork branch, so do it only after explicit authorization. The other three branches have no trailers (same grep, zero hits).

3. FIX (11153 and 12543). Handoff-doc commits are in branch history, and a PR's commit list shows them. 11153: `936855708e0` adds `SOLR-11153-TESTING.md` (subject: "add hypothetical-reproduction handoff doc"), removed by `093df0d65bd`. 12543: `5331639cc76` adds `SOLR-12543-TESTING.md` (same subject kind), removed by `53645c899b3`. The final trees and net diffs are clean. Replacement: squash those commits into their neighbors, so no internal file or subject appears in the PR. Needs owner authorization for the history rewrite.

4. FIX (11153). The changelog title names only `wt=schema.xml`, but the writer also runs when Solr saves a managed schema. Evidence: `SchemaXmlResponseWriter.java` line 28 (head 093df0d65bdc) for `wt=schema.xml`; `IndexSchema.java` lines 449-458 (`persist(Writer)`) call `SchemaXmlWriter.writeResponse`; `ManagedIndexSchemaFactory.java` line 390 calls `persistManagedSchema(true)`. File: `changelog/unreleased/SOLR-11153-schema-xml-missing-name.yml`, lines 1-3 (the `title: >` block). Replacement title: `Writing a schema as XML no longer fails with a NullPointerException when the schema has no name attribute.` The draft already uses the wider wording.

5. FIX (11153). Three comments overstate the version case. IndexSchema always sets a version: `IndexSchema.java` line 514 defaults it to `1.0f`, and the version property is always written (line 1578 handler; line 1693 skips only null values, and a float is never null). (a) `SchemaXmlWriter.java` lines 89-90 say a schema without a version "has no such property". Replacement for lines 89-90: `// a schema with no name has no name value: omit the attribute instead of failing with a NullPointerException. IndexSchema always sets a version, so the version check is a guard.` (b) `SchemaXmlWriterMissingNameTest.java` line 34 says the map is "what IndexSchema.getNamedPropertyValues() yields for a schema lacking both attributes". The real map has a version. Replacement for line 34: `// what IndexSchema.getNamedPropertyValues() yields for a schema with no name; the real map also has a version, which IndexSchema always sets`. (c) Rename the method on line 33, `testSchemaWithoutNameOrVersion`, to `testSchemaWithoutName`. Owner decision 5 covers whether to keep the version guard at all.

6. FIX (13245). Comment layout in `solr/core/src/java/org/apache/solr/handler/StreamHandler.java`. Lines 93-94 are an orphan comment (`// see inform(): shared by all StreamHandlers ...`) followed by a blank line, then `@Override` on line 95. Lines 113-115 split one sentence across three lines ("see" and "them" stand alone). Replacement: delete lines 93-94. Replace lines 113-115 with two lines: `// SOLR-13245: daemons are registered per collection (SolrCloud), so list, start, stop` and `// and kill see them on every co-located replica of the collection.`

7. FIX (13245). A behavior change is missing from the changelog. At `StreamHandler.java` lines 263-266 (head 16e62ab65425): `if (daemons.containsKey(id)) { daemons.remove(id).close(); }`. Because the map is now shared per collection, a start with a name that a sibling replica already runs closes that daemon. The changelog title (`changelog/unreleased/SOLR-13245-daemons-visible-on-all-replicas.yml`, lines 1-3) does not say so. If the owner keeps this behavior (owner decision 2), replacement title: `Streaming daemon list, start, stop and kill requests now see the daemons of every replica of the collection on that node. Starting a daemon whose name is already in use on that node replaces the running one.`

8. NOTE (13245). Stale entries. `CoreContainer.java` line 284 holds the node's `ObjectCache`, line 715 is its getter, and line 1268 closes it only at shutdown. A grep of `solr/core/src/java` at head found no removal of the collection-keyed entries. Effect: daemons left running after a collection is deleted stay listed when a collection with the same name is created later on that node. No test covers this. Owner decision 2 covers the fix. The draft's Limits discloses it for now.

9. NOTE (12543). Partial fix, and the draft says so. `ExportWriter.java` at head 88d236db6b7d still writes status 400 into a 200 body at lines 236-240 (score sort), 262-266 (missing `rq={!xport}`), and 288-292 (fl with score). The draft's Limits names all three. Owner decision 4.

10. NOTE (12543). The draft's Proof says the two tests fail on base with "expected 400, actual 200". That text comes from `receipts/SOLR-12543.md` line 7. The gate log is not on disk, so the exact assertion text is unconfirmed. Confirm it from the base run before posting. If it differs, change the Proof line only.

11. NOTE (12543). No base run is recorded for the changed assertions in `TestExportWriter` (`receipts/SOLR-12543.md` line 6 gives only the head count). The draft says no base run is recorded.

12. NOTE (15041). The draft's Proof says the new test fails on the base code, based on the receipt's "pre-fix proof PASS" (`receipts/SOLR-15041.md` line 7). The Solr-issues `AGENTS.md`, Test Runs section, defines the fail-before verdict PASS as "failed without the fix". If the owner reads PASS differently, change the Proof line.

13. NOTE (15041). `research/branch-reviews/round-28/SOLR-15041-review.md` line 3 gives "Verdict: Close" without defining it. Its findings say no blocking issue, and it names only a lone CR and escaped encapsulators as follow-ups. It did not find the blank-line case in Finding 1. Owner decision 8.

14. NOTE (15041). There is no local Jira packet for SOLR-15041 (`research/jira-context` has 11153, 12543, and 13245 only). The round-28 review (line 8) says a read-only lookup showed Open/Major with no description. The draft does not cite ticket text.

15. NOTE (13245). The formula says narrow scope on a straightforward patch is not a choice. This ticket's comments name two mechanisms (Jira comment 16786577: ephemeral znodes, or iterate over all replicas), so I kept the scope question as a Choice. Owner decision 1.

16. NOTE (12543). The draft's Choice section (handler check versus writer status) is my judgment. Owner decision 3.

17. NOTE (all). The local branch refs in this worktree are stale for three branches: `solr-11153-submit` at `936855708e0` (origin `093df0d65bd`), `solr-12543-submit` at `5331639cc76` (origin `88d236db6b7`), and `solr-13245-submit` at `3c1e023eb80b` (origin `16e62ab65425`). `solr-15041-submit` has no local ref. This review read the `origin/*` refs, which match the live heads. Do not use the local refs for PR work.

18. NOTE (11153). The `@Ignore` on `TestSchemaManager.java` line 81 is in the base file, and the branch does not touch that file. The skip predates this change. The receipt's 33-test total includes it.

19. NOTE (all). No draft names Lucene behavior, so the 9.x and 10.x check did not apply.

## Task results

- SOLR-11153: Draftable. Draft at `pr-drafts/search-components/SOLR-11153.md`, against head 093df0d65bdc. Verdict: held until Findings 3, 4, and 5 are done, and owner decision 5 is made. The premise holds at upstream: the NPE line (`SchemaXmlWriter.java`, base line 89) is still unguarded at upstream/main, and the save path reaches it through `IndexSchema.persist`. The receipt's proof (one new test, failing on base with the NPE) matches the test file. Choice section: omit the attribute versus the ticket's warning and default name.

- SOLR-12543: Draftable as a partial fix. Draft at `pr-drafts/search-components/SOLR-12543.md`, against head 88d236db6b7d. Verdict: held until Finding 2 (trailers) and Finding 3 (handoff commits) are resolved by an authorized history rewrite, and Finding 10 is confirmed. The draft says "partial" and names the three remaining 200 paths. Choice section: handler check versus writer status.

- SOLR-13245: Draftable. Draft at `pr-drafts/search-components/SOLR-13245.md`, against head 16e62ab65425. Verdict: held for owner decision 1 (scope) and decision 2 (start replacement and stale entries). Findings 6 and 7 must be fixed in the same change. The base run in the receipt uses the branch test file, and `testAPIs` fails there too, so the draft says that failure does not isolate the change.

- SOLR-15041: Held. Draft at `pr-drafts/search-components/SOLR-15041.md`, against head 55fca0a7c243. The draft is written for the head as it stands, but Finding 1 (blank line) is a real defect by code reading. The draft must not be posted until the fix is in and a focused test covers it, which needs owner authorization for a run. No Choice section: the only decision is narrow scope, which the formula does not count. No premise count is cited, as the record requires.

Interactions: none among the four tickets. Their file sets are disjoint (SchemaXmlWriter and IndexSchema for 11153; ExportHandler and the export tests for 12543; StreamHandler and DaemonStreamApiTest for 13245; CSVLoaderBase and TestCSVLoader for 15041). No overlap with 11364 or 16155 files.

## Owner decisions

1. SOLR-13245 scope: node-local (as coded) or collection-wide (ZooKeeper registry or fan-out). Keep it as a Choice, or treat it as Limits only per the formula.
2. SOLR-13245 behavior: keep the start-replaces and stale-entry behavior and disclose it (Findings 7 and 8), or change the code first (clear the collection's entries when its last core closes).
3. SOLR-12543: keep the handler check as the Choice, or move the status to the writer.
4. SOLR-12543: plan for the three remaining 200 paths (named in Limits; a follow-up is offered, not promised).
5. SOLR-11153: omit the attribute (as coded) or warn and write a default name (the ticket's route). Also keep or drop the version guard.
6. History rewrites: remove the three Claude trailers on 12543 and squash the handoff commits on 11153 and 12543. Needs explicit authorization for a fork force-push. Not done.
7. SOLR-15041: fix the blank-line case (Finding 1), then authorize a focused run before the draft is rewritten and posted.
8. Meaning of "Close" in the round-28 SOLR-15041 review.

## Not checked

- Live Jira text. I have no Jira tool in this session. I read the local packets for 11153, 12543, and 13245 (files dated 2026-10-03 and 2026-10-04). There is no packet for 15041.
- Gate logs and test-queue results. None are on disk for these four tickets (searched `research` to depth 4). All proof counts come from the receipts only.
- Builds and tests. Compile correctness and test outcomes are unverified. The Finding 1 defect is from code reading and the repo's own `CSVParserTest`, not a run.
- The base failure texts for 12543 and 13245 come from the receipts only.
- Live PR state. No `gh` call was made. The inventory lists 13245 as PR-ready and the other three as gated with no PR, so no PR consistency pass applied.
- How streaming clients handle the new 400 on `/export`. Not traced.
- Whether 11153's `SolrQueryRequestBase(null, ...)` test constructor and `SolrTestCase` import compile against the head. Not checked.
- Trailing line breaks and lone CR handling in the 15041 splitter. Read only, not traced through `CSVParser` end-of-input.
- Whether the Solr `SolrException` path in the 12543 handler reaches clients as HTTP 400 in every deployment. Relied on the code reading.
