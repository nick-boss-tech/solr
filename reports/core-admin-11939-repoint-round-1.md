# SOLR-11939 core-admin draft re-point, round 1

Verdict: **CONVERGED on the draft text at packaged head `cdb9903506e9`, with one note.** The ticket facts in the first paragraph of "What happens today" were not re-checked against JIRA this round. The draft is about 3,900 characters, above the roughly 3,500 length guide, because the formula asks for citations and each one is a link.

Scope: `pr-drafts/core-admin/SOLR-11939.md` only. Claim: `claims/pool-core-admin-11939-gate-repoint.md`. Trigger: the gate for SOLR-11939 went green at `cdb9903506e9` (`receipts/SOLR-11939.md`, top block), and the fork tip `solr-11939-submit` equals that head. Owed by core-admin round 1, CA-R1-29 and CA-R1-30.

Counts: two read-only subagents, four tasks each (A1 to A4 for citations and docs anchors, B1 to B4 for receipt facts, wording, Limits and formula). Mismatches found: 12 (B1 5, B2 4, B3 1, B4 2). Applied: 12. Not applied: 0. Nothing was edited, posted, built or tested except the draft and this report.

## Changes to the draft

- **What happens today:** deleted "One name cannot fit all four shards, so" (CA-R1-29; the code does not show that reason, and the note states it instead). The reference guide entry is now a link at the base commit `cabedd1d968059215188f4e7563fb303241899ed`, L246-L262.
- **What this change does:** the summary now says `property.name` "does not rename the cores of a new collection", which is the note's scope. The note link points to `cdb9903506e9` at L264-L265. "It gives the generated name format" became "It gives an example generated name", because the note gives one example, not a format.
- **Proof:** the stale text "The guide was not built for this change" is replaced (CA-R1-29 and CA-R1-30). The new text names the head `cdb9903506e9c20c32aa4ec2be77f500235399ba`, the verification date 2026-10-12, the site build, the rendered note, and the changelog parse and lint. The code links now use the base `cabedd1d968`, and the CreateCollectionCmd range is L305-L310 (the old L306-L311 included an unrelated `if`).
- **Limits:** all base-commit links use `cabedd1d968`, and the sentence names that SHA.
- **Changelog link:** points to `cdb9903506e9`. The fragment is identical at the old head.

## Checks that held

- The NOTE at L264-L265 at `cdb9903506e9` matches the draft's scope and its "one name cannot be valid for every shard and replica" wording.
- `CreateCollectionCmd.java`, `Assign.java`, `CreateReplica.java` and `AddReplicaCmd.java` are byte-identical between `b6b2b8f10e` and `cabedd1d968`, so the old ranges still hold at the new base.
- The branch changes only the docs page (+3 lines) and the changelog fragment (+8) against `cabedd1d968`. No ADDREPLICA or CREATE code changed, so the Limits claims hold.
- No em dash, no en dash, no PR number, no process vocabulary (gate, receipt, claim, job, host, vm2, rc).

## Not checked

- The ticket facts in "What happens today" (`property.name=carmen_test`, four shards, the `carmen-test` example). They are not in the receipt, and JIRA was not read this round.
- The VM2 gate logs. The Proof cites the receipt, which is the named source.
- The older "NO GATE" block in the receipt (head `d4cff5e`). Only the top block was used.

## Convergence line

CONVERGED on the draft text at `cdb9903506e9`. Open for Nick: the JIRA check of the ticket facts, and whether to trim the draft toward the 3,500 length guide.
