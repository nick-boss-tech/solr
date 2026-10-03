# SOLR-17029 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** No Gradle, no bats, no shellcheck run
locally (neither bats nor shellcheck is installed on this machine).
A reviewer must run the checks below before this goes near a PR.

## What the patch does

`bin/solr` word-split JVM options on every space in two places, so quoted
or escaped whitespace broke startup:

1. `SOLR_OPTS=(${SOLR_OPTS:-})` (script top-level): unquoted expansion
   split `-Dyak="white space"` into fragments and `java` died with
   `Error: Could not find or load main class space"`.
2. `--jvm-opts` value flowed through `ADDITIONAL_CMD_OPTS` into
   `start_solr`'s `SOLR_ADDL_ARGS` scalar and was passed **unquoted**
   (`$SOLR_ADDL_ARGS`) to the final `exec`/`nohup` java invocation.

Changes (all in `solr/bin/solr`):

- `SOLR_OPTS` is now parsed with `eval "SOLR_OPTS=(${SOLR_OPTS:-})"` so
  shell quoting rules are honored while keeping array semantics (later
  `SOLR_OPTS+=(...)` appends and `"${SOLR_OPTS[@]}"` consumption are
  untouched).
- `SOLR_ADDL_ARGS` scalar replaced by `SOLR_ADDL_ARGS_ARR`, parsed the same
  way inside `start_solr()`; both `exec` paths now pass
  `"${SOLR_ADDL_ARGS_ARR[@]}"` (quoted); the verbose settings echo prints
  `${SOLR_ADDL_ARGS_ARR[*]}`.
- The `# shellcheck disable=SC2086` comments on the exec lines are kept:
  they are still needed for the (out of scope) unquoted
  `$SOLR_JETTY_ADDL_CONFIG`.

Safety note for the reviewer: `eval` is used because bash has no builtin
for quote-aware word splitting. Exposure is contained — both values come
from the operator's own environment / command line, and the script already
runs with the operator's privileges. This matches the approach discussed
on the ticket.

**Open design decision (round-3 review, not changed here):** `eval` changes
the meaning of option strings that work today. Before, `SOLR_OPTS=(${SOLR_OPTS:-})`
only split on whitespace; `eval` also interprets `$`, backticks, `;`, `&`, `|`,
`(`, `)`, `<`, `>` and backslashes in the value. Examples that would change or
break: a ZooKeeper credential in `SOLR_OPTS` such as
`-DzkDigestPassword=pa$$word` (becomes the shell PID), an `&` or `;` in a
password (runs or backgrounds a command), an unbalanced `(` (syntax error at
script start), and a Windows-style `C:\path` (backslashes consumed).
`solr.in.sh` documents `SOLR_OPTS="$SOLR_OPTS $SOLR_ZK_CREDS_AND_ACLS"`. The
"operator's own environment" argument covers injection by a third party, not
breakage of existing values. Options to decide between: a quote-aware split that
does not execute expansions (for example reading the string through a small
`read`/`xargs`-style loop, or the `@Q` approach from the ticket), or accepting
`eval` and documenting it as a breaking change in the ref guide and
`solr.in.sh`. Also unchecked: `bin/solr start -e ...` re-passes `--jvm-opts`
through `RunExampleTool` (`" --jvm-opts \"" + jvmOpts + "\""` and
`addArgument`), so values with embedded quotes cross two more quoting layers.

Deliberately out of scope: `GC_TUNE_ARR=($GC_TUNE)` has the same smell
but was left alone per the research note (reviewers can ask).

## Files changed

- `solr/bin/solr`
- `solr/packaging/test/test_start_solr.bats` (two new tests, see below)

## New bats tests

In `test_start_solr.bats`:

- `"SOLR-17029 quoted whitespace in SOLR_OPTS"`: starts Solr with
  `SOLR_OPTS='-Dsolr.17029.prop="white space"'`, asserts it comes up, then
  (round-3 patch pass) asserts `/solr/admin/info/properties` lists
  `solr.17029.prop` with the value `white space`.
- `"SOLR-17029 quoted whitespace in --jvm-opts"`: the same through
  `--jvm-opts`.

Not added, because it depends on the decision above: a test that a value with
`$`, `&` or `;` passes through unchanged. It would fail with the current `eval`.
Changelog fragment added: `changelog/unreleased/SOLR-17029.yml` (states the
quoted-whitespace fix only; revise it, and add a ref-guide note, with the
decision). Bats and shellcheck are not available here; nothing was run.

Pre-fix both fail (java cannot find main class `space"`; the port never
opens and `solr assert --started` times out). Post-fix both should pass.

## Recommended reviewer commands

```bash
shellcheck solr/bin/solr
# run just the new tests (bats test names support filtering):
gradlew :solr:packaging:iTest --tests "test_start_solr.bats"  # or the repo's
# documented equivalent: gradlew iTest --tests test_start_solr.bats
```

Also worth a manual check: `solr start --verbose --jvm-opts '-Dyak="white space"'`
and confirm the `SOLR_ADDL_ARGS` echo line shows the value intact.

## Patch limits and follow-ups

- **Not tested at all** — no bats run, no shellcheck run, not even a real
  `bin/solr start` (only `bash -n` syntax check + isolated eval-semantics
  probe of the parse snippet).
- If reviewers dislike `eval`, the alternative discussed on the ticket is a
  `@Q`-based round-trip (bash >= 4.4); the reporter (hossman) explicitly
  invited the patch and a demonstrating bats test, both provided here.
- Remove this file before opening the upstream PR.
