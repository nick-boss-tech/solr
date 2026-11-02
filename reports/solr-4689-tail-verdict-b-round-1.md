# SOLR-4689 tail verdict, batch B claim 1: CLOSURE DRAFT (superseded by landed work)

- Assignment: `assignments/overseer-tail-verdicts-b-round-1.md`, Claim 1.
- Claim: `claims/solr-4689-tail-verdict-b-phone-agent.md`.
- Ticket: SOLR-4689, "Overseer should assign node names by default for
  the collections API split command", Open, last updated 2013-04-08,
  zero comments. Its whole description: "Integrate Marks` changes for
  delegating node name assignment to Overseer [SOLR-4655] for the
  SPLITSHARD collections API."
- Deliverable type: closure draft. The cited work landed, the split
  path was rewritten around it, and no delegation gap remains. Draft
  only; no Jira post, no close, pending a maintainer's or Nick's
  explicit go.

## Verdict

Close as superseded by landed work. The check the assignment requires
comes out in favor of closure on all three points:

1. SOLR-4655 landed. It is Closed: the generic node-name work went in
   with SOLR-4916 in July 2013 (Mark Miller's comments and the
   bulk-close of resolved 4.4 issues on the ticket). The 4655 thread
   itself records the split case being tested: Anshum Gupta
   (2013-05-01) ran shard splits with generic names and reported it
   working "just about fine out of the box", noting "We don't seem to
   be using the hostname in the SplitShard code."
2. The current split path uses the generic assignment, not
   hostname-based names. New replica positions for a split are chosen
   by the `Assign` strategy (`SplitShardCmd.java:551-563`), core
   names are built by `Assign.buildSolrCoreName`, and core node names
   come from the generic counter mechanism,
   `Assign.assignCoreNodeName`, which returns `core_node<N>` via a
   ZK-counter increment (`Assign.java:132-134`). That method is what
   the overseer mutation path uses (`ReplicaMutator.java:339`,
   `SliceMutator.java:88`), i.e. the ADDREPLICA flow the split
   issues for its sub-shard replicas. There is no hostname-derived
   node naming left in the split path.
3. The split API has been rewritten since 2013, which is what removed
   any remaining delegation question. The current `SplitShardCmd`
   (with distributed-state-updater support, placeholder replicas in
   DOWN state, and the parent-leader wait flow) bears no resemblance
   to the 2013 `OverseerCollectionProcessor` split code the ticket
   was written against. The "delegation" framing is obsolete: node
   assignment now happens inside the command/mutator path itself,
   which runs under the Overseer in Overseer mode and distributed
   otherwise. There is no separate delegation step left to integrate.

## Suggested closure text (draft, not posted)

> SOLR-4655 landed in 2013 (with SOLR-4916) and the split path was
> verified against generic node names at the time. The current
> SPLITSHARD implementation assigns replica positions through the
> `Assign` strategy and core node names through the generic
> `core_node<N>` counter (`Assign.assignCoreNodeName`), with no
> hostname-based naming in the split path. The delegation this ticket
> asked to integrate is the universal mechanism today, so closing as
> superseded by landed work.

## Sources checked

- Jira SOLR-4689 (full content: one-line description, zero comments)
  and SOLR-4655 (Closed; 2013 thread including the split test
  comment), read live.
- `solr/core/src/java/org/apache/solr/cloud/api/collections/SplitShardCmd.java:551-563`
  (Assign strategy for split replica positions),
  `:452-458` (sub-shard leader wait flow).
- `solr/core/src/java/org/apache/solr/cloud/api/collections/Assign.java:132-134`
  (generic `core_node<N>` assignment).
- `solr/core/src/java/org/apache/solr/cloud/overseer/ReplicaMutator.java:339`,
  `SliceMutator.java:88` (mutator-path use of the generic assignment).
- Limit: code read against the local main checkout at `e55d2e7bcb`
  (2026-09-29), not a fresh upstream fetch; the split flow was not
  traced end-to-end through every coreNodeName minting site, only
  through the assignment and mutator paths cited above. A maintainer
  confirming no hostname-based naming survives in any split branch
  is the remaining check before posting.
