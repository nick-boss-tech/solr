# Internal blocks removed from draft bodies, round 1

Convention adopted in this round: owner notes, hold headers and staging notes live in reports, not in draft bodies. Each block below is copied verbatim from the draft before it is removed, so no owner text is lost. Lines are the block's line range in the draft as it stood at this round's start.

## build-docs/SOLR-16322.md (lines 1-2)
```
[HOLD: do not post. Owner decision 17 is open. An exercised non-beast failing run and a passing build are owed before any PR. Delete this line when they land.]

```

## build-docs/SOLR-16914.md (lines 1-2)
```
[DRAFTABLE, narrow claim only. Owner item 3 recommends a main-side token stream check of the non-linear statement before posting; the Limits line covers it if the owner posts without that check.]

```

## build-docs/SOLR-17252.md (lines 1-2)
```
[HOLD: do not post. Owner decision 1 (where the check runs, Q3) is posed in the Choice below: two recorded positions conflict (the receipt's Q3 keeps the check at startup, before release state exists; this branch checks at the first menu build, after the state file is written), so the question goes to the maintainer with the implemented position stated and the earlier check offered. Owner decisions 2 and 3 (the emacs list, the exit or warning choice) may change the Choice. Settle these before posting.]

```

## build-docs/SOLR-17752.md (lines 1-2)
```
[WAITING: owner decision 1, the branch base. The merge-base is 56ec140e3636, not the receipt's 14c7aac0d151. Rebase and focused rerun are the owner's call before posting. Do not post until settled.]

```

## build-docs/SOLR-17842.md (lines 1-2)
```
[WAITING ON OWNER CALL, DO NOT POST YET: keep SOLR-17842 open for published results, or narrow it to instructions only. The guide's example also has an unquoted `<build>` placeholder that bash reads as a redirect. Fixing that needs a new commit on the submit branch, which this round does not make. Rewrite this text against the new head, then delete this note before posting.]

```

## build-docs/SOLR-3684.md (lines 1-2)
```
[HOLD: do not post. Owner decision 1 (submit or retire) is open, and the round 12 review at this head says retire. Paste the design record's exact Choice wording before posting.]

```

## cli/SOLR-10667.md (lines 1-2)
```
OWNER NOTE (not PR text; delete before posting). HOLD. Written against 1e3f022552be4d281a020e015f4c8c05ce4eec6c. Owner decision 10's recommendation (exclude `solrclient/**`) was adopted under the decision default on 2026-10-11. Before posting: the branch gains the exclusion for the `solrclient/` folder the Learning To Rank build writes into the example directory, the head moves, a re-gate runs at the new head, and this text is rewritten against it. The changelog title fix (CLI-R1-05) rides the same branch work.

```

## replication-backup/SOLR-11650.md (lines 1-4)
```
OWNER NOTE (not PR text; delete before posting). HOLD. Written against `e4f5e941cd8e88524196b03f2022261c2215c3ba`. Posting waits until the owed commits land on `solr-11650-submit`: the changelog title narrowing, the URLUtil javadoc replacement and the test comment replacement. When they land, re-point the citations and Proof to the new head and add: Only comments and the changelog title changed after the verified run.

---

```

## replication-backup/SOLR-8430.md (lines 1-4)
```
OWNER NOTE (not PR text; delete before posting). HOLD. Written against 0d64c35c5a7d3caffa9981fd1e1a61e1d818edf5. The cap and the fixture fix are gated green at this head (receipts/SOLR-8430.md), and the post-cap text for RB-R1-01 and RB-R2-01 is applied. Before posting: take the choice call (DISCUSS, not taken: one limiter per rate value, or one per node).

---

```

## replication-backup/SOLR-9598.md (lines 1-5)
```
OWNER NOTE (not PR text; delete before posting). HOLD. Written against c8407773f76ca49711b5777ff9f69736d87b1e90.
Before posting: (1) decide the default for RESTORE and the choice section (SOLR-17712 direction versus the opt-out in this branch). (2) Apply report Finding 4 (comment only in RestoreCmd.java). That moves the head, so re-run the focused test at the new head or say that only a comment changed. (3) Apply report Finding 5 (changelog wording). (4) Landing order decided in the answers record: this branch lands before SOLR-12651.

---

```

## replication-backup/SOLR-9865.md (lines 1-5)
```
OWNER NOTE (not PR text; delete before posting). HOLD. Written against 4937608bb181efae104c0d6f0257f445af50bf52.
Posting waits on the packaging call for the overlap with the SOLR-18532 change (PR #5100): if that change lands first, this branch drops its rollback write-back at packaging and keeps only the regression test. See the Choice and Limits in the text below.

---

```

## security/SOLR-18368.md (lines 1-2)
```
[WAITING ON OWNER CALL. Do not post until the owner decides under which key the PR opens: (a) no key, with the upstream PR cited in the body (delete the Jira line below); (b) a follow-up reference to SOLR-18368 (keep the Jira line). Remove this note after deciding.]

```

## Not changed in this round

- `search-components/mergeids-skipped-shard-totals.md`: a staging draft with no ticket number. The title exists only in its staging block, and `SOLR-XXXXX` still appears in the body. It waits for the ticket number, and the staging block moves to the report then.
- `schema-analysis/SOLR-15357.md` and `schema-analysis/SOLR-15358.md`: each carries an "INTERNAL" comment at line 1. SOLR-15357 is a re-gate draft, so it waits for the regate-watch report. SOLR-15358 has the record-conflict hold, which the decisions round settled, so its block goes next.
- Broader matches the pattern list did not cover, found by a read-only scan: `query-parsing/SOLR-11391.md` and `query-parsing/SOLR-12608.md` (line 1, "HOLD: not for posting"), `search-components/SOLR-15319.md` (lines 50 to 59, "REVIEWER NOTES"), `search-components/SOLR-15331.md` (line 41, "Owner notes"), `search-components/SOLR-17051.md` (lines 56 to 63, "Owner notes"), `search-components/SOLR-17155.md` (line 21, "Owner to confirm"), `search-components/SOLR-6831.md` (lines 43 to 48, "NOT FOR POSTING"). These go in the next cleanup round, with the same copy-first step.
- `build-docs/SOLR-17842.md` and `build-docs/SOLR-17752.md`: their Proof heads still name the pre-decision heads. The text stays until the owner decides the branch base and the guide placeholder fix.

## Added later in this round

The re-gate watch found two more INTERNAL comments at line 1. They are copied verbatim here before removal.

### schema-analysis/SOLR-15357.md (lines 1-2)
```
<!-- INTERNAL. Remove this block before posting. Written against head d717899b8736309bd72f54b11230f198ae226e7d. HOLD. Before posting: (1) fix the sub-field names in CopyFieldSubFieldsTest (report FIX 1) and rerun; (2) replace the changelog title (report FIX 2); (3) owner decision on the real-time get and fl=* docValues path (report owner decision 2); (4) update the head in the Proof and in the links once the fix commit exists. -->

```

### schema-analysis/SOLR-15358.md (lines 1-2)
```
<!-- INTERNAL. Remove this block before posting. Written against head cbb345f2e7df452e20036d357c82cc63db96ffd3. HOLD for two items: (1) owner call on the default docValues as stored setting (choice section); (2) land after SOLR-15357 (report, finding 8). The Proof base result is filled from the 2026-10-12 proof record in receipts/SOLR-15358.md. -->

```

