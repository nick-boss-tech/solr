# Skip verdict remediation, batch 6

One line per item: ticket, claim file, verdict recorded, new draft branch head, and the evidence the record cites.

- SOLR-13698 | claims/skip-SOLR-13698-vm5.md | keep-open on solr-13698-draft-open, head 2eb7f8d2d9 (unchanged verdict) | Jira read 2026-10-19: Open, no resolution; SuggestComponent.java:217-229 distributed suggest; HttpSolrCall.java:763 (called :318); suggest test files 34, alias mention 0; run not done
- SOLR-13687 | claims/skip-SOLR-13687-vm5.md | keep-open on solr-13687-draft-open, head 5ca377d998 (unchanged verdict) | Jira read 2026-10-19: Resolved/Fixed by comment only; ApiTool.java:38, :82-84; AssertTool.java:320-322; URL support in 44668dce33 and ddfe2f1b44; per-tool option grep Healthcheck 0, Delete 0; per-command run and fixing commit missing
- SOLR-13678 | claims/skip-SOLR-13678-vm5.md | close-fixed on solr-13678-draft-close, head 801ba6a830 (unchanged verdict; earlier draft replaced) | Jira read 2026-10-19: Open, no resolution; fix commit 67a642fe02 (SOLR-17720) ancestor of 498a09aea97aac86c2cc7ac114113b946d249942; CollectionPropertiesZkStateReader.java:44-45 lock taken outside compute(); a052fb5436 is test hardening only; deadlock not run
- SOLR-13654 | claims/skip-SOLR-13654-vm5.md | keep-open on solr-13654-draft-open, head 30b5bdd5da (unchanged verdict; earlier record replaced) | Jira read 2026-10-19: Open, no resolution; coreNodeName guard present at CoreContainer.java:1545-1547 (earlier error-removed claim corrected); collection-create trace and SplitShardTest run not done
