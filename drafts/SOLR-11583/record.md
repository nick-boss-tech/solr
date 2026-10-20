# Skip record: SOLR-11583

- Ticket: SOLR-11583, SolrJ to have a memory efficient mode for consuming response minimizing creating objects. JIRA read in this pass: Open, resolution none, fix versions none, 0 comments; updated 2019-06-08.
- Verdict: keep-open (unchanged status; this record replaces the bulk audit-derived record on this branch).
- Draft branch: solr-11583-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket is an enhancement request for SolrJ response parsing: (1) use CharSequence values backed by offsets into the response byte array instead of Strings, copying bytes when a value is written on, and (2) read one object at a time instead of building containers (Maps, Lists, NamedLists) for short-lived data. It has no comments.

## Code reading

- Part of (1) is present: solr/solrj/src/java/org/apache/solr/common/util/ByteArrayUtf8CharSequence.java exists, and JavaBinCodec.java:19 imports convertCharSeq from it, with a stringProvider (:1149) and a ByteArrayUtf8CharSequence built over the input (:1158). The javabin path therefore reads some strings as byte-backed sequences.
- Part of (2) is present in a narrow form: solr/solrj/src/java/org/apache/solr/client/solrj/response/StreamingResponseCallback.java (a callback for streaming responses) and HttpJdkSolrClient.java:214 (wantStream checks the parser). A caller can receive the response as a stream instead of a parsed object.
- The default response parsing still builds NamedList and Map containers for the response, and the byte-backed sequences are converted to String at the default boundary (the stringProvider). The ticket's full goal (no container objects for the default path, and strings kept as sequences through the response API) is not present.

So the ticket is partly implemented at the pin (byte-backed sequences in the javabin codec; a streaming callback), and the default container-building path the ticket asks to avoid remains.

## History

- No commit at the pin names SOLR-11583 (git log --grep at the pin: none).
- The commit that introduced ByteArrayUtf8CharSequence was not traced in this pass.

## Reproduction attempts

Not applicable as a reproduction: this is an enhancement. A measurement (allocation profile of a large query response parsed with the default parser and with the streaming callback) would show how much remains; not run in this pass.

## Gap analysis

The enhancement is partly delivered at the pin. What remains is the default parsing path that builds containers and converts sequences to strings. Whether that is still wanted, and what scope a close would cover, is a maintainer decision.

## Verdict justification

keep-open: the request is only partly implemented at the pin, so close-fixed is not supported (no commit names the ticket, and the containers path remains). close-obsolete is not supported (the feature is still a live goal, and the code it touches is present). close-answered is not supported (the ticket asks for a change, not an answer).

Missing for a closing verdict: a maintainer decision on whether the default container-free path is still wanted (then keep-open as a feature with the remaining scope named, or close as partly delivered by the javabin byte-backed sequences and the streaming callback).

## Limits

This record does not measure allocations, does not trace the history of ByteArrayUtf8CharSequence, and does not post anything to JIRA or GitHub.
