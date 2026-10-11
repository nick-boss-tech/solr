# Draft fidelity confirmation round

Verdict: **DRIFT in seven drafts.** Most of the applied edits read correctly in place. Seven passages disagree with the record or with the cited source, and each is listed below with its exact passage and the fix. Nothing was edited; the main side applies the fixes.

Coverage: two read-only slices. Slice 1 covered every draft edited by the Group D decision-default applications (8 drafts, SOLR-8240 recorded as standing and not edited) and by the Group O settlements (15 drafts). Slice 2 covered 38 drafts across 11 of the 12 categories, including all the judgment-call drafts the record names and the three core-admin OWED deletions. Security had no draft edits in the record. Several drafts were checked by both slices. Drafts not checked are listed at the end.

Method: live text is the head of the worktree (clean; latest pr-drafts commit `1ff913db217`). Each edit was compared with its commit diff (application `1ad4d52f55b`; settlements `7f583f139d5`, `29ab095519d`, `325ca02c2f6`, `14680b1fcaf`), the application and its response record, and the receipts. Source claims were checked against the cited heads. Nothing was built, tested, gated or posted.

## Drift (seven drafts)

**1. `metrics/SOLR-17987.md` line 15: DRIFT.** "The environment variable SOLR_METRICS_DISABLED_REGISTRIES sets the same property." The response's Group D entry says the rename is recommended because "the reference guide already uses that form". At head `38abf6423126` it does not: `metrics-reporting.adoc` line 106 says `SOLR_METRICS_DISABLEDREGISTRIES`, `solr/solrj/src/resources/EnvToSyspropMappings.properties` line 39 maps `SOLR_METRICS_DISABLEDREGISTRIES`, and `solr-properties.adoc` line 170 gives the same form. The draft states as current behaviour a name the head does not read. The rename is branch work (F13), so the sentence runs ahead of the branch.
- **Fix:** restore the head name in line 15, or mark the new name as "after the branch rename". Also correct the response's premise that the guide already uses the form. Line 31 states the cost and then restates it in its last sentence; trim the repeat.

**2. `search-components/SOLR-12556.md` line 13 (bold summary): DRIFT.** "With processEmpty:true, buckets that were not refined against every shard that returned the facet are now dropped instead of returned with partial stats." The receipt (`receipts/SOLR-12556.md` line 9) records that buckets first seen during refinement keep the base behaviour, and that "the changelog title's first sentence is true only for buckets known before refinement, and the PR text should say so." The draft's own body (line 17) and Choice (lines 27 to 29) say the same. The summary applies the drop to every bucket not refined against every shard, so the first sentence a reader meets contradicts the body. The deleted NOT FOR POSTING block was the only in-draft note that the changelog title overstates the change, and no line says so now.
- **Fix:** qualify line 13 to buckets known before refinement, as the receipt says.

**3. `edismax/SOLR-4362.md` line 13: DRIFT.** "A boost on the slop is skipped too, in either order." The response (item 62, rung 1) records that `^2~10` returns false and that only `~N` and `~N^boost` (such as `~10^2`) are recognised. Line 15 and line 30 of the same draft already say `^2~10` is not covered, so "in either order" contradicts both the record and the draft. Line 30's claim that the slop number can still reach a pf2 shingle is not traced anywhere in the record.
- **Fix:** line 13 names `~N^boost` only. Line 30 is either traced to the source or removed.

**4. `build-docs/SOLR-16914.md` lines 11 and 13: DRIFT (source).** Lines 11 and 13 say the Japanese tokenizer section "does not name the option that keeps compound tokens" and that the option table "gives [discardCompoundToken] no default". At head `cd878023d3d0`, `language-analysis.adoc` line 2292, inside the Japanese Tokenizer section, already names the option (search mode with `discardCompoundToken="false"` keeps the original compound terms), and lines 2315 to 2317 give the default and the synonym interaction. The option entry (lines 2347 to 2352) reads "Optional | Default: true". The quoted phrase "as synonyms" does not occur on that page. The "What this change does" line that says the change "gives its default" would add a default the head already states.
- **Fix:** rewrite lines 11 and 13 from lines 2292, 2315 to 2317 and 2347 to 2352, and drop or source the quotation. The "Verified 2026-10-08" sentence added in `7f583f139d5` matches `receipts/SOLR-16914.md`.

**5. `cli/SOLR-17029.md` line 37 (Proof bullet): DRIFT (unsourced).** The parenthetical "(the deprecation line reaches the log a few seconds after the test checks for it)" states a mechanism. The response (F5) records only the classification, "an upstream log-timing race that also fails on pristine base at roughly the same rate", and neither the response nor the receipt (`receipts/SOLR-17029.md` line 7) records the "few seconds" detail.
- **Fix:** source it from the round 35 artifacts (main side), or drop the parenthetical and carry "at roughly the same rate", so the base claim is no stronger than the record.

**6. `security/SOLR-18368.md` line 1 (banner): DRIFT.** The banner still offers option (c), "do not open, and fold the three lines into the next docs pass". The response (F7) and `receipts/SOLR-18368.md` line 9 say that option is foreclosed by the still-an-issue verdict and the resolved-ticket rule.
- **Fix:** remove option (c). Options (a) and (b) remain, with the key question settled at opening approval. `reports/solr-18368-still-an-issue.md` is not in this worktree, so the verdict was checked only as the record states it.

**7. `search-components/SOLR-9396.md` lines 15 and 34: DRIFT.** The Limits bullet 1 says "Only a `$row.field` reference written in the subquery's own parameters is scanned." It contradicts line 35 ("The scan also matches the `${row.` spelling") and the response's rung 3, which keeps the `${row.` branch in this change. Line 15 ("What this change does") has the same gap: it names only `$row.field`. The record's replacement (`reports/draft-fidelity-searchcomponents-edismax-s10.md` lines 51 and 52) names `${row.field}` too and was not applied.
- **Fix:** name both spellings in line 15 and in the bullet at line 34, as the record's replacement does.

## Judgment calls and readiness flags (not drift)

- `search-components/SOLR-17731.md`: the response says the draft "must not be used until that work lands", but the file now has no HOLD or OWED marker (only `SOLR-8576` keeps one in core-admin). Flag for packaging.
- `build-docs/SOLR-17252.md` line 37: the cost clause ("the check also runs for invocations that never open an editor") is not in the response text read by slice 2. Slice 1 traced the call site at `releaseWizard.py` line 1380, before `state.save()` at line 1392, on the general start path; the full start path was not traced. Check against the build-docs decisions.
- `search-components/SOLR-9396.md`: the response calls the bold summary "rewritten", but the commit only added a Limits summary line and left the bold summary unchanged. Correct the response.
- `edismax/SOLR-3962.md` line 16: "is not accepted yet" implies a planned fix the record does not state. Consider "is not accepted".
- `query-parsing/SOLR-874.md`: the head test covers leading AND, OR and `&&`, not leading `||`. The draft's wording does not claim the `||` case, so no change; note for the record.

## Confirmed (drafts checked; no drift)

Query parsing: `SOLR-874` (lines 7, 15, 19), `SOLR-11761` (line 9), `SOLR-17796` (line 32), `SOLR-6014` (line 21 and Proof; the run-37639870647 sentence is gone and the verification date is present).

Core admin: `SOLR-15024` (OWED removed; code-based text matches the record), `SOLR-17297` (line 41 names the reverse order as a separate pull request), `SOLR-17708` (OWED removed; top-up gate per `receipts/SOLR-17708.md` lines 5 to 11), `SOLR-17731` (OWED removed as the record prescribes; readiness flag above).

Search components: `SOLR-17051` (lines 26 and 27), `SOLR-17748` (lines 21, 27 and 29), `SOLR-10492` (line 31), `SOLR-6193` (line 19), `SOLR-9396` (lines 9, 32 and 35; the drift at line 34 is in item 7), `SOLR-10424` (line 36), `SOLR-10694` (line 32), `SOLR-11129` (line 15 and the offset Limit; the HOLD matches the record), `SOLR-14678` (line 33), `SOLR-15895` (line 39), `SOLR-7550` (line 11; the citation stands), `SOLR-7498` (line 25; run date 2026-10-05), `SOLR-9148` (lines 9 and 26), `SOLR-9864` (line 24).

Replication and backup: `SOLR-11650` (lines 25 and 32; 18 of 18, no split; `URLUtilTest` lines 157 to 159), `SOLR-17287` (lines 17 and 27; `RestoreCore.java` 253 and `UpdateLogTest.java` 264), `SOLR-12085` (line 23) and `SOLR-12246` (line 23).

Edismax: `SOLR-3962` (line 16; minor wording above).

Schema and analysis: `SOLR-18134` (line 19; "Lucene 9.12" narrowing).

CLI: `SOLR-16813` (line 39; the count note), `SOLR-9342` (lines 28 and 46). The receipt `receipts/SOLR-9342.md` line 8 still says "Gradle-built distribution" while line 5 says packaged, and still states the Windows TZ fact the record flags; correct the receipt.

Build/docs: `SOLR-17252` (lines 1 and 37; readiness and cost-clause notes above).

Spellcheck: `SOLR-4367` (Proof; the process sentence removal matches the record; no counts added, which matches the record).

Configsets: `SOLR-6960` (line 9; base citation labelled "base commit").

Also noted: the response's F14 premise about the reference guide (item 1 above) is the source of the `SOLR-17987` drift and should be corrected in the response, not only in the draft. `SOLR-6014` F28: the response quotes "likely to fail at search time", which is not in the live draft, so the response describes stale text.

## Not checked

- Build/docs: `SOLR-3684`, `5821`, `17842`. (`SOLR-17752` is covered by slice 1 at its judgment-call edits only.)
- Configsets: `SOLR-7267`, `15478`, `17363`.
- Core admin: `SOLR-4989`, `6438`, `8275`, `8576`, `9750`, `11939`, `12007`, `13246`, `15805`, `17377`, `18010`.
- Edismax: `SOLR-2988`, `6009`, `6320`, `7120`.
- Query parsing: `SOLR-8977`, `9048`, `9149`, `10897`, `11391`, `12212`, `12532`, `12608`, `12871`.
- Replication and backup: `SOLR-8430`, `9598`; `SOLR-9865` (seen only as its `29ab095519d` diff).
- Schema and analysis: `SOLR-9349`, `14199`, `15357`, `15358`, `15945`, `17047`.
- Search components: `SOLR-4374`, `5394`, `6207`, `6831`, `7390`, `8009`, `8240`, `8939`, `10844`, `11153`, `11310`, `11364`, `11470`, `12044`, `13245`, `13876`, `14451`, `15041`, `15319`, `15331`, `17976`.
- CLI: `SOLR-17598` (edited only in `7f583f139d5`).
- Metrics beyond `SOLR-17987`; Spellcheck beyond `SOLR-4367`; Security (no draft edits in the record, but `SOLR-18368` banner checked above).

Gate, BATS and test logs named in the receipts are not on disk. Counts were checked against the receipts only; nothing was re-run.
