# Assignment: drafts owed for build and docs and CLI branches, one hold confirmation, one metrics confirmation, census follow-up round 1

Capability tags: [review]. Intended host: Windows (review coordinator). Claim under claims/ before starting, per WORKFLOW.md.

## Owner directive: run to SATISFIED

- Write each owed draft, work the two confirmations, review your own work against the receipts at the live tips, apply what the review calls for, and keep going until a SATISFIED verdict comes back on each item.
- Drafts follow `pr-formula.md` exactly (AI header, bold one-line summary opening every section, Proof only from the receipt or gate record, citations at the head SHA or the named merge-base, plain language, no internal process vocabulary, no "we", no em dashes).
- For docs-only and script-only branches, the Proof states the evidence shape the receipt records (a docs build, a light gate, BATS runs, a harness run) in plain words; it does not borrow the Gradle gate's shape.
- Scope questions: decide under the decision default and the ladder; record each decision in the deliverable report.
- No comments are posted by the review side anywhere. Nothing here opens, flips, posts or deletes.

## Scope

Drafts owed, each branch verified at its live tip per its receipt:

- Build and docs (to `pr-drafts/build-docs/`): SOLR-11700 (docs-only gate green at the packaged tip), SOLR-17356 (docs-only gate green 2026-10-11), SOLR-7119 (docs-shaped gate green at the packaged head).
- CLI (to `pr-drafts/cli/`): SOLR-7924 (gate green), SOLR-10390 (check the state of `gates/SOLR-10390-premise.md` first: if its premise job has not run, write the draft against the receipt and flag the premise as owed in the deliverable; do not state a premise result no record carries).

Hold confirmation:

- SOLR-3684 (build and docs): the draft is CONVERGED on text per `reports/build-docs-convergence-round-1-response.md`, but that response also records the draft's own HOLD (owner decision 1) as standing. Find owner decision 1 in the build-docs round 1 answers record, and either discharge the hold under the ladder with the reason recorded here, or restate it as the owner item it is, in one line, in the deliverable.

Confirmation:

- SOLR-17987 (metrics): the draft exists under `pr-drafts/metrics/` and the branch is gate green at its live tip per its receipt. Confirm the draft against the receipt, including that it carries the `SOLR_METRICS_DISABLED_REGISTRIES` rename; the branch-side rename itself is opening-time packaging, not this round.

Census context: `reports/openings-census-round-1.md`, build and docs, CLI and metrics sections.

## Deliverable

`reports/census-followup-build-docs-cli-metrics-round-1.md`: one line per ticket, SATISFIED (READY TO OPEN) or the named hold, plus every draft written and the SOLR-3684 hold outcome.
