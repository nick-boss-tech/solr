# solr-15478-submit

- Branch: origin/solr-15478-submit
- Head: 0478bdf0ac5c (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base b5c71bc5573c, 46 commits behind, 5 commits ahead)
- Scope: 5 commits, 3 files. `ZkConfigSetService.java` (`getCurrentSchemaModificationVersion` now returns `stat.getMzxid()` instead of `(long) stat.getVersion()`, `:114-117`), `ZkConfigSetServiceModificationVersionTest.java` (new, 86 lines, ZooKeeper test server), changelog `SOLR-15478.yml` (`type: fixed`). No `SOLR-15478-TESTING.md` on the tip.
- Verdict: Not ready on its own. The change is correct for the `ConfigSetService` cache it feeds, but the reported symptom also needs the second cache that SOLR-15674 fixes. The two must ship together.
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head.

## Priority: SOLR-15674 P1

The handoff says this fix closes the open P1 on the gated SOLR-15674 branch. Verified:

- The 15674 P1 (`research/branch-reviews/round-28/SOLR-15674-review.md`, finding 1) is that the shared `ConfigSetService.schemaCache` can return a deleted configset's schema. It cites `ConfigSetService.java:259,304-317`.
- At this tip, the `schemaCache` key is built from `getCurrentSchemaModificationVersion(...)` (`ConfigSetService.java:306-317`). This branch changes that value to `mzxid`, which changes on re-creation, so a re-created configset gets a new key. That closes the 15674 P1 on the ZooKeeper path.
- The two branches must ship together. Verified in the other direction too: `IndexSchemaFactory.java:182` on this tip still compares `result.version == res.second()` (data version), so this branch alone leaves the second, version-only cache in place (see finding 1). 15674 alone leaves its P1 open, because the `ConfigSetService` key is still version-based on that branch.

## Delta check against the bulk review

- The only bulk file for this ticket is `research/branch-reviews/round-2/SOLR-15478-review.md`. It was written at head `6e2c5426bf7d`, not at the listed head `0478bdf0ac5c`. The branch has moved since: the round-2 patch (`6c828249bca`) and its comment follow-up are not the current commits. The handoff's round-36 bulk verdict "Not ready" has no file in this workspace; the round-2 verdict was "Needs work". This review uses the handoff verdict and checks the round-2 findings against the current tip.
- Round-2 finding 1 (HIGH, second version-only cache still serves the old schema): **confirmed still present at this tip.** See finding 1.
- Round-2 finding 2 (`mzxid` as the single primitive): **agreed** (design point, not a defect). The same primitive is used by 15674.
- Round-2 finding 3 (the cache is weak-referenced, so the test must force that): **still open.** See finding 3.
- Round-2 finding 4 (`FileSystemConfigSetService` implements the same abstract method): **not checked** at this tip. See Not checked.
- Round-2 finding 5 (no test, no changelog): **partly addressed.** A unit test and a changelog now exist. The integration repro from the handoff is still missing. See finding 3.

## Findings (ranked)

1. **HIGH, verified by reading (carried from round 2, still present). The second, version-only cache is untouched.** `IndexSchemaFactory.loadConfig` takes the version from the stat's data version (`IndexSchemaFactory.java:145`), and `getFromCache` treats an entry as fresh when `result.version == res.second()` (`:182`). A deleted and re-created znode starts again at data version 0, so a stale parsed schema can still match on a node that holds the old entry. This branch does not change `IndexSchemaFactory`; SOLR-15674 does. So this is the part of the symptom that this branch cannot fix alone. Ship it with 15674.

2. **MEDIUM, verified in the test file. The new test does not cover the reported path.** `ZkConfigSetServiceModificationVersionTest` checks only `ZkConfigSetService.getCurrentSchemaModificationVersion`: a stable value for an unchanged file, a different value after delete and re-upload while the data version resets, a different value after an in-place change, and null for a missing configset (diff lines 84-112). It does not test `ConfigSetService.schemaCache` (`:306-317`) or `IndexSchemaFactory` (`:182`). The handoff's integration repro (create configset and collection, downconfig, edit, delete, upconfig, recreate, assert `/schema/fields`) is not in the branch.

3. **MEDIUM, verified by reading. The schema cache is weak-referenced, so a test must keep a core reference.** Round-2 finding 3 said `schemaCache` uses `weakValues()` (`ConfigSetService.java:269` in round 2's snapshot; not re-checked at this tip). A test that does not keep an `IndexSchema` or core alive can pass without the fix. The new unit test does not touch the cache, so this is open.

4. **LOW, hypothesis. A re-upload with identical content may also move `mzxid`.** `mzxid` moves on every `setData`. If the configset upload writes the node even when the content is unchanged, the schema is reloaded unnecessarily. That costs a reload, not a wrong answer. Not checked.

## Owner calls (not decided here)

1. **Ship as one PR or two?** Round 2 suggested one PR with one primitive in both caches. The priority note above says both must land for the symptom to be fixed. Pose it: is the owner's plan a single combined change (15478 + 15674), or two changes that land together? The choice is the owner's.

## Proposed fixes (not applied; the owner decides)

- Finding 1: ship with SOLR-15674, which changes `IndexSchemaFactory` to `mzxid`.
- Finding 2: add the integration repro from the handoff (recreate, then assert `/schema/fields`), and a `ConfigSetService.schemaCache` test that keeps a core reference.
- Finding 3: the same test, with a live reference, so the weak cache is actually exercised.

## Interactions with other branches

- SOLR-15674 (gated, P1 open on its own): this branch closes its ConfigSetService P1, and 15674 closes the `IndexSchemaFactory` cache this branch leaves. The two are a pair. See the priority section.

## Not checked

- Not compiled, formatted, or run. No Gradle, no `drain`, no test runs.
- `FileSystemConfigSetService` (round-2 finding 4): not checked at this tip.
- `ConfigSetService.java:269` (the `weakValues()` line cited by round 2): not re-read at this tip.
- The SOLR-15674 branch's own code and test were not read in this review; its P1 and its cache change are taken from the round-28 15674 review and the grep of `IndexSchemaFactory` at this tip.
- The integration repro: not written and not run.
- No GitHub or JIRA writes were made.
