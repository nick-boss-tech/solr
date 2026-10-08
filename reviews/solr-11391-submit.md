# solr-11391-submit

- Branch: origin/solr-11391-submit
- Head: 825d7f81d12b (reviewed and unchanged; no patch)
- Base: upstream/main (merge-base cabedd1d968, 18 commits behind)
- Scope: 4 files, +42/-2. `solr/core/.../search/JoinQParserPlugin.java` (the `parse()` path now calls the existing `parseMethodString` instead of `Method.valueOf`, and the TODO is removed), `TestScoreJoinQPNoScore.java` (one new test, `method=nosuchmethod` expects BAD_REQUEST with "not supported"), changelog fragment (type `fixed`), `SOLR-11391-TESTING.md` (author's unrun note, left in place).
- Verdict: Ready for review (code read only; nothing compiled or run)
- Reviewer: review-agent A1, 2026-10-08

## Findings (ranked)

No defects found. The change is a single call swap with the same case-sensitive matching:

- verified: `parseMethodString` is `private static` on `JoinQParserPlugin` (line 268). It calls `Method.valueOf(method)` and maps `IllegalArgumentException` to a `SolrException` with BAD_REQUEST and the message "Provided join method '<x>' not supported" (lines 268-276). The inner parser's `parse()` can reach it, since both are in the same outer class.
- verified: the matching is still case-sensitive (`Method.valueOf`), as before. Valid values such as `topLevelDV` behave the same. This resolves TESTING guess 3.
- verified: the null guard (`localParams.get(METHOD) != null`, line 219) remains, so `Method.valueOf(null)` is not reachable from `parse()`.
- verified: the test's four-argument `assertQEx(String, String, SolrQueryRequest, ErrorCode)` exists in `SolrTestCaseJ4` (lines 1069-1073). The asserted substring "not supported" matches the new message.
- verified (behavior change, intended): the request now returns 400 for an unknown method instead of a raw IllegalArgumentException (500). The static `createJoinQuery` path already did this.
- not verified: TESTING guess 1 (nothing earlier in query parsing rejects the unknown local param with a different message or code). The test is the check for this, and it has not been run.

LOW: the changelog title is a folded block and reads as a sentence. Style only.

## Not checked
- Nothing compiled, formatted, or run. No Gradle, no tests.
- Whether anything earlier in query parsing rejects `method=nosuchmethod` before `parse()` (TESTING guess 1). The test's assertion would catch that, but it has not run.
