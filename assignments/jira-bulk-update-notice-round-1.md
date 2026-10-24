# Assignment: dev-list notice for the planned bulk JIRA update, round 1

Status: OPEN

## Background

Nick plans to submit bulk JIRA comments that update backlog tickets. Two things about the plan matter to the dev list:

- Anyone who relies on "order by updated DESC" in JIRA (My Open Issues, or the active-ticket views) will see large changes at the top of their lists. The owner recommends that people download a list now, and/or download a later list and filter out the days the bulk changes were made.
- Rough plan, as the owner wrote it:
  1. Non-Bug tickets: CLOSE or mark FIXED for end-of-life versions.
  2. Non-Bug tickets: "???". The owner has not decided this category yet.
  3. Fill out the list.

Nothing in this assignment posts anything. The owner approves any dev-list message and any JIRA comment before it goes out.

## Work

Draft a short dev-list email in `drafts/jira-bulk-update-notice-draft.md`, written as Nick (first person, "I"). It should:

- say what is planned, in plain words, without internal process terms (no "gate", "receipt", "ledger", "audit", "pool", "claim");
- give the sort-order warning and the owner's recommendation (download a list now, and/or filter later downloads by date);
- name the categories in the plan. For category 2, leave a visible placeholder such as `[CATEGORY 2: owner to decide]`. Do not invent it;
- say how people can reply or object, and that the owner will post the comments on a stated date (leave the date as a placeholder).

## Rules

- Draft only. Nothing is posted to the dev list, to JIRA, or to GitHub. The owner sends it himself.
- Do not name EOL versions unless the list is checked. Use a placeholder such as `[EOL VERSIONS: list to confirm]`, unless the list has been verified from `releases/solr/*` tags (read-only) and written in the draft with its source.
- No em dashes. No "we" for the owner plus the agent. No internal jargon.
- Keep it short: one screen, plain language, with a clear first line saying what is changing.
- Do not state a count of affected tickets unless it has been counted from JIRA in a read-only query, and then say how it was counted.

## Decisions

- Decision 1: category 2 is the owner's call. The draft keeps the placeholder.
- Decision 2: the post date and the list of EOL versions are placeholders until the owner gives them.
- Decision 3: no JIRA comment drafts in this round. Those follow once the owner has picked the categories.

## Capability tags

`draft`, `records`.

## Deliverable

- `drafts/jira-bulk-update-notice-draft.md`

## Deadline

None set by the owner. Deliver before any bulk JIRA post goes out.
