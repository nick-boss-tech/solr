# SOLR-14346 premise, Critical batch 2, unit 10: REPRODUCED

Verdict: REPRODUCED on current main. An LB client over root base
URLs (no collection, exactly the ticket's configuration) never
revives a restarted server: the server comes back up and answers
direct queries, but the zombie check leaves it in the zombie map
indefinitely, so it keeps receiving no LB queries. The
withDefaultCollection control revives promptly, which isolates
the deciding variable to the collection-less zombie check.

Jira (re-read this run): Open, Critical, reporter maoxiajun, last
updated 2021-12-11. Summary: "Solr fails to check zombie server in
LbHttpSolrClient". Ticket steps: LbHttpSolrClient over
http://host1:8983/solr and http://host2:8983/solr, shut host2 down
with queries running, restart host2, wait 1 minute or more, host2
receives no queries. The 2020 comment blames checkAZombieServer
for sending its check without a core name; the 2021 comment says
putting a collection name in the base URL mitigates it.

## Probe

Scratch probe only, never committed, fresh worktree at batch base
83d560030fde (apache main), seed 14346C0FFEE14346, BUILD
SUCCESSFUL, 1 test, 0 failures, fresh JUnit XML
2026-11-01T22:57:21Z (copy
~/workspace/tools/14346-premise-TEST-Solr14346PremiseProbeTest.xml;
log ~/workspace/tools/14346-premise-probe.log).

Test: Solr14346PremiseProbeTest (SolrCloudTestCase), 2-node
MiniSolrCloudCluster, collection test14346 (1 shard, 2 replicas,
so the live node serves while node 2 is down). Two phases, each
with its own LBJettySolrClient over the nodes' root base URLs and
a 1-second alive-check interval:

- TICKET-CASE (no default collection, the ticket's shape):
  baseline LB query OK; node 2 stopped; after 2 LB queries node 2
  is zombie (map: [http://127.0.0.1:40687/solr]); node 2 restarted
  on the same base URL and confirmed answering direct queries;
  after up to 15 seconds of 1-second alive checks the zombie map
  still contains node 2 (STILL_ZOMBIE=true); final LB query still
  OK via node 1 only.
- CONTROL (same client with withDefaultCollection test14346):
  identical kill/restart cycle; after the restart the zombie map
  is empty (STILL_ZOMBIE=false), i.e. the server revived.

## Mechanism

Current main's checkAZombieServer (LBSolrClient.java) sends the
`*:*` rows=0 probe through doRequest with the endpoint's core if
any, else the client's default collection, else null. With root
base URLs and no default collection the probe goes out with no
collection, so it cannot reach a real core handler and its
status==0 revive condition is never met; every check just bumps
failedPings. The control leg proves the same check revives as
soon as a collection is supplied. This matches both ticket
comments: the 2020 "without a core name" diagnosis and the 2021
collection-in-base-URL mitigation.

## Limits

Single cluster shape: 2 nodes, 1 shard, 2 replicas, cloud-minimal
configset, HTTP (non-SSL) Jetty nodes. The probe asserts the
revival outcome, not the exact HTTP status of the failed probe
request; the collection-less request path follows from the code
read above. The ticket's 1-minute wait is covered by the
mechanism (the check fails identically on every interval), not
by waiting a full minute.

## Next step

Per the queue rule, the next unit is this ticket's implementation
(branch solr-14346-submit, proof pair, gate, push, no PR). The
fix shape is a design question for that unit, not settled here;
candidates: make the zombie check use a collection-less admin
request that a bare node can answer, fall back to a known
collection, or document the default-collection requirement. The
current code already prefers endpoint core, then default
collection, so the gap is strictly the neither case.

No branch, no push, no PR, no Jira/GitHub post this run.
