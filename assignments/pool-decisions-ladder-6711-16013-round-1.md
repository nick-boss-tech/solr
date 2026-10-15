# Pool assignment: resolve the open decisions in the SOLR-6711 and SOLR-16013 drafts under the ladder, round 1

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: one read-only subagent. Claim: this round runs under the standing coordinator claim, claims/coordinator-review-rounds.md.

## Purpose

Two drafts raised questions for the owner. Under the standing ladder and the decision default, most of them settle without Nick. This round settles each one, records the rung used, and only sends a question to Nick if the ladder cannot settle it.

## Items

1. **SOLR-6711, the Choice on opt-in persistence** (`pr-drafts/replication-backup/SOLR-6711.md`, section "A choice to check"). The material records a recommendation to keep persistence opt-in, adopted under the owner's decision default. Confirm the recorded recommendation in `material/replication-backup-round-1-answers.md`, and confirm the Choice poses the question with opt-in as the implemented position. If the recommendation is recorded, the decision is taken; the Choice stays as a question for the maintainer and the draft is unchanged. Record the rung.
2. **SOLR-6711, the unverified restart sentence** ("Turning off polling lasts only until the next restart or core reload."). Verify it from the code at head `73c5e6e0d243` and base `cabedd1d968`: the poll flag is in memory and a reload creates a new handler. If the code shows it, keep it and cite the lines. If not, rewrite it to what the code shows.
3. **SOLR-16013, the behaviour claims** (`pr-drafts/solrcloud/SOLR-16013.md`): "Stopping a node can take longer, because `close()` returns only when the Overseer close is done" and "The election nodes are released after that wait". The claims follow the code: check `ZkController.close()` at head `408317d7e241` (the close path lines the draft links). A behaviour statement follows the code evidence, so if the code shows it, keep it and record that no owner confirmation is needed.

## Ladder

Decision default first: a recorded recommendation is adopted. Where none is recorded, take the more thorough option. Behaviour statements follow the code. Only a retire call, a send, a filing or a PR is Nick's decision, and none of these items is one of those.

## Deliverable

`reports/decisions-ladder-6711-16013-round-1.md`: for each item, the decision or verdict, the evidence (file and line), the rung used, and the exact text change if any. Apply any text change to the draft in the same push and say so. Do not send a question to Nick unless the ladder cannot settle the item, and say so explicitly if you do.

Rules: read-only except for the text changes named above. No builds, no tests, no Gradle, no posting. WORKFLOW.md binds (claim before work, hourly heartbeats, mark the claim DONE in the same push as the deliverable).
