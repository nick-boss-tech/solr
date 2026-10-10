# SOLR-6973 review at head 51fcd05e695: six FIX, five NOTE

The head SHA, the cited test lines, the changelog file, and the footer check out, but the draft's behavior change covers new documents only, the code also changes updates to existing documents, and the proof wording claims a base re-run that the receipt does not give.

Scope: read-only review of `origin/pr-prepare:pr-drafts/update-processing/SOLR-6973.md` against `origin/pr-prepare:receipts/SOLR-6973.md` and the fork head `51fcd05e695ae1a5983ed69a3d2afc50e7f7bc1e`. Base is `cabedd1d968059215188f4e7563fb303241899ed` (see Not checked). No builds, tests, GitHub calls, or edits.

## Findings

### 1. FIX - Re-verify sentence claims a base count the receipt does not give at the new head

- Where: draft L23 and L25.
- Evidence: The only base count is receipt L8: "Pre-fix proof: PASS. On base production the class runs 7 tests with 1 failure." That line sits under the header for gated head `fa5b59ba07b4`, dated 2026-10-08 (receipt L4 and L5). Receipt L11 (re-gate at `51fcd05e695`, 2026-10-09) gives "proof PASS (the new test fails on base production), SignatureUpdateProcessorFactoryTest 7 of 7 at the head". It gives no base count at the new head. So draft L25 "the same 7 tests and the same base failure were re-verified at that head" is not supported. The 7 of 7 and the PASS are supported.
- Replace draft L23 "On the base production code, the same class ran 7 tests with 1 failure." with: "On the base code, the same class ran 7 tests with 1 failure (run 2026-10-08 at head `fa5b59ba07b`)."
- Replace draft L25 (whole paragraph, see also Findings 2 and 7) with: "With this change, the focused `SignatureUpdateProcessorFactoryTest` ran 7 of 7, verified 2026-10-09 at head `51fcd05e695`. At that head, the new test fails on the base code."

### 2. FIX - "The core module checks also pass." names a module the receipt never names

- Where: draft L25, last sentence: "The core module checks also pass."
- Evidence: Receipt L7 says "module check rc=0." Receipt L11 says "module check passes." Neither names a module. "core" is the drafter's addition. A build check is also not a test outcome, and the formula's Proof section is about test outcomes. "rc=0" and "module check" do not appear in the draft.
- Replace: delete the sentence.

### 3. FIX - Summary of "What happens today" drops the condition its body needs

- Where: draft L7: "**With `overwriteDupes`, a partial update with no signature fields can delete unrelated documents.**"
- Evidence: Base `cabedd1d968` `SignatureUpdateProcessorFactory.java` L151-L156: without a `fields` list, any partial update throws. So the bug needs a `fields` list. Draft L9 says "With a `fields` list", but the bold summary does not.
- Replace draft L7 with: "**With `overwriteDupes` and a `fields` list, a partial update with no signature fields can delete unrelated documents.**"

### 4. FIX - The loop claim in "What happens today" has no citation that shows it

- Where: draft L9. The first sentence says: "With a `fields` list, the processor's loop throws only if a signature field is present in the partial update." The only link in that paragraph for the processor points at `51fcd05e695` L208-L213.
- Evidence: Head L208-L213 is the signature write block (`String sigString`, `doc.addField`, `if (overwriteDupes)`, `cmd.updateTerm = ...`). That is the same code as base L199-L204. Its line numbers moved by +9 (diff hunks `@@ -164,0 +165,16 @@` and `@@ -172,7 +187,0 @@`). The loop and its throw are base L169-L178. Head no longer has that loop, so the first sentence has no link.
- Replace: after "in the partial update" add this link, then keep the existing head link on the write block: "([SignatureUpdateProcessorFactory.java, base](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/update/processor/SignatureUpdateProcessorFactory.java#L169-L178))". Note: this link uses a base SHA. The formula asks for the PR head SHA, but head no longer has this code. Owner decides.

### 5. FIX - Behavior change names new documents only; the code also changes updates to existing documents

- Where: draft L17: "Behavior change: a partial update that creates a new document now stores it without `signatureField`. The old code gave it the shared empty signature, which caused the collision."
- Evidence (read from code, not run):
  - `UpdateRequestProcessorChain.java` at `51fcd05e695` L104-L105: a DistributedUpdateProcessor is "injected immediately prior to the RunUpdateProcessorFactory". So the signature step runs before the atomic merge.
  - `DistributedUpdateProcessor.java` L723-L724: `getUpdatedDocument` merges atomic updates.
  - `AtomicUpdateDocumentMerger.java` L195: "normal fields are treated as a "set"".
  - So on the old code, a partial update with no signature fields to an existing document sets its stored `signatureField` to the empty signature. Its `updateTerm` also deletes other documents that carry that term. The new code skips both steps for existing documents too. This is wider than "creates a new document".
- Replace draft L17 with: "Behavior change: a partial update with no signature fields no longer gets an empty signature. This covers new documents and existing ones. A new document is stored without `signatureField`. An existing document keeps its stored `signatureField`, and the update no longer deletes other documents that share the empty signature."

### 6. FIX - Limits says only new documents were checked, and the test checks processor output only

- Where: draft L37: "The effect on new documents was checked by reading only."
- Evidence: After Finding 5, existing documents are in scope too. The new test (head L205-L236) checks only processor output. Head L233-L235: `assertNotNull(seen.get());`, `assertNull(seen.get().solrDoc.getField("signatureField"));`, `assertNull(seen.get().updateTerm);`. It indexes nothing.
- Replace draft L37 first sentence with: "The new test checks the processor output only. It does not index documents. The effect on existing documents was checked by reading the code, not by a run."

### 7. NOTE - The review-fix sentence narrates a review round inside Proof

- Where: draft L25: "The head includes a review fix that decides the partial-update outcome before the signature class is constructed;"
- Evidence: The order matches the code. At `51fcd05e695`, the partial-update decision is L165-L179 (early return at L177-L178). The signature class is built at L181-L182 (`newInstance(signatureClass, Signature.class)`). The review fix is commit `51fcd05e695`, subject "SOLR-6973: check for signature fields before constructing the signature". In `04eb4ef911f`, the class was built at L165-L167 and the partial return sat at L191-L196. So one thing shipped changed: a partial update with no signature fields no longer loads the signature class. The formula keeps process catches out of Proof unless they change shipped behavior. This change is an edge case.
- Option A (recommended): delete the sentence. Option B: keep the edge as a behavior note at the end of draft L15: "A partial update with no signature fields also no longer loads the signature class, so a bad `signatureClass` does not fail those updates."
- The draft names no review bot. The receipt names one (receipt L11). Keep it out.

### 8. NOTE - "A follow-up submission is planned" commits more than the formula asks

- Where: draft L37: "A follow-up submission is planned if the shard-routing path is confirmed as the cause of the reported symptom."
- Evidence: The formula asks for "an offer to open a follow-up ticket and PR for it on request". The audit `origin/pr-prepare:audits/update-processing/SOLR-6973.md` L21 says "The branch cannot settle this." No plan is recorded.
- Replace with: "A follow-up ticket and PR for the shard-routing path can be opened on request."

### 9. NOTE - The ticket claim is stronger than the only source I have

- Where: draft L37: "The ticket comments suggest shard routing."
- Evidence: The audit (`audits/update-processing/SOLR-6973.md` L21) says "may be shard routing, not this path. The ticket is not conclusive." I did not read the ticket.
- Replace with, unless the owner has re-read the ticket: "The ticket may point to shard routing."

### 10. NOTE - Public commit messages on the submit branch use internal words

- Where: commit `04eb4ef911f` body: "Hypothetical, unrun regression test; see SOLR-6973-TESTING.md." Commit `4c6092614e5` subject: "SOLR-6973: add hypothetical-reproduction handoff doc." Commit `fa5b59ba07b` subject: "SOLR-6973: remove the handoff testing note at packaging."
- Evidence: The testing file is gone at head. The diff from base changes only three files, so no residue is in the tree. The commit list still shows these words once a PR is open. Changing them needs a history rewrite and a force push, which the fork rules do not allow without direction.
- Replace (if the owner rewrites before opening the PR): drop the body line of `04eb4ef911f`. Fold the `4c6092614e5` and `fa5b59ba07b` commits into the ticket commits. Owner decides. I did not check whether a PR is open.

### 11. NOTE - Length is at the guide now, and the fixes add text

- Where: whole draft.
- Evidence: `git show origin/pr-prepare:pr-drafts/update-processing/SOLR-6973.md | wc -m` returned 3523. In this shell, `wc -m` and `wc -c` both return 3523, so it counts bytes. The two emoji are 4 bytes each. So the draft is about 3,517 characters. The guide is about 3,500.
- Replace: if the owner wants the guide held, delete the review-fix sentence (Finding 7, Option A). That removes about 115 characters. The proposed text for Findings 1, 3, 5, and 6 was not measured.

## Task results

Task 1 (draft against receipt). The draft's "7 of 7" (L25) and "verified 2026-10-09 at head `51fcd05e695`" (L25) match receipt L11. The draft uses the new head, which is correct; the receipt header at L4 still says `fa5b59ba07b4`, and the re-gate line names the new head. "The new test fails on the base code" (L21) matches receipt L11's wording. "7 tests with 1 failure on base" (L23) matches receipt L8 in count only; that run is at the earlier head and has no date line in the draft. The re-verify claim at L25 is not supported (Finding 1). "rc=0" and "module check" are not in the draft, but "core module checks" is (Finding 2).

Task 2 (code citations at `51fcd05e695`). All four paths exist at that SHA. Base is not named in the draft or the receipt. It is named in `audits/update-processing/SOLR-6973.md` L5 as the merge-base with upstream `4b58db1a42b`. Both commits are local. `git merge-base 51fcd05e695... 4b58db1a42b` returns `cabedd1d968`, and `cabedd1d968` is the parent of the first ticket commit `04eb4ef911f`. Diffs from base to head: the signature processor has two hunks (net +9 lines); head L208-L213 equals base L199-L204 in content, but the numbers moved (Finding 4). Head L146-L182 is head code for "What this change does", which is the right use; the range ends inside the `Signature sig =` statement, which is harmless. `DirectUpdateHandler2.java` has no diff from base, so L1177-L1180 are base lines too. The test file has one insertion at head 205-237, so L205-L236 is new code. Only the "What happens today" loop sentence lacks a fitting link (Finding 4).

Task 3 (review-fix sentence). At `51fcd05e695`, the partial-update decision is at L165-L179 and the signature class is built at L181-L182. The sentence's order is correct. The review-fix commit is `51fcd05e695`, subject "SOLR-6973: check for signature fields before constructing the signature". It changes only `SignatureUpdateProcessorFactory.java` (16 insertions, 13 deletions). The draft names no review bot. The sentence is a process note in a Proof section, so see Finding 7.

Task 4 (changelog, formula, wording). The changelog `changelog/unreleased/SOLR-6973-signature-partial-update.yml` exists at `51fcd05e695`. Its type is `fixed`. Its author is `Nick Shanin` (nick `nick-boss-tech`), with no placeholder. The draft's Changelog line (L39) matches the file name exactly. Formula shape: the AI header (L1) matches the template; the Jira link is at L3; the five sections (L5, L11, L19, L27, L33) each open with a bold line (L7, L13, L21, L29, L35); "A choice to check" has a live alternative (the no-`fields` path already rejects partial updates, base L151-L156) and ends with a pointed question (L31); Limits names the scope; the footer (L41-L43) matches the template. Vocabulary: the draft has no "gate", "receipt", "ledger", "seed", "log", "takeover", "rc=", "JUnit XML", internal log file names, or review-bot names. "Proof" appears only as the required heading (L19). "core module checks" is Finding 2, and "review fix" is Finding 7. No em dash or en dash appears in the draft. Branch commit authors are `Nick Shanin`; no placeholder and no Claude trailer appear in the messages. Length: Finding 11.

## Not checked

- JIRA SOLR-6973: not read. I have no Jira access in this review. The "5%" symptom and the ticket claim rest only on the audit note (`audits/update-processing/SOLR-6973.md` L21).
- Gate logs `g6973-gate.log` and `g6973-copilotfix-gate.log`: not on disk, per `claims/update-4841-6973-gated-heads.md`. Numbers come from the receipt only.
- Test runs: none, by rule. The base failure reason ("the base code sets both values") comes from reading base L199-L203, not from a run.
- Existing-document merge (Finding 5): read from code. I did not trace the full merge end to end. Owner should confirm.
- Fork blob links: I checked SHAs and paths in git only. I did not open the GitHub pages.
- Base SHA: `cabedd1d968` appears only in the audit file, not in the draft or receipt. I did not confirm that the fork has that commit online.
- PR state: I do not know whether a PR is open, so I cannot say whether the commit messages are public yet.
- Changelog schema: I checked the file text. I did not check `type: fixed` against Solr's changelog tooling.
- Length of the proposed wording: not measured.
