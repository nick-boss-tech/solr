# Security convergence, round 1: main-side response

Round 1 returned NOT CONVERGED: four of the five drafts held open items, with 4 blocking, 5 should-fix and 3 note findings. This lane verified every blocking and should-fix finding against the receipts and the branch code before applying anything, and applied each holding finding's exact replacement text to its draft. Three blocking findings and one should-fix on SOLR-11678, and the blocking finding on SOLR-12161, are held on the packaged heads that are local to vm2 and not yet pushed; nothing that depends on those heads was applied. One should-fix (SEC-R1-07) is not applied because its precondition cannot be met from this host. Application method: exact-string replacements, each asserted to occur exactly once before applying; the full diff was then reviewed file by file.

## Counts

| Grade | Items | Applied in this lane | Held on the packaged-head push | Branch-side, recorded below | Not applied |
|---|---|---|---|---|---|
| Blocking | 4 | 0 | 3 (SEC-R1-03, SEC-R1-04 on SOLR-11678; SEC-R1-09 on SOLR-12161) | 1 (SEC-R1-05, rides the same hold) | 0 |
| Should-fix | 5 | 4 (SEC-R1-01, 06, 10, 11) | 0 | 0 | 1 (SEC-R1-07) |
| Note | 3 | 0 | 0 | 1 (SEC-R1-02) | 2 standing or confirmed (SEC-R1-08, 12) |

After application, three of the five drafts are CONVERGED on their text: SOLR-10627 (a branch-side changelog title is owed at packaging), SOLR-13705 (no finding in round 1) and SOLR-18368. SOLR-11678 and SOLR-12161 are NOT CONVERGED; both stand on the packaged-head hold.

## Fact checks before applying

- **SOLR-11678 heads:** the packaged head `611eae2464536fdae61b8cae32abac97ee3a453b` named throughout the draft is local to vm2. `git ls-remote` shows the live tip of `solr-11678-submit` at `55d8cd189d15367b6896f0ea07570d3395ce404b`. SEC-R1-03 holds.
- **SOLR-11678 (SEC-R1-04, 05):** at the live tip, `EnvToSyspropMappings.properties` (solr/solrj/src/resources/) lists every `SOLR_SSL_*` variable with an empty right-hand side, and `SOLR_SSL_KEY_MANAGER_PASSWORD` is absent entirely. The branch diff against the merge-base `cabedd1d968` does not touch that file. The only keymanager hits on the branch are the `SSLConfigurations` constant and the `AbstractSSLCredentialProvider` pairing, so nothing sets the Java property from the variable. Both findings hold on the facts; their text lands with the branch commit and the pushed head, because SEC-R1-05's branch commit changes the facts SEC-R1-04's sentence describes.
- **SOLR-11678 (SEC-R1-06):** `jetty-ssl.xml` at the merge-base `cabedd1d968` carries the TrustStorePassword Set at file line 21, outside the lines 14 to 19 the draft cited. The citation now reads lines 14 to 21.
- **SOLR-12161 heads:** the packaged head `cdd265168a2a` named in the draft is not pushed; the live tip of `solr-12161-submit` is `1725cbd8489889a1dad75279e4b2c11b0ec93d66`. SEC-R1-09 holds.
- **SOLR-10627 (SEC-R1-01):** the draft's Limits section has exactly five bullets, so the inserted summary line counts them correctly.
- **SOLR-18368 (SEC-R1-11):** the draft's Limits section has exactly three bullets, so the inserted summary line counts them correctly.

## Per-finding dispositions

### SOLR-10627
- SEC-R1-01 (should-fix): applied. The Limits section now opens with "**Five things are not covered or not tested, and each is named below.**"
- SEC-R1-02 (note): branch-side. The changelog fragment title at head `5a15dc0ba22083a294524210b3be007d870daa35` overstates the change ("previously protected nothing" for a set-permission-only fix). The draft carries no title text, so there is nothing to edit in the draft. Replacement recorded verbatim for the packaging lane: "The authorization set-permission and update-permission commands now reject \"collection\": null on the per-collection permissions read, update, schema-read and schema-edit, because a null collection never applies to a request routed to a collection." Adopted under the title-accuracy rule; owed before opening.
- Owner decisions 1 and 6 from reports/security-round-1-s1.md (title and length) are settled by this lane. The title is the branch-side replacement above. The length call: ACCEPT, no trim. The draft is 7,708 characters with links and 5,198 without link targets; it carries a real Choice (the ticket contradicts itself), a behavior change reaching legacy entries, and five Limits. That is the formula's unusually-complex case, and the length is earned.

### SOLR-11678
- SEC-R1-03 (blocking): HELD on the packaged-head push. Once `611eae246453` (or its pushed successor) is on the fork, substitute the pushed head everywhere the draft names it, in the form the register prescribes.
- SEC-R1-04 (blocking): HELD with SEC-R1-03. The facts were verified at the live tip (above), and the register's replacement sentence is recorded here for the same pass: "A new environment variable, `SOLR_SSL_KEY_MANAGER_PASSWORD`, is read by the environment credential provider. The Java property `solr.jetty.keymanager.password` is read by the system property provider. `SSLConfigurations` reads both through the existing credential providers." It is not applied now because SEC-R1-05's branch commit changes the underlying facts first.
- SEC-R1-05 (blocking): branch-side, HELD with the same pass. Branch action per the register: add the `SOLR_SSL_KEY_MANAGER_PASSWORD` mapping line to `EnvToSyspropMappings.properties` plus a test assertion, then the draft sentence that reports it. Owner decision 2 requires the mapping.
- SEC-R1-06 (should-fix): applied. The merge-base citation now reads lines 14 to 21.
- SEC-R1-07 (should-fix): NOT APPLIED. Its precondition is confirming the standalone run's keystore type from the run log. receipts/SOLR-11678.md does not name the keystore type, and the vm2 gate logs are not reachable from this host. The item stays open for round 2, to be settled from the vm2 log once the packaged head is pushed.
- SEC-R1-08 (note): stands, and rides the hold. The gate date comes from the vm2 log at the pushed head; owner decision 8's Limits line is a draft-completion item for the same pass.

### SOLR-12161
- SEC-R1-09 (blocking): HELD on the packaged-head push, same shape as SEC-R1-03. Once the packaged head is pushed, substitute it for the full and short SHA everywhere the draft names it.
- SEC-R1-10 (should-fix): applied. The opening now reads "**The ticket reports that an update without credentials can get through a `CloudSolrClient` under basic auth. The base code refuses that update in the test below.**", which matches the draft's own Proof (the base code refuses the update).

### SOLR-13705
- No finding. CONVERGED, unchanged.

### SOLR-18368
- SEC-R1-11 (should-fix): applied. The Limits section now opens with "**Three gaps are named below. This change does not close any of them.**"
- SEC-R1-12 (note): CONFIRMED by the main side, no text change. The draft's banner already carries the settled fidelity form ("Do not post until the owner decides under which key the PR opens"). The key choice is settled at opening approval; it is not a finding.

## Verdicts after application

- SOLR-10627: CONVERGED on text (branch-side changelog title owed at packaging).
- SOLR-11678: NOT CONVERGED (HOLD on the packaged-head push; SEC-R1-03, 04, 05 held, SEC-R1-07 open).
- SOLR-12161: NOT CONVERGED (HOLD on the packaged-head push; SEC-R1-09 held).
- SOLR-13705: CONVERGED.
- SOLR-18368: CONVERGED.

## What round 2 needs to settle

1. Whether the two packaged heads have been pushed; if so, re-check SOLR-11678 and SOLR-12161 against the pushed heads, including the SEC-R1-07 keystore-type precondition from the vm2 gate log and the SEC-R1-08 gate date.
2. Whether SEC-R1-05's branch commit (the properties mapping plus its test assertion) has landed; if so, check SEC-R1-04's recorded replacement against the new branch state.
3. Whether the SOLR-10627 changelog fragment title has been replaced on the branch with the text recorded above.
4. Confirmation that the four applied should-fix replacements are present in the live drafts, word for word in substance.
