# Gate job: SOLR-9060 (changelog narrowing and top-up proof)

- Branch: solr-9060-submit. Head: 704ca28bf79db94d958471cbe1137fd9e10dd9fd (live tip, verified by `git ls-remote origin refs/heads/solr-9060-submit` on 2026-10-14 by the Windows review agent; the gated head in receipts/SOLR-9060.md).
- Filed by: windows (review coordinator), 2026-10-14, from assignments/pool-census-followup-spellcheck-round-1.md.
- Intended host: vm2 (Linux gate coordinator), under the WORKFLOW.md claim rules. Capability tags: implementation, gate.
- Status: OPEN, unclaimed.
- Why this job exists:
  - material/spellcheck-round-5-answers.md, decision 7 (adopted under the decision default), narrows the changelog. The fragment at the head says distributed spellcheck "now ranks merged suggestions by frequency even when spellcheck.extendedResults is false". The coordinator merge ranks by score first and uses frequency only to break ties. The narrowing is a wording fix, not a new decision.
  - receipts/SOLR-9060.md records a green gate at this head (2026-10-05). It records no test counts and no base-code result, so the proof section of the 9060 draft cannot be filled from it. The top-up run below supplies both.
- Step 1, text commit: in changelog/unreleased/SOLR-9060-spellcheck-shard-frequencies.yml, replace the title with exactly:
  `Distributed spellcheck now uses frequency to break ties between equally close suggestions when spellcheck.extendedResults is false, because shards always report frequencies to the coordinator.`
  The commit touches that one file only. Use the workspace commit wrapper (ICLA identity, no co-author trailer). Push as a fast-forward to solr-9060-submit on the fork under the coordinator push rule. Record the new head here.
- Step 2: parse the changelog YAML at the new head (rc 0).
- Step 3: tidy and module check are not needed for a text-only commit, but record that the tree is clean after the commit.
- Step 4, top-up proof at the new head:
  - Focused class: org.apache.solr.handler.component.DistributedSpellCheckComponentTest in :solr:core, with the focused method testShardMergeRanksTiesByFrequency. Use -WithSpotless and -WithFailBefore. Confirm the package from the branch's test file.
  - Record tests, failures, errors and skipped from fresh JUnit XML at the new head.
  - Fail-before: production SpellCheckComponent.java reverted to the merge-base, with the branch test kept. The merge-base is `git merge-base upstream/main solr-9060-submit` at the gate host (e432df19c4a50b9d54d7fba545397b859cc54f98 on the review host's upstream fetch). The test must fail by content (the tie order), not by count alone. Record the failure text.
  - If the test passes on the merge-base, record NOT_PROVEN and stop. The question of whether the test discriminates goes to the main side. Do not change the test to make it fail.
- Premise: the premise is not re-decided by this job. A changelog-only delta does not rerun the premise; the top-up run covers the new head (material/spellcheck-round-5-answers.md, decision 7).
- On green: the claimant refreshes receipts/SOLR-9060.md at the new head with the counts and the fail-before result, marks this job DONE, and marks the claim DONE. The review side then fills the 9060 draft's proof section and changelog link from the refreshed receipt.

## Step checklist

- [ ] text commit: changelog title narrowed (new head: ____)
- [ ] pushed to solr-9060-submit, fast-forward from 704ca28bf79d (ls-remote verified: ____)
- [ ] changelog parse (rc ____)
- [ ] tree clean after commit
- [ ] focused run at new head: DistributedSpellCheckComponentTest tests=__ failures=__ errors=__ skipped=__ (fresh JUnit XML)
- [ ] fail-before at merge-base: rc=__, failure text recorded
- [ ] receipts/SOLR-9060.md refreshed; job and claim marked DONE
