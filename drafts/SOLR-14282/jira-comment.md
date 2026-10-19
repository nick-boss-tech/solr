We checked this against Solr main at commit 498a09aea97 and found that the real-time get handler leaves out fields that are copy targets, on purpose.

What we checked:

- The real-time get code skips copyField destinations in three places. The reference guide page for real-time get does not say this.

How we tried to reproduce it:

- Not run. The behaviour is deliberate in the code, so a run would show the designed result.

What this means:

- Whether /get should return copyField destinations, or the guide should say it does not, is a decision for the Solr developers. Until then, read copied fields from a query.

If you still see this:

- If you need a copied field from a single-document lookup, please describe the use case, and it can go into that decision. Please reopen with the Solr version and your request.
