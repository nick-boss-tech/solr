We checked this against Solr main at commit 498a09aea97 and found that the general multiterm analyzer is now documented, but the Schema API documentation is not.

What we checked:

- The analyzers guide describes a multiterm analyzer and gives an XML example.
- The Schema API page does not mention multiterm analyzers, so the JSON form and its property names are not documented there.

How we tried to reproduce it:

- Not applicable. This is a documentation request, and we checked the text on main.

What this means:

- The Schema API part of this request is still open. The note about the analysis/field API has not been located yet.

If you still see this:

- If the guide does not show how to add a multiterm analyzer through the Schema API, please reopen with the page section you looked at.
