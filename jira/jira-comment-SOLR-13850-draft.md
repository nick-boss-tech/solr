DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Shape: scoping. Evidence: reports/critical-comments-round-1-batch-04.md.

----

[~drapushko] This ticket reports that an atomic update of other fields clears the data in a PreAnalyzedField. The 2019 comments show that atomic updates need the field stored, and PreAnalyzedField cannot use docValues.

The decision the ticket turns on is whether a PreAnalyzedField must be stored for atomic updates, or whether atomic updates should keep the stored analysed value. That changes what a field definition must declare, so I have not drafted an implementation.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
