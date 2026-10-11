# Streaming expressions and SolrJ draft fidelity, slice S1

Worktree: `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-suggester`, HEAD `3576773b2f8f10e6f7d3d13f42071a988efb86c5` (origin tip, per the lead). The brief's claim commit e84522fa5bc was not used.

Claims and assignments named in the round roll-ups, not re-read: streaming claim 90052cb497e and assignment f55404e1127; SolrJ claim 5bbcedf2d1e and assignment dd6c4c7e397.

Drafts checked: `pr-drafts/streaming/SOLR-10322.md`, `SOLR-12505.md`, `SOLR-12657.md`, `SOLR-14231.md`, and `pr-drafts/solrj/SOLR-2018.md`. Receipts: `receipts/<TICKET>.md`. Round material: `reports/streaming-expressions-round-1.md` (with -s2, -s3, -s4) and `reports/solrj-clients-round-1.md` (with -s2). `material/` has no answers for these tickets (a grep for 10322, 12505, 12657, 14231 and 2018 found no hits).

Head checks: `git ls-remote origin refs/heads/<branch>` for each draft. Base checks: `git merge-base <head> 8e62c2686882` (upstream main) returns the base SHA each draft names. Every cited commit resolves (`cat-file --batch-check`). The 10322 gated-to-tip diff (`0523a10639` to `80ce9d7a`) changes only the changelog fragment. No fetch was needed.

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-10322 | 80ce9d7a3c8a61dfb5c5169a371890f11d9e17ff (`solr-10322-submit`), matches | DRIFT (1 item) |
| SOLR-12505 | 5f20e1171bc890509d76e4f5f7defcc4fc8c102c (`solr-12505-submit`), matches | DRIFT (2 items) |
| SOLR-12657 | 16a0a69e8398079cceb4bec4bd7e0f6bcf02c469 (`solr-12657-submit`), matches | DRIFT (1 item); HELD in the round report |
| SOLR-14231 | 2f73d00a3996da1623156ccef649b59be0b59d45 (`solr-14231-submit`), matches | DRIFT (1 item) |
| SOLR-2018 | 213457aa91fd6d4809094d80dd9c8d312c0a6d3b (`solr-2018-submit`), matches | CONSISTENT |

Shared checks, all five drafts:
- Em dashes: none (Grep count 0).
- Changelog link: points at the head SHA, and the file exists there. Each title matches the draft's "What happens today" headline. The drafts have no title line, so the changelog title is the reference.
- Verification date: present in 10322 (2026-10-06), 12505 (2026-10-04), 12657 (2026-10-07) and 2018 (2026-10-06). Missing in 14231.
- Internal vocabulary: only "seed" in 10322 (item 1). No other hits for gate, receipt, ledger, claim, pool, assignment, subagent, round or run identifiers.

## SOLR-10322

Verdict: DRIFT (1 item).

1. Draft says: "Same commit and seed, with only `TopicStream.java` reverted to the base code: the class runs 34 tests with one failure."
   - Evidence: `receipts/SOLR-10322.md` line 7 says "Proof (log g10322-premise2.log, same seed)" and gives no commit for that run. `reports/streaming-expressions-round-1.md` line 78 and `-s3` open point 2 flag the same gap. The log is not under `research/` (Glob for `g10322*` found nothing). "Seed" is internal vocabulary (brief check 7).
   - Replacement: "Same randomized test settings, with only `TopicStream.java` reverted to the base code: the class runs 34 tests with one failure."
   - Restore "at the gated commit" only after the lead reads the header of `g10322-premise2.log`.

Optional notes, not blocking:
- Round owner decision 4 (a Choice on fail-fast versus the own-cache route) is open. The draft has no Choice section, which matches the round default.
- Citations checked at head and base: TopicStream head L297-L301, L352-L353, L427, L485, L541-L545, L552-L555; test L2639-L2661 (testTopicStream also uses `Assume.assumeTrue(!useAlias)`); base TopicStream L486 and L550; base SolrClientCache L150 (basicAuthCredentials is null by default); base StreamHandler L111-L112 and L233. All match the text.
- The 401 is stated as not addressed, as the round report asks. "What happens today" matches the call site (`getPersistedCheckpoints` runs when the stream has no checkpoints).
- Plain language: "aliasing", "per-slice query", "checkpoints" are compressed. Length is 5,198 characters with links (over the guide).

## SOLR-12505

Verdict: DRIFT (2 items).

1. Draft says: "**Covered: fetch batches on a handler with a non-lucene default. Not covered: other default parsers, and the realtime get endpoint.**"
   - Evidence: the draft's own summary (line 15) says the prefix "is read whatever the handler's default is". At `5f20e1171bc8` the batch sets `defType=lucene` on every request (`solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/FetchStream.java` L247-L249). So no default parser leaves the batch unprotected. The test uses edismax only (draft line 23; `receipts/SOLR-12505.md` line 8). The Limits line contradicts the change.
   - Replacement: "**Covered: fetch batches on a handler with any non-lucene default. The test uses edismax only. Not covered: the realtime get endpoint.**"

2. Draft says: "The ticket reporter found that setting the `/select` default to lucene fixed it."
   - Evidence: `research/jira-context/SOLR-12505.json` line 21 "Reporter": "Dariusz Wojtas"; line 22 "Assignee": "Eric Pugh". Comment 17429032 (lines 110-114, author Eric Pugh): "I just tested my setup by remvoing the defType=edismax, and it worked like a champ!"
   - Replacement: "The ticket's assignee, Eric Pugh, found that removing `defType=edismax` from the `/select` handler fixed it."

Optional notes, not blocking:
- The Choice section (`defType=lucene` versus `defType=terms`) is live. The ticket supports both: Bernstein's proposal (16519604), Smiley's +1 (16519637) and Smiley's terms suggestion (16519475). The plugin lines (TermsQParserPlugin.java L74, L150) match. Round owner decision 3 (keep or cut) is open.
- Round `-s4` item 3 and owner decision 2 are open. The `on=` field name is written unescaped into `{! df=... }` at FetchStream.java L239 (head). The draft omits it on purpose. This is not a drift at this head, but the round report says to decide before opening: escape the name with a test, add a Limits sentence, or check deployment first.
- "The realtime get endpoint ... works only when the join field is the uniqueKey" is in neither the receipt nor the round report, and I did not check it against code. Keep or cut.
- The branch is 66 commits behind upstream main (round `-s4` item 5). Not in the draft.
- Verified: QParser.java L387-L396 (head); FetchStream base L239-L246, which match upstream main 8e62c2686882 at the same lines; StreamDecoratorTest head L946-L1006 (method starts L951); Joel and Smiley attributions.
- Plain language: "local params", "qparser", "`{! df=...}`" are compressed.

## SOLR-12657

Verdict: DRIFT (1 item). The round report HOLDS this draft for an owner ruling (`reports/streaming-expressions-round-1.md` line 20 and owner decision 1). The draft matches option (a), ship with the Limits paragraph. The report recommends (b), fix and re-gate. No ruling is in `material/`. Do not open before the ruling.

1. Draft says: "keep the smallest or largest string they see."
   - Evidence: at `16a0a69e`, MinMetric.java L71-L74 is `getValue()` returning `stringMin`, and MaxMetric.java L65-L68 is `getValue()` returning `stringMax`. The lines that keep the smallest or largest string are MinMetric.java L105-L112 (`s.compareTo(stringMin) < 0`) and MaxMetric.java L104-L111 (`s.compareTo(stringMax) > 0`). Round `-s4` cites the same ranges.
   - Replacement (replaces the first sentence of line 16, keeping the rest): "[MinMetric.java](https://github.com/nick-boss-tech/solr/blob/16a0a69e8398079cceb4bec4bd7e0f6bcf02c469/solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/metrics/MinMetric.java#L105-L112) and [MaxMetric.java](https://github.com/nick-boss-tech/solr/blob/16a0a69e8398079cceb4bec4bd7e0f6bcf02c469/solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/metrics/MaxMetric.java#L104-L111) keep the smallest or largest string they see."

Optional notes, not blocking:
- Round `-s4` item 2: callers of `Metric.getValue()` on other branches were not checked. The draft states the API change and says nothing about callers.
- The Limits claim "The direct facet path takes min and max from Solr" is not in the receipt or round report and has no citation. I read `FacetStream.fillTuples` but did not trace the direct path further.
- "Solr writes only the fractional digits a value needs" has no link. Round `-s4` cites DatePointField.java L249 on upstream main. A link would help.
- Verified: FacetStream base L951 (same line on upstream main); head L953-L958; Metric.java L75 (`public abstract Object getValue();`); StreamExpressionTest head L1421-L1530; receipt counts (35 of 35; premises A and B) and the "ClassCastException" wording (ticket text: "cannot be cast").
- Plain language: "rollup" is compressed; the draft explains it in parentheses.

## SOLR-14231

Verdict: DRIFT (1 item).

1. Draft says: "**The new tests pass at head 2f73d00a3996da1623156ccef649b59be0b59d45. The base code was not run against them.**"
   - Evidence: `receipts/SOLR-14231.md` line 8 dates the counts only as "reconcile gate, 2026-10-04 era; head verified 2026-10-10". The 2026-10-10 date is the ls-remote check, not the count run. Round `streaming-expressions-round-1.md` line 23 and `-s2` open point 3 list the date as open. No 14231 gate log turned up under `research/`. `pr-formula.md` section 3 requires "verified <date> at this head".
   - Replacement (paste once the lead confirms the count date; the receipt supports only "2026-10-04 era"): "**The new tests pass at head 2f73d00a3996da1623156ccef649b59be0b59d45, verified 2026-10-04. The base code was not run against them.**"

Optional notes, not blocking:
- The changelog title (`changelog/unreleased/SOLR-14231.yml` at head) says the stream "no longer fails when it receives a single node or no nodes". The draft's "What happens today" describes only a request with no field and no collection, and names no failure. The ticket text is missing (`research/jira-context/SOLR-14231.json` does not exist), so the failure cannot be checked against the ticket (`-s2` item 1). Either the draft shows a failure that a ticket or base run supports, or the changelog drops "fails" (a branch commit, which moves the head).
- No fail-without-fix run. The draft says so honestly. Round owner or main-side item: a base run, or a decision that none is owed.
- No Choice section. No real live alternative found (`-s2` open point 4).
- Verified: ScoreNodesStream base L214-L218 and L233; head L218-L219, L223-L226, L235-L237; GraphExpressionTest head L844-L851 and L853-L928; ScoreNodesStreamTest head L46-L89 (new file, absent at merge-base); counts 8 of 8 and 1 of 1 match the receipt.
- Length: about 3,953 characters with links (over the guide, `-s2`).
- Plain language: "gather", "graph walk", "/terms request" are compressed.

## SOLR-2018

Verdict: CONSISTENT.

Checks:
- Head `213457aa91fd` is the live tip. The merge-base with upstream main is `cabedd1d968`, the base the draft names.
- `git diff cabedd1d 213457aa` touches three files: the changelog (8 lines), `stream-decorator-reference.adoc` (one line, L446), and `SolrClient.java` (8 `@param waitFlush` lines at L454, L477, L499, L525, L579, L597, L616, L638). `diff -U0` on SolrClient shows only comment lines, so "No signature or code changes" holds. UpdateRequest.java is untouched.
- Base citations match: SolrClient.java L454 (javadoc "block until index changes are flushed to disk"); AbstractUpdateRequest.java L53-L64 (sets commit, optimize, soft commit and waitSearcher, never waitFlush); CommitStream.java L294; RequestHandlerUtils.java L50 (comment only).
- Head citations match: SolrClient.java L409, L429, L543, L560 (the "waitFlush=true ... inline with the defaults" notes); the ref guide L446 ("Solr ignores this option.").
- No server code reads waitFlush at base (git grep in `solr/core`, `solr/modules` and the solrj common package finds only the RequestHandlerUtils comment).
- Receipt counts (TestUpdateRequestCodec 4 of 4) and date (2026-10-06) match. The changelog at head exists, and its title matches the draft's "The branch documents that waitFlush is ignored."
- The JIRA summary (`research/jira-context/SOLR-2018.json`) says Solr does not pay attention to waitFlush, as the draft says.

Optional notes, not blocking:
- Receipt error, main side (not a draft defect): `receipts/SOLR-2018.md` line 6 says the change is "one javadoc clarification ... in UpdateRequest". The diff is in SolrClient.java, as the draft says. The round roll-up lists this correction.
- Ref guide L446 still reads "The value passed to the commit handler (true/false, default: false). Solr ignores this option." The value is not passed on (`-s2` item 2; round owner decision 10). The draft does not quote that clause. Amend on the branch (a commit, which moves the head) or accept.
- The four SolrClient notes and the RequestHandlerUtils comment are named in Limits, as `-s2` recommends.
- Proof is stated as inconclusive by construction, as the formula asks.
- Length: about 3,783 characters with links (over the guide).

## Not done

- No build, test, Gradle, test-queue, `gh` write, JIRA access, commit, push or edit. Only read-only git (`ls-remote`, `cat-file`, `merge-base`, `diff`, `show`, `grep`), Grep, Glob and Read were used.
- The gate and premise logs (g10322-premise2.log, g10322-gate.log, g12505-premise.log, the g12657 logs, the g14231 gate, g2018-gate.log) are not in the worktree or under `research/`. Counts and dates rest on the receipts.
- Live CI runs and GitHub PR state were not checked. Mergeability and rebase state were not rechecked (the round reports list a rebase before opening).
- `Metric.getValue()` callers on other branches were not checked.
- The claim commit e84522fa5bc and the assignment files were not read.
- For SOLR-14231 no ticket text exists in the workspace, so the "fails" wording is unverified.
