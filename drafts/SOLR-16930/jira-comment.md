We checked Solr main at commit 498a09aea9 (October 7, 2026). Solr looks up analysis components (tokenizers, token filters, and character filters) by their short names through the Lucene library. The lookup that accepts a component of another kind is in Lucene, and we did not check its code in this pass.

What we checked:

- How Solr's schema loader resolves the component names in a field type definition.
- The comment on this ticket, which lists names in Lucene that are shared by more than one class.

How we tried to reproduce it:

- We did not run a reproduction for this check.

What this means:

- Whether a schema should be allowed to name a component of another kind is a design choice. It is not settled by the code or the documentation, so we are leaving the ticket open for a decision on the intended behavior.

If you still see this:

- Please tell us whether you expect the schema to be rejected, or accepted with a warning, when a component is named with the class name of another kind. With that, we can make the change or close the report.
