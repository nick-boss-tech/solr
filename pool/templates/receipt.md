# Receipt section: <TICKET> (<gate id>)

Prepend one section per gate to `receipts/<TICKET>.md`. The first lines are structured: the work graph generator (`pool/bin/build-work-graph.py`) reads them, so keep the labels and the backticks exactly.

## Gate <GATE_ID> (<host>, <YYYY-MM-DD>): <GATE GREEN, or the result>

- Branch: `<branch>`. Head: `<40-character sha>` (confirmed by ls-remote before the run). Base: `<sha>`.
- Links: Ticket or parent: <SOLR-XXXXX or parent PR/branch>. Category: <one of the 14 in pool/work-map.md>. Siblings: <branches or records on the same surface, or none known>. Supersedes: <record or head, or none>.
- Runner: <runner and host>. Log: <log path>.
- Step 0, changelog YAML parse of `<path>`: rc=<n>.
- Step 1, tidy: rc=<n>; post-tidy status <clean, or the tidy repair commit and its files>.
- Step 2, Error Prone compile (`<tasks> -Pvalidation.errorprone=true`): rc=<n>.
- Step 3, proof leg: <what was reverted, and the expected failure by content; or none requested, with the reason>.
- Step 4, focused head run: `<fully qualified class>`, seed `<seed>`: <t> tests, <f> failures, <e> errors, <s> skipped (fresh JUnit XML, copied to <path>).
- Step 5, module check `<task> -x test`: rc=<n>.

For a NO GATE receipt, the section says NO GATE in the result line, names the state instead (what is launched, owed, or held, and why), and keeps the Branch and Links lines.
