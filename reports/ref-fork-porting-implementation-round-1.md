# Reference fork porting branches: source implementation and ranking

Status: source phase complete; code Implemented, Proof Pending. Windows,
2026-11-06. Claim: `claims/ref-fork-porting-windows-source-round-1.md`.
Owner asked for scoped branches, ranked contribution value and an emphasis
on Overseer removal. No PR, Jira comment or default change was made.

## Ranked portfolio and verified fork refs

| Rank | Branch on nick-boss-tech/solr | Exact pushed/read-back head | Base |
| --- | --- | --- | --- |
| 1 | solr-16542-configset-port | `e08978bcabe469775dd56640c453408b902bd034` | `f489a30a31b1b97c7c8aa7a2c05c15d80114dc70` |
| 2 | solr-14927-distributed-lock-targets | `6ab0486cc20b691ed4544bf19770e5e954e427b8` | `c0c219e374611e0edc475c0f793c9bd688b13292` |
| 3 | solr-17946-submit | `dab14772d9151e90d3c69dc714370f18bc2439b0` | same Apache main base |
| 4 | solr-17293-distributed-admission-tests | `b85f3fe3476dc3bd7f4ac3dad5f92c1b27395d26` | same Apache main base |
| 5 | solr-17293-control-plane-load-plan | `1290532bcf5adfa0e1a477b0ef6dd563dd29e3bd` | same Apache main base |

Each new commit was plain-pushed to Nick's fork, with a separate GitHub ref
read-back matching the full local SHA. No existing public PR head moved.
The reference fork's live tip remained
`4ce82ee7bb425595365ef9e392179730ae4abc0d`.

## Why this ordering

1. ConfigSet migration removes a complete Overseer execution responsibility
   under SOLR-16542. The existing implementation at
   `85ce6f5475a259f61f786edcbf0058852a9f5db2` is preserved as an ancestor;
   its original branch remains unchanged. The new ordinary commit forces the
   test's intended Overseer-enabled mode and removes its dependency on a new
   accessor, making the original-production negative leg executable.
2. Resource-footprint reasoning found a concrete current-source gap:
   CollectionApiLockFactory invents a shard name when none is specified,
   allowing a split.key/count command to overlap a command naming the actual
   shard. Promote to collection scope, retain named-shard concurrency and
   test both ordering directions, unrelated work and nested ADDREPLICA reuse.
   Two outbound files, no new lock framework or API. A specific new ticket
   was not invented; SOLR-14927 is umbrella context.
3. SOLR-17946 cleanup gates eager task queue/map creation and collection
   processor construction in distributed mode. Keep `/overseer/queue` for
   QUIT and `/overseer_elect` for singletons. Exit cleanup uses the updater's
   own tenure ID; startup/prioritization remains available without the task
   queue. Preferred-node prioritization uses the existing executor to avoid
   blocking node startup. Two production files, one new test, one changelog.
4. Admission coverage reuses real distributed locks and MockOperationCmd to
   reach executor rejection, check async ID cleanup/duplicates, then drain and
   retry. Test-only; baseline and new coverage are expected to pass. No
   production defect or throughput improvement is claimed.
5. A developer Markdown experiment plan specifies both modes, PRS, independent/
   hot/skewed resources, node barriers, recovery and overload measurements.
   Includes the required AGENTS developer-doc index entry. It is a plan, not
   an implemented harness or measured benchmark result.

The ranking follows SOLR-14927/SOLR-17293 and visible maintainer direction;
it is not a recorded PMC consensus. The fork's new scheduler still runs on
one Raft leader. Importing that central executor or replacing ZooKeeper is
low fit for this program. Borrow its invariants and failure scenarios for
Apache Solr's distributed paths.

## Verification status and jobs

Windows ran no Gradle, compiler, test or benchmark. Source review, exact
outbound-file inspection, whitespace checks, clean task worktrees, fork ref
read-backs and raw diff exports were performed. These are not runtime proof.
The inherited SOLR-16542 report's compile/focused success covers its old head
only. No new gate is claimed or executed by this source session.

Post/claim the following separate Linux gate jobs; each names the exact head,
fixed seed, focused suites, Error Prone compile, tidy/module/root checks,
fresh log/XML evidence and executable negative or mutation proof:

- `gates/ref-fork-solr-16542-configset-round-1.md`
- `gates/ref-fork-solr-14927-lock-targets-round-1.md`
- `gates/ref-fork-solr-17946-disabled-queues-round-1.md`
- `gates/ref-fork-solr-17293-admission-round-1.md`

The documentation-only experiment branch has no runtime claim; its linked
test paths and basic Markdown were inspected. Executing the performance
matrix is a later, explicitly scoped Linux experiment, not part of a code
gate and not justified by a synthetic dispatcher benchmark alone.

## Review limits and integration

Correctness: bounded source invariants were assessed; runtime results are
pending. Architecture: distributed execution and existing ZK lock/async
infrastructure are retained; no new control plane, endpoint, dependency or
auth boundary. Maintenance/reuse: existing fixtures, commands, executors and
lock factories were reused. Queue cleanup includes necessary prioritization
and exit-lifecycle changes; its singleton consumer must be gated.

The ConfigSet and queue patches overlap Overseer/ZkController and have not
been composed or tested together. After ConfigSet migration, runner presence
does not identify Collection API mode. Use the explicit Overseer mode for
legacy maps, Collection routing and shutdown, keep the distributed ConfigSet
runner in both modes, preserve ancestry and gate a composed head separately.
Upgrade/legacy queue behavior remains a compatibility review item. No
transparent rolling transition or lossless queued-work migration is promised.

Local user documents and raw patches are under
`C:/Users/shaninna/dev/Solr-issues/research/solr-reference-branch/`:
`contribution-ranking.md`, `branch-inventory.md`, `diffs/` and `handoff/`.
Pinned GitHub comparisons can be formed from each base/head in the table.
Source board snapshots reviewed were
`936322de124342b38c9b6673ca9b2ca21575bc08` and
`40ad79d168d6414b2d4b296aa0ecdd12a99b41ac`; no overlapping live implementation
claim or gate was found for these new branch slices.
