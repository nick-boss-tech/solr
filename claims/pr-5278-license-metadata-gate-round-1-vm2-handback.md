# Handback: vm2 license gate for PR 5278, round 1

- Claim released: `claims/pr-5278-license-metadata-gate-round-1-vm2.md` (vm2, started 2026-11-02T00:44:29Z).
- Reason: the VM crashed during the gate run, after tidy and before the licenses step completed. Gate steps from this claim are not complete, so no receipt is written by this host.
- Pinned head H: `cb8c6ab2752b5202b52c7479b9b77ee7806b350e`, parent P `82f60b1b7976c187541a6f6808a781bb7f8b9228`. Fork ref re-read at handback: H, unchanged.

## Completed on vm2 (rc=0 unless noted)

- `tidy`: rc=0, tree clean, `git diff --check` rc=0. Log `pr-5278-license-metadata-round-1/tidy.log`.
- Preflight (`gate-preflight.sh`): tidy clean at H, rc=0.
- Committed JCL license: SHA-256 `59899c6091b540582ed617e8eeaac4919dc985ccfc35459ee9752b699be5205b` matches the required value (after line-ending normalization, no change needed). The stale `jcl-over-slf4j-LICENSE-MIT.txt` is removed in the PR diff.

## Not completed (remaining legs)

1. `./gradlew --no-daemon :solr:server:licenses :solr:core:licenses --info`, with full log. Required: JCL selects ASL, exactly one matching JCL license file plus the empty notice companion, no MIT JCL path, API and JUL bridge stay MIT. The interrupted attempt log is `licenses-interrupted-vm-crash-3.log` and has no `licenses_rc`; do not treat it as evidence.
2. Manual JAR comparison: the 2.0.20 JAR is not yet located in `/workspace/gradle-home` (cache search was slow over NFS and found nothing on this host). Its checksum file `solr/licenses/jcl-over-slf4j-2.0.20.jar.sha1` exists in the PR tree. Compare the license text inside the JAR (or its tag) after LF normalization.
3. `./gradlew --no-daemon check -x test`, with full log.
4. Final `git status` and `git diff --check`, and remote reads of the fork branch and PR 5278 head.
5. Receipt `receipts/PR-5278-license-metadata.md`.

## PR 5276 receipt also pending

PR 5276 at `5f9613e13865` completed all five gate legs on vm2 (claim `claims/pr-5276-license-metadata-gate-round-1-vm2.md`):

- tidy rc=0, diff check rc=0 (`pr-5276-license-metadata-round-1/tidy.log`)
- licenses rc=0; four new artifacts validated: CEL common, protobuf, runtime as ASL; ThreeTen Extra as BSD; each with its notice companion (`licenses.log`)
- GCS test rc=0: `GCSBackupRepositoryTest` 12 tests, 0 failures, 0 errors, 0 skipped; fresh XML in `xml-gcs/` (SHA-256 prefix archived)
- `check -x test` rc=0 after the second crash; the first partial run is `check-interrupted-vm-crash-2.log`
- final status clean, `final_diffcheck_rc=0`

The committed CEL and ThreeTen license hashes match the implementation report (`cfc7749b96f6…`, `decba4464d41…`). The receipt still needs the copy-rule evidence and the remote reads, then the PR body draft. The next host or this host can write it.
