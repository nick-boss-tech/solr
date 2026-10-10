# Claim: Lucene 9.12.3 version claims in the drafts and receipts

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: commit `6413d368e65` ("Lucene claims scoped by version"), which adds statements that checks hold on Lucene 9.12.3 as well as 10.4.0. It changes `pr-drafts/update-processing/SOLR-6065.md`, `pr-drafts/suggester/SOLR-10937.md`, `pr-drafts/suggester/SOLR-11844.md`, `receipts/SOLR-2632.md`, and `receipts/SOLR-2681.md`.

What the local Gradle cache holds, checked on 2026-10-09 under `C:\Users\shaninna\.gradle\caches\modules-2\files-2.1\org.apache.lucene`:
- `lucene-core`: 9.12.3 and 10.4.0 are present.
- `lucene-suggest`: only 10.4.0 is present.
- `lucene-highlighter`: only 10.4.0 is present.

So the 9.12.3 claims about the suggester and highlighter jars cannot be checked here. The 9.12.3 claim about the `lucene-core` message in SOLR-6065 can be checked by reading the class file.

Split: two subagents, read only.
- The first checks the SOLR-6065 claim against the `lucene-core` jars for 9.12.3 and 10.4.0.
- The second checks the suggester drafts and the highlighting receipts, states which claims cannot be verified from the cache, and proposes wording that states only what is verified here, marked as the main side's record where it is not.

Output: `reports/lucene-version-claims-6065.md` and `reports/lucene-version-claims-suggest-highlight.md`. The lead writes the roll-up.

Not in scope: edits to any draft or receipt, PR, comment, or branch; builds, Gradle, and test runs. Reading class files with `javap -c -p` is allowed, and so is reading a jar's contents with a zip listing. No Gradle task runs.
