# PR 5188 glob field loading, repair round 1 (vm6)

- Candidate head: `b71a1687bf98cf7b4b3edf635bfb89f25e4af8e2`, preflight at `/workspace/gates/logs/vm6/preflight/pr5188-glob-field-loading-round-1`, exit 3 (tidy changed 3 files).
- Repair commit: `b428596720d0ee1119167d04323681252e86136d` (`PR 5188: format glob field loading changes`), author Nick Shanin `<nick.boss.us@gmail.com>`, no trailers, pushed fast-forward to `nick-boss-tech/solr:pr-5188-glob-field-loading-followup`. Remote read back as this SHA.
- Scope: formatting only, from `tidy.patch`. It rewraps one javadoc in `SolrDocumentFetcher.java` and reflows argument lists in `BasicFunctionalityTest.java` and `TestPseudoReturnFields.java`. No production logic or assertion changed. Diffstat 3 files, 17 insertions, 11 deletions.
- Final head tree `1f5ca309fb`. Blobs: `SolrDocumentFetcher.java` `828524d37d` (candidate `876bec7519`), `BasicFunctionalityTest.java` `ef9903c5d7` (candidate `69bd41b`→`ef9903c`), `TestPseudoReturnFields.java` `b7569c8f70` (candidate `fce1efa`→`b7569c8`).
- Pre-fix overlay for the superseding gate: original production `e07455a0f953e702da27f677c51ea894b7bafc3a` (tree `e7be378018`) with the final test blobs `BasicFunctionalityTest.java` `ef9903c5d7` and `TestPseudoReturnFields.java` `b7569c8f70`. The three production files are the same blobs as `e07455a`.
- Board snapshot: `9a7d6c8afc` (claim commit).
- Superseding gate: `gates/pr-5188-glob-field-loading-round-1-superseding-b428596.md`. Original gate and Windows claim are unchanged.

## Why this is a repair, not a new behavior

The gate's Repairs section authorizes formatting changes within the five outbound files on the same branch. The original candidate stays the input to the earlier preflight. The superseding gate pins `b428596` for every post-fix leg and the overlay above for the pre-fix legs.
