# Security convergence, round 1

Verdict: **NOT CONVERGED.** Four of the five drafts hold open items. SOLR-13705 converges with no finding.

Counts: drafts checked 5. Drafts with no finding 1 (SOLR-13705). Drafts with blocking findings 2 (SOLR-11678, 12161), both on a HOLD for a head that is not yet pushed. Drafts with should-fix findings only 2 (SOLR-10627, 18368). Findings 12: 4 blocking (SEC-R1-03, 04, 05, 09), 5 should-fix (06, 07, 10, 01, 11), 3 note (02, 08, 12). All are new on this pass.

Scope: `assignments/pool-security-convergence-round-1.md`. The decision record is the security entries of `reports/security-round-1.md` and its parts, for the five tickets in scope only. The private security material is not named, copied or summarized here. Heads were checked with `git ls-remote origin refs/heads/<branch>`. SOLR-11678 and SOLR-12161 name packaged heads that are not on origin; those drafts are on HOLD until the packaged commits are pushed and re-gated. One read-only subagent did the review. Nothing was edited, posted or built.

## Verdict per draft

- **SOLR-10627: NOT CONVERGED.** SEC-R1-01 (should-fix); SEC-R1-02 (note, owner decisions 1 and 6 open).
- **SOLR-11678: NOT CONVERGED (HOLD).** SEC-R1-03 (blocking, HOLD: every head link names an unpushed packaged commit); SEC-R1-04 and SEC-R1-05 (blocking); SEC-R1-06, 07 (should-fix); SEC-R1-08 (note).
- **SOLR-12161: NOT CONVERGED (HOLD).** SEC-R1-09 (blocking, HOLD on the unpushed packaged head); SEC-R1-10 (should-fix).
- **SOLR-13705: CONVERGED.** No finding.
- **SOLR-18368: NOT CONVERGED.** SEC-R1-11 (should-fix); SEC-R1-12 (note).

## Register: blocking and should-fix rows

**SEC-R1-03 | SOLR-11678 | blocking (HOLD).** Every head link and the Proof name `611eae2464536fdae61b8cae32abac97ee3a453b`, which is local to VM2 and not pushed. The live tip is `55d8cd189d15367b6896f0ea07570d3395ce404b`. Once the packaging commit is pushed, substitute the pushed head everywhere, and make the Proof line read "Focused tests at head `<pushed head>`" without the parenthetical. Hold until then.

**SEC-R1-04 | SOLR-11678 | blocking.** "A new environment variable, `SOLR_SSL_KEY_MANAGER_PASSWORD`, sets the Java property `solr.jetty.keymanager.password`." The environment provider reads the variable, and the system-property provider reads the property. The branch sets that property from nothing. Replacement: "A new environment variable, `SOLR_SSL_KEY_MANAGER_PASSWORD`, is read by the environment credential provider. The Java property `solr.jetty.keymanager.password` is read by the system property provider. `SSLConfigurations` reads both through the existing credential providers."

**SEC-R1-05 | SOLR-11678 | blocking (branch side; absent from the draft).** `EnvToSyspropMappings.properties` maps every SSL password variable to nothing, and `SOLR_SSL_KEY_MANAGER_PASSWORD` is absent. The owner decision 2 requires the mapping. Branch action: add `SOLR_SSL_KEY_MANAGER_PASSWORD=` with a test assertion, then add to What this change does: "The variable maps to nothing in `EnvToSyspropMappings.properties`, as the other SSL password variables do, so it is not copied to a system property." Hold the draft until this lands.

**SEC-R1-06 | SOLR-11678 | should-fix.** The link "(lines 14 to 19 at the merge-base)" does not show the truststore password line, which is at line 21. Replacement: "([lines 14 to 21 at the merge-base](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/server/etc/jetty-ssl.xml#L14-L21))".

**SEC-R1-07 | SOLR-11678 | should-fix.** Limits do not say the run used JKS. Add: "- **The standalone run used a JKS keystore.** A PKCS12 keystore cannot carry a key password that differs from its store password, so the run does not cover PKCS12." Confirm the keystore type from the run log before posting, because the receipt does not name it.

**SEC-R1-09 | SOLR-12161 | blocking (HOLD).** The link "lines 248 to 260 at the head" and the Proof "at head `cdd265168a2a`" name an object that is absent locally. The live tip is `1725cbd8489889a1dad75279e4b2c11b0ec93d66`, and it carries a root testing file. Once the packaging commit is pushed, substitute the pushed head for the full and short SHA. Hold until then.

**SEC-R1-10 | SOLR-12161 | should-fix.** "**The ticket reports that an update without credentials can get through a `CloudSolrClient` under basic auth.**" reads as current behavior, but the base code refuses that batch in the test. Replacement: "**The ticket reports that an update without credentials can get through a `CloudSolrClient` under basic auth. The base code refuses that update in the test below.**"

**SEC-R1-01 | SOLR-10627 | should-fix.** "## Limits" opens straight into a list. Insert after the heading: "**Five things are not covered or not tested, and each is named below.**"

**SEC-R1-11 | SOLR-18368 | should-fix.** "## Limits" opens with bullets and has no bold summary. Insert after the heading: "**Three gaps are named below. This change does not close any of them.**"

## Notes

- **SEC-R1-02 (SOLR-10627):** the changelog title says "previously protected nothing" and names set-permission only. The draft text is accurate; owner decisions 1 and 6 are open.
- **SEC-R1-08 (SOLR-11678):** the focused-test line has no date, and the receipt gives none. Add the gate date from the log at the pushed head. Owner decision 8 (custom class providers now receive the new constant) is not in Limits.
- **SEC-R1-12 (SOLR-18368):** the opener line 9 matches the still-an-issue report. Whether the settled fidelity text was meant to change line 9 is for the main side to confirm.

## Not checked

- The gate and VM2 logs, which are not on disk, and the objects at `611eae` and `cdd265`, which are absent.
- Jetty and JDK behavior, which were not run.
- The master receipts ledger, the takeover log and any private security file.

## Convergence line

NOT CONVERGED. Four blocking and five should-fix findings are open. Three of the blocking findings wait on pushed packaged commits or a branch commit.
