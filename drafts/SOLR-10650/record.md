# Skip record: SOLR-10650

- Ticket: SOLR-10650, "First start fails due to Problem accessing /solr/. Reason: Not Found". JIRA status read 2026-10-20T11:49:06Z (UTC): Open, resolution None, updated 2019-06-08.
- Verdict: close-answered. This replaces the recorded close-fixed: no fixing commit exists; the cause is an unsupported Java version, and the reference guide now states the minimum.
- Draft branch: solr-10650-draft-close.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T11:49:06Z (UTC).

## Ticket reading

On JDK 9 the first start of Solr 6.5.1 showed a 404 page and no log file. The reporter asks for an easier startup path. The comments say the cause is the JDK 9 environment, that it works on JDK 8, and that the log messages for a wrong Java version were improved later.

## Docs (pinned head)

- `solr/solr-ref-guide/modules/deployment-guide/pages/system-requirements.adoc:62`: "The minimum Java version for Solr 10.x is Java 21 for the Solr server." The page also gives the Java checks and the tested versions for Solr 9.x (lines 70-84).
- The page answers the question: the server needs a supported JDK, and the start path is not expected to work on an older or unsupported one.

## Code reading (pinned head)

Not needed for the answer. The start script has a Java-version branch for security manager handling (`solr/bin/solr:1170`), which does not change the requirement.

## History

- evidence.py at the pinned head: no commits naming SOLR-10650.
- No fixing commit is named in the ticket or found by the search, so close-fixed is not supported.

## Reproduction attempts

Not run. The question is answered by the requirement; a run on JDK 9 is not available on this host (JDK 21 only).

## Gap analysis

The reporter's first-start experience is a separate usability question. It is not answered by the docs and is not a bug in the requirement.

## Verdict justification

close-answered meets the rubric: a support question that the reference guide answers, with the page and line linked (system-requirements.adoc:62).

## Limits

This record does not address the usability of the start-up error page. It does not check older reference guide versions.
