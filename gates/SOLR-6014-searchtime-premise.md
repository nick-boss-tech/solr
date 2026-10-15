# Premise job: SOLR-6014 stopword-only dismax with cache/cost local params, and as a filter query

- Branch: solr-6014-submit. Head: 505849d2d4e7d8699a2fe34d410773b8ecf3a55a (live tip, matches receipts/SOLR-6014.md).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
Status: DONE
- Capability tags: gate.
- Job type: premise run only. No branch edit, no gate. Posted by the Group O settlement (2026-10-11): two Limits bullets in pr-drafts/search-components/SOLR-6014.md rest on code traces that were never run (round q6 FIX 4 and NOTE 5). Until this job lands, the draft states only that the cases are not covered by the new checks, with no failure claim. This job supplies the run; the draft's final wording follows the result.

## Cases to run (at the branch head, in the TestExtendedDismaxParser harness shape)

1. Cache/cost case: a nested or top-level stopword-only `{!dismax}` clause that also sets a `cache` or `cost` local parameter (for example `{!dismax qf=<field> cache=false}<stopword>` inside an enclosing query, and the same with `cost=...`). Trace being tested: `QParser.getQuery` calls `extendedQuery()` when local params are present even when the parse returned null, and `WrappedQuery` accepts null, so the likely result is a failure at search time where the base matched nothing.
2. Filter case: a stopword-only `{!dismax}` clause used as a filter query (`fq`). Trace being tested: `QueryUtils.parseFilterQueries` adds `fqp.getQuery()` with no null check, so a null filter likely reaches the search path.

For each case record, from the run: fails at search time (the exception class and message), or returns a result cleanly (and what it matches). A probe test written for this job lives in the job's worktree only; do not commit it to the branch in this job. If a case cannot be expressed in the harness, record that and the reason instead of a verdict.

## Run shape

- Worktree of the live tip; probe tests added under the same test class or a scratch sibling; run in :solr:core with seed 6014C0FFEE6014 via the host's normal Gradle route (the gate flock and solr-gradle.sh rules on vm1; ./gradlew where the host allows it).
- Read outcomes from fresh JUnit XML, not from the console log alone.

## Deliverable

- Record per-case verdicts here (FAILS AT SEARCH TIME with the exception text, MATCHES NOTHING CLEANLY, or NOT EXPRESSIBLE with the reason), with counts, seed, and log paths.
- Append the verdicts to receipts/SOLR-6014.md in the same push that marks this job DONE. The main side then writes the draft's final Limits wording from the verdicts; do not edit the draft in this job.

## Verdicts, 2026-10-13 (vm2)

Run: head 505849d2d4e7d8699a2fe34d410773b8ecf3a55a, GATE START 07:02:30Z, GATE END 07:09:06Z, probe rc=0, seed 6014C0FFEE6014, `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestStopwordDismaxProbe6014`. Outcomes were read from the fresh JUnit XML's printed probe lines (XML timestamp 2026-10-13T07:08:44Z). Counts: 8 tests, 0 failures, 0 errors, 0 skipped. The probe test is untracked in the runner worktree (/workspace/gates/solr-6014-gate); nothing was committed to the branch.

Per-case verdicts:

- Case 1, cache local parameter, nested stopword-only `{!dismax}` inside an enclosing query: RETURNS CLEANLY, no failure. numFound=2 (both corpus docs: "big apple", "the big city"). The predicted search-time failure did not occur for `cache`.
- Case 1, cache local parameter, top-level stopword-only `{!dismax}`: RETURNS CLEANLY, no failure. numFound=2.
- Case 1, cost local parameter, nested stopword-only `{!dismax}` joined with AND: FAILS AT SEARCH TIME. java.lang.NullPointerException: Cannot invoke "org.apache.lucene.search.Query.hashCode()" because "this.q" is null.
- Case 2, stopword-only `{!dismax}` as an `fq`, plain: FAILS AT SEARCH TIME. java.lang.NullPointerException with a null message. The probe recorded the class and message only, so the throwing frame is not confirmed by this run; the predicted location (QueryUtils.parseFilterQueries, no null check) is unverified.
- Case 2, stopword-only `{!dismax}` as an `fq` with cache: FAILS AT SEARCH TIME. java.lang.NullPointerException: Cannot invoke "org.apache.lucene.search.Query.hashCode()" because "this.q" is null.

Controls (same run):

- Nested stopword-only `{!dismax}` with no local parameter: numFound=0 (matches nothing cleanly).
- Nested non-stopword `{!dismax}` with no local parameter: numFound=2.
- `fq` with non-stopword `{!dismax}`: numFound=2.

Reading for the draft (the main side writes the Limits wording; the draft is not edited here): a stopword-only clause with `cache` set matches the whole corpus (numFound=2) where the same clause without local parameters matches nothing, so the cache case is a wrong-result finding, not a crash. The cost case and both fq cases fail at search time with a NullPointerException. The cost-and-cache distinction is observed, not traced further.

## Step checklist

- [x] probe tests written for both cases
- [x] runs executed; outcomes read from fresh JUnit XML
- [x] verdicts recorded here and in receipts/SOLR-6014.md; job and claim DONE
