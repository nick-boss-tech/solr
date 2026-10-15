# Pool assignment: strong review of the four draft PRs opened 2026-10-12, round 1

Status: DONE

Capability tags: `review`. Intended host: Windows. This assignment is self-contained for a reviewer joining cold; everything needed is on this branch (pr-prepare) or on the fork.

## Context

Four draft PRs were opened on apache/solr on 2026-10-12. Each has had one review pass from a weaker reviewer. This is the strong pass. Assume the first pass missed things. Re-derive every claim from the code and the records; do not trust the draft texts.

Records live on this branch:

- `receipts/`: one file per ticket, recording the gated head, the test counts and the proof result.
- `pr-drafts/`: the text that was opened as each PR body.
- `reports/`: premise reports, comparison reports and earlier review rounds.

The hardening standard: a claim stands only on a gate receipt at the exact head it names. Test counts come from run records, never from the draft text. Where a draft and a receipt disagree, the receipt governs and the disagreement is a finding.

## The four PRs

### 1. SOLR-18539, PR #5115, mergeIds skipped-shard totals

Branch `mergeids-skipped-shard-totals` at `4eb996121804b043f1ee53f5bb7e22b9c3021b0a`. Receipt: `receipts/mergeids-fix.md`. Draft: `pr-drafts/search-components/mergeids-skipped-shard-totals.md`.

The defect: `QueryComponent.mergeIds` folded a partial shard's totals (its numFound, its approximateTotalHits, its maxScore, its exactness) into the merged result before reaching the skip for that shard, and the approximateTotalHits fallback added the running merged count instead of the current shard's own count.

### 2. SOLR-18540, PR #5116, gradlew under Git Bash

Branch `gradlew-msys-double-conversion` at `58e07b4571fbbfc83c14bdd28283cd573acb0d7a`. Receipt: `receipts/SOLR-18540.md`. Draft: `pr-drafts/build-docs/SOLR-18540.md`.

The fix deletes three lines from `gradlew`: the second `cygpath --path` conversion, which splits the classpath at the drive-letter colon under Git Bash, MSYS and Cygwin so the wrapper's main class is not found and no Gradle task runs from those shells.

### 3. SOLR-18541, PR #5117, CombinedQueryComponent merge totals

Branch `combinedquerycomponent-merge-totals` at `d5ed34125db9582b584bbfdd149a4de4b40650d0`, plus a side branch `solr-18541-combined-query-merge-totals` at `fcd92f23ba65` carrying one further fix commit from the first review (clear numFoundExact when a skipped shard reported hits). Receipt: `receipts/SOLR-18541.md`. Draft: `pr-drafts/search-components/SOLR-18541.md`. Premise and real-exit verdicts: `reports/combinedquerycomponent-premise.md`.

The branch fixes three defects in `CombinedQueryComponent.mergeIds`: totals folded in before the partial skip for that shard and query, a fallback that added the running merged count (with the current shard already folded in) instead of the shard's own per-query count, and a hitCountIsExact fold inverted both ways.

### 4. SOLR-18010, PR #5118, standalone security.json edits

Branch `solr-18010-submit` at `2b5ad801c9e88807337f4f0a3fdd689126ad37d1`, moved past its opening head by the first review (a changelog title correction and a permissions-preservation fix with its own test; both gated at their heads per the receipt). Receipt: `receipts/SOLR-18010.md`. Draft: `pr-drafts/core-admin/SOLR-18010.md`.

A related PR by another contributor exists for the same ticket, and an offer comment pointing at this draft is posted on it; the comparison is `reports/solr-18010-overlap-comparison-round-1.md`. Review proceeds normally, but a verdict on this PR does not flip it ready; that decision stays with the owner while the offer is outstanding.

## Per-PR focus

The items below are the minimum. Go beyond them wherever the code leads.

### SOLR-18539 (mergeIds)

- Prove or refute the fix's semantics from the code: the skip is tested before every fold; maxScore and hitCountIsExact behave correctly when some shards are skipped, when all shards are skipped, and when none are; the fallback now adds the shard's own count and cannot double-count across iterations; the all-shards-skipped guard does what the body says it does.
- Audit the tests: does each of the 17 tests in `QueryComponentPartialResultsTest` actually pin the claim the body makes for it? The pinned scope is the skip when sort values are absent; is that scope stated honestly in the body, and is anything outside it implied?

### SOLR-18540 (gradlew)

- Is deleting the conversion safe for every input the wrapper supports: paths with spaces, CLASSPATH already set, and the differences between Cygwin, MSYS and Git Bash?
- Check what the current upstream Gradle wrapper script does at the equivalent place, and whether the deletion matches its direction.
- Confirm the body claims no more verification than was run: the Windows end-to-end run by the owner and a Linux `-version` run of the patched script. No full build was run on the patched script; the body must not imply one.

### SOLR-18541 (CombinedQueryComponent)

- Re-derive all three folds and the skip placement from the code at both heads (the PR branch and the side branch).
- Review the side-branch exactness commit on its merits. Its condition clears exactness only when the skipped shard reported hits. Decide whether that is the correct rule, with a case analysis (a skipped shard reporting zero hits; a skipped shard reporting hits; no skipped shards).
- Take an evidence-based position on the open maximum-fold question: whether the merged maxHitsTerminatedEarly flag should be the union of the shards' flags or the winning shard's flag, since the body poses it as a Choice. Ground the position in how the value is consumed and in the run evidence in the premise report.
- Assess the confirmed real-exit crash: an unchecked per-query list access throws IndexOutOfBoundsException when a shard exits above query index zero (recorded in the premise report). State whether it belongs in this PR, as a follow-up named in Limits, or in its own ticket, with reasons.
- Audit the 7 tests converted from the probe for fidelity: does each test assert the value the probe recorded, and do the maximum-fold tests describe the current rule honestly?

### SOLR-18010 (security.json)

- Audit the lock scope: what is and is not serialized (one process, one JVM, concurrent handlers and entry points).
- Audit the atomic write: temp file in the same directory, the move semantics relied on, and the failure modes (a failed write, a crash mid-write).
- Audit the permissions-preservation commit added by the first review: POSIX-only behavior, the debug-only failure path, and whether silently proceeding when the permissions copy fails is the right call.
- Judge whether the body's Choice about copying permissions is posed fairly, and whether the changelog title's scope sentence is accurate.
- Given the offer outstanding on the other contributor's PR, state what should happen to this draft under each answer they might give (adopts both elements, adopts one, declines, or does not answer).

## Method and authority

- Read the actual diffs at the stated heads; the fork carries every branch.
- Verify every number in each PR body against its receipt. Flag any claim the receipt does not support.
- Construct adversarial cases on paper for each fix (inputs, shard shapes, orderings) and state whether the code survives them. Where a doubt needs a run, post a gate job under `gates/` naming the exact class, seed and question; a Linux host runs builds, this reviewer does not.
- Findings are applied, not just listed. Wording fixes go into both the live PR body (GitHub API, each edit verified by re-reading) and the matching draft on this branch. Code findings are written as patches and pushed by the reviewer; a patch counts as reviewed only after a Linux host has gated the patched head.
- Keep looping in further rounds until each PR returns SATISFIED, or a finding is recorded as an owner item with the reason it cannot be settled.
- Scope questions are decided under the owner's ladder: where information is missing, get it; where the cost is effort, do the hard thing; where an item is optional, defer it as a named follow-up in Limits. Record each decision in the report.
- No comments are posted. Anything needing one is drafted in the report for the owner.
- Voice rules for everything written: no em dashes; never "we" for the owner plus agents (write "I" or neutrally); no internal process words in public text (gate, receipt, ledger as labels belong in the report only, not in PR text); citations to changed code link the head SHA, and citations to pre-change behavior link the merge-base and say so.

## Deliverable

`reports/four-new-prs-strong-review-round-1.md`, one section per PR:

- claims verified, and how;
- adversarial cases tried, and their outcomes;
- findings, each with a severity and how it was applied (or the owner item and why it cannot be settled);
- the position taken on each open question named above;
- a final verdict line per PR: SATISFIED, or NOT SATISFIED with the blocking items.

The assignment discharges only when all four verdict lines exist.
