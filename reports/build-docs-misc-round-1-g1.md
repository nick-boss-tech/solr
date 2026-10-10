# Build, docs and misc round 1, part G1: SOLR-3684 and SOLR-17752

Audit pass only. Nothing was built, compiled, gated or tested. No JIRA call, push, comment or edit. Gate evidence is the receipts' record only; the logs the receipts name were not opened. Condensed by the lead from the subagent's final report. Both drafts are copied in full in `pr-drafts/build-docs/`.

## SOLR-3684 (`solr-3684-submit`, head `663b8ce754b6`): draftable on the record, HOLD

Refs: head matches the live tip and `receipts/SOLR-3684.md:4`. Merge-base `14c7aac0d151` matches the receipt base. Main read `3f5d4c5bf8ac`. Main is 70 commits past `14c7`, and none of the three touched code files changed in that span.

Verdict: the shipped diff is knob-only and the changelog claims exactly the knob. The knob changes no default and does not deliver the remedy the ticket asks for (a lower default). **The round 12 review at this same head says "premise-dead, retire the branch".** The draft says plainly what the change does and does not do. Owner decision 1 comes before anything is posted.

Evidence:
- Shipped diff: 4 files, +54 and -1 (changelog, `JettyConfig.java`, `JettySolrRunner.java`, `TestJettySolrRunner.java`). No `jetty.xml` change.
- `JettySolrRunner.java:108` `THREAD_POOL_MAX_THREADS = 10000`, used at `:207` only as the fallback when `config.maxThreads` is null.
- `JettyConfig.java:45`, `:63`, `:88`, `:110-113`: the new `withMaxThreads(int)` and its field (default null). `Builder.clone()` carries the value.
- Test-only accessor `JettySolrRunner.java:718-721` `getConfiguredMaxThreads()`, package-private.
- Tests `TestJettySolrRunner.java:35` (default is 10000) and `:50` (configured 123). Both call API absent on base, so neither can fail on base.
- Production setting on main: `solr/server/etc/jetty.xml:34`, `solr.jetty.threads.max`, default 10000.
- Ticket: Robert Muir (2012-08-07) asks whether SOLR-683 still needs the 10000 default. The word "deadlock" appears once, in that question. No later comment settles it.

Changelog: `changelog/unreleased/SOLR-3684.yml`, type `added`. Two wording points, not blocking: the title does not say this is test-framework code or that the default is unchanged; the "SOLR-3684:" prefix is rare (4 of 242 unreleased fragments on main use one).

Receipt disagreements:
1. Inventory says "retire candidate [retire call pending]" (`branch-focus-inventory-2026-10-08.md:444`). The receipt (`:9-10`) says gated, pushed and PR wording prepared, and that the log wins. The round 12 review is a third view.
2. The round 12 review (`research/branch-reviews/round-12/SOLR-3684-review.md:8-10, :57`) is not in the receipt: premise-dead, retire, at head `663b8ce754b`.
3. The receipt (`:8`) states as fact that "the 10000 default was chosen for the SOLR-683 deadlock". The ticket has only a hypothesis. The draft uses the hypothesis wording.
4. The receipt (`:8`) cites `design-decisions-open.md` for the Choice wording. The file was not found. The Choice in the draft is the subagent's wording, built from the receipt and round 5. The exact record text must be pasted in before posting.
5. The receipt dates the gate and the DONE entry 2026-10-03. The head commit is authored 2026-10-04 01:24 +0000. May be local time; confirm.

Owner decisions:
1. Submit or retire. (a) Open the knob-only PR with the draft, whose Limits say the change does not deliver the ticket's remedy. (b) Retire the branch, as round 12 and the inventory suggest. Recommendation: (b), unless a planned test needs the knob.
2. Record label: set one status in the inventory row and the takeover log, matching decision 1.
3. Title and prefix: if (a), use a revised title and drop "SOLR-3684:". Recommendation: yes.
4. Choice wording: confirm the design record's exact text before posting.

Not checked: no build, compile, gate or test. `g3684-harden2.log` was not opened. The SOLR-683 ticket text is not in the packet. Whether `close()` on an unstarted runner (used by the new tests) is harmless (round 5 flagged it, unverified). The Gradle module (round 12 says `:solr:test-framework:test`) is not confirmed.

## SOLR-17752 (`solr-17752-submit`, head `e5c0a64f993c`): draftable, WAITING on owner decision 1 (the base)

Refs: head matches the live tip and `receipts/SOLR-17752.md:4`. **The merge-base is `56ec140e3636`, not the receipt's base `14c7aac0d151`** (`receipts/SOLR-17752.md:7`). 56ec is 81 commits behind main; 14c7 is 70 behind and descends from 56ec. Main read `3f5d4c5bf8ac`. None of the three touched files changed between 56ec and main, so the diff applies cleanly.

Verdict: the diff is three ticket-scoped files; the changelog title matches the final warning; the receipt records a green gate at the live head (not re-run here). Before opening, settle the branch base and the receipt's date and "plain push" wording.

Evidence:
- Diff: `changelog/unreleased/SOLR-17752.yml` (new), `SolrResponseUtil.java` (+20 and -1), `TestShardResponseLogging.java` (new, 73 lines). No handoff file in the diff.
- The only corrupted-response log site on main is `SolrResponseUtil.java:55`. The head warning at `:58-61` prints the node (or shard address) and `responseKeyNames(response)`. Main printed the shard request and the whole response; both are gone.
- Exception at `:62-69` is unchanged. Helper `:84-94` returns top-level key names only.
- Echo path on main: `SolrCore.java:3004` (EXPLICIT) and `:3006` (ALL). The `_default` configset sets `explicit` on `/select` (`solrconfig.xml:597`) and `/query` (`:605`).
- Test `TestShardResponseLogging.java:34`: one test. On base the message has no node name, so the first failing assertion is the node check at `:69`; the term check at `:70` is not reached. The failure on base therefore is the node check, and the captured message holds the query text in both the request and the response header. The draft's Proof says exactly that.
- Adjacent, not fixed: `SuggestComponent.java:181, 219, 300` (INFO) and `:241` (DEBUG) log request params. Named in the draft's Limits.

Changelog: title matches the head. Type `fixed`. No SOLR- prefix, which matches the convention. An optional shorter title: "The corrupted shard response warning no longer logs the shard request or the whole response".

Receipt disagreements:
1. `receipts/SOLR-17752.md:4` says "plain push from a49a4a35502, 2026-10-04". On the branch, `a49a4a35502` is dated 2026-10-03 07:40 -0400, and the head and `eb10580` are dated 2026-10-05 04:34 +0000. The wording does not match the history. The receipt gives no gate run time and no tree hash.
2. `receipts/SOLR-17752.md:7` gives production base `14c7aac0d15`; the branch merge-base is `56ec140e3636`. The receipt does not say which base the head gate ran on.
3. `receipts/SOLR-17752.md:8`: the "final extended test passes at the head" run does not name the head it used. Minor.
4. `receipts/SOLR-17752.md:5`: ":solr:core:check -x test passes" skips tests. The only test evidence is line 6 (1 of 1). The Proof should be read that way.

Owner decisions:
1. Base. (a) Open as is, since the three files have no commits since 56ec, but the gate's base is unstated. (b) Rebase the submit branch onto current main and rerun the focused test before opening. A rebase of a published branch needs the owner's direction. Recommendation: (b).
2. Changelog title: keep the current accurate title or use the shorter one. Not blocking.
3. SuggestComponent logs: (a) follow-up on request, as the draft's Limits say; (b) fold into this PR. Recommendation: (a), so this PR stays on the one warning.

Not checked: no build, compile, gate or test. The `g17752-*.log` files were not opened. The base the head gate ran on is not stated. Runtime echoParams behavior was read in code only. SOLR-16155 (cited in the ticket text) was not read.
