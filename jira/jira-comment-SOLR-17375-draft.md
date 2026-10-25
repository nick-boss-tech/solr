DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Shape: scoping. Evidence: reports/critical-comments-round-1-batch-08.md.

----

[~dsmiley] This ticket asks for IndexReader close to run asynchronously on commit, because closing becomes expensive with the MemorySegments API. A 2026 comment quotes Uwe Schindler on where the close happens in SolrCore.

The decision the ticket turns on is whether the close should move off the commit path at all, given that a Lucene change may reduce the cost. I have not drafted an implementation.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
