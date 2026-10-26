# Gate: PR 5235 compatibility fixes, round 3

Status: OPEN
Tags: gate, premise-run, review, records
Intended host: vm2 (prior proof host), or any capable Linux gate host if unclaimed; JDK 21, branch Gradle wrapper, Python 3 and normal Solr build dependencies. One gate at a time under the host build lock.

## Inputs and ownership

- Direct source: Nick requested implementation and handoff to the gate agent in the Windows chat.
- Fork: https://github.com/nick-boss-tech/solr.git, branch solr-18351-submit.
- Fixed head F: **5f279c7aa44a993bd999be1d4dfc6ecab7942bc8**. Fix commit 8f4a8f22674c86598e01cf3043fc884757e55fbc; merge F includes the author's new tests.
- Original production PR head O: **9ff44cdd0b818c6f1296d69651732f794acd76b7**, apache/solr PR 5235, SOLR-18351-backport-10x. O adds two tests above formerly reviewed 73fa87f1f41b45862c165a9571176c89e2ef4def, without changing production.
- Historical branch base B: f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7. B is context, not a required third execution leg for this fix gate.
- Seed on all focused and neighboring runs: **5235C0FFEE18351**.
- Windows implementation claim: claims/pr-5235-compatibility-fixes-windows-round-3.md.
- Implementation report: reports/pr-5235-compatibility-fixes-windows-round-3.md.
- Prior evidence: receipts/PR-5235-compatibility-premise-round-1.md and receipts/PR-5235-jdk-raw-capture-round-2.md. Preserve those receipts, claims, gates and reports.
- New deliverables: receipts/PR-5235-compatibility-fixes-round-3.md and reports/pr-5235-compatibility-fixes-round-3-outcome.md. The gate host owns the receipt. Claim this new job before execution, following detached tidy preflight.

## Preflight and exact-head rules

Read the live board and check for another active claim on this job. Confirm remote branch F, current PR head and merge-base ancestry before starting. If the PR moved, assess the delta; do not silently substitute a head. Record an explicit superseding job and reconcile if the comparison/adoption path changes.

Use fresh full Linux worktrees, not the sparse Windows tree or old dirty proof trees. Verify O is an ancestor of F. Inspect the incremental diff O..F: six production files and the three added regression suites, no changes to the author's HttpJettySolrClientTest relative to O. Run ./gradlew tidy on a detached fixed tree. If formatting changes files, commit only those changes as Nick Shanin without trailers, push the fork normally, post an add-only superseding gate with the exact new head, and use that repaired head for every fixed run and test overlay. Do not overwrite this job or claim green for an earlier head.

Archive commands, UTC times, exit codes, toolchain and logs separately for each step. Invoke Gradle with separate shell arguments; the earlier runner's malformed single-string check command is not a check result. Run steps sequentially under the host lock. Use --no-daemon and --no-build-cache; cleanTest must produce fresh XML for each focused leg. Preserve each step's XML and OUTPUT before the next cleanTest.

## Required matched pair

F leg: exact final fixed head, clean production and tests.

O leg: exact O production with only these three test files overlaid byte-for-byte from final F:

```text
solr/core/src/test/org/apache/solr/client/solrj/embedded/TestEmbeddedContentStreamCompatibility.java
solr/solrj/src/test/org/apache/solr/common/util/TestCommandOperationCompatibility.java
solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/TestLegacyContentStreamCompatibility.java
```

Use git restore --source=<final-F> --worktree -- <the three literal paths> in the original detached tree, or an equivalent exact-file copy. Record hash-object values on both legs, status and production diff. Do not cherry-pick the fixes onto O, overlay production files, weaken assertions, or use stale build results.

Run these three commands on BOTH O and F, archiving each command separately. Expected failures on O do not cancel the remaining commands:

```sh
./gradlew --no-daemon :solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.common.util.TestCommandOperationCompatibility -Ptests.seed=5235C0FFEE18351 --no-build-cache
./gradlew --no-daemon :solr:core:cleanTest :solr:core:test --tests org.apache.solr.client.solrj.embedded.TestEmbeddedContentStreamCompatibility -Ptests.seed=5235C0FFEE18351 --no-build-cache
./gradlew --no-daemon :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test --tests org.apache.solr.client.solrj.jetty.TestLegacyContentStreamCompatibility -Ptests.seed=5235C0FFEE18351 --no-build-cache
```

Acceptance, from fresh per-method XML and raw observations:

- All 18 methods execute on each leg, zero skips: 2 command API, 4 embedded, 12 HTTP. F must pass all 18.
- O: both public API reflection methods must fail with NoSuchMethodException, while F resolves and executes them.
- O: unnamed ContentStreamUpdateRequest must index one document instead of two. The generic legacy-stream case is expected to index zero instead of two because its inherited GenericSolrRequest writer returns null. Both cases must index both documents on F. Named streams and explicit multipart writer precedence must pass on both legs. The generic case is new coverage; report observed results, not this prediction as fact.
- O: all four HTTP legacy-body methods send wt=xml (6 bytes) instead of original-request-body (21 bytes). F must send the original bytes with Jetty/JDK and JavaBin/XML writers.
- O: all four HTTP override methods send original-request-body instead of custom-writer-body (18 bytes). F must send custom bytes and call the hook.
- The four HTTP explicit-writer controls pass on both legs, including form bodies; each now checks both built-in writer types and verifies the custom stream hook is never called. Retain exact body, hook and content-type assertions.
- Read raw bytes before any servlet parameter parsing. The prior empty-body observation was a fixture artifact; it must not be repeated as the wire result.

Predicted O totals: 12 regression failures and 6 passing controls. These are expected outcomes, not existing execution proof. A different failure must be investigated and classified rather than counted as the intended regression.

## Required neighboring tests and checks on final F

```sh
./gradlew --no-daemon :solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.client.solrj.impl.HttpJdkSolrClientTest --tests org.apache.solr.client.solrj.embedded.EmbeddedSolrServerTest -Ptests.seed=5235C0FFEE18351 --no-build-cache
./gradlew --no-daemon :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test --tests org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest -Ptests.seed=5235C0FFEE18351 --no-build-cache
./gradlew --no-daemon :solr:core:cleanTest :solr:core:test --tests org.apache.solr.client.solrj.embedded.TestEmbeddedSolrServerSchemaAPI --tests org.apache.solr.client.solrj.embedded.TestEmbeddedSolrServerResponseParser -Ptests.seed=5235C0FFEE18351 --no-build-cache
./gradlew --no-daemon tidy
env -u NODE_ENV ./gradlew --no-daemon check -x test --no-build-cache
```

Run the full named classes, including the author's two new HttpJettySolrClientTest methods and existing multipart upload. Read fresh XML, test counts and any assumptions/skips; no silently missing targeted class. Preserve individual test results even if later checks fail. Final tidy must leave a clean tree. The root check is required, not an affected-module substitute; disclose the NODE_ENV condition. Do not silently add exclusions to obtain green. Investigate environment problems and record any legitimate repair and rerun, preserving failed logs. An unresolved check, unexplained test failure or unexecuted proof leg leaves this job incomplete.

## Receipt and author package

Publish exact O/F/test SHAs, test blob hashes on both legs, ancestry, toolchain, seed, all command/exit-code pairs, per-method matched results, controls, neighboring counts/skips, tidy status, root check and execution conditions. Link durable evidence where available and identify any VM-local raw logs. Record code/test changes and any superseding head; refresh fork and PR heads at completion.

Include an author-facing draft following the PR 5232 example referenced in root Windows AGENTS.md. Link the exact current-PR-to-final-tested-head diff, summarize the fixes, and give pinned git switch/fetch/merge --ff-only commands plus copyable focused validation commands. Substitute the actual final tested SHA; verify ancestry against the live PR before proposing fast-forward commands. No placeholder or untested SHA may be presented as validated. This job does not authorize the gate worker to post to GitHub/JIRA or change the contributor's branch; return the receipt and draft for the Windows review follow-up.

On failure, post an add-only outcome explaining the failing assertion or environment condition and the needed repair. Do not overwrite prior evidence or classify a module check as root-check success.
