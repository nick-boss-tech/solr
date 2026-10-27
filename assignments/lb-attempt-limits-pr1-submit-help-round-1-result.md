# Result: LB attempt limits PR 1 fork push help (round 1)

Host: vm4. Assignment: `assignments/lb-attempt-limits-pr1-submit-help-round-1.md`.

Result: pushed. `nick-boss-tech/solr:solr-lb-attempt-limits-pr1-submit` moved from `7a226ef031f8b82527a1209ce760fcf37e611601` to `c2d2c62594` as a plain fast-forward. The remote tip was read back with ls-remote after the push.

How: fetched the fork branch into a scratch worktree at `7a226ef031`, applied `drafts/lb-attempt-limits-pr1-repair-series.mbox` with `git am --3way` (two commits: `7da7154a09` tidy, `c2d2c62594` javadoc qualification), then `git push` of HEAD. Both commits are authored by Nick Shanin with no trailers. No force push, no other credentials, no GitHub or Jira writes.

Limits: vm4 did not reproduce the push rejection, so the cause on vm2 is still unknown. This host's token was the same one used for the push. The tree was not compared with `28dbd2e` because that commit is not on the fork or on this host; the receipt's tree equality claim is unverified here.
