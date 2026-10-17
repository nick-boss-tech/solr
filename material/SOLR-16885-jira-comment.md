🤖 _AI text below_ 🤖 _(posted on behalf of Nick Shanin)_

I checked whether the problem in [SOLR-16885|https://issues.apache.org/jira/browse/SOLR-16885] reproduces on current main. It does not. The IndexOutOfBoundsException described in the ticket does not occur.

*What was checked*

 * The check ran on 2026-10-09 on upstream commit [cabedd1d968|https://github.com/apache/solr/commit/cabedd1d968059215188f4e7563fb303241899ed]. The branch was built on this commit.
 * The base had only the branch's test files added. It had no production code change.
 * In {{TestUnifiedSolrHighlighter}}, all 34 tests passed.
 * The new test, {{testTermVectorOffsetsWithoutPositions}}, passed on that base, without the fix.
 * No file the branch touches had changed on upstream main by that date.

*Result*

The premise of the ticket is not grounded on current main. The branch's fix addresses no failure shown on current main. The branch also had no test run before this check.

*Proposed change*

No change is proposed for this ticket.
