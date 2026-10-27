# SOLR-17914: create collection form built to the new design (round 1)

Assignment: `assignments/solr-17914-new-design-form-round-1.md`. Claim: `claims/solr-17914-new-design-form-round-1.md`. Host: vm4. No PR opened, nothing posted to Jira or GitHub.

## Head

- Branch `solr-17914-create-collection-dialog-new-design` on the fork, at `125448970db256fdecb802733abbde7e20d2b1a2`. It is a new branch from the stacked tip `5ea0055190`. The former-design branch `solr-17914-create-collection-dialog-stacked` is unchanged.
- Commits: `e4149fc10a` removes `pr-draft/SOLR-17913.md` and `pr-draft/SOLR-17914.md`. `27297913d9` builds the form to the new design. `125448970d` makes the form scroll so Create stays reachable with the advanced section open.
- Diff against `5ea0055190`: 6 files, 385 insertions, 102 deletions.

## Gate

- Command: `./gradlew -p ui :shared:jvmTest :shared:spotlessCheck --no-daemon --rerun-tasks`, with the software renderer, under `/workspace/tools/.gradle-vm4.lock`. Build successful in 1m 25s.
- JUnit: 57 tests, 0 failures, 0 errors, 2 skipped (`UserAuthenticationContentTest`, `StartContentTest`, both outside this change). `CreateCollectionFormTest` 8 of 8. `CollectionDetailsCardTest` 2 of 2.
- Logs: `/workspace/gates/logs/vm4/solr-17914-new-design/`.
- Two earlier runs are not counted. One at `9db08afff6` failed to compile on an unresolved import; its logs were overwritten. One at `27297913d9` failed `testCreatePassesTheTypedValuesAndAdvancedRouter` (no callback), logs in `solr-17914-new-design.run1/`. The cause is inferred: with the advanced section open, the Create button is probably below the test window. The scroll commit fixed that run, but the cause was not measured.

## Mutation check (ladder rung 2)

Two rules were broken in the worktree, then the file was restored (`git status` clean afterwards).

| Mutant | Tests that failed |
| --- | --- |
| PULL-only allowed (`nrtCount + tlogCount >= 0`) | `testRejectsPullOnlyCollection` |
| Any name allowed (`Regex(".*")`) | `testRejectsNamesTheServerRejects`, `testCreateIsDisabledWithoutAName` |

Logs: `/workspace/gates/logs/vm4/solr-17914-new-design-mutation/`. The other new assertions (defaults, stepper, advanced toggle, typed values with router) are behavioral checks on the callback values, not validation rules. They are not labelled as proofs of a rule.

## Decisions (ladder)

| # | Item | Built as | Rung | Source |
| --- | --- | --- | --- | --- |
| D1 | Name rule | Client check `^(?!-)[._A-Za-z0-9-]+$`, the server pattern. Helper "Letters, digits, _ and -" kept as drawn. | 1 | `SolrIdentifierValidator` in solrj |
| D2 | Label "Name" | As drawn | 3 | new design |
| D3 | Shards stepper, default 2, minimum 1 | As drawn. Minus and plus buttons; typed value also accepted. | 3 | new design |
| D4 | NRT 2, TLOG 0, PULL 1; replication factor field removed | As drawn | 3 | new design |
| D5 | Replica validity: each count 0 or more; NRT + TLOG at least 1 | As the old UI rule that rejects PULL-only | 1 | `collections.js`, `addCollection` |
| D6 | Placement | Read-only field showing "Any node, spread across zones (recommended)". No menu. | 4 | Only one option is drawn; no other option is invented. Not sent to the caller. |
| D7 | Advanced row | Collapsed by default, text as drawn. Expands to Router and Route field. | 4 | new design row; router values from the old UI |
| D8 | Router values | `compositeId` (default) or `implicit`; route field sent only when not blank | 1 | old UI router options |
| D9 | Button label "Create Collection" | As drawn | 3 | new design footer |
| D10 | Callback | `onCreate(CreateCollectionInput)` with name, configset, shards, NRT, TLOG, PULL, router, route field | 4 | the create request is not in this change, so the caller needs all typed values |
| D11 | Error texts | Caller supplies `nameError` and `generalError`. None drawn in Figma. | 3 | ticket acceptance criteria; `reports/figma-christos-17914-nodes-round-1.md` item 4 |

## Drawn in the new design but not built

- **Placement preview card** ("6 replicas on 6 nodes in 3 zones", shard badges, survival text, disk estimate). It needs node and zone data the new UI does not have.
- **"Preview configset" link** and the configset usage count "(used by 3)". No drawn target and no data source.
- **"Which replica type do I need?" link.** No drawn target.
- **Placement options beyond the one drawn.** Not drawn, not invented.
- **Advanced row content beyond router and route field.** The row text says "properties, async". Neither is drawn expanded, and neither is built. The row text is kept as drawn, so the label promises more than the panel shows.
- **Name-exists check.** Not built. The new UI has no collections data. The form shows the caller's `nameError`. The old UI never implemented this check either (`collections.js`, `@todo detect whether core exists`).
- **Create request and collections screen.** Not built. This was out of scope in the assignment.

## Deviations from the drawing

- Configset dropdown label comes from the existing `ConfigsetsDropdown` ("Configsets"), not the drawn "Configset".
- Advanced panel expands inline. No animation, no design for its open state.
- Form scrolls as a whole, so the Create button stays reachable with the advanced section open. The design shows no scroll behavior.

## PR questions for the maintainer or design

1. The helper "Letters, digits, _ and -" omits the period, which the server allows (`SolrIdentifierValidator`). Should the helper list periods, or should the rule drop them?
2. Placement: which options exist besides "Any node, spread across zones (recommended)", and what does each send?
3. Advanced: what are "properties" and "async" (control types, defaults, and what the create request sends)?
4. Name-exists: who checks the name, and when? The form only displays it.
5. Error texts for failed create and general error: drawn nowhere, so who writes them?

## Not done

- No PR. No Jira or GitHub post. No change to `solr-17914-create-collection-dialog-stacked`.
- The placement preview card, the configset link and usage count, and the replica help link are not built (see above).
