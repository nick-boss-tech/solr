# Review round 1, unit 22: SOLR-3684 (branch solr-3684-submit), vm3

## Pass 1 (vm3, 2026-10-21T08:00:49Z)

- Head reviewed: `663b8ce754b69fd81dcd1b5c1abe7e94ca8e6faa`. Base `14c7aac0d1`. Diff: 4 files, 54 insertions, 1 deletion: changelog SOLR-3684.yml, `test-framework` `JettyConfig.java` (new `maxThreads` field, `withMaxThreads`), `JettySolrRunner.java` (honours it when set; `getConfiguredMaxThreads` for tests), `TestJettySolrRunner.java` (two new tests).
- Ticket SOLR-3684 (Open, Critical): frequent full GC under pressure in indexing; the embedded Jetty thread pool was fixed at 10000 threads with no way to configure it. The branch adds a knob without changing the default.
- Surrounding code check: `JettyConfig.builder(JettyConfig)` returns `other.builder.clone()`, and `Builder.clone()` uses `super.clone()`, so `maxThreads` survives the copy. The default `THREAD_POOL_MAX_THREADS` is unchanged on the `null` path.
- Test expectations: `testJettyThreadPoolDefaultIsUnchanged` pins 10000 for a default config; `testJettyThreadPoolCanBeConfigured` pins 123 for a configured one. They check the configured value only. They do not show the GC effect the ticket describes, which is a load property, not a unit-test property.
- Proof: inconclusive by construction. The new tests call `withMaxThreads`, which does not exist on base, so no base run can discriminate (receipt). Recorded plainly; the default-value test is a pin, not a fail-before.
- Finding, minor: `getConfiguredMaxThreads` is package-private and reads the live pool, which is the only way to observe the value. Acceptable for a test helper; no repair.
- Repairs in pass 1: none.
- Regate: `gates/SOLR-3684-review-pass-1.md`, GREEN at `663b8ce754b69fd81dcd1b5c1abe7e94ca8e6faa`. Logs `/workspace/gates/logs/vm3/solr-3684-review-pass-1/`. Changelog rc 0; tidy rc 0, empty status; Error Prone compile rc 0 (compileJava and compileTestJava executed, no skip warning); `TestJettySolrRunner`: 1 class, 4 tests (the 2 new plus 2 existing), 0 failures, 0 errors (fresh XML); `:solr:test-framework:check -x test` rc 0.

Pass 1 result: GREEN at `663b8ce754b69fd81dcd1b5c1abe7e94ca8e6faa`.
