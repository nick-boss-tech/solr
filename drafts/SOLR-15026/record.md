# Skip record: SOLR-15026

- Ticket: SOLR-15026, MiniSolrCloudCluster can inconsistently get confused about when it's using SSL. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2020-12-02, updated 2021-02-13, no fix versions, no links; ten comments, the last on 2020-12-15.
- Verdict: keep-open.
- Draft branch: solr-15026-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and every cited name grepped at this head (detached worktree).
- Source: remediation redo of batch 03, item 25, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) cited the thread from audit evidence; the thread is checked here.

## Ticket reading

MiniSolrCloudCluster takes SSL decisions from system properties in some places and from its JettyConfig in others, so a test that instantiates it directly can end up with mixed SSL behavior. The reporter lists two workarounds: the SuppressSSL annotation, or RandomizeSSL with SolrCloudTestCase. The thread (hossman and thelabdude, 2020-12-02 to 2020-12-15) settled on the SuppressSSL workaround in SOLR-14934.

## Grep and code reading at the pinned head

- `git grep -n "@interface SuppressSSL" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/test-framework/src`: the annotation is at solr/test-framework/src/java/org/apache/solr/SolrTestCaseJ4.java:212.
- `@interface RandomizeSSL` is at solr/test-framework/src/java/org/apache/solr/util/RandomizeSSL.java:42.
- `class MiniSolrCloudCluster` is at solr/test-framework/src/java/org/apache/solr/cloud/MiniSolrCloudCluster.java:93.
- MiniSolrCloudCluster.java:268-271: SSL mode comes from `jettyConfig.sslConfig.isSSLMode()`, the JettyConfig path the ticket asks for.
- MiniSolrCloudCluster.java:118: the solr.xml template still sets `<str name="urlScheme">${urlScheme:}</str>`, so the URL scheme is still read from a system property. That property is the source of the mixed behavior the ticket describes.
- The workaround commit 8732df8c50 (2020-12-02, "SOLR-14934: test workaround for SOLR-15026") is an ancestor of the pin (`git merge-base --is-ancestor 8732df8c50 498a09aea97aac86c2cc7ac114113b946d249942` succeeds).

## History

- 8732df8c50 is the workaround, not a fix of the cluster's property handling. The ticket's wish for a simple way to instantiate a cluster with SSL from JettyConfig is partly in place (MiniSolrCloudCluster.java:268), and the solr.xml urlScheme still reads the property (:118).

## Reproduction attempts

Not run. A test that instantiates MiniSolrCloudCluster with an SSL JettyConfig and a conflicting system property would show whether the scheme comes from the property; this pass did not run it.

## Gap analysis

The ticket is partly addressed: the JettyConfig path exists and a workaround is in place. What is open is whether the urlScheme property should still decide the scheme, and whether the test framework should drop the property path. That is a test-framework design choice the thread left to the maintainers; the record does not decide it.

## Verdict justification

keep-open: the attempts made are the code reads and greps above at the pinned head, the Jira read, and the history check of the workaround commit. The missing piece is (1) a run that instantiates the cluster with an SSL JettyConfig and a conflicting property, showing which scheme is used; and (2) the maintainer decision on removing the property path. close-fixed is not supported: the workaround commit does not change the cluster's property handling. close-answered is not supported: the thread answers the workaround, not the open design question.

Missing for a closing verdict: the run output and the design decision.

## Limits

This record does not reproduce the mixed scheme. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
