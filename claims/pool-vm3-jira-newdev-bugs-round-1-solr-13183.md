# Claim: pool-vm3-jira-newdev-bugs-round-1, slice SOLR-13183 (item 2 only)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: DONE (not reproduced; no branch).
- Job or assignment: `assignments/pool-jira-newdev-bugs-round-1.md`, item 2.
- Capability tags used: implementation, gate.
- Started: 2026-10-19T12:53:39Z (UTC).

Heartbeat: 2026-10-19T12:53:39Z

## Result (vm3, 2026-10-19)

- Code path: the ticket's SolrDispatchFilter.doFilter no longer exists at base c8ab71430f. SOLR-18112 (b3b2f83fe9) renamed it to SolrServlet. The base dispatches through ServletUtils.getPathAfterContext (solr/core/src/java/org/apache/solr/servlet/ServletUtils.java lines 59 to 61), which handles a null path info.
- Reproduction: throwaway test Repro13183Test (SuppressSSL, SolrJettyTestRule, collection films, default configset) at base c8ab71430f, seed 13183C0FFEE13183, job repro13183c-132359, logs /workspace/gates/logs/vm3/13183/repro13183c-132359/. Results: /films/schema/%25 400, /films/%25 400, /films/schema/%2525 400, each "Ambiguous URI path encoding" from the container, before Solr runs. Earlier run repro13183b-131302 gave the same 400 for /films/schema/%25. No 500 and no NullPointerException.
- Conclusive: the same 400 on the ticket's input in two runs, plus two more encodings. The ticket's symptom did not reproduce on the pinned base.
- Verdict: not reproduced. Default in the assignment: record and stop; no branch pushed. Throwaway worktree removed.
- Limits: the test used the default configset and a Jetty test container. The ticket's reporter used a SolrJ client, which this test did not exercise.

DONE: 2026-10-19. Deliverables: this claim file (result section). No branch.
