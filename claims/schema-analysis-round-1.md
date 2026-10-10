# Claim: Schema, analysis and field types round 1

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: `assignments/schema-analysis-round-1.md` (commit `44bae5cc588`). Eleven tickets: SOLR-9349, 10131, 10403, 14199, 15357, 15358, 15712, 15945, 16977, 17047, 18134. Audit first, then drafts under `pr-drafts/schema-analysis/` for the draftable tickets.

## Heads checked live on 2026-10-09

`git ls-remote`, then a read-only fetch of every branch. Named heads from the assignment match the live heads.

| Ticket | Branch | Live head | Named in the assignment | Result |
|---|---|---|---|---|
| 9349 | `solr-9349-submit` | `1e79bb42123cddd64767b04a34add2e590a2f244` | `1e79bb42123` | matches |
| 10131 | `solr-10131-submit` | `fd495167cd096334434dbb602d7bbefcf212a5d5` | none (no gate) | the fetch moved it from `b93cf24a9d9`; audit flags it |
| 10403 | `solr-10403-submit` | `d8e03755cb5d3d8bc990cdb49a6bc1d11ac8583c` | none (no gate) | the fetch moved it from `1e285cd730c`; audit flags it |
| 14199 | `solr-14199-submit` | `e743c90da79a155fd6e6161233af5d924dc9dad1` | `e743c90da79` | matches |
| 15357 | `solr-15357-submit` | `d717899b8736309bd72f54b11230f198ae226e7d` | `d717899b873` | matches |
| 15358 | `solr-15358-submit` | `cbb345f2e7df452e20036d357c82cc63db96ffd3` | `cbb345f2e7d` | matches |
| 15712 | `solr-15712-submit` | `cb988a2ff4c3e890e3a9c258019d22d1f888aebd` | none (tidy drift named) | the fetch moved it from `555f9cab6b7`; audit flags it |
| 15945 | `solr-15945-submit` | `adb0fd450c06c07f49ccd187c3d810410a76c5b3` | `adb0fd450c0` | matches |
| 16977 | `solr-16977-submit` | `1142f9563abe13aea113bd80d241db770fd6cdb6` | `1142f9563ab` | matches |
| 17047 | `solr-17047-submit` | `1ba7e33bfe7b86c147f2cd22d6ba517aaa5cbb00` | `1ba7e33bfe7` | matches |
| 18134 | `solr-18134-submit` | `c5a0bdb21e862ec403b37c51c39070d952b972d8` | `c5a0bdb21e8` | matches |

## Shared rules for every part

- Read only, except the report file and the drafts for your part. No commit, push, checkout, reset, merge, stash, or fetch of anything new. `git show`, `git log`, `git diff`, `git merge-tree`, `git rev-parse`, `git cat-file -e`, `git grep`, `git ls-tree`, and `git merge-base` are fine. Trial merges with `git merge-tree --write-tree` are fine if they write no ref.
- No builds, no Gradle, no tests, no test runs of any kind. No `gh` calls.
- Post nothing anywhere. No PR, no comment, no JIRA.
- Proof numbers come only from the receipt named for the ticket (`receipts/SOLR-<ticket>.md` on `origin/pr-prepare`). Gate logs named in receipts are often not on disk; say so.
- Drafts follow `pr-formula.md`: the AI header as line 1, the Jira link line, a bold one-line summary in each section, "What happens today", "What this change does", "Proof", "A choice to check" (only with a live alternative, and a pointed question), "Limits", the Changelog line with a link, and the AI assistance footer. Length guide about 3,500 characters. Each draft names the head it was written against in its Proof.
- Public text carries no internal process vocabulary: gate, receipt, ledger, handoff, top-up, takeover, audit, rc=0, JUnit XML, pre-fix proof as a label, "submission" as a workspace word, internal log names, "pin" for a test that does not fail on base.
- Plain words and short sentences. No em dash and no en dash in anything you write.
- Lucene version claims: check against both the 9.x and 10.x lines wherever a PR description names Lucene behavior, and say which line you checked.
- Every finding: file and line, evidence (SHA and line, receipt line, or command output), and exact replacement wording. Mark FIX or NOTE. If you cannot check something, say so under "Not checked". Do not guess.

## Parts

Each subagent takes one part. Reports go to `reports/schema-analysis-round-1-<part>.md`. Drafts go to `pr-drafts/schema-analysis/`.

**Part s1: SOLR-9349 and SOLR-14199.** Audit both. Draft the draftable one(s) under `pr-drafts/schema-analysis/`. Cross checks: SOLR-9349 changes `ManagedIndexSchema.java` in `deleteFields`, and SOLR-15357 changes the copy-target count in the same file. Check the hunks against the 15357 head (`origin/solr-15357-submit`). SOLR-14199 removes the `getExistenceQuery` carve-out in `FieldType.java`, and SOLR-15357 adds `getSubFields` in the same file. Check that hunk too. Use trial merges. State the landing order.

**Part s2: SOLR-15357, SOLR-15358, and SOLR-10403 (audit only).** SOLR-15357 and 15358 are a pair; audit them together. Both change `CurrencyFieldType.java`. 15358 builds the amount and currency-code sub-fields through `SchemaField.createFields` so their docValues settings are honored. 15357 adds the `FieldType.getSubFields` hook with a `CurrencyFieldType` override and counts sub-fields as copyField targets in `IndexSchema` and `ManagedIndexSchema`. Check the hunks for overlap, state which should land first, and check whether 15358's docValues sub-fields are exactly the sub-fields 15357 counts. The 15357 Proof must say plainly that its behavioral tests are pins, because they call the new `getSubFields` API that base does not have. The receipt's test count is 4 of 4. For 10403: audit only; it changes `CurrencyValue`, the value class the currency field type produces. Note any bearing on the 15357/15358 pair without auditing 10403 as its own ticket.

**Part s3: SOLR-10131 and SOLR-15712 (audit only), and SOLR-15945 (audit and draft).** 10131 has no gate and still carries its testing note; state what a premise run and gate must show. Do not draft it. 15712 has no gate and tidy-only drift at the tip (a javadoc rewrap that tidy would make in its test); the tidy fold-in is main-side packaging work before its gate. Do not draft it. 15945 is gated green (`NonIndexedSpatialFieldTest` 1 of 1). Audit and draft. Cross check: 15945 changes `AbstractSpatialPrefixTreeFieldType`, and 18134 (part s4's ticket) changes the analysis package. Confirm from the diffs that they share no files.

**Part s4: SOLR-16977, SOLR-17047, and SOLR-18134.** 16977 is PR-ready with a scope split that is an owner call: the ticket's document-vector half is not addressed. State that as an owner decision in the report and as a Limit in the draft. 17047 is PR-ready (`BadIndexSchemaTest` 29 of 29; the `hnswM=0` gap was fixed in round 35). 18134 is PR-ready; its direction (ship the branch's own factory workaround rather than wait for the upstream Lucene fix) is the owner's recorded call, so the draft poses it to maintainers as a Choice. Cross check: 16977 changes `DenseVectorField.java`, and 17047 changes `SchemaCodecFactory`, `SolrCore`, and the dense vector field classes, including `BinaryQuantizedDenseVectorField`. Check whether the `DenseVectorField` hunks overlap, and whether 17047's eager format construction changes any assumption in 16977's zero-vector check. For 18134, check any Lucene behavior claim against both 9.x and 10.x.

## Deliverables

1. `reports/schema-analysis-round-1-s1.md` through `-s4.md`, one per part. The lead writes `reports/schema-analysis-round-1.md`, with per-ticket verdicts, the interaction results, and the owner decisions.
2. Drafts in `pr-drafts/schema-analysis/` for the draftable tickets, each naming its head.

Not in scope: opening PRs, posting comments, editing branches or live PR descriptions, builds, Gradle, and test runs. Owner decisions go in each part's report as a short list at the end.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: reports/schema-analysis-round-1.md. The work was performed by the original claimant; this mark is a record correction, not a new claim.
