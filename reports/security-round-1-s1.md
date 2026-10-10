# Security and authentication round 1, part S1: SOLR-10627 (draftable)

## Result

- **Verdict:** draftable. The head implements the rule the receipt describes: `set-permission` and `update-permission` reject a JSON null collection on read, update, schema-read and schema-edit, the only four names whose default collection list excludes null. Stored files load through an unchanged load path.
- **Two things need owner attention before a PR:** the changelog title is not exact (a branch edit is needed), and the receipt's "protecting nothing" wording is an overstatement, which the draft avoids.
- **Draft:** `pr-drafts/security/SOLR-10627.md` (new folder created). Length: 7,704 characters with links (5,198 without link targets), via `LC_ALL=C.UTF-8 wc -m`. That is over the roughly 3,500 guide; the excess is mostly full-SHA links and the scope statements.
- **Head verification:** `git ls-remote origin refs/heads/solr-10627-submit` returned `5a15dc0ba22083a294524210b3be007d870daa35`, matching the claim. The commit object was already local, so no fetch was needed. The diff against the merge-base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` is four files, 39 insertions and 0 deletions, as the receipt says. The worktree shows only the new `pr-drafts/security/` folder.

## Scope check (items 1 to 3)

All at head `5a15dc0ba220` unless marked base.

**1. Claim scope**
- Edit-time only: `validateOnEdit` has one call site, `AutorizationEditOperation.java` line 75 (inside the set-permission try block, lines 73 to 79). The definition is `Permission.java` lines 119 to 129.
- `set-permission` and `update-permission`: `UPDATE_PERMISSION` merges the stored entry (`AutorizationEditOperation.java` lines 133 and 134) and runs `SET_PERMISSION.edit` (line 136), so it is checked. `CommandOperation.java` lines 62 to 69 confirm that `getDataMap` returns the merged map.
- The four names: `PermissionNameProvider.java` line 46 (read, `"*"`), line 47 (update, `"*"`), line 50 (schema-read, `"*"`) and line 51 (schema-edit, `"*"`).
- Still accept null: config-edit (line 48), config-read (line 49), metrics-read (line 54), health (line 55), and `all` (line 61), whose lists include null; collection-admin-edit (line 41) and security-edit (line 52), and the other null-only names. Custom path permissions skip the check (`Permission.java` lines 121 to 124, the `wellKnown != null` test).
- Stored files keep loading: `Permission.load` (`Permission.java` lines 48 to 113) has no `validateOnEdit` call. The startup path is `RuleBasedAuthorizationPluginBase.init` lines 346 to 359 (`Permission.load` at line 352). Confirmed.
- Correction to the "protected nothing" wording: null entries are consulted for admin requests (`RuleBasedAuthorizationPluginBase.java` lines 104 to 108, base). They never apply to collection-routed requests (lines 110 to 123, base). The draft uses the second statement only.

**2. Mirror rule: not implemented.** `Permission.java` lines 119 to 128 check only `m.get("collection") == null`. The draft's Limits bullet 1 names it with the follow-up offer. Limits bullet 2 covers the existing-file note, with a load-time warning as the possible follow-up.
- Two extra findings, both in the draft's Limits:
  - (a) A list form such as `"collection": [null]` is not rejected. `readValueAsSet` (`Permission.java` lines 171 and 172) turns the null element into the string `"null"`.
  - (b) An update-permission on an existing entry that already has a null collection is checked after the merge, so a role-only update is rejected until the same update sets a collection. No test runs the update-permission path; the new test calls set-permission only.

**3. Changelog title: not exact.** The changelog YAML lines 1 to 3 read: "The authorization set-permission command now rejects `"collection": null` on per-collection permissions such as read and update, which previously protected nothing." The problems:
- It names set-permission only. update-permission is covered through delegation (`AutorizationEditOperation.java` line 136).
- "Such as read and update" reads as partial, where the rule is exact for four names.
- "Previously protected nothing" overstates (see item 1).

Proposed replacement for the branch owner to commit (not applied; the branch is out of scope): "The authorization set-permission and update-permission commands now reject `"collection": null` on the per-collection permissions read, update, schema-read and schema-edit, because a null collection never applies to a request routed to a collection."

## Standalone and cloud (item 4)

- The validation runs in both modes. `CoreContainer.java` line 851 picks `SecurityConfHandlerZk` when ZooKeeper-aware, otherwise `SecurityConfHandlerLocal`.
- Both extend `SecurityConfHandler`, which owns `doEdit` (`SecurityConfHandler.java` lines 97 to 161). `plugin.edit` is called at line 134, a rejected command throws at lines 135 to 140, and `persistConf` is at line 151. A rejected command never reaches `persistConf` in either mode.
- The plugin chain is the same in both: `RuleBasedAuthorizationPluginBase.edit` (lines 416 to 427) to `SET_PERMISSION` to `validateOnEdit`. `MultiAuthRuleBasedAuthorizationPlugin.java` line 125 calls `super.edit`, so it is covered too. A grep found no standalone, `isZooKeeperAware` or `ZkStateReader` code in `RuleBasedAuthorizationPlugin.java`, `RuleBasedAuthorizationPluginBase.java` or `Permission.java`.
- The v2 route `/cluster/security/authorization` is served by `ModifyNoAuthzPluginSecurityConfigAPI.java` lines 42 to 49, which calls `handleRequestBody` and so `doEdit`. It is registered in the shared `SecurityConfHandler` (lines 300 to 303 and 315 to 320), not per mode.
- Where the modes differ (not validation): standalone reads `SOLR_HOME/security.json` on each call with no version (`SecurityConfHandlerLocal.java` lines 52 to 65) and writes with no version (lines 86 and 87), so the three-attempt retry loop (`SecurityConfHandler.java` lines 123 to 158) cannot detect a concurrent standalone edit. Cloud writes with the expected version (`SecurityConfHandlerZk.java` lines 79 to 82) and returns false on `BadVersion` (lines 85 to 87). This is the SOLR-18010 seam, not audited here.
- **Seam note for SOLR-13097:** the check lives in the plugin's edit and is reached only through `SecurityConfHandler.doEdit`. A standalone change that writes `security.json` without calling `plugin.edit` skips it. So does anything that writes the file directly (a hand-edited `SOLR_HOME` file, or an upload to ZooKeeper). Those files load without validation in both modes.

## Self-check

- Em and en dashes in the draft: zero (grep for U+2014 and U+2013). None in this report.
- Process words in the draft (gate, receipt, ledger, rc=0, JUnit, pre-fix, owed, round, guard, premise, live tip): zero hits. The header line is the `pr-formula.md` template text, kept as written.
- Head references: 12 links to `5a15dc0ba22083a294524210b3be007d870daa35`, including the changelog link. 3 base links to `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`, each labeled "base". All 15 line ranges were checked by printing the first and last cited line at the cited SHA; all match. The changelog link uses the real file name (`SOLR-10627-reject-null-collection-permission.yml`), not the template placeholder.
- No Lucene version is named in the draft.

## Receipt disagreements (exact wording)

1. **Receipt line 10 (2):** "Existing security.json files already carrying collection null on read or update keep loading and keep protecting nothing, by design here." Null entries are consulted for admin requests (`RuleBasedAuthorizationPluginBase.java` lines 104 to 108), so they are not inert. The accurate form is that they never apply to collection-routed requests. The draft uses that form.
2. **Assignment, starting state for SOLR-10627:** "The changelog title already claims exactly the implemented rule; confirm it still does." It does not (see item 3 above).
3. **Omission in receipt line 10 (1):** it covers only the ticket's second bullet. The ticket's first bullet says a null collection on a per-collection permission "should be ignored", while the ticket description says the API "should throw an error". The branch rejects. The draft has a short Choice section for this.
4. **Omissions (not contradictions), recorded in the draft:** the update-permission behavior on legacy null entries, the `[null]` list form, and the fact that the new test covers set-permission only.

Confirmed, no disagreement: receipt line 8 (names with null in `collName` and custom path permissions still accept null; `SET_PERMISSION` covers `UPDATE_PERMISSION` through delegation; a `BAD_REQUEST` command error); the receipt counts of 18 of 18 and the base failure "expected error" (the counts are not independently verified; see below).

## Owner decisions

1. **Changelog title:** approve the replacement above and have the branch owner commit it, or accept the current title with its overstatement. The branch was not touched.
2. **Choice section ("ignore versus error", from the ticket's first bullet):** keep it, or drop it to shorten the draft.
3. **update-permission behavior change** (a role-only update on a legacy null entry is rejected): accept it, or add an update-permission case to the test before the PR. Recommendation: add the case, since no test runs that path.
4. **Load-time warning for legacy null entries:** keep as a stated follow-up offer, or drop.
5. **The `[null]` list form:** close it now (a small change to `validateOnEdit`), or leave it as a Limit.
6. **Draft length:** 7,704 characters with links, over the 3,500 guide. Trim the Choice section or the scope bullets if wanted.

## Not checked

- No tests, builds, gates, `gh` write calls, commits, pushes or posts. The gate counts are taken from the receipt.
- Test counts: 11 test methods are in `BaseTestRuleBasedAuthorizationPlugin` at the head (9 annotated with `@Test`, 2 unannotated, as is `testEditRules`). That is consistent with the receipt's 11 for `TestExternalRoleRuleBasedAuthorizationPlugin`, which extends that class and declares no tests of its own. `MultiAuthPluginTest` (6) and `TestAuthorizationFramework` (1) were not verified.
- The gate logs named in the receipt (`g10627-gate.log`, `g10627-premise.log`) were not found in a bounded search (depth 4) of the Solr-issues tree or in `research/`. The counts and the base failure are unverified against logs.
- The changelog YAML parse was not checked.
- The update-permission legacy behavior is derived from code (`AutorizationEditOperation.java` lines 133 to 136, `CommandOperation.java` lines 62 to 69), not run.
- The admin path: not every route into the admin branch was traced. `V2HttpCall.java` lines 179 to 183 (`initAdminRequest` when no local core serves the request) could send a v2 request that declares a READ, UPDATE or SCHEMA name into that branch. It was not confirmed whether any such request exists today. This is why the draft says it has not confirmed that no current request depends on these names.
- Standalone behavior is from code reading only; nothing was run in standalone mode.
- Jira: `research/jira-context/SOLR-10627.json` was read in the main checkout (read only). No JIRA call.
- The SOLR-13097 and SOLR-18010 branches were not read (out of scope).
- The worktree HEAD is the claim commit `b547acf543602b97ad5fbdf162342983bdec9885`, not the ticket branch. All citations use the head SHA from `ls-remote`.
