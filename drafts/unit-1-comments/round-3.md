# Round 3 drafts: 20 remaining not-a-bug records, and 7 duplicate candidates (vm5, 2026-10-26)

Nothing is posted. Drafts use a different opening from the round 1 and 2 template, because these were not code-checked against current main. Each closure draft says only what the ticket and its discussion show.

## Not-a-bug drafts (19)

### SOLR-1780
I reviewed this ticket and the code path it names (AddUpdateCommand.extractIdsIfNeeded). It rejects more than one uniqueKey value whatever overwrite is set, and only a missing key is allowed when overwrite=false. That was agreed as the intended behaviour. I plan to close it as not a bug unless you see a case that breaks it.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-5376
I reviewed this ticket. The code downgrades FCS to FC for multi-token fields on purpose, and debug=facet shows the requested and applied method. Raising an error would break existing clients. I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-5878
I reviewed this ticket and the distributed group.format=simple path. Rows bounds the number of groups, the same as non-distributed search, so the behaviour is by design. I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-6922
I reviewed this ticket. It asks for a spatial example dataset and schema, with no malfunction reported. That is an enhancement, not a bug. I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-9352
I reviewed this ticket. A bare * with df is a match-all query by design, as discussed in SOLR-2996. I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-10284
I reviewed this ticket. The reporter says it is not a bug, and the discussion is about a policy for standalone ZooKeeper with an ensemble string. That is a design choice, not a demonstrated regression. I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-12363
I reviewed this ticket. Random sort is seeded per docId, so cursors are unstable across replicas by design. A test would need a schema change, which is a design question. I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-14140
I reviewed this ticket. It is an umbrella asking to identify plugin types that need package loading, and it names no failing plugin or regression. It needs splitting into feature tickets, not a bug fix. I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-14282
I reviewed this ticket. Returning the input document without copyField targets from /get is by design (SOLR-3743), and the request is an improvement. I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-14530
I reviewed this ticket. It is a ShardHandler refactor, and the ticket says it is not a bug. I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-14966
I reviewed this ticket. The ticket describes a minor optimization, not a bug. I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-15304
I reviewed this ticket. It proposes a permission hierarchy, which is a feature request, not a bug. I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-15388
I reviewed this ticket. The PKIAuthenticationPlugin behaviour is an architectural question, and the reviewer proposed not a bug. No concrete defect is named. I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-15522
I reviewed this ticket. Removing solr.pid on stop is by design, and no defect is named. I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-16548
I reviewed this ticket and its discussion. The filterCache statistics are explained by queryResultCache, and the related test passes. I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-16652
I reviewed this ticket. Multi-term synonyms need the original terms at query time (Lucene QueryBuilder behaviour, see TestQueryParser), and that was confirmed in the discussion. I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-17765
I reviewed this ticket. It is an improvement idea, and the maintainer discussion pushed back on it. I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-17838
I reviewed this ticket. It is the SIP-25 encryption umbrella and asks for subtasks. It gives no reproducible failure, so it is feature tracking, not a bug. I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### SOLR-18444
I reviewed this ticket. It proposes a coordinated autoCommit feature, not a bug. I plan to close it as not a bug.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

## Repro needed (1)

### SOLR-4277
Audit note: the spellchecker behaviour needs a current-version repro. No comment until the repro is done.

## Duplicate candidates (7): no target recorded, not drafted

Pack rule: the target must be recorded and closed or fixed, or the live original. None of these meets that:

- SOLR-3765: no duplicate link on JIRA.
- SOLR-5772: only references SOLR-5211 and SOLR-10144, neither a duplicate.
- SOLR-7721: no duplicate link.
- SOLR-9089: no duplicate link.
- SOLR-11034: no duplicate link; related to closed SOLR-9843 and SOLR-11035.
- SOLR-11787: a 2024 comment asks whether it duplicates SOLR-6213; unanswered.
- SOLR-13470: no duplicate link; linked to open SOLR-13471 and closed SOLR-13474 as problem/incident.
