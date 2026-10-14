# Category ready lists, round 1 (W1 step 4)

- Assignment: `assignments/pool-category-closeout-overnight-round-1.md`, W1 step 4. Claim: `claims/pool-category-closeout-w1-ready-lists-round-1.md`.
- Tree: worktree `wt/pr-prepare-suggester`. Drafts, receipts and rules read from `origin/pr-prepare` at `5205dda997a`. Live tips read with `git ls-remote origin` on 2026-10-13.
- Read only: no build, test, Gradle or gate run; no commit, push, checkout, stash or reset; no GitHub write; no live PR body; no draft edited.
- Written by the parent from the subagent's returned text. The subagent could not write `.md` report files.

## Owner rulings (2026-10-14), applied

- Proof verification date is advisory, not a hold. The 13 drafts that held only on it move to READY: search components SOLR-6207, 6831, 6975, 7550, 10694; query parsing SOLR-4824, 8977, 9048, 9149, 10897, 11761, 17280, 17311. Gate dates can still go into Proof where the author wants them.
- Changelog: a missing fragment is fine for a tests-only change. SOLR-18109 stays READY on that basis.
- Length: one branch over 3,850 visible characters is acceptable. SOLR-15319 (3,945) is the only one. Its length no longer holds it; its S1 and S3 holds remain.
- Counts after these rulings: READY 30 (search components 19, query parsing 11); HOLD 32 (search components 28, query parsing 4). Total 62.
- The per-draft lists below are the round 1 record. Apply these rulings to them.
- Still open, unchanged by these rulings: the SOLR-17051 receipt sentence (correct the receipt, not the draft), SOLR-11391 D4, the SOLR-9148 changelog type, and the SOLR-874, SOLR-14381 and SOLR-15319 title and wording items.
- PR #5115: the body was trimmed to about 3,200 visible characters and PATCHed. The read-back matches the trimmed text; the only difference is a byte-order mark that the read-back adds. The draft `pr-drafts/search-components/mergeids-skipped-shard-totals.md` carries the same text.

## Summary

- Candidates: 62 drafts (47 search components, 15 query parsing, all SATISFIED). The 20 round 2 landings are inside these 62; all 20 match their verdict-branch blobs (20 of 20).
- Not on either list: SC SOLR-6193 (OPEN gate); SC SOLR-18541 and mergeids-skipped-shard-totals (excluded by the assignment); QP SOLR-6014, SOLR-12608, SOLR-17796 (OPEN gate).
- Counts: search components READY 14, HOLD 33. Query parsing READY 3, HOLD 12. Total READY 17, HOLD 45.
- Most common HOLD: a section does not open with a bold one-line summary (29 drafts, 40 sections, mostly Limits written as bullets). Next: Proof has no verification date (20 drafts; 13 of them have no other hold).

## Receipts and tips

- Receipts live on `pr-prepare` under `receipts/`. None of the 62 submit tips carries a receipt file (checked with `git cat-file`). The receipt at the live tip is read as the `pr-prepare` receipt whose gated head equals the live tip. All 62 match and all read GATE GREEN.
- All 62 submit branches exist on origin; every live tip is a local commit.
- Gate dates come from the receipt's gated-head line. Five carry no date there (SOLR-8009, 10424, 10694, 12044, QP SOLR-11391); those lines say so.

## Checks run

1. Live head: `ls-remote` per submit branch, 62 of 62.
2. Receipt at tip: gated head equals live tip for 62 of 62; all GATE GREEN.
3. Head citations: every cited head equals the live tip. Other SHAs are the merge-base (`git merge-base` with `upstream/main`) or an earlier head the text labels as earlier. Exceptions: SC SOLR-7390 links an "upstream main" file at `8e62c2686882` (merge-base is `0cc328310f8f`); SOLR-14381, 9148 and 11364 cite earlier heads, labeled.
4. Counts: every Proof count compared with the receipt Counts line. No mismatch.
5. Packaging: `TESTING*.md` absent at all 62 tips. Changelog fragment present at the tip for 61; SC SOLR-18109 has none, and its draft says a test-only change needs none. YAML read by hand for all 61 (title, type, authors, links): no unquoted title with ": " or " #", no indent fault, author Nick Shanin on all, no ICLA placeholder. One type not listed in dev-docs (SOLR-12044 "optimized"). Changelog link SHA equals the tip and names the fragment at the tip for all 61. Title against draft claim: two failures (SOLR-874, SOLR-14381).
6. `pr-formula.md`: bold one-line summary tested on the first non-blank line of each `##` section (40 sections in 29 drafts fail; the AI assistance footer is exempt). Em dashes: none in any draft. "we": none. AI header, Jira link line and AI footer present in all 62. Process vocabulary: tidy check and Error Prone compile sentences in SOLR-15895, 16444, 17791; landing vocabulary in SOLR-15319 Limits. Visible length (rendered text): all under 3,700 except SOLR-15319 at 3,945. Plain language: sentence length and a jargon scan for all 62; the 17 READY drafts read in full.

## Judgment calls for the owner

- Bold rule read strictly: a bullet list or plain sentence under a heading fails, and so does a bold summary sharing its line with evidence (QP SOLR-15615).
- Date rule: `pr-formula.md` section 3 requires "the verification date and head" in Proof. Applied as a hold. If advisory instead, these 13 move to READY: SC SOLR-6207, 6831, 6975, 7550, 10694; QP SOLR-4824, 8977, 9048, 9149, 10897, 11761, 17280, 17311.
- Length limit 3,850 visible characters (about 10 percent over the 3,500 guide). Only SOLR-15319 exceeds it.
- SOLR-18109 is READY on its own "not needed" changelog note. Owner to confirm.

## Round 2 flags

- SOLR-17051 (wording against tip): the draft matches the tip. `FacetFieldProcessor.java` lines 534-542 at the tip screen single-node requests (`effectiveMincount > 1`); `FacetFieldMerger.java` lines 139-145 screen the summed count. The receipt sentence "only at the coordinator" is stale; correct the receipt, not the draft. Draft stays HOLD for the Limits summary and the Proof date.
- QP SOLR-11391 (decision D4): the receipt names GATE GREEN at `b0f15a22856a`; the D4 confirmation is still open with the main side. Draft stays HOLD for the Limits summary and the Proof date.

## Not checked

- No build or test. Upstream-code facts in drafts were not re-verified (for example the PR 4779 merge date in SOLR-18109), except the SOLR-17051 tip lines.
- The 45 HOLD drafts were not read in full; their holds rest on the mechanical checks above.
- Title accuracy is a reading check against each bold claim.

## READY: search components (14)

- SOLR-4374 | head `801c62290f79` | receipt `receipts/SOLR-4374.md`: GATE GREEN at this head, gate 2026-10-07
- SOLR-7498 | head `2050d8e447a7` | receipt `receipts/SOLR-7498.md`: GATE GREEN at this head, gate 2026-10-05
- SOLR-8020 | head `79f790523d2f` | receipt `receipts/SOLR-8020.md`: GATE GREEN at this head, gate 2026-10-05
- SOLR-9148 | head `30f0d7a42d50` | receipt `receipts/SOLR-9148.md`: GATE GREEN at this head, gate 2026-10-06
  - Advisory: type "added" is opt-in in dev-docs; a fq pass-through fix reads fixed or changed; owner to confirm. The tip proof is a labeled smoke run (the tip adds a docs-only commit over `f8758ebe763`, where the premise proof ran).
- SOLR-9396 | head `a5ab2eda4e6a` | receipt `receipts/SOLR-9396.md`: GATE GREEN at this head, gate 2026-10-05
- SOLR-9864 | head `b5826e466b9a` | receipt `receipts/SOLR-9864.md`: GATE GREEN at this head, gate 2026-10-05
- SOLR-10492 | head `ecf21e2192cb` | receipt `receipts/SOLR-10492.md`: GATE GREEN at this head, gate 2026-10-05
  - Advisory: the title omits the second behavior change (`facet.field` with `group.facet=true` and `group.field`, without `group=true`, now returns grouped counts instead of BAD_REQUEST); consider adding it.
- SOLR-11129 | head `dc82676b8096` | receipt `receipts/SOLR-11129.md`: GATE GREEN at this head, gate 2026-10-12
- SOLR-11153 | head `093df0d65bdc` | receipt `receipts/SOLR-11153.md`: GATE GREEN at this head, gate 2026-10-05
- SOLR-12543 | head `88d236db6b7d` | receipt `receipts/SOLR-12543.md`: GATE GREEN at this head, gate 2026-10-08
- SOLR-12556 | head `033ec65a0e1b` | receipt `receipts/SOLR-12556.md`: GATE GREEN at this head, gate 2026-10-05
- SOLR-13876 | head `2e110473dbad` | receipt `receipts/SOLR-13876.md`: GATE GREEN at this head, gate 2026-10-07
  - Advisory: the gate ledger row names no gate log or date; round 35 re-runs at this head are dated 2026-10-07.
- SOLR-15041 | head `55fca0a7c243` | receipt `receipts/SOLR-15041.md`: GATE GREEN at this head, gate 2026-10-03
  - Advisory: the bold claim "fails on the base code" rests on the receipt's pre-fix proof PASS; no premise count or log is on record.
- SOLR-18109 | head `b19395e1f60a` | receipt `receipts/SOLR-18109.md`: GATE GREEN at this head, gate 2026-10-04
  - Advisory: no changelog fragment at tip; the draft says the change is test only and needs none; owner to confirm.

## READY: query parsing (3)

- SOLR-12532 | head `69c06da44679` | receipt `receipts/SOLR-12532.md`: GATE GREEN at this head, gate 2026-10-04
- SOLR-12871 | head `c79a49320cdb` | receipt `receipts/SOLR-12871.md`: GATE GREEN at this head, gate 2026-10-07
- SOLR-16267 | head `8f4b0c6d2fb0` | receipt `receipts/SOLR-16267.md`: GATE GREEN at this head, gate 2026-10-04

## HOLD: search components (33)

- SOLR-5394 | head `967445622f92` | receipt `receipts/SOLR-5394.md`: GATE GREEN, gate 2026-10-07
  - Step: S1 bold summary missing on Proof, A choice to check, Limits.
  - Advisory: fragment type fixed for a default change; dev-docs reads changed for modified defaults.
- SOLR-6207 | head `b099a9f1be5a` | receipt `receipts/SOLR-6207.md`: GATE GREEN, gate 2026-10-07
  - Step: S2 Proof has no verification date.
- SOLR-6831 | head `96b33ba5f6f8` | receipt `receipts/SOLR-6831.md`: GATE GREEN, gate 2026-10-06
  - Step: S2 Proof has no verification date.
- SOLR-6975 | head `761aa629bb83` | receipt `receipts/SOLR-6975.md`: GATE GREEN, gate 2026-10-05
  - Step: S2 Proof has no verification date.
- SOLR-7390 | head `7463dd006a78` | receipt `receipts/SOLR-7390.md`: GATE GREEN, gate 2026-10-05
  - Step: S1 bold summary missing on Limits.
  - Advisory: one link labeled "upstream main" points at `8e62c2686882`, not the merge-base `0cc328310f8f`.
- SOLR-7520 | head `10b6931e1c05` | receipt `receipts/SOLR-7520.md`: GATE GREEN, gate 2026-10-05
  - Step: S1 bold summary missing on Proof, Limits.
- SOLR-7550 | head `687165651f92` | receipt `receipts/SOLR-7550.md`: GATE GREEN, gate 2026-10-03
  - Step: S2 Proof has no verification date.
- SOLR-8009 | head `8795661ddc98` | receipt `receipts/SOLR-8009.md`: GATE GREEN, gate not on the gated line (ledger)
  - Step: S1 bold summary missing on Limits.
- SOLR-8240 | head `fad7a1dd8e23` | receipt `receipts/SOLR-8240.md`: GATE GREEN, gate 2026-10-05
  - Step: S1 bold summary missing on Limits.
- SOLR-8939 | head `a855a2d8965c` | receipt `receipts/SOLR-8939.md`: GATE GREEN, gate 2026-10-06
  - Step: S1 bold summary missing on Limits.
- SOLR-10424 | head `e629ab8bb293` | receipt `receipts/SOLR-10424.md`: GATE GREEN, gate not on the gated line (status line: 2026-10-08)
  - Step: S1 bold summary missing on Limits; S2 Proof has no verification date.
- SOLR-10694 | head `64e86811548b` | receipt `receipts/SOLR-10694.md`: GATE GREEN, gate not on the gated line (ledger)
  - Step: S2 Proof has no verification date.
- SOLR-10844 | head `e87515c56d04` | receipt `receipts/SOLR-10844.md`: GATE GREEN, gate 2026-10-05
  - Step: S1 bold summary missing on Proof, A choice to check, Limits.
- SOLR-11310 | head `e38ddec5279c` | receipt `receipts/SOLR-11310.md`: GATE GREEN, gate 2026-10-03
  - Step: S1 bold summary missing on Limits.
- SOLR-11364 | head `562d3e7e685a` | receipt `receipts/SOLR-11364.md`: GATE GREEN, gate 2026-10-08
  - Step: S1 bold summary missing on Limits.
- SOLR-11470 | head `f44c294da374` | receipt `receipts/SOLR-11470.md`: GATE GREEN, gate 2026-10-05
  - Step: S1 bold summary missing on Limits.
- SOLR-12044 | head `c92bbf9349b1` | receipt `receipts/SOLR-12044.md`: GATE GREEN, gate not on the gated line (ledger)
  - Step: S1 bold summary missing on Limits; S2 Proof has no verification date; S4 fragment type "optimized" is not in dev-docs (use changed or fixed).
- SOLR-13245 | head `16e62ab65425` | receipt `receipts/SOLR-13245.md`: GATE GREEN, gate 2026-10-05
  - Step: S1 bold summary missing on Limits.
- SOLR-14381 | head `a28b3f672cb4` | receipt `receipts/SOLR-14381.md`: GATE GREEN, gate 2026-10-07
  - Step: S1 bold summary missing on A choice to check, Proof, Limits; S4 changelog title is cut off mid-sentence ("...SolrJ GroupCommand getMatches and getNGroups now return long and Long") and runs about 390 characters; write a complete title matching the draft claim.
- SOLR-14451 | head `e60891d8716d` | receipt `receipts/SOLR-14451.md`: GATE GREEN, gate 2026-10-07
  - Step: S1 bold summary missing on Limits.
- SOLR-14678 | head `5d94e6cf3981` | receipt `receipts/SOLR-14678.md`: GATE GREEN, gate 2026-10-07
  - Step: S1 bold summary missing on Limits.
- SOLR-14931 | head `1a7d678d9a15` | receipt `receipts/SOLR-14931.md`: GATE GREEN, gate 2026-10-07
  - Step: S1 bold summary missing on A choice to check, Proof, Limits.
- SOLR-15018 | head `d0f29b4630c0` | receipt `receipts/SOLR-15018.md`: GATE GREEN, gate 2026-10-07
  - Step: S1 bold summary missing on Limits.
- SOLR-15319 | head `4bda91f46fc1` | receipt `receipts/SOLR-15319.md`: GATE GREEN, gate 2026-10-07
  - Step: S1 bold summary missing on Limits; S3 Limits names the SOLR-8051 branch and uses internal wording ("landed", "disposition", "rebased", "the run settles"). The SOLR-8051 landing dependency (named in the round 1 landing report) must be stated in plain words, or the owner decides the ordering; S5 visible length 3,945, over 3,850.
- SOLR-15331 | head `6769b4cd8a12` | receipt `receipts/SOLR-15331.md`: GATE GREEN, gate 2026-10-03
  - Step: S1 bold summary missing on Limits.
- SOLR-15479 | head `57bd53ce4d07` | receipt `receipts/SOLR-15479.md`: GATE GREEN, gate 2026-10-07
  - Step: S1 bold summary missing on Limits.
- SOLR-15895 | head `02930909397a` | receipt `receipts/SOLR-15895.md`: GATE GREEN, gate 2026-10-03
  - Step: S3 delete the Proof sentence "The tidy check and the Error Prone compile also passed."
- SOLR-16444 | head `8ffee94a5e75` | receipt `receipts/SOLR-16444.md`: GATE GREEN, gate 2026-10-05
  - Step: S3 delete the Proof sentence "The tidy check and the Error Prone compile also passed."
- SOLR-17051 | head `148544e9ed54` | receipt `receipts/SOLR-17051.md`: GATE GREEN, gate 2026-10-04
  - Step: S1 bold summary missing on Limits; S2 Proof has no verification date.
  - Advisory: receipt sentence "only at the coordinator" is stale; the draft matches the tip (see round 2 flags).
- SOLR-17155 | head `1413237f7a75` | receipt `receipts/SOLR-17155.md`: GATE GREEN, gate 2026-10-05
  - Step: S1 bold summary missing on A choice to check, Proof, Limits; S2 Proof has no verification date.
- SOLR-17748 | head `dfacaf347668` | receipt `receipts/SOLR-17748.md`: GATE GREEN, gate 2026-10-04
  - Step: S1 bold summary missing on Limits.
  - Advisory: the title covers the one-pass route only; the draft also covers the error-with-no-cause route. Accurate, narrower than the draft.
- SOLR-17791 | head `a39c1c973766` | receipt `receipts/SOLR-17791.md`: GATE GREEN, gate 2026-10-05
  - Step: S3 delete the Proof sentence "The tidy check and the Error Prone compile passed for core and the LTR module..."
- SOLR-17976 | head `56ea43c448ed` | receipt `receipts/SOLR-17976.md`: GATE GREEN, gate 2026-10-05
  - Step: S1 bold summary missing on Limits.

## HOLD: query parsing (12)

- SOLR-874 | head `ac9ab3375375` | receipt `receipts/SOLR-874.md`: GATE GREEN, gate 2026-10-06
  - Step: S1 bold summary missing on Limits; S2 Proof has no verification date; S4 changelog title says a leading NOT is dropped, but the draft keeps a leading NOT ("NOT ipod is a valid query"). Narrow the title to the four leading operators (AND, OR, &&, ||).
- SOLR-4824 | head `76c777e4661e` | receipt `receipts/SOLR-4824.md`: GATE GREEN, gate 2026-10-07
  - Step: S2 Proof has no verification date.
- SOLR-8977 | head `395b24964fd0` | receipt `receipts/SOLR-8977.md`: GATE GREEN, gate 2026-10-05
  - Step: S2 Proof has no verification date.
- SOLR-9048 | head `b145018563cb` | receipt `receipts/SOLR-9048.md`: GATE GREEN, gate 2026-10-05
  - Step: S2 Proof has no verification date.
- SOLR-9149 | head `151dfed119ee` | receipt `receipts/SOLR-9149.md`: GATE GREEN, gate 2026-10-05
  - Step: S2 Proof has no verification date.
- SOLR-10897 | head `d9240d0750fa` | receipt `receipts/SOLR-10897.md`: GATE GREEN, gate 2026-10-05
  - Step: S2 Proof has no verification date.
- SOLR-11391 | head `b0f15a22856a` | receipt `receipts/SOLR-11391.md`: GATE GREEN, gate not on the gated line (ledger)
  - Step: S1 bold summary missing on Limits; S2 Proof has no verification date.
  - Advisory: D4 (receipt as gate record) still awaits main-side confirmation.
- SOLR-11761 | head `41893ee9ce61` | receipt `receipts/SOLR-11761.md`: GATE GREEN, gate 2026-10-05
  - Step: S2 Proof has no verification date.
- SOLR-12212 | head `876953fdc927` | receipt `receipts/SOLR-12212.md`: GATE GREEN, gate 2026-10-05
  - Step: S1 bold summary missing on Limits.
- SOLR-15615 | head `be77267bf554` | receipt `receipts/SOLR-15615.md`: GATE GREEN, gate 2026-10-07
  - Step: S1 the bold summary shares its line with the evidence under "A choice to check"; split it onto its own line; S2 Proof has no verification date.
- SOLR-17280 | head `40817c5cb7ec` | receipt `receipts/SOLR-17280.md`: GATE GREEN, gate 2026-10-07
  - Step: S2 Proof has no verification date.
- SOLR-17311 | head `9f7524582f9d` | receipt `receipts/SOLR-17311.md`: GATE GREEN, gate 2026-10-04
  - Step: S2 Proof has no verification date.

## Step key

- S1 = section lacks a bold one-line summary (add one bold line as the first line of each listed section).
- S2 = Proof lacks a verification date (add "verified <gate date>" from the receipt).
- S3 = internal gate or landing wording.
- S4 = changelog title or type.
- S5 = visible length over 3,850.
