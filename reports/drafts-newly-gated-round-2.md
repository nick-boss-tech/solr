# Newly gated drafts, round 2 (solrj: SOLR-14187, SOLR-11356; build-docs: SOLR-6430)

Worktree head at start: `bd71ed56f21`. Drafts written: `pr-drafts/solrj/SOLR-14187.md` (head `29023ec1ecab`), `pr-drafts/solrj/SOLR-11356.md` (head `6120dae28d04`), `pr-drafts/build-docs/SOLR-6430.md` (head `c0c6e5dd21c4`). No commit, no push, no build, no test run, no GitHub or Jira write. The only repository change is the fetch described under Head check. The claim file was not edited; the lead marks it DONE with the push. The worktree was clean at my start. During this run, other sessions changed `pr-drafts/solrcloud/*` and added `reports/solrcloud-drafts-round-1-sc1.md` and `-sc2.md` in the same worktree. I did not touch those files; the commit should take only the four files named here.

## Receipt numbers used

SOLR-14187 (`receipts/SOLR-14187.md`):
- L3 status GATE GREEN (main-side proof decision). L4 proof shape: branch test compile failure at merge-base, scratch base vehicle, branch test passing at head.
- L5 gated head `29023ec1ecab...`, local to vm2, not pushed; packaging removes `SOLR-14187-TESTING.md`.
- L6 merge-base `e044bf20b405`.
- L7 base: branch test does not compile (`CollectionAdminRequestAsyncAuthTest.java:51`); scratch base test 2 tests, 0 failures.
- L8 head branch test `tests=2 failures=0` from fresh XML (at `45b0f7ce34f8`).
- L9 gate steps at `29023ec1eca`: focused `CollectionAdminRequestAsyncAuthTest tests=2 failures=0`; changelog parse, tidy, compile and `:solr:solrj:check -x test` steps listed.
- L12 live tip `45b0f7ce34f8`, verified by ls-remote 2026-10-11.
- Date: the receipt has no run date for the head test or the gate steps. Its only dated lines are L4 (decision, 2026-10-10) and the earlier BLOCKED status (2026-10-11T04:18Z UTC). See hold 1.

SOLR-11356 (`receipts/SOLR-11356.md`):
- L3 status GATE GREEN at packaged head `6120dae28d04` (vm2, 2026-10-11).
- L4 live tip `8474e5a3a26d`, verified by ls-remote 2026-10-11.
- L5 proof base `cabedd1d968`; L6 branch change is `cabedd1d96..8474e5a3a2`; L6 test name.
- L8 heading "Premise run (2026-10-11, vm2)".
- L10 base `ConcurrentUpdateJettySolrClientTest tests=12 failures=1`; failing test at `ConcurrentUpdateSolrClientTestBase.java:328`.
- L11 head `8474e5a3a2` `tests=12 failures=0`.
- L20 focused tests at packaged head `6120dae28d0` `tests=12 failures=0` from fresh JUnit XML.
- L21 module check rc 0.

SOLR-6430 (`receipts/SOLR-6430.md`):
- L2 status GREEN for the docs-shaped gate; no Java tests apply or were run.
- L3 packaged head `c0c6e5dd21c4` (removes only `SOLR-6430-TESTING.md`).
- L4 premise: `FieldType.missingValue` returns null when neither property is set; numeric sort uses 0; dates are epoch milliseconds.
- L5 changelog lint `logchangeLint`, exit 0.
- L6 `:solr:solr-ref-guide:buildLocalSite`, 69 tasks, exit 0; generated page contains the new text.
- L9 recorded `2026-10-11T04:32:06Z UTC` (used as the verification date).
- Not used in the draft: L7 (Antora linked-worktree environment note) and L11-L15 (inode and disk notes). These are environment and process items, not proof outcomes.

## Head check

- SOLR-14187: `ls-remote origin refs/heads/solr-14187-submit` = `45b0f7ce34f802d0568c499bb3e5357f41be77ec`, matches receipt L12 (live tip). The receipt's packaged head `29023ec1ecab...` is not on the fork and is not in the worktree or the source checkout (`cat-file` fails for it). The draft is written at `29023ec1` as instructed. Code and line checks were made at `45b0f7ce` (the live tip). Per the receipt, the packaging commit removes only `SOLR-14187-TESTING.md`, so the Java lines are the same, but this was not checked directly. Every link to `29023ec1` will return 404 until vm2 pushes that commit.
- SOLR-11356: `ls-remote` = `8474e5a3a26d8aacd25d9fc9838c627bc6c5b14f`, matches receipt L4 (live tip). The packaged head `6120dae28d04...` is not on the fork and not in any local repo. Draft written at `6120dae2` as instructed. Line checks were made at `8474e5a3` (same code per the receipt). Links to `6120dae2` resolve only after the push.
- SOLR-6430: `ls-remote` = `c0c6e5dd21c4eee60257db6b85b2fec7f01489b7`, which equals the receipt's packaged head. The fork tip has moved from the registered `22f83870c4a0` to the packaged head, so there is no discrepancy for drafting. Fetched `origin/solr-6430-submit` in the worktree (remote-tracking ref and FETCH_HEAD only) so the head objects could be read.

## Title source

- SOLR-14187: `changelog/unreleased/SOLR-14187-wait-for-async-request-credentials.yml` at `45b0f7ce` (same file at `29023ec1` per the receipt's packaging scope). Used verbatim as the draft's Title line.
- SOLR-11356: `changelog/unreleased/SOLR-11356-cusc-credentials-per-stream.yml` at `8474e5a3`. Used verbatim.
- SOLR-6430: `changelog/unreleased/SOLR-6430-doc-numeric-missing-sort.yml` at `c0c6e5dd`. Used verbatim.

## Material and category reports

- No answers material exists for these tickets or these categories (`material/` holds none for solrj or build-docs; the only hit on these numbers is an unrelated head string in `core-admin-round-1-answers.md`).
- SOLR-14187 and SOLR-11356: `reports/solrj-clients-round-1.md` (lines 29, 31, 89, 102-104, 109, 118) and the per-ticket part `reports/solrj-clients-round-1-s5.md` (items for 11356 and 14187, and owner decisions 5 and 6). These are audit recommendations, not recorded decisions.
- SOLR-6430: `reports/build-docs-misc-round-1.md` (item 13) and `reports/build-docs-misc-round-1-g4.md` (premise accurate; changelog recommended dropped).

## Citation checks

All citations were read at the named SHA with `git show <sha>:<path> | cat -n`.

SOLR-14187:
- Merge-base `e044bf20` `CollectionAdminRequest.java` 1829-1832 (static three-argument helper). Read.
- Merge-base 260 (`processAndWait` passes `propagateBasicAuthCreds(requestStatus(asyncId))`). Read.
- Merge-base 1874 (`RequestStatus.waitFor` copies credentials to `deleteAsyncId`). Read. Poll at 1872 uses the status request's own credentials. Read.
- Head `45b0f7ce` `CollectionAdminRequest.java` 1834-1848 (new overload, javadoc included). Read.
- Head `CollectionAdminRequestAsyncAuthTest.java` 48-58 (credentials test; line 51 is the five-argument call). Read.

SOLR-11356:
- Base `cabedd1d96` `ConcurrentUpdateJettySolrClient.java` 126-129 (old `belongToThisStream`) and 209 (`decorateRequest` on the opening request). Read.
- Head `8474e5a3` `ConcurrentUpdateJettySolrClient.java` 126-128 (captured user, password, headers) and 138-144 (new match). Read.
- Head `ConcurrentUpdateSolrClientTestBase.java` 300-333 (new test; the failing assertion is at 328). Read.
- Base `cabedd1d96` `SolrRequest.java` 374-377 (`getHeaders` returns `Collections.unmodifiableMap` of the live map). Read. File unchanged on the branch.
- `ConcurrentUpdateJettySolrClientTest` extends the base class (8474 line 27); `ConcurrentUpdateJdkSolrClientTest` extends it too (line 24). Read.

SOLR-6430:
- Base `cabedd1d96` ref guide line 202 (old row text). Read.
- Head `c0c6e5dd` ref guide line 202 (new row text). Read.
- Base `FieldType.java` 854-862 (`missingValue` returns null when neither property is set) and 887-888 (`getStringSort` passes `STRING_FIRST`/`STRING_LAST`). Read.
- Base `PointField.java` 318-319 (`getSortField` calls `getNumericSort`); `DatePointField` extends `PointField` (line 101). Read.
- Base `DatePointField.java` 163 (`LongPoint` on `getTime()`, the epoch-millisecond value). Read.

All links in the three drafts use the full 40-character SHA of the named commit. Pre-change symptom links use the merge-base (SOLR-14187: `e044bf20`; SOLR-11356: `cabedd1d96`; SOLR-6430: `cabedd1d96`), and the text says so.

## Choice-section decisions

- SOLR-14187: included. The alternative is real and cheap: `requestStatus(id)`, then `setBasicAuthCredentials`, then `waitFor` already sends the credentials, so a maintainer could plausibly decline a new public overload and document the three-argument helper instead. The draft asks that question.
- SOLR-11356: not included. The only route that keeps the wrong-identity send is "document one identity per client", which leaves the reported bug in place, so it is not a live alternative for a fix. The stream split is stated as a behavior change in "What this change does". The open user-principal question is unverified, not a decision, so it is a Limit.
- SOLR-6430: not included. The alternative (change the default so missing dates sort first) is a broader code fix, so it is one Limits line with a follow-up offer, per the formula.

## Receipt gaps, head discrepancies, and holds

1. HOLD (SOLR-14187, date). The receipt has no run date for the head test or the gate steps. The draft's Proof says "Verified at head `29023ec1ecab...`" with no date. Before posting, the lead should take the date from `/workspace/gates/logs/vm2/14187-premise-head.log` (or the vm2 run record) and add it. The gate file's run plan (2026-10-11T03:58Z) is not a receipt line, so it was not used.
2. HOLD (SOLR-14187, unpushed head). `29023ec1` is not pushed; the gate file says vm2 has the push as its one outstanding step. Links at `29023ec1` do not resolve until then. Do not post before the push lands.
3. SOLR-14187 merge-base wording. The receipt names `e044bf20` as the merge-base. The branch's own changes are against `cabedd1d96`: `git merge-base 45b0f7ce upstream/main` gives `cabedd1d96`, and `cabedd1d96` is an ancestor of `upstream/main` (apache/solr). `CollectionAdminRequest.java` is byte-identical at `e044bf20` and `cabedd1d96` (empty diff), so the symptom links hold at either. The draft keeps the receipt's `e044bf20`. The lead may prefer `cabedd1d96`.
4. HOLD (SOLR-14187, changelog type). The fragment says `type: fixed`. The solrj round report (`reports/solrj-clients-round-1.md`, item 4; S5 owner decision 6) recommends `added` for a new overload. Not applied at `45b0f7ce`. The draft does not mention the type. Changing it moves the head and needs a new head check.
5. HOLD (SOLR-11356, header copy). The solrj round report (item 6; S5 item (a) and owner decision 5) says the header copy is required before the gate. The gated head stores `getHeaders()` as a live view (`ConcurrentUpdateJettySolrClient.java` 128 at `8474e5a3`). The draft states this in Limits. The lead decides: copy the map (the head moves; the focused run would need repeating at the new head) or post with the Limit.
6. SOLR-11356 packaging delta (report only, per the assignment). The packaged head `6120dae28d04` is one local commit on top of the fork tip `8474e5a3a26d` that removes `SOLR-11356-TESTING.md`. It is not pushed. The draft is written at `6120dae2`; the Proof's counts come from the gate run at that packaged head.
7. SOLR-11356 base wording. The receipt says `cabedd1d96` "is not on main's history". In this checkout `cabedd1d96` is an ancestor of `upstream/main` (apache/solr, `3f5d4c5bf8a`) and not of `origin/main` (the fork). The base used is correct for the branch; the wording in the receipt is about the fork's `origin/main`. The claim file's "origin/main merge-base e044bf20b405" refers to the same thing.
8. HOLD (SOLR-6430, changelog). The build-docs round report (item 13) recommends dropping the changelog fragment for SOLR-6430. The draft includes the changelog line at `c0c6e5dd` because the template includes it. If the fragment is dropped, the head moves and the changelog line goes; the Title line would come from elsewhere.
9. SOLR-6430 premise main. The receipt's premise SHA `d76fc14b9857` is not in the local object store, so the premise cannot be re-read here. The draft cites the merge-base, not main.
10. SOLR-6430 string sentence. "String fields sort missing values first" is in the new row (and in the build-docs audit), but the receipt does not verify it. The base code path is `FieldType.getStringSort` (passes `STRING_FIRST`/`STRING_LAST`, and `missingValue` returns null). No sort was run. The draft's Limits says so.
11. Excluded from the SOLR-6430 Proof on purpose: the Antora linked-worktree failure and the retry in a clone (environment, no source change), and the inode and disk notes. Process catches stay out of Proof per the formula.

## Length

Visible text with URLs removed: SOLR-14187 about 3,480 characters (raw 4,550 with 6 links); SOLR-11356 about 2,960 (raw 4,040); SOLR-6430 about 2,370 (raw 3,340). The SOLR-14187 raw count is above the guide because of its links and its Choice section.

## Not verified

- No build, Gradle, test, or gate run. Test counts are from the receipts only.
- Packaged heads `29023ec1` and `6120dae2` are not in any local repository. Their line numbers were checked at the live tips (`45b0f7ce`, `8474e5a3`), which the receipts say differ only by the packaging commit.
- The SOLR-11356 user-principal question (whether it reaches the wire on a shared stream) was not checked.
- The SOLR-14187 head test run date (hold 1).
- Jira was not read or written. GitHub was not read (only git ls-remote and git objects were used). No `gh` call.
- No public write of any kind; no PR, comment, review, or submit-branch edit.
