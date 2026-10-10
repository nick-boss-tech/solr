# Pool assignment: evidence restoration runs (SOLR-18178, SOLR-18482)

Capability tags: `gate`. Intended host: vm2. Claim path: claims/pool-evidence-restoration-round-1.md.

Background: review confidence round 3 and the main side's fixes found two live PR Proof claims with no run record behind them. The bodies were corrected to what the records support. These two runs restore the missing evidence; results go into the receipts, and the main side updates the bodies from the receipts.

Job 1: SOLR-18178 (PR #4968), branch solr-18178-submit at head b75e7d4d3c4. Run org.apache.solr.core.DownloadConfigSetAPITest (module :solr:core) at the head, fixed seed of your choice recorded in the receipt. The earlier body claimed 5 of 5 with no record anywhere; the run's actual count and outcome replace it. Append the result to receipts/SOLR-18178.md (class, tests, failures, seed, date, log path on vm2).

Job 2: SOLR-18482 (PR #5009), branch solr-18482-submit at head 49ca9099d8e. Premise run: identify the tests the branch adds or changes (diff against its merge-base), run those classes at the head (expected pass), then run the same classes with the branch's production files reverted to merge-base and the branch's tests kept (expected premise-shaped failures). Record per-class counts for both legs in receipts/SOLR-18482.md so a fail-before claim can name classes. If a class does not compile on base production, record that as inconclusive by construction for that class.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeat, mark the claim DONE in the same push as the deliverable, the gating host writes the receipt). One Gradle build at a time on vm2; GRADLE_USER_HOME under /workspace per hosts/vm2.md. No body edits; receipts only.
