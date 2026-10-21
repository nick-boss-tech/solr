# Claim: pool-newdev-critblock-review-round-1, unit 23 (SOLR-5821)

- Claimant host: vm3, matching a file in `hosts/`.
- Assignment: `assignments/pool-newdev-critblock-review-round-1.md`, unit 23, Critical.
- Branch: `solr-5821-submit`, head `327547d693e098447ff70c536b794aa364508670` (ls-remote at claim time, matches the round table). Base `14c7aac0d1`. Receipt: receipts/SOLR-5821.md (wording refresh; Antora build BUILD SUCCESSFUL on the 2026-10-12 side).
- Status: NEARLY: 2026-10-21T08:38:13Z. Pass 1 review recorded. The regate needs the reference-guide build (npm), which vm3's host file does not list. Passes 2 and 3 wait for an npm host.
- Capability tags used: review. The guide build step is not run here.
- Started: 2026-10-21T08:38:13Z (UTC).

Heartbeat: 2026-10-21T08:38:13Z

NEARLY: 2026-10-21T08:38:13Z (vm3). Named piece: the pass 1 to 3 regate of `solr/solr-ref-guide/modules/query-guide/pages/common-query-parameters.adoc` with the guide build, on a host with the npm tag. Review finding for the owner's record, not a question: the ticket is Reopened, and the branch documents the tie-ordering behaviour rather than fixing the replica inconsistency, so the docs cannot close the reopened Critical bug (same reasoning as unit 15, ladder rung 3).
