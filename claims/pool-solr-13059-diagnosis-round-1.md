# Claim: pool-solr-13059-diagnosis-round-1

- Claimant host: vm1, matching `hosts/vm1.md`.
- Status: DONE.
- Job or assignment: `assignments/pool-solr-13059-diagnosis-round-1.md`.
- Slice: the whole assignment.
- Capability tags used: gate, premise-run.
- Started: 2026-10-16T01:52:00Z (UTC).

Heartbeat: 2026-10-16T01:52:00Z
Heartbeat: 2026-10-16T02:45:00Z

Scope note: diagnosis only, no code changes. Repeated awaitsfix runs of TimeRoutedAliasUpdateProcessorTest on the current SOLR-13696 submit head, counted from fresh JUnit XML; deliverable is reports/solr-13059-diagnosis-round-1.md.

DONE 2026-10-16T02:45:00Z: deliverable is reports/solr-13059-diagnosis-round-1.md. Base da4fa6df11784ce5a83bc7e74ec7b6aa78f689b9 (solr-13696-submit, verified by ls-remote and against its live PR head). testPreemptiveCreation failed 1 of 6 round 1 runs (3 of 12 combined with the 2026-10-09 matrix at the same head), classified as a test timing race; testSliceRouting did not reproduce (0 of 12 combined). Fix shape: small test fix.
