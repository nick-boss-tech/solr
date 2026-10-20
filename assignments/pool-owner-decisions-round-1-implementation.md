# Pool assignment: implement the answered owner decisions, round 1

Status: OPEN

- Posted by: the phone agent, 2026-10-19, during the owner's factory pause.
- Source of authority: `assignments/pool-vm3-owner-decisions-round-1.md`, all nine rows answered by the phone agent for the owner on 2026-10-19. Each answer authorizes implementation of its row on a new branch named for the ticket, with the usual gate and receipt. SOLR-14970 is answered "working as designed" and has no branch; it is not part of this assignment.
- Claim: one ticket at a time, claim file `claims/pool-decisions-impl-<KEY>.md` before starting. First post wins.
- Rows to implement, with the answer that governs each:
  1. SOLR-16124 (A): add the embedded ZooKeeper port prompt last in the cloud example flow, default port+1000. Evidence and file anchor are in the decisions file.
  2. SOLR-18214 (A): an omitted `baseConfigSet` on the v2 create endpoint creates an empty configset; v1 keeps defaulting to `_default`. First verify an empty configset uploads and a core loads from it; if that fails, stop, record the evidence in the decisions file, and do not change the answer.
  3. SOLR-16865 (A): startup warning below 2 percent free on the data directory. The branch settles and records the log level and the exact check placement.
  4. SOLR-16220 (A): a clear error when an explicit non-SSL `SSLConfig` is paired with an `https` URL; no silent fallback.
  5. SOLR-16750 (A): `groupValue` carries the label via `indexedToReadable`, matching the distributed path. First run the probe that finds which line prints the index in the single-shard path.
  6. SOLR-12718 (B): add a `StreamContext` overload that takes the `SolrClientCache` and keep the existing constructor; deprecate the old one if the surrounding pattern supports it. Do not break the signature.
  7. SOLR-8614 (A): one toggle on `SolrClient` covering the request writer and the response parser.
  8. SOLR-2534 (A): `min()` and `max()` on strings are lexicographic; missing values are ignored; a multi-valued field is an error, matching the numeric behavior.
- Deliverable per ticket: the branch pushed to the fork, the gate run at its head with a receipt in `receipts/<KEY>.md`, and the claim marked DONE with the branch and head. A PR draft per the formula may be written to `drafts/` on coordination; opening the PR stays with the owner.
- Limits: no PR openings, no Jira or GitHub posts or comments, no force push. If a row's answer turns out to be unimplementable as written, stop and record the evidence in the claim instead of improvising a different behavior.
