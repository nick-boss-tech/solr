# PR 5204 js-client npm failure: cause and correction (vm4)

Status: CORRECTION. The failure is caused by `NODE_ENV=production` in the vm4 shell. It is not a defect at the PR 5204 head. Earlier vm4 records called it a pre-existing defect; those statements are wrong.

Checked: 2026-10-26T03:18:10Z (UTC), at PR 5204 reviewed head `7eb288a6f5777770d4472fc2c550b66e0d1cec12` (detached worktree, `/workspace/worktrees/js-probe`, removed after the run).

## Cause

- openapi-generator's `javascript` package template (6.6.0, `Javascript/libraries/javascript/package.mustache`) lists every babel package under `devDependencies`, including `@babel/plugin-syntax-dynamic-import`. The generated package's `prepare` script runs `babel src -d dist`.
- npm omits `devDependencies` when `NODE_ENV=production`. The babel plugin is then missing, and babel fails with `Cannot find package '@babel/plugin-syntax-dynamic-import'`.
- The plugin is not declared anywhere in the repository at that head. That is expected for a generated package.

## Test

Task `:solr:webapp:js-client:jsClientDownloadDeps` at the same head, one variable changed:

| Leg | Environment | Result | Log |
| --- | --- | --- | --- |
| A | `NODE_ENV=production` | FAILED, rc=1, `Cannot find package '@babel/plugin-syntax-dynamic-import'` | `evidence/legA-nodeenv-production.log` |
| B (control) | `NODE_ENV` unset | BUILD SUCCESSFUL, rc=0; `@babel/plugin-syntax-dynamic-import` installed | `evidence/legB-nodeenv-unset.log` |

Leg B cleared `jsClientWorkspace` before running, so it did not reuse leg A's partial install. Both legs ran under `/workspace/tools/.gradle-vm4.lock` through `pool/bin/sweep-run.sh`. Times: A 03:07:58Z to 03:13:34Z; B 03:13:50Z to 03:16:01Z.

## Where NODE_ENV comes from

The vm4 tool shell reports `NODE_ENV=production`. It is not set in `/etc/profile`, `/etc/environment`, `~/.bashrc`, `~/.profile`, `/workspace/tools/vm4-env.sh`, or the shell snapshot I searched. Its origin is not found. The gate preflight script unsets it, but manual Gradle runs do not.

## Records that need correction (add-only, not edited here)

- `receipts/SOLR-18566.md`, Limits paragraph: says the defect is pre-existing and separate. Correct: caused by the shell's `NODE_ENV`.
- `reports/pr-5204-cli-review-implementation-vm4-round-1.md`, item 1: "the package is not declared anywhere" is true, but the defect attribution is wrong. The cause is `NODE_ENV`.
- `reports/pr-5204-review-comment-draft-vm4-round-1.md`, Limits: the draft says the js-client failure is separate. Not posted. If the draft is revisited, drop or correct that sentence.
- `reports/pr-5221-license-headers-gate-round-1.md`, the "possible related observation": now verified. Same cause.

## What this does not change

- The PR 5204 test results. The js-client build is not in the BATS or Gradle test path measured there, and `-PdisableJsClient=true` only skipped that task.
- The PR 5204 gate was run with `-PdisableJsClient=true`. So js-client was not exercised at that head in the gate. A clean run with `NODE_ENV` unset would close that gap; not run here.
- The PR 5221 receipt, and its environment notes on `NODE_ENV` and `@asciidoctor/tabs`. Those are unchanged.

## Suggested host fix (not applied)

Add `unset NODE_ENV` to the vm4 shell setup, or to `/workspace/tools/vm4-env.sh`, once the origin of the variable is known. Not applied here, since the source of the variable is unknown and it affects every shell on this host.
