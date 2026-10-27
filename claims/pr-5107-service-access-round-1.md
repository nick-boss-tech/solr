# Claim: pr-5107-service-access-round-1 (vm2)

- Claimant host: vm2, matching `hosts/vm2.md`.
- Status: ACTIVE. Gate job `gates/pr-5107-service-access-round-1.md`; superseding gate for the repaired head `gates/pr-5107-service-access-superseding-5ff8f32.md`.
- Started: 2026-10-27T09:12Z (UTC).
- Capability tags used: gate.
- Candidate: `0c53fa4c6b36090d4cd7c9870e1259860036d04c` on `nick-boss-tech/solr:solr-8442-pr-5107-submit`, parent live PR 5107 head `e8a488ec4f70`. Checked before claiming: fork candidate and PR head unchanged.
- Preflight (detached pool `gate-preflight.sh`): exit 3, tidy changed three files, formatting only (`/workspace/gates/logs/vm2/pr5107-preflight/tidy.patch`).
- Repair: applied the tidy patch as one commit `5ff8f324ef3c965fe2bbff9803b8990ab3d2945a` (Nick Shanin, no trailers), fast-forward on the fork branch. Gate proof is for that repaired head.
- Limit: `IncludeFileEditorTest.testDifferentOwnerIsPreserved` needs root in a disposable container. This host is not root, so that leg is recorded as not run unless a container is available.
- Constraints: no GitHub PR, comment or Jira write. No force push. No changelog.
- Heartbeat: 2026-10-27T09:12Z
