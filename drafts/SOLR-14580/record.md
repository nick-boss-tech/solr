# Skip record: SOLR-14580

- Ticket: SOLR-14580, "CloudSolrClient cannot be initialized using 'zkHosts' builder". JIRA status Open, resolution none, fix versions none, created 2020-06-18, updated 2020-11-04. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-14580-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14580-vm2.md).

## Ticket reading

Building a CloudSolrClient with the zkHosts builder throws IllegalArgumentException, because the builder checks the solrUrls field with a null comparison and the value is an empty array. The reporter found a workaround: create the state provider before build(). It fails only when a client extends the Builder and overrides build(). Eight comments (2020-06-19 to 2020-11-04). One maintainer marked it a release blocker on 2020-06-19; no comment names a fix.

## Code reading

- solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java:1293 (origin/main 498a09aea97): solrUrls is a List<String> initialised to new ArrayList<>(), so it is never null. The reporter's array null check is not present.
- CloudSolrClient.java:1585: `if (!solrUrls.isEmpty()) providedOptions++;`. The builder now counts the options by emptiness, not by null.
- Whether the zkHosts path with a subclassed build() still throws at the pin was not run.

Commands at the pinned head:
- git grep -n 'solrUrls' origin/main -- solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java: lines 1293, 1330-1331, 1368, 1585.
- git log --oneline --grep='SOLR-14580' origin/main: no output.
- git log --oneline --grep='zkHosts\|CloudSolrClient.Builder' origin/main (bounded): SOLR-18130 (bf610531f1c, b2e1f8ed4a3) and SOLR-18368 (3ef3d093c06) change the builder; neither names this ticket and neither was checked as the fix.

## History

- Commits on origin/main naming SOLR-14580: none.
- The builder change that replaced the array check was not identified (a path-limited pickaxe was not run within the time limit).

## Reproduction attempts

Not attempted. The check is a builder call with zkHosts on a subclass that overrides build(), which needs a small test class; the run was not set up in this pass.

## Gap analysis

The builder check the ticket describes is gone from the pin, which suggests the bug may be fixed by a later builder change. No commit is named as the fix, the candidate refactors were not verified, and the reproduction was not run. So the fix cannot yet be cited.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-fixed does not hold: no commit names the ticket, and the candidate builder refactors are not verified as fixes. close-obsolete does not hold: the builder still exists and the zkHosts path was not run. close-cannot-reproduce does not hold: no reproduction was made. The missing pieces are (1) the commit that changed the builder check, found with a bounded search, and (2) a run of the zkHosts builder with a subclassed build() on the pin.

## Limits

This record does not reproduce the exception. It does not identify the commit that changed the check. It does not verify SOLR-18130 or SOLR-18368 as the fix.
