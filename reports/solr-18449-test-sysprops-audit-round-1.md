# SOLR-18449 test system property audit, round 1

Ticket: SOLR-18449, "Ensure test oriented system properties start with test." (reporter Eric Pugh, Open, Minor, created 2026-09-15). The ticket's example is `test.solr.cloud.replica.autorecovery.skip.enabled`, which is not in the tree at the pinned SHA. The ticket also says such properties should not appear in the `solr.properties` doc.
Pinned main: `e59ce3c97ddb` (apache/solr main, committed 2026-10-26 19:44 -0400).
Scope: system property names read or set by Java code under `solr/*/src/test`, `solr/test-framework`, and the randomized-test runner config under `gradle/testing`, compared with names read by main Java code under `solr/*/src/java`.
Method: rung 1 only. A regex over `System.getProperty`, `System.setProperty`, `System.clearProperty`, `systemSetProperty*`, `systemClearProperty`, and `getProperty`/`setProperty` call sites with a literal name. Command outputs are reproduced below. No run was needed; no finding depends on a runtime result.

## Census

Distinct names read or set in test trees: 266. Distinct names read or set in main Java: 142. Names that appear only in test trees (the "test-only" set): 178.

Prefix census of the 178 test-only names:

| Prefix | Names | Notes |
|---|---|---|
| `test.` | 2 | `test.ccjoin.solr.url.` (`CrossCollectionJoinQueryTest.java:164`), `test.solr.data.home` (`DirectoryFactoryTest.java:54`) |
| `tests.` | 5 | Randomized-test runner knobs, for example `tests.COMPRESSION_MODE` (`TestCodecSupport.java:121`), `tests.ssl` (`RandomizeSSL.java:132`), `tests.src.home` (`ExternalPaths.java:81`) |
| `solr.tests.` | 15 | Solr test-harness knobs, for example `solr.tests.numeric.dv` (21 sites, `TestJoin.java:49`), `solr.tests.cloud.cm.enabled` (`ChaosMonkey.java:77`), `solr.tests.mergeScheduler` (`SolrTestCaseJ4.java:463`) |
| `solr.test.` | 10 | For example `solr.test.fieldLimit.warnOnly` (`NumFieldLimitingUpdateRequestProcessorIntegrationTest.java:42`), `solr.test.sys.prop1` (`TestConfigPropertySubstitution.java:35`) |
| `solr.` (other) | 78 | Mostly product config names set by tests and substituted into test configs, for example `solr.autoCommit.maxTime`, `solr.statsCache`, `solr.similarity`, `solr.ulog.numRecordsToKeep`. Renaming these would break config substitution. |
| other | 68 | Fixture names, for example `dataDir1`, `foo.foo1`, `CONFIG_TEST`, `onInit`, plus JDK and AWS names (`java.home`, `aws.accessKeyId`). |

Test-harness knobs, the names this ticket is about, are the first four rows: 32 names in three prefixes (`test.`, `tests.`, `solr.tests.`, `solr.test.`). Only 2 of 32 use the literal `test.` prefix the ticket asks for.

## Findings

### F1. The code has no single prefix; the ticket's `test.` is the least used

- Harness knobs use `solr.tests.` (15 names, including `solr.tests.numeric.dv` with 21 call sites, first at `TestJoin.java:49`), `solr.test.` (10 names), `tests.` (5 names), and `test.` (2 names).
- The ticket asks for `test.`. Applying it literally means renaming 30 test-only names across 31 files, including the `solr.tests.` family that `SolrTestCaseJ4.java:463` and `ChaosMonkey.java:77` read.
- This is a choice of prefix, not a bug. Which prefix to standardize on is the maintainer's call; this unit does not pick one.

### F2. The ticket's example name does not exist; the nearest real name is a main-code knob

- `test.solr.cloud.replica.autorecovery.skip.enabled` has zero hits in the tree (`git grep` at the pinned SHA).
- The name that tests use is `solrcloud.skip.autorecovery`. It is read by main code at `ZkController.java:159`, `DefaultSolrCoreState.java:50`, and `SyncStrategy.java:52`, and set by at least five test classes (for example `ClusterStateUpdateTest.java:35`, `LeaderElectionIntegrationTest.java:35`). It is a product-level flag that tests set, so it is not test-only and should not be renamed by this ticket.

### F3. Nothing test-oriented is in the Ref Guide

- `git grep` for `solr.tests?.`, `tests.`, and `test.solr` under `solr/solr-ref-guide` returns no hits. `skip.autorecovery` returns no hits either. The "should not be in solr.properties" concern is not present at this SHA.

## Not flagged

- The 78 `solr.*` names and the 68 fixture names are not renamed. They are product config keys or fixture data, and a `test.` rename would not describe them.

## Answer to the ticket's ask

The ticket asks that test-oriented properties start with `test.`. Measured against the code: 2 of 32 harness knobs already do. The rest use `solr.tests.`, `solr.test.`, or `tests.`. The concrete list to rename, if the maintainer picks `test.`, is the 30 test-only names in those three prefixes, with file and line for each (the 2 `test.` names already conform and are excluded).

## Verdict

Findings are concrete and the census is reproducible. The rename itself needs the maintainer to pick a prefix. No draft is proposed as a closure; the draft asks which prefix to standardize on and offers the list.

Not checked in this unit: whether any `solr.test.*` name is read from an XML config file rather than Java (only Java call sites were scanned); whether any open PR already renames these names (no PR found by search for `SOLR-18449`, checked 2026-10-27 01:50Z).

## Reproduce

`git grep -n -E '(System\.getProperty|System\.setProperty|System\.clearProperty|systemSetPropertyS?|systemClearProperty|getProperty|setProperty)\(\s*"[^"]+"' e59ce3c97d -- 'solr/*/src/test/*.java' 'solr/test-framework/*.java'`
`git grep -n 'solrcloud.skip.autorecovery' e59ce3c97d -- solr`
`git grep -n -E 'solr\.tests?\.|test\.solr' e59ce3c97d -- solr/solr-ref-guide`
