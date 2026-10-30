# Gate: PR 5265 split-shard follow-up, round 1 (exact head)

Status: CLAIMED by phone agent (see `claims/pr-5265-split-shard-followup-round-1-phone-agent.md`); running.

- Assignment: `assignments/pr-5265-split-shard-followup-round-1.md`.
- Fork: nick-boss-tech/solr, branch `solr-16398-split-shard`.
- Candidate (gated head): `0495ffa2b00772189d2f92d83b32140f1b5e8b39`, read back from the remote after a normal fast-forward push. Head chain: `6c56a18236` (PR head as opened) → `7e0419e399` (request-body mapping coverage) → `0495ffa2b0` (changelog title narrowed to split shard).
- Seed: `16398C0DE`. Host: phone agent (this VM), JDK 21 via `~/workspace/tools/solr-gradle.sh`, serialized Gradle lock.
- Logs: `~/workspace/tools/gate-5265-followup/` on the phone agent host.
- Deliverables: `reports/pr-5265-split-shard-followup-round-1.md` and, only if every step passes, `receipts/pr-5265-split-shard-followup-round-1.md`. No PR body edit, GitHub comment/review, or JIRA write.

## Required proof

1. Tidy leaves a clean tree at the candidate head; the narrowed changelog parses as YAML.
2. Error Prone compile with OpenAPI client generation: `:solr:api:compileJava :solr:solrj:compileJava :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
3. Focused tests at the candidate head with fresh JUnit XML: `org.apache.solr.handler.admin.api.SplitShardAPITest`, `org.apache.solr.handler.admin.TestCollectionAPIs`, plus the new `org.apache.solr.handler.admin.api.SplitShardMessageMappingTest` from this patch, seed `16398C0DE`.
4. Module check at the same head: `:solr:core:check -x test`.
5. `git diff --check` clean; pushed branch head still equals the tested SHA at the end.

PASS requires all five steps at `0495ffa2b0`. If tidy changes anything, the formatting repair is committed and pushed, this job is superseded by a new one pinned to the new head, and proof reruns there.
