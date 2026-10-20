# Skip record: SOLR-11655

- Ticket: SOLR-11655, SolrCloud should support encrypted ZooKeeper ACL digest details. JIRA read in this pass: Open, resolution none, fix versions none, 0 comments; the link field shows "Reference SOLR-11369 (Resolved)"; updated 2019-06-08.
- Verdict: keep-open (unchanged status; this record replaces the bulk audit-derived record on this branch).
- Draft branch: solr-11655-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket asks whether SolrCloud can take the ZooKeeper digest username and password (for the ACL and credentials providers) in an encrypted form, and if not, whether it is planned. The reporter's environment sets them as plain -D properties in SOLR_ZK_CREDS_AND_ACLS (Solr 6.5.1, ZooKeeper 3.4.8).

## Code reading

- There is no encryption of the digest credentials at the pin: git grep -i "encrypt" over solr/core/src/java/org/apache/solr/cloud and solr/solrj/src/java/org/apache/solr/common/cloud returns no hits.
- The digest providers are present: solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/DigestZkACLProvider.java:37, DigestZkCredentialsProvider.java:32, VMParamsSingleSetCredentialsDigestZkCredentialsProvider.java:30. The credentials are read as values (system properties or a properties file).
- VMParamsZkCredentialsInjector.java:47 and :67 define the file option (DEFAULT_DIGEST_FILE_VM_PARAM_NAME = "zkDigestCredentialsFile"), which loads the username and password from a properties file rather than the command line.
- solr/core/src/java/org/apache/solr/core/NodeConfig.java:648-649 lists zkDigestPassword and zkDigestReadonlyPassword among the entries handled as sensitive settings; this does not encrypt them.
- solr/solr-ref-guide/modules/deployment-guide/pages/zookeeper-access-control.adoc:142-155 and :317-333 document the plain-text properties and the zkDigestCredentialsFile option.

So at the pin the answer to the ticket's first question is that digest details are not encrypted; the documented alternative keeps them out of the command line in a file. Whether encryption should be added is not decided in the code.

## History

- No commit at the pin names SOLR-11655 (git log --grep at the pin: none).
- The file-based option and the credential providers came with the SOLR-11369 work (the linked resolved ticket); its commits were not traced in this pass.

## Reproduction attempts

Not applicable as a reproduction: this is a feature question. The check is whether the pin supports encrypted digest details, which the grep above answers.

## Gap analysis

The ticket is a feature request, and the code does not implement encryption at the pin. The documented file option is a partial answer that the reporter may or may not accept. Closing as answered (and so declining encryption) or keeping the request open as a feature is a maintainer decision.

## Verdict justification

keep-open: a close-answered verdict would decide that encryption is not planned, which the maintainers have not stated; the rubric's close-answered path needs a ref-guide link that answers the question, and the guide answers the file-based question, not the encryption question. close-fixed and close-obsolete are not supported.

Missing for a closing verdict: a maintainer statement on whether encrypted digest details are planned (then keep-open as a feature or close as not planned), or a ref-guide statement that answers the encryption question directly.

## Limits

This record does not change or test ZooKeeper authentication at the pin and does not post anything to JIRA or GitHub.
