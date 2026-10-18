Checked on main. Commit 7c4584bd4f changed `MetricsHandlerTest` to use `solrconfig-minimal.xml` and to wait until the core is up before the test starts (`solr/core/src/test/org/apache/solr/handler/admin/MetricsHandlerTest.java`, line 40). The reporter said in 2018 that the test no longer fails, and the change was committed on that basis.

If you still see this:

- Reopen with the Solr version and steps, and say which of the commits named here does not cover it.
