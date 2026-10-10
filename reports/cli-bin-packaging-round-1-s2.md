# CLI, bin scripts and packaging round 1, part S2: package tooling (SOLR-16272 and SOLR-16813)

Drafts written; no builds, tests, `gh` writes, commits, pushes or posts. Only edits: the two drafts below, plus the new folder `pr-drafts/cli/`.

## Head verification

- **SOLR-16272:** `ls-remote origin refs/heads/solr-16272-submit` returns `d2cf817391692b29c3d221ec4ebed65c3833bb91`. It matches the assignment and the receipt. The merge-base with `upstream/main` is `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`. The diff is three files: `RepositoryManager.java`, `PackageToolTest.java`, and `changelog/unreleased/SOLR-16272.yml`. Read with `git show` and `git diff`; no checkout.
- **SOLR-16813:** `ls-remote` returns `1b288170e8aa9c3b1ea9bce418e26bd61f08498e`. It matches. The merge-base is the same `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`. The diff is three files: `SolrPackageLoader.java`, `TestPackages.java`, and the changelog YAML.
- Neither tip moved.

## Per ticket

**SOLR-16272: draftable.** The gate is green at the live tip per the receipt. `PackageToolTest` has 3 `@Test` methods at head (2 at base), matching 3 of 3. The rule the receipt states matches the code (`RepositoryManager.java` line 224 and lines 258 to 263 and 299 to 311). No Choices section: the keep-or-remove rule is not a live alternative, since always rolling back would delete files a registered version needs.
- Draft `pr-drafts/cli/SOLR-16272.md`, 4,508 characters with links (2,899 without URLs).
- Title: "Remove a failed package install's files unless the version may be registered".

**SOLR-16813: draftable.** The gate is green at the live tip. `TestPackages` has 6 `@Test` methods at head (4 at base), matching 6 of 6. Checks:
- (a) Best effort holds: `fetchManifest` catches both a non-FILE result and an exception, logs a warning, and the version still loads (`SolrPackageLoader.java` lines 294 to 315 at head).
- (b) Same lazy path as the JARs: both call `getType(path, true)`. The JAR path is `FileStoreUtils.validateFiles` (base line 54).
- (c) Unsigned: the test passes null to the `TestDistribFileStore.postFile` helper. `RepositoryManager` passes null to `PackageUtils.postFile` (`RepositoryManager.java` lines 192 to 196). The wire form differs slightly (see Not checked).
- Draft `pr-drafts/cli/SOLR-16813.md`, 5,817 characters with links (3,852 without URLs). It is above the roughly 3,500 guide because of the Choice section and two tests.
- Title: "Fetch a package version's manifest.json into the local file store when the version loads, best effort", matching the best-effort scope.
- The Choice section (best effort versus mandatory) stays, because the ticket asks that exact question and the mandatory route has a real cost.

## Filestore sequence (install, retry, load), both diffs read together

- **Install (16272):** the manifest is posted first, then the artifacts, then the registration request. A failure before the request removes everything posted. A failure after the request removes files only when the registry read shows the version absent; a failed read keeps them. Result: after a failed install, either nothing from that attempt is in the store and the version is not registered, or the full set is in the store and the version may be registered.
- **Retry:** after removal, the re-post goes to empty paths and registers. If files were kept and the version is registered, the retry stops at the existing-plugin check (`RepositoryManager.java` line 167) with "Plugin already installed". If files were kept and the version is not registered, the re-post succeeds only with identical bytes and signature (`ClusterFileStore.java` lines 111 to 131 treat the same metadata as idempotent; otherwise "Path already exists").
- **Load (16813):** the server registration (`PackageAPI.add`, base lines 276 to 283) validates the JAR files only, so a registered version can lack its manifest on a node. On load, the manifest is fetched from a peer when missing. If no peer has it, a warning is logged and the version loads, but package commands on that node fail. If a JAR is missing, `validateFiles` throws before the manifest fetch runs.
- **Install, retry, then load:** the retry re-posts the manifest and registers, so the node that loads the version finds the manifest locally (FILE, no warning).
- **Result:** one consistent story. The install path keeps a registered version's manifest, and the load path does not need the manifest to load the JARs. Two caveats, both drafted or reported:
  - (a) The keep check is one registry read, so a registration the server completes after that read is missed, and its files could be removed. The load would then fail at `validateFiles`. Named as a Limit in the 16272 draft.
  - (b) `PackageManager.fetchInstalledPackageInstances` (base lines 186 to 210) reads every registered version's manifest from the node the client contacts, so one missing manifest makes that read throw. Through such a node, the 16272 check returns "may be registered" (keep, the safe direction), and the install pre-check at `RepositoryManager.java` line 167 throws before anything is posted. 16813 fixes this only on nodes that load the version, which the 16813 Limits say.
- Neither ticket touches `solr/bin/solr` or `solr.cmd`. They are Java only, so there is no landing-order effect for the `bin/solr` cluster.

## Self-check

- Dashes: zero em dashes, zero en dashes, and zero double hyphens in both drafts (byte-level grep).
- Process vocabulary: none of gate, receipt, ledger, rc=0, JUnit, pre-fix, owed, round, takeover, "by construction", Claude, audit, fresh.
- Head references: the 16272 draft has 9 links to `d2cf817391692b29c3d221ec4ebed65c3833bb91` and 1 base link to `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`. The 16813 draft has 7 links to `1b288170e8aa9c3b1ea9bce418e26bd61f08498e` and 5 base links to `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`. No cross-ticket head mentions and no short SHAs. Line spans were checked against the files with `sed` and `git show`.
- Character counts (`LC_ALL=C.UTF-8 wc -m`): 16272 at 4,508 with links; 16813 at 5,817 with links.

## Receipt disagreements (exact wording)

1. **16272 changelog title:** "A failed bin/solr package install, for example because of a signature error, now removes the files it already uploaded to the package store, so the install can be retried and uninstall no longer refuses". The code keeps files once registration may have happened (`RepositoryManager.java` line 224, lines 258 to 263, lines 299 to 311), so "removes the files it already uploaded" is too broad. "Uninstall no longer refuses" is not supported: uninstall is unchanged (`PackageManager.java` lines 105 to 114), and a version that never registered still gets "doesn't exist", which the ticket itself calls correct.
2. **16272 receipt, "The change" line:** "rolls back the files it posted, so a retry is not blocked by leftovers". The same receipt's next clause states the keep rule, so the first clause is incomplete. The draft states the full rule.
3. **16272 receipt, Proof:** "the new failed-install test fails on the unpatched code and passes at the head". Accurate, but the test (`PackageToolTest.java` lines 443 to 508) covers only an artifact signature failure before registration. No test covers the keep branch after registration. The receipt does not say so.
4. **16813 changelog title:** "A node that joins a cluster with installed packages now also fetches each package version's manifest.json into its file store when another node can serve it, so package commands such as list-installed no longer fail with NOT_FOUND on that node". The receipt says the fetch happens "when the version loads". The code trigger is the version load (`fetchManifest` is called from the `Version` constructor, `SolrPackageLoader.java` line 276), not joining. Also, NOT_FOUND is the inner cause: the client-side read wraps the failure in BAD_REQUEST (`PackageManager.fetchInstalledPackageInstances`).
5. **16813 receipt:** "on base `14c7aac0d151` the class runs 6 tests with exactly 1 failure". The base `TestPackages` has 4 `@Test` methods. The 6 is this branch's test file run against base code. This is a clarification, not a contradiction; the draft words it that way.
6. **16813 receipt** cites "fresh JUnit XML (seed `16813C0FFEE16813`)". Omitted from the draft as process vocabulary.

## Owner decisions

1. Amend the 16272 changelog title on the branch (a changelog-only commit, as 16813's title was narrowed) to the keep rule, and drop "uninstall no longer refuses". Recommendation: yes, before the PR.
2. Amend the 16813 changelog title to "when a version loads", and scope the list-installed claim to nodes that load the version. Recommendation: yes.
3. The 16272 keep branch has no test. Either add a registration-failure test (a main-side gate run), or ship with the Limit as drafted. Recommendation: ship with the Limit and offer the test as a follow-up.
4. The 16272 single-read race: accept and name it (as drafted), or change the rule to a second read. Recommendation: accept and name.
5. 16813 Choice section: keep (recommended), or drop.
6. 16813: install and uninstall through a node missing any manifest also fail (the registry read). Name it explicitly in Limits, or leave it under the general "package commands fail" wording. The owner's call.

## Not checked

- No builds, tests or gate runs (per the rules). Gate and test counts come from the receipts and from counting `@Test` in the files at head and base.
- The 16272 pre-fix failure text is not recorded in the receipt, so the draft quotes none.
- `TestDistribFileStore.postFile` always sends a "sig" parameter, which is null here. `PackageUtils.postFile` omits it when null. How SolrJ encodes a null parameter value was not checked. The gate's pass is the evidence the upload was accepted.
- When a node builds `Version` objects was not traced. The "joins" versus "loads" point rests on the constructor placement only.
- `DistribFileStore.fetch` internals (timeouts, behavior with peers down) were not read.
- The 16272 race is inferred from code (one registry read; the server's add is a synchronous ZooKeeper `atomicUpdate`). Not reproduced.
- Jira context files were read only; no JIRA call.
