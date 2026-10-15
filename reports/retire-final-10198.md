# Retire pack: SOLR-10198 (branch solr-10198-submit)

Verdict: RETIRE CLEAN. The change is merged, and the branch holds nothing that main lacks.
Round item: reports/retire-candidates-review-round-rr2.md, item 1. Checked 2026-10-15 against live upstream main 714d6ceff852e6dc743ef51ae094e4eaf8f3b3f9 (ls-remote).

## Premise state

receipts/SOLR-10198.md: "Status: MERGED. The pull request for this branch was merged into apache/solr main on 2026-10-07 ... There is nothing to draft." It also says the live tip is the exact head that was merged, with merge commit e97c2a0884cbd.

## Head checked

- ls-remote origin refs/heads/solr-10198-submit: 9448cda146f7e09f3389551c4ef883a63d415eeb (2026-10-15).
- PR #4965 (apache/solr, GitHub API, 2026-10-15): state MERGED, mergedAt 2026-10-07T13:19:33Z, headRefOid 9448cda146f7e09f3389551c4ef883a63d415eeb, mergeCommit e97c2a0884cbd68790fd7e7036fbb7800db27856.

## What the branch holds, and whether main has it

- The squash commit e97c2a0884cb changes five files against its parent 52fba69a4eb (+293/-5): changelog/unreleased/SOLR-10198.yml; solr/core/src/java/org/apache/solr/client/solrj/embedded/EmbeddedSolrServer.java; solr/core/src/java/org/apache/solr/response/DocsStreamer.java; solr/core/src/test/org/apache/solr/client/solrj/embedded/TestEmbeddedSolrServerStreamingTypes.java; solr/core/src/test/org/apache/solr/handler/tagger/EmbeddedSolrNoSerializeTest.java.
- All five paths exist on live main. `git diff 9448cda146f7 714d6ceff852` over those five paths is empty (2026-10-15). The branch's net content is on main.
- The ten branch commits not reachable from main are the pre-squash history of the same change (rr2).

## Gainable and taken

- Gainable: nothing.
- Taken: all of it, merged as e97c2a0884cb.
- Fork CI: no runs on solr-10198-submit (rr2, `gh run list --branch`).

## Owner step

1. Record the closeout: "merged as e97c2a0884cb (PR #4965, 2026-10-07); fork branch solr-10198-submit left in place." Under the owner's current retire rule (2026-10-14: a retired branch is labelled retired in the records and left in place, never deleted), this is the whole step. The receipt already says the ticket is closed out. Inventory row 388 still reads "live PR #4965" and can be updated by the owner.
2. Deletion is optional and not the current rule. Only if the owner chooses the harvest-and-delete pile: `git -C C:\Users\shaninna\dev\Solr-issues\source push origin --delete solr-10198-submit`. Here origin is the fork (nick-boss-tech/solr). This does not touch apache/solr.
3. PR to close: none. #4965 is merged, and no open PR has that head.
4. Jira note: none needed for the closing or the deletion. Nothing is drafted. Jira state was not checked in this pass; the receipt says closed out.

## Condition

None beyond the merge. The verdict holds while the five paths stay as merged on main. If the owner has reason to doubt that, repeat the `git diff 9448cda146f7 714d6ceff852` check over the five paths.
