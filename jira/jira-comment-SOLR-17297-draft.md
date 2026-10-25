DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Shape: scoping. Evidence: reports/critical-comments-round-1-batch-07.md.

----

[~patson] This ticket reports a ClassNotFoundException when a plugin jar holds a PostingsFormat and a module is enabled. A 2024 maintainer comment says all modules and the shared lib share one classloader, and that package isolation is different.

The decision the ticket turns on is whether modules should get classloader isolation, or whether plugin jars should be loaded through packages. That is a design choice, so I have not drafted an implementation.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
