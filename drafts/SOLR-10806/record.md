# Skip record: SOLR-10806

- Ticket: SOLR-10806, Solr replica goes down with NumberFormatException: Invalid shift value (64) in prefixCoded bytes. JIRA read in this pass: Open, resolution none, fix versions none, 7 comments (2017-06-03 to 2017-06-07; the reporter's data-driven schema theory and the Lucene thread); updated 2019-06-08.
- Verdict: keep-open (unchanged status; this record replaces the earlier record on this branch, whose cross-audit reading named missing items J and C).
- Draft branch: solr-10806-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: nodes go down during indexing with NumberFormatException "Invalid shift value (64) in prefixCoded bytes (is encoded value really an INT?)". The reporter's theory is that data-driven schema guessed a field as long on one shard and as integer on another. The 2017 comments say the error comes from the Lucene numeric encoding when a field was indexed with a long/double type and read as an int/float type, and ask for the Lucene view.

## Code reading

- solr/core/src/java/org/apache/solr/legacy/LegacyNumericUtils.java:165-167 and :180: the exception with the ticket's message ("Invalid shift value (...) in prefixCoded bytes (is encoded value really an INT?)") is still in the tree, in Solr's copy of the legacy numeric utilities (the Trie fields).
- solr/core/src/java/org/apache/solr/update/processor/AddSchemaFieldsUpdateProcessorFactory.java is present: the data-driven schema path that chooses a field type from the first value it sees.
- Trie field types are in solr/core/src/java/org/apache/solr/schema/TrieField.java:90, TrieDateField.java:78, TrieDoubleField.java:53.

So the error site and the data-driven type guessing are both present at the pin. The mechanism the reporter proposes (different shards guessing different numeric types for the same field) is consistent with the code, but it is not shown by a run.

## History

- No commit at the pin names SOLR-10806 (git log --grep at the pin: none).
- The file history of LegacyNumericUtils.java was not traced in this pass.

## Reproduction attempts

Not run. A reproduction needs two shards (or two cores) that receive the same new field first as a long-valued and then as an integer-valued document under data-driven schema, then a commit and a search on the field, recording whether the prefixCoded exception appears.

## Gap analysis

The error site and the schema-guessing path are present at the pin, matching the ticket. Whether the type conflict across shards produces the exception is an open runtime question. The data-driven schema behavior is a design question the 2017 comments already raise.

## Verdict justification

keep-open: the mechanism is present at the pin, so close-fixed and close-obsolete are not supported. close-cannot-reproduce needs a run attempt, and none was made.

Missing for a closing verdict: a two-core data-driven run at the pin with a field first seen as long on one core and integer on the other, recording whether the exception occurs; a non-occurrence over repeated runs supports close-cannot-reproduce.

## Limits

This record does not run the reproduction, does not trace the Lucene side, and does not post anything to JIRA or GitHub.
