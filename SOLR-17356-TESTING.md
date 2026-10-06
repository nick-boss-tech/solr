# SOLR-17356 - hypothetical reproduction (not run)

Nothing here was built or checked. Docs-only change guessed from reading `upstream/main`.

## JIRA context
Thomas Mortagne: the Ukrainian section says the `lucene-analyzers-morfologik` jar contains
`org/apache/lucene/analysis/uk/ukrainian.dict`; since LUCENE-7785 the dictionary lives in the external
`ua.net.nlp:morfologik-ukrainian-lt` library at `org/languagetool/resource/uk/ukrainian.dict`.

## What main shows
`solr/modules/analysis-extras/build.gradle` only has `runtimeOnly libs.apache.lucene.analysis.morfologik`; nothing in `gradle/libs.versions.toml`
mentions the Ukrainian dictionary jar, so the documented path cannot resolve out of the box.

## Change
`language-analysis.adoc` Ukrainian section: module jar name corrected to `lucene-analysis-morfologik`, the dictionary
dependency and path described, both example `dictionary` attributes use the languagetool path.

## Guessed / verify first
- That the stopword resource `org/apache/lucene/analysis/uk/stopwords.txt` is still in the Lucene jar (left unchanged).
- Whether the project would rather add the dictionary dependency to `analysis-extras` (the ticket's parenthetical suggestion; license/size review needed). Docs only here.
- Antora build not run.
