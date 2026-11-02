# Draft comment: PR 5278 JCL license metadata (round 1)

Status: POSTED 2026-11-02 after Nick's per-text go. Comment: https://github.com/apache/solr/pull/5278#issuecomment-6581876754 (author nick-boss-tech; body read back byte-identical, details/summary verified).
Pinned identities (verified live 2026-11-02): PR head P 82f60b1b7976c187541a6f6808a781bb7f8b9228
(branch renovate-9x/logging); repair head H cb8c6ab2752b5202b52c7479b9b77ee7806b350e
(fork branch pr-5278-jcl-license-metadata, one commit on P).

## Comment text

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Prepared fix: [compare this PR's head with the repair branch](https://github.com/nick-boss-tech/solr/compare/82f60b1b7976c187541a6f6808a781bb7f8b9228...cb8c6ab2752b5202b52c7479b9b77ee7806b350e)

One metadata item in this update: the `jcl-over-slf4j` 2.0.20 JAR carries an Apache 2.0 license (upstream corrected the JAR packaging in 2.0.20, per the [SLF4J release notes](https://www.slf4j.org/news.html)), but Solr's metadata for it still holds the previous MIT license text and notice. CI stays green because the jar license check accepts a recognized license filename suffix without comparing the file's contents, so this one is easy to miss. The branch above replaces the stale MIT license file with the Apache 2.0 text from the JAR and clears the stale notice contents. The SLF4J API and JUL bridge metadata stay MIT and are not touched.

<details>
<summary>Verification and limits</summary>

The committed Apache license file matches META-INF/LICENSE.txt from the jcl-over-slf4j 2.0.20 JAR and the tagged module license at qos-ch/slf4j v_2.0.20, SHA-256 59899c6091b540582ed617e8eeaac4919dc985ccfc35459ee9752b699be5205b, with line endings normalized to LF. The artifact ships no NOTICE, so the cleared (empty) notice companion follows the existing repo convention. This is a static provenance audit: the texts were compared against the artifact and the tagged source and the hash recorded, but no Gradle run has been executed at the repair head yet.

</details>

The repair is one commit on top of this PR's head, so it fast-forwards:

```
git switch renovate-9x/logging
git fetch https://github.com/nick-boss-tech/solr.git pr-5278-jcl-license-metadata
git merge --ff-only cb8c6ab2752b5202b52c7479b9b77ee7806b350e
```

You're welcome to take the commits or adapt the changes.
