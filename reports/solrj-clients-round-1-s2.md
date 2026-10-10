# SolrJ and clients round 1, part S2: document binding (SOLR-4422, SOLR-10364, SOLR-3999, SOLR-2018)

Drafts written: `pr-drafts/solrj/SOLR-4422.md`, `SOLR-3999.md`, `SOLR-2018.md`. No draft for SOLR-10364. Nothing committed, pushed, posted, built or tested. The folder also holds SOLR-3722, 4336, 4424 and 7709 drafts from other parts, which this part did not touch.

## Per ticket

**SOLR-4422: draftable.**
- Head `3e5b7afecddbe93eeea3b2cd468c2836091392a8`. `ls-remote` matches. The local object exists, and `origin/solr-4422-submit` points at it.
- Draft `pr-drafts/solrj/SOLR-4422.md`, 3,252 characters with links.
- The premise holds on read: base `DocumentObjectBinder` line 419 is `new HashMap<>()`, and head line 420 is `new LinkedHashMap<>()`. `SolrDocument` stores fields in a `LinkedHashMap` (base line 49). The test count is 5 on base and 6 at head, matching the receipt's 6 of 6.
- No Choices section, since the assignment names none. Limits carry the ticket's second suggestion (fill the bean's existing map), which the binder does not do: a map the bean's constructor filled is replaced when matching fields exist.

**SOLR-10364: audit only, no draft.**
- Head `502bdbf033faa648792372960d01c54728349ee9`. `ls-remote` matches the receipt.
- `DocumentObjectBinder.java` has blob `e22ada013c7` on `upstream/main` and at both merge-bases, so the branch diff applies to current main content.
- Main already binds `Collection`, `List`, `ArrayList` and arrays (base `cabedd1d968`, lines 288 to 297), and `Map` (line 304). It does not bind `Set`, `HashSet` or `LinkedHashSet`. The Jira error ("Can not set java.util.Set field ... to java.util.ArrayList") follows from reading the code. The title's "only List and Map" is therefore narrower than the code: `Collection` and arrays already work. `Set` is the real gap.
- The branch adds an `isSet` flag for `Set`, `HashSet` and `LinkedHashSet` (head lines 195 and 299 to 304). `inject` builds a `LinkedHashSet` (head lines 498 to 505). Child documents are rejected. The changelog title states the existing support correctly. The test file has 6 methods at head.
- Approach against the existing list handling: the same pattern (flag, type `Object`, no element conversion). Two differences: the `Set` path copies into a new `LinkedHashSet`, so duplicates drop silently while the list path keeps them; and `TreeSet`, `SortedSet` and `EnumSet` still fail with the same error.
- Unhandled gap: `Map<String, Set<String>>` dynamic fields. Base lines 329 to 338 accept any `ParameterizedType` value and fall through with type `Object`, so the map gets raw `List` or `String` values, not `Set`s. The branch leaves this alone (its `isSet` path is guarded by `!isContainedInMap`). No test covers it.
- Stray file: `SOLR-10364-TESTING.md` at the repo root of the branch. It says "nothing was compiled or run" and must not ship.
- What a gate would need: (a) remove the stray file; (b) a premise run on main: `testSetFields` must fail on base with `BindingException` wrapping `IllegalArgumentException` (the branch note's guess, not yet run); (c) a focused `TestDocumentObjectBinder` run at head with the formatting check and fail-before, as for other gated branches; (d) a decision on `Map<String, Set<String>>` (handle it, or name it in Limits); (e) a decision on `TreeSet` and `SortedSet` scope; (f) a rebase after 4422 (see the interaction below).

**SOLR-3999: draftable.**
- Head `e3af6f36e312`. `ls-remote` matches.
- Draft `pr-drafts/solrj/SOLR-3999.md`, 3,712 characters.
- The draft states that the declaration fixes the number only for builds that declare it. Streams from earlier builds carry the computed number and are still rejected, including the ticket's own case. Later builds must keep `1L`. Field-layout compatibility is not checked. Values inside a document keep their own version numbers. `SolrDocumentList` (an `ArrayList` subclass with no number) is not covered.
- Verified at base `b6b2b8f10e9`: none of the four classes declared a number before.

**SOLR-2018: draftable, with a scope correction to the assignment.**
- Head `213457aa91fd6d4809094d80dd9c8d312c0a6d3b`. `ls-remote` matches.
- Draft `pr-drafts/solrj/SOLR-2018.md`, 3,783 characters.
- The branch is not one clarification in `UpdateRequest`. The diff is eight `@param waitFlush` lines in `SolrClient.java` (the commit and optimize overloads), one line in the commit stream reference (`stream-decorator-reference.adoc` line 446), and a changelog file. `UpdateRequest.java` is not in the diff.
- Behavior checked by reading: SolrJ never sends `waitFlush`. `AbstractUpdateRequest.setAction` (base lines 53 to 64) sets only commit or optimize, `softCommit`, `maxSegments` and `waitSearcher`. No server code reads it; the only mention is a comment at `RequestHandlerUtils.java` line 50. The draft says the flag is ignored and that the PR documents this. It does not claim a fix.

## Interaction on the binder (SOLR-4422 and SOLR-10364)

- Both branches edit `DocumentObjectBinder.java`. 4422 changes `getFieldValue` (head line 420). 10364 changes `storeType` (head lines 299 to 304), adds `isSet`, changes `inject` (head lines 498 to 505), and adds imports.
- Trial three-way merge (`git merge-file`, read-only, in the scratchpad, base = main's binder `e22ada013c7`): one textual conflict, in the import block. Both branches add lines directly after `import java.util.HashMap;` (4422 adds `LinkedHashMap`; 10364 adds `HashSet` and `LinkedHashSet`). Every other hunk merges cleanly, including the two test files.
- No logic overlap: the `isSet` path sits in `inject` behind `!isContainedInMap`, and 4422's change sits in `getFieldValue`.
- Landing order: 4422 first; 10364 rebases and keeps both import sets. This was a text merge only, not a compile.

## Self-check

- Dashes (em or en): zero in all three drafts.
- Process words (gate, receipt, ledger, rc=0, JUnit, pre-fix, owed, round, pipeline, handoff, audit, takeover, hardening, fresh, claim, spotless, tidy, Error Prone, lint): none in the drafts.
- Head references: 4422 links use `nick-boss-tech` at `3e5b7afecddbe93eeea3b2cd468c2836091392a8`. Base links use `apache/solr` at the merge-base `97d973814336101e12475558d7419321c743de79`, labelled base. 3999 uses `e3af6f36e312` (head) and `b6b2b8f10e9827e3b86e44649fb7966ce646c185` (base). 2018 uses `213457aa91fd6d4809094d80dd9c8d312c0a6d3b` (head) and `cabedd1d968059215188f4e7563fb303241899ed` (base).
- Lengths: 4422 at 3,252, 3999 at 3,712, 2018 at 3,783. The last two exceed the roughly 3,500 guide with links counted.
- Changelog link: read as the blob link at the head SHA, placed after Limits as in the template and in the 15478 precedent. The lead should confirm that reading of "changelog link at the head".

## Receipt disagreements

1. **SOLR-2018 receipt:** "The change is one javadoc clarification only (in `UpdateRequest`'s waitFlush documentation: waitFlush is about flushing the data, not visibility)." The assignment's starting-state entry also says "one javadoc clarification on waitFlush in UpdateRequest". The diff shows `SolrClient.java` (eight lines), one ref-guide line and a changelog. `UpdateRequest.java` is untouched. "waitFlush is about flushing the data" describes the old javadoc; the code never reads the flag.
2. **SOLR-3999 receipt:** "The new test constructs each class, requires the field to be present, and checks it equals `1L`." `testExplicitSerialVersionUid` uses `getDeclaredField` and `ObjectStreamClass.lookup` and creates no instances. Only the round-trip test constructs an object (a `SolrInputDocument`).
3. **SOLR-3999 receipt:** "on base the new test fails exactly 1 time with NoSuchFieldException." The class has two test methods, and the receipt does not name the failing one. From the code it is `testExplicitSerialVersionUid`. The receipt should name it.
4. **SOLR-4422 receipt:** "the two changes are in different methods." True for the method bodies. The import block collides when both are applied (the text conflict above).
5. **SOLR-2018 receipt:** "GitHub corroboration: run 37551553299 SUCCESS." Not verified (see Not checked).

## Owner decisions

1. **SOLR-10364:** first decide whether a `Set`-support enhancement with one reporter is worth a PR (the Jira comment calls it "one feature enhancements"). If yes, recommend bean `Set` fields only, with `Map<String, Set>` named in Limits, then the gate. Either way, remove `SOLR-10364-TESTING.md`.
2. **SOLR-2018:** the ref-guide line still reads "The value passed to the commit handler (...). Solr ignores this option." The value is not passed on. Amend that line on the branch before posting (needs a commit, not done here), or accept the wording. Recommendation: keep the four `SolrClient` notes (lines 409, 429, 543 and 560) and the `RequestHandlerUtils` comment out of scope, as the draft's Limits say.
3. **SOLR-4422:** keep the ticket's "fill the existing map" alternative in Limits (as drafted, since no Choice was named), or promote it to a Choice. Recommendation: Limits.
4. **SOLR-3999:** the changelog title says "so Java-serialized documents survive recompiles", which reads wider than the draft's Limits. Recommendation: align the changelog wording. Also confirm the PR is still worth submitting as a forward-only fix, since the reporter's case is not fixed.
5. **Landing order:** 4422 before 10364.

## Not checked

- No build, test, Gradle, `gh` call or JIRA call. The Jira context came from `research/jira-context` JSON snapshots dated 2026-10-04, which may be stale.
- The 10364 premise (`BindingException` on base) is from reading the code, not a run.
- The 2018 CI run `37551553299` was not looked up.
- The gate logs (`g4422-gate.log`, `g3999-gate2.log`, `g2018-gate.log`) were not read. The test dates in the drafts come from the receipts' round records dated 2026-10-06. No run timestamps exist in the receipts.
- The trial merge was text only. Nothing was compiled.
- Ref heads were checked with `ls-remote` and the existing origin tracking refs. No fetch was needed.
