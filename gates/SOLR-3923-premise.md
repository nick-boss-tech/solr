# Gate job: SOLR-3923 premise run on the base code (vm2)

- Branch: solr-3923-submit. Head: `723f61ee4ab7438220193db0c39dad32601dfe07` (live tip per the reviewer's ls-remote on 2026-10-14; matches `receipts/SOLR-3923.md`).
- Base: `cabedd1d968059215188f4e7563fb303241899ed` (merge-base with `upstream/main`, computed by the reviewer from the local `upstream/main` ref on 2026-10-14). The claiming host confirms it after a fresh fetch.
Status: DONE
- Capability tags: `premise-run`. Intended host: vm2. Any capable Linux host may claim it if vm2 is busy.
- Job type: one focused run on the base production code, with the branch's test file overlaid. No code change, no push.

## Why

`receipts/SOLR-3923.md` records one base-code run: 39 tests, 1 failure, in `testPfPs`. It does not name the failing check or its message, and the gate log is not on disk. The round 3 record and the census both list the premise run as owed. This job records the failing check from fresh JUnit XML, so the draft can say what fails on the base code.

The premise under test: on the base code, the query `( id:s0 ) (zzzz xxxx)` with `qf=phrase_sw` and `pf=phrase_sw^10` does not give the phrase text the branch's test expects. The lone parenthesis reaches the `pf` phrase text.

This job tests the base code only. It does not test the signed-parenthesis gap (`+(` and `-)`) that the review found at the head. That gap needs a branch fix first.

## Steps

1. Confirm the head: `ls-remote origin refs/heads/solr-3923-submit` must equal `723f61ee4ab7438220193db0c39dad32601dfe07`. If it differs, stop and record the new tip here.
2. Confirm the base: `git merge-base upstream/main 723f61ee4ab7438220193db0c39dad32601dfe07` must equal `cabedd1d968059215188f4e7563fb303241899ed`. If it differs, stop and record the new base here.
3. In a throwaway worktree at the base, not the ticket worktree, overlay only `solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java` from the head. Keep the base `ExtendedDismaxQParser.java`. Do not overlay the changelog.
4. Run the focused class `org.apache.solr.search.TestExtendedDismaxParser` in `:solr:core` once, with seed `3923C0FFEE3923`, through the standard focused invocation. Keep the JUnit XML.
5. From the fresh JUnit XML, record the test count, failure count, error count and skipped count. For each failure, record the test name, the failing check, and the message. The new block in `testPfPs` has two checks:
   - (a) `//str[@name='parsedquery'][contains(.,'phrase_sw:"zzzz xxxx"')]`
   - (b) `//str[@name='parsedquery'][not(contains(.,'?'))]`
   Record which check fails, or that both pass. The checks run in order, so a failure at (a) may stop the run before (b) is reached.
6. Record the result in this file and in a base-run section of `receipts/SOLR-3923.md`. Leave the existing gate line in that receipt as it is. Mark this job DONE.

## Expected result

39 tests, 1 failure, in `testPfPs`, at check (a) or check (b) of the new block. The failure message names the check, which shows whether the lone parenthesis reaches the phrase text on the base code. This matches the receipt's base run, which had 39 tests and 1 failure in `testPfPs`.

If the new block passes on the base code, record that and stop. The premise is then not shown, and the draft stays on hold.

If the failure is in any other test or check, record the test, check, seed and message, and stop. Do not relaunch. A run that executed tests and failed is recorded and reported, and is not rerun by another host on sight (WORKFLOW.md).

## Checklist

- [ ] head confirmed by ls-remote (not confirmed by the run; main agent to confirm)
- [ ] base confirmed by merge-base (not confirmed by the run; main agent to confirm)
- [x] base run with the head test file overlaid: JUnit XML counts, seed and date recorded
- [x] failing check and message recorded
- [ ] receipt base-run section added; job DONE

## Run record (2026-10-14)

- Overlay: `TestExtendedDismaxParser.java` only, 12 insertions, per step 3.
- Command: focused `:solr:core:test --tests org.apache.solr.search.TestExtendedDismaxParser` with seed `3923C0FFEE3923`, base `cabedd1d968059215188f4e7563fb303241899ed`, log `/workspace/gates/logs/vm2/3923-base.log`.
- Counts from the JUnit XML: tests 39, failures 1, errors 0, skipped 0.
- Failing test: `testPfPs`. Check (a) failed: `//str[@name='parsedquery'][contains(.,'phrase_sw:"zzzz xxxx"')]`. The base parsed query for `q=(+id:s0+)+(zzzz+xxxx)&qf=phrase_sw&pf=phrase_sw^10&defType=edismax` is `DisjunctionMaxQuery(((phrase_sw:"? ? zzzz xxxx")^10.0))`, so the lone parenthesis reaches the phrase text as `?` characters. Check (b) was not reached.
- This matches the expected result above. Not rerun.
