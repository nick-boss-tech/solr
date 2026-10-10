# Replication and backup round 1, part g2 (IndexFetcher cluster)

Result: SOLR-12246 and SOLR-12085 are draftable as they stand, SOLR-11650 is draftable once its changelog title and two comment edits are settled, and SOLR-6711 is held as audit only.

## Findings

**F1. NOTE. Heads, base, and trial merges (cluster).** Heads match the claim table: 6711 `5a120cd69d6`, 11650 `e4f5e941cd8`, 12085 `c8dba502339`, 12246 `3ffc2539ec7`, each checked with `git rev-parse --short origin/solr-<ticket>-submit`. The three gated branches and 6711 apply cleanly to `upstream/main` `8e62c268688` (`git merge-tree --write-tree`, no conflicted files). All six pairs merge cleanly. Merge bases: 6711 from `cabedd1d968`; 11650, 12085 and 12246 from `14c7aac0d15`. IndexFetcher.java hunks, base line numbers: 11650 at lines 306 to 317 and 470; 12085 after line 78 (new import) and at lines 867 to 870; 12246 at line 1265; 6711 an insertion after line 897. No two branches change the same line. The closest pair, 6711 and 12085, is about 20 lines apart. Upstream has one later commit touching IndexFetcher.java (`6d6504668e3`, SOLR-18124); it causes no conflict. Replacement: none.

**F2. NOTE. Assumptions and landing order (cluster).** The four changes do not share an assumption. 12085 treats a commit's files as in use while the commit is kept. 12246 treats a checksum mismatch as routine. 11650 treats `leaderUrl` as a connection string that is only masked for display. 6711 writes the same `replication.properties` file that the fetcher writes. Landing order for the gated three: 12246, then 12085, then 11650. 12246 is one log-level line with one test. 12085 adds one helper and one test. 11650 spans SolrJ and core, and it needs findings F3 to F6 first. 6711 is held and would land last. No order causes a textual conflict. Replacement: none.

**F3. FIX. 11650 changelog title overstates the redaction.** File `changelog/unreleased/SOLR-11650-redact-replication-leader-url.yml`, line 1. Evidence: the title says the password is no longer exposed "in IndexFetcher log and error messages". At `e4f5e941cd8`, `solr/core/src/java/org/apache/solr/handler/IndexFetcher.java` lines 501 to 504 and 507 to 510 still log `leaderCoreUrl` as configured. The receipt names three messages only (the "Updated leaderUrl" line and the malformed and not-allowed errors). Replacement title: "Replication no longer shows the password from a leaderUrl with user-info in the follower details response or in three leaderUrl messages". A changelog-only commit moves the head; see decision 1.

**F4. FIX. 11650 draft must not claim the client error text is covered.** Evidence (traced by reading, not run): `IndexFetcher.java` line 499 sets `errorMsg = e.toString()`, and lines 501 to 504 print it. The leader client is `HttpJettySolrClient`, built on `leaderBaseUrl` (line 254). `URLUtil.extractBaseUrl` (`solr/solrj/src/java/org/apache/solr/common/util/URLUtil.java` lines 72 to 82) keeps user-info. The Jetty client builds its messages from the request URL: `solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java` lines 485, 501 and 506 ("IOException occurred", "Timeout occurred", "Server refused connection" at url). `ClientUtils.buildRequestUrl` (lines 56 to 71) puts the base URL first. A refused connection or a timeout can therefore print the password in these warnings. Replacement, already in the draft's Limits: "The 'Leader at ... is not available' warnings print the leader URL as configured. The HTTP client's connection and timeout errors can carry the same URL, and those errors are printed in these warnings. This change does not cover them." Do not write "no longer exposes the password in log messages" in any public text. A code fix is decision 2.

**F5. FIX. 11650 javadoc contradicts the code and the test.** File `solr/solrj/src/java/org/apache/solr/common/util/URLUtil.java`, lines 109 to 114. Evidence: the javadoc says user-info ends at the first `/`, `?` or `#`, and that a password cannot contain a raw `/`, `?` or `#`. The pattern at lines 36 to 37 is `^([A-Za-z][A-Za-z0-9+.-]*://)([^/?]*)(?=@)`, which does not stop at `#`. `URLUtilTest.java` lines 126 to 129 expect `http://solr:pa#ss{word}@localhost:8983/solr/core` to be redacted, and the code does redact it. Replacement for lines 109 to 114:
```
   * <p>The user-info is the text between the scheme and the last {@code @} before the first {@code
   * /} or {@code ?}. A {@code #} does not end it here, so a raw {@code #} in a password is
   * redacted. A raw {@code /} or {@code ?} ends the user-info, so a URL with one in its password is
   * returned unchanged. Percent-encode such characters in the URL (for example {@code %2F}), and the
   * encoded form is redacted like any other password.
```

**F6. FIX. 11650 test expects a password to stay visible, and its comment says it cannot happen.** File `solr/solrj/src/test/org/apache/solr/common/util/URLUtilTest.java`, lines 154 to 159. Evidence: lines 157 to 159 assert that `http://solr:pa/ss@localhost:8983/solr/core` comes back unchanged, so the password `pa/ss` stays. The comment at lines 154 to 156 says a password with a raw `/` "cannot be carried by a URL in the first place". The IndexFetcher error path takes the configured string as it is, so the password can still show there. The commit subject "fail closed when redacting leaderUrl" does not hold for this input. Replacement comment for lines 154 to 156:
```
    // a raw '/' ends the user-info for this method, so the URL is returned unchanged and
    // the password is not redacted (a known limit; the leader URL error can show it)
```
The assertion stays as it is. Comment-only changes move the head; see decision 1.

**F7. NOTE. 11650 details test has no recorded base run.** `solr/core/src/test/org/apache/solr/handler/TestUserManagedReplicationWithAuth.java` lines 188 to 221 (`testFollowerDetailsRedactLeaderUrlPassword`). The receipt records 3 of 3 at head and no base run for this case. The draft says only that the case passes. Replacement: none for now; a base run is owed before the Proof may say it fails without the change.

**F8. FIX. 6711 handoff file must not ship.** File `SOLR-6711-TESTING.md` at the root of `origin/solr-6711-submit` (added by `5a120cd69d6`, 29 lines). It says "hypothetical reproduction (nothing was compiled or run)" and lists "Guesses to verify first". The other three tips already removed their handoff files (`c8dba502339` and `3ffc2539ec7` say "Remove the testing handoff doc"; the 11650 diff has no such file). Replacement: delete `SOLR-6711-TESTING.md` in a branch commit before any PR. Not done here.

**F9. NOTE. 6711 scope gap.** The JIRA summary names `disablepoll` and `disablereplication` (`research/jira-context/SOLR-6711.json`). The branch changes only `disablepoll`, and the handoff says so. Replacement Limits sentence for any future draft: "The leader-side `disablereplication` command is not changed by this change."

**F10. NOTE. 6711 keeps the reported behavior by default.** `solr/core/src/java/org/apache/solr/handler/ReplicationHandler.java` line 312 reads `persist` with default false. `solr/core/src/test/org/apache/solr/handler/TestPersistedPollDisabled.java` line 54 asserts "a plain disablepoll is not persistent". The test therefore locks in the behavior the ticket reports. Replacement: none; owner decision 6.

**F11. NOTE. 6711 lost-update race.** In the 6711 base (`cabedd1d968`), `IndexFetcher.java` line 912 loads `replication.properties` and lines 956 to 964 store it in `logReplicationTimeAndConfFiles`. The new `storeReplicationProperties` (`IndexFetcher.java` lines 898 to 916 at `5a120cd69d6`) does the same from `disablepoll`, with no lock on either path. A fetch that finishes during a `disablepoll persist=true` can write back the old file and drop the flag. The handoff names this race and leaves it unguarded. Replacement: a Limits line if held as is, or one lock shared by both writers (code change, new focused run).

**F12. NOTE. 6711 writes the state before the file, so a failed write leaves polling changed.** `ReplicationHandler.java` lines 815 and 828 set `pollDisabled` before the file write at lines 817 to 819 and 830 to 832. A failed write returns an error, but polling has already changed. Replacement for `disablePoll` (lines 813 to 824):
```
  private void disablePoll(boolean persist, SolrQueryResponse rsp) {
    if (pollingIndexFetcher != null) {
      if (persist && !updatePersistedPollDisabled(true, rsp)) {
        return;
      }
      pollDisabled.set(true);
      log.info("inside disable poll, value of pollDisabled = {}", pollDisabled);
      rsp.add(STATUS, OK_STATUS);
    } else {
      reportErrorOnResponse(rsp, "No follower configured", null);
    }
  }
```
For `enablePoll` (lines 826 to 837), move `pollDisabled.set(false)` after the `updatePersistedPollDisabled(false, rsp)` check, the same way.

**F13. NOTE. 6711 has two lines over 100 columns.** `IndexFetcher.java` line 902 and `ReplicationHandler.java` line 1345. Spotless would reject them at the gate; `spotlessApply` rewraps them. Hand replacement for line 1345:
```
      pollDisabled.set(
          Boolean.parseBoolean(loadReplicationProperties().getProperty(POLL_DISABLED)));
```

**F14. NOTE. 12085 changelog says "every replication".** The wait runs only when `!isFullCopyNeeded && !fetchFromLeader` (`solr/core/src/java/org/apache/solr/handler/IndexFetcher.java` line 622 at `c8dba502339`). The draft says "on each replication", limited to that path. Replacement changelog title: "IndexFetcher no longer treats the files of commits kept by the deletion policy (maxCommitsToKeep > 1) as unused, which made a replica wait 30 seconds and then fall back to a full index copy on each replication that reaches the check." This needs a changelog-only commit (decision 1).

**F15. NOTE. 12085 ticket offers three fixes; the branch takes none as written.** The JIRA lists: remove the check and call `deleteUnusedFiles`; add a pending-deletion count from `IndexFileDeleter`; track unused files in the deletion policy wrapper. The branch changes the used set instead. The draft has no "A choice to check" section. Owner decision 7.

**F16. NOTE. 12085 visibility change.** `IndexFetcher.java` line 872 changes `hasUnusedFiles` from private to `static`, package-private. It exists so the unit test can call it. The draft says so. No public signature changes.

**F17. NOTE. 12246 test covers equal length only.** `IndexFetcherCompareFileTest.java` writes one file with an equal length. The lowered branch at `IndexFetcher.java` line 1265 also covers a different length with a different checksum. The draft's Limits says the test covers equal length only. No code change needed.

**F18. NOTE. Cross-check for g3 (ReplicationHandler).** 11650 changes `ReplicationHandler.java` line 1041 only (details output). 6711 changes lines 312, 813 to 840, 1345, and 1673 to 1676. The pair merges cleanly. Passed to g3 for its premise check against the same file.

## Task results

**SOLR-6711, held (audit only, no gate).** The premise is real: the ticket reports that `disablepoll` and `disablereplication` are lost after a restart. The branch makes only `disablepoll` persist, and only with `persist=true`. It has no gate, and its handoff note lists unverified guesses. It cannot be drafted until F8 is removed and the owner decides the points in decisions 6 and 7. Its base is `cabedd1d968`, not `14c7aac0d15`, but it merges cleanly with the other three.

**SOLR-11650, draftable after F3 to F6.** Draft: `pr-drafts/replication-backup/SOLR-11650.md`, written against head `e4f5e941cd8`. The Proof follows the receipt: `IndexFetcherLeaderUrlRedactionTest` 2 of 2 (fails on the base code, password visible in both messages), `URLUtilTest` 18 of 18 (three new tests call the new helper), and `TestUserManagedReplicationWithAuth` 3 of 3 (the new details case passes; F7). Cross-area effect, stated in the draft: a new public static method in the SolrJ module, no existing method changed, and no SolrJ class in this repository reads the follower `leaderUrl` from details (checked by grep). The draft carries the Limits for the warnings, client errors, raw `/` and no-scheme values. It has no choice section: masking follows the ticket's own suggestion.

**SOLR-12085, draftable.** Draft: `pr-drafts/replication-backup/SOLR-12085.md`, written against head `c8dba502339`. The premise matches the base code: `hasUnusedFiles` counts only the given commit's files, and the wait loop runs 30 one-second rounds before a forced full copy (head lines 640 to 649). The Proof is a helper-level unit test, 1 of 1, which fails on base at its first assertion. `IndexFetcherPacketProtocolTest` 18 tests pass. `TestReplicationHandler` is `@Nightly` and was skipped, so the draft claims no end-to-end coverage. No choice section (F15).

**SOLR-12246, draftable.** Draft: `pr-drafts/replication-backup/SOLR-12246.md`, written against head `3ffc2539ec7`. The one-line change lowers the checksum mismatch message from WARN to INFO (line 1265). The Proof follows the receipt: `IndexFetcherCompareFileTest` 1 of 1, which fails on base at its WARN assertion (`IndexFetcherCompareFileTest.java` line 45). `IndexFetcherPacketProtocolTest` 18 tests pass; `TestReplicationHandler` was skipped as `@Nightly`. The draft carries the Choice (lower the whole message, or keep WARN when lengths differ) and the open `.liv` cause as a Limit. The changelog is accurate for the checksum path (F17).

## Owner decisions

1. Moved head: the changelog title (F3), the javadoc (F5) and the test comment (F6) are comment or changelog changes. They move 11650 off `e4f5e941cd8`. Decide whether comment-only and changelog-only commits can go without a new focused run, or whether to re-run at the new head.
2. 11650 scope: keep the narrowed claim and the Limit (recommended), or extend redaction to the two warnings and the client error text (F4). The second is a code change and needs a new focused run.
3. 11650 raw `/`, `?` and no-scheme values: keep them as a Limit (recommended), or change `redactUserInfo` to redact up to the last `@` before the path. The second changes what the unit test pins and would also redact an `@` in a path.
4. 11650 details case: a base run is owed before the Proof can say it fails without the change (F7). Until then the draft says only that it passes.
5. 12246 Choice: confirm the implemented route (lower the whole message), or change the branch to keep WARN for length differences (a code change and a new focused run).
6. 6711 hold: decide opt-in `persist=true` or default persistence (F10); whether `disablereplication` is in scope (F9); whether to guard the file race (F11); and the write order (F12). Remove the handoff file in any case (F8).
7. 12085 Choice: decide whether to add a "A choice to check" section naming the ticket's first option, removing the check (F15). The draft has none.
8. Landing order: 12246, then 12085, then 11650, with 6711 last. Confirm.

## Not checked

- No build, test, or gate ran. No gate log, JUnit XML, or preflight file for these tickets is on disk. All proof counts come from the receipts.
- The HTTP client error path (F4) was traced by reading the code, not run.
- The `redactUserInfo` cases (raw `/`, no scheme, `#`) were read from the regex and the test, not run.
- The base run for `testFollowerDetailsRedactLeaderUrlPassword` (F7) is not recorded and was not run.
- Whether `DirectoryReader.listCommits` matches the deletion policy on directories with extra commit points (12085) was not traced.
- Lucene API use in 12085 (`DirectoryReader.listCommits`, `IndexCommit.getFileNames`) was not checked against Lucene 10.4.0 (main) or 9.12.3 (branch_9x). The drafts name no Lucene version.
- External clients that read `leaderUrl` from replication details were not checked beyond this repository.
- Admin UI templates: a grep found no `leaderUrl` under `solr/webapp` at `e4f5e941cd8`. The UI code was not read.
- 6711: the handoff guesses (`h.reload()` keeps the data dir, the direct `handleRequestBody` call, `loadReplicationProperties()` safety in `inform()`) were not verified. A grep found no v2 `disablepoll` in `solr/core/src/java`; the reference guide and other modules were not checked.
- Live PRs: none in this part. No `gh` calls were made.
- `upstream/main` was used as already fetched (`8e62c268688`); nothing was refetched.
