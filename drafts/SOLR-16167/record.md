# Skip record: SOLR-16167

- Ticket: SOLR-16167, "Review static initializers that reference subclasses". JIRA status Open, resolution none, fix versions none, created 2022-04-25, updated 2025-10-17. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-16167-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-16167-vm2.md).

## Ticket reading

The ticket asks for two things: fix existing static initializers that reference a subclass, which can deadlock during class loading, and add a build check so new ones are not introduced. It is a follow-up to SOLR-16165 (a deadlock in SlotAcc initialization). It links SOLR-15254 (resolved) and SOLR-16165 (closed).

The comments (7 read) discuss a Palantir Gradle plugin that reportedly has false negatives, an error-prone check that was waiting upstream (google/error-prone#2062), and an IntelliJ implementation. One commenter offers to fix the existing cases. No comment names a commit for the general review.

## Code reading

- gradle/validation/error-prone.gradle:304 (origin/main 498a09aea97): `// '-Xep:ClassInitializationDeadlock:WARN', // todo check if useful or comment why not`. The check that targets this pattern is present in the list and commented out, with a todo. The build does not run it.
- gradle/validation/error-prone.gradle:525: `'-Xep:TimeInStaticInitializer:WARN'` is enabled; it is not the same check.
- solr/core/src/java/org/apache/solr/search/facet/SlotAcc.java: the SlotAcc hierarchy (Resizer, SlotContext, FuncSlotAcc, DoubleFuncSlotAcc, LongFuncSlotAcc) is the class family SOLR-16165 changed. This pass did not re-read the static initializers in that file.

Commands at the pinned head:
- git grep -n -i 'ClassInitializationDeadlock' origin/main: one hit, gradle/validation/error-prone.gradle:304 (commented out).
- git grep -n -i 'palantir\|static initializer' origin/main -- build.gradle gradle build-tools: no active check for static initializers.

## History

- SOLR-16165 fix: ccc5db9fd04 (SOLR-16165: Rare deadlock in SlotAcc initialization). Ancestor of the pinned head: yes. Changes in the same area: b73a28c90cc (PR #819), da67c104dab and d9a039729d1 (CHANGES.txt).
- Commits on origin/main naming SOLR-16167: none.

## Reproduction attempts

Not attempted. The deadlock depends on class-loading order across threads and is not reproduced by a single test run. The cheap check available is a build with error-prone enabled and ClassInitializationDeadlock on (-Pvalidation.errorprone=true); that would list the current sites. It is a full compile and was not run in this pass.

## Gap analysis

The ticket is a review task, not a single defect. The fixed instance (SOLR-16165) is in the history. The rest of the review is not done: this pass did not list the static initializers that reference subclasses across solr/. The build check exists in the error-prone list but is disabled with a todo, so the second part of the ticket is a configuration decision.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, the code paths are cited with file and line, and the attempts and the missing piece are listed. The missing pieces are (1) the list of current static initializers that reference subclasses, from a run of the ClassInitializationDeadlock check or a manual pass, and (2) a decision on enabling the check. A close verdict needs the review finished; a pr-needed verdict needs a specific site that deadlocks or a patch.

## Limits

This record does not list all the sites. It does not show that any current site deadlocks. It does not test whether enabling the disabled check produces false positives on this codebase.
