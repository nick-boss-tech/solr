# Claim: PR 5238 client search dispatch, gate round 1 (vm2)

Status: CLAIMED

- Claimant host: vm2, per `hosts/vm2.md`.
- Tags: gate, premise-run, review.
- Started: 2026-10-27T02:20Z (UTC).
- Job: `gates/pr-5238-client-dispatch-round-1.md`. Candidate F1: `ede5646e2b2281bf4fc1483c2867f0f047088527` on `nick-boss-tech/solr:solr-11994-pr5238-submit`, one commit above P.
- Negative base P: apache/solr PR 5238 head `cc9ea9b1b6b153317217ee458ff6287b5e24cc65`.
- Verified before claiming: live fork tip equals F1; `P..F1` is one commit (Nick Shanin, no trailers), seven files, 132 insertions and 8 deletions, matching the job.
- Competing claims: none for PR 5238 on the board besides the Windows implementation claim.
- Constraints: no GitHub, PR, or Jira write. No fork push unless tidy changes files and the gate requires a superseding head. No force push. Commits as Nick Shanin with no trailers.
- Host resources: JDK `/workspace/jdk/jdk-21.0.12.1+1`, Gradle home `/workspace/gradle-home`, build lock `/workspace/tools/.gradle-vm2.lock`.
- Plan: detached tidy preflight at F1 under the build lock, then focused and neighboring suites, then the full clone root check, then the negative proof on P with only the three test files overlaid.
