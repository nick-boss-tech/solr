# Decisions under the ladder, SOLR-6711 and SOLR-16013, round 1

Verdict: **all four items are settled without asking Nick.** No draft text changes are needed. One record line should be updated for the main side.

Scope: `assignments/pool-decisions-ladder-6711-16013-round-1.md`. Claim: `claims/pool-decisions-ladder-6711-16013-round-1.md`. One read-only subagent, as the assignment names. Code was read at the fork heads `73c5e6e0d243` (SOLR-6711) and `408317d7e241` (SOLR-16013), and at the base `cabedd1d968` for SOLR-6711.

## I1. SOLR-6711 Choice on opt-in persistence: KEPT, draft unchanged

- **Rung.** The decision default: a recorded recommendation is adopted.
- **Recorded.** `material/replication-backup-round-1-answers.md` lines 61 and 154 say "Recommendation: keep opt-in, as implemented." Both sit under a DISCUSS heading, and line 3 says the DISCUSS calls are "not taken".
- **Adopted.** `material/coordinator-handoff.md` lines 49 and 86 (dated 2026-10-11) say a recorded recommendation is adopted as the decision. That covers this question.
- **Code.** `ReplicationHandler.java` line 312 is `disablePoll(solrParams.getBool(PERSIST, false), rsp);`, so opt-in is what the code does.
- **Draft.** The Choice (`pr-drafts/replication-backup/SOLR-6711.md` lines 25 to 29) stays a question, with opt-in as the implemented position.
- **Proposed record line for the main side.** `material/replication-backup-round-1-answers.md` line 61 should read: "Call taken under the decision default: keep opt-in, as implemented."

## I2. SOLR-6711 restart sentence: KEPT WITH LINKS

- **Rung.** Information from the code. No replacement needed.
- **Base.** `ReplicationHandler.java` line 269 is an in-memory `AtomicBoolean pollDisabled`. `disablePoll` at lines 813 to 821 sets that flag at line 815 and writes nothing.
- **Startup.** The base reads no persisted poll setting at startup. The startup read at lines 1347 to 1348 exists only at the head.

## I3. SOLR-16013 "Stopping a node can take longer": KEPT

- **Rung.** Behaviour follows the code, so no owner confirmation is needed.
- **Head.** `ZkController.java` at `408317d7e241` queues the Overseer close as one task (lines 883 to 888), waits on it at line 916, and only then closes the state reader and client (lines 926 and 932). Interrupts set a flag and the loop continues (lines 918 to 919).

## I4. SOLR-16013 "The election nodes are released after that wait": KEPT

- **Rung.** Information from the code. No replacement needed.
- **Ephemeral nodes.** `LeaderElector.java` lines 242 and 256 use `EPHEMERAL_SEQUENTIAL`. `OverseerElectionContext.java` line 64 creates the leader node as `EPHEMERAL`.
- **Close.** Nothing in the close path deletes the election node. The node goes only when the ZooKeeper session ends, at `zkClient.close()` (`ZkController.java` line 932), which runs after the wait at line 916.

## Summary

Four items settled under the ladder. None needed Nick. The only follow-up is the record line in I1, which belongs to the main side.

## Convergence line

Nothing in the two drafts changes. The SOLR-6711 Choice stays as a question, and the SOLR-16013 behaviour claims stand.
