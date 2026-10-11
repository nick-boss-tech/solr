# Configsets draft fidelity, slice 1

Assignment: `assignments/pool-draft-fidelity-configsets-query-schema.md`, slice A1. Claim: `claims/pool-draft-fidelity-configsets-query-schema.md` (slice A1: SOLR-15478, 17363, 6960, 7267). Slice drafts: `pr-drafts/configsets/SOLR-15478.md`, `SOLR-17363.md`, `SOLR-6960.md`, `SOLR-7267.md`. Worktree HEAD `d627304e96bb614fccb1b031b99ce4b4cc819869`, status clean.

Category round report: `reports/configsets-round-1.md`, with parts `-a`, `-b`, `-c`. Answers material: `material/solrcloud-round-1-answers.md` (its SOLR-15674 entry names SOLR-15478). A grep of `material/` for the four ticket numbers finds no other answers.

Head checked per draft (`git ls-remote origin refs/heads/<branch>`):
- SOLR-15478: `solr-15478-submit` = `0478bdf0ac5cd100d5020451a6732ca5cbaa1a52`. Matches the draft.
- SOLR-17363: `solr-17363-submit` = `b8e8e1c48461be964ac0ef9a0cc570c277e7420d`. Matches.
- SOLR-6960: `solr-6960-submit` = `9bef536fc12a3c5a1718f22b11cffa348e57a433`. Matches.
- SOLR-7267: `solr-7267-submit` = `59f34a339b0d3cc227c79e56bc2e59814fed2daf`. Matches.

Every head SHA resolves (`cat-file -t`). Merge-bases with `upstream/main` (worktree ref at `3f5d4c5bf8`), used as the base in the symptom-link replacements below: SOLR-15478 `b5c71bc5573c`, SOLR-17363 `56ec140e3636`, SOLR-6960 `97d973814336`, SOLR-7267 `cabedd1d968`. The first three match round 1.

Receipts present for all four: `receipts/SOLR-15478.md`, `SOLR-17363.md`, `SOLR-6960.md`, `SOLR-7267.md`.

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-15478 | `0478bdf0ac5` (matches live tip) | DRIFT (1 item) |
| SOLR-17363 | `b8e8e1c4846` (matches live tip) | DRIFT (2 items) |
| SOLR-6960 | `9bef536fc12` (matches live tip) | DRIFT (3 items) |
| SOLR-7267 | `59f34a339b0` (matches live tip) | DRIFT (2 items) |

## SOLR-15478

Verdict: DRIFT (1 item).

1. Draft says: "The cache key is built in [ConfigSetService.java](https://github.com/nick-boss-tech/solr/blob/0478bdf0ac5cd100d5020451a6732ca5cbaa1a52/solr/core/src/java/org/apache/solr/core/ConfigSetService.java#L304-L318)."
   - Evidence: This is pre-change symptom code in "What happens today". `git diff --stat b5c71bc5573 0478bdf0ac5` does not list ConfigSetService.java, and lines 304-318 read the same at the merge-base. The cited lines are correct (cache key built at L309-L317, cache get at L317-L318). Per pr-formula.md, the link goes to the merge-base and the text says so.
   - Replacement: "The cache key is built in [ConfigSetService.java, base commit](https://github.com/apache/solr/blob/b5c71bc5573c4e31b4cee5a7965d73587fc0ae58/solr/core/src/java/org/apache/solr/core/ConfigSetService.java#L304-L318)."

Checked, no drift: head; receipt count 1 of 1 at the head and date 2026-10-03; test class `ZkConfigSetServiceModificationVersionTest` has one @Test, and its steps match the draft's Proof (upload, delete, re-upload, unchanged file keeps its version, changed file gets a new one); ZkConfigSetService.java L101-L118 at head returns `stat.getMzxid()`, and L117 at base returns `stat.getVersion()`; FileSystemConfigSetService.java is not in the branch diff and uses `getLastModifiedTime` (L352); `shareSchema` defaults to off (NodeConfig.java builder default at L606) and is read from solr.xml (SolrXmlConfig.java L364); changelog `SOLR-15478.yml` exists at head (type fixed, Nick Shanin); the Limits match round 1 part B and the material entry for SOLR-15674 (the branch "covers only the configset cache that shareSchema enables").

Optional notes, not blocking:
- The Proof claim "fails on the base code" rests on the receipt's wording "the gate's pre-fix proof step passed at this head". In the pipeline's verdict words (AGENTS.md: PASS means failed without the fix), this supports the draft. The receipt does not name the failing assertion, and `g15478-harden.log` is not on disk. Round 1 owner check 5 stays open.
- "A new znode starts that version over at 0." is identical in the SOLR-15674 draft, as the material asks. "ZooKeeper does not reuse this ID, so a recreated configset gets a new cache key." differs from the SOLR-15674 draft's sentence ("...so a recreated znode gets a new cache key") by one word. The material (solrcloud round 1 answers, part p1 finding 5) asks for these sentences to stay identical. Left as is; owner may align.
- Premise: the Jira packet does not say the reporter had `shareSchema` on (round 1 owner check 3).
- The changelog title does not name the shareSchema condition (round 1 part B finding 3, optional).
- Plain language: "mzxid", "ZooKeeper data version" and "znode" have no gloss on first use.

## SOLR-17363

Verdict: DRIFT (2 items).

1. Draft says: "The replicas to wait for are chosen when the request starts ([SolrConfigHandler.java](https://github.com/nick-boss-tech/solr/blob/b8e8e1c48461be964ac0ef9a0cc570c277e7420d/solr/core/src/java/org/apache/solr/handler/SolrConfigHandler.java#L865-L867))."
   - Evidence: pre-change symptom in "What happens today". The branch changes SolrConfigHandler.java only at the wait (L909-L937) and the new helper (L964-L977); replica selection at L865-L867 is unchanged (`git diff --stat 56ec140e3636 b8e8e1c4846`). The same lines read identically at the merge-base 56ec140e3636. Per pr-formula.md, link the merge-base and say so.
   - Replacement: "The replicas to wait for are chosen when the request starts ([SolrConfigHandler.java, base commit](https://github.com/apache/solr/blob/56ec140e3636d5f4150fa87fbf7103529536ac99/solr/core/src/java/org/apache/solr/handler/SolrConfigHandler.java#L865-L867))."

2. Draft says: "A failed core still fails the request only if its replica is an active replica on a live node ([helper](https://github.com/nick-boss-tech/solr/blob/b8e8e1c48461be964ac0ef9a0cc570c277e7420d/solr/core/src/java/org/apache/solr/handler/SolrConfigHandler.java#L964-L977))."
   - Evidence: head L964-L977 is `failedCoresStillActive`, which only tests whether a failed core URL is in the list it is given. The live-node test is in `getActiveReplicas` at head L993-L994 (`replica.getState() == Replica.State.ACTIVE` and `liveNodes.contains(replica.getNodeName())`). The cited range does not hold the live-node part of the claim.
   - Replacement: "A failed core still fails the request only if its replica is an active replica on a live node ([helper](https://github.com/nick-boss-tech/solr/blob/b8e8e1c48461be964ac0ef9a0cc570c277e7420d/solr/core/src/java/org/apache/solr/handler/SolrConfigHandler.java#L964-L977); the active filter is at [L993-L994](https://github.com/nick-boss-tech/solr/blob/b8e8e1c48461be964ac0ef9a0cc570c277e7420d/solr/core/src/java/org/apache/solr/handler/SolrConfigHandler.java#L993-L994))."

Checked, no drift: head; the four test names exist at head (L77, L88, L99, L127), and `testActiveReplicaThatNeverReportsStillFails` spans L99-L124; receipt counts (TestConfigWaitForReplicas 4 of 4, TestSolrConfigHandler 8 of 8, TestReqParamsAPI 1 of 1, TestSetPropertyConfigApis 4 of 4) match the draft; the receipt's base result (both new tests fail with the overlay-version message on 56ec140e363) matches the draft; the base throw at L912-L922 matches the draft's base link; the post-wait re-read at L909-L937 matches; the Limits sentence about the collections API unloading the core matches the test class javadoc (L52); changelog `SOLR-17363.yml` exists at head (type fixed, Nick Shanin) and its title agrees with the draft's account; the ticket facts "9.4" and "only one replica on a node can be reloaded at any time" are in the local Jira packet (Versions: 9.4; solr-core-9.4.0.jar in the server-side trace).

Optional notes, not blocking:
- "verified 2026-10-05": the receipt gives no run date; it has a takeover-log DONE entry dated 2026-10-05. The only on-disk result, `research/test-queue/results/SOLR-17363.json` (SUCCESS, 4 tests), finished 2026-10-03, before the head commit (2026-10-05T07:39:57Z). The counts for this head are receipt-only. Owner to confirm (round 1 finding 7).
- The stale-replica Limit assumes round 1 option (a) or (c). Round 1 lists that scope call as an open owner decision; no answer exists in the material.
- The choice section's narrower alternative is a live alternative with its cost stated. Round 1 owner decision 2 (keep or narrow the down-or-recovering excusal) is still open.
- Receipt line 8 omits the node-not-live case that the draft and the code include. The draft is right; the receipt needs the correction (round 1 finding 6). Not draft drift.
- Length is 4,940 characters, above the roughly 3,500 guide. Most of the excess is link URLs.
- Plain language: "overlay version" and "property overlay" need a gloss.

## SOLR-6960

Verdict: DRIFT (3 items).

1. Draft says: "**Built-in handlers are not covered. The ticket's own example is one of them.**" (Limits), with "The report now merges initParams into request handlers defined in solrconfig.xml." (What this change does).
   - Evidence: the branch changelog `changelog/unreleased/SOLR-6960-config-report-initparams.yml` at 9bef536fc12, line 2, reads: "  The /config API (requestHandler section) now reports the defaults, appends and invariants contributed by initParams." That claims the whole requestHandler section. Built-in handlers are added as stored (SolrConfigHandler.java L352-L358), and `/update/json/docs` is built-in (ImplicitPlugins.json L28). The title and the draft disagree, and the title is the item to fix (round 1 part A finding 1, FIX). This is a branch edit for the owner; the draft text needs no change.
   - Replacement (branch changelog line 2, same folded `title: >` block): "  The /config API now reports the defaults, appends and invariants that initParams add to request handlers defined in solrconfig.xml."

2. Draft says: "([SolrConfig.java L985-L993](https://github.com/nick-boss-tech/solr/blob/9bef536fc12a3c5a1718f22b11cffa348e57a433/solr/core/src/java/org/apache/solr/core/SolrConfig.java#L985-L993))" (in "What happens today").
   - Evidence: pre-change symptom code. At head 9bef536fc12, L985-L993 is the changed loop (the applyInitParams call is at L991). The pre-change loop is at the merge-base 97d973814336, L985-L993, where `items.put(info.name, info);` is at L988. Per pr-formula.md, link the merge-base and say so.
   - Replacement: "([SolrConfig.java L985-L993, base commit](https://github.com/apache/solr/blob/97d973814336101e12475558d7419321c743de79/solr/core/src/java/org/apache/solr/core/SolrConfig.java#L985-L993))"

3. Draft says: "Changelog: `changelog/unreleased/SOLR-6960-config-report-initparams.yml`"
   - Evidence: the changelog line must be a link to the fragment at the head SHA (pr-formula.md template; main-side decision 2026-10-10). The file exists at 9bef536fc12 under this name. The template name `SOLR-6960.yml` is not on the branch.
   - Replacement: "Changelog: [changelog/unreleased/SOLR-6960-config-report-initparams.yml](https://github.com/nick-boss-tech/solr/blob/9bef536fc12a3c5a1718f22b11cffa348e57a433/changelog/unreleased/SOLR-6960-config-report-initparams.yml)"

Checked, no drift: head (9bef536fc12 matches the live tip 9bef536fc12a3c5a1718f22b11cffa348e57a433); the top-up receipt gives TestInitParams 9 of 9 and TestSolrConfigHandler 8 of 8 at 9bef536fc12, dated 2026-10-09, matching "Verified 2026-10-09"; the premise result in the receipt (one failure, testConfigReportIncludesInitParams, "expected:<A> but was:<null>") matches the draft; both test methods are at the cited lines (L67-L73, L76-L87); the applyInitParams loop (L988-L992) and the overlay loop (L994-L996) match the draft's citations; RequestHandlers.java startup use (L110-L117) and helper (L134-L152) match; ImplicitPlugins.json L28 is `/update/json/docs`; the local Jira packet uses `<initParams path="/update/json/docs">` as its example; no choice section, which matches round 1 (the built-in gap sits in Limits with the follow-up offer).

Optional notes, not blocking:
- The second Proof bullet says the run without the change "is not recorded", which is accurate. Round 1 owner decision 2 (run it without the change, or accept the Proof as pending) is still open.
- "Recorded 2026-10-05 at 4569ad9d591" and "The first test's failure is recorded at 4569ad9d591": the receipt gives the premise result without a head. The head is inferred from the 8-test count (the second test appears only at 9bef536fc12). "Recorded" is record-keeping wording; "checked" would read more plainly.
- The two Limits citations (SolrConfigHandler.java L352-L358, ImplicitPlugins.json L28) point at head. Those lines are unchanged by the branch and read the same at the merge-base, so they are acceptable as they are.
- The branch file name differs from the template `SOLR-<ticket>.yml`. Whether to rename the branch file is an owner decision (round 1 note 11).
- Length is 4,371 characters, above the guide. Most of the excess is link URLs; round 1 note 12 suggests dropping the startup-helper link.
- Plain language: "initParams", "appends", "invariants" and "overlay handlers" need a gloss.
- Not in the draft: `RequestHandlers.applyInitParams` is public and only package code calls it (round 1 part A note 5). Owner decision.

## SOLR-7267

Verdict: DRIFT (2 items).

1. Draft says: "([managed-schema.xml L620-L628](https://github.com/nick-boss-tech/solr/blob/59f34a339b0d3cc227c79e56bc2e59814fed2daf/solr/server/solr/configsets/_default/conf/managed-schema.xml#L620-L628))" (in "What happens today").
   - Evidence: pre-change symptom code (the cz names before the change). The change adds the cs block at L630-L639 and leaves L620-L628 alone. The same lines read identically at the merge-base cabedd1d968. Per pr-formula.md, link the merge-base and say so.
   - Replacement: "([managed-schema.xml L620-L628, base commit](https://github.com/apache/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/server/solr/configsets/_default/conf/managed-schema.xml#L620-L628))"

2. Draft says: "Changelog: `changelog/unreleased/SOLR-7267-czech-cs-field-type.yml`"
   - Evidence: the changelog line must be a link to the fragment at the head SHA. The file exists at 59f34a339b0 under this name (type added, author Nick Shanin, title matches the draft's account). The template name `SOLR-7267.yml` is not on the branch.
   - Replacement: "Changelog: [changelog/unreleased/SOLR-7267-czech-cs-field-type.yml](https://github.com/nick-boss-tech/solr/blob/59f34a339b0d3cc227c79e56bc2e59814fed2daf/changelog/unreleased/SOLR-7267-czech-cs-field-type.yml)"

Checked, no drift: head matches the live tip; receipt count DefaultConfigSetCzechTest 1 of 1 at the head, takeover-log date 2026-10-07, gated tree c3d8cb47d27 (the draft's "The count above comes from c3d8cb47d27" agrees with the receipt); the base failure text "expected text_cs but was empty" matches the receipt; `testCzechLanguageCodeAlias` at head L33-L57 checks the cs dynamic field, the cs stopword filter, that text_cz stays, and that the two stopword files are equal, so the draft's Limits sentence is accurate; the cs block is at head L630-L639 as cited; the cs stopword file has 172 lines at head (`wc -l`), matching the draft's "172 lines"; base has no `_txt_cs`, `text_cs` or `stopwords_cs` (grep at cabedd1d968), matching "no rule before"; the Choice has a live alternative (rename cz to cs) with its cost stated; the LUCENE-6366 Limit matches the ticket's one comment in the local Jira packet ("we should figure out LUCENE-6366 before trying to address in the sample configs").

Optional notes, not blocking:
- The Choice wording comes from the receipt's one-line description. The review it names (`goal files/reviews-2026-10-06-round28-fresh-arrivals/7267.md`) is not on disk. Round 1 owner check 3 stays open before use.
- "Recorded 2026-10-07 at c3d8cb47d27." is a takeover-log date, not a run date. The date is present, but "Recorded" is ledger wording. Owner decides whether to keep it (round 1 note 10).
- The LUCENE-6366 Limit may be kept or dropped by the owner (round 1 note 14).

## Not done

- The gate, premise and top-up logs named in the receipts are not on disk (`g15478-harden.log`, `g17363-gate.log`, `g17363-premise.log`, `g6960-gate.log`, `g6960-premise.log`, `g6960-topup.log`, `g7267-gate.log`). Test counts are receipt-only, apart from the on-disk SOLR-17363 result JSON, which predates the head.
- The SOLR-7267 Choice review and the SOLR-6960 round 27 review are not on disk, so their wording was not checked against source.
- No live GitHub PR state and no live Jira state were checked. Jira facts come from the local packets under `research/jira-context/`.
- Changelog YAML was read by eye, not parsed. No build, Gradle run, test, or gate run was made. No commit, push, PR, or Jira write.
- The brief named worktree commit e84522fa5bc; the worktree is at d627304e96b, as the lead instructed, and is clean.
