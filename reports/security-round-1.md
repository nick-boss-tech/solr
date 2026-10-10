# Security and authentication round 1: round roll-up

Claim: `claims/security-round-1.md` (commit `b547acf5436`). Assignment: `assignments/security-round-1.md` (commit `eb1c0ef578a`). Part reports: `reports/security-round-1-s1.md` through `-s4.md`. Drafts: `pr-drafts/security/` (two files).

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Four subagents in parallel, one per ticket, within the cap of six. No build, Gradle run, or test was run. No branch, live PR, JIRA item, or comment was touched. Nothing was posted. The receipts were not edited.

## Heads

- SOLR-10627 `5a15dc0ba220` and SOLR-18368 `a7ec9a1b65c0` match the assignment (`ls-remote`).
- SOLR-11678 `55d8cd189d15` and SOLR-12161 `1725cbd84898` match their receipts.
- Upstream moved during the round: `upstream/main` is now `3f5d4c5bf8ac` (fetched by part S2 for SOLR-18368). The claims were checked against both the merge-base and the moved main, and the citations hold on both.

## Verdicts

| Ticket | Verdict | Draft | Owner decision |
|---|---|---|---|
| SOLR-10627 | Draftable. Rejects a null collection on four per-collection permission names. | `SOLR-10627.md` | The changelog title (a branch commit); the "ignore versus error" Choice; the update-permission case in the test |
| SOLR-18368 | Salvage draft, waiting on the owner's call about whether and how a PR opens. | `SOLR-18368.md` (bracketed waiting note at the top) | Option (a) recommended: open with no key, citing upstream `#4782` |
| SOLR-11678 | Audit only. Two blocking items for any PR. | none | Remove the TESTING file; add the environment mapping; authorize the premise run |
| SOLR-12161 | Audit only. Predicted to be a pin, not a defect; undetermined until run. | none | Which tree runs the premise; whether the pin is strengthened |

## SOLR-10627: draftable, with a branch edit owed

- Draft `pr-drafts/security/SOLR-10627.md`, 7,704 characters with links (5,198 without the link targets). Over the roughly 3,500 guide, because of the links and the scope statements.
- The rule is exact: edit-time validation of `set-permission` and `update-permission` (delegated) rejects a null collection on the four names whose default collection list excludes null (read, update, schema-read, schema-edit). The other names, custom path permissions, and the stored-file load path are unchanged. The draft says so.
- **The changelog title is not exact.** It names only `set-permission`, says "such as read and update" where the rule is exact for four names, and says "previously protected nothing", which overstates. A replacement is proposed in the part report for the branch owner to commit. The branch was not touched.
- **Standalone and cloud:** the validation runs in both modes, through the shared `SecurityConfHandler.doEdit` and the plugin's `edit`. A rejected command never reaches the persist step in either mode.
- **Seam for SOLR-13097:** the check is reached only through `doEdit`. A standalone change that writes `security.json` without calling the plugin's edit, or a direct file write or ZooKeeper upload, loads without validation in both modes.
- **Two extra gaps the draft states in Limits:** a list form such as `"collection": [null]` is not rejected (it becomes the string `"null"`), and an update-permission on an existing entry that already has a null collection is checked after the merge, so a role-only update is rejected until the update sets a collection. No test runs the update-permission path.
- Test counts: 11 test methods in the base test class (the receipt's 11). `MultiAuthPluginTest` (6) and `TestAuthorizationFramework` (1) were not verified.

## SOLR-18368: salvage, waiting on the owner's call

- Draft `pr-drafts/security/SOLR-18368.md`, 5,574 characters with links (3,380 of prose with URLs removed). A bracketed note at the top says not to post until the owner decides.
- The branch is one ref-guide page with three corrected lines. The pin test is gone at the head. Diff against the merge-base `14c7aac0d151`: one file, 3 insertions and 3 deletions.
- **All three claims are confirmed on main:** the example calls `withInternalClientBuilder`, which was removed by upstream commit `3ef3d093c068` (`#4782`, before the base); the example constructs `HttpJettySolrClient.Builder` without `new`; the sentence names `Http2SolrClient.Builder`, which no longer exists. The corrected example matches the main signatures (`HttpJettySolrClient.java` line 956; `HttpSolrClient.java` line 526; `CloudSolrClient.java` lines 1398, 1550 and 1582).
- **Verification statement:** a snippet compiled against the built client classes, and the page build finished, both at tree `1acaced80b2`. The draft says so, and says it is not a test run. By construction, the head differs from that tree only by the dropped pin test, and the page is byte-identical.
- **Limit:** the example also omits `zkHostList` and `chroot` declarations, which is a pre-existing gap outside the three lines. Whether to fix it is a scope call.
- **Receipt conflicts:** the gate log `g18368-gate.log` is not on disk, so the "3 of 3" count is not confirmed. The receipt says upstream commit `3ef3d093c06` is also on `branch_10x`; that branch has a different commit (`62886913954`) with the same content. The receipt says the retry hunk is superseded by SOLR-18341, which exists only on the fork. The Jira status conflicts: the receipt says Resolved and Fixed; the local snapshot says Open.
- **Owner options:** (a) open with no key, citing `#4782` and `3ef3d093c068` in the body, and delete the Jira line (recommended, because the removal is already on main and the remaining defect is only in the page); (b) a follow-up reference to SOLR-18368 (confirm the status in JIRA first); (c) do not open, and fold the three lines into the next docs pass. An optional one-line upgrade note is a second option, not in the branch.

## SOLR-11678: audit only, two blocking items

- **Blocking 1:** `SOLR-11678-TESTING.md` is at the repository root, inside the branch diff. It says "Guessed, never compiled or executed." It cannot ship.
- **Blocking 2:** `SOLR_SSL_KEY_MANAGER_PASSWORD` has no entry in `EnvToSyspropMappings.properties`. Every password environment variable on main maps to nothing, so the new variable would become the system property `solr.ssl.key.manager.password` holding the secret. The default redaction hides it from the node-properties API, but a custom hidden-properties list would expose it. The fix is one mapping line and a test assertion.
- **Scope:** the branch covers the server half only. The SolrJ client, the JDK default client keystore and the test framework have no key-manager path, and the branch adds none.
- **Wiring:** the Jetty XML, `SSLConfigurations`, the credential types, the environment provider and the shared map are wired consistently, and the names agree across `bin/solr`, `solr.in.sh`, `solr.in.cmd` and the code. Jetty 12.1.12 accepts a null for the new key and falls back to the keystore password, but a mistyped reference would also be a silent null (read, not run).
- **Side effect:** after the branch, an unset environment variable makes Jetty read the JVM property `org.eclipse.jetty.ssl.keypassword`, which is inert on main. Low risk, but "unset keeps today's behavior" needs that qualifier.
- **Premise run:** the premise is a JKS keystore whose key password differs from its store password. A PKCS12 keystore cannot carry that, so the premise is JKS-only, and the run must set `SOLR_SSL_KEY_STORE_TYPE=JKS` (and the trust store type). The part report gives the keystore, the seven runs and the outcomes. Nothing was run.
- **Overlap with the CLI round:** the branch's `bin/solr` change is one three-line insertion at head lines 229 to 231, after the keystore password export. It conflicts only with CLI branches that edit main lines 225 to 231.

## SOLR-12161: audit only, predicted to be a pin

- **Verdict:** undetermined until the premise run, but the reading predicts a pin. The scenario is unpinned on main. No code path on main attaches a PKI identity to the client the test uses: the PKI header listener is installed only on node-owned clients, never on the cluster client. The 401 comes from the authorization layer.
- **The adjusted expectation is correct.** The update goes down the parallel path, and the 401 surfaces as a `RouteException`, which extends `SolrException`. The original `RemoteSolrException` expectation would fail, because `RemoteSolrException` is final and the thrown object is a `RouteException`.
- **Premise run:** module `solr/core`, `BasicAuthIntegrationTest.testBasicAuth`, with fail-before. The queue's fail-before tree is the merge-base, and the head's test overlay is applied on it, so one run settles both trees. A green focused run gives NOT_PROVEN and never reaches SUCCESS, which is expected for a pin. A pass means the scenario is closed and the test documents it. A failure with the batch accepted would be a live defect, an unauthenticated update admitted under basic auth.
- **Gap in the pin:** the assertion does not check that no document was written. A post-commit count of ids 200 to 229 would close it.
- **Receipt conflicts:** the receipt's reason for the closed path (the `isSolrThread()` condition) is wrong, since that condition is true on pool threads; the closure is structural (listener placement). The existing-401 list is incomplete (two cluster-client 401s exist). The TESTING note's "wrapped `SolrServerException`" does not occur for a 401.

## Cross-ticket interactions

- **The `security.json` seam (SOLR-10627 and SOLR-13097):** the edit-time validation runs in standalone and cloud modes through the shared `doEdit`. A write that skips the plugin's edit, or a direct file write, skips the check. This is the seam the Core admin round's SOLR-13097 works on, and the SOLR-10627 draft's Limits say what it does not cover.
- **SOLR-10627 and SOLR-18010:** SOLR-18010 adds locking and atomic writes underneath the same edit path. No file is shared. The two changes meet at the file, and neither changes the other's validation.
- **The basic-auth test family (SOLR-12161 and SOLR-18010):** no file is shared, and neither covers a no-credentials HTTP update in standalone mode. The standalone gap is coverage only, since standalone has no PKI path. The nearest coverage is the unit-level authorization decision, which never goes through HTTP.
- **SOLR-18368 and the others:** the page documents the basic-authentication plugin that SOLR-12161's test exercises and SOLR-10627's permissions configure. The page as corrected does not contradict either branch's behavior. This was not checked line by line.
- **SOLR-11678 and SOLR-18132:** the same SSL configuration family. The 11678 change adds a getter, a constant and a map entry, and nothing on main switches over the credential types, so it is compile-safe on main.

## Owner decisions

1. **SOLR-10627:** approve the proposed changelog title and have the branch owner commit it, or accept the current title with its overstatement. Keep the "ignore versus error" Choice, or drop it. Add an update-permission case to the test (recommended), or accept that path untested.
2. **SOLR-18368:** whether a PR opens, and under which key. Recommendation: (a), no key, citing `#4782`. Confirm the Jira status before any option that names the ticket.
3. **SOLR-18368:** whether to fix the `zkHostList` and `chroot` declarations in the example, as a small scope call.
4. **SOLR-11678:** remove `SOLR-11678-TESTING.md` from the outbound tree (the owner chooses how the submit branch changes). Approve the `EnvToSyspropMappings` line and its test assertion (recommended). Authorize the main-side premise run, accepting that the premise is JKS-only. Decide whether to accept the Jetty property side effect as a documented non-issue. Decide client scope (server-only, recommended).
5. **SOLR-12161:** which tree runs the premise (the merge-base, or main after the hunk is applied; recommendation: main). If it passes, keep the test as a documented pin (framing: a pin, not a fix), and decide whether to add the no-write count.

## Main-side work owed

- The premise run for SOLR-11678 (JKS keystore, the seven runs in the part report) on packaged base and head.
- The focused runs for SOLR-11678 (the three new test classes, through the queue with fail-before).
- The premise run for SOLR-12161 (`testBasicAuth` with fail-before), on the owner's verify request.
- The SOLR-10627 gate logs (`g10627-gate.log`, `g10627-premise.log`) are not on disk; the counts rest on the receipt.
- The SOLR-18368 gate log `g18368-gate.log` is not on disk; the "3 of 3" count rests on the receipt. Optional: re-run the snippet compile and the page build at the live tip.
- Receipt corrections listed in the part reports (SOLR-10627, SOLR-18368, SOLR-11678, SOLR-12161).

## Not done

- No build, Gradle run, or test. No `gh` write call. No commit to a submit branch. No live PR edit. No JIRA access.
- The changelog YAML files were read by eye, not parsed by a tool.
- Jetty and JDK behavior for SOLR-11678 was read from class files. Nothing was executed.
- The part reports' "Not checked" sections list what each part could not verify.
