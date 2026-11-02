# SOLR-13007 tail verdict, batch A claim 5: CORRECTION, do not close yet

- Assignment: `assignments/overseer-tail-verdicts-a-round-1.md`, Claim 5.
- Claim: `claims/solr-13007-tail-verdict-a-phone-agent.md`.
- Ticket: SOLR-13007, "Use javabin instead of JSON to send messages to
  overseer", Open, unassigned, last updated 2019-06-08.
- Ticket ask: Overseer messages are ephemeral and readability is not a
  big issue, so send them in javabin to reduce payload size and speed
  processing. The thread narrows the scope to the messages offered to
  the Overseer queues (cluster state in ZK stays JSON), notes the read
  side already accepts both formats, and ends with the fix unmade:
  Noble Paul (2019-01-17) says the fix is easy but was held for
  backcompat, with SOLR-13006 committed first so that nodes on 7.6+
  can read both; Tomas Fernandez Lobbe (2019-01-15) asks for
  benchmarks proving a big speedup before accepting the readability
  cost of unreadable queue contents during a prod outage.
- Deliverable type: correction report, not a closure draft. The batch
  rules require this when the ask is still live, and it is: messages
  still travel as JSON today. No Jira post, no close.

## Verdict

Do not close SOLR-13007 yet, on either ground the batch offers:

1. NOT fixed by later work. The read side does accept javabin
   (`ZkNodeProps.load`: a leading byte of 2 means javabin, anything
   else JSON), which is the SOLR-13006 backcompat enabler the thread
   describes. But no sender switched. The queue offer paths still
   serialize as JSON: `DistributedQueue.offer(MapWriter)` defaults to
   `offer(Utils.toJSON(mw))`
   (`solr/solrj-zookeeper/.../DistributedQueue.java:43-44`), and the
   state-update offer sites build `MapWriter` messages
   (`ZkController.java:1901,2066`) that flow through that default.
   A grep for `JavaBinCodec` marshaling across `solr/core/.../cloud/`
   production code finds no overseer-message sender using it. So the
   ticket's ask, sending messages in javabin, is unimplemented, not
   fixed.
2. NOT retired by removal yet. In the overseer-disabled mode there are
   no Overseer queue messages at all, so the ask would be moot there;
   but that mode is opt-in, not default (`ZkController.java:383`,
   `solr.cloud.overseer.enabled` defaults to `true`), and the removal
   umbrella SOLR-17293 is still Open. A closure citing removal today
   would cite an unlanded default.

## What would close this ticket

One of three, each a decision rather than a discovery:

- Implement it: switch the queue offer path to javabin (the backcompat
  condition Noble named, all nodes on 7.6+, has long been satisfied
  in supported versions), with the benchmark Tomas asked for, since
  the claimed 20-30% encode/decode gain on small messages was never
  demonstrated in the thread beyond Noble's own assertion.
- Close as won't-do: a maintainer rules the gains unproven and the
  readability cost during outages decisive. That is a legitimate
  closure ground, but it must be stated as a tradeoff decision, not
  as retirement by removal or as already-fixed.
- Wait for SOLR-17293: when decentralized processing becomes the
  default (or the Overseer is removed), there are no Overseer queue
  messages left to re-encode, and the ticket retires with the queue.

## Sources checked

- Jira SOLR-13007 (full thread, quoted above), SOLR-17293 (Open),
  SOLR-14927 (Open), read live.
- `solr/solrj/src/java/org/apache/solr/common/cloud/ZkNodeProps.java:84-93`
  (load accepts javabin or JSON).
- `solr/solrj-zookeeper/.../DistributedQueue.java:41-44`
  (offer(MapWriter) defaults to JSON).
- `solr/core/src/java/org/apache/solr/cloud/ZkController.java:1901,2066,2627`
  (MapWriter offer sites), `:383` (overseerEnabled default true).
- Grep for javabin marshaling senders in `solr/core/.../cloud/`
  production code: none for overseer messages.
- Limit: code read against the local main checkout at `e55d2e7bcb`
  (2026-09-29), not a fresh upstream fetch; the cited constructs are
  long-standing, but a re-read at PR time should confirm nothing moved.
