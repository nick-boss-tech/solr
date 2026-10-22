# Gate job: SOLR-16914 owner-items round 1 (item 7, reporter configuration probe)

Status: DONE (probe passes; docs are the resolution, pending the owner's public ticket reply)

- Branch `solr-16914-submit`, head `cd878023d3` (docs only; no branch change). Probe is not committed to the branch: it is a probe, not a shipped test.
- Host: vm5. Log: `/workspace/gates/logs/vm5/solr-16914-probe/`. Probe files: `reports/probe-16914/` (test, schema, synonym sample).

## Probe

- Index and query analyzers copied from the ticket's `text_ja` type (JapaneseTokenizer mode=search, BaseForm, POS stop, CJKWidth, stop, SynonymGraph expand=true, Flatten, KatakanaStem, LowerCase, WordDelimiterGraph, EdgeNGram on index only).
- Probe-only resources: `lang/stoptags_ja.txt`, `lang/stopwords_ja.txt` (minimal), `lang/synonyms_ja.txt` with `東京,とうきょう` and `ビール,beer`.
- Gate: `:solr:core:compileTestJava` rc=0; `TestJapaneseSynonymProbe16914`: 1 test, 0 failures. Queries `東京`, `とうきょう`, `beer`, `ビール` each return the expected document.

## Limits

- The reporter's synonym file is an attachment on the JIRA ticket and is not available to this host. The probe uses two entries of its own. A pass with this sample does not prove the reporter's file works. It shows the analyzer chain itself expands synonyms at index and query time.

## Result

Probe passes. The ticket's configuration is not reproduced with this sample, so the docs change is the resolution, as the decision row says. Whether to tell the reporter is a public JIRA comment: owner action (Nick). Nothing was posted.
