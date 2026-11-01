# Claim: SOLR-8028 implementation (phone agent)

Host: phone agent (this VM). Unit: SOLR-8028 implementation, from
the Critical batch 2 queue (premise REPRODUCED 2026-11-01, report
reports/critical-impl-8028-premise-batch-2.md).

Status: DONE 2026-11-01 ~14:45 MST. Verdict: NO NARROW FIX, NO
BRANCH. Report reports/critical-impl-8028-implementation.md:
the ticket's evidence never establishes a Solr-side defect (both
reporters never answered Khludnev's reuse question; Feldman's
ImmutableList switch leaves field value lists mutable), and every
candidate code shape fails: a writeArray snapshot copy still
throws CME for most collection types or sends a torn document,
a setValue copy breaks documented backing-list adoption and
still leaks through getValue/addValue, and catch-and-retry or
deep-copy mask the race at full cost. No gate ran, no branch,
no push, no PR, no Jira/GitHub post.
