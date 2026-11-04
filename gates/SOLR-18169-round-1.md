# Gate: SOLR-18169 slim distribution examples, round 1

Status: OPEN. Implementation pushed; Linux proof pending. No PR, JIRA comment, GitHub comment, or PR creation is authorized by this job.

- Assignment: `assignments/solr-18169-slim-examples-round-1.md`.
- Claim: `claims/solr-18169.md` (Windows implementation; proof pending).
- Tags: gate, records.
- Intended host: any capable Linux gate host with JDK 21. Follow `hosts/pool-linux.md` and the host's own record. The live BATS/CLI check is only for a host that can run it without a root workaround.
- Fork: `nick-boss-tech/solr`, branch `solr-18169-slim-no-examples`.
- Candidate: `50117e60af3358c5f2e39f2bde9852817115c23d` (remote ref verified after push).
- Parent: `498a09aea97aac86c2cc7ac114113b946d249942` (`origin/main` at claim time).
- Modules: `solr/packaging`, `solr/core`. Focused test seed: `18169C0FFEE18169`.
- Gate claimant: unclaimed. Before claiming, refresh the fork ref and verify the candidate head. Run the detached tidy preflight at this exact head. If tidy changes files, commit and plain-push the formatting fix with Nick Shanin identity, then post a new gate pinned to the new head before running positive proof.
- Idempotency key: `SOLR-18169-round-1@50117e60af3358c5f2e39f2bde9852817115c23d`.
- Suggested log directory: `/workspace/gates/logs/<host>/solr18169-round-1/`. Preserve the exact command, stdout, stderr, exit status, fresh JUnit XML, and installed-tree listing for each leg.
- Deliverable: the gating host writes `receipts/SOLR-18169-round-1.md` with final head, parent, host/JDK, commands and statuses, test counts/skips, tree evidence, logs/XML paths, any live-check limit, and a PR description draft marked NOT POSTED. The gate job and claim are not edited.

## Required candidate proof

1. Use an isolated full Linux checkout/worktree at the candidate, not the Windows sparse checkout. Record clean status, exact head and parent, JDK path/version, runner path, and worktree. Follow the host disk/inode guards. Acquire `/workspace/tools/.gradle-$(hostname -s).lock` with `flock -n`; one Gradle build per host.
2. Run `./solr-gradle.sh tidy` before the proof legs. Require no formatting changes. If it changes files, follow the superseding-head rule above and rerun proof at the new head.
3. Run both distribution installs in one serialized invocation:
   `./solr-gradle.sh :solr:packaging:installSlimDist :solr:packaging:installFullDist`
   List the installed full and slim trees. Show `example/` absent from the slim tree and present in the full tree.
4. Run the focused CLI suite with fresh XML:
   `./solr-gradle.sh :solr:core:test --tests org.apache.solr.cli.TestSolrCLIRunExample -Dtests.seed=18169C0FFEE18169`
   Record executed/passed/failed/skipped counts from the fresh XML and preserve the test output log.
5. Run the required core check with Error Prone on compilation:
   `./solr-gradle.sh :solr:core:check -x test -Pvalidation.errorprone=true`
   Preserve the full log and exit status. Do not count a compile/setup failure as test proof.
6. Run `./solr-gradle.sh :solr:packaging:devSlim`. On a non-root host, run the slim development distribution's `bin/solr start -e techproducts`; capture the exit status and verify the helpful slim/full-distribution message. If the host runs as root or otherwise cannot run this live check, record the exact limit in the receipt. Do not use `-force` or another workaround. Do not report an unrun live check as passing.
7. Require a clean final tracked tree, verify the remote fork branch still equals the gated head, and record whether any leg was skipped or limited. Do not run root `check` or the ref-guide leg in this worktree because of the Antora trap.

The focused tests cover the default missing-example message, the explicit missing `--example-dir` validation, and the existing unsupported-example error when the default example directory is present. If any source or formatting change creates a new head, this job no longer proves that head; post a superseding gate before accepting its results.
