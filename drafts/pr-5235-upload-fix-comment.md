# Draft comment for apache/solr PR #5235 (SOLR-18351): the testUpload fix handoff

Status: DRAFT, not posted. Prepared by the phone agent 2026-10-28 under the handoff shape in `drafts/comment-formula-update-round-1.md`. Record: `receipts/PR-5235-upload-fix-on-pr-head-round-1.md`. Posting needs the owner's go.

---

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

@dsmiley, the CI on the current head `ccace89730ae` fails on one test: `TestConfigSetAdminRequest.testUpload`, in both the SolrJ Tests and Crave runs. It throws a NullPointerException at line 55 because `Upload.getContentWriter(String)` returns null after `setUploadFile`. The cause is in the upload paths: `setUploadFile` routes through the deprecated `setUploadStream`, which sets only the stream field, and the writer lookup falls through to null on that path.

I prepared a fix as a single commit on top of `ccace89730ae`: `Upload.setUploadFile` now stores a `RequestWriter.ContentWriter` for the file directly; `setUploadStream` is unchanged and remains the legacy stream path. The diff is one file, `ConfigSetAdminRequest.java`, +14/-4: [compare](https://github.com/dsmiley/solr/compare/SOLR-18351-backport-10x...nick-boss-tech:solr-18351-pr5235-upload-fix)

<details>
<summary>Verification</summary>

At `f890a62fc79` (your head plus this commit), on Linux with JDK 21:

- `testUpload` passes with the CI seed `747FA8C56DB2B91B` and with seed `4478EC01207498A8`.
- The full `TestConfigSetAdminRequest` class passes, 5 of 5.
- `tidy` leaves the tree clean before and after.

Without the commit, the same test fails at `ccace89730ae` under the same seed with the NullPointerException above, matching the two CI runs. [Full run record](https://github.com/nick-boss-tech/solr/blob/b979c2304bb334439c1ce522be124a2090e81cb4/receipts/PR-5235-upload-fix-on-pr-head-round-1.md)

</details>

To take the commit:

```sh
git fetch https://github.com/nick-boss-tech/solr.git solr-18351-pr5235-upload-fix
git cherry-pick FETCH_HEAD
```

You're welcome to take the commit or adapt the change.
