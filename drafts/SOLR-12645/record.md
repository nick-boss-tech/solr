# Skip record: SOLR-12645

- Ticket: SOLR-12645, MemClassLoader#loadResource() is broken.
- Verdict: close-obsolete.
- Draft branch: solr-12645-draft-close (verdict unchanged; this record replaces the earlier record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 08, item 9 (`assignments/pool-aaa-skip-verdict-remediation-08.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-08-08, updated 2019-06-08, no fix versions, no comments. The report, in the visible text: `MemClassLoader`'s `ResourceLoader#openResource()` returns null and ignores the data it fetched, which breaks the `ResourceLoader` contract, and it does not ask the parent loader first. The ticket names `MemClassLoader#loadResource()` as the symbol in question.

## Code reading

Read at the pin:

- Whole-tree search for the named class: `git grep -n 'MemClassLoader' 498a09aea97aac86c2cc7ac114113b946d249942 -- solr` returns zero hits (the grep used for the close-obsolete basis). The ticket's named symbol is therefore absent from the tree.
- The class was deleted by `6f2f1c3de2` ("SOLR-14654 Remove plugin loading from .system collection (for 9.0) (#1677)", 2020-08-06), which removed `solr/core/src/java/org/apache/solr/core/MemClassLoader.java` (203 lines). `git merge-base --is-ancestor 6f2f1c3de2 498a09aea97aac86c2cc7ac114113b946d249942` confirms it is an ancestor of the pin.
- The resource loader the ticket contrasts with is still present (`solr/core/src/java/org/apache/solr/core/SolrResourceLoader.java`); the defect was in the removed class, not in that loader.

## History

- `6f2f1c3de2` (SOLR-14654): ancestor of the pin; deletes the class.
- `321c4bbe1f` (SOLR-14654, ref guide and CHANGES cleanup): ancestor; not needed for the verdict.
- No commit at the pin names SOLR-12645 (the evidence tool lists none).

## Reproduction attempts

Not run in this record. The removed class cannot be exercised at the pin; the close rests on the removal and the empty grep, which the rubric accepts for close-obsolete.

## Gap analysis

- The ticket's named symbol is confirmed in the visible text, and the grep for it at the pin is empty.
- The removal commit is an ancestor of the pin, so the removed code is not on main.
- The surviving resource loader is a different class; the ticket does not ask about it.

## Verdict justification

close-obsolete: the class the ticket names (`MemClassLoader`) was removed from main by `6f2f1c3de2`, which is an ancestor of the pin, and a search of the whole solr tree for it at the pin finds no remaining use (zero hits).

Missing for this verdict: none for close-obsolete; the grep result and the removal commit are recorded above.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title and code path, as stated above.
</content>
