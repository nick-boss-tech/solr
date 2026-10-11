# SolrJ and clients round 1 draft fidelity, slice S3

Assignment: the draft fidelity brief in the windows review agent scratchpad (`brief-draft-fidelity.md`). Slice: SOLR-4424, SOLR-5220, SOLR-6046, SOLR-7709 and SOLR-8536, drafts in `pr-drafts/solrj/`. Round records checked: `reports/solrj-clients-round-1.md` (roll-up; claim `5bbcedf2d1e`, assignment `dd6c4c7e397`) and its parts S1 (4424, 7709), S3 (6046, 8536) and S4 (5220). S2, S5 and S6 do not mention these five tickets (grep). Worktree at `3576773b2f8`, the origin tip named by the lead. The brief's claim commit `e84522fa5bc` is older. The five drafts last changed in `d7f234ecbe1`.

Checks run: `git ls-remote origin` for each fork branch (2026-10-10), `git cat-file`, `git merge-base` and `git show` for cited SHAs, read of the cited lines at each head and base, `receipts/<TICKET>.md`, `material/` grep, the Jira snapshots in the main checkout's `research/jira-context` (read only), and one read-only `gh pr view 5077`. No build, test, gate, queue run, commit, push or post.

Material: no answers file in `material/` names any of the five tickets. The one grep hit is a commit hash that contains "8536", not a ticket.

## Verdicts

| Draft | Head checked (ls-remote) | Verdict |
|---|---|---|
| SOLR-4424 | `2e947b7f622c6c530350ecef3679b01e1b004cde` (solr-4424-submit), matches draft | DRIFT (1 item) |
| SOLR-5220 | `e415c409044c4a6cc846cddfe56c1d7b9f262767` (solr-5220-submit), matches draft | DRIFT (1 item) |
| SOLR-6046 | `3b833e369a0bf5fc06c976c7e1febe17694223c4` (solr-6046-submit), matches draft | DRIFT (1 item, branch changelog title; draft text consistent) |
| SOLR-7709 | `501078f22203f636c171ec90f63d746896365593` (solr-7709-submit), matches draft | CONSISTENT |
| SOLR-8536 | `28552ddc26eb30df5c9bfcd612bbac3698372442` (solr-8536-submit), matches draft | DRIFT (2 items: 1 draft sentence, 1 branch changelog title) |

Merge-bases used for base links: 4424 and 6046 `97d973814336101e12475558d7419321c743de79`; 5220 `cabedd1d968059215188f4e7563fb303241899ed`; 7709 `c3cdf7b46e8cfff3673f76d881f32cf8e7b00622`; 8536 `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`. All resolve, and each equals `git merge-base <head> upstream/main`.

Proof counts, test names and dates match the receipts for all five. Verification dates present: 4424 2026-10-06, 5220 2026-10-07, 6046 2026-10-06, 7709 2026-10-05, 8536 2026-10-05.

## SOLR-4424

Verdict: DRIFT (1 item).

1. Draft says: "In this tree the places are `getSolrParamsFromNamedList` in [`RequestHandlerBase.java`](...#L217-L221), which handles handler defaults, appends and invariants; the javabin update reader in [`JavabinLoader.java`](...#L165); and the javabin update request codec in [`JavaBinUpdateRequestCodec.java`](...#L122)."
   - Evidence: `git grep -n "toSolrParams()" 2e947b7f622c -- solr`, non-test, excluding NamedList.java, finds 29 source files that call it. They are in solr/core (for example IndexSchema.java, UpdateRequestHandler.java, FacetParser.java, QueryElevationComponent.java, HighlightingPluginBase.java, eight update processor factories), solr/solrj (JavaBinUpdateRequestCodec.java at L122 and L229) and solr/modules (ClusteringComponent, XSLT handler and writer, MirroredSolrRequestSerializer, langid and language-models processors). `getSolrParamsFromNamedList` also has callers at SolrConfigHandler.java L403-405 and admin/api/GetConfig.java L115-117. The draft lists three paths as if they were all of them. The round report (S1) says only "the callers that convert lists to params in this tree" and does not enumerate them. The first Limits sentence ("The check runs only where a list becomes parameters") is correct, and the "no response reader" sentence holds for these callers.
   - Replacement: "In this tree the check runs in 29 non-test source files that call `toSolrParams`, in `solr/core`, `solr/solrj` and `solr/modules`. The handler defaults, appends and invariants path (`getSolrParamsFromNamedList` in [`RequestHandlerBase.java`](https://github.com/nick-boss-tech/solr/blob/2e947b7f622c6c530350ecef3679b01e1b004cde/solr/core/src/java/org/apache/solr/handler/RequestHandlerBase.java#L217-L221)) is one of them, and so are the javabin update reader in [`JavabinLoader.java`](https://github.com/nick-boss-tech/solr/blob/2e947b7f622c6c530350ecef3679b01e1b004cde/solr/core/src/java/org/apache/solr/handler/loader/JavabinLoader.java#L165) and the javabin update request codec in [`JavaBinUpdateRequestCodec.java`](https://github.com/nick-boss-tech/solr/blob/2e947b7f622c6c530350ecef3679b01e1b004cde/solr/solrj/src/java/org/apache/solr/client/solrj/request/JavaBinUpdateRequestCodec.java#L122)."

Other checks:
- Head: matches `ls-remote`. Full SHA in all links is the head.
- Proof: NamedListTest 6 test methods at head (L28, L37, L50, L64, L90, L117); `testToSolrParamsRejectsUnnamedEntry` at L37. Receipt: 6/6 at head, base fails exactly that test, `:solr:solrj:check -x test rc=0`. Consistent.
- Citations: base `toSolrParams` L349-356 (method at L349, no name check). Head `toSolrParams` L349-361 covers the method and the new check at L357-361 (SERVER_ERROR, message text matches the draft). `getAttr` DOMUtil.java L80-88 returns "" for `name=""`. All resolve and show what the text says.
- Choice: a live alternative, the config-reader check the ticket asks for. Its cost is stated correctly: it would reject unnamed entries in lists never used as parameters, and `arr` children are read without names (`DOMUtil.childNodesToList` L103-105; `addToNamedList` L128-155 at head). Ends with a question. CONSISTENT.
- Changelog: `changelog/unreleased/SOLR-4424-unnamed-config-param.yml` exists at head. The draft has no title line.
- Public text: no em dash, no process vocabulary, verification date present.

Optional notes, not blocking:
- Changelog says the entry was "silently dropped". The draft says base "stores it under a null name". Both describe the same effect. Pick one word if the two should match.
- The changelog title says "in solrconfig.xml", but the check also fires for javabin update requests (Limits covers that).
- Plain language: "arr element", "javabin" and "a list becomes parameters" are compressed terms.

## SOLR-5220

Verdict: DRIFT (1 item).

1. Draft says: "The run used the same source content as this head."
   - Evidence: receipt lines 4-5 say the gated tree was `7650584a756`, "verified byte-identical in content to the tip". `git cat-file -t 7650584a756` fails (not in the shared object store, `source/.git`). No commit on `origin/solr-5220-submit` has that tree: `a2c7b2af83f` (handoff doc) has tree `7926020f517...`, `e7c32c8d7c8` has `1debca30b919...`, the tip `e415c409044c` has `333d3a64e5cf...`. The receipt also says the tip differs from the gated history "only by the dropped handoff document", which would make the trees differ. The roll-up's receipt corrections say the gated tree must be re-recorded or the identity claim dropped. S4 disagreement 2 says the same.
   - Replacement: delete this bullet. The Proof line above it ("Run at head e415c409044c...") already names the head and the run date, and the receipt supports that the gate ran at the head commit.

Other checks:
- Head: matches `ls-remote`.
- Citations: base RETRY_CODES `404, 403, 503, 500` at L132-133 (`cabedd1d968`). Base zombie step L649-650, with the else branch that rethrows at L651-656. Head zombie step with the new check at L650-652 (`serverMayBeDown = e.code() != 403 && e.code() != 404`). `LBAsyncSolrClient` head L194-197 has the same zombie step for 403 and 404, unchanged. All match the text.
- Scope: only 403 and 404 change, not all 4xx (round S4 correction). The draft says 403 and 404. Consistent.
- Proof: `LBSolrClientClientErrorTest` L77-89 at head with the three methods `testNotFoundIsRetriedWithoutZombie` (L77), `testForbiddenIsRetriedWithoutZombie` (L82) and `testServiceUnavailableStillMarksZombie` (L87). Receipt: 3/3 at head, 2 of 3 fail on base. The draft names the two 4xx tests as the failing ones. The receipt gives only the count, so the names are deduced from the test code and the receipt's "two 4xx cases" wording. Consistent with the receipt.
- Neighbor counts and `solrj:check -x test rc=0` match the receipt.
- Choice: none. The assignment named none. The roll-up leaves "add a Choice for stopping the retries altogether" as an owner decision (S4 decision 10). Optional text if the owner adds one:

  **This change keeps the retry for 403 and 404 and only skips the zombie mark. The other route stops the retry too.**

  - Implemented here: a 403 or 404 moves the request to another server, and the first server stays in rotation.
  - The ticket's own proposal was to stop retrying 403 and 404 altogether ([SOLR-5220](https://issues.apache.org/jira/browse/SOLR-5220)).
  - Cost of that route: a comment on the ticket says stopping a server can make queries fail, and a server can answer 404 while it shuts down. A request that is not retried would fail instead of moving to another server.

  Was this the right call?

- Changelog: `changelog/unreleased/SOLR-5220-no-zombie-on-403-404.yml` exists at head. Title matches the scope.
- Public text: no em dash, no process vocabulary, verification date present ("run 2026-10-07").

Optional notes, not blocking:
- The Choice above is optional and depends on the owner's decision.
- "Alive check" and "zombie" are used without a plain gloss. Zombie is defined by context.

## SOLR-6046

Verdict: DRIFT (1 item). The draft text is consistent. The item is in the branch changelog, which the draft's title check covers.

1. Branch changelog title (`changelog/unreleased/SOLR-6046-xml-atomic-update-array.yml`, head 3b833e369a0b), not the draft: "SolrJ's XML update writer expands array values of atomic update operations (such as set with a String[]) instead of writing their toString()."
   - Evidence: the code expands `Object[]` only (`ClientUtils.java` L115-118, `v instanceof Object[]`). Primitive arrays still go through `toString()`, as the draft's Limits say. The title says "array values" without the Object restriction, so it overstates.
   - Replacement (branch changelog title): "SolrJ's XML update writer expands Object array values of atomic update operations (such as set with a String[]) instead of writing their toString()."

Other checks:
- Head: matches `ls-remote`.
- Base: Collection branch and single-value fallback at base `ClientUtils.java` L111-116 (`97d973814336`). Head Object[] branch at L115-118 and escapeCharData at L162. All match the text.
- XML loader: `XMLLoader.java` L381-398 at head merges repeated elements with the same field name and update key into a list. Matches "set receives both values".
- Escape routine: `escapeCharData` (ClientUtils L162) calls private `XML.escape` (base `XML.java` L75-77). The SOLR-4335 branch changes `escape` (adds the U+FFFE and U+FFFF branch). "That is the routine that SOLR-4335 changes" holds with that reading.
- Null element: at head, `writeVal` writes a `null="true"` field element for a null value under an update key (ClientUtils L170-173). The draft's null claim is right.
- Test: `ClientUtilsTest.testWriteXmlAtomicUpdateWithArrayValue` at L88-100 (head); the assertion checks `[Ljava.lang.String;` at L100. The class has 5 test methods at head (L37, L46, L88, L103, L119). Receipt: 5/5 at head, 1 of 5 fails on base.
- PR 5077 (draft cites SOLR-6045 as the pair, PR #5077). The receipt does not name it. The roll-up says it was unchecked. Checked read-only with `gh pr view 5077`: OPEN, title "SOLR-6045: atomic updates w/ solrj + BinaryRequestWriter aren't working when adding multiple fields w/ same name in a single SolrInputDocument", head branch `solr-6045-submit`. The number is right. Ticket snapshot supports the draft's "List works, binary writer works" and the String[] example.
- Choice: none. The assignment named none, and the primitive-array case is stated in Limits. CONSISTENT for the Choice part.
- Changelog link to the head file: exists.
- Public text: no em dash, no process vocabulary, verification date present.

Optional notes, not blocking:
- Plain language: "Object[]", "toString()" and "javabin" are compressed. Gloss on first use if the reader is not a Java developer.
- Length 4,210 characters, above the 3,500 guide. The owner decides.

## SOLR-7709

Verdict: CONSISTENT.

Checks:
- Head: matches `ls-remote`.
- Proof: `TestJavaBinCodec` 15/15 at head, including `testRepeatedFieldNamesKeepAllValues` (L340). Receipt: base fails exactly that test, expected `[value_1, value_2, value_3]` but was `[value_3]`. Counts 15 + 4 + 5 + 5 = 29 tests, as the draft says. Verified 2026-10-05 (reconciliation finish date). Module check passed (receipt).
- Citations: base `setField` at `JavaBinCodec.java` L734 and L804 (`c3cdf7b46e8c`). Head change at L734-739 (`readSolrDocument`) and L809-814 (`readSolrInputDocument`). Head `XMLLoader.java` L403-405 calls `doc.addField(currentFieldName, v)`. All match.
- Behavior: the draft states the change for senders that repeat a name, with the instruction to send each field once. Matches the code.
- Limits: only the two readers change (three files in the branch, per S1). Matches.
- Changelog: `changelog/unreleased/SOLR-7709-javabin-repeated-fields.yml` exists at head. Title matches the change.
- Choice: none. The ticket asks for XML parity, which merges. The behavior change is stated openly in "What this change does". No live alternative needed.
- Public text: no em dash, no process vocabulary, verification date present.

Optional notes, not blocking:
- The roll-up asks for a rebase onto `upstream/main` before submission. A rebase changes the head SHA and every link in this draft. Re-run this check after any rebase.
- The receipt's double ticket id in the fix commit subject is branch-side. It is not in the draft.
- "Javabin" and "stream order" are not defined in the draft.
- Length 3,021 characters (`wc -m`, UTF-8). The part report counted 2,982.

## SOLR-8536

Verdict: DRIFT (2 items: 1 draft sentence, 1 branch changelog title).

1. Draft says: "The string is used in the thread name without any change (base code, line 349)."
   - Evidence: base `ExecutorUtil.java` L349 (`14c7aac0d151`) is `String ctxStr = contextString.toString().replace("/", "//");`. That line changes the string (each `/` is doubled). The thread name is built from the result afterwards. Only the control-character handling is absent, so "without any change" is wrong.
   - Replacement: "The joined string is used in the thread name with only `/` doubled and no control characters removed (base code, line 349)."

2. Branch changelog title (`changelog/unreleased/SOLR-8536-mdc-thread-name-control-chars.yml`, head 28552ddc26eb), not the draft: "Thread names set by MDC aware executors no longer contain line breaks or other control characters coming from application MDC values."
   - Evidence: the code replaces `\p{Cntrl}` only, which is ASCII (U+0000 to U+001F and U+007F). The draft's Limits say U+0080 to U+009F, U+2028 and U+2029 pass through. The title overstates, as the roll-up flagged. Check 4 requires the title to match the code.
   - Replacement (branch changelog title): "Thread names set by MDC aware executors no longer contain ASCII control characters, such as line breaks, that come from application MDC values."

Other checks:
- Head: matches `ls-remote`.
- Citations: base join loop L339-343 (`14c7aac0d151`). Head replaceAll at L349-350, before the 512-character cut (`MAX_THREAD_NAME_LEN = 512` at L263). Head L341 `for (String value : values)` is where a non-String MDC value fails. Head test L48-65 (Javadoc L48, method L50-65). All match.
- Proof: `ExecutorUtilTest` 7 test methods at head (L50, L71, L107, L134, L247, L266, L299), 7/7 with the receipt. `StallDetectionTest` 13/13 (receipt). On base, 1 of 7 fails, `testMdcControlCharactersNotInThreadName` (receipt). Verified 2026-10-05.
- The test asserts `line1 line2 x`, no `\n`, no `\t`, which matches the draft's example.
- Choice: option 2 is the ticket's own suggested route. The ticket's description proposes a prefix check in `MDCAwareThreadPoolExecutor.execute`, so it is a live alternative. Matches the receipt's note. The draft ends with "Was this the right call?"
- Limits: the ClassCastException comment on the ticket reports SolrJ 7.7 with an Integer in MDC. Matches the snapshot.
- The ticket reports log lines split by line breaks in MDC values. Matches the snapshot description.
- Changelog file exists at head.
- Public text: no em dash, no process vocabulary, verification date present.

Optional notes, not blocking:
- "MDC" is not expanded (Mapped Diagnostic Context). Expand it once for plain language.
- Branch items the roll-up tracks, not draft text: the commit subject `2f631a960ae` says "strip" where the code replaces, and internal "handoff" commit subjects are in the PR commit list.
- Length 4,159 characters, above the 3,500 guide.

## Cross-draft notes

- Changelog links: all five link the fragment that exists at head. The fragment names carry a suffix (for example `SOLR-4424-unnamed-config-param.yml`), so they do not match the formula's example `SOLR-<ticket>.yml` pattern. Not flagged as DRIFT, because the links resolve to the real files. The owner decides whether to rename the fragments on the branches.
- Public text: no em dash (U+2014) and no en dash in any of the five drafts (byte count). No process vocabulary (gate, receipt, ledger, seeds, claim, pool, assignment, subagent, submission, round, owed, handoff, takeover, queue, premise, audit, pipeline). Each draft has a verification date.
- Emoji header: the template header carries an emoji. It is template-approved (`pr-formula.md`). No change.
- Lengths measured now with `wc -m` (UTF-8): 4424 4,045; 5220 3,539; 6046 4,210; 7709 3,021; 8536 4,159. The part reports list 3,999, 3,501, 4,169, 2,982 and 4,114. The drafts have not changed since `d7f234ecbe1`, so the part reports counted differently. Not a fidelity item.

## Not done

- No build, Gradle, test, fail-before or queue run (per brief). Proof claims are checked against receipts, and base-failure claims are not re-run.
- Gate logs named in the receipts (`g*-gate.log`) were not read. They are not in the worktree.
- SOLR-5220 gated tree `7650584a756` cannot be verified (object absent). Flagged as DRIFT, not resolved.
- SOLR-4424: which side calls the `JavaBinUpdateRequestCodec` unmarshal path (L122, L229) was not traced, so the "client's javabin update request" wording was checked against the code location only.
- Jira content was read from the read-only snapshots in the main checkout, not from live JIRA.
- Round parts S2, S5 and S6 were not read beyond the grep, since they do not mention these five tickets.
