DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Shape: scoping. Evidence: reports/critical-comments-round-1-batch-03.md.

----

[~mohanmca01] This ticket asks for Arabic search that treats the alef variants (أ, إ, آ) as equivalent. The reporter says users miss results when the exact letter is not typed.

The decision the ticket turns on is which normalization applies to which field: a query-time filter, an index-time filter, or both. That changes results for existing indexes, so I have not drafted an implementation.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
