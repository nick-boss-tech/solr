# solr-9342-submit

- Branch: origin/solr-9342-submit
- Head: 833e11192a7f (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind, 3 commits ahead)
- Scope: 3 commits, 3 files. `solr/bin/solr` (exports `TZ="${TZ:-$SOLR_TIMEZONE}"` into the server JVM launch only, via `env`, in the foreground and background branches), `solr/packaging/test/test_start_solr.bats` (one new BATS test), changelog `SOLR-9342-gc-log-timezone.yml` (`type: fixed`). No Java.
- Verdict: Not ready (unchanged from the bulk verdict). The default configuration is aligned. The override paths the JIRA reporter uses still diverge.
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. The BATS test was read, not executed. Every claim rests on reading the diff and the code at the listed head.

## Script-only check (handoff item)

Confirmed script-only at this head. The three files are a shell launcher, a shell test, and a changelog. No Java, no packaging manifest, no build file. One correction to the handoff record: `solr/bin/solr` is the shipped launcher that end users run, not developer tooling. The "developer tooling" label in the round-36 handoff should be corrected on the Linux side.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-9342-review.md` (Not ready) reviewed the live head `833e11192a7f`, which matches the listed head. Its assignment snapshot was `6e1ade86ec78`, one commit earlier. No delta since the bulk pass.

- Bulk F1 (HIGH, JVM timezone and GC timezone can still disagree): **confirmed.** See finding 1 for each override path.
- Bulk F2 (LOW, regression covers one precedence case only): **confirmed.** The BATS test (`test_start_solr.bats:59-66`) unsets `TZ` and sets `SOLR_TIMEZONE`. It does not cover a preset `TZ` or a `-Duser.timezone` override.
- Bulk changelog point (the logs share a timezone "unless TZ is already set"): **confirmed and widened.** The claim is also false with `TZ` unset, when `-Duser.timezone` is in `SOLR_OPTS`. See finding 1(b).

## Findings (ranked)

1. **HIGH, verified (code path). The server has two independent timezone sources, and the supported override paths diverge.** The Java property comes from `SOLR_TIMEZONE`, which defaults to UTC (`solr/bin/solr:1196`, `: "${SOLR_TIMEZONE:=UTC}"`) and is passed as `-Duser.timezone=$SOLR_TIMEZONE` (`:1329`). The child process `TZ` is `${TZ:-$SOLR_TIMEZONE}` (`:1352`, `:1356`). Three cases:
   - (a) Operator presets `TZ` to a value different from `SOLR_TIMEZONE`: the GC log follows `TZ`, Solr's log follows `SOLR_TIMEZONE`. The two disagree.
   - (b) Operator sets `-Duser.timezone=PST` in `SOLR_OPTS` (the JIRA reporter's setup): `SOLR_OPTS` is appended after the scripted option (`:1334`), so Java logging uses PST. `TZ` is still the default UTC from `SOLR_TIMEZONE`, so the GC log stays UTC. The two disagree, with `TZ` unset, which contradicts the changelog's "unless TZ is already set".
   - (c) Default (no `TZ`, no `-Duser.timezone`): both use `SOLR_TIMEZONE`. Aligned. This is the only path the branch tests.
   - Proposed fix (not applied): compute one effective zone and apply it to both mechanisms, or document `SOLR_TIMEZONE` as the only supported knob and say so in the changelog. Owner call 1.

2. **LOW, verified. The BATS test covers only the default precedence.** `test_start_solr.bats:59-66`. It cannot catch finding 1(a) or (b). A test for each case, with the expected alignment or an explicit documented divergence, would make the owner's decision testable.

3. **LOW, verified. The Windows launcher is not covered.** `solr/bin/solr.cmd` still sets `set START_OPTS=-Duser.timezone=%SOLR_TIMEZONE%` (`solr.cmd:1100`) with no `TZ` handling. The branch changes only the bash launcher. The changelog does not say the fix is Linux/macOS only. Owner call 2.

4. **LOW, verified (checked, no issue). Process ID handling is unchanged.** `nohup env ... "$JAVA" ...` and `exec env ... "$JAVA"` both `exec` through to the JVM, so `echo $! > .../solr-<port>.pid` (`:1358`) still records the JVM PID. `TZ` is scoped to the child environment, so the caller's shell is untouched. `${TZ:-...}` is safe with `set -u` not enabled (`:72`, commented out).

5. **LOW, hypothesis. The BATS assertion depends on unverified details.** It assumes the GC log is `solr_gc.log` (`:1088`, `:1097`, both confirmed) with unified-logging `time` decoration ending in `+0530]`. That was not checked against a real run. The test was not run here.

## Owner calls (not decided here)

1. **Which timezone configuration is supported?** Option A: make the script derive one effective zone from `TZ`, `SOLR_OPTS` `-Duser.timezone`, and `SOLR_TIMEZONE`, and apply it to both the Java property and the child `TZ`. Option B: document that `SOLR_TIMEZONE` is the only supported setting and that `-Duser.timezone` in `SOLR_OPTS` or a preset `TZ` is unsupported. The bulk review's note is right that the reporter's configuration must be settled with the reporter before the fix is finished. This review does not choose.
2. **Windows scope.** Should `solr.cmd` get the same change, or should the ticket be scoped to Linux/macOS? The branch as written is silent.

## Proposed fixes (not applied; the owner decides)

- Finding 1: pick option A or B above. For A, one function computes the effective zone and both launch paths use it. For B, update the changelog to name `SOLR_TIMEZONE` as the only supported setting.
- Finding 2: add BATS cases for a preset `TZ` and for `SOLR_OPTS=-Duser.timezone=...`, asserting the agreed behavior.
- Finding 3: decide the Windows scope and say it in the changelog.

## Not checked

- Nothing compiled, formatted, or run. The BATS test was read, not executed (finding 5).
- Unified-logging time-decoration format was not checked against a JDK run.
- The JIRA comments (the reporter's `-Duser.timezone=PST` case, the Mac/Java 8 `TZ` note) were not re-read here; the bulk review's summary stands as context.
- Other launch paths (for example `solr-exporter`, `post`) were not checked for `TZ` handling. The branch does not change them.
