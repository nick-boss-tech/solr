# SOLR-18609 review fixes: Windows implementation claim

Status: LIVE
Started: 2026-11-05T20:42:40Z.
Host: windows. Tags: implementation, review, records.
Authority: Nick's direct request in this chat to patch the reviewed branch and hand off to the gate.
Board snapshot reviewed: `3a926cc3bd7c7dfb72116a51863d056542837a0a`.

Slice: follow-up to PR 5299 / SOLR-18609 on fork branch `solr-bats-assertion-hardening-submit`, verified parent `99d4b8e68b446b1e750159a2ccb6549495154d60`. Add an ordinary commit preserving ancestry, then plain-push to this same fork branch and verify both fork ref and PR head.

The old phone-agent-2 gate claim is stale per `claims/phone-agent-2-stale-claims-20261104.md`. This is a new implementation slice, not a Linux gate claim. No gate is run or resumed on Windows, and the old gate reports remain immutable.

Planned scope: `solr/packaging/test/bats_helper.bash`, `solr/packaging/test/test_bats_assertions.bats`, `solr/packaging/test/test_ssl.bats`, and `.github/workflows/bin-solr-test.yml`. Recognize the specific direct and wrapped JDK hostname-mismatch diagnostics, cover discrimination with fixtures, and provision a separate candidate-image rolling-upgrade CI leg that rejects skipped execution. No production changes.

Decision ladder: rung 1, producer source plus existing workflow/task wiring settle these test corrections. The OpenJDK wrapper message follows identity comparison; it is not proof of an unconditional unsupported-policy failure. No production hostname-policy change is authorized or required by this implementation slice.

Deliverables: verified pushed commit; add-only `reports/pr-5299-review-fixes-round-1-windows.md`; new exact-head `gates/pr-5299-review-fixes-round-1.md` with normal matrix, portability discrimination, inherited outstanding controls, candidate-image Docker execution and formatting/root checks; local review record updated with implementation and proof-pending status.

Windows performs source inspection and diff checks only. Status remains Implemented, Proof Pending until the Linux gate issues a matching successful receipt.
