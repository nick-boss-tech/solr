# Assignment: first-contact offer for NVIDIA/cuvs PR 2970, round 5 (drafts only)

Status: OPEN (drafts only). Posting anything upstream is NEEDS-OWNER: Nick approves the exact target, text, header and delivery under step 04 before any post.

- Intended host: any host with the `draft` and `records` tags. Not `vm6` for the drafts if another host is available, since `vm6` wrote the port; the re-review in the Not in scope section must come from a host that prepared neither the port nor these drafts.
- Capability tags: draft, records.
- Claim: `claims/cuvs-lucene-pr2970-first-contact-round-5-<host>.md`, after reading this assignment and the listed inputs.
- Gate dependency: the port's CPU proof, `gates/cuvs-lucene-pr2970-port-round-5.md` (OPEN, not yet claimed). Drafting does not wait for it. Posting does: the comment may cite evidence only after a GREEN receipt exists for the pinned package.
- Pinned package: `material/cuvs-lucene-pr2970-port-round-5/`, board commit `57e8a6c989ec63c306d571cd85435d38403cfdce`; `port.patch` SHA-256 `3185ea28f53520e9fd1e8b72d83b15d3f912c2cb88b6591c3c97989d19a8c36d`.
- Upstream target: NVIDIA/cuvs PR 2970 "Add support for Lucene 10.3" (author imotov), head `3602ef0a8047ef222ad994a316d69ffec26e98bd`, open, two approvals. Re-check the head read-only before writing; if it has moved, record the new head and stop.
- Context: `reports/cuvs-lucene-readiness-review-round-4.md` (findings T1 to T5), `reports/cuvs-lucene-pr2970-port-round-5.md`, `drafts/cuvs-lucene-first-contact-round-4-revision-1.md` (the PR 2702 text this replaces), `comment-formula.md`.

## Objective

Write the first-contact comment for PR 2970, and the technical notes for after an engaged reply, so that Nick can approve (step 04) a comment that offers our tests as a small follow-up PR once 2970 merges. The comment asks one question and does not ask for review work or a date. The patch is not in the first comment.

## Decisions already made (do not reopen)

1. Target is PR 2970 at its current head. Option 1 of the round 4 review, approved by Nick on 2026-11-03.
2. Framing is a follow-up after 2970 merges. Do not ask for the tests to be added to 2970 while it is in review.
3. No diff, patch, attachment, or fork branch in the first comment. The comment closes with an offer: "I can send the patch if that is useful." The diff is sent only after the maintainer engages, with the sentence "you are welcome to take or adapt the changes." Reasons: `comment-formula.md` gives no diff link by default; the proof is not GREEN yet; and the base moves until 2970 merges. Delivery of the patch file or a fork branch needs Nick's direction under step 04.
4. Motivation: one sentence may say Solr runs on Lucene 10.4 and downstream users need cuVS artifacts that match it. Nick's approval is needed for this sentence; it is written as optional in the draft, with the text in brackets.
5. No review request, no reminder, no date, no "please" stacking. Friendly tone.

## Work

1. Read `comment-formula.md`: the ask-comment shape (stages 1 to 5), the GitHub disclosure rule (header only), the no-diff default, and the standing text rules (no internal process words such as gate, receipt, claim, rung, board or host names; "I" not "we"; no em or en dashes).
2. Read the round 4 review findings T1 to T5 and make sure each is fixed in the new text:
   - T1: the old conditional "if #2867 lands first" is stale, because 2867 merged on 2026-10-29.
   - T2: the old question asked for inclusion in PR 2702, which is the umbrella being split.
   - T3: the old technical notes cite the old 2867 head and blob.
   - T4: the disclosure form. A GitHub comment carries the header only; drop the footer line.
   - T5: "reviewed by Nick Shanin" stays proposed until Nick reviews the posted text.
3. Draft `drafts/cuvs-lucene-pr2970-first-contact-round-5.md`, following the ask-comment shape:
   - Stage 1, evidence: the four test files in PR 2970 terms, the two Lucene versions (10.2.0 and 10.3.2), the dropped-mask check (the three new query tests fail when the filter is dropped, and the original passes), and the archived-index upgrade with deletes and sparse vectors (183 live documents and 173 vectors checked, on the seven released fixtures). Cite the gate receipt only after it is GREEN; until then the sentence is a bracketed placeholder.
   - Stage 2, one question about scope: whether the tests belong in a follow-up PR once 2970 merges. One sentence ending in a question mark. Nothing else is a question.
   - Stage 3, the states the ticket could take, as a short list: merge the tests as a follow-up after 2970; keep them as a record only; or leave them out.
   - Stage 4, our recommendation in one or two sentences: the follow-up after 2970 merges, and why.
   - Stage 5, no review ask.
   - Closing offer: "I can send the patch if that is useful."
   - CPU limits stated plainly: no GPU, no native build, no Solr run; Lucene 10.2 and 10.3 only, since 2970 supports only those.
   - Header: `🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*`. The "reviewed by" wording stays proposed in the file until Nick reviews it.
   - Length: under 250 words.
4. Draft `drafts/cuvs-lucene-pr2970-technical-notes-round-5.md`, for use only after the maintainer engages. Include:
   - Identity: PR head, `port.patch` digest, the four final Git blobs (`f2692a1f698a3974994315772355327edb340530`, `cfcab142c250f96aed4131d61b5056271ac2635f`, `fa04413d7e5537ba8cd03242908d5306c5a7673d`, `9bdc09a5865d21acb40a8812d867bdbc1293c592`).
   - Reconstruction: `git apply --whitespace=error-all port.patch` against the PR head, then the blob check.
   - The diff offer, with the sentence "you are welcome to take or adapt the changes," and the note that the patch applies only to the 2970 head. If 2970 merges, the patch must be re-checked against main before it is offered again.
   - Limits as in the gate.
5. Write `reports/cuvs-lucene-pr2970-first-contact-round-5.md` as the owner packet:
   - How each of T1 to T5 is resolved.
   - Digests of both drafts.
   - Four decisions for Nick: the target; whether the Solr motivation sentence is included; the header wording once reviewed; and the delivery sequence (no diff first, patch on engagement).
   - The posting prerequisites: the gate GREEN receipt, the head re-check, and step 04 approval.

## Deliverables

- `drafts/cuvs-lucene-pr2970-first-contact-round-5.md`
- `drafts/cuvs-lucene-pr2970-technical-notes-round-5.md`
- `reports/cuvs-lucene-pr2970-first-contact-round-5.md`

Completion is stated in the report as DRAFTS DELIVERED, NOTHING POSTED, with the head re-check result and the gate state at writing.

## Acceptance

- The first-contact draft has exactly one question, and it ends in a question mark.
- It contains no diff, attachment, branch link, date, or review request.
- It contains no em or en dash, no "we", and none of the internal words listed in `comment-formula.md`.
- Every claim in it has a source on the board or on the upstream PR, and the gate receipt is cited only if it exists.
- T1 to T5 each have a line in the owner packet saying how they were resolved.
- The earlier drafts (PR 2702 revision 1 and round 4) are unchanged. They are add-only.

## Not in scope

- Posting, attaching, or pushing anything upstream. Step 04 is Nick's.
- Editing the port package, the gate, or any claim other than this assignment's own.
- Running any build, test, or gate.
- Writing the re-review of these drafts. That is a separate assignment, for a host that prepared neither the port nor the drafts, to be posted after these drafts exist.

## Limits

Drafts and owner packet only. Upstream state is a snapshot at the time of writing. If PR 2970 merges or its head moves before Nick approves, the drafts need a revision before posting.
