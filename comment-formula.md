# Comment formula (Nick, 2026-10-23)

Comment shapes for Jira and GitHub. PR descriptions follow pr-formula.md; this doc covers comments only, plus Jira ticket descriptions.

## Disclosure, split by platform (Nick, 2026-10-23)

The disclosure wording is the same everywhere: AI text, reviewed by Nick Shanin. The placement and weight differ by platform.

- **GitHub comments: prominent header.** The comment opens with:

  🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

- **Jira comments: unobtrusive footer.** No header. The comment closes with one line, set off by a blank line:

  🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

- **Jira ticket descriptions: footer only.** Same closing line as Jira comments. No header in Jira, anywhere.

The disclosure appears on comments longer than one sentence. A one-sentence comment carries no disclosure line on either platform.

Already-posted text stands; the split applies from 2026-10-23 forward.

## Markup

- Jira text is Jira wiki markup: code spans {{like_this}}, links [label|https://...], mentions [~username], bullets start with ` * `.
- GitHub text is GitHub markdown.

## The ask comment (the typical Jira comment)

The typical Jira comment opens a conversation in stages (Nick, 2026-10-23). It does not ask for a review commitment; that ask comes only after the other side has engaged.

1. **Run some test.** The comment leads with evidence we produced or verified: a reproduction, a test run, a checked claim. Stated plainly, with the run or artifact linked.
2. **Ask if it is sufficient.** One question, about the evidence: whether it meets the ticket's bar. That judgment is the owner's; it is the one question the comment asks.
3. **Pose the decision ladder.** The possible states of the ticket, listed as a ladder the owner can step down: what closing now rests on, what staying open would still require, what a follow-up would carry.
4. **Recommend the scope.** Our recommendation, stated as ours: which rung we would take and why, in one or two sentences.
5. **Wait for engagement.** No review ask in this comment. If the owner engages and work follows, willingness to review is asked then, and reconfirmed when a PR is posted.

Companion rules:

- The slice and the files still belong in the comment when work is being proposed: the slice in one or two sentences at the level of behavior, and the files named as an expectation ("the files I expect to touch are ..."), so the reader can correct the scope from memory: add a file, remove one, name a different layer.
- No branch or diff link by default. A linked diff turns the proposal into finished work asking for review labor before anyone agreed to spend it, and moves the conversation from direction to implementation defense. Exception: when the fix is mechanical enough that the diff is shorter than the argument about it, a diff link may go in the footer, after the question.
- The addressee is a named mention of the ticket's sponsor candidate: the committer whose ticket it is or whose recent activity makes them the sponsor. On the new UI tickets this is Christos (malliaridis).

## The status note (evidence comment)

For reporting a finding on a ticket (a stale ticket that looks complete, a failure that reproduces, a check someone asked for):

1. Disclosure per platform.
2. The facts, each checkable: versions, dates, commit hashes, run links, counts. State plainly what was verified and what was not.
3. The conclusion the facts support, no stronger. Where part of the claim is unverified, say which part.
4. End with a question to the ticket's owner or last active committer when a decision is theirs (close, re-scope, confirm).

## Review replies (GitHub)

- Answer the question that was asked, at the length the question sets. A yes/no question gets a one-line answer; background lives in the PR description.
- When a maintainer asks how the code works, give a complete, precise answer with citations instead.
- Edit an existing reply rather than adding a new comment when correcting one.

## Standing text rules

- No internal process vocabulary in public text: no gate, receipt, ledger, rung, or similar. State plainly what ran and what passed.
- Authored text never uses "we" for Nick plus the agent; write as "I" or neutrally.
- Friendly tone, never snarky.
