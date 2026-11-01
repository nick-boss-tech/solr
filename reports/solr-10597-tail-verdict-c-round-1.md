# Verdict: SOLR-10597, Overseer election endless loop on NodeExists /overseer_elect/leader

Verdict: STILL LIVE, promote to implementation, not closure. The exact
path the report describes is still in current main, and the retry bound
the earlier keep-open record left untraced is now traced: the NodeExists
retry has no cap, no backoff, and no stale-leader reconciliation, and
there is one shape in which it loops without bound plus a retrigger
shape that reproduces the report's endless log flood in a multi-node
cluster. Nothing closes without a maintainer's or Nick's go.

Ticket: https://issues.apache.org/jira/browse/SOLR-10597 (2017, reporter
David Smiley, Open, Major, last updated 2019-06-08). The description
says a 5-node cluster reached a state where each node reported it was
going to be the leader and then failed to create
/overseer_elect/leader since it was already there, generating tons of
logs without end; Solr stayed available, and a full cluster restart
(except ZooKeeper) cleared it. Smiley notes the cluster ran in
Docker/Kontena, where host name to IP (DNS) can change frequently.
The ticket has no comments, no links, no fix version, and no assignee,
so there is no recorded fix or maintainer read to check against.

## Code read (current main as checked)

Checked against the local main checkout at e55d2e7bcb (2026-09-29).
The solr/core tree is unmodified there; the checkout is shallow
(363 commits back to June 2026), so no whole-history claim is made
here. The prior pool record at the pinned head found no commits
naming SOLR-10597, and the ticket's own empty link list agrees.

The failure path, step by step:

- OverseerElectionContext.runLeaderProcess
  (solr/core/src/java/org/apache/solr/cloud/OverseerElectionContext.java:60-64)
  logs "I am going to be the leader" and then creates the leader
  registration with zkClient.makePath(leaderPath, ...,
  CreateMode.EPHEMERAL). That log line is the report's signature,
  verbatim.
- makePath fails on an existing node: SolrZkClient.makePath with
  failOnExists true (the default for this overload,
  solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/SolrZkClient.java)
  throws KeeperException.NodeExistsException.
- LeaderElector.checkIfIamLeader
  (solr/core/src/java/org/apache/solr/cloud/LeaderElector.java:130-133)
  catches NodeExistsException, logs "node exists", and calls
  retryElection(context, false). There is no counter, no sleep, and
  no check of who owns the existing leader node.
- LeaderElector.retryElection (LeaderElector.java:372-389) cancels
  the node's watcher, calls context.cancelElection(), which deletes
  the node's own sequential election node
  (solr/core/src/java/org/apache/solr/cloud/ElectionContext.java:54-58),
  closes the Overseer object, copies the context, and rejoins the
  election with a fresh sequential node at the tail of the line.
- Nothing anywhere in this path reads /overseer_elect/leader to ask
  whether its owner is alive, in line, or gone; nothing deletes a
  stale leader registration; and nothing watches the leader node
  itself. The only watchers in the election watch predecessor
  sequential nodes. Overseer.java:457 and :595 only read the leader
  node to learn who the leader is.

## The bound, traced

Two shapes, and the distinction matters for the fix:

1. Unbounded loop, single node in line. If the retrying node rejoins
   and is still the lowest sequential node (a one-node cluster, or
   every other node out of line), checkIfIamLeader immediately runs
   the leader process again, hits NodeExists on the same stale
   leader node again, and retries again, with no sleep and no cap.
   That is a true endless loop in current code. The stale node can
   even be the node's own earlier ephemeral registration surviving
   under a suspended-but-unexpired session, which fits the report's
   DNS-churn note: a node rejoining under a changed name or a new
   session meets its own old registration.
2. Bounded cascade per trigger, retriggered without bound,
   multi-node line. With several nodes in line, one stale leader
   node drives a single pass: the lowest node tries, fails, and
   deletes its sequential node on retry, which fires the next
   node's watcher; that node is now lowest, tries, fails, retries,
   and so on down the line. After the last node retries and
   rejoins at the tail, the election code itself has no further
   trigger, so one stale node alone does not loop forever in this
   shape. But triggers recur: ZkController.onReconnect
   (solr/core/src/java/org/apache/solr/cloud/ZkController.java:455,
   election rejoin at 482-491) builds a fresh OverseerElectionContext
   and rejoins the election on every reconnect, and SOLR-18301
   records that the Curator migration made election run on every
   reconnection rather than only on session expiry. Each rejoin by
   a node that lands lowest re-attempts the leader node and
   re-fires the cascade. Under flapping sessions or DNS churn, the
   log flood runs without end, which is what the report describes.

Contrast: joinElection does cap its own retries (tries > 20, with
a 50 ms sleep, LeaderElector.java:277 and :290), but those caps
cover ConnectionLoss and NoNode while creating the sequential
node, not the NodeExists path on the leader node. The leader-node
path is the one place in the election with no bound at all.

## Scope note

The fix shape is a verdict note, not a design: bound the NodeExists
retry (cap plus backoff), or reconcile the stale registration
before retrying (check the leader node's owner and clear it when
its owner is gone or not in line), or make waiting nodes watch the
leader node instead of churning sequential nodes. Any of the three
closes the loop; the ticket needs an implementation assignment to
pick one, plus a test that plants a stale leader node and asserts
the election settles instead of churning.

## Relation to SOLR-18301 and to the earlier record

Not the same mechanism, so this verdict does not stop on the
boundary note in the batch C assignment. SOLR-18301 (Open, reporter
Lucas Kot-Zaniewski, updated 2026-10-08) is about the election
running on every reconnection after the Curator migration and not
converging after a ZK disconnect; its fix would change when the
election runs. SOLR-10597 is about what happens when the election
runs and the leader node already exists. The two interact (18301's
reconnect trigger re-fires 10597's cascade), but fixing 18301's
trigger does not bound 10597's retry, and 10597's fix does not
settle 18301's convergence question. The two stay separate items.

This verdict advances the 2026-10-20 pool keep-open record on fork
branch solr-10597-draft-open (5df656a1fc), which kept the ticket
open but explicitly left the retry bound untraced ("whether
retryElection can loop without a bound under a stale-leader ZK
state was not traced to the end in this pass"). The trace above
settles that gap: unbounded in the single-node-in-line shape,
bounded-per-trigger but retriggered without bound in the
multi-node shape. The keep-open conclusion stands; the open
question behind it is now answered.
