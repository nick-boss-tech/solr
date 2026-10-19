We checked Solr main at commit 498a09aea9 (October 7, 2026). When a multi-word synonym rule is applied at query time, Solr analyzes the whole phrase as one unit, which is why the rule requires all of the original words to match. The reference guide describes that mechanism, but it does not say that single-word matches are lost, so we are leaving the ticket open for a decision on the intended behavior.

What we checked:

- The reference guide text on how the query parser passes word sequences to analysis, and the note on multi-term synonym support in the synonym filter.
- The comments on this ticket, including the explanation that the behavior is intended.

How we tried to reproduce it:

- We did not run a reproduction for this check.

What this means:

- The matching behavior follows from the synonym design, and the workaround of directional rules is one way to keep single-word matches. Whether this should be documented as the intended behavior, or changed, has not been decided.

If you still see this:

- Please send the synonym rule you use, the query, and the documents you expect to match. With those, we can check whether the directional rules give the result you need.
