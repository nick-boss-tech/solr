# gradle-queue

The gate lane channel for the Solr contribution queue. A second
Linux VM runs full gates here in parallel with the original queue
(VM1). This branch is the only shared surface between the two
gate lanes: claims, the queue inventory, and gate results.

Layout:

- `claims/<slug>.md`: one gate lane per branch. Create the file
  and push before starting a gate; a rejected push means the
  branch is already claimed. Slug = branch name with `/`
  replaced by `-` (for example `solr-11479-submit`).
- `inventory/*.tsv`, `inventory/*.md`: the queue and its handoff
  notes. Written by VM1 (the master record keeper) or triage.
- `results/<slug>.md`: one gate result per branch, written by
  the lane that ran the gate. Only that lane edits its result
  file.

Hard limits for the gate lane on this branch:

- Gates are read-only against the submit branches: no commits,
  no pushes to them, no GitHub or Jira writes. Pushes go to
  `gradle-queue` only.
- Commit identity is the ICLA identity (Nick Shanin
  <nick.boss.us@gmail.com>); no Co-Authored-By trailers.
- A gate failure is reported in the result file, never fixed on
  this lane. Fixes belong to the side that holds the branch.
- One Gradle build at a time on this VM.

The full gate spec, evidence format, and coordination rules are
in `inventory/handoff-2026-10-08-gradle-queue.md`.
