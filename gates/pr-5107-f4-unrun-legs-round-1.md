# Gate: PR 5107 at F4, legs that cannot run on the phone agent host, round 1

Status: OPEN.

## Why this gate exists

The round 3 gate at F2 `7e531b083182366f8b6e7f00a3781ce3cbb713de`
closed PARTIAL by owner direction: the BATS packaging legs were
skipped, and the receipt named runtime ZK TLS and the end-to-end
credential shell flow as unverified. Since then the branch gained F3
`e75405eff845eda0e0f4f252d447347e2206acbe` (removal of the two scoped
changelog fragments) and F4
`7aa3f77bed263728a13f361d9f371bf16937672c` (the RunExampleTool
credentials-option declaration; report
`reports/pr-5107-credentials-option-fix-round-1.md`).

The phone agent host cannot run the remaining legs, for two recorded
reasons: it executes as root, and `bin/solr` refuses to start as root
without `--force`, which product tests must not use; and its sandbox
intercepts Java localhost connects, so `RunExampleTool`'s port probe
reports every port as already in use and no example start can
complete there (evidence:
`reports/pr-5107-example-rerun-local-diagnostic-round-1.md`). This
gate covers exactly those legs at F4, on a host where they can run.
Nothing in this gate authorizes a public post or comment.

## Pinned inputs

- Fork branch: `nick-boss-tech/solr:solr-8442-security-review-r2-submit`.
- Required head: F4 `7aa3f77bed263728a13f361d9f371bf16937672c`,
  verified by ls-remote before starting.
- Ancestry: contributor head A
  `7d193dac0f4abc441488f0ac2a9a734983d4fcd2`, H2
  `d264f756644ac2fb8b56c09c42a0c737198c148a`, F
  `4b04ba0c5f412403ac46db64364d90324b90b55d`, F2 and F3 are all
  ancestors of F4.
- Seed `8442C0DE` throughout. Logs, JUnit XML and TAP under the
  host's gate log directory for this gate, named in the receipt.

## Host requirements

- Linux, non-root execution account: record `id -u` (nonzero) in the
  receipt. Do not add `--force` to any product test or start, and do
  not run BATS as root and call the refusal a result.
- No localhost intercept: before the BATS legs, record a preflight
  showing a socket connect to an unused localhost port is refused on
  this host. If the connect succeeds, this host has the phone agent
  host's defect; stop and hand the gate back rather than recording
  port-probe failures as branch behavior.
- JDK 21 and the branch's Gradle tooling, OpenSSL, keytool, curl, and
  the seeded BATS dependencies per the existing gate records. One
  build at a time under the host lock. Windows does not execute this
  job.

## Legs

1. Exact-head basics at F4, in a normal clone (not a worktree; the
   root check's Antora leg fails in a worktree by layout): tidy
   clean; `:solr:core:compileJava -Pvalidation.errorprone=true`;
   root `check -x test`. If `:solr:webapp:verifyUiWasm` fails with a
   checksum mismatch, apply the recorded diagnosis before calling it
   a branch failure: Gradle downloads through some relays arrive
   corrupted while the pinned nightly artifact is byte-correct by
   direct download (see the round 3 partial receipt addendum);
   seeding the verified file into `solr/webapp/build/ui-download/`
   and rerunning is the recorded repair, and the receipt must
   disclose which path was taken.
2. Java suites at F4, both variants from the round 3 gate
   (`gates/pr-5107-security-integration-round-3.md`): the default
   security-manager run (SecureExampleCredentialsTest 5,
   SecureExampleZkTest 3, CLIUtilsTest, AuthToolTest,
   AuthToolPicocliTest, SSLToolTest, IncludeFileEditorTest) and the
   no-security-manager run (SSLToolTest 9 with no skips, the editor
   suite, and both secure-setup methods in each of
   TestSolrCLIRunExample and SolrCLIRunExamplePicocliTest), plus the
   F4 parse test `testCredentialsOptionIsAccepted`. These were green
   at F2 and at F4 in focused form; this leg is the exact-head
   record.
3. Packaging BATS, both parser modes, serialized, as the non-root
   account, with logs/TAP archived after each mode:

   ```bash
   env SOLR_PICOCLI=false ./gradlew :solr:packaging:integrationTests --tests test_ssl.bats --tests test_secure_example.bats
   env SOLR_PICOCLI=true ./gradlew :solr:packaging:integrationTests --tests test_ssl.bats --tests test_secure_example.bats
   ```

   Require 8 SSL and 3 secure-example cases per mode, all executed,
   no skips. The case-level expectations are the round 3 gate's:
   invalid administrator in both spellings reaches the Java policy
   error before any server state exists; missing option values and
   credentials on a plain start fail before anything starts; the
   valid-credentials start verifies actual ZK TLS with hostname
   checking, rejects an unrelated CA, succeeds with the correct CA,
   rotates the admin password through the API, restarts with the
   short form, rejects the old password, and restarts with saved
   credentials; the mTLS renewal case shows a real TLS rejection,
   not an HTTP status.
4. Matched pairs, in separate disposable checkouts at F4, tests
   unchanged, distributions rebuilt so no stale binary is tested:
   - Original production: restore only `SSLTool.java` and
     `SecureExampleCredentials.java` from A; the three Java tests
     fail with their intended signatures (per the round 3 gate), and
     one `SOLR_PICOCLI=false` run of the two BATS files fails
     exactly the mTLS renewal and early admin-policy cases.
   - Shell: restore only `solr/bin/solr` from A; rebuild;
     `test_secure_example.bats` with `SOLR_PICOCLI=false` fails all
     three cases at the unsupported credentials option, with no
     servers created.
   - The F4 Java pair (parse test failing without the declaration)
     is already recorded in
     `reports/pr-5107-credentials-option-fix-round-1.md`; cite it,
     do not rerun it.
5. Failure policy for the known upstream exception: if the secure
   example fails with `ERROR: No value present` after the servers
   start, preserve the complete stack, the first failing command,
   both node logs, TAP/XML, and process/port state, then stop and
   hand back the diagnosed failure. Do not relabel it environmental,
   do not rerun silently, and do not work around it in production
   code under this gate.

## Relationship to the rerun diagnostic assignment

`assignments/pr-5107-example-rerun-diagnostic-round-1.md` stays open
for Eric's rerun sequences (techproducts, films, techproducts
again). Its secure-setup sequence was pinned to F3; read that
sequence at F4 instead, since F4 is what makes the credentials start
parse at all. A host that claims this gate may also claim that
assignment and run the sequences on the same dev distribution.

## Claim and receipt

Claim before starting, one claim per host, at
`claims/pr-5107-f4-unrun-legs-round-1-<host>.md`, naming the host,
the account uid, and the start time. Receipt at
`receipts/PR-5107-f4-unrun-legs-round-1.md` naming every leg with its
exact counts, skips, failures, parser modes, the non-root account,
the localhost preflight result, and fresh absolute log/XML/TAP
paths. Green requires every leg above, including both matched
pairs; anything less is PARTIAL with the missing legs named, never
GREEN by omission. A tidy or generated-doc change during the gate
follows the round 3 protocol: ordinary repair commit, plain push,
and a superseding gate pinned to the new head before positive proof
counts. No code changes beyond that protocol, no public posts.
