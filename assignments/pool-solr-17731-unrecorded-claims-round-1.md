# Pool assignment: SOLR-17731 unrecorded code claims, round 1

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: one read-only subagent. Claim: this round runs under the standing coordinator claim, claims/coordinator-review-rounds.md.

## Purpose

`pr-drafts/core-admin/SOLR-17731.md` has two sentences that the receipt does not record. The moved-heads round left them as notes for the main side, and this round settles them under the standing ladder.

## The two sentences

- Line 16: "The lookup logic is the same as before."
- Line 39: "The alias response still uses the existing wire key `name` for the alias field. This change does not touch it."

## Ladder

1. Get the information first. At the head `18165d35a7e3761afb11ecb26218a4b3c9525dbe` (fetched locally as origin/solr-17731-submit) and the base `e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f`, read the alias lookup code and the alias response serialisation, and decide whether each sentence is true.
2. If a sentence is true, keep it and add the file and line that show it, as a link at the head or base as the sentence requires.
3. If a sentence is true but not needed for the PR, drop it.
4. If a sentence cannot be verified from the code, defer it as a named Limits follow-up with a stated plan, or drop it. Do not leave it as an unverified claim.

## Deliverable

`reports/solr-17731-unrecorded-claims-round-1.md`: the verdict for each sentence (KEPT WITH LINK, DROPPED, or DEFERRED), the exact before and after text, the code lines read, and the ladder rung used. If any change is needed, apply it to the draft in the same push and say so in the report, limited to these two sentences.

Rules: read-only except for the two sentences. No builds, no tests, no Gradle, no posting. WORKFLOW.md binds (claim before work, hourly heartbeats, mark the claim DONE in the same push as the deliverable).
