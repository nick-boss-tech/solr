# Premise job: SOLR-17796 negated collapse case

- Branch: solr-17796-submit. Head: 661165d2673de9211846e276c05daf1fd0e879d6 (live tip, matches the draft in pr-drafts/query-parsing/SOLR-17796.md).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed.
- Capability tags: gate.
- Job type: premise run only. No branch edit, no gate. Posted by the Group O settlement (2026-10-11): the draft's Limits now state that a negated collapse "may still occur there" for the ticket's UnsupportedOperationException, verified by code reading (the branch unwraps only a lone required PostFilter; a negated one stays wrapped, and a wrapped CollapsingPostFilter has no createWeight). The fidelity round asked for the case to be run before posting; this job is that run.

## Case to run

At the branch head, in the TestCollapseQParserPlugin harness shape: a negated collapse query, such as `q.op=AND` with `-{!collapse field=<field>}` as the clause (mirror the ticket's failing shape: a lone clause under AND, negated). Record whether the request fails with `UnsupportedOperationException` from the wrapped PostFilter's weight evaluation, or returns results.

A probe test written for this job lives in the job's worktree only; do not commit it to the branch in this job. Run in :solr:core with seed 17796C0FFEE17796 via the host's normal Gradle route, and read the outcome from fresh JUnit XML.

## Deliverable

- Record the verdict here (EXCEPTION OCCURS or NO EXCEPTION, with the exception text if any), the counts, the seed, and the log path.
- Append the verdict to receipts/SOLR-17796.md in the same push that marks this job DONE. If the exception occurs, the draft's Limits line stands as verified by run. If it does not, say so plainly; the main side restates the bullet. Do not edit the draft in this job.

## Step checklist

- [ ] probe test written for the negated collapse case
- [ ] run executed; outcome read from fresh JUnit XML
- [ ] verdict recorded here and in receipts/SOLR-17796.md; job and claim DONE
