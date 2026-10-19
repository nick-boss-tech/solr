# Skip record: SOLR-16077

- Ticket: SOLR-16077, "Solr basic authentication is a bit strange". JIRA status Open, resolution none, fix versions none, created 2022-03-04, updated 2025-04-01. Linked issue SOLR-17724 (Closed) is the split for the documentation part. Checked 2026-10-19 UTC.
- Verdict: close-fixed (changed from keep-open; this redo supersedes the recorded verdict).
- Draft branch: solr-16077-draft-close (the earlier keep-open branch solr-16077-draft-open is not updated).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-16077-vm2.md).

## Ticket reading

The reporter asks for the Basic Authentication plugin documentation to explain how to produce the credentials string in security.json (base64(sha256(sha256(salt + password))) plus the salt), and to state which salt is used. The reporter found the answer only in the source. The description also raises that the only way to add users is an unauthenticated instance first. Five comments (2022-03-04 onward). The ticket names no test class.

## Code reading

- The fix is documentation. The page is solr/solr-ref-guide/modules/deployment-guide/pages/basic-authentication-plugin.adoc.
- Line 87 at origin/main 498a09aea97: "Solr stores the passwords in the format: `base64(sha256(sha256(salt+password))) base64(salt)`." This is the explanation the reporter asked for: the hash, the salt, and how they are stored together.
- The same page's security.json example (line 44) uses the credentials format described above.
- Line 88-89 point to a third-party web utility for encoding passwords, and say that the file must be edited by hand if security.json is changed directly.

Commands at the pinned head:
- git grep -n 'base64(sha256(sha256(salt+password)))' origin/main -- solr/solr-ref-guide/modules/deployment-guide/pages/basic-authentication-plugin.adoc: line 87.
- git grep -c 'clemente-biondo' origin/main -- the same page: 1 (the third-party encoder link is still present).

## History

- 84003aba0cb (2025-04-05): SOLR-17724: clarify basic auth credential docs (#3301). Changed basic-authentication-plugin.adoc (14 insertions, 4 deletions). Ancestor of the pinned head: yes (git merge-base --is-ancestor).
- Commits on origin/main naming SOLR-16077: none directly. The linked split issue is SOLR-17724.

## Reproduction attempts

Not applicable. The ticket is a documentation request, and the claim is that the documentation now states the format. The check is the text at the pinned head, recorded above.

## Gap analysis

The reporter's first ask (how to produce the credential string) is answered by the format line, which gives the exact composition. The second ask (which salt is used) is answered by the same line, since the salt is the base64 value appended to the hash. The page does not include a worked example, a code snippet, or the reporter's suggested steps to test a credential by hand. The reporter's comment that users cannot add users without an unauthenticated start is not addressed in this section. The ticket asked for an explanation, which the page now gives; a worked example is a further request not made in the ticket's core text.

## Verdict justification

close-fixed meets the rubric: a commit on main fixes the reported problem (84003aba0cb, SOLR-17724), the fixing commit is an ancestor of the pinned head, and the changed text is still present at the head (line 87). The minimum evidence for close-fixed is the fixing commit sha, the ancestor check, and the check that the changed code is still present; all three are recorded here. Requirement not met: the ticket's remark about adding users without an unauthenticated start is not covered; the verdict rests on the documentation request, not that remark.

## Limits

This record does not check the rendered documentation site. It does not check any other version of the guide. It does not test that a credential built by the stated formula is accepted by Solr.
