# SOLR-8430 - hypothetical reproduction (nothing was compiled or run)

JIRA: replication throttling (`maxWriteMBPerSec`, SOLR-6485) is applied per request, so N replicas recovering at
once pull N * maxWriteMBPerSec. Audit note said "fixed in 5.5/6.0 (fix version set)", but nothing was committed:
on `upstream/main` each `ReplicationAPIBase.DirectoryFileStream` builds its own `RateLimiter.SimpleRateLimiter`.

## Change
`ReplicationAPIBase.rateLimiterFor(double)` returns one `SimpleRateLimiter` per distinct configured rate from a
static `ConcurrentHashMap` (rate 0 maps to the old "no throttle" `Double.MAX_VALUE`). The stream constructor uses
it. All streams with the same rate now draw from one budget (a node-wide limit, which is the "per-node" idea from
the ticket's last sentence). Lucene's `SimpleRateLimiter.pause` is thread safe.

## Test (guessed)
`ReplicationRateLimiterTest`: same rate gives the same instance; different rates differ; zero is unthrottled.
It does not drive concurrent HTTP replication; `TestReplicationHandler.testRateLimitedReplication` (measures one
slave) should still hold and would be the place for a two-slave aggregate check.

## Guesses to verify first
- `RateLimiter.SimpleRateLimiter(double)` is public and `getMBPerSec()` returns the configured value.
- `testRateLimitedReplication` computes expected duration from one stream; sharing the limiter must not slow a
  single stream (it should not: one stream owns the whole budget).
- Design pick: shared across cores of a node (key is the rate only). A per-core key would be the narrower option.
- A very long pause for one stream now delays others at that rate (that is the point), so aggregate recovery
  time rises when many replicas recover together.

## Fail-before
Not applicable to the unit test as a behaviour proof; reverting the helper makes `assertSame` fail to compile or fail.
