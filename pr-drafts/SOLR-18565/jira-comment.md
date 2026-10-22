🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

The fix is in [PR #5193](https://github.com/apache/solr/pull/5193), which has been merged to main. It changes the expected text at line 103 to the part of curl's message that both wordings share, "no alternative certificate subject name matches target host". The Script Tests run on the PR head passed: [run 39737853693](https://github.com/apache/solr/actions/runs/39737853693/job/119785524016).
