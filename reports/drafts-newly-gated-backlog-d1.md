# Newly gated backlog drafts, part d1 (core-admin: SOLR-4502, SOLR-12916, SOLR-16499)

Worktree head at start: `d627304e96b`. Drafts written: `pr-drafts/core-admin/SOLR-4502.md`, `pr-drafts/core-admin/SOLR-12916.md`, `pr-drafts/core-admin/SOLR-16499.md`. No commit, no push, no build, no test run. The only repository change is the fetch described under Head check.

## Receipt numbers used

SOLR-4502 (`receipts/SOLR-4502.md`):
- L4 branch tip `4491f5162c1` (before the packaging commit); L5 gated head `0ee4c644ee63` (packaging commit removes `SOLR-4502-TESTING.md`); L6 merge-base `cabedd1d9680`.
- L7 base run: `testCreateBeforeLoadIsRejected` fails with a create-time NPE (`"this.coreConfigService" is null`); assertion at `TestCoreContainer.java:257`. L7 also says the search-time NPE was not run.
- L13 focused: `TestCoreContainer` tests=26, failures=0, errors=0, skipped=3 (Windows-only path assertions).
- L14 module check `:solr:core:check -x test` rc 0. L15 gate 2026-10-10T21:23:49Z to 21:37:17Z. L16 guard placement is the owner's call; search-time NPE path not exercised.

SOLR-12916 (`receipts/SOLR-12916.md`):
- L5 gated head `50b1f4bcab03` (packaging commit removes `SOLR-12916-TESTING.md`); L6 merge-base `cabedd1d9680`; L7 branch-only files.
- L8 base run: `testFlatNameValueQueryFromConfigApi` fails, `expected:<2> but was:<0>`; `QuerySenderListenerTest` tests=2, failures=1.
- L9 Config API round trip not run (stated limit). L10 base XML tests=2, failures=1, errors=0.
- L15 focused tests=2, failures=0, errors=0, skipped=0. L16 module check rc 0. L17 gate 2026-10-10T22:08:00Z to 22:26:52Z. L18 XML path change is the owner's call.

SOLR-16499 (`receipts/SOLR-16499.md`):
- L5 gated head `51b7addddd45` (packaging commit removes `SOLR-16499-TESTING.md`); L6 proof base fork point `e2cdb2d7e8`.
- L7 base run: `testParallelAndTimeoutAreForwardedToTheOverseerMessage` fails, `expected:<4> but was:<2>`; `ReplaceNodeAPITest` tests=4, failures=1, errors=0.
- L12 focused tests=4, failures=0, errors=0, skipped=0 (timestamp 2026-10-10T22:50:03Z). L13 module check BUILD SUCCESSFUL, rc 0.
- L14 not part of the gate: reference-guide timeout default, v2 request description, OpenAPI or SolrJ regeneration check. L15 gate 2026-10-10T22:35:13Z to 22:53:44Z.

Every count and date in the three Proof sections comes from these lines. Verification date used: 2026-10-10 for all three.

## Head check

- `git ls-remote origin refs/heads/solr-4502-submit` gives `0ee4c644ee63c65cbda5752bb4e4e9fcdb70b4bf`, equal to receipt L5. Match.
- `solr-12916-submit` gives `50b1f4bcab039f8d25a729a8d02a1e8a16defd3e`, equal to receipt L5. Match.
- `solr-16499-submit` gives `51b7addddd45dfbb8e18e4f80982a3793206bdcd`, equal to receipt L5. Match.
- The gated head objects were not in the worktree or the source checkout. I ran `git -C <worktree> fetch origin solr-4502-submit solr-12916-submit solr-16499-submit`. That only updated the `origin/solr-*-submit` remote-tracking refs and `FETCH_HEAD`. No files changed.
- Drafts are written at the receipt heads. No head discrepancy for these three tickets. The SOLR-10364 packaging-delta case does not apply here.
- Merge-base checks: `git merge-base <head> upstream/main` gives `cabedd1d9680` for 4502 and 12916, and `e2cdb2d7e8` for 16499. Both match the receipts.

## Title source

- SOLR-4502: `changelog/unreleased/SOLR-4502-create-before-load.yml` at `0ee4c644`. Title says the call fails "instead of creating a core that later hits a NullPointerException on search." The receipt says the search-time NPE was not run (L7, L16). The title overstates the evidence. The draft has no title line and does not repeat that claim. Recommend trimming the fragment title before posting. This changes the branch and the head.
- SOLR-12916: `changelog/unreleased/SOLR-12916-query-sender-flat-queries.yml` at `50b1f4bc`. Title names only the Config API. The answers item (`material/core-admin-round-1-answers.md`, branch corrections item 7; `reports/core-admin-round-1-k1.md` finding 8) asks for both input paths in the title. That is not done at head. The draft states the XML change in "What this change does."
- SOLR-16499: `changelog/unreleased/SOLR-16499-replacenode-parallel-timeout.yml` at `51b7addd`. Title is accurate for the pass-through. The draft uses it as the changelog link only.

## Citation checks

All citations were read at the named SHA with `git show <sha>:<path> | cat -n`. Head links use the head SHA. Pre-change symptom links use the merge-base, and the text says so.

SOLR-4502:
- Head `CoreContainer.java` 1503-1507 (guard). Read.
- Base `CoreContainer.java` 856 (`coreConfigService` set in `loadInternal`, which runs from 756), 1674 (`coreConfigService.loadConfigSet`), 1557-1592 (catch that wraps as `BAD_REQUEST`). Read.
- Head `TestCoreContainer.java` 249-261 (new test; assertion at 257 matches L7). Read.
- Head `SyntheticSolrCore.java` 53-69 (`createAndRegisterCore` calls `registerCore` with no check). Read. Caller at `CoordinatorHttpSolrCall.java` 109-110.
- `shardHandlerFactory` is assigned once, at head `CoreContainer.java` 789 (in `loadInternal`). Checked by grep.
- Jira: the 2014-12-31 Alan Woodward comment in `research/jira-context/SOLR-4502.json` proposes `load()` from the constructor. Read.
- SERVER_ERROR as HTTP 500 is the standard SolrException mapping. Not checked in code in this pass.

SOLR-12916:
- Base `QuerySenderListener.java` 107-121 (current drop path). Read.
- Head `QuerySenderListener.java` 107-126 (new branches, flat top level at 108-110, nested at 118-119, XML loop) and 141-158 (helpers). Read.
- Head `QuerySenderListenerTest.java` 50-67 (new test). Read. Existing case 27-48 shows XML queries are an `ArrayList` of `ArrayList`, which is why the XML `<arr>` reaches the flat branch.
- Jira `research/jira-context/SOLR-12916.json` describes the List-of-List deserialization. Read.

SOLR-16499:
- Reference guide, head `cluster-node-management.adoc` 838-874 (`parallel` at 838; `timeout` entry, default 300 seconds, at 867-874). Read.
- Head `ReplaceNodeCmd.java` 55-56 (defaults 600 and false). Read.
- Base `ReplaceNode.java` 72-75 (only target and waitForFinalState copied). Read.
- Base `CollectionsHandler.java` 992-996 (v1 request built without the two params). Read.
- Head `ReplaceNodeRequestBody.java` 57-68 (new fields; the `timeout` description says "per replica move"). Read.
- Head `ReplaceNode.java` 79-80; head `CollectionsHandler.java` 997-998. Read.
- Head `ReplicaMigrationUtils.java` 105-109 (add message has no timeout), 182 and 191 (batch waits on `timeout`). Read.
- Head `AddReplicaCmd.java` 129 (timeout default 600), 193-220 (`parallel` and `waitForFinalState` branches; executor when parallel and not waitForFinalState), 208 (wait on `timeout`). Read.
- Head `ReplaceNodeAPITest.java` 57-75 (new test). Read.
- Jira `research/jira-context/SOLR-16499.json`: 2022-10-26 Noble Paul comment ("I guess it should be a mistake that these params are omitted. We should pass these parameters on to overseer"). Read.

## Choice-section decisions

All three include a choice section. Each has a live alternative the answers record as an open DISCUSS call, and a maintainer could plausibly reject the implemented route.

- SOLR-4502: included. Options are the `create()` guard (implemented) or `load()` from the constructor (proposed on the ticket). The constructor route changes what every caller of the constructor gets. Answers DISCUSS item 13 recommends keeping the guard; not decided.
- SOLR-12916: included. Options are both input paths (implemented) or the Config API only. The XML change is a real behavior change: an even run of plain `<str>` values now runs a query. Answers DISCUSS item 9 recommends keeping both; not decided.
- SOLR-16499: included. Options are plumbing both parameters (implemented) or removing them from the guide and the operation. Plumbing changes results for callers who already send them. Answers DISCUSS item 10 recommends plumbing; not decided.

## Receipt gaps, discrepancies, and holds

1. SOLR-4502, owed-before-draft criterion not met as written. The answers (`material/core-admin-round-1-answers.md` SOLR-4502 entry) require "a premise run showing `create` on an unloaded container producing a core whose search fails with the NPE (or otherwise reproducing the ticket)." Receipt L7 shows a create-time NPE only. The draft says so in Limits. The lead should decide whether this clears the bar before posting.
2. SOLR-4502, scope gap. The ticket's own failure is for a core built by hand, not through `create()`. The guard does not cover that path (see the `SyntheticSolrCore` citation). The draft's Limits says this.
3. SOLR-16499, hold. Before posting, the reference-guide default (300 in the guide at head line 871, 600 in code), the v2 `timeout` description ("per replica move"), and the OpenAPI or SolrJ regeneration check must be done. Each correction moves the head, so the draft must be re-cut, and the matching Limits lines removed. The Limits lines state these as the current state of the branch.
4. SOLR-16499, new finding from this pass, not in the answers. `timeout` does not reach `AddReplicaCmd`. With `waitForFinalState=true`, each replica add still waits up to 600 seconds. Fixing this is a code change on the branch (it would pass `timeout` into the add message), not a doc fix. The lead should decide whether to fix it or keep the Limit line.
5. SOLR-12916, branch correction. The changelog title should name both input paths (answers finding 8). Not at head. This moves the head.
6. SOLR-12916, no Config API round trip. Stated as a limit in the draft, which the answers accept ("a Config API round trip or a stated limit").
7. Landing order from the answers, cross-cutting: SOLR-12916 after SOLR-13246; SOLR-4502 in the lifecycle cluster after its TESTING file is out and its gate lands (satisfied at head), before SOLR-5011 and SOLR-12007.

## Length

Visible text (link URLs removed): SOLR-4502 about 2,670 characters; SOLR-12916 about 2,520; SOLR-16499 about 3,410. Raw markdown with URLs: 3,749, 3,318, and 5,969. The formula's length guide applies to rendered text, so all three are within about 3,500 visible characters.

## Not verified

- No build, Gradle, or test run. All test counts are from the receipts and were not re-run.
- Not checked: the OpenAPI and SolrJ regeneration for SOLR-16499; the SERVER_ERROR to HTTP 500 mapping; the XML `<arr>` behavior for SOLR-12916 (read from code and the existing test comment, not run).
- Ticket text came from the hydrated packets in `research/jira-context/`, not live Jira. No Jira reads or writes beyond that.
- No public write of any kind. No GitHub write. `gh` was not used.
