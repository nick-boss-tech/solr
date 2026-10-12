# Gate status for SOLR-12505 and SOLR-18134, round 1

Verdict. SOLR-12505 needs the main side: the vm2 gate failed, the route for the fix is not recorded, and the draft stays held at its current head. SOLR-18134 is ready to patch after its re-gate, and no draft edit is needed now. No draft was changed in this round.

Inputs: `claims/pool-gate-status-12505-18134-round-1.md`, the vm2 gate records `gates/SOLR-12505-fetchstream-fix.md` and `gates/SOLR-18134-branch-items.md`. Two read-only reviewers checked the records against the fork tips and the code at each head. Nothing was fetched beyond what was already local, and nothing was built, run, posted or pushed to a submit branch.

## SOLR-12505 (`pr-drafts/streaming/SOLR-12505.md`): NEEDS MAIN SIDE

- Fork state. `solr-12505-submit` is at `5f20e1171bc8`, the reviewed head. The failed commit `d81a77311773` is not on the fork and is not in the local object store. Nothing harmful reached a submit branch.
- The failure. The gate's compile leg (`compileTestJava` with Error Prone and `-Werror`) failed at `StreamDecoratorTest.java:1033`. At the reviewed head, line 1033 is a plain builder call inside the existing `testFetchStream`, not an `assertThrows` lambda. The new test is not at the reviewed head, so the recorded line refers to the failed commit, which is not local. The Error Prone diagnostic cannot be checked here without a compile.
- The construction error. The recorded text "Unable to construct instance of FetchStream" is consistent with the head code. The constructor runs before the batch loop, so a construction error cannot show the corrupted-batch premise that the fix is meant to prove. The trigger input for the new test is in no local ref.
- Route. The gate job lets the claimant pick escape or validate, and requires the choice to be recorded in the gate file and the receipt. Neither records it. The reports record only "escape or validate" as the adopted recommendation, with no owner ruling. Inferred: no route has been chosen.
- Draft. Held at `5f20e1171bc8`, not converged. Sentences the failed gate leaves open: the "What this change does" sentences (defType only), the Proof line 21 (the ST-R1-23 test's proof is not established), the Limits that omit the unescaped `on=` join field, and the changelog link on line 43. The counts on line 25 (56 tests) are correct for `5f20e`, and must be refreshed from fresh JUnit XML at any new head.

Decision needed from the main side: relaunch the job under the vm2 claim owner or under a new claimant. The claimant must make the Error Prone failure go away (the `assertThrows` lambda must hold exactly one statement under `-Werror`), explain and fix the construction error, record the route, rerun the gate at a new head, and push fast-forward only after green. The draft then gets its round 2 re-check at that head.

## SOLR-18134 (`pr-drafts/schema-analysis/SOLR-18134.md`): READY TO PATCH AFTER RE-GATE

- Head. `solr-18134-submit` is at `c5a0bdb21e86`, equal to the draft and the gate record. The worktree is clean.
- Current text. All four recorded items occur exactly once at the head: the changelog title (line 1), the two upgrade-note sentences (lines 379 and 383), the `filters.adoc` clause (line 3884), and the test comment (line 111, with line 110 blank).
- Draft sentences. None quotes an item verbatim, so the recorded replacements change no draft sentence. The draft's line 9 and line 38 already say "quoted text takes the same phrase path", which matches the replacement's content. The draft is ahead of the head there, and the re-gate brings the head into line.
- Counts. The draft's counts (5 of 5 and 2 of 2) match `receipts/SOLR-18134.md`. Wording note: line 25 says the filter test "shows no base failure", but the premise leg fails to compile on base. That is more exact than the draft's wording and should be stated when the draft is re-checked.
- Edit required after the re-gate: the changelog link on line 42 names `c5a0bdb21e86`. It must name the new head that the re-gate pushes.

Nothing is changed in the draft in this round.

## Not done

- No build, test, Gradle run, `gh` call, PR write, comment or post.
- No commit was pushed to a submit branch.
- The failed compile for SOLR-12505 was not reproduced here.
