# Core admin answers round 1, part c2 (items 3 to 8)

Result: items 3 to 8 applied in place to the seven named drafts. No DISCUSS item decided, no head changed, nothing committed, no builds, no tests, no posts.

## Changes

### SOLR-17297.md (items 3 and 6)

- Item 6, inside "What this change does": removed the line `- Changelog: [changelog/unreleased/SOLR-17297-modules-before-shared-lib.yml](https://github.com/nick-boss-tech/solr/blob/c0ab38fc0a8b57953f8b8df03f54085fb37e138d/changelog/unreleased/SOLR-17297-modules-before-shared-lib.yml)`. The closing Changelog line stays.
- Item 3, Proof, first placeholder. Old: `- On the base code, that test fails with: [PASTE THE BASE FAILURE LINE BEFORE POSTING]`. New: `- On the base code, that test fails. A run on the base code confirmed it.` Basis: receipt line 7, "Proof: PASS (pre-fix proof step in the gate)". The failure line is in `g17297-harden.log`, which is not on disk.
- Item 3, Proof, second placeholder. Old: `- [CONFIRM BEFORE POSTING: the reverse-order check result at this head, see the choice below]`. New: `- A test for the reverse order was run at this head, and it fails. It is not part of this change.` Basis: receipt line 8, reverse order "CONFIRMED BY RUN ... at this head"; the probe was not committed.

### SOLR-17377.md (item 3)

- Proof, placeholder. Old: `- On the base code, the failure reads: [PASTE THE BASE FAILURE LINE BEFORE POSTING]`. New: `- On the base code, \`TestSolrXml\` runs 35 tests with exactly 1 failure, and that failure is the new test.` Basis: receipt line 7 (base production with head tests: TestSolrXml 35 tests, exactly 1 failure). `g17377-premise.log` is not on disk.

### SOLR-15805.md (item 4)

- Proof summary line. Old: `**The new test fails on the base code and passes with this change.**` New: `**The new test passes with this change. By code reading, it fails on the base code.**` Judgment call: the bold line states the base failure as fact, so it changed with the sentence below. The claim names only the sentence; the lead may revert this line.
- Proof body, last sentence. Old: `On the base code the startup exception is logged and not rethrown, so the expected exception never appears and the first assertion fails.` New: `The base failure is read from the code, so its failure text is not quoted here. On the base code the startup exception is logged and not rethrown, so the expected exception never appears and the first assertion fails.`

### SOLR-8576.md (item 5)

- Added at the end of Proof, before "## Limits": `[OWED BEFORE POSTING: the new test's alias check needs a fix on the branch first. After that edit and a run of \`CollectionsAPISolrJTest\` at the new head, update the head, the counts, and the links in this draft.]`
- Head, counts, and links unchanged.

### SOLR-17731.md (item 5)

- Added at the end of Proof, before "## A choice to check": `[OWED BEFORE POSTING: the branch needs its fixes first: the two license header lines, the two comments on the Jersey routing rule, and the changelog title. Then run \`ListAliasesAPITest\` at the new head. After that, update the head in the links and in this Proof.]`
- Head and links unchanged.

### SOLR-17708.md (item 5)

- Added after the Counts line in Proof, before "## Limits": `[OWED BEFORE POSTING: the changelog title on the branch overstates the change and needs a fix first. After that edit, update the head references in this draft, and run the three test classes named above again at the new head.]`
- Head references unchanged.

### SOLR-15024.md (item 7)

- Added under "What happens today": `[OWED BEFORE POSTING: check that the Jira ticket describes duplicate char filter keys in the Luke output. The ticket text is not on hand, so this section rests on the code. If the ticket describes only the Admin UI symptom, the summary line changes.]`
- Summary line unchanged. The draft has no owner list, so the owed note sits in the body as a bracketed line. The lead may move it.

### Edits by part c1 seen during this pass (not mine)

Part c1 edited several of the same files while I worked. The Edit tool reported on-disk changes for SOLR-17297 and SOLR-17377 before my edits; my edits applied to the current text. On re-read I saw c1's Limits openers (17297, 17377, 8576), the removed INTERNAL blocks (8576, 17731), and the Choice opener in 15024. I did not touch those lines.

## Not done

- No DISCUSS item decided. No head reference changed (item 5 is notes only).
- SOLR-17377 carries the same duplicate changelog bullet that item 6 removed from SOLR-17297: `- Changelog: [changelog/unreleased/SOLR-17377.yml](...)` inside "What this change does", line 18. Item 6 names only SOLR-17297, so I left it for the lead.
- SOLR-15805: the receipt records "Proof: PASS (pre-fix proof step in the hardening run)". That is a recorded fail-before verdict, not failure text. I followed the claim and reworded to a code reading. The lead may decide the verdict supports a recorded outcome instead.
- SOLR-17297: the reverse-order result now appears in Proof (line 24) and again in the Choice (line 35). I left the Choice text alone because it is outside items 3 to 8.
- The four OWED markers (SOLR-8576, SOLR-17731, SOLR-17708, SOLR-15024) are bracketed placeholders. They must be removed before posting. I did not post or remove them.
- Items 1 and 2 belong to part c1 and were not done here.

## Placeholders left and why

- OWED markers in 8576, 17731, 17708, and 15024. Each names work that is still on the branch or in the ticket text (fixes, a test run, the ticket check). They cannot be filled until that work is done.
- No other bracketed placeholders remain in the seven drafts. The PASTE and CONFIRM placeholders were replaced with the recorded outcome.

## Checks

- Dashes: a byte scan of all 17 drafts in `pr-drafts/core-admin/` found no em dash and no en dash.
- Internal words, all 17 drafts: scanned for gate, receipt, ledger, JUnit XML, rc=0, pre-fix, harden, tidy, takeover, claim, handoff, round, lane, owner, main side, review side, live tip, premise, probe, queue, drain, pipeline, TESTING, audit, dispositioned, negative control, planted, Claude, and Muse. Remaining hits are: the four new OWED markers (removed before posting); "Verified <date>" stamps; the test class `AuditLoggerIntegrationTest` (a class name); "on main" (the upstream branch name); and "By code reading" (plain words).
- Heads: all 17 drafts name their head as a full 40-character SHA in their links. SOLR-11939 names `d4cff5e76430...`, and SOLR-17708 names `10a7fa07a79ac...`. The seven edited drafts keep their heads: 17297 `c0ab38fc0a8b`, 17377 `22b5f209a11a`, 15805 `3432f950f0ae`, 8576 `4c46f95c7851`, 17731 `f2b4ba164f56`, 17708 `10a7fa07a79`, and 15024 `f95b5010b3fe`.
- Proof numbers: the one new number (17377, TestSolrXml 35 tests, exactly 1 failure) matches its receipt. No other number changed.
- The checks ran after my last edit. Part c1 may still be editing, so re-run the dash, vocabulary, and head checks once c1 finishes.
- Not run: builds, tests, `gh` write calls, posting, commits.
