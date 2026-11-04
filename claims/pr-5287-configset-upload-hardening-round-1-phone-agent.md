# Claim: pr-5287-configset-upload-hardening-round-1-phone-agent

- Claimant host: phone agent (Linux VM), matching the board's phone-agent records.
- Status: DONE. Gate executed 2026-11-04T16:16:25Z to 16:47:20Z; verdict GATE FAILED at H1, receipt at `receipts/PR-5287-configset-upload-hardening-round-1.md`. Blockers: tidy reformatted one new test file at H1 (scoped repair pushed as `8899ebd9d7d8a9985aef5d3c29b5f9b5189d5ee9`, tidy re-verified clean there); both new symlink tests fail in their own setup at both legs and two seeds (test policy grants LinkPermission "hard" only; LocalFS test mixes default-filesystem and mock-filesystem paths), so neither symlink behavior is proven; S3 suite skipped 10/10 in both legs (Docker testcontainer, no Docker on this host). All other legs passed, including the fail-before discriminations for body/repository exclusivity and dot-directory imports, Error Prone, and root `check -x test` in a clone.
- Job: `gates/pr-5287-configset-upload-hardening-round-1.md`.
- Branch: `pr-5287-configset-source-validation` on nick-boss-tech/solr. Pinned head H1: `04e570d3426496aa8634ac03e556d45bc159af63`, re-verified against the fork remote at claim time. Contributor parent H0: `19f7c83812a4c81227601ff1abadca84beb05cb9`. PR 5287 live state at claim time: open, head unchanged at H0.
- Capability tags used: gate, records.
- Started: 2026-11-04T16:15:15Z (UTC).
- Checkout: task worktree `~/workspace/solr-worktrees/5287-gate` at H1 (detached tidy preflight first); step 5 root check runs in a normal clone at the same head, per this host's Antora worktree rule, and the receipt will disclose the layout.
- Runner: the host's standard gate runner, `~/workspace/tools/solr-gradle.sh` (Gradle 9.7.0, vendored JDK 21), serialized under the host build lock (`~/workspace/tools/.solr-test-queue.lock`). Logs and fresh JUnit XML retained under `~/workspace/tools/gate-5287/`.
- Seed: `5287C0DE`. Receipt reserved: `receipts/PR-5287-configset-upload-hardening-round-1.md`.
- Links: SOLR-18618, PR 5287. Category: existing PR follow-up. Siblings: none known. Supersedes: none. No competing Linux gate claim or receipt was present on the board at claim time.
