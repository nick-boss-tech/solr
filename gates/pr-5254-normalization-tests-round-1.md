# Gate: SOLR-18432 prerelease normalization tests, round 1

Status: OPEN. Immutable once posted. Proves only the pinned positive head below.

- Required tags: gate, review, records. Intended host: any capable Linux CPU gate host with JDK 21, Git, Python 3, this branch's Gradle 9.7.0 wrapper distribution and build dependencies. Serialize behind the host build lock; follow its normal disk/inode guards.
- Fork: nick-boss-tech/solr. Branch: solr-18432-submit.
- Exact positive head F: 378248b76b7782ddb8037ab692f4211eb666336c. Verify the live fork ref before running.
- Original contributor head O: d59169c9cc96dd60c976079d18ef4da56024bb26. F is one ordinary test-only commit above O.
- Public PR: https://github.com/apache/solr/pull/5254, dsmiley/solr:SOLR-18432-solrversion-prerelease. Its head remains O at handoff. Do not modify that branch or public PR.
- Implementation claim: claims/pr-5254-normalization-tests-windows-round-1.md.
- Report: reports/pr-5254-normalization-tests-implementation-round-1.md.
- Receipt destination: receipts/PR-5254-normalization-tests-round-1.md, owned by the execution host. Add a completion/failure report as well; leave the immutable job and claim unchanged.
- Windows status: test patch pushed, proof pending. Static diff review only; no Gradle task, formatter, compilation, or test ran on Windows. Existing upstream green CI covers O, not F's added tests.

Nick requested additional tests after a read-only review found no blocking defect. This is coverage of existing intended behavior, not a claimed production fix. New tests are in solr/api/src/test/org/apache/solr/util/TestSolrVersion.java. No production seam is added to inject SolrVersion.LATEST. This gate does not prove a snapshot node joining a release cluster.

## Claim and repair rules

Read current WORKFLOW.md and START-HERE.md, check for a live competing gate claim and verify F. Follow the detached tidy preflight before claiming execution. Record host, JDK, cache, build lock, runner, positive and mutation worktrees, and log/XML paths in an add-only claim. Use fresh detached worktrees and keep every mutation separate from positive F.

If tidy changes files, commit the formatting repair using Nick's identity, plain-push it to the fork, and add a superseding gate naming the new exact head before executing proof. Apply the same rule to any source/test repair. Do not silently rerun an executed-test failure: record and investigate it first, then repair under WORKFLOW.md. A pre-test environment refusal may be relaunched once under that workflow. No proof at an older head covers a changed head.

## Positive proof

At clean F, run `./gradlew tidy` and require rc 0 with no tracked changes. Record head/tree and git diff --check. The existing contributor changelog is unchanged; this test-only extension adds no changelog fragment.

Run the focused API class:

```bash
./gradlew :solr:api:cleanTest :solr:api:test --tests org.apache.solr.util.TestSolrVersion -Ptests.seed=18432C0DE
```

Require twelve executed tests, zero failures/errors/skips, and fresh XML. Eight tests come from O; these four are new:

- testPrereleaseRangeBoundaries
- testWithoutPrereleasePreservesBuildMetadata
- testWithoutPrereleaseOnRelease
- testWithoutPrereleaseOrdering

Retain fresh TEST-org.apache.solr.util.TestSolrVersion.xml and its output before subsequent cleanTest tasks overwrite it.

Run the affected core neighbor:

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.admin.NodeSystemInfoProviderTest -Ptests.seed=18432C0DE
```

Require two executed tests, zero failures/errors/skips, and fresh XML. This confirms the contributor's full-version fallback remains green with F.

Run root validation:

```bash
./gradlew check -x test
```

Require success; record any environment condition or setup repair accurately. No additional task exclusions are authorized. End positive proof with git diff --check, clean tracked/untracked state, and local head/tree and remote fork ref still matching the final pinned head.

## Discriminating mutation proof

The tests exercise the new withoutPrerelease API, which is absent before the contributor's PR. Do not overlay them on pre-PR production and call a compile failure fail-before proof. Instead use the following four compiling, isolated mutations at F. The unchanged tests must pass at F and fail at their specified assertions on the mutations. These are synthetic regression checks, not reproduced defects in O.

Save the following as prepare-mutant.py outside the source tree, then run it from each fresh mutation worktree at F with the indicated argument. It changes exactly one return statement in SolrVersion.java and no test. Retain the resulting one-file diff, synthetic tree/hash, base F and source/test SHA-256 hashes.

```python
from pathlib import Path
import sys

p = Path('solr/api/src/java/org/apache/solr/client/api/util/SolrVersion.java')
s = p.read_text()
normalizer = '    return new SolrVersion(version.withClearedPreRelease());'
mutations = {
    'range': (
        '    return version.withClearedPreRelease().satisfies(semVerExpression);',
        '    return version.satisfies(semVerExpression);',
    ),
    'noop': (normalizer, '    return this;'),
    'metadata': (
        normalizer,
        '    return new SolrVersion(version.withClearedPreReleaseAndBuild());',
    ),
    'patch': (
        normalizer,
        '    return SolrVersion.forIntegers(version.getMajor(), version.getMinor(), 0);',
    ),
}
old, new = mutations[sys.argv[1]]
assert s.count(old) == 1, 'Mutation anchor must occur exactly once'
p.write_text(s.replace(old, new, 1))
```

### A: bypass range normalization

Prepare with argument range. Run:

```bash
./gradlew :solr:api:cleanTest :solr:api:test --tests org.apache.solr.util.TestSolrVersion.testPrereleaseRangeBoundaries -Ptests.seed=18432C0DE
```

Require exactly one executed test and one assertion failure: the SNAPSHOT candidate no longer satisfies >=10.1.0. Compilation, assumptions, setup failures or a passing method are not proof. The remaining strict comparison/equality assertions in that method are compatibility pins; do not claim they were reached in this failed run.

### B: disable prerelease removal

Prepare with argument noop in another fresh F worktree. Run:

```bash
./gradlew :solr:api:cleanTest :solr:api:test --tests org.apache.solr.util.TestSolrVersion.testWithoutPrereleasePreservesBuildMetadata --tests org.apache.solr.util.TestSolrVersion.testWithoutPrereleaseOrdering -Ptests.seed=18432C0DE
```

Require exactly two tests and two assertion failures. The first method still observes rc.1 in the purported normalized string. The second sees the SNAPSHOT version as less than the same numeric release. Preserve the actual first failure in each method rather than claiming later assertions executed.

### C: discard build metadata

Prepare with argument metadata in another fresh F worktree. Run:

```bash
./gradlew :solr:api:cleanTest :solr:api:test --tests org.apache.solr.util.TestSolrVersion.testWithoutPrereleasePreservesBuildMetadata --tests org.apache.solr.util.TestSolrVersion.testWithoutPrereleaseOnRelease -Ptests.seed=18432C0DE
```

Require exactly two tests and two assertion failures because the expected +build.123 suffix is missing. The already released case without metadata should reach its assertions successfully before the second loop iteration fails. Repeat-normalization and original-object preservation assertions are compatibility pins; this mutation does not independently demonstrate every such assertion.

### D: collapse patch versions

Prepare with argument patch in another fresh F worktree. Run:

```bash
./gradlew :solr:api:cleanTest :solr:api:test --tests org.apache.solr.util.TestSolrVersion.testWithoutPrereleaseOrdering -Ptests.seed=18432C0DE
```

Require exactly one executed test and one assertion failure: normalized 10.1.0 is no longer less than normalized 10.1.1-SNAPSHOT because both collapse to 10.1.0. Same-release checks must pass first. Minor/major order assertions are compatibility pins, not claimed executed in this failed run.

## Completion

GREEN requires all positive legs at the final exact head, clean tidy/root check, and all four mutation legs with the stated executed counts and relevant assertion failures. Unexpected pass, extra failure, no matching tests, compilation failure, assumption skip or environment failure does not satisfy a negative leg. Record and investigate it without weakening assertions or silently rerunning.

The receipt records O/F and trees, full commands and seed, actual counts, return codes, fresh XML and log paths/hashes, mutation diffs and source/test hashes, final local/remote heads, any repairs and the limits above. Distinguish tests that discriminate a mutation from compatibility pins. No public comment, JIRA write, PR change, approval or merge is part of this gate. A completed receipt is the handback; a job posted OPEN is not a claimed or completed execution.
