# Solr agent skills: first-level implementation and review handoff, round 1

Status: FOUNDATION DELIVERED; independent review, Linux portability, installation and automation deployment pending.

## Published artifacts

- Owner direction: post assignments, implement the first level, prefill monthly activity from existing PR scan artifacts and hand off for review. No new archaeology was requested or performed.
- Assignments/claims landed first at `4e4b3294c0a097933f071a937553595b241651bd` on coordination-ui: eight assignments and six scoped initial implementation claims.
- Fork branch: `factory-solr-skills-round-1`.
- Verified package commit: [b30a0a77df5f3ae10191c814256245319f56cdd7](https://github.com/nick-boss-tech/solr/commit/b30a0a77df5f3ae10191c814256245319f56cdd7); all 34 payload blob hashes read back against the remote fork tree.
- Package base: `9d68d65d455fd0fae68eb43b745c8d9f2c0b8722`, the inspected board snapshot. This is factory tooling, not an apache/solr implementation patch.
- [Start with the handbook](https://github.com/nick-boss-tech/solr/blob/b30a0a77df5f3ae10191c814256245319f56cdd7/handbook/INDEX.md), [package metadata](https://github.com/nick-boss-tech/solr/blob/b30a0a77df5f3ae10191c814256245319f56cdd7/package.json), and [review instructions](https://github.com/nick-boss-tech/solr/blob/b30a0a77df5f3ae10191c814256245319f56cdd7/evaluation/REVIEW.md).

| Skill | Board work | State |
| --- | --- | --- |
| [solr-navigate](https://github.com/nick-boss-tech/solr/blob/b30a0a77df5f3ae10191c814256245319f56cdd7/.agents/skills/solr-navigate/SKILL.md) | [Assignment](../assignments/agent-skill-solr-navigate-round-1.md) | Foundation delivered; behavior review pending |
| [solr-pr-map](https://github.com/nick-boss-tech/solr/blob/b30a0a77df5f3ae10191c814256245319f56cdd7/.agents/skills/solr-pr-map/SKILL.md) | [Assignment](../assignments/agent-skill-solr-pr-map-round-1.md) | Foundation delivered; behavior review pending |
| [solr-review](https://github.com/nick-boss-tech/solr/blob/b30a0a77df5f3ae10191c814256245319f56cdd7/.agents/skills/solr-review/SKILL.md) | [Assignment](../assignments/agent-skill-solr-review-round-1.md) | Foundation delivered; behavior review pending |
| [solr-proof](https://github.com/nick-boss-tech/solr/blob/b30a0a77df5f3ae10191c814256245319f56cdd7/.agents/skills/solr-proof/SKILL.md) | [Assignment](../assignments/agent-skill-solr-proof-round-1.md) | Foundation delivered; behavior review pending |
| [solr-contribute](https://github.com/nick-boss-tech/solr/blob/b30a0a77df5f3ae10191c814256245319f56cdd7/.agents/skills/solr-contribute/SKILL.md) | [Assignment](../assignments/agent-skill-solr-contribute-round-1.md) | Foundation delivered; behavior review pending |
| [solr-maintain](https://github.com/nick-boss-tech/solr/blob/b30a0a77df5f3ae10191c814256245319f56cdd7/.agents/skills/solr-maintain/SKILL.md) | [Assignment](../assignments/agent-skill-solr-maintain-round-1.md) | Foundation delivered; behavior review pending |

Each skill has a scoped SKILL.md, supporting reference and discovery UI metadata. solr-pr-map also includes the offline monthly processor and its meaningful fixture checks. The handbook supplies navigation and a monthly evidence index, not invented architecture chapters. solr-contribute retrieves the current PR/comment formulas and derives drafts from actual issue/diff/proof inputs; no full stale policy copy is embedded.

## Prefilled monthly material

The ongoing scan project's existing raw census and scan-register files were read only. The PR census capture is `2026-11-05T03:17:59Z`; the coverage overlay was copied separately at `2026-11-06T05:26:00.305738+00:00`. Source hashes and normalized records are in the pinned [snapshot](https://github.com/nick-boss-tech/solr/blob/b30a0a77df5f3ae10191c814256245319f56cdd7/handbook/months/snapshot.json). Full scan/review reports remain in the scan project.

| UTC month | Merged | Open | Draft | Complete scan at captured head |
| --- | ---: | ---: | ---: | ---: |
| 2026-08 | 137 | 7 | 5 | 44 |
| 2026-09 | 120 | 12 | 4 | 6 |
| 2026-10 | 155 | 29 | 104 | 16 |
| 2026-11 | 14 | 13 | 3 | 23 |

Population: 603 PRs. Merged records use merge month; Open/Draft records are captured backlog grouped by creation month. These are not complete counts of PRs opened, updated or reviewed in each month. Closed-unmerged and pre-window open PRs are outside this cohort. November is partial. Later live changes, including PR 5281's merge, are intentionally not silently folded into the historical census. Scan completion is source-review coverage, not runtime proof.

## Executed checks and limits

- All 6 skills passed skill-creator's structural validator.
- 23 package-relative links resolved; UI description lengths/default skill prompts were checked.
- The actual monthly-index CLI regenerated the prefilled files byte-for-byte from the normalized snapshot, and totals reconcile to 603.
- 6 standalone metadata-processor fixture checks passed, exit code 0; full log [here](https://github.com/nick-boss-tech/solr/blob/b30a0a77df5f3ae10191c814256245319f56cdd7/evaluation/monthly-checks.txt).
- [Structural results and file hashes](https://github.com/nick-boss-tech/solr/blob/b30a0a77df5f3ae10191c814256245319f56cdd7/evaluation/structural-validation.json) are included with a portable runner. Initial validation lacked PyYAML in the host runtime; validation succeeded in an isolated uv environment with PyYAML 6.0.3. The shared runner documents that dependency.
- Publication used the GitHub Git Data API to create the fork ref after a normal Git transfer stalled and the task-specific push was stopped. The final remote ref, parent, author/committer, complete changed-file list and all payload blob hashes were separately checked. No existing branch was overwritten or force-updated.
- No Solr Gradle/test queue, new source scans, gateway calls, public comments, source PR creation, host skill installation or recurring schedule was performed.
- Ten [behavioral cases](https://github.com/nick-boss-tech/solr/blob/b30a0a77df5f3ae10191c814256245319f56cdd7/evaluation/scenarios.json) are prepared, not claimed as evaluated. Structural validity is not behavioral acceptance. Linux portability and evaluation with actual host/tool access remain to be assessed.

## Next owner/host action

Claim [the independent bundle review](../assignments/agent-skills-bundle-review-round-1.md). Review package commit `b30a0a77df5f3ae10191c814256245319f56cdd7` with existing scan artifacts only, exercise the supplied and unseen scenarios without revealing their acceptance notes to an evaluating agent, and post `reports/agent-skills-bundle-review-round-1.md` with per-skill verdicts and scoped repair proposals. Preserve source/authority boundaries and distinguish historical records from current state.

The six initial implementation claims are completed by this pushed package and report. Their original assignment files remain add-only historical records; this report does not mark independent review complete. After review, [the automation follow-up](../assignments/agent-skills-automation-followup-round-1.md) covers delta/cursor/digest implementation and a specific proposed deployment. Cadence, host, output destination and installation choice remain for that follow-up. No scheduler is active.

Suggested handoff prompt: Review the six first-level Solr skills at `b30a0a77df5f3ae10191c814256245319f56cdd7` using `evaluation/REVIEW.md` and the bundle-review assignment. Reuse the ongoing PR scan, report observed behavior and portability gaps, and implement only the scoped follow-ups assigned after review. Do not restart the archaeology or install/schedule the package as part of review.
