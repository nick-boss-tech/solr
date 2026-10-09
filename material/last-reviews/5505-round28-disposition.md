# SOLR-5505 pipeline disposition: DONE, PR-ready

Outcome: premise GROUNDED, gate green, shipped and pushed, GH-corroborated.
Shipped head: **44c444aa5cd38618424c504fff307ead5cf466cf** on solr-5505-submit
(two commits on base cabedd1d968: 21f8a3d0d6b fix+test with a cleaned message,
44c444aa5cd changelog; handoff doc dropped). Pushed with --force-with-lease
pinned to the received tip 5fc18d6d661f3faff4af0b73e0f0994a7d3c2eda after
re-reading it in the same step; ls-remote verified at 44c444aa5cd.

## The branch

SOLR-5505 (2013 Bug, still Open): `LoggingInfoStream` logs IndexWriter
messages as `[component][thread]: message` with no core context, so in a
multi-core setup the lines from different cores cannot be told apart. The fix
gives `LoggingInfoStream` an optional core name (messages become
`[core][component][thread]: message`; the no-arg constructor keeps the old
format), and `SolrIndexConfig.toIndexWriterConfig(core)` hands the
IndexWriter a per-core copy carrying `core.getName()`. The config object is
core-agnostic and may be shared across reloads, so the stream is tagged where
the IndexWriterConfig is built; a custom `InfoStream` passes through
unchanged. New test `TestInfoStreamLogging.testMessagesNameTheCore` captures
the INFO line with `LogListener` and asserts it names the core.

## Premise: GROUNDED, discriminating

Gate step 1 (seed 5505C0FFEE5505, both production files reverted to base
cabedd1d968, branch test kept; JUnit XML preserved at
~/workspace/tools/g5505-premise-TEST-TestInfoStreamLogging.xml):
TestInfoStreamLogging runs 2 tests with exactly 1 failure,
testMessagesNameTheCore: `Expected: a string containing "[collection1][IW]"
but: was "[IW][TEST-TestInfoStreamLogging.testMessagesNameTheCore-seed#[5505C0FFEE5505]]:
hello from the stream"`. The base line names no core, exactly the ticket's
defect. The pre-existing testIndexConfig passes on base (control). At the
shipped head the class passes 2/2 from fresh JUnit XML.

## Gate

Logs: ~/workspace/tools/g5505-gate.log (steps 0 to 5) and
~/workspace/tools/g5505-gate2.log (re-gate after the fix below).
- Step 0: changelog YAML parse rc=0.
- Tidy (:solr:core) rc=0; its only change is joining the new test's
  assertThat call onto one line, shipped in the fix commit.
- Error Prone compile rc=0.
- Focused at head (seed 5505C0FFEE5505): TestInfoStreamLogging 2/2,
  neighbor TestSolrIndexConfig 2/2, from fresh JUnit XML.
- :solr:core:check -x test rc=0 (gate 2; see the defect note).

One defect found by the gate and fixed on this side: the received
`message()` shape (both `log.info` calls nested under an `if (coreName ...)`
inside the `isInfoEnabled` guard) fails `:solr:core:validateLogCalls`, which
requires the line before a parameterized info call to be the
`if (log.isInfoEnabled())` guard itself. `message()` was restructured so each
call sits directly under its own guard (`if (coreName == null) { if
(log.isInfoEnabled()) ... } else if (log.isInfoEnabled()) ...`). Behavior is
unchanged: same logger, same two format strings, same guard semantics;
SolrIndexConfig.java is byte-identical to the received fix commit, and the
test file differs from the received version only by tidy's line join. Gate 2
re-ran tidy, Error Prone, both focused classes (2/2 and 2/2), and check, all
rc=0, on the exact shipped tree.

Process notes: both gates ran through a per-branch blocking-flock runner
(g5505-gate.sh, g5505-gate2.sh) on the shared test-queue lock; gate 1 waited
about 5 minutes behind the 15003 alias chain (contention, not a stall). No
VM replacement touched this branch. The gate ran on the received tree minus
the handoff doc (removed in the worktree before gating), so the gated tree
is the shipped tree; the diff of the shipped head against the received tip
shows only the doc removal, the validateLogCalls restructure, and the tidy
line join.

## GH corroboration

Run **37629157392** (ci/5505-infostream-r28,
org.apache.solr.core.TestInfoStreamLogging, :solr:core,
-Ptests.seed=5505C0FFEE5505; FQCN grepped in one call, add composed in a
separate call) completed **SUCCESS**; job steps verified via the API
(prepare job success; the test job's "Run focused tests" and "Upload test
results" steps both success, no runner defect). The ci branch head
93a197c6ce25 has the shipped head 44c444aa5cd as its parent.

## Draft PR description

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-5505

## What happens today

With `<infoStream>true</infoStream>` in solrconfig.xml, IndexWriter messages
are logged through `LoggingInfoStream` as `[component][thread]: message`.
Nothing in the line names the core, and merge threads carry no core context
either, so in a multi-core setup the lines from different cores land in the
same log indistinguishable from each other. The ticket asks for the core
name to be prepended automatically.

## What this change does

`LoggingInfoStream` gains an optional core name; when set, every message is
logged as `[core][component][thread]: message`, and the no-arg form keeps
the current format. `SolrIndexConfig.toIndexWriterConfig(core)` hands the
IndexWriter a per-core `LoggingInfoStream` carrying `core.getName()`. The
config object itself is core-agnostic and may be shared across reloads, so
the tagging happens where the IndexWriterConfig is built rather than on the
configured stream. Custom `InfoStream` implementations are passed through
unchanged. Behavior change, stated openly: with the infoStream enabled, the
log line format gains the leading `[core]` segment.

## Proof

`TestInfoStreamLogging` gains `testMessagesNameTheCore`, which builds an
IndexWriterConfig for the test core and asserts the logged line names that
core. On the base code (cabedd1d968) the class runs 2 tests with exactly 1
failure: the polled message is `[IW][<thread>]: hello from the stream` and
contains no core name; the pre-existing `testIndexConfig` passes on base.
With this change both tests pass, and the neighboring `TestSolrIndexConfig`
passes 2/2 (seed 5505C0FFEE5505; verified 2026-10-07 at 44c444aa5cd).
GitHub Actions corroboration: run 37629157392, TestInfoStreamLogging,
success.

## Limits

The configured `SolrIndexConfig.infoStream` instance itself stays core-less
(it backs the `infoStreamEnabled` flag in the config's map form); only the
stream handed to each IndexWriter is tagged, which is the path IndexWriter
logging actually takes. The core name is fixed when the IndexWriterConfig
is built, so a core rename takes effect on the next reload, the same point
at which a new IndexWriterConfig is created anyway. There is no switch to
keep the old format while the infoStream is enabled; the pre-change format
remains in effect only when the infoStream is not enabled. Happy to add an
opt-out or a configurable prefix if maintainers prefer one.

Changelog: `changelog/unreleased/SOLR-5505-infostream-core-name.yml`

### AI assistance

AI agents assisted with research, implementation, review, and drafting.
Nick Shanin directed the work and takes responsibility for this
contribution.

## Left for Nick's decision

Only the standard one: whether and when to open the PR.
