DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Shape: scoping. Evidence: reports/critical-comments-round-1-batch-08.md.

----

[~janhoy] This ticket asks for secure ZooKeeper to be easy to set up, and eventually the default. The 2025 comments say embedded ZooKeeper could make this simpler, with generated certificates and a matching client config.

The decision the ticket turns on is whether embedded ZooKeeper is the route to a secure default, or whether the external ZooKeeper setup changes. That affects every installation, so I have not drafted an implementation.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
