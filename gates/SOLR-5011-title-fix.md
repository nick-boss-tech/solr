# Gate job: SOLR-5011 changelog title correction, re-gate at the new head

- Branch: solr-5011-submit. Fork tip: 00060d2ed92bc6414fb420d7c52558c14c49f75a (the narrowing, GATE GREEN 2026-10-13, receipts/SOLR-5011.md). Merge-base: cabedd1d968059215188f4e7563fb303241899ed.
- Why: the fragment title changelog/unreleased/SOLR-5011-close-core-resource-loader.yml says a SolrCore now closes its SolrResourceLoader on every close. After the narrowing, with shareSchema on, the loader is deliberately left open (SolrCore.doClose skips the close when the node uses a schema cache). The receipt names this title correction as owed before the next re-gate.
- Intended host: any capable Linux host (vm2 or vm1), under the WORKFLOW.md claim rules. Windows never claims build work.
- Capability tags: implementation, gate. One claim covers the title commit, the gate and the push.
- Implementation (one commit, changelog only): change the title line to "A SolrCore now closes its SolrResourceLoader when the core is closed (unload, reload, shutdown), releasing the class loader and the jars of the core's lib directories; with shareSchema on, the loader stays open because the shared schema may still use it". Keep the type, authors and links unchanged. No plain em dash in the title.
- Module: :solr:core. Gate: docs-shaped, no focused Java test, since the change touches no code and the narrowed code is gated by its own proving test at 00060d2ed9.
- Gate steps: changelog YAML parse of the fragment; changelog lint (logchangeLint); tidy with a clean tree afterward (restore gradle/libs.versions.toml if touched); module check `:solr:core:check -x test`.
- Push: after the gate is green at the committed head, a plain fast-forward push to solr-5011-submit (verify by ls-remote).
- On green: refresh receipts/SOLR-5011.md with a title-correction section (new head, steps, the title named); mark this job DONE and the claim DONE. Check the draft pr-drafts/core-admin/SOLR-5011.md for the old title wording and name it in the receipt if present.
- On failure: record the failing step and message in this file; do not relaunch unchanged.

## Step checklist

- [ ] title commit on solr-5011-submit (local, fast-forward from 00060d2ed9)
- [ ] changelog parse and lint
- [ ] tidy on a clean tree
- [ ] module check :solr:core:check -x test
- [ ] push fast-forward to solr-5011-submit, verified by ls-remote
- [ ] receipt refreshed; job and claim DONE
