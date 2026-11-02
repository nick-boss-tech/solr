# Report: PR 5107 round 3 partial gate at F2 (phone agent)

Status: DONE, PARTIAL by owner direction. Full record:
`receipts/PR-5107-security-integration-round-3-partial.md`.

- This host took over vm6's handed claim at Nick's direction and ran
  the remaining legs at F2 `7e531b083182366f8b6e7f00a3781ce3cbb713de`.
- Passed: generateCliDocs with a clean tree (the F2 start-page sync
  holds), compileTestJava and validateLogCalls, the default
  security-manager run (credentials 5 of 5, SecureExampleZkTest 3 of
  3, CLIUtilsTest 10 of 10), the no-security-manager run (SSLToolTest
  9 of 9 with no skips, editor 7 of 7, both secure-example classes 2
  of 2), the three-test original-production pair (all three fail at
  A-restored production with the intended signatures), and final tidy.
- Root `check -x test` failed at `:solr:webapp:verifyUiWasm` on a
  checksum mismatch for the prebuilt UI bundle; the same task at the
  contributor head A fails with the same expected checksum and a
  different downloaded hash, so the failure pre-exists at A, is
  outside this branch's delta, and the served artifact is not
  byte-stable between downloads on this host.
- Skipped by owner direction and named in the receipt: both BATS
  parser modes, the BATS original-production pair, the shell pair,
  and the non-root BATS account requirement. Runtime ZK TLS and the
  end-to-end credential shell flow remain unverified at F2.
- The receipt ends with a PR follow-up draft for Nick's review only.
  Nothing was posted.
