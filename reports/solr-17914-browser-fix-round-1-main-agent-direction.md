# SOLR-17914 browser fix round 1: main agent direction

- Recorded: 2026-10-28 06:20:44 UTC.
- Assignment: `assignments/solr-17914-browser-fix-round-1.md`.
- Reviewed board snapshot: `24c6abf00774843f08d29552f50e22cb66f9db8e`.
- This is direction for the existing OPEN assignment, not an implementation claim or proof receipt.

## Host and source scope

Keep the assignment with a capable Linux host holding `ui` and `gate`. Windows does not hold those tags and cannot perform the required first browser reproduction or the final gate. No implementation claim is taken here.

Keep source changes in scope as the assignment explicitly allows. The Linux implementer should diagnose each failure first, including the browser comparison at the SOLR-17913 base, and fix only category (a), causes in this branch's files. Do not assume that the four form failures are category (a) merely because the files are in the diff. Report category (b) base failures and category (c) shared harness or environment failures separately for a subsequent decision.

Keep fix commits on `solr-17914-browser-fix-round-1`, starting at `6170ba10ac5061203339fa0a96e714c7e64616c2`. The open draft PR's branch is not advanced by this work.

## Agreed screenshot method

The main agent selects real Chromium viewport PNG capture of the production create-collection form rendered inside the app's `SolrTheme` in the wasm browser environment. Use browser-native screenshot capture through an external browser controller, such as WebDriver or the Chrome DevTools Protocol. Capture the actual rendered canvas, not a DOM reconstruction or a Figma image.

An isolated, temporary browser fixture is permitted for screenshot setup. It may mount the existing production form or its preview, provide deterministic inputs and error state, and expose the four required states: empty, advanced section open, name-exists error, generic failure. It must not duplicate or modify the form's production layout, add a permanent app route, change the shared test harness, or weaken existing assertions. Keep fixture code and capture tools out of the fix commits. Record the fixture and capture command in the execution logs so the images can be reproduced.

Use a consistent viewport and app theme, wait for the form to render, and record the viewport, device scale, browser version, and source head with the screenshot status. Clearly label fixture-supplied error states. Images demonstrate rendering; the browser tests establish behavior.

Publish the four PNGs under `receipts/SOLR-17914-screens/` as the assignment requires. If this browser method cannot capture the required states, record the limitation and stop at that point. Do not substitute JVM captures or add another scratch approach without returning for direction.

## Closeout

The original assignment's exact-head full UI check, fresh XML counts, receipt, and red/green closeout rules remain in force. No public comment or PR body edit is authorized by this direction.
