# Assignment: review the ten ungated branches in Update processing and atomic updates

## Context

The inventory on this branch (`branch-focus-inventory-2026-10-08.md`)
lists 29 branches under Update processing and atomic updates. Ten of
them have no completed gate: six awaiting the pipeline, one with a
gate paused mid-run, one held, and two new candidate rows. This
assignment reviews those ten, split into two groups of five, and
decides for each branch what happens next.

The heads in the tables below are the inventory heads. They are the
starting point for verification, not a substitute for it.

## Rules for this assignment

- Reading work only. No builds, no Gradle, no test runs, nothing
  executed. Gates run later on Linux or on the GitHub runner.
- Push nothing to any submit branch. All work product lands on this
  branch, `pr-prepare`.
- Claim first, per group. Before starting a group, commit
  `claims/update-processing-not-gated-group-a.md` (or
  `claims/update-processing-not-gated-group-b.md`) with your name
  and the date. A rejected push means the group is already taken.
- Verify each branch's head before reviewing it. Fetch the fork and
  compare the live tip with the table. If a head has moved, say so
  in the review. Review the current head only if the branch's own
  change is still easy to isolate; otherwise stop on that branch
  and flag it as head moved, needs a main-side check.
- Review the branch's own diff: the three-dot diff from the
  merge-base with upstream main. Tag every factual claim in a
  review as verified (seen in the diff or the ticket) or
  hypothesis.
- Judge the test by reading: would it fail on the base code, and
  for the stated reason? Record that as a reading judgment, never
  as a run result.

## Deliverables, per branch

A review file at `reviews/update-processing/SOLR-<ticket>.md`
holding:

- the head SHA actually reviewed;
- the premise in one or two sentences;
- what the diff does, including any behavior change the ticket does
  not name;
- the test-oracle reading (does the test discriminate on base);
- interactions with other branches in this category;
- one verdict: **Gate now**, **Fix first** (with the exact fixes
  listed), **Decision for Nick** (with the question stated), or
  **Do not proceed** (with the reason).

For each branch with a Gate now verdict, also write a PR
description draft at `pr-drafts/update-processing/SOLR-<ticket>.md`,
following `pr-formula.md` on this branch. Name the reviewed head
SHA at the top of the draft. The Proof section says "Awaiting
gate" and nothing more; proof counts come only from a gate receipt
that does not exist yet for these branches.

## Group A: the five production-code branches awaiting the pipeline

| Ticket | Branch | Inventory head | Topic |
|---|---|---|---|
| SOLR-4841 | solr-4841-submit | f8850ffd421 | DetectedLanguage constructor should be public |
| SOLR-5505 | solr-5505-submit | 44c444aa5cd | LoggingInfoStream not usable in a multi-core setup |
| SOLR-5754 | solr-5754-submit | 36fe859d5fb | SolrStreamingServers returns a synchronizedList of Errors |
| SOLR-5887 | solr-5887-submit | c4c57ef7bcb | Document exception does not give core information |
| SOLR-6973 | solr-6973-submit | 4c6092614e5 | Signature partial update with no signature fields computes an empty signature |

Notes for Group A:

- SOLR-4841, SOLR-5505 and SOLR-5887 moved non-fast-forward since
  the 2026-10-06 inventory. Take extra care isolating each branch's
  own change from its history.
- SOLR-6973 carries a `SOLR-6973-TESTING.md` run-shape document.
  Treat it as context for the premise. Do not run anything.
- SOLR-5754 changes `SolrCmdDistributor` and
  `StreamingSolrClients`. SOLR-5939 in Group B changes the same two
  files. The SOLR-5754 review must name that overlap and say
  whether the two branches can proceed independently.

## Group B: the five judgment calls

| Ticket | Branch | Inventory head | State and topic |
|---|---|---|---|
| SOLR-5939 | solr-5939-submit | 8f7a36fa6dd | Candidate, new row. Streaming update errors recorded against the wrong request |
| SOLR-5941 | solr-5941-submit | 62516cc338e | Candidate, new row. Autocommit runs through the default update processing chain |
| SOLR-11475 | solr-11475-submit | 42fb8817b25 | In pipeline. Endless loop and OOM in PeerSync |
| SOLR-12864 | solr-12864-submit | 8c5455d3d24 | Awaiting pipeline. Test-only pin: echo with mapUniqueKeyOnly |
| SOLR-13696 | solr-13696-submit | 05ab4664dac | Held, banked untouched. RoutedAliasUpdateProcessorTest failures |

Notes for Group B:

- SOLR-5939 and SOLR-5941 are new candidate rows. First establish
  that the premise is real and that no other branch already covers
  the ticket. For SOLR-5939, check the overlap with SOLR-5754 in
  Group A (same two streaming files) and say whether both can
  proceed.
- SOLR-11475 has a gate paused mid-run on the Linux side. The
  paused gate is not evidence either way. Judge the premise and
  the diff, and say whether the gate should resume on the branch
  as it stands or the branch needs work first.
- SOLR-12864 is a test-only pin. The review question is whether
  the pin earns a PR at all, and what it protects if it does.
- SOLR-13696 is held and stays untouched. Its review is a
  disposition: a real fix path, or retire. A Gate now verdict is
  not available for it while the hold stands.
