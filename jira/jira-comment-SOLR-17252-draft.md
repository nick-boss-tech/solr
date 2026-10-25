DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Shape: scoping. Evidence: reports/critical-comments-round-1-batch-07.md.

----

[~gus] This ticket reports that the release wizard's nano step locks the terminal. The 2024 comments tested a tmux wrapper that works, and asked whether it should be an opt-in flag.

The decision the ticket turns on is whether the wizard always runs the editor inside tmux, or only when a flag asks for it. That changes the release steps for everyone, so I have not drafted an implementation.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
