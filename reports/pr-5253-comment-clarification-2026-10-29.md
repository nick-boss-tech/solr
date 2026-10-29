# SOLR-18601 / PR 5253: clarify the removed discard endpoint and tests

Status: existing public comment edited and read back; awaiting contributor or maintainer response.

- Owner direction: "I'm not sure if that was clear in the comment, maybe you can add some of these details?" This authorizes the clarification of the already-posted explanation.
- Existing comment: https://github.com/apache/solr/pull/5253#issuecomment-6489721489
- Edit time from GitHub (UTC): 2026-10-29T07:00:03Z
- Public PR refresh before editing: unchanged original head `32b91612edde057c19bf1a7963e8276b816551df`, no reviews, only the existing offer comment. Offered and tested head remains `21804641d0ebbb5199a1c205f031af47c9424290`; this edit changes no code or test claims.
- Fresh comment GET revealed the appended line `changelog.yml was accidentally included in the diff.` The edit was prepared from that live body and preserves the line. The pre-edit snapshot is retained locally as `PR-5253-comment-before-clarification-2026-10-29.json`.

The clarification explicitly names `REQUESTDROPBUFFEREDUPDATES`, its enum entry and CoreAdminOperation implementation. It explains that the original PR introduced the action for discarding, while repaired cleanup uses existing `REQUESTAPPLYUPDATES` to preserve acknowledged writes from earlier migrations. The new discard endpoint has no remaining caller in the patch. It identifies the two removed CoreAdminOperationTest cases as the endpoint's missing-parameter 400 and unexpected-error 500 checks. These additions did not exist at upstream base `e59ce3c97d`; the three files return to that baseline. Replacement regression coverage and the partial-target-data limit are retained.

Edited the existing comment through REST PATCH with the clarified body file; no duplicate comment was added. A separate GET matched the clarified draft after line-ending and final-newline normalization, including the disclosure, collapsed verification, pinned diff, fetch / fast-forward commands and appended changelog note. Snapshot retained locally as `PR-5253-posted-comment-clarified-2026-10-29.json`.

No source change, branch push, PR body edit, review submission, JIRA write, approval or merge occurred. No Windows runtime validation was run. The original publication record `reports/pr-5253-public-comment-2026-10-29.md` remains unchanged.
