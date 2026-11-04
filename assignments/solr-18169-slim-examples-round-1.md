# Assignment: implement SOLR-18169, the slim image shouldn't have examples (any capable host, round 1)

Status: OPEN. Implementation assignment. Claim first in `claims/solr-18169.md` on this branch, then do the work. No PR, no Jira comment, and no GitHub comment is part of this assignment; those need the owner's separate go.

## The ticket

SOLR-18169, "The slim image shouldn't have examples". Task, Minor priority, reporter David Smiley, Open, unassigned, labels newdev and pull-request-available. Description, in full: "Our 'slim' distribution shouldn't have examples. Furthermore, an attempt to use {{bin/solr start -e techproducts}} etc. should yield a helpful error message." Smiley's only comment: "{{solr/core/src/java/org/apache/solr/cli/RunExampleTool.java}} will be modified".

Prior attempt: PR #4876 by r4mercur covered only the error-message half and carried a large amount of unrelated work (cluster commands, SQL module, schema designer). It was closed unmerged on 2026-09-05. Read its RunExampleTool diff for reference if useful; the new branch must be clean and single-purpose.

## Scoping already done (phone agent, 2026-11-03, verified against origin/main ad26fdf2c6)

- Packaging: `solr/packaging/build.gradle` builds the slim distribution with the examples included: the slim contents contain `from(configurations.example, { into "example" })`. The full distribution is built on top of the slim contents (`with(distributions.slim.getContents())`) and adds modules and cross-dc-manager, so moving the example block from the slim contents to the full contents removes examples from slim and keeps them in full.
- The `:solr:example` packaging configuration contributes `exampledocs/**`, `films/**`, and `README.md` from `solr/example/` (its `files/**` include currently matches nothing).
- Error path: `bin/solr` passes `-e` to the `run_example` tool (RunExampleTool). In `initDirs`, the default example dir is `serverDir/../example`; when it is missing, the tool currently throws "Value of --example-dir option is invalid! <path> is not a directory!", which tells a slim user nothing about why. Nothing on main mentions slim in RunExampleTool today.
- The Docker official slim image is built from the slim distribution, so the packaging change covers the image in the ticket title; no Dockerfile change is expected.
- Existing test home for this tool: `solr/core/src/test/org/apache/solr/cli/TestSolrCLIRunExample.java`.

## The work

1. Packaging: move the example content out of the slim distribution contents and into the full distribution contents in `solr/packaging/build.gradle`. Nothing else about either distribution changes.
2. RunExampleTool: when the example directory is absent and the user did not pass an explicit `--example-dir`, fail with a helpful message that says the examples are not included in the slim distribution and points at the full distribution for examples. The explicit `--example-dir` pointing at a missing directory keeps a clear invalid-value error. Behavior with the examples present (full distribution) is unchanged, including the existing unsupported-example error.
3. Tests: extend `TestSolrCLIRunExample` (or the nearest existing suite, same style) to pin the new slim message and the unchanged present-examples path. New test classes, if any are needed, extend SolrTestCase or SolrCloudTestCase, never SolrTestCaseJ4.
4. Changelog: add a logchange fragment for SOLR-18169 under `changelog/unreleased/`, following the format of neighboring fragments.
5. Branch: `solr-18169-slim-no-examples` on the fork, based on current origin/main. Commits authored Nick Shanin, no other author or trailer names.

## Proof required in the receipt

- `:solr:packaging:installSlimDist` and `:solr:packaging:installFullDist` both run; list the installed trees and show `example/` absent from slim and present in full.
- Focused test run for the touched CLI tests, with fresh JUnit XML counts.
- `:solr:core:check -x test` at the final head, plus Error Prone on the compile (`-Pvalidation.errorprone=true`), plus tidy with any formatting commit pushed.
- Receipt at `receipts/SOLR-18169-round-1.md` on this branch with the final head SHA, then mark this assignment DONE with the head named.

## Limits and notes

- Gate rules from the board guides apply: solr-gradle.sh only, one build at a time through the queue flock, no root `check` or ref-guide leg in a worktree (Antora trap; run any such leg in a normal clone at the same head and say so in the receipt).
- Solr-starting BATS tests cannot run on hosts that run as root; if a live `bin/solr start -e techproducts` check against the slim dev distribution cannot run on your host, state it as a limit in the receipt instead of working around it.
- If the helpful-message wording needs a maintainer decision (for example, whether the message may name the slim distribution explicitly), implement the plain reading of the ticket and flag the wording as a choice in the receipt rather than stopping.
