# Gate: triage test-workflow indentation repair, round 1

Status: OPEN (Implemented, Proof Pending; Linux execution not yet claimed).

Tags: gate, review, records.
Intended host: any available capable Linux CPU gate host with Python 3, Git, Bash, actionlint with ShellCheck, JDK 21, Node/npm and this branch's Gradle wrapper/build dependencies.

## Revision and scope

- Upstream PR: https://github.com/apache/solr/pull/5281
- Merged main parent H0: `00a5f9701ea9850a26b1bc1ba83326edbd92f383` (PR 5281 squash merge, fetched upstream/main).
- PR merged at 2026-11-06 04:19:43 UTC; historical contributor head was `d05b60284ad24d842597fe9210cd23d74eb643b3`.
- Fork: `nick-boss-tech/solr`, branch `pr-5281-workflow-repair-main`.
- Required head H1: `a3a930aa07d9676315f6133bdb1d7c88a1940942` (remote fork ref separately verified).
- Idempotency key: `pr-5281-test-workflow-repair-round-1@a3a930aa07d9676315f6133bdb1d7c88a1940942`.
- Pinned diff: https://github.com/nick-boss-tech/solr/compare/00a5f9701ea9850a26b1bc1ba83326edbd92f383...a3a930aa07d9676315f6133bdb1d7c88a1940942
- Outbound file: `.github/workflows/pr-triage-test.yml`, one command indented under `run: |`.
- Implementation claim: `claims/pr-5281-test-workflow-repair-main-windows-round-1.md`, superseding the original pre-merge claim's base.
- Implementation report: `reports/pr-5281-test-workflow-repair-round-1.md`.
- Execution host's receipt: `receipts/PR-5281-test-workflow-repair-round-1.md`.
- Related historical gate: `gates/pr-5281-triage-followup-round-1.md`, GREEN at `6bcaa9e1191fb8d0c6def2eaf005b602bf882e40`, not proof for H0 or H1 here.

This is a workflow validation failure, not a triage-unit-test assertion regression. At H0, the command at line 26 starts in column 1. Upstream Actions run https://github.com/apache/solr/actions/runs/42155946264 failed without creating jobs at the historical contributor head; merged main retains the same malformed workflow. The repair restores the existing test command as the step's block-scalar content. Script, configuration, tests, triggers, permissions and action pins are unchanged. No user-facing changelog or Java focused test is required.

## Before claiming

Read the live board instructions and host capabilities; check for a current claim and the named receipt. Verify the remote fork head equals H1. Use a clean task-specific clone at H1 with an actual `.git` directory: the historical PR 5281 gate hit Antora's worktree `.git`-file limitation and passed after using a real clone. Record the source SHA, UTC, checkout path, JDK/Python/Git/actionlint/ShellCheck versions, commands, fresh log paths and exit codes. Serialize under the host's build lock. Perform the board's detached tidy preflight before claiming; if preflight repairs source, follow the repair rules below and prove the new head.

Offline tests require only Python's standard library and Git. No PyYAML install or requirements-pr-triage.txt is needed at this revision; the judgement config is JSON. Do not call the LLM gateway, use model credentials, publish a comment, change the contributor's branch or simulate success for GitHub event delivery.

## Required proof legs

1. **Validate the actual failure-before and success-after.** Export the original workflow directly from H0 into a fresh evidence directory without changing its bytes. Record its hash and run `actionlint` on that file with ShellCheck available. Require a nonzero exit caused by the original YAML/block-scalar syntax at the unindented Python command. A missing tool, unsupported host setup or unrelated linter error is not this proof. Then, from clean H1, run:

   ```sh
   actionlint .github/workflows/pr-triage-test.yml .github/workflows/pr-triage-llm.yml
   ```

   Require exit 0 with ShellCheck enabled. Keep both complete logs and exact commands. This is validator fail-before proof; do not describe it as a failing Python test. Preserve the before/after workflow diff and verify it changes only the command's leading indentation.

2. **Execute the current workflow command at H1.** With no live API/model credentials and from the checkout root, run the exact command:

   ```sh
   python3 -m unittest discover -s dev-tools/scripts/tests -p test_pr_triage.py -v
   ```

   Require the complete discovered suite to pass with no skipped tests. Source inspection identifies 44 test methods at H0/H1; verify and report the discovered count. The old receipt's 23-test count is historical. Retain complete fresh stdout/stderr and exit status. stdlib unittest produces no JUnit XML; retain its test log rather than inventing XML. Python tests have no random seed. These tests are expected to pass on H0 as well because production script and tests are unchanged; the failure-before target is workflow validation, not these assertions.

3. **Formatting and root validation at H1.** Run `./gradlew tidy`, require no tracked changes, then `./gradlew check -x test`, requiring success. Keep complete logs and exit codes. No other exclusions or weakened checks. No Java focused suite is required for the single workflow indentation repair. Use seed `5281C0DE` only for any incidental randomized task that needs one. If the root check requires an environment repair, document the initial failure and exact repair before a bounded retry; do not silently count an interrupted run as a pass.

4. **Final identity, scope and cleanliness.** Require `git diff --check` and clean tracked/untracked status. Verify H1 descends from H0 with one ordinary commit and only the named workflow in its H0-to-H1 diff. Record that the unit-test workflow still executes contributor code only with `contents: read`, no secrets and `persist-credentials: false`; the privileged production workflow is unchanged. Separately read the remote fork ref again and inspect current upstream main for an equivalent fix. Report main movement or an equivalent repair; do not silently rebase or substitute a new proof base.

## Failure and repair rules

Investigate and report an executed positive failure before a retry. The expected original actionlint syntax rejection is the negative proof, not a positive gate failure. A setup interruption before checks run may be relaunched once after recording the cause and repair. Do not weaken tests or lint rules to get green.

Any source, test or formatting repair changes the proof head. Commit only a scoped authorized repair with Nick Shanin's ICLA identity, plain-push to this fork branch, verify the remote SHA, and publish an add-only report and superseding gate with the new head before treating proof as complete. Never force-push or modify upstream/the contributor fork. Report unrelated blockers separately.

## Completion

The execution host owns the named receipt. Include every proof leg, exact original/patched SHAs, tool versions, logs, exit codes, test count and any documented environment condition. The receipt's final section should contain a concise follow-up description and the pinned H0-to-H1 diff. PR 5281 is merged: do not propose fast-forward commands for its closed contributor branch. Opening a new public PR or posting a response is outside this gate. This gate does not prove live model quality, ASF key setup, GitHub event delivery, or comment publication. Status remains Implemented, Proof Pending until all required positive legs pass at the verified offered head.
