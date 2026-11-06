# SOLR-18623 exact-head green proof and public follow-up, round 4

Status: GATE GREEN at H3; tested fix offered publicly; separate read-back confirmed.

- Reviewed board snapshot: `9981127c1ae999d442c5f15bca806aa9896501ef`.
- Receipt: `receipts/PR-5301-reconnect-liveness-round-3.md`; required job `gates/pr-5301-reconnect-liveness-round-3.md`.
- Offered and tested H3: `dfde9bde0a348ad44e6f6ceec197d478ef11bfd0` on `nick-boss-tech/solr:solr-18623-reconnect-liveness`. Independent remote ref read matches H3. H0 `01e820f0539cddc4697d26176d216614a776a99a` remains an ancestor (git merge-base check exit 0).
- Live PR: apache/solr 5301, OPEN at H0, contributor branch SOLR-18623-connection-manager-event-ordering. Prior reviewed discussion: no conversation or inline comments, one unchanged approval from dsmiley for H0. The guarded publisher refreshes all three before posting and refuses if their identity/content changed.
- Authority: Nick's standing clean-gate authorization in root AGENTS.md. The actual receipt and every named proof leg were audited at the exact offered head; no subsequent draft-review hold exists in this chat. This covers one factual contributor follow-up comment only.
- Comment URL: https://github.com/apache/solr/pull/5301#issuecomment-6673432147 . Posted once at 2026-11-06 00:39:55 UTC by nick-boss-tech; stored updatedAt is the same timestamp.
- Stored text/read-back: a separate issues/comments/6673432147 GET matched the complete local draft exactly (exactBodyMatch=true). Fresh PR and fork reads still showed H0 and H3, respectively, OPEN with one conversation comment (the offer) and the same one review. Raw before/after JSON, stored body, URL and comparison summary are under local evidence/PR-5301/public-followup/.

## Required proof audited

The evidence archive `evidence/pr-5301-reconnect-round-3/pr-5301-round-3-evidence.tar.gz` was read from the pinned board commit and matches SHA-256 `72ffbe08ea9023eff6425a17f17161e86444e85ee2bbb691bdeb431faf105bc2`. Logs, XML, original-source/mutation diffs and hashes are extracted under local `evidence/PR-5301/gate-round-3/`.

- New ordering suite: all five methods execute and pass, zero skips. Existing manager suite: two methods pass, plus one pre-existing ignored test. Client neighbor: seven pass with no skips. The archived module report folders include copied neighbor XML; the audit uses each selected suite's fresh invocation (client XML timestamp 2026-11-05T23:47:36), not the stale H2 neighbor copy in the ordering folder.
- Explicitly enabled nightly stress: two independent positive executions at seed 5301C0DE, each one test with zero failures/errors/skips. All 25 iterations and five expirations, actual disconnect observations, and equal delayed/processed counts are conditions inside the unchanged test assertions. The logs do not print separate per-iteration counters, so no printed counter values are claimed.
- Controlled comparison: the only diff reverts halfClose to close/reopen, retaining all other assertions/deadlines/seed. The stress assertion fails again with the connection-refused pattern, isolating the fixture driver as the difference. No specific swallowed bind exception is invented.
- Negative A: the exact original H0 ConnectionManager production blob (SHA-256 99faae67945695df385cb1c4bb2b629b44d9e3364fd2691057582637f4c29e33) causes the one liveness test to fail at its intended bounded assertion.
- Negative B: only acceptStateEvent is changed to accept all callbacks. The three stale-state tests execute and fail at their intended assertions. Positive and negative ordering test hashes match (d7b28f86dcd2e60a0a70b664d6f54eb8e2de19c6d135d5b7186835e840b1f7e3). Four methods are negative-proven; the fifth in-order method remains positive coverage.
- Tidy and diff check pass clean; source-pattern validation now passes; logchangeGenerate produces both existing SOLR-18623 entries; root check -x test completes successfully at H3.
- Error Prone compilation completes with its diagnostics enabled and the explicit documented javac.failOnWarnings=false condition. Full log shows only the FloatRangeFieldTest.java:328 FloatingPointLiteralPrecision warning already reproduced at H0, outside the diff. This is not a strict warning-free compile. No new Error Prone warning is present, and the public comment states the condition.

Both prior failed H2 items are resolved with exact-head execution evidence. The failed H2 receipt is preserved as history. No further patch/scope/runtime concern remains from the assessed findings. This is a focused contribution assessment with neighboring coverage and root checks, not a claim that every possible ZooKeeper schedule or external strategy listener behavior is serialized.

## Publication controls and scope

The draft was prepared locally with the required prominent AI disclosure, exact original-to-offered diff, factual proof limits, and pinned fast-forward commands. A task-specific publisher rechecks OPEN/H0, the unchanged reviewed approval, absence of conversation/inline comments, and the fork H3 immediately before its sole gh pr comment --body-file call. It saves the pre-publication state and refuses if discussion or heads differ. A failed HTTP response must be reconciled against live stored comments before any retry.

A separate read-back compared the stored comment with the local draft and refreshed PR/fork heads, with the results recorded above. The public PR head is still the contributor's original H0; this is an offer of the tested fork follow-up, not a claim that the contributor has incorporated it. Earlier add-only draft files are historical; the stored public comment is the final text. No PR body edit, contributor-branch write, JIRA post, approval or merge is part of this action. No Windows test, compiler, Gradle or formatter has run. Wait for contributor/maintainer review before further changes.
