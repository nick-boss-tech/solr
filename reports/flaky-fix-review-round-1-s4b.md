# Flaky-fix review round 1, slice 4, part b: SOLR-16630 receipt check and PR draft

Scope: receipt check (Part A), draft (Part B), and this report (Part C), for fork branch `solr-16630-submit` at `9bea59741ac30ffd11ed11be5269ea025cf17f68` (base upstream main `3f5d4c5bf8ac`). Read-only. No builds, tests, gate or test-queue runs, no fetch, no PR, comment, Jira or branch write.

Pre-check: `git ls-remote origin refs/heads/solr-16630-submit` returns `9bea59741ac30ffd11ed11be5269ea025cf17f68`. Equal to the named head. The parent of the head is `3f5d4c5bf8ac`, the named base.

Verdict: the draft is ready for owner review. It is not ready to post until owner decisions 1, 3, 6 and 7 below are taken. The change matches the primary fix in the t3 report (Part 3, steps (a) to (c)) in the same places, including the two-minute warning text. The secondary catch-widening was not applied, as the t3 report advised.

## Part A: receipt and gate claims

Sources: `receipts/SOLR-16630.md` and `gates/SOLR-16630.md`. "Branch" means checkable from the head commit or its diff.

| # | Claim | Checkable from | Draftable or internal | Use in draft |
|---|---|---|---|---|
| R1 | GATE GREEN at the branch head, finished 2026-10-10 UTC | receipt only | internal wording; outcome in plain words is draftable | "passed six focused runs at this head" |
| R2 | PR-ready; opening needs Nick's approval | receipt only | internal | not used |
| R3 | Gated head `9bea59741ac3` | branch (head commit; origin ls-remote matches) | draftable | links and Proof line |
| R4 | Base upstream main `3f5d4c5bf8` | branch (parent of head) | draftable | not used |
| R5 | Branch name `solr-16630-submit`; pushed to `nick-boss-tech/solr` | branch remote (origin is that repo) | internal name | not used as text |
| R6 | Test-only; `testNRTRestart` keeps PULL up until the add loop's first successful add (CountDownLatch), then stops PULL | branch diff (test L243, L262-L264, L313) | draftable | used. Precision: the latch is released after add and commit (L311-L313), so "first successful add" means add plus commit |
| R7 | Changelog `changelog/unreleased/SOLR-16630.yml` | branch (new file, 10 lines) | draftable | Changelog line |
| R8 | No production change | branch (diff touches the test file and the changelog only) | draftable | evidence in What this change does |
| R9 | Gate steps on vm2: changelog parse, tidy clean tree, Error Prone compile, module check rc=0 | receipt only | internal | not used |
| R10 | Tidy log `solr-16630-tidy.log` | receipt only | internal | not used |
| R11 | Focused runs: CI seed plus five random seeds; each 1 test, 0 failures, 0 errors, rc=0 | receipt only (logs on vm2) | counts and CI seed draftable; rc and log names internal | counts and CI seed used |
| R12 | Log names `solr-16630-localtmp-seed-<seed>.log` | receipt only | internal | not used |
| R13 | The five random seed values | receipt only | internal (reproduction detail) | not used; CI seed named |
| R14 | Environment attempt: work dir on NFS, "Too many links" in all six seeds, recorded ENV and not counted, six seeds rerun with a /tmp work dir | receipt only | internal; process catch under pr-formula section 3; does not change shipped behavior | not used. Must not appear. |
| R15 | Logs under `/workspace/gates/logs/vm2/` | receipt only (not readable by a reader) | internal | not used |
| R16 | No fail-before run | receipt and gate file (no such step in either checklist) | the absence is a limit, draftable | Proof: "no run of this test on the base code is on record" |
| G1 | Module `:solr:core`; class `TestCoordinatorRole`; method `testNRTRestart` | branch (method at L179) | draftable | Proof |
| G2 | Secondary catch-widening not applied | branch (L315 still catches only SolrException) | draftable; statement is accurate | Limits |
| G3 | Full gate with repetition verification (intermittent failure) | gate file only | internal | intermittency stated in Limits, shape not named |
| G4 | First attempt failed all six seeds, ENV "Too many links" | gate file only | internal; must not appear | not used |
| G5 | Claimed by vm2, runner and log paths, step checklist | gate file only | internal | not used |

Claims the draft takes from outside the receipt:

| # | Claim | Source | Draftable? | Use |
|---|---|---|---|---|
| X1 | Failure on record: `client.add` threw `SolrServerException` caused by `ClosedChannelException`, after the NRT restart | t3 report Part 2, quoting the assignment; not in branch or receipt | draftable at this level, "on record" wording | What happens today. A reader cannot check it until a CI log is linked (decision 6). |
| X2 | Mechanism: fixed-timer stop of PULL closes the client the loop still uses (likely, not proven) | branch code (test L222, L242, L261-L266, L315; JettySolrRunner L616-L617, L847-L852) plus t3 reasoning | draftable with "likely, not proven" | What happens today |
| X3 | Ticket SOLR-16630 is reopened, not new | assignment text only; the workspace Jira CSV export has no SOLR-16630 row | draftable, not verified against Jira | framing sentence in What happens today (decision 7) |
| X4 | Stale pooled connection (t3 H2) not addressed | t3 report | draftable as a limit | Limits; percentage omitted |

Omitted on purpose: the 70 percent and 10 percent confidence figures from the t3 report, the 4 second wait, and all internal labels.

## Proof-numbers check

| Number in draft | Source | Result |
|---|---|---|
| six focused runs | receipt line 7 (CI seed plus five random seeds); gate checklist steps 1 to 6 | match |
| CI seed `681E2A715B2CE1D3` | receipt line 7; gate file | match |
| 1 test, 0 failures, 0 errors per run | receipt line 7 | match |
| head `9bea59741ac30ffd11ed11be5269ea025cf17f68` | receipt line 4; gate file; origin ls-remote | match |
| "Run on 2026-10-10 (UTC)" | receipt: gate "finished 2026-10-10 UTC" | matches the finish date only; per-run dates are not on record |
| test class and method | gate file line 5; branch | match |
| "no run of this test on the base code is on record" | receipt has no base run and no fail-before step | match |
| "two minutes" | branch L262 and t3 Part 3; not a receipt number | from code; see decision 8 |

Result: no proof number in the draft is outside the receipt, except the two-minute constant, which is a code value and is named as such in decision 8.

## Citation check

Every link points at `https://github.com/nick-boss-tech/solr/blob/9bea59741ac30ffd11ed11be5269ea025cf17f68/<path>#L<a>-L<b>`. The lines were printed from `git show 9bea59741ac3:<path>` and checked with `sed -n`. All 11 unique targets (12 links with one repeated) resolve to the head SHA and contain the claimed code.

| File | Lines | Code at those lines | Supports | Contains claim |
|---|---|---|---|---|
| `solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java` | L222-L222 | `SolrClient client = pullJetty.getSolrClient();` | the add loop uses the PULL client | yes |
| same | L242-L242 | `final long pullServiceTimeMs = 1000 + (long) r.nextInt(9000);` | fixed delay chosen at random at start | yes |
| same | L243-L243 | `CountDownLatch addDone = new CountDownLatch(1);` | the latch | yes |
| same | L261-L266 | sleep, await block, log line, `pullJettyF.stop();` | stop follows the delay | yes |
| same | L262-L264 | `if (!addDone.await(2, TimeUnit.MINUTES)) { log.warn(...) }` | two-minute wait and warning | yes |
| same | L311-L313 | `client.add(COLL, d);` / `client.commit(COLL);` / `addDone.countDown();` | latch released after add and commit | yes |
| same | L315-L315 | `} catch (SolrException ex) {` | loop catches only SolrException (used twice) | yes |
| `solr/solrj/src/java/org/apache/solr/client/solrj/SolrServerException.java` | L24-L24 | `public class SolrServerException extends Exception {` | not a SolrException | yes |
| `solr/test-framework/src/java/org/apache/solr/embedded/JettySolrRunner.java` | L616-L617 | `IOUtils.closeQuietly(jettySolrClient);` / `jettySolrClient = null;` | stop closes the cached client | yes |
| same | L847-L852 | `getSolrClient()` returns the cached `jettySolrClient` | the client the test holds is that cache | yes |
| `changelog/unreleased/SOLR-16630.yml` | L1-L10 | whole file (10 lines), title, `type: fixed` | Changelog line | yes |

The t3 report's line numbers are for upstream `8e62c2686882`, not for this base. They are off by one or two lines at the head. The draft uses head lines only.

## Draft length

`pr-drafts/flaky-fixes/SOLR-16630.md`: 4,843 characters raw, including 12 full link targets. With link targets removed it is 2,924 characters, under the 3,500 guide as a rendered description. No em dashes. The sections are: AI header, Jira link, What happens today, What this change does, Proof, Limits, Changelog, AI assistance. The "A choice to check" section is omitted (see decision 4).

## Owner decisions (flagged, not taken)

1. **Fail-before run.** No run of the test on base code is on record. The Solr-issues `AGENTS.md` Test Runs section asks for `-WithFailBefore` on any ticket that changes a test, and this receipt has no such stage. A fail-before run needs a verify run on a Linux gate host, which this review does not do. Decide: run it, or ship with the draft's plain "no base run on record" statement.
2. **Mechanism wording.** The draft says "likely cause, not proven" in What happens today. Decide whether to keep it there or move it to Limits. No run on record confirms the mechanism. The t3 report's Part 4 checks (Tree A on main, Tree B with the fix) are not recorded. Six passing runs fit the mechanism but do not prove it.
3. **Changelog title overstates the fallback.** The title reads "no longer stops the PULL node while an add through it is still in flight". After the two-minute wait, the test still stops PULL, and the add loop may still be retrying. The receipt's "first successful add" also leaves out the fallback. Decide whether to amend the title. This needs a submit-branch edit, which this review does not do.
4. **Choice section omitted.** The only candidate with a real trade-off is widening the add loop's catch to retry transport errors. Its cost: a write that reached the server may be sent again (the fixed id makes that an overwrite, per t3), and a closed client keeps failing until the test timeout. Under pr-formula section 4, a broader-fix route goes in Limits, so the draft does that. Decide whether Nick wants it as a choice section.
5. **Assignment slice text conflicts with t3.** The "Added slice 4" text calls the cause a "coordinator-endpoint race: the test read cluster state before the coordinator role finished registering". The t3 report and the implementation assignment both describe a fixed-timer stop of PULL. The draft follows t3. Confirm the slice text is wrong.
6. **Failing CI run.** The implementation assignment cites apache/solr run 38009158573 (seed `681E2A715B2CE1D3`) as the failing run. The draft does not link it, because it was not checked here (no web access or fetch was used). Decide whether to link it, after checking it.
7. **Reopened status.** The "reopened" framing comes only from the assignment. The workspace Jira export has no SOLR-16630 row. Confirm the ticket state before posting.
8. **The two-minute number.** It comes from code (branch L262), not from the receipt. Decide whether to keep it or to say "a bounded wait".
9. **Seeds.** The CI seed value is in the draft as a reproduction detail. The five random seed values are left out. Decide whether to list them.

Not used on purpose: the settling-run result on main that the implementation assignment mentions ("did not reproduce"). It is not in the receipt or in any file here, so the draft does not use it.

Scratch copies of the head test and JettySolrRunner files were written to the session scratchpad only, not to the worktree.
