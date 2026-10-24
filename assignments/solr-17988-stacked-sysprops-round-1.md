# Assignment: SOLR-17988 stacked sysprops PR (owner decision, 2026-10-24)

- Status: OPEN. Intended host: any pool Linux host with the `implementation` or `review` tag. Depends on `assignments/solr-17988-subtasks-round-1.md` ST7 landing first.
- Owner decision (Nick Shanin, 2026-10-24): the ST6 recommended minimum does NOT go into the ST7 branch. ST7 assembles gaps 1 to 7 only, keeping PR1 narrow. The ST6 minimum goes into a stacked PR instead.

## Scope

1. After ST7 lands `solr-17988-docs`, cut the stacked branch from it (working name `solr-17988-docs-sysprops`).
2. Add the ST6 recommended minimum doc entries on that branch: 23 properties recommended in the ST6 section of `reports/solr-17988-subtasks-round-1.md`, minus the 2 already covered by ST5, so 21 net entries. The remaining 30 properties stay a named follow-up; no branch for them.
3. Run the Ref Guide build validation on the stacked branch and record the result and the final head in the report.
4. Write the PR draft under `pr-formula.md`. The draft states the stacking plainly: this PR targets the `solr-17988-docs` branch, its diff is only the sysprop entries, and it is reviewed after PR1.

## Limits

- Docs only, same as the parent assignment. No code or script changes.
- Opening the stacked PR waits for the owner's go, and for PR1 to be open.
- Commit as Nick Shanin with no trailers. No em dashes in the doc text.
