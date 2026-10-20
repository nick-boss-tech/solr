# The documentation base: what agents read, and where it lives

Owner direction, 2026-10-19: build the documentation base out, by category, the way the work itself is organized. The base has layers. The standing docs say what the work is and where it comes from; the guides say how the code and the build actually behave; the category layer is what an agent reads to bootstrap into an area before writing code. This page is the index and the maintenance rules; the content lives in the files it points to.

## Reading order for any agent starting work

1. `pool/owner-complaints-2026-10-19.md`: what the owner currently expects done differently.
2. `pool/work-stores.md`: where work comes from, in pull order.
3. `pool/work-map.md`: the category taxonomy and the linkage rules; find your item's category and siblings.
4. Your category's brief in `pool/guides/` (below), and the dossiers of the surfaces your item touches.
5. `pool/advisory-wont-do.md` before claiming, and `pool/guides/traps-build-and-test.md` before building.

## The layers

**Standing docs (pool/).** START-HERE, the work stores, the work map, the tickets-to-open registry, the WON'T DO advisory, WORKFLOW.md, the templates, the tick prompt. Maintained as they change; they are the rules, not a record of events.

**Guides (pool/guides/).** How things behave, learned the expensive way:

- `traps-build-and-test.md`: build, test, and gate-reading traps, each with its date and ticket. Seeded 2026-10-19; grows by the rule on its own page (an entry lands in the same push that pays for the lesson).
- Category briefs, one per category in the work map, named `category-<slug>.md`: the bootstrap for an area. Structure is fixed by the round 1 assignment: what the area does, the code map, the call flow through the classes an agent will touch, the invariants a change must not break, the tests and how to run them, the area's own traps, the representative finished work to imitate, and the open questions.
- Surface dossiers, one per hot shared surface, named `surface-<slug>.md`: a single class or surface that several branches collide on, with its callers, its cross-module subclasses, its invariants, and every live branch touching it.
- `failure-atlas.md`: failure classes with their diagnosis steps, so a red build gets classified before it gets retried: gate-log step separation, run-shape failures (a check that fails identically at the base), environment refusals, and the known harness hangs.

**Worked examples.** One finished item per category, dissected in its category brief: the branch, the proof, the PR text, and what review asked for. An agent imitates a worked example before it improvises. The briefs assignment (round 1) picks them from merged work; where a category has no merged example yet, the brief says so and uses the best gated one.

**Category knowledge that is not a guide.** Reviewer patterns (what maintainers in this area ask for, from their actual review comments) are a section of each category brief, not a separate file. Open questions and maintainer asks pending an answer live in the brief too, dated, so an agent does not re-ask a settled question or trip over an unanswered one unknowingly.

## Maintenance rules

- A guide entry cites its evidence: a ticket, a PR, a gate log, a dated run. Undated, uncited advice is challenged and removed, the same standard as the traps page.
- When a brief or dossier is found wrong by a run, the correction lands in the same push as the run's records, naming the run. Stale guidance is worse than missing guidance: it gets acted on.
- New doc types are proposed on this page first, with their home and their maintenance rule, before files appear. One home per kind of knowledge; a second home for the same knowledge is a duplicate, and duplicates get WON'T DO under the advisory.
- The tick does not read the whole base. It reads the standing docs every cycle and the category brief when an item is claimed, which is why the briefs stay short enough to read in one sitting.
