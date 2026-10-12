# Configsets convergence, round 1

Verdict: **NOT CONVERGED.** Two drafts hold open should-fix items (SOLR-6960 and SOLR-15478). SOLR-7267 converges with no finding. SOLR-17363 converges with notes only.

Counts: drafts checked 4. Drafts with no finding 1 (SOLR-7267). Drafts with blocking findings 0. Drafts with should-fix findings 2 (SOLR-6960, 15478). Drafts with notes only 1 (SOLR-17363). Findings 11: 0 blocking, 5 should-fix, 6 note. All are new on this pass. Register IDs CS-R1-01 to 11.

Scope: `assignments/pool-configsets-convergence-round-1.md`. The decision record is `reports/configsets-round-1.md` and its parts, with the draft-fidelity settlements. Heads were checked with `git ls-remote origin refs/heads/solr-<n>-submit`, and all four match their named heads. Every merge-base equals the base the draft cites. One read-only subagent did the review. Nothing was edited, posted or built.

## Verdict per draft

- **SOLR-6960: NOT CONVERGED.** CS-R1-01 (should-fix); CS-R1-02, 03, 04 (note).
- **SOLR-7267: CONVERGED.** No finding. The Choice is posed with its cost.
- **SOLR-15478: NOT CONVERGED.** CS-R1-05, 06, 07, 08 (should-fix); CS-R1-09 (note).
- **SOLR-17363: CONVERGED (notes only).** CS-R1-10, 11 (note).

## Register: should-fix rows

**CS-R1-01 | SOLR-6960 | should-fix.** The Limits links to `ImplicitPlugins.json` line 28 and `SolrConfigHandler.java` lines 352 to 358 point at the head, but neither file is in the branch diff. These are pre-change symptom links. Replacement: "[ImplicitPlugins.json L28, base commit](https://github.com/nick-boss-tech/solr/blob/97d973814336101e12475558d7419321c743de79/solr/core/src/resources/ImplicitPlugins.json#L28)" and "[SolrConfigHandler.java L352 to L358, base commit](https://github.com/nick-boss-tech/solr/blob/97d973814336101e12475558d7419321c743de79/solr/core/src/java/org/apache/solr/handler/SolrConfigHandler.java#L352-L358)".

**CS-R1-05 | SOLR-15478 | should-fix.** Bold line 7 says "A schema cached for a configset can still be used after the configset is deleted and uploaded again." The body limits the premise to schema sharing, which is off by default. Replacement: "**With schema sharing on, a schema cached for a configset can still be used after the configset is deleted and uploaded again.**"

**CS-R1-06 | SOLR-15478 | should-fix.** Line 9 says "SOLR-15478 reports this." The ticket describes a backup restore and never mentions schema sharing. Replacement unless the owner confirms the reporter's setting: "SOLR-15478 reports the same symptom after a backup restore. The ticket does not say whether schema sharing was on."

**CS-R1-07 | SOLR-15478 | should-fix.** Line 17 says "so a recreated configset gets a new cache key." The recorded decision requires the sentence to match SOLR-15674 line 17, which says "so a recreated znode gets a new cache key." Replacement: "ZooKeeper does not reuse this ID, so a recreated znode gets a new cache key." If the owner prefers "configset", SOLR-15674 must change too.

**CS-R1-08 | SOLR-15478 | should-fix.** The Limits opener says "**Covered: SolrCloud configsets with schema sharing on.**" It must name what is not covered. Replacement: "**Not covered: the default SolrCloud schema path, where schema sharing is off. [SOLR-15674](https://issues.apache.org/jira/browse/SOLR-15674) covers that path.**" Keep the three bullets.

## Notes

CS-R1-02 (the visibility change of `RequestHandlers.applyInitParams` is not stated; an open owner decision), CS-R1-03 (SOLR-13706 order is not stated; no change unless the owner wants it), CS-R1-04 (SOLR-6960 length, about 4,400 characters), CS-R1-09 (SOLR-15478 landing order is not stated; the adopted order is SOLR-15674 first), CS-R1-10 (the `[helper]` link label in SOLR-17363), and CS-R1-11 (SOLR-17363 length).

## Owner items on the record (not findings)

SOLR-6960's branch changelog title fix (decision 1) and the second test run without the change (decision 2). SOLR-7267's Choice wording against a review file not on disk (decision 3). SOLR-15478's reporter setting and the failing log line (decision 5). SOLR-17363's stale-replica option and down-or-recovering excusal (decision 6), and its "verified 2026-10-05" date, which has only a takeover-log record.

## Not checked

- Gate and premise logs are not on disk, so counts are receipt-only.
- No live GitHub or Jira state was read.
- The master receipts ledger, the takeover log and security material.

## Convergence line

NOT CONVERGED. Five should-fix findings are open against SOLR-6960 and SOLR-15478.
