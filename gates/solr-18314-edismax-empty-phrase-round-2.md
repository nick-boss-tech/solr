# Gate: SOLR-18314 eDisMax empty phrase clauses, round 2 (superseding, tidy repair head)

Status: OPEN. Posted by the executing gate host under the repair route of `gates/solr-18314-edismax-empty-phrase-round-1.md`.

- Supersedes round 1 for execution: the round 1 detached tidy preflight at E `6a8eb075ad7e906a547225845f8ac13420179e29` was not clean. Tidy made two formatting-only changes: it joined the multi-line guard's `return;` onto the condition line in `ExtendedDismaxQParser.java`, and wrapped one `assertThat` line in `TestExtendedDismaxParser.java`. No logic changed in either file.
- Repair commit `9b4bf9276515187b3d29a39a0923f644e1b457d2` ("SOLR-18314: Tidy eDisMax empty phrase changes", Nick Shanin identity, no trailers), an ordinary child of E, plain-pushed to `nick-boss-tech/solr:solr-18314-edismax-empty-phrase` and read back through the fork refs API at the same SHA. Parent P `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a` (draft PR 5294 head) is unchanged.
- New exact head for all proof: E2 `9b4bf9276515187b3d29a39a0923f644e1b457d2`. Every leg of the round 1 job runs at E2 unchanged, including the negative control that restores `ExtendedDismaxQParser.java` from P and the one-token phrase-generation mutation. Seed `18314E` throughout.
- Receipt target unchanged: `receipts/SOLR-18314-edismax-empty-phrase-round-1.md`, which will state E, the repair, and E2 plainly.
