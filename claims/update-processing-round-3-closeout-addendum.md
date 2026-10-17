# Claim: round 3 close-out addendum (answers and SOLR-13943)

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: `material/update-processing-round-3-closeout-answers.md` (commit `e441a3a3b93`), which answers the six owner decisions in `reports/update-processing-round-3-closeout.md` and supplies the SOLR-13943 material. The assignment `assignments/update-processing-round-3-closeout.md` (commit `49bc048b3e9`) says SOLR-13943 joins the close-out when that addendum lands. Output: draft edits under `pr-drafts/update-processing/`, and an addendum section in `reports/update-processing-round-3-closeout.md`.

Items and heads:
- SOLR-16655 at `5e2317443f4116a9e97330b0d6ce175cf44b54df`: the Proof cites the gate, head, date, and counts from answers item 1 (gate GREEN 2026-10-09 08:03 MDT; FieldMutatingUpdateProcessorTest 36 of 36; ParsingFieldUpdateProcessorsTest 44 of 44; `:solr:core:check -x test` rc=0), and the pre-fix proof (the new test fails on the pre-fix head `b201a57fb3e5`). Its "no fail-before run is claimed" line is replaced.
- SOLR-16673 at `d7170b12f312`: add the narrowed changelog title verbatim as a title line (answers item 4).
- SOLR-13943 at `cc155cf68e1d8e79f1bcecd2e4c25ada864d8f53`: NEW draft from answers item 6, stacked on the SOLR-13696 head `1d0b8a0a73cd`. The Proof states the awaitsfix result plainly, naming `testPreemptiveCreation` as pre-existing.
- SOLR-6045 and SOLR-5941: no change (close-out material items 4 and 6, already verified).

Held, not edited:
- SOLR-13696: the live tip of `solr-13696-submit` is now `da4fa6df117`, one commit on top of `1d0b8a0a73cd`. That commit adds a changelog fragment, and answers item 5 says its gate (r8) is pending. The branch is held under the assignment's rule: verify the live tip equals the named head, or hold the branch. Its draft stays at `1d0b8a0a73cd` until a further addendum names the new head with gate r8 GREEN. The pre-fix heads and the create-alias citation (answers item 2) wait for that addendum too.

Source note: the gate logs and receipts ledger cited by the material are main-side records, not in this workspace. The drafts cite the material's record of them, as before.

Not in scope: opening or commenting on pull requests, edits to any `solr-*-submit` branch or PR description, builds or test runs, SOLR-13696 draft edits (held as above), SOLR-18505, SOLR-9637, and the suggester round 4 work (committed separately).
