# SOLR-13097 merge conflict analysis (read-only)

Date: 2026-10-11. Assignment: `assignments/pool-solr-13097-conflict-analysis.md`. Claim: `claims/pool-solr-13097-conflict-analysis.md` (left ACTIVE by this run; see Limits). Live PR: apache/solr #5016.

No branch, PR, or ref was changed. Only `refs/remotes/upstream/main` moved (fetched). No build, test, gate, rebase, push, or comment.

## 1. Tips and merge-base

| Role | SHA | Date | Note |
|---|---|---|---|
| Merge-base | `c3e18f1e45506af2ed1a0087af6bcaec5b60fdf6` | 2026-10-01 12:50:09 -0400 | SOLR-18460 |
| Live branch tip (`origin/solr-13097-submit`) | `f0e7395f58f3c8d8c9066cd88baa6931d8b6f425` | 2026-10-07 07:21:31 +0000 | 11 commits past merge-base; equals live PR head |
| Current main tip (`upstream/main`) | `d76fc14b9857e77b830d7a82514b28c523a239c2` | 2026-10-10 21:13:16 -0400 | fetched 2026-10-11 (was `3f5d4c5bf8a`); 82 commits past merge-base |

Live PR #5016 (read-only `gh pr view`, 2026-10-11): state OPEN; reviewDecision CHANGES_REQUESTED (janhoy, 2026-10-06 00:05:18Z); mergeable CONFLICTING; mergeStateStatus DIRTY; headRefOid `f0e7395f58f3`; baseRefOid `14c7aac0d151` (a 2026-10-03 main commit, not an ancestor of the merge-base); updatedAt 2026-10-10 21:50:14Z.

The k2 report (`reports/core-admin-round-1-k2.md`) trial-merged against an older local main (`8e62c268688`). Its conflict description is superseded by section 2.

## 2. Conflict surface

Method: `git merge-tree --write-tree` between `d76fc14` and `f0e7395` (in-memory, no checkout). A scratch worktree was tried first; `git worktree add` under the scratchpad failed with "Filename too long" (`ui/...` path) and left no registration, so there is no scratch copy to remove. The per-commit check below used `--merge-base=<parent>` (cherry-pick style, one commit at a time against main). No rebase was run.

### 2a. Net merge (branch tip into current main): 1 file, 1 hunk

| File | Conflict hunks |
|---|---|
| `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc` | 1 |
| `changelog/unreleased/SOLR-13097.yml` | 0 |
| `solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java` | 0 |
| `solr/core/src/test/org/apache/solr/security/CoreScopedAuthStandaloneTest.java` | 0 (new file on branch) |
| `solr/solr-ref-guide/modules/deployment-guide/pages/rule-based-authorization-plugin.adoc` | 0 |

Hunk: `major-changes-in-solr-10.adoc`, merged file lines 39 to 59 (markers at 39, 53, 59).

Why it conflicts: the merge-base has `== Solr 10.1` at line 37, directly after the "Solr 10.0 requires at least Java 21" paragraph. Both sides insert at that same point.

- Main side: `ef23d0f8ad2` (SOLR-18518, "Port the snapshot commands to picocli (#5044)", 2026-10-08) inserted `== Solr 10.2` and a 16-line `=== Snapshot commands are now bin/solr snapshot sub-commands` section. This is the only reason main has a Solr 10.2 section. Main commit `1489e19164d` (SOLR-18455, 2026-10-05) touched only the 10.0 section and does not overlap this hunk.
- Branch side: `8aa4759770e` ("Scope the upgrade note to Solr 10.2 instead of 11", 2026-10-06) inserted `== Solr 10.2` and `=== Security` with the note body (moved from the Solr 11 page at the reviewer's request).
- The identical `== Solr 10.2` heading and blank line merge as common. The conflict is the two different sections: main's snapshot section (merged lines 40 to 52) against the branch's Security section (merged lines 54 to 58).

Resolution shape: keep one `== Solr 10.2` heading, then main's snapshot section, then the `=== Security` block, then `== Solr 10.1`. The k2 finding-4 wording still describes the target result, but main's heading is now common, so there is no duplicate-heading line to delete as a separate step.

### 2b. Per-commit check (what a rebase would stop on): 2 commits

A rebase replays commits one at a time, so the net merge understates the stops. Single-commit trials against current main:

| Branch commit | Stops on | Detail |
|---|---|---|
| `65b2753a901` (two-core tests; adds the Solr 11 note) | `major-changes-in-solr-11.adoc`, 1 hunk at end of file | Main commit `0cfeeb26a6c` (SOLR-18450, 2026-10-06) appended `=== V2 collections tree` at the same end point. The changelog and test-file "modify/delete" lines in this trial are artifacts: a real replay has already created both files in `83305b4f320` and `7546958a894`. |
| `8aa4759770e` (scope note to 10.2; removes the Solr 11 note) | `major-changes-in-solr-10.adoc` (same hunk as 2a) and `major-changes-in-solr-11.adoc` | The note removal sits next to main's V2 section. Likely to stop again after `65b2753a901` is resolved. |

Net effect on `major-changes-in-solr-11.adoc`: `65b2753a901` adds the note and `8aa4759770e` removes it, so the branch's final diff to that file is empty. The final file must keep main's V2 section and carry no Security note.

Files with no main-side commit since the merge-base (checked with `git log c3e18..upstream/main -- <path>`): `HttpSolrCall.java`, `RuleBasedAuthorizationPluginBase.java`, `AuthorizationUtils.java`, `BasicAuthPlugin.java`, `HttpShardHandler.java`, `HttpShardHandlerFactory.java`, `HttpJettySolrClient.java`, `V2HttpCall.java`, `rule-based-authorization-plugin.adoc`, `changelog/unreleased/SOLR-13097.yml`, `CoreScopedAuthStandaloneTest.java`. Commits touching only these (plus the new test and changelog files) do not stop a rebase.

## 3. Preserve list (for any later rebase)

Reviewer requests. janhoy's CHANGES_REQUESTED review is on `e869956cdf4`, with inline comments on `HttpSolrCall.java` line 233 (shards, 2026-10-05 23:58Z), `major-changes-in-solr-11.adoc` line 49, and `changelog/unreleased/SOLR-13097.yml` line 2. Nick's replies are on `4c08aa5751e`.

1. Shards question, answered with no code change (authorization stays serving-core only). Code: `solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java`, `getAuthorizationCollectionsList()` (line 226 at tip), carried by `83305b4f320`. The answer text is in the PR body Limits and the thread reply, not in branch code.
2. Upgrade note under Solr 10.2, not 11.0. File: `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc`, `=== Security` under `== Solr 10.2`, carried by `8aa4759770e`. Keep it out of `major-changes-in-solr-11.adoc`, which must end with main's V2 section.
3. Changelog as a fix. File: `changelog/unreleased/SOLR-13097.yml`. Tip: `type: fixed`, title "Core-scoped authorization permissions were not applied in standalone mode", carried by `4c08aa5751e` (created in `83305b4f320`).
4. Ref guide: core name allowed in standalone `collection`. File: `solr/solr-ref-guide/modules/deployment-guide/pages/rule-based-authorization-plugin.adoc`. First sentence ("In standalone mode there are no collections, and the value can instead be the name of a core...") in `4c08aa5751e`; second sentence ("For a distributed request, only the core that receives the request is checked...") in `f0e7395f58f`.

Gate fix in live history:

5. `String.formatted` replaced by `String.format(Locale.ROOT, ...)` in the test (forbiddenApis failure). File: `solr/core/src/test/org/apache/solr/security/CoreScopedAuthStandaloneTest.java`, carried by `e869956cdf4`. The receipt's fix SHA `8cc61e00e60` is not in the live history (same subject and timestamp, different SHA). Keep: no `.formatted(` call in the file.

Gate-covered test methods (the three the 38e306c2cd5 gate ran):

6. `testScopedRuleAllowsItsRoleOnItsCore`, `testScopedRuleDeniesOtherRolesOnTheCore`, `testRequestWithoutCredentialsIsRejected`. File: the test above, carried by `65b2753a901` (which also adds the CORE_2 two-core setup and the unauthenticated test, and per its subject the framework configset path). Gated at `38e306c2cd5`, which is the same content before the rewrite.

Test methods the gate never ran (covered only by the 5-of-5 receipt at `f0e7395f58f`):

7. `testV2RequestUsesCoreScopedRules` and the v2 helpers (`queryV2As`, `assertV2Allowed`, `assertV2Denied`). File: the test above, carried by `f0e7395f58f`.
8. `testWildcardRuleAppliesOnlyWhereNoScopedRuleGoverns`, with the extended test configuration it needs (`CORE_3`, `STAR_USER`, the `star-role` wildcard permission in `security.json`). File: the test above, carried by `f0e7395f58f`. This is the first commit that adds `STAR_USER`.

Other carried items: license header (`9dd8702905c`), tidy (`9adca1a65f1`), no `SOLR-13097-TESTING.md` at tip (added `a522cabfd2d`, removed `e8a7543e61b`). Commit `a522cabfd2d` has the subject "add testing handoff", but the net diff carries no handoff file, so the subject is not a blocker.

## 4. Body-claim flags (live PR #5016 body)

The body was read with `gh pr view 5016 --json body` on 2026-10-11. Corrected body text is not written here.

F1. Proof: "`CoreScopedAuthStandaloneTest`: 5 of 5 pass with this change, verified at head f0e7395f58f on 2026-10-07; tidy, the Error Prone compile and `:solr:core:check -x test` pass as well."
- Status: the current `receipts/SOLR-13097.md` records this gate at `f0e7395f58f` (log `g13097r29-gate3.log`, 2026-10-07). The starting record (material entry, 2026-10-10) said no receipt recorded the 5-of-5 count, so the receipt was corrected after that entry. The log is not in this worktree and was not opened.
- A rebase changes the head. A corrected body must name the new head, and the count holds only with a receipt at that head.

F2. Proof: "(3 of 5 methods fail on base under the extended configuration)" and "(4 failing test executions under randomization)".
- Status: the receipt cites premise log `g13097r29-premise.log` (5 tests, 3 failures on base: v2, wildcard, allow under the extended configuration). Not opened here. The "4 failing test executions" for `testScopedRuleDeniesOtherRolesOnTheCore` rests on a round 26 description (2026-10-05), per the receipt, not on a log at a current head.
- Base code for `HttpSolrCall.java` is unchanged on main since the merge-base, so the premise still applies to the rebased base.
- Corrected body after rebase: cite the premise log and its head, and drop the 4-execution count unless a log supports it.

F3. Limits (shards): "re-run at head 4c08aa5751e on 2026-10-06" for the temporary probe (not shipped).
- Status: the receipt records the same probe at `4c08aa5751e`. Probe artifacts are not in this worktree. `HttpSolrCall.java` is identical between `4c08aa5751e` and the live tip (not in the `4c08..f0e7` diff), so the probe still covers the shipped code.
- The thread reply records an outcome the body omits: under the default configuration one shards variant failed at the front door with 403 (caller lacked the serving core's role).
- Corrected body after rebase: name the probe head and say the auth code is unchanged since, or re-probe at the rebased head. Optionally add the 403 outcome.

F4. Limits (blockUnknown=false): "A target core with no governing permission at all, and no wildcard permission either, can return its documents to the caller in that configuration. That last case is the plugin's designed treatment of names with no configured permission, it behaves the same in cloud mode, and it is not a gap this PR opens."
- Observed part: the receipt's probe at `4c08aa5751e` records an ungoverned target returning its document with blockUnknown=false. The mechanism (NO_PERMISSIONS_FOUND maps to OK in `RuleBasedAuthorizationPluginBase`) is in code main has not changed since the merge-base. Supported.
- Not supported by a probe or the record: "behaves the same in cloud mode" (no cloud probe; the path is shared code, so likely but unrun). "designed treatment ... not a gap this PR opens" is a judgement. janhoy's last thread comment (2026-10-06 21:58Z) accepts the shards domain as a follow-up, which is not the same as agreeing the behavior is designed.
- Corrected body after rebase: keep the observed sentence with its probe head. Cite or cut the cloud-mode and "designed" sentences. This is the DISCUSS item in the material; the observed part can now stand.

F5. Limits: "Such sub-requests send no user credentials under BasicAuth with default settings."
- Supported on main: `solr/core/src/java/org/apache/solr/security/BasicAuthPlugin.java` line 55 sets `forwardCredentials = false`; forwarding is gated on that flag at line 216. Holds after rebase.

F6. Limits: "under JWT authentication the caller's token is forwarded, so the receiving core authenticates and authorizes the actual user."
- Code on main: `solr/modules/jwt-auth/src/java/org/apache/solr/security/jwt/JWTAuthPlugin.java` lines 945 to 949 (`interceptInternodeRequest` sets the Bearer header from the principal's token). Supported by code. Not probed (the probe covered BasicAuth only).

F7. Limits: "With blockUnknown at its default (`true`), the receiving core rejects them at authentication."
- Recorded by the probe (thread reply: sub-requests fail at authentication on the receiving core). Not re-verified here.

F8. Process wording. The k2 finding-6 flag ("Added in this round" in the Proof) is no longer in the live body as read. No action, except keep future edits free of round wording.

F9. Links. The body has no code citation links; its only links are the JIRA URL and the changelog path, neither of which a rebase moves. Nick's thread reply (`4196711305`) has permalinks pinned to `4c08aa5751e`; those are not body text, but they will not match a rebased diff.

Claims checked and supported at the tip without change: the changelog path and fixed framing; the ref-guide statement (both sentences); the Solr 10.2 note location; "Cloud mode is untouched by this change" (cloud branch of `getAuthorizationCollectionsList` unchanged); the v2 path using the same list (`V2HttpCall.java` line 335 at tip).

## 5. Limits of this analysis

- The receipt logs (`g13097r29-gate3.log`, `g13097r29-premise.log`) and the probe artifacts are not readable from this worktree. Their counts and outcomes are cited from `receipts/SOLR-13097.md` and the PR thread, not from the logs.
- The per-commit check is cherry-pick style, not a rebase. The stop list can change with how earlier conflicts are resolved.
- Not checked: review-thread resolved state; the Copilot bot review on the PR (it says "Approval recommended", no findings).
- No gate, build, or test was run.
- Claim status: `claims/pool-solr-13097-conflict-analysis.md` is still ACTIVE. WORKFLOW.md says to mark a claim DONE in the same push as its deliverable. This run has no push, so the parent should mark it DONE when it pushes this report. The claim was not edited here, per the one-report limit.
- Command style: some read-only Bash calls used shell variables or a loop, against the host's command-style rule. They wrote nothing to the branch or the source checkout.
