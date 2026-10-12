# Core admin convergence, round 2

Verdict: **NOT CONVERGED.** Eleven of the eighteen re-checked drafts converge. Seven do not: SOLR-4502, 5011, 8576, 9750, 12916, 17708 and 17731. Each of the seven has one open should-fix item, and most of them are branch-side or gate-side items the draft cannot close on its own.

Counts: drafts re-checked 18. Round 1 findings carried or confirmed on the live text: all dispositions in the response were checked against the live draft text, and none of them was disputed as recorded. Two applied texts carry defects that the replacement text itself introduced (CA-R2-01 and CA-R2-02). Two verdicts in the response are disputed (SOLR-17708 and SOLR-17731 listed as CONVERGED), and one is disputed (SOLR-12916). New findings: 4 should-fix (CA-R2-01, 02, 41, 42), 2 note (CA-R2-03, 04). Still open should-fix: 7 (CA-R1-01, CA-R1-24, CA-R1-34, CA-R2-01, CA-R2-02, CA-R2-41, CA-R2-42).

Scope: `assignments/pool-core-admin-convergence-round-2.md`, checked against `reports/core-admin-convergence-round-1-response.md`. Three read-only subagents covered six drafts each. Every live head was checked with `git ls-remote origin refs/heads/<branch>`, and no draft is on hold for a head mismatch. Settled decisions were not reopened. The drafts were not edited in this round.

## Verdict per draft

- **SOLR-4502: NOT CONVERGED.** CA-R1-01 (should-fix, branch-side) still open: the changelog fragment at `0ee4c644ee63` still has the old title. CA-R1-02 confirmed. CA-R2-03 (note): the head moves when the fragment lands. Should CA-R1-01 land, the draft converges, subject to CA-R2-03.
- **SOLR-5011: NOT CONVERGED.** CA-R2-02 (should-fix): the Limits line and bullet 1 describe one unsafe case, but the record names at least two. CA-R1-04, 05, 06 (applied, but see CA-R2-02), 07 confirmed. The hold itself is not counted.
- **SOLR-6438: CONVERGED.** CA-R1-09 confirmed.
- **SOLR-8576: NOT CONVERGED.** CA-R1-24 (should-fix) still open: the alias fix has not landed, the `[OWED BEFORE POSTING]` marker stands, and `solr-8576-submit` is unchanged at `4c46f95c7851`. CA-R1-21, 22, 23, 25, 27 confirmed.
- **SOLR-9750: NOT CONVERGED.** CA-R2-01 (should-fix): line 9 says `/admin/info` "linked at the merge-base" but no such link exists. CA-R1-26 and 27 confirmed; CA-R1-28 standing.
- **SOLR-12007: CONVERGED.** CA-R1-31 confirmed. CA-R1-32, 33 standing. CA-R2-04 (note): the inline overload runs to line 3528, not 3525.
- **SOLR-12916: NOT CONVERGED.** CA-R1-34 (should-fix, branch-side) still open: the fragment commit `1a4eb16c567` has landed, but its title names only the Config API. CA-R1-35 and 36 confirmed. The response lists this draft as CONVERGED; that verdict is disputed.
- **SOLR-13246: CONVERGED.** CA-R1-41 to 43 confirmed or standing.
- **SOLR-15024: CONVERGED.** CA-R1-44 to 47 confirmed or standing.
- **SOLR-15805: CONVERGED on text.** CA-R1-48, 49 confirmed or standing. The co-author trailer in the branch history blocks opening the branch; it is a main-side decision, not a draft finding.
- **SOLR-16499: CONVERGED.** CA-R1-50, 53 confirmed or standing.
- **SOLR-16725: CONVERGED.** CA-R1-51 confirmed; CA-R1-52 standing. The owed branch edits (a raw-request test comment and a ClusterStatus javadoc) have not landed and would move the head.
- **SOLR-16849: CONVERGED.** CA-R1-61 to 67 confirmed or standing. Base links verified: `b2e1f8ed4a30` is the parent of `f2aaf8769fd`.
- **SOLR-17297: CONVERGED.** CA-R1-63, 68, 69 confirmed or standing. CA-R1-69's observed base line is still owed on the main side.
- **SOLR-17377: CONVERGED.** CA-R1-64, 65, 70, 71 confirmed or standing.
- **SOLR-17708: NOT CONVERGED.** CA-R2-42 (should-fix): the scope sentence says "a v2 path that matches no JAX-RS resource no longer goes through the first check", but the code skips the first check only for requests with no v2 API object, so the sentence covers non-JAX-RS APIs too. CA-R1-81 to 84 confirmed; the head is `2eceb547`, the gated tip. The response lists this draft as CONVERGED; that verdict is disputed.
- **SOLR-17731: NOT CONVERGED.** CA-R2-41 (should-fix): the Proof gives a run date (2026-10-07) that the receipt does not record; the receipt cites only a takeover-log entry. CA-R1-85, 86, 88 confirmed. CA-R1-87 is owed: `gates/SOLR-17731.md` has not landed, and the branch still carries the test-file and changelog items the gate job names. The response lists this draft as CONVERGED; that verdict is disputed.
- **SOLR-18010: CONVERGED.** CA-R1-90, 92, 93 confirmed or standing. CA-R1-91 (branch title) is still open, but it is a branch-side note and not a should-fix, so it does not hold the draft.

## Register

Carried findings keep their round 1 IDs. Replacement text is given for each new should-fix finding.

**CA-R2-01 | SOLR-9750 | should-fix.** Line 9: "A few do not, such as `/admin/info`, which uses `_ADMIN_CORE` (linked at the merge-base)." No link exists on `/admin/info` in the draft. Replacement: "A few do not, such as `/admin/info`, which uses [`_ADMIN_CORE`](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/resources/ImplicitPlugins.json#L78)."

**CA-R2-02 | SOLR-5011 | should-fix.** The bold Limits line (32) says "one shared-schema case is unsafe", and bullet 1 (line 34) describes only the unload case. The analysis (`reports/solr-5011-shared-schema-analysis.md` lines 58, 75 and 99) and `gates/SOLR-5011-narrowing.md` line 10 also name the reload case. Replacement for line 32: "**Not every close path is tested, and the shared-schema cases that reach a closed loader are unsafe by a read of the code and have no test.**" Append to bullet 1, after "No test in this change runs it.": " The same happens when the core that built the cached schema is reloaded with its schema file unchanged: the reloaded core takes the cached schema, whose loader the old core then closes. No test in this change runs that case either."

**CA-R2-41 | SOLR-17731 | should-fix.** Line 22: "- `V2ResourcePathOverlapTest` 2 of 2 at head `f2b4ba164f56` (verified 2026-10-07)." The receipt records no run date; its only date is a takeover-log entry (round 35, 2026-10-07). This is the defect CA-R1-65 graded for SOLR-17377. Replacement: "- `V2ResourcePathOverlapTest` 2 of 2 at head `f2b4ba164f56`. Recorded 2026-10-07 at this head."

**CA-R2-42 | SOLR-17708 | should-fix.** Line 17: "Behavior change: a v2 path that matches no JAX-RS resource no longer goes through the first check. Jersey answers it with its not-found response, and the authorization plugin is not consulted for it. Non-JAX-RS v2 APIs and v1 requests are unchanged." At `2eceb547`, `V2HttpCall.java` lines 228 to 232 skip the first check only when `api == null` and the action is admin or process. A non-JAX-RS v2 API has an `api` object and still runs the check, so the sentence contradicts the paragraph's last sentence. The settled behavior is not reopened; only the scope words change. Replacement: "Behavior change: a v2 request that has no v2 API object and is an admin or core request no longer goes through the first check. If no JAX-RS resource matches it either, Jersey answers it with its not-found response, and the authorization plugin is not consulted for it. Non-JAX-RS v2 APIs and v1 requests are unchanged."

**CA-R2-03 | SOLR-4502 | note.** When the CA-R1-01 fragment commit lands, `0ee4c644ee63` stops being the head. The draft's references at lines 15, 21, 33 and 37 must move to the new head. The receipt covers only `0ee4c644ee63`, so a title change needs a changelog parse and a top-up gate at the new head. The response records the head move for CA-R1-34 but not for this item.

**CA-R2-04 | SOLR-12007 | note.** Line 16: "The overload that runs the cleanup inline is at lines 3501 to 3525." At `bdeba582fd63` the overload runs 3501 to 3528. Optional: "3501 to 3528".

## Carried items still open

- **CA-R1-01 (SOLR-4502, branch-side):** the fragment `changelog/unreleased/SOLR-4502-create-before-load.yml` still has the old title. Not landed.
- **CA-R1-24 (SOLR-8576):** the alias fix is not landed; `gates/SOLR-8576-alias-fix.md` is OPEN and unclaimed; the marker stands.
- **CA-R1-34 (SOLR-12916, branch-side):** the title commit has not landed.
- **CA-R1-87 (SOLR-17731, owed):** `gates/SOLR-17731.md` has not landed.
- **CA-R1-91 (SOLR-18010, branch-side note):** the branch title still reads "can no longer corrupt the file or overwrite each other".

## Record notes for the main side

- The response lists SOLR-12916, SOLR-17708 and SOLR-17731 as CONVERGED while a should-fix item is open against each. The convergence rule does not allow that.
- The answers' summary still says "No gate recorded" for SOLR-12916 and SOLR-16499. The receipts record green gates at the live tips.
- `material/core-admin-round-1-answers.md` still says NO GATE for SOLR-4502 and SOLR-5011. The receipts govern and show green gates.
- The owed branch edits for SOLR-16725 (a raw-request test comment and a ClusterStatus javadoc) would move the head and need a top-up run.

## Not checked

- Ticket text for drafts that quote a Jira packet, beyond the packets read as data under `research/jira-context/`.
- The master receipts ledger, the takeover log and security material.

## Convergence line

NOT CONVERGED. Seven drafts hold open should-fix items: SOLR-4502, 5011, 8576, 9750, 12916, 17708 and 17731.
