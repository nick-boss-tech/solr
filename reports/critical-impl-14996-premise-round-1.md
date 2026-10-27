# SOLR-14996 premise run, round 1 (vm2)

Verdict: NOT ESTABLISHED AS A DEFECT. No implementation. Open maintainer question below, so the item is NEEDS-OWNER and the claim stops here.

- Claim: `claims/critical-impl-14996-round-1-vm2.md`. Assignment: `assignments/critical-impl-14996-round-1.md` (step 1 only).
- Base: apache/solr `upstream/main` `ee2f1bf2e01`. Scratch probe `Probe14996Test` (not pushed). Seed `14996C0DE`. Logs under `gates/logs/vm2/s14996/` (probe.log, probe-status.txt). Probe data: 400 docs, 40 users (`user_s`), job type `TYPES[(i/7)%5]` so each user mixes job types. First run used one job type per user and was discarded as degenerate.

## Probe output (system-out of the test, probe rc 0)

- `collapse+tagged+ex` (fq collapse on user_s, fq tagged thinker, facet ex=sel on job_s): numFound=40, facet job_s = runner 12, developer 7, digger 7, ninja 7, thinker 7.
- `collapse+untagged` (fq collapse only, facet job_s): numFound=40, facet job_s = runner 12, developer 7, digger 7, ninja 7, thinker 7.
- `nocollapse+tagged+ex` (fq tagged thinker, facet ex=sel): numFound=77, facet job_s = developer 84, runner 84, digger 78, ninja 77, thinker 77.

## Reading

- Without collapse, thinker facet (77) equals numFound (77). The ex facet is consistent with the filtered count.
- With collapse, numFound counts groups under the thinker filter (40 users have a thinker doc). The thinker facet counts group heads whose head document is a thinker (7). The head is chosen from the filtered set when the filter applies and from the unfiltered set under ex. The ex facet (7) equals the untagged collapsed facet (7), so ex has no effect on the collapsed facet. That is the expected consequence of ex over collapsed heads, not a counting error shown by this run.
- The ticket's own example has the same shape: numFound 850 (groups), thinker facet 195 (heads).
- The ticket expects either thinker=numFound=195 or thinker=numFound=850. Neither is what collapse group-head semantics produce for `numFound` and a head-based facet. The probe does not show a counting defect.

## Owner question (NEEDS-OWNER)

For a collapsed result with `ex` on the tagged field, should the facet count reflect group heads over the unfiltered collapsed set (current behavior, consistent with the untagged facet), or the group count under the tagged filter (what the ticket expects)? Only the owner can decide whether the ticket's expectation is a bug or a semantics change.

## Not done

- No step 2 implementation, no branch `solr-14996-submit`, no gate request.
- No PR, Jira, or GitHub write.
