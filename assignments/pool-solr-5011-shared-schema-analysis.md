# Pool assignment: SOLR-5011 shared-schema loader analysis

Status: DONE

Capability tags: `review`. Intended host: windows or vm3. Staffing: 1. Claim path: claims/pool-solr-5011-shared-schema-analysis.md.

## Question

SOLR-5011 is gated green at `3fd85bedef646b5b73c26e26a86a6130b5fc209f` on branch `solr-5011-submit`. The branch makes `SolrCore.doClose()` close the core's resource loader and adds an observable closed state to `SolrResourceLoader`.

The gate carries an open finding: no branch test covers two cores built on one config set with `shareSchema` on, with one core unloaded while the other remains live and later loads a lazy lib class. `gates/SOLR-5011.md`, `receipts/SOLR-5011.md`, `reports/core-admin-round-1-k3.md` NOTE 12, and the SOLR-5011 entry in `material/core-admin-round-1-answers.md` are the starting record.

Answer this question by reading code only: can closing the first core's loader break the second live core because the two cores share a schema, a loader, or a resource reached through either one?

## Required trace

Read the branch diff at the gated head, then trace the relevant code in that same tree. Cite files and line numbers for each mechanic. Cover at least:

1. What `SolrCore.doClose()` closes, in what order, and what `SolrResourceLoader.close()` actually closes or makes unusable. Separate an observable closed flag from effects on the class loader, cached classes or resources, and later lookups.
2. How each core obtains its `SolrResourceLoader` during creation and reload. Establish whether two cores can hold the same loader instance, or whether any sharing is narrower than a shared loader.
3. How `shareSchema` changes schema creation and caching in `ConfigSetService`, what `IndexSchema` retains, and which loader a cached schema carries. Identify which core's loader that is and how long the cached schema can outlive that core.
4. Which later operations by the surviving core can reach the retained schema loader or the surviving core's own loader. Look specifically for lazy lib or plugin loading after the first core has unloaded, rather than assuming that a schema reference alone proves a live use.
5. The lifecycle and reference-count conditions under which the first core reaches `doClose()`, and whether a live second core, a cached schema, or a config set service holds anything that delays or prevents the first loader's close.
6. Reload and unload ordering where it changes the answer, including whether the surviving core can create a fresh schema or loader after the first close.

## Verdict and recommendation

Put the verdict first in the report: UNSAFE, SAFE, or NOT SETTLED BY READING.

- If UNSAFE, identify the exact failure path from the first core's close to a concrete later operation by the second core. Recommend the smallest narrowing that removes that path while preserving SOLR-5011's leak fix, and specify the test that would prove it: setup, `shareSchema` state, core unload order, the lazy load attempted afterward, and the expected result on the narrowed branch.
- If SAFE, show why each plausible sharing path cannot produce a use after close. Distinguish separate loader instances, a retained schema that no longer uses its construction loader for the operation in question, resources that remain valid after close, and lifecycle ordering that prevents overlap.
- If NOT SETTLED BY READING, name the smallest runtime check or test that would settle the exact unresolved mechanic. Do not turn uncertainty into either a safety claim or a defect claim.

## Deliverable and limits

Deliverable: `reports/solr-5011-shared-schema-analysis.md`, with the verdict up front, the sharing mechanics supported by file and line citations at the gated head, the recommended narrowing or settling test where applicable, and a short list of paths checked that did not change the verdict.

This assignment is read-only apart from the claim and the report. Do not edit the branch, write a test, run a build or test, or open a follow-up gate job. Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
