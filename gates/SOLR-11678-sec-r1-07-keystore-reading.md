# Reading job: SOLR-11678 SEC-R1-07, keystore type of the standalone premise run

- Question: did the standalone Jetty premise run (receipts/SOLR-11678.md, "Premise", vm2, 2026-10-11) use a JKS keystore? The receipt does not name the type. The SEC-R1-07 Limits sentence in reports/security-convergence-round-1.md says JKS, and it can be added only after this reading confirms it.
- Host: vm2 only. The premise files and the vm2 logs are not on the Windows review host. This is a read. No gate run, no build, no new keystore, and no change to the keystore file.
- Read, in this order:
  1. /workspace/gates/solr-11678-premise/Premise.java: any `KeyStore.getInstance(...)` call, or any keystore type set on the SslContextFactory, and its line.
  2. The keystore file used by the run, `ks.jks` (gates/SOLR-11678.md, "Premise outcome"): its type from a read-only `keytool -list -v` on that file, or from the type line the run log prints.
  3. The premise run log under /workspace/gates/logs/vm2/. The receipt does not name the file. Locate it by searching that folder for `UnrecoverableKeyException` or `Premise`, dated 2026-10-11.
- Record: the type, the file, and the line for each read. Put the note under "Premise" in receipts/SOLR-11678.md as a reading note. This is not a new gate.
- Outcomes:
  - JKS, confirmed by a read: add the SEC-R1-07 Limits sentence to pr-drafts/security/SOLR-11678.md (text in reports/security-convergence-round-1.md), and SEC-R1-07 reads CONVERGED.
  - PKCS12, or the type is not recorded in any read: do not add the sentence. A PKCS12 keystore cannot carry a key password that differs from its store password (keytool drops the different key password with a warning; reports/review-confidence-round-2.md, point 6). If the run used PKCS12, the premise as recorded does not hold. Return SOLR-11678 to the owner as a premise finding.
