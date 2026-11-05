# Gate: SOLR-18615 packaged Admin UI cache coverage, round 1

Status: OPEN

- Required tags: gate, bats, review, records.
- Intended host: any capable Linux gate host with JDK 21, the branch's pinned Gradle wrapper/build dependencies, Bash, curl, awk, sed, cmp, Python 3 and Node/npm access for the real generated JS client. No browser or Docker daemon is needed by this focused BATS suite. Serialize behind the host build lock with disk/inode guards.
- Public PR: https://github.com/apache/solr/pull/5300 (SOLR-18615).
- Contributor parent O: `090c4dc298a521466a120d1f3c0f676398c36534`.
- Fork branch: `nick-boss-tech/solr:pr-5300-solr-18615-cache-tests`.
- Exact positive head H: `0bae701e819706680809b253e94b637faf2c330e`, separately read back from the fork ref after the plain push.
- Canonical H BATS Git blob: `125a62a8bb6eb005274c74715c6192e4eb8e89ff` (compare Git objects or Linux LF hashes; Windows checkout line endings may differ).
- Implementation claim: `claims/pr-5300-cache-tests-windows-round-1.md`.
- Implementation report: `reports/pr-5300-cache-tests-round-1.md`.
- Execution host's receipt: `receipts/PR-5300-cache-tests-round-1.md`; add an execution completion/failure report and retain fresh log/XML/TAP artifacts and hashes under a uniquely named evidence directory.

Nick explicitly requested deeper review, these tests on a branch, and a gate handoff. H changes only `solr/packaging/test/test_adminconsole_urls.bats`. There is no newly established production bug: original O and expanded H suites should both pass. Do not call an O pass a failed reproduction. No public comment, PR edit, review submission, JIRA action or contributor-branch change is part of gate execution.

## Claim, exact head and repair rules

Read the live board rules and competing claims/receipts, verify the fork ref still equals H, then perform a detached tidy preflight before claiming execution. Record the host, JDK, cache, build lock, worktrees, runner and exact log/XML/TAP paths. Use clean separate worktrees for O, H, M1 and M2. Preserve the real generated `libs/solr/index.js`; no `-PdisableJsClient=true`, placeholder client, servlet replacement or hand-edited distribution may satisfy the positive leg. Do not reuse the stale upstream pull-ref head recorded in the first review; O is the verified contributor parent.

At clean H require `./gradlew tidy` exit 0 with no diff, and `git diff --check`. If tidy or a source/test repair changes H, commit with Nick's ICLA identity, plain-push to this fork branch, read back the new SHA, and add a superseding gate before collecting proof. An old-head run cannot certify the repaired head. Record/investigate every executed failure before a repaired rerun. A pre-test environment refusal may be relaunched once under WORKFLOW.md. Do not weaken assertions, silently exclude tasks, or treat setup/build failures as negative proof.

## Baseline and expanded packaged-server tests

From O, run:

```bash
./gradlew :solr:packaging:integrationTests --tests test_adminconsole_urls.bats -Ptests.seed=18615C0DE
```

Require exactly 3 executed BATS tests, zero failures/errors/skips and fresh JUnit/TAP. Archive `solr/packaging/build/test-output/` and the command log before another run can overwrite them.

Run the same command from clean H. Require exactly 5 executed BATS tests, zero failures/errors/skips. The three changed/new test names are:

- `assert admin console static assets require revalidation`: successful startup, HTTP 200 and `no-cache` on HEAD for the generated JS client and existing CSS asset.
- `assert admin console static assets support conditional requests`: nonempty GET at HTTP 200, configured cache policy and a parsed Last-Modified validator, matching If-Modified-Since returning HTTP 304 with an empty body and retained `no-cache`, then an old validator returning HTTP 200 with the full identical JS bytes.
- `assert admin console references a versioned client asset`: successful actual HTML response, a nonempty client version query with no unresolved `${version}`, no bare JS/CSS reference, and HTTP 200 when following the extracted versioned generated-client URL.

The two existing launch/CSP tests must also pass. BATS uses no randomized test seed; record the actual randomly selected ports. `18615C0DE` pins any randomized Java build/test work and the neighbor leg. This is HTTP/distribution proof, not a real-browser cache upgrade experiment.

## Neighbor and root checks at H

```bash
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.CacheHeaderTest --tests org.apache.solr.servlet.NoCacheHeaderTest -Ptests.seed=18615C0DE
./gradlew check -x test
```

Require both named API-cache classes to execute all their methods successfully, with no skips/failures/errors and fresh XML. Record the actual counts. These APIs use a separate servlet/cache contract from static resources. Require root check exit 0 with no additional exclusions, and final clean H with local and fork refs still H. Record environment conditions beside their results; prior upstream green checks do not substitute for these legs.

## Discriminating coverage mutations

M1 and M2 each start from H in their own isolated worktree and retain the exact H BATS bytes. They are synthetic production/configuration mutations and must never be pushed onto the offered branch. Save the script below outside the checkout and execute with `cache-policy` for M1 and `client-version` for M2:

```python
from pathlib import Path
import sys

mode = sys.argv[1]
assert mode in ('cache-policy', 'client-version')
if mode == 'cache-policy':
    p = Path('solr/webapp/web/WEB-INF/web.xml')
    old = '<param-value>no-cache</param-value>'
    new = '<param-value>max-age=3600</param-value>'
else:
    p = Path('solr/webapp/web/index.html')
    old = '<script src="libs/solr/index.js?_=${version}"></script>'
    new = '<script src="libs/solr/index.js"></script>'
text = p.read_text()
assert text.count(old) == 1
p.write_text(text.replace(old, new, 1))
```

Stage only the intended synthetic source edit to satisfy the build's working-copy guard, and record the mutation diff, tree, unchanged BATS hash, command, exit and fresh XML/TAP/logs. Run the same packaging integration command in each worktree so the mutated WAR/web.xml or index.html is actually rebuilt into that worktree's distribution. Archive results separately. Verify the packaged file contains the intended mutation; do not substitute another worktree's binaries.

- M1: require 5 tests executed, exactly 2 intended failures, at the `no-cache` assertion in the static HEAD test and the initial GET header assertion in the conditional-request test. HTTP 200 startup/delivery checks before those assertions must succeed. The launch, versioned-client and CSP tests must pass.
- M2: require 5 tests executed, exactly 1 intended failure, at the positive versioned-client-reference assertion. The page must return HTTP 200 and contain HTML first. The launch, static-header, conditional-request and CSP tests must pass.

Compilation, startup failures, unavailable bundles, absent tools, wrong head, skipped tests or arbitrary assertion failures are not discriminating proof. These two mutations demonstrate policy and version-reference detection only. The extra HTTP-status/body/304/old-validator assertions are coverage pins unless a separately documented mutation actually discriminates them; do not claim the whole suite is mutation-proven.

## Acceptance and receipt

GREEN requires O baseline, all H tests, H API neighbors, clean H tidy/root checks, and both exact intended mutation outcomes. The receipt records O/H/M1/M2 heads and synthetic trees, diffs, test/file/artifact hashes, commands, seeds, counts, actual assertion failure sites, toolchain/conditions, fresh XML/TAP/log paths and cleanup. Retain HTTP diagnostics on a failure. Source review alone does not satisfy this gate. A posted job does not mean any Linux host has claimed it. Until the complete receipt exists at the offered H, state remains **Implemented, Proof Pending**.
