# Comment formula

Goal: a comment on a PR or a JIRA ticket reads the same way every time. The answer comes first, the evidence is short and linked, and the recommendation is clear. Use this with `pr-formula.md`, which covers PR descriptions.

## Header and footer

- Header, on its own line at the top: `🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*`
- Required for any comment longer than one sentence. One-sentence comments stay plain.
- No footer on comments. The footer `*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*` belongs only to PR descriptions and new JIRA ticket descriptions.

## Shape

1. First sentence: the finding or the answer. No preamble such as "Thanks for this" or "Just a thought".
2. Evidence: one or two sentences per claim, each with a link to the diff, run or ticket. Put the scope in the same sentence, for example "on a two-replica collection".
3. Options, only when a real decision exists: numbered, one line each, with the cost of each option stated in the same line.
4. Recommendation: start with "**Recommendation:**" and say what to do and why, in one or two sentences.
5. Bottom line, when there is a recommendation: one sentence the reader can act on. Do not end on a question when the recommendation is clear.

## Words

- No "we", "our" or "us". Write "the branch", "this change" or "the test".
- No internal vocabulary: gates, claims, assignments, receipts, job IDs, pool, hosts, agent names.
- Simple language (the rule in `pr-formula.md`, Nick, 2026-10-08). Short sentences, one idea per sentence, plain words in place of compressed jargon. Say "checked", not "dispositioned"; say "a planted error", not "a negative control".
- Simpler wording never changes a claim. Numbers, counts, hashes and citations stay exact and complete.
- No em dashes.

## Links

- A code change links the compare view (`base...head`), not the branch. A branch link alone is not a diff.
- Another PR is a full markdown link to its URL, never a bare `#NNNN`.
- A JIRA ticket is its full URL.
- A CI run links the job page on GitHub.

## Proof

- State the result and its scope in one or two sentences. Give counts only when the count is the point.
- If a check was not run, say so in one sentence.
- Do not describe internal process steps.

## Length and corrections

- A standard comment is about 1,500 characters. Longer only when the options need the room.
- A posted comment that is wrong or incomplete is fixed by editing it, then read back. Do not post a second comment to correct the first.
- Posting a new comment needs the owner's approval. Editing one that is already posted does not.

## JIRA comments

JIRA comments use the same header and no footer. A JIRA ticket description, filed as new, uses the header and the footer.
