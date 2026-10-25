# Comment draft: SOLR-5821

- Status: DRAFT (not posted). Posting needs the owner's go.
- Shape: staged ask (last activity is a committer's)
- Addressee: [~erickerickson] — the ticket's final maintainer commenter and decision-maker on the ordering-versus-count distinction.

[~erickerickson], I re-read the reopened report and the eight comments. The record distinguishes different tie ordering from a different `numFound`: your final comment says differing counts after indexing stops and all replicas have committed/opened searchers would be a bug. I have a documentation-only clarification for the tied-sort case, but it does not fix or test the reopened count-divergence case; I have not run a current-main multi-replica reproduction.

Is the ticket's distinction enough to keep the investigation focused on persistent `numFound` divergence rather than expected tie-order variation?

 * Close only if the reported problem was tie-order variation and the count-divergence claim is explicitly corrected.
 * Keep open for a current cluster reproduction showing different `numFound` after all replicas expose the same committed corpus.
 * Track the tied-sort documentation separately; it does not resolve a persistent count mismatch.

I recommend keeping the bug open for a current-main cluster check of the count case. The existing documentation change describes ordering behavior only and is not a fix for the reopened report.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
