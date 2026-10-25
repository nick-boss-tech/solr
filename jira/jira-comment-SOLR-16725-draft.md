DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Shape: scoping. Evidence: reports/critical-comments-round-1-batch-06.md.

----

[~indurajagopalan] This ticket reports that CLUSTERSTATUS returns different data types for a restored collection than for other collections. The 2023 comments say the impact is mainly on clients that parse the response case-sensitively.

The decision the ticket turns on is whether the restored collection's output should match the other collections, or whether the API should document the types. That changes what existing clients receive, so I have not drafted an implementation.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
