# Final source and board verification

**Read-back time:** 2026-11-05 20:23:30 UTC, after all required test legs and before posting the receipt.

## Source refs

- Apache Solr PR #5110: OPEN; head P `6b5d830132b941faa85856347099ade8b190be0e`; PR base `82abb86bb5b7326fe48c117e4de69981a43e4251`; head ref `admin-ui-node-picker`.
- Fork `nick-boss-tech/solr:pr-5110-core-menu-lifecycle`: F `f02bb7bb06decce976c3ed2fdfbf5907764c2183`.
- P tree `cdd291ceed4c8414adf458cfa7aee2ad50bcfb64`, parent `73b30956ff2c0f7b16f6af3bceacf95bda8271b9`; F tree `2d2cfd804b2a8488f1940c012914202176942164`, sole parent P.
- Diff P..F: exactly `solr/webapp/web/js/angular/app.js` and `solr/webapp/src/test/org/apache/solr/webapp/AdminUiNodePickerTest.java`, 261 insertions and 11 deletions. The P checkout, normal F clone, and isolated fail-before clone all ended clean; fail-before `app.js` was restored to F before final verification. No source branch, PR #5110, PR state, or comment was changed.

## Board routing and claim

- Live board tip immediately before closeout: `2fd7e0ab416f182b89633f4477adb163831e47af`.
- Capability/routing commit: `31e38aae65bbb51d7fd9224e4cbca6dfc89862aa`; host record now lists `review, draft, records, gate, npm, selenium` and verified Chrome 155.0.8059.39 / cached compatible ChromeDriver 155.0.8059.84 / Java Selenium / JDK 21. WORKFLOW routing says any host with a verified compatible browser/driver, explicitly including the Windows host and Linux `phone-agent-2`.
- Claim commit: `aebab74fbcc486cb3064121c9a02bb238eaea96b`; exact-head claim `claims/pool-phone-agent-2-gate-SOLR-12613-pr-head-lifecycle-round-1.md` was the sole live pool claim for this gate. Its idempotency key is pinned to F. Gate file remains OPEN.
- The receipt, new failure verdict and evidence paths were absent from the live board at this read-back, so the add-only posting paths were conflict-free.

## Verification sources

GitHub API sources read without modifying upstream state:

- https://api.github.com/repos/apache/solr/pulls/5110
- https://api.github.com/repos/nick-boss-tech/solr/branches/pr-5110-core-menu-lifecycle
- https://api.github.com/repos/nick-boss-tech/solr/branches/coordination-ui

The remote P/F read-back after test execution still matched the claim pins. This note records the after-run, pre-publication state. The receipt/evidence/verdict publication is board-only and does not change either source ref.
