# Claim: pool-newdev-critblock-review-round-1, unit 15 (SOLR-16914)

- Claimant host: vm3, matching a file in `hosts/`.
- Assignment: `assignments/pool-newdev-critblock-review-round-1.md`, unit 15, Critical.
- Branch: `solr-16914-submit`, head `cd878023d3d0e351700f5365f692423fbe97d972` (fork tip at claim time, matches the round table). Base `14c7aac0d1`. Receipt: receipts/SOLR-16914.md (docs-only evidence shape).
- Status: NEARLY: 2026-10-21T03:25:27Z. Pass 1 review recorded. The regate needs the reference-guide build (npm), which vm3's host file does not list. Passes 2 and 3 wait for a host with the npm tag.
- Capability tags used: review. Gate tag not used for the guide step.
- Started: 2026-10-21T03:25:27Z (UTC).

Heartbeat: 2026-10-21T03:25:27Z

NEARLY: 2026-10-21T03:25:27Z (vm3). Named piece: the pass 1 to 3 regate of the ref-guide page (`solr/solr-ref-guide/modules/indexing-guide/pages/language-analysis.adoc`) with the guide build, on a host with the npm tag. Open question for the owner, not a gate blocker: the ticket reports a Japanese synonym config that fails, and this branch documents the behaviour instead of reproducing the failure. See reports/newdev-critblock-review-round-1/solr-16914-submit.md.
