# Receipt: PR 5238 client search dispatch (vm2, round 1)

Status: GREEN at F1 `ede5646e2b2281bf4fc1483c2867f0f047088527`. Fork tip at receipt time: `ede5646e2b2281bf4fc1483c2867f0f047088527` (unchanged).

- Gating host: vm2. Job: `gates/pr-5238-client-dispatch-round-1.md`. Claim: `claims/pr-5238-client-dispatch-vm2-round-1.md` (board 1ffb2173f84).
- Fork: nick-boss-tech/solr, `solr-11994-pr5238-submit`. Negative base P: apache/solr PR 5238 head `cc9ea9b1b6b153317217ee458ff6287b5e24cc65`.
- Shape verified before gating: `P..F1` is one commit (Nick Shanin, no trailers), seven files, 132 insertions, 8 deletions.
- Toolchain: JAVA_HOME `/workspace/jdk/jdk-21.0.12.1+1`, GRADLE_USER_HOME `/workspace/gradle-home`, `--no-daemon`. Build lock `/workspace/tools/.gradle-vm2.lock` on every gradle run. NODE_ENV unset.
- Seed: `5238C0DE` for focused, neighbors and negative legs.

## Results

1. Tidy at F1: rc 0, tree clean. No repair needed. Log `f1-tidy.log`.
2. Focused `SearchRequestListenerTest` at F1: 20 tests, 0 failures, 0 errors, 0 skipped. All 20 methods executed, including `testCpuTime` (2.371 s). Logs `f1-focused.log`, XML `f1-focused-xml/`.
3. Neighbors at F1: `TestSolrConfigHandler` 8, `PingRequestHandlerTest` 6, `TestSolrRequestInfo` 3, `TestSubQueryTransformerDistrib` 1. Total 18 tests, 0 failures, 0 errors, 0 skipped. Logs `f1-neighbors.log`, XML `f1-neighbors-xml/`.
4. `check -x test` at F1 in a clean full clone (`/workspace/worktrees/pr5235-r3-Fclone`, checked out detached at F1): rc 0, tree clean, BUILD SUCCESSFUL in 12m 22s. Antora site build ran. Log `root-check-f1.log`.
5. Negative proof on P: a clean detached checkout of `cc9ea9b1b6b` with only the three test files from F1 overlaid. Hashes verified against F1 (see below). Production files equal P. Result: 3 tests run, 3 failed, 0 errors. Logs `negative-p.log`, XML `negative-p-xml/`.

## Negative proof detail

Overlay hashes (sha256, first 16 hex chars), verified byte-for-byte against F1:

- `SearchRequestListenerTest.java`: a2ff815b33afd0a4
- `conf/schema.xml`: 94ba06024bc0c8ab
- `conf/solrconfig.xml`: 27aae3a0fcc0440e

The `search-request-listener-routing` configset directory did not exist on P, so the two configset files are new files in the overlay. Only the three listed paths changed on the parent.

| Method | Failure | Line | Preceding assertions that passed |
| --- | --- | --- | --- |
| `testV2OnlySearchReported` | `expected:<1> but was:<0>` | 298 | v1 `/v2-only-select` lookup null; v2 response nonempty |
| `testEmbeddedQtFallbackReported` | `expected:<1> but was:<0>` | 323 (`assertEmbeddedFallbackReported`) | `/select` absent; embedded response nonempty |
| `testEmbeddedDefaultFallbackReported` | `expected:<1> but was:<0>` | 323 (`assertEmbeddedFallbackReported`) | `/select` absent; embedded response nonempty |

Each failure is the single-event callback count, as the job requires. There were no compile, setup, route, skip, or missing-method failures.

## Repairs and execution conditions

- No tidy repair. No superseding head. No fork push.
- The first overlay attempt failed on the new configset file because the directory did not exist on P. Repaired by creating the directory and writing the same bytes from F1. Hashes were then verified. The parent worktree was created fresh and never ran before the overlay.
- The full-clone root check reused the clone `/workspace/worktrees/pr5235-r3-Fclone` from the finished PR 5235 round 3 gate. Its tree was clean; it was checked out to F1. The earlier worktree-based root check was not used.

## Remote head

Live fork tip at receipt time: `ede5646e2b2281bf4fc1483c2867f0f047088527`, one commit above P.

## Draft upstream follow-up (for owner review, not posted)

Pinned diff: apache/solr PR 5238 head `cc9ea9b1b6b` to tested head `ede5646e2b2`, seven files, 132 insertions, 8 deletions. Fix: preserve the originally selected handler under `SolrRequestHandler.class` in the request context, so listener events for v1-to-v2 and embedded fallback dispatch are reported once on the original node. Tests added for v2-only selection and embedded `qt`/default fallback; the routing configset carries `standard` and `/v2-only-select`, with no `/select`.

Fast-forward pull-in commands for a checkout of the PR branch:

```
git remote add nick https://github.com/nick-boss-tech/solr.git   # if not present
git fetch nick solr-11994-pr5238-submit
git merge --ff-only ede5646e2b2281bf4fc1483c2867f0f047088527
```

Not posted. Any upstream comment needs Nick's approval and a fresh read of the live PR head and ancestry first.
