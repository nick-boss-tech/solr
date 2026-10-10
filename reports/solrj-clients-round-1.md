# SolrJ and clients round 1: round roll-up

Claim: `claims/solrj-clients-round-1.md` (commit `5bbcedf2d1e`). Assignment: `assignments/solrj-clients-round-1.md` (commit `dd6c4c7e397`). Part reports: `reports/solrj-clients-round-1-s1.md` through `-s6.md`. Drafts: `pr-drafts/solrj/` (sixteen files).

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Six subagents in parallel, split by ticket cluster, within the cap of six. No build, Gradle run, or test was run. No branch, live PR, JIRA item, or comment was touched. Nothing was posted. The receipts were not edited.

## Heads

All twenty-four live tips match the assignment's expected heads (`ls-remote`, 2026-10-10). The four audit-only tips match their receipts. The merged ticket's head matches PR 4965's head. Origin tracking refs were used throughout, because several local branch refs point elsewhere (part S5). Which fork ref is authoritative should be confirmed before any push.

## Verdicts

| Ticket | Verdict | Draft | Owner decision |
|---|---|---|---|
| SOLR-2018 | Draftable. Documentation only. | `SOLR-2018.md` | Amend a ref-guide line on the branch before posting (a commit) |
| SOLR-3498 | Audit only. The premise holds on the wire for `ContentWriterUpdateRequest`. | none | Scope: one class or all three |
| SOLR-3722 | Draftable. Skips null elements. | `SOLR-3722.md` | Confirm skip versus fail (recommended: skip, as implemented) |
| SOLR-3999 | Draftable. Forward-only fix. The reporter's own case stays broken. | `SOLR-3999.md` | Align the changelog wording; confirm the PR is still worth submitting |
| SOLR-4335 | Draftable. Writes `#65534;` and `#65535;` (no ampersand, as the code does). | `SOLR-4335.md` | Widening to the XML response and delete-request writers |
| SOLR-4336 | Draftable. Blank numeric parameters are unset. | `SOLR-4336.md` | Run date; changelog wording |
| SOLR-4422 | Draftable. Field order follows the document. | `SOLR-4422.md` | Keep the "fill the existing map" alternative in Limits (recommended) |
| SOLR-4424 | Draftable. Unnamed entries are rejected at conversion. | `SOLR-4424.md` | Keep the conversion-time check; empty name as a Limit |
| SOLR-5220 | Draftable. Only 403 and 404 stop marking the server a zombie. | `SOLR-5220.md` | Confirm the 403 and 404 scope; add a Choice for stopping the retries altogether |
| SOLR-6046 | Draftable. Array operands expand to one element each. | `SOLR-6046.md` | Confirm the SOLR-6045 pull request number before posting |
| SOLR-7709 | Draftable. Repeated fields merge in stream order. | `SOLR-7709.md` | Rebase before submission; the double-id commit subject |
| SOLR-8536 | Draftable, with branch fixes owed (changelog title, commit subject). | `SOLR-8536.md` | Squash internal commit subjects; the Choice wording |
| SOLR-10198 | Merged into main (squash `e97c2a0884c`). Consistent. | none | Delete the merged fork branch (public) |
| SOLR-10364 | Audit only. Main already binds collections and arrays; `Set` is the real gap. | none | Is a `Set`-support PR worth it? Remove the stray TESTING file |
| SOLR-11356 | Audit only. The stream reuse still exists on main. | none | Copy the headers in the key; decide on the user principal |
| SOLR-12094 | Draftable. Misplaced mapped input raises an error. | `SOLR-12094.md` | Squash the handoff commit subjects |
| SOLR-14187 | Audit only. Only the static three-argument helper drops credentials. | none | Accept an inconclusive Proof; change the changelog type to "added" |
| SOLR-14298 | Draftable. The zombie-check query is tagged. | `SOLR-14298.md` | Confirm the Choice; the attribution to Noble Paul |
| SOLR-14967 | Draftable. Caller `_stateVer_` handling. | `SOLR-14967.md` | Correct the receipt; a test helper change if 18341 lands second |
| SOLR-15823 (submit) | Live PR, consistent. The gate move is one docs sentence. | none | Update the PR's Proof head (public) |
| SOLR-15823 (levels) | Live PR, stacking stated loosely. | none | Restack onto the submit branch; retarget or keep the base (public) |
| SOLR-17866 | Draftable, flagged for the owner. The design choice is open. | `SOLR-17866.md` | Check upstream first; decide on the sticky flag and the side effects |
| SOLR-18129 | Live PR, head consistent. The receipt omits a title commit. | none | Confirm the gate head on the main side |
| SOLR-18341 | Drafted, not submit-ready. | `SOLR-18341.md` | Five decisions (see below) |

## Drafts written

Sixteen drafts in `pr-drafts/solrj/`. Lengths with links, from the part reports: 3722 (3,424), 2018 (3,783), 3999 (3,712), 4335 (4,666), 4336 (3,080), 4422 (3,252), 4424 (3,999), 5220 (3,501), 6046 (4,169), 7709 (2,982), 8536 (4,114), 12094 (4,340), 14298 (3,733), 14967 (3,645), 17866 (4,986), 18341 (9,683).

Every draft is above the roughly 3,500 guide or close to it, except 3722, 4336, 7709 and 14967. The link targets drive most of the length. The owner decides whether to trim.

Each draft names its head. Each has no dashes and no process words (checked by search in each part; re-checked here for the whole folder).

**Notes per draft that affect submission:**
- **SOLR-2018.** The branch is not one javadoc clarification in `UpdateRequest`. It is eight `@param` lines in `SolrClient.java`, one ref-guide line, and a changelog. The ref-guide line still says "Solr ignores this option" with a value that is not passed on. The line needs a branch commit before posting.
- **SOLR-4335.** The receipt and the assignment say `&#65534;`. The code writes `#65534;` (no ampersand), so the output is plain text. The draft follows the code. The writer change also reaches the XML response writer, the schema XML writer, and the delete-request writers. The draft states this.
- **SOLR-6046.** The draft refers to SOLR-6045's pull request number 5077, which was taken from a report and not checked live.
- **SOLR-8536.** The changelog title and one commit subject overstate ("strip" where the code replaces with spaces; control characters beyond ASCII still pass through). The draft states the ASCII-only limit.
- **SOLR-14298.** The draft attributes the settable-query suggestion to "Noble Paul", the name in the Jira comment. The receipt says "Paul Noble".
- **SOLR-14967.** The receipt misdescribes the discriminating run (a tree that is not a branch commit) and the retry (the test asserts no `_stateVer_` on the retry; the first attempt carries the computed value). The draft follows the code.
- **SOLR-17866.** The draft states two side effects that the receipt does not record: the sticky flag feeds the cloud and load-balancing decisions for admin-typed requests, and the wrapper does not forward the new hook. The design choice is not decided.
- **SOLR-18341.** Not submit-ready. See the owner decisions.

## Landing orders

- **NamedList:** 3722, then 4424, then 7709. SOLR-4336 is independent. The second landing of `NamedListTest` gets one conflict hunk (keep both methods). A `NamedListTest` run on the landed tree (7 tests) is owed.
- **DocumentObjectBinder:** 4422 before 10364. One import-block conflict (keep both import sets).
- **LBSolrClient:** 5220 before 18341 (required: the 403 and 404 updates would otherwise mark a server down). SOLR-14298 is independent. The three merge clean in every order.
- **CloudSolrClient `_stateVer_`:** 14967 and 18341 merge clean, but the 14967 test helper uses `UNSPECIFIED`, which 18341 no longer replays. Whichever lands second must set the helper to QUERY. Recommended: 14967 first with that change, then a new run at its new head.
- **XML write path:** 4335 and 6046 are independent and compose through `XML.escapeCharData`.
- **SolrRequest:** 17866 and 18341 merge clean and do not change each other's replay decision. One asymmetry: the wrapper forwards 18341's `isRetriable()` but not 17866's hook.
- **15823 pair:** the levels branch is stacked on the submit branch. The restack conflicts in one test file (`NodeLoggingNodesSolrCloudTest.java`, three hunks), which is main-side work.
- **Shared pairs elsewhere:** no other pair has a textual conflict, except the two named above.

## SOLR-18341: decisions before it can be submitted

The branch is a load-balancing and cloud retry change across fifteen files. Its design record lists five points, each checked against the diff: a 503 route error is not replayed for update-typed requests; the base policy is narrower than "unchanged" for some request types; the invalid-state replay is gated, but the cached state is not cleared on that path; the protected `CloudSolrClient.wasCommError` was removed with no upgrade note; and the connect exemption is in the load balancer only.

Before submission:
1. Remove the SOLR-18368 Javadoc hunk from `CloudSolrClient.java` (line 1550). It is out of scope, and the research note says it was already removed.
2. Competing apache/solr PR 4829 (SOLR-18402): the research note says not to submit separately while it stands. Its live status was not checked. Decide whether to name it, and whether to hold the PR.
3. The load balancer widening (idempotent updates retried after 500, 503, timeout and reset; the sync client fails over on any `IOException`; admin, untyped, security and streaming requests no longer retried). Keep as drafted, add a Choice for the update widening, or restore failover for some types.
4. The invalid-state and 404 path does not clear cached state for non-retriable requests. Fix before the PR (clear the entry, keep the replay gate), or keep it as a Choice.
5. The removed `wasCommError`: keep as a Choice, or add a deprecated delegate and write the upgrade note the record asks for. The changelog type is "changed", and its title does not name the load balancer change or the new public method.

## Receipt and record corrections (main side)

- **SOLR-2018:** the receipt's "one javadoc clarification ... in `UpdateRequest`" (the diff is `SolrClient.java`); the claimed corroboration run is not verified.
- **SOLR-3999:** the receipt's description of the test that constructs each class (only the round-trip test constructs one); the failing test is `testExplicitSerialVersionUid`, which the receipt does not name.
- **SOLR-4335:** the `&#65534;` form, in the receipt and in the assignment's starting state (the code writes `#65534;`).
- **SOLR-4336:** the failing test methods are not named; the "empty message" wording needs a run; no run date.
- **SOLR-4424:** "the check Hoss described, which fires when the XML response is parsed" (the check is at config read, `DOMUtil.addToNamedList`).
- **SOLR-5220:** the scope is 403 and 404, not all 4xx; the gated tree `7650584a756` is not in the repository, so the identity claim is unverified; "the two meet in the same method area" is wrong (they share no method).
- **SOLR-8536:** the first sentence overstates ASCII-only control character handling.
- **SOLR-14187 and SOLR-3498:** the claim wording overstates (only one helper drops credentials; the setter stores the value and is a no-op only on the wire).
- **SOLR-14967:** the discriminating-run description and the retry description (see the draft notes).
- **SOLR-17866:** the flag's stickiness is not recorded.
- **SOLR-14298:** "Paul Noble" should be "Noble Paul".
- **SOLR-15823 (submit, PR 5030):** the Proof says "Verified 2026-10-05 at head 85d8df82502"; the live head is `1f60cd39baae`, one docs commit later. The stacking sentence in the levels description should name the base SHA.
- **SOLR-18129:** the head's last commit `657e443d866` changes the title text, which the receipt does not mention. Whether the round 29 gate ran before or after that commit cannot be settled from git.
- **SOLR-10198:** "merge commit `e97c2a0884cbd`" is a squash commit.

## Owner decisions

1. **SOLR-4335:** keep the widening to the XML response and delete-request writers as stated, or narrow to the SolrJ update writer (this could become a Choice).
2. **SOLR-18341:** the five decisions above, plus the SOLR-18368 hunk removal and the PR 4829 question.
3. **SOLR-17866:** check whether the explicit-constructor work exists upstream before any submission; then decide on the sticky flag and the admin-typed effects; then decide whether to submit ahead of the design conversation. Recommendation: no submission before the upstream check.
4. **SOLR-14187:** accept an inconclusive-by-construction Proof, or restructure the test to use the base API. Change the changelog type to "added".
5. **SOLR-3498:** scope, either `ContentWriterUpdateRequest` only, or all three content-writer classes. Recommendation: all three.
6. **SOLR-11356:** copy the headers in the key (required). Decide on the user principal after checking whether it reaches the wire.
7. **SOLR-10364:** is a `Set`-support enhancement with one reporter worth a PR? If yes, bean `Set` fields only, with `Map<String, Set>` named in Limits, then the gate.
8. **SOLR-5220:** the 403 and 404 scope, and whether to add a Choice for the ticket's proposal to stop retrying those codes altogether (its reporter's own proposal, with a shutdown counter-argument).
9. **SOLR-3999:** align the changelog title ("so Java-serialized documents survive recompiles" reads wider than the draft); confirm the PR is still worth submitting as a forward-only fix.
10. **SOLR-2018:** the ref-guide line amendment (a branch commit), or accept the current wording.
11. **Branch history and commit subjects:** squash or reword the internal commit subjects before opening, on SOLR-8536 and SOLR-12094 (the "handoff" subjects), and remove the stray TESTING files on SOLR-10364, SOLR-11356, SOLR-14187 and SOLR-3498.
12. **SOLR-15823:** restack the levels branch onto the submit branch (a force push to the fork, owner-authorized), or keep the stack on the superseded base and say so. The 5031 base: keep main, or retarget. The 5030 Proof head.
13. **SOLR-10198:** delete the merged fork branch `solr-10198-submit`? (Public-facing only in the sense of the fork state.)
14. **SOLR-18129:** confirm on the main side which head the round 29 gate ran on, or re-gate on an explicit verify request.
15. **Length:** the drafts above the guide (4424, 17866, 18341, 4335, 6046, 8536, 12094, 2018, 3999, 14298, 5220). Trim or accept for complex tickets.
16. **Authoritative fork ref** for the branches whose local refs differ from origin.

## Main-side work owed

- Gate runs where a branch changed after its gate: SOLR-14967 (a new run at the new head if the helper changes), SOLR-14187 (an inconclusive Proof by construction), SOLR-3498 (premise run and first gate), SOLR-10364 (premise run and first gate), SOLR-11356 (a focused run for the race, fail-before, and the copy fix first).
- A `NamedListTest` run on the landed tree (7 tests) after the NamedList landing order.
- Receipt corrections listed above, including SOLR-5220's gated-tree identity.
- The gated tree of SOLR-5220 (`7650584a756`) needs to be re-recorded, or its identity claim dropped.

## Not done

- No build, Gradle run, or test. No `gh` write call. No commit to a submit branch. No live PR edit. No JIRA access.
- The Jira context came from the read-only snapshots in the main checkout, some of them dated 2026-10-04 and possibly stale. Two tickets (SOLR-14967 and SOLR-14187) have no snapshot.
- The changelog files were read by eye, not parsed by a tool.
- One dangling commit object was created with `commit-tree` by part S4 for a trial merge. No refs were written.
