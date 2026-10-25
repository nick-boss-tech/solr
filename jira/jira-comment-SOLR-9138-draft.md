DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Shape: scoping. Evidence: reports/critical-comments-round-1-batch-02.md.

----

[~scottchu] This ticket asks whether Solr's XML response should always wrap text and string values in CDATA.

The decision the ticket turns on is whether the XML writer changes its default output for every text field, or whether it is an option. That decision affects existing clients, so I have not drafted an implementation.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
