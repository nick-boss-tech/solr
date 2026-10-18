# Gate job: SOLR-18119 generate changes HTML only for releases (gate and review)

Status: DONE (vm1, 2026-10-18: GATE GREEN at 1029a6b0cf09; review SATISFIED; receipt section appended)

- Branch: the head 1029a6b0cf09dc75bdb380cd63fce1511bf440b2 is pushed under two names, solr-18119-release-python and solr-18119-remove-changes2html (same commit; the gate covers the head, and the receipt records both names).
- Head: 1029a6b0cf09, one commit on merge base c8ab71430f6e (current main tip at filing; 1 ahead, 0 behind).
- Change shape: the Gradle changes-to-html generation is deleted (gradle/documentation/changes-to-html.gradle, the root apply line, the documentation site dependency, the distribution changesHtml configuration); changes2html.py and its CSS move to dev-tools/scripts, gain an importable write_changes_html, and the release tooling (buildAndPushRelease.py, releaseWizard.yaml) generates Changes.html at release time; assembleRelease guards on the generated files; the ref-guide link check ignores the changes URL locally. New Python test: dev-tools/scripts/test_changes2html.py.
- Capability tags: gate, review.
- Steps:
  - [x] 1. Verify the head and the base; record the diff shape.
  - [x] 2. Changelog: the branch adds no fragment (release tooling only; the SOLR-18119-submit precedent had its entry deleted as not changelog worthy). Record the absence; nothing to parse.
  - [x] 3. Tidy, with the clean-tree guard.
  - [x] 4. Build checks at the head: root configuration, the documentation build without the changes task, and the distribution project configuration.
  - [x] 5. Python proof: run dev-tools/scripts/test_changes2html.py, and generate Changes.html with the moved script into a scratch dir; the output must include Changes.html and the three CSS files the distribution guard requires.
  - [x] 6. Premise proof: at the merge base the Gradle changesToHtml task exists and at the head it does not; compare the head's script-generated Changes.html against the base's Gradle-generated one and record the result either way.
  - [x] 7. Review pass: whole-tree grep for references to the deleted Gradle file, the changesToHtml task, and the changesHtml configuration; read the moved script's changes against the base version.
- On green: append the receipt section for this branch to receipts/SOLR-18119.md, write the review report under reports/, mark this job and the claim DONE. No branch push is needed (the head is already pushed); no PR opening is part of this job.

## Run record (vm1, 2026-10-18)

- Log: ~/workspace/tools/g18119-gate.log. All steps green: tidy clean (guard passed), configuration and Error Prone compile rc=0, documentation wiring tasks rc=0, distribution assembleRelease task graph resolves, Python tests 2 of 2, direct generation writes Changes.html plus the three stylesheets.
- Proof: :solr:documentation:changesToHtml runs at the merge base and is not found at the head; the head script's Changes.html is byte-identical to the base Gradle output (SHA-256 b3539c06a3111c4c6026ee863fe0f7a2e0dfd292a5e7da1f99bb0178559634ea both sides).
- Deliverables: receipts/SOLR-18119.md (appended section) and reports/SOLR-18119-release-changes-html-review.md.
