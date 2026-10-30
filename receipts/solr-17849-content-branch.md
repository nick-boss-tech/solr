# Receipt: SOLR-17849 content branch built and gated (phone agent, 2026-10-30)

At Nick's direction (option 2: keep PR #5257 header-only per Christos's scope call on SOLR-17849, 2026-10-27, and build the content as the separate PR he asked for), the screen work was ported from solr-17849-index-query-screen onto the header PR's head.

Branch: solr-17849-index-query-content @ dc8c1c75c6, stacked on solr-17849-header-submit @ 90d4efb848 (PR #5257). One commit: "SOLR-17849: host the Index and Query screen on the Configsets Index / Query tab".

Scope of the port: the hosting commit 0099c758d3 (IndexQueryViewModel, Configsets component and route view model wiring, ConfigsetsContent hosting, preview, one string), with IndexQueryContent rewritten as a shell on the header's current dropdown-state API (HeaderDropdownState), plus the layout fix from b91f75ed93 (no vertical scroll on the content column). One adaptation: the preview's fake IndexQueryRepository still carried the old interface's execute override; removed, since the current interface has only listCollections and listRequestHandlers.

Deliberately excluded: the SOLR-17850 SearchHandler form and its execution flow (commit 887778ce5d, the form block inside the old IndexQueryContent, SearchRequestForm.toParameters and its IndexQueryContentTest). The form is SOLR-17850's task and plugs in below the header when it lands.

Gate at dc8c1c75c6 on the phone agent: :shared:spotlessCheck passes; :shared:jvmTest passes, 59 tests, 0 failures, 2 pre-existing skips (UserAuthenticationContentTest, StartContentTest); IndexQueryStateHolderTest 3/3, HttpIndexQueryRepositoryTest 3/3, IndexQueryHeaderTest 6/6. Logs: ~/workspace/tools/gate-17849-content/. The wasm browser leg was not run (this VM cannot fetch the npm dependencies; see receipts/pr-5258-wasm-attempt-2026-10-30.md).

State: branch pushed to the fork. No PR opened and no comment posted; both wait for Nick's go. The PR, when opened, is a draft against main whose diff includes the header commits until #5257 merges; its body states the stacking plainly.
