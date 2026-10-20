# Gate job: SOLR-18297 review pass 1 (Jetty 12.1.14 upgrade)

Status: DONE (GATE GREEN at 5cdc657470, vm5, 2026-10-20T14:00Z)

- Branch: solr-18297-jetty-12-1-14. Head: `5cdc65747028cefeaf35aad9df23ab1b71661960` (ls-remote 2026-10-20T13:45Z).
- Prior gated head: none (the branch's own draft gate ran only StatusToolTest on compile). Merge-base with fork pin: `498a09aea9`.
- Intended host: vm5. Capability tags: gate.
- Module: `:solr:core`, `:solr:server`. Focused class: `org.apache.solr.cli.StatusToolTest`. Seed: default (not pinned; single class).
- Diff scope against `498a09aea9`: one commit, 68 files (version catalog, four module lockfiles, license sha1 files, `pr-draft/SOLR-18297.md`).
- Why: pass 1 of the review round for this unit; first real gate on this branch.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [ ] step 0 changelog: not applicable (no changelog file in the commit)
- [ ] step 1 tidy: not run in this pass (no Java changed); the pre-merge gate must run it.
- [x] step 2 compile: `:solr:core:compileTestJava :solr:server:compileJava` BUILD SUCCESSFUL, rc=0
- [x] step 3 proof leg: not applicable to a version bump
- [x] step 4 focused run: `StatusToolTest` tests=1 failures=0 errors=0 skipped=0 (fresh JUnit XML)
- [x] step 5 module check: not run in this pass (the full core and server suites are the owner-stated pre-merge gate)
- [x] lockfile gap recorded (14 modules still on 12.1.12)

Not run: the full core and server test suites, and the module check. The draft PR says they must run before merge.

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-18297-jetty-12-1-14.md`.
- Receipt section in `receipts/SOLR-18297.md`, top section.
