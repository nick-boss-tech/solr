# Premise job: SOLR-6014 stopword-only dismax with cache/cost local params, and as a filter query

- Branch: solr-6014-submit. Head: 505849d2d4e7d8699a2fe34d410773b8ecf3a55a (live tip, matches receipts/SOLR-6014.md).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed.
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

## Step checklist

- [ ] probe tests written for both cases
- [ ] runs executed; outcomes read from fresh JUnit XML
- [ ] verdicts recorded here and in receipts/SOLR-6014.md; job and claim DONE
