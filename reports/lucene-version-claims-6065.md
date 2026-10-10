# SOLR-6065 Lucene message check (9.12.3 and 10.4.0)

Checked 2026-10-09 by Claude Code (AI agent), read only. Method: jar extraction to a scratch directory, then `javap -v` for the constant pool and `javap -c` for method bodies. The scratch extract was deleted afterwards. No builds, Gradle, or tests were run.

## Jars

- 10.4.0: `lucene-core-10.4.0.jar`, in `org.apache.lucene\lucene-core\10.4.0\7493bc763cd5e91f2a8f7722c2f90d8ce15c6319\`
- 9.12.3: `lucene-core-9.12.3.jar`, in `org.apache.lucene\lucene-core\9.12.3\b4b3fbe8608b438d0a810d15187498e6de974d0e\`. The sibling directory `707e6b7a3f0afe3bfb76b11003a51bbc6189b993` holds only a `.pom` file.

## Where the message is raised

The same two methods exist in both jars:

- `org.apache.lucene.index.IndexWriter.tooManyDocs(long)` throws `IllegalArgumentException`. The message is "number of documents in the index cannot exceed ... (current document count is ...; added numDocs is ...)".
- `org.apache.lucene.index.DocumentsWriterPerThread.reserveOneDoc()` throws `IllegalArgumentException`. The message is "number of documents in the index cannot exceed ...".

Prefix in both jars: "number of documents in the index cannot exceed".

Identical: yes. The full message constants in both classes are byte-identical between 10.4.0 and 9.12.3.

Other classes (`SegmentInfos`, `BaseCompositeReader`) hold a different message, "Too many documents: an index cannot exceed". The draft's prefix does not match that text.

Scope: this checks the message text only. It does not trace which update call paths reach these two methods, and it does not check the BAD_REQUEST mapping at base.

## Verdict

Draft sentence (`pr-drafts/update-processing/SOLR-6065.md`, line 39): the prefix "was checked in the Lucene jars for both version lines ... The wording is identical in both."

SOLR-6065 9.12.3 claim: verified.
