# Assignment: review the RC3 mailing list reply (round 1)

Status: OPEN, for review. Nothing is posted. The owner decides whether and how it goes out.
Tags: review, owner-gate, mailing-list

## Draft

Subject: Re: [VOTE] Release Apache Solr 10.1.0 RC3

We ran the RC3 smoke tester on aarch64 Linux (Ubuntu 22.04, JDK 21.0.12.1), with /workspace on NFS. It did not pass cleanly on our host. Findings:

1. Replica recovery fails on NFS. `IndexFetcher` calls `Files.createLink` with no copy fallback (solr/core/src/java/org/apache/solr/handler/IndexFetcher.java:1184). On a filesystem that refuses hard links (EMLINK), recovery retries and fails. Four tests fail for this reason on NFS and pass on local disk at the same seed: TestTlogReplica.testKillLeader, testRecovery, testOnlyLeaderIndexes, and TestCoordinatorRole.testNRTRestart. Tracked in SOLR-18586. A copy-fallback change passes the affected classes on NFS. Fix branch, not a PR: https://github.com/nick-boss-tech/solr/tree/emlink-copy-fallback
2. The JS client build fails when NODE_ENV=production. npm skips the devDependencies that the generated package.json lists for the Babel plugins, so jsClientBuild fails. The install line is the same in RC3 (solr/webapp/js-client/build.gradle.kts), from a build change with no JIRA ticket. The one-flag fix (--include=dev) is on the same branch. Not yet filed.
3. CoreAdmin unload after a failed reload does not close the still-loaded core. Tracked in SOLR-18587, with a fix on the candidate branch.

On the patched head (23053d08816) the Java test phase and the integration tests passed in our run. The final binary-release step of that run did not produce a result, so we do not have a clean full gate. A headless browser check of the patched build (Dashboard, Security, Cloud, Collections, Logging) showed no console errors, no failed requests and no role message, with the stock standalone start.

We are not claiming RC3 is broken for users. The NFS hard-link issue may be specific to our host. Environment, commands and logs are in the receipts on this board (receipts/rc3-patched-smoke-run3-round-1-vm5.md, receipts/rc3-patched-test-phase-round-1-vm5.md).

## Review checklist

- Each number matches the receipt it cites (test counts, skip counts, dates, head hashes).
- The binary-release step is described as incomplete, not as passed.
- No ticket is cited for the js-client change. Confirm that nothing in the draft implies a ticket number for it.
- The SOLR-18588 close is not mentioned.
- Tone: findings, not a verdict on the release.
- Who sends it, and to which list, is the owner's call.
