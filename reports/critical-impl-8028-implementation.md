# SOLR-8028 implementation verdict: NO NARROW FIX, NO BRANCH (Critical batch 2)

Ticket: ConcurrentModificationException thrown from JavaBinCodec.
Jira re-read this run: Open, Critical, reporter lvchuanwen, last
updated 2017-02-03. No fork branch, no PR, no prior implementation
claim or receipt on coordination-ui. The premise unit (report
reports/critical-impl-8028-premise-batch-2.md) reproduced the
mechanism; this unit settles the fix shape, as the premise report
required, and finds there is none narrow enough to build.

## The full thread, re-read this run

The ticket's own evidence never establishes a Solr-side defect.
The reporter ran a LoadRunner pressure test and never answered
whether their load code shared documents. Brian Feldman's
2017-02-03 follow-up adds a second stack with the same shape and
says his workaround is to catch the exception and retry adding
the documents, which usually succeeds on the retry. Mikhail
Khludnev asked the deciding question twice: is the load code
reusing a SolrInputDocument instance, or reusing field value
lists? Feldman's reply does not answer it: switching to a Guava
ImmutableList of SolrInputDocuments leaves every document's field
value lists mutable, so the field-list reuse hypothesis survives
his change untouched. Neither reporter ever answered, and no
maintainer has directed a fix shape since.

## Code facts at the batch base (apache main 83d560030fde)

- `SolrInputField.setValue` documents and implements backing-list
  adoption: "If a collection is given, then that collection will
  be used as the backing collection for the values."
- `JavaBinCodec.writeArray(Collection)` iterates the collection
  with a fail-fast iterator; `writeArray(List)` walks by index.
  Nothing on the marshal path copies or synchronizes.
- `SolrInputDocument` and `SolrInputField` carry no thread-safety
  wording and no synchronization anywhere: a plain mutable
  builder object, mutated through `setValue`, `addValue`, and
  through the live list `getValue` returns.
- The ticket's stack is client-side marshalling
  (`JavaBinUpdateRequestCodec` / `BinaryRequestWriter`), not a
  server path where Solr itself could be mutating a document
  during marshal. No in-tree trigger was found.

## Why each candidate shape fails as a narrow fix

(a) Snapshot copy in `writeArray(Collection)`: does not reliably
close the hole. A copy iterates too, so for most collection
types it throws the same CME; for ArrayList sources the copy is
CME-free only by accident of `toArray` not checking modCount,
and it can then be torn (mixed old and new values, or nulls),
turning a loud fail-fast exception into a silently sent document
that never existed. It also taxes every marshal of every
collection on a hot client path, and leaves the `List` index
walk (which throws IndexOutOfBoundsException instead) untouched.

(b) Copying caller collections in `setValue`: breaks documented
behavior (backing-list adoption) for every SolrJ user, and still
does not close the hole, because `getValue` returns the live
backing list and `addValue` mutates it in place. Closing it
fully means defensive copies at set, get, and add: a wide API
behavior change, not a narrow fix.

(c) Catch-and-retry inside the codec: Feldman's client-side
workaround, baked in. It masks caller misuse, can loop, and can
still send a torn document on the retry's own race.

(d) Deep-copying documents before marshal in the request writer:
copies every update request at full cost, and the copy itself
iterates the same fail-fast collections.

What remains is the contract the code already implies and the
ticket never refuted: a SolrInputDocument must not be mutated
while it is being marshalled, exactly as a HashMap must not be
mutated while it is being iterated. The CME is Java's fail-fast
behavior doing its job, not a Solr defect with a patchable site.

## Verdict

NO NARROW FIX, NO BRANCH. No gate ran: there is nothing to gate.
The ticket's open question belongs to the reporters (whose
answers never came) or to a maintainer directing a design: a
documented thread-safety contract, a copy-on-write document
type, or a deliberate decision that fail-fast is the behavior.
A one-line "not thread-safe" javadoc note is the only change
small enough to contemplate, and it resolves nothing on the
ticket, so it is named here as an option for Nick's call, not
built. No branch, no push, no PR, no Jira/GitHub post from
this unit.
