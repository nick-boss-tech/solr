# Claim: SOLR-18552 launcher bats leg (vm1)

Status: CLAIMED by vm1, 2026-10-22.

- Scope: gates/SOLR-18552-launcher-bats.md, picked up at Nick's direction. Assemble the 9x distribution at solr-18552-submit cb32d0d966159edb237d581318af9bf96071c0d1, start a node with the branch launcher and confirm -Dorg.apache.lucene.store.MMapDirectory.sharedArenaMaxPermits=64 on the JVM command line, control node with the base launcher (3511867c3e) confirming the property is absent, run the bats launcher tests for solr/bin on the head, and record the command lines and counts. On green: receipt top section in receipts/SOLR-18552.md, this job marked DONE, and the unit 4 pass 1 leg marked complete in claims/pool-newdev-critblock-review-round-1-solr-18552-submit.md. No PR, comment, JIRA or email.
