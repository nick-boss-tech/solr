# Comment draft: SOLR-7351

- Status: DRAFT (not posted). Posting needs the owner's go.
- Shape: stale-check status note
- Addressee: [~erickerickson] — the maintainer who discussed whether MLT should support numeric/date fields or reject them clearly.

[~erickerickson], the 2015 report says `mlt.fl=testDate_dt` on `TrieDateField` fails with `Invalid Date String:'-670816800000'`; the later comment asks whether MoreLikeThis should handle numeric fields, and you suggested returning a clearer unsupported-field error. On current main, the parser supplies selected fields to Lucene MoreLikeThis with Solr's index analyzer, while `DatePointField` stores the date as a numeric long ([parser path|https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/core/src/java/org/apache/solr/search/mlt/AbstractMLTQParser.java#L104-L132], [date storage/conversion|https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/core/src/java/org/apache/solr/schema/DatePointField.java#L192-L229]). That is source-level evidence only: I did not run a current-main reproduction, and the earlier repro source is not available in this workspace.

Is this still seen on a supported Solr release, and if so can the report include the exact release, schema, request, and response?

 * Keep it open for a current reproduction, then decide whether dates should be supported or rejected with a clear message.
 * Close as stale only if the behavior is no longer seen on supported releases.

I recommend a current-main reproduction before either closing the ticket or choosing a fix; the related SOLR-4860 history does not by itself settle this date-field report.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
