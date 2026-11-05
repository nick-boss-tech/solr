# Claim: PR 5299 review fixes gate round 1, phone-agent

Status: IN PROGRESS
Job: `gates/pr-5299-review-fixes-round-1.md`.
Executor: phone-agent (Linux, this VM).
Claimed: 2026-11-05, after Nick directed the phone agent to take the assignment.

## Preflight and ownership checks

- Live PR #5299 read back before claiming: open, draft, head `f342e160417d64ed2dced989052b29a76d31996e` on `solr-bats-assertion-hardening-submit`, exactly the job's candidate; reviewed parent `99d4b8e68b446b1e750159a2ccb6549495154d60`.
- No other gate claim exists for this job (only the Windows implementation claim/report). The stale phone-agent-2 claim named in the job is administrative; the host test-queue lock was checked and is free, and no other host build is running.
- Disk: 68 percent used, 46G free (guard is 85 percent).

## Host capability statement, recorded before running

- JDK 21 only (vendored 21.0.12.1+1). No JDK 25 exists on this host, so every JDK 25 leg is unexecutable here.
- No Docker binary or daemon on this host, so proof C legs 1 to 4 (candidate image build and rolling upgrade) are unexecutable here.
- This VM runs as root and `bin/solr` refuses to start as root, so every Solr-starting BATS leg is unexecutable here: proof B matrix, proof A legs 4 to 6 real TLS cases, and the proof D server controls.
- Runnable here and taken by this claim: tidy preflight at the candidate; proof A leg 1 helper suite on the JDK 21 environment; proof A legs 2 and 3 predicate mutations plus the original gate's helper guard mutations; proof C leg 5 report-guard fixtures and leg 6 workflow validation; final tidy, `git diff --check`, `:solr:packaging:check -x test`, and root `check -x test` in a normal clone at the candidate.

Expected outcome on this host: a partial execution with exact counts for the runnable legs and named capability blockers for the rest. GREEN is not attainable here and will not be claimed.
