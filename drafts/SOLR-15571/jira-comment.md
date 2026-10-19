We checked this against current Solr main and could not find a bug in the export path that streaming expressions use.

What we checked:

- The export writer (ExportWriter.java, lines 501 to 539) returns fields that have DocValues. A field that is stored only is returned only when includeStoredFields is set. Other fields that match a wildcard in fl are skipped without an error.

How we tried to reproduce it:

- Not run yet. The ticket does not include the field definition or the streaming expression.

What this means:

- If the field is stored only, or indexed without DocValues, the export writer drops it from the output and does not report an error. That would explain the missing values.

If you still see this:

- Send the field definition from the schema (for example the Collaborator_ss type and its docValues and stored settings), the streaming expression with its fl value, and whether includeStoredFields was set. With those, the case can be checked on a current release.
