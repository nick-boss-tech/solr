# Skip record: SOLR-16526

- Ticket: SOLR-16526, "java.lang.IllegalArgumentException: Invalid URI host: null". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2022-11-07, updated 2023-01-25, no fix version, no comments.
- Verdict: keep-open. Unchanged from the earlier record.
- Draft branch: solr-16526-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 14:15-14:15 UTC.

## Ticket reading

On Solr 9.0.0 deployed to Docker Swarm, `SOLR_HOST` was set to the Swarm task name, which contains an underscore (the service is named `solr-cloud_solr1`). The log shows "Sync Failed => java.lang.IllegalArgumentException: Invalid URI host: null (authority: solr-cloud_solr1.2...)" from `org.eclipse.jetty.client.HttpClient.checkHost`. The ticket has no comments.

## Code reading

- Solr does not validate or normalize the advertised host. `git grep -n -i -E 'underscore|hostname.*(valid|replace|sanitiz)'` over `ZkController.java`, `AddressUtils.java`, `CoreContainer.java` and `ZkStateReader.java` at the pin returns no hits.
- `solr/core/src/java/org/apache/solr/util/AddressUtils.java:25-60`, `getHostToAdvertise`: picks a host from the local address, with no character check. It is the default when no advertise host is set; `SOLR_HOST` in the ticket is an explicit override.
- The exception is raised inside Jetty's `HttpClient.checkHost`, the URI-host check that rejects underscores. Solr builds the request URI from the advertised host and passes it to that client.

What this shows at the pin: Solr hands the advertised host to Jetty without checking it, so a host with an underscore fails at the first inter-node request (the sync). The fix is either on the operator's side (a host name without underscores, or an IP in `SOLR_HOST`) or a Solr-side choice to validate or warn about the advertised host.

## History

- No commit on the pinned head names SOLR-16526 (evidence tool: no hits).

## Docs

- `solr/solr-ref-guide/modules/deployment-guide/pages/solr-in-docker.adoc:160` and `:253` use `SOLR_HOST_ADVERTISE=solr1`, a name without underscores. No page at the pin warns that the advertised host must be a valid URI host.

## Reproduction attempts

Not attempted. A reproduction needs a host name with an underscore in a running node's advertised host. This pass is review-only and runs no builds.

## Gap analysis

The failure is the Jetty URI check on the advertised host, reached through Solr's unchecked use of that host. It is not a Solr logic error, but Solr could reject the value early or document the restriction. Whether Solr should validate, warn, or leave it to the operator is a product decision. The ticket's workaround (a host name without underscores) is not in the ticket and is not verified here.

Missing piece for a verdict change: a decision from the Solr owners on whether to validate or document the advertised host, or a confirmation that the operator-side fix resolves the reporter's case.

## Verdict justification

keep-open meets the rubric: the code path is read (`AddressUtils.java:25-60` and the Jetty check named in the trace), the docs are checked, and the missing piece is named (the owner decision on validation or documentation). close-answered is not met, because no docs page answers the underscore question. pr-needed is not met as a defect: the code does what it is written to do, and the choice of check is a decision, not a demonstrated bug.

## Limits

This record does not reproduce the failure and does not confirm the operator-side fix. It does not decide whether a Solr-side validation is wanted. It does not post anything to JIRA or GitHub.
