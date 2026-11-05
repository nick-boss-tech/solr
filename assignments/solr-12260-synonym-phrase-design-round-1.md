# SOLR-12260: synonym-aware phrase boosting premise and bounded design, round 1

Status: OPEN

- Category: owner-directed adjacent investigation from PR 5294. Tags: implementation, review, records, linux, probe.
- Intended host: capable Linux CPU host, JDK 21, standard runner, resource guards and one build under the lock. Windows cannot execute the next required probe.
- Exact source input P: `nick-boss-tech/solr:solr-18624-flaky-multiword-synonyms` at `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`. Preserve the live PR branch and test fix.
- Related tickets: SOLR-12260; explicit-slop SOLR-12532/11022 and empty-clause SOLR-18314 are separate source candidates with their own gates.
- Suggested claim: `claims/solr-12260-synonym-phrase-design-round-1-<host>.md`.
- Required report: `reports/solr-12260-synonym-phrase-design-round-1.md`; execution receipt: `receipts/SOLR-12260-synonym-phrase-design-round-1.md`.

## Question and decision ladder

Rung 1 identifies the mechanism: addPhraseFieldQueries selects unquoted/unfielded raw clauses, then addShingledPhraseQueries shingles them before per-field analysis. Thus bar foo and tropical cyclone foo can generate different pf2/pf3 inputs despite synonym-equivalent main matching. Quoted clauses are skipped and the remaining raw clauses can become neighbors. The round-2 SOLR-18624 receipt already shows the final query's tie; do not repeat that result as a newly reproduced production defect.

Rung 2 is required before selecting a general relevance change: separate raw-token shingling, graph-analysis paths, position holes and intentional syntax exclusion. A ranking asymmetry alone does not establish which scoring contract should replace it. Read SOLR-12260's current intent and the query guide, source history and related tests; then record rungs 3/4 as applicable. Do not turn a choice settled by evidence into an owner question. No public commitment, new parameter or global ranking policy is authorized by this probe assignment.

## Bounded execution

Refresh live source/ref, job, claims and receipts; first claim wins. Use a clean full Linux checkout at P and standard detached tidy preflight before claiming. Record all source/test blob IDs, host/JDK/runner, UTC times, seed, commands, raw outputs and fresh XML. Follow host resource guard and WORKFLOW repair rules.

1. Reuse TestMultiWordSynonyms' schema, synonyms and existing four-document fixture. In disposable diagnostic tests capture IDs, scores, parsed main and boost queries for equivalent inputs bar foo and tropical cyclone foo, both phrase directions, with pf, pf2 and pf3 individually and together. Include ps=0/2, sow=true/false, an analyzer without the synonym graph, and a configured second pf field with different analysis. Keep main matching separate from boost ranking.
2. Probe quoted-clause boundaries using cat "allergic reaction" dog and a minimal matching/nonmatching fixture. Compare retained raw clauses versus their original positions. Determine whether an apparent cat-dog phrase is intentional documented filtering or an accidental bridge; quote exclusion must not silently become a full query-syntax behavior change.
3. Retain a graph/stopword/position-gap example and a raw single-token multi-token-analysis example, so a proposed fix cannot discard meaningful analysis or change all one-token boosts. Control for ties through explicit score comparisons; no arbitrary winner assertions.
4. Preserve exact temporary diagnostic diff and run it once per predeclared case at a recorded fixed seed. If competing explanations remain, specify the smallest additional discriminating case before executing it. No loop-until-failure, synthetic compile failure or skipped test counts as runtime proof.
5. Recommend a bounded implementation after the matrix: define the shingles' unit, per-field analysis ownership, stopword gaps, graph path behavior, quote/field boundaries and compatibility consequences. Prefer existing graph/query-building infrastructure over a second analyzer implementation. Explain whether a small independent boundary repair is possible while the general synonym scoring change remains separate. If evidence settles such a repair, prepare a concrete source/test patch in a distinct fork branch preserving P ancestry, commit/plain-push and independently verify its head, and post a new exact-head Linux gate before calling it proven. Otherwise report the remaining choice with two concrete options and strongest-evidence recommendation; do not invent a compatibility flag without evidence.

This assignment is not a build gate for the other two candidates and does not require re-running their suites. No Gradle on Windows, no changes to PR 5294, no public PR/body/comment/Jira action, force push or upstream/contributor-branch write. A handoff is not an execution result. Report which assertions are discriminated, which are coverage pins and which semantics remain a maintainer contract choice.
