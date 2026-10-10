# Streaming expressions round 1, part S5: SOLR-11922, SOLR-14200 and SOLR-15326

No builds, tests, `gh` writes, commits, pushes, posts, or file edits.

## Heads and base

- `ls-remote` matches the receipts: `solr-11922-submit` `da29a963236f`, `solr-14200-submit` `10991af9bdac`, `solr-15326-submit` `dab6a466316`. The `origin/` refs were used. The local branches with the same names point elsewhere (`79559827`, `bcc803ac`) and were not used.
- `upstream/main` is `8e62c2686882`. The merge-base with `upstream/main` is `14c7aac0d151` for all three branches.
- The run logs (`g11922-premise.log`, `g14200-settle.log`, `g15326-settle.log`) are not in the worktree, `research/`, the workspace root, or anywhere under `C:/Users/shaninna/dev`. The settling claims rest on the receipts plus code reading.

## SOLR-11922: test-only branch, NO GATE by finding

**Finding, verified on main.**
- The ticket's NPE is the unguarded `orderBy.toExpression(factory)` call at `CartesianProductStream.java` line 154 in the `releases/lucene-solr/6.6.2` tag. It is reached when the expression has no `productSort`. The ticket's trace matches that line.
- On main the call is guarded: `CartesianProductStream.java` lines 197 to 199 (`toExpression`) and lines 220 to 222 (`toExplanation`). The guard is commit `463907a13c4` (SOLR-10855, Joel Bernstein, 2017-06-08). It is an ancestor of main, absent from the 6.6.1 tag, and the 6.6.2 file still has the unguarded line. The fix missed the 6.6 line, so the 2018 NPE is this defect. The commit added no test.
- The branch's expression reaches the guarded path on main: `ParallelStream.constructStreams` calls the inner stream's `toExpression` at `ParallelStream.java` line 290. This fits the premise run passing.
- No existing main test puts a `cartesianProduct` under parallel or calls `toExpression` on a cartesian stream. `MathExpressionTest` uses `cartesianProduct` at lines 102, 151 and 186, without parallel and without `productSort`, through `/stream` only.
- Adjacent, not this ticket: `ParallelStream.java` line 71 (the SolrJ constructor taking an expression string) dereferences `streamFactory`, which that path never sets (field at line 52, set only at lines 75 to 77 and 168). A separate NPE, not checked further.

**Options.**
- (a) Retire the branch. Report on SOLR-11922 that the NPE was fixed on master by SOLR-10855 and does not reproduce. Cost: nothing on main pins the guard.
- (b) Keep the test as regression coverage, narrowed. Test only, no fix. First remove `SOLR-11922-TESTING.md`: it says nothing was run and there is no fix, both stale, and it must not reach a PR. The title and Proof must say this is coverage of the guard, that it passes at the head, and that it cannot fail on base because base already has the guard. Cite SOLR-10855 either way.

**Recommendation: (b), narrowed.** The test is the only thing on main that would catch the guard being removed. It is one method, and it changes no production code. Retiring leaves the guard unpinned. Closing the ticket as fixed by SOLR-10855 is needed either way.

Ticket (JSON): Open, version 6.6.2, no comments.

## SOLR-14200: GATE GREEN, retire candidate

**Finding, verified on main.**
- `shards.tolerant` is a server-side distributed-search concept (`SearchHandler.java` line 336, `HttpShardHandler.java` line 142). `solrj-streaming` on main never reads it.
- Replica path (no manual map): `TupleStream.getReplicas`, lines 189 to 199. Each active slice contributes its first active replica on a live node (filter at line 191, `ifPresent` at line 197). A slice with no active replica adds nothing and raises no error. The replica-less slice is therefore dropped silently, whatever `shards.tolerant` says. The settling run tested only `tolerant=true` (receipt), so the `tolerant=false` case rests on this reading. `CloudSolrStream` fails only when every slice is gone ("No replicas available", `CloudSolrStream.java` lines 438 to 439).
- The ticket's `IndexOutOfBounds` (`get(0)` in `getShards`) does not exist on main. `getShards` (`TupleStream.java` lines 208 to 234) has no index access.
- Manual path (the streamContext "shards" map, filled by `StreamHandler.java` line 230 from request params `<collection>.shards=`): `TupleStream.java` line 221 returns null for a collection absent from the map. `CloudSolrStream.java` line 422 calls `shards.isEmpty()` on it, and the NPE is wrapped at lines 459 to 460. This residual is real and reachable through `/stream`. `TupleStream.java` line 224 also `removeIf`-mutates the caller's list when a core filter is set.
- `ParallelStream.java` line 302 does `shardUrls.get(w)` per worker. With one slice dropped and workers equal to the slice count, it throws IndexOutOfBounds, wrapped at lines 315 to 316. So parallel fails with a raw error rather than skipping silently, and the branch does not change that. `SqlStream.java` lines 194 to 196 pick one entry node (shuffle, then `get(0)`). All-gone gives IndexOutOfBounds on main. The receipt states this correctly.

**Options.**
- (a) Retire. Report that the `shards.tolerant` scenario does not reproduce: the replica-less slice is skipped silently at `TupleStream.java` lines 189 to 199 on main and on the branch alike.
- (b) Keep, re-scoped. The real changes fix the manual-map NPE (`CloudSolrStream.java` line 422 via `TupleStream.java` line 221), the caller-list mutation (`TupleStream.java` line 224), and give a message in the ParallelStream and SqlStream all-gone cases. The changelog title matches this code. The tests exercise the manual map and fail on base. The title and ticket text would drop the `shards.tolerant` crash. Wording issue if kept: `CloudSolrStream.java` line 423 says "No shards available from ZooKeeper" for a manual map.
- (c) Keep with the current title: not recommended, since the ticket describes a crash main no longer has.

**Recommendation: (a).** The crash the ticket reports is gone. What the branch still changes is hardening for a manual map that only `/stream` callers sending `<collection>.shards=` can reach. Keeping it means a new title and a new story, which the owner should choose. The residual that matters is the silent skip, which the branch leaves untouched.

Ticket (JSON): Patch Available, version 7.4. The attached patch was reported as not applying to master (QA comment, 2020-01-22).

## SOLR-15326: GATE GREEN, retire candidate

**Finding, verified on main.**
- Premise: a missing record in `gatherNodes` over `search(qt="/export", sort="id_l desc")`, version 8.4.1. Christine Poerschke asked for steps on 2021-04-09. No reproduction is recorded (one comment, status Open).
- No tie collapse on main: `TupleWrapper.compareTo` returns 1 on equal keys (`CloudSolrStream.java` lines 537 to 541), and the merge set is a `TreeSet` (line 315). The loss a missing-record story needs is absent. This fits the settling run.
- The one loss path found on main is the replica path: `getReplicas` (`TupleStream.java` lines 189 to 199) drops replica-less and inactive slices silently, and `CloudSolrStream.constructStreams` uses it (lines 436 to 442). The receipt describes only healthy-cluster shapes, so this path was not exercised.
- Branch, read against main:
  1. The condition is `params.get(CommonParams.QT)` only (branch `CloudSolrStream.java` line 600). `path="/export"` is held in the `path` field and excluded from `params` (`CloudSolrStream.java` lines 156 and 177 to 180), so that spelling gets no tie-breaker. The SOLR-11922 test uses `path="/export"`.
  2. The unique-key `SchemaRequest` (`getUniqueKeyField`, branch lines 622 to 642) runs before the already-contains-key check (branch lines 610 to 611). Every `qt=/export` open with a sort adds a round trip.
  3. Docvalues dependency: `ExportWriter.getSortDoc` throws "<field> must have DocValues to use this feature" (`ExportWriter.java` lines 633 to 635, called from `ExportBuffers.java` line 97). An appended unique key without docValues fails the whole export. `StrField` gets docValues by default only at schema version 1.7 or later (`FieldType.java` line 191, `PrimitiveFieldType.java` line 42; `StrField` extends it). The gate's configsets are version 1.7 (streaming `schema.xml` line 28, `_default` managed-schema line 41), so the gate cannot see this. This is conditional on legacy schemas and was not run.
  4. The merge-side comparator append in `ensureExportSortUsesUniqueKey` does nothing unless the unique key is in `fl`, because two null values compare equal (`FieldComparator.java` lines 119 to 121).
- The proof asserts only that the stream sort text contains "id asc". It checks no tuple count or order.

**Options.**
- (a) Retire. Report that the missing record does not reproduce on main in the shapes tested, and ask the reporter for steps (the owner's public act).
- (b) Keep, re-scoped as an ordering change, after: handling `path="/export"`, moving the key lookup behind the contains check, a docValues guard, an fl-aware merge comparator, and a test that reads tuples and checks order. The title must say tie order is now deterministic and must not say records come back. The change alters the export sort string for every `qt=/export` stream.
- (c) Keep as is: not recommended.

**Recommendation: (a).** The change does not address a demonstrated loss, the one loss path on main is untouched, and the branch has four issues (the `qt`-only condition, the extra request, the docValues dependency, and a merge comparator that does nothing without the key in `fl`). A keep would need all four fixed first. A deterministic-order change, if wanted, is a new change with its own proof.

Ticket (JSON): Open, version 8.4.1, one comment asking for steps.

## Cross-ticket

- **Same code.** `getReplicas` and `getShards` (`TupleStream.java` lines 189 to 234) are the common path. The 11922 test runs `ParallelStream.constructStreams` (`getShards` at line 292, `get(w)` at line 302). The 14200 silent skip is in `getReplicas`. The 15326 `CloudSolrStream` open uses the same replica path (lines 436 to 442). All three retire calls depend on how `getReplicas` and the merge behave on main.
- **Settling runs.** No direct conflict. The 15326 run shows complete records on base in healthy shapes. The 14200 finding is that dead slices vanish silently. Together they make the silent skip the only record-loss mechanism identified on main, and neither run exercised it. So the 15326 retire rests on healthy-cluster evidence, and the 14200 retire rests on code plus a degraded-cluster run. The 15326 ticket wording should say "does not reproduce on a healthy cluster", not "is not a bug".
- **Shared suite.** 14200 and 15326 both add tests to `StreamingTest.java` at the end of the same method (the same three context lines), so whichever lands second conflicts textually and needs a rebase. 11922 is in `StreamDecoratorTest.java` (around line 5482). Its neighbour is 12505, which part S6 orders.

## Receipt check

- **11922:** "the 2018 NullPointerException ... does not reproduce on current main, matching the handoff doc's own first guess." Agree. Not in the receipt: the mechanism is the unguarded call fixed by SOLR-10855 (`CartesianProductStream.java` lines 197 to 199).
- **11922:** the receipt's disposition reading (the handoff doc's own recommendation) agrees. But `SOLR-11922-TESTING.md` says "nothing on this branch was compiled or run, and there is no fix" and asks for a verify run. That is stale after the premise run and must not ship. The receipt does not flag it.
- **14200:** "the branch behaves identically there." Agree for the replica-less slice. The branch's `TupleStream` change is in the manual path only (lines 222 to 226). The SqlStream residual is accurate (`SqlStream.java` line 196). Missing from the receipt: ParallelStream's all-gone case is also a raw IndexOutOfBounds on main (`ParallelStream.java` line 302), and so is the partial-gone parallel case, which the branch does not change.
- **14200:** "a NullPointerException on the null shard list (the direct getShards test and the CloudSolrStream message test)." Agree (`TupleStream.java` line 221, `CloudSolrStream.java` line 422).
- **15326:** "CloudSolrStream makes the export sort use the unique key as a stable tie-breaker." Agree in substance. It omits the `qt`-only condition, the extra request on every open, the docValues dependency, and the fl-dependent merge comparator. These belong in the receipt before any keep.

## Owner decisions

1. SOLR-11922: keep the test as narrowed coverage (recommended), or retire and report fixed by SOLR-10855.
2. SOLR-11922: whether to close the ticket as fixed by SOLR-10855 (a public act).
3. SOLR-14200: retire (recommended), or keep re-scoped to the manual-map NPE under a new title.
4. SOLR-15326: retire (recommended), or keep re-scoped as an ordering change after the four fixes.
5. SOLR-15326: whether to ask the reporter for steps, given the 2021 request has no answer recorded (a public act).
6. Whether to raise the silent skip of replica-less and inactive slices (`TupleStream.java` lines 189 to 199) as its own question. Neither branch changes it, and it is the one record-loss path found on main.

## Not checked

- The three run logs were not found anywhere reachable. The settling claims are receipts plus code reading.
- No runs. The docValues failure, the path-spelling gap, the `tolerant=false` silent skip and the merge comparator behavior are code readings, not executed.
- SqlStream's own distributed query on the `/sql` node was not traced, so its data-level behavior with a dead slice is open.
- SolrJ schema-request routing for comma-separated collection names was not traced end to end. `CloudSolrClient` splits at line 604, which suggests routing but does not confirm it.
- Legacy configsets (below schema 1.7) in use were not checked. The docValues risk is conditional on them.
- The 6.6.2 reference is the `releases/lucene-solr/6.6.2` tag, not release artifacts. The SOLR-10855 backport history was read from tags and ancestry only.
- 12505's `StreamDecoratorTest` edits were not read (part S6).
