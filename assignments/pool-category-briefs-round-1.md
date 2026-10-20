# Pool assignment: category briefs round 1

Status: OPEN

Owner direction, 2026-10-19: build the documentation base by category, so an agent can bootstrap into an area before writing code. `pool/documentation-base.md` describes the base and the brief's place in it. This assignment writes the briefs: one per category in `pool/work-map.md`, at `pool/guides/category-<slug>.md`.

## The brief's fixed structure

1. What this area does, in five sentences or fewer, in the vocabulary the tickets use.
2. The code map: modules, packages, and the ten or so classes an agent will actually touch, each with one line on its role.
3. The call flow: how a request or an operation travels through those classes, naming the entry points.
4. Invariants: what a change in this area must not break, each cited to the code or a test that pins it.
5. The tests: where they live (the real layout; some modules differ), the focused-run command shape for this area, and the slow or flaky classes to plan around.
6. The area's own traps, beyond `pool/guides/traps-build-and-test.md`, each dated and cited.
7. Reviewer patterns: what maintainers asked for on this area's merged and open PRs, from their actual comments, with PR numbers.
8. One worked example dissected: a merged (or best gated) item from this area: the branch, the proof, the PR text, what review changed.
9. Open questions: what is undecided or awaiting a maintainer or the owner, dated.

## Slices (claim one; the linkage pass slices are the same groupings, and the two passes may share a claimant)

- Slice A: update processing and atomic updates; search and query components; highlighting.
- Slice B: SolrJ and clients; replication, backup, and restore; configsets and the filestore; permissions and security declarations.
- Slice C: streaming expressions; suggester, spellcheck, and language detection; core admin and cores; SolrCloud and ZooKeeper.
- Slice D: build, scripts, and release tooling; test quality; reference guide and docs.

## Floor

- Every structural claim (a class's role, a call flow, an invariant) is read from the code at the current main tip in this pass, not copied from a draft, a receipt, or memory. Cite the file for sections 2 to 4.
- The brief passes the bootstrap test: a host that has never touched the area can, from the brief alone, name the classes to read first, the test command, and the invariants. A second host confirms this in a review round before the slice is DONE; the reviewer is never the author.
- Brevity is a requirement: a brief is read in one sitting at claim time. Anything that belongs in a dossier or the traps guide is linked, not repeated.
- Check the WON'T DO advisory before claiming: a category whose brief already exists gets WON'T DO, not a rewrite.
