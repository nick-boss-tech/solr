We checked this against Solr main at commit 498a09aea97 and found that the reference guide describes how to limit the child documents returned with a parent.

What we checked:

- The child document transformer has a childFilter option. It takes a query that selects which child documents are returned with each parent. Its default is to return all children, which is why each parent comes back with all of its children.
- The guide page for document transformers gives the example q=book_title:Solr&fl=id,[child childFilter=doc_type:chapter limit=100].

How we tried to reproduce it:

- Not applicable. This is a usage question, answered by the documented option.

What this means:

- To return only the children that match your query, add a childFilter that selects them. The default returns every child.

If you still see this:

- If the childFilter does not give the result you need, please send the query, the document structure, and the Solr version, and we will check it. Please reopen with that information.
