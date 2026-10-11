# Core admin draft fidelity, slice 2

Assignment: `assignments/pool-draft-fidelity-coreadmin-replication-spellcheck.md`. Claim: `claims/pool-draft-fidelity-coreadmin-replication-spellcheck.md`, slice B2 (SOLR-16725, 16849, 17297, 17377, 17708). Worktree at `d627304e96b`. Read-only: no build, test, gate, queue, commit, push, or GitHub or JIRA write. Heads were checked with `git ls-remote origin refs/heads/solr-<n>-submit` on 2026-10-11. Sources: `receipts/<ticket>.md`; `reports/core-admin-round-1.md` and parts `-k1` to `-k6`; `reports/core-admin-answers-round-1.md` and parts `-c1`, `-c2`; `material/core-admin-round-1-answers.md` (owner decision default 2026-10-10); `pr-formula.md`. Code checks used `git show` and `git diff` against the head and the merge-base with `upstream/main`.

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-16725 | `solr-16725-submit` = `be1838ef8ccf21a21938a9669d418abeec66e29d` (matches draft, links and receipt) | CONSISTENT |
| SOLR-16849 | `solr-16849-submit` = `d612b055da20cfdfe9fec089241404a17c2b2751` (matches) | CONSISTENT |
| SOLR-17297 | `solr-17297-submit` = `c0ab38fc0a8b57953f8b8df03f54085fb37e138d` (matches) | DRIFT (2 items) |
| SOLR-17377 | `solr-17377-submit` = `22b5f209a11a593302b64b0e4e2c2632b540b3f7` (matches) | DRIFT (1 item) |
| SOLR-17708 | `solr-17708-submit` = `10a7fa07a79ac4b4b52a83d307ee65349d015a23` (matches) | DRIFT (3 items) |

Merge-bases with `upstream/main`, used for pre-change links: 16725, 16849 and 17297 at `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`; 17377 at `56ec140e3636d5f4150fa87fbf7103529536ac99`; 17708 at `9b3a84b1c460981eab09d8ffaef776acc4a184f8`.

Shared notes, not blocking:
- Dates. Every "verified" date in the five drafts is a record date from the receipt (takeover log or ledger), not a run date that the receipt states. The round reports ask Nick to confirm each one or change the wording to "recorded" (`reports/core-admin-round-1-k3.md` NOTE 16; `-k2.md` note on 17708). Dates in the drafts: 16725 2026-10-07, 17297 2026-10-04, 17377 2026-10-05, 17708 2026-10-08. 16849 already says "recorded 2026-10-04".
- Owed draft corrections from the answers pass. Present in the drafts now: 16725 (INTERNAL block gone, Limits bold opener), 17297 (Limits bold opener, duplicate Changelog line gone), 17377 (Limits bold opener; the Proof placeholder is replaced by the shape wording the answers allow). Still open: 17708 (see item 1 and item 2 below).
- Heads will move when owed branch edits land: 16725 (test comment, `ClusterStatus.java` javadoc), 17377 (rebase onto 17297), 17708 (changelog title). Each move needs the draft's head and links refreshed, as the answers state.

## SOLR-16725

Verdict: CONSISTENT.

Checks passed:
- Proof counts match `receipts/SOLR-16725.md` (LocalFSCloudIncrementalBackupTest 7 tests, 1 skipped, 0 failures at head; base run fails `testCustomProperties` on replicationFactor, expecting String but was Long).
- Citations hold at the head: `ClusterStatus.java` L341 to L353 is `normalizeCollectionCountTypes`, and L377 is its call in `buildResponseForCollection`. These lines are code the change produces, so the head SHA is correct.
- Changelog link resolves at the head: `changelog/unreleased/SOLR-16725-clusterstatus-restored-count-types.yml` exists there, and its title matches "What this change does".
- Choice is real: numbers would change every CREATE collection's output (`-k6.md` FIX 5 and the answers adopt strings). Limits names `maxShardsPerNode` as the answers require (`-k6.md` FIX 5).
- Scope claim "The stored cluster state of a restored collection is not changed" matches the branch diff (only `ClusterStatus.java`, the test and the changelog).

Optional notes, not blocking:
- The head will move if the owed branch edits land (test comment and `ClusterStatus.java` javadoc, `-k6.md` NOTE 3 and 4). Refresh the head and links, then re-run the focused test.
- Plain language: "replica count properties" and "CREATE" could use one gloss each.

## SOLR-16849

Verdict: CONSISTENT.

Checks passed:
- Proof counts match `receipts/SOLR-16849.md` (SegmentsInfoRequestHandlerTest 7 of 7; the file has 7 `@Test` methods at `d612b055da20`).
- The draft says the new test passes on the base and that it is a regression test that does not fail first, and that the revert was not run. This matches the receipt ("not applicable by design") and the `-k4.md` and answers text. Proof is honest.
- Citations: `GetSegmentData.java` L156 and L260 at the merge-base `14c7aac0d151` hold the two `getIndexWriter(..., false)` calls that `f2aaf8769fd` (SOLR-18083) changed. The draft says that commit "is already on the base of this branch", which meets the "text says so" rule for a pre-change link. The base `DefaultSolrCoreState.getIndexWriter` throws "Indexing is temporarily disabled" when `failOnReadOnly` is set on a read-only core, so the claim about the old call is supported by reading.
- Test link `SegmentsInfoRequestHandlerTest.java` L156 to L170 at the head holds `testSegmentInfosOnReadOnlyCore`.
- `/admin/segments` with `coreInfo` is the request COLSTATUS sends per core (`ColStatus.java` L191 to L204 at the merge-base).
- "Changelog: none" matches the branch (test-only diff).

Optional notes, not blocking:
- Held. The owner call (open the regression-test PR, or close the ticket as fixed by SOLR-18083) is not taken (answers DISCUSS item 4). Do not post until it is.
- Optional: say "at the base commit" in the `GetSegmentData.java` link text, so the reader knows the link shows the fixed calls.
- Not in the draft: the branch commit subject "test-only salvage" is process wording. The answers ask for a reword if the PR goes ahead (`-k4.md` finding 13). The pending fail-before job for this ticket must also be cancelled or marked not applicable (`-k4.md` finding 14).

## SOLR-17297

Verdict: DRIFT (2 items).

1. Draft says: "## A choice to check" (draft lines 26 to 35), including "Ship the narrower change here, which fixes shared lib SPIs, and track the reverse order as a follow-up. This is the change in this PR." and "Widen this change so both directions work, for example by adding all the jars first and loading the Lucene SPIs once after both steps." and "Was the narrower fix the right call, or should this change also cover module SPIs when a shared lib is present?"
   - Evidence: `pr-formula.md` section 4 (lines 40 to 51 and 64 to 67) says a decision that is only "scope the PR narrowly versus fix the broader issue" is NOT a choice. The broader issue goes in Limits with an offer of a follow-up ticket and PR. The big-PR amendment (lines 52 to 63) does not fit this branch: the diff is one changed line in `NodeConfig.java`, a 157-line test in `TestCoreContainer.java`, and a changelog fragment (`git diff 14c7aac0d151 c0ab38fc0a8`, 165 insertions, 1 deletion). The round report keeps the question as a Choice (`reports/core-admin-round-1-k3.md` NOTE 18), and the answers keep the widening as the owner's DISCUSS call, "not taken; the draft stays held" (answers DISCUSS item 2). The owner call is open and belongs in the report's owner list, not in the public text. Replacement: delete the whole "## A choice to check" section, and replace the "## Limits" section with the text below. If the owner rules to widen the fix, the draft is rewritten and gated again. It stays held until then.
   - Replacement:
     ```
     ## Limits

     **Module SPIs are not fixed when a shared lib is present, and only shared lib SPIs are tested.**

     - A module SPI can still lose its classes when a shared lib is also configured. A probe test for that order failed at this head. The probe is not part of this change. A follow-up ticket and PR for it can be opened on request.
     - The new test covers a shared lib SPI only. Module precedence and same-name classes in both places are not tested.
     ```

2. Draft says: "([replace and close](https://github.com/nick-boss-tech/solr/blob/c0ab38fc0a8b57953f8b8df03f54085fb37e138d/solr/core/src/java/org/apache/solr/core/SolrResourceLoader.java#L263-L281))" and "([URL order](https://github.com/nick-boss-tech/solr/blob/c0ab38fc0a8b57953f8b8df03f54085fb37e138d/solr/core/src/java/org/apache/solr/core/SolrResourceLoader.java#L270-L271))"
   - Evidence: `git diff 14c7aac0d151 c0ab38fc0a8` is empty for `SolrResourceLoader.java`, so both ranges hold the same code at the merge-base: `addURLsToClassLoader` at L263 to L281 ("replace and close"), and the two `allURLs.addAll` lines at L270 to L271. Both describe the pre-change behavior that causes the symptom. `pr-formula.md` line 93 says pre-change code links the merge-base, and the text says so. The links to code the change produces (`NodeConfig.java` L228 to L229, `TestCoreContainer.java` L466 to L506) correctly stay on the head.
   - Replacement:
     - `([replace and close, at the base commit](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/core/SolrResourceLoader.java#L263-L281))`
     - `([URL order, at the base commit](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/core/SolrResourceLoader.java#L270-L271))`

Checks passed: the head is correct. The `NodeConfig.java` L228 to L229 order (`initModules();` then `setupSharedLib();`) matches the diff. The new test name and range match `TestCoreContainer.java` L466 to L506. The changelog link resolves at the head. The 26 tests, 3 skipped, 0 failures count matches the receipt. The Limits claims match the test scope. The ticket packet (`research/jira-context/SOLR-17297.json`) describes the same ClassNotFoundException with a module enabled in solr.xml, matching "What happens today".

Optional notes, not blocking:
- The answers' owed draft corrections (bold Limits opener; duplicate Changelog line removed) are already in the draft.
- "A run on the base code confirmed it" is the receipt's "PASS (pre-fix proof step in the gate)" in words. No base failure line is on disk (`-k3.md` item 5). Keep it that way and do not add a quoted message.
- "Verified 2026-10-04" is the gate date. The 2026-10-07 tidy re-check is separate. See the shared date note.
- Branch only, optional at the next touch: the changelog title is about 190 characters. The shorter title in `-k3.md` NOTE 17 is "Lucene SPI plugins in the shared lib directory no longer fail with ClassNotFoundException when modules are enabled".

## SOLR-17377

Verdict: DRIFT (1 item).

1. Draft says: "([former check location](https://github.com/nick-boss-tech/solr/blob/22b5f209a11a593302b64b0e4e2c2632b540b3f7/solr/core/src/java/org/apache/solr/core/SolrXmlConfig.java#L682-L684))"
   - Evidence: at the head `22b5f209a11a`, `SolrXmlConfig.java` L682 to L684 are three comment lines that say the classes are no longer validated there. The branch removes the check (`git diff 56ec140e3636 22b5f209a11a` on `SolrXmlConfig.java`). The former check is at the merge-base `56ec140e3636d5f4150fa87fbf7103529536ac99`, `SolrXmlConfig.java` L684 to L694: the `try` block that calls `loader.findClass` and the `catch` that throws "clusterSingleton plugins must implement the interface". The text describes pre-change behavior, so the link must use the merge-base and say so (`pr-formula.md` line 93).
   - Replacement: `([former check location, at the base commit](https://github.com/nick-boss-tech/solr/blob/56ec140e3636d5f4150fa87fbf7103529536ac99/solr/core/src/java/org/apache/solr/core/SolrXmlConfig.java#L684-L694))`

Checks passed: the head is correct. `NodeConfig.java` L229 to L231 at the head hold `setupSharedLib();`, `initModules();` and `validateClusterSingletonClasses();`, which matches "What this change does". The method at L240 to L256 matches. The test `testClusterSingletonClassFromSharedLib` at `TestSolrXml.java` L577 to L615 matches the text, and it compiles into `solrHome/lib/classes`. `SolrXmlConfig.fromString` still raises the error, because it builds the `NodeConfig`, and the validation runs in the constructor. The changelog link resolves at the head. The check messages are unchanged.

Optional notes, not blocking:
- Changelog link appears twice: line 18 inside "What this change does" and line 36 at the end. The same defect was fixed in the 17297 draft. Consider deleting line 18.
- The answers rebase 17377 onto 17297, with NodeConfig order `initModules();`, `setupSharedLib();`, `validateClusterSingletonClasses();`. The NodeConfig links move then. Refresh them with the head.
- The package-prefix question is open (`-k3.md` NOTE 20; the answers say it "is not a blocker"). The Limits do not mention it. It is on the owner list.
- The assignment says to state which option shipped. The draft states the shipped route (validation at the end of the NodeConfig constructor), and the answers accept that form. No change needed.

## SOLR-17708

Verdict: DRIFT (3 items).

1. Draft says: "[OWED BEFORE POSTING: the changelog title on the branch overstates the change and needs a fix first. After that edit, update the head references in this draft, and run the three test classes named above again at the new head.]"
   - Evidence: this is process text inside public draft text (brief check 7). The answers put the same work in the owed lists (`reports/core-admin-answers-round-1.md` draft corrections item 5 and main-side work item 8). It belongs in the report, not the draft.
   - Replacement: delete the bracketed line and the blank line after it. Nothing replaces it. Do not post until the branch changelog fix (item 2) lands and the head is refreshed.

2. Changelog says (branch file, in the PR diff): "v2 APIs implemented with JAX-RS are now authorized once per request instead of twice (once by HttpSolrCall and again by SolrRequestAuthorizer)." The draft says: "**One route still checks twice, and the new test covers one API.**" and "That route still runs the first check before Jersey, so a JAX-RS API matched there is still checked twice."
   - Evidence: `V2HttpCall.java` L166 at `10a7fa07a79` sets `ADMIN_OR_REMOTEPROXY`, and the route still runs `shouldAuthorize()`, which the override does not skip (`-k2.md` FIX 7; answers branch correction 1). The changelog title overstates the change, and it contradicts the draft's own Limits.
   - Replacement for `changelog/unreleased/SOLR-17708-jaxrs-single-authorization.yml` line 1 and the folded title lines (`-k2.md` FIX 7; keep `type`, `authors` and `links` unchanged): `title: JAX-RS v2 APIs are authorized once on the local request path instead of twice`

3. Draft says: "For v2 requests, `HttpSolrCall` runs the authorization plugin before the request reaches Jersey ([HttpSolrCall.java](https://github.com/nick-boss-tech/solr/blob/10a7fa07a79ac4b4b52a83d307ee65349d015a23/solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java#L480-L482)). When a JAX-RS resource then matches, `SolrRequestAuthorizer` runs the plugin again for that resource ([SolrRequestAuthorizer.java](https://github.com/nick-boss-tech/solr/blob/10a7fa07a79ac4b4b52a83d307ee65349d015a23/solr/core/src/java/org/apache/solr/jersey/SolrRequestAuthorizer.java#L54))."
   - Evidence: both sentences describe the pre-change double check. The branch changes neither line: its only `HttpSolrCall.java` edit is at L608 (`shouldAuthorize` visibility), and `SolrRequestAuthorizer.java` is not in the branch diff. At the merge-base `9b3a84b1c460981eab09d8ffaef776acc4a184f8`, L480 to L482 is the `if` that calls `shouldAuthorize()`, and L485 is `AuthorizationUtils.authorize(...)`. Pre-change code links the merge-base and says so (`pr-formula.md` line 93).
   - Replacement: `For v2 requests, `HttpSolrCall` runs the authorization plugin before the request reaches Jersey ([HttpSolrCall.java, at the base commit](https://github.com/nick-boss-tech/solr/blob/9b3a84b1c460981eab09d8ffaef776acc4a184f8/solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java#L480-L485)). When a JAX-RS resource then matches, `SolrRequestAuthorizer` runs the plugin again for that resource ([SolrRequestAuthorizer.java, at the base commit](https://github.com/nick-boss-tech/solr/blob/9b3a84b1c460981eab09d8ffaef776acc4a184f8/solr/core/src/java/org/apache/solr/jersey/SolrRequestAuthorizer.java#L54)).`

Checks passed: the head is correct. `V2HttpCall.java` L228 to L232 at the head holds the `shouldAuthorize()` override (see the optional note on the closing brace). `HttpSolrCall.shouldAuthorize()` is `protected` at the head, as the draft says, and nothing else in it changed. `JaxRsSingleAuthorizationTest.java` L47 to L86 matches the two tests, and the assertions (one authorization, 403 on denial) match the text. The test counts (2 of 2, 1 of 1, 9 of 9) and the 2026-10-08 date match `receipts/SOLR-17708.md`. The behavior-change paragraph matches the override, which applies only when `api == null` and the action is ADMIN or PROCESS.

Optional notes, not blocking:
- `V2HttpCall.java` L166 in the Limits is code the branch does not change, and it has the same line at the base. Linking the base is optional.
- The override link L228 to L232 stops one line early. The method closes at L233. Use L228 to L233 if the whole method should show.
- The date "2026-10-08" comes from a takeover-log entry that is not on disk (`-k2.md` notes). Confirm the run date before posting. See the shared date note.
- Plain language: "JAX-RS", "Jersey" and "admin route" are compressed. One gloss each would help a reader who does not know the Solr v2 stack.

## Not done

- No build, test, gate, queue run, commit, push, or GitHub or JIRA write. The `ls-remote` calls were the only remote reads.
- Gate logs, the takeover log and the base failure lines are not on disk. Counts and dates come from the receipts, and nothing was re-run.
- Ticket text for 16725, 16849 and 17708 was not checked against `research/jira-context/`. The "What happens today" ticket claims in those drafts are unverified here. The 17297 and 17377 packets were spot-checked.
- Live PR state was not read. The round reports list no PR for these five heads.
- Changelog titles of 16725 and 16849 were not matched to a draft title line, since the drafts carry none. 16849 has no changelog.
- Landing orders and trial merges are covered by the round reports and were not re-run.
