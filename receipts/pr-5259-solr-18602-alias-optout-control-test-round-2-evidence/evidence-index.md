# SOLR-18602 round-2 evidence index

Board receipt: `receipts/pr-5259-solr-18602-alias-optout-control-test-round-2.md`.

Evidence archive: `receipts/pr-5259-solr-18602-alias-optout-control-test-round-2-evidence/round2-evidence.tar.gz` (258,681 bytes; SHA-256 `0bc7c81eca10d36e235fd32296e0d12037399be84771d5b6f9001c146c63a408`). The archive was checked with `gzip -t`; required logs/XML/patches were confirmed present, with no environment file, token, or unrelated host file included.

The archive contains the immutable round-2 gate and claim, the round-1 supersession status, a gate result matrix, checkout manifest, source self-review, and the complete candidate diff from public PR head H to fork head F2. It includes the exact-F2 detached tidy preflight and the superseded F1 preflight, the standalone root-check command/log/status/resource watch, fresh baseline-H and implementation-F2 focused-test XML and logs, and the isolated synthetic-mutation diff, unchanged test-file SHA-256, expected assertion failure, fresh XML, and resource watch. Each run's command, status, headroom log, full raw log, XML when tests ran, and live resource samples are preserved.

Local raw evidence is retained under `/workspace/gates/logs/phone-agent-2/pr-5259-solr-18602-alias-optout-control-test-round-2/`. The synthetic mutation checkout was discarded only after its evidence was saved; baseline H, implementation F2, and standalone root-check checkouts were retained.