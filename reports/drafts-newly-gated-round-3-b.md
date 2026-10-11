# Drafts for newly gated round 3, part B (NG3B): SOLR-16322, SOLR-10667, SOLR-12347

Date: 2026-10-11. Claim: `claims/pool-drafts-newly-gated-round-3.md` (not edited here; the lead marks it DONE).
Files written: `pr-drafts/build-docs/SOLR-16322.md`, `pr-drafts/cli/SOLR-10667.md`, `pr-drafts/cli/SOLR-12347.md`. This report. Nothing committed, pushed, posted, or built. No tests run.

## Head check (ls-remote origin, read only)

| Ticket | Draft head | Live fork tip | Result |
|---|---|---|---|
| SOLR-16322 | `68dd5e2b154e3b9e61c715db94ca91fadad545a6` | same | matches receipt L4 |
| SOLR-10667 | `1e3f022552be4d281a020e015f4c8c05ce4eec6c` | same | matches receipt L4 |
| SOLR-12347 | `56d38389c75a39223f9b31e97671bb27d45ba99c` | same | matches receipt L4 |

- No packaging delta at the fork tip. The assignment called the 10667 and 12347 packaged heads "local to the gating host"; the live fork tips are the same SHAs, so the drafts cite them directly.
- The worktree's `origin/solr-*-submit` tracking refs are stale (they point at the registered heads 65e0b8d7, 32b594f2, b77acba2). The packaged-head objects were not local, so I ran `git fetch --no-tags origin <sha>` for each head. That writes FETCH_HEAD only; no branch or tracking ref moved.
- Packaging commits: `git diff --stat` between each registered head and its packaged head shows only the removal of the branch's TESTING note file (16322: `SOLR-16322-TESTING.md`; 10667: `SOLR-10667-TESTING.md`; 12347: `SOLR-12347-TESTING.md`). Code is identical to the registered head.
- Merge-bases with upstream: 16322 and 10667 are `cabedd1d968059215188f4e7563fb303241899ed` (matches the 16322 receipt L5). 12347 is `b6b2b8f10e9827e3b86e44649fb7966ce646c185`, not `cabedd1d968`. The receipt's base distribution for the 12347 run is `cabedd1d968`; `git diff --stat` between those two commits is empty for `solr/bin` and `solr/packaging/test`, so the run's base is equivalent for these files. The draft says so.

## Receipt numbers used

SOLR-16322 (`receipts/SOLR-16322.md`)
- L3: status green; the premise holds.
- L4: registered head 65e0b8d7; packaged and final tested head 68dd5e2b.
- L5: base cabedd1d968.
- L7: the temporary test class (name not copied into the draft), 1 test, 1 intentional assertion failure, 0 errors, 0 skipped; beast task with 16 repeats; no production test suite run; the temporary file was not committed and was removed.
- L8: stock beast invocation produced no tests (generated tasks had no source); a temporary local init script wired them to the standard test classes; no tracked build file changed. Used in Limits.
- L9: packaging commit removes only the TESTING note.
- L6: seed values. Not used; no value appears in the draft.
- Gap: the receipt has no date. The Proof omits the verification date. (The template asks for one.)

SOLR-10667 (`receipts/SOLR-10667.md`)
- L3: gate green at the packaged head, 2026-10-10.
- L4: gated head 1e3f0225; fork tip verified at this head.
- L5: what the change is (packaging copy, BATS check).
- L6: no Java files change; test steps are the premise legs and the BATS check.
- L7: premise: base distribution (merge-base cabedd1d968) has modules/ltr without modules/ltr/example; head has the example with config.json, train_and_upload_demo_model.py, exampleFeatures.json; distribution produced with the full-distribution install task.
- L8: BATS test "ltr module ships its example directory": 1 test, 0 failures at the head distribution and at the packaged head.

SOLR-12347 (`receipts/SOLR-12347.md`)
- L3: gate green at the packaged head, 2026-10-10.
- L4: gated head 56d38389.
- L5: what the change is; start wait keeps its 180 default; BATS test and changelog.
- L6: no Java files change.
- L7: premise part 1: default 180 at merge-base b6b2b8f10e and base cabedd1d96 (solr/bin line 154); solr/bin and solr/packaging/test identical between them.
- L8: premise part 2: branch test fails on the base distribution at the 600-second assertion (base stop output says 180 seconds); passes at the head distribution (1 test, 0 failures) and at the packaged head (tests 1, failures 0).

## Title source

- SOLR-16322: `changelog/unreleased/SOLR-16322.yml` at 68dd5e2b: "Use the failing beast subtask's randomization seed in its reproduce command." (type fixed).
- SOLR-10667: `changelog/unreleased/SOLR-10667-ltr-example-packaging.yml` at 1e3f0225: "The Solr distribution now includes the Learning To Rank example files (modules/ltr/example)." The draft does not use the title line (the template has none). The cli audit (`reports/cli-bin-packaging-round-1-s5.md` lines 99 and 111) suggested "full distribution" wording; the changelog still says "The Solr distribution". The draft says "full distribution" in its body, where the check was run. The changelog file is on the branch and was not changed.
- SOLR-12347: `changelog/unreleased/SOLR-12347-stop-wait-default.yml` at 56d38389: "The default SOLR_STOP_WAIT is now 600 seconds (was 180) so nodes with many cores can stop gracefully; SOLR_START_WAIT keeps its 180 second default." (type changed). The draft does not repeat the "many cores" reason, because the draft does not support it with evidence.

## Citations and checks

All line ranges were read at the named SHA with `git show <sha>:<path>` (or grep at that SHA).

SOLR-16322
- Base L33 at cabedd1d968 (reproduce line built from testOptionsForReproduceLine): checked.
- Head L40-53 (addFailInfo helper, task seed override): checked.
- Head L58-67 (afterTest and afterSuite call sites): checked.
- Changelog at head: checked.

SOLR-10667
- Base L80-83 at cabedd1d968 (assemblePackaging, no example copy): checked.
- Head L83-85 (new from block): checked.
- Head test_modules.bats L42-47 (BATS check): checked.
- Changelog at head: checked.
- Example directories: `git ls-tree` at head shows `solr/modules/ltr/example` as the only `example` directory under solr/modules. The packaging block applies to `:solr:modules:` projects and cross-dc-manager (packaging.gradle L39-40).

SOLR-12347
- Base L154 at b6b2b8f10e (SOLR_STOP_WAIT default 180): checked.
- Base ref guide L400 at b6b2b8f10e (180 seconds, kill): checked.
- Head solr/bin/solr L154-155: checked.
- Head solr.cmd L823-825: checked.
- Head solr.in.sh L27-32 and solr.in.cmd L31: checked.
- Head test_start_solr.bats L135-144: checked.
- Head solr.cmd L1308-1311 (5 percent cut for waits of 30 or more, so 600 becomes about 570): checked by reading code; not run.
- Head solr.service L32 (`TimeoutSec=180s`, unchanged): checked.
- Head ref guide L400 (still 180, unchanged): checked.

## Choice section decisions

- SOLR-16322: no choice section. The change alters only the printed reproduce line. The only route is which seed the line prints, and the head code comment says beast subtasks override the root seed, so the subtask's seed is the one a reader needs. The category report (`reports/build-docs-misc-round-1.md`) records no alternative route. Not a design choice with a cost.
- SOLR-10667: no choice section. Three-line copy in the existing assemble task. Placement in packaging.gradle follows the existing task (the cli audit recommended it). Copy scope (every file, module-wide reach) is stated openly in "What this change does", and the test-scope limit is in Limits. Narrow-versus-broad items are not choices on a straightforward patch.
- SOLR-12347: choice section included. The default value (keep 180 and document, 600 as implemented, or 900/1200) is a real behavior decision with a cost (longer wait before a forced stop). The cli audit (`reports/cli-bin-packaging-round-1.md` item 11) listed it as an owner decision. Pointed question: "Was 600 seconds the right default for this change?"

## Receipt gaps, discrepancies, and open items for the lead

1. SOLR-16322 receipt has no date; the Proof omits it. Please supply the date if the template must carry one.
2. SOLR-16322: the receipt records only beast runs. The category report (`reports/build-docs-misc-round-1.md` L110) also wanted a non-beast failing run, a passing build, and a run with a user-given seed. None is in the receipt, so Limits names them as not run.
3. SOLR-16322 draft uses the word "seed" for the product concept (the `-Ptests.seed` option). It contains no values. The brief lists "seeds" as process vocabulary; if you want the word removed, the 16322 draft needs a rewording pass.
4. SOLR-12347: the branch does not change `solr.service` (TimeoutSec 180s), the Control Script Reference (line 400), or the test name's start-default claim. The draft names all three in Limits, as unchanged. The cli audit recommended changing them in this change; that is a branch decision, not a draft decision.
5. SOLR-12347 receipt L5 and the draft both say the start wait keeps 180. The test does not assert the 180 start default (only an explicit 45-second stop wait). The draft says so.
6. SOLR-10667: changelog wording ("The Solr distribution") is broader than the checked full-distribution case. Not changed (branch file).

## Not verified

- The systemd statement ("under systemd a stop may end at 180 seconds") is an inference from `TimeoutSec=180s`. It was not run.
- The Windows 570-second figure is computed from the 5 percent reduction in code. solr.cmd was not run.
- Nothing was built or run. The 16322 "beast subtasks override the root seed" statement is the head code comment (lines 44-45) plus the receipt's run result.
- Material: no `material/` entries name these tickets (grep found none). Context came from the category reports cited above; the drafts follow the receipts.
- Draft lengths: 16322 is 3,002 characters (2,476 without link URLs); 10667 is 2,826 (2,304); 12347 is 4,366 (2,945). 12347 is over the 3,500 guide only because of its link-heavy citations and its choice section.
- No em dashes in the three drafts (checked by byte search). No gate, receipt, ledger, claim, pool, assignment, subagent, submission, or run-identifier words in the drafts (checked by grep, except the "seed" note above).
