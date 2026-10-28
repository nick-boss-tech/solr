# PR 5254: validated unit-test offer posted and reviewed, October 28, 2026

Status: POSTED AND VERIFIED. This records a conversation comment, not a GitHub approval review or a full snapshot-join gate result.

- Comment: https://github.com/apache/solr/pull/5254#issuecomment-6474453598
- Author: nick-boss-tech.
- Created: 2026-10-28 16:21:08 UTC.
- Read-back verified: 2026-10-28 16:24:05 UTC.
- Authorization: Nick explicitly approved the revised draft with "It looks good, you can post it and review." The default clean-gate posting exception was not used: the overall Docker/BATS job remains PARTIAL.
- Pre-post board snapshot: 3c4b250c8563a70e5a2f0cf6003cf38137bd92ef. Read the live comment formula and vm6 receipt there; refreshed the PR and inline discussion, verified both fork heads, and checked that no equivalent Nick comment existed before posting.

## Scope and evidence

The offered branch is solr-18432-gradle-only at 842b1fb426860764d442c87738252b70eccf2c93. It changes only TestSolrVersion.java relative to the contributor head d59169c9cc96dd60c976079d18ef4da56024bb26, which is its ancestor. The pinned fetch and merge --ff-only instructions offer the validated unit tests without including the separate BATS work or rewriting history.

Proof source: receipts/PR-5254-snapshot-join-round-2-gradle-vm6.md, explicitly PARTIAL for the full round-2 job. At the offered head it records clean tidy, TestSolrVersion 12/12, NodeSystemInfoProviderTest 2/2, root check -x test success, and mutations A/B/C/D failing at their named assertions with executed/failure counts 1/1, 2/2, 2/2 and 1/1. Environment: Linux aarch64, JDK 21.0.12.1+1, Gradle 9.7.0; seed 18432C0DE. No raw XML was independently rerun on Windows; these execution results come from the execution host's receipt.

The comment states that the production changes look good, offers the four additional unit methods, puts Linux validation inside details/summary, and identifies the separate BATS commit b849945c88bf894d273455882b25d786a8c1e6d1 on solr-18432-submit as untested because Docker was unavailable. It does not claim direct startup proof. That branch still needs the recorded formatting repair and a capable Docker/BATS host for the full matched pair; no such repair or proof is claimed by this publication.

## Stored text and rendering review

Posted once using gh.ps1 pr comment --body-file with the approved draft's absolute path. A separate GET of the stored comment confirms exact body equality after CRLF/LF normalization and author nick-boss-tech. GitHub's rendered HTML contains one details wrapper, the correct 14-test summary, the pinned test-only diff, copyable Git commands and the separate BATS commit link. GitHub automatically abbreviates and links the plain hash in the summary; the underlying target retains the full offered SHA.

The final live PR refresh shows OPEN at unchanged head d59169c9cc96dd60c976079d18ef4da56024bb26 and exactly one conversation comment by Nick. The existing Jan Hoydahl review/discussion and David's response were read; no review conversation was resolved, and no approval review, merge, PR body edit or JIRA write was performed.

Local read-back artifacts are under research/branch-reviews/2026-10-27-pr-5254/evidence/PR-5254/: gradle-offer-posted.json, gradle-offer-posted.md, gradle-offer-posted.html, gradle-offer-pr-readback.json and gradle-offer-verification.json. The read-only verifier is verify-gradle-offer.ps1 in the review directory. The original draft remains the approved posted text.
