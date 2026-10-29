# Report: SOLR-18178 rebased configset upload gate, round 1, blocked on vm4

- Claim: `claims/solr-18178-rebased-head-round-1-vm4.md`. Gate job: `gates/solr-18178-rebased-head-round-1.md`.
- Head: `d6abc36c94687dc5744a7573bafb1ce473acc75f`, fork `nick-boss-tech/solr:solr-18178-verify`. Verified by ls-remote before the claim.
- Verdict: BLOCKED. Not GREEN. No receipt is written for this head.
- Logs: `/workspace/gates/logs/vm4/solr-18178-rebased-head-round-1/`.

## Passing at the head (fresh XML)

- Tidy and clean tree, Error Prone compile, diff check, final clean tree: all rc 0.
- Focused classes at seed `4968D15EA5E2`: `TestFileSystemConfigSetService` 3/3, `DownloadConfigSetAPITest` 4/4, `UploadConfigSetAPITest` 20/20, `TestSchemaDesignerConfigSetHelper` 8/8. No failures, errors or skips.
- `UploadConfigSetAPITest` at seeds `4968D15EA5E3` and `7777777777771007`: 20/20 each, no failures, errors or skips.

## Blocked: `check -x test`

- `:solr:solr-ref-guide:buildLocalAntoraSite` fails. Two environment causes on vm4, neither in the branch:
  1. `NODE_ENV=production` is set by the login shell. npm skips devDependencies, so `@asciidoctor/tabs` is never installed. Ticket SOLR-18593 documents the same npm behavior for the js-client build. Its fix (`--include=dev`) covers the js-client task only; the ref-guide npm tasks in `solr/solr-ref-guide/build.gradle` need the same treatment.
  2. Antora refuses a git worktree as its content source. The gate ran from a worktree, and the first run failed on the tabs module. After a manual install of the tabs package, the run failed with `Local content source must be a git repository`.
- Repair attempts logged: `check-repair-nodeenv-development.log` (NODE_ENV=development, tabs missing), `check-repair2-nodeenv-development.log` (tabs installed by hand, worktree source rejected).
- A shared clone of `/workspace/solr` failed because that repo is a shallow partial clone.

## Not run

- Fail-before leg on the base `35e2be2a7de9855a0f909613065ba22243097102`. It needs a checkout that Antora accepts.

## To finish

- Run `check -x test` from a full clone on a host with `NODE_ENV` unset or with the SOLR-18593 fix in place for the ref-guide npm tasks. Then run the fail-before leg and write the receipt.
