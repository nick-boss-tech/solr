# Record job: SOLR-16267 def() behavior check against Lucene 9.12.3

- Branch: solr-16267-submit. Head: 8f4b0c6d2fb0c89c833512de502f39eb1bec560e (live tip, verified by ls-remote at posting, 2026-10-11; GATE GREEN at this head per receipts/SOLR-16267.md).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: DONE by vm1, 2026-10-12. Verdict in receipts/SOLR-16267.md: DefFunction consults the wrapped source's exists() in both Lucene 9.12.3 and 10.4.0; the draft's def() claim stands for both versions, no narrowing for 9.x. Claim: claims/pool-vm1-SOLR-16267-def-check.md.
- Capability tags: investigation, record.
- Job type: record only. A code-reading check against the Lucene artifacts, recorded in receipts/SOLR-16267.md. No branch commit, no push, no gate run.
- The claim to settle: pr-drafts/query-parsing/SOLR-16267.md says of the branch's new `exists()` overrides on the single and two argument math wrappers: "The same answer reaches `exists()` and `def()`, as the changelog says. Those results change for any sparse field used inside these math functions." The changelog title names `def()` too. `def()` is Lucene's DefFunction, whose source is not in the Solr tree (reports/query-parsing-round-1-q7.md, finding 15: not confirmed that it calls `exists()`). The gate at the head ran against Lucene 10.4.0 (main's pin). Whether the claim holds on Lucene 9.12.3, which branch_9x pins, is the check receipts/SOLR-16267.md still owes (reports/draft-fidelity-fixes-application-response.md, item F24).
- The check: read DefFunction's implementation in Lucene 9.12.3 and in Lucene 10.4.0. On vm1 the 9.12.3 jars are at ~/workspace/tools/lucene-9.12.3/ and the 10.4.0 jars are in the Gradle cache; a claimant on vm2 records which artifacts it read instead. For each version, settle whether DefFunction's value path consults the wrapped ValueSource's `exists()` (so a wrapped math function whose `exists()` now reports false makes `def()` return its default), or whether it decides on another signal. Quote the deciding lines or the decompiled shape for each version.
- If reading leaves the 9.12.3 behavior ambiguous, a scratch probe outside the branch is allowed: a small run against the 9.12.3 jars exercising `def()` over a wrapped source with a false `exists()`. Record the probe's shape and outcome if one runs; a reading that settles the question needs no probe.
- The record: write the verdict into receipts/SOLR-16267.md, one part per Lucene version, naming the artifacts read. End with a sentence stating whether the draft's claim stands for both versions or must be narrowed for 9.x, and if it must be narrowed, quote the draft sentence that changes. The draft itself is not edited by this job.
- On completion: receipts/SOLR-16267.md carries the verdict, this job is marked DONE, and the claim is marked DONE.

## Step checklist

- [x] DefFunction read in Lucene 9.12.3 (artifacts named); whether its value path consults exists() settled
- [x] DefFunction read in Lucene 10.4.0 (artifacts named); same question settled
- [x] scratch probe run and recorded, if the reading left 9.12.3 ambiguous (not run: the bytecode reading was unambiguous in both versions, recorded in the receipt)
- [x] verdict written to receipts/SOLR-16267.md, including whether the draft's def() sentence stands or needs narrowing for 9.x
- [x] job and claim marked DONE
