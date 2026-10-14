# Gate job: SOLR-17215 receipt at the reviewed head

- Branch: solr-17215-submit. Head: `04d35df186ddeab079ed93fce15c6fc61554912a` (live tip, `git ls-remote origin refs/heads/solr-17215-submit`, checked 2026-10-14).
- Status: DONE (main agent ruling, 2026-10-14): GATE GREEN at the head; receipt written at receipts/SOLR-17215.md. Written by the windows review coordinator (round 5, slice S3).
- Capability tags: gate.
- Intended host: a Linux host with the gate toolchain (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Why: the gate result at this head is recorded on the main side (`material/suggester-17215-receipt.md`, 2026-10-09), but `receipts/SOLR-17215.md` is not on this branch. Under the date rule in `reports/draft-date-flags-round-1.md`, the draft's Proof carries counts and head but no verification date until a receipt file exists. The draft also needs the observed failure of the pre-fix run, which no record names yet.
- Job type: record write first, then a re-run only if the record is not on the host.

## Steps

1. Confirm the head: `ls-remote origin refs/heads/solr-17215-submit` must equal `04d35df186ddeab079ed93fce15c6fc61554912a`. If it does not, stop and record the new tip.
2. If the gate log `g17215-gate.log` is on this host and its head line is `04d35df186d`, write the receipt from it (no re-run) and go to step 8. Otherwise run steps 3 to 7.
3. Changelog YAML parse for `changelog/unreleased/SOLR-17215-suggester-not-built-503.yml`.
4. Tidy with a clean tree, then Error Prone compile.
5. Pre-fix proof: with `solr/core/src/java/org/apache/solr/spelling/suggest/SolrSuggester.java` reverted to `cabedd1d968` and the test kept, run `TestFreeTextSuggesterNotBuilt` at seed `17215C0FFEE17215`. Record the failing assertion or exception text verbatim. This is the observed failure the draft's Proof needs.
6. Focused run at head with fresh JUnit XML: `TestFreeTextSuggesterNotBuilt`, `SuggestComponentTest`, `SuggestComponentContextFilterQueryTest`. Record counts, seed and the date.
7. Module check: `:solr:core:check -x test`.
8. Write `receipts/SOLR-17215.md` at this head: the counts from the JUnit XML, the date, the observed pre-fix failure from step 5, and the note that the record came from the existing log when step 2 applied. Mark this job DONE.

## Checklist

- [x] head confirmed by ls-remote
- [x] record written from `g17215-gate.log`, or steps 3 to 7 run
- [x] pre-fix observed failure recorded (step 5)
- [x] focused counts and date recorded (step 6)
- [x] receipt written at the head; job DONE

On DONE, the review coordinator restores the verification date in `pr-drafts/suggester/SOLR-17215.md` from the receipt.

## Coordinator run record (2026-10-14)

- Gate run at 04d35df186dd ended 14:30:50Z (log logs/vm2/queue-g17215-gate-20261014T140914Z.out). Pre-fix leg rc=1 as expected: TestFreeTextSuggesterNotBuilt testQueryBeforeAndAfterBuild fails on base with `java.lang.RuntimeException: Exception during query` (1 failure, 0 errors). Head leg rc=0: SuggestComponentContextFilterQueryTest 10 tests, 0 failures, 1 skipped; SuggestComponentTest 12 tests, 0 failures; TestFreeTextSuggesterNotBuilt 1 test, 0 failures. Changelog rc=0, tidy rc=0 (tree clean), compile rc=0, core check rc=0.
- Not GREEN under the all-rc-zero rule because of the expected-nonzero pre-fix leg. Not pushed, not marked DONE, not relaunched. Left for the main agent.

## Main agent ruling (2026-10-14)

The hold vm2 left on this job is discharged. The pre-fix leg's rc=1 is the expected proof shape, not a gate failure: with SolrSuggester.java reverted to cabedd1d968, TestFreeTextSuggesterNotBuilt.testQueryBeforeAndAfterBuild fails with `java.lang.RuntimeException: Exception during query` (1 failure, 0 errors), because the test asserts a SolrException with code SERVICE_UNAVAILABLE and the message "is not built on this node", and base code never converts the unbuilt lookup's bare IllegalStateException into that answer. The failure matches the premise by content; every head leg returned rc=0 (changelog, tidy, compile, focused runs, core check). An all-rc-zero rule misreads the gate, since the pre-fix leg runs on reverted production where failures are the pass condition. Gate stands GREEN at 04d35df186ddeab079ed93fce15c6fc61554912a; the receipt is receipts/SOLR-17215.md.
