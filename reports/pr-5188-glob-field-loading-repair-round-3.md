# PR 5188 current API and regression boundaries, repair round 3 (vm6)

- Round-3 head H: `f00caeb5e83ac375672ae27fb43ee1546795e98b`. Preflight at H (`/workspace/gates/logs/vm6/preflight/pr5188-round-3`): tidy exit 3, one file. The patch is formatting only: a `reals` call in `testGlobRTGEmptyAndAliasFieldLoading` and the `req(...)` argument list in `testGlobPrefetchBoundaries` are reflowed.
- Repair commit F6: `555e1bf2398c08b1018ed44c056274c727bd6454` (`PR 5188: format boundary test calls`), Nick Shanin, no trailers, fast-forward pushed; remote read back as this SHA.
- Production since H: unchanged. Only `BasicFunctionalityTest.java` differs from H.
- Test blob at F6: `BasicFunctionalityTest.java` `1303fbab6b64abd4289cd0ca4a14f59831a43830`, sha256 `bbbd61eead2610a458a1b4cb1c1b5bed51d89e6f21813398d90bdea4085d8552` (blob at H `190f9f9ed0153eb04cf2c18ca2ddf674123e90df`). `TestPseudoReturnFields.java` blob `b81b328189af7f2a3c273009890c642ed946d44e`, identical to H.
- Ancestry verified before the run: current PR production `db867d80365510790bb6a04ce33105cd196cf34c` and prior `409183aa9be8c8500942eadf22d036abaf1ead5c` are both ancestors of H.
- Superseding gate: `gates/pr-5188-glob-field-loading-round-3-superseding-555e1bf.md`. The round-3 gate `gates/pr-5188-glob-field-loading-round-3.md` keeps its head H and is unedited.
