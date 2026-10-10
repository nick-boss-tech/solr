# Claim: SolrJ and clients round 1 (audit, then drafts for the draftable tickets)

Claimed 2026-10-10 by Claude Code (AI agent), working for Nick Shanin.

Trigger: `pr-prepare` gained `assignments/solrj-clients-round-1.md` in commit `dd6c4c7e397`. The assignment covers twenty-four branches carrying twenty-three tickets, with their receipts under `receipts/`. The assignment says the round is one audit pass, the main side does one answers pass, and then drafts go to `pr-drafts/solrj/`.

Scope: the assignment's twenty-four branches. Sixteen tickets get drafts, four get audit-only outcomes, and four get consistency passes on live or merged work.

## Heads checked live on 2026-10-10

Fetched with `git ls-remote origin refs/heads/solr-<ticket>-submit`. Every head matches the head the assignment names. Four audit-only branches are recorded in their receipts. The round does not re-audit the settled branches' gates.

| Ticket | Branch | Live head | Expected | Result |
|---|---|---|---|---|
| 2018 | `solr-2018-submit` | `213457aa91fd` | `213457aa91f` | matches |
| 3498 | `solr-3498-submit` | `812598302dee` | audit-only (receipt) | matches receipt |
| 3722 | `solr-3722-submit` | `f9d3dda1d3de` | `f9d3dda1d3d` | matches |
| 3999 | `solr-3999-submit` | `e3af6f36e312` | `e3af6f36e31` | matches |
| 4335 | `solr-4335-submit` | `8f6e267d6482` | `8f6e267d648` | matches |
| 4336 | `solr-4336-submit` | `798618aa08fd` | `798618aa08f` | matches |
| 4422 | `solr-4422-submit` | `3e5b7afecddb` | `3e5b7afecdd` | matches |
| 4424 | `solr-4424-submit` | `2e947b7f622c` | `2e947b7f622` | matches |
| 5220 | `solr-5220-submit` | `e415c409044c` | `e415c409044` | matches |
| 6046 | `solr-6046-submit` | `3b833e369a0b` | `3b833e369a0` | matches |
| 7709 | `solr-7709-submit` | `501078f22203` | `501078f2220` | matches |
| 8536 | `solr-8536-submit` | `28552ddc26eb` | `28552ddc26e` | matches |
| 10198 | `solr-10198-submit` | `9448cda146f7` | `9448cda146f` | matches; merged |
| 10364 | `solr-10364-submit` | `502bdbf033fa` | audit-only (receipt) | matches receipt |
| 11356 | `solr-11356-submit` | `8474e5a3a26d` | audit-only (receipt) | matches receipt |
| 12094 | `solr-12094-submit` | `8d957a73f4fb` | `8d957a73f4f` | matches |
| 14187 | `solr-14187-submit` | `45b0f7ce34f8` | audit-only (receipt) | matches receipt |
| 14298 | `solr-14298-submit` | `67e75ea1eea4` | `67e75ea1eea` | matches |
| 14967 | `solr-14967-submit` | `ee76643f5b3f` | `ee76643f5b3` | matches |
| 15823 | `solr-15823-submit` | `1f60cd39baae` | `1f60cd39baa` | matches; live PR |
| 15823 (levels) | `solr-15823-levels-submit` | `6fa54c4de8c9` | `6fa54c4de8c` | matches; live PR |
| 17866 | `solr-17866-submit` | `3f9367d796a6` | `3f9367d796a` | matches |
| 18129 | `solr-18129-submit` | `657e443d866d` | `657e443d866` | matches; live PR |
| 18341 | `solr-18341-submit` | `458b098719d7` | `458b098719d` | matches |

## Staffing

Six subagents in parallel, split by ticket cluster. The cap of six at once is met, and no other round runs while this one does. The lead writes the roll-up.

- **S1, NamedList and params:** 3722 (draft), 4424 (draft), 4336 (draft), 7709 (draft). The NamedList landing order (3722, 4424, 7709) and the params interaction.
- **S2, document binding:** 4422 (draft), 10364 (audit only, read against 4422's binder), 3999 (draft), 2018 (draft).
- **S3, XML and misc:** 4335 (draft), 6046 (draft), 8536 (draft), 12094 (draft). The XML write-path composition of 4335 and 6046.
- **S4, load balancing:** 5220 (draft), 14298 (draft), 18341 (draft, the largest branch). The LBSolrClient landing order (5220 before 14298, or the reason not).
- **S5, cloud client and requests:** 14967 (draft), 17866 (draft), 3498 (audit only), 11356 (audit only), 14187 (audit only). The SolrRequest and CloudSolrClient interactions with 18341.
- **S6, consistency and merged work:** 10198 (merged), 15823 pair (live PRs), 18129 (live PR). The SOLR-15823 stacking check.

## Shared rules for every part

- Read the receipt first. A receipt at the exact live tip settles gate state. Do not re-audit a gated branch's gate. The audit verifies the recorded state against the branch and judges PR readiness.
- No builds, no Gradle, no tests, no gate runs. Gate evidence comes from the receipts. Where a receipt says NO GATE, or gated at an older head, say what is owed instead of substituting a local run.
- No PRs, no comments, no edits to submit branches or live PR descriptions, no posting.
- Code claims cite file and line at the head SHA. Use `git show <head>:<path>` and `git diff`. Do not check anything out.
- Read the Jira ticket from `C:\Users\shaninna\dev\Solr-issues\research\jira-context\SOLR-<ticket>.json` if it exists (read only; in the main checkout). Do not call JIRA.
- Drafts follow `pr-formula.md`. Each draft names its head in its Proof. Public text carries no internal process vocabulary (no gate, receipt, ledger, rc=0, "fresh JUnit XML", "pre-fix proof" as a label, "owed", "round"). The Proof states what ran, at which head, what passed, and that the new test fails without the fix. Where a receipt records the proof as partial, configuration-split, or inconclusive by construction, the draft says that instead. Titles must be accurate.
- Lucene versions: if a draft names one version, name every version that applies. Main and branch_10x pin Lucene 10.4.0; branch_9x pins 9.12.3.
- Plain words. No em dash and no en dash, anywhere, including the report.
- Each subagent returns its part report as text. The lead writes the files.

## Deliverables

1. `reports/solrj-clients-round-1.md`: per-ticket verdicts (draftable, consistency result for the live and merged work, audit outcome for the NO GATE tickets, or owner decision with options and a recommendation), disagreements with the receipts, the landing orders, and a short owner-decision list at the end.
2. Drafts in `pr-drafts/solrj/` for the sixteen draftable tickets, each naming its head.
3. The part reports, written by the lead to `reports/solrj-clients-round-1-s1.md` through `-s6.md`.

## Not in scope

- SOLR-18196 (merged, audit home Search components), SOLR-16499 and SOLR-17731 (audit home Core admin), SOLR-11650 (audit home Replication and backup), SOLR-17433 and SOLR-17143 (audit home Streaming expressions), SOLR-9864, SOLR-15331, SOLR-3865 and SOLR-8576.
- Opening PRs, posting comments, editing submit branches or live PR descriptions, builds, Gradle, and test runs.
