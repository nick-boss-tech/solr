# Correction: SOLR-18325 ladder decision (vm2, round 1)

Corrects `claims/solr-18325-ladder-decision-vm2-round-1.md` (board `ebd67b9043e`). Add-only; that file stays as written.

The restore decision there is already implemented on the fork branch `solr-18600-http-abi-restore-submit` (head `c40665af8d6b`, base `origin/branch_10x` `a336bf871b2c`) under SOLR-18600, with gate round 1 evidence in `receipts/SOLR-18600.md`. That receipt records a consumer built against released 10.0.0 failing with `NoSuchMethodError` on published 10.1.0 and passing on the branch's solrj jar.

So the SOLR-18325 binary leg does not need a new head. The next step is the SOLR-18600 PR, which the receipt says waits for Nick's go. The decision record's "implement A on a new head" step is withdrawn.

Note on base: the 18600 receipt targets the 10.2 line (`branch_10x`). The break also exists in the 10.1.0 release, so a 10.1.1 backport or a main port is a separate step on Nick's call. My earlier analysis used upstream main `e59ce3c97d`.

No GitHub, PR, or JIRA action taken.
