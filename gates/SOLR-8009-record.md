# Record job: SOLR-8009 counted-set reconciliation for FullSolrCloudDistribCmdsTest

- Branch: solr-8009-submit. Head: 8795661ddc98c8bffdb8db49a7dd5f293216e929 (live tip, verified by ls-remote at posting, 2026-10-11; GATE GREEN at this head per receipts/SOLR-8009.md).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed.
- Capability tags: gate (record run only).
- Job type: record only. One run at the head, recorded in receipts/SOLR-8009.md. No branch commit, no push.
- Why: receipts/SOLR-8009.md counts FullSolrCloudDistribCmdsTest at 10 tests, but the head's test file declares 9 test methods, and no record explains the tenth. Either the run's tenth testcase has an origin the file alone does not show (inherited from a base class, or a variant the runner expands), or the recorded count is wrong. This run settles which.
- Run: FullSolrCloudDistribCmdsTest in its module at the head (class and module read from the branch's test file by grep, per the FQCN rule). From the fresh JUnit XML, enumerate every testcase that ran. Compare the list against the test methods declared in the head's file. Name the tenth testcase and its origin, or, if the XML shows 9, record that and correct the count in receipts/SOLR-8009.md.
- Method rules: the count stands only on the fresh JUnit XML from this run, not on console output; one Gradle build at a time on the host; no tidy or packaging step applies: the branch tree is not modified.
- On completion: receipts/SOLR-8009.md carries the reconciliation (the enumerated set, the tenth test named with its origin, or the corrected count), this job is marked DONE, and the claim is marked DONE.

## Step checklist

- [ ] FullSolrCloudDistribCmdsTest run at 8795661ddc98; testcases enumerated from fresh JUnit XML
- [ ] the tenth test named with its origin against the 9 declared methods, or the receipt count corrected; receipt updated
- [ ] job and claim marked DONE
