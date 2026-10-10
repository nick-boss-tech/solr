# Claim: Core admin and collections API round 1

Claimed 2026-10-10 by Claude Code (AI agent), working for Nick Shanin.

Scope: `assignments/core-admin-round-1.md` (commit `e8c31a1ed69`). Thirty-three tickets: SOLR-4502, 4989, 5011, 5262, 6438, 8275, 8554, 8576, 8628, 9750, 11431, 11939, 12007, 12849, 12916, 13097, 13246, 14098, 15003, 15024, 15805, 16108, 16499, 16725, 16849, 16887, 17297, 17377, 17708, 17731, 18010, 18278, 18317. Audit first, then drafts under `pr-drafts/core-admin/` for the draftable tickets.

Branch names: `solr-<ticket>-submit`, except SOLR-11431 (`solr-11431-core-init-503`) and SOLR-18317 (`solr-18317-server-submit`, the banked server-side variant).

The cap is six subagents at once, across all assignments. The SolrCloud round has fully reported, so this round runs now.

## Heads checked live on 2026-10-10

`git ls-remote`, then a read-only fetch of all 33 refs.

| Ticket | Live head | Earlier or named head | Result |
|---|---|---|---|
| 4502 | `4491f5162c1d` | none (no gate) | audit only |
| 4989 | `5bac95376cd4` | `5bac95376cd4` | matches |
| 5011 | `f20ffe480781` | none (no gate) | audit only |
| 5262 | `ade8b80264ac` | none (no gate, no pipeline record) | audit only |
| 6438 | `8c77988d5917` | `8c77988d591` | matches |
| 8275 | `e52e10fa50a3` | `e52e10fa50a` | matches |
| 8554 | `32697b6c3f82` | none (no gate) | audit only |
| 8576 | `4c46f95c7851` | `4c46f95c785` | matches |
| 8628 | `ce8211e05e06` | none (no gate) | audit only |
| 9750 | `f97da6da14aa` | `f97da6da14a` | matches |
| 11431 | `968fad873c4b` | `968fad873c4` | matches; live PR, consistency only |
| 11939 | `d4cff5e76430` | none (no gate, docs only) | audit only |
| 12007 | `bdeba582fd63` | `bdeba582fd6` | matches |
| 12849 | `6b92223bc24f` | gated at an older head | live PR, consistency only; flag the older gated head |
| 12916 | `ebe5db374336` | none (no gate) | audit only |
| 13097 | `f0e7395f58f3` | gated at an older head | live PR, consistency only; flag the older gated head |
| 13246 | `6817c6c0c267` | `6817c6c0c26` | matches |
| 14098 | `dd4995515e88` | `dd4995515e8` | matches; ledger record, flag gaps |
| 15003 | `1004abee39ab` | `8f5b6f8360a` gated; live tip `1004abee39a` | the live tip has moved twice since the gate; flag |
| 15024 | `f95b5010b3fe` | `f95b5010b3f` | matches |
| 15805 | `3432f950f0ae` | `3432f950f0a` | matches |
| 16108 | `70ad7371b31c` | `70ad7371b31` | matches |
| 16499 | `6a2ff7618d9a` | none (no gate) | audit only |
| 16725 | `be1838ef8ccf` | `be1838ef8cc` | matches |
| 16849 | `d612b055da20` | `d612b055da2` | matches |
| 16887 | `42675f65d6fc` | `42675f65d6f` | matches; BATS branch |
| 17297 | `c0ab38fc0a8b` | `c0ab38fc0a8` | matches |
| 17377 | `22b5f209a11a` | `22b5f209a11` | matches |
| 17708 | `10a7fa07a79a` | `10a7fa07a79` | matches |
| 17731 | `f2b4ba164f56` | `f2b4ba164f5` | matches |
| 18010 | `c3685bb37d9d` | gated at `fadc5ee31e3`; live tip `c3685bb37d9` | the live tip has moved since the gate; flag |
| 18278 | `bf19c9fa4498` | `bf19c9fa449` | matches; retire candidate |
| 18317 | `ae918a03fa7b` | banked head | no gate at the banked head; audit the banked state only |

## Shared rules for every part

- Read only, except the report file and any draft named for your part. No commit, push, checkout, reset, merge, stash, or fetch of anything new. `git show`, `git log`, `git diff`, `git merge-tree`, `git rev-parse`, `git cat-file -e`, `git grep`, `git ls-tree`, and `git merge-base` are fine. Trial merges with `git merge-tree --write-tree` are fine if they write no ref.
- No builds, no Gradle, no tests, no test runs of any kind. No `gh` write calls.
- `gh pr view`, `gh pr list`, and `gh pr checks` are allowed for read-only consistency passes, through `C:\Users\shaninna\dev\Solr-issues\research\gh.ps1` (quote a JSON field list as one argument). Nothing that writes.
- Post nothing anywhere. No PR, no comment, no JIRA. No new Jira ticket unless someone asks.
- Proof numbers come only from the receipt named for the ticket. Gate logs are often not on disk; say so. Where a receipt says inconclusive by construction, a pin, or a proof that passes on base too, the draft says that, and does not claim a failing base run.
- Drafts follow `pr-formula.md`: the AI header as line 1, the Jira link line, a bold one-line summary in each section, "What happens today", "What this change does", "Proof", "A choice to check" (only with a live alternative and a pointed question), "Limits", the Changelog line with a link, and the AI assistance footer. Length guide about 3,500 characters. Each draft names the head it was written against in its Proof.
- Public text carries no internal process vocabulary (gate, receipt, ledger, handoff, top-up, takeover, audit, rc=0, fresh JUnit XML, pre-fix proof as a label, "premise run", internal log names). Plain words and short sentences. No em dash and no en dash in anything you write.
- Lucene version claims: main and branch_10x pin Lucene 10.4.0, and branch_9x pins 9.12.3. If one version is named in a draft, name every version that applies.
- A moved tip, or a diff that differs from the receipt, is flagged, not silently adopted.
- Every finding: file and line, evidence (SHA and line, receipt line, or command output), and exact replacement wording. Mark FIX or NOTE. If you cannot check something, say so under "Not checked". Do not guess.

## Parts

Six subagents. Reports go to `reports/core-admin-round-1-<part>.md`. Drafts go to `pr-drafts/core-admin/`.

**Part k1: LukeRequestHandler and QuerySenderListener (SOLR-4989, 15024, 13246, 12916).**
- 4989: gated green, `LukeRequestHandlerTest` 10 of 10. PR-ready. Limits lines named in its report.
- 15024: gated (hardened) at `f95b5010b3f`, `LukeRequestHandlerTest` 9 of 9. The gate narrowed the branch to `charFilters`; the wider filters question is raised for maintainers in the planned PR text.
- 13246: gated, focused class 4 of 4. PR-ready; Limits lines.
- 12916: no gate, audit only. Listeners added through the config API fail to parse queries. No draft.
- Check the `LukeRequestHandler.java` hunks of 4989 and 15024 for overlap, and state the landing order. Note the `QuerySenderListener.java` overlap between 12916 and 13246 for the landing order, without auditing 13246 twice.
Draft each draftable one.

**Part k2: request path and security (SOLR-12849, 13097, 17708, 18010).**
- 12849 and 13097: live PRs, consistency only. Check each live PR's head against the branch. 12849 is gated at an older head; 13097 is gated at an older head and is closed out, awaiting re-review. Flag each. Do not draft replacement PR text.
- 17708: gated, `JaxRsSingleAuthorizationTest` 2 of 2 plus two integration classes. PR-ready. One review finding is a deliberate structural exception and belongs in the draft's Limits.
- 18010: gated (hardened) at `fadc5ee31e3`; the live tip `c3685bb37d9` has moved; no gate at the live tip is recorded. Audit against the gated head, flag the move, and read the settling run in the takeover record before drafting any claim about concurrent `security.json` edits. The takeover record is not on disk, so say so; if the claim cannot be settled, hold the draft.
- Check whether 17708's `HttpSolrCall` hunks overlap 12849's and 13097's. State the landing order among the three if the PRs move. Keep the three accounts consistent with each other on shared security files.
Draft 17708 if draftable.

**Part k3: core lifecycle and NodeConfig (SOLR-4502, 5011, 11431, 12007, 17297, 17377).**
- 4502: no gate; `TESTING.md` note on the branch. Audit only. No draft.
- 5011: no gate; premise unverified. Audit only. No draft.
- 11431: gated green, `TestCoreContainer` 25 tests, 3 skipped. Already a live PR; consistency only. No draft.
- 12007: gated green, four focused classes. PR-ready. One design question for the owner: the synchronous-cleanup route, against a background cleanup with ordering guards. Pose it as a Choice in the draft.
- 17297: gated green, `TestCoreContainer` 26 tests, 3 skipped. One inverse-direction review finding was confirmed by run and stays an owner decision (widen the fix, or ship as is). State it in the owner list and in the draft.
- 17377: gated green, 41 focused tests. The branch implements option 1 of a design decision recorded in its handoff. State which option shipped; do not re-open it.
- The `NodeConfig.java` hunks of 17297 and 17377 must be checked against each other, and 17377 also changes `SolrXmlConfig.java`. State a landing order for the lifecycle cluster, with 4502, 5011, 11431, 12007 and 15805 (part k4) noted.
Draft the draftable ones (12007, 17297, 17377).

**Part k4: gated singles, banked, and retire (SOLR-9750, 15805, 16108, 16849, 18278, 18317).**
- 9750: gated, 11 focused tests. Draft if draftable.
- 15805: gated (hardened), `CoreContainerProviderTest` 1 of 1. Draft if draftable.
- 16108: gated, `SplitHandlerTest` 5 of 5; the proof is inconclusive by construction, recorded as such. An owner call on this branch is open; state it in the owner list.
- 16849: gated, `SegmentsInfoRequestHandlerTest` 7 of 7. Test-only salvage: the ticket was already fixed on main by SOLR-18083, and the branch carries only the regression test. The draft's Proof states the pin honestly, and the report notes the close-as-fixed option.
- 18278: gated, `TestHttpSolrClientProvider` 2 of 2. A retire candidate; the retire call is the owner's. Its proof fails by construction (a behavior-preserving refactor; the tests pass on base too). State the retire question in the owner list. Draft only if the audit argues against retiring, and then with an honest Proof.
- 18317: no gate at the banked head; held. The combined branch was narrowed at a reviewer's request; the Admin UI half shipped and merged; this server-side half returns later only if a reviewer wants it. Audit the banked state only. No draft.
Draft the draftable ones.

**Part k5: no-gate and docs (SOLR-5262, 8554, 8628, 11939, 16499, 14098).**
- 5262: no gate and no pipeline record on the main side. Audit only; state what a premise run and first gate must show. No draft.
- 8554 and 8628: no gate, premise unverified. Audit only. No drafts.
- 11939: no gate; the branch is a reference-guide change under a `TESTING.md` note. A docs-only branch needs no test gate, so judge submission readiness as a review question, and say so. Draft only if readiness is clear.
- 16499: no gate; the only test evidence is a GitHub corroboration run at its head, which does not settle gate state. Audit only. No draft.
- 14098: reconciled green at `dd4995515e8` (unit test 5 of 5, SolrCloud end-to-end 2 of 2). The record is a ledger table row with no per-branch gate log. Flag, do not fill in, any gap that matters. Draft if draftable.
Draft the draftable ones.

**Part k6: gated singles and collections (SOLR-6438, 8275, 8576, 15003, 16725, 16887, 17731).**
- 6438: gated, `MergeIndexesTest` 4 of 4 plus two neighbor classes. Draft if draftable.
- 8275: gated, `TestPrepRecovery` 3 of 3. PR-ready; Limits lines in its report.
- 8576: gated, `CollectionsAPISolrJTest` 25 tests, 1 skipped, test only. Its new test is a pin (it passes on base too); the draft presents it as added coverage, not as a failing base run. This class is shared with the SolrCloud corroboration run for SOLR-16437 (audit only there). The drafts must not claim the class's coverage twice.
- 15003: gated green at the older head `8f5b6f8360a`; the live tip `1004abee39a` has moved twice since, and a gate at the live tip is main-side work still owed. Audit against the gated head and flag the move. Do not draft it as ready until the main side gates the tip. It is also filed under replication and backup; this round owns the ticket, and states the cross-file once.
- 16725: gated, `LocalFSCloudIncrementalBackupTest` 7 tests, 1 skipped. Draft if draftable.
- 16887: gated, a BATS branch; counts come from the BATS logs, not JUnit XML, and there is no GitHub corroboration by design. PR-ready. Draft if draftable, and say the counts come from the BATS run.
- 17731: gated, `V2ResourcePathOverlapTest` 2 of 2. PR-ready. An owner call on this branch is open on the main side; state it in the owner list.
Draft the draftable ones.

## Deliverables

1. `reports/core-admin-round-1-k1.md` through `-k6.md`. The lead writes `reports/core-admin-round-1.md`, with per-ticket verdicts, the interaction results, the live PR consistency results, and the owner decisions.
2. Drafts in `pr-drafts/core-admin/` for the draftable tickets, each naming its head.

Not in scope: opening PRs, posting comments, editing branches or live PR descriptions, builds, Gradle, and test runs.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: reports/core-admin-round-1.md. The work was performed by the original claimant; this mark is a record correction, not a new claim.
