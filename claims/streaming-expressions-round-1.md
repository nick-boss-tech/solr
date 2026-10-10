# Claim: streaming expressions round 1 (audit, then drafts for the draftable tickets)

Claimed 2026-10-10 by Claude Code (AI agent), working for Nick Shanin.

Trigger: `pr-prepare` moved from `a6477aef4c1` to `f55404e1127`. That commit ("Assignment: Streaming expressions round 1, with receipts for its twelve tickets") adds `assignments/streaming-expressions-round-1.md` and twelve receipts under `receipts/`.

Scope: the assignment's eleven branches carrying twelve tickets. The round is one audit pass. Drafts go to `pr-drafts/streaming/` for the tickets the audit finds draftable. The retire candidates and the held ticket get owner decisions with recommendations, not drafts.

## Heads checked live on 2026-10-10

Fetched with `git ls-remote origin refs/heads/<branch>-submit`. Every head matches the assignment's expected tip or, for the three NO GATE branches, the receipt's recorded head.

| Ticket | Branch | Live head | Receipt state | Result |
|---|---|---|---|---|
| 9852 | `solr-9852-submit` | `31f58dbe8e61` | NO GATE | matches receipt; audit only |
| 10322 | `solr-10322-submit` | `80ce9d7a3c8a` | GATE GREEN at `0523a10639dd`, plus one title-only commit | moved head, disclosed in the draft's Proof |
| 10882 | `solr-10882-submit` | `83fc3dfeb24b` | NO GATE | matches receipt; audit only |
| 11922 | `solr-11922-submit` | `da29a963236f` | NO GATE by finding (premise run done) | matches receipt; owner decision |
| 12505 | `solr-12505-submit` | `5f20e1171bc8` | GATE GREEN | matches |
| 12657 | `solr-12657-submit` | `16a0a69e8398` | GATE GREEN (round 35 re-gate) | matches |
| 13524 | `solr-13524-submit` | `f08e7c12a8ed` | GATE GREEN; live PR | matches; consistency only |
| 14200 | `solr-14200-submit` | `10991af9bdac` | GATE GREEN; settling run says the scenario is gone | matches; retire call |
| 14231 | `solr-14231-submit` | `2f73d00a3996` | GATE GREEN (reconciled) | matches |
| 15326 | `solr-15326-submit` | `dab6a4663169` | GATE GREEN; settling run says the premise is dead | matches; retire call |
| 17433 / 17143 | `solr-17433-17143-submit` | `42b6c4fc9158` | GATE GREEN; retire candidate | matches; owner decision |

## Staffing

Six subagents in parallel, split by ticket cluster. The cap of six at once is met, and no other round runs while this one does. The lead writes the roll-up.

- **S1:** 9852 (audit only, SQL surface) and 10882 (audit only, evaluator cluster, read against 13524's pinned evaluator behavior).
- **S2:** 13524 (live PR consistency) and 14231 (draft).
- **S3:** 10322 (draft at `80ce9d7a3c8`, with the moved head disclosed; check its assumptions against 17433's changed cache).
- **S4:** 12657 (draft) and 12505 (draft; check that the `defType=lucene` change in fetch() breaks no other branch's tests).
- **S5:** 11922 (finding and options), 14200 and 15326 (retire calls). These three are read together, because they meet in the parallel and cloud stream machinery and two of their premises died in settling runs.
- **S6:** 17433 / 17143 (retire call, the combined branch) and the cross-suite landing order for the shared-suite pairs (StreamExpressionTest: 10322 and 12657; StreamDecoratorTest: 11922 and 12505; StreamingTest: 14200 and 15326).

## Shared rules for every part

- Read the receipt first. A receipt at the exact live tip settles gate state. Do not re-audit a settled branch's gate. The audit verifies the recorded state against the branch and judges PR readiness.
- No builds, no Gradle, no tests, no gate runs. Gate evidence comes from the receipts. Where a receipt says NO GATE or gated at an older head, say what is owed instead of substituting a local run.
- No PRs, no comments, no edits to submit branches or live PR descriptions, no posting.
- Code claims cite file and line at the head SHA. Use `git show <head>:<path>` and `git diff`. Do not check anything out.
- Drafts: follow `pr-formula.md`. Each draft names its head in its Proof. Public text carries no internal process vocabulary (no gate, receipt, ledger, rc=0, "fresh JUnit XML", "pre-fix proof", "owed" as a label). The Proof states what ran, at which head, what passed, and that the new test fails without the fix. Where the receipt records the proof as partial, configuration-split, or inconclusive by construction, the draft says that instead. Titles must be accurate.
- Lucene versions: if a draft names one version, name every version that applies. Main and branch_10x pin Lucene 10.4.0; branch_9x pins 9.12.3.
- Plain words. No em dash and no en dash, anywhere, including the report.
- Each subagent returns its part report as text. The lead writes the files.

## Deliverables

1. `reports/streaming-expressions-round-1.md`: the per-ticket verdicts (draftable, held with reason, consistency result for the live PR, audit outcome for NO GATE tickets, or owner decision with options and a recommendation), disagreements with the receipts, and a short owner-decision list at the end.
2. Drafts in `pr-drafts/streaming/` for the draftable tickets: 10322, 12657, 12505 and 14231. Retire candidates and 11922 get no draft unless the owner rules keep.
3. The part reports, written by the lead to `reports/streaming-expressions-round-1-s1.md` through `-s6.md`.

## Not in scope

- SOLR-5754 (its audit home is Update processing; not re-audited here).
- `solr-16130-export-join` and `solr-8291-13217` (outside the population).
- Opening PRs, posting comments, editing submit branches or live PR descriptions, builds, Gradle, and test runs.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: reports/streaming-expressions-round-1.md. The work was performed by the original claimant; this mark is a record correction, not a new claim.
