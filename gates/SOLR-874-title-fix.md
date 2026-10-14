# Gate job: SOLR-874 changelog title narrowing, re-gate at the new head

- Branch: solr-874-submit. Head: ac9ab33753751a6fef05b5f100f5c1c5683510b2 (live tip, verified by `git ls-remote origin refs/heads/solr-874-submit` on 2026-10-14; receipt `receipts/SOLR-874.md`, GATE GREEN at this head).
- Intended host: vm2 (gate backlog coordinator), under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed. Written by the review side (Windows) on 2026-10-14. Not run by the review side.
- Capability tags: gate.
- Job type: branch-side changelog title correction on a gated branch, then a re-gate at the new head. Same pattern as `gates/SOLR-12916-title-fix.md` and `gates/SOLR-10882-title-fix.md`.
- Why owed: the query-parsing census follow-up (`reports/category-ready-lists-round-1.md`, HOLD on SOLR-874, step S4) found that the changelog title says a leading NOT is dropped. The draft says the opposite, and the code agrees with the draft: at the tip, `LEADING_BOOL_PATTERN` (`solr/core/src/java/org/apache/solr/util/SolrPluginUtils.java` lines 649-650) strips a leading AND, OR, && and || only, so `NOT ipod` is kept. The trailing pattern (`DANGLING_BOOL_PATTERN`, lines 646-647) does strip a trailing NOT.
- Current title at the tip (the changelog value to replace): `dismax: a trailing or leading AND/OR/NOT/&&/|| in the user query (for example q=ipod AND) is dropped instead of causing a parse error`
- The fix: in `changelog/unreleased/SOLR-874-dismax-dangling-boolean-operator.yml` at the head above, replace the title value with exactly the text below. Keep the double quotes and the rest of the YAML shape; the parse step verifies it.

  `dismax: a trailing AND, OR, NOT, && or || and a leading AND, OR, && or || in the user query (for example q=ipod AND) is dropped instead of causing a parse error`

- No other file changes: verify with `git diff` against `ac9ab33753751a6fef05b5f100f5c1c5683510b2` that the delta is this one fragment only. Stop and record here if anything else differs.
- Proof shape: none owed. The delta touches no production or test code, so the gate evidence at `ac9ab33753751a6fef05b5f100f5c1c5683510b2` carries over the title-only commit (the SOLR-16630 precedent, as in the two title jobs above). The focused run below still re-runs at the new head and is counted from fresh JUnit XML.
- Modules: `:solr:core`. Focused classes at the new head: `org.apache.solr.util.SolrPluginUtilsTest` and `org.apache.solr.DisMaxRequestHandlerTest` (package read from the branch's test files by grep, per the FQCN rule). Seed: `874C0FFEE874` (the seed recorded in `receipts/SOLR-874.md`).
- Gate steps: changelog YAML parse of the edited fragment with a real YAML parser; tidy with the GATE INVALID guard (restore `gradle/libs.versions.toml` afterward; any tidy change to another file invalidates the delta check above); Error Prone compile for `:solr:core` with `-Pvalidation.errorprone=true`; focused tests; module check `:solr:core:check -x test`.
- Push: the title commit pushes plainly (fast-forward) to `solr-874-submit` after the gate is green at the committed tree. Commit author Nick Shanin, no trailers.
- On green: refresh `receipts/SOLR-874.md` with the new head, a line recording that the delta from `ac9ab33753751a6fef05b5f100f5c1c5683510b2` is the title correction only, and the focused counts from fresh JUnit XML. Re-point every `ac9ab33753751a6fef05b5f100f5c1c5683510b2` citation in `pr-drafts/query-parsing/SOLR-874.md` (the changelog link and the code links) to the new head. Mark this job DONE and the claim DONE.

## Draft status

- `pr-drafts/query-parsing/SOLR-874.md` is HELD on this job. Its Limits summary (S1) is applied and re-read. Its title item (S4) is branch-side and is carried here. The draft returns to SATISFIED only after the steps above are done and the review side re-reads it at the new head.

## Step checklist

- [ ] head confirmed by `git ls-remote` equals `ac9ab33753751a6fef05b5f100f5c1c5683510b2`; recorded here
- [ ] title replaced in `SOLR-874-dismax-dangling-boolean-operator.yml`; delta against `ac9ab33753751a6fef05b5f100f5c1c5683510b2` is that fragment only
- [ ] changelog YAML parse
- [ ] tidy on the final tree; tree clean (GATE INVALID guard)
- [ ] Error Prone compile (`:solr:core`)
- [ ] focused tests at the new head: SolrPluginUtilsTest and DisMaxRequestHandlerTest, counted from fresh JUnit XML, seed 874C0FFEE874
- [ ] module check (`:solr:core:check -x test`)
- [ ] pushed plainly to `solr-874-submit`; receipt refreshed; draft citations re-pointed; job and claim DONE
