We checked this against Solr main at commit 498a09aea97 and found that the documentation now explains the credential format.

What we checked:

- The Basic Authentication plugin page now states how stored passwords are formed: a base64 encoding of the double SHA-256 of the salt and password, followed by the base64-encoded salt. This answers the questions about how to produce the credential string and which salt is used.
- The change was made in SOLR-17724 (commit 84003aba0cb, April 2025). That issue was split from this one and is closed.

How we tried to reproduce it:

- Not applicable. This is a documentation request, and we checked the text on main.

What this means:

- The explanation the report asked for is in the guide. A worked example is not in the guide, and the note about adding users before enabling authentication is not covered.

If you still see this:

- If the guide does not explain a step you need, please reopen with the page section and the step that is missing.
