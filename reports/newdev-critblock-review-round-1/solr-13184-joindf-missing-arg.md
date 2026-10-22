# Review: SOLR-13184, branch solr-13184-joindf-missing-arg (unit 29)

Head reviewed: 6bb01a08cd8ddd19856b39e40a16481f659b2e00. Base: c8ab71430f. Ticket: SOLR-13184, "NPE due to missing input checking in ValueSourceParser".

## Pass 1

Review (2026-10-22, vm4):

- Ticket fit. A `joindf` call with a missing query argument reaches `JoinDocFreqValueSource` with a null `qf` and fails with an NPE. The branch checks both arguments after parsing and throws a 400 `SolrException`. The change fits the ticket.
- Scope. One guard in `ValueSourceParser.java` (four lines) and one new test class. Nothing else.
- Finding, minor: no changelog file. The repo convention for a fixed bug is a `changelog/unreleased` entry. Recorded, not repaired (a new file is outside the repair scope).
- Finding, minor: the test covers a missing query argument (`$x` unresolved) and a single argument. A call with no arguments at all is not pinned. Recorded, not repaired.
- Proof leg: on base the test fails with the ticket's NPE ("this.qfield" is null). The proof is real.
- Repairs: none.

Regate: `gates/SOLR-13184-review-pass-1.md`, GATE GREEN at 6bb01a08cd on vm4.

## Pass 2

Review (2026-10-22, vm4), same head 6bb01a08cd. No new findings against the head. The pass 1 minor findings stand.

- Repairs: none.

Regate: `gates/SOLR-13184-review-pass-2.md`, GATE GREEN at 6bb01a08cd on vm4.

