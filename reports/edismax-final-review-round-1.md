# eDisMax final review, round 1

- Slug: pool-edismax-final-review-round-1
- Assignment: `assignments/pool-edismax-final-review-round-1.md` (origin/pr-prepare)
- Claim: `claims/pool-edismax-final-review-round-1.md` (started 2026-10-15T15:35Z)
- Reviewer: windows review agent, review lane, round 1 (2026-10-15)
- Status: PARTIAL. Two tickets are SATISFIED (READY TO OPEN). One is satisfied on its draft and opens after SOLR-3729. Seven tickets still have a branch patch and gate owed, an owner item open, or both. The branch patches could not be pushed in this session because the permission to create the ticket worktree was refused (section 5).
- Nothing was built, tested, or gated. Nothing was posted. No branch, PR, JIRA item, or live body was touched.

## 1. Verdict per ticket

| Ticket | Live head (ls-remote, 2026-10-15) | Draft this round | Verdict |
|---|---|---|---|
| SOLR-2309 | `06f5a1c4a87` | three wording fixes | SATISFIED (READY TO OPEN). Keep both blocks at the 2988 and 12092 conflicts (section 6). |
| SOLR-2988 | `d2d144dfd9f` | two wording fixes (base named) | SATISFIED (READY TO OPEN). Keep both blocks at the 2309 conflict. Optional comment nit not patched (D8). |
| SOLR-3243 | `1db99c13662` | "A choice to check" removed (decided, D1); negated form stated | NOT SATISFIED. Owed: patch P1 (negated test and changelog title) and its gate. |
| SOLR-3729 | `0fe7e503945` | unchanged; its Choice stays (open owner call) | NOT SATISFIED. Owner item O1. Owed: patch P2 (two comment defects) and its gate. |
| SOLR-3962 | `e7d5f350503` | dependency on SOLR-3729 stated; early-return limit added | SATISFIED on the draft. Opens after SOLR-3729 lands. Reconcile to 3729's helper and its re-gate are owed then. |
| SOLR-6009 | `a41bb034a1f` | unchanged | NOT SATISFIED. Owed: patch P3 (drop "(or errored)") and its gate. Owner item O4 (history). |
| SOLR-6320 | `cb710c96353` | unchanged | NOT SATISFIED. Owed: patch P4 (two test comment blocks) and its gate. Owner items O2 (mixed-case neighbours) and O5 (history subject). |
| SOLR-7120 | `4664014a919` | status sentence added; verified date and base named | Owner item O6 (history subjects say "handoff"). The draft is otherwise SATISFIED. The head tree has no handoff note. |
| SOLR-12092 | `ca9573dabd3` | review flag on the index-side sentence | NOT SATISFIED. Owed: patch P5 (changelog title, guide line) and its gate; the flagged sentence stays flagged until then. Owner item O3 (history). |
| SOLR-14913 | `b80221f46d3` | "A choice to check" replaced by a stated decision (D3) | NOT SATISFIED. Owed: patch P6 (changelog title, compound-query assertion) and its gate. |

Count: SATISFIED (READY TO OPEN) 2 (2309, 2988). SATISFIED on draft, sequenced after 3729: 1 (3962). Owner item only, draft satisfied: 1 (7120). Branch patch and gate owed: 6 (3243, 3729, 6009, 6320, 12092, 14913).

## 2. Heads and bases

- All ten live heads were read with `git ls-remote origin refs/heads/solr-NNNN-submit` on 2026-10-15. Each matches the claim table and the receipt named in its draft.
- Bases (merge-base with `upstream/main`): 2309, 2988, 3243, 3729, 3962, 6320: `97d973814336`. 6009 and 12092: `14c7aac0d151`. 7120: `cabedd1d968`. 14913: `b5c71bc5573`.
- The round 28 review of SOLR-2988 names `97d973814336` as its comparison base (`research/branch-reviews/round-28/SOLR-2988-review.md`, line 7). The 2988 premise base is taken from that record, because the receipt names no base.
- Every changelog path the drafts cite exists at its live head.
- The 7120 head tree has no `SOLR-7120-TESTING.md`; the packaging commit removed it, as the receipt says.
- The 7120 code path was checked at head: `ExtendedDismaxQParser.java` line 359-360 builds `ExtendedDismaxConfiguration(localParams, params, req)` for every parser instance, and line 1733 holds the `BAD_REQUEST` wrap. The local-parameter claim in the draft follows from that.

## 3. Wording fixes applied (pool worktree, not committed)

1. `pr-drafts/edismax/SOLR-2309.md`: "We did not check prefix and wildcard terms" is now "Prefix and wildcard terms are not checked." The follow-up sentence and the cost sentence no longer use "we". The Proof names the base `97d973814336` (merge-base).
2. `pr-drafts/edismax/SOLR-2988.md`: the field-type limit no longer uses "We did not add one" or "we will make it". The Proof names the base `97d973814336`.
3. `pr-drafts/edismax/SOLR-3243.md`: "A choice to check" section removed, because the widening is recorded as intended (D1). "What this change does" now says that a document with no value in any qf field now matches, and that this is a compatibility change. The negated form stays in Limits.
4. `pr-drafts/edismax/SOLR-3962.md`: "What this change does" now says the change should land after SOLR-3729 and then reuses that change's match all check. Limits gains a bullet: with word-gram 0 the phrase step stops early when no phrase clause is left, which also covers fielded-only queries, and no test covers that shape.
5. `pr-drafts/edismax/SOLR-7120.md`: the Proof states the verification date (2026-10-09, from the receipt) and names the base `cabedd1d968`. "What this change does" states that the HTTP status changes from 500 to 400.
6. `pr-drafts/edismax/SOLR-12092.md`: an HTML review flag follows the sentence "The reference guide and the changelog both state this." The flag says to keep the sentence only after patch P5 is gated, and to remove the comment before posting. The sentence is not true at `ca9573dabd3` yet.
7. `pr-drafts/edismax/SOLR-14913.md`: "A choice to check" replaced by "Why an alias with no usable target matches nothing", a stated decision (D3).

Not changed: SOLR-3729, SOLR-6009, SOLR-6320. Their remaining items are branch-side. Their Proof lines will name a new head once the patches are gated.

Checks on the ten drafts: no em dash and no en dash; no "we", "our", or "us" left; no process words (handoff, gate, receipt, takeover, hypothetical) left outside the SOLR-12092 review flag. Length: round 3 part e4 measured SOLR-12092 at about 3,875 characters and SOLR-14913 at about 3,921, above the 3,500 guide, about a third of each being link URLs. Not re-measured or changed here.

## 4. Decisions (decision default and three-rung ladder)

- D1. SOLR-3243 widening (an unfielded `[* TO *]` means every document). The owner's position is on record as intended (round 3 record). Decided: keep. The Choice section is removed and the statement stays.
- D2. SOLR-3243 negated form (`-[* TO *]` now excludes every document). No position on record. Default: keep the consequence, which matches `-*`. Stated in Limits. Patch P1 pins it with a test.
- D3. SOLR-14913 all-missing alias returns `MatchNoDocsQuery`. Recommendation on record: keep. Decided: keep. The draft states it and the question form is removed. Owner ratification is not needed for the draft to stand.
- D4. SOLR-12092 index-side rule (a managed filter in the index analyzer now also makes `stopwords=false` a no-op for the query analyzer). No record. Default, harder and more complete option: keep. It is implemented and tested. The Choice stays in the draft, because the route changes behaviour for indexes with a managed filter and a maintainer could reject it (pr-formula bar for a choice).
- D5. SOLR-14913 compound-query assertion (round 28 item M2, still open). Cost is effort, so take the harder option: add the assertion (patch P6), with a fail-before leg.
- D6. SOLR-3729 broad or narrow match-all. Still an owner call on the record, which reserves it. Not decided here. Owner item O1. The draft keeps its Choice, as the round 3 record directs.
- D7. SOLR-6320 finding 1 remedy (mixed-case neighbours). Still the owner's call on the record. Owner item O2. The Choice stays.
- D8. SOLR-2988 comment at `ExtendedDismaxQParser.java` line 1473 ("so it does match >1 words"). Awkward, not wrong. Optional, not patched, so SOLR-2988 is not held for it.
- D9. SOLR-3729 javadoc (narrower than the code) and test comment (wrong for `*:*^abc`). These are defects in the branch's own comments. Patch P2.
- D10. SOLR-12092 changelog title names only the query side. The index-side rule changes behaviour, so the title must say it. Patch P5, using the wording from round 3 part e4, adjusted so the title says the parameter has no effect when the index analyzer has a stop filter.
- D11. SOLR-6009 changelog "(or errored)" has no evidence (round 3 part e3). Patch P3.
- D12. SOLR-3962 reconcile to SOLR-3729's helper. The assignment defers it until SOLR-3729 lands. Not a decision of this round.
- D13. History rewrites (squashes, trailer removal) are not review-side work under the assignment. They are recorded as owner items O3 to O6 and not performed.

## 5. Branch patches owed (not pushed)

Blocked: `research\branch-work.ps1 new SOLR-NNNN`, the wrapper that creates the ticket worktree, was refused by the auto-mode permission classifier on 2026-10-15 under "Irreversible Local Destruction". I did not retry the same operation by another route. Nothing was created in `source\` or on the fork. Without a ticket worktree I cannot commit or push P1 to P6 below. Once permission is granted, a host with rights runs the wrapper, applies each patch, pushes fast-forward (never force), and posts the gate job listed under each patch.

The texts below were read from the live heads with `git show` and checked against the cited line numbers. They were not compiled or run through tidy. Javadoc and Java comment lines must be re-flowed to 100 columns the way google-java-format does before the push, because the gate's tidy step fails the run if it changes a file.

**P1. SOLR-3243, branch `solr-3243-submit`, head `1db99c13662`.**
- Changelog `changelog/unreleased/SOLR-3243-edismax-unfielded-range.yml`, line 2. Replace the title with:
  `title: edismax treats an unfielded inclusive range [* TO *] as a match all docs query instead of expanding it over every qf field. Documents with no value in any qf field now match, and a negated -[* TO *] now excludes every document.`
- `solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java`, insert after `testUnfieldedOpenRangeMatchesDocWithoutQfValue` (ends at line 216):
```java
  /**
   * SOLR-3243: a negated unfielded open range excludes every document, as a negated bare * does.
   */
  @Test
  public void testNegatedUnfieldedOpenRangeExcludesEveryDocument() throws Exception {
    for (String sow : Arrays.asList("true", "false")) {
      assertJQ(
          req("defType", "edismax", "q", "*:* -[* TO *]", "qf", "name title", "sow", sow),
          "/response/numFound==0");
    }
  }
```
- Expected: fails on the base parser (doc 46 has neither a name nor a title, so base returns it, `numFound` above 0) and passes at head. The fixture doc 46 is the one `testUnfieldedOpenRangeMatchesDocWithoutQfValue` already uses.
- Gate: changelog parse; tidy; Error Prone compile; focused `TestExtendedDismaxParser` (expect 43 of 43); proof leg with the parser reverted to base, expected failure is the new test with `numFound` above 0; module check.

**P2. SOLR-3729, branch `solr-3729-submit`, head `0fe7e503945`. Comments only.**
- `solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java`, javadoc of `isStandaloneMatchAll` (the paragraph at lines 738-741; the `/**` and `*/` lines stay). Replace the paragraph with:
  `Whether a clause is a standalone "match all" query: a {@code *:*} term with any leading {@code (}, {@code +}, or {@code -} characters, then any closing {@code )} characters and an optional plain-number boost. Parentheses are not counted, so an unbalanced clause such as {@code (*:*} is accepted; the grammar then rejects it and the query falls back to the escaped parse. A {@code *:*} glued to other text, such as {@code foo(*:*)bar}, is not standalone.`
- `solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java`, lines 207-209 (comment above the `*:*^abc` loop). Replace the three lines with:
  `// a malformed boost makes the helper reject the clause, so its colon is escaped; extra closing` / `// parens are accepted by the helper and then rejected by the grammar. Either way the query` / `// parses without throwing`
- Gate: changelog parse; tidy; Error Prone compile; focused `TestExtendedDismaxParser` (expect 41 of 41); module check. No proof leg (comment-only).

**P3. SOLR-6009, branch `solr-6009-submit`, head `a41bb034a1f`.**
- Changelog `changelog/unreleased/SOLR-6009-edismax-regexp.yml`, lines 4-5 (folded title). Replace them with:
  `  regular expression clause previously matched no documents; it now matches the same way` / `  a fielded regular expression clause does.`
- Gate: changelog parse; focused `TestExtendedDismaxParser` (expect 41 of 41); module check. Title-only changelog fix, per the existing handling.

**P4. SOLR-6320, branch `solr-6320-submit`, head `cb710c96353`. Test file comments only.**
- `solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java`, lines 336-338 (javadoc of `testLowercaseOperatorBoundaryPins`). Replace with:
  `promotion candidate. In "Zapp and and Brannigan" the chained lowercase operators have no` / `explicit operator neighbor. Both still promote, the rebuilt query is invalid, and the escaped` / `form is used; that shape is outside this ticket's scope.`
- Same file, delete lines 341-342 (the `// TODO` line and its continuation). Line 343 stays.
- Same file, javadoc of `testLowercaseOperatorDemotedOrShapes`, lines 395-402. Replace with:
  `foundOperators must agree with the demotion rule and not count the word as an operator, or it would switch off the configured mm for a query that contains no OR. These strings pin the demotion rule. The mm check does not change them: under q.op=AND every clause is required, so mm has no optional clause to act on, and under q.op=OR the derived mm is already 0%.`
- Gate: tidy; Error Prone compile; focused `TestExtendedDismaxParser` (expect 44 of 44); module check. No proof leg (comment-only). Because a test comment changes, the gate also confirms the test count is unchanged.

**P5. SOLR-12092, branch `solr-12092-submit`, head `ca9573dabd3`.**
- Changelog `changelog/unreleased/SOLR-12092-edismax-managed-stopwords.yml`, lines 2-3 (folded title). Replace with:
  `  The edismax parameter stopwords=false now also keeps the words removed by a` / `  ManagedStopFilterFactory in the query analyzer, not only those removed by StopFilterFactory.` / `  The parameter has no effect when the index analyzer has a stop filter of either kind.`
- `solr/solr-ref-guide/modules/query-guide/pages/edismax-query-parser.adoc`, line 110. After the sentence "If this is set to `false`, then the stop filter in the query analyzer is ignored." add: "If the index analyzer also has a stop filter, this parameter has no effect."
- Gate: changelog parse; tidy; Error Prone compile; focused `TestManagedStopFilterFactory` (5 of 5) and `TestExtendedDismaxParser` (39 of 39), and the other seven classes in the receipt's focused run; module check. The `.adoc` change is not docs-only under the 2026-10-12 rule (only `.md` qualifies), so the full gate applies.
- When gated, the SOLR-12092 draft's flagged sentence becomes true at the new head, and the Proof and Changelog links move to that head.

**P6. SOLR-14913, branch `solr-14913-submit`, head `b80221f46d3`.**
- Changelog `changelog/unreleased/SOLR-14913.yml`, line 1. Replace the title with:
  `title: edismax field aliases now skip targets that are missing from the schema instead of re-parsing the query with escaped characters; an alias with no usable target matches nothing`
- `solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java`, in `testAliasingWithNonSchemaField` (starts at line 953), insert after the last `nor` assertion:
```java
    assertQ(
        req(
            "defType",
            "edismax",
            "q",
            "myalias:Zapp AND name:Zapp",
            "f.myalias.qf",
            "nosuchfield"),
        nor);
```
  The one-line form is 101 columns with the indent, so the arguments go one per line, as the existing calls in this method do.
- Expected: the new assertion fails on the base parser (base returns the `name:Zapp` document for the compound query) and passes at head (receipt, round 33: 1 document on base, 0 at head).
- Gate: changelog parse; tidy; Error Prone compile; focused `TestExtendedDismaxParser` (expect 40 of 40 at the new head); proof leg with the parser reverted to base, expected failure is the new assertion; module check.

## 6. Landing order

From the round 3 record (parts e1 to e5), restated. Trial merges were not re-run this round.

1. SOLR-3729 first in its family. Then SOLR-3962, after its reconcile to 3729's helper (swap to `isStandaloneMatchAll(clause.raw)`, delete the old helper, add the `(+*:*)` and `(-*:*)` spellings to its test list). SOLR-3243 at any point: its hunks do not overlap 3729 or 3962, and its tests join the same anchor.
2. SOLR-6009 before SOLR-6320. The two touch separate methods in the parser and conflict at one anchor in the test file (base lines 259-261). Keep both test blocks, 6009 first. SOLR-6320 absorbs the reconcile.
3. SOLR-12092 before SOLR-2309. One insertion point after `getQuery()`. Keep both methods and both import sets (2309: `java.io.IOException`, `TokenStream`, `TextField`; 12092: `ManagedStopFilterFactory`).
4. SOLR-2309 and SOLR-2988 conflict at one insertion point. Keep both blocks: 2309's `analysisYieldsNoTokens` first, then 2988's `containsWhitespace`. Whichever lands second makes that edit.
5. SOLR-14913 merges cleanly with 2309, 3243, 6009 and 12092. Its new test sits near SOLR-7120's test; check the order of the two methods in the merged file.
6. SOLR-7120 merges cleanly with the family.

## 7. Owner items (not decided here)

- O1. SOLR-3729: broad or narrow match-all set. Owner call open on the record. The draft's Choice poses it. Broad keeps `-*:*` and `*:*^2` changed, narrow leaves them escaped.
- O2. SOLR-6320 finding 1: mixed-case `And`, `Or`, `NOT` as explicit neighbours, or the implemented exact-uppercase rule. Owner call open on the record. The draft's Choice poses it.
- O3. SOLR-12092 history. Squash the six commits above `upstream/main` into one with subject `SOLR-12092: edismax stopwords=false also keeps ManagedStopFilterFactory words` and no trailer. Commits `cde23f0a9d7`, `b989934d4ea` and `3a54f4f3fdf` carry `Co-Authored-By` Claude trailers (round 3 part e4; my count agrees: three commits). `3a54f4f3fdf` and `c171ae986d7` have "handoff" in the subject. This rewrites the fork branch, so it needs Nick's go-ahead.
- O4. SOLR-6009 history. `8b17fca972a` ("add hypothetical-reproduction handoff doc") and `b2976bdc7dc` ("remove the hypothetical-reproduction handoff doc before submission") net to nothing. Drop or squash them. New head; the receipt and the draft Proof then name it. Needs go-ahead.
- O5. SOLR-6320 history. `cb710c96353` has the subject "record mm alignment re-check". Reword when history is cleaned. New head. Needs go-ahead.
- O6. SOLR-7120 history. `6a434a7fc3d` ("add hypothetical-reproduction handoff doc") and `4664014a919` ("remove handoff doc") leave a clean tree but show process words in the PR's commit list. Squash to one clean commit. New head, so the receipt must be refreshed. Needs go-ahead.

## 8. Pending approval and public writes

- No GitHub comment, review, PR body edit, PR state change, JIRA post, or mail was made. None is drafted for posting in this round.
- O1 to O6 above. O1 and O2 are Nick's calls. O3 to O6 are fork history rewrites.
- O7 (blocker, not an owner decision): permission for `research\branch-work.ps1 new SOLR-NNNN` (and then the commit and push wrappers), so that P1 to P6 can be pushed. Until then none of the six gate jobs can be posted, because each names a patched head that does not exist yet.
- The SOLR-12092 review flag must be removed before that draft is posted.

## 9. Not verified, and why

- No build, test, Gradle, or gate run (hard rule). Patch texts in section 5 are unverified against tidy, the compile, and the tests.
- The gate logs named in the receipts (for example `g3243r32-gate.log`) are not on disk. Proof counts are taken from the receipts only.
- Live JIRA was not read. Jira text comes from the packets and round 3 reports.
- Trial merges and the landing order are restated from round 3 parts e1 to e5. They were not re-run.
- The 2988 premise base comes from the round 28 review, not from the round 32 gate log (not on disk).
- Lucene 9.x and 10.x: no in-scope draft names Lucene behaviour.
- The claim file was not updated, so its last heartbeat is 15:35Z. The coordinator should record this round as PARTIAL.
