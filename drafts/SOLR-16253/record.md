# Skip record: SOLR-16253

- Ticket: SOLR-16253, "Spellcheck collateParam cannot override fq when maxCollationTries > 0". JIRA status Open, resolution none, fix versions none, created 2022-06-14, updated 2022-09-09. Checked 2026-10-19 UTC.
- Verdict: pr-needed.
- Draft branch: solr-16253-draft-pr.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-16253-vm2.md).

## Ticket reading

The ticket reports that with spellcheck.maxCollationTries greater than zero, a spellcheck.collateParam.fq value does not replace the request's fq when the collation query is verified. The reporter's steps: a misspelled query returns collations; adding fq=id:someNonexistentId returns no collation, as expected; adding spellcheck.collateParam.fq=*:* still returns no collation, where the reporter expects one.

The only comment (gerlowskija, 2022-09-09) says the behaviour is expected: spellcheck.collateParam adds parameters to the collation query, and for a single-valued parameter the effect is not what the reporter expects.

Linked issues: none named in the ticket. The evidence helper found no commit on origin/main that names SOLR-16253 and no test class named in the ticket.

## Code reading

- solr/core/src/java/org/apache/solr/spelling/SpellCheckCollator.java:111-121: each request parameter with the spellcheck.collateParam. prefix is copied onto the collation request with params.set(name, values). For fq this sets the value on the collation request's parameters.
- SpellCheckCollator.java:152-156: the collation check runs through ultimateResponse.req.subRequest(params), and checkResponse.setFilters(ultimateResponse.getFilters()). The filters are those already parsed from the original request, including fq=id:someNonexistentId.
- solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java:223-226 (prepare): the filters from the request's fq parameters are appended to rb.getFilters() (new ArrayList of the existing filters plus QueryUtils.parseFilterQueries(req)). So the override fq=*:* is added to the list that already holds id:someNonexistentId, and the intersection matches nothing.
- SpellCheckCollator.java:169-180: the hit count is read from the collation response; a collation is kept only when hits > 0 or verification is off. With the original filter still applied, hits stay at 0 and no collation is returned.

Commands run at the pinned head (grep on the fork tree):
- git grep -n -i 'collateParam' origin/main -- solr/core/src: one hit in SpellCheckCollator.java, at lines 111 and 114. Test coverage: solr/core/src/test/org/apache/solr/spelling/SpellCheckCollatorTest.java, testCollateWithOverride, uses collateParam.mm only. No test uses collateParam.fq.
- git grep -n 'setFilters\|getFilters' origin/main -- solr/core/src/java/org/apache/solr/spelling/SpellCheckCollator.java: lines 156 only.

## History

- Commits on origin/main naming SOLR-16253: none (evidence helper, pinned head).
- Commits touching the collation override code: not traced in this pass. The ancestry of the cited lines was not checked with git log -S.

## Reproduction attempts

1. Command: :solr:core:test --tests org.apache.solr.spelling.SpellCheckCollatorTest --offline, with an added test method testRepro16253CollateParamFq (diff saved at /workspace/gates/logs/vm2/16253/repro-test.patch) on a worktree at origin/main 498a09aea97. Input: q=lowerfilt:(+fauth +home +loane), fq=id:99999 for the control and override variants, spellcheck.collateParam.fq=*:* for the override variant, no fq for the nofq variant. Run 1, log /workspace/gates/logs/vm2/16253/16253-repro-r1.log, XML /workspace/gates/logs/vm2/16253/16253-repro-r1.xml/. Result: BUILD SUCCESSFUL; the first version of the test printed collations=[] for the control and the override variants.
2. Same command, second run with three variants (log /workspace/gates/logs/vm2/16253/16253-repro-r2.log, XML /workspace/gates/logs/vm2/16253/16253-repro-r2.xml/). Result: nofq collations=[lowerfilt:(+faith +hope +loaves), lowerfilt:(+faith +hope +love)]; control (fq=id:99999, no override) collations=[]; override (fq=id:99999 plus spellcheck.collateParam.fq=*:*) collations=[]. No failures in either run, and no exception was logged by the collator.
- Conclusive: yes. The same command gave the same override result twice, the nofq variant shows the harness produces collations, and the override result matches the reported symptom exactly.
- Not covered: a patched build, and any run against a live index outside the test harness.

## Gap analysis

With spellcheck.collateParam.fq set, the collation check keeps the original request's fq filters and adds the override on top. It does not replace them, so the reporter's override cannot widen the collation query. The reproduction matches the symptom: the override returns no collation with an fq that matches nothing.

The maintainer's comment treats this as intended, because collateParam is described as adding parameters. The override code at SpellCheckCollator.java:111-121 uses params.set, which reads as a replacement for single-valued parameters, so the code and its description disagree. The disagreement is about intent, not about the reproduction.

## Verdict justification

Verdict pr-needed meets the rubric: the problem reproduces on the pinned head (two conclusive runs), the code path with file and line shows the cause, and a change is needed to make a collateParam override replace the original filter for the collation query. The rubric also asks that the draft say we reproduced it; this record does.

Requirement not met: the fix itself has not been written or tested. The verdict is pr-needed on the strength of the reproduction and the code reading, not a verified patch. Whether the change should replace the filter or document the additive behaviour is a decision for the owner; the reporter and maintainer disagree.

## Limits

This record does not establish a fix. It does not check the git history of the collator's filter handling, and it does not test the reporter's exact index. The reproduction uses the SpellCheckCollatorTest index, not the reporter's data.
