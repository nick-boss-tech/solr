# solr-3498-submit

- Branch: origin/solr-3498-submit
- Head: 812598302dee (matches the listed head; checked against the ticket worktree)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d9680, 16 commits behind, 3 commits ahead)
- Scope: 3 commits, 4 files, +92. `solr/solrj/src/java/org/apache/solr/client/solrj/request/ContentWriterUpdateRequest.java` (override of `setCommitWithin` that also sets or removes the `commitWithin` request parameter), `solr/solrj/src/test/org/apache/solr/client/solrj/request/TestContentWriterUpdateRequest.java` (new, two tests), changelog `changelog/unreleased/SOLR-3498-content-writer-commit-within.yml` (`type: fixed`, author Nick Shanin), and `SOLR-3498-TESTING.md` (author's hypothetical-reproduction note, left in place).
- Verdict: Nearly
- Reviewer: review-agent A3 (claude-haiku-5-5, Claude Code), 2026-10-08
- Patch: none

Nothing here was compiled, formatted, or run. No Gradle, no spotless, no test run, no fail-before proof. Every claim below rests on reading the diff and the code at the listed head. `SOLR-3498-TESTING.md` is labeled "hypothetical reproduction (not run)" and is treated as unverified.

## Findings (ranked)

1. **LOW, verified. `setParams` drops the value while `getCommitWithin()` still reports it.** `AbstractUpdateRequest.setParams` replaces the `params` object outright (`AbstractUpdateRequest.java:113-114`). The branch writes `commitWithin` into `params` only inside its `setCommitWithin` override. So `setCommitWithin(1234)` followed by `setParams(new ModifiableSolrParams())` leaves `getCommitWithin()` at 1234 and the request without the parameter. The author flags this in TESTING. No SolrJ code path in this grep replaces the params of a `ContentWriterUpdateRequest` (`setParams` callers are `UpdateRequest.java:256,314`, `JavaBinUpdateRequestCodec.java:122,229`, and `CloudSolrClient.java:397`; the type of the last one was not traced). Not patched: the fix shape is a design choice (override `setParams` to re-apply the value, or read the field in `getParams()`), and it is reached only by a caller who replaces params after setting the value.

2. **LOW, verified. The negative-value test does not discriminate.** `testNegativeCommitWithinRemovesTheParameter` asserts the parameter is null after `setCommitWithin(-1)`. On `main` the parameter is never set, so the assertion also passes there. Only `testCommitWithinIsSentAsRequestParameter` proves the change. Not patched; it is a coverage note.

3. **Verified (checked, no issue). The APIs used by the change exist with the signatures the branch uses.** `params` is `protected` (`AbstractUpdateRequest.java:26`); `commitWithin` is a protected field (line 27); `getCommitWithin()` (line 141) and `setCommitWithin(int)` returning `AbstractUpdateRequest` (line 145) match the override's signature and return type. `ModifiableSolrParams.set(String, int)` exists (line 94), `remove(String)` exists (line 142), and `UpdateParams.COMMIT_WITHIN = "commitWithin"` exists (line 41). `ContentWriterUpdateRequest` does not override `getParams()`, so the test reads the same `params` object.

4. **Verified (checked, no issue). The fail-before is a real assertion failure.** On `main`, `setCommitWithin(1234)` stores the field only, so `req.getParams().get(COMMIT_WITHIN)` is `null` and `assertEquals("1234", null)` fails. The method exists on `main`, so the failure is the behaviour under test, not a missing API.

5. **Verified (checked, no issue). Code comments.** The added comments describe what the value does (`value travels as the commitWithin request parameter`) and do not tag the ticket. This is consistent with the repo's `AGENTS.md` rule on comments.

## Owner calls (not decided here)

None needed for the stated case. If finding 1 is reachable through a real caller, whether the value should live in `params` or be computed in `getParams()` is a small design call. Pose it if the owner wants it covered.

## Not checked

- Nothing compiled, formatted, or run. No focused test, no fail-before run, no Spotless.
- Whether the CSV, XML, JSON and Solr Cell loaders behind `/update/extract` read `commitWithin` from request params. The TESTING note flags this as unverified, and it was not traced here.
- Whether the request writer sends `getParams()` for `ContentWriterUpdateRequest` as a query string on the multipart path. Not traced.
- The type of `CloudSolrClient.java:397`'s `nonRoutableRequest`, for finding 1.
- `SOLR-3498-TESTING.md` is treated as unverified. Left in place.
