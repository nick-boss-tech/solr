# Gate job: SOLR-16272 changelog title correction (CLI-R1-14), re-gate at the new head

- Branch: solr-16272-submit. Head: d2cf817391692b29c3d221ec4ebed65c3833bb91 (live tip, verified by ls-remote at posting, 2026-10-14T02:10Z; GATE GREEN per receipts/SOLR-16272.md).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed. Posted by the CLI convergence round 2 review (claims/pool-category-closeout-w2-cli-round-1.md).
- Capability tags: gate.
- Job type: branch-side changelog title correction on a gated branch, then a re-gate at the new head.
- Spec: reports/cli-convergence-round-1-response.md, item CLI-R1-14 (replacement text recorded there verbatim). Owner decision 2 in reports/cli-bin-packaging-round-1.md: amend the changelog title to the keep rule, as a changelog-only commit.
- Code check at the head (read 2026-10-14): the cleanup runs on a failed install (RepositoryManager.java L258-L263); a version that may already be registered keeps its files (mayBeRegistered, L299-L311). The replacement's "unless the version may already be registered" matches that rule. The uninstall command is unchanged, so the old clause "uninstall no longer refuses" is removed.
- The fix: in `changelog/unreleased/SOLR-16272.yml` at the head above, the `title:` line is a plain scalar. Replace its value with exactly:
  A failed bin/solr package install now removes the files it posted, unless the version may already be registered, so a retry is not blocked by leftover files.
- Commit: one new changelog-only commit on top of d2cf817391692b29c3d221ec4ebed65c3833bb91. Do not amend or rewrite history. Push plainly (fast-forward) to solr-16272-submit after the gate is green at the committed tree.
- Delta check: `git diff --stat d2cf817391692b29c3d221ec4ebed65c3833bb91 HEAD` must list only `changelog/unreleased/SOLR-16272.yml`. Stop and record here if anything else differs.
- Proof shape: none owed beyond the carry-over. The title-only delta carries the pre-fix proof recorded at d2cf817 (the new failed-install test fails on the unpatched code; gate log g16272-harden.log). The focused run below re-runs at the new head and is counted from fresh JUnit XML.
- Modules: :solr:core. Focused class: org.apache.solr.cli.PackageToolTest (package line confirmed at the head). Expected at the new head: 3 of 3, per receipts/SOLR-16272.md.
- Seed: 16272C0FFEE16272 (the ticket pattern); record the seed used in the gate log.
- Gate steps: changelog YAML parse of the edited fragment with a real YAML parser; tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward; any tidy change to another file invalidates the delta check); Error Prone compile for :solr:core with `-Pvalidation.errorprone=true`; focused tests; module check `:solr:core:check -x test`.
- On green: refresh receipts/SOLR-16272.md with the new head, a line that the delta from d2cf817 is the CLI-R1-14 title correction only, and the focused counts from fresh JUnit XML. Re-point every link in pr-drafts/cli/SOLR-16272.md from d2cf817 to the new head (the code is unchanged, so the line numbers hold). Mark this job DONE and the claim DONE.

## Step checklist

- [ ] title replaced verbatim in SOLR-16272.yml; delta against d2cf817 is that fragment only
- [ ] changelog YAML parse with a real parser
- [ ] tidy on the final tree; tree clean (GATE INVALID guard)
- [ ] Error Prone compile (:solr:core)
- [ ] focused tests at the new head: PackageToolTest, counted from fresh JUnit XML
- [ ] module check (:solr:core)
- [ ] pushed plainly to solr-16272-submit; receipt refreshed; draft links re-pointed; job and claim DONE
