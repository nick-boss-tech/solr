# Assignment: the 13 unworked Blockers (Phone Agent 2)

Status: DONE (reports/phone-agent-2-blockers-round-1.md; partial implementation outcomes explicit)

- Posted by: the phone agent, 2026-10-22, at the owner's direction: contributions prioritize new development and Blocker and Critical items. These 13 Blockers are the ones with no work record anywhere (slate audit, 2026-10-22: of the 100 open Blocker and Critical tickets, 29 have any record and 71 have none; 13 of the 16 Blockers are in the unworked set).
- Directed to: phone-agent-2 (hosts/phone-agent-2.md). Claim before starting: `claims/phone-agent-2-blockers-round-1.md`, first post wins, using the `phone-agent-2` identity. If the round cannot be taken, say so in the claim file instead of claiming.
- Source list: `assignments/pool-csv-critical-blocker-round-1.md` (the 100-ticket census) and `material/jira-issues-csv/` on this branch. The CSV is a snapshot, not evidence; every ticket gets a fresh Jira read in this pass.

## The 13

- SOLR-12833 (Blocker, 2018-10-04): Use timed-out lock in DistributedUpdateProcessor
- SOLR-16450 (Blocker, 2022-10-07): Proper handling on watcher registration failure in ZkStateReader#waitForState
- SOLR-17463 (Blocker, 2024-09-26): Solr hanging at SolrCore.getSearcher
- SOLR-17546 (Blocker, 2024-11-06): Update .htaccess for Ref Guide for removed pages
- SOLR-17840 (Blocker, 2025-08-07): LTR is broken after Lucene 10 upgrade
- SOLR-17844 (Blocker, 2025-08-07): Investigate TestFieldCacheSortRandom failures
- SOLR-17988 (Blocker, 2025-11-11): Ensure Solr 10 Release Conveys Changes Users Need To Be Aware Of For Properties
- SOLR-18240 (Blocker, 2026-05-14): Complete Major changes and Upgrade Notes in RefGuide for 9.11.0
- SOLR-18297 (Blocker, 2026-06-30, PR available: Yes): Upgrade Jetty to v12.1.14
- SOLR-18398 (Blocker, 2026-08-23): Migrate SolrQueryResponse and responseHeader from NamedList to LinkedHashMap
- SOLR-18446 (Blocker, 2026-09-14): Remove "collection" param routing support in HttpSolrCall
- SOLR-18486 (Blocker, 2026-09-28): Complete Major changes and Upgrade Notes in RefGuide for 10.1.0
- SOLR-18501 (Blocker, 2026-10-04): Solr MCP Project IP Clearance

Several of these are release, documentation, or process Blockers rather than code defects; the classification below is the product for those, and an honest non-code verdict with evidence is a finished item. SOLR-18297 already has a PR available: read it first and classify against it instead of duplicating it.

## Per-ticket work (evidence first; a classification without this evidence does not count)

1. Re-read the ticket on Jira in this pass: status, description, comments, linked tickets, PRs and commits.
2. Check the issue against current upstream main (or the branch line the ticket targets): read the code path the ticket names, with file and line. Where the ticket describes reproducible behavior, attempt the reproduction and record the result, including the seed where one applies.
3. Classify: STILL-PRESENT, ALREADY-FIXED (name the fixing commit or PR), OBSOLETE (say why), or NOT-REPRODUCIBLE (record exactly what was tried). Where the pool's WON'T DO rule applies (already done, already claimed and live elsewhere, structurally unfinishable as reported), refuse and clear it in the same push with the evidence, per pool/advisory-wont-do.md.
4. For STILL-PRESENT: implement the fix on a branch named for the ticket and run the full hardening gate at its head on your host; the gate's module check is sufficient. Write the receipt in `receipts/`. A PR draft per pr-formula.md may be written to `drafts/`; opening stays with the owner.
5. Premise bar (owner, 2026-10-21): a fix is very narrow. Do not harden a working path against a hypothetical failure. Where a proposed fix fails that bar, the verdict and the evidence are the product, with a brief closure-comment draft in the report for the owner.

## Standing rules that bind this round

- Commits as Nick Shanin; no "Claude" in authors, committers, or trailers; no em dashes in authored text.
- New tests extend SolrTestCase or SolrCloudTestCase, never SolrTestCaseJ4 (the traps guide in pool/guides/ carries the conversion rules).
- One Gradle build at a time under the shared build lock; the disk guard pauses work at or above 85 percent on the home volume.
- Changelog titles containing ": " are quoted in the YAML fragment, and the fragment parses before the gate counts.
- Neighboring live work, not part of this round: SOLR-14200 and SOLR-16155 are being worked from the phone agent's side.

## Deliverable

`reports/phone-agent-2-blockers-round-1.md`: one section per ticket with its classification and the evidence pointers (Jira read date, file and line, reproduction result, fixing commit, or the gate receipt for built items), plus a summary table. The claim goes DONE in the same push as the report. Partial delivery is fine per ticket: a finished classification for some tickets beats a silent round.

Limits: no Jira or GitHub posts or comments, no PR openings or ready flips, no force push except a rebuild of a branch this round created, pushed with lease. Do not lower a classification to clear the list faster.
