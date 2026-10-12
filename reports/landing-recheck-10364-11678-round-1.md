# Landing re-check for SOLR-10364 and SOLR-11678, round 1

Verdict. SOLR-10364 is CONVERGED AT THE LIVE HEAD, with no patches. SOLR-11678 is NOT CONVERGED: two round 1 findings are patched, and SEC-R1-05 stays open on the branch side, so the draft remains on hold.

Inputs: `assignments/pool-landing-recheck-10364-11678-round-1.md`, claim `claims/pool-landing-recheck-10364-11678-round-1.md`. Two read-only reviewers did the draft checks. The review agent fetched both packaged heads read-only, to check every citation in the object store rather than at the pre-push tips.

Live tips, confirmed with `git ls-remote`: `solr-10364-submit` at `efb1e6717f9ffda64b52c0820efb52ba58832b50`; `solr-11678-submit` at `611eae2464536fdae61b8cae32abac97ee3a453b`.

## SOLR-10364 (`pr-drafts/solrj/SOLR-10364.md`): CONVERGED AT THE LIVE HEAD

- SJ-R1-45 is confirmed. The packaged head is the live tip. Its object is present, and the three files it touches exist. The diff from the pre-push tip `502bdbf033fa` is one file, `SOLR-10364-TESTING.md`, with 22 deletions. Every citation resolves at the packaged head with its line range inside the file.
- Heads: every citation names `efb1e6717f9`, except the two base-side links that name the merge-base `cabedd1d968`.
- Merge-base label: line 9 says "On the merge-base commit". `cabedd1d968` is an ancestor of `upstream/main`, and it is the merge-base of the branch with `upstream/main`. The receipt's "not on main's history" statement uses the fork's `origin/main`, which is a different ref. The label holds, so it is left as written.
- Counts and dates: the Proof's 6 of 6 at the head, verified 2026-10-11, and the base premise (one failure of six) match `receipts/SOLR-10364.md`.
- The pre-existing sentence on line 27 ("We can open a follow-up ticket and pull request for it on request.") is not contradicted by any receipt. It is left unchanged, as the assignment directs.
- Process vocabulary: no line contains gate, gated, receipt, claim, job, vm1 or vm2. No dashes.

## SOLR-11678 (`pr-drafts/security/SOLR-11678.md`): NOT CONVERGED

Patched in this round:
- **SEC-R1-03, Proof form (line 24).** "Focused tests at packaged head `611eae...` (the fork branch tip `55d8cd189d15` plus one commit that removes a root-level file):" becomes "Focused tests at head `611eae...`:". This is the form the register prescribes.
- **SEC-R1-04, environment and property sentences (line 15).** "A new environment variable, `SOLR_SSL_KEY_MANAGER_PASSWORD`, sets the Java property `solr.jetty.keymanager.password`." becomes the recorded replacement from `reports/security-convergence-round-1-response.md`: the variable is read by the environment credential provider, the property is read by the system property provider, and `SSLConfigurations` reads both through the existing credential providers.
  - Judgement for the review side to accept or reject: the recorded replacement's third sentence restates the draft's second sentence, so the predicate of the second sentence was also changed to avoid saying the same claim twice. The citation parenthetical after it is unchanged.
  - Each claim was checked at the packaged head: the environment provider reads the variable (`EnvSSLCredentialProvider.java` lines 40, 55, 60 to 67); the system property provider reads the property (`AbstractSSLCredentialProvider.java` line 37, `SysPropSSLCredentialProvider.java` lines 25 to 33); `SSLConfigurations` reads both through the providers (`SSLConfigurations.java` lines 96 to 98 and 127 to 133).

Still open:
- **SEC-R1-05 (blocking, branch side).** `EnvToSyspropMappings.properties` has no `SOLR_SSL_KEY_MANAGER_PASSWORD` line at the packaged head. The branch mapping and its test assertion belong to a gate job, which has not run. When the mapping lands at the pushed head, insert this after the environment-provider sentence in line 15: "The variable maps to nothing in `EnvToSyspropMappings.properties`, as the other SSL password variables do, so it is not copied to a system property." Until then the draft stays on hold.
- **SEC-R1-07 and SEC-R1-08.** Out of scope for this round, as the assignment says. Not patched and not graded.
- **Proof-leg wording.** The draft says "inconclusive by construction", and the receipt says "by compile". The substance matches. This is not patched because proof-leg wording is out of scope.

Citations at the packaged head: all six GitHub links in the draft were checked in the object store after the fetch. Each resolves, and the changelog file exists. The diff from the pre-push tip `55d8cd189d15` is one file, `SOLR-11678-TESTING.md`, with 18 deletions.

## Not done

- No build, test, Gradle run, `gh` call, PR write, comment or post.
- No branch was edited. The packaging commits were already pushed by an earlier owner decision.
- The gate for SEC-R1-05 is not run here.

## Queued from this window, not reviewed in this round

Other lanes pushed these to `pr-prepare` during the same window. They were not part of this assignment and are not reviewed here: the vm2 claims and receipts for SOLR-15357, SOLR-15945, SOLR-16977 and SOLR-17047, and the SOLR-11678 SEC-R1-05 gate note. They need their own round.
