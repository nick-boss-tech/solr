DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Shape: stale-check. Evidence: reports/critical-comments-round-1-batch-08.md.

----

[~nilesh892003] This ticket reports that cores hang during a full index bootstrap in leader and replica mode, in a two-layer setup with a repeater. Closed PR #3389 by another contributor tried to abort replication on a retry limit. It has no comments since it was filed in 2025.

Is this still seen on a supported Solr release with the same two-layer setup? A thread dump from a hung core would show where the bootstrap waits.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
