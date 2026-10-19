We checked this against Solr main at commit 498a09aea97 and could not reproduce it yet.

What we checked:

- Learning to rank rescores the top results with the model and the feature values of each document. Scores can differ between two environments if the feature store, the model, or the index contents differ.
- The report does not give the query, the feature store, the model, or the index details for each environment.

How we tried to reproduce it:

- Not attempted. A run would need the query, the model and feature definitions, and the index contents from each environment.

What this means:

- We cannot say which of those differences causes the score change from the report.

If you still see this:

- Please send the query, the feature store and model definitions, and the feature values for one document on each environment, along with the Solr version. That would let us compare them.
