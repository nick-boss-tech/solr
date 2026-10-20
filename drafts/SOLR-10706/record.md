# Skip record: SOLR-10706

- Ticket: SOLR-10706, "SolrJ wrongfully uses URLEncoder.encode for constructing url fragments". JIRA status read 2026-10-20T11:40:30Z (UTC): Open, resolution None, no fix versions, updated 2019-06-08.
- Verdict: close-cannot-reproduce. This replaces the recorded keep-open: the premise (a space is sent as %20) does not hold for SolrJ's parameter encoding, and a run shows the form encoding the ticket asks for.
- Draft branch: solr-10706-draft-close.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T11:40:30Z (UTC).

## Ticket reading

The reporter says `URLEncoder.encode` produces `%20` for a space in query parameters, and asks for URL-fragment encoding in its place. The example is a local-params query `{!parent which=type:...}+(name:testName)`. No comments.

## Code reading (pinned head)

- `solr/solrj/src/java/org/apache/solr/common/params/SolrParams.java:415-429` (`toQueryString`): the only `URLEncoder` use in `solr/solrj/src/java` (grep `git grep -l URLEncoder 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/solrj/src/java`: one file). It encodes each parameter name and value with `URLEncoder.encode(..., UTF_8)`, which is the application/x-www-form-urlencoded form the reporter names as correct.

## Reproduction attempt

- Command: `java Enc.java` (JDK 21.0.12.1, the pool's JAVA_HOME), a two-line program calling `URLEncoder.encode` with UTF-8 on `"a b"` and on the reporter's example `{!parent which=type:x}+(name:testName)`. Input is deterministic; no seed.
- Result: `a+b` for the space, and `%7B%21parent+which%3Dtype%3Ax%7D%2B%28name%3AtestName%29` for the example. The space is `+`, not `%20`; the literal `+` is `%2B`.
- Log: `/workspace/gates/repro-10706/run.out` on vm2.
- Conclusive: yes for the premise. The output matches the form encoding that servers decode as a space. The run does not cover a live Solr node.

## History

- evidence.py at the pinned head: no commits naming SOLR-10706.

## Gap analysis

The reported space-as-%20 does not come from `toQueryString`. The example's `+` is sent as `%2B`, which the server decodes as a literal plus. If a client sends a raw `+` in a URL, that is the caller's encoding, not SolrJ's.

## Verdict justification

close-cannot-reproduce meets the rubric: at least one reproduction with its command, input and result (the run above), a statement of what it does not cover (the live request path), and the code line that encodes parameters.

## Limits

This record does not test the HTTP client send path on a live node, nor the Http2 client's URL building. It does not check the parameter names beyond the example.
