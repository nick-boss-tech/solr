# SOLR-17215: gate receipt at the new head

Main side, 2026-10-09. The draft round 2 claim (`claims/suggester-draft-round-2.md`) holds SOLR-17215 because the material had no gate receipt for the new tip. It does now.

- Head: `04d35df186ddeab079ed93fce15c6fc61554912a` on solr-17215-submit (catch narrowing plus ref-guide note at `04d35df186d`; root TESTING.md removal at `a14086d1f56`).
- Gate GREEN at that head (log g17215-gate.log; receipt recorded in the receipts ledger and the takeover log by the queue check, 2026-10-09): changelog parse ok, tidy ok with a clean tree, Error Prone compile ok, `:solr:core:check -x test` ok.
- Pre-fix proof PASS: with `SolrSuggester.java` reverted to base `cabedd1d968`, the new test fails (seed 17215C0FFEE17215).
- Focused counts at head, from fresh JUnit XML: TestFreeTextSuggesterNotBuilt 1 of 1, SuggestComponentTest 12 of 12, SuggestComponentContextFilterQueryTest 10 tests with 1 pre-existing skip, 0 failures.
- For the draft: both `AlreadyClosedException` classes (Lucene's and Solr's) extend `IllegalStateException`, verified against the pinned Lucene 10.4.0 jar and the Solr source; the guard rethrows them unchanged. The guard has no dedicated test (a closed lookup mid-query is not practical to simulate in this test shape); state that in Limits. `AnalyzingInfixSuggester.lookup` also throws "suggester was not built" when unbuilt, so the conversion covers it correctly; Fuzzy lookups return empty when unbuilt and are unaffected.

SOLR-17215 is draftable at that head, with the decisions in `material/suggester-round-4-decisions.md` item 6 (500 to 503 as the Choice; the ref-guide note is in the branch).
