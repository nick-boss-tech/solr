# Draft fidelity review: CLI, Security, Build/docs and Metrics drafts

Assignment: `assignments/pool-draft-fidelity-cli-security-builddocs-metrics.md`. Claim: `claims/pool-draft-fidelity-cli-security-builddocs-metrics.md`. Lead: the windows review agent. Part reports, with the exact replacement text for each item:

- `reports/draft-fidelity-cli-security-builddocs-metrics-s1.md`: CLI SOLR-9342, 16272, 16813, 17029.
- `reports/draft-fidelity-cli-security-builddocs-metrics-s2.md`: CLI SOLR-17598, 18132, 18339; Security SOLR-10627.
- `reports/draft-fidelity-cli-security-builddocs-metrics-s3.md`: Security SOLR-18368; Build-docs SOLR-3684, 5821, 16914.
- `reports/draft-fidelity-cli-security-builddocs-metrics-s4.md`: Build-docs SOLR-17252, 17752, 17842; Metrics SOLR-17987.

No draft was edited under this assignment. Read-only throughout. No build, test, gate or test-queue run. No PR, comment, review, submit-branch, PR-description or Jira write.

## Verdicts

Sixteen drafts. Thirteen are DRIFT and three are CONSISTENT. Each head was matched against `git ls-remote` on the fork, and every draft's named head matched the live tip at the time of the check.

| Draft | Head checked | Verdict | Part report |
|---|---|---|---|
| SOLR-9342 | `833e11192a7` | DRIFT (4 items) | s1 |
| SOLR-16272 | `d2cf8173916` | DRIFT (1 item) | s1 |
| SOLR-16813 | `1b288170e8a` | DRIFT (2 items) | s1 |
| SOLR-17029 | `4a98ef0a89d` | DRIFT (1 item) | s1 |
| SOLR-17598 | `8c91cf047a96` | DRIFT (1 item) | s2 |
| SOLR-18132 | `54835cac6f85` | CONSISTENT | s2 |
| SOLR-18339 | `47e53884609c` | CONSISTENT | s2 |
| SOLR-10627 | `5a15dc0ba220` | CONSISTENT | s2 |
| SOLR-18368 | `a7ec9a1b65c0` | DRIFT (1 item) | s3 |
| SOLR-3684 | `663b8ce754b6` | DRIFT (2 items) | s3 |
| SOLR-5821 | `b8af2d1ce2f5` | DRIFT (2 items) | s3 |
| SOLR-16914 | `cd878023d3d0` | DRIFT (3 items) | s3 |
| SOLR-17252 | `d730a266a042` | DRIFT (2 items) | s4 |
| SOLR-17752 | `e5c0a64f993c` | DRIFT (4 items) | s4 |
| SOLR-17842 | `008973f63133` | DRIFT (3 items) | s4 |
| SOLR-17987 | `38abf6423126` | DRIFT (6 items) | s4 |

## Items by draft

Each line summarizes one item. The exact replacement text is in the named part report.

- **SOLR-9342 (s1).** The title does not match the head changelog fragment. The head-run date and the "built from this head" claim are not in the receipt. The Windows JVM claim is not supported by the round 1 report. The draft has four items in total; s1 lists all four with replacement text.
- **SOLR-16272 (s1).** The title does not match the head changelog fragment. The fragment needs the amended title.
- **SOLR-16813 (s1).** The title does not match the head changelog fragment. The "still runs six tests" claim does not match the base, which has four `@Test` methods.
- **SOLR-17029 (s1).** The Proof cites the 2026-10-08 run as passing and does not mention that run's test 5 failure. See the open owner decision below.
- **SOLR-17598 (s2).** The Proof is missing its verification date.
- **SOLR-18368 (s3).** The opening does not say that the ticket's removal is already upstream (commit `3ef3d093c06`). The replacement opening line is in s3.
- **SOLR-3684 (s3).** The title does not match the changelog fragment. The "always uses" symptom cites line 207 at the head, and it should cite the merge-base.
- **SOLR-5821 (s3).** "See lines 74-76" should read line 77, the unique-sort advice. The tie-breaker symptom link must point at the merge-base and say so.
- **SOLR-16914 (s3).** The symptom overstates the case: the option table does name `discardCompoundToken` and its kept tokens. The Proof has no verification date (2026-10-08, from the light gate). The Proof's content-check claim is not in the receipt.
- **SOLR-17252 (s4).** The symptom links point at upstream main `3f5d4c5` instead of the merge-base `14c7aac0d151`. The yaml line 1534 is line 1532 at the merge-base. The Choice omits the earlier placement (`check_prerequisites`) that the round 1 report and receipt Q3 record.
- **SOLR-17752 (s4).** The title does not match the changelog fragment. "What happens today" cites `SolrResponseUtil` line 55 at the head, but the warning is at lines 58 to 61. The pre-change links use the fork head SHA on apache/solr, and they should use the merge-base. The Proof has no verification date. The receipt's 2026-10-04 date predates the head commit (2026-10-05), so the date must be confirmed against the gate log.
- **SOLR-17842 (s4).** The draft says "one or two" classes per area, but its search names three. The pre-change links point at main, not the merge-base. The Limits say "vector search accuracy numbers"; the round 1 report says "vector search numbers".
- **SOLR-17987 (s4).** Neither owner flag is stated accurately. (a) The draft does not say `jetty`, and the changelog title names it. (b) The env var name is stated, but its departure from the ref guide rule (`SOLR_METRICS_DISABLED_REGISTRIES`) is not. The draft has no Title line. The Limits omit the untested property and env path and the no-op JVM start path. The Choice says the draft "keeps every metrics switch in one form", which the `solr.xml` on/off flag contradicts.

## Open for the owner or the main side

1. **SOLR-17029, test 5.** Whether the Proof states the 2026-10-08 result, including the test 5 failure, is an open owner decision. It is recorded in `reports/cli-bin-packaging-round-1.md` (line 37 and line 123) and in s3's notes. It is not an assignment rule. The draft says nothing about test 5 until the owner decides.
2. **SOLR-18368, live status.** The receipt says Resolved. The local snapshot says Open. The main side should confirm the live ticket state before the opening is written.
3. **SOLR-17752, gate date.** The receipt's 2026-10-04 date predates the head commit (2026-10-05). The gate log should confirm which run the Proof counts come from. The log is not on disk.
4. **SOLR-17987, owner flags.** Both flags need owner wording. The subagent's replacement text is a starting point only.
5. **SOLR-3684, design notes.** `design-decisions-open.md` was not found. The Choice was checked against the Jira snapshot and the receipt only.
6. **Upstream moved.** Upstream main has moved since the brief was written. Every symptom link was checked at the merge-base SHA that its draft names, not at the moved tip.

## Not done

- No build, test, gate or test-queue run.
- The gate and run logs named in the receipts are not on disk. Proof counts were checked against the receipts as recorded, not against the logs.
- No live ticket text was re-read. The Jira check on SOLR-18368 used the local snapshot only.
- No GitHub write. Read-only `gh` calls only, where a subagent used them.
- Commit and push of this roll-up and of the part reports follow in the same push as the claim DONE mark.
