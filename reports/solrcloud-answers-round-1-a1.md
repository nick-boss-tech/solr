# SolrCloud answers round 1, part a1: draft corrections

Result: all seven assigned drafts are edited for items 1 to 5 and the bold openers. Nothing in part a1 is left undone. No DISCUSS position changed. One run stays owed before opening (SOLR-12651 at `f3131d1ee84`); that is main-side work, not a draft edit.

Scope: `pr-drafts/solrcloud/` SOLR-9155, SOLR-13186, SOLR-15106, SOLR-15386, SOLR-15863, SOLR-12651, SOLR-15035. Worktree head at start: `f52f0504fc8` (clean). No commit, no build, no test, no `gh` write, no posting.

## Changes

### SOLR-9155.md (items 1, 5, 6)
- FIX, header removed: lines 1 to 11 ("## Owner notes (remove before posting, not part of the PR text)" through "=== PR TEXT BELOW ==="). This drops the line "Open: the gate record gives no run date" and the two owner-decision lines.
- FIX, Proof date: old `` `ZkControllerGetLeaderTest` 1 of 1 at head 9f08d033023, verified on [date to confirm] `` new `` ... verified on 2026-10-06 ``. Source: `receipts/SOLR-9155.md`, "round 27, 2026-10-06".
- FIX, section order: Proof moved from after Limits to after "What this change does", before "## A choice to check". Text unchanged apart from the date above.
- FIX, Limits opener added (new line 35): `**Three limits remain: the flow-control point, the shared message on the conflict path, and when the test interrupts.**`
- Choice and Limits bullets not reworded. Checked against the answers entry for 9155: the SolrException route, the conflict-path wording and the flow-control limit all match.

### SOLR-13186.md (items 1, 6)
- FIX, header removed: lines 1 to 10 ("## Owner notes" block, the "Open: the gate record gives no run date" line, and "=== PR TEXT BELOW ===").
- FIX, Proof date: old `verified on [date to confirm]` new `verified on 2026-10-06`. Source: `receipts/SOLR-13186.md`, "round 27, 2026-10-06".
- FIX, Limits opener added (line 25): `**The test covers one path and runs on mocks, and the election sequence node is not touched.**`

### SOLR-15106.md (items 1, 6)
- FIX, header removed: lines 1 to 11 (owner-notes block, including the two "Open" lines about the base failure text and the "Owner call" line, and "=== PR TEXT BELOW ===").
- FIX, Proof date: old `verified on [date to confirm]` new `verified on 2026-10-07`. Source: `receipts/SOLR-15106.md`, "takeover log (round 35, 2026-10-07)".
- FIX, Limits opener added (line 27): `**Not covered: the session-expiry race, a real session expiry or multi-node failure, and the early-return paths.**`

### SOLR-15386.md (items 1, 5, 6)
- FIX, header removed: lines 1 to 10 (owner-notes block, including "Open owner question (main side, not settled)" and the "Open" line, and "=== PR TEXT BELOW ===").
- FIX, Proof date: old `verified on [date to confirm]` new `verified on 2026-10-07`. Source: `receipts/SOLR-15386.md`, "takeover log (round 35, 2026-10-07)".
- FIX, section order: Proof moved from after Limits to after "What this change does", before "## A choice to check". Text unchanged apart from the date above.
- FIX, Limits opener added (line 36): `**The guard has no end-to-end test, does not run on the distributed path, and is ignored by an older Overseer.**`
- Choice unchanged. The answers adopt the single-check position as drafted.

### SOLR-15863.md (item 3)
- FIX, Choice opener added (line 29): `**The implemented route records the oldest segment version in indexVersion, and a separate field is the alternative.**`

### SOLR-12651.md (items 2 and 3)
- FIX, Proof bracketed line (line 24): old `- [Before opening: run the class at f3131d1ee84, which adds the property-upload check and its cleanup, and replace this line with the result.]` new `- A run at the live tip, `f3131d1ee84`, is owed before opening, because that head adds the property-upload check. The result above is for the earlier head `90032e274b7`, which does not have it.`
  - Deviation from the claim's wording: the claim says "the gated head". I wrote "earlier head" because "gated" is internal process vocabulary under the shared rules.
- FIX, Choice opener added (line 28): `**The implemented route deletes the new collection on failure, and the alternative keeps it so a retry can resume.**`
- Sources: `receipts/SOLR-12651.md` (gated head `90032e274b7`, live tip `f3131d1ee84`, "2026-10-04"). The answers say the property-upload leg is in the two commits after the gate and has no run.

### SOLR-15035.md (item 4)
- FIX, Proof sentence (line 21): old `On the base code it fails with [the observed failure line, to be copied from the run output before opening].` new `On the base code it fails. So does the ADDREPLICA change alone, for the reason in the next bullet.`
- Basis: `receipts/SOLR-15035.md` records that the ADDREPLICA-only patch failed the shipped test, because the CoreAdmin create path copies only its allowlist and numShards was not on it. The base failure line is not in the receipt, in `reports/solrcloud-round-1-p2.md`, or in any file on disk (gate logs are not on disk). So no line is quoted and no placeholder remains.

## Not done

- None of the seven drafts has an item left undone.
- Not part of this part: item 7 (SOLR-15674 length) and item 6 for the other drafts (part a2). Other draft files show as modified in this worktree (SOLR-5813, 11288, 12991, 13369, 14919, 15674, 17292, 17680, 17733). They are not from this part.
- Owed before opening, not a draft fix: the run of SOLR-12651 at `f3131d1ee84`. The draft states it.

## Notes (NOTE, no change made)

- NOTE, SOLR-15106.md line 23 (Proof): the removed header asked for the base failure to be confirmed against the base run before posting. The receipt says only "the premise step fails as designed", and the gate log is not on disk. The Proof names the assertion, which the answers accept. The confirmation stays open.
- NOTE, SOLR-15386.md (removed header): the answers adopt the single-check position as drafted, but `receipts/SOLR-15386.md` still says the owner question "stays open on the main side ... it is Nick's call". Confirm before posting. The Choice text is unchanged and still poses the question to the maintainer.
- NOTE, SOLR-12651.md line 24: "owed before opening" is process wording. Replace it with the run result, or cut it, before the PR is posted.
- NOTE, SOLR-15035.md line 9: "The ticket summary describes this difference." No Jira packet for 15035 is on disk (`reports/solrcloud-round-1-p2.md`, finding 8). Confirm the summary before opening.
- NOTE, bold openers versus bullets: the new Limits openers name the same limits the bullets state. The presentation rule says a claim is carried once. I wrote the openers at summary level and did not rewrite the bullets. The lead decides whether to trim the bullets.
- Checked and clean: SOLR-9155 Choice says both `getLeader` callers already handle Exception. Verified at `9f08d033023` in `ZkController.java`: `register` declares `throws Exception` (line 1384), and the `getLeader` call in `rejoinShardLeaderElection` sits inside a `try` (line 2588) with `catch (Exception e)` (line 2618). No change.

## Checks

- Dashes: byte check for U+2014 and U+2013 in the seven drafts after edits: 0 hits.
- Internal words after edits: none of gate, gated, receipt, ledger, handoff, top-up, takeover, audit, rc=0, JUnit, pre-fix, premise, banked, owner, round, lane appears in the seven drafts' text. Two hits are acceptable: "logs" in SOLR-9155 (describes base behavior, not a process word), and "before opening" in SOLR-12651 (see the NOTE above).
- Placeholders: none left. No "[date to confirm]", no bracketed instruction, and no non-link brackets in the seven drafts.
- Section order: SOLR-9155 and SOLR-15386 now run What happens today, What this change does, Proof, A choice to check, Limits. The other five were already in that order.
- Openers: every Limits and Choice section in the seven drafts now opens with a bold one-line summary. Proof and What this change does sections already did.
- Dates: all four Proof dates come from the receipts named above, in the "verified on <date>" form the drafts already use. The answers' "recorded" wording was not copied into the drafts.
- Counts: unchanged and checked against receipts (9155 1 of 1; 13186 1 of 1; 15106 1 of 1; 15386 3 of 3; 12651 2 of 2; 15035 4 of 4; 15863 6, 4 and 7).
- Git: only the seven drafts were edited by this part. Nothing staged or committed. Git warns that LF will become CRLF on the next touch. HEAD stores LF, so no action was taken.
