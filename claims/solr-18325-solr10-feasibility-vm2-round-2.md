# Claim: SOLR-18325 Solr 10 client feasibility, round 2 (vm2)

Status: CLAIMED

- Claimant host: vm2.
- Tags: implementation, gate, premise-run, review.
- Started: 2026-10-27T07:24Z (UTC).
- Supersedes: `claims/solr-18325-solr10-feasibility-vm2-round-1.md` (round 1 stays as the record; its gate ran at `ed07d0dd67e` and failed root check).
- Fixed head: `8bfca8edd30acf11bdf9d6a733312e97f99ec78a` on `nick-boss-tech/solr:solr-18325-submit`, chain above `f45b38d5f6c0`:
  - `958a8411cfe` tidy formatting, six source/test files.
  - `35a35d69900` dependency locks, 26 lockfiles.
  - `ed07d0dd67e` Error Prone fix in `CoreAdminJacksonCompatibilityTest`.
  - `00be45f` / `8bfca8e` source-pattern repair: `JacksonBehavior.java` explicit type, then google-java-format of that file (its 11 pre-existing format violations included; formatting only).
- Why round 1 is superseded: the root check at `ed07d0dd67e` failed in `validateSourcePatterns` on `JacksonBehavior.java:48` (diamond with `var`).
- Constraints: no GitHub, PR, or Jira write. Fork pushes only to `solr-18325-submit`. No force push. Commits as Nick Shanin, no trailers.
- Build lock: `/workspace/tools/.gradle-vm2.lock`, via detached flock chain.
