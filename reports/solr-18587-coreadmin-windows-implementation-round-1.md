# SOLR-18587: CoreAdmin unload candidate and Linux handoff, round 1

Status: IMPLEMENTED, PROOF PENDING. Runtime cause confirmation and the matched fixed leg are pending Linux. This report does not claim that the assignment's probe-first step has run.

Claim: `claims/solr-18587-windows-implementation-round-1.md`. Host: windows. Owner direction in chat explicitly permits Windows implementation and says Nick will hand off to Linux; this splits implementation from the original Linux premise-run assignment. No gate host was dispatched here.

## Branch and existing-work check

- Fork: `nick-boss-tech/solr`.
- Branch: `solr-18587-coreadmin-unload-failed-reload`.
- Base and only parent: RC3 `749712b71940c9f96d824c533f32f5c56dcd3924`.
- Pushed head: `1de4f006f1cf8d4b13c9ed673ac2b7b7f9b57157`.
- Commit: `SOLR-18587: Close loaded cores when unloading after a failed reload`.
- Author and committer: Nick Shanin, `nick.boss.us@gmail.com`. No attribution trailers.
- Independent GitHub ref read-back returned exactly that full head SHA after the plain fork push. This created a new branch; no existing public PR head moved.

At board snapshot `dde5d9159fa184331fa213b1ec82c4b7a1c39543`, the implementation assignment was OPEN and there was no implementation claim, fix branch or completion report. The previous CoreAdmin reproduction report was read rather than repeated. Jira was Open with no comments when read through the Apache Jira connector. GitHub search returned ticket references in PRs 5098/5106, not a competing CoreAdmin implementation.

## Rung 1: code-supported defect and bounded change

RC3 `CoreContainer.java:1955-2015` gets the already loaded core without removing it, rereads the descriptor and attempts reload. A bad configuration enters the catch at 2007, records `coreInitFailures` at 2008, and leaves the prior loaded core registered. `SolrCores.java:245-268` confirms that getting the loaded core does not remove it.

The old unload code (`CoreContainer.java:2081-2097`) treated any recorded failure as an unloaded core. It called `SolrCore.deleteUnloadedCore`, removed the descriptor and returned before normal core removal, metrics removal, recovery cancellation, unload-on-close deletion and close. That path can coexist with a live old core after a reload failure.

The candidate restricts fallback deletion to a recorded failure with no loaded core. It still removes the recorded failure in either case. A retained loaded core falls through to the existing normal unload flow. Truly failed initialization without a loaded core keeps its prior fallback behavior. The operation remains protected by the existing pending-core-operation lock. No new loading, public API or lifecycle implementation is introduced.

`SolrCore.java:3267-3302` confirms that normal unload uses the loaded core's actual data/index/tlog paths, whereas `deleteUnloadedCore` at 3325-3331 computes `instanceDir.resolve(cd.getDataDir())`. Whether those two directories differ in the failing seed remains a runtime question. The code defect above is established by inspection; its causal link to the reported assertion is still a candidate until the probe and pair run.

History: blame attributes the fallback branch to longstanding code; commit `2c64848c78d0bf235cf3e7d4e2ee9d770ebd05e1` wraps an already existing early-return branch with a null-name guard. This is not evidence of a newly introduced RC3 regression. SOLR-9433's adjacent create-failure path is unchanged.

## Exact outbound files

1. `solr/core/src/java/org/apache/solr/core/CoreContainer.java`: one condition and an explanatory comment.
2. `changelog/unreleased/SOLR-18587-unload-failed-reload.yml`: author name only, no `nick` field.

Production diff:

```diff
       CoreLoadFailure loadFailure = coreInitFailures.remove(name);
-      if (loadFailure != null) {
+      // A failed reload can leave the previous core loaded.
+      if (loadFailure != null && !solrCores.isLoaded(name)) {
```

No regression assertion was added: `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure` already exercises bad reload, unload with `deleteDataDir=true`/`deleteInstanceDir=false`, and data-directory absence. The assignment explicitly asks to retain that assertion without unnecessary additions.

## Local validation and security

The two-file commit was reviewed, `git diff --check` passed before commit and again against RC3 after commit, author/changelog identity was inspected, and the worktree was clean after commit. No Java compilation, Gradle, tidy, JUnit, local Windows queue or runtime probe was executed here. Windows is explicitly excluded by the existing regression test as well as the workspace build rule. A workspace-wide sync scan was launched; only this ticket worktree was staged, committed or pushed.

No new trust boundary, endpoint, permissions, path parsing or wire/persistence format. Existing loaded-core deletion and close behavior is reused; create-failure fallback paths are unchanged. The candidate does change which cleanup branch runs after a failed reload, so Linux must check actual directory deletion and ordinary initialization-failure cleanup before accepting it. It does not alter JS-client dependencies or the tarball build.

## Linux probe first, then matched proof

The self-contained Python diagnostic generator is embedded in `reports/solr-18586-tlog-windows-investigation-round-1.md`. Save it outside the scratch tree as `rc3-failure-probes.py`. Both ticket modes were dry-run on a clean RC3 worktree and both printed patches passed `git apply --check`; that checks applicability only.

On a separate clean RC3 scratch worktree:

```bash
python3 /outside-tree/rc3-failure-probes.py 18587 /scratch/rc3-coreadmin --apply
```

The probe logs `SOLR18587_PROBE` with the loaded flag, loaded core's actual data directory, fallback deletion directory and `branch=failed-init`. It inspects the loaded core without incrementing its reference count or changing deletion/close behavior. Save `git diff`, log, exit status, and XML immediately after the run. Do not push this scaffolding or apply it to the candidate. A different branch or different paths are observations to record, not assumptions to fill in. If the observed state contradicts this candidate, report that before treating a green run as a causal proof.

Run on Linux only, under the host lock, with recorded JDK version/resources:

```bash
./gradlew :solr:core:test --tests org.apache.solr.handler.admin.CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure -Ptests.seed=787C6229591C6F1E --no-daemon
```

| Leg | Revision | Required outcome / evidence |
| --- | --- | --- |
| Existing clean base | RC3 `749712b71940c9f96d824c533f32f5c56dcd3924` | Previously reproduced at seed `787C6229591C6F1E`, same single method, 1 failure; `/workspace/smoke-rc3/coreadmin-repro/rc3-tip-run1.log`, XML archive `/workspace/smoke-rc3/repro-archive/coreadmin-2026-10-25/`, as recorded in `reports/rc3-coreadmin-deleteinstance-rootcause-round-1.md`. This is the prior Linux host's evidence, not a Windows run. |
| Diagnostic scratch | Same RC3 plus logging only | Same method and seed; capture both paths and loaded flag. This diagnostic run is separate from clean proof legs. |
| Fixed leg | `1de4f006f1cf8d4b13c9ed673ac2b7b7f9b57157` | Same method and seed must pass, with fresh log and XML. Not run. |

If the previous base XML/log is unavailable, run a fresh clean base at the same filter/seed and archive it. Keep each run's XML and `outputs/` in a distinct absolute evidence directory before another invocation. Record commands, full revision, Java version, exit status, test counts and signatures. A build failure before JUnit is not a test result. The TLOG handoff includes a runner fragment for this archive pattern; substitute this method/seed and use distinct worktrees for probe and fixed legs.

After the premise and single-method pair, the owner's gate should name the focused `CoreAdminHandlerTest` and `TestCoreContainer` classes (including ordinary create-failure cleanup), `:solr:core:spotlessJavaCheck`, and the repository's required `tidy` / `check -x test` checks. Tidy belongs on Linux; if it changes the candidate, commit and push that delta, record the new head, and obtain fresh proof at that head. No result on the RC3 SHA or this head proves a later code/formatting head.

## Disposition

Candidate pushed, implementation done for this Windows slice, proof pending. The original assignment's runtime probe and fixed leg remain unfinished and are handed to Nick for Linux. No receipt, completed pair, gate-green or submission-ready claim is made. No PR, Jira/GitHub comment, live PR body edit, upstream write, force push or mailing-list message occurred.
