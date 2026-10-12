# Security convergence, round 2

Verdict: **NOT CONVERGED, and both drafts stay on HOLD.** SOLR-11678 and SOLR-12161 each name a packaged head that is not on origin. SOLR-11678 also has branch-side items open, and the SOLR-10627 changelog title has not landed.

Counts: drafts re-checked 2 (SOLR-11678, SOLR-12161), plus the SOLR-10627 fragment and the SOLR-18368 item 4 check. Dispositions checked 10: 3 applied and present, 4 held and still held, and 3 not applied or owed and still open. Disputed 0. Items still open 7: SEC-R1-02, 03, 04, 05, 07, 08 and 09. New findings: none blocking or should-fix; two notes.

Scope: `assignments/pool-security-convergence-round-2.md`, checked against `reports/security-convergence-round-1.md`. One read-only subagent did the review, working from the drafts, the receipts and the branch code only. Heads were checked with `git ls-remote origin refs/heads/<branch>`. Nothing was edited, posted or built.

## Verdict per draft

- **SOLR-11678: NOT CONVERGED (HOLD).** The draft names packaged head `611eae2464536fdae61b8cae32abac97ee3a453b` throughout. The live tip of `solr-11678-submit` is `55d8cd189d15367b6896f0ea07570d3395ce404b`, which matches the receipt. The packaged head is not on any origin ref and is not in this checkout, so the draft stays on HOLD.
- **SOLR-12161: NOT CONVERGED (HOLD).** The draft names packaged head `cdd265168a2adc76fb788f3d1f5587a3eee05e42`. The live tip of `solr-12161-submit` is `1725cbd8489889a1dad75279e4b2c11b0ec93d66`, which matches the receipt. The packaged head is not on any origin ref and is not in this checkout, so the draft stays on HOLD.

## Item states

**Item 1, SOLR-11678 (branch at `55d8cd`)**

- **SEC-R1-03 (HOLD): OPEN.** The draft still names `611eae` in every link, the changelog link and the Proof parenthetical.
- **SEC-R1-05 (branch side): OPEN.** `EnvToSyspropMappings.properties` at the tip has no `SOLR_SSL_KEY_MANAGER_PASSWORD` line. The file is absent from the 13-file branch diff, and no test assertion was added.
- **SEC-R1-04 (held): OPEN.** Line 15 still reads "sets the Java property `solr.jetty.keymanager.password`". The tip code contradicts that. The environment provider reads the variable, the property is read only by the system property provider, and nothing copies the variable into the property. The recorded replacement sentence matches the tip code, and it stays accurate after SEC-R1-05 lands.
- **SEC-R1-06: CONFIRMED.** The draft cites lines 14 to 21 at the merge-base, and line 21 there is the truststore password set.
- **SEC-R1-07: OPEN.** The Limits have no JKS line. The receipt does not name the keystore type, and the VM2 gate log is not on this host, so this is not checked here.
- **SEC-R1-08: OPEN (note).** The focused-test line has no gate date, and the Limits have no custom-class-provider line.
- Other citations match the tip: `bin/solr` 229 to 231, `solr.in.sh` 164 to 165, `solr.in.cmd` 149 to 150, `SSLConfigurations` 37 and 92 to 98, `jetty-ssl.xml` 17 to 21, and `SSLConfigurationsTest` 184 to 199. The tip stands in as a proxy, because the packaged commit only removes a root file, according to the receipt.

**Item 2, SOLR-12161 (branch at `1725cbd`)**

- **SEC-R1-09 (HOLD): OPEN.** The draft still names `cdd265` in the link and the Proof.
- **SEC-R1-10: CONFIRMED.** Line 7 matches the replacement word for word, and it agrees with the Proof: the base code refuses the update.
- The branch diff is test-only: `BasicAuthIntegrationTest.java` plus a root testing file. The new block is at lines 248 to 260, inside `testBasicAuth`, as the draft says.

**Item 3, SOLR-10627 fragment (branch at `5a15dc0`, matching the receipt)**

- `changelog/unreleased/SOLR-10627-reject-null-collection-permission.yml` still carries the round 1 title: set-permission only, "such as read and update", and "previously protected nothing". The response's replacement title is not present. **SEC-R1-02: OPEN.** A title commit moves the head off the gated `5a15dc0`, so the receipt's changelog parse should be re-run at the new head.

**Item 4, SOLR-18368: SEC-R1-11 CONFIRMED.** The Limits section opens with "**Three gaps are named below. This change does not close any of them.**"

## Register

No new blocking or should-fix findings. The two notes are below.

**SEC-R2-01 | branch file, not a draft | note.** The fragment title at `55d8cd` reads "...so HTTPS works when the key password differs from the keystore password." The draft's own Limits say the change was not run in a live server, and the receipt says the `jetty-ssl.xml` path was not exercised. This is the same class as SEC-R1-02. Optional replacement: "Add SOLR_SSL_KEY_MANAGER_PASSWORD (solr.jetty.keymanager.password) for keystores whose key password differs from the keystore password."

**SEC-R2-02 | register traceability, not a draft | note.** SEC-R1-05 and the response cite "owner decision 2" for the mapping line. In `reports/security-round-1.md` the mapping approval is owner decision 4, and decision 2 there concerns SOLR-18368. Decisions 6 and 8, cited in round 1 for SOLR-10627 and SOLR-11678, are not in that file's list of 1 to 5. The citations should be corrected to the record actually used.

## Not checked

- The packaged heads `611eae2464536fdae61b8cae32abac97ee3a453b` and `cdd265168a2adc76fb788f3d1f5587a3eee05e42`, whose objects are absent.
- The VM2 gate logs, which give the keystore type for SEC-R1-07 and the gate date for SEC-R1-08.
- Fork objects that are not on any ref, since fetching is outside the read-only rule.
- The master receipts ledger, the takeover log and any private security file.

## Convergence line

NOT CONVERGED. Both drafts stay on HOLD until their packaged heads are pushed and re-gated. SOLR-11678 also waits on the branch mapping line (SEC-R1-05) and on the citation and fragment edits.
