# Review: SOLR-18481, branch solr-18481-xff (unit 41)

Head reviewed: 8f687ca04de6cfb6baa6075cb69fcb03737de918. Base: c8ab71430f. Ticket: SOLR-18481, "Proxied requests should use X-FORWARDED-FOR header".

## Pass 1

- Ticket fit. The ticket asks that proxied requests carry X-Forwarded-For. The branch sets the header on the proxied request in `HttpSolrProxy` and appends the client address to any existing hops through a small `forwardedFor` helper. The helper's behaviour matches that, including the empty and null cases (read in code and in the five tests).
- Finding, major: the five tests cover the `forwardedFor` helper only. None sends a request through the proxy and checks that the upstream receives `X-Forwarded-For`. The header being set on the wire is not shown by the branch. Recorded, not repaired (a new end-to-end test is outside the repair scope).
- Finding, major: the proof leg is inconclusive. The test calls the helper, so base does not compile; the proof does not show the header behaviour.
- Read in code: the client's own `X-Forwarded-For` values are read by the helper and then the joined value replaces the copied header (`put`), so there is no duplicate header.
- Repairs: none.

Regate: `gates/SOLR-18481-review-pass-1.md`, GATE GREEN at 8f687ca04d on vm4.

## Pass 2

Review (2026-10-23, vm4), same head 8f687ca04d. - Pass 1 major findings, unchanged and confirmed in the test file: no test sends a request through the proxy, so the upstream header is not checked; the proof is inconclusive.
- Repairs: none.

Regate: `gates/SOLR-18481-review-pass-2.md`, GATE GREEN at 8f687ca04d on vm4.
