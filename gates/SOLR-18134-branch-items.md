# Gate job: SOLR-18134 branch-side items SCH-R1-34, SCH-R1-35 and SCH-R1-36, re-gate at the new head

- Branch: solr-18134-submit. Head: c5a0bdb21e862ec403b37c51c39070d952b972d8 (live tip, verified by ls-remote at posting, 2026-10-11; GATE GREEN at this head per receipts/SOLR-18134.md). Merge-base with main: 14c7aac0d151402b00259e2fb9bf5eed7049ec5d.
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed.
- Capability tags: gate.
- Job type: changelog title correction, two upgrade-note sentence corrections, one guide sentence correction, and a test comment correction on a gated branch, then a re-gate at the new head. Claimable end to end by vm1 or vm2 under the lane coordinators' authorization.
- Spec: reports/schema-convergence-round-1-response.md, items SCH-R1-34, SCH-R1-35 and SCH-R1-36 (all held branch-side there, replacements recorded verbatim; register text in reports/schema-convergence-round-1.md lines 74, 76 and 78).
- The fixes (anchor every edit on the quoted current text in the file at claim time, not on line numbers alone):
  1. SCH-R1-34: in `changelog/unreleased/SOLR-18134.yml`, the title now reads "Add solr.ZeroPositionIncrementFilterFactory to restore ancestor_path matching after Lucene 10 sequential PathHierarchy tokens." Replace the title with exactly: "Add solr.ZeroPositionIncrementFilterFactory so ancestor_path phrase queries match ancestor paths after Lucene 10 made PathHierarchyTokenizer emit sequential tokens."
  2. SCH-R1-35, upgrade notes, in `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc`, PathHierarchyTokenizer Behavior Change passage: the sentence "The field query parser and other parsers then build a phrase from those query tokens." becomes "The field query parser, and quoted text, build a phrase from those query tokens. The standard parser does not build a phrase for unquoted text on schema version 1.4 and later." and the sentence "That configuration produces descendant matching, not ancestor matching." becomes "On the phrase path (the field query parser and quoted text), that configuration matches descendant paths, not ancestors."
  3. SCH-R1-35, guide, in `solr/solr-ref-guide/modules/indexing-guide/pages/filters.adoc`: the clause "so a query-time phrase over the prefixes cannot match a path indexed as a single keyword" becomes "so a phrase over the prefixes, as the field query parser builds it, cannot match a path indexed as a single keyword".
  4. SCH-R1-36: in `solr/core/src/test/org/apache/solr/analysis/PathHierarchyTokenizerFactoryTest.java`, the comment "    // A short path must not match longer descendants (the both-sides Lucene 10 config would)." becomes "    // A short path must not match longer descendants." and the blank line immediately before that comment is deleted.
  No other file changes: verify with `git diff` against c5a0bdb21e86 that the delta is these four files only, and stop and record here if anything else differs.
- Proof shape: none owed. The delta is text only (a fragment title, documentation sentences, a test comment); no behavior changes, so the gate evidence at c5a0bdb21e86 carries over the text-only commits (the SOLR-16630 precedent). The focused runs below still re-run at the new head and are counted from fresh JUnit XML.
- Modules: :solr:core. Focused classes at the new head: org.apache.solr.analysis.TestZeroPositionIncrementFilterFactory and org.apache.solr.analysis.PathHierarchyTokenizerFactoryTest (packages read from the branch's test files; the receipt's counts at the gated head are 5 of 5 and 2 of 2, seed 18134C0FFEE18134).
- Gate steps: changelog YAML parse of the edited fragment with a real YAML parser; tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward; re-run tidy on the final tree after the last edit, before committing); Error Prone compile for :solr:core with `-Pvalidation.errorprone=true`; focused tests; module check `:solr:core:check -x test`.
- Push: the commits push plainly (fast-forward) to solr-18134-submit after the gate is green at the committed tree.
- On green: refresh receipts/SOLR-18134.md with the new head and a line recording that the delta from c5a0bdb21e86 is the SCH-R1-34 title, the SCH-R1-35 note and guide sentences, and the SCH-R1-36 comment only, with the focused counts from fresh JUnit XML. Re-point the changelog link in pr-drafts/schema-analysis/SOLR-18134.md to the new head. Mark this job DONE and the claim DONE.

## Step checklist

- [ ] fragment title replaced verbatim (SCH-R1-34); upgrade-note and filters.adoc sentences replaced verbatim (SCH-R1-35); test comment replaced and its preceding blank line deleted (SCH-R1-36); delta against c5a0bdb21e86 is those four files only
- [ ] changelog YAML parse
- [ ] tidy on the final tree; tree clean (GATE INVALID guard)
- [ ] Error Prone compile (:solr:core)
- [ ] focused tests at the new head: TestZeroPositionIncrementFilterFactory and PathHierarchyTokenizerFactoryTest, counted from fresh JUnit XML
- [ ] module check (:solr:core)
- [ ] pushed plainly to solr-18134-submit; receipt refreshed; draft changelog link re-pointed; job and claim DONE
