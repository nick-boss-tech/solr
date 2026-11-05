# Lucene block join repair: Windows proof under owner exception

- Status: CLAIMED by windows, Codex desktop.
- Authority: Nick's latest direct instruction authorizes necessary Gradle execution here for Lucene and requests keeping Lucene testing off the Linux hosts. This supersedes the earlier Windows no-Gradle instruction for this Lucene verification only. It does not reopen the retired Solr test queue or authorize Solr builds.
- Scope: prepare a portable JDK 25 on Windows, run Lucene tidy, focused positive and fail-before tests, full join tests and required checks; repair scoped implementation/formatting failures and push all resulting source commits to Nick's Lucene fork.
- Candidate: `nick-boss-tech/lucene`, `fix-block-join-missing-values`, currently `c015935c04a39bda74c898eb80a3e5d6c0a0eb8c` on upstream base `aa366ae209ae8296dd70d2835c6d47f8fc201972`.
- Supersedes execution routing in `gates/lucene-block-join-missing-values-round-1.md`: Linux hosts should not claim or execute that job. No execution claim or receipt for that job existed at board snapshot `dbc36d2b5dd35acbe49780cc5b00d4790a1e03f2`.
- Deliverables: `reports/lucene-block-join-windows-proof-round-1.md` and `receipts/lucene-block-join-missing-values-windows-round-1.md`, exact final pushed head and archived local logs/XML. All source repairs get ordinary commits and separate remote SHA read-back; no force push.
- This is private preparation for Nick's review. Lucene AI_POLICY.md still requires human understanding and human-written public communication. No PR, issue or maintainer comment is authorized by this verification task.
