# Pool assignment: a simpler rule for docs-only commits (accepted by the owner, 2026-10-12)

Status: DONE

Posted by the vm2 gate backlog coordinator, 2026-10-12. This file proposes a rule change. It does not change WORKFLOW.md; the owner decides whether the rule takes effect.

## Problem

Packaging commits that only delete a local handoff note (for example SOLR-XXXX-TESTING.md) change the head SHA. Under the current rule the receipt must name a gated head, so the packaged commit needs its own gate run, about 20 minutes per ticket. The deleted file is Markdown and is never compiled, tested, or read by the build, so the run cannot change its result.

Affected now: SOLR-10364, SOLR-11356, SOLR-11678, SOLR-12161. Each receipt already names a packaged head that was gated (efb1e6717f, 6120dae28d, 611eae2464, cdd265168a). Those tickets can be pushed without a new run once this rule is decided.

## Proposed rule

1. A commit is docs-only when every path it changes is a Markdown file (.md) outside any src/ directory.
2. A docs-only commit on top of a gated head inherits that gate. No new gate run is needed for it.
3. The receipt names both SHAs: the gated head and the head pushed. It also records the output of `git diff --name-only <gated head>..<pushed head>`, which must list only docs-only paths. If that output contains any other path, the rule does not apply and the full gate runs.
4. Every other change keeps the current rule. Title-only changelog fixes keep their existing handling.

## Questions for the owner

- Accept the rule as written, or amend the file list (for example, allow .adoc or exclude pr-drafts/)?
- Confirm that the four packaging commits above may be pushed to their fork branches under this rule.

## Not in scope

No change to the premise-run, proof-shape, or heartbeat rules. No change to what counts as a gate for a code change.

## Owner decision (2026-10-12)

Accepted. Pushes made under this rule: SOLR-10364 `efb1e6717f` to `solr-10364-submit` and SOLR-11678 `611eae2464` to `solr-11678-submit`, each a fast-forward over the fork tip, each the packaged head its receipt already gates, changing only the handoff .md file. The HOLD entries for these two in the decision round 1 report are superseded by this. SOLR-11356 and SOLR-12161 keep the fork-tip adoption from that round. WORKFLOW.md is not amended by this note.
