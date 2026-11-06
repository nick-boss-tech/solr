# Review worksheet

Record PR/ticket identity, repository, assessed head/base, source of discussion/inline threads, reviewed files and earlier review links.

| Dimension | Questions to resolve |
| --- | --- |
| Correctness | What invariant changes? Which success, failure and boundary paths establish it? |
| Architecture | Does the change fit lifecycle/ownership and related consumers? Are API additions justified? |
| Maintenance/reuse | Is there existing code or fixture infrastructure to reuse? Is the scope coherent? |
| Compatibility/security | What public behavior, format, default or supported trust boundary changes? |
| Verification | What assertions distinguish the intended behavior? Which required proof legs ran at this head? |

Each finding includes severity, bounded source citation, concrete trigger/result, consequence, and the smallest useful repair or evidence request. Avoid severity based on speculation or merely missing tests. Keep suggestions and maintainer choices distinct from demonstrated defects.

Verification ledger fields: required leg, exact input head, executed command, environment, outcome/counts/skips, log/XML locator, remaining limit. Missing execution is unknown. Setup failures and skipped tests are not successful runtime negative proof.

Conclude with the assessment's actual coverage and unresolved items. Closure of previous findings alone supports only that closure. In this workspace, `research/research-note-review-guide.md` and `research/branch-reviews/REVIEW-COMMANDS.md` provide the canonical detailed procedure; use their current versions rather than copying old per-ticket commands.
