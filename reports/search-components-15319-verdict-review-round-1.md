# SOLR-15319 premise verdict and draft Limits: review, round 1

Verdict: the verdict is not yet complete on the record, and the settled Limits bullet needs four edits before the main side relies on it. Nothing was edited here. Each defect below comes with its replacement text, for the main side to apply.

Triggering commits: `86aa0daaf15` (the main side's verdict and Limits settlement) and `7c48ddaae80` (vm2 claims for SOLR-13186 and SOLR-15863). The vm2 claims are checked in the process note below. Claim: `claims/pool-review-15319-verdict-and-vm2-claims-round-1.md`.

Sources: `gates/SOLR-15319-8051-premise.md`, `receipts/SOLR-15319.md`, `receipts/SOLR-8051.md`, `pr-drafts/search-components/SOLR-15319.md`, `pr-formula.md`, `WORKFLOW.md` (the standing limits), and the code at the 15319 head `4bda91f46fc1` and the 8051 head `e50a2aa3437`. The vm2 run log (`logs/vm2/15319-head.log`) and the JUnit XML are not in the repository, so nothing from them is checked here.

## Part 1: the verdict record

- **R1, run counts and top frame.** Partly supported. The seed `15319C0FFEE15319` and the log path are in the record. The record gives no tests, failures, or errors count, no JUnit XML path, and no quoted top stack frame. The spec asks for all three.
- **R2, checklist tick.** Not supported. The step "run executed; outcome read from fresh JUnit XML" is ticked, but the repository holds no XML counts or path behind it. Untick it, or add the counts (see R1).
- **R3, failure text.** Partly supported. Line 120 at the 15319 head is `if (res.getException() != null) {`, and no null-response guard comes before it. The record's phrase "thrown from SolrResponse.getException on a null response" names a top frame that the repository cannot show. If the body is null, the top frame may be `SolrResponse.java` line 49, with line 120 as its caller. Replacement: "failed with a NullPointerException; the caller frame is `ExactStatsCache.doMergeToGlobalStats` (`ExactStatsCache.java` line 120)."
- **R4, the 8051 line.** Not supported as written. `receipts/SOLR-8051.md` cites line 117 for the same `res.getException()` call on its pre-fix base. The verdict says "at the same line". Replacement: "The 8051 receipt cites line 117 for the same call on its pre-fix base; on the 15319 head it is at line 120, because the branch adds lines above it. The statement matches; the line number does not."
- **R5, adaptations.** Not verifiable here. The record says no adaptation was recorded. Keep that wording; do not write "compiled unchanged" unless the vm2 log confirms it.

To complete the record, the main side needs the vm2 JUnit XML for `TEST-org.apache.solr.search.stats.ExactStatsCacheMergeTest.xml`. Fill this line under the verdict, from the XML, not from memory: "JUnit XML `<vm2 path>`: tests=`<n>` failures=`<n>` errors=`<n>` skipped=`<n>`, read `<timestamp>`." If the values cannot be filled, untick the "fresh JUnit XML" item (R2).

## Part 2: the settled Limits bullet, `pr-drafts/search-components/SOLR-15319.md` line 39

- **L1, "still throws at the exception check ... line 120".** Holds. The caveat: line 120 dereferences only `res`, so the no-body throw rests on `receipts/SOLR-8051.md`, which says `getException` reads the body. No listed source shows that directly.
- **L2, "the same failure ... for the pre-fix SOLR-8051 tree".** Holds for the exception type and the method only. Do not add "same line" (see R4).
- **L3, process vocabulary.** Does not hold. `WORKFLOW.md` (the standing limits) says public text follows `pr-formula.md` with no internal process vocabulary. The bullet uses "disposition", "settles", and "A run confirms". Replacements are in the full bullet below.
- **L4, the follow-up offer.** Does not hold. The bullet dropped "A follow-up ticket and PR can be opened on request." The null-response path is a limit found during the work and left unfixed for scope. `pr-formula.md` section 4 asks for the offer in that case.
- **L5, the top-frame claim.** Does not hold as written. The repository cannot show "thrown by SolrResponse.getException" (see R3). "As recorded in the gate file" is internal process; drop it from public text.

Replacement bullet, replacing line 39 in full. Links are unchanged from the draft:

> - Not covered: a shard response with no body still throws at the exception check in [ExactStatsCache.java line 120](https://github.com/nick-boss-tech/solr/blob/4bda91f46fc18142809c919d7c563129fdf7fb96/solr/core/src/java/org/apache/solr/search/stats/ExactStatsCache.java#L120). This change leaves that path as it is. On this head, ExactStatsCacheMergeTest from the [SOLR-8051](https://issues.apache.org/jira/browse/SOLR-8051) branch shows that this path is the same case: it failed with a NullPointerException at line 120, the same failure recorded for the pre-fix SOLR-8051 tree. The fix for this shared path is on the SOLR-8051 branch, which moves the null-response skip ahead of the exception check. This PR does not carry that fix. This change will be merged after SOLR-8051, and it will be rebased onto that fix if the two edits to ExactStatsCache need to be reconciled. The test failure shows that the unfixed path is present on this tree. The rebase, not the test failure, decides how the two changes combine. A follow-up ticket and PR for this path can be opened on request.

## Process note: vm2 claims

- `claims/pool-vm2-gate-backlog-13186.md` names head `b436d90d2a88`, which equals the fork tip of `solr-13186-submit`.
- `claims/pool-vm2-gate-backlog-15863.md` names head `78dff1d52f6c`, which equals the fork tip of `solr-15863-submit`.
- Both job files exist. Both claims are vm2's, and this host did not run anything.

## Not done

- No edit to the draft, the gate file, or the receipt. The main side applies R1 to R5 and the replacement bullet.
- No PR, comment, Jira write, or email. No build, test, or gate. No vm2 log read.
