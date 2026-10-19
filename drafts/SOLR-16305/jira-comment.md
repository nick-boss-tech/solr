We checked Solr main at commit 498a09aea9 (October 7, 2026). Since this ticket was opened, a partial change (SOLR-17006, in October 2023) stores user-defined properties in the collection's state at creation time. The gap this ticket describes is still there: a property changed with MODIFYCOLLECTION does not reach the cores' configuration variables, while the same property set at creation does.

What we checked:

- The collection creation path, which passes the property values to the cores.
- The collection modify path, which stores the change but does not update the cores.
- The partial change from SOLR-17006, which covers creation only.

How we tried to reproduce it:

- We did not run a cluster test for this check.

What this means:

- Fixing the rest of this means choosing where a collection property should live and whether a modify should update the cores. The comments on this ticket do not yet agree on that, so we are leaving it open for a decision on the property API.

If you still see this:

- Please tell us the Solr version, the collection and configset you use, and whether you need the modified value to take effect without a reload. That will help us check the cases the partial change does not cover.
