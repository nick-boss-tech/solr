# Skip record: SOLR-15310

- Ticket: SOLR-15310, HttpClientUtil interceptors are shared by multiple CoreContainer in same Test JVM. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-04-02, updated 2021-04-12, no fix versions, no links. One comment (dsmiley, 2021-04-04): the sharing could mask real bugs in tests.
- Verdict: close-obsolete.
- Draft branch: solr-15310-draft-close (the existing close draft; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and grep run at this head (detached worktree). Upstream sha not cited.
- Source: remediation redo of batch 03, item 14, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (vm2, from the B2 reading) cited a grep that was not re-run; this record re-runs it.

## Ticket reading

In a test JVM with several nodes, all CoreContainers share the static `HttpClientUtil` interceptors, including the PKI authentication interceptor, so requests may be processed more than once. The ticket is about that shared static state in `HttpClientUtil`.

## Grep at the pinned head

- `git grep -n "class HttpClientUtil\b" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java' | wc -l` gives 0. No Java class named HttpClientUtil exists at the pin.
- `git grep -n "HttpClientUtil" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java' | wc -l` gives 2. Both are comments in solr/core/src/test/org/apache/solr/cloud/TestMiniSolrCloudClusterSSL.java (lines 157 and 376); neither is code that uses the class.

## Code reading

- The static interceptor registry the ticket describes is not at the pin. The authentication listener that the ticket names is now registered through the `setup(MutableListenerFactory)` hook (solr/core/src/java/org/apache/solr/security/PKIAuthenticationPlugin.java:315), which is the design the 2026 code uses instead of a static interceptor list. That is a different mechanism; this record does not establish whether its registration is shared across CoreContainers in a test JVM.

## History

- None cited in the earlier B2 reading. The commit that removed `HttpClientUtil` was not identified; `git log -S "class HttpClientUtil"` was not run in this pass.

## Reproduction attempts

Not attempted. The class the ticket describes is absent at the pin, so a run of the old sharing cannot be reproduced here. Whether the new listener registration is shared across cores in one test JVM was not tested.

## Gap analysis

close-obsolete rests on the removed class: the grep at the pin finds no HttpClientUtil class. The underlying question dsmiley raised (test-JVM sharing masking bugs) is not answered by this verdict. It is a different question about the current registration, and it is not shown either way.

## Verdict justification

close-obsolete: the class the ticket is about (HttpClientUtil) has been removed. The grep at the pinned head finds no class and no code use, only two comments (empty result for the class). The record does not claim the current listener registration was checked for the same cross-core sharing.

## Limits

This record does not reproduce the shared-interceptor behavior on any build. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
