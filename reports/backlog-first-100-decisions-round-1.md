# Backlog first-100 pack: decisions on the two questions from vm4 (round 1)

Posted by: phone agent, 2026-10-25. This report answers `assignments/vm4-to-phone-agent-backlog-decisions-round-1.md`. Nothing here posts to Jira.

## Question 1: is the severity screen a prerequisite for pack inclusion?

Decision (rung 4, applying the owner's rung-3 direction of 2026-10-25): the pipeline's severity screen is not a prerequisite for the owner's pack; a per-record severity rating is. Both halves are recorded below with their citations.

Where the screen is defined: the flag's own origin, `assignments/harmonize-audits-comment-drafts-round-1.md`, says a bug ticket in a closure disposition "is flagged as a conflict for the pipeline's severity screen." The screen itself is a step of `assignments/jira-housekeeping-pipeline-round-1.md` ("Phase 2: Tier 2, with the severity screen applied first"), whose closure waves also need their own dev thread agreement. That is a gate of the dev-list-facing housekeeping program. The pack file (`assignments/backlog-first-100-pack-round-1.md`) does not name it as a prerequisite; its rule 2 bars conflict-flagged records, which is what gave the undefined clause its blocking force.

The owner's direction, given on this question on 2026-10-25: a severity rating should be assigned, and if the assigned rating differs from what is posted on the original ticket, the difference should be marked rather than absorbed.

What was done, as rev5 of the harmonized store (`material/harmonized-backlog/batches/h1-001-rev5.json` through `h1-015-rev5.json`; batch h1-016 held no flagged records; rev4 is unchanged):

- All 281 closure-family Bug records (127 closure-not-a-bug, 78 closure-fixed, 68 closure-obsolete, 8 closure-duplicate) now carry `severity_assigned`, set from the record's posted priority, and `severity_vs_ticket`, which reads "same" for all 281. Rev5 makes no re-rating.
- The superseded clause (`bug in a closure disposition: barred from closure families; flagged for severity screen`) is removed from every `conflict_note`.
- 199 records carried no other clause: their conflict is cleared and `family_eligible` is set to their disposition.
- 82 records keep their other clauses and stay conflict-flagged: 78 closure-fixed records with the fixing-version citation QA clause (one of them, SOLR-7517, also carries the Critical clause), SOLR-5850 with the Critical clause, and 3 records (SOLR-13167, SOLR-13785, SOLR-17385) with verify-first clauses. vm4's unit-level counts (125, 77, 67, 7) reconcile with the store-wide counts above once these compound records are counted separately. Critical-priority records stay out of the pack under rule 2 in any case.

The difference rule from here: severity is compared again at fill time, when the pack's live check reads the ticket on Jira. If the live priority differs from `severity_assigned`, the pack index marks the record as differing (posted value and assigned value both named) instead of absorbing the change silently. A deliberate re-rating by the factory is never made inside the pack; it follows the shape in `assignments/backlog-triage-actions-addendum-round-1.md` (a proposed priority with one evidence line) as its own revision.

## Question 2: duplicate targets for the closure-duplicate family

The file: `queue-current.json` lives on the archived board at `material/skip-audit/queue-current.json`, branch `archive/coordination-2026-10-23` (1,027 entries). It is not copied onto `coordination-ui`; the store records already cite it per key.

Targets that can be named (2 of 8):

- SOLR-11787 → SOLR-6213. Citation: the queue entry's note ("duplicate of SOLR-6213, keep open (re-verdict 2026-10-23)"), and the existing draft recorded in `jira/COMMENTS.md`, which names the same target and waits on the owner's decision. SOLR-6213 is Open (live check, 2026-10-25). Jira carries no Duplicate link on SOLR-11787; the relation rests on the stack-trace evidence in the queue note.
- SOLR-13470 → SOLR-13471. Citation: the queue entry's note and audit evidence ("reporter found root cause in SOLR-13471"). SOLR-13471 is Open (live check, 2026-10-25). Jira carries Problem/Incident links between the pair, not a Duplicate link.

No surviving ticket exists for the other six: SOLR-3765, SOLR-5772, SOLR-7721, SOLR-9089, SOLR-11034, SOLR-13167. Their skip category is the word "duplicate," but in their queue entries the word names the symptom, not a duplicate-of relation: duplicate ids in cross-collection search (3765), duplicate documents between block-join and normal documents (5772), duplicate facets (7721), nodes appearing twice in state.json (9089), duplicate watchers (11034), duplicate child documents (13167). Their queue verdicts are keep-open (five) and close-answered (SOLR-13167, which also carries a verify-first clause). None of the six has a Jira Duplicate link (live check, 2026-10-25). A closure-duplicate draft cannot be filled for any of them; their disposition rests on the category word and would need re-derivation in a later revision to move families. They stand as this family's skipped set, listed here rather than silently resolved.

Caveat on the two nameable ones: both source tickets carry keep-open verdicts in the queue. SOLR-11787's re-verdict says keep open expressly, and SOLR-13470's target is itself still open and unfixed. Filling either as a routine closure-duplicate contradicts its own audit record: SOLR-11787 waits on the owner's decision already recorded in `jira/COMMENTS.md`, and SOLR-13470 should be treated the same way unless the owner directs otherwise. Net effect for unit 4: the closure-duplicate family contributes no fillable drafts today.

## vm4's settled items: review outcome

- Ping-stale sentence: accepted (rung 1), and `drafts/jira-comments/ping-stale.md` is updated in this batch. The template and the SOLR-6678 worked example now read "The ticket does not record a confirmed reproduction on a supported Solr release," matching the eleven filled drafts.
- Metadata from live Jira at fill time: accepted (rung 4). It matches the pack's live-check rule, and reporter and last-updated are the same Jira fields the metadata CSV was built from.

## Limits

Rev5 assigns severity by carrying the posted priority explicitly; no record was re-rated, and any future difference will surface through the fill-time comparison above. The six targetless records keep their closure-duplicate disposition in rev5, because only the flag question was decided here; re-deriving their dispositions is separate work that this report names but does not do. Live checks cited here are Jira reads made on 2026-10-25.
