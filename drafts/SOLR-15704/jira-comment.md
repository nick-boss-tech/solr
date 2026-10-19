We checked this against Solr main at commit 498a09aea97 and found that DataImportHandler is no longer part of Solr.

What we checked:

- DataImportHandler was removed from Solr in 2020 (SOLR-14783, removing it from Solr 9.0). A search of the current code finds no DataImportHandler files.

How we tried to reproduce it:

- Not applicable. The component is not in the current code, so the reported behaviour cannot run on it.

What this means:

- This report is about a component that Solr no longer ships. We cannot say from here whether the behaviour exists in the older release that still had it.

If you still see this:

- If you use DataImportHandler from an older release, the Solr version and the sequence of calls that fails will help. Using a supported release, or a third-party replacement, is the way to move forward.
