# Skip record: SOLR-12688

- Ticket: SOLR-12688, LTR Multiple performance fixes + pure DocValues support for FieldValueFeature.
- Verdict: keep-open.
- Draft branch: solr-12688-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 08, item 6 (`assignments/pool-aaa-skip-verdict-remediation-08.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-08-21, updated 2019-06-08, no fix versions, no links. The umbrella names three problems in the LTR `FieldValueFeature`: pure DocValues fields (stored false) are not supported; fields that are both stored and DocValues extract one field from the stored document (a performance issue); and the `LTRScoringModel` is mutable and recomputes its hash code per query. Seven comments (2018-08-21 to 2019-05-18): the reporter attaches patches and proposes separate tickets, cpoerschke says he will review, and a 2019 exchange discusses the EFI parameters on the model.

## Code reading

Read at the pin:

- Pure DocValues support (the first problem): SOLR-12697 is in the pin's history as `3db4cdd7d0` ("In contrib/ltr FieldValueFeature support "stored=false docValues=true" a.k.a. pure DocValues fields"), an ancestor of the pin. `solr/modules/ltr/src/java/org/apache/solr/ltr/feature/FieldValueFeature.java` reads doc values through `NumericDocValues` and `SortedDocValues` scorers.
- The immutable model and cached hash code (the third problem): SOLR-12699 is in the pin as `be65b95e80`, an ancestor of the pin.
- The multi-valued case: `solr/modules/ltr/src/test/org/apache/solr/ltr/feature/TestFieldValueFeature.java:514-546` (`testThatExceptionIsThrownForUnsupportedType`) asserts that a `SORTED_SET` (multi-valued) docValues field raises `IllegalArgumentException: Doc values type SORTED_SET of field dvStringPopularities is not supported`. So the multi-valued restriction is pinned by a test.
- The performance fix for fields that are both stored and DocValues (the second problem): SOLR-12698 is not in the pin's history (`git log --grep=SOLR-12698` returns nothing), and the stored-and-DocValues extraction path was not traced in this pass.

## History

- `3db4cdd7d0` (SOLR-12697): ancestor of the pin.
- `be65b95e80` (SOLR-12699): ancestor of the pin.
- SOLR-12698: no commit at the pin.

## Reproduction attempts

Not run. This pass ran no builds or tests. The second problem is a performance question: a run would measure the extraction for a stored-and-DocValues field against the DocValues path.

## Gap analysis

- Two of the three sub-problems have commits in the pin (pure DocValues and the immutable model).
- The performance fix for stored-and-DocValues fields (SOLR-12698) has no commit at the pin, so that part is open.
- Multi-valued DocValues support is refused by a test; whether to support it is a design decision the ticket does not settle.

## Verdict justification

keep-open: the umbrella is not complete (the stored-and-DocValues performance fix has no commit at the pin), and the multi-valued restriction is a design question pinned by a test. close-fixed does not fit as an umbrella; close-obsolete does not fit (the feature is present).

Missing for this verdict: a commit (or a decision that none is needed) for the stored-and-DocValues performance fix, and a maintainer decision on multi-valued DocValues in FieldValueFeature.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
