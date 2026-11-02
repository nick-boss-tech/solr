# Gate: SOLR-18613 changelog-only completion, round 1

Status: OPEN

- Ticket: https://issues.apache.org/jira/browse/SOLR-18613
- Tags: gate, records. Intended host: any capable Linux gate host with JDK 21, Python 3 with PyYAML, the pinned Gradle/build dependencies, and a serialized host build lock.
- Authority: Nick's November 2 message that the published-head receipt is back and only the changelog remains.
- Fork/branch: nick-boss-tech/solr:solr-lb-attempt-limits-pr1-submit.
- Exact head: b49413e5ab5217ec45f1693fa9eff6c3e11f78dc.
- Parent already gated GREEN: c2d2c6259462a7e848008b065d65df69a1b82ebb.
- Original production base: 0d6b2e32f598d772aed094038022a6a588fdfc5a.
- Sole new file: changelog/unreleased/SOLR-18613-lb-attempt-limits.yml.
- Implementation claim: claims/SOLR-18613-changelog-windows-round-1.md.
- Prior receipt: receipts/SOLR-18613.md, phone agent's exact published-head receipt.
- New receipt target: receipts/SOLR-18613-changelog-round-1.md, owned by the host executing this gate.
- State: changelog pushed, final metadata proof pending. No execution is claimed by this handoff.

## Preflight and scope

Read the live workflow, this job, prior receipt, existing claims and any superseding records. Use a clean detached full clone at the exact head; a full clone avoids the known Antora worktree .git-file failure. Verify the live fork ref still equals this SHA. Claim before execution and record UTC start, host, JDK, lock, checkout, command logs and result paths. One build at a time under the host lock; do not duplicate an active claim.

Verify that this head has parent c2d2c6259462a7e848008b065d65df69a1b82ebb and that its entire parent-to-head diff is the single new 10-line changelog fragment, without code, test, configuration or dependency changes. Review the fragment for accurate scope, type fixed, author name Nick Shanin only (no nick field), and the SOLR-18613 link.

## Required fresh metadata checks at the new head

Run and retain full logs and exit codes:

~~~sh
python3 dev-tools/scripts/validate-changelog-yaml.py changelog/unreleased/SOLR-18613-lb-attempt-limits.yml
./gradlew tidy
./gradlew check -x test
git diff --check
git status --porcelain
~~~

Require successful YAML validation, clean tidy, successful root check, no whitespace errors and a clean final tree at the exact SHA. No task exclusions beyond -x test. The Windows validation attempt stopped at import yaml because PyYAML was missing; it did not validate or reject the fragment. Linux must run the actual validator.

The prior root check required the execution host's combined Hatch CA truststore in JAVA_TOOL_OPTIONS after an egress CA rotation. Read the receipt's environment note; use the host's current documented trust configuration if needed, and state any condition beside the result. Do not silently disable checks.

## Carry forward completed runtime proof

Do not rerun the 47 Java tests or the four negative-proof methods solely for this changelog addition. This is a metadata-only gate because the complete Java/test blobs are unchanged from the exact GREEN parent.

Verify that fact from the entire parent-to-head diff and carry forward, with explicit attribution and limits:
- 47 focused/neighboring tests, zero failures/errors/skips at c2d2c6259462a7e848008b065d65df69a1b82ebb, seed 5B407A2D9C1310E7, as recorded in receipts/SOLR-18613.md.
- Four original-production failures reconciled in that receipt and originally reported in receipts/lb-attempt-limits-pr1.md. Those used a synthetic base with the two tests and inert exception declaration, not untouched production. Only four new methods are discriminated; the other 20 are compatibility pins. The phone host reconciled this from board records and ancestry/tree/test-token checks without rereading vm2's raw artifacts.

Do not claim these runtime tests ran at b49413e5ab5217ec45f1693fa9eff6c3e11f78dc. If the delta includes runtime-affecting changes, stop this metadata-only procedure, record the finding and post an appropriate superseding gate instead.

## Completion and repairs

GREEN requires verified unchanged runtime files, all fresh metadata checks passing, a clean final tree, and separate before/after remote head confirmation. Write the new receipt and an add-only report with exact parent/head, delta, author/link review, commands, logs, environment notes, carried-forward proof and its limits. Leave the prior phone agent receipt unchanged.

If formatting or any fix changes the tree, commit only the scoped repair as Nick Shanin, plain-push to the fork, verify the remote SHA and post a superseding gate for that head. Investigate executed failures before retrying; distinguish environment setup from patch failure.

No Windows Gradle, upstream PR, JIRA comment/edit, mailing-list message, force-push, or implementation of PRs 2/3 is authorized by this job.

