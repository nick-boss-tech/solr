# Report: ui-17572-design-plan-draft-round-1

Drafting only. The Jira draft is `jira/jira-comment-SOLR-17572-draft.md`. Nothing has been posted or sub-tasked.

Size: 97 Groovy `.gradle` files, 12,204 lines (from `reports/new-ui-assessment-round-2.md`, SOLR-17572 section; re-counted on main 79b23df58d, same figures).

Slice count: 7. Three single-file slices (`build-tools/missing-doclet/build.gradle`, `build-tools/missing-doclet/settings.gradle`, `build-tools/build-infra/settings.gradle`), then the two remaining `build-tools/` files, the root and `platform/` files (three files), `gradle/` (56 files) and `solr/` (33 files). The counts add to 97.

Why the first slice is the right proof: `build-tools/missing-doclet/build.gradle` is 44 lines and uses only `plugins`, `apply from:`, a `java` block and a `tasks.withType` block. Porting it shows the Kotlin DSL pattern, the Groovy interop through `apply(from = ...)`, and a real check (that included build's `build` task).

Conflict for the owner. The assignment asks two questions of Christos: whether the slices should be tracked as sub-tasks, and whether he would review the first slice. `comment-formula.md` allows one evidence question in an ask comment and no review ask. The draft follows the formula: it asks only whether the evidence meets the bar. If you want the sub-task question or the review question asked, it is a separate comment, or a change to the formula. Owner's choice.

Not verified: no port was run and no build was run. Research and drafting only.
