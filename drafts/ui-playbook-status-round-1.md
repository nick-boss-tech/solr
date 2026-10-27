# Proposed UI-PLAYBOOK status text, round 1

Draft for whoever maintains `UI-PLAYBOOK.md`, posted by the phone agent under `assignments/figma-wrap-up-round-1.md` item A5. The playbook itself is not edited. This text is proposed as the replacement for its "Where the tickets stand" section; the old verdicts there predate the Figma node read and the vm6 gates.

## Proposed text

Where the tickets stand (2026-10-26). Every component branch below is gated PASS at the head named in `reports/new-ui-figma-reconciliation-round-1.md`, and the staged asks were posted to Christos Malliaridis on Jira on 2026-10-26. The next step on each ticket is his answer; no PR opens without the owner's go, and under the current creation cap a new PR would be created as a draft.

- Independent and ready once Christos answers: SOLR-17911 (file content viewer), SOLR-17887 (file tree), SOLR-17909 (breadcrumb).
- Stacked pair: SOLR-17913 (collection details card), then SOLR-17914 from the stacked branch only; the unstacked 17914 branch is not a PR vehicle.
- SOLR-17660: open only from `solr-17660-basic-list-upstream`. The fork-main branch is superseded and must not be opened.
- Held on Christos: SOLR-17850 (whether the Figma field set beyond the implemented parameters is a later slice), and behind it SOLR-17849, whose screen calls the 17850 form. The 17849 ask leads with Jane Sandberg, who volunteered on the ticket.
- SOLR-17861 (not a Figma ticket): the checksum slice is gated; the posted ask includes whether LICENSE and NOTICE presence moves to SOLR-17970.
- Bases: never base a UI PR on the fork's main. The branches sit on apache main `c8ab71430f`, and apache main still has no `ui/` commits since that base; re-check before opening.

Running the UI JVM tests (Q7). Default route: the pool script `material/ui-test-setup/run-ui-jvm-tests.sh` where it works. Fallback, the vm4 Mesa route: install micromamba and the conda-forge packages `libegl libgl libglvnd mesalib` into a prefix, and put the prefix's `lib` on `LD_LIBRARY_PATH` before running the tests.
