# solr-17356-submit

- Branch: origin/solr-17356-submit
- Head: ea7fc15cade4 (reviewed and unchanged; no patch)
- Base: upstream/main (merge-base b6b2b8f10e9, 18 commits behind)
- Scope: 3 files, +35/-4. `solr/solr-ref-guide/modules/indexing-guide/pages/language-analysis.adoc` (Ukrainian section: module jar name, dictionary paragraph, both example `dictionary` attributes), `changelog/unreleased/SOLR-17356-ukrainian-dictionary-docs.yml`, `SOLR-17356-TESTING.md` (author's unbuilt note, left in place). Docs only.
- Verdict: Needs work (the docs example points at a dictionary the default build does not load; the fix is an owner call, see HIGH)
- Reviewer: review-agent A1, 2026-10-08

## Findings (ranked)

HIGH (posed, not patched; owner call): the documented dictionary is not the one Solr's default analysis-extras classpath provides. The page says the dictionary comes from `ua.net.nlp:morfologik-ukrainian-lt` at `org/languagetool/resource/uk/ukrainian.dict`, which the user must add to `analysis-extras`. In fact:
- `lucene-analysis-morfologik-10.4.0.pom` declares a runtime dependency on `ua.net.nlp:morfologik-ukrainian-search:4.9.1`. Solr's `analysis-extras` has `runtimeOnly` on that Lucene artifact, and `analysis-extras/gradle.lockfile` puts `morfologik-ukrainian-search:4.9.1` on `runtimeClasspath` and `runtimeLibs`. So a Ukrainian dictionary is on the default classpath (verified).
- That jar contains `ua/net/nlp/ukrainian.dict` and `ua/net/nlp/ukrainian.info`, and no `org/languagetool/...` path (verified by listing entries).
- Lucene's `UkrainianMorfologikAnalyzer.class` references `ua/net/nlp` and `ukrainian.dict`, and does not reference `languagetool` (verified by searching the class bytes). So Lucene's own default is the `ua/net/nlp` dictionary.
So the two `dictionary="org/languagetool/resource/uk/ukrainian.dict"` examples fail on a default install unless the user also adds the `-lt` jar. The TESTING premise ("nothing ... provides the dictionary, so the documented path cannot resolve out of the box") is wrong about the classpath. The options for the owner: (a) document `ua/net/nlp/ukrainian.dict`, which needs no new dependency and matches Lucene's default; (b) keep `morfologik-ukrainian-lt` and add it to `analysis-extras` as the ticket's parenthetical suggests, which needs the licence and size review and would put two Ukrainian dictionaries on the classpath. Not patched, because the choice is a design call.

verified (checked against the artifacts):
- The Lucene jar's dictionary claim holds: `lucene-analysis-morfologik-10.4.0.jar` has no `ukrainian.dict` under `org/apache/lucene/analysis/uk/`. The "no longer part of the Lucene Morfologik jar" sentence is accurate for 10.4.0.
- The stopword path `org/apache/lucene/analysis/uk/stopwords.txt` is still in the Lucene jar, so leaving it unchanged (TESTING guess 1) is correct.
- The module jar name `lucene-analysis-morfologik` matches the Gradle catalog entry (`gradle/libs.versions.toml`).

LOW (not verified here): the `ua.net.nlp:morfologik-ukrainian-lt` artifact, its `org/languagetool/resource/uk/ukrainian.dict` path, and the LUCENE-7785 reference. The `-lt` jar is not in the local Gradle cache, and there is no network access from this review.

LOW: the changelog title is one long sentence. Style only.

## Not checked
- Nothing built or rendered. The Antora build was not run.
- `morfologik-ukrainian-lt` contents (not cached; see LOW).
- Whether the analysis-extras README (`solr/modules/analysis-extras/README.md`, which says "lib/morfologik-*.jar") needs the same correction. Noted, not checked in full.
