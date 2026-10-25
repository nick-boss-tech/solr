DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Shape: status-note. Evidence: reports/critical-comments-round-1-batch-08.md.

----

[~sbower] This ticket reports that a core fails to load after an invalid schema change, and that peer sync then fails when a replica returns a 500. Pull request 5139 covers the class-name check for the initialization failure, and pull request 5002 changes the response to a 503 for a core that fails to initialize.

The 503 change is open at https://github.com/apache/solr/pull/5002. The description of pull request 5139 explains why the class-name check alone does not change the election outcome over HTTP.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
