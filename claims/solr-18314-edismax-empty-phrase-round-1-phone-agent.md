# Claim: solr-18314-edismax-empty-phrase rounds 1-2, phone agent

- Claimant host: phone agent (Linux VM). Claimed 2026-11-05 MST at Nick Shanin's direction in the main chat ("18314 assignment").
- Jobs: `gates/solr-18314-edismax-empty-phrase-round-1.md` as superseded for execution by `gates/solr-18314-edismax-empty-phrase-round-2.md`. Implementation claim: `claims/solr-18314-edismax-empty-phrase-round-1-windows.md`; report: `reports/solr-18314-edismax-empty-phrase-round-1.md`.
- Pins verified live before claiming: fork branch `solr-18314-edismax-empty-phrase` at E `6a8eb075ad7e906a547225845f8ac13420179e29` with parent exactly P `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`; diff against P is the three reported files (+65/-2). No competing execution claim or receipt exists.
- Preflight outcome: detached tidy at E was NOT clean (two formatting-only rewraps, one per changed Java file). Per the job's repair route and the owner's standing tidy rule, the repair was committed as `9b4bf9276515187b3d29a39a0923f644e1b457d2`, plain-pushed, and read back through the fork refs API. All proof runs at E2. Logs under `~/workspace/tools/gate-18314/` on this host.
- Runner: `~/workspace/tools/solr-gradle.sh` (Gradle 9.7.0, vendored JDK 21), serialized under the host build lock; root `check -x test` will run in a normal clone with the SHA explicitly fetched and `rev-parse HEAD` recorded (host Antora and clone rules), layout disclosed in the receipt.
- Receipt target: `receipts/SOLR-18314-edismax-empty-phrase-round-1.md`. Execution state follows in the receipt and an updated claim.
