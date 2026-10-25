# Jira comment draft: SOLR-18295 (round 2, gate results)

Status: DRAFT (not posted). Supersedes the pre-gate practice note (goal files/jira/jira-note-solr-18295-draft.md). Evidence: `receipts/SOLR-18295.md` and `reports/solr-18295-gate-round-1.md` (vm6, 2026-10-24). Posting needs Nick Shanin's explicit go.

---

[~janhoy] The runs I asked about last time are done, on the branch [solr-18295-submit|https://github.com/nick-boss-tech/solr/tree/solr-18295-submit] at {{b273b245115b}}. The change is test-only: {{ReplicationFactorTest}} overrides {{createCloudClient}} to route updates to shard leaders first, and the AwaitsFix mute comes off. Format, Error Prone compile, and the module check pass at that head.

The numbers, from 20 fresh whole-class runs each:

 * With the change: 20 of 20 pass (seeds {{18295ABC0001}} to {{18295ABC0014}}).
 * Baseline (mute removed, any-replica routing forced): 16 of 20 pass. The 4 failures are all {{ClosedChannelException}} on a send to a follower whose proxy the test had just closed.

The honest limit: those baseline failures are transport errors before commit, not the lost-after-commit case your analysis describes, so this sample does not confirm the bug as diagnosed. What it does show is that leaders-first routing removes the send-to-a-cut-follower failures in this sample, which is the code path the change is justified by.

Given that, do you want this as a PR alongside #4563, or should it stay parked until a run actually reproduces the lost-after-commit case? I will not open anything before you answer.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
