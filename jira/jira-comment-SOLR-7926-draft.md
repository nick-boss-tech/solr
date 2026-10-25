# Comment draft: SOLR-7926

- Status: DRAFT (not posted). Posting needs the owner's go.
- Shape: stale-check status note
- Addressee: [~mikemccand] — the Lucene maintainer who discussed the EdgeNGram token-graph behavior in the ticket.

[~mikemccand], I re-read the history: the original report is that `EdgeNGramFilterFactory` highlights the whole word, while later comments describe a different failure from a `NGramFilterFactory`/FastVectorHighlighter workaround, filed as [LUCENE-7016|https://issues.apache.org/jira/browse/LUCENE-7016]. LUCENE-7016 remains open, but it does not by itself establish that the original EdgeNGram highlighting report is a duplicate or still present. I have not run either case on current Solr/Lucene.

Is the original EdgeNGram highlighting behavior still seen on a supported release, and if so can the ticket specify the current field type, highlighter, and minimal query/document?

 * Keep SOLR-7926 open for a current reproduction of the original EdgeNGram symptom.
 * Close it as stale if that original symptom no longer occurs on supported versions.
 * Keep the separate FastVectorHighlighter exception with LUCENE-7016 rather than treating that link as a fix here.

I recommend keeping this reopened ticket focused on its original symptom until that check is made; the later workaround exception is separate evidence, not a duplicate resolution.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
