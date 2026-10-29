# Assignment: premise review for SOLR-18600 PR (vm2, round 1)

Status: OPEN. Review first. Nothing has been posted to GitHub, Jira, or the mailing list, and no PR is open.

## Ask

Check whether the premises behind the SOLR-18600 PR hold, and decide the open questions below before anyone posts. The scope, route, and wording are open.

## Background

SolrJ 10.1.0 removed `org.apache.solr.client.solrj.impl.HttpSolrClientBase` and `HttpSolrClientBuilderBase` (SOLR-17968, PR #3829, merged 2026-05-18). The removal breaks code compiled against 10.0.0 at runtime, with `NoSuchMethodError`, when it runs on 10.1.0 without recompiling. Recompiling fixes it. SOLR-18600 tracks this.

## Heads and state

- Ticket: SOLR-18600, Open, affects 10.1, linked Related to SOLR-17968. Its own expected result is either that old bytecode runs on 10.1.0, or that the 10.1 upgrade notes call out the need to recompile.
- Fork branch: `solr-18600-http-abi-restore-submit`, head `c40665af8d6b`, one commit on `branch_10x` base `a336bf871b2c`.
- Board receipt: `receipts/SOLR-18600.md`, commit `86975eea718` dated 2026-10-27. The raw logs are on vm2, not on the board.
- PR draft: `gates/logs/vm2/solr18600-pr/body.md` on vm2. Target `branch_10x`. Not opened.
- main (`e59ce3c97d`): the same change conflicts with SOLR-17968 in `HttpSolrClient.java`. A main version needs a port.

## What the branch changes

Source, on `branch_10x`:
- Added: `HttpSolrClientBase.java`, `HttpSolrClientBuilderBase.java` (top level, released members).
- Changed: `HttpSolrClient.java` (now extends `HttpSolrClientBase`), `SolrRequest.java` (javadoc reference `processWithBaseUrl(HttpSolrClientBase, ...)`), `CloudSolrClient.java` is not changed on the branch but holds the methods the Cloud consumer calls.
- Changed: `CloudHttp2SolrClient.java`, `CloudSolrClient.java`, `ConcurrentUpdateBaseSolrClient.java`, `HttpClusterStateProvider.java`, `LBAsyncSolrClient.java`, `LBSolrClient.java`, and `solrj-zookeeper` `SolrClientNodeStateProvider.java`.

The branch also adds a changelog entry, an upgrade note, and tests (`HttpSolrClientBinaryCompatTest`, `HttpJettySolrClientBinaryCompatTest`, `CloudSolrClientCacheTest`). Its own counts are in the receipt.

## Evidence

1. Break is real. The SOLR-18600 consumer, compiled against released 10.0.0, exits 1 on published 10.1.0 (jar on Maven Central) with `NoSuchMethodError`. The same source recompiled against 10.1.0 runs.
2. Base control on `branch_10x` (`a336bf871b2c`): the same consumer fails with `NoSuchMethodError` on `HttpJdkSolrClient$Builder.withConnectionTimeout`.
3. Earlier fail-before on the pre-Jackson base (SOLR-18325, `f45b38d5f6c0`): same `NoSuchMethodError` before any HTTP call (`receipts/SOLR-18325-binary-attribution-round-1.md`). `f45b38d5f6c0` is an ancestor of `a336bf871b2c`. No SolrJ source file differs between them; the only core change is `OtelRuntimeJvmMetrics.java` (+6).
4. Probe, restoring only `HttpSolrClientBuilderBase`: does not compile on `branch_10x`, because it needs `HttpSolrClientBase`.
5. Probe, restoring `HttpSolrClientBase` and `HttpSolrClientBuilderBase` (with one javadoc `@see` line removed, probe only): compiles, but the HttpJdk consumer still fails with `NoSuchMethodError`. The nested builder hierarchy must change as well.
6. Bisect, HttpJdk consumer (the SOLR-18600 consumer):
   - All ten changed source files: `CONSUMER OK`.
   - Core four (`SolrRequest`, `HttpSolrClient`, `HttpSolrClientBase`, `HttpSolrClientBuilderBase`): `CONSUMER OK`.
7. Cloud builder consumer, compiled against 10.0.0, calling `CloudHttp2SolrClient.Builder.withHttpClientBuilder` and `withHttpClient(null)` in separate runs:
   - Base: fails, class missing.
   - Core four: fails, `NoSuchMethodError` on `CloudSolrClient$Builder`.
   - All ten: passes both.
8. The 10.0.0 public methods that name the restored types are in `CloudSolrClient$Builder` (`withHttpClient`, `withHttpClientBuilder`, `withInternalClientBuilder`) and `CloudHttp2SolrClient.getHttpClient`. `LBSolrClient$Builder` is generic over `HttpSolrClientBase`. LB builders are not tested.

Probe artifacts (vm2, not on the board): `gates/logs/vm2/solr18600-probe/` (`steps.txt`, `out/run-*.log`, `out/run2-*.log`, consumer sources). The scratch clone `/workspace/worktrees/pr5253-U-up` still has the last probe's edits and needs a reset before reuse.

## Premises to review

P1. The break matters for users. Evidence 1 supports this.

P2. Restoring the classes is the right fix. The ticket accepts restore or an upgrade-note warning. The upgrade-note route needs no API change. Not decided.

P3. The removal was unintended. SOLR-17968 was a deliberate rename. The PR discussion (dsmiley, gerlowskija, igiguere, about 13 comments) covers names and packages only, and does not mention binary compatibility for compiled consumers. The ticket says the upgrade notes don't mention the effect.

P4. Restoring `HttpSolrClientBase` is safe. It is marked `@lucene.internal` in 10.0.0's Javadoc. The PR must address this.

P5. The full ten-file scope is needed. Evidence 6 and 7 show the four core files are enough for the HttpJdk consumer, and the Cloud consumer needs more. Which Cloud and LB files are needed is unknown. The six Cloud and LB files have not been narrowed.

P6. The binary requirement applies upstream. The owner's binary-compatibility requirement is for SOLR-18325's client, not an established upstream requirement. Nothing in the docs or PR #3829 discussion states a SolrJ binary-compatibility policy.

P7. The fix belongs on `branch_10x` first. Main needs a separate port (see Heads and state). Whether a 10.1.x backport is wanted is open. 10.1.0 is published with the break.

## Open questions for the reviewer

1. P2: restore, or document recompilation in the 10.1 upgrade notes, or both?
2. P4: is a restored `@lucene.internal` class acceptable?
3. P5: narrow the six Cloud and LB files, or accept all ten?
4. P6 and P3: is there a maintainer decision on SolrJ binary compatibility within 10.x?
5. P7: target `branch_10x`, main, or both? Is a 10.1.x backport wanted?
6. Where should the question go: the PR's choice section, the ticket, or the mailing list? Nothing has been posted anywhere.

## Limits

- The verification date in the receipt is the commit date (2026-10-27), not a confirmed run date. The base-failure run is the consumer, not `HttpSolrClientBinaryCompatTest`, which does not compile on base.
- Probe-only edit: one `@see` line removed in the two-class probe. It is not in the branch.
- The Cloud consumer covers `CloudHttp2SolrClient.Builder` only. The LB builders and client-side use of `getHttpClient()` are not tested.
- No GitHub, JIRA, or mailing list action taken. No PR opened.
