# Claim: SOLR-11668 premise (Critical batch 2, unit 11)

Host: phone agent. Run: board-work-2026-11-01, 2026-11-01 ~17:36 MST.
Unit: one premise run for SOLR-11668 (FuzzyLookupFactory combined
with DocumentDictionaryFactory throws StackOverflowError while
building the dictionary, per the ticket on 6.2.0 and 6.6.2; Jira
Open, Critical, reporter Krzysztof Madejski, last updated
2019-06-08; no comments on the ticket).
Plan: fresh worktree on batch base 83d560030fde, scratch probe only
(never committed): two legs in one focused run. Leg 1, the exact
lookup+dictionary pair in the existing harness (suggest_fuzzy_doc_dict
in solrconfig-suggestercomponent.xml: FuzzyLookupFactory +
DocumentDictionaryFactory, buildOnCommit true): index docs, commit,
explicit build, fuzzy query. Leg 2, ticket-faithful variant: same
pair, no weightField, suggestAnalyzerFieldType an EdgeNGram
autocomplete type (text_auto11668), buildOnCommit true,
buildOnStartup false. Settle whether the premise reproduces on
current main, and record the limits (the ticket's own schema and
titleAutocomplete field are not available, so the variant
approximates them).
Report to reports/critical-impl-11668-premise-batch-2.md. No PR, no
Jira/GitHub post.

Status: CLAIMED.
