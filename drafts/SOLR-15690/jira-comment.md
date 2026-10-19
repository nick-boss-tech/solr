We checked this against Solr main at commit 498a09aea97 and found that the file named in this report is no longer read by Solr.

What we checked:

- The datasource.xml file belongs to DataImportHandler, which was removed from Solr in 2020 (SOLR-14783, removing it from Solr 9.0). A search of the current code finds no reference to datasource.xml.

How we tried to reproduce it:

- Not applicable. Solr no longer reads this file, so the reported display cannot be produced on the current code.

What this means:

- This report is about a component that Solr no longer ships. We cannot say from here whether the older release that carried it shows the same stale display.

If you still see this:

- If you use DataImportHandler from an older release, the Solr version and the steps that show the stale value will help. A supported release, or a third-party replacement, is the way forward.
