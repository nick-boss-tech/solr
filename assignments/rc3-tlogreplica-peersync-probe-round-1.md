# Assignment: TestTlogReplica peersync probe (round 1)

Status: OPEN
Tags: premise-run

Follows `reports/rc3-tlogreplica-rootcause-round-1.md` (vm5), which reproduced the failures at the RC3 tip and at apache/main and left a named, unconfirmed hypothesis: a rejoining TLOG replica stalls in `recovering` because TLOG recovery skips peersync (`RecoveryStrategy.java` line 571, `firstTime = replicaType != Replica.Type.TLOG`). This unit runs the probe that report proposed. It changes no shared branch and files nothing.

## Work

1. On a scratch tree at apache/main `13c693d805f`, make the smallest change that lets a TLOG replica take the peersync path at the cited line. Keep the scratch change on the host or on a `scratch-*` fork branch; it is scaffolding, never a fix candidate in this form.
2. Run `TestTlogReplica.testKillLeader` and `testRecovery` focused, seed `9D6BB87C4F145405` (the seed from the main reproduction run), on the scratch tree; then the same two methods and seed on the unmodified main tree as the control, if the control does not already exist from round 1 at this seed.
3. Report both outcomes. A scratch pass against a control failure supports the hypothesis and names the next step (a real fix design on SOLR-18586); a scratch failure rules the hypothesis out and says what the captured state showed instead.

## Deliverables

- `reports/rc3-tlogreplica-peersync-probe-round-1.md` with the scratch diff, both runs' results counted from the JUnit XML, and the verdict on the hypothesis.
