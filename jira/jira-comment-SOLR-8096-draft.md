DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Shape: stale-check. Evidence: reports/critical-comments-round-1-batch-04.md.

----

[~yseeley@gmail.com] This ticket reports a large faceting slowdown on multi-valued fields, caused by LUCENE-5666. A 2018 comment points to a patch in LUCENE-8178 that reports a 2 to 2.5 times speedup on a 35M-document index.

Is this still a problem on a supported Solr release, and does the LUCENE-8178 change address it? A timing run on the current release with the same facet request would show whether the gap remains.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
