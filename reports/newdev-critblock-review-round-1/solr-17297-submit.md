# Review round 1, unit 18: SOLR-17297 (branch solr-17297-submit), vm3

## Pass 1 (vm3, 2026-10-21T04:16:14Z)

- Head reviewed: `c0ab38fc0a8b57953f8b8df03f54085fb37e138d`. Base `14c7aac0d1`. Diff: `NodeConfig.java` (two lines reordered: `initModules()` now runs before `setupSharedLib()`), `TestCoreContainer.java` (+157), one changelog entry. Commits: 4f10ab1d60, 73ae0d7eda, c0ab38fc0a.
- Ticket SOLR-17297 (Open, Critical): with a plugin jar in `lib/` and a module enabled, startup can raise `ClassNotFoundException`. The reporter's mechanism: `setupSharedLib` loads a codec from the lib jar through a `FactoryURLClassLoader`; `initModules` then calls `addURLsToClassLoader`, which closes that loader; the codec's `<init>` references a class in the jar and fails.
- Surrounding code check (`SolrResourceLoader.addToClassLoader`, read at the head): every call builds a new loader over the old URLs and closes the old one (`IOUtils.closeWhileHandlingException(oldLoader)`). The reorder therefore does not remove the close; it moves it. With modules first, `setupSharedLib` is now the step that closes the module-stage loader.
- Finding, major (owner item, not repaired): the receipt's round-35 review finding is the same mechanism in the other direction. A module SPI provider loaded in the module stage is orphaned when the shared-lib step closes that loader. The receipt records it as CONFIRMED BY RUN with a probe test at this head (`g17297r35-probe.log`); the probe was not committed. The reorder fixes the ordering the ticket reports for lib plugins, but the close that caused it remains, and it now affects module classes. A fix changes the loader design (one combined add, or no close of a loader still in use), which is outside the gate-repair scope and needs the owner's choice.
- Test: `TestCoreContainer` additions cover a lazily loaded shared-lib class with modules enabled. They pass, but they do not cover the module-stage orphaning the probe shows, so a green gate does not show the residual is fixed.
- Repairs in pass 1: none.
- Regate: `gates/SOLR-17297-review-pass-1.md`, GREEN at `c0ab38fc0a8b57953f8b8df03f54085fb37e138d`. Logs `/workspace/gates/logs/vm3/solr-17297-review-pass-1/`. Changelog rc 0; tidy rc 0, empty status; Error Prone compile rc 0 (compileJava and compileTestJava executed, no skip warning); `TestCoreContainer`: 1 class, 26 tests, 0 failures, 0 errors, 3 skipped (fresh XML, `count-core.txt`); `:solr:core:check -x test` rc 0.

Pass 1 result: GREEN at `c0ab38fc0a8b57953f8b8df03f54085fb37e138d`, with the owner item open.
