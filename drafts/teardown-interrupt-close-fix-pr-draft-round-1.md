# DRAFT, NOT POSTED: PR description for the interrupted-close fix

Owner note (not part of the description): not yet reviewed by Nick Shanin. Branch `nick-boss-tech/solr:solr-teardown-interrupt-close-fix` at `5ec461c4012036faf3fd14363202e82c4686fb07`, on apache/solr main `d298cd0b42602247bee7bf9e2bcf302e725f6690`. Opening a PR needs Nick's go. Changelog waits for the PR number.

---

🤖 *AI-generated text, not yet reviewed by Nick Shanin* 🤖

**A caller's interrupt makes `HttpJettySolrClient.close()` fail before Jetty finishes stopping the client.**

When the calling thread's interrupt flag is already set, Jetty's selector stop throws `InterruptedException` at `ManagedSelector.doStop`. `close()` then throws `RuntimeException: Exception on closing client`. The client stays in the object tracker, and the Jetty HttpClient stays in a failed state.

**This change clears the flag only for the Jetty stop.** `close()` captures the flag after draining async requests, runs the normal stop and cleanup, and restores the flag in `finally`, after any executor shutdown it owns. The tracker release, the borrowed-client path, and the caller-executor path are unchanged. Source: `HttpJettySolrClient.java` lines 347 to 377 at the head.

**Proof.** On `5ec461c4012036faf3fd14363202e82c4686fb07`, the full `solrj-jetty` module suite ran once (seed FFF5A3C3AAC69084, Linux aarch64, JDK 21.0.12.1): 76 tests, 0 failures, 0 errors, 0 skipped across 7 classes. Three new tests cover the fix:
- Owned client, default executor: close returns, the interrupt is preserved, the HttpClient is stopped, and the client is released from the tracker. Before the change this test failed with `Exception on closing client`.
- Caller-owned executor: close returns, the interrupt is preserved, the HttpClient is stopped, and the caller's pool is still usable. Before the change this test failed the same way.
- Borrowed client: close does not stop the parent HttpClient, and the interrupt is preserved. This passed before the change too, so it guards against regression rather than proving the fix.

Error Prone and the module `check` task passed with `-Pvalidation.errorprone=true`.

**The choice.** The fix restores the caller's interrupt in `finally` rather than leaving the flag cleared. A caller that expects a cleared flag after close would see a change. We recommend restoring it, because the flag is the caller's and the fix should not change it. Do you agree?

**Limits.** An interrupt that arrives during the stop is not handled and still aborts cleanup. Unrelated failures during stop can still skip later cleanup. The change was tested on Linux aarch64 with JDK 21 only, not on a full repository build.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
