# Skip record: SOLR-13846

- Ticket: SOLR-13846, PreemptiveBasicAuthClientBuilderFactory use of static code blocks makes it unreliable in tests. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-10-15, updated 2020-01-21, no fix versions, no links; seven comments, the last on 2019-10-17 (the workaround backported to branch_8_3).
- Verdict: close-obsolete.
- Draft branch: solr-13846-draft-close (the existing close draft; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Grep, ancestry and the replacement class read at this head (detached worktree). Upstream sha not cited.
- Source: remediation redo of batch 05, item 17, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (vm2, from the B2 reading) cited a grep that was not re-run; this record re-runs it.

## Ticket reading

PreemptiveBasicAuthClientBuilderFactory uses static blocks that read system properties at class load, so later instances in the same test JVM use the first test's settings. Two tests that use it can fail depending on order.

## Grep at the pinned head

- `git grep -n "PreemptiveBasicAuthClientBuilderFactory" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java' | wc -l` gives 0.
- `git grep -l "class PreemptiveBasicAuthClientBuilderFactory" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java' | wc -l` gives 0. The class is not in the tree.

## Replacement and history

- The workaround commit 939b3364e6 (2019-10-15, "SOLR-13846: workaround - elliminate use of problematic PreemptiveBasicAuthClientBuilderFactory in tests that don't need it") is an ancestor of the pin.
- The test that used the class now uses its replacement: solr/core/src/test/org/apache/solr/cloud/RecoveryZkTestWithAuth.java:27 and :48 use `PreemptiveBasicAuthClientCustomizer`.
- The replacement, solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/PreemptiveBasicAuthClientCustomizer.java, has no `static {` block. It reads its system properties at lines 102 and 104, inside its code path rather than at class load. This record does not trace whether those reads are cached elsewhere.

## Reproduction attempts

Not attempted. The class named in the ticket does not exist at the pin, so the reported order-dependent failure cannot occur with that class.

## Gap analysis

The named class and its static initialization are gone, and the test that used it no longer does. The replacement's property handling was read but not tested for the same cross-test effect; the record does not claim that was checked.

## Verdict justification

close-obsolete: the class the ticket is about (PreemptiveBasicAuthClientBuilderFactory) has been removed; the grep at the pinned head finds no class and no reference (both empty results above). The workaround commit is on the pin, and the replacement has no static block. The record does not claim the replacement was tested for cross-test property use.

## Limits

This record does not run the tests. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
