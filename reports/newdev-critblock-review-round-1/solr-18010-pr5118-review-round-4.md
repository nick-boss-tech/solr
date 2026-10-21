# SOLR-18010, PR #5118: review round 4, maintainer pass (vm3, 2026-10-21)

Head reviewed: `1b14ce9556` (round 3). Head after this round: unchanged. The round changed the PR body and title only; no code change was needed.

PR body and title applied to #5118 on 2026-10-21 and read back: title "SOLR-18010: Standalone security.json is replaced through a temporary file where that is safe, and no longer stores its version marker"; body in `pr-drafts/solr-18010/PR-5118-body-round-4.md`.

## Findings in the round-3 body

1. **Wrong claim: "Reads do not wait."** `SecurityConfHandlerLocal.handleRequestBody` (main, `4422d26ac7`, lines 52 to 56) takes the lock for every request, GET included. Fixed in the body.
2. **Symptom links pointed at a commit before #5065** (`14c7aac0d15`). The merge base of the branch is now `4422d26ac7`. Security.js, `SecurityConfHandler` (L124, L147 to L148, L151), `SecurityConfHandlerLocal` (L52 to L56, L99 to L100) and `CoreContainer.readVersion` (L611 to L617) are re-anchored to it. Head links use `1b14ce9556`.
3. **The body implied the lost-update case was still open.** On main, #5065 serializes edits within one process. The body now says what remains: the in-place truncate and the stored marker.
4. **A behavior was listed as a choice.** Keeping the owner and permission bits is what the code does and what a maintainer would expect; it moved from Choices to What this change does. The real maintainer choice is whether to keep the atomic move at all (crash safety versus file identity; ACLs and xattrs are not carried over; symlinks fall back).
5. **Title said "serialized".** That comes from #5065, not this PR. Title changed.
6. **Proof had no run against main.** Run on 2026-10-21 with this PR's tests and the current upstream main file (`7892348272`, same `SecurityConfHandlerLocal.java` as `4422d26ac7`):
   - `SecurityConfHandlerTest` 5 tests, 1 failure: `testConcurrentPersistConfLeavesOneWholeDocument` ("not a single JSON document"). Passes: `testEdit`, `testConcurrentEditsToLocalSecurityJson`, `testEditKeepsExistingSecurityJsonPermissions`, `testEditInPlaceWhenSolrHomeIsReadOnly`.
   - `BasicAuthStandaloneTest` 2 tests, 1 failure: `testBasicAuth` ("authentication config should not contain internal metadata"). Passes: `testConcurrentEdits`.
   - So the atomic-write test and the marker test are the two fail-before proofs on main. The lock test and the permission and read-only tests pin behavior; the body says so.
   - Logs: `gates/logs/vm3/r4-18010-main-with-head-tests/`.
7. **Length.** 6,026 visible characters down to 4,104 (raw 6,353 to 5,324). Trimmed without dropping a claim, SHA, test name or link. The remaining excess is citations and test names. Not trimmed further: each one carries a claim a reviewer checks.

## Gate and runs this round

- No code change, so no new gate on `1b14ce9556`. The round 3 gate stands (`gates/logs/vm3/solr-18010-r3-r3-head/`).
- Main proof above. Symlink and read-only results are the round 3 runs (receipts/SOLR-18010.md).

## Open for maintainers (in the body)

- Keep the atomic move, or write in place and strip only the marker.
- Allow `LinkPermission "symbolic"` in the repo test policy, or leave the link case untested.

## Not run

- Windows, owner change, single-file bind mount (unchanged from round 3).

## Upstream change seen this round

`7892348272` (`Cap concurrent open PRs from non-committers at 7 (#5163)`, Jan Høydahl, 2026-10-21): contributors without write access may have at most 7 open PRs, drafts included, and GitHub refuses new ones past that limit. Affects any new PR openings from this account (the nested 2681 follow-up needs one). Existing PRs are not affected by the text.
