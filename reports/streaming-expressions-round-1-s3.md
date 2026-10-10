# Streaming expressions round 1, part S3: SOLR-10322 draft

Result: the draft is written at `pr-drafts/streaming/SOLR-10322.md`. Verdict: draftable with the disclosed head. The live tip is `80ce9d7a3c8a61dfb5c5169a371890f11d9e17ff`. Its diff from the gated `0523a10639dd9a74dfca559503324ebf74103464` touches only the changelog fragment, as the receipt says. The Proof names the tip, gives the receipt's counts with the gated commit disclosed, and claims no end-to-end reproduction.

The premise check narrows the PR. The change matters only when the context has no cache. It adds no credentials to the cache it creates, and the cloud-node path is unchanged. The draft's Limits say plainly that the 401 is not addressed. The PR must not be titled or described as a 401 fix.

Length: 5,198 characters with links, over the roughly 3,500 guide. About 13 full-SHA link targets account for most of that.

## Draft self-check

- Dashes: zero em, zero en.
- Process words: none. Scanned for gate, receipt, ledger, rc=0, JUnit, pre-fix, owed, round, shipcheck, harden, tidy, Error Prone, verify, xml, claude.
- Heads: 9 references to the full live tip, 2 to the full gated SHA (the Proof's counts and disclosure, as required), and 5 base citations to the merge-base `97d973814336101e12475558d7419321c743de79`, labelled "base". No short SHAs.
- Changelog link names the live tip.
- Citation read-back: each cited range was printed from its commit and matches the code it describes (tip `TopicStream` lines 297 to 301, 352 to 353, 427, 485, 541 to 545, 552 to 555; tip test lines 2639 to 2661; base `TopicStream` lines 486 and 550; base `SolrClientCache` line 150; base `StreamHandler` lines 111 to 112 and 233).
- Sections: header, Jira link, What happens today, What this change does, Proof, Limits, changelog, AI assistance. No Choice section (see open point 3).
- The "34 of 34" count and the 2026-10-06 date come from the receipt. The receipt records that the new test fails with one NullPointerException in `getPersistedCheckpoints` without the `TopicStream` change.

## Interaction results

**SOLR-17433 cache (head `42b6c4fc9158`).** Independent of 10322, so no landing order is required for the cache itself. 17433 leaves credential handling unchanged. The zero-argument constructor keeps the same 60-second floors that 10322's `new SolrClientCache()` gets on main, and nothing in `solr/` references the old static floor fields.

One change does reach 10322's path: `withRequestTimeout(Long.MAX_VALUE)` in `newHttpSolrClientBuilder` (17433 head `SolrClientCache.java` line 169). It applies to the stream's own cache, which 10322 uses for checkpoint reads when the context has no cache. On main, that request timeout falls back to the idle-timeout floor (`HttpSolrClient` base lines 597 to 601). So if both ship, the null-context checkpoint read has no request timeout. 10322's test does not exercise timeouts. 17433's own commit touches only `SolrClientCache.java`, its test, and one query-guide line, so there is no file overlap with 10322.

**SOLR-12657 `StreamExpressionTest` overlap.** None, in methods or in lines. 12657 adds two methods (`testFacetStreamMinMaxOnDateField` and `testFacetStreamMinMaxOnDateFieldParallelRollup`) as one 111-line insertion. 10322 adds `testTopicStreamContextWithoutClientCache` as a 24-line insertion elsewhere in the file. Neither deletes lines in this file. A three-way merge in the scratchpad (base `14c7aac0`, the two tips) is clean, with zero conflicts and all three methods present. No landing order is required. Both bases are behind main (10322 by 40 commits, 12657 by 66), so each still needs a rebase, and the second to land should merge cleanly in this file.

## Open points for the lead

1. Confirm the verdict: draftable, with the 401 stated as not addressed. Keep "fixes SOLR-10322" out of the PR title and description.
2. The Proof says "same commit and seed" for the failing run. The receipt gives no commit or date for `g10322-premise2.log`, so the part agent inferred the commit. Check the log header before publishing.
3. No Choice section. A live question exists: fail fast with a clear error when the context has no cache, versus the implemented own-cache route, which carries the credential gap. The receipt does not record it, so it was not added. Add it if the maintainers should be asked.
4. The draft is over the guide at 5,198 characters. Cuts available if wanted: the L427 link, the L352 to 353 link, or the L233 bullet (about 330 characters combined). Each supports a claim, so they were kept.
5. Mergeability: the 10322 base is 40 commits behind `upstream/main` `8e62c2686882`. A rebase onto main would move the head, so the draft's head and disclosure would need updating. Check mergeability before opening.
6. The 17433 commit message says "reconciled onto current main", but its base is 66 commits behind main. This matters for the owner's ship-or-retire call on 17433.
7. Supporting finding for the security follow-up: on a cloud node, the stream's context carries `InternalSolrClientCache`, which calls `super()` and passes no HTTP client up (base `InternalSolrClientCache.java` line 30). Its per-URL checkpoint clients therefore get no credentials (base `SolrClientCache.java` lines 140 to 150). That fits the ticket's 401. Not verified at runtime.
8. The changelog is `SOLR-10322-topic-stream-client-cache.yml`, not the template's `SOLR-<ticket>.yml`. The draft links the actual file. Confirm that is acceptable.

## Not checked

- No builds, Gradle, tests, gate runs, `gh` writes, commits, pushes, or posts.
- The gate counts, the premise run, and the dates come from the receipt. `g10322-gate.log` and `g10322-premise2.log` are not in this worktree and were not read.
- SolrJ's `HttpClientUtil` default builder was not read. If a system-wide default injects credentials in a secured deployment, the "adds no credentials" statement needs revisiting.
- Whether `Long.MAX_VALUE` as a request timeout is safe in the underlying HTTP client (17433) is unchecked.
- Neither branch was compiled, and no merge onto current main was tested. The merge check used only the two bases.
- Main was checked, not the ticket's 6.4.1 code, so the reporter's exact path is not verified.
- Scratch merge files are in the scratchpad, not the repo. Nothing in the repo changed except the new draft and its folder.
