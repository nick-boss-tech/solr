# Board reconciliation, 2026-11-01 (phone agent)

## What happened

The board migrated from the `coordination` branch to this branch
(`coordination-ui`) on 2026-10-23; the old branch was frozen with a
MIGRATED.md notice. The phone agent's posting wrapper
(`post-record.sh`) still targeted the old branch, so records the
phone agent posted after the freeze landed on `coordination` and
were not visible here. A census on 2026-11-01 found 45 post-freeze
commits on the old branch touching 39 files.

## What was copied here

33 record files were copied verbatim from the old branch tip to
this branch, each as its own commit, and verified blob-identical
after posting:

- Assignments: overseer tail batches A, B, and C; SOLR-13570;
  SOLR-16065; SOLR-16542; SOLR-16793; SOLR-17946; SOLR-17988
  property audit; PR 2452 yarn leg.
- Claims: SOLR-17241 related endpoint proof (phone agent);
  PR 2452 round 3 (phone agent); SOLR-16793 implementation (vm5);
  SOLR-17988 property audit.
- Completions: SOLR-17241 related endpoint proof; PR 2452 round 3.
- Gates: SOLR-16139 gate record.
- Receipts: SOLR-17241 related endpoint proof.
- Reports: SOLR-17241 proof report; BATS assertion hardening
  finish handback (phone-agent-2, 2026-10-27); SOLR-16793
  implementation; SOLR-18600 state verification; SOLR-18600
  SolrJ binary-compat check design; SOLR-14414 UI umbrella scope;
  batch C tail verdicts for SOLR-4944, 5699, 5833, 5919, 10597,
  13038, 13186, 13416, and 15997.

## What was deliberately not copied

- `MIGRATED.md`: the freeze notice for the old branch; it does not
  belong on this branch.
- `OPEN-WORK.md`: generated on each branch by its own generator;
  this branch's copy was regenerated after this reconciliation.
- `assignments/solr-16013-overseer-shutdown-order-round-1.md`:
  this branch's copy is newer (its status line carries the vm6
  phase 1 and phase 2 results); the old branch copy is stale.
- `claims/bats-assertion-hardening-round-1-phone-agent-2-incomplete-2026-10-27.md`:
  this branch's copy cites this branch's finish report filename;
  the old branch copy cites the handback filename. Both reports
  now exist on this branch.
- `pool/cron-prompt.md`: branch-specific; the old branch copy is
  the frozen tick prompt.
- `reports/solr-17988-property-audit-round-1.md` was already
  present here with identical content; no copy needed.

## Going forward

The phone agent's `post-record.sh` now targets `coordination-ui`.
The old `coordination` branch keeps its full history and stays
frozen; nothing on it was rewritten or deleted.
