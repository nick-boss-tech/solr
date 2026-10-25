# Assignment: PR 5126 executable highlighting examples on Linux, round 1

Status: OPEN

- Posted: 2026-10-25 by the Windows review chat, at Nick's explicit request to hand examples to the Linux VMs before replying.
- Capability tags: `premise-run, implementation, review, draft`.
- Intended host: any available capable Linux VM; one claimant for the query slice. A second Linux VM may independently claim the RTG slice below. Serialize builds with each host's lock.
- Priority: follow-up to today's maintainer response on Nick's existing review comment. Produce evidence before another public reply.
- Target: https://github.com/apache/solr/pull/5126 (David Smiley's PR, not Nick's).
- Ticket: SOLR-3479.
- Pinned PR source head: `74b09fb443f060caac3f8ec9701909cec276cfb4`.
- PR branch: `dsmiley/solr:SOLR-3479-highlightDocTransformer`.
- PR base: `bd2b2d5a02584893c5b5fe16c1a7eee89c2bb6c9`.
- Board bootstrap snapshot used to prepare this assignment: `a06f271b81a8f1e2423b647316bb4f9a431d13d5`.
- Authorization: diagnostic examples, test-only evidence branches on Nick's fork, and local Linux runs of the named tasks. No production fix, foreign-branch push, public comment, JIRA write, or PR creation is authorized by this handoff.

## Bootstrap, claims and exact revision

Read OPEN-WORK.md, WORKFLOW.md, START-HERE.md, UI-PLAYBOOK.md, pool/decision-map.md, material/decision-ladder.md, pool/owner-complaints-2026-10-19.md, hosts/pool-linux.md and your own host file. Read comment-formula.md before drafting the reply. Records are add-only; do not edit this assignment, another host's claim/receipt, or the generated index. Claim Q or R explicitly in a new claim file; first published claim wins for that slice. Record UTC start time, board snapshot, host, tags, JAVA_HOME and the expected deliverables. The two slices are independent and require separate worktrees/claims.

Read the live PR head and discussion before work. Our disputed comment is https://github.com/apache/solr/pull/5126#issuecomment-6390625724; David's response is https://github.com/apache/solr/pull/5126#issuecomment-6393523710. Check existing claims and reports for PR 5126 / SOLR-3479 before claiming; do not duplicate a completed or live slice.

Fetch `apache/solr`'s `refs/pull/5126/head` and verify the pinned SHA. Work from a clean isolated worktree at the pinned head, never the dirty shared source tree. If the PR has advanced, preserve this pinned example and record both heads; inspect whether the relevant code changed. Any run on a newer head is a separately recorded input, not proof for this SHA. Do not rebase or modify David's branch.

The proposed fork branches are `solr-3479-pr5126-query-examples-r1` and `solr-3479-pr5126-rtg-examples-r1`. Confirm branch absence and claims before creating; do not overwrite another worker's branch. Test changes must be committed as Nick Shanin, with no attribution trailers, pushed normally to Nick's fork, and read back for the remote full SHA. No changelog fragment for this diagnostic test-only work. Preserve the baseline contributor commits. Publish the small incremental test patch/compare, not the entire PR as if it were Nick's work.

These are premise runs, not an implementation gate or a reason to label the PR fixed. The production source must remain byte-identical to the pinned PR head. Record baseline SHA, diagnostic commit SHA, test patch, exact commands and outputs so the original-source observation can be reproduced. If a code fix is suggested, describe it separately and wait for a separately scoped implementation assignment.

## Why the query example is needed (slice Q, primary)

At the pinned head, `HighlightAugmenterFactory.java:125-135` reads `HighlightParams.Q`, which is `hl.q`, and returns null when absent. Transformer-local `q` maps to that explicit override. `setContext` at line 180 instead uses `context.getQuery()` when no override was parsed. `BasicResultContext.java:48` takes `rb.getQuery()`, populated with `parser.getQuery()` by `QueryComponent.java:190-197`. eDisMax adds boost queries at `ExtendedDismaxQParser.java:181-183`; `getHighlightQuery()` at lines 703-705 returns only the user query or alternate user query. `HighlightComponent.java:144-155` uses that parser method for its implicit path.

David thinks ordinary request `q` goes through the transformer's `parseHighlightQuery`. Our static reading distinguishes it from `hl.q`. Receiving different query objects is source-backed; the claimed fox/cats output has NOT been executed. Determine whether the observable discrepancy actually occurs. A non-reproduction with a traced explanation is a successful evidence outcome; do not force a predicted failure.

## Q fixture and exact requests

Reuse the existing `TestHighlightAugmenterFactory` fixture, whose setup already commits these two documents:

| id | title_t | body_t |
| --- | --- | --- |
| 1 | Fox story | The quick brown fox jumps over the lazy dog. Nothing else here. The fox ran away. |
| 2 | Fox cameo | A story about cats. Cats sleep a lot. |

Base request, with parameter values encoded normally by the client:

```text
q=fox
defType=edismax
qf=body_t title_t
sort=id asc
rows=10
fl=id,body_hl:[hl f=body_t]
hl.defaultSummary=false
hl.tag.pre=<em>
hl.tag.post=</em>
```

Run each combination of:

- `bq` absent versus `bq=body_t:cats`.
- Implicit query (neither request `hl.q` nor local `q`), request `hl.q=fox`, local `fl=id,body_hl:[hl f=body_t q=fox]`.
- `hl.weightMatches=true` versus `false`.
- Transformer alone (`hl=false`) versus transformer plus existing component (`hl=true`, `hl.method=unified`, `hl.fl=body_t`). This additional control detects any influence from enabling the component in the same request.

That is 24 requests, not 24 independent JUnit methods. Both documents should match through body/title in all rows. Use document 2 for the cats observation and document 1 as a positive fox control. The expected discrepancy to test is cats being highlighted in doc 2 for implicit query + bq, disappearing with explicit/local highlight query. Do not assume it occurs under both weightMatches settings. For the no-bq and explicit/local controls, expect no cats highlight. Record the component output as a separate comparison.

## Q ready-to-insert diagnostic method

Add this method to the existing class `solr/core/src/test/org/apache/solr/response/transform/TestHighlightAugmenterFactory.java`. It uses the committed fixture and existing imports/helpers. It deliberately does not assert the disputed implicit+bq result. Small API or formatting adjustments are allowed if documented; do not change the requests silently.

```java
  @Test
  public void testReview5126QueryExamples() throws Exception {
    for (boolean weightMatches : List.of(true, false)) {
      for (boolean boost : List.of(false, true)) {
        for (boolean component : List.of(false, true)) {
          for (String variant : List.of("implicit", "explicit", "local")) {
            ModifiableSolrParams params = new ModifiableSolrParams();
            params.set("q", "fox");
            params.set("defType", "edismax");
            params.set("qf", "body_t title_t");
            params.set("sort", "id asc");
            params.set("rows", "10");
            params.set(
                "fl",
                variant.equals("local")
                    ? "id,body_hl:[hl f=body_t q=fox]"
                    : "id,body_hl:[hl f=body_t]");
            params.set("hl", component);
            params.set("hl.method", "unified");
            params.set("hl.fl", "body_t");
            params.set("hl.defaultSummary", false);
            params.set("hl.tag.pre", "<em>");
            params.set("hl.tag.post", "</em>");
            params.set("hl.weightMatches", weightMatches);
            if (boost) {
              params.set("bq", "body_t:cats");
            }
            if (variant.equals("explicit")) {
              params.set("hl.q", "fox");
            }
            var response = solrTestRule.getSolrClient().query(params);
            String label =
                "variant=" + variant + " boost=" + boost
                    + " component=" + component + " weightMatches=" + weightMatches;
            System.out.println("PR5126_QUERY " + label + " request=" + params);
            System.out.println(
                "PR5126_RESPONSE " + label + " "
                    + org.apache.solr.common.util.Utils.toJSONString(response.getResponse()));
            var docs = response.getResults();
            assertEquals(label, 2, docs.size());
            assertEquals("1", docs.getFirst().getFieldValue("id"));
            assertEquals("2", docs.get(1).getFieldValue("id"));
            assertContains("<em>fox</em>", docs.getFirst().getFieldValue("body_hl"));
            String inline =
                String.valueOf(docs.get(1).getFieldValue("body_hl"))
                    .toLowerCase(java.util.Locale.ROOT);
            if (!boost || !variant.equals("implicit")) {
              assertFalse(label + " inline=" + inline, inline.contains("<em>cats</em>"));
            }
            if (component) {
              String highlights =
                  String.valueOf(response.getHighlighting()).toLowerCase(java.util.Locale.ROOT);
              assertFalse(label + " highlighting=" + highlights,
                  highlights.contains("<em>cats</em>"));
            }
          }
        }
      }
    }
  }
```

Every case prints request and full response before its assertions. Ensure the report includes all 24 rows; if an unexpected assertion stops the loop, retain that first failure and gather the remaining observations with a documented observation-only harness change. Do not weaken a control and then call it passed. Capture the diagnostic commit separately for every changed harness.

## Q named Linux tasks and proof record

Use the Linux pool runner, host build lock and disk/inode guards from hosts/pool-linux.md. Read the runner's current docs before invoking it. JDK must satisfy the pinned checkout; record its path and version. No Windows Gradle execution.

Before each test leg run `:solr:core:cleanTest` through the runner, preserve separate raw logs and fresh XML. Use seed `34795126` throughout:

```text
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.response.transform.TestHighlightAugmenterFactory.testReview5126QueryExamples -Dtests.seed=34795126
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.response.transform.TestHighlightAugmenterFactory -Dtests.seed=34795126
```

These commands describe the task/filter input to the serialized runner, not permission to start another unguarded Gradle build. First run the focused diagnostic method, then the whole existing class with the added method. For the test-only patch, run named formatting and compile checks: `tidy`, `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`. Keep any tidy delta scoped to the diagnostic test. If the pinned production tree itself needs formatting, record that separately; do not fold an unrelated production repair into this example branch. A report must distinguish the original-source run from any modified-source experiment.

Capture command lines, seed, UTC times, exit status, host/JDK, clean source baseline and test-overlay SHAs, test patch, runner/log paths, fresh XML test/failure counts, and all raw request/response pairs. Publish evidence in durable files reachable from the report; a VM-only absolute path is insufficient for another host to review it. Do not quote old CI as execution of this diagnostic.

If the query effect reproduces, reduce it to the smallest two-request example using the SAME index/config: implicit+bq versus `hl.q=fox`, preserving the exact observed JSON for document 2. Include field definitions and whether weightMatches was true or false. Explain that the document matches `q=fox` in its title while cats appears only in its body/boost query. Then add one explicit regression assertion to show the implicit result differs from the explicit one on unchanged production. Run it as a separate focused leg. If instead you choose an equality assertion to express intended behavior, its expected assertion failure on the current PR head is the reproduction; it is not a green regression or a fixed bug. Do not run against pre-PR main, which lacks this transformer.

If it does not reproduce, preserve the complete matrix and inspect the Unified Highlighter query/weight handling to explain why the different query objects do not change output. Correct the static prediction in the report and draft. Do not turn a harness startup/compile failure or missing response into a non-reproduction verdict.

Deliver `reports/pr-5126-query-examples-round-1.md`, evidence links, and the verified fork diagnostic branch SHA. Verdict: REPRODUCED, NOT REPRODUCED WITH EXPLANATION, or INCONCLUSIVE with the exact failed step. This is the decision-ladder rung 2 probe the original static review lacked.

## Optional independent RTG slice R (secondary, not a query blocker)

This is a known support limitation, not a newly alleged defect. David's October 24 JIRA comment `18131608` says current `[hl]` cannot support RTG because it must read from the index. The PR description's Solution bullet still advertises real-time get. A small diagnostic can clarify exactly what works without arguing about the acronym.

Use a separate test-only branch/worktree at the same pinned head. Reuse the existing embedded client but create fresh IDs, distinct from fixture IDs 1 and 2. Keep all examples without a query so fallback is what is exercised. For each phase compare plain `/get` (`fl=id,body_t`) with `/get` using `fl=id,body_t:[hl]`; capture full JSON and the exact client method/params. The original suite already uses `client.getById(id, params)` for this endpoint.

1. Add a document with id `rtg-existing-5126`, body `Original stored text.`, then commit. Compare both forms; retain as committed control.
2. Fully replace that ID without committing, body `Fresh update text.`. Plain get must return the fresh text. Capture an ordinary `/select` for the ID to show the searcher's stored value is still the old text, then compare transformed get. A response containing old text, omitting the field, or throwing is an observation to record, not a result to predict in advance.
3. Add id `rtg-new-5126`, body `Uncommitted insert text.`, without committing. Plain get must return it; ordinary `/select` for that ID must not find it. Compare transformed get.
4. Commit, then repeat transformed gets as a post-commit control.

Disable automatic commits appropriately in the isolated test fixture or otherwise prove with the adjacent `/select` controls that phases 2 and 3 are genuinely uncommitted for the active searcher. If an automatic reopen makes the new data visible, that case is non-discriminating and must be labeled so. Avoid timing-only sleeps. Keep doc/update/query ordering explicit, restore test settings and clean up fresh documents in finally so the whole class still passes. No `/export` or alias issue investigation; #5188 already owns that subject.

Use module `:solr:core`, class `org.apache.solr.response.transform.TestHighlightAugmenterFactory`, proposed method `testReview5126RtgExamples`, seed `34795126`, the same cleanTest/runner/log/XML requirements and formatting/compile tasks as Q. Print observations before assertions. Commit/push/read back the test-only branch and publish `reports/pr-5126-rtg-examples-round-1.md` with exact heads, commands, small incremental patch, plain-versus-transformed responses and searcher-visibility controls. This slice must not delay the primary query report.

## Result-based reply draft, no posting

Q's claimant owns `reports/pr-5126-reply-with-examples-round-1.md`, added after the Q result. Incorporate R only if its report has arrived; otherwise quote the already-visible PR description and JIRA scope statement without claiming runtime RTG proof. Read the live PR and discussion again before drafting. The reply must open with the GitHub AI disclosure in comment-formula.md, name @dsmiley, acknowledge that the original explanation was too compressed, state only what ran, and include the smallest observed request/response pair with evidence links. If the query prediction was wrong, say so directly and withdraw that predicted behavior. Avoid repeating P2 as a substitute for evidence. No em/en dashes, checklist, or internal process vocabulary in public text. A draft should not assert Nick has reviewed it until he does; label the file awaiting his review while preserving the required disclosure in the proposed outbound text.

Publish the findings and concrete draft to coordination-ui using the add-only posting workflow. Do not post a GitHub/JIRA comment or edit the existing comment. Nick will review the measured examples before choosing a public response.
