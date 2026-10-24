# Jira comment EDIT draft: SOLR-17988, comment 18131215

Status: DRAFT (not applied). This is an edit of Nick Shanin's own posted comment 18131215 (2026-10-24), whose text calls the ticket complete and then corrects itself in one line. The ticket's only watchers are Eric Pugh and Nick, so the edit's notification cost is the small case the owner has already accepted for this ticket. Applying the edit needs Nick Shanin's explicit go.

Current text of 18131215:

> 🤖 _AI text below_ 🤖 _(reviewed by Nick Shanin)_
>
> Status check on this ticket: Solr 10.0.0 has been released, and the property name changes are covered in the Solr 10 major changes and upgrade notes. If specific property transitions are still missing from the docs, naming them here would let someone pick that up; otherwise this looks complete with the release.
>
> Update: Actually the above is wrong and I'm working on it.

Replacement text:

---

Correcting my status note from earlier today: the property transitions are not all covered. I checked the Solr 10 upgrade notes against the code and found transitions missing, and a set of system properties that 10.0 supports but the Ref Guide does not document.

The upgrade-notes fixes are assembled on a branch ({{solr-17988-docs}}), and the Ref Guide builds clean at that head: the site builds, the link check passes over 858 files with no errors, and the CLI docs check is current. A PR with those changes is next.

The undocumented system properties are a larger set. The minimum worth documenting is 21 entries; I will put those in a second PR stacked on the first, and the rest can be named here as a follow-up if anyone wants them.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
