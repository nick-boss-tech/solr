# Streaming expressions convergence, round 1

Verdict: **NOT CONVERGED.** All six drafts hold open items. Three carry a blocking finding (SOLR-10322 and SOLR-14231), and the rest carry should-fix items.

Counts: drafts checked 6 (slice A: 9852, 10322, 10882; slice B: 12505, 12657, 14231). Drafts with no finding 0. Drafts with a blocking finding 2 (SOLR-10322, 14231). Drafts with should-fix findings and no blocking finding 4 (SOLR-9852, 10882, 12505, 12657). Drafts with notes only 0. Findings 27: 3 blocking (ST-R1-01, 32, 33), 12 should-fix (02, 03, 05, 07, 08, 09, 21, 22, 23, 24, 26, 27), 12 note. All are new on this pass.

Scope: `assignments/pool-streaming-convergence-round-1.md`. The decision record is `reports/streaming-expressions-round-1.md` and its parts, with the draft-fidelity settlements. Heads were checked with `git ls-remote origin refs/heads/<branch>`, and all six match their named heads. Merge-bases were checked against the local `upstream/main`. Two read-only subagents did the review. Nothing was edited, posted or built.

## Verdict per draft

- **SOLR-9852: NOT CONVERGED.** ST-R1-05 (should-fix); ST-R1-06 (note).
- **SOLR-10322: NOT CONVERGED (blocking).** ST-R1-01 (blocking); ST-R1-02, 03 (should-fix); ST-R1-04 (note).
- **SOLR-10882: NOT CONVERGED.** ST-R1-07, 08, 09 (should-fix); ST-R1-10, 11 (note).
- **SOLR-12505: NOT CONVERGED.** ST-R1-21, 22, 23, 24 (should-fix); ST-R1-25 (note). ST-R1-23 is a precondition to opening.
- **SOLR-12657: NOT CONVERGED.** ST-R1-26, 27 (should-fix); ST-R1-28, 29, 30, 31 (note).
- **SOLR-14231: NOT CONVERGED (blocking).** ST-R1-32, 33 (blocking); ST-R1-34 (note).
- **Cross-draft: ST-R1-12 (note).** Owner decision 5 (rebase before opening, or open on the older base with the drift stated) has no recorded recommendation. Under the coordinator default, the harder option applies, and that would change every head named here.

## Register: blocking rows

**ST-R1-01 | SOLR-10322 | blocking.** "With no context cache, the stream's own cache is null too (base L550), so that read dereferences null." The stream's own cache is created later, on `open()`. The read uses the stream context's cache, which is null. Replacement: "With no context cache, that read dereferences null, because the stream context's cache is null. The stream's own cache is created later, on open, and the read did not use it."

**ST-R1-32 | SOLR-14231 | blocking.** "The new tests pass at head `2f73d00a3996da1623156ccef649b59be0b59d45`, verified 2026-10-04." The receipt's "2026-10-04 era" is not a verified run date; the 10-10 date is the head check. The pasted line was not confirmed by the lead. Replacement: "**The new tests pass at head 2f73d00a3996da1623156ccef649b59be0b59d45. The base code was not run against them.**" Restore a run date only when the receipt records one.

**ST-R1-33 | SOLR-14231 | blocking (branch changelog).** The changelog title says the stream "no longer fails when it receives a single node or no nodes". The ticket text is absent, and no base run exists, so the claimed wrong output is not shown. Branch commit: "scoreNodes streaming expression now sets its field and collection from every node, returns no tuples when there are no nodes, and sends its /terms request as a POST." If a wrong output is confirmed, name it in line 7.

## Register: should-fix rows

**ST-R1-02 | SOLR-10322 | should-fix.** The "[base]" link labels do not say they point at the merge-base. Add to the first paragraph of "What happens today": "Links marked base point to the merge-base commit, `97d973814336`."

**ST-R1-03 | SOLR-10322 | should-fix.** "On open, the stream creates a cache only when it has none, and closes it in close()." The open and close code is unchanged by the branch. Replacement: "The stream already creates its own cache on open when it has none, and closes it in `close()` ([L297 to L301](https://github.com/nick-boss-tech/solr/blob/80ce9d7a3c8a61dfb5c5169a371890f11d9e17ff/solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/TopicStream.java#L297-L301), [L352 to L353](https://github.com/nick-boss-tech/solr/blob/80ce9d7a3c8a61dfb5c5169a371890f11d9e17ff/solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/TopicStream.java#L352-L353)). Those lines are unchanged. The change is in the three reads below."

**ST-R1-05 | SOLR-9852 | should-fix.** "Only the getTypeInfo exception class changed." The ticket's SQuirreL error comes from getTypeInfo, which this change does not fix, and whether SQuirreL continues was not checked. Append to the Limits paragraph: "The ticket's SQuirreL error comes from getTypeInfo, so this change does not fix that error. The exception is now the JDBC type, but I did not check whether SQuirreL continues after it."

**ST-R1-07 | SOLR-10882 | should-fix.** The Limits do not name the 2017 umbrella ticket. Replacement: "SOLR-10882 is a 2017 ticket that tracks restructuring the stream evaluators. This change covers the two defects above, not the whole ticket."

**ST-R1-08 | SOLR-10882 | should-fix.** The null-pointer claims have no gated null-value test. Until one exists, replace bold line 7 with "**Sorting an array that mixes text or a boolean with numbers fails with a ClassCastException.**", delete the line 9 sentence about the missing field and `toString()`, replace bold line 13 with "**A sort over mixed kinds now stops with a clear IOException.**", delete the line 15 sentence about `toString()`, and in the Limits say "No test covers a boolean mixed with numbers, or two different non-number classes." and "The boolean case was checked by reading the code only." Remove the null clause. The branch changelog title carries the same claim, so it changes in the same packaging.

**ST-R1-09 | SOLR-10882 | should-fix.** The Proof gives head counts without a date. Replacement: "- With this change, at head `5d7d07a5b17`, verified <date of the vm2 focused run, from its gate log>, ArrayEvaluatorTest runs 7 tests with 0 failures, 0 errors and 0 skipped." The main side supplies the date from the log, which is not in this worktree.

**ST-R1-21 | SOLR-12505 | should-fix.** "fetch() sends its batch as `{! df=<key> q.op=OR cache=false }`". The df value is the join field, not the escaped key. Replacement: "fetch() sends its batch as `{! df=<join field> q.op=OR cache=false }` followed by the escaped key values."

**ST-R1-22 | SOLR-12505 | should-fix.** The links to `QParser.java` and `TermsQParserPlugin.java` name the head for pre-change code. Re-point each to `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` and label it "base". Line 9: "[QParser.java lines 387 to 396, base](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/search/QParser.java#L387-L396)". Line 31: the plugin name and the comma separator, each linked the same way at lines 74 and 150.

**ST-R1-23 | SOLR-12505 | should-fix (precondition to opening).** The Limits omit owner decision 2 on the `on=` field name. The recommended option is to escape or validate the name in code, with a test. No ruling is recorded, so the recommendation is adopted by the coordinator default. The head does not do it yet: `FetchStream.java` line 239 still appends the join key unescaped. No draft text fixes this; the branch needs a commit that escapes or validates the name, with a test, and a new focused run. If the owner rules otherwise, add to the Limits: "- The field name in on= goes into the batch without escaping. Whether it can carry end-user text depends on deployment."

**ST-R1-24 | SOLR-12505 | should-fix.** Base drift is not stated. The merge-base is 72 commits behind the local `upstream/main`. Until the draft is rebased, add after line 40: "- Base: this change is written against merge-base 14c7aac0d151402b00259e2fb9bf5eed7049ec5d, which is 72 commits behind upstream main. It has not been rebased."

**ST-R1-26 | SOLR-12657 | should-fix.** "On the base FacetStream and base metrics, both fail with the ClassCastException from the ticket." The runs used the pre-fix head `16a0a69e8398079cceb4bec4bd7e0f6bcf02c469` on 2026-10-07; the test file and FacetStream are unchanged at the current head. Replacement: "On the base FacetStream and base metrics, both fail with the ClassCastException from the ticket. These runs used head 16a0a69e8398079cceb4bec4bd7e0f6bcf02c469, the pre-fix head, on 2026-10-07; the test file and FacetStream are unchanged at this head." On line 29, replace "it fails" with "exactly 1 of 3 tests fails".

**ST-R1-27 | SOLR-12657 | should-fix.** "Verified 2026-10-10." on lines 28 and 29. The only run timestamp in the receipt is 2026-10-11 UTC; 2026-10-10 is the recording date. Replacement: "Verified 2026-10-11." on both lines, unless the main side confirms a different run date.

## Notes

ST-R1-04 (the premise log's header is not read; the main side reads it before publishing), ST-R1-06 (the key match is in `Tuple.java`, which the draft does not link), ST-R1-10 (the changelog title says "incomparable types" and is loose), ST-R1-11 (the receipt labels `e044bf20` as the merge-base; `cabedd1d968` is the merge-base with upstream/main), ST-R1-12 (the rebase decision for all three drafts), ST-R1-25 (the realtime-get limit could cite a 2021 comment), ST-R1-28 (the "ISO-8601 instants" wording should say UTC form, and the mixed-value test range), ST-R1-29 (a link for `FacetStream` lines 952 to 958), ST-R1-30 (the compile check of the core and SQL callers), ST-R1-31 (a javadoc position on the branch), and ST-R1-34 (the nested-gather jargon in Limits), plus ST-R1-35 (length over the 3,500 guide, an owner call).

## Owner items on the record (not findings)

SOLR-12505 decision 3 (the Choice section is kept, the default). SOLR-14231's length and its fail-without-fix run. SOLR-10322 has no Choice section (the round default; owner decision 4 open). The SOLR-17433 interaction is left out of SOLR-10322's Limits, because the recommendation is to retire SOLR-17433.

## Not checked

- Gate logs and JUnit XML, which are not on disk, so Proof counts rest on receipts.
- The base proof and head runs for SOLR-10322 (the log header).
- Live Jira state; the SOLR-14231 ticket packet is absent.
- The master receipts ledger, the takeover log and security material.

## Convergence line

NOT CONVERGED. Three blocking and twelve should-fix findings are open across the six drafts.
