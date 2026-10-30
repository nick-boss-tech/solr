# Addendum: wasm browser leg attempt for PR 5258 at d02ccdcdd8 (phone agent, 2026-10-30)

The `:shared:wasmJsBrowserTest` leg does not run on this VM today. Recorded so the next host does not repeat the diagnosis.

What was fixed on the way: Gradle's own downloads failed first with a PKIX error on dl.google.com (the egress proxy intercepts TLS with its own CA, which the vendored JDK does not trust). A truststore of the JDK cacerts plus /usr/local/share/ca-certificates/hatch-egress-ca.crt, passed via JAVA_TOOL_OPTIONS, fixed all Gradle resolution; the build then reached :kotlinWasmNpmInstall.

The remaining blocker: yarn 1.22.22 (the Kotlin plugin's bundled yarn, on its bundled Node 26) cannot fetch npm tarballs through this VM's egress. Verbose runs show GETs to registry.yarnpkg.com tarballs aborting at the TLS socket (Error: aborted, TLSSocket.socketCloseListener) after the CONNECT tunnel opens. Verified identical through the local relay (127.0.0.1:3129) and through the egress proxy directly, at network concurrency 1 and at the default, with proxy set by env, by ~/.yarnrc, and by CLI flags. The same URLs return 200 via curl and via Node's fetch, so the network and the registry are fine; it is yarn 1's tunneling fetcher against this intercepting proxy. This matches the UI playbook's note that the vm1 probe also died in kotlinWasmNpmInstall.

State of proof for PR #5258 at d02ccdcdd8: spotless and the full JVM suite green (see receipts/pr-5258-17913-removal.md); the wasm leg last passed at bb7a4f32eb, before the card removal. A host with unproxied egress or a working yarn route (vm6 ran UI gates; the bb7a4f32eb run happened somewhere with a browser) can close the leg with: CHROME_BIN set, -p ui :shared:wasmJsBrowserTest.
