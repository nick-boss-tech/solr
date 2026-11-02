# Assignment: finish PR 5278 license gate, remaining legs, round 1

Status: OPEN. Continues `gates/pr-5278-license-metadata-round-1.md` (the immutable job) after the vm2 handback `claims/pr-5278-license-metadata-gate-round-1-vm2-handback.md`. Capability: gate, records. Host: any capable Linux host with JDK 11 and Gradle 8.10 wrapper, no Docker needed.

- Pinned head H: `cb8c6ab2752b5202b52c7479b9b77ee7806b350e`; parent P `82f60b1b7976c187541a6f6808a781bb7f8b9228`. Fork branch `pr-5278-jcl-license-metadata`. Refresh the fork and PR head before proof; stop and report if either moved.
- Claim first: add a new execution claim naming this assignment and the host. Do not edit the handback or the job.
- Done on vm2 and not repeated: tidy (rc=0, clean), the diff check, the committed JCL SHA-256 check.
- Remaining: licenses task (`:solr:server:licenses :solr:core:licenses --info`), JCL JAR comparison against 2.0.20 (locate the JAR in the Gradle cache; the PR tree has `solr/licenses/jcl-over-slf4j-2.0.20.jar.sha1`), `check -x test`, final `git status` and `git diff --check`, remote reads, then the receipt `receipts/PR-5278-license-metadata.md`.
- Serialize behind the host build lock. After any crash, check for stale `index.lock` only after confirming no git process is running, and check for stray Gradle processes before rerunning.
- Also write the PR 5276 receipt (`receipts/PR-5276-license-metadata.md`) from `handoff-5278/pr-5278-handback.md`'s 5276 section and the vm2 log archive, or from a fresh gate run if the next host prefers.
