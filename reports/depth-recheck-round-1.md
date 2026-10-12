# Depth recheck of proof statements, round 1

Verdict. Five drafts were checked in depth against their receipt bodies and the object store. Three were QUALIFY and were corrected in place: SOLR-5939, SOLR-13876 and SOLR-18530. One is HOLD with no edit: SOLR-11678, whose named packaged head is not in the object store. One is QUALIFY and partly corrected: SOLR-2632. Its option-(a) description, production hunks, unified case and changelog line change what the PR contains, so they wait for the owner's disposition, and the PR must not be posted until that decision is made.

Inputs: `assignments/pool-depth-recheck-round-1.md`, claim `claims/pool-depth-recheck-round-1.md`. Two read-only subagents did the checks. Each edit below was made here and checked against the receipt line it cites.

One process note: the prompt I gave the SOLR-13876 reviewer named the wrong folder. The draft is at `pr-drafts/search-components/SOLR-13876.md`. The reviewer found the file itself, so its findings stand.

## Draft changes

### SOLR-5939 (`pr-drafts/update-processing/SOLR-5939.md`, line 36)

Before: "The combined run (verified 2026-10-08) passed 26 of 26 focused tests on a local merge commit of this head with SOLR-5754 at `7fbe0128d8b0`. The merge commit is `1ddbf36202d`, and its tree is `f2e33340f0ea`."

After: "The combined run with SOLR-5754 (verified 2026-10-08) passed 26 of 26 focused tests, on a merge of this head with SOLR-5754 at `7fbe0128d8b0`, with merged tree `f2e33340f0ea`."

Basis:
- `git merge-tree --write-tree f8d4bdbea518 7fbe0128d8b0` gives tree `f2e33340f0ea`, so the merged tree reproduces from the two heads.
- The merge commit `1ddbf36202d` is absent from the object store, so it is no longer named.
- "local" is not in either receipt. The receipt says "merge commit".
- The 26 total is the SOLR-5754 receipt's combined-gate line, "GREEN, 26 of 26 tests". SOLR-5939's own receipt lists five of them, and the draft's breakdown (2+1+2 and 19+2) sums to 26. The draft's breakdown is kept.

The earlier held item, "merged tree absent", is resolved for the tree. Only the merge commit is absent, and the draft no longer names it.

### SOLR-13876 (`pr-drafts/search-components/SOLR-13876.md`, line 21)

Before: "Verified 2026-10-07 at head 2e110473dbad."

After: "Re-run at head 2e110473dbad, dated 2026-10-07."

Basis: receipt line 4 says "the round 35 re-runs at the same head are dated 2026-10-07" and that the ledger row "names no gate log and no explicit gate date". It is a statement about re-runs, not a gate record, so "Verified" overstated it. The date is kept, with the narrower status word. The counts (9 of 9 with the change, 7 of 9 on base) are supported by receipt lines 6 and 7 and were not changed.

### SOLR-18530 (`pr-drafts/flaky-fixes/SOLR-18530.md`, line 25)

Before: "`RecoveryAfterSoftCommitTest` with this change: 6 of 6 runs passed. Checked 2026-10-10 at 14868bc7a7f5ec5c4fa1ade5f894cbda9533307d."

After: "`RecoveryAfterSoftCommitTest` with this change: 6 of 6 runs passed at commit 98e5368d9965, finished 2026-10-10. The later commit 14868bc7a7f5 removes only the changelog fragment, so the test is unchanged at the branch tip."

Basis:
- Receipt line 4 says the gate runs all used `98e5368d9965`. Line 3 gives only the gate finish date, 2026-10-10.
- `git diff --name-status 98e5368d9965 14868bc7a7f5` gives one line, `D changelog/unreleased/SOLR-18530.yml`. The test file is the same at both commits.
- The draft's head stays `14868bc7a7f5`, the fork tip. The carry-over sentence states the reason the runs apply to it.

### SOLR-2632 (`pr-drafts/highlighting/SOLR-2632.md`), Proof only

| Line | Before | After |
|---|---|---|
| 7 (heading) | "The reported failure did not reproduce on the base code." | "The standard-parser form of the test passes on the base code, and the dismax form from the ticket was not run." |
| 9 | "In checks run on 2026-10-06, a `{!boost}`-wrapped query highlighted the wrapped term for the `original` and `fastVector` methods. That run used the base code, without the branch's production changes." | "On base production, with only the branch test file added, the new test passed 1/1 on 2026-10-06. The test asserts that a `{!boost}`-wrapped query highlights the wrapped term for the `original` and `fastVector` methods." |
| 23 (heading) | "The test passes on the base code by design, so it is not a fail-before proof." | "The test also passes on the base code, so it is not a fail-before proof." |
| 25 | "1/1 on base with only this test file. Verified 2026-10-06." | "1/1 on base with only this test file, run on 2026-10-06." |

Basis: receipt line 3 says "NO GATE" and that the gate's pre-fix step would fail by design. Receipt line 5 records the premise run on base production and at the received head, 1/1, dated 2026-10-06. It does not record a dismax run or a highlight output. The draft's "Verified" and "by design" wording was not supported.

Left for the owner, not edited:
- Line 15 and line 19 describe option (a) as test-only, but head `1d7018f3fd7b` adds two production hunks in `DefaultSolrHighlighter.java` (lines 303-305 and 566-571), and the receipt says they restate what Lucene 10.4.0 already does.
- Line 17 says the PR "also adds the `unified` method". `1d7018` has no `unified` case, and option (a) would add one in a new commit that has not been gated.
- Line 35 says "Changelog: none". `1d7018` includes `changelog/unreleased/SOLR-2632-highlight-boost-wrapper.yml`.
- Line 32 makes a Lucene 9.12.3 statement. The receipt says the 9.12.3 bytecode was checked, and the draft makes no backport claim.
- Receipt line 7 says the disposition is "an owner call" between options (a) and (b). The named head is neither.

## HOLD

- **SOLR-11678** (`pr-drafts/security/SOLR-11678.md`). The named packaged head `611eae2464536fdae61b8cae32abac97ee3a453b` is absent from the object store, and the receipt says it is local to vm2. The fork tip `55d8cd189d15` is present and contains `SOLR-11678-TESTING.md`. No edit was made. The hold rule applies until the vm2 commit is pushed or the draft moves to the fork tip.
  - When recovered, two Proof sentences need rewording: line 25, "The core module check also passes at this head." becomes "The `:solr:core:check -x test` task exits 0 at this head, with test tasks excluded." (receipt line 25 says the check excludes test tasks), and line 26's "This proof is inconclusive by construction" becomes "This proof is INCONCLUSIVE by compile (`:solr:core:compileTestJava` rc 1, 10 errors). No test ran at base, so it shows no before and after result." (receipt lines 3 and 29-31).
- **SOLR-10364, SOLR-11356, SOLR-12161** from the moved-heads round are still held. See `reports/moved-heads-round-1.md`.
- **SOLR-2632** is QUALIFY, and its publication waits on the owner's disposition.

## Verified in the object store

- `f8d4bdbea518`, `7fbe0128d8b0`, `46b919e2d4e8`, `98e5368d9965`, `14868bc7a7f5`, `2e110473dbad` and `1d7018f3fd7b` are present. `1ddbf36202d` is absent.
- The fork branches `solr-5939-submit`, `solr-18530-submit` and `solr-13876-submit` match the local refs named in the subagent report. `solr-2632-submit` is at `1d7018f3fd7b`, the draft's named head.

## Not done

- No build, test, Gradle run, PR or comment.
- The logs named in the receipts were not opened.
- The owner decisions above were not made here.
