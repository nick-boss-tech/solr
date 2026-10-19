# Skip record: SOLR-15801

- Ticket: SOLR-15801, "Ref Guide docs on Java support out of date". JIRA status Open, resolution none, fix versions none, created 2021-11-16, updated 2021-11-17. Checked 2026-10-19 UTC.
- Verdict: close-fixed (the recorded verdict was close-answered; the docs fix that answers the ticket is a documentation commit, so the rubric's close-fixed applies. Draft branch suffix stays close).
- Draft branch: solr-15801-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-15801-vm2.md).

## Ticket reading

The ticket says the Java requirements in the reference guide (system_requirements.adoc) still list Java versions tested in 2019 (Solr 8.x against Java 8 to 13) and that this is out of date. It quotes the 2019 text from SOLR-12809. It has no comments.

## Code reading

- Page: solr/solr-ref-guide/modules/deployment-guide/pages/system-requirements.adoc (origin/main 498a09aea97). Line 30 now reads "You will need the Java Runtime Environment (JRE) version 21 or higher."
- The 2019 list ("Solr 8.x is currently tested against Java 8, 9, 10, 11, 12 and (pre-release) 13") is gone: `git show origin/main:<page> | grep -c 'Java 8, 9, 10, 11, 12'` returns 0.
- The fix commit that replaced the list is bea30b959f9 (2022-04-08, "Update ref-guide system requirements page", #795). It is an ancestor of the pinned head (git merge-base --is-ancestor). It was made after the ticket was filed (2021-11-16). It states the minimum Java version per Solr release and removes the old per-version testing list.
- Later updates to the same page: 45fa999f6ec (2024-03-22), 50d348474c9 (2024-05-17), d840a3bef65 (2024-10-24, "Bump up Java version to 21"). They keep the page current.

Commands at the pinned head:
- git log --format='%h %ad %s' --date=short origin/main -- solr/solr-ref-guide/modules/deployment-guide/pages/system-requirements.adoc: the commits above.
- git grep -c 'Java 8, 9, 10, 11, 12' origin/main -- solr/solr-ref-guide/modules/deployment-guide/pages/system-requirements.adoc: 0.

## History

- bea30b959f9 (2022-04-08): the docs fix; ancestor of the pinned head.
- Commits on origin/main naming SOLR-15801: none directly.

## Reproduction attempts

Not applicable. The claim is about the documentation text, and the check is the text at the pinned head, recorded above.

## Gap analysis

The stale statement the ticket describes is no longer on the page. The page now gives the Java requirement as a minimum (21 or higher) and describes the minimum per release, so the ticket's concern (a list that goes out of date) is addressed in structure, not only in content. The page is kept current by later commits.

## Verdict justification

close-fixed meets the rubric: a commit on main fixes the reported problem (bea30b959f9, after the ticket was filed), the commit is an ancestor of the pinned head, and the changed text is still present (the page states Java 21, and the old list text is absent). The minimum evidence is the fixing commit sha, the ancestor check, and the check that the changed text is still present; all three are recorded.

## Limits

This record does not check the rendered documentation site. It does not check the Solr release notes. It does not verify the Java 21 requirement against the build.
