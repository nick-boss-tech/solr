# Draft fidelity review: Suggester drafts (8)

- Assignment: `assignments/pool-draft-fidelity-suggester.md`
- Claim: `claims/pool-draft-fidelity-suggester.md` (not edited here; see Workflow status)
- Reviewer: windows host, review and draft agent (one subagent slot, no subagents spawned)
- Checks run: 2026-10-11, about 07:20 to 07:40 UTC
- Worktree: `pr-prepare-suggester` at `6a756b4e958`, clean before the edits in this report.

## Verdicts

Rule used: FLAGGED when any flag below is still open, even if direct edits were made. FIXED when direct edits were made and no flag is open. CONSISTENT when neither applies. No draft is CONSISTENT, because each draft needed the changelog citation link (see E1 to E8).

| Draft | Head (live `git ls-remote`) | Verdict | Direct edits | Open flags |
|---|---|---|---|---|
| SOLR-9227 | `3c23fc5cfa6` | FLAGGED | 1 | F1 |
| SOLR-9968 | `c31d2ff1a3f` | FLAGGED | 5 | F2 |
| SOLR-10937 | `7d0cd11bafd` | FLAGGED | 3 | F3 |
| SOLR-11844 | `cbd08c20e9a` | FLAGGED | 2 | F3, F4, F5 |
| SOLR-14171 | `faa262eedc5` | FLAGGED | 3 | F6 |
| SOLR-17215 | `04d35df186d` | FIXED | 2 | none |
| SOLR-17393 | `626241e647e` | FLAGGED | 3 | F7 |
| SOLR-9637 | `c50fa4ffd93` | FLAGGED | 4 | F8, F9 |

Total direct edits: 23 (8 changelog links, 15 wording edits). Every edit is listed below with old text, new text and evidence.

## Checks that passed for every draft

- Heads: all eight heads match the `solr-<n>-submit` tips on origin (`git ls-remote origin "*<n>*"`, 2026-10-11).
- Bases: 9227 and 9968 use `97d973814336`, 11844 and 17215 use `cabedd1d968`, 14171 uses `9b3a84b1c460`, 17393 uses `c3cdf7b46e8`. Each equals `git merge-base` with the local `upstream/main` (3f5d4c5bf8a, not re-fetched). The receipts name the same bases for 14171, 17215 and 17393.
- SOLR-9637 stacking: `dd6c82924fff` is an ancestor of `c50fa4ffd93` (its own commits: `872c8130da2`, `c50fa4ffd93`). The 17393 tip `626241e647e` is exactly two commits past `dd6c82924fff` (`063a040e1d7`, `626241e647e`). This matches decision 8.
- Changelog fragments exist at each named head with the stated type (fixed, changed, other). Titles compared with the draft summaries: consistent for all eight (one note under Notes).
- Line anchors checked at the named SHAs (base and head copies read from git): 9227 `SolrSuggester.java` L174-L182, L186-L191, L192-L197, L189 (base), L185-L186 (base); `SuggestComponent.java` L196-L199; tests L111-L129, L124, L403-L405. 9968 base L111, L267-L268; head L119-L127, L136-L152, L155-L171, L269-L287, L283-L285, L467-L469; ref guide L933-L944. 10937 NOTE L204-L207; `LookupFactory.getTempDir` uses `java.io.tmpdir`. 11844 base L399-L400; head L402-L406. 14171 base `SuggestComponent.java` L270 and head L270; head `SolrSuggester.java` L93, L108-L110, L244-L247; `AnalyzingInfixLookupFactory.java` L100-L103; tests L381-L410, L412-L414; config `solrconfig-suggestercomponent-context-filter-query.xml` `allTermsRequired=false` for the any-term suggester. 17215 base L220-L234, head L230-L237, L240-L248; ref guide L199-L201; test L32-L57. 17393 base L346-L378; head L78-L82, L273, L352, L368-L378; tests L55, L62. 9637 base (dd6c) L368-L377; head L274-L287, L369-L383, L389-L397; tests L62-L72, L74-L79. Every anchor matches.
- Counts and dates against the records: 9227 11 tests, 0 failures, 1 skip (2026-10-06); 9968 13 tests, 0 failures, 1 skip at `c31d2ff1a3f` (2026-10-09); 10937 and 11844 checked 2026-10-09; 14171 11 tests, 0 failures, 1 skip (2026-10-09); 17215 TestFreeTextSuggesterNotBuilt 1 of 1, SuggestComponentTest 12 of 12, CFQ 10 tests with 1 skip, 0 failures (2026-10-09, `material/suggester-17215-receipt.md`); 17393 MergeTest 2 of 2, SuggestComponentTest 12 of 12, DistributedSuggestComponentTest 1 of 1 (2026-10-09); 9637 MergeTest 4 of 4, SuggestComponentTest 12 of 12, DistributedSuggestComponentTest 1 of 1 at `c50fa4ffd93` (2026-10-05).
- 9968 gate line: `git diff --stat d688e1efdf2 c31d2ff1a3f` changes only the changelog and the ref guide. `git diff b1865b605184 c31d2ff1a3f` for the test file adds two tests and leaves the keyword test body unchanged, as the draft says.
- Formula: every draft has the AI header, the JIRA link line, the AI assistance footer, and a bold one-line summary opening each section. Em dash count in the eight drafts: 0.
- Decisions: 9227 has no Choice and keeps the store path in Limits. 9968 keeps its tokenizer Choice. 10937 and 11844 are docs only, with the 10937 follow-up as a Limits offer. 14171 keeps the literal `true` as a Choice, the factory split in Limits, and the unconfigured default `true`. 17215 keeps the 503 Choice, narrows the catch (AlreadyClosedException rethrown), and carries the ref-guide note. 17393 keeps the per-shard cut in Limits as a planned follow-up, with the payload and memory bounds. 9637 states the stacking in Limits (see F8 for the rest).
- JIRA facts (read-only search of `research/jira-context/`): 9637 packet has two shards and Solr 4.9.1 and a duplicate `test` result; 11844 packet has Solr 6.5 and the expected and actual order given in the draft; 17215 packet has Solr 8.11.2, three replicas on three nodes, `FreeTextLookupFactory`, "Lookup not supported at this time", and a 2024-12-20 comment that suggesters have never supported replication; 10937 packet says 612 GB free (see F3 note).

## Direct edits

All edits were exact replacements. The old and new text are given in full below.

### E1 to E8: changelog line becomes a link at the head SHA (one per draft)

Old text pattern: ``Changelog: `changelog/unreleased/<file>` (type: <type>)``
New text pattern: `Changelog: [changelog/unreleased/<file>](https://github.com/nick-boss-tech/solr/blob/<head>/changelog/unreleased/<file>) (type: <type>)`

| Edit | Draft | Head | Changelog file |
|---|---|---|---|
| E1 | SOLR-9227 | `3c23fc5cfa6cba5d6f932cae1806c4f44fbacba4` | `SOLR-9227-suggester-store-message.yml` |
| E2 | SOLR-9968 | `c31d2ff1a3fd967d4f51931073c8641fc1435e64` | `SOLR-9968-suggester-cfq-tokenizer.yml` |
| E3 | SOLR-10937 | `7d0cd11bafdce4a258b8758a34b9ccca364e4023` | `SOLR-10937-suggester-tmpdir-doc.yml` |
| E4 | SOLR-11844 | `cbd08c20e9a4594ad05073e2ee2acbed53c4038d` | `SOLR-11844-blended-weight-docs.yml` |
| E5 | SOLR-14171 | `faa262eedc554109123106b4f26a4ad9ad063a76` | `SOLR-14171-suggester-cfq-all-terms-required.yml` |
| E6 | SOLR-17215 | `04d35df186ddeab079ed93fce15c6fc61554912a` | `SOLR-17215-suggester-not-built-503.yml` |
| E7 | SOLR-17393 | `626241e647e5fcc5ac6cc485f6a96d4ffe12e00a` | `SOLR-17393-suggester-merge-order.yml` |
| E8 | SOLR-9637 | `c50fa4ffd932ede86c36b0748ee6aabe34d30e0a` | `SOLR-9637-suggest-merge-duplicates.yml` |

Evidence: `pr-formula.md` presentation rule (file citations are real links at the PR head SHA) and template line for the changelog. Each fragment exists at its head (`git show <head>:changelog/unreleased/<file>`) with the type the draft states.

### E9: SOLR-9968, Proof bullet 2 label

- Old: `- Gate at \`c31d2ff1a3f\`, recorded 2026-10-09:`
- New: `- Test run at \`c31d2ff1a3f\`, recorded 2026-10-09:`
- Evidence: "Gate" is the internal name for the hardening gate (`WORKFLOW.md`, `AGENTS.md`); the bullet itself reports test counts (13 tests, 0 failures, 1 skip), which match the top-up record in `material/suggester-draft-round-answers.md` B.

### E10: SOLR-9968, Proof bullet 2 sentence

- Old: `The code and tests are unchanged since the gated run at \`d688e1efdf2\`.`
- New: `The code and tests are unchanged since the test run at \`d688e1efdf2\`.`
- Evidence: `git diff --stat d688e1efdf2 c31d2ff1a3f` shows only the changelog and ref guide changed. "Gated" is internal vocabulary.

### E11: SOLR-9968, Limits bullet

- Old: `No pre-fix run is on record for \`testBadContextFilterQueryTokenizerFailsAtInit\` or \`testUnparseableContextFilterQueryReportsQueryAndCause\`.`
- New: `No run on the base code is on record for \`testBadContextFilterQueryTokenizerFailsAtInit\` or \`testUnparseableContextFilterQueryReportsQueryAndCause\`.`
- Evidence: no run of either test against base is recorded in the answers or the round-4 review. Same meaning, plain words.

### E12: SOLR-9968, Limits bold line

- Old: `Two later tests have no pre-fix run on record.`
- New: `Two later tests have no run on the base code on record.`
- Evidence: as E11.

### E13: SOLR-10937, Proof sentence

- Old: `There is no test and no failing-before run, because this is a docs change.`
- New: `There is no test and no run on the base code, because this is a docs change.`
- Evidence: docs-only change. "Failing-before" is the internal fail-before proof term (`AGENTS.md`, Fail-before proof). Meaning unchanged.

### E14: SOLR-10937, What this change does bullet 1

- Old: `A NOTE at the top of "Lookup Implementations" names five lookups:`
- New: `A NOTE near the top of "Lookup Implementations" names five lookups:`
- Evidence: at `7d0cd11bafd`, the NOTE is at L204-L207, after the section's two opening paragraphs (L201-L202). "At the top" overstates the position.

### E15: SOLR-11844, Proof sentence

- Old: `There is no test and no failing-before run, because this is a docs change.`
- New: `There is no test and no run on the base code, because this is a docs change.`
- Evidence: as E13.

### E16: SOLR-14171, Proof bullet 2 label

- Old: `- Gate at \`faa262eedc5\`, recorded 2026-10-09:`
- New: `- Test run at \`faa262eedc5\`, recorded 2026-10-09:`
- Evidence: `material/suggester-draft-round-answers.md` B: class 11 tests, 0 failures, 1 pre-existing skip at `faa262eedc5` (recorded 2026-10-09).

### E17: SOLR-14171, Proof bullet 1 last sentence

- Old: `Pre-fix proof against base \`9b3a84b1c460\`: PASS, so the test fails on the base code.`
- New: `On the base code \`9b3a84b1c460\`, the new test fails.`
- Evidence: `AGENTS.md` defines the fail-before verdict PASS as "failed without the fix". The answers record the run against base `9b3a84b1c460`. The new sentence says the same thing with the base SHA kept.

### E18: SOLR-17215, Proof bullet 3

- Old: `Gate GREEN at 04d35df186d, recorded 2026-10-09: changelog parse, tidy with a clean tree, Error Prone compile, and \`:solr:core:check -x test\` pass.`
- New: `Checks at 04d35df186d, recorded 2026-10-09: changelog parse, tidy with a clean tree, Error Prone compile, and \`:solr:core:check -x test\` pass.`
- Evidence: `material/suggester-17215-receipt.md` line 6 lists the same four checks as ok at `04d35df186ddeab079ed93fce15c6fc61554912a`.

### E19: SOLR-17393, Proof bullet 2 label

- Old: `- Gate at \`626241e647e\`, recorded 2026-10-09:`
- New: `- Test run at \`626241e647e\`, recorded 2026-10-09:`
- Evidence: `material/suggester-draft-round-answers.md` B: MergeTest 2 of 2, SuggestComponentTest 12 of 12, DistributedSuggestComponentTest 1 of 1.

### E20: SOLR-17393, Proof sentences (pre-fix and shim)

- Old: `Pre-fix proof: PASS, so the test fails on the base code. The pre-fix proof used the visibility-only shim, at seed \`17393C0FFEE17393\`.`
- New: `On the base code, with only the visibility of \`merge\` changed so the test can call it, the test fails. The run used seed \`17393C0FFEE17393\`.`
- Evidence: `material/suggester-draft-round-answers.md` B ("pre-fix proof PASS via the visibility-only shim at seed 17393C0FFEE17393"). In the base file `SuggestComponent.java` (`c3cdf7b46e8`, L346) `merge` is `private static`; at head (`626241e647e`, L352) it is `static`, so the shim is the visibility change only.

### E21: SOLR-9637, What happens today sentence and link text

- Old: `The base merge keeps every shard's copy, so a text found on two shards appears twice ([base merge](https://github.com/nick-boss-tech/solr/blob/dd6c82924fff6b8f7cedaf020494c2f06ab502a2/solr/core/src/java/org/apache/solr/handler/component/SuggestComponent.java#L368-L377)).`
- New: `The stacked base merge keeps every shard's copy, so a text found on two shards appears twice ([stacked base merge](https://github.com/nick-boss-tech/solr/blob/dd6c82924fff6b8f7cedaf020494c2f06ab502a2/solr/core/src/java/org/apache/solr/handler/component/SuggestComponent.java#L368-L377)).`
- Evidence: the linked blob is the recorded stacked base for this draft (Limits: "stacked on SOLR-17393 at dd6c82924fff"). The link is kept; the text now says which base it is, as the formula requires for pre-change symptoms.

### E22: SOLR-9637, Limits sentence

- Old: `That branch has since gained two packaging commits.`
- New: `That branch has since gained two more commits.`
- Evidence: `git log dd6c82924ff..626241e647e` shows `063a040e1d7` and `626241e647e`. "Packaging" is internal process vocabulary.

### E23: SOLR-9637, Limits bold line

- Old: `The rebased PR is not yet gated.`
- New: `The rebased PR has not been tested yet.`
- Evidence: decision 8 and the round-4 review: the rebased tree has no gate or test yet. "Gated" is internal vocabulary.

## Flags (not edited)

Each flag gives the exact text at issue, the evidence found, and what is needed. Nothing below was changed in the drafts.

### F1. SOLR-9227: reload path not covered by the Limits sentence (decision needed)

- Text: Limits bullet 1, last sentence: "Suggest queries that do not request a build never see it."
- Evidence: `reports/suggester-round-4.md` open decision 1. At `3c23fc5cfa6`, `SolrSuggester.reload()` calls `lookup.load(...)` at L213, which this branch does not change. A JDK exception from that call can name the store path in a `suggest.reload` request. `material/suggester-draft-round-answers.md` A answers the build path only. `material/suggester-round-4-decisions.md` is silent on reload.
- Needed: the owner either narrows the sentence to cover reload requests or accepts it as written. The draft is not edited.

### F2. SOLR-9968: premise run head and reverted files not on record

- Text: Proof bullet 1: "On the base code, a run at `b1865b605184` (seed `9968C0FFEE9968`) gives exactly 1 failure, this test, where base returns the wrong suggestion for context `c#x`."
- Evidence: `material/suggester-round-4-answers.md` cites `g9968-premise.log` and the seed, but not the run head or which files were reverted for "base". The SOLR-9227 answer names its reverted file; the 9968 answer does not. `reports/suggester-round-4.md` draft-round table, row 9968, leaves this open ("the material does not name the reverted files"). In favor of the head: the keyword test body is unchanged between `b1865b605184` and `c31d2ff1a3f` (see Checks).
- Needed: record the premise run's head and reverted files, or name them in the draft. Not edited.

### F3. SOLR-10937 and SOLR-11844: Lucene 9.12.3 claim has no record

- Text, 10937 Proof: "Each of the five builds reaches OfflineSorter with the lookup's temp directory in Lucene 9.12.3 as well as 10.4.0, by the same code paths in both versions."
- Text, 11844 Proof: "The blend rules were checked against Lucene 10.4.0 and Lucene 9.12.3 bytecode; in both, ..."
- Text, 11844 Limits: "The Lucene check is against 10.4.0 and 9.12.3."
- Evidence: `material/suggester-draft-round-answers.md` (section on the two docs verifications) records `javap` on the pinned 10.4.0 `lucene-suggest` jar only. A search of `material/`, `reports/` and the drafts finds no 9.12.3 check.
- Needed: either a recorded 9.12.3 check, or the main side removes these clauses. Claims are not edited here.
- Number note (10937): the draft says "about 600 GB free". The reporter's packet (`research/jira-context/SOLR-10937.json`) says "612 GB free on the Solr data partition". "About 600" is a rounding; the main side may prefer 612. Not edited.

### F4. SOLR-11844: "does not sort by match position" conflicts with the draft's own text

- Text, bold line of What happens today: "A blended suggester with no weight field does not sort by match position, and the ref guide did not explain why."
- Conflicts with: What this change does, "Suggestions with equal weights still follow match position", and Limits, "this text says equal weights still do".
- Evidence: the reporter's packet gives the observed order (sky blue, blue whale, blue water, blue; expected blue first). The general statement in the bold line is wider than the single report.
- Needed: restate the bold line as the reporter's observed order, or main side rules on the wording. Not edited.

### F5. SOLR-11844: shipped ref-guide text differs from the verified rule (branch file, record conflict)

- Shipped text at `cbd08c20e9a`, `suggester.adoc` L402-L406: L405 says "weights below `10` are scaled by a factor of `10`"; L402 says "the earlier the match, the higher the coefficient".
- Verified rule (`material/suggester-draft-round-answers.md` D): "a weight with absolute value under 10 is multiplied by 10". The review round 4 report (item 5) says the "earlier match, higher coefficient" wording is not in the verified facts, and that the material does not say whether a promoted weight of 1 is then scaled.
- The draft-round answers say the shipped text "states all of this". It does not: "below 10" differs from "absolute value under 10" for negative weights.
- The draft itself uses the verified wording and does not repeat L402 or L405. Not edited, because the shipped text is on the submit branch. Needed: a submit-branch fix before the PR, or a main-side ruling that the shipped wording stands.

### F6. SOLR-14171: unrecorded Lucene constant claim (low severity)

- Text: "In Lucene 10.4.0 that constant is `true`, so both sources agree today." (A choice to check)
- Evidence: no main-side record. `reports/suggester-round-4.md` item 4 states the value without a method. The 17215 receipt has a javap check for a different constant, not this one.
- Needed: a recorded `javap` read of `DEFAULT_ALL_TERMS_REQUIRED` on the pinned 10.4.0 jar, or the clause is dropped. Not edited.

### F7. SOLR-17393: reporter symptom linked to equal weights is an inference

- Text, Limits: "At `count` 1 with equal weights, a shard can return a different candidate than the merge would have chosen. So the reporter's symptom can persist."
- Evidence: decision 7 ratifies this link. `reports/suggester-round-4.md` item 3 says the JIRA packet has no weight text, so the link is an inference. The packet (`research/jira-context/SOLR-17393.json`) shows `AnalyzingLookupFactory` and `DocumentDictionaryFactory` and no `weightField`, but the full reporter configuration is not in the packet. The reporter's symptom is "closest match not on top" and "inconsistent results".
- Needed: the main side confirms the reporter used no weight field, or the sentence is reworded to say the merge can keep the symptom at equal weights without asserting the reporter's own weights. Not edited.

### F8. SOLR-9637: stacking relationship missing from the change description and Proof

- Text, What this change does: no mention of SOLR-17393. Text, Proof: "Two new tests fail on the stacked base and pass with this change." (no SOLR-17393 named). The relationship and "the proof ran on the stacked base, not on the rebased tree" sit only in Limits.
- The assignment requires the relationship in the change description and in Proof.
- Evidence: decision 8; ancestry as in Checks. The stacked base `dd6c82924fff` is an ancestor of the head, and the 17393 tip is two commits past it.
- Proposed text for What this change does (for the main side to adopt or not): "This change is stacked on SOLR-17393 (dd6c82924fff). It opens as its own PR after SOLR-17393 lands, rebased onto the landed tip."
- Proposed text for Proof: "The run was on the stacked base (SOLR-17393 at dd6c82924fff); no run on the rebased tree is recorded."
- Not edited: a new statement of the stacking relationship is a decision-level addition, not a wording fix.

### F9. SOLR-9637: summing weights is a design choice with no record and no Choice

- Text, bold line of What this change does: "Suggestions with the same text become one entry, and their weights add up."
- Evidence: `reports/suggester-round-4.md` draft-round item 2: "The material does not decide summing weights versus taking the maximum. The draft has no Choice section for it." No decision in `material/suggester-round-4-decisions.md`.
- Needed: a ratified decision, and a Choice section if the alternative is live. Not edited.

## Record conflicts (for the main side)

1. Lucene 9.12.3: drafts 10937 and 11844 say it was checked; the records show 10.4.0 only (F3).
2. SOLR-11844 shipped text: the draft-round answers say it "states all of this"; the text says "weights below 10" and "earlier match, higher coefficient" (F5).
3. SOLR-9227 reload path: open in the round-4 review; not answered in the answers or decisions (F1).
4. SOLR-9968 premise run: head and reverted files open in the round-4 review; not answered (F2).
5. SOLR-9637 summing weights: not decided in any record (F9).
6. SOLR-17393 equal-weights link: ratified in decision 7, but the review report says the packet does not support it (F7).
7. No `receipts/<ticket>.md` file exists for any of the eight tickets. The citations rest on `material/suggester-round-4-answers.md`, `material/suggester-draft-round-answers.md`, `material/suggester-17215-receipt.md` (17215 only) and `material/suggester-round-4-decisions.md`. The round-4 answers say the ledger (`test-receipts-63-branches.md`) and gate logs live on the main side and are not visible here. The draft counts were checked against the answers, not against the ledger.

## Notes (no verdict change, nothing edited)

- SOLR-9637 head `c50fa4ffd93` has `SOLR-17393-TESTING.md` at the repo root (`git ls-tree --name-only`), inherited from `dd6c82924fff`. The rebase onto landed 17393 should drop it (round-4 review item 2). Branch hygiene, not draft text.
- SOLR-9637 changelog title "with the weights of the shards added up" is wider than the Limits sentence "the sum counts only shards that kept the text in their own top list". Branch file, not edited.
- SOLR-17215 changelog title states the 503 answer as settled; the draft poses 500 or 503 as a Choice. Not a conflict: the title describes the branch behavior.
- SOLR-9227 Limits keeps the word "audit" ("the wider logging audit"). Not treated as internal vocabulary; left as written.
- Length guide: the 9637 draft (3,711 characters) and the 17215 draft (3,792) are over the roughly 3,500 guide (round-4 review). Not changed.
- Origin also has CI trigger refs: `ci/9227-suggest`, `ci/9968-suggest`, `ci/9968-suggest-r2`, `ci/14171-suggest-cfq`, `ci/17393-suggestmerge`, `ci/9637-suggestmerge`. They sit at earlier packaging states (for example `ci/17393-suggestmerge` is one CI commit on `dd6c82924ff`). The drafts use the `solr-<n>-submit` tips, which match the live tips.
- Round-4 review and answers say "AlreadyClosedException" verified against Lucene 10.4.0 for 17215; the receipt agrees, so no flag.

## Workflow status

- Claim `claims/pool-draft-fidelity-suggester.md` is not edited (the assignment limits edits to the eight drafts and this report). Last heartbeat in the claim is 2026-10-11T07:20Z; this report was written at about 07:40Z. The main side should append a heartbeat and the DONE mark (WORKFLOW.md, claim rule 5) with its push.
- No commit, no push, no build, no test, no gate, no GitHub write, no receipt, material or other-report edit. Git was used read-only (`ls-remote`, `log`, `show`, `diff`, `merge-base`, `ls-tree`, `grep`, `cat-file`). Extracted snapshots are in the scratchpad, outside the worktree.
