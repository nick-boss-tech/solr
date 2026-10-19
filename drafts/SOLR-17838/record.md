# Skip record: SOLR-17838

- Ticket: SOLR-17838, "SIP-25: Encryption Module". JIRA status Open, resolution none, created 2025-08-06, last updated 2026-01-01, checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-17838-draft-open.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

An umbrella issue for the SIP-25 Encryption Module proposal. The description links the wiki page and a mailing-list thread, and asks for sub-tasks or linked JIRAs. It names no symptom, no code path, no test, and has no comments and no linked issues. The audit-5 note (2026-10-17) reads it the same way: feature tracking, not a bug report.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `git ls-tree -d solr/modules/` lists no encryption module (no directory matches `encr` or `crypt`).
- `git grep -il encrypt -- solr/` returns only unrelated code and docs: the DIH password encryption option (`solr/core` and the DIH docs), PDF and DOCX password handling in `solr/modules/extraction` (`ExtractingParams.java`, `ExtractionRequest.java`, and their tests), JWT well-known config test files, and the enabling-SSL and securing-solr reference pages.
- No encryption-at-rest or encryption-of-index code exists at the pinned head.

## History

- `git log --grep` for SOLR-17838, SIP-25 and "encrypt" on the pinned head finds commits only for DIH password encryption (`a37c4b5ff1`, `d997e8b4a2`, `c2aea1b803`) and extraction library upgrades (`19cb7404f5`, `dd09f0f42b`). None names SOLR-17838 or SIP-25, and none adds an encryption module.

## Reproduction attempts

Not attempted. The ticket asks for sub-tasks and links, not for a behavior. There is no failure to reproduce.

## Gap analysis

The ticket is a feature proposal tracker. Its status depends on the SIP-25 decision (wiki and mailing-list thread) and on whether the maintainers want a single umbrella open while sub-tasks are created. The SIP status was not checked here: it is on the Confluence wiki and the mailing-list thread, and this closeout read only the JIRA record and the code.

## Verdict justification

None of the rubric verdicts fits a feature umbrella. close-obsolete and close-fixed do not apply: the feature was never in the code, and nothing was removed. close-cannot-reproduce and pr-needed need a reproduction or a code-path failure, and the ticket names neither. keep-open is the closest verdict: the record names the attempts (JIRA reading, module and grep checks at the pinned head, history search) and the missing piece (the SIP-25 status and a maintainer decision on the umbrella). The keep-open minimum is met.

## Limits

This record does not check the SIP-25 wiki page or the mailing-list thread. It does not establish whether an encryption module is planned for a release. It does not run tests.
