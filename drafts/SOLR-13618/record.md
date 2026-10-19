# Skip record: SOLR-13618

- Ticket: SOLR-13618, The core admin API should fail to create a core if the instanceDir is not under SOLR_HOME or a child or ancestor of instanceDir contains core.properties.
- Verdict: keep-open.
- Draft branch: solr-13618-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the queue entry's audit (round audit-1, 2026-10-04, confirmed-skip), re-checked in the batch 06 remediation pass on 2026-10-19.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-07-10, updated 2019-12-21, no fix version, no links. The ticket asks for two checks on a core admin CREATE: (1) fail when instanceDir is not under SOLR_HOME, and (2) fail when a child or ancestor of instanceDir contains core.properties. The description is about cores outside SOLR_HOME that do not reload after a restart. The 2019 comments discuss how much latitude to give users and do not settle the rule.

## Code reading

Part 1, instanceDir outside SOLR_HOME: present at the pin.

- `solr/core/src/java/org/apache/solr/handler/admin/api/CreateCore.java:109-117` (`determineInstancePath`) resolves instanceDir under the core root directory and normalizes it; a relative value therefore reaches the check as an absolute path under that root.
- `CreateCore.java:102` calls `coreContainer.create(...)`.
- `solr/core/src/java/org/apache/solr/core/CoreContainer.java:1525` calls `assertPathAllowed(instancePath)` before the CoreDescriptor is built.
- `CoreContainer.java:1621-1622` delegates to `SolrPaths.assertPathAllowed` with the container's allow list.
- `solr/core/src/java/org/apache/solr/core/SolrPaths.java:65-79` throws BAD_REQUEST for an absolute path that is not under an allow path.
- `CoreContainer.java:431-441` builds the allow list from SOLR_HOME, the core root, the configset base directory, SOLR_DATA_HOME, and the solr.xml allowPaths.
- Test: `solr/core/src/test/org/apache/solr/core/TestCoreContainer.java:618-625` expects `/tmp` to be rejected when `/var/solr` is the allowed path. Not run in this pass.

The check was added by SOLR-14561, commit `936b9d770e` (2020-06-18, "CoreAdminAPI's parameters instanceDir and dataDir are now validated"), an ancestor of the pin. Commit `2d2e35259f` (SOLR-18058, 2026-01-09) tweaked the allowPath checks and is also an ancestor. No commit at the pin names SOLR-13618.

Part 2, child or ancestor containing core.properties: absent at the pin.

- `grep -rn 'PROPERTIES_FILENAME' --include=*.java solr/core/src/java` returns the constant at `solr/core/src/java/org/apache/solr/core/CorePropertiesLocator.java:50` and uses at `:70`, `:88`, `:119`, `:159`, `:206`. Each reads `cd.getInstanceDir().resolve(...)`, so only the instance directory itself is checked. Line `:159` is a file-walk match on the name, not an ancestor scan.
- `CoreContainer.java:1569-1571` (the create failure path) deletes the descriptor for a failed create and comments that there should never be an existing core.properties there. It does not reject an ancestor or child that has one.

## Reproduction attempts

Not run in this record. Part 1 has the unit test cited above. Part 2 would need a scenario with a core.properties in an ancestor or child of a new instanceDir; none was built or run.

## History

- `936b9d770e` SOLR-14561 (2020-06-18): added the allow-path validation of instanceDir and dataDir. Ancestor of the pin.
- `2d2e35259f` SOLR-18058 (2026-01-09): tweaked allowPath checks to avoid NPEs. Ancestor of the pin. Not for this ticket.
- `git log --grep='SOLR-13618' 498a09aea9` returns no commits. (A grep for the number alone matches a git-svn-id in an unrelated commit; it is not a reference.)

## Gap analysis

- Part 1 exists at the pin, under SOLR-14561, which does not name this ticket. Whether that closes SOLR-13618 in part is a maintainer decision.
- Part 2 has no code at the pin. The 2019 discussion does not settle whether an ancestor check should reject creates or only warn, and the change would restrict existing behavior. That is a design decision for the maintainers.
- Not close-obsolete: the feature is not removed, and part 2 is not obsolete.

## Verdict justification

keep-open. Part 1 is present at the pin, but no commit names this ticket, and part 2 is absent with no settled rule for it. A close-fixed verdict would need a maintainer ruling that part 1 alone satisfies the ticket. A close verdict of any kind needs that ruling first.

Missing for a closing verdict: a maintainer ruling on whether part 1 alone closes the ticket and whether part 2 is wanted; or, for keep-open to become a fix, a run that shows part 2 needs code.

## Limits

This record does not run the build or the unit test cited at TestCoreContainer.java:618-625. It does not check the upstream main commit cited in the bulk audit (c8ab71430f), which this pass replaces with the fork pin. It does not post anything to JIRA or GitHub.
