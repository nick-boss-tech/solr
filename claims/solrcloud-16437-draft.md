# Claim: SOLR-16437 first gate, check and draft

Claimed 2026-10-10 by Claude Code (AI agent), working for Nick Shanin.

Trigger: `pr-prepare` moved from `46c4401ee8c` to `9125af019ec`. That commit ("Receipt: SOLR-16437 gate green at its packaged head") refreshes `receipts/SOLR-16437.md` from NO GATE to GATE GREEN. No assignment or material file came with it. The SolrCloud round 1 audit (`reports/solrcloud-round-1-p5.md`, verdict on 16437) said the branch becomes draftable once a first gate runs on the main side, and the adopted answers (`material/solrcloud-round-1-answers.md`, the 16437 entry) fix its scope.

Scope, two parts, one ticket:

1. **Part a: receipt check.** Check the refreshed receipt against the branch at its live head, and give a verdict on gate state.
2. **Part b: draft.** If part a finds the gate usable, write `pr-drafts/solrcloud/SOLR-16437.md` following `pr-formula.md` and the adopted answers. Two subagents for one ticket is the minimum the rule allows; the work split is by part, not by branch.

## Heads checked live on 2026-10-10

| Ticket | Branch | Live head | Receipt head | Result |
|---|---|---|---|---|
| 16437 | `solr-16437-submit` | `aa2a6b8afb6f` | `aa2a6b8afb6f` (refreshed at `9125af019ec`) | matches |

The head was fetched explicitly (`git fetch origin refs/heads/solr-16437-submit:refs/remotes/origin/solr-16437-submit`). The local branch ref `refs/heads/solr-16437-submit` lags and must not be used (p5 finding 19).

## Shared rules for every part

- Edit only the named draft, in place. Read only otherwise. No commit, push, checkout, reset, merge, stash, or fetch beyond the head above.
- `git show`, `git log`, `git diff`, `git rev-parse`, `git cat-file -e`, `git grep`, `git ls-tree`, and `git merge-tree --write-tree` (a trial merge that writes no ref) are fine.
- No builds, no Gradle, no tests. No `gh` write calls. Post nothing.
- The gate log `g16437-gate.log` is not expected on disk. Say so if it is missing. Proof numbers come only from the receipt.
- Plain words. No em dash and no en dash, anywhere, including the report.
- Every finding: file and line, evidence, and exact replacement wording. Mark FIX or NOTE. If you cannot check something, say so.
- Return the part report as your final message text. The lead writes the report file.

## Parts

**Part a: receipt check.** Receipt: `receipts/SOLR-16437.md`.
- The packaging commit `aa2a6b8afb6` removes `SOLR-16437-TESTING.md` only. Confirm `git diff 673ae584de0 aa2a6b8afb6` touches only that file. The receipt says tidy ran clean on the received tree, so check that the gated head differs from the tidied tree by that note file alone.
- Commit bodies from `e2cdb2d7e8ae..aa2a6b8afb6`: list any still carrying "Hypothetical", "handoff", or similar process wording. Subjects stay, as adopted (p5 finding 1, and the adopted answers); report which ones.
- The merge-base of the head with upstream/main. The receipt names `e2cdb2d7e8ae`. upstream/main is now `8e62c2686882`. Confirm the merge-base and that a trial merge is clean.
- The premise by reading: the check in `CollApiCmds.java` at the head (the ADDREPLICAPROP existence checks, and the shard-scoped lookup), and the new test `testAddReplicaPropRejectsUnknownReplica` in `CollectionsAPISolrJTest.java`. Confirm the base code returns success for an unknown replica (`ReplicaMutator.java` around lines 162 to 163 and the base `CollApiCmds.java`).
- The receipt's counts and Proof: the class 25 tests, 1 skipped, exactly 1 failure with `CollApiCmds.java` reverted. Report them as the receipt's numbers, and note that they are not verified here.
- The changelog fragment `changelog/unreleased/SOLR-16437-addreplicaprop-validate-inputs.yml` at the head: the `nick:` line is removed, and the other fields are as before.
- Verdict: usable gate, or hold.

**Part b: draft.** Write `pr-drafts/solrcloud/SOLR-16437.md`. Requirements:
- Formula from `pr-formula.md`: AI header line, Jira link, bold one-line summary per section, Proof, a Choice section if one is owed, Limits, Changelog link, AI footer.
- The summary says what ADDREPLICAPROP now does with an unknown collection, shard or replica. Confirm each of those checks in `CollApiCmds.java` at the head.
- Scope: ADDREPLICAPROP only. The adopted answer says DELETEREPLICAPROP and the other APIs the ticket names go into Limits with a follow-up offer. The follow-up offer is the standing rule; state it plainly.
- Behavior change to state in the text (p5 finding 13): a valid replica name given with the wrong shard is now rejected with a 400. Before, it changed the replica in the other shard.
- Proof: name `testAddReplicaPropRejectsUnknownReplica` and its one failure on base, from the receipt. Do not restate the class counts (25 tests, 1 skipped): the adopted answer keeps them in the 8576 draft only.
- Citations link to the blobs at `aa2a6b8afb6f1720c0a04b7869f331ab47bf554d`. A base-state citation links to `e2cdb2d7e8ae`, and is named as base.
- Length: count characters with links (`LC_ALL=C.UTF-8 wc -m`) and report the number. The guide is about 3,500.
- No process words in public text (receipt, gate, round, owed, premise, takeover, handoff, TESTING). No dashes.

## Deliverables

1. Part a report, as text. The lead writes `reports/solrcloud-16437-receipt-check.md`.
2. Part b report, as text. The lead writes `reports/solrcloud-16437-draft.md`.
3. The draft `pr-drafts/solrcloud/SOLR-16437.md`, only if part a finds the gate usable.

## Not in scope

Opening or editing PRs, posting comments, editing submit branches or live PR descriptions (including the commit-history rewrite and the changelog author rule, which are owner decisions), builds, Gradle, and test runs.
