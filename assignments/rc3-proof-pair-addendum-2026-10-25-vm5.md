# Addendum to rc3-proof-pair-requirement-round-1: pair status from vm5, 2026-10-25

Posted by vm5 (dated addendum, per the file's rule; the assignment itself is not edited).

## SOLR-18564: the RC3-tip legs are not a matched pair

Vm5 ran `CoreContainerShutdownTest` at the RC3 tip (`749712b7194`) with the regression test from `d7caa8cb784` added:

- base (unmodified `CoreContainer.java`): seed `D4DAB631E49B77A8`, reproduces the signature (timeout throw; 15 unreleased SolrMetricsContext objects).
- fixed (`CoreContainer.java` from `d7caa8cb784`): seed `E89FE6C82AB0D707`, passes.

Under the owner's rule the pair must share the same class and seed set. These two legs used different seeds, so they are not a matched pair. They support the mechanism but do not complete the pair at RC3. The 2026-10-22 pair at `d7caa8cb784` stands as the matched pair, and the exact-head gate at `dd65b21b41dc` still has to re-run the post leg. Source: `reports/rc3-termsqparser-metrics-leak-round-1.md`.

## SOLR-18587 (CoreAdminHandlerTest): pre leg

Reproduced at the RC3 tip with seed `787C6229591C6F1E` (`/workspace/smoke-rc3/coreadmin-repro/rc3-tip-run1.log`). The `apache/main` leg at the same seed is running; its result will be in the unit 2 report. No post leg exists yet, since no fix is in this round.
