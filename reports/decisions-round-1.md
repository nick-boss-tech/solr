# Decisions on open items, round 1

Verdict. Every open item that was held for the owner has a decision under the ladder: get information first, adopt a recorded recommendation where one exists, otherwise take the harder, more thorough option, and defer optional items as named Limits follow-ups. Drafts were edited where the decision changes text. Nothing was sent, posted or written to a PR. The items that need your send are listed at the end.

Inputs: `assignments/pool-decisions-round-1.md`, claim `claims/pool-decisions-round-1.md`. Fork branches for SOLR-8430, SOLR-4502 and SOLR-16437 were fetched read-only to resolve links.

## Decisions

### 1. Packaging commits: SOLR-11356 and SOLR-12161 move to the fork tip

Information. The receipts name a packaged head that is not on the fork. Each receipt also gives a labelled run at the fork tip. The packaging commit only removes the handoff file. SOLR-11356: "Head (8474e5a3a2, branch tests kept): tests=12 failures=0 from fresh JUnit XML", with the gate dated 2026-10-11. SOLR-12161: "Labelled tip run, L11: Head 1725cbd8489 (branch test as shipped): 1 run, 0 failures, 0 errors, 0 skipped", with the pin run dated 2026-10-11 UTC.

Rung. Step 1 and then step 2. The labelled tip run is the more thorough source available, so the drafts name the tip.

Edits.
- `pr-drafts/solrj/SOLR-11356.md`: every `6120dae28d04a07763f44d4bc5e9d9793af025f6` becomes `8474e5a3a26d8aacd25d9fc9838c627bc6c5b14f`. That covers the Proof line "This head", the code links and the changelog link. All cited paths were confirmed to exist at the tip.
- `pr-drafts/security/SOLR-12161.md`: `cdd265168a2adc76fb788f3d1f5587a3eee05e42` becomes `1725cbd8489889a1dad75279e4b2c11b0ec93d66`, and the short form `cdd265168a2a` becomes `1725cbd84898`. All cited paths were confirmed at the tip.

Caveat. Same-tree equality with the packaged head is taken from the receipts, not verified here, because the packaged objects are absent from this checkout.

### 2. Packaging commits: SOLR-10364 and SOLR-11678 stay on hold

Information. Neither receipt gives a labelled run at its fork tip. SOLR-10364's only run is at the unpushed packaged head. SOLR-11678's counts are at the packaged head, and its proof leg is INCONCLUSIVE by compile.

Rung. Step 1 is impossible here, since the packaged commits cannot be fetched. Hold, with the information request recorded: push the vm2 packaging commit, or have the main side name a labelled run at the fork tip.

### 3. Attributed counts are not carried into drafts

Information. Receipts that attribute a count only to a takeover log or ledger citation cannot support a Proof count. This is the rule already applied to dates.

Rung. Step 2, the more thorough option. Defer the optional content as a named Limits follow-up.

Edit. `pr-drafts/spellcheck/SOLR-4367.md`: the owner placeholder becomes "The run counts and the base-code result for this test are not stated here. They will be added once a recorded run supports them." SOLR-12212's count was removed in the earlier round under the same rule.

### 4. SOLR-2632: option (b), a Jira comment and no PR

Information. The receipt says the disposition is an owner call between (a) a test-only pin PR, which needs a new commit that cannot be gated here, and (b) a Jira comment with the finding and no PR. The receipt recommends (a) or (b).

Rung. Step 2. Option (a) is blocked on a gate this host cannot run, so option (b) is the path that can be completed. The highlighting draft stays a PR draft for now and is not to be sent as a PR. A Jira comment draft is the next item.

Owner action. Posting the Jira comment is yours.

### 5. SOLR-13136: keep the live behaviour

Information. The live tip keeps the shard in CONSTRUCTION after a failure once replicas exist. The PR title and changelog describe that CONSTRUCTION change. Deleting the shard on failure can drop writes the replicas already acknowledged. The receipt's "consistency pass only" label understates the behaviour change.

Rung. Step 2, the thorough option. Keep the live behaviour, which matches the title and changelog and avoids dropping acknowledged writes. A restored delete would need a new gate.

Actions. None on drafts, since there is no SOLR-13136 draft.
- The receipt is a main-side copy. The main side should correct its "consistency pass only" label and record a gate at `486b3877556` as owed.
- The PR #5017 Proof sentence dated 2026-10-06 does not match a run at that head, which the receipt dates to 2026-10-12. Owner action: remove or re-date it on the PR. Nothing was posted.

### 6. SOLR-15805 force update: accepted

Information. The fork branch was force-updated from `3432f950f0a` to `6d78603d825`. The two trees are identical (`23254470611802b3c2bac0bbde19733e1531ac69`), and the receipt body dates the run at the new tip to 2026-10-12.

Rung. Step 1 is complete. The draft already names the new tip. No action.

### 7. SOLR-15358: the 2026-10-12 proof run supersedes the 2026-10-03 pre-fix claim

Information. The 2026-10-12 proof run reverts CurrencyFieldType and gets 2 failures in CurrencyFieldTypeDocValuesTest. That is the fail-before proof. The 2026-10-03 receipt line that says the pre-fix proof passed contradicts it. The 2026-10-03 date for the with-fix pass comes only from a ledger citation.

Rung. Step 2. The later, more specific run supersedes the earlier claim. The ledger date comes out under the same rule as the other removals.

Edit. `pr-drafts/schema-analysis/SOLR-15358.md`: "Run at cbb345f2e7df452e20036d357c82cc63db96ffd3, recorded 2026-10-03:" becomes "Run at cbb345f2e7df452e20036d357c82cc63db96ffd3:". The 2026-10-12 sentence stays.

Main-side action. The receipt's 2026-10-03 pre-fix line should be marked superseded. The receipt is a main-side copy.

### 8. SOLR-11939: keep the 2026-10-12 date

Information. The receipt body has a main-side note dated 2026-10-12 that verifies the gate record at the head. That is a dated verification in the body, not a ledger citation.

Rung. Step 1 is done. Keep.

### 9. Link checks after fetching

- SOLR-8430 and SOLR-4502: the cited paths exist at the fork tips `0d64c35c5a7d` and `3695620b22ab`. The earlier misses were commits that had not been fetched. No change.
- SOLR-16437: the fork branch was force-updated from `aa2a6b8afb6` to `f72a9edd0a8b`. The receipt says the new head is a message-only rewrite, with an identical tree, and the draft already names it. The merge-base is `e2cdb2d7e8ae` for both. No change.
- SOLR-17866: one link pointed at the base commit for a test file that the branch adds, so it could not resolve there. The link now points at this change's test file on the tip (`3f9367d796a6`), and the sentence says the test file is copied into the base run, matching the receipt's premise.

## Drafts edited in this round

- `pr-drafts/solrj/SOLR-11356.md`: head swap to the fork tip.
- `pr-drafts/security/SOLR-12161.md`: head swap to the fork tip.
- `pr-drafts/spellcheck/SOLR-4367.md`: owner placeholder replaced with a named Limits follow-up.
- `pr-drafts/schema-analysis/SOLR-15358.md`: ledger date removed from the with-fix run.
- `pr-drafts/solrj/SOLR-17866.md`: base link corrected to the tip test file.

## Left for you to send

- The SOLR-2632 Jira comment under option (b), once drafted.
- The PR #5017 correction for the 2026-10-06 Proof sentence.
- Any PR edit that follows from the SOLR-13136 decision.

## Not done

- No build, test, Gradle run, PR write, comment or Jira post.
- No gate logs were opened. Receipts were taken as the record.
