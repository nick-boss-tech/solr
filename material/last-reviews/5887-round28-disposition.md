# SOLR-5887 pipeline disposition (round 28 fresh arrivals)

**Outcome:** PR-ready, gated, pushed, and GH-corroborated. Shipped head **c4c57ef7bcbde765807bf02f9e3a6db605868b7e** on solr-5887-submit (two commits on the branch's own base b6b2b8f10e9827e3b86e44649fb7966ce646c185: a97ca909e47 fix+test with a cleaned message, c4c57ef7bcb changelog; handoff doc dropped; shipped tree differs from the received tip only by that removal, verified by diff). Pushed with --force-with-lease pinned to the received tip edbcdfbd5b5b after re-reading it in the same step; ls-remote verified. Commit identity Nick Shanin, no Claude markers.

**The branch:** SOLR-5887 (2014 Bug, still Open): a document validation error such as `ERROR: [doc=...] unknown field 'location'` does not say which core rejected the document, so on a node with many cores the core with the schema problem cannot be identified from the error. The fix routes all three document-conversion paths in `AddUpdateCommand` (single document, document stream, in-place update document) through a private `toLuceneDocument` wrapper around `DocumentBuilder.toDocument`; a `SolrException` with code 400 is rethrown as a 400 with `core <name>: ` prepended and the original as the cause, but only when the request carries a core. `DocumentBuilder` itself is unchanged. Mechanism verified in code before running anything: `DocumentBuilder.toDocument` builds the message from the doc id only and holds no core; the base 2-arg overload delegates with `(forInPlaceUpdate=false, ignoreNestedDocs=true)`, exactly the arguments the branch's stream path passes to the wrapper, so no conversion behavior changes.

**Premise: GROUNDED, discriminating.** Gate step 3 (seed 5887C0FFEE5887, production AddUpdateCommand.java reverted to base b6b2b8f10e9, branch tests kept; JUnit XML preserved at ~/workspace/tools/g5887-premise-TEST-DocumentBuilderTest.xml): DocumentBuilderTest 17 tests, exactly 1 failure, 0 errors: `testAddUpdateCommandErrorNamesCore`, whose core-name assertion fails on the base message `ERROR: [doc=123] unknown field 'unknown'`. The other 16 tests pass on base, including the pre-existing unknown-field test that pins the 400 code. At the shipped head the class passes 17/17 from fresh JUnit XML.

**Gate green on the exact shipped tree** (log g5887-gate.log, per-branch blocking-flock runner at ~/workspace/tools/g5887-gate-runner.sh): changelog YAML rc=0; :solr:core:tidy rc=0 with zero changes; Error Prone compile rc=0; premise as above with the production file restored clean; focused DocumentBuilderTest 17/17; :solr:core:check -x test rc=0. No defects were found in the received production code, test, or changelog; the only changes to the received commits are packaging (handoff doc dropped; the "Hypothetical, unrun regression test" line replaced with a factual commit body). No VM replacement touched this branch.

**GH corroboration:** run **37635905367** (ci/5887-docerror-r28, org.apache.solr.update.DocumentBuilderTest, :solr:core, -Ptests.seed=5887C0FFEE5887; FQCN grepped in one call, add composed in a separate call) completed **SUCCESS**; job steps verified via the API (prepare job and test job fully green, Run focused tests and Upload test results both success, no runner defect); the ci branch head 4990e21c22f has the shipped head c4c57ef7bcb as its parent.

**Left for Nick's decision:** only the standard one, whether and when to open the PR.

## Draft PR description

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-5887

## What happens today

When an update contains a document the schema rejects, the error names only the document: `ERROR: [doc=123] unknown field 'unknown'`. Nothing in the message says which core rejected the document. On a node hosting many cores or collections, the error alone does not identify the core with the schema problem, which is what SOLR-5887 reports.

## What this change does

`AddUpdateCommand` now converts documents through a small wrapper around `DocumentBuilder.toDocument`, used by all three conversion paths in the command (single document, document stream, and the in-place update document). When the conversion throws a `SolrException` with code 400 and the request carries a core, the wrapper rethrows a 400 `SolrException` whose message is prefixed with `core <name>: ` and keeps the original exception as the cause. `DocumentBuilder` itself is unchanged, and so are its other callers (real-time get and classification among them). The error code does not change; the message text gains the prefix. When the request carries no core, the original exception is rethrown unchanged.

## Proof

`DocumentBuilderTest.testAddUpdateCommandErrorNamesCore` sends a document with an unknown field through `AddUpdateCommand` and asserts the 400 error message names the core and the unknown field. Against the base production code (b6b2b8f10e9) the class runs 17 tests with exactly 1 failure: the new test, whose assertion fails on the base message `ERROR: [doc=123] unknown field 'unknown'`. With this change the class passes 17/17. Verified 2026-10-07 at c4c57ef7bcb; the GitHub Actions corroboration run 37635905367 at the same head also passed.

## Limits

Only document-conversion errors raised through `AddUpdateCommand` name the core. Errors from later update stages, and conversion errors raised by `DocumentBuilder`'s other callers, are unchanged; threading the core name into `DocumentBuilder` itself so every caller gets it can be done in a follow-up ticket and PR on request. The core name is prepended, so these messages no longer start with `ERROR:`; anything matching the message prefix rather than the error code will see a different string. Requests that carry no core keep the old message exactly.

Changelog: `changelog/unreleased/SOLR-5887-doc-error-core-name.yml`

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
