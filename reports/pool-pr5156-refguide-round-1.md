# PR #5156 (SOLR-9396) Ref Guide update, round 1

- Page changed: `solr/solr-ref-guide/modules/query-guide/pages/document-transformers.adoc`.
- Documented that fields referenced as `row.__fieldname__` inputs in subquery parameters are requested automatically. They do not need to be included in the main `fl` just to provide subquery input; include them there when they should also appear in the parent result. Kept the existing `fl` requirements for fields returned in subquery documents.
- Verification: Asciidoctor.js 3.0.4 parsed the page with `--failure-level WARN`; exit code 0 and no warnings. The full guide build was not run on Windows.
- PR description: checked the live body before the push. It already states that referenced fields are requested automatically and are not added to the response, so no body edit was needed.
- Branch head: `ef437b5feafe4e9607c5b4729de70f68405aa3b1`. The fork ref and PR head were verified to match. The commit changes only the Ref Guide page.
- No new code gate was run for this documentation-only assignment. The page parse verifies AsciiDoc syntax; it does not change or extend the code-gate evidence in the receipt.
