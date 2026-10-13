# Security convergence, round 2: category response

Verdict: 8 of the 10 round 1 security findings on the three flagged tickets are CONVERGED. Two are HOLD: SEC-R1-02 (SOLR-10627, owed landing and changelog parse) and SEC-R1-07 (SOLR-11678, owed keystore-type reading on vm2). SOLR-12161 is CONVERGED. SOLR-10627 and SOLR-11678 stay HOLD until their open item settles. SOLR-13705 and SOLR-18368 were CONVERGED in round 1 and are not re-audited.

Starting records: reports/security-convergence-round-1-response.md and the receipts for SOLR-10627, SOLR-11678, SOLR-12161, SOLR-13705 and SOLR-18368.

## Remainders, one line each

| Item | Ticket | Round 1 state | Final state | Rung that settled it | Artifacts |
|---|---|---|---|---|---|
| SEC-R1-01 | SOLR-10627 | should-fix, applied | CONVERGED | Round 1 wording; confirmed present (Limits opener, draft line 49) | none |
| SEC-R1-02 | SOLR-10627 | note, branch-side title owed | HOLD: landing on solr-10627-submit, then changelog parse at the landed head | Patch (code); landing and parse owed | gates/SOLR-10627-sec-r1-02-title.patch, gates/SOLR-10627-sec-r1-02-title.md |
| SEC-R1-03 | SOLR-11678 | blocking, held on push | CONVERGED | Later record: packaged head 2c68a83c pushed (origin/solr-11678-submit; receipts/SOLR-11678.md; claim DONE); wording | Draft edit: head substitution |
| SEC-R1-04 | SOLR-11678 | blocking, held with 03 | CONVERGED | Later gate at 2c68a83c (EnvSSLCredentialProviderTest and SysPropSSLCredentialProviderTest green); round 1 sentence is already in the draft, checked verbatim | none |
| SEC-R1-05 | SOLR-11678 | blocking, branch side | CONVERGED | Later gate: branch commit at 2c68a83c (EnvToSyspropMappings +1, EnvUtilsTest +6), gate green; wording | Draft edit: sentence added to What this change does |
| SEC-R1-06 | SOLR-11678 | should-fix, applied | CONVERGED | Round 1 wording; confirmed present (line 9) | none |
| SEC-R1-07 | SOLR-11678 | should-fix, not applied | HOLD: keystore type of the standalone run is in no record on this host | Get more information: reading job, owed on vm2 | gates/SOLR-11678-sec-r1-07-keystore-reading.md |
| SEC-R1-08 | SOLR-11678 | note | CONVERGED: gate date 2026-10-13 from receipt (GATE START 04:07:58Z, GATE END 04:30:36Z); owner decision 8 Limits line added | Later receipt; wording | Draft edits: Proof dates, Limits bullet |
| SEC-R1-09 | SOLR-12161 | blocking, held on push | CONVERGED | Later record: packaged head cdd265168a2a pushed (origin/solr-12161-submit; its diff from 1725cbd removes SOLR-12161-TESTING.md only); gate green at cdd265; wording | Draft edits: link and Proof head |
| SEC-R1-10 | SOLR-12161 | should-fix, applied | CONVERGED | Round 1 wording; confirmed present (line 7) | none |

Ticket states: SOLR-10627 HOLD (SEC-R1-02). SOLR-11678 HOLD (SEC-R1-07; all other items CONVERGED). SOLR-12161 CONVERGED.

## Draft edits (before and after)

pr-drafts/security/SOLR-11678.md
- Head hash: all nine occurrences of `611eae2464536fdae61b8cae32abac97ee3a453b` (SSLConfigurations, jetty-ssl.xml, bin/solr, solr.in.sh, solr.in.cmd, the SSLConfigurationsTest link, the Proof head and the changelog link) became `2c68a83c64ee8d4e8bece83392ad4cf28eb5b55b`. Zero occurrences of 611eae remain. The merge-base links (`cabedd1d968`) are unchanged.
- Line 15, after the jetty-ssl.xml link sentence, added: "The variable maps to nothing in `EnvToSyspropMappings.properties`, as the other SSL password variables do, so it is not copied to a system property."
- Proof, before (one bullet): "- Focused tests at head `611eae...`: `SSLConfigurationsTest` 20 tests run ... `EnvSSLCredentialProviderTest` 2 tests, 0 failures. `SysPropSSLCredentialProviderTest` 2 tests, 0 failures."
  After: one bullet per class, each with head `2c68a83c...` and "verified 2026-10-13": `EnvUtilsTest` 13 run (new mapping assertion); `SSLConfigurationsTest` 20 run; `EnvSSLCredentialProviderTest` 2 run; `SysPropSSLCredentialProviderTest` 2 run; plus the solrj module check.
- Proof, before: "- The core module check also passes at this head." After: removed. The receipt records a solrj module check at 2c68a83c, not a core check. The solrj check line replaces it.
- Proof, base leg, before: "- Base leg: with the production files reverted to the merge-base `cabedd1d968` and the three branch test files kept, the core test sources do not compile (10 errors). ... inconclusive by construction ..."
  After: "- Base leg: production files reverted to the merge-base `cabedd1d968`, new `EnvUtilsTest` assertion kept: 13 tests run, 1 failure, `sslKeyManagerPasswordMapsToNothing` (`expected:<> but was:<null>`), because the key is absent from the mappings file at base. The same assertion passes at the head. The three SSL test classes ... show no before and after result."
- Limits, new bullet after the "Server side only" bullet: "- **Custom `class://` credential providers receive the new credential type.** They load at every server start and get the new type unchanged. None of them has key manager support, and this change does not alter them." This documents owner decision 8 (the decision allows accept or document).

pr-drafts/security/SOLR-12161.md
- Line 15 link: `1725cbd8489889a1dad75279e4b2c11b0ec93d66` became `cdd265168a2adc76fb788f3d1f5587a3eee05e42`. The #L248-L260 anchor is unchanged, because the packaged commit removes only SOLR-12161-TESTING.md.
- Line 21: "at head `1725cbd84898` (checked 2026-10-11)" became "at head `cdd265168a2a` (checked 2026-10-11)".

pr-drafts/security/SOLR-10627.md: no edit this round. The round 1 draft carries no title text. Its head links move after the landing (see the SOLR-10627 job file).

All edited lines were re-read. No em dashes and no first-person plural in the edited text.

## Artifacts

- gates/SOLR-10627-sec-r1-02-title.patch: replaces the title in changelog/unreleased/SOLR-10627-reject-null-collection-permission.yml at 5a15dc0ba22. Dry-run clean with `patch -p1` in a scratch directory.
- gates/SOLR-10627-sec-r1-02-title.md: owed landing and changelog YAML parse job.
- gates/SOLR-11678-sec-r1-07-keystore-reading.md: owed vm2 reading job for the keystore type.
- reports/security-convergence-round-2-response.md: this report.
- Scratchpad: security-round2-notes.md (working notes).

## Holds (named)

1. SEC-R1-02, SOLR-10627. Owed landing: the branch owner applies the patch to solr-10627-submit as a plain fast-forward from 5a15dc0ba22, then the changelog YAML parse runs at the landed head. Reason: the fork branch is outside this review lane, and the gated fragment still has the old title. Land from origin/solr-10627-submit, not the older local refs/heads/solr-10627-submit.
2. SEC-R1-07, SOLR-11678. Owed reading on vm2: the keystore type of the standalone premise run. Reason: no record on this host names the type, and the vm2 logs are not on disk here. If the run used PKCS12, the premise as recorded does not hold, and the ticket goes back to the owner.

## Outside the round 1 remainder list (not patched)

- Title accuracy, SOLR-11678 fragment at 2c68a83c: "so HTTPS works when the key password differs from the keystore password" states more than the record shows, because the jetty-ssl.xml path was not run in a live server (draft Limits). This is the same class as SEC-R1-02. It is recorded here as the SEC-R2-01 note from the earlier pass. Owner title call. Any change moves the gated head and owes the same landing and parse as SEC-R1-02.
- reports/security-convergence-round-2.md is an earlier pass. Its states for SEC-R1-03, 04, 05, 08 and 09 predate the 2c68a83c gate and the cdd265 pin. This report supersedes it.
- Owner-decision numbers: the round 1 citations (decision 2, mapping; decision 8, custom providers) match the SOLR-11678 part's own list in reports/security-round-1-s3.md. The list in reports/security-round-1.md numbers the mapping as decision 4. No text change is needed.

## Checks and limits

- Not done: no build, test, Gradle or gate run; no commit, push, checkout, reset, stash or rebase; no GitHub or JIRA write; no live PR body edit; no claim edit; build-docs, CLI, streaming and SolrJ drafts not touched.
- Reads only: git show, git cat-file, git diff --stat, git for-each-ref, and git diff --no-index into scratchpad to build the patch.
- Pushed heads come from the local remote-tracking refs (last fetch), not a fresh ls-remote. Gate counts and dates come from the receipts, job files and claim DONE lines. The vm2 logs are not on this host.
- pr-drafts/security/SOLR-11678.md and SOLR-12161.md differ from HEAD in the worktree. Round 1 and this round's edits are uncommitted.
