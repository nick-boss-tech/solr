# Verdict landing, query-parsing patch loop, and the security category: round 1

Trigger: the owner allowed fork pushes and live PR edits, and the main side confirmed standing authority for the two held batch assignments. This round claimed them at pr-prepare tip `4c30bcd581c`. Round claim: `claims/pool-pr-prepare-tip-4c30bcd-round-1.md`.

Scope this round: drafts, reports and gate job files only. No live PR body was edited, since each edit needs its draft settled and re-read first. No fork branch was pushed, since no code patch was ready to gate. No PR opened, closed or flipped. No comment posted. No build, test or Gradle run.

## 1. Verdict landing (steps 1 and 2)

Ten verdict reports landed under `reports/`, byte-identical to their branch blobs. The full record, with provenance, the accounting, the flags, and one line per draft, is in `reports/branch-review-verdicts-landing-round-1.md`.

Headlines:
- Search components: 47 SATISFIED, 1 OPEN (gate), out of 48 with verdicts.
- Query parsing: 15 SATISFIED, 3 OPEN (gate), out of 18 with verdicts.
- 23 drafts have review-tip wording edits that are not on pr-prepare yet, so their verdicts describe the review tip.

## 2. Query-parsing patch loop (step 3, the three OPEN (gate) drafts)

- SOLR-6014: the Change line is qualified, Limits states the four 2026-10-13 verdicts, and the Proof date is added. Still open: a Linux gate for the base-merge probe, and a branch commit and gate for the in-branch fix.
- SOLR-12608: a fail-before premise sentence is added, an internal Limits bullet is replaced, and the Proof date is added. Still open: the changelog title (G2) needs a branch commit and a gate, so PR opening waits. The quoted base failure line rests on `g12608-premise.log`, which is not on disk.
- SOLR-17796: the Limits negated-collapse bullet states the EXCEPTION OCCURS verdict. Still open: the fix, the changelog title and the Limits offer line need code, grammar regeneration and a gate. SOLR-17796 and SOLR-12212 both change the QueryParser.jj Query production, so their landing order matters.

Disagreement D-1: `reports/query-parsing-round-1-q6.md` (NOTE 6) bars a SOLR-12608 fail-before claim without an on-disk base run, but the q2 sentence rests on the receipt alone. Kept and flagged.

## 3. Security category, round 2 (step 1 to 4 of the category assignment)

Full response: `reports/security-convergence-round-2-response.md`.

- CONVERGED: SEC-R1-01, 03, 04, 05, 06, 08, 09, 10.
- HOLD: SEC-R1-02, the SOLR-10627 title landing and changelog parse. A title patch is written: `gates/SOLR-10627-sec-r1-02-title.patch` (dry-run clean), with its landing and parse job in `gates/SOLR-10627-sec-r1-02-title.md`.
- HOLD: SEC-R1-07, the SOLR-11678 keystore type reading owed on vm2: `gates/SOLR-11678-sec-r1-07-keystore-reading.md`.
- Draft edits: SOLR-11678 (head moved to the pushed `2c68a83c`; the Proof split per test class; the base leg rewritten to the EnvUtilsTest proof; a custom `class://` Limits bullet added) and SOLR-12161 (head moved to the pushed `cdd265168a2`).
- Not patched, an owner call: the SOLR-11678 changelog title ("so HTTPS works") overstates the evidence.

## 4. Owner items from this round

1. Ownership of SOLR-16570 (search components or query parsing).
2. SOLR-11391: confirm the receipt at the live tip as the gate record (query-parsing decision D4).
3. The search-components summary wording about SOLR-18541 and the mergeIds staging file (they are excluded drafts, not unreviewed ones) and the SOLR-13876 edit count.
4. SOLR-12608: the G2 changelog title decision, and whether the `g12608-premise.log` gap blocks PR opening.
5. SOLR-11678: the changelog title wording; the keystore reading on vm2.
6. Whether to write the 23 pending wording edits from the review tips to pr-prepare, and to apply the drafts to live bodies. Live body edits are allowed but not yet done; they need each draft re-read first.

## 5. What is not done

- The live PR bodies for SOLR-6014, SOLR-12608, SOLR-17796, SOLR-11678 and SOLR-12161 are not edited. Each draft needs your review first.
- The 23 "No" drafts still carry pre-edit text on pr-prepare.
- The search-components OPEN (gate) item (SOLR-6193) and the ready lists (step 4) are not built.
- The round 2 category settlements for build-docs, CLI, streaming and SolrJ are not started.
