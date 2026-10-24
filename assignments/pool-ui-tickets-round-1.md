# Pool assignment: new Admin UI tickets (ui/, Compose Multiplatform), round 1

Status: OPEN. Claim one ticket at a time as `claims/pool-ui-SOLR-NNNN.md` before starting (WORKFLOW.md).

- Capability tags: ui, jvm-tests.
- Intended hosts: any pool host with no other claimed work. One ticket per claim.
- Posted by: vm2, 2026-10-19, at the direction of Nick.
- Owner complaint 5 applies: do not take the same ticket type as the previous claimant if another type is open.

## Setup (already on this branch)

`material/ui-test-setup/` runs the new UI's JVM tests on hosts without a GL driver. Use it:

    REPO=<checkout> material/ui-test-setup/run-ui-jvm-tests.sh org.apache.solr.ui.views.<area>.<TestName>

Also run `./gradlew -p ui :shared:spotlessCheck`. Set `JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1` and `GRADLE_USER_HOME=/workspace/gradle-home`.

The first gate is green on vm2 for `FileContentViewerTest` (3 of 3) at `solr-17911-file-content-viewer` commit 8047b99ef09.

## Decision: GL assumption

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Does the change touch GL or custom graphics? | Default: no. The fixes are layout, state, navigation, and text. | If a change uses `Canvas`, `DrawScope`, `drawWithCache`, `graphicsLayer`, shaders, or platform rendering, flag it `gl-blast-radius` in its claim and draft PR. Such a change is not gated by the software-renderer tests alone. Hold it until a GL host or CI run passes. | Nick |
| Test renderer | decided: software raster via the setup above | A GL-only bug will not show here. | Nick |

No row may leave the choice to the claimant.

## Tickets

| Ticket | Summary | Status at posting | Dependency | Tag | Notes |
| --- | --- | --- | --- | --- | --- |
| SOLR-17911 | File content viewer | Branch `solr-17911-file-content-viewer` at 8047b99ef09, 3 of 3 tests pass; not yet a claim | none | component | Needs a reviewer, then a PR draft under `pr-draft/`. |
| SOLR-17887 | File tree component | Open, no code | none | component | Reusable tree like an IDE file list, with a chevron and single-click expand. Pair with 17911 for the Configsets Files screen. |
| SOLR-17913 | Collection details card | Open | needs a collection domain model | component | Display only. Define the model in the claim first. |
| SOLR-17909 | Breadcrumb component | Open, criteria stale | criteria rewrite | component | Its criteria mention Decompose childstack. The baseline removed Decompose, so rewrite the criteria in the claim before coding. |
| SOLR-17849 | Index & Query header (collection and handler dropdowns) | Open; the Index / Query tab exists but has no header | a screen to host it | screen | Configsets Index / Query shows only "Edit solrconfig.xml". Needs the tab to load collections and handlers. |
| SOLR-17850 | solr.SearchHandler form | Open | 17849 | screen | Too big for one claim; split in the claim if needed. |
| SOLR-17912 | Collections management umbrella | Open; Collections is disabled in the sidebar | navigation and screens | screen | Umbrella. Take a child, not the whole ticket. |
| SOLR-17914 | Create Collection dialog | Open | a Collections screen | screen | The Create Configset dialog already exists and is a model. |
| SOLR-17886 | Reusable components umbrella | Open, assignee Christos Malliaridis | children above | umbrella | Do not claim the umbrella; claim a child. |
| SOLR-17861 | updateLicenses task covering the KMP module | Open | none | build | Gradle task. Requires `updateLicenses resolveAndLockAll --write-locks` per AGENTS.md. |

Out of scope for this round: SOLR-12863 and SOLR-12613 (the earlier decision), and SOLR-17885 (user authentication, reopened, too large for one claim).

## Limits

- Base apache/main c8ab71430f. New `solr-NNNN-<short>` branches only, pushed to the fork after a green gate.
- Draft PR = a draft PR description file on the fork branch (`pr-draft/SOLR-NNNN.md`). Pool hosts do not open GitHub PRs.
- No JIRA or GitHub comments, no force push, and no commit trailers. Commit as Nick Shanin. No em dashes.
- Do not change another host's claim or branch.

## Deliverable

- A fork branch `solr-NNNN-<short>` with the change and a test, and the draft PR description, pushed after the gate passes.
- `claims/pool-ui-SOLR-NNNN.md` marked DONE with the gate command, the commit, and the blast-radius tag if one applies.
