# Gate job: SOLR-11356 SJ-R1-49, header copy and header-match test, then the full gate

- Branch: solr-11356-submit. Head before the patch: `6120dae28d04a07763f44d4bc5e9d9793af025f6` (fork tip by ls-remote, read by the Windows review pass for the round 2 SolrJ settlement). The packaging commit on that tip removes SOLR-11356-TESTING.md only, so `8474e5a3a26d` and `6120dae28d04` carry the same Java.
Status: DONE
- Intended host: vm2, or vm1 if vm2 is busy. Claim under WORKFLOW.md before work; hourly heartbeat; a gate runs on one host only.
- Capability tags: gate.
- Job type: code change plus the full hardening gate at the patched head. The patch is provided. Under the 2026-10-12 patch-loop directive the claimant may change the patch or the test so the build passes, and must record each change in this file. The patching agent pushes.
- Decision carried in (not reopened here): copy the request headers into the stream key. Recorded owner decision, SJ-R1-49, required.

## Why

At `6120dae28d04`, `OutStream` stores `origRequest.getHeaders()`. `SolrRequest.getHeaders()` returns `Collections.unmodifiableMap(headers)` (`solr/solrj/src/java/org/apache/solr/client/solrj/SolrRequest.java`, base lines 374 to 377), which is a live view of the request's own map. The HTTP request takes its headers once, when the stream opens (`client.decorateRequest(postRequest, updateRequest, false)`, `ConcurrentUpdateJettySolrClient.java` line 224 at the head). A later change to a queued request's headers therefore changes the join check but not the headers on the wire.

The header part of the match also has no test. The shared test changes only the credentials (`ConcurrentUpdateSolrClientTestBase.java`, lines 300 to 333). The branch's own draft says so (Limits, "no test covers it").

## The patch

`gates/SOLR-11356-sj-r1-49-header-copy.patch`: one import (`java.util.HashMap`) and a null-safe copy in the `OutStream` constructor, with a two-line comment. The reviewer checked it with `git apply` against the `6120dae28d04` content of `ConcurrentUpdateJettySolrClient.java` in a scratch directory (LF endings, autocrlf off): it applies, and the result is byte-identical to the intended file. It was not built. The patch paths are relative to the `source/` checkout root.

## Tests to add (claimant writes them; the shared base runs in both suites)

1. Header-match test, new: `testRequestsWithDifferentHeadersAreNotSentOnOneStream` in `ConcurrentUpdateSolrClientTestBase`, in the style of `testRequestsWithDifferentCredentialsAreNotSentOnOneStream`. Twenty documents, two users or one user, alternating two values of one custom header (for example `X-Gate-Tag: alpha` and `beta`), on one client. `TestServlet` must record the custom header received with each document, next to the Authorization it already records. Assert that every document arrived with the header value its own request carried. This test proves the header match. It does not prove the copy.
2. Copy: no test is required. Do not write a test that changes a request's headers while the request is queued or in flight. With the copy, a change to an opener's headers before its `send()` trips `assert outStream.belongToThisStream(req, ...)` (line 241) when assertions are on. That is misuse, not a branch fault. If the claimant finds a way to change headers only after the stream has closed, record it and add the test.
3. Hazard for any test: the existing credential test and the new header test are timing-dependent (queued updates must meet on one stream). Record the observed shares; do not claim a deterministic proof.

## Proof shape

- Premise leg: production reverted to the merge-base `cabedd1d968059215188f4e7563fb303241899ed` (the base the branch starts from; `upstream/main` merge-base, checked by the reviewer against `upstream/main` at `d38e0f1623`), with the branch's new tests kept. `testRequestsWithDifferentHeadersAreNotSentOnOneStream` must fail by content (a document that arrived with the other request's header value). Record the assertion line, the observed value and the count from fresh JUnit XML. A pass at the merge-base means the test does not discriminate: record it, and the header match is then not proven.
- Pre-patch head, for information: `6120dae28d04` with the new test kept. Expected to fail the header test if the stream is shared, and to pass the credential test. Record the result either way.
- Patched head: `ConcurrentUpdateJettySolrClientTest` (all tests, now 13) in `:solr:solrj-jetty`; `ConcurrentUpdateJdkSolrClientTest` in package `org.apache.solr.client.solrj.impl` in `:solr:solrj`. The second class runs the same shared base. The draft's Limits line says the JDK suite was not part of the earlier run; this run settles that line either way. Counts from fresh JUnit XML.
- Seed: record the seed the run used. Suggested: `11356C0FFEE11356`.

## Steps

1. Confirm the head: `ls-remote origin refs/heads/solr-11356-submit` must equal `6120dae28d04a07763f44d4bc5e9d9793af025f6`. If it does not, stop and record the new tip here.
2. File the claim under `claims/`.
3. Premise leg (see Proof shape).
4. Apply the patch (`git apply`), add the header test and the `TestServlet` header recording, or fix either under patch authority. Record every change here.
5. Changelog parse of `changelog/unreleased/SOLR-11356-cusc-credentials-per-stream.yml`. No text change is expected; if one is needed, record it.
6. Tidy with a clean tree afterwards. Error Prone compile of `:solr:solrj-jetty:compileTestJava` and `:solr:solrj:compileTestJava` with `-Pvalidation.errorprone=true`.
7. Focused runs at the patched head from fresh JUnit XML (see Proof shape).
8. Module checks: `:solr:solrj-jetty:check -x test` and `:solr:solrj:check -x test`.
9. Push: the patching agent pushes the patched head to `solr-11356-submit` as a plain fast-forward from `6120dae28d04`. No history rewrite. Record the new head here.
10. Receipt: write `receipts/SOLR-11356.md` at the patched head with the gate record and the proof outcome. Mark this job DONE and the claim DONE.
11. The draft revision waits on the receipt. The text to apply is in `reports/solrj-convergence-round-2-response.md`, under "SOLR-11356, post-gate text".

## Standing rules

- Commit author and committer: Nick Shanin. No co-author trailer. No em dash in the commit message or in test comments.
- The packaging commit that removes SOLR-11356-TESTING.md stays in the history below the new commit.

## Step checklist

- [ ] head confirmed by ls-remote
- [ ] claim filed under claims/
- [ ] premise leg recorded (failure line, count, seed, date)
- [ ] pre-patch head run recorded
- [ ] patch applied; header test and servlet recording written; each change recorded
- [ ] changelog parse
- [ ] tidy with a clean tree; Error Prone compile
- [ ] focused runs from fresh JUnit XML (both suites)
- [ ] module checks
- [ ] pushed; new head recorded here
- [ ] receipt refreshed at the new head; job and claim DONE
