# SOLR-8028 premise verdict: REPRODUCED (Critical batch 2, unit 7)

Ticket: ConcurrentModificationException thrown from JavaBinCodec.
Jira re-read this run: Open, Critical, reporter lvchuanwen, last
updated 2017-02-03. No fork branch, no PR, no prior premise report
or claim on coordination-ui. The 2017 thread ends with Mikhail
Khludnev's unanswered question: is the trigger reusing a
SolrInputDocument instance or reusing field value lists?

## What ran

Scratch probe only, never committed, fresh worktree at apache main
83d560030fde (base used by the whole batch-2 run). Plain JUnit
probe in the solrj test tree (no core boot): a SolrInputDocument
with one multi-valued field whose backing list is a plain
ArrayList of 20,000 strings, marshalled through
`new JavaBinCodec().marshal(doc, os)` (one codec per marshal, as
JavaBinUpdateRequestCodec does). Command:
`:solr:solrj:test --tests
"org.apache.solr.common.util.Solr8028PremiseProbeTest"
-Ptests.seed=8028C0FFEE8028`. BUILD SUCCESSFUL, 3 tests, 0
failures. Fresh JUnit XML 2026-11-01T21:28:12Z (copy kept at
~/workspace/tools/8028-premise-TEST-Solr8028PremiseProbeTest.xml;
log ~/workspace/tools/8028-premise-probe.log).

## Verdict: reproduced, under exactly one sharing shape

- Concurrent mutation leg: one thread marshalling the shared
  document while another thread structurally modifies the shared
  backing list (add, then remove to hold size) threw
  ConcurrentModificationException on the first marshal, from the
  ticket's own call shape:
  `ArrayList$Itr.checkForComodification/next ->
  JavaBinCodec.writeArray:957 -> writeKnownType:382 -> writeVal:261
  -> BinEntryWriter.put -> SolrInputDocument.writeMap:64 ->
  writeSolrInputDocument:821 -> writeKnownType:395 -> writeVal ->
  marshal:171`.
  The ticket's 2015/2017 stacks show the same shape
  (`ArrayList$Itr.next -> writeArray -> writeKnownType -> writeVal
  -> writeSolrInputDocument -> ... -> marshal`).
- Control, sequential marshal: 50/50 OK, no exception. Marshal
  itself is not broken.
- Control, concurrent marshal without mutation: 4 threads x 200
  marshals of the same shared document, no exception. Concurrent
  marshalling alone is safe; the trigger is modification during
  marshalling.

## Code path on current main

`SolrInputField.setValue` keeps a caller-supplied collection as
the backing list ("that collection will be used as the backing
collection for the values"), and `writeSolrInputDocument` walks
fields through `writeMap`, where a collection field value reaches
`writeArray(Collection)`, which iterates with a fail-fast
iterator. Nothing on the marshal path copies or synchronizes.
So Khludnev's 2017 hypothesis is confirmed as the mechanism:
whoever mutates a document's field value list (or reuses the
document across threads while one thread still mutates it)
while another thread marshals it gets this CME. The ticket's
pressure-test reporter never answered whether their load code
shared documents, so the caller side of the trigger is still
unconfirmed for their case, but the mechanism is now proven.

## Caution for the implementation unit

A reproduced premise does not yet settle a fix shape, and this
one has a design question, not just a patch. Candidate shapes:
(a) a defensive snapshot copy in `writeArray(Collection)` so a
marshal never throws CME (marshals a possibly torn snapshot;
cost on every marshal); (b) copying caller collections in
`SolrInputField.setValue` instead of adopting them as backing
lists (a wider behavior change for every SolrJ user); (c) a
documented thread-safety contract (SolrInputDocument is not
thread-safe; do not mutate during marshal) with no code change.
Which of these, if any, is the right fix is the implementation
unit's first decision; it should not be treated as a small
narrow fix until that call is made. No branch, no push, no PR,
no Jira/GitHub post from this unit.
