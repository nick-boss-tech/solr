# Retire final pack: SOLR-17722

Verdict: RETIRE CLEAN. Nothing gainable. The premise failed at the base commit, and the branch change has no effect on the wire on main.

Checked 2026-10-15.
- Fork tip: `origin/solr-17722-submit` = fee3a26beb3c8289057ac41d94dfc8d83a969b71 (ls-remote origin refs/heads/solr-17722-submit). Matches the receipt and RR1.
- Main: upstream main = 714d6ceff852e6dc743ef51ae094e4eaf8f3b3f9 (live). The round used d76fc14b.

## Premise

From `receipts/SOLR-17722.md`:
- "NO GATE by finding. The premise is false at the merge-base: the base-only wire probe passed and verified `wt=javabin` on the captured request, not `wt=json`."
- The run was base only, at 56ec140e3636d5f4150fa87fbf7103529536ac99, test `Solr17722WireProbeTest.testRawWireQueryOverridesRequestWtJson`, seed 9891B4F8740AE16E: PASS.
- No topic-head run and no VM1 result are recorded. RR1 notes that the assignment described vm3 and VM1 runs at base and head. The receipt records only the vm3 base run, and it governs.

## What the branch holds

Merge-base 56ec140e3636. Branch-only commits 0b10917f8c2, 807f1a89c55, fee3a26beb3. Three files:
- `solr/cross-dc-manager/src/java/org/apache/solr/crossdc/manager/messageprocessor/SolrMessageProcessor.java` (+8): sets wt=javabin on the request params before send.
- `solr/cross-dc-manager/src/test/org/apache/solr/crossdc/manager/messageprocessor/SolrMessageProcessorTest.java` (+24): asserts that mutation on a mock. It does not test the wire.
- `SOLR-17722-TESTING.md`: says nothing was compiled or run.

Why the hunk has no effect on main: `solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java`, `initializeSolrParams` (lines 153-163), copies the request params and then calls `params.set(CommonParams.WT, parserToUse.getWriterType())` at line 162, before each send. The branch's wt value is overwritten.

## Gainable, and what is taken

- Gainable: nothing.
- Taken: nothing. The probe source is not on the branch. It ran on the gate host, and the receipt cites its logs under /workspace/gates/logs/vm3/. Deleting the branch does not remove those logs.

## Owner step

1. Label the fork branch `solr-17722-submit` RETIRED and leave it in place (owner rule, 2026-10-14: a retired branch is never deleted).
2. PR: none. No PR authored by nick-boss-tech on apache/solr has this head, in any state (`gh pr list --author nick-boss-tech --state all`, 2026-10-15).
3. Local worktree `wt\SOLR-17722` is clean and tracks origin. Its local branch is `wt-solr-17722-submit`. Leave both as they are.
4. Main-side record: mark RETIRED.

Deletion is not the current rule. It would apply only if the owner reverses the 2026-10-14 ruling: `git -C C:\Users\shaninna\dev\Solr-issues\source push origin --delete solr-17722-submit`. Here origin is the fork (nick-boss-tech/solr).

## Jira note (draft, not posted, optional)

Post at most one comment, and only if you want one. Draft:

"Not reproduced at the base commit. A SolrJ wire probe at the merge-base captured wt=javabin on the raw query, not wt=json, so the premise did not hold there. The branch change sets wt on the request params, but HttpSolrClient.initializeSolrParams overwrites wt before each send on current main, so it has no effect on the wire. No fix is proposed and no PR was opened."

Jira status: not recorded in the local packet and not checked live.

## Condition

The verdict rests on two findings. The premise run shows the symptom does not occur at base. The code read shows the hunk has no wire effect on main. The code read alone closes the branch, even if the premise held. The head or VM1 probe the assignment named was never run, and nothing here needs it.
