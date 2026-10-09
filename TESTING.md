# TESTING.md: decisions for the PR preparation batches

Decisions taken from the reviews, batch by batch. A decision
marked ADOPTED follows the recorded recommendation, under Nick's
standing agreement. A decision marked DISCUSS is a tough call: the
recommendation is recorded, the call is not taken, and the item
waits for a discussion with Nick. Audits and PR drafts on this
branch follow this file.

## Update processing and atomic updates (2026-10-08)

### ADOPTED

- **SOLR-12245, framing.** The PR is framed as an error-message
  improvement: the distributed-update error message now names the
  target replica. The description states plainly that the
  ticket's MDC ask is not addressed by this change and offers it
  as a follow-up. The review's optional null-guard test is not
  added; the gated tree stays untouched, and the missing
  null-guard coverage is named in the draft's Limits instead.
- **SOLR-16655, upgrade note.** The branch's design position
  stands. The upgrade note the review is owed goes into the PR
  description draft when it is written. No code change, no
  re-gate.
- **SOLR-18505, comment-only head.** No re-gate for the
  comment-only commit past the gated head on the live PR.
  Upstream CI runs on the live head and covers it.

### DISCUSS (recommendation recorded, call not taken)

- **SOLR-6045, the factory defect.** The branch's premise work
  found a real pre-existing defect on the opt-in atomic path:
  the factory returns the list of maps, the instanceof Map skip
  misses, and the maps are stored as values. Recommendation:
  fix it in this branch before the PR; it is the same code path
  the PR touches. Consequence: a production change on a gated
  branch, plus a re-gate. The alternative is to ship as-is with
  the defect named in Limits and a follow-up offer. Tough
  because it trades scope discipline against shipping a known
  defect.
- **SOLR-12864, the passing pin.** The review's owner call:
  accept a test that pins working behavior (its fail-before
  stage reports NOT_PROVEN), or drop the branch. The ticket's
  symptom does not reproduce on the current base. Recommendation:
  drop the standalone pin. The alternative is to accept it as
  coverage. This is the same open question as SOLR-10641.
- **SOLR-13696, fix or retire.** The branch's re-enabled tests
  fail on current main for schema and config drift beyond the
  commitWithin race the ticket names, so its premise does not
  hold as shipped. Recommendation: retire the branch as it
  stands; funding the routed-alias test-drift fix is a separate
  project. Retire calls are Nick's. The branch stays untouched
  while the hold stands, whichever way the call goes.
