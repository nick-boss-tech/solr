# Assignment: check whether the SOLR-18317 server branch's work is already covered, round 1

Status: DONE

Capability tags: [review]. Intended host: Windows (review agent). Claim under claims/ before starting, per WORKFLOW.md. This is a code-reading assignment: no builds run on Windows. If a verdict turns on runtime behavior, file a gate job instead (see below).

## Owner directive

Nick, 2026-10-14: run the coverage check on the split-out SOLR-18317 server branch. His recorded expectation is that a new ticket will likely be needed for whatever gap remains, so a drafted ticket text for the NOT COVERED case is part of the deliverable. The draft waits for Nick: this assignment does not file the ticket, open a PR, or touch any branch.

## Background

- PR #5001 (SOLR-18317, "standalone solr 10 logging UI tries to access zookeeper: NullPointerException") merged on 2026-10-05 with the Admin UI side fixed. SOLR-18317 is Resolved in Jira.
- The server-side hardening was split out at reviewer request and preserved on the fork branch `solr-18317-server-submit`, tip `ae918a03fa7b622853aa1c5ba3e53ab567e2fef7`, never opened. The full story is in `reports/merged-pr-followup-promises-round-1.md` (round 2 section, the #5001 item and its addendum), including Pugh's redirect of the follow-up to SOLR-15823.
- The SOLR-15823 pair is open: #5030 (PUT side: V2 logging level changes broadcast to named nodes, the Admin UI migrated to V2) and #5031 (GET side: nodes broadcast on the V2 logging levels listing). The pair's file set covers the logging API and the Admin UI logging page; whether it covers any of the server branch's behavior is part of what this check decides.
- The branch has exactly 8 commits of its own on base `0d2a4649c7` and no merges, so its own diff is readable without merge noise. Two of the 8 are bookkeeping (a testing handoff file added, then removed at the tip). The branch carries its own tests; the scenarios those tests pin are the behavior spec for each item.

## Starting itemization (confirm or correct it against the diffs)

Extract each item from the branch's own commits (`git log` and `git show` on `solr-18317-server-submit`); if the diffs support a different split, use the better split and say so in the deliverable.

1. **Proxy layer: standalone nodes=all is answered locally.** Commits `fc17add67a` ("Treat standalone nodes=all as local") and `42418afcae` (HttpJettySolrClient downcast restore). `RemoteRequestProxy`, `GenericV1RequestProxy`, and `V2SolrRequestBasedProxy`: with no ZkController present, a nodes=all admin request no longer goes through the ZooKeeper live-nodes path (the path behind the original NullPointerException) and is served by the local node instead.
2. **Metrics handler narrowing and the Admin UI logging caller.** Commits `4ca4725cb8` and `6a3002106b` (comment correction). `MetricsHandler` handles nodes=all in standalone mode; the Admin UI logging caller (`solr/webapp/web/js/angular/controllers/logging.js`, `services.js`) is fixed to match; the metrics node=all comment is corrected to describe the behavior.
3. **The standalone-all opt-in moves to the broadcast call sites.** Commit `f887bed7d7`. `LoggingHandler`, `MetricsHandler`, `SystemInfoHandler`, and `GetNodeSystemInfo` declare the standalone-all opt-in where the broadcast happens, and the proxy classes stop deciding it centrally (the opt-in code leaves `GenericV1RequestProxy` and `V2SolrRequestBasedProxy`).
4. **Proxy cleanup and system-info coverage.** Commit `3dceafb9a6`. The unused `shouldProxyTo` parameter is dropped from the proxy layer, and `GetNodeSystemInfoTest` pins the system-info nodes behavior in standalone mode.
5. **Admin UI mode on system-info failure.** Commit `ae918a03fa` (the `app.js` part). The Admin UI settles its mode when the system-info call fails, instead of leaving the mode unset in standalone.

## The check, per item

For each item, read the code at current apache/solr main (fetch `origin/main` fresh; cite file and method for every claim) and give exactly one verdict:

- **COVERED on main**: the behavior the branch produces is already what main does. Name the covering code, or the merged change that brought it in.
- **COVERED by the SOLR-15823 pair**: the behavior lands only if #5030 or #5031 merges. Name which PR and what in it produces the behavior.
- **NOT COVERED**: a live gap. Describe the concrete standalone scenario that still misbehaves (which admin call, which parameter, what the user or the UI sees), derived from the code, not from the branch's age.
- **MOOT**: the surrounding design on main changed so the item no longer applies. Say what changed and why the item's premise is gone.

Notes on scope: the Admin UI side of #5001 already merged, so an item is not COVERED merely because the UI stopped sending the request; the question is what the server does when the request arrives (another client, an older UI, a direct call). Judge the pair's coverage from its actual diff, not its title.

## If a verdict needs a run

Code reading settles most of this. If an item's verdict turns on runtime behavior that reading cannot settle (for example, what a standalone node actually answers to a nodes=all admin call on current main), do not run a build on the review host. File `gates/SOLR-18317-server-coverage-premise.md` for a Linux host instead: branch or main head to run, the exact standalone scenario (server mode, request, parameters), the expected answer under each verdict it would settle, and the gate shape from the standing record (tidy, Error Prone compile, focused run counted from fresh JUnit XML). Record in the deliverable which items wait on that job.

## Deliverable

`reports/solr-18317-server-coverage-round-1.md`:

- The confirmed itemization (with any corrections to the starting split).
- Per item: the branch behavior in brief, the verdict, and the citations (file and method on current main, or the pair PR and its change, or the gap scenario, or the design change that moots it).
- A summary of what, if anything, remains live.
- If any item is NOT COVERED, a drafted new-ticket text for the remaining gap, in the same report: one or two paragraphs maximum, Jira wiki markup, opening with the Jira AI header `🤖 _AI text below_ 🤖 _(posted on behalf of Nick Shanin)_`, stating the standalone symptom and the expected behavior. Plain language, no internal process vocabulary. One ticket for one coherent gap; if the gaps are genuinely separate defects, draft separately and say why. The draft is text for Nick's decision; nothing is filed.

## What this assignment does not authorize

- No Jira ticket is filed, no PR is opened, no comment is posted, and no branch is modified, including `solr-18317-server-submit` itself.
- No builds on the review host; run-shaped questions go through the gate job above.
