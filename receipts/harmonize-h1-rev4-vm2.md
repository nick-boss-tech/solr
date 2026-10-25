# Harmonize H1 rev4: correction receipt (vm2)

rev4 (`material/harmonized-backlog/batches/h1-NNN-rev4.json`, 16 batches) corrects rev3 in one rule. Rev3 treated `ping-stale` as a closure family, so it barred bugs from it. The assignment bars bugs only from the four closure families (closure-fixed, closure-obsolete, closure-not-a-bug, closure-duplicate). Critical and Blocker tickets are barred from all families.

Effect: 11 `ping-stale` records are now family-eligible and no longer carry a bug conflict flag. Dispositions, sources, branch and PR state are unchanged from rev3.

Eligible per family in rev4: ping-stale 11; closure-fixed, closure-obsolete, closure-not-a-bug and closure-duplicate 0 (all 281 closure-disposition records are Bugs). The 281 are flagged as bug conflicts for the severity screen.

Rev3 remains as published.
