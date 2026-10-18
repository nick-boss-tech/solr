# Skip record: SOLR-5516

- Ticket: SOLR-5516, Solr test ChaosMonkeyNothingIsSafeTest, 2.5-second sleep as the only mitigation for updates arriving during leader sync. JIRA status at check: Open with no resolution, checked 2026-10-18.
- Verdict: keep-open.
- Draft branch: solr-5516-draft-open.
- Pinned head: fork origin/main .
- Checked by: vm5, .

## Ticket reading

Mark Miller's last comment (2013-12-06) says the wait helps in testing, is not enough on slow machines, and some runs still fail. Nothing in the ticket shows the underlying cause was fixed.

## Code reading

- `solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java:209`: `Thread.sleep(2500)` in the replacement-leader branch, before `syncStrategy.sync` at line 219. The sleep is still on main.
- The commit that added the 2.5-second sleep, `2cc87df6ef` (2013), and the later move into `runLeaderProcess`, `07a9b5d1b0` (2020), are both on main (checked in the review rounds).

## History

See the code reading. The mitigation has not changed since the 2.5-second sleep was added.

## Reproduction attempts

1. `:solr:core:test --rerun --tests org.apache.solr.cloud.ChaosMonkeyNothingIsSafeTest -Ptests.nightly=true` at . Result: pass, 1 test, 0 failures. Seed `1F1EBA800A9BB08D`. Log `/workspace/gates/logs/vm5/skip-5516r2/chaos-nightly.log`. Conclusive: no. One passing run does not reproduce an intermittent failure. The nightly flag was needed; without it the class is skipped.

## Gap analysis

The mitigation is still in place, and the ticket's failure is intermittent on slow machines. One run cannot confirm or rule that out.

## Verdict justification

keep-open: the attempt is listed above, and the missing piece is repeated runs with new seeds under the nightly flag. A close needs two matching runs or a fix commit, and neither exists.
