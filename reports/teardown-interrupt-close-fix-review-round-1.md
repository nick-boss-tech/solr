# Interrupted client-close fix: branch review, round 1

Status: DONE; close, final Linux validation pending
Host: windows
Claim: claims/teardown-interrupt-close-fix-review-round-1.md
Board snapshot: 91cef01a60cfd31c2e11c7e69022f9f4eb5012ac, refreshed through 7f703f86ad4a626904242eb9304c82df8fc5905a
Decision ladder: rung 1, source and recorded proof

## Verdict

No blocking production defect found for the stated pre-interrupted-close case.
The patch implements the narrow boundary recommended in source-review round 2.
It is a credible PR candidate, but the remaining build validation and a small
test import cleanup should finish before requesting publication approval.

The Linux handoff is
`assignments/teardown-interrupt-close-fix-final-validation-round-1.md`.
It covers that preparation, exact-head static checks, one module-suite run and
preservation of existing proof artifacts. No unchanged premise rerun, original
CI schedule claim, interrupt-during-stop experiment or repository-wide test suite
is requested. No public action is authorized by this review.

## Reviewed revision

| Item | Value |
| --- | --- |
| Fork branch | nick-boss-tech/solr:solr-teardown-interrupt-close-fix |
| Reviewed / independently verified remote head | 8787c6605ad5a5d322e9bca253235cd73142f101 |
| Base | d298cd0b42602247bee7bf9e2bcf302e725f6690 |
| Fresh upstream/main | same base at review |
| Commits | d1d5e963c704929062dffdd136e96ff2f074116b tests; 423390023a63cdac2d7509e3f2d957b5db6a0b3c production; 8787c6605ad5a5d322e9bca253235cd73142f101 formatting |
| Outbound files | HttpJettySolrClient.java and HttpJettySolrClientTest.java under solr/solrj-jetty |
| Authorship | Nick Shanin, correct ICLA email, no attribution trailers |
| Whitespace | git diff --check passes |
| Public state | no PR reported; nothing opened or posted by this review |

Read immutable Git objects, not the dirty Windows source tree. The final commit
only wraps test lines; it does not alter production behavior.

## Production assessment

At the head,
[HttpJettySolrClient.close:347-377](https://github.com/nick-boss-tech/solr/blob/8787c6605ad5a5d322e9bca253235cd73142f101/solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java#L347)
still drains asynchronous requests first. Line 350 then captures and clears the
current interrupt. The existing owned HttpClient stop/destroy and scanner stop
run through the same ownership guard. Executor termination still runs only for
an internally created executor. A nested finally restores the captured flag even
if executor termination throws. Tracker release remains after successful cleanup;
it was not moved into an unconditional finally.

This fixes the demonstrated pre-existing-flag entry condition without adopting
Jetty's selector internals or shutting down a caller's executor. The
interruptible async drain and its shutdownNow behavior are unchanged. A borrowed
HttpClient is still not stopped by a wrapper. No public API, dependency, protocol,
authorization or persistent-format change is introduced. No new trust boundary
was found in this small lifecycle change; this is not a broad security review.

An interrupt newly delivered during stop can still be consumed by Jetty and
abort cleanup. The branch does not claim to fix that separate window, and this
review does not require solving it before the narrow fix can be proposed.
Likewise, unrelated stop failures can still skip later cleanup. Preserve those
limits in the eventual PR description.

## Regression assessment

The three new tests are at head lines 943, 963 and 993:

- Owned client: explicitly closes an interrupted caller, checks interrupt
  preservation, HttpClient stopped state and tracker release.
- Supplied executor: checks interrupt preservation, HttpClient stopped state,
  the pool not shut down and a bounded sentinel task succeeding.
- Borrowed client: checks interrupt preservation and the parent's started/not
  stopped lifecycle state. It passes on base and is correctly described as a
  compatibility guard, not failing-before proof.

These are meaningful behavioral tests. Their permanent assertions are narrower
than the earlier diagnostic scaffold: they do not save/check raw selectors, the
owned case does not explicitly inspect executor termination, and the borrowed
case does not issue a request through the parent after child close. The full-class
resource checks and the prior raw evidence supplement those assertions; do not
describe the new tests themselves as directly asserting all those resources.
Adding those assertions is a possible later strengthening, not a discovered
production failure or a reason to repeat the same premise pair now.

**P3 preparation:** the new tests use fully qualified
`org.eclipse.jetty.client.HttpClient` at lines 947, 974 and 1002 despite no import
ambiguity. The branch's AGENTS.md:23 explicitly prefers an import. Add that import
and shorten those three declarations before final formatting/gating. This is a
style-only change; record the resulting new head before running proof.

**Test hygiene advisory:** the owned regression clears the thread flag in finally
but does not close the client again if the original call/assertion fails. That
explains additional classMethod tracker failures on the failing-before leg.
The original exception remains a valid discriminator. Do not miscount the
classMethod failure as a third independent fixed bug. No source change for this
advisory is required by the final-validation assignment.

## Proof and its limits

`reports/teardown-interrupt-close-fix-round-1.md` reports:

| Leg | Head | Reported result |
| --- | --- | --- |
| Failing before, three new methods | d1d5e963c704929062dffdd136e96ff2f074116b | owned and supplied-executor methods throw Exception on closing client; borrowed passes; extra classMethod tracker failure |
| Passing after, whole class | 423390023a63cdac2d7509e3f2d957b5db6a0b3c | 53 tests, zero failures |
| Final whole class | 8787c6605ad5a5d322e9bca253235cd73142f101 | 53 tests, zero failures/errors/skips |

This correctly distinguishes the passing base guard from the two discriminatory
tests and covers the formatting commit with another class run. The reported
60.051-second owned base failure and 0.006-second fixed test are consistent with
the earlier source/probe analysis, but are single-run timings, not a benchmark.

The report points to vm2-local `logs/fix-before/`, `logs/fix-after/` and
`logs/fix-final/`; the reviewed board snapshot does not supply their raw logs/XML.
Thus counts and failures above are host-reported, not independently parsed by
this Windows review. Have Linux archive those existing artifacts with hashes;
do not recreate the original before leg merely to publish a log.

Remaining checks are material pre-PR validation, not more premise investigation:
the repository AGENTS.md:15-16 requires tidy and check before declaring work done;
the existing pool gate shape uses module check and explicit Error Prone compile.
The branch report confirms module check and Error Prone have not run. The
seven-class module suite also covers concurrent-update, compatibility, proxy and
authentication clients beyond the already-run HttpJettySolrClientTest, which is
appropriate once for a shared close-method change. A module check must not be
described as a full-repository check.

## Publication preparation

No changelog fragment is present. This is expected **before** a Jira or PR number:
the branch's AGENTS.md:83-84 explicitly says not to add one without an identifier.
`dev-docs/changelog.adoc:94-97` supports adding a PR-linked fragment after opening
a PR. Do not fabricate an issue/PR number. A later fragment must name Nick Shanin
only and use type fixed. Draft wording:

> Allow HttpJettySolrClient to finish closing when the caller is already interrupted, while preserving the caller's interrupt status.

Read-only GitHub searches for `HttpJettySolrClient interrupt` and
`HttpJettySolrClient close` returned no PR matches. A scoped Jira query for that
class and interrupt/ManagedSelector returned five issues; none directly names
this pre-interrupted-close defect. SOLR-18174 is background for the async drain;
SOLR-18488 concerns selector scheduling under load. Neither is adopted as this
fix's ticket. This is a scoped duplicate check, not an exhaustive uniqueness claim.

Suggested eventual title: **Preserve interruption while closing HttpJettySolrClient**.
The body should describe the pre-existing interrupt, explain clearing only after
the async drain, name both ownership modes and the borrowed guard, cite actual
final-head gate results, disclose AI assistance, and retain the during-stop/CI
attribution limits. Do not claim module, Error Prone or raw-selector assertions
until the corresponding evidence exists.

## Patch / disposition

No patch was made by this Windows review. No Gradle, formatter or local queue was
run here. The reviewed fork head remains 8787c6605ad5a5d322e9bca253235cd73142f101.
Final preparation and Linux validation are handed back as one new assignment;
the gate host must pin its resulting source head and publish a receipt.
After that receipt, publication remains Nick's explicit decision. No PR, comment,
Jira issue, source push or public review was created in this review.
