# PR formula supplement: plain English and related work

Apply this supplement with the historical PR formula and the current test-adequacy guidance. User instructions and the live workspace rules take precedence. This is guidance for authors, not text to paste into a PR.

## Plain-English pass

Apply the STE80 Humanizer skill when available. Use its default two-pass method: remove stock AI phrasing, then use short sentences, active voice, simple tenses and one idea per sentence. Use familiar words without changing technical meaning. Explain terms that a reviewer needs to understand the change. Vary sentence length where useful. Do not claim compliance with ASD-STE100.

Do not use the word “we” in PR descriptions. State the behavior, recommendation or person responsible directly. Keep the required AI header and responsibility footer.

Before and after the pass, check every claim against the source. Preserve uncertainty, failure conditions, test counts, dates, tested heads, commands, names and links. Never turn a pending or partial check into a passing result. Do not rewrite code, identifiers, commands, URLs or quoted text. If the skill is unavailable, follow these rules directly and say so in the internal handoff when relevant.

Keep the formula's sections, a real choice where needed, the closed verification block, limits and disclosures. Keep proof tied to the tested head. Follow the current live rules if older formula examples conflict with them. Simple language does not justify dropping evidence or limitations.

## Related work for multi-part changes

When it helps reviewers understand the scope, add a Related work section after the choice and before Limits. Use a brief table for merged prerequisites, the current PR, and relevant open or proposed follow-ups. For each item give its status, scope and relationship to this change. Do not describe a prepared branch as an open PR or invent an issue number.

Link follow-up code to a comparison with upstream main or the current PR head, not just a branch file listing. Check that the comparison resolves and describes the intended changes. If it includes an older baseline or unrelated commits, say so and do not imply it is a clean submission diff. Verify live issue and PR status before publishing. A prepared branch is a candidate, and an older receipt proves only its recorded head. These links do not authorize public posts or body edits.
