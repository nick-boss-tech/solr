# Directed assignment: vm5 clean RC3 receipt and vote email draft (round 1)

Status: OPEN
Intended host: vm5 (directed by Nick Shanin, 2026-10-24)
Tags: records, draft

The RC3 record is scattered across reports and three email drafts, and `receipts/` holds no RC3 receipt at all. Produce the clean pair: one receipt that is the single source for vm5's RC3 work, and one email draft derived from that receipt only.

## Deliverable 1: the clean receipt

`receipts/rc3-vm5-clean-receipt-round-1.md`, built from vm5's own logs and `results.tsv`, not from other hosts' summaries:

- The artifacts (Solr 10.1.0 RC3, rev `749712b71940c9f96d824c533f32f5c56dcd3924`), the host and JDK (aarch64, 4 CPUs, 7 GB, JDK 21.0.12.1), and the run table for every smoke run, completed, died, and stopped runs labeled as such, with per-run failing methods and counts taken from the JUnit XML.
- The fix-set table: each repeating failure, its vehicle (PR #5098, PR #5106, PR #5192, SOLR-18586, SOLR-18587, SOLR-18564), and its proof-pair status under `assignments/rc3-proof-pair-requirement-round-1.md`.
- One section each for the tarball check verdict and the TestTlogReplica reproduction (RC3 tip and main), with their log paths.
- The unit 3 result from `reports/rc3-termsqparser-metrics-leak-round-1.md` (the SOLR-18564 hypothesis test), stated in one paragraph.
- A limits section: one small ARM host; run-001's stdout was overwritten; run-004's partial output was deleted; anything else the logs cannot support.
- Where any earlier report or draft disagrees with this receipt, name the disagreement and say which reading the logs support. This receipt supersedes the scattered record for citing purposes; the earlier files stay as history.

## Deliverable 2: the email draft

`drafts/rc3-vote-email-vm5-round-1.md`, derived only from the receipt:

- Addressed to the 10.1.0 RC3 vote thread on the dev list, first person as the owner, plain text, short.
- States what was run, the repeating failures, and the vehicles: #5098 with Sanjay Dutt's 100-run pair cited as his, #5106 with its gate status as the receipt records it, #5192 open; tickets SOLR-18586, SOLR-18587, SOLR-18564. The one-host caveat in one line. No proof claim the receipt does not back.
- Casts no vote; the vote line is the owner's to add.
- Ends with a check note: the three earlier email drafts (`drafts/rc3-vote-note-draft.md`, `drafts/rc3-vote-followup-draft.md`, `drafts/rc3-fix-set-email-draft.md`) are superseded by this one, and any claim in them that the receipt does not support is listed in the receipt's disagreement section.

The vote closes Tuesday 2026-10-27 11:00 UTC; the receipt comes first, the email second, in one claim.
