# Receipt: SOLR-18624 assertion repair, round 2 gate

Verdict: GATE GREEN at H1 `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`. Every leg of `gates/solr-18624-assertion-repair-round-2.md` passed or produced its required failure. Status moves to Implemented, Proof Complete at this head. No PR was opened and nothing was posted publicly; both wait for the owner's direction.

## Identity and ancestry

- Executor: phone agent (Linux VM), claimed at the owner's direction in chat; claim `claims/solr-18624-assertion-repair-round-2-phone-agent.md`.
- Fork branch: `nick-boss-tech/solr:solr-18624-flaky-multiword-synonyms`. Live fork ref read through the GitHub API before claiming and again after the final leg: `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a` both times.
- H1 `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`; parent H0 `c610e6d05db5700835a123de54da61137478e48f`; upstream base B `07ccc73cd24e26e59da169ca35b3fd5f64c35986`. Both ancestors preserved. Follow-up diff H0 to H1: one test file, `TestMultiWordSynonyms.java`, +36/-6, as pinned.
- Host: this Linux VM, vendored JDK 21, runner `~/workspace/tools/solr-gradle.sh` (Gradle 9.7.0) for every `./gradlew` command in the job text; one build at a time under the host build lock. Logs and fresh JUnit XML retained on this host under `~/workspace/tools/gate-18624/`.

## Preflight (before claiming)

- Detached tidy at H1 in a clean worktree: rc 0, tracked tree clean, `git diff --check` clean. One environment repair preceded the successful attempt: the first tidy died on the Gradle journal lock held by two stale daemons left by the preceding gates on this host; daemons killed, lock cleared, rerun clean. No test had executed, so no result was affected.

## Acceptance legs at H1 (2026-11-05T05:26:40Z to 05:32:22Z)

- Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`, rc 0.
- Focused repetition: `:solr:core:test --tests org.apache.solr.search.TestMultiWordSynonyms.testPf -Ptests.seed=18624C0DE -Ptests.iters=25 --rerun-tasks`, rc 0. Fresh XML: 25 executions, 0 failures, 0 errors, 0 skipped.
- Full class and neighbor: `TestMultiWordSynonyms` plus `org.apache.solr.search.TestExtendedDismaxParser.testPfPs`, seed `18624BEEF`, `--rerun-tasks`, rc 0. Fresh XML: class 6 tests, neighbor 1 test, 0 failures, 0 errors, 0 skipped.
- Candidate tree clean after the legs.

## Query-shape diagnostic (disposable H0 checkout, 2026-11-05T05:33:44Z to 05:35:23Z)

At H0 `c610e6d05db5`, in a disposable checkout only, a temporary diagnostic method issued the two final slop-2 requests with `debugQuery=true` and `sort=score desc,id desc` added, against the same fixture documents 10 to 13 that `testPf` indexes. This is a deterministic demonstration of the invalid ordering assumption, not a replay of the reported run; the original failure seed is not recorded in SOLR-18624.

- `bar foo` (pf2 `text^10`, ps 2): numFound 4; order 10, 13, 12, 11; scores 4.650775 for document 10 and 0.8255836 for each of 13, 12, 11. Parsed query's pf2 clause: `(text:"tropical cyclone foo"~2 text:"bar foo"~2)^10.0`. The reversed phrase favors document 10, as expected.
- `tropical cyclone foo` (pf2 `text^10`, ps 2): numFound 4; all four scores tied at 4.953501; with the deterministic tie-break the order is 13, 12, 11, 10. Parsed pf2 clauses: `(text:bar text:"tropical cyclone"~2)^10.0` and `(text:"cyclone foo"~2)^10.0`. Both bigrams present, as expected.
- The premise holds exactly as the job stated it. (Context verified in the schema during the run: synonyms apply in the query analyzer only, so the `tropical cyclone foo` phrase path is present in the parsed query but matches no indexed document; document 10's advantage comes from the `bar foo` path.)

## Mutations (disposable H1 checkout, seed 18624C0DE, `--rerun-tasks`, 2026-11-05T05:36:15Z to 05:41:37Z)

Each mutation changed only `solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java`, compiled, and ran `testPf` once. The acceptance test file stayed byte-identical in every run (md5 `3de941b2ded25ac9baf5bd4f1b8eecaf`, the H1 value) and the checkout was reset to clean H1 between mutations. Each run: 1 test, 1 failure, 0 errors, 0 skipped. Full diffs, logs and XML retained on this host as `~/workspace/tools/gate-18624/mutation-M{1,2,3}.diff` and `mutation-M{1,2,3}-xml/`.

- M1, omit the `bar foo` phrase path at slop 2: in `addShingledPhraseQueries`, after the phrase sub-parse, a recursive filter drops the `PhraseQuery` whose term sequence is exactly `bar, foo` on field `text`, only when slop is 2 and the shingle is `"bar foo`; the `tropical cyclone foo` path and the main query are untouched. Required failure obtained: the restored score-advantage assertion failed, xpath `//doc[str[@name='id'][.='10']]/float[@name='score'][. > //doc[str[@name='id'][.='13']]/float[@name='score']]` on the `bar foo` ps=2 request; the response maxScore fell to the unboosted tie value 0.8255836.
- M2, omit the `bf` additive queries: the `getBoostFunctions()` results are computed but the loop that adds them as SHOULD clauses no longer adds anything (one line removed in `parse()`). Required failure obtained: the first strict baseline assertion failed, xpath `//doc[1]/float[@name='score'][. > ../../doc[2]/float[@name='score']]` on the `foo bar` request with `bf=boost_d`, while document 13 still ranked first through the secondary `boost_d` sort at a tied score of 0.8255836.
- M3, omit the `cyclone foo` pf2 bigram: same filter shape as M1, dropping the `PhraseQuery` with term sequence `cyclone, foo` only when slop is 2 and the shingle is `"cyclone foo`; the `tropical cyclone` bigram and the rest of the request are untouched. Required failure obtained: the precise parsed-query assertion failed, xpath `//str[@name='parsedquery'][contains(.,'(text:"cyclone foo"~2)^10.0')]` on the `tropical cyclone foo` ps=2 request.

## Root check (2026-11-05T05:41:54Z to 05:50:26Z)

- `check -x test` at H1 in a normal git clone of the local repository (layout disclosed per host rule: Antora cannot run in a worktree). The clone fetched H1 explicitly from the source repository and recorded `git rev-parse HEAD` as `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a` before building. Result: BUILD SUCCESSFUL in 7m 20s, 556 actionable tasks, rc 0. Clone tree clean afterwards.

## Final state

- Candidate worktree at H1, tree clean. Mutation worktree reset to H1, tree clean; no mutation remains in any checkout or branch. Final fork read-back: `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`.
- Unproven, stated separately: the originally reported flaky failure was not replayed; SOLR-18624 records no seed for it. The repair is proven by construction (the diagnostic above shows the tie and the boost deterministically), by 25 clean repetitions, and by the three mutation discriminations, not by reproducing the original run.
- No assertion was weakened, no executed failure was silently rerun, and no production defect is claimed: this remains a test repair.

## PR description draft (factual, not published)

Title: `SOLR-18624: Stabilize TestMultiWordSynonyms.testPf assertions`

Body:

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

`TestMultiWordSynonyms.testPf` could fail when phrase-boosted documents tied on score and their return order depended on index internals. This change is test-only: it makes the assertions state the score relationships the requests actually guarantee.

- The two `bf` baseline requests still expect IDs 13, 12, 11, 10 under the secondary `boost_d` sort, and now also require strictly descending scores, so the sort cannot hide a missing function-boost contribution.
- A new slop-0 request requires documents 10 and 13 to score equally: a reversed phrase gets no exact-phrase boost without slop.
- The slop-2 `bar foo` request requires document 10 to outscore document 13 and rank first, and requires both synonym phrase paths (`bar foo` and `tropical cyclone foo`) in the parsed query with slop 2.
- The `tropical cyclone foo` request requires all four IDs without an order, and the exact boosted pf2 bigrams `tropical cyclone` and `cyclone foo` in the parsed query.

Verification: at the offered head, `testPf` passed 25 of 25 repetitions (seed 18624C0DE); the full class and `TestExtendedDismaxParser.testPfPs` passed with no skips (seed 18624BEEF); tidy and Error Prone compilation are clean; the full `check -x test` build succeeded. Three temporary production mutations, each reverted after its run, confirm the new assertions discriminate: removing the `bar foo` phrase path fails the document-10 score assertion, removing the `bf` contribution fails a strict baseline score assertion while the ID order stays unchanged, and removing the `cyclone foo` bigram fails its parsed-query assertion.

Limits: the originally reported failure's seed is not recorded in SOLR-18624, so it was not replayed; a deterministic diagnostic with an explicit tie-break sort shows the tie on `tropical cyclone foo` and document 10's boosted win on `bar foo`, which is the assumption the old assertions got wrong.

*Nick Shanin directed the work and takes responsibility for this contribution.*
