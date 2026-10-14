# Gate job: SOLR-16813 changelog title correction (CLI-R1-17), re-gate at the new head

- Branch: solr-16813-submit. Head: 1b288170e8aa9c3b1ea9bce418e26bd61f08498e (live tip, verified by ls-remote at posting, 2026-10-14T02:10Z; GATE GREEN per receipts/SOLR-16813.md at this head).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: DONE. Fork push to solr-16813-submit at 0ab6087cf43723873d83e5547ef8227e85e93653 after the gate was green; receipt refreshed, draft links re-pointed. Posted by the CLI convergence round 2 review (claims/pool-category-closeout-w2-cli-round-1.md).
- Capability tags: gate.
- Job type: branch-side changelog title correction on a gated branch, then a re-gate at the new head.
- Spec: reports/cli-convergence-round-1-response.md, item CLI-R1-17 (replacement text recorded there verbatim). Owner decision 3 in reports/cli-bin-packaging-round-1.md: amend the changelog title to "when a version loads".
- Record conflict to note: the head's commit 1b288170e8a ("narrow the changelog title to the best-effort fetch") is a title-only commit. It did not apply decision 3: its title still begins "A node that joins a cluster with installed packages now also fetches ...", and the trigger it names is the join, not the version load. The replacement below is a new commit on top of that head and supersedes that title.
- Code check at the head (read 2026-10-14): the manifest fetch runs after the JAR validation in version load (SolrPackageLoader.java L276), and a fetch that is still not a file or throws logs a warning and the version loads (fetchManifest, L294-L315). The replacement matches that.
- The fix: in `changelog/unreleased/SOLR-16813.yml` at the head above, the `title:` line is a plain scalar. Replace its value with exactly:
  When a package version loads, a node fetches its missing manifest.json from another node that has it. If the fetch fails, the version still loads with a warning.
- Commit: one new changelog-only commit on top of 1b288170e8aa. Do not amend or rewrite history. Push plainly (fast-forward) to solr-16813-submit after the gate is green at the committed tree.
- Delta check: `git diff --stat 1b288170e8aa9c3b1ea9bce418e26bd61f08498e HEAD` must list only `changelog/unreleased/SOLR-16813.yml`. Stop and record here if anything else differs.
- Proof shape: none owed beyond the carry-over. The title-only delta carries the pre-fix proof recorded for this branch (the manifest fetch test fails on base; receipts/SOLR-16813.md). The focused run below re-runs at the new head and is counted from fresh JUnit XML.
- Modules: :solr:core. Focused class: org.apache.solr.pkg.TestPackages (package line confirmed at the head). Expected at the new head: 6 of 6, per receipts/SOLR-16813.md.
- Seed: 16813C0FFEE16813 (the receipt's seed); record the seed used in the gate log.
- Gate steps: changelog YAML parse of the edited fragment with a real YAML parser; tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward; any tidy change to another file invalidates the delta check); Error Prone compile for :solr:core with `-Pvalidation.errorprone=true`; focused tests; module check `:solr:core:check -x test`.
- On green: refresh receipts/SOLR-16813.md with the new head, a line that the delta from 1b288170e8aa is the CLI-R1-17 title correction only, and the focused counts from fresh JUnit XML. Re-point every link in pr-drafts/cli/SOLR-16813.md from 1b288170e8aa to the new head where the line numbers still hold; re-check each anchor before re-pointing. Mark this job DONE and the claim DONE.

## Step checklist

- [x] title replaced verbatim in SOLR-16813.yml; delta against 1b288170e8aa is that fragment only
- [x] changelog YAML parse with a real parser
- [x] tidy on the final tree; tree clean (GATE INVALID guard)
- [x] Error Prone compile (:solr:core)
- [x] focused tests at the new head: TestPackages, counted from fresh JUnit XML
- [x] module check (:solr:core)
- [x] pushed plainly to solr-16813-submit; receipt refreshed; draft links re-pointed; job and claim DONE
