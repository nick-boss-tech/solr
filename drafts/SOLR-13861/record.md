# Skip record: SOLR-13861

- Ticket: SOLR-13861, SynonymGraphFilterFactory - with pattern tokenizer - not able to start. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-10-23, updated 2020-01-21, no fix versions, no links, no comments.
- Verdict: keep-open.
- Draft branch: solr-13861-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Grep and code read run at this head (detached worktree).
- Source: remediation redo of batch 05, item 15, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (vm2, from the C reading) cited the Lucene-sources removal; that is confirmed here.

## Ticket reading

A schema whose index and query analyzers use SimplePatternTokenizerFactory with a `pattern` argument, and whose synonym filter sets `tokenizerFactory` and `tokenizerFactory.pattern`, fails to start with "Configuration Error: missing parameter 'pattern'". The reporter traced it to a method that runs more than once and loses the tokenizer pattern argument.

## Grep and code reading at the pinned head

- `git merge-base --is-ancestor 168df90d79 498a09aea97aac86c2cc7ac114113b946d249942` succeeds; 168df90d79 is "SOLR-14497: remove lucene sources from main". The Lucene analysis factories are no longer in this repo.
- gradle/libs.versions.toml:39: `apache-lucene = "10.4.0"`. SynonymGraphFilterFactory comes from that dependency.
- `git grep -l "SynonymGraphFilterFactory" 498a09aea97aac86c2cc7ac114113b946d249942 -- 'solr/core/src/java/*'` returns one file: solr/core/src/java/org/apache/solr/rest/schema/analysis/ManagedSynonymGraphFilterFactory.java. It imports Lucene's SynonymGraphFilterFactory (:32) and builds one as `delegate` (:428-429), passing every init arg through `filtArgs` (:422-425). That is the REST-managed synonym path, which the reporter's plain configuration does not use.
- solr/core/src/test-files/solr/collection1/conf/schema-synonym-tokenizer.xml exists at the pin; the earlier note that it uses StandardTokenizerFactory was not re-read in this pass.

## History

- 168df90d79 (SOLR-14497) removed the Lucene sources; the failing parser is in the Lucene jar, not in this tree.

## Reproduction attempts

Not run. A reproduction loads a schema with a plain SynonymGraphFilterFactory whose tokenizerFactory and tokenizerFactory.pattern arguments are set, on the pinned head, and checks whether the core starts. This pass did not start a core.

## Gap analysis

The failing code is in Lucene 10.4.0 (the factory and its tokenizer loading), and the Solr side only passes arguments through. Whether the 10.4.0 factory still loses the pattern argument is not shown without the Lucene source or a run.

## Verdict justification

keep-open: the attempts made are the grep and the code reads above at the pinned head, the Jira read, and the ancestry check. The missing piece is a run on the pin: a core with the reporter's analyzer chain (plain SynonymGraphFilterFactory with tokenizerFactory and tokenizerFactory.pattern), showing whether it starts; if it fails, the defect is in Lucene 10.4.0 and needs a Lucene issue. close-fixed is not supported without that run, and close-obsolete is not supported because the factory is still used.

Missing for a closing verdict: the start result of the reporter's schema on the pin, or the Lucene source check.

## Limits

This record does not start a core. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
