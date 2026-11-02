# SOLR-7872 tail verdict, batch B claim 5: CORRECTION, do not close yet

- Assignment: `assignments/overseer-tail-verdicts-b-round-1.md`, Claim 5.
- Claim: `claims/solr-7872-tail-verdict-b-phone-agent.md`.
- Ticket: SOLR-7872, "Move Overseer related classes in cloud package
  to cloud.overseer package", Open, Major, reporter and assignee Mark
  Miller, last updated 2015-08-05. No description and no comments;
  the title is the whole ask.
- Deliverable type: correction report, not a closure draft. The batch
  rules require this when the cited superseding work never landed or
  covers only part of the ask, and that is exactly this ticket: the
  move never happened, and the removal that was supposed to make it
  pointless has not happened either. No Jira post, no close.

## Verdict

Do not close SOLR-7872 as superseded. The census premise ("a cosmetic
move that removal makes pointless") fails on both of its legs as a
statement about landed work:

1. The move never happened. The classic Overseer classes still sit
   in `org.apache.solr.cloud`, not in `cloud.overseer`, and they are
   still live: `ZkController` still constructs an `Overseer`, and
   `OverseerTaskProcessor` is still referenced by the collection API
   path.
2. The removal has not happened either. SOLR-14927 ("Remove
   Overseer") is Open under the live umbrella SOLR-17293
   ("Decentralized Cluster Processing as default", Open), with one
   open subtask, SOLR-16542. Removal is a direction, not a landed
   fact, so it cannot yet retire the ask.

There is also a trap in the current tree that a closure draft must
not fall into: a `cloud.overseer` package DOES exist today, so a
quick check could conclude the ticket is done. It is not the ticket's
move. The package holds only newer machinery from the distributed
state work (`ClusterStateMutator`, `CollectionMutator`,
`NodeMutator`, `ReplicaMutator`, `SliceMutator`, `OverseerAction`,
`ZkStateWriter`, `ZkWriteCommand`), not the moved classics. The
split is now confusing in exactly the way the ticket wanted to fix:
new Overseer-adjacent classes live in `cloud.overseer` while the ten
classes the ticket named still live one level up in `cloud`.

## Current code, verified against the local main checkout at e55d2e7bcb

- Still in `org.apache.solr.cloud` (10 files): `Overseer.java`,
  `OverseerTaskProcessor.java`, `OverseerTaskQueue.java`,
  `OverseerCollectionConfigSetProcessor.java`,
  `OverseerConfigSetMessageHandler.java`,
  `OverseerElectionContext.java`, `OverseerMessageHandler.java`,
  `OverseerNodePrioritizer.java`, `OverseerSolrResponse.java`,
  `OverseerSolrResponseSerializer.java`.
- Still live: `ZkController.java` constructs the `Overseer`, and
  `OverseerTaskProcessor` is imported by the collection API
  (`OverseerStatusCmd`, `DistributedCollectionConfigSetCommandRunner`).
- In `org.apache.solr.cloud.overseer` (8 files, none of them the
  classics): the mutators and `ZkStateWriter` listed above.

## What the record does and does not settle

- It does not settle whether the move is worth doing. As a pure
  cosmetic rename it is churn: every reference to the ten classes
  across core, solrj, and tests would move, for code the removal
  program intends to delete. Against that, the classes are still
  the live path, removal has no landing date on the record, and
  the current half-populated package split is a real readability
  wart. That tradeoff is a maintainer call.
- If maintainers do want this ticket closed, the honest ground is
  "won't do: cosmetic churn on code slated for removal", not
  "superseded": nothing superseded it, and the closure draft
  should say so rather than citing the removal as a landed fact.
  That wording needs their go, like any closure draft in this
  batch.

## Limits

- Code read only, no build, per the batch rules. The ticket has no
  description or comments, so the 2015 scope is inferred from the
  title and from which classes were Overseer classes in the cloud
  package at the time; the ten files above are that set as it
  survives today.
- The local checkout head e55d2e7bcb dates to 2026-09-29. A
  re-check against current upstream main before any closure draft
  is cheap and worth doing, but a move of ten live classes would
  be a visible commit, and none is on the record.
