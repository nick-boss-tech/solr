# Suggester round 4: decisions

Main side, 2026-10-09. The owner ratified every recommendation in `material/suggester-round-4-answers.md` on 2026-10-09, including the two items that file left open. He added one standing instruction, recorded at the end. States:

1. **SOLR-9227**: draftable now at `3c23fc5cfa6` with its gate receipt; WARN stands, no Choices owed; the store path stays as build-error context in Limits.
2. **SOLR-9968**: wording narrowed at `c31d2ff1a3f`; top-up gate running (log g9968-topup-gate.log).
3. **SOLR-10937**: docs only, NOTE corrected to the verified facts; the path-naming follow-up is a Limits offer, no separate ticket unless a maintainer asks. Docs verification lane running.
4. **SOLR-11844**: docs only, conditional on the Lucene verification; line 402 sentence fixed by the same lane.
5. **SOLR-14171**: the `Boolean.getBoolean` factory split is named in Limits with a follow-up offer, not fixed in this branch; the literal `true` fallback stays and is posed as the draft's Choice; the unconfigured default stays `true`. Packaged at `faa262eedc5` (root TESTING.md removed, tidy clean); first full gate running (log g14171-gate.log).
6. **SOLR-17215: adopted as recommended.** Keep the clearer 503 error; narrow the catch so `AlreadyClosedException` is not reported as "not built"; the 500 to 503 change is posed as the draft's Choice; add the ref-guide note that suggester dictionaries are not replicated and must be built on each node. A main-side lane is implementing the narrowing and the note, removing the root TESTING.md, and gating the result.
7. **SOLR-17393: scope decided.** Ship the merge-only fix. Its Limits state plainly that the merge orders what the shards return and that a shard's own top-count cut happens first, so at count 1 with equal weights the reporter's symptom can persist; the per-shard fix is named there as a planned follow-up submission. Payload scope: the stable order applies to distinct suggestion text; duplicate text with different payloads is out of scope and named in Limits. Memory: the collected list is bounded by count times the shard count, because each shard returns at most count candidates; the draft states that bound. Packaged at `626241e647e` (root TESTING.md removed, tidy reflow folded); baseline gate running (log g17393-gate.log).
8. **SOLR-9637: decided.** Its own PR after SOLR-17393 lands, rebased onto the landed 17393. Note for its packaging step: the 17393 tip has already moved to `626241e647e` (two packaging commits past the tip 9637 stacked on), so the rebase happens against the then-current 17393 tip, and again if 17393 moves before landing.
9. **Gate order**: as executed in the answers file.

Standing instruction from the owner (2026-10-09): if something needs a follow-up fix, the PR lists it in Limits or in a Choice that states a plan to submit one. The SOLR-17393 per-shard item above is the first application in this category.

The draft round for this category can start with SOLR-9227 immediately, and take 9968, 14171, 17393, and 17215 as their gates record green, and 10937 and 11844 when the docs lane reports.
