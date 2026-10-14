# Gate job: SOLR-17215 receipt at the reviewed head

- Branch: solr-17215-submit. Head: `04d35df186ddeab079ed93fce15c6fc61554912a` (live tip, `git ls-remote origin refs/heads/solr-17215-submit`, checked 2026-10-14).
- Status: OPEN, unclaimed. Written by the windows review coordinator (round 5, slice S3).
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

- [ ] head confirmed by ls-remote
- [ ] record written from `g17215-gate.log`, or steps 3 to 7 run
- [ ] pre-fix observed failure recorded (step 5)
- [ ] focused counts and date recorded (step 6)
- [ ] receipt written at the head; job DONE

On DONE, the review coordinator restores the verification date in `pr-drafts/suggester/SOLR-17215.md` from the receipt.
