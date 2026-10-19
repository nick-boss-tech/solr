We checked this against Solr main at commit 498a09aea97 and found that the status request no longer fails for implicitly routed collections.

What we checked:

- The collection status code guards the range of each shard. Implicitly routed shards have no range, and the guard skips them. The guard was added in August 2021 (commit 83770854a35, which is linked to SOLR-14506, a related report of the same error).
- The same commit adds a test for collection status on implicitly routed collections.

How we tried to reproduce it:

- We have not run the reporter's sequence on current main in this pass.

What this means:

- The error in this report appears to be fixed on current main. The fix is inferred from the same error and the same code path, so we have not confirmed it against this exact report.

If you still see this:

- If the status request still fails on a current release for an implicitly routed collection, please send the Solr version and the error with its stack trace, and we will check it against the guard.
