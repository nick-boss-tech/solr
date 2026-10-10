# Claim: Configsets and config API round 1

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: `assignments/configsets-round-1.md` (commit `ecf98d6e72a`). Seven tickets: SOLR-6960, SOLR-7267, SOLR-7323, SOLR-13706, SOLR-15478, SOLR-17363, SOLR-18178. Audit first, then drafts for the draftable tickets under `pr-drafts/configsets/`.

Heads checked live on 2026-10-09 with `git ls-remote`, and fetched read-only:

| Ticket | Branch | Live head | Named head in the assignment | Result |
|---|---|---|---|---|
| SOLR-6960 | `solr-6960-submit` | `9bef536fc12a3c5a1718f22b11cffa348e57a433` | `9bef536fc12` | matches |
| SOLR-7267 | `solr-7267-submit` | `59f34a339b0d3cc227c79e56bc2e59814fed2daf` | `59f34a339b0` | matches |
| SOLR-7323 | `solr-7323-submit` | `1f5b0f2c82d36ac41446fb55ee18a9c2e336f8e4` | none named | moved from `fb034dc5f87` during this check; audit flags it |
| SOLR-13706 | `solr-13706-submit` | `590dd5c24d97c5c1b8f2ed262037c0a9c4ba6f77` | `590dd5c24d9` | matches |
| SOLR-15478 | `solr-15478-submit` | `0478bdf0ac5cd100d5020451a6732ca5cbaa1a52` | `0478bdf0ac5` | matches |
| SOLR-17363 | `solr-17363-submit` | `b8e8e1c48461be964ac0ef9a0cc570c277e7420d` | `b8e8e1c4846` | matches |
| SOLR-18178 | `solr-18178-verify` | `b75e7d4d3c464ceeb9f14fdaf7711212adcccac2` | `b75e7d4d3c4` | matches |

SOLR-6960's top-up of the added test is a main-side run. The draft names it as pending until the main side confirms it.

Split: three subagents, read only, about four tasks each.
- Part A: SOLR-6960 draft; SOLR-7267 draft; SOLR-7323 audit (no draft); the SOLR-7323 and SOLR-18178 `FileSystemConfigSetService.java` check.
- Part B: SOLR-15478 draft (note the pairing with SOLR-15674); SOLR-17363 draft with a Limit for the stale-replica gap, as an owner decision; the SOLR-17363 and SOLR-18129 `/config` check.
- Part C: SOLR-13706 consistency pass; SOLR-18178 consistency pass; the SOLR-6960 and SOLR-13706 `SolrConfig.java` overlap check.

Read-only GitHub calls for the consistency passes: `research\gh.ps1 pr view` on the two live PRs. No `pr comment`, `pr edit`, or other write.

Output: each part writes `reports/configsets-round-1-a.md`, `reports/configsets-round-1-b.md`, and `reports/configsets-round-1-c.md`. The lead writes the round roll-up `reports/configsets-round-1.md`, with per-ticket verdicts and owner decisions.

Not in scope: opening PRs, posting comments, editing submit branches or live PR descriptions, builds, Gradle, and test runs. Reading files, `git show`, `git log`, `git diff`, `git rev-parse`, `git cat-file -e`, `git grep`, and read-only `gh pr view` are allowed. SOLR-15674 and SOLR-18129 are not audited; they are only noted where a diff shows an interaction.
