# Review: SOLR-18209, branch solr-18209-list-of (unit 38)

Head reviewed: c8fef7c4cd970fb84cae835a7a2f7a497b7dd4fc. Base: c8ab71430f. Ticket: SOLR-18209, "Replace Arrays.asList with List.of for immutable static final fields".

## Pass 1

- Ticket fit. The ticket asks to replace `Arrays.asList` with `List.of` for immutable static final fields. The branch changes three production files (`NodeHealth.java`, `ContentStreamBase.java`, `SchemaDesignerConfigSetHelper.java`) and nothing else. The change fits the ticket.
- Behaviour difference, minor, latent: `List.of` rejects `null` in `contains`, and `Arrays.asList` returned false. In `NodeHealth.java:265`, `UNHEALTHY_STATES.contains(c.getLastPublished())` would now throw NullPointerException if the last published state were null. `CloudDescriptor` initialises it to ACTIVE, and the only setter caller (`ZkController.java:1959`) passes the state from `publish`, so the null path is not reached in this tree. Recorded, not repaired (a null guard is a behaviour choice for the owner).
- Finding, minor: the schema designer change (`SchemaDesignerConfigSetHelper.java`) is not covered by the focused classes in this gate. Recorded.
- Scope check: the `ContentStreamBase` lists are only read, and none of the three branches mutates a list, so the immutable type does not change behaviour on the paths in the diff. Read in code.
- Repairs: none.

Regate: `gates/SOLR-18209-review-pass-1.md`, GATE GREEN at c8fef7c4cd on vm4.

## Pass 2

Review (2026-10-22, vm4), same head c8fef7c4cd. - Pass 1 minor findings, unchanged: the null-state path in `NodeHealth.contains` (latent); the schema designer change has no focused coverage.
- Repairs: none.

Regate: `gates/SOLR-18209-review-pass-2.md`, GATE GREEN at c8fef7c4cd on vm4.

## Pass 3

Review (2026-10-22, vm4), same head c8fef7c4cd. - No new findings against the head. The pass 1 minor findings stand: the null-state path in `NodeHealth.contains` (latent); the schema designer change has no focused coverage.
- Repairs: none.

Regate: `gates/SOLR-18209-review-pass-3.md`, GATE GREEN at c8fef7c4cd on vm4.

Final head: c8fef7c4cd970fb84cae835a7a2f7a497b7dd4fc. Unit 38 DONE with the three pass sections.
