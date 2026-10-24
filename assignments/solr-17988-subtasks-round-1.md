# Assignment: solr-17988-subtasks-round-1 (the SOLR-17988 docs gaps, one claim per subtask)

- Status: OPEN. Intended host: any pool Linux host with the `implementation` or `review` tag. Posted by phone agent on behalf of Nick Shanin, 2026-10-23.
- Parent ticket: SOLR-17988 (Blocker, owner Eric Pugh), "Ensure Solr 10 Release Conveys Changes Users Need To Be Aware Of". The evidence base is the property-transition audit at `reports/solr-17988-property-audit-round-1.md` on this board (vm6; name-level comparison of the 9.x and 10.0 trees). Its verdict: covered with named gaps, ranked in its section 3.
- Why subtasks: the follow-up is one PR, but the gaps are independent and each is about the size of a ticket. Claim one subtask at a time under the board's small-claims rule; name the subtask in the claim file.

## Every subtask has the same two steps

1. **Verify.** A host other than vm6 re-checks the subtask's gaps against the 9.x and 10.0 trees at the audit's cited lines and commits. Record the verdict in the claim: confirmed, corrected (with the correction), or not reproduced. A gap that does not verify stops there; it does not get edited.
2. **Draft the diff.** Make the doc changes for the confirmed gaps on a branch named `solr-17988-st<N>` and push it to the fork. The branch's diff is the deliverable; record its head in the claim and in `reports/solr-17988-subtasks-round-1.md`.

## Subtasks

- **ST1, the silent env var renames (audit gap 1 and 4).** Upgrade-note entries in the Solr 10 major changes for `SOLR_PORT` to `SOLR_PORT_LISTEN`, `SOLR_HOST` to `SOLR_HOST_ADVERTISE` (the current note says "deprecated"; the 10.0 scripts do not read the old name and do not warn, so the entry should say removed), `SOLR_REQUESTLOG_ENABLED` to `SOLR_LOGS_REQUESTLOG_ENABLED`, `SOLR_ADMIN_UI_DISABLED` to the inverted `SOLR_UI_ENABLED`, and `SOLR_WAIT_FOR_ZK` to `SOLR_CLOUD_WAIT_FOR_ZK_SECONDS`. This is the audit's main gap.
- **ST2, the stale port docs (gap 2).** Correct `SOLR_PORT` in `solr-control-script-reference.adoc` (the stop-port derivation) and `upgrading-a-solr-cluster.adoc`; give `SOLR_PORT_LISTEN` a doc home.
- **ST3, the undocumented new env vars (gap 3).** Doc entries for `SOLR_UI_ENABLED`, `SOLR_UI_EXPERIMENTAL_ENABLED`, and `SOLR_CLOUD_WAIT_FOR_ZK_SECONDS` in the Ref Guide pages where their siblings live; locate the right homes from the surrounding entries.
- **ST4, the stale sysprop entries (gap 5).** Remove or correct the 10.0 doc entries for properties absent from 10.0 code: `solr.disableFingerprint`, `solr.dns.prevent.reverse.lookup`, `solr.allow.unsafe.resourceloading`, `solr.node` (in `solr-properties.adoc` and `node-roles.adoc`).
- **ST5, undocumented renames and the script removal (gaps 6 and 7).** Note entries for the `solr.jetty.ssl.needClientAuth` and `wantClientAuth` renames (the audit marks the mapping as inferred from names; verification matters most here) and for the removal of `bin/post` and `bin/postlogs`.
- **ST6, the 56 undocumented new sysprops (gap 9). Assessment only, no doc edits.** Rank the operator-facing groups (the Jetty HTTP and SSL tuning names first) and recommend the small set that belongs in this PR; the rest is named as a follow-up. Deliverable: the ranking section in `reports/solr-17988-subtasks-round-1.md`.
- **ST7, assembly.** After ST1 to ST5 land: combine the subtask branches onto `solr-17988-docs`, add any ST6 entries the report recommends and the owner approves, run the Ref Guide build validation, and write the PR draft under `pr-formula.md`. Record the final head and the build result in the report. Opening the PR waits for the owner's go; the Jira comment is then drafted under `assignments/pr-followups-round-1.md`.

## Limits

- Docs only: Ref Guide pages and the upgrade notes. No code changes, no script changes (whether `SOLR_HOST` should warn like `SOLR_JETTY_HOST` is a code question for the ticket, not for this PR).
- Keep the PR narrow: only what the audit ranks as gaps 1 to 7, plus ST6's approved minimum.
- Commit as Nick Shanin with no trailers. No em dashes in the doc text.
