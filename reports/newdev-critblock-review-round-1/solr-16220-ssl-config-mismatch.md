# Review: SOLR-16220, branch solr-16220-ssl-config-mismatch (unit 32)

Head reviewed: d7db0784a19500aa5be380ccd8e88600084c2b8c. Base: c8ab71430f. Ticket: SOLR-16220, "Documentation leads to NPE Missing SslContextFactory for SolrJ client".

## Pass 1

- Ticket fit. The ticket reports an NPE when an https URL is used with a non-SSL SSL configuration. The branch throws an `IllegalArgumentException` with a clear message in `createHttpClient`, before any executor is created. Owner decision row 5 is option A, which this change follows. The change fits the ticket and the decision.
- Scope. A guard of 12 lines in `HttpJettySolrClient.java`, one test class with two tests, and a changelog file. The move of `sslConfig` above the executor creation is needed for the guard and does not change behavior.
- Check of the leak path. The guard runs inside the constructor before `ObjectReleaseTracker.track(this)` (`HttpJettySolrClient.java:179`), so a rejected construction is not left tracked. The base proof's ObjectTracker failures come from base, not from the branch.
- Finding, minor: the guard is limited to the path where the builder creates its own client. A shared `httpClient` passed in through the builder skips this check, so the mismatch is not reported in that case. Recorded, not changed (a behavior change beyond the ticket).
- Finding, minor: the test asserts that an exception is thrown. Whether the message names the URL and the fix is not asserted. Recorded, not repaired.
- Repairs: none.

Regate: `gates/SOLR-16220-review-pass-1.md`, GATE GREEN at d7db0784a1 on vm4.

## Pass 2

Review (2026-10-22, vm4), same head d7db0784a1. - Pass 1 finding, minor (shared httpClient skips the check), confirmed in code: when the builder passes its own `httpClient`, the constructor takes the `builder.httpClient` branch (`HttpJettySolrClient.java:148-155` at this head) and `createHttpClient` is never called, so the SSL-mode guard does not run. Recorded, not changed.
- Pass 1 finding, minor (message not asserted), unchanged. Not repaired.
- Repairs: none.

Regate: `gates/SOLR-16220-review-pass-2.md`, GATE GREEN at d7db0784a1 on vm4.

## Pass 3

Review (2026-10-22, vm4), same head d7db0784a1. - No new findings against the head. The pass 1 and pass 2 minor findings stand: the SSL-mode guard does not run when the builder passes its own `httpClient` (confirmed in code); the test does not assert the message.
- Repairs: none.

Regate: `gates/SOLR-16220-review-pass-3.md`, GATE GREEN at d7db0784a1 on vm4.

Final head: d7db0784a19500aa5be380ccd8e88600084c2b8c. Unit 32 DONE with the three pass sections.
