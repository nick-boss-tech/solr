# Assignment: ui-17572-design-plan-draft-round-1 (Kotlin DSL migration, sliced as a design plan)

- Status: OPEN. Intended host: any host with the `review` tag. Posted by phone agent on behalf of Nick Shanin, 2026-10-24.
- Scope: drafting only. No Jira comment is posted and no Jira sub-task is created under this assignment. Every outward text comes back as a draft for the owner's go.
- One claim covers the whole item.

## Background

SOLR-17572 (reporter malliaridis, labels `gradle`, `newdev`) asks to migrate the Groovy Gradle scripts to Kotlin DSL, "either one by one or all together". The round 2 assessment (`reports/new-ui-assessment-round-2.md`, SOLR-17572 section, vm2) returned TOO BIG AS STATED and named the first slice. The ticket belongs to Christos; creating sub-tasks on it unilaterally would presume his agreement, so the plan goes to him as a staged ask in public, per the comment formula, and sub-task creation happens only if he blesses the shape.

## Size, from the assessment (do not re-derive; cite it)

- 97 Groovy `.gradle` files, 12,204 lines total: 56 under `gradle/`, 33 under `solr/`, 5 under `build-tools/`, plus the root `build.gradle` and `settings.gradle` and one under `platform/`.
- 8 Kotlin `.gradle.kts` files already exist: five under `ui/` (already Kotlin DSL, its own version catalog) and three outside `ui/` showing the team already accepts Kotlin DSL per file (`gradle/testing/coverage.gradle.kts`, `gradle/validation/dependency-analyze.gradle.kts`, `solr/webapp/js-client/build.gradle.kts`).
- The hard parts a mechanical port must rewrite by hand: 78 `apply from:` calls, 183 `ext` uses, about 730 closure or `def` lines. A Kotlin script can still `apply(from = ...)` a Groovy file, so the port can proceed one file at a time without a flag day.
- First slice: `build-tools/missing-doclet/build.gradle` (44 lines, only `plugins`, `apply from:`, a `java` block and a `tasks.withType` block). Prove it by running that included build's `build` task. Next slices: `build-tools/missing-doclet/settings.gradle` (31 lines) and `build-tools/build-infra/settings.gradle` (31 lines).

## What to draft

A staged-ask comment for SOLR-17572, in Jira wiki markup with the footer, short enough for an overloaded reviewer, that does four things:

1. States the size honestly (97 files, 12,204 lines) and the assessment's verdict that "all together" is not startable.
2. Proposes the slice plan as a design, not a fait accompli: prove the pattern on the 44-line missing-doclet build file, then the two settings files, then slices by directory (gradle/, solr/, build-tools, root), each slice its own sub-task and its own PR, each independently shippable because Kotlin can still apply Groovy files.
3. Asks Christos the two questions the plan needs: whether he wants the slices tracked as sub-tasks on this ticket, and whether he would review the first slice.
4. Names what Nick would do first if Christos says yes (the missing-doclet port) and nothing else.

Deliverable: `jira/jira-comment-SOLR-17572-draft.md` plus a one-paragraph note in `reports/ui-17572-design-plan-draft-round-1.md` stating the total size in one line (files and lines), the slice count the plan proposes, and why the first slice is the right proof of the pattern.
