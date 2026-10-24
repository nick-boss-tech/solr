[~malliaridis] The file content viewer for SOLR-17911 is written. It is on a fork branch, and I have not re-run its tests myself. The points below are read from the branch, at commit 8047b99ef0, and its test file.

 * {{FileContentViewer}} takes a plain string and shows one numbered line for each line of the input. Line breaks are kept as written, because the input is split with {{content.lines()}}.
 * The gutter with the line numbers sits inside {{DisableSelection}}, and the text sits inside {{SelectionContainer}}. Copying a selection therefore does not copy the numbers.
 * Escaped characters are shown as written. The view scrolls horizontally and vertically, and both scroll states are applied.
 * {{FileContentViewerTest}} has three tests: line numbers, escaped characters, and scrolling past the viewport. A test run on that commit was reported as 3 of 3 passing. I have not repeated it.
 * The component takes a {{modifier}} and has no {{editable}} parameter yet. The ticket allows that.

Does this evidence meet the bar you set for the ticket? That is the one question I am asking.

The options as I see them:
 * Option A: open a PR from the branch now, as a self-contained component with its tests, and leave the screen wiring to later.
 * Option B: keep the ticket open until a caller exists, such as the Configsets Files screen or the Zookeeper view, so the component is reviewed together with its first use.
 * Option C: close the ticket when the component lands, and file the screen wiring as a child under SOLR-17886.

My recommendation is Option A. The ticket asks for the component, and the criteria are met on the branch. The screens that use it are separate work, and the file tree (SOLR-17887) is the natural first caller. One note on the test setup: on a host without a GL driver, the test JVM needs a stand-in {{libEGL}}. The branch's draft PR description records that, and the repository does not carry it.

No review is asked here. If the direction is right and work follows, I will ask about that then.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
