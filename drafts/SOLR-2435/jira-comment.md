The code this ticket describes was removed from Solr main, and no remaining use was found at 498a09aea9.

What we checked:

- Search at 498a09aea9: `git ls-tree -r 498a09aea9` shows no tracked `.jar` files and no `solr/contrib` directory. icu4j resolves only through `solr/modules/analysis-extras/build.gradle:25`.

If you still see this:

- Reopen with the Solr version and steps, and say which of the commits named here does not cover it.
