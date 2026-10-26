# PR 5235: gate reconciliation and raw-body fixture repair, round 2

Status: IMPLEMENTED, PROOF PENDING (test fixture only)

## Existing evidence

Read vm2's receipt and outcome report from board snapshot `7c153810307077fef3ec87a28f55c6128a6619f5`:

- `receipts/PR-5235-compatibility-premise-round-1.md`
- `reports/pr-5235-compatibility-premise-round-1-outcome.md`
- `gates/pr-5235-compatibility-premise-superseding-a7215a5.md`

The completed experiment used test head `a7215a5bf1c79b3b7dbcbf9289aac2a43e31fbcd`, production PR head `73fa87f1f41b45862c165a9571176c89e2ef4def`, and base `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`. F1 and F2 are confirmed, with F2 confirmed by reflection rather than a precompiled linkage probe. F3 and F4 are confirmed through both Jetty request writers. Their JDK-client halves remain BLOCKED BY HARNESS. The seven passing-base/failing-PR methods span all four finding IDs; the outcome report's phrase "three of the four" should not be read as leaving an entire finding unconfirmed.

Receipt totals: base 10 pass and 4 fail (the four JDK cases), PR 3 pass and 11 fail, 14 methods and zero skips per leg. The three positive controls pass on both. The manually rerun `:solr:core:check -x test` passed; the runner's malformed invocation remains INVALID. Neither result establishes that the whole PR is fixed or that a repository-wide check passed. This Windows reconciliation read the receipt, not the VM-local raw logs/XML.

My first embedded fixture used nonnumeric IDs incompatible with the standard schema's numeric copy fields. Vm2 repaired that defect in `a7215a5` with numeric IDs. That change and its Nick Shanin authorship/no-trailer commit are preserved.

## Why the JDK capture is suspect

At the unchanged base, HttpJdkSolrClient.java lines 305-309 chooses `application/x-www-form-urlencoded` unless a ContentWriter supplies a type. Its legacy stream branch still creates a body publisher from the stream at lines 326-333. DebugServlet calls getParameterMap at ServletFixtures.java lines 242-244 and 270-273, before reading the input stream at line 277. Servlet form parsing can consume bytes before this capture. Thus "empty captured body" must not be strengthened to "the JDK client sent no body" from this gate alone.

This is a source-based explanation to test, not a measured cause. The old test also asserted text/plain for every legacy request; that would fail on the base JDK content type even after fixing raw capture, independently of the body regression being tested.

## Pushed repair

- Fork branch: `nick-boss-tech/solr:solr-18351-submit`.
- New pushed and remote-verified test head: `63e517018db57c59a9c4eab5140152f898bfee1d`.
- Parent: `a7215a5bf1c79b3b7dbcbf9289aac2a43e31fbcd`.
- Only changed path: `solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/TestLegacyContentStreamCompatibility.java`.
- Git blob: `f3827885d6039e99259221796d2ee0d3d0ff3aaf`.
- Claim: `claims/pr-5235-jdk-raw-capture-windows-round-2.md`.
- Follow-up gate: `gates/pr-5235-jdk-raw-capture-round-2.md`.

The suite now owns a RawBodyServlet that reads the request stream without invoking parameter APIs. It captures method, content type, and raw bytes in an AtomicReference snapshot. A separate HEAD response preserves the JDK client's capability probe without overwriting POST observations. The servlet returns the same valid XML response shape.

All original eight body assertions are retained, including the custom-writer marker and hook assertion. Legacy content types are recorded as diagnostics, not used to reject the base for an unrelated behavior. Explicit ContentWriter controls still assert the specified type. Two added controls send explicit form-encoded bodies, one through each client, to prove that the fixture records those bytes without form parsing. The HTTP suite now has 12 methods: eight regression scenarios and four controls. It prints fixed test markers and content types into the test output for comparison; no user input or credentials are logged.

The embedded and command suites are unchanged by this repair. Their round-1 evidence remains pinned to its recorded inputs. No production code was changed and the public PR was not moved.

## Required interpretation

The new Linux gate compares identical HTTP test bytes on unchanged base and the PR production tree. Prediction: base 12 pass, PR eight body-regression failures and four passing controls, with no skips. These are hypotheses, not results. If base JDK still fails, retain the unresolved verdict and investigate the raw observations. Do not alter expected markers to force agreement.

Windows checks: reviewed the one-file diff, clean worktree after commit, and `git diff --check` passed. No Java compilation, Gradle, formatting task, or queue was run. The repair is uncompiled and unrun. No public comment, review, PR/body edit, JIRA write, or alteration of vm2's receipt/report was made.
