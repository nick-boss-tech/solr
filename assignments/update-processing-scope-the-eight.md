# Assignment: scope the eight DISCUSS items in the update processing category

## Purpose

`TESTING.md` on this branch records eight DISCUSS items from the Groups C and D audit reconciliation (report: `reports/update-processing-cd-reconcile.md`). Each item is a scope call: ship the branch narrow as it stands, or do the broader thing in the same branch. The owner prefers doing more when it is contained, and narrow when it is not, but right now the cost of "more" is unpriced for all eight.

This assignment prices each one. For every item, produce a scoping report that states what the broader option concretely touches, how big it is, whether a test can pin it, and what risk it adds. The decisions stay with the owner; the reports make them sized calls instead of philosophy calls.

## Rules

- Claim first: add `claims/update-processing-scope-the-eight.md` with your claim before starting, and commit it on this branch.
- Read-only on submit branches: no code changes, no commits, no pushes to any `solr-*-submit` branch. This is reading and writing reports only.
- No builds or test runs are required. Where a pinning test can be sketched from reading, sketch it. Where a run would be needed to confirm a claim, say so and mark the claim as read-only; the main side can run it later.
- One report per item under `scopes/update-processing/`, named `SOLR-<ticket>.md`, plus one index file `scopes/update-processing/INDEX.md` summarizing all eight in the shared shape.
- Every size claim needs evidence: file paths, the methods involved, and line counts from the actual diff or the actual target code, not estimates from memory.
- Commit as Nick Shanin, no trailers, no em dashes anywhere, no PR numbers in file names or text (ticket keys only).

## Shared report shape

Each report has these sections:

1. **The call.** One paragraph restating the narrow option and the broader option, in plain terms.
2. **Mechanism.** What the broader option touches: classes, methods, call paths, and how it interacts with what the branch already changes.
3. **Size.** Files touched, rough added and changed lines, new tests needed, and whether the branch's gate would need a full re-run (assume yes if any production file changes).
4. **Pin.** Can a test prove the broader behavior or the defect it addresses? Sketch the test: class, setup, assertion. If a test already exists nearby, name it.
5. **Risk.** Behavior surface beyond the ticket, review contention (is the broader behavior itself a judgment a maintainer might reverse), and any interaction with other branches in the category.
6. **Sized recommendation.** One of: include, it is contained; ship narrow, the broader work is a separate change; or still a judgment call, with the one fact that would settle it. Restate the recommendation from `TESTING.md` only if the scoping evidence supports it; if the evidence changes it, say so and why.

## The eight items

### SOLR-5065: locale handling and suggester inference

Branch state: the helper `normalizeExponent` rewrites the exponent before `NumberFormat` runs, whatever an explicit `locale` parameter says, and the same helper makes `DefaultSchemaSuggester` infer Double for `E+` and lowercase samples with no test covering that inference.

Scope both broader options: (a) a locale-aware parse instead of the regex rewrite, stating what behavior changes for which locales; (b) a test pinning the suggester inference, stating whether the inference itself looks intended or accidental from the code and the ticket.

### SOLR-6065: the ticket's cloud test

Branch state: the ticket asks for cloud based tests that set a lower limit and verify clean error messages for a single shard. The branch has one single-core test and no cloud test; its audit made that the blocking item.

Scope the cloud test: what it needs (collection shape, how the limit is set in a cloud test, how the error is asserted), which existing cloud tests it can be modeled on, and its flake risk. Also state what the single-core test already proves, so the owner can judge what the cloud test adds.

### SOLR-12245: target detail in the client response

Branch state: the improved error message puts the target's URL, collection and shard into the error the client receives. The adopted framing accepts the message change; whether that detail belongs in the client response or only in logs is undecided.

Scope the facts, since this one is a judgment call rather than a size call: do the server logs already carry the same detail (so removing it from the response loses nothing), who consumes this error (client applications, operators), and what precedent exists in this codebase for node URLs or shard detail in client-visible errors.

### SOLR-12705: counting processors on the atomic path

Branch state: the fix sits in the shared `FieldMutatingUpdateProcessor` base class, so a counting processor shares the new path: a single-map `add` on a counted field becomes `{add: <count of the operand>}` and `remove` becomes `{remove: <count>}`, where base wrote a plain count. Neither case is in the changelog or the tests. Note the overlap: SOLR-16655 also changes `FieldMutatingUpdateProcessor`.

Scope both options: (a) exclude counting processors in code, stating the check involved and its size; (b) state the behavior and pin it with a test, sketching that test. Say which processors are affected by name.

### SOLR-16356: the second close-race ERROR

Branch state: the branch silences the update-log close race. The ticket transcript also shows a second stack trace: the periodic task in `DocExpirationUpdateProcessorFactory` commits after its delete-by-query, the commit fails with `SolrCoreState already closed`, logged at ERROR. The branch does not touch that factory.

Scope the suppression: where that ERROR is raised, what a fix looks like (guard, catch, or close-order change), its size, and whether the two traces share a root cause or are independent.

### SOLR-16910: scope of the logging fix

Branch state: the branch fixes the slow-WARN format in `LogUpdateProcessorFactory` so the WARN carries the request details. The ticket also calls out `SolrCore.Request` logging behavior, whose desired behavior the reporter says is undecided. The branch does not touch it.

Scope what implementing the second item would even mean: the plausible behaviors, what the ticket and its discussion actually ask for, and how large each plausible version is. If the desired behavior cannot be determined from the ticket, say that plainly; that is a scoping result.

### SOLR-7504: chain placement and the uncovered shape

Branch state: separate from its code fix (the plain-first detection scan, already recorded as fix-first work). The counter runs only when the source field is in the update, so an update that leaves the source field alone is not covered: a trailing `DefaultValue` writes a plain 0 into the partial document, an atomic update applies it as a `set`, and the ticket's symptom persists for that shape. The changelog title says "fixed".

Scope the chain change: what moving or widening the counter's placement involves, which processors it would interact with, its size, and what test would pin the uncovered shape. Also price the narrow option honestly: a Limits entry plus narrowing the changelog title.

### SOLR-14718: the second flaw in the ticket packet

Branch state: the branch clones the update command in `distribAdd` so a later clear does not reach the in-flight request (three files, +43/-1). The ticket packet names a second flaw: the per-node streaming-client association can report the first document of a batch as the failed one. The branch does not change it. Its audit is `audits/update-processing/SOLR-14718.md`, finding 2. The branch also owes a deterministic regression test (finding 1); that fix is recorded separately and is not part of this scope question, but note if the second flaw's test would live in the same place.

Scope the second flaw: find the association code (how a per-node streaming client connects an error back to a document), state the mechanism that picks the first document, sketch the failing test, and size the fix. Say plainly whether it shares files or call paths with the clone fix.

## Deliverable

Eight reports and the index, committed and pushed on this branch (`pr-prepare`). The index table lists, per item: broader-option size in one line, pin feasibility, and the sized recommendation. The owner decides from the index; the reports are the evidence behind it.
