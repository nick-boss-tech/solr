# Gate job: SOLR-18119 generate changes HTML only for releases (gate and review)

Status: CLAIMED (claims/pool-vm1-18119-release-changes-html-gate.md)

- Branch: the head 1029a6b0cf09dc75bdb380cd63fce1511bf440b2 is pushed under two names, solr-18119-release-python and solr-18119-remove-changes2html (same commit; the gate covers the head, and the receipt records both names).
- Head: 1029a6b0cf09, one commit on merge base c8ab71430f6e (current main tip at filing; 1 ahead, 0 behind).
- Change shape: the Gradle changes-to-html generation is deleted (gradle/documentation/changes-to-html.gradle, the root apply line, the documentation site dependency, the distribution changesHtml configuration); changes2html.py and its CSS move to dev-tools/scripts, gain an importable write_changes_html, and the release tooling (buildAndPushRelease.py, releaseWizard.yaml) generates Changes.html at release time; assembleRelease guards on the generated files; the ref-guide link check ignores the changes URL locally. New Python test: dev-tools/scripts/test_changes2html.py.
- Capability tags: gate, review.
- Steps:
  - [ ] 1. Verify the head and the base; record the diff shape.
  - [ ] 2. Changelog: the branch adds no fragment (release tooling only; the SOLR-18119-submit precedent had its entry deleted as not changelog worthy). Record the absence; nothing to parse.
  - [ ] 3. Tidy, with the clean-tree guard.
  - [ ] 4. Build checks at the head: root configuration, the documentation build without the changes task, and the distribution project configuration.
  - [ ] 5. Python proof: run dev-tools/scripts/test_changes2html.py, and generate Changes.html with the moved script into a scratch dir; the output must include Changes.html and the three CSS files the distribution guard requires.
  - [ ] 6. Premise proof: at the merge base the Gradle changesToHtml task exists and at the head it does not; compare the head's script-generated Changes.html against the base's Gradle-generated one and record the result either way.
  - [ ] 7. Review pass: whole-tree grep for references to the deleted Gradle file, the changesToHtml task, and the changesHtml configuration; read the moved script's changes against the base version.
- On green: append the receipt section for this branch to receipts/SOLR-18119.md, write the review report under reports/, mark this job and the claim DONE. No branch push is needed (the head is already pushed); no PR opening is part of this job.
