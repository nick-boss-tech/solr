# PR 5291 / SOLR-18621 parameter factory fixes, round 1

Status: Implemented, Proof Pending.

- Authority: Nick's direct 2026-11-05 chat instruction to deepen the review and implement fixes. Claim: `claims/pr-5291-params-factory-fixes-round-1-windows.md`.
- Contributor H0: `600e0ec4afd82004a94852c419892897be14e6f0`. Live PR remained OPEN at H0 with one author comment and no reviews at the pre-commit refresh. Base and merge-base: `82abb86bb5b7326fe48c117e4de69981a43e4251`.
- Fork branch: `solr-18621-params-factory-fixes`. Pushed H1: `f2952c41c7c023aa6e79868df9efe8c65b02c993`, independently read back from the fork refs API and equal to the committed head.
- Commit author/committer: Nick Shanin, `nick.boss.us@gmail.com`; ordinary child commit of H0; no attribution trailers. The contributor ancestry is preserved and the public PR head was not moved.
- [Pinned offered diff](https://github.com/nick-boss-tech/solr/compare/600e0ec4afd82004a94852c419892897be14e6f0...f2952c41c7c023aa6e79868df9efe8c65b02c993).
- Local review: `research/branch-reviews/2026-11-05-pr-batch/PR-5291-deeper-review.md`. Previous review: `reports/pr-5291-review-round-1.md`.
- Gate: `gates/pr-5291-params-factory-fixes-round-1.md`. Reserved receipt: `receipts/PR-5291-params-factory-fixes-round-1.md`. Posting a gate does not establish that a Linux host claimed or executed it.

## Findings and implementation

**M1, medium, confirmed and patched:** H0's `SolrParams.of(String...)` wraps only its map. `MultiMapSolrParams.getParams`, iterator and getMap expose stored arrays; asMultiMap(newCopy=true) copies only the map, and a ModifiableSolrParams copy also retains the values returned by the iterable API. Array edits through any of those paths change the advertised immutable result. The patch introduces a private final SolrParams implementation whose private map owns newly constructed value arrays and whose getParams returns clones. It inherits the base iterator/stream/writeMap, so those paths expose copies too. Empty input reuses EmptySolrParams. Shared MultiMapSolrParams semantics are untouched.

**M2, medium, confirmed and patched:** H0 adds of(String[]) alongside the existing null-accepting of(SolrParams), making the previously valid `ModifiableSolrParams.of(null)` ambiguous. Renaming only the subclass overload would leave the superclass overload inherited. Both new varargs methods are therefore named `ofPairs(String...)`. The empty and single-pair of methods remain available; the existing uncast null test call is restored. This is an explicit alternative to the ticket's proposed unreleased method name, chosen to preserve existing source compatibility. Consumers of the proposed varargs API must use ofPairs. All six helper delegates and the changelog are updated consistently.

**Null policy:** retain the current ticket's explicit null-to-empty-string behavior, duplicate ordering and odd-count rejection. The author's discussion comment suggests another option but does not settle a replacement contract. Null arrays are separately rejected and documented. The original helper migration already deliberately changes stored-null-element behavior; this patch does not claim that normalization is a newly discovered defect.

**Deeper scope:** examined the full original 11-file change; inherited getters, iterators, stream, writeMap, equality, query encoding, asMultiMap and modifiable-copy paths; factory history and pre-change null test; all six delegates; and SolrQuery's analogous constructor. SolrQuery uses set and has last-value-wins duplicate behavior, so it is a separate contract and is not migrated. No further independent production defect was established. No new trust boundary, public class, fixture or dependency is introduced.

## Coverage and execution limits

H1 changes 11 files, +143/-26 relative to H0: SolrParams.java, ModifiableSolrParams.java, their two existing test files, the six original helper files and changelog/unreleased/SOLR-18621.yml. The changelog retains David Smiley and adds Nick Shanin by name only. The exact outbound list was checked before staging. git diff --check passed; the author/committer identity and contributor ancestry were checked.

Eight test methods are added: six immutable-isolation regressions (getter, iterator, map view, copied map, writeMap callback and modifiable copy) and two input-array isolation pins. Existing factory tests are expanded for insertion order, query serialization, duplicate/null collisions, empty inputs, odd counts, null arrays and the uncast null source call. These reuse the existing SolrJ suites.

No Gradle, compilation, formatting tool or tests ran on Windows. The gate requires six intended runtime failures using H0's shallow-wrapper algorithm under H1's executable API, positive focused/neighbor tests, all affected helper compilation, tidy and root check. Input-copy and secondary contract assertions remain coverage pins unless the gate separately demonstrates a discriminating mutation. M2 is a source compilation property and must be reported separately from runtime proof.

Nick clarified in chat that `TestMultiWordSynonyms.testPf` is unrelated and its cause is tracked in a separate PR. This supersedes the original review's unresolved attribution. No synonym fix or additional reproduction is included in this gate.

No upstream comment, formal review, JIRA post or PR body edit was made. The offered API alternative and runtime evidence remain subject to contributor/maintainer review after Linux proof.
