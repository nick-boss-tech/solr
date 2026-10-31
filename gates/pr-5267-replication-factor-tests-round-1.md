# Gate: SOLR-18603 per-shard replication-factor coverage, round 1

Status: OPEN. Immutable once posted. Proves only the pinned final head below.

- Required tags: gate, review, records. Intended host: any available capable Linux CPU gate host with JDK 21 and this branch's Gradle wrapper/build dependencies. Serialize behind the host build lock and follow its disk/inode guards.
- Fork: nick-boss-tech/solr. Branch: solr-18603-submit.
- Final head F: b244ae94d6c84eec06bf1de5754e566b0fdb88d1.
- Contributor head O: 7f9acb9df610338c20537e1efd6ed75a6c64a13b.
- Public PR: https://github.com/apache/solr/pull/5267, isaric/solr:SOLR-18603-seal-namedlist. Its head remains O at handoff. Do not modify that branch or PR.
- Implementation claim: claims/pr-5267-replication-factor-tests-windows-round-1.md.
- Report: reports/pr-5267-replication-factor-tests-implementation-round-1.md.
- Receipt: receipts/PR-5267-replication-factor-tests-round-1.md, owned by the execution host. Add an execution completion/failure report; leave this job and the implementation claim unchanged.
- Status at handoff: Implemented, Proof Pending. No Gradle task, compiler, formatter or tests ran on Windows. Original upstream CI covers O, not the added tests at F.

Nick requested a coverage repair and pre-fix/post-fix proof. No production defect was established in the review. F changes only CloudHttp2SolrClientTest.java; all production bytes at F must match O. Both original O and test-extended F are expected to PASS. This is not a claim that O has a behavioral regression. Discriminating failures come from the two synthetic compiling mutations below, not from presenting a missing API, absent test, compile error or infrastructure failure as fail-before proof.

## Claim and repair rules

Read current WORKFLOW.md and START-HERE.md, inspect the live claim/receipt state for this job, verify the fork ref equals F, and follow detached tidy preflight before claiming execution. Record host/JDK, cache, build lock, worktrees, runner and log/XML paths in an add-only execution claim. Use fresh detached worktrees for O, F and each mutation.

If tidy changes a tracked file, commit the formatting repair using Nick's identity, plain-push to the fork, and post an add-only superseding gate with the new exact head before proof. Source/test repairs require the same new-head treatment. Record an executed-test failure and investigate before any retry; do not silently relaunch. A pre-test environment refusal may be relaunched once under WORKFLOW.md. No result at an older head covers a changed head.

## 1. Pre-fix comparison at O

Verify O and preserve its tree ID. Run the original two affected classes in a fresh O worktree:

```bash
./gradlew :solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.client.solrj.impl.CloudHttp2SolrClientTest --tests org.apache.solr.client.solrj.response.NestableJsonFacetTest -Ptests.seed=18603C0DE
```

Expect 20 CloudHttp2SolrClientTest methods and one NestableJsonFacetTest method, zero failures/errors/skips. Retain fresh XML and output before any subsequent cleanTest. The two new methods are absent at O and are not requested here. This comparison establishes the original behavior baseline, not a negative-control failure.

## 2. Post-fix proof at F

Verify the full live fork SHA equals F, record local head/tree, and require `git diff --name-only O F` to list only solr/solrj/src/test/org/apache/solr/client/solrj/impl/CloudHttp2SolrClientTest.java. A test overlay from F onto O would therefore have exactly F's tracked tree; no separate production fix is hidden in this comparison.

At clean F run `./gradlew tidy`, require rc 0 and no tracked changes, and record `git diff --check`. This test-only follow-up adds no changelog fragment and leaves the contributor's existing changelog untouched.

Run the same two classes and seed:

```bash
./gradlew :solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.client.solrj.impl.CloudHttp2SolrClientTest --tests org.apache.solr.client.solrj.response.NestableJsonFacetTest -Ptests.seed=18603C0DE
```

Expect 22 CloudHttp2SolrClientTest methods and one NestableJsonFacetTest method, zero failures/errors/skips, with fresh XML. Explicitly confirm these two methods execute:

- testShardReplicationFactors: two real shard leaders, synthetic factors 2 and 3, exact complete route header, exact complete shard map, minimum 2.
- testShardReplicationFactorsWithoutRouteFactors: omitted route factors, empty per-shard result, minimum -1, and an absent responseHeader for the per-shard API.

The changed checkSingleServer helper also checks route completeness, factor values and the public per-shard API on real routed update responses in testRouting. Other original methods cover neighboring response handling and routing. Preserve all fresh XML/output and actual test counts. If counts differ, identify the actual methods and explain the difference; do not silently reduce scope or accept skipped new methods.

Run root validation at F:

```bash
./gradlew check -x test
```

Require success; no extra task exclusions are authorized. Record material environment conditions accurately. Finish with clean tracked/untracked state, diff-check, local head/tree and live remote ref still matching the final pinned head.

## 3. Discriminating failures on synthetic production mutations

Each mutation starts in a separate fresh detached worktree at F with tests unchanged. Save this script outside the source tree as prepare-mutant.py. Record F, mutation diff, synthetic tree/source SHA-256 and unchanged test SHA-256. Never push a mutation.

```python
from pathlib import Path
import sys

p = Path('solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java')
s = p.read_text()
mutations = {
    'omit-route-map': (
        '    if (rfByRoute != null) cheader.add(REPFACT_BY_ROUTE, rfByRoute);',
        '    // Synthetic negative control: omit the per-route factor header.',
    ),
    'corrupt-shard-factor': (
        '          results.put(shard, rf);',
        '          results.put(shard, 0);',
    ),
}
old, new = mutations[sys.argv[1]]
assert s.count(old) == 1, 'Mutation anchor must occur exactly once'
p.write_text(s.replace(old, new, 1))
```

For each mutation independently run:

```bash
./gradlew :solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.client.solrj.impl.CloudHttp2SolrClientTest.testShardReplicationFactors -Ptests.seed=18603C0DE
```

- omit-route-map: exactly one executed method and one assertion failure at the exact route-map comparison (expected both route URLs and their factors, actual null). This demonstrates the new test detects missing per-route metadata.
- corrupt-shard-factor: exactly one executed method and one assertion failure at the exact shard-map comparison (expected shard1=2/shard2=3, actual both 0). The route-header comparison must pass first. This demonstrates the public reader is exercised independently of the unchanged minimum-factor shortcut.

Compilation failure, setup failure, no matching tests, assumptions/skips, an unexpected pass or an unrelated assertion failure do not satisfy a negative leg. Record and investigate without weakening tests. Additional assertions, including the missing-factor compatibility cases, remain pins unless separately shown to discriminate a mutation; do not claim every assertion is mutation-proven.

## Completion

GREEN requires successful original O comparison, complete positive proof at the final exact F, clean tidy/root check, and both specified negative controls. The receipt must distinguish coverage repair from a production fix and synthetic mutations from observed contributor bugs. Record O/F and tree IDs, commands/seeds, actual test counts and return codes, fresh log/XML paths and hashes, each mutation diff/source/test hashes, repairs/environment conditions, and final local/remote heads.

Do not mark this job green on upstream CI or a posted job alone. Return any executed failure through the repair process. No PR/JIRA comment, PR edit, approval, merge or contributor-branch push is part of this gate. Public follow-up, if later appropriate, follows Nick's standing clean-gate rules after the complete receipt is verified.
