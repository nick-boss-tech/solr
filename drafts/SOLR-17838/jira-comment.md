We checked this against Solr main at commit 498a09aea9. This ticket is the umbrella for the SIP-25 encryption module proposal, and it does not describe a bug.

What we checked:

- Solr main has no encryption module. There is no module directory for encryption, and no encryption-at-rest code in the core.
- The existing encryption-related code is unrelated to this ticket: the password encryption option for the data import handler, and password handling for protected PDF and DOCX files in the extraction module.
- The commit history on main has no commit that names SOLR-17838 or SIP-25.

What this means: there is no defect to fix or reproduce here. We are leaving the ticket open as a tracker until the SIP-25 status is settled and the sub-tasks are created or the umbrella is closed.

If you still see this: nothing in this ticket needs a reply from a reporter. Please link the sub-tasks and the SIP-25 discussion that define the scope, so the umbrella can be closed or kept with a clear list.
