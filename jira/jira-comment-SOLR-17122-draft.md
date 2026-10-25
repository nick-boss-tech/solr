DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Shape: scoping. Evidence: reports/critical-comments-round-1-batch-06.md.

----

[~kvanlieshout] This ticket reports that unique, HLL and numBuckets counts are wrong when a field has many distinct values. The Ref Guide says the unique function is not exact beyond 100 values, and that HLL is a cardinality estimate.

The decision the ticket turns on is whether the reported counts are outside the documented estimate error. If they are, it is a defect. If they are not, the ticket should point to the documentation. I have not drafted a fix.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
