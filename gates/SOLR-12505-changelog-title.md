# Gate job: SOLR-12505 changelog title, name the join field check (title-only re-gate)

- Branch: solr-12505-submit. Head: 1a1875d95f3680c20447006efc3c949c57f5135f (live tip, verified by `git ls-remote origin refs/heads/solr-12505-submit` on 2026-10-13; GATE GREEN at this head per `receipts/SOLR-12505.md`, section "Gate at 1a1875d95f").
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
Status: DONE
- Capability tags: gate.
- Job type: branch-side changelog title correction on a gated branch, then a re-gate at the new head. Same shape as `gates/SOLR-10882-title-fix.md`.
- Spec: `reports/streaming-convergence-round-2-response.md`, SOLR-12505 line. The current title describes only the defType change. The branch now also rejects a join field name that would break the batch query, and the receipt records that the changelog "does not yet name the join field check".
- Owner items that hold this job:
  1. The Choice in `pr-drafts/streaming/SOLR-12505.md` ("defType versus the terms parser"). The title's first clause names `defType=lucene`. If the owner rules for the terms parser, the code changes, this title changes, and this job is replaced by a code gate. Do not run this job before the ruling.
  2. The title wording below. It is proposed here, for the owner to approve before the job opens.
  The Unicode whitespace decision (widen the check with `\p{javaWhitespace}`, or keep "ASCII whitespace") is also the owner's. Widening is a code change and needs its own gate. The title below reads true either way, so it does not wait on that decision, but a widening would supersede this job.
- Modules: `:solr:solrj-streaming`.
- Focused class at the new head (package read from the branch's test path): `org.apache.solr.client.solrj.io.stream.StreamDecoratorTest`. Seed: `12505C0FFEE12505`.
- The fix: in `changelog/unreleased/SOLR-12505-fetch-stream-deftype.yml` at the head above, replace the title block with exactly the following (block scalar form, as the fragment already has it):

  "The fetch() streaming expression now sends defType=lucene with its batch query, so it no longer fails to enrich tuples when the target request handler defaults to a different query parser, and it rejects a join field name that would break that query."

  No other file changes. Verify with `git diff --stat 1a1875d95f` that the delta is this fragment only, and stop and record here if anything else differs.
- Proof shape: none owed beyond the focused run. The delta is changelog-only, so the gate evidence at 1a1875d95f carries over the title-only commit, following the SOLR-10882 precedent. The focused run still re-runs at the new head and is counted from fresh JUnit XML.
- Gate steps: changelog YAML parse of the edited fragment with a real YAML parser; tidy with the GATE INVALID guard (restore `gradle/libs.versions.toml` afterward; any tidy change to another file invalidates the delta check); Error Prone compile for `:solr:solrj-streaming` with `-Pvalidation.errorprone=true`; focused tests; module check `:solr:solrj-streaming:check -x test`.
- Push: the title commit pushes plainly (fast-forward) to `solr-12505-submit` after the gate is green at the committed tree.
- On green: refresh `receipts/SOLR-12505.md` with the new head and a line recording that the delta from 1a1875d95f is the title correction only, with the focused counts from fresh JUnit XML. Re-point the changelog link in `pr-drafts/streaming/SOLR-12505.md` to the new head. Mark this job DONE and the claim DONE.

## Step checklist

- [ ] owner ruling on the Choice recorded (not the terms-parser route) and the title wording approved; status set to OPEN
- [ ] title replaced verbatim in SOLR-12505-fetch-stream-deftype.yml; delta against 1a1875d95f is that fragment only
- [ ] changelog YAML parse
- [ ] tidy on the final tree; tree clean (GATE INVALID guard)
- [ ] Error Prone compile (`:solr:solrj-streaming`)
- [ ] focused tests at the new head: StreamDecoratorTest, counted from fresh JUnit XML
- [ ] module check (`:solr:solrj-streaming`)
- [ ] pushed plainly to `solr-12505-submit`; receipt refreshed; draft changelog link re-pointed; job and claim DONE
