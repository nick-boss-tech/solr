# Pool assignment: run the bats integration tests on the SOLR-18565 branch

Status: OPEN

- Capability tags: bats.
- Category: none. This is a CI check of a one-line test fix.
- Intended host: vm1, the phone agent host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: vm2, 2026-10-22, at the direction of Nick.

Each item says what to do, the file to start from, and the output path. If an item needs a decision, its row in the Decisions table gives the answer or the default.

## Items

1. **Run the full bats suite on the branch head.** Confirm the head first: `git ls-remote origin refs/heads/solr-18565-submit` must print `2c747689c32bd17a6327ecd2b60999b972771269`. If it prints anything else, stop and follow the first Decisions row. Then, from your worktree at that head, run `pool/bin/sweep-run.sh <worktree> <log dir> solr-18565-head-full :solr:packaging:integrationTests`. This is the same Gradle task that fails in CI. Record the pass and fail counts from `solr/packaging/build/test-output` and the state of test 88 ("use different hostname when not checking peer-name", `test/test_ssl.bats`). Output: `reports/SOLR-18565-bats-run-1.md`, section "Head full run".
2. **Run only the SSL file on the head.** Run `pool/bin/sweep-run.sh <worktree> <log dir> solr-18565-head-ssl :solr:packaging:integrationTests --tests test_ssl.bats`. Record the same counts and the result of test 88. Output: `reports/SOLR-18565-bats-run-1.md`, section "Head SSL file".
3. **Control on the base commit.** In a second worktree at `7892348272727b8530d26ad08db734712ab2dacb12`, run the same command as item 2. Record the result of test 88. This shows whether the failure also happens on this host without the fix. Output: `reports/SOLR-18565-bats-run-1.md`, section "Base SSL file".
4. **Write the PR proof line.** When items 1 and 2 are finished, add a section "PR proof line" to the deliverable. It is one or two plain sentences for the Proof section of the pull request: the head commit, the command, the pass and fail counts, the result of test 88, and the date. Write for a public reader. Do not name the host, the pool, or any agent. If test 88 fails on the head, the sentence says so. Output: `reports/SOLR-18565-bats-run-1.md`, section "PR proof line".

Record the host OS and kernel (`uname -a` and `/etc/os-release`) at the top of the deliverable. Record the log paths for every run.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Head moved | decided: stop | if the branch head is not `2c747689c3`, do not run anything; write that line in the deliverable and stop | Nick |
| `--tests` option refused | default: run the full task | if Gradle rejects `--tests test_ssl.bats`, run the full `:solr:packaging:integrationTests` task for items 2 and 3 and say so in the deliverable | Nick |
| Other tests fail | decided: record only | if a test other than test 88 fails, record its name and message; do not change code or fix it | Nick |
| Test 88 passes on head | decided: record and stop | record the pass; do not run more tests | Nick |
| Host is not ubuntu-26.04 | decided: record and continue | the host OS is recorded; the run still counts for this branch | Nick |

No row may leave the choice to the claimant.

## Deliverable

`reports/SOLR-18565-bats-run-1.md`: the host OS line, then one section per item with its status, the command used, the pass and fail counts, the result of test 88, and the log path. Also name any other failing test.

## Limits

- New PRs, comments, JIRA entries and emails need the owner's approval. Editing text that is already posted (a PR body, a comment, a JIRA description) is allowed. No force push. Do not change or push the branch.
- vm2 updates the PR body from the proof line. This assignment does not edit the PR.
- Commit as Nick Shanin with no trailers. No em dashes.
