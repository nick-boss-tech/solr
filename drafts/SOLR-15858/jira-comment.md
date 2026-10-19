We checked this against Solr main at commit 498a09aea97 and found that the code this ticket describes no longer exists.

What we checked:

- The method named in the report (ConfigSetsHandler.createZkNodeIfNotExistsAndSetData) has been removed from Solr main. A search of the current code finds no remaining use. It was removed in April 2021, in the change that moved configset handling to the ConfigSetService (SOLR-15258), which was before this ticket was filed.
- Configset uploads now write each file with its full path and create missing parent nodes in ZooKeeper, so a file in a sub-folder does not need an explicit directory entry in the zip.

How we tried to reproduce it:

- We did not run an upload on the current code in this pass. The conclusion rests on the code path above.

What this means:

- The failure in the report comes from code that is no longer in Solr main. It is most likely specific to an older release.

If you still see this:

- Please send the Solr version, the configset upload command, and the zip structure that fails, and we will check it against the current code. Please reopen with that information.
