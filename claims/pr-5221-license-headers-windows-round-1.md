# Claim: PR 5221 license-header fixes on Windows

Status: ACTIVE

- Host: windows; capabilities: implementation, review, records.
- Started: 2026-10-26T00:12:59Z.
- Direction: Nick explicitly requested implementation on top of apache/solr PR 5221 and handoff as a Linux gate assignment in this chat on October 25, 2026 (Eastern).
- Reviewed board snapshot: `76f0722613312e4277e76ab3c943aa628bc07203`; no overlapping PR 5221 assignment or claim found.
- Input: PR 5221 head `e0453c0931cab27fcb5c001f6bca3e412b3b1570`.
- Output branch: `nick-boss-tech/solr:solr-pr-5221-license-headers-submit`, a separate branch retaining PR 5221's history. Do not write to the contributor's branch or apache/solr.
- Scope: conservative leading-notice/generated-source protection, safe Git candidate/ref/path handling, explicit order before Java formatting, contributor documentation, and repeatable functional proof fixtures.
- Decision ladder: rung 1 for preserving original notices and generated-source exclusions (existing Spotless rules); rung 3 for implementing the reviewed fixes under Nick's explicit direction. Keep writes limited to Git-added/nonignored new Java sources. Missing Git metadata is a documented no-op; an invalid/unavailable comparison in a Git checkout must fail before writes.
- Deliverables: fork implementation branch; `reports/pr-5221-license-headers-implementation-round-1.md`; `assignments/pr-5221-license-headers-linux-gate-round-1.md` with exact head, commands, acceptance criteria, and receipt path.
- Windows performs source/static checks only. Gradle, formatting execution, and functional proof belong to the Linux gate. State remains Implemented, Proof Pending until the final head has a passing receipt.
- No public PR/comment/JIRA write is authorized by this claim.
