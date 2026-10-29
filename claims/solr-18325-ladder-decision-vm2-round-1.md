# Decision: SOLR-18325 binary compatibility (vm2, round 1, decision ladder)

Status: DECIDED at rung 4, recorded here. Implementation not started; no claim on any head yet.

## Rung 1: code and history

- The released 10.0.0 consumer binary calls `HttpJdkSolrClient$Builder.withConnectionTimeout` through a top-level `HttpSolrClientBuilderBase`.
- The top-level class was removed by SOLR-17968 (`8d3f995c6ac`, #3829, 2026-05-17), which deleted `solrj/impl/HttpSolrClientBuilderBase.java` and nested the base as `HttpSolrClient.BuilderBase`. Upstream main `e59ce3c97d` has no top-level file.
- So the binary break comes from a source move on main, not from the SOLR-18325 prototype.

## Rung 3: stated intent

- Owner, board update: binary compatibility is a requirement.
- Owner, board update: prefers additive compatibility that preserves the old contract.
- Settles the first question in the owner assignment: option C (narrow the verdict to source compatibility) is out.

## Rung 4: choice between the remaining options

- Option A: restore a top-level `HttpSolrClientBuilderBase` with the released members. Nested base stays as it is on main.
- Option B: a top-level shim beside the nested base, kept in sync with it.
- Chosen: A. The released binary references this exact type name and members, so restoring the released type is the smallest change that makes `binaryHttp` pass. B adds a second base to keep in sync for the same effect.
- Scope: the change is in SolrJ source outside the prototype. The owner's additive preference covers it. If the owner wants this as a question instead, it moves to NEEDS-OWNER.

## Next step (not run)

- Implement A on a new head on the fork branch.
- Run the full gate on that head: tidy, Error Prone compile, `binaryHttp` leg, focused SolrJ, `check -x test`.
- Record the new head in a superseding gate; do not reuse `8bfca8edd30`.

## Limits

No GitHub, PR, or JIRA action. No head pushed in this decision.
