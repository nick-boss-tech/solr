# Gate job: SOLR-17842 placeholder fix, light gate at the new head

- Branch: solr-17842-submit. Head: 008973f6313374517e20a0c347d0fb4885afe1dd (live tip, verified by `git ls-remote origin refs/heads/solr-17842-submit` on 2026-10-13; the gated head in receipts/SOLR-17842.md). Merge-base with upstream main: 14c7aac0d151402b00259e2fb9bf5eed7049ec5d.
Status: DONE (GATE GREEN at 9da1242b1d29)
- Intended host: any capable Linux gate host. Windows never claims build work.
- Capability tags: gate.
- Job type: a docs-only branch commit (the placeholder fix), then the light gate at the new head. No Java changes.
- Spec: `reports/build-docs-convergence-round-2-response.md`, SOLR-17842. Decision record `reports/build-docs-misc-round-1.md`, item 10: keep the ticket open (recommended, adopted), and the placeholder fix needs a branch commit by the branch's owner.
- The defect: the example command at `solr/benchmark/docs/release-benchmarking.md` line 69 reads `-rff work/jmh-<build>.json`. An unquoted `<` in a shell command is read as input redirection, so the example cannot run as written. The fix replaces the placeholder with `BUILD`, and the sentence before the block names `BUILD` as the placeholder for the build's name. Two lines change; the line count does not.
- The fix: `gates/SOLR-17842-placeholder-fix.patch`. Patch check, done 2026-10-13 read-only: the patch applies to the live-tip blob and reproduces the edited file exactly, with LF endings (checked with core.autocrlf off).
- Push: the commit is a branch commit by the branch's owner (decision 10). This review assignment does not push it. When applied, it pushes as a plain fast-forward from 008973f6313374517e20a0c347d0fb4885afe1dd. Any other head is recorded in this file before the gate runs.
- Proof shape: docs-only; no Java test applies. The changelog step and the module check are not applicable (the branch adds no changelog file and no Java; the light gate in receipts/SOLR-17842.md recorded them the same way).
- Gate steps (light gate, as in receipts/SOLR-17842.md):
  1. Apply the patch at the live tip. Confirm `git diff --stat 008973f6313374517e20a0c347d0fb4885afe1dd HEAD` lists the one file, and that the example line reads `-rff work/jmh-BUILD.json`.
  2. Run tidy at the new head (`./gradlew --no-daemon tidy`). Restore any tree file the run changed (the light gate restored gradle/libs.versions.toml) and leave the tree clean.
  3. Content check against upstream main: the eight classes named in the guide and `solr/benchmark/jmh.sh` exist (the 2026-10-13 tree listing at d38e0f1623 already shows them), and the README link at line 201 resolves to `solr/benchmark/docs/release-benchmarking.md`.
  4. The example command is not run, because it runs benchmarks. Syntax check only: with `BUILD` substituted, the block parses in zsh if zsh is available on the host.
- Receipt: on green, refresh `receipts/SOLR-17842.md` with the new head, the patch, the tidy result and the check results. Then update `pr-drafts/build-docs/SOLR-17842.md`: move the head links and the Proof's head and tidy line to the new head, and remove the HOLD line only once the commit has landed on the fork.
- Landing order: this lands before the PR opens. The draft stays on HOLD until the commit is pushed and this gate is green.

## Step checklist

- [ ] patch applied at the live tip; delta is one file, two lines
- [ ] tidy at the new head; tree clean
- [ ] content check against upstream main
- [ ] example command syntax check (if zsh is available)
- [ ] branch commit pushed by the branch's owner
- [ ] receipt refreshed; draft head links re-pointed; HOLD line removed
