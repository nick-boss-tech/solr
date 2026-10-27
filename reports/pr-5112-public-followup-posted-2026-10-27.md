# PR 5112 tested routing fix: public follow-up posted

- Comment: https://github.com/apache/solr/pull/5112#issuecomment-6450355228
- Posted: 2026-10-27T18:29:55Z (14:29:55 Eastern), author `nick-boss-tech`.
- Authorization: Nick explicitly requested, "OK try to post it and verify it", after reviewing the draft and requesting a collapsed Linux verification block. The separate certificate-identity topic was removed from this narrow reply as discussed.
- PR head before posting and after read-back: `b4fa46f1ecb11a60cc2cfd4783492639bb4e3a85`.
- Offered/tested fork head, independently confirmed live: `46bc7e428b5be519a497ecf65dcd0256b65fa84c`, branch `solr-18537-submit`.
- Proof: `receipts/PR-5112-target-header-followup.md`, reviewed at board snapshot `c7c5777b87fed3f1ed8578da8090ebb643ab4509`. 18 focused and 11 neighboring tests passed; both named header-loss regressions failed on original production code plus the new test file. Other two new tests have positive coverage only. Tidy clean; final check passed after the documented Antora dependency repair with NODE_ENV unset. Two earlier environment failures are disclosed.
- Before posting: refreshed live PR conversation/reviews, verified unchanged head, and confirmed no equivalent fix-offer comment was already present. Ancestry had been verified for the same two immutable heads, supporting the pinned fast-forward command.
- Posted once using `gh.ps1 pr comment 5112 --repo apache/solr --body-file` with the absolute approved draft path.
- Verification: separate REST GET read-back and `verify-pr5112-comment.ps1` confirmed author, URL, full stored body equal to approved draft after CRLF normalization, and unchanged PR head. The body preserves the disclosure, details/summary block, pinned diff, exact adoption SHA and disclosed proof limits/environment conditions.
- Local artifacts: `PR-5112-comment-draft.md`, `PR-5112-posted-comment.json`, `PR-5112-posted-comment.md`, `verify-pr5112-comment.ps1` in the October 27 review batch.
- No Jira action, PR body edit, contributor-branch change, review approval or merge performed. The certificate-identity question remains outside this routing fix.
