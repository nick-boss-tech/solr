DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Shape: scoping. Evidence: reports/critical-comments-round-1-batch-03.md.

----

[~ehatcher] This ticket asks whether a real-time get should return a copyField destination when the source field is stored and the destination is also stored. A 2018 comment says a user hit the same behaviour.

The decision the ticket turns on is whether real-time get should follow the same rule as the normal stored-field response. That affects which fields existing clients see, so I have not drafted an implementation.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
