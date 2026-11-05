# Core-menu response lifecycle implementation, round 1

Status: Implemented, Proof Pending.

- Fork `pr-5110-followup-selection-lifecycle`, live head `e59bc25bf6dda7d3ca05ec6aad727d6ed7c51bd4`, separately read back after plain push.
- UI baseline `b45bae5da3a151c7b439ff4e42009c383b538fae` integrates main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`, picker `73b30956ff2c0f7b16f6af3bceacf95bda8271b9`, and already-gated Selenium/property prerequisites `35b5c45ec6ddb8ec8d54b89bbf9ca97368d2a8ca` through conflict-free ordinary merges. Initial main-plus-picker baseline was `6ec2e527ad716bde74404b2037bb1a1e6c52f79c`.
- New implementation commit `eb1548c7ac9491b5bb409c81ac8ba31508175b40`, followed by the ordinary prerequisite integration. No history rewrite. Delta against UI baseline is exactly app.js and AdminUiNodePickerTest.java.
- Gate `gates/solr-12613-adjacent-selection-lifecycle-round-1.md`; receipt `receipts/PR-5110-adjacent-selection-lifecycle-round-1.md`.

MainController now discards core-status callbacks from old requests or an old selected node. Two browser regressions release older results after newer results for both node-switch and same-node refresh sequences. Generated network requests still run; their controller callbacks are delivered with controlled, distinguishable payloads. Assertions cover parent-scope inventory and rendered core options, and the stub is restored in finally. Reuses the existing picker browser framework and cluster; no new fixture framework.

No runtime outcome is claimed. The Linux gate requires intended new_core/old_core failures with baseline production, all candidate/neighbor Selenium tests, tidy and root check. Windows reviewed the diff and diff --check only. The existing SOLR-18619 receipt covers the inherited prerequisites, not this fix. No public action, new issue/changelog identity, PR or comment was created. C4 request targeting remains active; C1 was excluded as already implemented elsewhere.
