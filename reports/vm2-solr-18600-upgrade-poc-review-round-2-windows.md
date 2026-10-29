# SOLR-18600 upgrade demo review, round 2 (Windows)

Status: REVIEW COMPLETE. WORTH RAISING as an ordinary SolrJ upgrade regression, using the existing SOLR-18600 rather than creating a duplicate bug. Recommend a concise evidence update and dev-list question, an explicit 10.1 upgrade warning, and continued HOLD on the current restoration. No public communication is authorized or performed by this review.

## Revision and evidence

- Assignment: assignments/vm2-solr-18600-upgrade-poc-review-round-2.md, announced at 6e59041d4c4; reviewed at board snapshot 92db1f3493258b754f05a74c6148979897d9a70a.
- Claim: claims/vm2-solr-18600-upgrade-poc-review-round-2-windows.md, posted at 6b4e4b32f9eb87588855202885e4c7e914b4a6d6.
- Read all four reports/solr-18600-poc files and assignments/vm2-solr-18600-premise-review-addendum-round-1.md. Earlier review: reports/vm2-solr-18600-premise-review-round-1-windows.md at a468ae747228.
- Fork fix head remains c40665af8d6bd23c85fef565f93de5004a7b2ca4, remotely checked in this review. No matching SOLR-18600 PR returned by GitHub's all-state search.
- Live SOLR-18600 is Open, created 2026-10-27 22:10:41 UTC, updated 22:45:40 UTC, no comments. Its description already contains the defect, reproducer, proposed restoration and verification claims. The repeated statement that nothing has been posted to JIRA is therefore inaccurate as a description of total public state. It may mean no additional post by the gate agent.
- Review date: 2026-10-29, America/New_York. No compilation, Gradle, probe, or full-suite execution on this Windows host. Raw vm2 logs are not accessible here. Runtime results below are attributed to the assignment/addendum, not independently executed results.

## Decision: why this merits reporting

The app calls the documented public builder for HttpJdkSolrClient and ordinary timeout methods. It need not name or extend an internal class. The removed builder type appears in bytecode through generic erasure. Treating a superclass as internal does not make that ordinary source use an explicit internal-API dependency.

The recorded three-leg demo isolates a library upgrade: 10.0-compiled bytes run on 10.0, fail on 10.1 at the first builder invocation, and work after recompilation against 10.1. No server is needed to establish this linkage failure. A full Solr test suite is not a prerequisite for reporting it. The workaround reduces severity but can involve rebuilding dependent libraries as well as the top-level application; recompiling only top-level source does not rewrite already compiled dependency jars.

There is now a documented compatibility norm to cite. The [Solr 9.11 upgrade notes](https://solr.apache.org/guide/solr/9_11/upgrade-notes/major-changes-in-solr-9.html#removing-local-tika) describe the project's normal avoidance of minor-release removals and explain a security-driven exception. At releases/solr/10.1.0 the source is solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-9.adoc line 86. The [downloads page](https://solr.apache.org/downloads.html#about-versions-and-support) similarly places compatibility-breaking changes normally in a major version. These support asking for a deliberate exception or repair; neither establishes an unconditional promise for every public/protected descriptor.

Additional primary discussion found since round 1: [Jan Høydahl's August 21 deprecation-policy reply](https://www.mail-archive.com/dev%40solr.apache.org/msg07060.html), available through the search index, argues for preserving public contracts in minor versions, excluding internal/experimental APIs, with justified exceptions called out prominently in upgrade notes. It quotes a different view from David Smiley on some removals and internal APIs. This is evidence of maintainer discussion, not a ratified decision for SOLR-18600. The linked wiki could not be retrieved; no claim about its current adopted text is made.

Round 1's bounded finding that no exact policy decision was found remains valid, but its policy context should now include this norm and discussion. Do not frame the report as an accusation that maintainers have violated a settled universal binary guarantee.

## What the new probe resolves

The addendum records a consumer compiled against published 10.1.0:

| Runtime variant | HttpJdk timeout builder | Cloud withHttpClientBuilder |
| --- | --- | --- |
| Base a336bf871b2c | passes | passes |
| Base plus four core restoration files | NoSuchMethodError, expected result type HttpSolrClient$BuilderBase | passes |
| Base plus all ten solrj production restoration files | same NoSuchMethodError | passes |

This confirms round 1 F1 for the tested JDK path. It is a probe of specified source variants, not a new full exact-head gate. The passing Cloud overload is useful and must be reported as passing; it does not test Cloud getHttpClient, LB, or ConcurrentUpdate. Those F2 concerns remain source findings awaiting their own binary fixtures.

One correction to the addendum: its parenthetical that withHttpClient is not in 10.1 is too broad. Released CloudSolrClient.java line 1533 declares withHttpClient(HttpSolrClient), and line 1550 declares withHttpClientBuilder(HttpSolrClient.BuilderBase<?, ?>). The restored old-type overload differs. Record which exact descriptor was not tested rather than implying the method name is absent.

## Demo review

The small Java source is appropriate for an upstream report. It builds and closes a client, uses ordinary methods and makes no HTTP call. Its output table is sufficient to explain the symptom, subject to the recorded-evidence limitation above.

The current run.sh is a host-local demonstration, not a portable acceptance test:

- deps.txt embeds nine /workspace/gradle-home cache paths. Other developers do not have those files. The supplied JAVA_HOME default is also host-specific, though overridable.
- It fetches into lib/<version> without creating those directories. A clean copy of the four tracked files lacks them and the fetch may fail before compilation.
- It uses set -u but does not capture each Java exit code or enable pipefail. Each Java process feeds grep and head, so the pipeline exit status does not reliably reflect Java's status. It also truncates the failure output. It does not assert the expected 0 / nonzero / 0 outcome pattern.
- It trusts existing/downloaded jars without verifying hashes and does not hash the class before and after the no-recompile leg. It reuses a fixed extra dependency set across versions; that is a useful isolation condition, but is not a full per-release resolved dependency graph.
- javac is invoked without --release. With the documented JDK 21 default this fixture is not evidence of Java 17 runtime compatibility. That is acceptable for a JDK 21 linkage demo if stated, but is different from the earlier --release 17 consumer.

Before sharing this helper as runnable, replace cache paths with resolved/pinned dependencies, create its output directories, retain full output and exit codes, assert expected outcomes, and record jar/class hashes. This cleanup is not necessary to delay a report using the small source plus version/outcome table already present in SOLR-18600. It also does not require running Gradle on Windows.

## Answers to the five assignment questions

1. **Acceptable in a minor release?** I would report it as a user-facing upgrade regression and ask for an explicit disposition. The general minor-release norm and ordinary builder usage make it worth raising. Whether an exception is acceptable, or should affect the release vote, belongs to maintainers and release voters. This review does not cast a vote or declare a release blocker.
2. **Restore or document?** Document the 10.1 behavior promptly. Do not offer c40665af8d6b as a finished compatibility fix: the 10.1 consumer probe proves it trades one break for another on this call. If restoration is pursued, preserve both eras of descriptors. Documentation is a viable resolution if maintainers accept the exception, but it does not repair old binaries. The current 10.2 restoration note does not replace a 10.1 migration warning.
3. **Proof for restoration?** Use two immutable compile cohorts, per the matrix below, plus both descriptor inventories and focused behavior checks. The original negative leg stays recorded. Source recompilation against only the candidate cannot demonstrate compatibility with either released binary cohort.
4. **Who handles the 10.1 note at RC3?** Nick can raise the linked defect on dev@solr.apache.org, ideally in the active release discussion if it is still active. A contributor can prepare the docs patch; a committer merges it and the release manager coordinates publication and any effect on a candidate. It is not exclusively the release manager's coding task. Do not silently change an already voted artifact. Current vote status must be checked before sending release-stage wording.
5. **Include the demo in the mailing-list question?** Yes: include the short Java call or link to the existing ticket's reproducer and the three-row result table. Keep the host-local deps.txt/run.sh out of the public instructions until made portable. No need to include the full investigation, a restoration diff or unrelated Solr tests in the question.

## Restoration acceptance matrix

| Compile cohort | Positive release runtime | Existing 10.1 runtime | Repaired candidate runtime |
| --- | --- | --- | --- |
| 10.0.0 | 10.0.0 PASS | original failure retained as negative control for affected builder call | PASS unchanged |
| 10.1.0 | 10.1.0 PASS | same positive control | PASS unchanged; currently FAIL for the probed JDK call |
| Candidate source | not required | not required | ordinary source use and focused behavior tests PASS |

Apply this to JDK and Jetty fluent builders, build/withHttpClient bridges, the promised SolrRequest overload, Cloud builder overloads AND client getters, LB constructor/getter/dispatch, HttpClusterStateProvider constructor/getter, and ConcurrentUpdate constructors/getter. Name the public/protected support boundary, including whether baseUrl subclass access is preserved. Hash every compile cohort, record separate compile/runtime graphs, and check descriptor lookup through supertypes and interfaces against both releases. Obtain a fresh exact-head Linux gate after any repair. This is forward upgrade compatibility; 10.1-compiled code running on 10.0 is not required unless separately promised.

## Release-stage limits

The board's RC3 receipt identifies revision 749712b71940c9f96d824c533f32f5c56dcd3924. The local releases/solr/10.1.0 tag resolves to the same commit, but a tag name is not proof of a completed release vote. The live official downloads page still listed 10.0.0 at this review; the release guide explains that Maven, distribution files, the website and reference guide are separately published with delays. GitHub has no release object at the queried tag. I did not retrieve a live RC3 vote/result thread, so whether the vote is still open remains unconfirmed. Do not infer it from either the website or Maven jar availability.

Before presenting the Maven-based probe as a test of the exact voted RC3 artifact, match the tested jar hash to that artifact or rerun the minimal consumer on the official candidate jar. This identity check matters to a release objection, not to whether the existing reported library failure warrants discussion. If the release has completed, request a published 10.1 guide correction and a follow-up fix rather than an edit to a frozen candidate.

## Suggested upgrade-note wording (draft only)

SolrJ 10.1 renames HttpSolrClientBase to HttpSolrClient and moves HttpSolrClientBuilderBase to HttpSolrClient.BuilderBase. Applications or libraries compiled against SolrJ 10.0.0 that call inherited HTTP client builder methods can fail with NoSuchMethodError when run with 10.1 jars, even if their source does not name either base class. Rebuild affected code against SolrJ 10.1; replacing the runtime jars alone is insufficient. Code that directly names the removed types must also migrate to the new names. See SOLR-18600.

## Disposition

Report-worthiness is settled: YES, using the existing issue and new evidence. Keep the current restoration held. The minimum public ask is an explicit 10.1 warning and a maintainer decision on accepting the binary break versus preserving both released surfaces. A local dev-list draft accompanies this report; it has not been sent. No JIRA update, PR, source change or mailing-list post was made.

Decision ladder: source/evidence and documented norm at rungs 1-3; recommendation at rung 4. Public posting and any release vote remain Nick's decisions. No owner response is needed to complete this assessment.
