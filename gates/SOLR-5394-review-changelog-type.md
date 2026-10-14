# Gate job: SOLR-5394 review finding, changelog type (round 1, slice 1)

- Branch: `solr-5394-submit` on the fork (`origin`). Head: `967445622f9294e26e425ec494d37e5ca13d4fe4` (draft PR #5131). Base: `b6b2b8f10e9827e3b86e44649fb7966ce646c185` (merge-base with `upstream/main`).
- Status: OPEN. Owner ruling (Nick, 2026-10-14): the changelog type `changed` is approved for SOLR-5394, per the review round's recommendation (dev-docs defines `changed` as modifying the behavior of existing requests, which fits this change). The approved target type is `changed`; apply the proposed patch below (`type: fixed` to `type: changed` in the branch's changelog fragment) and run the gate shape as written.
- Filed by: review slice 1 (Windows review agent, `claims/search-components-opened-review-round-1.md`). Finding answered: the changelog type for PR #5131. `dev-docs/changelog.adoc` defines `changed` as "Modifying behavior or performance of existing requests/configuration", and this change modifies the default behavior of `facet.method=fcs` requests that set no `threads` local param. `fixed` is defensible because the ticket reports a bug, so the choice is the owner's.
- Capability tags: gate. Linux only (vm1 or vm2). Windows never runs this job.
- Proposed patch: in `changelog/unreleased/SOLR-5394-fcs-default-threads.yml`, change line 3 from `type: fixed` to `type: changed`. No other file changes.
- Moves the live head only after the gate is green and Nick has agreed. The review agent does not push.

## Gate shape

1. Confirm the head: `origin/solr-5394-submit` is `967445622f9294e26e425ec494d37e5ca13d4fe4`. Record the SHA.
2. Changelog YAML parse: parse the fragment with a real YAML parser. The title is a plain scalar with a quoted-looking `"threads"` inside it; confirm it parses as one string.
3. Tidy, then restore the versions file if tidy touches it. Tidy must leave only the one-line fragment change.
4. Error Prone compile of `:solr:core` (with `-Pvalidation.errorprone=true`).
5. Pre-fix proof: on the base `b6b2b8f10e9827e3b86e44649fb7966ce646c185` with the branch tests kept, `SimpleFacetsTest.testFacetThreadsDefaultIsSingleThread` must fail with `expected:<1> but was:<-1>`. Record the failure text.
6. Focused run: `SimpleFacetsTest` at the head, counted from fresh JUnit XML. Expect 49 tests, 0 failures, 0 errors, 1 skipped (the counts in PR #5131's Proof section and in the receipt). Record the seed.
7. Module check: `:solr:core:check -x test`.
8. On green, mark this job DONE with the new head SHA and the test counts. Do not push to `solr-5394-submit` and do not edit PR #5131 from this job; the head move is a separate step after Nick's go.

## Checklist

- [x] owner word on fixed or changed (Nick): `changed`, ruled 2026-10-14
- [ ] head confirmed
- [ ] changelog YAML parse
- [ ] tidy, versions file restored
- [ ] Error Prone compile
- [ ] pre-fix proof on base
- [ ] focused run, 49 tests
- [ ] module check
- [ ] job DONE
