# PR 5188 full snippet and field-loading proof, repair round 2 (vm6)

- Round-2 head H: `cb8d3dcf0172515e11b92a9d1c7d9aa186f1364c`. Preflight at H (`/workspace/gates/logs/vm6/preflight/pr5188-round-2`): tidy exit 3, 1 file. The tidy patch is formatting-only: the `params(...)` argument list in `testGlobHighlightSnippets`/`testGlobHighlightPrefetch` is reflowed one argument per line.
- Repair commit F5: `409183aa9be8c8500942eadf22d036abaf1ead5c` (`PR 5188: format snippet fixture`), Nick Shanin, no trailers, fast-forward pushed; remote read back as this SHA.
- Production since 51e92b6: unchanged (diff count 0). Only `BasicFunctionalityTest.java` differs from H, by formatting. `TestPseudoReturnFields.java` is identical to H.
- Test blob at F5: `BasicFunctionalityTest.java` `1a320e232f3d96510997476223d69949aadcce78`, sha256 `b2be8289da46dd7d3d16273a7512872162a8dcdddec52976a86143a38e63592e`. Blob at H was `300869075fb8996e7a4445279ae2e7c579678300`.
- The superseding gate for F5: `gates/pr-5188-glob-field-loading-round-2-superseding-409183a.md`. The round-2 gate `gates/pr-5188-glob-field-loading-round-2.md` keeps its H head and is not edited.
