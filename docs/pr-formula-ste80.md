# PR formula supplement: review comments, plain English and related work

Apply this supplement with the historical PR formula and the current test-adequacy guidance. User instructions and the live workspace rules take precedence. This is guidance for PR descriptions and review comments, not text to paste into a PR.

## First-touch reviews of another contributor's PR

Start with the behavior and its impact. Put each actionable code finding in its own inline comment on the relevant changed line. Keep separate findings in separate threads. Use a top-level comment for a short overall summary or a concern that spans changes and has no useful line anchor.

State what you observed and why it matters, then ask about the intended behavior. Keep the first comment focused on the finding and leave room for the author to work through it. A brief offer to help is enough.

Do not attach a prepared branch, diff, or merge commands to a first-touch review, including when a patch is already tested. Wait for the author to engage, ask for help, or agree on a direction before preparing or offering code. A green gate verifies a patch; it does not establish that the author wants to take it.

Once the author engages, a focused patch can be useful. Tie any follow-up offer to the author's request or agreed direction. Verify the live PR head first, and state the exact tested head, checks, limits, and relevant commands. For multi-sentence GitHub comments, use the header `🤖 *AI text below* 🤖` without a parenthetical reviewer attribution.

GitHub supports inline comments through its Pull Request Review Comments REST API. I verified a request to `POST /repos/{owner}/{repo}/pulls/{pull_number}/comments` using `commit_id`, `path`, `line`, and `side`; the read-back identified it as a line comment. Anchor comments to a changed line in the current diff, and refresh the PR head before posting.

## Plain-English pass

Apply the STE80 Humanizer skill when available. Use its default two-pass method: remove stock AI phrasing, then use short sentences, active voice, simple tenses and one idea per sentence. Use familiar words without changing technical meaning. Explain terms that a reviewer needs to understand the change. Vary sentence length where useful. Do not claim compliance with ASD-STE100.

Do not use the word “we” in PR descriptions. State the behavior, recommendation or person responsible directly. Keep the required AI header and responsibility footer.

Before and after the pass, check every claim against the source. Preserve uncertainty, failure conditions, test counts, dates, tested heads, commands, names and links. Never turn a pending or partial check into a passing result. Do not rewrite code, identifiers, commands, URLs or quoted text. If the skill is unavailable, follow these rules directly and say so in the internal handoff when relevant.

Keep the formula's sections, a real choice where needed, the closed verification block, limits and disclosures. Keep proof tied to the tested head. Follow the current live rules if older formula examples conflict with them. Simple language does not justify dropping evidence or limitations.

## Related work for multi-part changes

When it helps reviewers understand the scope, add a Related work section after the choice and before Limits. Use a brief table for merged prerequisites, the current PR, and relevant open or proposed follow-ups. For each item give its status, scope and relationship to this change. Do not describe a prepared branch as an open PR or invent an issue number.

Link follow-up code to a comparison with upstream main or the current PR head, not just a branch file listing. Check that the comparison resolves and describes the intended changes. If it includes an older baseline or unrelated commits, say so and do not imply it is a clean submission diff. Verify live issue and PR status before publishing. A prepared branch is a candidate, and an older receipt proves only its recorded head. These links do not authorize public posts or body edits.
