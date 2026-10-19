We checked this against Solr main at commit 498a09aea97 and could not confirm the problem yet.

What we checked:

- The numeric collapse collector checks whether a document has a value before it reads the value, so a stored zero is kept as a value and not treated as missing on that path.
- Some other collector paths have not been checked line by line yet.

How we tried to reproduce it:

- Not run. The check needs a collapse on an integer field with one document at zero and one with no value.

What this means:

- The main path appears to handle zero correctly. We have not finished checking the other paths, so this report is not closed.

If you still see this:

- Please send the field type, the documents with the zero value and with no value, the collapse parameters, and the Solr version. Please reopen with that information.
