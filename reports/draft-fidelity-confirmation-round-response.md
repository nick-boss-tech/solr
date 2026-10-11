# Response: draft fidelity confirmation round fixes

Scope: the main side verified each of the seven DRIFT items in `reports/draft-fidelity-confirmation-round.md` against the record that item cites, and against the branch source at the cited head where the item cites source. Six fixes are applied in this push. One fix is not applied, because verification against the cited branch record contradicts it; the reason is stated in full under that draft. Plain record corrections from the report's notes are applied and listed separately. The rest of the notes are listed, not applied.

Base for this work: the work started off pr-prepare tip `09a8414d8908` and was rebased onto tip `ebc7105f64` before pushing; the intervening commits touched none of the files in this push.

## Per-draft outcomes

### 1. `metrics/SOLR-17987.md`: APPLIED

Verified at head `38abf6423126`: `metrics-reporting.adoc` line 106 and `EnvToSyspropMappings.properties` line 39 both use `SOLR_METRICS_DISABLEDREGISTRIES`, with no underscore between DISABLED and REGISTRIES. That is the name the head reads, so the draft's renamed form stated a name the branch does not read yet. The rename is branch work (item F13 in the application response), not landed text.

- Line 15 now names `SOLR_METRICS_DISABLEDREGISTRIES`, the head form.
- The Choice paragraph no longer restates the cost: the last sentence of that paragraph, which repeated the cost sentence before it, is removed.
- One citation nuance, recorded plainly: the report also cited `solr-properties.adoc` line 170 for the same form. That line gives a general example for another property, not this setting. The guide line and the mapping line above settle the item on their own.
- The response's F14 premise is corrected in `reports/draft-fidelity-fixes-application-response.md`, in both places it appeared (the Group D list and the flagged-items pass): the guide does not already use the renamed form for this setting, and the rename is branch work that has not landed.

### 2. `search-components/SOLR-12556.md`: APPLIED

Verified against `receipts/SOLR-12556.md` line 9: buckets first seen during refinement keep the base behaviour, and the changelog title's first sentence is true only for buckets known before refinement, and the PR text should say so. The draft's body and Choice already said that; only the bold summary did not.

- Line 13 now reads: buckets known before refinement that were not refined against every shard that returned the facet are now dropped.
- Tension with an earlier record, stated openly: the Group O settlement for item 32 aligned this summary to the gated changelog title, which states the change without the qualification. The receipt is the record the confirmation item cites, and it directs the PR text to carry the qualification, so the qualification wins in the draft. Narrowing the changelog title itself stays a packaging item: it is a branch edit and needs a re-gate.

### 3. `edismax/SOLR-4362.md`: APPLIED

Verified against the Group O settlement for item 62 and against `isSlopClause` at head `e96a057439`: `^2~10` returns false, and only `~N` and `~N^boost`, such as `~10^2`, are recognised.

- Line 13 no longer says "in either order". It now names a boost after the slop, such as `~10^2`.
- Line 30: the sentence claiming the slop number can still reach a pf2 shingle for the uncovered forms is removed. No record traces that claim. The bullet keeps the two uncovered forms and the named follow-up that covers them.

### 4. `build-docs/SOLR-16914.md`: NOT APPLIED, with reason

The fix contradicts the branch record it cites. The report checked the "What happens today" passage against the text at head `cd878023d3d0`. That head is the branch tip, so its page already contains this branch's own change. The passage describes the state before the change, and the record for that state is the base.

- `receipts/SOLR-16914.md` records that the diff against base `14c7aac0d151` is one file, `language-analysis.adoc`, the Japanese synonyms guidance.
- At that base, the Japanese tokenizer intro says search mode segments compounds "while also keeping the original compound terms as synonyms", with no option named in the intro. The quoted phrase "as synonyms" is present at the base; it is absent at the head because the branch rewrote that sentence.
- At that base, the `discardCompoundToken` entry reads "Optional | Default: none" and "Set to `false` to keep original compound tokens with the `search` mode".
- The branch diff against that base does exactly what the draft's "What this change does" section says: it names `discardCompoundToken="false"` in the intro (head line 2292), adds the paragraph at head lines 2315 to 2317, and changes the table entry to "Default: `true`" with `search` and `extended` modes (head lines 2347 to 2354).

Rewriting lines 11 and 13 from the head text, as the report directs, would make the "today" passage describe the post-change page and would contradict the receipt's diff record. The draft is left unchanged. Round 2 is asked to re-check this draft against the base, not the head.

### 5. `cli/SOLR-17029.md`: APPLIED

Verified against the Group O settlement for item F5 and `receipts/SOLR-17029.md` line 7: both record the classification only, an upstream log-timing race that also fails on pristine base at roughly the same rate. Neither records a "few seconds" detail.

- The Proof bullet no longer carries the parenthetical about a few seconds. It now says the race also fails on the base code at roughly the same rate, which is the strength of the record.

### 6. `security/SOLR-18368.md`: APPLIED

Verified against `receipts/SOLR-18368.md` line 9 and the Group O settlement for item F7: the do-not-open option is foreclosed by the still-an-issue verdict and the resolved-ticket rule, and the branch joins the openings list. The key question, the SOLR-18368 key or a follow-up reference, is settled at opening approval.

- The banner no longer offers option (c). Options (a) and (b) remain, and the note still says to remove it after deciding.
- Note on the report's caveat: the report said `reports/solr-18368-still-an-issue.md` was not in its worktree. That file is present at this tip, and the receipt's line 7 record of it matches the settlement.

### 7. `search-components/SOLR-9396.md`: APPLIED

Verified against the head source and the record's replacement: at head `a5ab2eda4e6`, `SubQueryAugmenterFactory.java` line 307 compiles the pattern `\$\{?row\.`, which matches both spellings, and `reports/draft-fidelity-searchcomponents-edismax-s10.md` lines 51 and 52 name `${row.field}` in the replacement bullet. The Group O settlement for item 57 keeps the `${row.` branch in this change and names the narrowing as a follow-up, which the draft's second Limits bullet already does.

- Line 15 now names both spellings: `$row.field` and `${row.field}` references.
- The first Limits bullet now names both spellings: only a `$row.field` or `${row.field}` reference written in the subquery's own parameters is scanned.

## Record corrections applied from the report's notes

- `reports/draft-fidelity-fixes-application-response.md`, item F14, two places: the premise that the reference guide already uses the renamed environment variable form is corrected, as item 1 above records.
- `reports/draft-fidelity-fixes-application.md` line 34: the line grouping `SOLR-6193` and `SOLR-9396` as "bold summary rewritten" is corrected. For `SOLR-9396` the commit only added a bold Limits summary line and left the bold summary in "What this change does" unchanged. The `SOLR-6193` half of the line stands as written.
- `receipts/SOLR-9342.md` line 8: "Gradle-built distribution" now reads "packaged distribution", matching line 5 of the same receipt and the draft. The Windows sentence no longer states as fact that the Windows JVM does not read TZ for GC log timestamps; it now records that the point was not verified, that the check is deferred, and that the follow-up is named in the draft's Limits. That matches the Group O settlement for item F4, which flagged this receipt sentence for the same correction.

## Notes listed, not applied

- `search-components/SOLR-17731.md` readiness: the application response says the draft must not be used until the owed branch work lands, but the file carries no HOLD or OWED marker. Flagged for packaging: add the marker, or land the owed work recorded in `material/core-admin-round-1-answers.md`, before the draft is used. No marker was added in this push.
- `build-docs/SOLR-17252.md` line 37 cost clause: the clause about invocations that never open an editor is not in the response text one slice read. The other slice traced the call site at `releaseWizard.py` line 1380, before `state.save()` at line 1392, on the general start path; the full start path was not traced. Listed for a check against the build-docs decisions. No edit made.
- `edismax/SOLR-3962.md` line 16: "is not accepted yet" may imply a planned fix the record does not state. The report raises it as a consideration ("is not accepted"), not a correction. No edit made.
- `query-parsing/SOLR-874.md`: the head test covers leading AND, OR and `&&`, not leading `||`. The report finds no draft wording change is needed. Noted for the record only.
- `query-parsing/SOLR-6014.md`, item F28: the application response quotes "likely to fail at search time", which does not appear in the live draft, so those F28 entries describe stale text. Listed here; the response's historical entries stand, and the settlement's posted premise job for the traced cases is the path that settles the wording.

## Follow-up

`assignments/pool-draft-fidelity-confirmation-round-2.md` is posted with this push, scoped to re-checking the seven drafts above in place, with a CONFIRMED or DRIFT verdict per draft. Nothing was built, tested, gated or posted outside this branch in this pass.
