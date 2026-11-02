# SOLR-6525 tail verdict, batch A claim 4: CLOSURE DRAFT (investigation discharged via linked threads)

- Assignment: `assignments/overseer-tail-verdicts-a-round-1.md`, Claim 4.
- Claim: `claims/solr-6525-tail-verdict-a-phone-agent.md`.
- Ticket: SOLR-6525, "Investigate Overseer performance issues", Open,
  unassigned, last updated 2014-09-16, zero comments.
- Ticket content, in full: "As mentioned in SOLR-5872 and SOLR-6524,
  there has been some evidence in clusters having many collections that
  the overseer can become a bottleneck, esp. when doing things that
  create many state change events across the cluster like rolling
  restarts." That is the whole ticket: a stub pointing at two other
  threads, with no measurement, question, or evidence item of its own.
- Deliverable type: closure draft. The investigation this ticket calls
  for has been done, in the threads it points at and their successors,
  and its results are landed mitigations. Draft only; no Jira post, no
  close, pending a maintainer's or Nick's explicit go.

## Verdict

Close SOLR-6525 as investigated, with the evidence consolidated in the
threads it cites. The grounds, in order:

1. The ticket holds no investigation item of its own. There is nothing
   in SOLR-6525 left to investigate: no comment, no measurement, no
   open question. Everything it points at lives elsewhere, and that
   elsewhere has been worked for a decade.
2. The investigation happened in SOLR-5872 (2014-2017 thread) and
   SOLR-10265 (2017 measurement thread). The 5872 thread established
   the concrete mechanisms: 4+ ZK writes per state update, workqueue
   backup on node bounce, batching limited to consecutive same-
   collection updates, and the downnode 40x over-update (spun off as
   SOLR-10277). The 10265 thread measured the rate (~170 events/second
   single-threaded in 2017) and produced the two child fixes the
   reporter cited, SOLR-11443 (Fixed, 2017: workqueue usage removed
   for state updates) and SOLR-11465 (parallel processing of
   independent messages; see its own batch A verdict).
3. The mitigations landed: per-collection `state.json` (stateFormat=2,
   via SOLR-5756, so collections no longer share one monolithic
   clusterstate.json), SOLR-11443, parallel dispatch in
   `OverseerTaskProcessor` (up to 100 tasks), and the opt-in
   overseer-disabled distributed mode (`ZkController.java:383-402`).

## What this closure does NOT claim

- It does not claim SOLR-5872 is resolved. SOLR-5872 ("Eliminate
  overseer queue") is still Open: its specific CAS-loop proposal was
  never implemented as proposed. The queue was reformed (11443,
  distributed mode) rather than eliminated as 5872 drew it. Closing
  6525 discharges the investigation stub, not the 5872 design
  question, and the closure text must say so.
- It does not claim removal retires this ticket. The removal path
  (SOLR-14927 under the SOLR-17293 umbrella, still Open, decentralized
  processing opt-in rather than default) is supporting context only.
  The closure ground is that the investigation is done and its
  findings are recorded and partly landed, not that the Overseer is
  gone.
- No open evidence item specific to SOLR-6525 remains, because the
  ticket names none. The still-open measurement questions (benchmarks
  at 1000+ cores, batching benefit quantification) belong to
  SOLR-5872 and SOLR-10265, which stay open, and must not be silently
  absorbed into this closure.

## Suggested closure text (draft, not posted)

> The investigation this ticket called for is documented in the
> threads it cites: SOLR-5872 established the mechanisms (ZK write
> amplification, workqueue backup on restart, batching limits) and
> SOLR-10265 measured the rate and produced SOLR-11443 and
> SOLR-11465. The resulting mitigations (per-collection state.json,
> workqueue removal for state updates, parallel dispatch, and the
> opt-in distributed mode) are landed. This stub ticket itself holds
> no further investigation item, so closing it as investigated; the
> design question continues under SOLR-5872 and the removal direction
> under SOLR-14927/SOLR-17293.

## Sources checked

- Jira SOLR-6525 (full content: description, zero comments, no
  links) and SOLR-5872 (description, full 2014-2017 thread, still
  Open), read live.
- SOLR-11443 (Closed, Fixed 2017), SOLR-11465 (Open, own batch A
  verdict), SOLR-17293 (Open umbrella), SOLR-14927 (Open), read live
  in this and the sibling verdict units.
- Code paths cited for the landed mitigations are those verified in
  the sibling units: `OverseerTaskProcessor` parallel dispatch,
  `ZkController.java:383-402` overseerEnabled switch.
- Limit: code read against the local main checkout at `e55d2e7bcb`
  (2026-09-29), not a fresh upstream fetch.
