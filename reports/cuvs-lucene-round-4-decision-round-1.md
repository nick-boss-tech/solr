# Decision: cuvs-lucene round 4 sequencing (phone agent, 2026-10-24)

Status: DECIDED under the owner's standing delegation of project-substance decisions. The owner asked for the decision after the nine round-4 assignments were posted.

## State relied on

The production compatibility implementation already exists upstream as NVIDIA/cuvs PR 2702 (authored by imotov). The owner's contribution is test additions, proven by two independent GREEN CPU gates on vm2 (receipts/CUVS-LUCENE-PR2702-CPU-ROUND-2.md and receipts/CUVS-LUCENE-BACKCOMPAT-ROUND-3.md). The round-1 helper-only production patch is not carried forward.

## Decision

1. Steps 01 (consolidate) and 07 (Solr docs reconciliation, drafts only) proceed now, in parallel. Both are OPEN with no prerequisites.
2. The chain then runs 02 (submission package) after 01, and 03 (independent readiness review) after 01 and 02, with no further decision needed at any of those steps.
3. Nothing public happens before step 04. Step 04 is the owner's: he reviews the READY package and gives or withholds the exact posting approval for submitting the test contribution upstream. That approval is not delegated.
4. Consolidation of the byte-identical, disjoint test patches is verified by application and blob review, not by repeating the two green CPU gates. A changed test, production source, API/dependency set, or source base triggers a new exact-input CPU job for the affected legs, per the roadmap.
5. Steps 05 (review follow-ups) and 06 (release evidence) stay armed and dormant; their triggers are external (real upstream feedback; a published candidate release). Silence is not a trigger.
6. Step 08 (Solr adoption) stays parked until a compatible cuvs-lucene release exists and the owner activates the Solr scope. Step 09 (GPU acceptance) stays parked until a capable GPU host exists; no host claims it to report the missing prerequisite, and CPU evidence is never presented as GPU proof.
7. The SOLR-18556 CuVSCodec guard remains deferred pending upstream's answer, as the roadmap records. Step 07 reconciles drafts with the existing closure route and does not reopen the guard question.
