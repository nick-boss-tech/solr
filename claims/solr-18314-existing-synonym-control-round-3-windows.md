# Claim: SOLR-18314 existing synonym control repair, round 3, Windows

Status: CLAIMED

- Authority: Nick's authorized adjacent implementation, followed by the returned failed receipt; ordinary scoped repair under the round-1 gate's repair route.
- Snapshot: `5c782192692ba3ef51694c626835861716b411f0`. Read both receipts, round-2 superseding gate and claims; the Linux executor explicitly handed the semantic test repair back to the implementation owner. No competing repair claim found at this snapshot.
- Job: `gates/solr-18314-edismax-empty-phrase-round-2.md`; receipt `receipts/SOLR-18314-edismax-empty-phrase-round-1.md` is FAILED and remains historical, never overwritten.
- Branch: `nick-boss-tech/solr:solr-18314-edismax-empty-phrase`, live E2 `9b4bf9276515187b3d29a39a0923f644e1b457d2`, independently verified through refs API. Preserve E2's tidy repair by fast-forwarding the clean task worktree before editing. PR 5294 and the independently green slop branch stay untouched.
- Exact implementation path: `solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java` only. No production, schema, synonym configuration or changelog change.
- Decision ladder rung 1: P's shared synonyms.txt already contains `crow blackbird, grackle`; PR 4652 adds `usa => united states of america`, which was not included in this bounded candidate. The receipt's absent usa expansion is therefore a missing test premise, not evidence of a synonym-generation production bug. Use the existing grackle mapping instead of adding a global test synonym or deleting graph coverage. Isolate the synonym case as its own method so a compiling mutation can discriminate it independently of wi-fi. No new helper or analyzer fixture is needed.
- Rung 2 remains Linux-only: require the corrected preservation controls to pass on both original production and the repaired head, and fail independently under the over-aggressive one-raw-clause mutation. Retain the two original fail-before assertions and rerun exact-head required suites/checks through a superseding round-3 gate.
- Windows performs source review/diff checks only, no Gradle/compilation/tests. Push the ordinary test-only commit and separately verify its remote SHA in this session, then post report and gate.
- No new PR, PR-body edit, public comment, Jira write, upstream/contributor-branch action or force push is authorized by this claim. Status remains Implemented, Proof Pending until the new gate succeeds.
