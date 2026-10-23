# PR #5169 (SOLR-12505) Ref Guide update, round 1

- Page changed: `solr/solr-ref-guide/modules/query-guide/pages/stream-decorator-reference.adoc`.
- Documented that `fetch` uses `defType=lucene` for batch lookups, regardless of the target `/select` handler's default parser. The page already documents `on` join-field syntax, so the update also states the rejected collection-side field-name forms: whitespace or `}` anywhere, and a leading single quote, double quote, or `$`.
- Verification: Asciidoctor.js 3.0.4 parsed the page with `--failure-level WARN`; exit code 0 and no warnings. The full guide build was not run on Windows.
- PR description: checked the live body before the push. It already describes the parser override and join-field validation, so no body edit was needed.
- Branch head: `f562683374604c312f2b688662a85736b7eed67f`. The fork ref and PR head were verified to match. The commit changes only the Ref Guide page.
- No new code gate was run for this documentation-only assignment. The page parse verifies AsciiDoc syntax; it does not change or extend the code-gate evidence in the receipt.
