# Receipt: PR review apache/solr #4653 (vm4)

- PR: https://github.com/apache/solr/pull/4653, open, not draft, head branch `solr-18315-utf-conversion` on `psalagnac/solr`, 8 files, +105/-313 (GitHub files API), updated 2026-09-26, `mergeable_state` unknown at the snapshot.
- Files: changelog entry (added, 7, `type: other`), `RequestWriters.java` (benchmark param `xml`/`javabin`), `ByteArrayUtf8CharSequence.java` (+6/-19, `stringProvider` removed), `ByteUtils.java` (removed, -230), `JavaBinCodec.java` (+49/-43, `CharsetEncoder`), `Utils.java` (+5/-4, `toUTF8` via `UTF_8.encode`), `TestJavaBinResponseWriter.java` (+4/-4), `TestJavaBinCodec.java` (+33/-12).
- Public removals: `org.apache.solr.common.util.ByteUtils` (class) and `ByteArrayUtf8CharSequence.stringProvider` (public field).
- Gate: none run.
- Outcome: NEEDS-CHANGES. Draft: `drafts/pr-review/4653.md`.
