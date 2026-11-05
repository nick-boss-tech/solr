# Final source and pin verification

Read back after all required round-2 proof legs and immediately before receipt publication, at `2026-11-05T21:37:20Z`.

| Ref | Exact value |
|---|---|
| Apache Solr PR #5110 | OPEN; head `6b5d830132b941faa85856347099ade8b190be0e` (P); base `82abb86bb5b7326fe48c117e4de69981a43e4251` |
| Fork branch `nick-boss-tech/solr:pr-5110-core-menu-lifecycle` | `b0e096ae19ad637107227e1bc04e951b9023a680` (F2) |
| F2 parent | `f02bb7bb06decce976c3ed2fdfbf5907764c2183` (F1), whose parent is P |
| P tree | `cdd291ceed4c8414adf458cfa7aee2ad50bcfb64` |
| F2 tree | `be294a23520476ec198eaee7f56195513cbf96ac` |

The pre-claim readback is recorded in the immutable round-2 claim: fork F2 and PR head P matched the required pins, with no competing round-2 execution claim. The final readback above still matches those source pins. The round-2 claim remained ACTIVE; the required receipt and failure verdict paths were absent before this finishing post. The board tip before this post was `b6e43c45aabd78e9a4ed064e1a1950bdd1333366`.

`P..F2` contains exactly two paths: `solr/webapp/web/js/angular/app.js` and `solr/webapp/src/test/org/apache/solr/webapp/AdminUiNodePickerTest.java` (262 insertions, 14 deletions total). `F1..F2` changes only the picker test (one insertion, three deletions); `app.js` is unchanged between F1 and F2. The fail-before worktree was restored to exact F2 `app.js` blob `d23e89b33a13eac833872a57f1b9d8ce10e82dea`; the picker test remained exact F2 blob `d1ec6d38d0b5eb0e4fff712b17b9e1c8ebd54ee0`.

The F2, P and isolated fail-before worktrees all ended at their pinned heads with clean tracked trees. The host Gradle lock was free after the final check. No source-branch commit/push, PR update, PR comment, or Jira action was made; the only pending write is the authorized add-only coordination-board receipt, failure verdict, and evidence set.
