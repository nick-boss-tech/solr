# Premise job: SOLR-17372

- Branch under test: solr-17372-submit at 048862fda8ab76f36f259c6849ec5c5a8cfafabe (live tip, verified by ls-remote at posting, 2026-10-11; commit 3257831180f carries the tolerance hunk in StatsComponentTest). Current main at the checking time: d76fc14b9857e77b830d7a82514b28c523a239c2. Branch merge-base: e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f.
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: DONE by vm2 (coordinator). Verdict NOT SUPPORTED: leg A passes at seed 2074D8EC40F42163 on current main; receipts/SOLR-17372.md.
- Capability tags: premise.
- Outcome: verdict recorded in this file and appended to receipts/SOLR-17372.md: CONFIRMED or NOT SUPPORTED, on the method below.

## Method

Question: whether the percentile variance failure reported on the ticket still reproduces on current main at the seed the ticket reports, and whether the branch's tolerance hunk (StatsComponentTest.testPercentiles: fixed 1.0D becomes Math.max(1.0D, expected * 0.10D)) makes that seed pass. receipts/SOLR-17372.md records a GATE FAILED on premise, but its base and head runs used seed 17372C0FFEE17372, which the record calls the ticket seed without defining it; the seed the ticket itself reports, 2074D8EC40F42163 (research/jira-context/SOLR-17372.json, the 2024-07-17 comment; the packet failure was at the percentile assert, expected 60.0, actual 58.29, outside the fixed 1.0 tolerance and inside 10 percent), was never run. The retire round specified the run below and left it not started: reports/retire-candidates-review-round-rr1.md, tickets SOLR-17372 verdict entry, condition 3. A run on the branch's CI tip does not test the premise: it already carries the hunk.

Run org.apache.solr.handler.component.StatsComponentTest.testPercentiles in :solr:core, seed pinned to 2074D8EC40F42163 for every leg, outcomes read from fresh JUnit XML:

1. Leg A (decisive): current main d76fc14b9857, no hunk. A failure at the percentile assert, checked by content (the expected and actual values in the failure text), means the reported failure still reproduces. A pass means the premise is dead at the ticket's own seed.
2. Leg B: branch head 048862fda8ab, hunk present, same seed. Expected pass: at expected 60.0 the 10 percent tolerance is 6.0.
3. Leg C (optional): merge-base e2cdb2d7e8ae, same seed. Closes the receipt's base claim at the right seed; run it if legs A and B leave any doubt.

Verdict: CONFIRMED if leg A fails at the percentile assert by content and leg B passes. NOT SUPPORTED if leg A passes, or if leg A fails for a different reason (record the actual failure text). This job edits no branch; the salvage or retire disposition follows from the verdict and is recorded on the main side.

On completion, append the verdict, the per-leg counts, the failure text, the seed, and the log path to receipts/SOLR-17372.md, and mark this job and its claim DONE, in the same push. Until then the job stays OPEN so the hourly buffer count keeps it visible as unclaimed supply.
