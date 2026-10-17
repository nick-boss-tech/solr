# Draft fidelity review: Streaming expressions and SolrJ drafts

Assignment: `assignments/pool-draft-fidelity-streaming-solrj.md`. Claim: `claims/pool-draft-fidelity-streaming-solrj.md`. Lead: the windows review agent. Part reports, with the exact replacement text for each item:

- `reports/draft-fidelity-streaming-solrj-s1.md`: Streaming SOLR-10322, 12505, 12657, 14231; SolrJ SOLR-2018.
- `reports/draft-fidelity-streaming-solrj-s2.md`: SolrJ SOLR-3722, 3999, 4335, 4336, 4422.
- `reports/draft-fidelity-streaming-solrj-s3.md`: SolrJ SOLR-4424, 5220, 6046, 7709, 8536.
- `reports/draft-fidelity-streaming-solrj-s4.md`: SolrJ SOLR-12094, 14298, 14967, 17866, 18341.

No draft was edited under this assignment. Read-only throughout. No build, test, gate or test-queue run. No PR, comment, review, submit-branch, PR-description or Jira write. The live-PR consistency drafts (SOLR-13524, 10198, 15823, 18129) are out of scope, as the assignment says.

## Verdicts

Twenty drafts. Thirteen are DRIFT and seven are CONSISTENT. Every draft's named head matched the fork's live tip in `ls-remote` at the time of the check, and every cited SHA resolved.

| Draft | Head checked | Verdict | Part report |
|---|---|---|---|
| SOLR-10322 | `80ce9d7a3c8a` | DRIFT (1 item) | s1 |
| SOLR-12505 | `5f20e1171bc8` | DRIFT (2 items) | s1 |
| SOLR-12657 | `16a0a69e8398` | DRIFT (1 item), HELD for an owner ruling | s1 |
| SOLR-14231 | `2f73d00a3996` | DRIFT (1 item) | s1 |
| SOLR-2018 | `213457aa91fd` | CONSISTENT | s1 |
| SOLR-3722 | `f9d3dda1d3de` | CONSISTENT | s2 |
| SOLR-3999 | `e3af6f36e312` | DRIFT (1 item) | s2 |
| SOLR-4335 | `8f6e267d6482` | DRIFT (1 item) | s2 |
| SOLR-4336 | `798618aa08fd` | DRIFT (2 items) | s2 |
| SOLR-4422 | `3e5b7afecddb` | CONSISTENT | s2 |
| SOLR-4424 | `2e947b7f622c` | DRIFT (1 item) | s3 |
| SOLR-5220 | `e415c409044c` | DRIFT (1 item) | s3 |
| SOLR-6046 | `3b833e369a0b` | DRIFT (1 item, branch changelog only) | s3 |
| SOLR-7709 | `501078f22203` | CONSISTENT | s3 |
| SOLR-8536 | `28552ddc26eb` | DRIFT (2 items) | s3 |
| SOLR-12094 | `8d957a73f4fb` | CONSISTENT | s4 |
| SOLR-14298 | `67e75ea1eea4` | DRIFT (1 item) | s4 |
| SOLR-14967 | `ee76643f5b3f` | CONSISTENT | s4 |
| SOLR-17866 | `3f9367d796a6` | CONSISTENT | s4 |
| SOLR-18341 | `458b098719d7` | DRIFT (6 items) | s4 |

## Items by draft

Each line summarizes one item. The exact replacement text is in the named part report.

- **SOLR-10322 (s1).** "Same commit and seed" is not supported by the receipt, because the premise log records no commit. "Seed" is internal vocabulary in public text.
- **SOLR-12505 (s1).** The Limits say "Not covered: other default parsers", which contradicts the `defType=lucene` change. The reporter is misattributed: the assignee reported the fix, and the reporter is named in the ticket.
- **SOLR-12657 (s1). Held.** The `MinMetric` and `MaxMetric` citations point at `getValue()` (lines 71-74 and 65-68), not at the min and max logic (lines 105-112 and 104-111). The round report holds this draft for owner decision 1 (`reports/streaming-expressions-round-1.md` line 20). The draft matches option (a), which ships with the Limits paragraph. The round report recommends option (b). Do not open before the owner rules.
- **SOLR-14231 (s1).** The Proof has no verification date. The receipt gives only "2026-10-04 era", so the lead must confirm the date. Open note: the changelog's "no longer fails" is not backed by any ticket text or base run.
- **SOLR-2018 (s1).** CONSISTENT. The receipt wrongly says "UpdateRequest"; this is a main-side receipt correction.
- **SOLR-3999 (s2).** The changelog title overstates the change ("so Java-serialized documents survive recompiles"). Branch-side title fix; the draft body needs no edit.
- **SOLR-4335 (s2).** The changelog title is narrower than the draft, which widens the change to the XML response and delete writers. Branch-side title fix.
- **SOLR-4336 (s2).** The Proof has no verification date. The changelog title says "NumberFormatException" and "(default applies)", which need checking against the code. Branch-side title fix.
- **SOLR-4424 (s3).** The Limits list of `toSolrParams` callers names three paths. Twenty-nine source files call it.
- **SOLR-5220 (s3).** "The run used the same source content as this head" rests on the gated tree `7650584a756`, which is not in the object store, so it cannot be verified here.
- **SOLR-6046 (s3).** The draft text is consistent. The branch changelog title says "array values", but the change covers `Object[]` only. Branch-side title fix.
- **SOLR-8536 (s3).** The draft says the string is used "without any change" at line 349, which doubles the `/`. The branch changelog title overstates the change, which covers ASCII control characters only.
- **SOLR-14298 (s4).** The Limits figures (14.5 to 19 seconds, 1.2 billion) are not in the receipt. They match the Jira snapshot, so the replacement drops them.
- **SOLR-18341 (s4).** Six items. The `LBSolrClient` citation should be lines 675-680, not 574-576. The `UpdateRequest` citation should be lines 412-505, not 412-427. "Tracked as SOLR-9355" has no source. "No dedicated test" is false, because `HttpJettySolrClientTest` exists. "About 60 seconds" is a receipt gap; the code agrees with it. The Limits use the words "fail-on-base claim", which is process vocabulary. Branch-side: the changelog title does not name the load-balancer change, and the branch still carries the SOLR-18368 hunk at `CloudSolrClient.java` line 1550.

Branch-side notes that are not draft drift: SOLR-17866's changelog title says `setRequiresCollection(false)`, but the code opts out on any explicit call. SOLR-14967's "first version" run has no branch commit.

## Open for the owner or the main side

1. **SOLR-12657.** Owner decision 1 of the streaming round 1 report. The draft stays closed until the owner rules.
2. **SOLR-14231 and SOLR-4336, dates.** Both need a verification date that the receipt supports. The lead must confirm each.
3. **SOLR-4335 and SOLR-4336, undated results.** Their results come from the undated Test Categories round. Confirm the date before any Proof sentence is posted.
4. **SOLR-2018, receipt.** The receipt says "UpdateRequest" where the code says otherwise. Correct the receipt on the main side.
5. **SOLR-5220, gated tree.** The gated tree `7650584a756` is not in the object store, so the "same content" sentence is unverifiable here.
6. **SOLR-18341, branch contents.** The branch still carries a SOLR-18368 hunk at `CloudSolrClient.java` line 1550. The main side must decide whether it belongs in this branch.
7. **Branch changelog titles.** SOLR-3999, 4335, 4336, 6046, 8536, 17866 and 18341 have changelog titles that overstate, understate or miss their change. The titles are branch-side fixes. The draft bodies need no edit for them.
8. **Jira text.** Some Limits figures and dates were read from the `research/jira-context` snapshots only. The snapshot dates were not checked (s4).

## Not done

- No build, test, gate or test-queue run. Gate and premise logs named in the receipts are not on disk, so Proof counts were checked against the receipts only.
- Round 1 part reports were searched for the ticket numbers, not read in full. SolrJ round 1 parts s2, s5 and s6 were checked by grep only (slice 3), and parts s4 to s6 were not read in full (slice 2). Live Jira text was not re-read; the Limits and dates were checked against the `research/jira-context` snapshots (slice 4).
- The claim commit `e84522fa5bc` was not checked against the drafts. The heads were checked against the live tips at the time of the check.
