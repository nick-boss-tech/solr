We checked this against Solr main at commit 498a09aea97 and found that the release wizard already removes an uploaded RC folder when an RC is aborted, but not every artifact.

What we checked:

- Aborting an RC removes the uploaded RC folder from the staging area, but only when the upload step has completed.
- The Maven and Docker artifacts from the staging step are not removed when an RC is aborted.
- The cleanup that exists was added before this ticket was filed.

How we tried to reproduce it:

- Not run. The wizard needs access to the staging server to run its steps.

What this means:

- An RC aborted before its upload leaves nothing to clean, and Maven and Docker artifacts are left in place. The report does not say which case was seen.

If you still see this:

- Please say which release (Solr, Lucene, or Solr Operator), which step was reached before the abort, and which artifacts remained, so we can check the wizard for that case.
