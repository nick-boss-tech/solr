# SOLR-18010: review of the narrow PR body draft (vm3, 2026-10-22)

Draft: `pr-drafts/solr-18010/PR-5118-body-narrow-v2.md`. Not applied. Comments and replies need Nick's approval; body edits do not.

## Ticket reread (SOLR-18010, fetched 2026-10-22 from the JIRA REST API)

- Reported symptom: role creation in the Admin UI sends parallel requests; security.json ends up with leftover text and is not valid JSON; the solr user stops working.
- Reporter's example file shows `"":{"v":0}` in both sections.
- Amrit Sarkar (2026-10-09) reproduced it on main in standalone mode. The fix is #5065, merged 2026-10-21 (`1afeee27c4`).
- Amrit Sarkar (2026-10-13): the v2 Users, Roles and Permissions endpoints in #4916 call `editSecurityConfig` and skip `handleRequestBody`, where the lock sits. Whichever of #4916 and #5065 merges second must move the lock into `editSecurityConfig`. #4916 is open, and `editSecurityConfig` is not on main.
- Gap found: the narrow PR does not change this. The draft now names it in Limits.

## Points from the review

| Point | Response in v2 |
| --- | --- |
| Did we reread the ticket? | Yes (above). The draft now opens with the ticket's reported path and says #5065 covers it. It names the #4916 gap. |
| Links point to merge base; link to Solr | Main citations now link to `apache/solr` at `7892348272` (current main). The line numbers were checked at that SHA. Our own change still links to the fork commit `e81e5e28e9`, because no Apache commit exists for it. Once the change is pushed to the PR branch, the link should point to that PR head. |
| Drop the "at head" wording | Removed. "This change" and the fork commit link replace it. "Merge base" removed. |
| "Already serializes ... reads included???" | Rewritten: "#5065, now on main, runs every request to the standalone security handler one at a time, GET included." The code path is `handleRequestBody`, which handles GET and POST. |
| The atomic-move "choice" is unclear | The atomic-move choice is gone with the narrow change. The choice is now the empty-string entry: keep it or remove it, with a short statement of what each means for maintainers. |
| Proof is too verbose | Cut to two bullets: the two tests that fail on main, and the gate result at `e81e5e28e9`. The detail is in the receipt. |
| Learn from the older PR | The v1 body had a list of behavior changes, a second choice, and a long limits list. v2 keeps one change, one choice, and limits that each name a case we could not check. |

## Still open

- Title: not in the draft. Suggested: "SOLR-18010: Standalone security.json omits its internal version marker and is opened only after its content is ready".
- The crash-startup check (what Solr does with a truncated file) is not run. Limit stays until it is.
- The #4916 lock question needs a decision by whoever owns #4916 or #5065. Not ours to settle here.
