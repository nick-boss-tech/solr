# SOLR-10234 - hypothetical reproduction (nothing was compiled or run)

JIRA (Hoss): `BasicDistributedZkTest` failed with "Too many open files" thrown by Lucene's mock `HandleLimitFS`
(fixed `MAX_OPEN_FILES = 2048` for the whole JVM, in `TestRuleTemporaryFilesCleanup`), although a test runs
several nodes in one JVM. Hoss suggested `@SuppressFileSystems` on distributed/cloud tests, or a per-node limit.
On `upstream/main` the base classes carry no such annotation (`SolrTestCaseJ4` only has `ExtrasFS`).

## Change
`@LuceneTestCase.SuppressFileSystems({"ExtrasFS", "HandleLimitFS"})` on `SolrCloudTestCase` and
`BaseDistributedSearchTestCase` (the latter is the parent of `AbstractFullDistribZkTestBase`). `ExtrasFS` is
repeated on purpose: the annotation is `@Inherited`, so a subclass annotation REPLACES the parent's instead of
merging, and dropping it would re-enable `ExtrasFS` for all cloud tests.

## Test (guessed)
`CloudTestBaseFileSystemsTest` reads the annotation of the three base classes by reflection and asserts both names
are present. This pins the suppression; it does not provoke the 2048 limit (flaky by nature, needs random FS choice).

## Guesses to verify first
- Lucene matches the suppression value against the provider's simple class name (`HandleLimitFS`). If the name is
  different in this Lucene version the annotation silently does nothing.
- No test subclass relies on HandleLimitFS to find leaked handles (a subclass with its own `@SuppressFileSystems`
  already replaces the base annotation, so those are unaffected).
- Design pick: suppress rather than a per-node limit. A per-node limit needs a Lucene-side change.
- `SolrCloudTestCase` is not abstract; the annotation applies to it and (inherited) to every subclass.

## Fail-before
Not applicable as a behaviour proof; without the annotations `getAnnotation` is null (for the cloud base) or lacks
`HandleLimitFS`, so the test fails.
