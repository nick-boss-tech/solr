# solr-4424-submit

- Branch: origin/solr-4424-submit
- Head: 2e947b7f622c (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind, 2 commits ahead)
- Scope: 2 commits, 3 files. `NamedList.java` (`toSolrParams`, `:352-361`: throws `SERVER_ERROR` when a name is `null`, with the raw value in the message), `NamedListTest.java` (+14, `testToSolrParamsRejectsUnnamedEntry`), changelog `SOLR-4424-unnamed-config-param.yml` (`type: fixed`). No `SOLR-4424-TESTING.md` on the tip.
- Verdict: Needs work (the check covers only the null-name case the test exercises; the scope question and the global `NamedList` effect are owner calls)
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-4424-review.md` (verdict Needs work) was written at the same head (2e947b7f622c). No delta.

- Bulk F1 (scope gap against the expanded JIRA cases): **confirmed as a gap; scope is an owner call.** See finding 1.
- Bulk F2 (global `NamedList` effect): **confirmed as a question.** See owner call 1.
- Bulk F3 (raw value in the error message): **confirmed.** See finding 2.

## Findings (ranked)

1. **MEDIUM, verified. The check rejects only `null`.** `NamedList.toSolrParams` tests `name == null` (`:357`) and nothing else. An empty name, a whitespace-only name, a name with surrounding whitespace (not trimmed), and punctuation names all pass through as `SolrParams` keys. The test (`NamedListTest.java`, diff lines 48-59) covers only the null case. The JIRA packet (`research/jira-context/SOLR-4424.json`, comment body from the 2013-02-12 thread) proposes extending the issue to "other forms of unreasonable names" for `SolrParams`. Whether that expansion is accepted for this ticket is the owner's call (owner call 2). The bulk review is right that the branch does not implement it.

2. **LOW, verified. The error message echoes the raw value.** `:360` concatenates `val` into the message: `"Parameter without a name (missing 'name' attribute?), with value: " + val`. A configuration value is printed to startup logs. If an unnamed config value can carry a secret, it reaches the log. Suggested (not applied): report the position or the value type, not the content.

## Owner calls (not decided here)

1. **Global effect of the check.** `toSolrParams` is the generic conversion, and 24 call sites in `solr/solrj/src/java` and `solr/core/src/java` call it. `NamedList` legitimately holds null-named entries on the response side, for example `FacetComponent.java:1155`, `NumericFacets.java:571`, `DocValuesFacets.java:304`, and `ExpandComponent.java:756`. The JIRA discussion raises exactly this point (validation belongs at the `SolrParams`/config boundary, not in `NamedList`). Checked here: the null-named adds exist and sit on the response side. Not traced: whether any of the 24 callers receives one of those lists with null names, which is the question that decides whether the global effect is real. Pose it: move the check to the config boundary, or keep it in `toSolrParams` and accept the global effect.

2. **Scope.** Does this ticket accept the expanded cases from the JIRA comment (empty, whitespace, untrimmed, and punctuation names), or only the null case the branch implements? If the expanded cases are in scope, finding 1 is needed before submission. If not, the changelog and the test name should say "missing" only.

3. **Flake classification (handoff item).** The handoff says an earlier GitHub failure on this branch was classified a flake, and asks that the classification be checked. Checked here and not found:
   - `research/` notes: no record for SOLR-4424 mentioning a flake or a GitHub failure (search over the workspace notes).
   - Fork CI: `gh.ps1 run list --repo nick-boss-tech/solr --branch solr-4424-submit` returns no runs.
   - Upstream: `gh.ps1 pr list --repo apache/solr --head solr-4424-submit --state all` returns no PRs.
   So the earlier failure, its run ID, and its classification cannot be verified from here. The owner needs to supply the run ID or the record that says "flake". Until then the classification is unverified, not confirmed.

## Proposed fixes (not applied; the owner decides)

- Finding 1: add the trimming, empty, and whitespace checks and tests if owner call 2 says the expanded scope is in. Otherwise narrow the changelog.
- Finding 2: drop the raw value from the message.
- Owner call 1: decide where the check lives.

## Interactions with other branches

- Handoff notes say this branch composes with SOLR-3722. SOLR-3722 was not read in this review, so the interaction is not checked.

## Not checked

- Not compiled, formatted, or run. No Gradle, no `drain`, no test runs.
- Whether any of the 24 `toSolrParams()` callers receives a null-named `NamedList` (owner call 1).
- The flake classification (owner call 3): no record found and no run found.
- The JIRA ticket text was not re-read beyond the comment packet; the expansion is taken from the packet.
- No GitHub or JIRA writes were made.
