DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Addressee: the ticket's reporter, Jan Høydahl (Jira user janhoy). Evidence: reports/solr-18503-facet-keys-audit-round-1.md.

----

[~janhoy] I checked each of the seven keys against apache/solr main at {{13c693d805f}}. The parser reads all seven, and the JSON Facet pages of the Ref Guide do not document them. The only {{perSeg}} hit in the guide is an unrelated spatial cache name.

 * {{processEmpty}}: read in {{FacetParser.parseSubs}}, solr/core/src/java/org/apache/solr/search/facet/FacetParser.java:73.
 * {{cacheDf}}: read in the terms parser, FacetParser.java:564. {{-1}} disables the filter cache, as in FacetFieldProcessorByEnumTermsStream.java:175.
 * {{perSeg}}: read at FacetParser.java:569. FacetField.java:38 calls it experimental, for testing.
 * {{f}}: read by {{getField}}, FacetParser.java:289, for terms and heatmap facets. Range facets read only {{field}}, FacetRangeParser.java:42.
 * {{excludeTags}} on a facet: read in {{parseCommonParams}}, FacetParser.java:209, and applied to the domain. The guide documents only the {{domain.excludeTags}} form.
 * {{count}} as a stat function: registered as {{agg_count}} in ValueSourceParser.java:1144. The stat function table lists {{countvals}} and no {{count}}.
 * {{query}} in a {{type: query}} facet: read after {{q}}, FacetParser.java:508.

I read these from the code. I did not run Solr, so the parse results are from code reading only.

Would you document {{processEmpty}}, {{cacheDf}}, {{count}} and facet-level {{excludeTags}} in one change, and leave the {{f}} and {{query}} aliases for a separate decision?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
