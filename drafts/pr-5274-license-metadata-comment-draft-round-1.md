# Draft comment: PR 5274 JCL license metadata (round 1)

Status: POSTED 2026-11-02 after Nick's per-text go. Comment: https://github.com/apache/solr/pull/5274#issuecomment-6581878450 (author nick-boss-tech; body read back byte-identical, details/summary verified).
Pinned identities (verified live 2026-11-02): PR head P 3f827a780a292feddeb58077dc531fc3c0086d0c
(branch renovate/all-non-major-dependencies); repair head H e3bcf3af6fcb544c624efdcb4329ce5ef5a271f3
(fork branch pr-5274-jcl-license-metadata, one commit on P). The repair diff is identical
to the PR 5278 repair, on this PR's own parent.

## Comment text

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Prepared fix: [compare this PR's head with the repair branch](https://github.com/nick-boss-tech/solr/compare/3f827a780a292feddeb58077dc531fc3c0086d0c...e3bcf3af6fcb544c624efdcb4329ce5ef5a271f3)

One license metadata item in this sweep: it selects the same `jcl-over-slf4j` 2.0.20 files as the logging update, and the same correction applies. The 2.0.20 JAR carries an Apache 2.0 license (upstream corrected the JAR packaging in 2.0.20, per the [SLF4J release notes](https://www.slf4j.org/news.html)), but Solr's metadata for it still holds the previous MIT license text and notice. CI stays green because the jar license check accepts a recognized license filename suffix without comparing the file's contents, so this one is easy to miss. The branch above replaces the stale MIT license file with the Apache 2.0 text from the JAR and clears the stale notice contents. The SLF4J API and JUL bridge metadata stay MIT and are not touched. This comment covers only that item; the rest of the sweep was not audited for license metadata.

<details>
<summary>Verification and limits</summary>

The committed Apache license file matches META-INF/LICENSE.txt from the jcl-over-slf4j 2.0.20 JAR and the tagged module license at qos-ch/slf4j v_2.0.20, SHA-256 59899c6091b540582ed617e8eeaac4919dc985ccfc35459ee9752b699be5205b, with line endings normalized to LF. The artifact ships no NOTICE, so the cleared (empty) notice companion follows the existing repo convention. This is a static provenance audit: the texts were compared against the artifact and the tagged source and the hash recorded, but no Gradle run has been executed at the repair head yet.

</details>

The repair is one commit on top of this PR's head, so it fast-forwards:

```
git switch renovate/all-non-major-dependencies
git fetch https://github.com/nick-boss-tech/solr.git pr-5274-jcl-license-metadata
git merge --ff-only e3bcf3af6fcb544c624efdcb4329ce5ef5a271f3
```

You're welcome to take the commits or adapt the changes.
