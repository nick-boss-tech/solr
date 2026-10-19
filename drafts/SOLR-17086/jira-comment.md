We checked this against Solr main at commit 498a09aea9. The NullPointerException in the report is fixed on main.

What we checked:

- The media-type filter for v2 APIs called the request parameters without checking that the request was set. That call is the NullPointerException in the report.
- Commit c058ea5dac (a change for the v2 COLSTATUS and segments APIs) adds the missing check. Before that commit, the call is unguarded; on main it is guarded.

What this means: the HTTP 500 caused by that NullPointerException should not occur on current main. We did not reproduce the failure on current main, so this comment is based on the code change.

If you still see an HTTP 500 from a v2 backup request on a current release: please send the Solr version, the request, and the stack trace, so we can check whether another path raises it.
