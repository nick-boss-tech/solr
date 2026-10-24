# Jira comment draft: SOLR-17976 (status note)

Status: DRAFT (not posted). Promoted unchanged from the comment family examples of 2026-10-24, where it was spot-checked the same day: branch `solr-17976-submit` live at `56ea43c448ed` (re-verified 2026-10-24), no PR, the ticket has never been told about the branch. Posting needs Nick Shanin's explicit go.

---

I read this ticket and the distributed search code. When scores tie, the ordering falls back to replica URL, which follows node addresses, so results can come back in a different order when the nodes change. The shard-name tie-break discussed on this ticket is deterministic.

I have a branch that implements the deterministic tie-break: [solr-17976-submit|https://github.com/nick-boss-tech/solr/tree/solr-17976-submit]. It has not been through a full test run yet, and I have not opened a PR.

What would you like: should I finish the test runs and put the PR up, or is this being handled somewhere I have not seen?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
