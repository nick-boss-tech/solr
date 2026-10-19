# Skip record: SOLR-16772

- Ticket: SOLR-16772, "SOLR_LOG_LEVEL's effects are buggy/non-intuitive, should be handled by log4j2.xml". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2023-04-25, updated 2023-05-03, no fix version. Links: SOLR-16044 (Closed). One comment, from hossman (2023-04-25), proposing to remove `StartupLoggingUtils.changeLogLevel` and rely on `${solr.log.level:-...}` defaults in `log4j2.xml`.
- Verdict: keep-open. Unchanged from the earlier record; this record replaces the audit-only record with a pinned-head code check.
- Draft branch: solr-16772-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 14:36-14:37 UTC.

## Ticket reading

The `solr.log.level` system property (`SOLR_LOG_LEVEL`) is applied by a call that changes only the root logger level, after some logging has already happened. Other loggers configured explicitly in `log4j2.xml` keep their levels, and the output does not match what the property suggests. The proposal is to move the level into `log4j2.xml` with property fallbacks, and remove the programmatic change.

## Code reading

- `solr/core/src/java/org/apache/solr/servlet/CoreContainerProvider.java:163-165`: reads `SOLR_LOG_LEVEL`, logs "Log level override", and calls `StartupLoggingUtils.changeLogLevel(logLevel)`.
- `solr/core/src/java/org/apache/solr/util/StartupLoggingUtils.java:100-112`: `changeLogLevel` gets the root `LoggerConfig` from the context and sets its level, then calls `ctx.updateLoggers()`. Only the root logger is changed, which is the behavior the ticket describes.
- `CoreContainerProvider.java:156-160`: the same class handles the console mute (`SOLR_LOG_MUTECONSOLE`) with `StartupLoggingUtils.muteConsole()`, which is part of the same design question.
- `solr/core/src/java/org/apache/solr/cli/ToolBase.java:113`: a CLI tool also calls `changeLogLevel("WARN")`, so the method has more than one caller.

What this shows at the pin: the root-only change and the programmatic path are as the ticket describes. The proposal would remove them and move the control into `log4j2.xml`; the removal touches the CLI tool as well, so it is a cross-cutting change.

## History

- No commit on the pinned head names SOLR-16772 (evidence tool: no hits).
- SOLR-16044 (linked, Closed) is not traced in this pass.

## Reproduction attempts

Not attempted. Showing the mismatch needs a run with a logger configured in `log4j2.xml` and the property set, which is a test run; this pass is review-only.

## Gap analysis

The defect is a design concern the maintainers have not settled: whether the startup log-level path should be removed in favor of `log4j2.xml` fallbacks. The code matches the ticket's description, so the behavior is real and surprising. The fix is a redesign with several callers (the server startup and the CLI tool), and the ticket records no consensus.

Missing piece for a verdict change: a decision from the logging maintainers on the redesign, including the console mute and the CLI caller. With that, a PR (or a close as a wontfix) can be written against a defined design.

## Verdict justification

keep-open meets the rubric: the code path is read at the pin (`CoreContainerProvider.java:156-165`, `StartupLoggingUtils.java:100-112`, `ToolBase.java:113`), no attempt is run, and the missing piece is named (the design decision). close-fixed and close-obsolete are not met: the code is still present. close-answered is not met: the docs do not answer the design question. pr-needed is not met: no design is agreed, and the change crosses more than one caller.

## Limits

This record does not test the logging mismatch, does not check SOLR-16044, and does not decide the redesign. It does not post anything to JIRA or GitHub.
