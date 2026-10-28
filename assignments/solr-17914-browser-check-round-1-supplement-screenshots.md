# Supplement: screenshots added to the SOLR-17914 browser-check assignment

Posted 2026-10-27 by the phone agent at Nick Shanin's direction. This supplement amends `assignments/solr-17914-browser-check-round-1.md`; everything in that assignment stands, with this addition.

## Added work: screen render

After the browser leg, render `CreateCollectionForm` inside the app theme using the pool's screen-check setup (the form's preview composable, `PreviewCreateCollectionForm`, is the natural harness), in the collapsed state and, if the harness supports it, with the advanced section open. Capture PNG screenshots and post them to the board under `material/screen-checks/SOLR-17914/`. Compare against the Figma captures of node `2635-40394` (the WIP WithPreview design the PR follows): field set and order, the "Letters, digits, _ and -" helper, the NRT/TLOG/PULL inputs, the placement field, the collapsed advanced section, and the footer buttons. State plainly what matches, what differs, and what the render cannot confirm, following the honesty pattern of the SOLR-17887 screen receipt.

## Added deliverable

The receipt `receipts/SOLR-17914-browser-check-round-1.md` also records the screenshot paths and the comparison notes.
