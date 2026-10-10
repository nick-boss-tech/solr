# Lucene 9.12.3 version claims: roll-up

Claim: `claims/lucene-version-claims.md` (commit `4967ec15308`). Commits checked: `b569f89dd85` (SOLR-6065 draft wording) and `6413d368e65` (Lucene claims in drafts and receipts). Detail for SOLR-6065: `reports/lucene-version-claims-6065.md`.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Two read-only subagents checked the claims against the local Gradle cache, by reading class files and jar listings. No build, Gradle run, or test was run, and no draft or receipt was changed.

## What the cache holds

- `lucene-core`: 9.12.3 and 10.4.0.
- `lucene-queries`: 9.12.3 and 10.4.0.
- `lucene-suggest` and `lucene-highlighter`: 10.4.0 only.

So the 9.12.3 claims that depend on suggester or highlighter bytecode cannot be checked here. The `FunctionScoreQuery` class is in `lucene-queries` 9.12.3; a jar listing confirmed it. Whether the highlighter calls it in 9.12.3 cannot be checked.

## Result

**SOLR-6065: verified.** The message "number of documents in the index cannot exceed" is thrown by the same two methods, with the same exception, in `lucene-core` 9.12.3 and 10.4.0. The message constants are byte-identical. Not checked here: which update call paths reach those methods, and the BAD_REQUEST mapping at base.

**Suggester drafts and highlighting receipts: not verifiable here, except one class-presence point.** Five claims state a 9.12.3 result. Items 1 to 3 and item 5 depend on `lucene-suggest` or `lucene-highlighter` bytecode, which exists only at 10.4.0. Item 4 depends on `lucene-highlighter` for its usage claim. Each proposed replacement states only the 10.4.0 result, and marks the 9.12.3 point as the main side's record. Nothing has been applied.

The second subagent wrote no report file, so the planned `reports/lucene-version-claims-suggest-highlight.md` does not exist. Its findings are recorded here.

1. **SOLR-10937 draft, the item about the `OfflineSorter` path.** The draft says each build reaches `OfflineSorter` with the lookup's temp directory "in Lucene 9.12.3 as well as 10.4.0, by the same code paths in both versions". Replace with "in Lucene 10.4.0."
2. **SOLR-11844 draft, the blend-rules item.** The draft says the rules were checked "against Lucene 10.4.0 and Lucene 9.12.3 bytecode; in both, ...". Replace with "against Lucene 10.4.0 bytecode: ...", and drop "in both,".
3. **SOLR-11844 draft, the Limits item.** The draft says "The Lucene check is against 10.4.0 and 9.12.3." Replace with "The Lucene check is against 10.4.0."
4. **SOLR-2632 receipt.** The receipt says the `FunctionScoreQuery` case and `FieldQuery.flatten` are "present in Lucene 9.12.3 bytecode". The class itself is in `lucene-queries` 9.12.3. The usage in `WeightedSpanTermExtractor.extract` and `FieldQuery.flatten` is in `lucene-highlighter`, which is 10.4.0 only. Replace with "present in Lucene 10.4.0 bytecode; the 9.12.3 point is recorded by the main side."
5. **SOLR-2681 receipt.** The receipt says the missing case was "verified in Lucene 9.12.3 bytecode as well as 10.4.0". Replace with "verified in Lucene 10.4.0 bytecode; the 9.12.3 point is recorded by the main side."

The receipts are internal, so the words "receipt" and "log" in them are not a problem. The drafts 10937 and 11844 have no internal vocabulary.

## Decisions for you

1. Accept the five replacements, or keep the 9.12.3 statements as the main side's recorded result.
2. Items 1 to 3 change public drafts. They need your ratification before anything is applied. Items 4 and 5 are internal receipts.

## Upstream read in this pass

- `b569f89dd85`: SOLR-6065 draft names the Lucene versions the message prefix was checked against. Its line 39 matches the 6065 verdict above.
- `6413d368e65`: Lucene 9.12.3 claims in drafts and receipts. This is the subject of the claim.
- `245e7b35b78`: main-side updates to the SOLR-6045 and SOLR-12703 drafts (link hashes and one sequencing sentence), the SOLR-2681 recount, and the SOLR-16885 premise result. No Lucene claim changed. Read here, not edited.

## Not done

Nothing was applied to a draft or receipt. No PR, comment, title, or branch was touched. No build, Gradle run, or test was run. The 6065 subagent reports it extracted jars to a scratch directory and deleted that extract afterwards; the lead did not check that.
