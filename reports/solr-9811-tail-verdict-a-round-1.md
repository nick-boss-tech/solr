# SOLR-9811 tail verdict, batch A claim 6: CORRECTION, do not close yet

- Assignment: `assignments/overseer-tail-verdicts-a-round-1.md`, Claim 6.
- Claim: `claims/solr-9811-tail-verdict-a-phone-agent.md`.
- Ticket: SOLR-9811, "Make it easier to manually execute overseer
  commands", Open, unassigned, last updated 2019-06-08.
- Ticket ask: an expert-operator escape hatch for when SolrCloud gets
  into a bad state on election or recovery: manually publish a node as
  active or leader, instead of restarting services (expensive when
  many cores share a server). The thread's concrete cases: a replica
  physically present and serving queries but stuck in DOWN state, and
  a replica marked both LEADER and DOWN at once. The reporter (Scott
  Blum, 2016-11-29) fixed the DOWN case by hand-crafting a state
  update node into the Overseer queue via zk-shell, and asked for a
  tool for that.
- Deliverable type: correction report, not a closure draft. The batch
  rules require this when the ask is still live, and it is: no such
  tool exists. No Jira post, no close.

## Verdict

Do not close SOLR-9811 yet, on either ground:

1. No dedicated tool was added. What exists today is the same set the
   thread already discussed and found insufficient:
   - The FORCELEADER Collections API exists
     (`solr/core/.../handler/admin/api/ForceLeader.java`), but the
     thread records it never fixing the DOWN-marked replica case
     (Scott Blum, 2016-11-30: "I've tried FORCELEADER a few times
     but for me it never puts a replica erroneously marked as DOWN
     into an ACTIVE state").
   - The REQUESTRECOVERY CoreAdmin API exists
     (`solr/core/.../handler/admin/api/RequestCoreRecoveryAPI.java`),
     but the thread records it failing on the LEADER+DOWN combination
     ("Cloud state still says we are leader", 2016-12-02), and David
     Smiley reported the same class of failure.
   - The CLI offers raw ZK tools (`ZkCpTool`, `ZkLsTool`, `ZkMvTool`,
     `ZkRmTool`), which let an operator hand-write queue or state
     nodes. That is the same manual workaround Scott described in
     2016, not the dedicated tool the ticket asks for.
   The ticket was never updated with a fix (last update 2019), and
   nothing in the current code publishes an arbitrary replica state
   on operator demand.
2. Removal does not retire the ask; it renames it. The underlying
   need is an escape hatch for bad election/recovery states without a
   node restart. That need survives in every mode: in distributed
   mode there is no Overseer queue to hand-write into, so even the
   manual workaround changes shape, while stuck DOWN replicas and
   leader confusion remain possible. A closure draft citing
   SOLR-17293 would claim the Overseer's removal answers a need that
   exists independently of the Overseer. The ticket's own framing
   ("I don't care, just get it running") is mode-independent.

## What would close this ticket

- A dedicated operator tool or API for publishing a replica state
  (active/leader) on demand, with the expert-operator warnings the
  ticket itself requests; or
- A maintainer ruling that FORCELEADER plus REQUESTRECOVERY now cover
  the reported cases, with evidence (the thread says otherwise, and
  no such evidence is on the ticket); or
- An explicit won't-do decision that the escape hatch is too
  dangerous to expose. Any of these must be stated as what it is;
  none of them is "retired by removal".

## Sources checked

- Jira SOLR-9811 (full thread, quoted above), SOLR-17293 (Open
  umbrella), SOLR-14927 (Open), read live.
- `solr/core/src/java/org/apache/solr/handler/admin/api/ForceLeader.java`
  and `.../RequestCoreRecoveryAPI.java` (existence confirmed).
- `solr/core/src/java/org/apache/solr/cli/` ZK tools (raw ZK access
  only, no state-publication tool).
- Grep for a manual state-publication API or CLI command in
  `solr/core/.../handler/admin/` and `.../cloud/api/collections/`:
  none found; state publication exists only inside recovery and
  collection-command flows (e.g. `RequestApplyUpdatesOp`,
  `MigrateCmd`, `SplitShardCmd`), not as an operator tool.
- Limit: code read against the local main checkout at `e55d2e7bcb`
  (2026-09-29), not a fresh upstream fetch; the cited constructs are
  long-standing, but a re-read at PR time should confirm nothing moved.
