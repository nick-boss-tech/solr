# Round 36 handoff updates

Tip changes and pin updates after the original handoff commit
(69ac08c4b14). The TSV in this directory is kept current; this
file is the running record of what changed and why.

- 2026-10-07 ~23:53 MDT: solr-11479-submit moved from
  e6fdc9ed5d50 to 7e538e844c45. The new commit sits directly on
  the pinned head, is authored by the owner, and touches only
  AddReplicaCmd.java (reject property.coreNodeName when several
  replicas are requested). Pin updated in the TSV; review the
  branch at the new head.
