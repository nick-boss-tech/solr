# Comment draft: SOLR-6420

- Status: DRAFT (not posted). Posting needs the owner's go.
- Shape: staged ask (last activity is a committer's)
- Addressee: [~shalin] — the ticket's last commenter and maintainer who described the missing verification inputs.

[~shalin], I reread the report and your 2014 response: the report is from `branch_4x` with an index built on 4.9 that could be read but not updated, and the ticket has no attached index or exact deployed build. I could not reproduce the false-green `ADDREPLICA` result on current main without those original inputs; your comment says those exact bits and index files are needed to verify it.

Is that evidence sufficient to leave this as an unverified report until a reproducible case is available?

 * Keep it open if the reporter can provide the exact build, index, replica state, and steps that produced a green replica without successful replication.
 * Close as not reproducible if those inputs are no longer available and no released Solr version reproduces the behavior.
 * Reopen the defect investigation if the false-green state is reproduced on a released version with the required artifacts.

I recommend not calling this fixed or impossible from the current record; the missing build and index prevent a faithful reproduction.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
