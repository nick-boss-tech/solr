# SOLR-18500: curl URL-quoting audit

## Finding

The current Ref Guide still mixes single-quoted, double-quoted, and unquoted URL arguments. In the tracked AsciiDoc guide at Apache Solr `main` commit [`13c693d805f6541f7cec684cd27d28eef7b3b614`](https://github.com/apache/solr/tree/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/solr-ref-guide), my source census found **476 curl command examples**:

| URL argument form | Count |
|---|---:|
| Single-quoted literal URL | 169 |
| Double-quoted literal URL | 94 |
| Unquoted literal URL | 202 |
| Non-literal or other | 11 |
| **Total** | **476** |

The 11 other cases comprise two `$SOLR_URL` expansions, five abbreviated `...` URLs, one SSL example with an ellipsis instead of a URL, and three config-set examples where the command and URL are on separate lines without a shell continuation. I classified those three under strict shell syntax as incomplete/other. If the indented URL lines are intended to continue the commands, the alternative tally is **169 single / 97 double / 202 unquoted / 8 other**, still 476 total.

## Examples in current main

- **Single quotes:** [`cluster-plugins.adoc:95`](https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/solr-ref-guide/modules/configuration-guide/pages/cluster-plugins.adoc#L95) and [`config-api.adoc:331`](https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/solr-ref-guide/modules/configuration-guide/pages/config-api.adoc#L331).
- **Double quotes:** [`configsets-api.adoc:122`](https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/solr-ref-guide/modules/configuration-guide/pages/configsets-api.adoc#L122) and [`managed-resources.adoc:82`](https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/solr-ref-guide/modules/configuration-guide/pages/managed-resources.adoc#L82).
- **No quotes:** [`collections-api.adoc:118`](https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/solr-ref-guide/modules/configuration-guide/pages/collections-api.adoc#L118) and [`config-api.adoc:353`](https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/solr-ref-guide/modules/configuration-guide/pages/config-api.adoc#L353). [`config-sets.adoc:102`](https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/solr-ref-guide/modules/configuration-guide/pages/config-sets.adoc#L102) is an unquoted URL with several `&` parameters, a concrete shell-sensitive example.
- **Other:** [`schema-designer.adoc:115`](https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/solr-ref-guide/modules/indexing-guide/pages/schema-designer.adoc#L115) uses `$SOLR_URL`; [`pagination-of-results.adoc:165`](https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/solr-ref-guide/modules/query-guide/pages/pagination-of-results.adoc#L165) shows an abbreviated URL.

## Count notes and live status

I counted command examples across the tracked AsciiDoc source, including source blocks, tabbed examples, multiline bodies, and inline command examples; prose mentions and `curl::` tab labels were excluded. The current-main count is not the same as the figures in the active [PR #5120](https://github.com/apache/solr/pull/5120). Its description reports **510 total** commands, with **161** single-quoted, **89** double-quoted, and **190** unquoted URLs. Those three quoted-form counts sum to 440, leaving 70 commands outside the stated forms; I could not reconcile that published summary with the 476-example census at current main, so I do not present the PR figures as a current-main count. The PR description also says 11 of its unquoted examples are not copyable as written because of `&` or `<signature>`; this audit did not execute the examples.

The live [SOLR-18500](https://issues.apache.org/jira/browse/SOLR-18500) record remains Open, unassigned, and has no Jira comments. The linked [PR #5120](https://github.com/apache/solr/pull/5120) is open and unmerged. Its live discussion already asks whether simple literal URLs should also be single-quoted; Nick's recent PR comment supports that convention and notes remaining cleanup. The evidence supports a consistent convention of single-quoting literal URLs, while preserving deliberate shell-variable expansion. A short Jira draft is included separately and remains unposted for Nick's per-text approval; it may be redundant with the active PR discussion.
