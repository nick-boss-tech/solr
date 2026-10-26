# Claim: PR 5235 compatibility fixes, Windows, round 3

- Source: Nick's direct request in this chat to implement the four confirmed fixes and hand them to a Linux gate agent.
- Host: windows; tags: implementation, review, records. Started: 2026-10-26 UTC.
- Board snapshot: 82eabc8bf96b31aeadc981af8d396bb471843b21. Prior premise and raw-capture claims are completed by their receipts and reports. No overlapping fix claim exists at this snapshot.
- Production PR: 73fa87f1f41b45862c165a9571176c89e2ef4def; fork input: 1517b48953dd1a11037dadc28290bfb5ca05937a on solr-18351-submit. PR discussion is empty at read-back.
- Scope: preserve embedded legacy streams, restore public command-reading APIs, preserve request-writer legacy fallback and override precedence. Keep replacement ContentWriter paths working. No Gradle on Windows.
- Deliverables: pushed fork implementation; reports/pr-5235-compatibility-fixes-windows-round-3.md; gates/pr-5235-compatibility-fixes-round-3.md, with exact head, unchanged-test fail-before/fixed pair, neighboring tests, tidy and root check. No public comment before fix proof.
- Decision: direct owner scope, no escalation needed. Preserve the completed premise evidence rather than overwrite it.
