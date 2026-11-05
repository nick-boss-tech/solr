# Claim: explicit and nested phrase slop repair, round 1 (Windows)

- Status: LIVE. Claimant: windows. Tags: implementation, review, records. Started: 2026-11-05 06:51:31 UTC.
- Authority: Nick's chat request to use the decision ladder and implement the adjacent patches after the PR 5294 scan.
- Tickets: SOLR-12532 / overlapping SOLR-11022, and SOLR-15611. These share the explicit-phrase slop entry point; one coherent combined patch is intended.
- Board snapshot checked: `b841908e77c2959cfe6024c1590bf59300150d93`. No competing implementation claim for this slice found. Existing fork `solr-12532-submit` at `69c06da446797cec5c3c8dd885e19a28381789d3` is retained unchanged; its base-only recursive repair does not cover eDisMax.
- Source parent: PR 5294 head `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`. New fork branch: `solr-12532-15611-phrase-slop`; Windows worktree `wt/SOLR-12532-15611-phrase-slop`.
- Decision ladder rung 1, corroborated by ticket intent at rung 3: explicit phrase slop must reach graph phrase construction; subparser state must not suppress a later independent phrase's slop. Feed the requested slop into analysis under a scoped save/restore, retain existing top-level postprocessing for custom consumers, and reset the nested-query guard for each explicit phrase. The standard and eDisMax parsers are the two real consumers. Preserve a nested parser's own slop and default slop on later clauses.
- Reuse the current TestMultiWordSynonyms schema and phrase-boost document setup. Add focused regressions for graph paths, explicit zero overriding qs, default restoration and nested-parser isolation. New assertions remain unproven until Linux fail-before and positive proof.
- Deliver: ordinary commit, same-session verified fork push, implementation report and exact-head Linux gate; status Implemented, Proof Pending. No change to PR 5294's branch or body in this claim.
- Windows runs no compiler/Gradle/tests. No public PR, comment, JIRA write or source history rewrite.
