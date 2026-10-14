# Gate job: SOLR-9342 changelog title correction (CLI-R1-03), title-only commit

- Branch: solr-9342-submit. Head: 833e11192a7ff918f7e401bde920cf63c89df063 (live tip, verified by ls-remote at posting, 2026-10-14T02:10Z).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed. Posted by the CLI convergence round 2 review (claims/pool-category-closeout-w2-cli-round-1.md).
- Capability tags: gate.
- Job type: branch-side changelog title correction on a gated branch. A title-only commit on top of the head, then the gate evidence check at the new head.
- Spec: reports/cli-convergence-round-1-response.md, item CLI-R1-03 (replacement text recorded there verbatim). Owner decision 1 in reports/cli-bin-packaging-round-1.md lists the changelog title as an owner item; the round 1 replacement is the adopted text.
- Code check at the head (read 2026-10-14): solr/bin/solr line 1196 sets `SOLR_TIMEZONE:=UTC`; lines 1352 and 1356 set the server JVM `TZ="${TZ:-$SOLR_TIMEZONE}"` in the foreground and nohup launches. The script does not export TZ for itself, so the replacement's "sets TZ ... for the server JVM ... unless TZ is already set" holds.
- The fix: in `changelog/unreleased/SOLR-9342-gc-log-timezone.yml` at the head above, the title is a folded block scalar (`title: >`). Replace its text line with exactly:
  "The Unix start script (bin/solr) sets TZ from SOLR_TIMEZONE (default UTC) for the server JVM, so its GC log uses that zone unless TZ is already set. solr.cmd is not changed."
  Keep the `title: >` shape. The old text ("bin/solr now exports TZ from SOLR_TIMEZONE ...") describes the superseded approach (commit 804b1da118a) and contradicts the Limits.
- Commit: one new changelog-only commit on top of 833e11192a7. Do not amend or rewrite history. Push plainly (fast-forward) to solr-9342-submit after the check below is green.
- Delta check: `git diff --stat 833e11192a7ff918f7e401bde920cf63c89df063 HEAD` must list only `changelog/unreleased/SOLR-9342-gc-log-timezone.yml`. Stop and record here if anything else differs.
- Proof shape: none owed for code. The script and the BATS file are unchanged, so the evidence in receipts/SOLR-9342.md at 833e11192a7 carries over to the title-only commit (the precedent is the SOLR-16630 and SOLR-9091 title-only re-gates). If the delta touches anything but the fragment, the BATS run in receipts/SOLR-9342.md is owed again.
- Gate steps: changelog YAML parse of the edited fragment with a real YAML parser (the block scalar must parse to the replacement text exactly); delta check above. No Java file changes, so no tidy, Error Prone or Java test step applies.
- On green: refresh receipts/SOLR-9342.md with the new head, a line that the delta from 833e11192a7 is the CLI-R1-03 title only, and the carried-over BATS evidence named as such. Re-point every link in pr-drafts/cli/SOLR-9342.md from 833e11192a7 to the new head (the code is unchanged, so the line numbers hold). Mark this job DONE and the claim DONE.

## Step checklist

- [ ] title replaced verbatim in SOLR-9342-gc-log-timezone.yml (block scalar shape kept); delta against 833e11192a7 is that fragment only
- [ ] changelog YAML parse with a real parser
- [ ] pushed plainly to solr-9342-submit; receipt refreshed; draft links re-pointed to the new head; job and claim DONE
