# SOLR-12246 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses. This ticket was first skipped (no repro steps, likely benign) and revisited because the user wanted a guessed change and test.

- JIRA: https://issues.apache.org/jira/browse/SOLR-12246 - "Any full recovery complains about checksum mismatch for a .liv file" (Varun Thacker, no comments). Every full recovery logs a WARN like `File _2yzfn_7pc.liv did not match. expected checksum is X and actual is checksum Y. expected length is N and actual length is N`, although `IndexFetcher` downloads `.liv` files anyway.
- Branch: `solr-12246-submit` off `apache/solr main` `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood (diagnosis is the ticket's)
`IndexFetcher.compareFile` logs a mismatch at WARN in the checksum branch, but at INFO in the equivalent length-only branch. A mismatch just means "fetch this file again" (and for `.liv`, `.si`, `segments_N` and small files `filesToAlwaysDownloadIfNoChecksums` forces the download anyway), so WARN alarms users without any action to take. I did not find out why the `.liv` checksums differ with equal lengths (possibly a differing live-docs file with the same generation name, which would be a deeper problem than log noise).

## What the branch changes
- `IndexFetcher.compareFile`: the checksum-mismatch message is logged with `log.info` instead of `log.warn`.
- New `IndexFetcherCompareFileTest` (`SolrTestCaseJ4`, same package): writes a small Lucene file with a codec footer, compares it with a wrong checksum, expects `equal == false`, `checkSummed == true`, and no WARN from `IndexFetcher` via `LogListener`.

## What was guessed (verify these first)
1. `LogListener.warn(IndexFetcher.class)` and `pollMessage()` returning null when nothing was logged (taken from `LogListener`'s API listing, not from an example of this exact use).
2. `CodecUtil.writeHeader/writeFooter` on a plain `IndexOutput` gives a file whose `retrieveChecksum` works, so `checkSummed` is true.
3. The maintainers may prefer keeping WARN when lengths differ too, or logging only for non-forced-download files; INFO was chosen to match the length-only branch.
4. The real cause of the differing `.liv` checksums on full recovery (which could be a real bug) is not addressed.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.handler.IndexFetcherCompareFileTest"
```
Fail-before: revert only `IndexFetcher.java`; the test sees a WARN.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
