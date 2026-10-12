# Re-gate watch, round 1

Verdict. All three drafts are on HOLD at the head their Proof names. None is changed by its re-gate in the draft text, except where noted. The internal blocks in the drafts were moved to `reports/internal-blocks-round-1.md`.

Inputs: `assignments/pool-regate-watch-round-1.md`, claim `claims/pool-regate-watch-round-1.md`. Two read-only subagents checked the drafts, the claims and the gate jobs. The fork branch for SOLR-14231 was fetched read-only to resolve a head change.

## SOLR-14231 (`pr-drafts/streaming/SOLR-14231.md`): HOLD at `2f73d00a3996`

- The fork tip moved to `593463f398cb`. That commit is a one-line changelog title correction, `changelog/unreleased/SOLR-14231.yml`, pushed ahead of the gate. Its parent is the claimed head `2f73d00a3996`, and no code changed.
- The draft's Proof names `2f73d00a3996` and was run there, so it stays HOLD at that head. Its code links still resolve at `593463f`, because no code changed.
- Post-gate text. The job gives the replacement changelog title: "scoreNodes streaming expression now sets its field and collection from every node, returns no tuples when there are no nodes, and sends its /terms request as a POST." The draft has no title text, so no sentence changes. Its changelog link (line 34) re-points to the gated head.
- Test names are unchanged by the job.

## SOLR-15357 (`pr-drafts/schema-analysis/SOLR-15357.md`): HOLD at `d717899b8736`

- The fork tip matches the claimed head. Gate job FIX 1 corrects the sub-field names in `CopyFieldSubFieldsTest.java`, and FIX 2 replaces the changelog title.
- Post-gate changelog title (FIX 2): "The sub-fields of CurrencyFieldType, PointType and BBoxField copyField destinations are now recorded as copyField targets." The draft has no title text.
- Proof, lines 27 and 29: wording depends on the gate. The job's finding 3 says the class does not compile on base, and only two of four methods call the API. The draft currently says "the class cannot run on base", so the wording needs one choice when the gate lands.
- Head re-point at the gated head: links at lines 1, 17, 21, 27, 29 and 44. Line 40 stays at the base commit.
- Open for the main side: whether the test-method rename in finding 12 is in scope. The job does not include it.
- The INTERNAL block at line 1 was removed, and its text is in `reports/internal-blocks-round-1.md`.

## SOLR-15945 (`pr-drafts/schema-analysis/SOLR-15945.md`): HOLD at `adb0fd450c06`

- The fork tip matches the claimed head. The gate is a text-only fast-forward: one changelog title and one test-schema comment.
- Post-gate changelog title: "Stored-only DateRangeField and SpatialRecursivePrefixTreeFieldType fields (indexed=\"false\") no longer fail to load because of the norms and index-options checks." The draft has no title text.
- Post-gate test-schema comment: "stored-only: the norms and index-options checks do not apply to these fields, so the type skips them." The draft has no comment text.
- Proof, line 31: the count comes from fresh JUnit XML at the gated head. The expected value is 1 of 1, which is not yet confirmed.
- Head re-point: links at lines 22, 30, 31 and 47. The job names only line 47.

## Internal blocks

- Fourteen blocks were removed from draft bodies this round, and each is copied verbatim into `reports/internal-blocks-round-1.md`. The convention is that owner notes and holds live in reports.
- Not yet handled: `search-components/mergeids-skipped-shard-totals.md` (no ticket number yet), and nine drafts with block-style text that the pattern list did not match (listed in the internal-blocks report).

## Not done

- No builds, tests, Gradle runs, PR writes, comments or Jira posts.
- No gate job was changed. The gate jobs belong to vm2.
