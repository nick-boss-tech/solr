# Completion gate: PR 5298 root tidy at the tested round-2 head

Status: OPEN
Tags: gate, review, records
Intended host: any capable Linux CPU gate host with the branch's JDK 21 toolchain and pinned Gradle wrapper/dependencies, serialized under its host build lock; the existing phone-agent clone may be reused if clean at R.

## Authority and exact input

This completes the named root tidy leg of Nick's authorized PR 5298 patch/gate workflow. It is not a new source change or a repeat of the completed runtime proof.

- Exact R: a99e40291ae17fbee56b821113cc9bc15659b147
- Fork branch: nick-boss-tech/solr:pr-5188-glob-field-loading-followup
- PR: https://github.com/apache/solr/pull/5298, still draft at reconciliation.
- Original job: gates/pr-5298-dv-prefetch-round-2.md (mandatory step 6 names root tidy).
- Passing runtime/compile/check receipt: receipts/PR-5298-dv-prefetch-round-2.md
- Evidence reconciliation: reports/pr-5298-round-2-receipt-reconciliation.md
- Completion receipt: receipts/PR-5298-root-tidy-completion-round-2.md (execution host only writer).
- Durable evidence: reports/pr-5298-root-tidy-completion-round-2-evidence/ or an accessible archive with SHA-256 manifest.

The existing receipt says GATE GREEN; retain it unchanged. Its archive records clean :solr:core:tidy and a passing root check including formatting checks, but no root tidy invocation. This job requests only that missing named task, with a clean-tree/head read-back.

## Claim and execution

Read live board bootstrap and the related claim/receipt paths. Verify fork ref and PR head still equal R, and the task checkout is clean at R. A moved head requires reconciliation before running. Claim this completion job in a new add-only claim; do not edit the original job/claims/receipts. Use the host build lock, disk/inode guard and supported Linux runner. Record source/board heads, JDK/runner and UTC start/end.

1. From the repository root at R, run ./gradlew tidy through the supported runner. No task prefix and no exclusions. Preserve the exact command, full log and exit status.
2. Require exit 0 and an empty git status --porcelain afterward. Record full HEAD/tree and separate fork/PR read-back; require all still R. Archive these identity/clean-tree records and a SHA-256 manifest.
3. If clean and unchanged, write the completion receipt linking this job, the round-2 receipt and durable logs. No test, Error Prone or root-check rerun is requested: their round-2 evidence remains valid at unchanged R. Record explicitly that the remaining named formatting leg is complete and that no source head moved.

If tidy writes tracked files, do not discard them or claim green at R. Investigate the scoped diff, commit a legitimate formatting repair as Nick Shanin with no trailers, plain-push to the fork, read back the new SHA, and post an add-only repair report and superseding exact-head gate. New code/format head requires fresh proof under the original gate's repair rule. Wider/unrelated generated changes must be diagnosed rather than shipped silently.

If an actual task fails, diagnose before retrying; no blind reruns. The one-relaunch rule applies only when an environment interruption prevented task execution, with cause/remedy recorded. Do not run this on Windows. No public PR comment/body edit, readiness change, JIRA write or merge is authorized here.
