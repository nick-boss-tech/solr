# SOLR-18613 standalone selection repair, Windows round 1

Status: Implemented, Proof Pending

- Authority: Nick's direct November 5 request to patch the reviewed branch and hand off the gate.
- Claim: claims/SOLR-18613-selection-windows-round-1.md, board commit 337d9cb6418def135dfefc9f09080221a578409c. Stored claim blob 626af84e9073a4b19a0a8fec0fc896761801dd55 independently matches the local file; claim commit was verified reachable from the live board.
- PR: https://github.com/apache/solr/pull/5297, SOLR-18613; open and draft at the start of this work, no review discussion.
- Fork branch: nick-boss-tech/solr:solr-lb-attempt-limits-pr1-submit.
- Parent P: 2399d0312fa320563773ea87884ee4f534594974.
- Pushed repair head H: e88dc392bcab67ac388a45340d96ac52c616f35b.
- Complete repair tree: 40843a7f4f083f5ee036c0cbbf60b812731c072a.
- Local isolated worktree: C:/Users/shaninna/dev/Solr-issues/wt/PR-5297-selection-repair, local branch solr-18613-selection-repair. This is a minimal source/test checkout at P; the Linux runner must use a full clone. The older checkout of the rebased submit branch was preserved.
- Gate: gates/SOLR-18613-selection-round-1.md, OPEN for a capable Linux host.
- Receipt target: receipts/SOLR-18613-selection-round-1.md; no execution or passing result is claimed here.

## Finding and repair

The detailed source review identified a P2 regression: the standalone loop's new Math.min(liveSnapshotSize, configuredLimit) bound assumes selections cover distinct endpoints. A shared round-robin counter violates that assumption when requests interleave. With [A, B], failing A, healthy B and primary limit 3, the primary dispatches A, another request dispatches B, then the primary dispatches A again and fails before reaching B despite an unused attempt.

The repair uses the existing execution-local justFailed map to recognize a repeated selection. It removes failed endpoints from that execution's retained candidate array and invokes the existing pickServer hook again on the remaining candidates. Only a delegate dispatch spends an attempt. The no-contention selection order remains unchanged, and custom selectors still choose retry destinations from the eligible candidates. The original live/zombie shared counter, no-limit path, deadline checks, and Req/async retry-eligibility rules remain in place.

Exactly two files change relative to P:

- solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java
- solr/solrj/src/test/org/apache/solr/client/solrj/impl/LBSolrClientAttemptLimitTest.java

The existing changelog already covers avoiding repeated exhausted-live-endpoint dispatches and retains Nick Shanin's ICLA name. No additional fragment is needed for this correction within that scope.

## Regression coverage authored

- testStandaloneInterleavedRequestDoesNotHideHealthyEndpoint uses latches to block the first primary A dispatch, complete a secondary B dispatch that advances the real selector counter, then release A to fail. It records primary attempts separately, asserts primary success and [A, B] without a repeated primary A, and exercises limits 3, 2 and null. The executor is released/shut down in finally; no sleeps or network services are used.
- testStandaloneRetryUsesCustomPickerWithUntriedEndpoints uses an overridable selector that always chooses the first supplied candidate. It requires primary dispatches [A, B] and requires the healthy B choice to have passed through that hook, preserving the extension point after a failed candidate is excluded.

The existing RecordingClient is extended with an endpoint-list constructor and a default-empty beforeResponse hook for controlled interleaving. Existing tests use the same endpoints and response behavior. No new fixture, configset, external dependency or production API is added.

## Verification and handoff limits

Windows source review and git diff --check are the local checks. No Gradle, compiler, tidy task or test ran on this host. Neither new test has runtime proof yet. The source finding is addressed by the patch, but its regression and compatibility proof remain pending.

The Linux job requires fresh execution of 49 focused/neighboring tests, clean tidy, full root check -x test, a clean final tree and independent fork/PR head checks at H. It also requires both new methods to fail at intended runtime behavior on P plus only H's test file. The first interleaving case uses limit 3, so that negative leg specifically covers the reviewed regression. The prior four-method original-production proof can be reconciled as historical evidence with its limits; it does not substitute for the new P-versus-H pair.

The ordinary fork push moves the head of the existing public draft PR to H; there is no history rewrite. Remote fork and PR heads are independently read back and must equal H. Older receipts at c2d2c625 and b49413e do not validate this pushed head. The gate handoff is posted and verified on coordination-ui, but remains unclaimed until a Linux host adds its execution claim.

No upstream review comment, live PR body edit, Jira write or draft-to-ready action was made. The first review's no-findings verdict is superseded by the detailed P2 review and this Implemented, Proof Pending follow-up.
