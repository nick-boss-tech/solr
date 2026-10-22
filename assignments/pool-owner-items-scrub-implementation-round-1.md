# Pool assignment: implement the decided review-round items, round 1

Status: OPEN
Priority: TOP

- Posted by: vm5, 2026-10-22.
- Source of authority: `reports/owner-items-scrub-2026-10-22.md`. Every row there is decided. No row is for the owner. If a host finds a row that cannot be carried out, it records the evidence in the claim and settles the choice again on the ladder (rung 4). It does not send the row back to the owner unless it is a public action.
- Intended host: any pool Linux host. The SOLR-18552 9.x row needs the vm1 launcher and bats leg (`gates/SOLR-18552-launcher-bats.md`).
- Claim: one ticket at a time, `claims/pool-owner-items-<KEY>.md`, before any change. First post wins (WORKFLOW.md).

## Items

Each item is a separate branch or commit on its fork branch, gated and receipted the usual way. Each receipt names the rung used.

1. **SOLR-18552, 9.x line** (branch `solr-18552-sharedarena-9x`). Add `set "SCRIPT_SOLR_OPTS=-Dorg.apache.lucene.store.MMapDirectory.sharedArenaMaxPermits=64"` to `solr/bin/solr.cmd`, matching the main line. Add the changelog entry from main, `changelog/unreleased/SOLR-18552-mmap-arena-permits.yml`, adjusted to 9.x. Gate: the launcher leg runs on vm1; this host runs the changelog and compile steps.

2. **SOLR-11650** (branch `solr-11650-submit`). Fix `URLUtil.USER_INFO` so a raw `/`, `?` or `#` in a password does not end the userinfo early. The userinfo ends at the last `@` before the first `/`, `?` or `#` after `scheme://`. Add a test with `/` and `?` in the password that checks the redacted URL in `redactUserInfo` and in the `IndexFetcher` messages (`IndexFetcherLeaderUrlRedactionTest`). Show the test fails on the base (rung 2).

3. **SOLR-14200** (branch `solr-14200-submit`). In `SignificantTermsStream` (around line 285) and `DeepRandomStream` (around line 280), throw `IllegalArgumentException` with the collection name and the missing manual shards entry, instead of an empty result or an `ArithmeticException`. Add one test per stream. Show each fails on the base.

4. **SOLR-8767** (branch `solr-8767-submit`). Add an `fl=*` test for a stored copyField target and a multivalued feedback test. Each must fail on a mutation that removes the target from the output, or be labelled a pin (rung 2). No change to the output behavior.

5. **SOLR-12718** (branch `solr-12718-streamcontext-cache`, the decided branch). Add a test that calls the new `StreamContext(SolrClientCache)` constructor and checks the cache is used. The table branch `solr-12718` is not changed and not deleted.

6. **SOLR-18458** (branch `solr-18458-submit`). Narrow the `ReRankQParserPlugin` fallback so that `debug=results` no longer counts; only `debug=all` and `debug=true` do, as before the widening. Add a test that `debug=results` does not enable the debug path.

7. **SOLR-16914** (branch as recorded in `reports/newdev-critblock-review-round-1/solr-16914-submit.md`). Run the reporter's configuration (synonyms with the default mode, no mode switch) at the head as a probe. If it fails, add the fix and a test that fails on the base. If it passes, the receipt states that the docs are the resolution and the ticket says so.

8. **SOLR-18297** (branch as recorded in `reports/newdev-critblock-review-round-1/solr-18297-jetty-12-1-14.md`). Remove `pr-draft/SOLR-18297.md` from the tree. Check the build files for a lockfile gap from this branch; fix it only if one exists.

9. **SOLR-18010** (narrow version, Nick's decision 2026-10-22, `reports/newdev-critblock-review-round-1/solr-18010-narrow-decision.md`). Keep the marker removal and serialize the content before the file is opened, both in place. Drop the temp file, the move, the permission copy and the owner check. Test the in-place write and the marker removal. Keep the `#5065` lock on main.

10. **SOLR-17297** (branch `solr-17297-submit`). No code change. Write the residual as a follow-up assignment under `assignments/`, with the round-35 probe log path (`g17297r35-probe.log`) and the loader-design options. This is an internal record, not a JIRA post.

## Not in this round

- SOLR-11310 forceElevation=true test (minor, accepted at rung 3 in pass 1).
- SOLR-15326 cache for the unique-key round trip (accepted at rung 4).
- SOLR-16155 PR (no PR; upstream #1151 covers the ticket).
- SOLR-12743 branch deletion (no action).

## Tracker

Verified 2026-10-22 against the fork heads and the coordination tip `717879a360`. A row is DONE only when the branch code matches the decision and a gate record names that head. Update a row in the same commit that changes its branch or its gate.

| # | Item | Branch head checked | Code matches decision | Gate at that head | State |
| --- | --- | --- | --- | --- | --- |
| 1 | SOLR-18552 9.x: solr.cmd default and changelog | `cd7cc695fb` | No: bin/solr only, no solr.cmd line, no changelog | Passes 1 to 3 at `cd7cc695fb`, for the bin/solr-only state | NOT DONE |
| 2 | SOLR-11650: redact raw `/` `?` `#` in password | `e4f5e941cd` | No: regex still `[^/?]*`, so a raw `/` or `?` passes unredacted. The branch fails closed instead, which is not the decided behavior | Review pass 1 (handback) at an earlier head | NOT DONE (mismatch) |
| 3 | SOLR-14200: IAE in SignificantTermsStream and DeepRandomStream | `10991af9bd` | No: neither stream is changed. The branch changes TupleStream, ParallelStream and SqlStream instead | Review pass 1 (handback) | NOT DONE (mismatch) |
| 4 | SOLR-8767: fl=* test and multivalued test | `3b5f2d2357` | No: four copyField tests added with explicit fl lists. No fl=* case, no multivalued case | Passes 1 to 3 at `3b5f2d2357`, for the state without the tests | NOT DONE |
| 5 | SOLR-12718: test on decided branch | `a0c4f926e4` | Yes: `StreamContextTest` asserts the cache from `new SolrClientCache()` | No gate record names `a0c4f926e4` | IMPLEMENTED, NOT GATED |
| 6 | SOLR-18458: narrow debug=results | `344485485f` | No: `ReRankQParserPlugin` still accepts `RESULTS` | Passes 1 to 3 at `344485485f` | NOT DONE |
| 7 | SOLR-16914: reporter config probe | `cd878023d3` | No probe. Only docs commits and a content check | Receipt only; no probe run | NOT DONE |
| 8 | SOLR-18297: remove pr-draft from tree | `7c07821070` | No: `pr-draft/SOLR-18297.md` still in the tree. Lockfile check not recorded | Pass 1 at `7c07821070` | NOT DONE |
| 9 | SOLR-18010 narrow | `e81e5e28e9` | Yes for the write path: `persistConf` serializes, then `Files.newOutputStream`; no temp file, move, permission copy or owner check | No gate record names `e81e5e28e9` | IMPLEMENTED, NOT GATED. Marker removal and tests not checked |
| 10 | SOLR-17297 follow-up assignment | `c0ab38fc0a` | No follow-up file under `assignments/` | n/a | NOT DONE |
| 11 | SOLR-12743: no branch deletion | `1bb4b227df` | Yes: branch untouched by this round | n/a | DONE (no action) |
| 12 | SOLR-18552 9.x changelog | `cd7cc695fb` | No changelog file on the 9.x line | n/a | NOT DONE (part of row 1) |

Row 12 is part of row 1 and is listed separately only so its changelog gap is visible.

## Done when

Each item has its claim marked DONE, a receipt naming the rung, and a gate file at the implementation head. The vm5 status file and `OPEN-WORK.md` regenerate on the next `pool/bin/open-work.py` run.
