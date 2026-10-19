We could not reproduce this on current Solr main.

What we checked:

- We indexed a multi-valued string field with stored values and doc values, with values such as a single space, two spaces, and text in angle brackets. We exported it with /export through a streaming expression, after several commits, a re-index, a delete and a merge. The exported values matched the stored values for every document.
- The export writer returns the stored bytes for these values, so it does not turn them into empty strings. Fields without doc values are left out of /export output unless includeStoredFields is set (see the exporting result sets page in the reference guide). That can drop a field, but it does not change its values.

What this means:

- The empty strings in the report are not explained by the current code. The report was made on Solr 8.7, and the export code was rewritten after that release, so the older behavior may have differed.

If you still see this:

- Please send the field definition for Collaborator_ss from schema.xml (the type, and whether it is indexed, stored and docValues), the Solr version, and one streaming expression with its output for a document that shows the empty values. With those we can check the case on a current release.
