# Skip record: SOLR-13166

- Ticket: SOLR-13166, Add smart checks for Config and Schema API in Solr to avoid malicious updates.
- Verdict: keep-open.
- Draft branch: solr-13166-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 13 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2019-01-25, updated 2019-06-08, no fix versions, no links. The request: checks in the Config and Schema APIs that stop changes which can take a node down (too many fields, too-short auto soft commit intervals, suggester builds on the wrong field). Five comments (2019-01-25 to 2019-03-07): the reporter attached two patches (a `SchemaChecksManager` with hard-coded checks, and cluster-property floor limits such as `updateHandler.autoSoftCommit.maxTime.lower.limit`); erickerickson notes that docValues changes can be handled by UninvertDocValuesMergePolicyFactory; janhoy asks for Admin UI handling of the field-count case; the reporter proposes a `force` override. No patch was committed.

## Code reading

Read at the pin:

- No `SchemaChecks` class exists under `solr/core/src/java` (`git grep -l SchemaChecks` returns no files).
- No cluster-property floor for auto soft commit exists: the only `lower.limit` hits under `solr/core/src/java` are in `IntervalFacets.java:461-485`, which are facet interval bounds.
- The schema and config admin APIs (`solr/core/src/java/org/apache/solr/handler/admin/api`) have no check of this kind; the `force` string found there is the leader-election `ForceLeader` API, not a schema override.

## History

No commit at the pin names SOLR-13166 (the evidence tool lists none). The reporter's patches were never committed.

## Reproduction attempts

Not run. A reproduction is not the question here: the ticket asks for a feature that does not exist at the pin. A run would only show the absence of the checks.

## Gap analysis

- The requested checks are not present, and the ticket's patches were not merged, so this is a feature request, not a defect with a reproduction.
- Design questions are open on the ticket: which checks apply, whether the limits are cluster properties, and whether a `force` override is acceptable (the reporter proposes one, and erickerickson's comment bears on docValues changes).
- Close-obsolete does not fit (nothing was removed); close-fixed does not fit (no fix commit); pr-needed needs the design decisions above.

## Verdict justification

keep-open: a feature request whose design is open on the ticket, with no committed patch. The maintainer decision on the set of checks and the override is what would move it.

Missing for this verdict: the maintainer decision on the check set, the limit mechanism (cluster properties or fixed), and the override semantics.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
