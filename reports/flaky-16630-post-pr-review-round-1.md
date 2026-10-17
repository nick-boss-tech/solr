# Post-PR review round 1: SOLR-16630 (apache/solr PR 5106)

Verdict: **STILL OPEN** (3 findings: F1 citations, F2 Proof wording, F3 Limits offer).

## Live PR and head

- PR 5106 on apache/solr, draft, OPEN. Head `nick-boss-tech:solr-16630-submit` at `9c77a4db09d7a890f62dc9a9ffb2bf5613245da7`. Matches the assignment. (The ticket number is not the PR number; `pr view 16630` fails, `pr view 5106` works.)
- Live body compared with `pr-drafts/flaky-fixes/SOLR-16630.md` sentence by sentence: no differences found. Byte-level hash not run.
- Commits after the gated head `9bea59741ac`: one (`9c77a4db`, changelog title only). `git diff 9bea59741ac 9c77a4db` touches only `changelog/unreleased/SOLR-16630.yml`, one line.
- Full change against merge-base `3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c`: changelog (new, 10 lines) and `TestCoordinatorRole.java` (6 added lines). Matches "test-only plus changelog".
- Title check: PR title and changelog title match (the PR title adds the `SOLR-16630: ` prefix only).
- Proof check against the receipt: six passing seeds at `9bea59741ac` (CI seed plus five random seeds, each 1 test, 0 failures, 0 errors): supported. Base run: one run, 2026-10-11 UTC, failed with SolrServerException caused by ClosedChannelException at `TestCoordinatorRole.java:306`, PULL stopped with no successful add: supported. Date "2026-10-10 (UTC)" matches the gate finish date; the receipt gives no per-run dates (see N8).
- Lucene: no version is mentioned, so the rule does not apply.
- AI header and AI assistance footer: exact match with the `pr-formula.md` template.
- Internal process vocabulary in the draft: none found (checked gate, receipt, ledger, seed, claim, pool, assignment, subagent, submission, round, worktree, Muse, vm). "Pooled connection" is a technical term, see N3.
- Em dashes in the draft: none.
- Head-SHA links for the change's own code are correct: L243 (latch), L262-L264 (wait block), L311-L313 (add, commit, countDown), changelog L1-L10.

## Findings

### F1. Symptom citations link the head SHA

- Draft, "What happens today", lines 11, 13 and 15 link `9c77a4db...` with head line numbers (L222, L242, L261-L266, L315, JettySolrRunner L616-L617 and L847-L852, SolrServerException L24). Limits line 38 links L315 at head too.
- Rule: `pr-formula.md` section "Presentation rule" says pre-change symptom citations link the merge-base commit, and the text says so.
- Evidence: the change shifts lines after line 30 and after line 243. At the base commit the same code sits at: client assignment L221, client.add L306, random delay L241, sleep and stop L259-L261, catch L309. JettySolrRunner (both lines) and SolrServerException L24 are unchanged, so they match at base.
- Replacement for line 11 (the draft's first line of that paragraph):

  The links in this section point to the base commit, which is the code before this change. The add loop sends its documents through the PULL replica's client ([test](https://github.com/nick-boss-tech/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L221-L221)), and the add call is on [line 306](https://github.com/nick-boss-tech/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L306-L306). The test stops the PULL replica after a delay that is chosen at random when the test starts ([test](https://github.com/nick-boss-tech/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L241-L241), [test](https://github.com/nick-boss-tech/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L259-L261)). The stop does not wait for the adds to finish.

- Replacement for line 13:

  The add loop catches only `SolrException` ([test](https://github.com/nick-boss-tech/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L309-L309)). `SolrServerException` extends `Exception`, not `SolrException` ([class](https://github.com/nick-boss-tech/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/solr/solrj/src/java/org/apache/solr/client/solrj/SolrServerException.java#L24-L24)). So the exception is not caught, and the test fails.

- Replacement for line 15:

  The likely cause is that the stop closes the client the loop is still using. This is not proven. `JettySolrRunner.stop()` closes its cached client first ([class](https://github.com/nick-boss-tech/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/solr/test-framework/src/java/org/apache/solr/embedded/JettySolrRunner.java#L616-L617)). The client the test holds comes from that cache ([class](https://github.com/nick-boss-tech/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/solr/test-framework/src/java/org/apache/solr/embedded/JettySolrRunner.java#L847-L852)).

- The Limits link on line 38 is replaced by the F3 text.

### F2. Proof overstates one base run and the causal link

- Draft line 27 (bold headline): "The test fails on the base code with the recorded failure, and passes six runs with this change."
- Draft line 29, last sentence: "That is the failure this change prevents, because the stop now waits for the first successful add."
- Evidence: the receipt says the base job stopped after run 1. One failing run does not show that the test "fails". Draft line 39 says six passing runs do not prove the fix holds, so "prevents" is stronger than the evidence.
- Replacement for line 27:

  **On the base code the test failed in its one run, with the recorded failure. With this change it passed six runs.**

- Replacement for the last sentence of line 29:

  This change targets that failure: the stop now waits for the first successful add.

### F3. Limits: no follow-up offer for the widened catch

- Draft line 38: "The add loop still catches only `SolrException` ([test](...#L315-L315)). Widening that catch was not done."
- Rule: `pr-formula.md` section 4 and the template rules. An adjacent or broader issue left unfixed gets one Limits line, named, with an offer of a follow-up ticket and PR on request. The draft has no offer.
- Replacement for the whole bullet on line 38:

  - The add loop still catches only `SolrException` ([test](https://github.com/nick-boss-tech/solr/blob/3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c/solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java#L309-L309)). Widening that catch was not done. On request, a follow-up ticket and pull request can cover it.

## Checked and correct

- The fix as drafted: the test waits for the first successful add (latch, L243, L262-L264, L311-L313 at head), with a 2-minute await, then logs a warning and stops PULL anyway. Matches the draft's "What this change does".
- Limits bullets present: the stale pooled connection (line 37) and the SolrException-only catch (line 38). Also the intermittent and product-code bullets.

## Optional notes (not findings)

- N1. Line 23: "if no add ever succeeds, the test reaches its failure up to two minutes later than before." The code only delays the stop. The failure timing depends on the add loop, which has no bound. Plainer and exact: "Behavior change: if no add succeeds within two minutes, the PULL replica stops two minutes later than before."
- N2. Line 9: "In a failing CI run". The receipt records no CI run; its only recorded failure is the base run at the CI seed. Confirm the source (ticket or CI link), or write "In a failing run".
- N3. Line 37: "can go stale" is a hypothesis the evidence does not show. Consider "could go stale". Plainer wording: "A saved connection from the test client to the PULL replica could go stale with no stop at all."
- N4. Line 29 could name the base commit once (`3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c`), to match the new links in F1.
- N5. Line 42: the changelog link text wraps the file name in backticks; the template uses plain text. Trivial.
- N6. No "A choice to check" section. The widen-the-catch route is named in Limits, which the formula allows for a test-only patch. The stop-timing cost (N1) is the one cost a maintainer could reject; the main side may decide whether it needs a choice section.
- N7. Length: about 3,200 characters of prose with link URLs removed (5,195 bytes with them). Within the guide on prose.
- N8. The receipt gives the gate finish date (2026-10-10 UTC), not per-run dates. The day-level claim holds.

## Not done

- No edits to the draft, receipt, claim, assignment, hosts file or any branch. No commit, push, PR comment, review, edit, close or Jira write. No builds, Gradle, tests, gate runs or queue commands.
- `claims/pool-flaky-16630-post-pr-review-round-1.md` not marked DONE (hard limit). The main side marks it with the deliverable push.
- `git fetch origin solr-16630-submit` was run as the assignment directs. It updated only the remote-tracking ref `origin/solr-16630-submit`.
- `research\gh.ps1` was called by its absolute path, because the relative path did not resolve from the worktree.
- CI check status and the source of the "failing CI run" were not read.
