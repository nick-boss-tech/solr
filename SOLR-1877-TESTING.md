# SOLR-1877 - hypothetical reproduction and fix (not run)

Nothing here was compiled or executed. The fix and the test were written by reading
`upstream/main`; treat every claim below as a guess to verify first.

## JIRA context
"Investigate unclosed Reader in IndexBasedSpellCheck" (2010, Open). Yonik: the spellcheck framework
has several readers to worry about and "Solr never seems to close its spell checkers".
Six test classes still carry `@SuppressTempFileChecks(bugUrl=...SOLR-1877...)` because of it,
and `AbstractLuceneSpellChecker.initIndex` has a comment saying the same.

## Leaks found on main
1. `SolrSpellChecker` has no close hook; `SpellCheckComponent` never closes its checkers.
   `AbstractLuceneSpellChecker` keeps a Lucene `SpellChecker` (which opens a reader on the
   spell index in its constructor) and a `Directory` (`FSDirectory` wrapped in a
   `FilterDirectory`) open for the life of the JVM.
2. `IndexBasedSpellChecker.initSourceReader` opens `FSDirectory` + `DirectoryReader` for the
   configured `location` on `init` and again on every `reload`, never closing the old ones.

## Fix
- `SolrSpellChecker implements Closeable` with a no-op `close()`.
- `AbstractLuceneSpellChecker.close()` closes the `SpellChecker`, then the spell index directory.
- `IndexBasedSpellChecker` remembers the source `FSDirectory`; `initSourceReader` closes the
  previous reader and directory after the new one is open; `close()` closes the current ones.
- `SpellCheckComponent.inform` registers a `CloseHook.postClose` that closes every checker.

## What the test pins
`IndexBasedSpellCheckerTest.testSourceReaderClosedOnReloadAndClose`: after `init` the source
reader has refcount 1; `reload` replaces it and drops the first to 0; `close` drops the second to 0.

## What was guessed / verify first
- `SpellChecker.close()` throws on a second call (`ensureOpen`); close is called once per checker,
  but a checker shared by two component instances would break. I believe none is.
- Reload closes the old source reader while an in-flight request may still use it via
  `determineReader` (Andrzej suggested incRef/decRef in the ticket). The window is narrow; if
  that is not acceptable the reload close should be dropped and only `close()` kept.
- `postClose` was chosen so no request can still be running. Check that `Directory.close()` on the
  `FilterDirectory` wrapper does not double close the inner `FSDirectory`.
- The `@SuppressTempFileChecks` annotations were NOT removed: tests that call `checker.init`
  directly never call `close()`, so they would still leak. Candidates for removal after a run:
  `SpellCheckComponentTest`, `DistributedSpellCheckComponentTest`, `SpellCheckCollatorTest`
  (they go through a core, so the new close hook applies).
- Fail-before: revert the three spelling classes and the component; the new test fails on
  the first refcount assertion after `reload`.
- Spotless formatting.
