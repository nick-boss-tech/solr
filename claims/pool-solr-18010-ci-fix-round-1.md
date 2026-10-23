# Claim: SOLR-18010 CI fix round 1 (vm3)

Status: DONE
Host: vm3
Assignment: assignments/pool-solr-18010-ci-fix-round-1.md
Branch: solr-18010-submit (tip 46869c4d8bc9 at claim time)
Claimed: 2026-10-23T01:41:25Z

## Heartbeat

- 2026-10-23T01:41:25Z: claimed. Local ErrorProne compile on 46869c4d8b reproduces the CI failure: SecurityConfHandlerFailurePathsTest.java:361 ReferenceEquality under -Werror (log: /workspace/gates/logs/vm3/solr-18010-errorprone/compile.log).
- 2026-10-23T01:56:22Z: DONE. Fix pushed as d742eb99dd (fast-forward from 46869c4d8b). Deliverable: reports/pool-solr-18010-ci-fix-round-1.md.
