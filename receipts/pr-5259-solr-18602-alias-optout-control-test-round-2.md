# Receipt: SOLR-18602 alias opt-out control test, round 2

**Verdict: GREEN at the exact fork candidate head F2** `2efbd9884eac22cc8ddd0bdad2f8715f800eebee` on `nick-boss-tech/solr:solr-18602-pr5259-alias-optout-test`.

## Pins and scope

The baseline H is `ec8e706a41a0213dd924b286b1f5ebfc476f7cd0`, the public PR #5259 head. The live PR is open and unmerged, from `sarkaramrit1993/solr:deletereplica-count-alias-18602` at H, with base `35e2be2a7de9855a0f909613065ba22243097102`. The candidate is a separate Nick fork branch; its ref was read back at F2 after push. The public PR branch, body, and comments were not changed.

F2's parent is F1 `b92dd3d7bdae76fd9fb2ac00adc26fcbcb3ba0a7`, whose parent is H. At H..F2, self-review found only the added test `deleteReplicaByCountDoesNotFollowAliasesWhenDisabled` in `solr/core/src/test/org/apache/solr/cloud/DeleteReplicaTest.java`. It creates a one-shard, three-replica collection and a simple alias, requests count-based deletion with `followAliases=false`, expects `BAD_REQUEST` naming the unresolved alias, and asserts the collection remains unchanged. F1..F2 is the Nick-authored tidy-only commit `2efbd9884eac22cc8ddd0bdad2f8715f800eebee`, which reformats the `expectThrows` call without changing behavior. Candidate `git diff --check` passed and the F2 implementation tree was clean.

The round-1 preflight at F1 exposed that tidy formatting change; no compile, test, or root-check proof ran at F1. The repair was pushed fast-forward and this exact-head superseding gate and claim were posted before any proof test.

## Proof results

The detached F2 `tidy` preflight completed with exit 0 and `tidy clean at F2` (62 actionable tasks).

The standalone F2 `./gradlew check -x test` passed in 4m 38s: 556 actionable tasks, all executed. Tests were excluded as specified by `-x test`; the checkout had a real `.git` directory for Antora.

At baseline H, the focused `DeleteReplicaTest` command with seed `18602C0DE` passed **8/8**, with zero failures, errors, or skips. Fresh XML did not contain the new control test. At candidate F2, the same focused command passed **9/9**, with zero failures, errors, or skips; fresh XML contains the new control test by its exact name.

In a separate F2 checkout, with the test file byte-identical to F2 (SHA-256 `60cd3f70e6b88371fbbae15929ec1204f9519ace1faee1a495cf04ee6f6cba12`), the only synthetic change replaced the `FOLLOW_ALIASES` conditional in `DeleteReplicaCmd.deleteReplicaBasedOnCount` with unconditional `resolveSimpleAlias(extCollectionName)`. The named control test ran alone and failed as required: **1 test, 1 failure, 0 errors, 0 skips**; exact assertion: `junit.framework.AssertionFailedError: Expected exception SolrException but no exception was thrown`. The failure was in the expected assertion, not compile/startup/timeout. The synthetic mutation was never committed or pushed; after preserving its diff, hash, log, XML, and monitor, the mutation checkout was discarded as the gate authorizes.

## Host and evidence

All Gradle legs ran serially through `pool/bin/sweep-run.sh` under `/workspace/tools/.gradle-phone-agent-2.lock`, with `NODE_ENV` unset and a live 3-second `inode-watch-run.py` guard. There were 263 recorded resource samples and no stop marker. The overall minimum was 35.3658% free inodes (660,667/1,868,096) and 43,741,593,600 free disk bytes—comfortably above the stop floors of <=15% free inodes or <2 GiB free disk. No gate legs overlapped.

Full commands, raw logs, fresh XML, resource monitors, the round-1 and round-2 preflight artifacts, the complete H..F2 diff, the tidy repair diff, and the synthetic mutation evidence are preserved locally under `/workspace/gates/logs/phone-agent-2/pr-5259-solr-18602-alias-optout-control-test-round-2/` and bundled at `receipts/pr-5259-solr-18602-alias-optout-control-test-round-2-evidence/round2-evidence.tar.gz`. Archive SHA-256: `0bc7c81eca10d36e235fd32296e0d12037399be84771d5b6f9001c146c63a408`.

## Caveats

This is GREEN for the Nick fork candidate F2. It is not a statement that the currently open public PR contains F2: that PR still points to the separate contributor branch at H, and was intentionally left untouched. The test is coverage-only evidence for the `followAliases=false` control; it does not claim a production false-branch defect, does not assert the separate SOLR-18589/PR #5255 alias-lock work, and is not a full-suite test run.

## PR description draft (not posted)

Add a regression control for count-based replica deletion through a simple alias when `followAliases=false`. The test expects `BAD_REQUEST` naming the unresolved alias and verifies the underlying one-shard, three-replica collection remains unchanged. On the Nick fork candidate F2, the focused class passes 9/9; the PR-parent baseline passes 8/8, and a synthetic unconditional-alias-resolution mutation fails at the expected exception assertion. The root `check -x test` passes at F2. This is coverage-only; production behavior is unchanged.

Draft only: public PR #5259 remains on the contributor's separate branch at H; this text was not posted.