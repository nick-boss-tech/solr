---
name: Skip Ticket Evidence
description: Gather the evidence for one Solr skip-queue ticket (JIRA record, comments, fork history, named test classes, code identifiers) as markdown, to start a skip record. Use when working a skip-queue ticket for a verdict.
metadata:
  version: "1.0.0"
---

# Skip Ticket Evidence

Collects the first evidence pass for one skip-queue ticket. It reads JIRA anonymously, searches the Solr repository history, finds test classes named in the ticket, and counts code identifiers. It does not reproduce anything and it does not post anything.

## Usage

Installed copy after the tick's sync: `/workspace/.claude/skills/skip-ticket-evidence/`. Source on `coordination`: `pool/skills/skip-ticket-evidence/`.

    python3 -I /workspace/.claude/skills/skip-ticket-evidence/evidence.py SOLR-13066 --repo <your pool worktree> --ref origin/main

Options:
- `--repo`: a Solr checkout or worktree with the fork history (default `/workspace/solr`).
- `--ref`: the pinned head to search (default `origin/main`). Record the sha you pin.
- `--commit`: an optional fix commit. The output says whether it is an ancestor of `--ref`.

## What the output covers

1. The JIRA summary, status, resolution, fix versions, and issue links.
2. The description (first 4000 characters) and every comment (first 2000 characters each). A cut-off text ends in `...`; read the ticket in JIRA when it does.
3. Commits on `--ref` whose messages name the key.
4. Test classes named in the summary or description, with the file path on `--ref` and the last two commits to that file.
5. Identifiers in backticks, with the number of matching lines under `solr/` on `--ref`.

## What it does not do

- It does not decide a verdict. Read the output and write the record using `material/skip-audit/templates/record.md` and the rubric in `material/skip-audit/verdict-rubric.md`.
- It does not run tests. Reproduction is a separate step recorded in the record.
- It reads JIRA over the network. If the JIRA fetch fails, the output says so and the rest still runs.
