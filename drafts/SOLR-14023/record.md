# Skip record: SOLR-14023

- Ticket: SOLR-14023, tests.src.home points at ${user.dir}" for tests (?!). JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-12-06, updated 2020-01-21, no fix versions, no links, no comments.
- Verdict: keep-open.
- Draft branch: solr-14023-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and grep run at this head (detached worktree).
- Source: remediation redo of batch 05, item 11, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (vm2, from the B2 reading) cited two lines; both are confirmed here.

## Ticket reading

The test property tests.src.home is set to ${user.dir} in the build, and the ticket says the path search in ExternalPaths is wrong: it scans upward from the working directory, and it should instead use an explicit property pointing at a distribution folder.

## Code reading and grep at the pinned head

- gradle/testing/randomization.gradle:132: `[propName: 'tests.src.home', value: null, description: "See SOLR-14023."]`. The build no longer sets the property to ${user.dir}; the value is null. This part of the ticket is addressed in the build.
- solr/test-framework/src/java/org/apache/solr/util/ExternalPaths.java:71-95, `determineSourceHome()`: it first looks for `solr/conf` on the working path and on the classpath (:73-79), and falls back to `System.getProperty("tests.src.home", ".")` at :81. It then walks upward from that directory until it finds `test-framework/build.gradle` (:84-88), and returns null if it reaches the root.
- So the upward search the ticket criticizes is still in the code. The explicit-property design the ticket proposes is not.

## History

- The "See SOLR-14023" note on randomization.gradle:132 is the change that removed the ${user.dir} value. The commit that added the note was not identified in this pass; no commit on the pin names SOLR-14023 in its message.

## Reproduction attempts

Not run. The question is whether a test run from a distribution layout resolves its source home. This pass did not run a test.

## Gap analysis

The literal complaint (tests.src.home points at user.dir) is fixed in the build. The design complaint (the ExternalPaths lookup scans upward and should use an explicit property) is not addressed, and the ticket asks for it. Whether to replace the scan is a test-infrastructure decision.

## Verdict justification

keep-open: the attempts made are the code and build reads above at the pinned head, the Jira read, and the history note. The missing piece is the design decision on replacing the upward search with an explicit property (and a distribution-folder source home), which the ticket proposes. close-fixed is not supported for the whole ticket: the literal property change is in place, but the lookup design the ticket asks to change is still there.

Missing for a closing verdict: the decision on the explicit property, or a split of the ticket into the two parts.

## Limits

This record does not run a test to check source-home resolution. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
