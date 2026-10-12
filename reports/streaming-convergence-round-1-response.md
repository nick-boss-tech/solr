# Streaming convergence, round 1: main-side response

Round 1 returned NOT CONVERGED: all six drafts held open items, with 3 blocking, 12 should-fix and 12 note findings. This lane verified every blocking and should-fix finding against the receipts and the branch code before applying anything, and applied each holding finding's exact replacement text to its draft. One should-fix (ST-R1-23) is a branch-side precondition and is recorded for the branch lane; SOLR-12505 stays NOT CONVERGED until that commit lands. One should-fix (ST-R1-27) is rebutted with evidence below. Two blocking findings are branch-side changelog titles (ST-R1-33) or rest on a receipt that records no verified run date (ST-R1-32); both are dispositioned below. Application method: exact-string replacements, each asserted to occur exactly once before applying; the full diff was then reviewed file by file.

## Counts

| Grade | Items | Applied in this lane | Branch-side, recorded below | Rebutted | Standing |
|---|---|---|---|---|---|
| Blocking | 3 | 2 (ST-R1-01, ST-R1-32) | 1 (ST-R1-33) | 0 | 0 |
| Should-fix | 12 | 10 | 1 (ST-R1-23, precondition) | 1 (ST-R1-27) | 0 |
| Note | 12 | 0 | 0 | 0 | 12 standing or record items, two with main-side calls recorded |

After application, five of the six drafts are CONVERGED on their text: SOLR-9852, SOLR-10322, SOLR-10882 (a branch-side changelog title is owed at packaging), SOLR-12657 and SOLR-14231 (a branch-side changelog title is owed at packaging). SOLR-12505 is NOT CONVERGED: ST-R1-23's branch commit is a precondition to opening.

## Fact checks before applying

- **SOLR-10322 (ST-R1-01, 02, 03):** at the merge-base `97d973814336101e12475558d7419321c743de79`, line 486 reads `streamContext.getSolrClientCache()`, and `open()` at lines 297 to 299 creates the stream's own `clientCache`. At head `80ce9d7a3c8` the read (line 485) uses `clientCache`. The lines the ST-R1-03 replacement cites (297 to 301, 352 to 353 at head) are identical to the base. The merge-base was verified with git.
- **SOLR-9852 (ST-R1-05):** reports/streaming-expressions-round-1-s1.md records that the ticket's SQuirreL error comes from getTypeInfo. The appended Limits sentences state exactly that limit.
- **SOLR-10882 (ST-R1-08, 09):** receipts/SOLR-10882.md confirms no null-value test was gated, so the draft's null claims came out of both section openers, the missing-field and toString sentences were deleted, and the Limits summary now names only the untested cases that exist. The run date rests on the vm2 claim heartbeat of 2026-10-11T02:09:11Z and the gate green recorded in the takeover log's 2026-10-10 20:38 MDT pass; the live tip `5d7d07a5b17bfbf9370fe8562ce7b5d9909118ee` was confirmed by ls-remote, so the Proof bullet now carries "verified 2026-10-10".
- **SOLR-12505 (ST-R1-21, 22, 23, 24):** at the base, `FetchStream` (line 240) appends the join field as `df`, and the key values are escaped through `ClientUtils.escapeQueryChars`. `QParser` lines 387 to 396 and `TermsQParserPlugin` (line 74 NAME = "terms", line 150 separator ",") at the base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` match the re-pointed links. The merge-base is 72 commits behind upstream main (verified with git) and the branch is not rebased; the Limits Base bullet now says so. ST-R1-23 holds on the code: at head `5f20e1171bc890509d76e4f5f7defcc4fc8c102c`, `FetchStream` line 239 still appends the join field name into `df=` unescaped.
- **SOLR-12657 (ST-R1-26):** the diff from `16a0a69e839` to the gated head `23cc27a9892594b96a689fa4cf0ed768792bc678` touches only Metric.java, MinMetric.java, MaxMetric.java and MinMaxMetricTest.java; the test file and FacetStream are unchanged, so the Proof bullet now names the pre-fix head and date for those runs. receipts/SOLR-12657.md records the proof leg as exactly 1 of 3 tests failing, and the draft now says so instead of "it fails".

## Rebuttal

- **ST-R1-27 (SOLR-12657, should-fix): REBUTTED, no edit.** The finding questioned the draft's "Verified 2026-10-10" against the receipt's re-gate timestamp of 2026-10-11T04:04:02Z. That timestamp is UTC; it is 2026-10-10 22:04 MDT local. The settled date convention (C1) keeps local MDT verification dates in PR bodies, and the register itself allowed the date to stand if the main side confirms a different run date. The main side confirms 2026-10-10 local. The draft is unchanged on this item.

## Per-finding dispositions

### SOLR-9852
- ST-R1-05 (should-fix): applied. The Limits paragraph now states that the ticket's SQuirreL error comes from getTypeInfo, that this change does not fix that error, and that whether SQuirreL continues after the exception was not checked.
- ST-R1-06 (note): stands. The key match sits in Tuple.java, which the draft does not link; a citation gap for the opening pass, not a text error.

### SOLR-10322
- ST-R1-01 (blocking): applied. The cause now reads: "With no context cache, that read dereferences null, because the stream context's cache is null. The stream's own cache is created later, on open, and the read did not use it."
- ST-R1-02 (should-fix): applied. The paragraph now ends: "Links marked base point to the merge-base commit, `97d973814336`."
- ST-R1-03 (should-fix): applied. The bullet now reads: "The stream already creates its own cache on open when it has none, and closes it in `close()` ([L297 to L301](head link), [L352 to L353](head link)). Those lines are unchanged. The change is in the three reads below."
- ST-R1-04 (note): stands. The premise log's header is not read on this side; the main side reads it before publishing.

### SOLR-10882
- ST-R1-07 (should-fix): applied. The Limits section now opens by scoping the change to the two defects, not the whole 2017 restructuring ticket.
- ST-R1-08 (should-fix): applied, all five sub-edits (both openers drop the null claims; the missing-field and toString sentences are deleted; the Limits summary and first sentence now match the gated evidence). Branch-side: the changelog fragment title at `5d7d07a5b17` still carries the null clause. Replacement recorded for packaging: "The array stream evaluator with sort=asc|desc now fails with a clear error when the values mix incomparable types (for example text and numbers) instead of a ClassCastException."
- ST-R1-09 (should-fix): applied (run date added; evidence above).
- ST-R1-10 (note): stands. The current fragment title's "incomparable types" wording is loose; the ST-R1-08 replacement recorded above is the title the branch should carry.
- ST-R1-11 (note): stands as a record item. receipts/SOLR-10882.md labels `e044bf20` as the merge-base (proof base); the merge-base with upstream/main is `cabedd1d968` (verified with git). The receipt line is corrected at its next refresh; the draft does not repeat the label.
- ST-R1-12 (note, cross-draft): main-side call recorded. Owner decision 5 (rebase before opening versus open on the older base) is settled under the decision default: the harder option applies, so these branches are rebased before opening. That is an opening-time action, not a draft edit; ST-R1-24's drift statement covers SOLR-12505 meanwhile.

### SOLR-12505
- ST-R1-21 (should-fix): applied. The fetch batch is now described as `{! df=<join field> q.op=OR cache=false}` followed by the escaped key values.
- ST-R1-22 (should-fix): applied. The QParser and TermsQParserPlugin links now point at the base commit with ", base" labels.
- ST-R1-23 (should-fix): NOT a draft edit; branch-side precondition. The adopted recommendation is recorded for the branch lane: escape or validate the `on=` field name in `FetchStream`, with a test, and a new focused run at the new head. SOLR-12505 stays NOT CONVERGED until that commit lands and the draft is re-checked against it.
- ST-R1-24 (should-fix): applied (the Base bullet in Limits).
- ST-R1-25 (note): stands. The realtime-get limit could cite a 2021 ticket comment; a citation addition for the opening pass.

### SOLR-12657
- ST-R1-26 (should-fix): applied (pre-fix head, date, and the exact 1-of-3 failure count).
- ST-R1-27 (should-fix): REBUTTED, above.
- ST-R1-28, 29, 30, 31 (notes): stand. Record items: the "ISO-8601 instants" wording and the mixed-value test range (28), a FacetStream lines 952 to 958 citation (29), the compile check of the core and SQL callers (30), and a javadoc position on the branch (31). None changes a claim the draft makes.

### SOLR-14231
- ST-R1-32 (blocking): applied. The Proof summary no longer claims "verified 2026-10-04": receipts/SOLR-14231.md records only a reconcile gate from that era and a head verification on 2026-10-10, with no verified run date.
- ST-R1-33 (blocking): branch-side. The fragment title at head `2f73d00a3996da1623156ccef649b59be0b59d45` claims the single-node and no-node failure, and no base run exists, so that failure claim is unshown. The draft carries no title text. Replacement recorded verbatim for the branch lane: "scoreNodes streaming expression now sets its field and collection from every node, returns no tuples when there are no nodes, and sends its /terms request as a POST."
- ST-R1-34 (note): stands (the nested-gather wording in Limits).
- ST-R1-35 (note, length): main-side call recorded. ACCEPT. The draft runs over the 3,500-character guide and carries five genuine Choices; the length is the formula's unusually-complex case.

## Verdicts after application

- SOLR-9852: CONVERGED.
- SOLR-10322: CONVERGED.
- SOLR-10882: CONVERGED on text (branch-side changelog title owed at packaging).
- SOLR-12505: NOT CONVERGED (ST-R1-23 branch precondition).
- SOLR-12657: CONVERGED (ST-R1-27 rebutted with the evidence above).
- SOLR-14231: CONVERGED on text (branch-side changelog title owed at packaging, ST-R1-33).

## What round 2 needs to settle

1. SOLR-12505: whether the ST-R1-23 branch commit (escape or validate the `on=` field name, with a test and a focused run) has landed; if so, re-check the draft against the new head.
2. The ST-R1-27 rebuttal: judge the date evidence (the re-gate log timestamp 2026-10-11T04:04:02Z is 2026-10-10 22:04 MDT) and the C1 convention.
3. Whether the two branch-side changelog titles (SOLR-10882, SOLR-14231) have been replaced on their branches with the texts recorded above.
4. Confirmation that the applied replacements are present in the live drafts, word for word in substance.
