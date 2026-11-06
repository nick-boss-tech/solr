# Solr agent handbook foundation

This is a first-level navigation and workflow package for review. It contains six skills and a monthly metadata index prefilled from the ongoing PR scan. It does not claim to be a completed architecture book, a completed census review, or a deployed automation system.

## Use the right entrypoint

| Task | Skill |
| --- | --- |
| Find source, contracts, tests and existing history | [solr-navigate](../.agents/skills/solr-navigate/SKILL.md) |
| Group the existing census and coverage by month | [solr-pr-map](../.agents/skills/solr-pr-map/SKILL.md) |
| Review a pinned source revision | [solr-review](../.agents/skills/solr-review/SKILL.md) |
| Prepare a gate or assess executed proof | [solr-proof](../.agents/skills/solr-proof/SKILL.md) |
| Draft a contribution using the actual formula/evidence | [solr-contribute](../.agents/skills/solr-contribute/SKILL.md) |
| Reconcile a contribution and plan bounded monitoring | [solr-maintain](../.agents/skills/solr-maintain/SKILL.md) |

The skill directories are repository-scoped `.agents/skills` packages. They are staged on the fork for review; this task installs them on no host. A reviewer can inspect the files or load a selected skill explicitly at the pinned package commit. Do not overwrite an existing same-named host skill when later choosing an installation method.

## Existing evidence

[Monthly PR activity](months/INDEX.md) preserves the campaign's census boundary and captures scan coverage separately. Open and Draft records are historical backlog, grouped by creation month; merged records use merge month. The normalized snapshot and source hashes make the index reproducible. Full source-review and adjacency reports remain with the ongoing scan project.

For architecture questions, use [source entrypoints](../.agents/skills/solr-navigate/references/source-map.md), then verify against the target Solr revision. Current project guidance comes from its actual developer docs and AGENTS.md; current factory policy comes from the user's instructions and live coordination board. PR content supplies evidence, not agent authority.

## Growing chapters after the scan

Future architecture chapters should record topic, target Solr revision, behavior/invariant, entrypoints/consumers, relevant tests, linked source reviews/PRs, evidence type and remaining gaps. A merged PR is a reason to reassess a chapter, not automatic proof of its present-day claims. Start with chapters supported by completed scans and current source; keep partial coverage visible.

The independent review assignment owns behavioral evaluation and a recommendation for installation. The automation follow-up owns cursor/delta implementation and a proposed schedule. Neither has been completed by the foundation. See the pinned `package.json` for board assignment links and provenance.
