# Core admin and collections API round 1: round roll-up

Claim: `claims/core-admin-round-1.md` (commit `b603029f182`). Assignment: `assignments/core-admin-round-1.md` (commit `e8c31a1ed69`). Per-part reports: `reports/core-admin-round-1-k1.md` through `-k6.md`.

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Six read-only subagents did the audit, at the cap of six. No build, Gradle run, or test was run. No submit branch, live PR, JIRA item, or comment was touched. Nothing was posted.

## Heads

Every named head matches its live branch. Three live tips are past their gated heads, flagged in the table: 15003 (three commits past, including a production change), 18010 (the lock and atomic-write change is ungated), and 12849 (the tip is ungated). The local branches in this checkout are stale; two of them are defective or not fast-forward, so do not push from them. Drafts name the live origin heads.

## Per-ticket verdicts

| Ticket | Verdict | Draft | Live head | What blocks it or what the draft must say |
|---|---|---|---|---|
| SOLR-4989 | Draftable, PR-ready | `SOLR-4989.md` | `5bac95376cd4` | Trails main; needs a rebase and a new gate before opening |
| SOLR-15024 | Draftable, one open check | `SOLR-15024.md` | `f95b5010b3fe` | The Jira text is not on disk. Choice: `charFilters` as a list or as keys. Trails main; needs a rebase and a new gate |
| SOLR-13246 | Draftable, PR-ready | `SOLR-13246.md` | `6817c6c0c267` | The PR head is `6817c6c0c267`. The local `solr-13246-submit` (`065b36710adc`) holds a defective version (`newSearcher.getName()` returns the class name, `SolrIndexSearcher.java` 2339-2340); do not push it |
| SOLR-12916 | Audit only; held | none | `ebe5db374336` | The premise holds on main. Needs a fresh gate, a decision on the XML path change, and removal of a handoff file |
| SOLR-17708 | Draftable | `SOLR-17708.md` | `10a7fa07a79a` | Gate green; no PR. Fix the changelog title first. Draft names the deliberate structural exception in Limits |
| SOLR-18010 | Held; no draft | none | `c3685bb37d9d` (moved) | The gated head only strips a marker. The live tip's lock and atomic write are ungated, and the settling run is not on disk |
| SOLR-12849 | Live PR #5011; consistency | none | `6b92223bc24f` | The tip has no gate, the discriminating test is ungated, and the PR's Proof counts are not in the receipt. The main side must gate `6b92223bc24` before the Proof is relied on |
| SOLR-13097 | Live PR #5016; consistency | none | `f0e7395f58f3` | The record says "closed out", but GitHub shows OPEN, CHANGES_REQUESTED, and CONFLICTING with main (a duplicate "Solr 10.2" heading in the upgrade notes). The tip is not gated |
| SOLR-12007 | Drafted; draftable | `SOLR-12007.md` | `bdeba582fd63` | You confirm the inline-cleanup route before posting. Choice in the draft |
| SOLR-17297 | Drafted; held | `SOLR-17297.md` | `c0ab38fc0a8b` | Needs your ruling on the reverse-order gap, and the base failure line |
| SOLR-17377 | Drafted; draftable once the base line is pasted | `SOLR-17377.md` | `22b5f209a11a` | Option 1 is stated, not reopened. The base failure line is a placeholder: the gate log is not on disk |
| SOLR-4502 | Audit only; not ready | none | `4491f5162c1d` | Remove the `TESTING.md` file. The guard covers `create()` only. Guard versus `load()` in the constructor is an open choice |
| SOLR-5011 | Audit only; not ready | none | `f20ffe480781` | Remove the `TESTING.md` file. The `shareSchema` risk is unverified; needs a gate |
| SOLR-11431 | Live PR #5002; consistency | none | `968fad873c4b` | Head matches. The body says CloudSolrClient "treats a 503 like a 404 in its stale-state retry". By reading, a 503 enters the communication-error block (`CloudSolrClient.java` 729); the 404 path (798) is separate. Replacement text is in part k3 |
| SOLR-9750 | Draftable after two fixes | `SOLR-9750.md` | `f97da6da14aa` | The changelog lacks the upgrade step. Handoff wording in two commits |
| SOLR-15805 | Draftable | `SOLR-15805.md` | `3432f950f0ae` | The receipt's PASS does not record the base failure text. The draft says what the code shows. Commit `b2a463cf64f` carries a Claude `Co-Authored-By` trailer |
| SOLR-16108 | Held; no draft | none | `70ad7371b31c` | The branch does not fix the Jira case: one route value still lands in one half. Commit `70ad7371b31` carries a Claude `Co-Authored-By` trailer |
| SOLR-16849 | Draftable as a regression test | `SOLR-16849.md` | `d612b055da20` | Pin with an honest Proof. You decide: open the PR, or close it as fixed by SOLR-18083 |
| SOLR-18278 | Retire; no draft | none | `bf19c9fa4498` | The retire call is yours |
| SOLR-18317 | Banked state confirmed; no draft | none | `ae918a03fa7b` | The changelog conflicts with main, and the branch still carries UI work that PR #5001 shipped |
| SOLR-5262 | Audit only; not ready | none | `ade8b80264ac` | The premise holds by reading. No gate yet |
| SOLR-8554 | Audit only; not ready | none | `32697b6c3f82` | The title and premise miss the ticket (the Overseer move is not done). Title: "SOLR-8554: FORCELEADER returns an error when no active leader appears or the shard is removed". Fix the changelog NullPointerException phrase |
| SOLR-8628 | Audit only; held | none | `ce8211e05e06` | The ticket's empty-directory case already works on main. The branch fixes a `write.lock`-only directory |
| SOLR-11939 | Drafted; docs only | `SOLR-11939.md` | `d4cff5e76430` | Docs-only, judged as a review question: ready |
| SOLR-16499 | Audit only; not ready | none | `6a2ff7618d9a` | No gate. The guide says a 300 second default; the code uses 600 |
| SOLR-14098 | Held; no draft | none | `dd4995515e88` | The fix covers only the BUFFERING case. The ticket symptom is unverified. The ledger date conflicts with the head date |
| SOLR-15003 | Held | none | `1004abee39ab` | The live tip is three commits past the gated head, including a production change. Gate owed |
| SOLR-6438 | Draftable | `SOLR-6438.md` | `8c77988d5917` | Choice: reject, or merge both. The history has commits that need the owner's rewrite decision |
| SOLR-8275 | PR-ready | `SOLR-8275.md` | `e52e10fa50a3` | No hold |
| SOLR-8576 | Draftable after one fix; draft held | `SOLR-8576.md` | `4c46f95c7851` | The alias assertion at `CollectionsAPISolrJTest.java` line 1188 checks a collection named like the alias, not the alias. Fix before the draft is used |
| SOLR-16725 | Draftable | `SOLR-16725.md` | `be1838ef8ccf` | One Choice (strings or numbers). Limits must name `maxShardsPerNode` |
| SOLR-16887 | Held | none | `42675f65d6fc` | The receipt says PR-ready; the audit disagrees. The branch drops `-XX:ErrorFile` from `bin/solr` and `solr.cmd`, which loses the native-crash file location for every JVM fatal error. The ticket does not ask for that. Restore the flag |
| SOLR-17731 | Draftable after three fixes; draft held | `SOLR-17731.md` | `f2b4ba164f56` | `ListAliasesAPITest` never ran. A malformed license header. Comments state an unverified rule |

## Interactions and landing order

- **LukeRequestHandler (4989 and 15024):** the hunks do not overlap; a trial merge is clean. Land 4989, then 15024. Both trail main and need a rebase and a new gate.
- **QuerySenderListener (13246 and 12916):** the hunks do not overlap; a trial merge is clean. Land 13246, then 12916.
- **Request path (12849, 13097, 17708):** the hunks of 17708 are checked against the two live PRs in part k2. Landing order matters if the PRs move.
- **NodeConfig (17297 and 17377):** checked against each other in part k3. 17377 also changes `SolrXmlConfig.java`.
- **Security (13097, 17708, 18010):** shared files are noted in part k2; keep the three accounts consistent.
- **CollectionsAPISolrJTest (8576 and SOLR-16437 in the SolrCloud round):** the class is shared. The drafts must not claim its coverage twice.

## Owner decisions

1. SOLR-12007: confirm the inline-cleanup route before posting.
2. SOLR-17297: a ruling on the reverse-order gap, and the base failure line.
3. SOLR-17377: paste the base failure line (the gate log is not on disk).
4. SOLR-16849: open the PR, or close it as fixed by SOLR-18083.
5. SOLR-16108: hold, or close.
6. SOLR-18278: retire.
7. SOLR-6438: reject, or merge both.
8. SOLR-16725: strings or numbers.
9. SOLR-4502: guard placement (`create()` versus `load()`).
10. SOLR-15024: the `charFilters` list-versus-keys Choice.
11. SOLR-16887: restore `-XX:ErrorFile`.
12. History rewrites: the Claude trailers on `b2a463cf64f` (15805), `70ad7371b31` (16108), and the handoff wording in several branches. Rewriting fork history needs your OK.
13. Re-gates owed: 12849 (`6b92223bc24`), 15003 (live tip), 18010 (live tip), and the rebase-and-regate for 4989 and 15024.

## Draft fixes before posting

- Remove the `TESTING.md` files on the audit-only branches, and the root `TESTING.md` files on five branches, before any PR.
- Remove the `<!-- INTERNAL ... -->` blocks in `SOLR-6438.md`, `SOLR-8275.md`, `SOLR-8576.md`, and `SOLR-17731.md` before posting.
- `SOLR-17731.md`: held until the three fixes are made.

## Housekeeping

- Part k5 left scratch files at `/tmp_files_<ticket>.txt` and `C:\Users\shaninna\all_files.txt`. A removal was blocked by the safety check. Those files are outside the repository and should be deleted by you.
- The local `solr-13246-submit` branch holds a defective version; do not push it. The local `solr-4989-submit` branch is not a fast-forward of the live head and adds a handoff file; do not push it.

## Not done

No build, test, Gradle run, `gh` write call, fetch, commit, or post. Gate logs and probe logs named in the receipts are not on disk, so counts are receipt-only and some base failure lines are placeholders. Live JIRA was not queried.
