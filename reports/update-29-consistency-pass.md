# Update-29 consistency pass: landing order, contradictions, and follow-ups

Assignment: `assignments/update-29-consistency-pass.md` (commit `2f873742f34`). Claim: `claims/update-29-consistency-pass.md` (commit `67d4a916ed8`).

Desk review, read only. Reviewed at `67d4a916ed8`. The branch then moved to `cfb8f96c4c3`, which adds 29 receipt files and changes no draft, audit, or assignment. The findings hold at both tips.

Two subagents did the work: one took checks 1 and 2, the other took checks 3 and 4. The lead merged the two reports, checked the quoted sentences behind the four real contradictions (R1 to R3 and the 3.1 and 3.2 findings, which restate R2 and R1) against the drafts at the branch tip, and wrote this report. No builds, tests, gate claims, or new owner decisions.

Sources: `pr-drafts/update-processing/SOLR-<ticket>.md` (28 drafts), the interaction sections of `audits/update-processing/*.md`, and `TESTING.md` at the branch root. SOLR-18505 has no draft, so it is not in the drafts count.

## Summary

- **Landing order.** Three prerequisite pairs are fixed by their sources: 5754 before 5939, 13696 before 13943, and 16655 before 12705. One pair has no source order: 12703 and 6045. Two branches (7504, 14718) wait on owner calls.
- **Real contradictions: four.** Three need an owner call (R1, R2, R3). One is a correction to our own audit (R4).
- **Apparent: eight.** Six from check 2 (A1 to A6), and two from check 3 (3.3, 3.4). None breaks a draft's position.
- **Owner-visible, not findings:** the SOLR-6065 status code (3.5), and the SOLR-13696 batch status (gaps).
- **Follow-up overlaps:** one real overlap (G1, streamed error naming) and one shared helper (G3). Neither draft names the other in G1.

## Check 1: landing order

Constraint-derived. Nothing here is an owner decision.

**Prerequisite pairs (the first ticket lands first)**

1. **SOLR-5754 before SOLR-5939.** Source: `audits/update-processing/SOLR-5939.md` item 11, "The smaller change, 5754, should go first." The 5939 draft's Proof names `StreamingSolrClientsTest` 2 of 2 and says the class "comes from SOLR-5754 and is not in this branch alone." Textual merge is clean at the drafts' heads `46b919e2d4e8` and `f8d4bdbea518`.
2. **SOLR-13696 before SOLR-13943 (stacked).** Source: the 13943 draft's Limits, "If SOLR-13696 lands first, this branch rebases onto main." `TESTING.md` (2026-10-08 entry): the base class's `@AwaitsFix(SOLR-13696)` keeps the moved class skipped in normal mode until the 13696 repair is on the base. Ancestry is checked: `1d0b8a0a73cd` is an ancestor of both `da4fa6df117` and `cc155cf68e1`. The 13943 draft's "This PR opens after, or alongside" concerns opening, not landing.
3. **SOLR-16655 before SOLR-12705.** Source: both drafts. The 16655 Limits say "this change lands first, and SOLR-12705 rebases onto it", and the 12705 Limits say the same. At the drafts' heads (`5e2317443f4116a9`, `8624b7c3238b5ca3`, base `14c7aac0d151`), the two branches conflict in two content hunks in `FieldMutatingUpdateProcessor.java`. The audits recorded the same file conflict at earlier heads.

**Sequenced, order not stated by any source**

4. **SOLR-12703 and SOLR-6045** share `AtomicUpdateDocumentMerger.java` and `mergeDocHavingSameId`. Both drafts say to sequence them, and neither names the first. Textual merge is clean at heads `63c84919c80b` and `e4b77fa7ae53`. Order: open.

**Waiting on a check 2 or 3 call**

5. **SOLR-7504** waits on R1 (with SOLR-12705) and R2 (with SOLR-6045). Source: the 12705 audit item 7, "The two must be sequenced, and the rejection kept."
6. **SOLR-14718** waits on R3 (with SOLR-5939). No audit covers this pair.

**No order constraint in any draft or audit (gate notes and shared files only)**

- SOLR-5941 with SOLR-13696: the 5941 audit says "If 13696 resumes, gate the two together."
- SOLR-5065 with SOLR-12705: the 5065 audit says the combined `{"set": "4.5E+10"}` case on a parse-double chain is not tested on either branch (marked as a hypothesis).
- Files named in more than one draft, clean textually at the drafts' heads, with no audit trial merge on record: `DistributedUpdateProcessor.java` (12245, 13265, 5939, 5941, 14262); `StreamingSolrClients.java` (5754, 5939, 14718) and `SolrCmdDistributor.java` (5754, 14718); the ParseDouble and ParseFloat factories (5065, 16673); `TestRecovery.java` (11483, 14262); `UpdateLog.java` (11483, 16356); `DirectUpdateHandler2.java` (6065, 6973, 7022, 13265); `DocumentBuilder.java` and its test (3657, 5887).
- No ordering constraint: 4841, 5505, 11475, 12864, 16910, 18505 (already open).

**Constraint-derived sequence, in one line:** 5754 before 5939; 13696 before 13943; 16655 before 12705; 12703 and 6045 in either order; 7504 and 14718 after the calls in checks 2 and 3; all other tickets unconstrained apart from the gate notes.

## Check 2: contradictory behavior claims

### Real

**R1. SOLR-7504 vs SOLR-12705: counter behavior on add, remove, and multi-value set.** Quotes verified at the branch tip.

- SOLR-7504 draft: "**The counter counts a `set` operand. Every other atomic operation on the counted field is rejected.**" It also says: "`add`, `remove`, `inc`, `add-distinct` and `removeregex` on the counted field now fail with BAD_REQUEST."
- SOLR-12705 draft: "`CountFieldValuesUpdateProcessorFactory` opts out. An atomic update on a counted field is still counted as one value." Its test "pins `{add}`, `{remove}`, and a multi-value `{set}` on the counting processor. Each returns a count of 1."
- In any tree that holds both branches, the 12705 test expects `{add}` to count 1 and the 7504 counter rejects it with 400. Each draft is true on its own branch. The 7504 audit (item 8) and the 12705 audit (item 7) record the same collision at an earlier 12705 head. `TESTING.md` chose to exclude counting processors from the atomic-operand path, but the pinned 12705 test still asserts the base count.
- **Owner call.** Position (i): the counter counts set operands and rejects other operations with 400 (SOLR-7504). Position (ii): the counter keeps base behavior, with the whole map counted as 1 (SOLR-12705, as pinned by its test). Which holds in a combined tree?

**R2. SOLR-7504 vs SOLR-6045: a plain value and an operation map in one field.** Quotes verified at the branch tip.

- SOLR-7504 draft: "A plain value in the same field as an operation map is rejected with BAD_REQUEST. The check reads every value, so a plain value first is rejected too." It adds that SOLR-6045 "adds a similar check to `AtomicUpdateDocumentMerger`" and that "the two checks should share one helper."
- SOLR-6045 draft: "An operation map first and a plain value after it now return 400 ... A plain value first is not atomic, so the map is stored as a value." It adds: "No test covers either case."
- The same input gets opposite outcomes: 400 in the 7504 counter, stored as a value in the 6045 merger. The 6045 audit (finding 4) lists plain-first as still open and asks only for it to be stated in the changelog or Limits. The 7504 audit (finding 3) calls plain-first a blocking silent drop on the counter.
- **Owner call.** Position (i): reject plain-first as well as operation-first (SOLR-7504). Position (ii): reject only operation-first and store plain-first as a value (SOLR-6045 as drafted). A shared helper would have to pick one.

**R3. SOLR-14718 vs SOLR-5939: which request a failed stream reports, and where the retry decision sits.** Quotes verified at the branch tip.

- SOLR-14718 draft Limits: "Each node has one streaming client, and the client reports its errors against the request it was created with. For a later document, the reported document is the first one sent to that node through that client. The copy does not change this." Also: "Retries are not per document. The retry count sits on the request the client reports."
- SOLR-5939 draft: "When a merged stream fails, the error is recorded only against the first request sent to that node." Its change: "Each request in a failed stream gets its own error, and a successful stream forgets its requests." And: "within a failed stream, each request gets its own retry decision."
- In a tree that holds both, SOLR-5939 changes the mechanism that the 14718 Limits describe as unchanged. Neither draft names the other.
- **Owner call.** Position (i): 14718 as drafted (the first request per client carries the error, and the retry count sits on it). Position (ii): 5939 (each request in a failed stream carries its own error and retry decision). Which order, or should 14718's Limits be rewritten after 5939?

**R4. SOLR-12705 audit record vs SOLR-12705 draft: which subclasses take the new atomic path.** Within one ticket; a record-level correction.

- The 12705 audit, "Settling run and gate": "the 3-arg mutator delegates with true, so Concat, FieldValueSubset, and Ignore are unchanged."
- The 12705 draft: "This is wider than the ticket's narrowest reading. It applies to every subclass of `FieldMutatingUpdateProcessor`, not only the date parser."
- With the default `true`, those three subclasses do take the new path. The draft describes the mechanism correctly. The audit's "unchanged" is wrong, or refers only to the call signature. **This is a correction to the audit note, not an owner call.**

### Apparent

- **A1. SOLR-7504 vs SOLR-12705, opt-out.** Both say the counter keeps its whole-field behavior. R1 is about the pinned test, not the opt-out.
- **A2. SOLR-5754 draft vs SOLR-5939 audit, window.** The 5754 draft says "that window does not open, so this change is hardening." The 5939 audit says the 5754 fix "closes a window that 5939 makes wider." Both can hold: the first is scoped to 5754 alone.
- **A3. SOLR-5939 audit vs draft, tolerant count (stale audit).** The audit says the count is "counted once per request"; the draft says "each distinct remote exception is counted once." The audit reviewed a head before the fix the draft describes.
- **A4. SOLR-7504 audit vs draft, combined tree (stale audit).** The audit names `mutateAtomicOperations` at an earlier 12705 head. The current 12705 draft names `mutateAtomicOperands()`, with the counter opting out. No combined run is on record. R1 still stands on the pinned test.
- **A5. SOLR-16655 draft vs audit, where the change lives.** The draft says the change "is confined to the child-document descent in `mutateDocument`". The audit says both branches change `processAdd`. The audit's own description, a `mutateDocument` recursion replacing the loop, reconciles the two.
- **A6. SOLR-13943 draft vs `TESTING.md`, sequencing.** The draft says the PR "opens after, or alongside" 13696. `TESTING.md` says 13943 "stacks on" 13696. Opening is not landing; the landing order is in check 1.

### No contradiction

- **Atomic operands, SOLR-12703 vs SOLR-12705.** 12703 rejects a nested operation map in the merger. 12705 runs `mutate` on the operand of set, add, add-distinct, and remove. Neither draft says anything about the other's case.
- **Repeated operation maps, SOLR-12705 vs SOLR-6045.** 12705 Limits: "A field with several operation maps in one update is passed to `mutate` unchanged." 6045 makes that shape atomic. The 12705 audit names this as a limit, but the 6045 draft does not mention it.
- **Nested operations, SOLR-12703 vs SOLR-6045.** The 6045 draft does not define "operation map", so its rule cannot be compared with 12703's definition.
- **Set and null, SOLR-7504 vs SOLR-12705.** 7504: "A null counts as 0." 12705 drops the field when the mutator returns null for every value operation. They meet only if the counter is on the 12705 path, which the opt-out prevents.
- **SOLR-16356, SOLR-16910, SOLR-14718.** None of the three describes atomic operations. The "add" in 14718 is the distributed add command.
- **Parse family, SOLR-5065 vs SOLR-16673.** Apparent agreement: 5065 says "the value is left unchanged"; 16673 says an unparsed empty string "is skipped, so the value stays as it was."
- **Autocommit skip, SOLR-14262 vs SOLR-5941.** Consistent: 14262 says "An autocommit has no client response, so the header would go nowhere there"; 5941 says "the autoCommit is logged as ignored and not committed."
- **Limit, not contradiction.** The 5941 audit says autocommits now reach the `IgnoreCommitOptimizeUpdateProcessorFactory` `getBool` check. The 5941 draft's adjacent note does not say so.

## Check 3: philosophy consistency

Rating: **real** means two positions cannot both hold for the same situation. **Apparent** means the wording or scope differs and no draft's position is broken.

**3.1 Real: plain value ahead of an operation map (SOLR-7504 vs SOLR-6045).** Same as R2. The input is the same, and the outcomes are opposite. `TESTING.md` treats the plain-first silent drop as a defect to fix in 7504 and records no decision for 6045's plain-first case. **Owner call:** should 6045 reject plain-first mixed values as 7504 does, or keep storing the map as a value and say so? The shared helper (G3) depends on it.

**3.2 Real: counted field on the atomic path (SOLR-12705 vs SOLR-7504).** Same as R1. `TESTING.md` chose option (a) for 12705, which covers 12705 alone. No recorded decision covers 7504's element counting or the 12705 pins. The 7504 audit (finding 6) marks the element-count rule "Open, owner to confirm." **Owner call:** which result a counted field gives for atomic add, remove, and multi-value set when both land.

**3.3 Apparent: skipped commit reported to clients vs autoCommit skip (SOLR-14262 vs SOLR-5941).** The 14262 draft states its rule for client responses and names the autoCommit gap itself ("This branch does not address that overlap"). The 5941 log-only skip for autoCommit does not break that rule, because an autoCommit has no response to carry a header. The overlap is one-sided: 5941 does not mention 14262.

**3.4 Apparent: interrupted searcher wait vs skipped commit (SOLR-7022 vs SOLR-14262).** In 7022 the commit runs and only the searcher wait is cut short. In 14262 the commit does not run. Listed because a reader could ask why one case returns success silently while the other signals the client.

**3.5 Owner-visible, not a finding: status code for max-docs (SOLR-6065).** The 6065 draft says "The alternative is 400, which keeps the status for this case and changes only the message. That choice is for maintainers to decide." The 3657 and 5887 drafts state their own status facts. No draft states a general rule about status codes. The round-28 review lists "500 versus 400" as an owner decision, and `TESTING.md` does not record it. **Still open.**

**No contradiction (check 3):**

- **Error detail in client responses (12245).** 12245 puts the replica in the client error text; 3657 puts the destination field in it; 5887 puts the core in it. The logs-only route appears only as 12245's alternative. `TESTING.md` (resolved 2026-10-09) keeps the detail in the response.
- **Reject versus skip, as a general rule.** No draft states a general rule. The per-branch pattern is: rejects in 12703, 7504, and the map-first case in 6045; skips or pass-through in 6973, 11475, 16673, 12705, and 5941. For the owner's awareness only.
- **Log level.** 16356 and 7022 both demote only the benign case and keep other failures at ERROR.
- **Scope and changelog disclosure.** 6045, 12705, 13696, and 5941 each state the wider effect in the body. Two changelog gaps (6045, 12705) remain open in their own drafts.
- **Client-visible text changes.** 3657, 5887, 12245, 5505, and the 16655 upgrade note disclose the change. None offers an opt-out.
- **11475 sign-mismatch.** No other draft takes a position on requesting a version pair. `TESTING.md` (resolved 2026-10-09) ships as implemented.

## Check 4: follow-up promises

"Offered" means the draft says the work can be opened on request. "Named" means the draft names the gap without an offer.

| ID | File | Follow-up | Form |
|---|---|---|---|
| F1 | 3657 | Vector message names the destination twice | Offered |
| F2 | 3657 | Wrapper does not copy other SolrException fields | Offered |
| F3 | 5065 | Locale-aware exponent parse | Named |
| F4 | 5941 | IgnoreCommitOptimizeUpdateProcessorFactory reads the flag with getBool | Offered |
| F5 | 7504 | Shared shape-check helper with 6045 | Recommended |
| F6 | 11483 | New test does not close its UpdateLog | Offered (fix-up) |
| F7 | 13696 | commitWithin coverage is no longer exercised | Offered |
| F8 | 13696 | Two folded production fixes as their own ticket and PR | Alternative |
| F9 | 13696 | testDateMathInStart, re-enabled under 13943 | Named |
| F10 | 13943 | Class-level SOLR-13059 annotation | Named, external |
| F11 | 14262 | Cloud-path skip behavior | Offered |
| F12 | 14262 | Autocommit overlap with 5941 | Named |
| F13 | 14718 | Second flaw: per-node streaming-client association | Offered |
| F14 | 16356 | Expiration-task commit trace | Offered |
| F15 | 16673 | Non-string unique key and child-document `_root_` errors | Offered |
| F16 | 16673 | Schema Designer path | Named |
| F17 | 16910 | SolrCore.Request logging | Offered |
| F18 | 5939 | Per-document identity through the callback | Alternative, not offered |
| F19 | 12245 | MDC ask and null-guard coverage | Required by `TESTING.md`, absent from the draft (see gaps) |

**Overlaps:**

- **G1. Which document a streamed error names (F13 and F18).** The 14718 Limits say the reported document is the first one sent to a node through its client. 5939 changes that for failed merged streams, and offers per-document identity as an alternative. If 5939 lands, part of F13 is already changed on the stream path. Neither draft names the other, and no audit names this overlap. The same follow-up is a deferred item in one draft and a design alternative in the other. Retry granularity also differs (R3).
- **G2. testDateMathInStart (F9 and the 13943 draft).** 13696 calls it "separate work". The 13943 draft delivers the move as a branch stacked on 13696. This is one work item framed two ways, not a conflict.
- **G3. Shared shape-check helper (F5).** 7504 recommends one helper with 6045. The 6045 audit also says one helper would avoid divergence. `SOLR-6045.md` does not mention 7504. This depends on the owner call in 3.1.
- **G4. Commit and close path (F4, F11, F12, F14).** These are distinct items, not a duplicated promise. F4 is a third site of the `getBool` defect that `TESTING.md` lists for `processCommit` and `RoutedAliasUpdateProcessor.wrap()`.
- **G5. Offer style against the owner's no-new-ticket rule.** `TESTING.md` (2026-10-09) says no new Jira ticket unless someone asks, and scope questions go in the PR's Limits or Choices. The "on request" offers fit that rule. `TESTING.md` also recommends recording the 14718 second flaw "as a follow-up" and splitting the 16356 trace "as a follow-up"; those drafts offer the work on request instead. This is a difference in commitment, not a contradiction.

**Not overlapping:** F1 and F2 are separate items in 3657. F7 and F8 are separate items in 13696.

**No duplicate promise:** no draft promises the same work twice as two separate pieces of work. G1 is the closest case.

**Overlaps the drafts do not cross-reference:**

- 6045 does not name 7504. 7504 names 6045, and the 6045 audit names 7504.
- 12705 does not name 7504. 7504 names 12705, and the 12705 audit names 7504. The 12705 opt-out and pinned counts change 7504's counter (3.2).
- 5941 does not name 14262. 14262 names 5941 (G4).
- 5939 and 14718 name neither other (G1).
- 5065 and 12705 name neither other. Both audits name the overlap.
- 5941 and 13696 name neither other. The 5941 audit says to gate them together, because both touch `RoutedAliasUpdateProcessorTest`.
- 6065 and 7022 name neither other. Both audits name the shared `DirectUpdateHandler2.java`, in different hunks.
- 3657 and 5887 name neither other. Both add tests to `DocumentBuilderTest.java`.

## Gaps

- **SOLR-18505 has no draft.** `pr-drafts/update-processing/SOLR-18505.md` does not exist at the branch tip. Its audit has no overlap section and no follow-up items. It is already open, so it is not sequenced here. The live PR description was not read.
- **`TESTING.md`.** The branch-root copy is the one read. It is identical to the worktree copy and to `wt/pr-prepare-scope/TESTING.md`. `wt/pr-prepare-groupb/TESTING.md` is an earlier revision, and it was not used. A search of the other worktrees timed out, so no other copies are confirmed.
- **SOLR-13696 status layers.** The 13696 audit hold table predates `TESTING.md`'s 2026-10-08 entry (fund as its own project) and its 2026-10-09 entry (fold into the 13696 branch). This report uses the 2026-10-09 entry. The owner should confirm 13696 is in the batch before 13943 is placed.
- **SOLR-12245 draft lacks owner-recorded Limits (F19).** `TESTING.md` (adopted 2026-10-08) says the draft states plainly that the ticket's MDC ask is not addressed and offers it as a follow-up, and that the missing null-guard coverage is named in Limits. Neither appears in the draft, and the 12245 audit records the MDC item as "Addressed by decision". **This is a fix to our round 3 draft, not made in this pass.**
- **Child documents as atomic operands.** Neither the 16655 nor the 12705 draft says what happens when an operation's operand is a child document.
- **No general error-surfacing policy.** A keyword search of `research/` for "silently" and "error surfacing" returned per-ticket notes only.

## Owner decisions needed

1. **R1 and 3.2:** the counted field on the atomic path (7504 against 12705).
2. **R2 and 3.1:** the plain value ahead of an operation map (7504 against 6045), and whether 6045 rejects it.
3. **R3:** which request a failed stream reports (14718 against 5939), and the order or a rewrite of 14718's Limits.
4. **3.5:** the SOLR-6065 status code (500 or 400). Open, and not recorded in `TESTING.md`.
5. **SOLR-13696 batch status:** confirm it is in the batch before 13943 is placed.

Corrections that need no owner call: the 12705 audit note (R4), the stale 5939 and 7504 audit notes (A3, A4), and the SOLR-12245 draft's missing Limits (F19), which the next drafting round should fix.

## Not done

No builds, tests, gate claims, PRs, comments, or submit-branch edits. No draft was edited in this pass. `cfb8f96c4c3` (29 receipt backfills) was not reconciled against the drafts here.
