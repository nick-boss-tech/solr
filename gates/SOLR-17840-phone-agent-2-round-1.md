# Gate job: SOLR-17840 (preserve collected LTR partial results)

Status: CLAIMED (claims/phone-agent-2-blockers-round-1.md)

- Branch: solr-17840-preserve-partial-results-phone-agent-2. Head: `3999ff0be34bd192c2325b9138b8aa8a7b5bc24d`, pushed to fork.
- Prior gated head: none. Base: `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`.
- Claiming host: phone-agent-2 under the directed round claim. Capability tags: implementation, review, gate, premise-run.
- Module: `:solr:modules:ltr`. Focused class: `org.apache.solr.ltr.TestLTRQParserPlugin`. Seed: `1BB02D3762B56611` as project property `tests.seed`.
- Diff scope: QueryComponent.java, existing TestLTRQParserPlugin.java, changelog/unreleased/SOLR-17840-preserve-partial-results.yml.

Links: Ticket SOLR-17840. Category Search and query components, secondary Test quality. Siblings SOLR-17631; existing QueryComponent work SOLR-18539 and SOLR-18541 concerns different distributed merge paths, untouched here. Supersedes no previous fix; initial compatibility commits remain in base.

## Reproduced premise

The existing ignored timeout method was enabled only in a disposable upstream worktree. It executed once and failed numFound 4 versus 0. A separate instrumentation-only repeat at the same seed established: ReRankCollector has 4 main hits, restores 4 after incomplete reranking; SolrIndexSearcher creates DocList size=4/matches=4; QueryComponent reaches its limit early return with those 4 hits but responsePresent=false. Logs `/workspace/phone-agent-2-logs/ltr-premise` and `/workspace/phone-agent-2-logs/ltr-diagnostic`. No production diagnostic prints are in the ticket branch.

The patch only moves the existing BasicResultContext/rsp.addResponse calls before that post-search early return and re-enables the original regression without altering its timeAllowed, expectations, order or scores. No speculative scorer hardening, timeout disabling or fixture changes.

## What to run

Runner `/workspace/tools/phone-agent-2-hardening-gate.py`, configuration `/workspace/phone-agent-2-logs/gate-17840.json`; logs `/workspace/phone-agent-2-logs/gate-17840-attempt-1`. Shared build lock and disk guard apply.

0. Parse the changed YAML fragment (repository schema validator already passed before commit).
1. Root tidy, exact repair if needed.
2. `:solr:modules:ltr:compileJava :solr:modules:ltr:compileTestJava -Pvalidation.errorprone=true`, including dependency compilation.
3. Proof: keep head test enabled; replace only QueryComponent.java with pinned base. Run exact timeout method. Require its executed failure `mismatch: '4'!='0' @ response/numFound`, not merely a nonzero exit; archive fresh XML.
4. Restore committed head, clean test outputs, run the full TestLTRQParserPlugin class. Archive/count fresh XML, including the formerly ignored method.
5. `:solr:modules:ltr:check -x test`. This is the assignment's allowed module check, not a whole-repository test run.

## Decisions

Rung 2 runtime boundary evidence localizes the loss to response exposure, not the LTR iterator. Preserve the existing partial-result and original-score assertions. Do not weaken an expected value if head fails. WORKFLOW gate repairs allow one tree-changing repair and one complete retry; failed retry is handed back. No PR opening, ready flip, posts or comments.

## Checklist

- [ ] fork head read back
- [ ] changelog parsed
- [ ] tidy
- [ ] Error Prone compile
- [ ] pre-fix proof, expected failure
- [ ] full focused class, fresh XML counts
- [ ] module check
- [ ] receipt and terminal outcome
