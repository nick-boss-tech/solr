# PR #5160 (SOLR-10492) Ref Guide update, round 1

- Page changed: `solr/solr-ref-guide/modules/query-guide/pages/result-grouping.adoc`.
- Documented that distributed `facet.field` refinement with `group.facet=true` returns group counts for terms omitted from a shard's initial facet response, rather than document counts.
- Verification: Asciidoctor.js 3.0.4 parsed the page with `--failure-level WARN`; exit code 0 and no warnings. The full guide build was not run on Windows.
- PR description: checked the live body before the push. It already states that refinement counts are grouped when `group.facet=true`, so no body edit was needed.
- Branch head: `6d51d59b026b0b624a56dddd4f4d529eb3ab31be`. The fork ref and PR head were verified to match. The commit changes only the Ref Guide page.
- No new code gate was run for this documentation-only assignment. The page parse verifies AsciiDoc syntax; it does not change or extend the code-gate evidence in the receipt.
