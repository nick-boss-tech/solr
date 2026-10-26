# Unit 1 comment drafts, round 1 (vm5, 2026-10-26)

Nothing is posted. Each draft needs a live Jira check at fill time and owner approval before posting. Evidence was checked against apache/main `b73f03976fa`, file names as cited.

## Sorted: 20 tickets

| Group | Action | Tickets |
|---|---|---|
| Closure (citable basis) | Draft as not-a-bug closure | SOLR-2420, SOLR-2516, SOLR-3473, SOLR-3539, SOLR-4558 |
| Maintainer call | Draft a question with a decision ladder | SOLR-2855, SOLR-3008, SOLR-3744, SOLR-4023, SOLR-4100, SOLR-4674 |
| Repro needed | No comment yet. Assign repro to the pool; draft after the repro | SOLR-3407, SOLR-3676, SOLR-3945, SOLR-4021, SOLR-4130, SOLR-4486, SOLR-4669 |
| Inconclusive | No comment | SOLR-1557, SOLR-3721 |

Closure drafts use the family template with the "I looked at this against current main" line, since each one has a code citation. Maintainer drafts do not claim a code check beyond what is cited.

## Closure drafts

### SOLR-2420 (term vector response format)
[~{REPORTER}] I looked at this against current main. The term vector component still returns a NamedList per document (TermVectorComponent.java, around line 134). This is a request to change the response format, not a malfunction. If you see a real defect on a supported release, reply and I will reopen it. Otherwise I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-2516 (stop caching searchers)
[~{REPORTER}] I looked at this against current main. Searchers are still managed by SolrCore (searcher handling in SolrCore.java). This describes a design change, not a defect. If you see a real defect on a supported release, reply and I will reopen it. Otherwise I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-3473 (dedup with non-uniqueKey signatureField)
[~{REPORTER}] I looked at this against current main. SignatureUpdateProcessorFactory.java (lines 92 and 100) now rejects the unsafe combinations in SolrCloud: overwriteDupes needs the signature field to be the uniqueKey. The safe setups described in the latest comment are the ones the code allows. If you see the unsafe case still succeed on a supported release, reply and I will reopen it. Otherwise I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-3539 (rethink softCommit and openSearcher)
[~{REPORTER}] I looked at this against current main. Both parameters exist on the update handler (UpdateRequestHandler.java, SOFT_COMMIT at line 56). This ticket asks for a design rethink, not a fix to a defect. If you have a concrete problem with the current parameters, reply and I will reopen it. Otherwise I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-4558 (raw query parser cannot parse "{!")
[~{REPORTER}] I looked at this against current main. Local params are parsed in QParser before the parser plugin sees the string (QParser.java, around lines 81 to 90). That is the designed order, so the plugin never gets "{!". Parameter dereferencing is the supported workaround. This is a feature request, not a malfunction. If you need a different escape, reply and I will reopen it. Otherwise I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

## Maintainer-call drafts (decision ladder)

Each draft asks one question and lists the three options. No option is a closure until a maintainer answers.

### SOLR-2855 (huge range facets exhaust memory)
[~{REPORTER}] Thanks for the detail. The overflow was in client code, but the server allocated memory for a very large range facet. I could not find a cap on the number of range buckets in the current range facet code. Maintainers, which should this be?
1. Close: the server behaviour is intended, document the limit.
2. Keep open: add a cap or an error for very large range requests.
3. Split: close this ticket and open a new one for the cap.
Reply with 1, 2 or 3.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-3008 (edismax pf with a quoted phrase)
[~{REPORTER}] The current code skips explicit phrase clauses when it builds pf shingles (ExtendedDismaxQParser.java, around line 308). So the behaviour is deliberate, but the ticket disputes the design. Maintainers, which should this be?
1. Close: keep the current design and document it.
2. Keep open: change pf handling for phrases.
3. Split: close this ticket and open a feature request.
Reply with 1, 2 or 3.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-3744 (pure negative queries inside parentheses)
[~{REPORTER}] The top-level pure negative query works, but the report is that nested ones inside parentheses do not. I have not yet confirmed which layer causes it. Maintainers, which should this be?
1. Close: the nested behaviour follows the Lucene classic syntax.
2. Keep open: make nested pure negatives match the top level.
3. Repro first: the pool confirms the behaviour, then we decide.
Reply with 1, 2 or 3.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-4023 (boolean precedence in the default parser)
[~{REPORTER}] The default operator comes from q.op (LuceneQParser.java, line 44). The precedence of mixed AND and OR is the Lucene classic syntax that this ticket describes. Maintainers, which should this be?
1. Close: classic syntax is intended, document it and recommend parentheses.
2. Keep open: change the precedence in the Solr default parser.
3. Split: close this ticket and open a documentation ticket.
Reply with 1, 2 or 3.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-4100 (commitWithin soft commit is not replicated)
[~{REPORTER}] Replication serves the latest commit point (ReplicationHandler.java, around line 756). A soft commit does not create a new commit point, so the slave does not see it. Maintainers, which should this be?
1. Close: replication is for hard commits by design.
2. Keep open: make soft commits visible to replication.
3. Split: close this ticket and document the behaviour.
Reply with 1, 2 or 3.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-4674 (repeaters must replicate on startup for config files)
[~{REPORTER}] I have not yet checked the startup path for repeaters in the current code. Maintainers, which should this be?
1. Close: repeaters are expected to be configured to replicate on startup.
2. Keep open: make config replication work without that setting.
3. Repro first: the pool checks the repeater setup, then we decide.
Reply with 1, 2 or 3.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

## Repro needed (no comment until the repro is done)

Assign to the pool as repro tasks. Draft the comment after the repro result is known.

- SOLR-3407: `fl` with digit-leading names. Repro on current main.
- SOLR-3676: lowercased uniqueKey allows duplicates on update. Repro on current main.
- SOLR-3945: SolrQuery.setFacet(false) removes the parameter, so the handler default cannot be turned off from SolrJ (SolrQuery.java, lines 370 to 374). Repro the SolrJ request.
- SOLR-4021: JavaBinCodec round-trip for unrecognized classes. Repro.
- SOLR-4130: eDismax phrase boost drops terms inside parentheses. Repro.
- SOLR-4486: batch command order in SolrCloud. Repro.
- SOLR-4669: config replication can load the index before the new core config. Repro.

## No comment

- SOLR-1557: shards parsing. The split step was not located; inconclusive.
- SOLR-3721: concurrent recoveries. The recovery guard was not confirmed; inconclusive.
