# Jira comment draft: SOLR-17887, round 2

Status: DRAFT (not posted). Refresh of the round-1 draft at Nick Shanin's direction on 2026-10-26: branch and diff URLs added in the SOLR-17861 round-2 pattern. Addressee: Christos Malliaridis (reporter). Posting needs Nick Shanin's explicit go.

Known caveat, verified 2026-10-26: the branch tip `9c7f72aa0e521151d4fcb3f662c59927a87866c0` still carries the factory working file `pr-draft/SOLR-17887.md` (+37), which the diff link below will show. Remove it from `solr-17887-file-tree` before posting, as was done for SOLR-17861, so the diff is exactly the component (`FileTree.kt` +164, `FileTreeTest.kt` +90, the two chevron drawables).

---

[~malliaridis] I implemented the file tree component from this ticket on a [branch|https://github.com/nick-boss-tech/solr/tree/solr-17887-file-tree], and the [diff|https://github.com/apache/solr/compare/main...nick-boss-tech:solr-17887-file-tree] is {{FileTree.kt}} under {{ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/files/}} with its test {{FileTreeTest.kt}}, plus the two chevron drawables it uses. Spotless and the test pass at the branch head.

Is this the right scope for the ticket, and would you review it if I open the branch as a PR?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
