# Handoff: 9.x distribution build needs an npm host (JS client step)

Status: HANDED. Phone agent, or any host that can run npm. Not claimable by Linux build hosts without npm access on this host.

Posted by vm2, 2026-10-20, at the direction of Nick.

## Problem

`:solr:packaging:assemble` on the 9.x line cannot run on vm2's host. It fails in `:solr:webapp:jsClientDownloadDeps`, which runs npm under `solr/webapp/js-client`:

    Error: Cannot find package '@babel/plugin-syntax-dynamic-import' imported from .../jsClientWorkspace/babel-virtual-resolve-base.js

The package is not declared anywhere in the repo at `branch_9` (upstream f4f25071778) or at the fork's pinned main head (498a09aea97), so the step only works when npm happens to install it transitively. Main avoids the step with `-PdisableJsClient=true`. `branch_9` has no such switch.

Core tests on 9.x run fine on vm2 (SOLR-18522 gate, `:solr:core:test`, 7 of 7). Only distribution assembly is blocked.

## Requested

1. Reproduce on a host with npm: `./gradlew :solr:packaging:assemble -x test` on branch_9. Record whether the error appears there too.
2. Decide the fix (rung 4, document tradeoffs): (a) declare `@babel/plugin-syntax-dynamic-import` in the js-client package.json and update its lock; or (b) add a `disableJsClient` switch on branch_9 as main has. Option (a) is the root fix; option (b) is the smaller change that matches main.
3. Record the result in `reports/9x-js-client-npm.md`.

## Blocked work waiting on this

- `solr-18552-sharedarena-9x` (SOLR-18552, bin/solr default): the launcher gate ran on an 11.0 distribution copy. The 9.x gate needs a 9.x distribution. Head 088cd840c06 on the fork, based on branch_9 f4f25071778.
- The Jetty bump `solr-18297-jetty-12-1-14` is on main, not 9.x, so it is not blocked by this.

## Limits

No PRs, no comments, no force push. Commit as Nick Shanin with no trailers.
