# Pool assignment: moved fork heads and placeholder text, round 1

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: two read-only subagents, about three tasks each. Claim: `claims/pool-moved-heads-round-1.md`.

## Purpose

A ls-remote sweep of the fork's `solr-*-submit` branches against the drafts found four drafts whose fork branch tip is not the head the draft names, and one bracketed placeholder:

- `pr-drafts/solrj/SOLR-10364.md` names `efb1e6717f9`. The fork branch is at `502bdbf033fa`.
- `pr-drafts/solrj/SOLR-11356.md` names `6120dae28d04`. The fork branch is at `8474e5a3a26d`.
- `pr-drafts/security/SOLR-12161.md` names `cdd265168a2a`. The fork branch is at `1725cbd84898`.
- `pr-drafts/core-admin/SOLR-15805.md` names `3432f950f0ae`. The fork branch was force-updated to `6d78603d825`, which has the same tree.
- `pr-drafts/query-parsing/SOLR-12212.md` line 27 carries `[per-class count to fill from the run log]`.

For SOLR-10364, SOLR-11356 and SOLR-12161 the named heads could not be fetched from the fork by SHA, so they are no longer reachable there.

## Checks

1. SOLR-15805. Confirm that the tree at `6d78603d825` equals the tree at `3432f950f0ae` (already done: `23254470611802b3c2bac0bbde19733e1531ac69`). Confirm that every link naming `3432f950f0ae` resolves at `6d78603d825` with the same line numbers. Confirm the receipt body gives a run at `6d78603d82` on 2026-10-12 (GATE START 2026-10-12T17:49Z, GATE END 2026-10-12T18:05:22Z), and state the exact before and after for the head swap and the date.
2. SOLR-12212. Read the receipt for the per-class counts and say whether the body gives them as a run result or only as a takeover-log citation. Recommend the exact sentence: keep the count clause if the body supports it, otherwise delete the bracketed clause and keep "One run of 80 focused tests, 0 failures."
3. SOLR-10364, SOLR-11356, SOLR-12161. For each, read the draft and the receipt for the named head, the gate date, and whether the receipt says the gate ran on a packaged or local head that was never pushed. State whether each draft is HOLD (named head not reachable), and what the main side must provide before publication.
4. Scan for the same kind of problem elsewhere. Search the receipts for any other head named as a packaged or local head that is not in the local object store and not on the fork. List each ticket and its draft, if any. Do not fetch or check out anything beyond `git cat-file -e`.

## Ladder

Information first from the receipt body and the object store. Where the body gives a run for the live tip, the mechanical head swap is allowed. Where the only evidence is for an unreachable head, hold the draft and record it. Do not carry a count or date from a takeover log or a ledger citation into a draft.

## Hold rule

A draft that names a head that is not the live fork tip waits for a confirmed swap or is held. The swap for SOLR-15805 is allowed only after a receipt body gives the run at the new head.

## Rules

- Read-only subagents. No builds, no tests, no Gradle, no posting, no PR or comment, no force push, no edits to submit branches.
- Proof comes only from the named receipt body and the object store.
- No em dashes or en dashes in authored text. No PR numbers in file names or commit messages.

## Deliverable

`reports/moved-heads-round-1.md`, verdict first, with the exact before and after text for each draft change and a HOLD list. Mark the claim DONE in the same push as the deliverable.
