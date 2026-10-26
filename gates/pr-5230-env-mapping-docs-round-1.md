# Gate: PR 5230 corrected environment guidance, round 1

- Status: OPEN. Intended host: any capable Linux gate host with JDK 21, Node/npm and the Solr Ref Guide toolchain.
- Tags: gate, premise-run. Posted by Windows Codex on Nick's explicit PR 5230 patch instruction.
- Ticket / PR: SOLR-17988 / https://github.com/apache/solr/pull/5230.
- Fork branch: `solr-17988-docs`.
- Exact documentation head: `bf285189f74b775165fc53ba305d35dccf880a47`.
- Source-behavior baseline: Solr 10.0.0 release `6c6c48a6f78486130682ea9c1f7a2723af5a87be`.
- Module: `solr/solr-ref-guide`; focused Java test classes and random seed: not applicable to this documentation-only patch.
- Claiming host, runner path and absolute log directory: record these in the new gate claim before execution, after the detached tidy preflight. Serialize behind the host's build lock.
- Implementation claim: `claims/pr-5230-env-mapping-windows-round-1.md`.
- Receipt: `receipts/SOLR-17988.md`, owned by the gating host. Check for an existing newer receipt before claiming.

## Why a fresh gate

This head corrects two operator-facing inaccuracies: exported legacy environment variables still map through Java's deprecated aliases, and exported `SOLR_UI_ENABLED=false` also disables the classic UI. The previous round-3 Ref Guide report passed at `16d3005293d3c6a047277bc586b30e05b6b6ef11`, before these guide edits. It cannot validate this head. The older round-1/round-2 environment failures and successful round-3 report remain unchanged.

## Required proof

1. Confirm the live fork branch and PR head still equal the pinned documentation SHA. Stop on movement; do not silently retarget. Use a clean standalone clone with a real `.git` directory for the Ref Guide build. The earlier linked-worktree Antora attempt failed before the guide could build.
2. Run the board's detached tidy preflight at this SHA (`./gradlew tidy`), recording the command, exit code and `git diff --exit-code`. If tidy changes files, follow the board's repair process: commit/push only the scoped repair with Nick's identity, record the new head and post a superseding exact-head gate. Never count old-head proof for a repaired head.
3. At the unchanged pinned head, run these exact tasks, recording separate exit codes and complete logs:

   ```sh
   ./gradlew :solr:solr-ref-guide:buildLocalSite
   ./gradlew :solr:solr-ref-guide:checkSiteLinks
   ./gradlew :solr:solr-ref-guide:checkCliDocsUpToDate
   ```

   All must pass. Inspect the rendered changed section and link-check summary. Record tool versions and final clean-tree status. Do not substitute the ambiguous `buildLocal` task.
4. In a separate clean Solr 10.0.0 environment at the behavior baseline, run narrow inherited-environment probes through the actual `EnvUtils` initialization, in fresh JVMs without conflicting explicit properties. An out-of-tree Java probe that initializes EnvUtils and prints `solr.ui.enabled` and `solr.cloud.wait.for.zk.seconds` is sufficient for property conversion. Keep its source, exact compile/classpath/run commands, stdout/stderr and warning output with the logs; do not add the probe to the PR. Compare each case with a clean baseline:

   | Input inherited by Java | Expected effective property |
   | --- | --- |
   | `SOLR_WAIT_FOR_ZK=45` | `solr.cloud.wait.for.zk.seconds=45`, deprecated-alias warning |
   | `SOLR_CLOUD_WAIT_FOR_ZK_SECONDS=45` | `solr.cloud.wait.for.zk.seconds=45` through the current name |
   | `SOLR_ADMIN_UI_DISABLED=true` | `solr.ui.enabled=false`, deprecated-alias warning |
   | `SOLR_UI_ENABLED=false` | `solr.ui.enabled=false` |

   In separate fresh POSIX shells, first unset the variable, then demonstrate that an unexported include assignment is absent from the child JVM while its exported counterpart is inherited. Confirm the corresponding explicit `-D` alternatives take effect when supplied via the launcher's `SOLR_OPTS` path.
5. Verify the release startup-script module decision for `SOLR_UI_ENABLED=false` and, separately, `SOLR_UI_EXPERIMENTAL_ENABLED=false`: each omits `--module=new-ui`. Record source-consumer checks showing the classic UI reads `solr.ui.enabled`, while the experimental-only switch does not set that property. Do not infer Java behavior solely from missing shell-script reads. A full running server/UI smoke test is optional; distinguish property/module proof from an HTTP result.

## Acceptance and failure handling

The gate is green only when the exact-head guide checks and the isolated behavior probes agree with the corrected prose. This is a documentation correction; a Java fail-before regression test is not required. No Windows builds were performed.

An executed failing check requires a recorded investigation and scoped repair before retry. Preserve failed logs; do not widen the scope or silently repeat it. If the isolated release behavior contradicts the prose, report the case and correct the docs before obtaining new-head guide proof. No full core or UI-module test suite is requested.

Write the receipt with documentation SHA, behavior-baseline SHA, all commands/exit codes, absolute artifact paths, rendered-page result and warning evidence. Link this gate and the implementation report. Its final section should provide a concise corrected PR proof paragraph; posting a public comment or changing draft status remains outside this gate.
