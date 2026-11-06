# SOLR-18623 green follow-up record: source-hash transcription correction

Status: GATE GREEN at H3; public comment remains verified and unchanged.

The Negative A bullet in reports/pr-5301-green-followup-round-4.md transcribed the original production-file SHA-256 incorrectly. The correct hash is `99faae67945695df385cb1c4bb2b629b44d9e3364fd5cead11af5f7f829f81e2`. It was independently calculated from the raw git blob at contributor H0 `01e820f0539cddc4697d26176d216614a776a99a` and matches both the gate's nega-hashes.txt and the actual receipt. The ordering-test hash `d7b28f86dcd2e60a0a70b664d6f54eb8e2de19c6d135d5b7186835e840b1f7e3` also matches the raw H3 blob and both negative legs. The local helper's initially mistyped expected constant was corrected; its extra assertion exposed this record transcription error, not a mismatch in the Linux evidence.

No production, test, evidence archive, execution result or public text changed. The public comment contains no such file hash and remains an exact match to the reviewed draft: https://github.com/apache/solr/pull/5301#issuecomment-6673432147 . Offered/tested H3 remains `dfde9bde0a348ad44e6f6ceec197d478ef11bfd0`, and public PR head remains H0. The evidence archive still matches `72ffbe08ea9023eff6425a17f17161e86444e85ee2bbb691bdeb431faf105bc2`.

This is an add-only correction to the Windows closeout record; the Linux receipt and archived baseline were already correct. The local canonical report is corrected. All proof qualifications, including the Error Prone warning-tolerance condition, remain as recorded.
