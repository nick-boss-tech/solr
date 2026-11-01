# SOLR-16725 premise report (Critical batch 2, unit 8)

Ticket: SOLR-16725, "CLUSTERSTATUS API output has inconsistent data
types for a few values against the newly restored collection".
Jira: Open, Critical, reporter Indumathy Rajagopalan, last updated
2023-06-06. The ticket's example: a CREATE collection reports
pullReplicas, replicationFactor, nrtReplicas, tlogReplicas as
strings ("3", "0"), while a RESTORE-created collection reports the
same fields as ints (3, 0); maxShardsPerNode likewise differs
("1" vs 3). The impact named in the thread is client-side parsing:
tools decoding the output with a fixed field type break after a
restore.

## Verdict: REPRODUCED on current main

Scratch probe only, never committed. Fresh worktree at batch base
83d560030fde (apache main, SOLR-18585). Two probe legs, both
LocalFSCloudIncrementalBackupTest.testCustomProperties (create,
backup, restore, then CLUSTERSTATUS for source and restored
collections), seed 16725C0FFEE16725, all Gradle through
~/workspace/tools/solr-gradle.sh under the test-queue flock.

Leg 1 (the existing branch's test verbatim): the submit branch's
test asserts every replica count is a String on both collections.
It fails on unmodified production, but at the SOURCE assertion,
not the restored one: "Source replica count for replicationFactor
must be reported as a string expected:<class java.lang.String>
but was:<class java.lang.Long>" (log
~/workspace/tools/16725-premise-probe.log, BUILD FAILED in 1m 45s).
That leg alone proves types are unstable, but not the ticket's
exact shape, so a second leg measured the types instead of
assuming them.

Leg 2 (same flow, standard SolrJ create helper, per-key type
print, fail only when source and restored classes differ):
fresh JUnit XML 2026-11-01T21:50:19Z, 1 test, 1 failure (copy
~/workspace/tools/16725-premise2-TEST.xml; log
~/workspace/tools/16725-premise-probe2.log):

- replicationFactor: source 1 (Long), restored 1 (Long): match
- nrtReplicas: source 1 (Long), restored 1 (Long): match
- tlogReplicas: source "0" (String), restored 0 (Long): DIFFER
- pullReplicas: source "0" (String), restored 0 (Long): DIFFER
- maxShardsPerNode: absent from both collections' output here

So the ticket's core defect reproduces on current main for
tlogReplicas and pullReplicas: the restored collection reports
them as numbers where the source reports strings. Two drift notes
against the 2023 ticket text: replicationFactor and nrtReplicas
are Long on BOTH sides on current main (not String on the source
as in 2023), and maxShardsPerNode did not appear in either
collection's CLUSTERSTATUS output in this flow, so its example
was not replayed.

## Prior work found during this unit (does not replace it)

Fork branch solr-16725-submit already exists at
550af283134e2f1cdbcbe9e395f9a242af072f6a (Nick Shanin, 5 commits
on base 14c7aac0d1): ClusterStatus.normalizeCollectionCountTypes
reports the four replica counts as strings for every collection,
plus the backup/restore test used in leg 1 and a changelog entry.
Round 35 gated that work at be1838ef8cc (log
~/workspace/tools/g16725r35-gate.log: tidy clean, Error Prone ok,
pre-fix proof failed as expected, focused tests green,
:solr:core:check -x test ok). But the branch has since gained two
commits (fc5c54770c javadoc reword, 550af28313 tidy output), the
current head has never been gated, and no board receipt, gate
file, or claim for 16725 exists on coordination-ui. The r35
premise failure is the same source-String assertion leg 1
reproduced, so this report's leg 2 per-key measurement is the
new evidence.

## Consequence for the queue

Per the queue rule a reproduced premise would normally turn the
next unit into that ticket's implementation, but an implementation
already exists and is pushed. The next unit is therefore NOT a
fresh implementation: it is a full hardening gate of
solr-16725-submit at its current head 550af28313 (tidy, Error
Prone, pre-fix proof, focused tests, :solr:core:check -x test,
kept logs and fresh JUnit XML), followed by a board receipt at
receipts/SOLR-16725.md. Open question for that gate unit: the
branch normalizes only the four replica counts, not
maxShardsPerNode, which the ticket also names; whether that gap
matters is a scope call, not a premise question. No PR, no
Jira/GitHub post from this unit.
