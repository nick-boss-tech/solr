# Premise job: is the SOLR-15319 null-response path the SOLR-8051 case?

- Branch under test: solr-15319-submit. Head: 4bda91f46fc18142809c919d7c563129fdf7fb96 (live tip, matches receipts/SOLR-15319.md).
- Reference branch: solr-8051-submit at e50a2aa3437cb14ce4310a5025ad783fc93a97b1 (gated green, receipts/SOLR-8051.md).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: FAILED (run 2026-10-12T23:45Z, runner gates/g15319-gate.sh, head 4bda91f46fc18142809c919d7c563129fdf7fb96, head rc=1, log logs/vm2/15319-head.log). ExactStatsCacheMergeTest.testShardWithoutResponseIsSkipped failed with java.lang.NullPointerException at ExactStatsCache.doMergeToGlobalStats (ExactStatsCache.java:120, SolrResponse.getException on a null response), the pre-fix signature this job predicted. Verdict not recorded; left for the main agent. Not relaunched.
- Capability tags: gate.
- Job type: premise run only. No branch edit, no gate. Posted by the Group O settlement (2026-10-11): pr-drafts/search-components/SOLR-15319.md states in Limits that a shard response with no body still throws at the exception check in ExactStatsCache.java, and that "it is the case in SOLR-8051". That sameness was traced by reading, never reproduced; this job settles it by run, and the draft's disposition follows the result.

## Question

On the SOLR-15319 tree, does a shard response with no body fail in `ExactStatsCache.doMergeToGlobalStats` the way it does on the pre-fix SOLR-8051 tree (NullPointerException in the global stats merge, because the exception check runs before any null-response guard)?

Reading evidence for the spec: at the 15319 head, `doMergeToGlobalStats` calls `res.getException()` with no null check on `res.getResponse()` before it, the same pre-fix order the 8051 receipt records. The 8051 branch's fix adds the null-response read and skip before that check.

## Spec

- Copy only the test class `org.apache.solr.search.stats.ExactStatsCacheMergeTest` (solr/core/src/test/org/apache/solr/search/stats/ExactStatsCacheMergeTest.java) from the 8051 head into a worktree of the 15319 head. Do not copy any production file.
- Run that class in :solr:core at the 15319 head, seed 15319C0FFEE15319. Read the outcome from fresh JUnit XML.
- Expected if the case is the same: the test fails with the NullPointerException in the global stats merge, matching the 8051 premise failure recorded in receipts/SOLR-8051.md. If it passes, the 15319 path is not the 8051 case as written.
- If the test does not compile against the 15319 tree, adapt it minimally (imports and API names only, same scenario and assertions), record the adaptation here, and run the adapted test. If no faithful adaptation exists, record the job INCONCLUSIVE BY COMPILE with the exact failure.

## Deliverable

- Record the verdict here (SAME CASE or NOT THE SAME CASE, or INCONCLUSIVE BY COMPILE), with the run counts, the failure text if any, the seed, and the log path.
- Append the verdict to receipts/SOLR-15319.md in the same push that marks this job DONE. The main side then settles the draft's Limits line and the 8051/15319 landing order from the verdict; do not edit the draft in this job.

## Step checklist

- [ ] test class copied onto the 15319 head (adaptations recorded, if any)
- [ ] run executed; outcome read from fresh JUnit XML
- [ ] verdict recorded here and in receipts/SOLR-15319.md; job and claim DONE
