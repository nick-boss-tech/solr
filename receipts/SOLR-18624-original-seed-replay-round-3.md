# Receipt section: SOLR-18624 (SOLR-18624-original-seed-replay-round-3)

## Gate SOLR-18624-original-seed-replay-round-3 (phone-agent-2, 2026-11-05): NON-REPRODUCTION — all legs passed, including historical F

- Branch: `solr-18624-flaky-multiword-synonyms`. Head: `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a` (confirmed by live `git ls-remote` before the run and after leg 3). Base: `07ccc73cd24e26e59da169ca35b3fd5f64c35986`.
- Links:
  - Ticket or parent: SOLR-18624 / PR #5294.
  - Category: none (per claim; live `pool/work-map.md` is absent).
  - Siblings: `gates/SOLR-18624-assertion-repair-round-2.md`; `receipts/SOLR-18624-assertion-repair-round-2.md`.
  - Supersedes: none; this supplements the round-2 proof.
- Runner: `pool/bin/sweep-run.sh` on `phone-agent-2` (Ubuntu 24.04.4 LTS, Linux `6.18.38+` x86_64; OpenJDK 21.0.12.1 Ubuntu; Gradle wrapper 9.7.0). Log: `/workspace/gates/logs/phone-agent-2/solr-18624-original-seed-replay-round-3/leg1-historical/F-historical.log` (all leg logs listed below).
- Step 0, changelog YAML parse: not requested by this replay gate; no changelog file was part of the assignment.
- Step 1, tidy: rc=0; post-tidy status clean; detached H1 preflight at 2026-11-05T06:44:31Z–06:45:55Z, 62 tasks (`preflight/preflight-tidy.log`).
- Step 2, Error Prone compile: not requested; the assignment specifies the three original-seed replay legs and no source change.
- Step 3, historical proof leg at F: expected the original runtime assertion that ID 10 ranks first; observed 1 test pass, 0 failures, 0 errors, 0 skipped. Original failure not reproduced; no retry.
- Step 4, focused offered-head run: `org.apache.solr.search.TestMultiWordSynonyms.testPf`, seed `B3B4534A1A4D09F4`: 1 test, 0 failures, 0 errors, 0 skipped (fresh XML copied to `leg3-H1/H1-offered.xml/`).
- Step 5, module check `:solr:core:check -x test`: not requested by this replay assignment.

### Verdict

The three required test executions completed on separate detached worktrees with fresh Gradle outputs. Each selected exactly one `testPf`; each fresh XML reports 1 test, 0 failures, 0 errors, 0 skipped. However, the historical merge F passed when the assignment expected its original assertion failure. This is a **NON-REPRODUCTION**, not a green reproduction of the reported defect and not evidence that H1 fixes that specific failure. Per the assignment, the F result was retained without retry, and the matched overlay and H1 legs were still completed.

### Revisions and setup

- Historical F: `fe84bf4e4844df854c3fbe821401ab4f9bf36b11`, parents `82abb86bb5b7326fe48c117e4de69981a43e4251` and contributor head `600e0ec4afd82004a94852c419892897be14e6f0`. The original Crave job confirms F was the checked-out merge; the contributor head was not substituted.
- H1: `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`, parent H0 `c610e6d05db5700835a123de54da61137478e48f`. The H1 test-file blob at `solr/core/src/test/org/apache/solr/search/TestMultiWordSynonyms.java` is `7f2e210d8ea93947dd246f76354b60848a4ebb94`; F's version is `2468f0ad2a2ba28075ef1aa5cbe48a4e1b19ad02`.
- The F-plus-H1-test leg was based on F and changed only that one test file. Its `git status` contains only the intended modification; the diagnostic diff is `F-overlay-test-file.diff` (SHA-256 `7a5ccb86b4c79180c8c3a4413deef1090e5cee1189498efad273993ca4cf27f9`). The overlay stayed local and was not pushed. F and H1 worktrees were clean after their runs; H1 remained at the exact offered head.
- Both F and H1 wrapper metadata specify `https://services.gradle.org/distributions/gradle-9.7.0-bin.zip` with `networkTimeout=10000`; copies and SHA-256 are in `wrapper-metadata/`.
- A pre-test source verifier initially used `git show %P`, which suppresses parent output at shallow boundaries. It stopped before tests; the raw commit object confirmed H1's parent is exactly H0, then preparation resumed. No source or test run was affected.

### Original Crave failure context

Source: [Crave run 41911581722](https://github.com/apache/solr/actions/runs/41911581722), full job log `original-ci/crave-job-126860245817.log`, excerpt `original-ci/original-failure-extract.txt`. It reports `TestMultiWordSynonyms.testPf` failing the XPath `//doc[1]/str[@name='id'][.='10']` on request `q=tropical+cyclone+foo&qf=text&pf2=text^10&ps=2&fl=score,*&defType=edismax&wt=xml`. The returned IDs were `13, 12, 11, 10`, all with score `7.7074103`; thus ID 10 was not first despite the equal-score ordering. The logged randomized seed was `B3B4534A1A4D09F4` (child seed `A3AE03DDF1BDCF19`). The Crave output does not provide a parsed query, and the Actions artifacts endpoint reports zero downloadable artifacts, so no original JUnit XML or parsed-query output was available; the complete job log and failure excerpt are retained.

The original test JVM note reports Linux `6.8.0-51-generic`, Eclipse Adoptium 21.0.10, `cpus=1,threads=1`. This replay used Linux `6.18.38+` and OpenJDK 21.0.12.1 (Ubuntu); the test worker's requested `-XX:ActiveProcessorCount=1` was present. The kernel and JDK patch level differ, so the non-reproduction does not identify which difference, if any, matters.

### Command and effective settings

All legs used the supplied Gradle arguments once, through `sweep-run.sh`, with `NODE_ENV` unset. `--info` was the only execution-only addition, to capture the actual forked-worker command. Each leg had a separate fresh checkout/output directory; no `cleanTest` or `--rerun-tasks` was needed or added.

```sh
./gradlew :solr:core:test \
  --tests 'org.apache.solr.search.TestMultiWordSynonyms.testPf' \
  '-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m' \
  -Ptests.seed=B3B4534A1A4D09F4 \
  '-Ptests.timeoutSuite=600000!' \
  -Ptests.useSecurityManager=true \
  -Ptests.file.encoding=US-ASCII \
  --info
```

The Gradle Test Executor command is preserved in each full log and confirms the requested four JVM flags, `-Djava.security.manager=default` with the Solr test policy, `-Dtests.useSecurityManager=true`, `-Dtests.file.encoding=US-ASCII`, the exact seed, and `-Dtests.timeoutSuite=600000!`. Java's actual `-Dfile.encoding` in the forked process was `UTF-8`; the requested Solr test property remained `US-ASCII`. One test-executor command was recorded per leg.

### Results

| Leg | Head and worktree | UTC interval; runner rc | Fresh XML and result |
| --- | --- | --- | --- |
| 1. Historical F | `fe84bf4e4844df854c3fbe821401ab4f9bf36b11`; `/workspace/tmp/solr-18624-original-seed-replay-round-3/F` | 06:52:05–06:53:47; 0 | `leg1-historical/F-historical.xml/`: 1 test, 0 failures, 0 errors, 0 skipped; **passed**, so the original failure did not reproduce. Full log: `leg1-historical/F-historical.log`. |
| 2. Matched test overlay | F, with only the H1 test-file blob; `/workspace/tmp/solr-18624-original-seed-replay-round-3/F-overlay` | 06:54:11–06:55:54; 0 | `leg2-overlay/F-overlay.xml/`: 1 test, 0 failures, 0 errors, 0 skipped; passed. Full log: `leg2-overlay/F-overlay.log`. |
| 3. Offered head H1 | `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`; `/workspace/tmp/solr-18624-original-seed-replay-round-3/H1` | 06:56:11–06:57:53; 0 | `leg3-H1/H1-offered.xml/`: 1 test, 0 failures, 0 errors, 0 skipped; passed. Full log: `leg3-H1/H1-offered.log`. |

Each complete log and fresh XML directory is also in that leg's `*-evidence.tar.gz`; archive checksums are in the corresponding `*-evidence.sha256` files. The source/blob manifest is `source-manifest.txt`.

### Resource guard and final read-back

The external watchdog sampled root inode and disk headroom throughout each run and would stop at or below 15% free inodes (and below 2 GiB free disk). The lowest observed test-leg headroom was **56.7325% free inodes** (1,059,817 of 1,868,096) and **51,273,969,664 bytes free disk**; no stop was triggered. Per-leg samples are in `leg1-historical/inode-monitor.tsv`, `leg2-overlay/inode-monitor.tsv`, and `leg3-H1/inode-monitor.tsv`.

After the H1 run, the separate live read-back at 2026-11-05T07:00:19Z found the fork ref and open draft PR 5294 both at H1, with base B; the local H1 checkout was clean. No source branch change, PR opening/edit/comment, Jira write, or public post was made. Only this receipt is being added to the coordination board.

### Suggested factual replacement for the PR seed limitation (not posted)

“The supplied Crave seed `B3B4534A1A4D09F4` was replayed on the exact historical merge F, but `testPf` passed 1/1 on this Linux/JDK 21 host. It also passed 1/1 on F with only the H1 test file and on H1. The original Crave log records the expected-ID-10 assertion failure and a four-document score tie, but that failure was not reproduced in these legs; this replay therefore does not establish that the test-file change fixes the historical run.”
