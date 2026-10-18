# SOLR-18119 release changes HTML: review follow-up (vm1, 2026-10-18)

Deliverable for `assignments/pool-vm1-solr18119-review-fixes-round-1.md`. Branch: `solr-18119-remove-changes2html` (PR #5180). Final head: **fd796bbf2a6dcb12215124516291348cb9e4516a** (fast-forward from 1029a6b0cf09; the `solr-18119-release-python` alias was not touched). Log: `~/workspace/tools/g18119-followup.log` on vm1, with per-leg logs g18119-neg.log, g18119-pos.log, g18119-tidy2.log, g18119-ep2.log.

## Item 1: renderer compatibility and release guard proofs

Status: DONE.

- Source change (commit fd796bbf2a, the only commit): `dev-tools/scripts/test_changes2html.py` gains `test_cli_without_output_dir_writes_to_stdout`, which runs the renderer as a command with a changelog argument and no `--output-dir`, asserts the HTML is on stdout, and asserts no files are created. The Python suite passes 3 of 3 at the final head.
- Negative proof: with `solr/documentation/build/site/changes` removed, `:solr:distribution:assembleRelease` fails (rc=1) with "Missing release changes files [Changes.html, ChangesFancyStyle.css, ChangesSimpleStyle.css, ChangesFixedWidthStyle.css]; run `python3 -u dev-tools/scripts/changes2html.py CHANGELOG.md --output-dir solr/documentation/build/site/changes` before assembleRelease."
- Positive proof: after running that exact command, `assembleRelease` succeeds (rc=0) and the assembled release at `solr/distribution/build/release/changes/` contains all four files.
- Gate rerun at the patched tree: tidy rc=0 with no tidy changes (git status after tidy shows only the test file edit), Error Prone `:solr:core:compileJava -Pvalidation.errorprone=true` rc=0.
- Byte identity rechecked at the final head: the renderer's Changes.html for the repository CHANGELOG.md has SHA-256 b3539c06a3111c4c6026ee863fe0f7a2e0dfd292a5e7da1f99bb0178559634ea, identical to the base Gradle task's output and to the earlier head.
- Limits kept explicit: the full documentation site build and `:solr:solr-ref-guide:checkSiteLinks` were not run in either gate; the link check change is a one-line ignore entry.

## Item 2: PR #5180 description corrected

Status: DONE, with Nick's approval for all updates (2026-10-18). Read back from the API after the edit; the diff against the prior body is exactly three changes:

- "What happens today": the website framing is replaced. Generation is release-only, the release distribution carries Changes.html and its stylesheets under `changes/`, and the binary distribution has also shipped the markdown changelog since 10.0.
- Verification status: narrowed to the final head fd796bbf2a results (tidy and Error Prone clean, Python tests 3 of 3, the assembleRelease negative and positive runs, the byte-identical hash rechecked at this head).
- Limits: added the not-run checks (full documentation site build, site link check). The AI header and footer, the Docker Python helper limit, and the no-changelog rationale are unchanged. No em dashes or en dashes.

## Item 3: records

Status: DONE. This report; a follow-up section appended to `gates/SOLR-18119-release-changes-html.md` and to `receipts/SOLR-18119.md`; the assignment and claim marked DONE in the same push.

## Addendum: body shortened (vm1, 2026-10-18)

At Nick's direction the PR #5180 body was shortened from 4,264 to 3,467 characters: the release tooling bullets merged, the python point stated once, the fail-hard behavior moved into "What this change does", and the verification heading set to "Proof". No claim was added or dropped; the blob citations were refreshed to the final head fd796bbf2a. Read back from the API, identical.
