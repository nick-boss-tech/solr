# SOLR-17840: owner-directed partial-result fetch patch

Status: CLAIMED (claims/phone-agent-2-blocker-patches-round-2.md)

Host phone-agent-2. Branch `solr-17840-preserve-partial-results-phone-agent-2`; verified fork head `21bf1dbb6ea2365cb3be7caab74ea853af74dd0d`. Base `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`; previous failed candidate `3999ff0be34bd192c2325b9138b8aa8a7b5bc24d`. New owner-directed implementation follow-up, not a retry of the terminal round-1 gate. Ticket SOLR-17840; Search and query components, secondary Test quality; SOLR-17631 API compatibility already upstream.

## Patch and decisions

Keep the original response-exposure fix and unchanged enabled LTR timeout regression. For partial results allowed with an already-latched limit only, finish fetching one selected document inside a thread-local scope. Restore timeout checks in finally, before transformers or subqueries. Do not exempt new limit checks or partialResults=false. Independent production review found no concrete blocker. New deterministic SolrTestCase coverage checks scope success/exception/nesting, false parameter, untripped checks, thread isolation, and DocsStreamer integration without a core fixture.

Diff scope: QueryComponent.java, DocsStreamer.java, QueryLimits.java, TestLTRQParserPlugin.java, TestPartialResultsFetch.java, and existing changelog fragment. The unchanged original regression retains its count, order, score, timing and fixture expectations.

Runner `/workspace/tools/phone-agent-2-hardening-gate-v2.py`; config `/workspace/phone-agent-2-logs/gate-17840-patches-round-2.json`; logs `/workspace/phone-agent-2-logs/gate-17840-patches-round-2/`. Shared build lock and disk/inode guards; one Gradle invocation at a time. Routing phase completed before this starts. Precommit root tidy rc=0, log `/workspace/phone-agent-2-logs/ltr-patches-round-2-precommit-tidy.log`.

## Steps

- [ ] Parse existing changelog.
- [ ] Root tidy with clean committed tree.
- [ ] Error Prone compile production and tests in core and LTR.
- [ ] Proof: restore only DocsStreamer to prior failed candidate, retaining response-exposure fix and head tests. Unchanged exact LTR timeout method at seed 1BB02D3762B56611 must fail at missing /response/numFound, with doc-values ExitingReaderException in response evidence. This isolates the second boundary; first boundary proof remains in prior receipt.
- [ ] Restore head, run full TestLTRQParserPlugin, archive XML.
- [ ] Run core TestPartialResultsFetch, TestQueryLimits, ComponentStageLimitsTest, archive XML separately.
- [ ] Run core and LTR module check excluding tests.
- [ ] Update receipt and terminal phase outcome.

One scoped tree-changing repair and full gate retry allowed per WORKFLOW; out-of-scope repair or failed retry is handed back. No Jira/GitHub comments, PR opening or ready flip. Original terminal claim and gate remain unchanged.
