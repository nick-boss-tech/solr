DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Shape: stale-check. Evidence: reports/critical-comments-round-1-batch-05.md.

----

[~ichattopadhyaya] This ticket asks for less strict validation of replica metadata, to avoid outages on upgrade. The 2021 comments say the strict validation came from SOLR-14245, which is Resolved Fixed in 8.5, and that validation should be more lenient.

Is the replica metadata validation still strict in the current release? If it is, an upgrade from an 8.x release with an existing replica would show whether the outage remains.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
