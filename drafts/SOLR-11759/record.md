# Skip record: SOLR-11759

- Ticket: SOLR-11759, "DocExpirationUpdateProcessorFactory causes unauthenticated inter-node requests". JIRA status read 2026-10-20T08:18:35Z (UTC): Open, resolution None, updated 2019-06-08. The 2018 comment asks whether it is still an issue.
- Verdict: close-fixed. This replaces the recorded keep-open.
- Draft branch: solr-11759-draft-close.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T08:18:35Z (UTC).

## Ticket reading

With DocExpirationUpdateProcessorFactory configured and a BasicAuth (blockUnknown) plugin, the expiration thread's update requests to other nodes get 401 "require authentication", because the request carries no user. The reporter ran Solr 6.6.2. A 2018 comment asks whether it still happens.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/update/processor/DocExpirationUpdateProcessorFactory.java:369`: `req.setUserPrincipalName(PKIAuthenticationPlugin.NODE_IS_USER);` — the deleter request is marked as the node itself, so the authorization plugins treat it as internal.
- `solr/core/src/java/org/apache/solr/security/PKIAuthenticationPlugin.java:396, 437`: `NODE_IS_USER = "$"` and the special name the PKI path returns for node-originated requests.

## History

- `95dfddc7d4` (2020-01-22), subject: "SOLR-12859: Fixed DocExpirationUpdateProcessorFactory to work with BasicAuth and other auth plugins that delegate to PKI for server initiated node-to-node communication." It changes `DocExpirationUpdateProcessorFactory.java` (7 lines) and adds a test rewrite in `DistribDocExpirationUpdateProcessorTest.java`. Blame attributes line 369 to this commit. It is an ancestor of the pinned head (`git merge-base --is-ancestor`, checked).
- The commit names SOLR-12859, not SOLR-11759. The link to this ticket is that the symptom (node-to-node 401 from the expiration deleter under a BasicAuth plugin) is the same. The ticket is not named in the commit.

## Reproduction attempts

Not run. The commit's own test (DistribDocExpirationUpdateProcessorTest) covers the delegate case; a run of that test at the pinned head would confirm the fix. Not run in this pass.

## Gap analysis

The symptom and the fix match on the path. The fixing commit names another ticket, so the link is by symptom, not by a stated reference.

## Verdict justification

close-fixed meets the rubric: the fixing commit is named (95dfddc7d4), it is an ancestor of the pinned head, and the changed line is present (DocExpirationUpdateProcessorFactory.java:369). The link to SOLR-11759 is by symptom; the record says so.

## Limits

This record does not confirm the 2017 Solr 6.6.2 case directly. It does not run the test. It does not establish that SOLR-12859 and SOLR-11759 are duplicates; that is a JIRA question for the owner.
