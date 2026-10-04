# SOLR-14171 - hypothetical-reproduction handoff

**Read this first: the fix and test on this branch were written without being compiled or run.** The audit pipeline has no Gradle access. Treat both as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-14171 - "allTermsRequired does not work when using context filter query" (2020). The old skip note said "Lucene handles it since LUCENE-7505, no current-main repro". Reopened in audit round audit-1 (Tier 1 batch 10).
- Branch: `solr-14171-submit` off `apache/solr` main `9b3a84b1c46`

## The bug, as understood
`SuggestComponent` reads `suggest.allTermsRequired` with a hard-coded default of `true` and always puts the result into `SuggesterOptions`. `SolrSuggester.getSuggestions` uses it only on the `suggest.contextFilterQuery` path; the other path (`lookup.lookup(token, false, count)`) uses the value configured on the suggester. So a suggester configured with `allTermsRequired=false` returns partial matches without a context filter, and as soon as `suggest.cfq` is present the request default `true` overrides the config and all terms become required. This is the reporter's exact symptom.

## What the branch changes
- `SuggesterOptions.allTermsRequired` is now a nullable `Boolean` (null = request did not say).
- `SuggestComponent`: `params.getBool(SUGGEST_ALL_TERMS_REQUIRED)` without a default.
- `SolrSuggester`: reads the configured `allTermsRequired` in `init` (`Boolean.parseBoolean`, default true like Lucene) and uses it on the cfq path when the request did not pass the parameter.
- Test: `SuggestComponentContextFilterQueryTest.testContextFilterHonorsConfiguredAllTermsRequired` plus a new suggester `suggest_blended_infix_suggester_any_term` (allTermsRequired=false) in `solrconfig-suggestercomponent-context-filter-query.xml`. The test asserts partial match returns one hit with cfq=ctx1 and no request override, and that `suggest.allTermsRequired=true` still wins.

## Guesses to verify first
1. `BlendedInfixSuggester.lookup(key, BooleanQuery, num, allTermsRequired, doHighlight)` with `allTermsRequired=false` really treats `example nomatchterm` as an OR (the last term is a prefix term in infix suggesters; "nomatchterm" is expected to match nothing).
2. The result entry name in the response is the whole query string `example nomatchterm` (the XPath uses it; the existing tests only use single-token queries).
3. `defaultAllTermsRequired` ignores the existing quirk below.
4. `suggest.highlight` has the same pattern (request default `false` overriding the configured default `true`) on the cfq path. Deliberately not changed here, since it would change the output of existing unconfigured suggesters.

## Related finding, NOT fixed here
`AnalyzingInfixLookupFactory` and `BlendedInfixLookupFactory` read the `allTermsRequired` and `highlight` config with `Boolean.getBoolean(...)`, which reads a *system property* named after the value, so a configured `true` becomes `false`. `solrconfig-infixsuggesters.xml` configures `highlight=true`, so fixing this to `Boolean.parseBoolean` probably changes an existing test's expectation. Worth its own ticket.

## Verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.handler.component.SuggestComponentContextFilterQueryTest"
```
Fail-before: revert `SuggestComponent` to `params.getBool(SUGGEST_ALL_TERMS_REQUIRED, true)`; the first assertion in the new test should fail (0 hits).

## Not done
No JIRA comment, no PR.
