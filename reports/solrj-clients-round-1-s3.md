# SolrJ and clients round 1, part S3: XML and misc (SOLR-4335, SOLR-6046, SOLR-8536, SOLR-12094)

Drafts only. No builds, tests, `gh` writes, commits, pushes, or posting. The part edited only the four draft files below. Other drafts in `pr-drafts/solrj/` belong to other parts and were not touched.

## Per ticket

**SOLR-4335: draftable.** Gated at head per the receipt. The source agrees with the receipt's test counts (`TestXMLEscaping` has 10 tests at head and 7 at base). The receipt's description of the escape form is wrong, so the draft follows the code (see receipt disagreements 1 and 2).
- Draft `pr-drafts/solrj/SOLR-4335.md`, 4,666 characters with links.
- Head `8f6e267d6482516e9853df69da630e4f0142c72a`: `ls-remote` matches. Merge-base `97d973814336`, matching the claim.

**SOLR-6046: draftable.** `ClientUtilsTest` is 5 tests at head and 4 at base, matching the receipt's 5 of 5 and base 1 of 5. The XML loader groups the per-element elements on the server side, so `set` receives both values (`XMLLoader.java` lines 381 to 398 and 476 to 479 at head).
- Draft `pr-drafts/solrj/SOLR-6046.md`, 4,185 characters with links.
- Head `3b833e369a0bf5fc06c976c7e1febe17694223c4`: `ls-remote` matches. Merge-base `97d973814336`, matching.
- The draft points to SOLR-6045 as the same pair, with its pull request number 5077, taken from `reports/open-update-29-prs.md` and not checked live.

**SOLR-8536: draftable, with branch-side fixes owed before the PR.** The draft poses the Choice (keep values with control characters replaced, versus the ticket's Solr-keys-only route) and states the ASCII-only limit. Owed on the branch: the changelog title, one commit subject, and the internal commit subjects in history (see below).
- Draft `pr-drafts/solrj/SOLR-8536.md`, 4,114 characters with links.
- Head `28552ddc26eb30df5c9bfcd612bbac3698372442`: `ls-remote` matches. Merge-base `14c7aac0d151`, matching.

**SOLR-12094: draftable.** `TestJsonRecordReader` has 10 test methods at head and 9 at base, matching the receipt's 10 of 10. `JsonLoaderTest` (core) has 31 tests at head, matching 31 of 31. The Limits carry the behavior change, the unbuilt relaxed mode (named as a follow-up offer), and unmapped fields still dropped (the test asserts this).
- Draft `pr-drafts/solrj/SOLR-12094.md`, 4,340 characters with links.
- Head `8d957a73f4fb1e8e6e72e53df4faa14266702e53`: `ls-remote` matches. Merge-base `c3cdf7b46e8c`, matching.

The local remote-tracking refs for all four already pointed at the live heads, so no explicit fetch was needed.

No Choice section for 4335, 6046 or 12094, since the assignment names none. The 12094 error-versus-buffer question sits in Limits, as the assignment directs.

## Interaction: SOLR-4335 and SOLR-6046 compose

Yes, by code path. In 6046, each array element goes to `writeVal` (`ClientUtils.java` head line 117), and the value writer there calls `XML.escapeCharData` (line 162). That routine is the one 4335 changes (`XML.java` head lines 142 to 146). Attribute values (name and update) reach the same routine through `XML.writeXML` and `escapeAttributeValue`. The two branches touch different files (`XML.java` and `ClientUtils.java`) and different test files, so landing order does not matter. No test combines an array element with one of the non-characters. The 6046 draft's Limits state that.

## Self-check

- Dashes: zero em or en dashes in all four drafts (grep for U+2014 and U+2013).
- Process words: none (gate, receipt, ledger, rc=0, fresh JUnit, pre-fix, owed, round, premise, audit, handoff, hypothetical, entity). "Drafting" appears only in the template AI footer.
- Head references: current-code links use the full head SHA (4335: 9 links; 6046: 5; 8536: 4; 12094: 7). Base links use the full merge-base and are labelled "base code" (4335: 1; 6046: 1; 8536: 2; 12094: 1).
- Proof lines name the head in full. Dates come from the receipts: 4335 and 6046 use 2026-10-06 (the recorded date; no run date is given). 8536 and 12094 use 2026-10-05 (finish dates).
- Length: all four exceed the roughly 3,500 character guide with links counted. Link-heavy citations drive the length. An owner decision.
- The AI header line keeps the template's emoji, as the formula template has it.
- The changelog link is pinned to the head SHA and placed after Limits, per the template. "The changelog link at the head" was read as "pinned to the head SHA".

## Receipt disagreements (exact wording)

1. **SOLR-4335 receipt:** "it now writes the numeric character references `&#65534;` and `&#65535;`" and "a parser reading the reference gets the non-character back as data, not as markup structure". The assignment's starting state repeats it: "XML.escape now writes U+FFFE and U+FFFF as the numeric character references `&#65534;` and `&#65535;`". The code at head (`XML.java` lines 142 to 146) writes `#65534;` and `#65535;` with no ampersand. The test asserts `a#65535;b` and `x#65535;y` (`TestXMLEscaping` lines 71, 72 and 85). The source comment at lines 58 to 59 and 143 says "#nn; *not* &#nn;". The output is plain text, so a parser reads `#65535;` and does not get the character back. The draft follows the code.
2. **SOLR-4335 receipt:** "XML.escape now writes". The public entry points are `XML.escapeCharData` and `escapeAttributeValue`. The shared routine is the private `escape`. Naming only.
3. **SOLR-4335 receipt** does not record that the same routine serves the XML response writer (`XMLWriter.java` line 405), the schema XML writer (`SchemaXmlWriter.java` line 475), and the XML request writer for delete-by-id and delete-by-query (`XMLRequestWriter.java` lines 141 and 148). The draft states this widening.
4. **SOLR-8536 receipt:** "so a value carrying a newline or other control character can no longer forge log lines through the thread name." The same receipt then says `\p{Cntrl}` covers ASCII only. U+0080 to U+009F, U+2028 and U+2029 pass through, so the first sentence overstates. The draft states the ASCII-only limit.
5. **SOLR-8536 branch changelog** (`changelog/unreleased/SOLR-8536-mdc-thread-name-control-chars.yml`): "no longer contain line breaks or other control characters coming from application MDC values". It overstates for the same reason. Not in the receipt.
6. **SOLR-8536 commit `2f631a960ae`** subject: "SOLR-8536: strip control characters from MDC derived thread names". The code replaces with spaces and does not strip. A title accuracy issue.
7. **Internal commit subjects in branch history**, visible in a PR commit list. SOLR-8536: `a32eb45761c` "add hypothetical-reproduction handoff doc", and `28552ddc26e` "remove the hypothetical-reproduction handoff doc before submission". SOLR-12094: `ae69f142525` "add hypothetical-reproduction handoff doc", and `8d957a73f4f` "Remove the testing handoff file". Not in the receipts.
8. **SOLR-6046 receipt:** "matching what the JSON and javabin writers already do". Not verified. Both writers have `Object[]` branches (`JavaBinCodec.java` line 385; noggit `JSONWriter.java` line 100 at `upstream/main`), but the per-element behavior was not checked. The draft does not use the claim.
9. **SOLR-12094 receipt:** "consistent with how the endpoint treats other misplaced mapped input". Not verified. The draft does not use it.

## Owner decisions

1. Correct the SOLR-4335 receipt and the assignment's starting-state text to `#65534;` and `#65535;` (no ampersand). The draft already matches the code.
2. Decide whether the 4335 widening (the server XML response and the delete-request output change too) stays as stated, or narrows to the SolrJ update writer. This could become a Choice section. None was written.
3. SOLR-8536 branch: fix the changelog title to say ASCII control characters, and change the commit subject "strip" to "replace". Decide whether to squash the internal commit subjects before opening.
4. SOLR-12094 branch: the same history decision for the "handoff" commit subjects.
5. SOLR-4335 and SOLR-6046: confirm the verification dates (the receipts give a record date only).
6. SOLR-8536: confirm the Choice wording, and whether the non-String MDC follow-up offer stays in Limits.
7. Length: all four drafts exceed the 3,500 character guide with links counted. Trim or accept.
8. SOLR-6046: confirm the SOLR-6045 pull request number (5077) before posting, and whether the JSON and javabin "matching" claim should be verified or dropped.
9. Confirm the changelog placement reading (head-pinned link, template position).

## Not checked

- No builds, tests or gate runs. Test counts come from counting test methods in source at each head. The 10 of 10 and 7 of 7 figures agree with the receipts.
- No JIRA calls. Ticket text came from the read-only JSON files under `research/jira-context` in the main checkout.
- No `gh` calls. PR 5077 comes from `reports/open-update-29-prs.md` only.
- Java's `\p{Cntrl}` class is ASCII-only per the `Pattern` documentation, as the part knows it. Not run.
- The head run dates for 4335 and 6046 are not in the receipts. The gate logs were not read.
- The JSON and javabin writers were not traced for per-element behavior.
- Lone-surrogate behavior (U+D800 to U+DFFF) is described from the escape routine's code, not run.
- Blob links were built from `git show` at each SHA. They were not opened in a browser.
- The drafts use the template emoji header. If the public text rule covers emoji, that header needs a decision.
