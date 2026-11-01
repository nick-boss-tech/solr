# Verdict: SOLR-13186, Overseer election race registers an invalid leader node

Verdict: STILL LIVE on main, promote to opening the existing fix branch,
not closure and not new implementation. The hole is exact and narrow:
when a node wins the Overseer election and creates the ephemeral leader
registration, then does not start an Overseer behind it, the
registration stays in place with no Overseer running, and the cluster
is left without an Overseer until that node is removed. A fix branch
already exists on the fork and covers this hole; it is unmerged, has
no PR, and has no gate record. Nothing closes without a maintainer's
or Nick's go.

Ticket: https://issues.apache.org/jira/browse/SOLR-13186 (2019,
reporter and assignee Mark Miller, Open, Major, last updated
2019-06-08, no fix version). The ticket is terse: no comments, no
links, and a one-sentence description, quoted in full: "These can
leave you without an Overseer until the bad ephemeral leader
registration node is removed." The summary names the mechanism: when
a node wins the Overseer election there is a race that can cause an
invalid Overseer leader node to be registered.

## Code read (current main as checked)

Checked against the local main checkout at e55d2e7bcb (2026-09-29).
The tree is unmodified there; the checkout is shallow (363 commits
back to June 2026), so no whole-history claim is made here.

The registration path is
solr/core/src/java/org/apache/solr/cloud/OverseerElectionContext.java,
runLeaderProcess:

- It logs "I am going to be the leader", then creates the ephemeral
  node at /overseer_elect/leader holding its own sequence id, via
  zkClient.makePath(leaderPath, ..., CreateMode.EPHEMERAL). That
  overload defaults failOnExists to true, so a pre-existing leader
  node throws NodeExistsException.
- It then starts the Overseer only inside a synchronized check:
  if not closed and the core container is not shut down,
  overseer.start(id). If either condition fails, the method returns
  with the registration already created and no Overseer started.
- Nothing in that path removes the registration on the skip. close()
  only sets isClosed and closes the Overseer; it does not delete the
  leader node. The only cleanup is the ephemeral node dying with the
  registering session.

That is the ticket's shape, in code:

- Readers trust the node. Overseer.amILeader reads
  /overseer_elect/leader and compares its id; Overseer
  checkIfIamStillLeader reads the same path. A registration with no
  running Overseer behind it answers those reads with an id that
  leads nowhere.
- A later election winner is blocked by the same node. When the next
  node in line runs its own runLeaderProcess, makePath throws
  NodeExistsException, and LeaderElector.checkIfIamLeader catches
  exactly that exception and sends the winner back to retryElection.
  The bad node therefore does not just sit there; it keeps the next
  leader from registering until it is gone, which is the "without an
  Overseer until the bad node is removed" outcome.
- The handled case is the contrast, not the fix. The NodeExists on
  takeover path retries, so a stale node left by a dead leader is
  retried around. The unhandled case is the live session that
  registered and then did not start: no retry and no removal apply
  to it on main.

## Existing fix branch (unmerged)

Fork branch solr-13186-submit at b436d90d2a (2026-10-06, Nick
Shanin) fixes exactly this hole: runLeaderProcess returns after a
successful start, and otherwise calls removeLeaderRegistration, which
reads the leader node and deletes it, version guarded, only when the
stored id still matches, tolerating NoNode, BadVersion, and a closed
client (in which case the ephemeral registration dies with the
session anyway). The branch also adds
solr/core/src/test/org/apache/solr/cloud/OverseerElectionContextTest.java
(new file, absent on main) and a changelog fragment. A merge-base
check confirms the fix is not in main, solr-work.sh finds no GitHub
PR for it, and the board has no receipt or draft for this ticket.
Fork branch ci/13186-overseer is the same tip plus a fork-runner
workflow commit; it is not gate evidence.

## Next step and gaps, stated plainly

Next step: rebase solr-13186-submit onto current main, gate it, and
open it, rather than writing a new fix. This verdict is a code read,
not a reproduction: the close-or-shutdown landing between makePath
and the start check was not executed here. One adjacent window is
left open by this record and belongs to the gate or review pass:
whether overseer.start throwing after registration leaves the same
dangling node, since the branch's removal covers the guarded skip
path only. The ticket's summary is broader than the one hole the
branch fixes ("when a node wins the election"), so the review pass
should also confirm no second registration leg hides behind that
wording before the PR text claims the ticket closed.
