# Gate: PR 5112 target header routing follow-up, superseding round 1 at 46bc7e4

Status: OPEN, superseding `gates/pr-5112-target-header-followup-round-1.md`.

- Tags: gate, premise-run, review.
- Intended host: vm5 (claim `claims/pr-5112-target-header-followup-linux-round-2-vm5.md`).
- Fork branch: `nick-boss-tech/solr:solr-18537-submit`.
- Exact implementation head: `46bc7e428b5be519a497ecf65dcd0256b65fa84c`, one commit above `3d762be30f6b8c2805c3edcfc48446488265a806`.
- Repair: `./gradlew tidy` at `3d762be30f6b` rewrapped one Javadoc paragraph in `NodeTargetProxyFilter.java` (the sentence starting "otherwise, a value with no '.' or ':'"). The repair is the only change, committed as "SOLR-18537: wrap NodeTargetProxyFilter Javadoc for tidy".
- Original PR / negative proof base: `b4fa46f1ecb11a60cc2cfd4783492639bb4e3a85`.
- Negative-proof overlay: `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterTest.java` from the head, sha256 prefix `6af5dbcb611bd15a`. Production files stay at the PR revision.

Steps are the round-1 job's, unchanged: tidy at the exact head (clean tree required), focused suites (`NodeTargetProxyFilterTest`, `NodeTargetProxyFilterAuthTest`, seed `5112C0DE`, expect 18 tests), neighbor suites (`HttpSolrProxyTest`, `SolrRequestParserTest`, seed `5112C0DE`), `./gradlew check -x test`, and the fail-before run of the two named negative methods on the base checkout with the overlay. No proof carries forward from `3d762be30f6b`.

Result: see the receipt `receipts/PR-5112-target-header-followup.md` once written.
