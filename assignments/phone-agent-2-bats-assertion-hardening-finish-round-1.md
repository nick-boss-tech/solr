# Directed assignment: finish the BATS assertion hardening gate (phone-agent-2)

Status: OPEN
Directed to: phone-agent-2, at Nick Shanin's direction on 2026-10-27. Other hosts leave this gate to her while this assignment is open.
Tags: gate, BATS, packaging

## Task

Finish the gate defined in `gates/bats-assertion-hardening-round-1.md`, which is the authoritative definition and is not restated here. You already hold the live claim (`claims/bats-assertion-hardening-round-1-phone-agent-2.md`); this assignment directs the remaining work under that claim, in the order below. Candidate: fork branch `solr-bats-assertion-hardening-submit`, current head `0d7c6f4e6cb6d2211b59ed9eed36db90182b122e`, base apache main `0d6b2e32f598d772aed094038022a6a588fdfc5a`.

## Already recorded (verify, do not redo without cause)

- Round 1 gate report (`reports/bats-assertion-hardening-round-1-phone-agent-2.md`): INCOMPLETE. Normal matrix at the current head: legacy 55 of 62 and Picocli 17 of 18 on both JDK 21 and JDK 25.
- Follow-up report and preserved patch (`reports/bats-assertion-hardening-round-1-phone-agent-2-follow-up-2026-10-26.md` and its `.patch.gz`): two assertion fixes plus a test-only TLS diagnostic adjustment, held locally and never pushed. Focused results: helper plus stream 14 of 14 on both JDKs; SSL file 6 of 7 on both JDKs.
- TLS hostname diagnosis and design recommendation reports (2026-10-26): the client-hostname probe inside `start solr with mTLS needed` fails with a client-side ClosedChannelException; the fresh server log shows `(certificate_unknown) Endpoint Identification Algorithm HTTPS is not supported on the server side`. The design report holds that the server-side identity policy for the pinned setting is underspecified and that no test-only adjustment makes the current behavior a valid hostname check.

## Remaining work, in order

1. **Land the follow-up patch.** Apply the preserved patch artifact to the candidate branch, after verifying its contents match the follow-up report (the ZooKeeper listing status check in `bats_helper.bash`, the SolrJ 401 wording in `test_stream.bats`, the fresh server-log TLS check in `test_ssl.bats`). Commit with Nick Shanin identity, no trailers, push normally with no force, and verify the remote head. Post a superseding add-only head record at `gates/bats-assertion-hardening-round-1-head-2.md` naming the new exact head. All proof from this point runs on the new head only; runs at `0d7c6f4e6cb6` are not final proof.
2. **Settle the mTLS hostname probe by discrimination.** Run the `start solr with mTLS needed` case on the pinned base in your own environment, with the same task-scoped IPv4 setup used in the follow-up, and compare it with the new head. Do not broaden the assertion: no bare ClosedChannelException, no arbitrary SSL exception, no skip. If the base passes and the new head fails, repair the check using an operation-scoped, fresh server-log assertion on the observed producer diagnostic, per the gate file's repair section; commit and push the repair, post a superseding head record, and continue. If the base fails identically, record it as a baseline blocker with the base and head evidence side by side and the design recommendation cited, leave the case unresolved, and continue with steps 3 to 5; the final report names this item as the blocker and the gate stays incomplete on it pending an owner scope decision.
3. **Run all eight discriminating control groups** from the gate file, in isolated scratch trees or wrappers, never pushed: helper guards; posting count; postlogs; TLS refusal and curl exit codes; degraded health; deletion and exit code; export and authentication diagnostics; quiet post, compression, and stop. For the representative posting, commit, TLS, and stale-export scenarios, run the same injection against the old base test shape and the hardened shape, and record old-accepts plus new-rejects as the discrimination.
4. **Rolling-upgrade Docker case.** Re-check Docker availability on your host once. If a daemon and the two rolling-upgrade images are available, run `test_rolling_upgrade.bats` as part of the final matrix and record image digests. If Docker is still unavailable, post an explicit subleg handoff for that single case as a new assignment file naming the pinned head and the image and digest recording required, and keep the gate incomplete until that leg returns. A skip does not prove the case.
5. **Final proof and receipt.** At the final exact head: the four-leg normal matrix (JDK 21 and JDK 25, legacy and Picocli) per the gate file, with fresh JUnit XML, full transcripts, and preserved logs per leg; final `tidy`, `git diff --check`, and `:solr:packaging:check -x test`, with `errorProneSkipped` reported accurately if it occurs. Write the finish report (deliverables below). On GREEN only, meaning every gate criterion is met including the Docker leg and a resolved hostname item, write the receipt at `receipts/bats-assertion-hardening-round-1.md` with the final head, the exact matrix, control results, limits, formatting and check outcomes, and the PR description draft the gate file requires. If the hostname item or the Docker leg is unresolved, the finish report states the gate is incomplete, names the exact blockers, and no receipt is written.

## Deliverables

- Superseding head record: `gates/bats-assertion-hardening-round-1-head-2.md` (and a further record for any later head change).
- Finish report: `reports/bats-assertion-hardening-finish-round-1-phone-agent-2.md`.
- On GREEN only: `receipts/bats-assertion-hardening-round-1.md`.

## Limits

No PR opening, no PR or GitHub comment, no Jira write, no upstream write. No production code changes: this branch is test-only, and the hostname policy question in the design report is production scope that stays out of this gate. Do not touch `test_security_manager.bats` or any PR 5225 follow-up work. Mutations and injections stay in scratch trees. Follow the gate file wherever this assignment and the gate file differ.
