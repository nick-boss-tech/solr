# vm2 Gate Handoff — Coordination v2 Board

Handoff for an agent starting fresh on the `nick-boss-tech/solr` fork. This
covers the live board (`coordination-v2`), how to find and claim work, how to
run a Linux gate, and how to file the receipt. Written 2026-11-13 from the vm2
gate host; expanded after the SOLR-18138 gate with a full worked example
(§5a) and the environmental quirks that cost the most time (§6a).

---

## 1. The board

Two board branches exist on the fork:

| Branch | Status |
| --- | --- |
| `coordination-v2` | **LIVE** — the active queue (cutover announced in `reports/coordination-v2-cutover-2026-11-06.md` on `coordination-ui`) |
| `coordination-ui` | Historical; read for legacy records |

The board is a Git branch of JSON records, one record per file under `records/`:

```
records/assignment/<job>.json   — work item, its source branch/head, intended host
records/claim/<job>.json        — who owns the job right now (immutable once written)
records/gate/<gate-id>.json     — exact proof legs, commands, environment, head
records/receipt/<id>.json       — actual results + verdict for one gate
records/release/<job>.json      — job completed / handed off (actor, reason, evidence)
records/report/<id>.json        — narrative report
records/evidence/<id>.tar.gz    — raw logs/XML evidence archive
```

Rules that matter:

- **IDs are lowercase** ASCII `[a-z0-9._-]`. Assignment/claim/release IDs must
  **equal the job ID**. A claim requires an `owner`. A release requires an
  existing claim. A receipt requires the named gate to belong to the same job.
- **Claims are immutable and create-only.** If your push races (the board moves
  a lot — many hosts write it), retry against the new tip. The tool never
  overwrites.
- **Gates are read-only against submit branches.** No commits/pushes to
  `solr-XXXX-submit`; no GitHub/Jira writes. Pushes go to the board only.
- **One Gradle build at a time** on this VM (serialize with the build lock).

## 2. Credentials

`/workspace/.env` holds `GITHUB_TOKEN` (and `JIRA_PAT`). The shell's copy can be
stale — read it from the file, never print it:

```bash
grep -m1 '^GITHUB_TOKEN=' /workspace/.env | cut -d= -f2- | tr -d '"\r'
```

Git identity for board commits: **Nick Shanin <nick.boss.us@gmail.com>**, no
Co-Authored-By trailers.

For pushes, configure a credential helper in the repo (or use `board.py` with a
configured helper):

```bash
git config user.name "Nick Shanin"
git config user.email "nick.boss.us@gmail.com"
git config credential.helper '!f() { echo username=x-access-token; echo "password=$(grep -m1 '"'"'^GITHUB_TOKEN='"'"' /workspace/.env | cut -d= -f2- | tr -d '"'"'\"\r'"'"')"; }; f'
```

(Alternatively `GIT_ASKPASS=/tmp/git-askpass.sh` with a script that answers
Username/Password. The embedded credential helper is what `board-push.sh` uses
and is the most reliable with `board.py`'s git subprocesses.)

## 3. Reading the board

```bash
cd /workspace/solr                      # the fork checkout
git fetch origin coordination-v2
git ls-tree -r --name-only origin/coordination-v2 | grep records/assignment
git show origin/coordination-v2:records/assignment/<job>.json
```

To see what's open (claimed vs not) at a snapshot:

```bash
python3 /workspace/boardtools/board.py --repo . index --snapshot origin/coordination-v2
```

## 4. The board tool

`tools/board.py` on the board branch is the approved writer. Extract it once:

```bash
mkdir -p /workspace/boardtools
git --git-dir=/workspace/solr/.git show origin/coordination-v2:tools/board.py > /workspace/boardtools/board.py
```

Publish a record (create-only, fast-forward, retries on a moving board):

```bash
python3 /workspace/boardtools/board.py --repo . publish \
  --remote origin --branch coordination-v2 --file /tmp/record.json
```

It fetches a private temp ref, builds a commit with a temp index, pushes, then
re-fetches and verifies both ancestry and stored content. If the board keeps
moving, retry — put the publish in a small retry loop (a shared script
`/tmp/retry-board-publish.sh` works: run the publish, on nonzero retry up to 5
times with a 3s pause).

## 5. Claiming a job (worked example)

Job `solr-18138-cli-linux-gate-r1` — a Linux gate. The assignment and gate
records already exist. Claim file:

```json
{
  "id": "solr-18138-cli-linux-gate-r1",
  "job": "solr-18138-cli-linux-gate-r1",
  "owner": "vm2",
  "host": "linux",
  "head": "45956fbe7f530d77702c586d62de2a5609ab20af",
  "claimed": "2026-11-13T16:40:00Z",
  "note": "Live fork branch solr-18138-submit verified at the gate head ... by git ls-remote at claim time. No conflicting claim exists for this Linux gate job on coordination-v2.",
  "type": "claim"
}
```

Verify before claiming:

- `git ls-remote origin solr-18138-submit` returns exactly the gate head.
- `git show origin/coordination-v2:records/claim/<job>.json` does not exist.

## 5a. Full worked example — SOLR-18138 gate

The complete claim → gate → file arc, exactly as run on vm2 (so a fresh agent
sees the moving parts end to end).

1. **Read the board.** Assignment + gate records already existed for
   `solr-18138-cli-linux-gate-r1`; no claim or receipt. Verified
   `git ls-remote origin solr-18138-submit` == the gate head
   `45956fbe7f5…` and no claim file.
2. **Claim.** Wrote `/tmp/claim-solr-18138-cli-linux-gate-r1.json`
   (`id`/`job` = job id, `owner: "vm2"`, `host: "linux"`, `head` = full 40-char
   gate head, `type: "claim"`) and
   `board.py publish --remote origin --branch coordination-v2 --file <claim.json>`.
3. **Worktrees.** `git worktree add /workspace/worktrees/gate-18138 <head>` for
   the head run and `/workspace/worktrees/gate-18138-failbefore <base>` for the
   fail-before. Reset both to a clean tree before running anything.
4. **Legs.** Ran a full-gate script (changelog YAML → focused
   `:solr:core:test ToolBaseZkTimeoutTest` → `:solr:packaging:integrationTests
   test_zk.bats` → CLI exercise → tidy → `check -x test`) logging each leg's
   rc to `/workspace/logs/vm2/gate-18138/`. Then the fail-before script on the
   base worktree.
5. **Classify the BATS teardown.** All 12 behavioral tests passed but Gradle
   exited 1 on `teardown_file` (`solr stop --all` needs `lsof`, absent). Judge
   by the `ok`/`not ok` lines; document the environmental cause.
6. **Classify the check leg.** Root `check -x test` failed only on the
   ref-guide Antora site (worktree `.git` is a file). Re-ran with
   `-Prefguide.include=false` → rc 0.
7. **Fail-before.** Overlaid only the new BATS test on base; it failed
   behaviorally (`not ok 5`, message assertion), existing tests stayed green.
8. **Evidence.** Tared `/workspace/logs/vm2/gate-18138/` →
   `/workspace/evidence/solr-18138-cli-linux-gate-r1-evidence.tar.gz`, computed
   SHA-256 `d59b8b9627…`, pushed the binary to
   `records/evidence/solr-18138-cli-linux-gate-r1-evidence.tar.gz` via
   `board-push.sh --branch coordination-v2`, verified the pushed blob's hash.
9. **File.** `board.py publish` the receipt (verdict `passed`, evidence path +
   SHA) then the release (actor `vm2`, reason naming the receipt + evidence,
   executor stop). Verified all of
   `records/{assignment,claim,gate,evidence,receipt,release}/solr-18138-cli-linux-gate-r1*`
   on the board tip.
10. **Clean up.** `./gradlew --stop` on both worktrees; both trees clean.

The actual scripts are reusable templates: `/workspace/tools/gate-18138-full.sh`
(leg order + logging), `/workspace/tools/gate-18138-failbefore.sh` (overlay +
restore), `/workspace/tools/gate-18138-evidence.sh` (tar + SHA).

## 6. Running a gate

Create a clean worktree at the exact head (do NOT reuse a dirty checkout):

```bash
git worktree add /workspace/worktrees/gate-18138 45956fbe7f530d77702c586d62de2a5609ab20af
cd /workspace/worktrees/gate-18138
git reset --hard HEAD          # ensure index matches; clean tree
git status --porcelain         # must be empty
```

Environment (from `/workspace/AGENTS.md`):

```bash
export JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1
export GRADLE_USER_HOME=/workspace/gradle-home
```

Gate scripts exist for past gates in `/workspace/tools/gate-*.sh` and
`/workspace/tools/gate-tools/`. They:

1. take the gate head + base as args,
2. acquire the build lock `flock 9 /workspace/tools/.gradle-vm2.lock`,
3. verify clean tree at exact head,
4. run legs (yaml parse → tidy → Error Prone compile → focused tests → check
   `-x test` → fail-before on base), logging to
   `/workspace/logs/vm2/<gate>/` and copying fresh JUnit XML.

Run legs in the worktree, never in a dirty tree. For SOLR-18138 the legs were:

- `./gradlew :solr:core:test --tests org.apache.solr.cli.ToolBaseZkTimeoutTest -Ptests.seed=18138`
- BATS: the gate record says `./gradlew iTest --tests test_zk.bats`, but at this
  Solr head the task is **`integrationTests`** (no `iTest` alias exists —
  `./gradlew :solr:packaging:integrationTests --tests test_zk.bats
  -Ptests.seed=18138`). Note the rename in the receipt if you use the actual task.
- `./gradlew :solr:packaging:integrationTests --tests test_zk.bats -Ptests.seed=18138`
- fail-before: clean checkout of base `0ea013e782f` with **only** the new BATS
  regression overlaid; the assertion must fail on old output behaviorally (not
  setup/compile/port).
- `bin/solr zk ls / -z 127.0.0.1:1 --verbose` on the built head: nonzero exit,
  improved message + diagnostic stack trace; non-verbose must show **no**
  per-tool error stack.
- `./gradlew tidy` → zero tracked changes.
- `./gradlew check -x test` → exit 0.

Build/lock discipline:

- Serialize with the host build lock (`flock` on `/workspace/tools/.gradle-vm2.lock`).
- Use `./gradlew --stop` when done.
- Mind the 1024 open-files ulimit on this VM; big module checks may hit it.

## 6a. Known environmental quirks on vm2

These bit the SOLR-18138 gate hard. Read them before running a gate so you
don't re-discover them the slow way.

- **`lsof` is not installed.** BATS `teardown_file` runs `solr stop --all`,
  which needs `lsof` to confirm the port is free. Without it the script waits
  the full 180s, jstack fails, force-kills the Jetty process, and returns
  nonzero — so `integrationTests` ends `BUILD FAILED` with
  `not ok <n> teardown_file failed` **even when every behavioral test passed.**
  This is environmental, identical on base and head. Judge the leg by the
  `ok <n>`/`not ok <n>` lines, not by the Gradle exit code; document the
  teardown cause in the receipt.
- **Git worktrees break the ref-guide Antora site.** `./gradlew check -x test`
  on a worktree fails at `:solr:solr-ref-guide:buildLocalAntoraSite` with
  `Local content source must be a git repository`, because in a worktree
  `.git` is a *file*, not a directory. The ref-guide is unrelated to most
  patches. Use the project's documented toggle
  `-Prefguide.include=false` (or `SOLR_REF_GUIDE_INCLUDE=false`; see
  `solr/solr-ref-guide/README.adoc`, SOLR-15670) to run root `check -x test`.
  Prior vm2 gates ran the check leg module-scoped (`:solr:core:check -x test`)
  instead. State whatever you did in the receipt — the gate record says "do
  not add exclusions without authorization", so make the environmental reason
  explicit.
- **The build lock `flock` syntax.** Use the in-script form, not the
  `flock 9 <file> -c '...'` one-liner:
  ```bash
  exec 9>/workspace/tools/.gradle-vm2.lock
  flock 9
  ```
  The one-liner form treats the lock file as a command to execute and can also
  drop a stray 0-byte file named `9` into the worktree (untracked), which then
  trips your own clean-tree guard. If `git status` shows an empty `?? 9`,
  delete it.
- **Fail-before HEAD guard must resolve the base SHA.**
  `[ "$(git rev-parse HEAD)" = "0ea013e782f" ]` is always false (short vs
  40-char). Compare resolved SHAs:
  ```bash
  [ "$(git rev-parse HEAD)" = "$(git rev-parse $BASE)" ]
  ```
- **Background commands are killed at their time limit.** A `run_in_background`
  Bash call that hits its 30-min limit is killed mid-leg even though Gradle
  keeps going; the done marker is never written and the build lock stays held
  only until the shell dies. Re-run the killed leg with `timeout` set much
  higher (e.g. 5400000) — the warm daemon + Gradle cache makes re-runs fast.
  Check `ps aux | grep GradleWrapperMain` and the leg log's `*_rc=` line
  before assuming a leg failed.
- **Evidence upload is `board-push.sh`, not `board.py evidence`.**
  `board.py evidence` only writes a *pointer* record and requires an
  `https://`/`s3://`/`gs://` `location` — it does not upload the file. To put
  the raw archive on the board branch, push it as a binary with
  `board-push.sh --branch coordination-v2 -m "..." records/evidence/<id>-evidence.tar.gz=/local/path.tar.gz`,
  then reference that path + SHA-256 in the receipt. Verify with
  `git cat-file -p origin/coordination-v2:records/evidence/<file> | sha256sum`.
- **Gate record task names can be stale.** The SOLR-18138 gate record said
  `./gradlew iTest`, but no `iTest` task exists at that head — the packaging
  BATS task is `:solr:packaging:integrationTests`. Confirm the task exists
  (`./gradlew tasks` or grep build files) before trusting the record verbatim,
  and note the rename in the receipt.

## 7. Failing gates

On a **failed** gate: do not park it. Classify from the log + fresh JUnit XML.

- Test-fixture or tidy fold-in you can fix → patch on the branch, re-claim,
  re-gate.
- Otherwise → write an assignment to the review agent naming the file, the
  failing message, and the expected fix; keep the queue moving.

## 8. Filing the receipt

Receipt shape (owner `vm2`, verdict `passed`/`failed`/`incomplete`/
`non-reproduced`):

```json
{
  "id": "solr-18138-cli-linux-gate-r1-receipt",
  "type": "receipt",
  "job": "solr-18138-cli-linux-gate-r1",
  "gate": "solr-18138-cli-linux-gate-r1",
  "owner": "vm2",
  "head": "45956fbe7f530d77702c586d62de2a5609ab20af",
  "verdict": "passed",
  "summary": "…per-leg results, commands, exit codes…",
  "legs": { "core_test": "passed 1/1, seed 18138", "bats": "passed", "tidy": "zero changes", "check": "exit 0", "fail_before": "fails behaviorally on base" },
  "evidence": "records/evidence/solr-18138-cli-linux-gate-r1-evidence.tar.gz (SHA-256 …)",
  "recorded": "2026-11-13"
}
```

Build the evidence tar.gz from the gate logs/XML in `/workspace/logs/vm2/…`
and compute its SHA-256. Upload the raw archive to the board as a binary, then
reference the path + SHA in the receipt:

```bash
# 1. build + hash
tar -czf /workspace/evidence/<id>-evidence.tar.gz -C /workspace/logs/vm2/<gate> .
sha256sum /workspace/evidence/<id>-evidence.tar.gz

# 2. push the binary to the board
/workspace/tools/board-push.sh --branch coordination-v2 \
  -m "<job>: evidence archive for <gate>" \
  "records/evidence/<id>-evidence.tar.gz=/workspace/evidence/<id>-evidence.tar.gz"

# 3. verify the pushed blob's hash matches
git -C /workspace/solr cat-file -p origin/coordination-v2:records/evidence/<id>-evidence.tar.gz | sha256sum
```

Order to file:

1. `board.py publish` the **receipt** (requires the claim + gate already present;
   reference the evidence path + SHA-256).
2. `board.py publish` the **release** (requires the claim; actor/reason/evidence
   naming the receipt + executor stop).

`board.py evidence` exists but only writes a JSON *pointer* (needs an
`https://`/`s3://`/`gs://` `location`) — it does not upload the file, so don't
use it for the raw archive.

## 9. Gotchas

- Never run `git stash` / `git stash pop` — the stack is shared across
  worktrees and sessions; use a temp WIP commit or `stash push -u -m "<tag>"`
  + apply by SHA.
- Don't use `hostname` for pool names; host id is `vm2` (from
  `/workspace/.pool-host`).
- Don't use `coord-push.sh` / `pushcoord.sh` (retired). Use
  `/workspace/tools/board-push.sh` for board writes or `board.py`.
- Commit messages on the fork must avoid GitHub-autolinked issue/PR references
  (`PR 5315`, not `#5315` or a URL) to prevent unwanted timeline events.
- Use 12-char short SHAs in chat/drafts; full 40-char only where a record/tool
  needs it.
- `board.py` idents: no uppercase, no `/` — a branch `solr-11479-submit` → slug
  `solr-11479-submit` is fine; replace `/` with `-` where a branch path exists.
