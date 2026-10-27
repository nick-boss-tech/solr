# Gradle gate gotchas (Solr pool)

Read before running a `check`, a site build, or a browser test. Each entry is a failure a gate host has already hit, with the fix that worked. Fixes here are environment or run-procedure fixes. They do not change the candidate code, and they do not need a new head.

## 1. `check` run in a git worktree fails in Antora

Symptom: `:solr:solr-ref-guide:buildLocalAntoraSite FAILED` with `Process '.../npx' finished with non-zero exit value 1`. The Gradle log shows no Antora message, because the Npx task does not log Antora's output.

Cause: Antora requires its local content source to be a git repository directory. A git worktree's `.git` is a file (`gitdir: ...`), so Antora rejects it with `Local content source must be a git repository: <worktree>`.

Confirm it directly, without a rebuild. Run from the Node project directory, not the staging directory (`~+` in the playbook resolves against the current directory):

```
cd <worktree>/solr/solr-ref-guide/.gradle/node
PATH=$PWD/nodejs/node-v22.18.0-linux-arm64/bin:$PATH \
  ./node_modules/.bin/antora --to-dir /workspace/tmp/antora-repro <worktree>/solr/solr-ref-guide/build/site-staging/playbook.yml
```

Running it from `build/site-staging` gives a different error (`Failed to read ui.supplemental_files entry`). That is a wrong-directory artifact, not the cause.

Fix: run `check` in a standalone clone of the same SHA, not a worktree. Shallow is fine for `check`:

```
AUTH=$(set -a; . /workspace/.env; set +a; printf 'x-access-token:%s' "$GITHUB_TOKEN" | base64 -w0)
git -c http.extraheader="AUTHORIZATION: basic $AUTH" clone --depth 1 --branch <branch> https://github.com/nick-boss-tech/solr.git <clone-dir>
git -C <clone-dir> rev-parse HEAD   # must equal the pinned SHA; status must be empty
pool/bin/sweep-run.sh <clone-dir> <log dir> <run name> check -x test   # with env -u NODE_ENV
```

Keep `git diff --check` in the worktree, which has the full history. A shallow clone cannot check a base commit that is not in its history.

Record the layout failure in the receipt and keep its log. It is not proof for the check leg.

`pool/bin/sweep-run.sh` now refuses a `check`, `:check`, `solr-ref-guide` or Antora task when the worktree's `.git` is a file. It exits 5 and writes the refusal to the run log, so no Gradle time is spent on it.

## 2. `NODE_ENV=production` skips devDependencies

Symptom: the Antora or js-client npm steps fail to find devDependencies. The vm4 shell sets `NODE_ENV=production`.

Fix: `env -u NODE_ENV` for every Gradle and npm invocation, and disclose it in the receipt. Do not change the host shell.

## 3. Linux ARM browser tests: Selenium Manager refuses

Symptom: `NoSuchDriverException: Unable to obtain: chromedriver` with `Linux ARM is not supported by Selenium Manager`. Putting chromedriver on `PATH` does not help: Selenium 4.47 does not search `PATH`.

Fix: pass the driver path to the forked test JVM with `JAVA_TOOL_OPTIONS=-Dwebdriver.chrome.driver=<absolute chromedriver>`, and add `--no-daemon`. A running Gradle daemon keeps the old environment, so `--no-daemon` is required when the variable changes. Keep the browser override `-Dtests.selenium.chrome.binary=<absolute browser>` as well.

Evidence that the setting reached the test JVM: the Java launcher line `Picked up JAVA_TOOL_OPTIONS: -Dwebdriver.chrome.driver=...` appears in the test-worker output, right after `> Task :<module>:test`. Line 1 of the log is the Gradle client and does not count. The JUnit XML does not record system properties.

Use the chromedriver whose version matches the browser (for example, Chrome for Testing 156.0.8078.4 with chromedriver 156.0.8078.4). Check `--version` before the run.

## 4. Long-running gate drivers must be detached with `setsid`

Symptom: a driver started with `nohup ... &` stopped silently partway through, with no end line, no error, and no test output. This happened twice on vm4, once during tidy and once at the start of a test leg.

Fix: start the driver with `setsid -f <script> > <out> 2>&1 < /dev/null`, and write a status line before and after each leg. A missing end line means the driver died. Check that no Gradle process is still running and that the build lock is free before relaunching. Relaunch once at most, and record the interruption.

## 5. Tidy can reformat the gate head

`./gradlew tidy` at the offered head may wrap or reformat lines. Under WORKFLOW.md, that is a formatting repair: commit it with the gate identity and no trailers, push fast-forward, verify the remote SHA, and write a superseding gate pinned to the new head before final proof. Runs at the old head are not proof.

## Related

- `pool/bin/gate-preflight.sh` runs tidy on a fresh clone before a claim. It does not run `check`, so it does not catch item 1.
- `pool/bin/sweep-run.sh` serializes Gradle through the host lock. It does not check whether the worktree is a worktree.
