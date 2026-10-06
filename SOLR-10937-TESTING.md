# SOLR-10937 - hypothetical reproduction (nothing was compiled or run)

JIRA: building a suggester on a 375 GB index logged `java.io.IOException: No space left on device` from Lucene's
`OfflineSorter` although the data volume had 600 GB free. Varun Thacker diagnosed it: the Analyzing/Fuzzy suggesters sort in
a temp directory (`java.io.tmpdir`), which was a small root volume. The reporter fixed it with `-Djava.io.tmpdir`. The ticket
was left open for two follow-ups: name the path in the error, and document it. The ticket note called it "support".

## Change
Docs only: a NOTE at the top of "Lookup Implementations" in `suggester.adoc` that the build writes temporary sort files to
`java.io.tmpdir` and how to move it (`SOLR_OPTS` in `solr.in.sh`). No code, no test. Naming the path in the error was not done
(the exception comes from Lucene; a wrapper belongs with the `solr-9227-submit` change to `SolrSuggester.build`).

## Guesses to verify first
- `FSTLookupFactory` (FSTCompletionLookup) also sorts through `ExternalRefSorter`/`OfflineSorter` in the temp dir; WFST and TST do not.
  The NOTE lists Analyzing, Fuzzy and FST. Check against the Lucene version in `gradle/libs.versions.toml`.
- Whether `AnalyzingInfixLookupFactory` also needs temp space: it builds a Lucene index under `indexPath`, so it was left out.

## Fail-before
n/a (documentation).
