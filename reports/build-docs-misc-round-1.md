# Build, docs and misc round 1: roll-up

Claim: `claims/build-docs-misc-round-1.md` (`085584e3e736`). Assignment: `assignments/build-docs-misc-round-1.md` (`0693fb6d72a`). Part reports: `reports/build-docs-misc-round-1-g1.md` through `-g6.md`. Drafts: `pr-drafts/build-docs/`.

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Six subagents ran in parallel, one per cluster, within the cap of six. No other round ran during this one. The part reports are condensed by the lead from the subagents' final reports.

Nothing has run. No build, compile, spotless, Gradle run, test, documentation build, Antora run, beast run or macOS run was performed by this review side. No PR, comment, Jira write, branch edit or live PR description edit was made. The GitHub reads were read-only (`gh pr list` and `gh pr view`).

Heads: all twenty live tips were fetched explicitly and matched the claim's table. SOLR-17722's inventory head `e5c0a64f993` is SOLR-17752's head; the ticket's tip is `fee3a26beb3c`.

## Cross-cutting: evidence the receipts name but the workspace does not hold

The receipts name these logs. None was found under the workspace paths searched, so every Proof that rests on them rests on the receipt's wording alone:
- `g5821-refguide.log`, `g5821-refguide2.log` (SOLR-5821 Antora build)
- `g16914-lightgate.log`, `g16914-antora.log` (SOLR-16914 tidy and Antora)
- `g3684-harden2.log` (SOLR-3684 gate; not under `research/test-queue/logs`)
- the `g17752-*.log` files (SOLR-17752 gate; not under `research/test-queue/logs`)
- `g17252r35-harness.log` (SOLR-17252, 21 checks)
- `g17842-lightgate.log` (SOLR-17842 tidy)

Before any draft's Proof is posted, the main side must open these logs and confirm the wording. This is main-side work and is not repeated per ticket below.

Two receipt dates to confirm: SOLR-3684's gate date (2026-10-03) against the head commit's author date (2026-10-04 01:24 +0000); SOLR-17752's "plain push" date (2026-10-04) against the branch's history.

## Per-ticket verdicts

| Ticket | Branch (head) | Verdict | Draft |
|---|---|---|---|
| SOLR-3684 | `solr-3684-submit` (`663b8ce754b6`) | Draftable, but the round 12 review at this head says **retire** (premise-dead). The shipped change is a knob that changes no default. | `SOLR-3684.md`, **HOLD** |
| SOLR-5821 | `solr-5821-submit` (`b8af2d1ce2f5`) | Draftable (docs-only; documentation build per receipt). The three sentences agree with the guide. Document counts stay in Limits. | `SOLR-5821.md`, ready after owner items 1 to 4 |
| SOLR-16914 | `solr-16914-submit` (`cd878023d3d0`) | Draftable, narrow claim. One statement (non-linear stream with `discardCompoundToken=false`) is unconfirmed. The mode row "Default: none" contradicts the branch's own text. | `SOLR-16914.md`, stream check recommended before posting |
| SOLR-17252 | `solr-17252-submit` (`d730a266a042`) | Draftable. The premise holds on main. The receipt's Q3 placement is wrong: the check runs after `state.save()`. | `SOLR-17252.md`, **HOLD** (owner items 1 to 3) |
| SOLR-17752 | `solr-17752-submit` (`e5c0a64f993c`) | Draftable. The merge-base is `56ec140e3636`, not the receipt's `14c7aac0d151`. The diff applies cleanly. | `SOLR-17752.md`, **WAITING** on the base |
| SOLR-17842 | `solr-17842-submit` (`008973f63133`) | Draftable as instructions only. The existence claims hold on main. **The branch's example has an unquoted `<build>` placeholder that bash reads as a redirect.** Fixing it is a branch commit this review cannot make. | `SOLR-17842.md`, **WAITING** on the scope call and the placeholder fix |
| SOLR-6430 | `solr-6430-submit` (`22f83870c4a0`) | Audit: premise accurate on main (missing numeric and date sort values are 0; strings sort missing first). No draft. | none |
| SOLR-7119 | `solr-7119-submit` (`9593f4bd0d63`) | Audit: accurate for the page's wording. Exclusion works on `facet.interval` and fails only on `facet.interval.set`. The receipt overstates the ticket's claim. No draft. | none |
| SOLR-11700 | `solr-11700-submit` (`c513388be053`) | Audit: the corrected positions are accurate on Lucene 10.4.0 and 9.12.3 for the two catenate examples. `catenateAll` is unpinned, and the NOTE's "spans" is unsettled. The same wrong text sits on main, branch_10x and branch_9x. No draft. | none |
| SOLR-17356 | `solr-17356-submit` (`ea7fc15cade4`) | Audit: **premise wrong in part.** The default Ukrainian dictionary is already on analysis-extras' runtime classpath at `ua/net/nlp/ukrainian.dict`. The new `org/languagetool/...` path needs a jar not in the build. The class name `solr.MorphologikFilterFactory` at line 3463 is wrong. No draft. | none |
| SOLR-9039 | `solr-9039-submit` (`13faf68fe858`) | Premise cannot be settled by reading; **parked** until one macOS run exists. Off macOS the change is a no-op. | none |
| SOLR-13705 | `solr-13705-submit` (`5f141fb2af38`) | The narrow premise (non-volatile field) holds. The broad premise (a defect) does not hold by reading: the object's only field is final. The register's "known DCL sites are volatile" is contradicted by `SecurityConfHandler.apis`. Decide scope before any gate. | none |
| SOLR-16322 | `solr-16322-submit` (`65e0b8d7c792`) | The premise holds by reading. The change also runs on **ordinary failing builds**, not only beast. An exercised failing run is owed before any PR. | none |
| SOLR-17722 | `solr-17722-submit` (`fee3a26beb3c`) | **Premise fails by reading**: SolrJ overwrites `wt` with javabin on the default path, so the mirrored `wt=json` never reaches the secondary. Not draftable. A wire check is owed. | none |
| SOLR-18119 (live) | `solr-18119-jvm` (`660faedd026d`), PR #5061 | Consistent with the record: tip, diff shape (10 files, +1172 and -895), 14 tests, changelog. The PR's Proof heading "Same output" contradicts its body (the outputs differ in one line). | none (consistency only) |
| SOLR-18523 (live) | `solr-18523-submit` (`26678c3737ca`), PR #5062 | Consistent with the record. The receipt omits a behavior change: the local `:solr:documentation:check` no longer builds the site outside CI. The PR text uses "gate" as a process word and links an internal review file. | none (consistency only) |
| SOLR-18119 (retire) | `solr-18119-submit` (`723022d35fce`), PR #4999 | Retire confirmed as superseded on the record. **The PR is still open with changes requested, and #5034 is based on it.** Closing it is public and was not done. | none |
| SOLR-18317 (retire) | `solr-18317-submit` (`fadbaee999f1`), PR #5001 MERGED | Merged state confirmed. Jira SOLR-18317 is still Open. Keep the branch until the server-side variant is decided. | none |
| SOLR-12743 (retire) | `solr-12743-submit` (`1bb4b227dfe5`) | Retire confirmed. `ConcurrentLRUCache` is absent on main (deleted by #4516, Caffeine). No PR. | none |
| SOLR-17825 (retire) | `solr-17825-submit` (`0ef08ec86c65`) | Retire confirmed. No beanutils declaration exists on main. **The receipt's cause is wrong:** the Kafka 4 upgrade removed kafka213 and streams, not beanutils. | none |

## Drafts written (six)

- `pr-drafts/build-docs/SOLR-5821.md`: draftable. Owner items 1 to 4 are open but do not block it.
- `pr-drafts/build-docs/SOLR-16914.md`: draftable. The Limits line covers the unconfirmed non-linear statement; a stream check is recommended first.
- `pr-drafts/build-docs/SOLR-3684.md`: **HOLD**. Submit or retire is open, and the round 12 review says retire.
- `pr-drafts/build-docs/SOLR-17752.md`: **WAITING** on the branch base.
- `pr-drafts/build-docs/SOLR-17252.md`: **HOLD** on the placement (Q3), the emacs list (Q2) and the exit-or-warning choice (Q1).
- `pr-drafts/build-docs/SOLR-17842.md`: **WAITING** on the scope call and the branch's placeholder fix.

The SOLR-17752 Proof was tightened to name the failing assertion on base (the node-name check at line 69), not a general "fails". The SOLR-17252 Choice now names the warn-only and tmux options raised on the ticket.

## Receipt disagreements (consolidated)

1. SOLR-3684: the inventory calls it "retire candidate, call pending"; the receipt says gated and prepared for a PR; the round 12 review says premise-dead, retire. Three records, one head. The receipt also states a cause ("chosen for the SOLR-683 deadlock") as fact; the ticket has only a hypothesis. The Choice's source (`design-decisions-open.md`) was not found.
2. SOLR-17752: the receipt's base `14c7aac0d151` differs from the branch's merge-base `56ec140e3636`; the receipt does not say which base the head gate ran on. The "plain push" date does not match the history.
3. SOLR-17252: the receipt's Q3 says the check stays "at startup, before release state exists". The shipped check runs after `state.save()` at `releaseWizard.py:1392`.
4. SOLR-17252: the receipt says the docs "match the implemented behavior". The docs name the five editors but do not say GUI emacs is refused.
5. SOLR-5821 and SOLR-16914: the Proof sections rest on logs not in the workspace (see cross-cutting).
6. SOLR-7119: the receipt overstates the ticket's claim (exclusion works on `facet.interval`).
7. SOLR-13705: the register says the known DCL sites are volatile; `SecurityConfHandler.java:284` is not.
8. SOLR-17722: the receipt's quoted register line was not found; the inventory label "premise-dead" is ahead of the evidence (no wire run).
9. SOLR-17825: the cause (the Kafka 4 upgrade removed beanutils) is wrong; it removed kafka213 and streams.
10. SOLR-18119 (jvm): the receipt names two files for the build-infra pair; build-infra holds seven. The receipt calls the review "complete"; GitHub shows no review.
11. SOLR-18119 (submit): the receipt's verification method ("clean root project configuration run") differs from the PR body's task runs; the receipt omits that #5034 depends on it.
12. SOLR-18523: the receipt omits the CI-only behavior change; the 185,623 measurement is cited in #5061, not in this PR.
13. SOLR-18317: the receipt does not record that the Jira ticket is still Open.

## Owner decisions (consolidated)

Submit or retire:
1. SOLR-3684: submit the knob-only PR, or retire (round 12 and the inventory suggest retire). Recommendation: retire, unless a planned test needs the knob.
2. SOLR-17722: retire on the reading, or run the wire check first. Recommendation: run the wire check, then retire.
3. SOLR-9039: keep parked until a macOS run exists; if no macOS runner is available, retire. Do not submit on the 2019 comment.
4. SOLR-13705: close as not reproduced and retire; or a narrow hardening change (Linux focused run, described as hardening); or widen to `SecurityConfHandler.apis`. Decide scope before any gate.
5. SOLR-12743 and SOLR-17825: delete the banked branches (agents do not delete branches).
6. SOLR-18317: keep `solr-18317-submit` until the server-side variant is decided.
7. SOLR-18119: close `solr-18119-submit` and PR #4999 now, or hold until the maintainer route is settled. Recommendation: hold, since #5034 is based on it.

Drafts and scope:
8. SOLR-17252: placement (Q3). Move the check into `check_prerequisites()`, before `ReleaseState`. Recommendation: move; it needs a new head and a rewritten "What this change does". Also Q2 (keep emacs in the list plus one docs sentence) and Q1 (exit, warn-only or tmux).
9. SOLR-17752: the branch base. Rebase onto current main and rerun the focused test, or open as is. Recommendation: rebase (needs direction; rebasing a published branch is the owner's call).
10. SOLR-17842: keep the ticket open (recommended), or narrow it. The placeholder fix needs a branch commit by the branch's owner.
11. SOLR-5821: the changelog (none, recommended); the document-count half (keep open and link SOLR-4260, recommended); the searcher wording (plain words, recommended).
12. SOLR-16914: the claim stays narrow; the mode row fix (recommended if unposted); the token stream check (recommended before posting).

Audit-only tickets:
13. SOLR-6430: keep the sentence; drop the changelog (recommended).
14. SOLR-7119: docs only; drop the changelog (recommended).
15. SOLR-11700: correct the catenate examples now and hold `catenateAll`; drop the NOTE's "spans" unless checked; mirror to branch_10x and branch_9x (recommended).
16. SOLR-17356: the dictionary path in the examples (`ua/net/nlp/ukrainian.dict` is recommended; the `-lt` dependency is a separate decision); fix the class name at line 3463 in the same change; the changelog (drop, recommended).
17. SOLR-16322: run the exercised verification (including a non-beast failing run and a passing build), then draft (recommended), or retire.
18. SOLR-18119 (jvm): the converter's home (keep in build-infra, recommended); the "Same output" heading fix needs authorization; the changelog (keep).
19. SOLR-18523: the local-check change (keep as written, recommended, recorded as a behavior change); the anchor follow-up (name a plan, recommended); the public text edits need authorization.
20. SOLR-18317 Jira: leave Open; no comment without the owner's go-ahead.

## Main-side work owed

- Open the logs named by the receipts (see cross-cutting) before any draft's Proof is posted.
- Documentation builds: SOLR-6430, SOLR-7119, SOLR-11700, SOLR-17356, SOLR-16914 (main side), and a whole-site build of SOLR-5821 on current main.
- Focused analysis runs: SOLR-11700 `catenateAll` on main and branch_9x; SOLR-16914 token stream check; SOLR-7119 set-level negative case; SOLR-17356 dictionary path check.
- Failing-run exercise: SOLR-16322 (beast with and without a user seed, a non-beast failing test, a passing build, and the GString call on the repo's Gradle version).
- Wire-level check: SOLR-17722 (a SolrJ test that sends `wt=json` and asserts `wt=javabin` on the query string).
- macOS run: SOLR-9039 (forced clientAuth on base and head, then beast).
- SOLR-13705: a focused Linux run on base and head, only if scope (b) or (c) is chosen.
- SOLR-17752: rebase onto current main and a focused rerun, if the owner chooses that.
- SOLR-3684: the focused test, only if submit is chosen.
- SOLR-17356: fetch and list `morfologik-ukrainian-lt` before naming its path; check the 9.12.3 morfologik jar before any backport.
- Jira check: the 2026-10-08 Jan Høydahl quote on PR #5061 is not in the packet (check before reuse).

## Not done

- Nothing was built, compiled, tested, run or gated by this review side.
- No PR, comment, Jira write, branch edit or live PR description edit.
- Drafts are local only. Nothing is ready to post until the owner items and the log checks above are closed.
- The part reports are condensed by the lead; the subagents' full text is not reproduced.
