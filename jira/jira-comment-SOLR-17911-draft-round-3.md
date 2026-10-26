# Jira comment draft: SOLR-17911, round 3

Status: DRAFT (not posted). Refresh of round 2 at Nick Shanin's direction on 2026-10-26: branch and diff URLs added. Addressee: Christos Malliaridis (reporter). Posting needs Nick Shanin's explicit go.

Branch state, verified 2026-10-26: `solr-17911-file-content-viewer` tip `39d7c64701c` after removing `pr-draft/SOLR-17911.md`; the compare against main shows exactly 2 files. Gated head `8047b99ef0`; the strip deletes a non-code file only.

---

[~malliaridis] The file content viewer from this ticket is implemented on a [branch|https://github.com/nick-boss-tech/solr/tree/solr-17911-file-content-viewer], and the [diff|https://github.com/apache/solr/compare/main...nick-boss-tech:solr-17911-file-content-viewer] is {{FileContentViewer.kt}} under {{ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/files/}} with its test {{FileContentViewerTest.kt}} (line numbers, escaped characters, scrolling). Spotless and the tests pass at the branch head. The component has no {{editable}} parameter yet, which the ticket allows.

Is this the right scope for the ticket, and would you review it if I open the branch as a PR?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
