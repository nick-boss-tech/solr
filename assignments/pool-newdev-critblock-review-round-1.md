# Pool assignment: review of newdev and Blocker/Critical implementations, round 1

Status: OPEN

Priority: TOP. This round outranks every other open assignment for Linux pool hosts. The CSV triage round is paused (see its Status line). Owner direction, 2026-10-20: review the implementations of the newdev tickets and the Blocker and Critical tickets first.

- Capability tags: review, implementation, gate. Linux hosts only (`hosts/pool-linux.md`). The Windows host does not take units; it runs no gate.
- Intended host: any pool Linux host. Claim one unit at a time under `claims/` before starting (WORKFLOW.md). A host runs one gate at a time (its own Gradle lock).
- Posted by: vm5, 2026-10-20, at the direction of Nick Shanin.
- Source: the fork branches listed in the table, read from `ls-remote origin` on 2026-10-20. Receipts are in `receipts/`; tickets are from `material/jira-issues-csv/solr-jira-metadata-2026-10-18-1792393324869.csv`.

Context: a fix already has a green gate and a receipt for most units, but the owner's review workflow takes an implementation through three review passes, and each pass ends in a regate of the head it reviewed. This round runs that workflow for each unit in the table. Units that never had a receipt (rows 2, 28, 35, 36, 37, 38, 41, 45) get their first gate in pass 1.

## Units

Claim in table order. A unit's passes run in order, with its regate between them. The head column is the fork tip on 2026-10-20 (short SHA); the claim records the full SHA confirmed by `git ls-remote` at claim time.

| # | Priority | Ticket | Summary | Branch (fork) | Head (2026-10-20) | Receipt |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | Blocker | SOLR-17842 | Publish benchmarks for important features | `solr-17842-submit` | `008973f631` | receipts/SOLR-17842.md |
| 2 | Blocker | SOLR-18297 | Upgrade Jetty to v12.1.14 | `solr-18297-jetty-12-1-14` | `5cdc657470` | none |
| 3 | Blocker | SOLR-18458 | Drop debugQuery=true  | `solr-18458-submit` | `344485485f` | receipts/SOLR-18458.md |
| 4 | Blocker | SOLR-18552 | Solr 9: set MMapDirectory.sharedArenaMaxPermits=64 | `solr-18552-submit` | `cb32d0d966` | receipts/SOLR-18552.md |
| 5 | Blocker | SOLR-18552 | Solr 9: set MMapDirectory.sharedArenaMaxPermits=64 | `solr-18552-sharedarena-9x` | `cd7cc695fb` | receipts/SOLR-18552.md |
| 6 | Critical | SOLR-11310 | QueryElevation doesn't work with Solr LTR  | `solr-11310-submit` | `e38ddec527` | receipts/SOLR-11310.md |
| 7 | Critical | SOLR-11650 | Credentials used for BasicAuth displayed in clear text on slave nodes | `solr-11650-submit` | `e4f5e941cd` | receipts/SOLR-11650.md |
| 8 | Critical | SOLR-12743 | Memory leak introduced in Solr 7.3.0 | `solr-12743-submit` | `1bb4b227df` | receipts/SOLR-12743.md |
| 9 | Critical | SOLR-14200 | Shard Tolerance in Streaming API | `solr-14200-submit` | `10991af9bd` | receipts/SOLR-14200.md |
| 10 | Critical | SOLR-14638 | Edismax boost function zero score | `solr-14638-submit` | `44cf1c8fc8` | receipts/SOLR-14638.md |
| 11 | Critical | SOLR-15326 | Missing one record in solr streaming expressions | `solr-15326-submit` | `dab6a46631` | receipts/SOLR-15326.md |
| 12 | Critical | SOLR-16155 | DocumentBuilder should not include field values in error messages | `solr-16155-submit` | `0881de1ed6` | receipts/SOLR-16155.md |
| 13 | Critical | SOLR-16655 | Indexing nested documents and URPs | `solr-16655-submit` | `5e2317443f` | receipts/SOLR-16655.md |
| 14 | Critical | SOLR-16725 | CLUSTERSTATUS API output has inconsistent data types for a few values  | `solr-16725-submit` | `550af28313` | receipts/SOLR-16725.md |
| 15 | Critical | SOLR-16914 | Japanese Synonyms Search not workinh | `solr-16914-submit` | `cd878023d3` | receipts/SOLR-16914.md |
| 16 | Critical | SOLR-17252 | Invoking nano from the release wizard destroys terminal | `solr-17252-submit` | `d730a266a0` | receipts/SOLR-17252.md |
| 17 | Critical | SOLR-17281 | Solr elected a down replica as a leader | `solr-17281-submit` | `ba21ca3279` | receipts/SOLR-17281.md |
| 18 | Critical | SOLR-17297 | Classloading issue with plugin and modules | `solr-17297-submit` | `c0ab38fc0a` | receipts/SOLR-17297.md |
| 19 | Critical | SOLR-17433 | SolrStream by default creates a Http2SolrClient that can only stream f | `solr-17433-17143-submit` | `42b6c4fc91` | receipts/SOLR-17433.md |
| 20 | Critical | SOLR-18010 | Adding a new role corrupts security.json file | `solr-18010-submit` | `571307400b` | receipts/SOLR-18010.md |
| 21 | Critical | SOLR-3044 | Incrementally deprecate NamedList & replace with typesafe API | `solr-3044-submit` | `04b877e9de` | receipts/SOLR-3044.md |
| 22 | Critical | SOLR-3684 | Frequently full gc while do pressure index | `solr-3684-submit` | `663b8ce754` | receipts/SOLR-3684.md |
| 23 | Critical | SOLR-5821 | Search inconsistency on SolrCloud replicas | `solr-5821-submit` | `327547d693` | receipts/SOLR-5821.md |
| 24 | Critical | SOLR-7394 | Shard replicas don't recover after cluster wide restart | `solr-7394-recovery` | `a92a0d89b2` | receipts/SOLR-7394.md |
| 25 | Critical | SOLR-7550 | PeerSync fails if a replica returns 500 error | `solr-7550-submit` | `fcc8a874ab` | receipts/SOLR-7550.md |
| 26 | Critical | SOLR-8767 | RealTimeGetComponent and stored/copyField exclusion | `solr-8767-submit` | `3b5f2d2357` | receipts/SOLR-8767.md |
| 27 | Newdev | SOLR-12718 | StreamContext ctor should always take a SolrClientCache | `solr-12718` | `ba6827557f` | receipts/SOLR-12718.md |
| 28 | Newdev | SOLR-13182 | NullPointerException due to an invariant violation in org/apache/lucen | `solr-13182-child-null-parent` | `2cf82a0b94` | none |
| 29 | Newdev | SOLR-13184 | NPE due to missing input checking in ValueSourceParser | `solr-13184-joindf-missing-arg` | `6bb01a08cd` | receipts/SOLR-13184.md |
| 30 | Newdev | SOLR-13185 | NPE in query parsing because of missing null check | `solr-13185-query-parse-null` | `84523828bb` | receipts/SOLR-13185.md |
| 31 | Newdev | SOLR-16124 | Solr Example should prompt for ZK port | `solr-16124-zk-port-prompt` | `7b81166ae9` | receipts/SOLR-16124.md |
| 32 | Newdev | SOLR-16220 | Documentation leads to NPE Missing SslContextFactory for SolrJ client | `solr-16220-ssl-config-mismatch` | `d7db0784a1` | receipts/SOLR-16220.md |
| 33 | Newdev | SOLR-16750 | Grouping on EnumFieldType fields fails | `solr-16750-enum-group-label` | `783c1323f0` | receipts/SOLR-16750.md |
| 34 | Newdev | SOLR-16865 | Solr should warn about low disk space upon startup | `solr-16865-low-disk-warning` | `606eca38e9` | receipts/SOLR-16865.md |
| 35 | Newdev | SOLR-17719 | TestManagedSchemaWithMultipleAdd timing related failures (should use a | `solr-17719-semaphore-newsearcher` | `732d3fbcc4` | none |
| 36 | Newdev | SOLR-18182 | Deprecate ByteBuffersDirectoryFactory | `solr-18182-deprecate-bytebuffers` | `0abcca2f4b` | none |
| 37 | Newdev | SOLR-18200 | TextProfileSignature produces different signatures for identical input | `solr-18200-textprofile-tiebreak` | `f8c4ee22bd` | none |
| 38 | Newdev | SOLR-18209 | Replace Arrays.asList with List.of for immutable static final fields i | `solr-18209-list-of` | `c8fef7c4cd` | none |
| 39 | Newdev | SOLR-18214 | Base configset during configset creation is not optional | `solr-18214-empty-configset` | `c00cacddf6` | receipts/SOLR-18214.md |
| 40 | Newdev | SOLR-18471 | MetricExporterFactory should use the standard OTEL env vars | `solr-18471-submit` | `f621473095` | receipts/SOLR-18471.md |
| 41 | Newdev | SOLR-18481 | Proxied requests should use X-FORWARDED-FOR header | `solr-18481-xff` | `8f687ca04d` | none |
| 42 | Newdev | SOLR-18503 | JSON Facet API: seven parser-accepted keys are missing from the refere | `solr-18503-submit` | `cc5b84dfcd` | receipts/SOLR-18503.md |
| 43 | Newdev | SOLR-2534 | Add min() and max() on strings | `solr-2534-string-min-max` | `a75b012a20` | receipts/SOLR-2534.md |
| 44 | Newdev | SOLR-8614 | Add simple SolrClient.setUseXmlFormat | `solr-8614-xml-format-toggle` | `658872c20a` | receipts/SOLR-8614.md |
| 45 | Newdev | SOLR-8755 | Add test cases for org.apache.solr.parser.TokenMgrError | `solr-8755-tokenmgrerror-tests` | `e338b6e26e` | none |

## Process for each unit

Pass n, for n = 1, 2, 3:

1. **Review.** Read the diff against the base the receipt names (`git diff <merge-base>..<head>`) and the ticket text (JIRA REST `issues.apache.org/jira/rest/api/2/issue/<KEY>`). Check: the change does what the ticket asks, nothing else, the test would fail on base, the changelog entry is present, and the code follows the surrounding code. Write the findings to `reports/newdev-critblock-review-round-1/<branch>.md` as a section named `Pass n`, each finding with file, line and severity (blocker, major, minor, nit).
2. **Repair, in scope only.** Apply repairs inside the Repair scope of WORKFLOW.md (Gate repairs): tidy output, changelog problems, compile and lint findings with a named fix, up to two failing tests in test code, or a production change of one or two lines that changes no expected value. Each repair is one commit, `TICKET: review repair pass n: <summary>`, pushed fast-forward to the branch. Anything outside that scope is recorded as a finding and not fixed in this round.
3. **Regate, every pass.** Write `gates/<TICKET>-review-pass-<n>.md` from `pool/templates/gate-job.md` with the head this pass reviewed (the tip after any repair), and run it through the host's standard gate runner. Run the regate even when pass n made no change: the regate is what the pass signs off. Green: write the new top section of `receipts/<TICKET>.md` (keep earlier sections), mark the job DONE. Not green: repair within the Gate repairs scope and retry once from step 0 if the tree changed; if the retry fails, or a repair falls outside scope, mark the job `HANDED BACK (reason)` and the unit stops there.
4. Pass n+1 starts only after pass n's regate is green or the unit is handed back. A unit that ends in HANDED BACK records the pass where it stopped and does not continue.

After pass 3 is green, the unit's claim is marked DONE with the three pass sections and the final head. A unit whose review finds the change is not wanted (the ticket is already fixed on base, or the change is not justified by the ticket) is recorded as such in the pass 1 section; the pass still runs its regate and the next two passes review the same head, so the record stays complete.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Scope of this round | decided: the 45 units in the table; the 18552 line appears twice because it has two branches (main and 9.x), each its own unit | none | vm5, under the decision ladder (rung 4): the table is the set of unresolved Blocker and Critical tickets and newdev tickets that have a fork implementation branch |
| Excluded: SOLR-18507 | decided: not in this round | its phase-4 repair is the open assignment `assignments/pool-solr-18507-phase-4-implementation-repair-round-1.md`; review it after that closes | vm5, rung 4 |
| Excluded: new-ui and ui tickets | decided: not in this round | they belong to `assignments/pool-ui-tickets-round-1.md`, which needs a browser | vm5, rung 4 |
| Excluded: draft-only branches | decided: not in this round | a `-draft-open` or `-draft-close` branch carries a PR description or a close note, not an implementation | vm5, rung 4 |
| Order | decided: Blocker first (rows 1 to 5), then Critical (rows 6 to 26), then newdev (rows 27 to 45); table order within each group | none | Nick Shanin |
| Passes per unit | decided: three review passes, each followed by a regate on the head that pass reviewed | none | Nick Shanin |
| Regate with no change | decided: run it | the pass's regate is its sign-off, even when the review made no change | Nick Shanin |
| Repair scope | default: the Repair scope in WORKFLOW.md (Gate repairs) | outside that scope, record a finding and do not fix it | WORKFLOW.md |
| Gate failure after retry | default: HANDED BACK | the unit stops; the record names the pass and the failing step | WORKFLOW.md |
| Head differs from the receipt | default: review the live tip and record the difference | no branch reset; a tip that moved past the receipt is reviewed as it stands | vm5, rung 1 (the receipt names its own gated head) |
| Review finds the change is not wanted | default: record it in pass 1, keep the three passes | no branch deletion, no close, no JIRA change | vm5, rung 4 |
| Units with no receipt | default: the first gate is pass 1's regate | a unit with no receipt that cannot gate on this host is recorded `NEARLY` with the blocking step | WORKFLOW.md |
| Host capacity | default: a host takes one unit at a time; a unit's three passes may be taken by different hosts | when a host is busy, the next free host takes the next pass; a stale heartbeat is handled under WORKFLOW.md (rule 4) | WORKFLOW.md |

## Deliverable

- `reports/newdev-critblock-review-round-1/<branch>.md` per unit, with one section per pass (findings, repairs with commit SHAs, regate head and result, gate log path on the host).
- Each unit's `receipts/<TICKET>.md` gets one new top section per green regate.
- Each unit's claim is marked DONE, HANDED BACK, or NEARLY with the pass and the step, in the same push as its last report.
- This assignment's Status becomes DONE when all 45 units reach DONE, HANDED BACK, or NEARLY.

## Limits

- No PR opening, PR comment, PR state change, JIRA comment, GitHub comment, or email. No force push. No branch deletion. No change to any upstream branch.
- Repairs are pushed only to the fork branch named in the row, fast-forward from the head the pass reviewed, as WORKFLOW.md requires.
- Commit as Nick Shanin with no trailers. No em dashes.
- Do not use the Windows host for any build or gate.
