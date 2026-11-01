# Claim: SOLR-8028 implementation (phone agent)

Host: phone agent (this VM). Unit: SOLR-8028 implementation, from
the Critical batch 2 queue (premise REPRODUCED 2026-11-01, report
reports/critical-impl-8028-premise-batch-2.md).

Scope: settle the fix shape first (the premise report's caution:
snapshot copy in writeArray vs copying collections in setValue vs
a documented thread-safety contract), then, if a narrow fix is
right, branch solr-8028-submit, proof pair, full hardening gate,
push, no PR. If no narrow fix is right, report the verdict with
reasons instead of a branch.

Status: CLAIMED 2026-11-01 ~14:33 MST.
