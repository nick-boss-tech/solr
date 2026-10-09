# pr-prepare

Working branch for preparing Solr pull requests by code area.

Contents:

- `pr-formula.md`: the formula every PR description draft on this
  branch follows.
- The inventory document is added by Nick from the Windows side.
- `TESTING.md`: decisions taken for the batches, with the
  recommendations adopted and the tough calls flagged for
  discussion.

How the work runs here:

- Branches are categorized by code area, not by status.
- For each code area: one review of the branches in that area, and
  one PR description draft per branch that is headed for a PR,
  written to `pr-formula.md`.
- Proof numbers in a draft come only from a gate receipt. A branch
  with no gate yet gets its Proof section marked as awaiting the
  gate instead of filled in.

Standing rules for this branch:

- This branch is public on the fork. Write everything here as
  publishable.
- Commit messages name ticket keys only; no PR numbers.
