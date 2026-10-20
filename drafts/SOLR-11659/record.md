# Skip record: SOLR-11659

- Ticket: SOLR-11659, JMX Metrics ObjectName needs comma escaping for /solr/col1,col2/select scenario. JIRA read in this pass: Open, resolution none, fix versions none, 0 comments; updated 2019-06-08.
- Verdict: close-obsolete (unchanged status; this record replaces the earlier record on this branch, whose grep result was taken from an earlier reading and not re-run).
- Draft branch: solr-11659-draft-close (unchanged status, updated).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: with the multi-collection request form /solr/col1,col2/select (SOLR-11444), the JMX metrics reporter builds an ObjectName that includes the comma-separated collection list, and JmxObjectNameFactory.createName throws a RuntimeException (seen in AliasIntegrationTest). The reporter asks that the comma be escaped in the ObjectName. The description was read in full through the Jira REST record.

## Code reading

- The named classes are absent from the whole solr tree at the pin: JmxObjectNameFactory, JmxMetricsReporter, and the onTimerAdded and reporters.jmx references all return 0 hits (git grep -- solr).
- The removal: commit 3d35f4defe (SOLR-17827, "Remove JXM, SLF4J and Graphite reporters", 2025-08-06) deleted solr/core/src/java/org/apache/solr/metrics/reporters/SolrJmxReporter.java, solr/core/src/java/org/apache/solr/metrics/reporters/jmx/JmxMetricsReporter.java, JmxObjectNameFactory.java, package-info.java, and the TestJmxIntegration test. Ancestry checked: 3d35f4defe is an ancestor of the pin.
- The remaining createName methods at the pin (solr/core/src/java/org/apache/solr/core/CorePropertiesLocator.java:235 and solr/test-framework/src/java/org/apache/solr/util/ErrorLogMuter.java:75) are unrelated to ObjectName creation.

So the code path the ticket names (the JMX ObjectName factory that the comma-containing name goes through) no longer exists at the pin.

## History

- No commit at the pin names SOLR-11659 (git log --grep at the pin: none).
- SOLR-17827 (3d35f4defe) is the removal the verdict rests on.

## Reproduction attempts

No run was attempted. For close-obsolete the rubric's check is the removed code and the whole-tree grep, which are done above.

## Gap analysis

The ticket's failure is in JMX reporting, which the pin no longer contains. Solr's metrics are still exposed by other reporters; whether a comma-containing collection list can break those is a different question and would need a new ticket if it arises.

## Verdict justification

close-obsolete: the named classes are absent from the whole tree, and the removing commit 3d35f4defe (SOLR-17827) is an ancestor of the pin. The rubric's close-obsolete check is met.

## Limits

This record does not run AliasIntegrationTest, does not check the other metrics reporters with a comma-containing collection name, and does not post anything to JIRA or GitHub.
