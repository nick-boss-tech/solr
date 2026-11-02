# SOLR-11668 premise (Critical batch 2, unit 11): NOT REPRODUCED

Ticket: FuzzyLookupFactory combined with DocumentDictionaryFactory
throws StackOverflowError while building the dictionary, per the
reporter on 6.2.0 and 6.6.2. Jira Open, Critical, reporter Krzysztof
Madejski, last updated 2019-06-08, no comments on the ticket. The
ticket text also mentions a different symptom for FreeTextLookupFactory
combinations ("Lookup not supported at this time"); that second
symptom was not replayed here, since it is a lookup-before-build
error, not the build-time stack overflow in the title.

Probe: scratch only, never committed, fresh worktree at batch base
83d560030fde, seed 11668C0FFEE11668, BUILD SUCCESSFUL, 2 tests,
0 failures. Evidence from fresh JUnit XML (copies
~/workspace/tools/11668-premise-TEST-Solr11668PremiseProbeTest.xml,
timestamp 2026-11-02T00:42:59Z, and
~/workspace/tools/11668-premise-TEST-Solr11668TicketProbeTest.xml,
timestamp 2026-11-02T00:43:11Z; log
~/workspace/tools/11668-premise-probe.log). Probe worktree removed.

Leg 1, the exact pair in the existing harness
(suggest_fuzzy_doc_dict in solrconfig-suggestercomponent.xml:
FuzzyLookupFactory + DocumentDictionaryFactory, field cat,
weightField price, suggestAnalyzerFieldType text, buildOnCommit
true): indexed docs, committed, explicit build, fuzzy query for
"exampel" returned numFound 2 with the expected suggestions. No
stack overflow, no error.

Leg 2, ticket-faithful variant (custom scratch config
solrconfig-11668.xml / schema-11668.xml in the probe worktree only):
same pair, no weightField, suggestAnalyzerFieldType text_auto11668
(StandardTokenizer, LowerCase, EdgeNGram 2-20), buildOnCommit true,
buildOnStartup false. The server log shows the buildOnCommit build
and the explicit build both ran (SolrSuggester.build, request
status 0), and the query returned a suggest block. No stack
overflow, no exception in either leg's XML.

Limits: the ticket's own schema and titleAutocomplete field are not
available, so leg 2 approximates them (string field cat, an
EdgeNGram autocomplete analyzer type, no weightField). The verdict
settles the title claim for this pair on current main in these two
configurations, not every analyzer or field shape the reporter may
have used.

Verdict: NOT REPRODUCED. No implementation unit follows.
