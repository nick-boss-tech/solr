# cuVS-Lucene review kit, round 4

Purpose: inputs for the step 03 readiness review, which a reviewer other than vm2 must run. This file is not a verdict. vm2 prepared steps 01 and 02, so it cannot be the independent reviewer.

Snapshot: coordination-ui `604ac7d38b1` (before this kit's claim). Claim: `claims/cuvs-lucene-review-kit-round-4-vm2.md`.

## Identity table

| Item | Value |
| --- | --- |
| Test base | NVIDIA/cuvs PR 2702 head `23004d0f0de2014f11b09313f04fcf0de7643c3b` (base `efc7d30ea7498d322b4cf806b67007995632469a`, draft, open) |
| Combined test patch | `material/cuvs-lucene-contribution-round-4/combined-tests.patch`, SHA-256 `84688de57719d0600505e42f4a62bfbd8b49373486ee90f6c4d9efbeff73ee5c` |
| Step 01 manifest | `material/cuvs-lucene-contribution-round-4/manifest.json`, SHA-256 `38af9de7bbf9b4fab805096b92faf46d9643e13713ef5ba44fe711d92af6cdf8` |
| Step 01 README | SHA-256 `d07dd5089030b2415039d9f74e4f195f5e118c14307643bbcf9364d04295da89` |
| Check A package | `2ad555a87a22acdf617f679ca03f5831f246b4a8`, patch `cpu-regression.patch` SHA-256 `d9ad46585c65846cf9b0d4f0ccffbf882e79be593f15897d8775115de810b154`, receipt `receipts/CUVS-LUCENE-PR2702-CPU-ROUND-2.md` (introduced by `c6e0a5564bb6a9e6dda9b446b216965be9034e46`) |
| Check B package | `e174c1a3e0dcfccf9d30a31847637733823df84b`, patch `backcompat.patch` SHA-256 `56cb5cb10f210e216c36fd413821dd1ddd931e5dd4ed26fc288299c928a84238`, receipt `receipts/CUVS-LUCENE-BACKCOMPAT-ROUND-3.md` (introduced by `b64dc930dac6d802ed53f37d7b6cb64159fe9eee`) |
| Revision 1 first-contact draft | `drafts/cuvs-lucene-first-contact-round-4-revision-1.md`, SHA-256 `a8fea52cf1fa33908f1462a87ae7e8473b2cd823c2ae40d305cac61e2b073912` |
| Revision 1 technical notes | `drafts/cuvs-lucene-technical-handoff-round-4-revision-1.md`, SHA-256 `dfa9a790c02e48617f1b208ea235703f8cd0c1b2caaf5ee56f035b76102522c0` |
| Revision 1 report | `reports/cuvs-lucene-submission-package-round-4-revision-1.md` (board commit `3a082c89ecc`) |
| Superseded, do not post | `drafts/cuvs-lucene-first-contact-round-4.md` (recommended PR 2867); `drafts/cuvs-lucene-pr2702-cpu-review-round-2.md` (stale proof status, "reviewed by" footer before review) |

Four changed files, each with its original and final Git blob (from the manifest):

| File | Original blob | Final blob |
| --- | --- | --- |
| `java/cuvs-lucene/src/test/java/com/nvidia/cuvs/lucene/TestCompatKnnFloatVectorQuery.java` | `2dc5e3b8564c1245b315481413b07ac73b97299b` | `0a2628c116e44094165e5feca73d0f74c87f0fd0` |
| `java/cuvs-lucene/compat/leaf-reader-102-bits/src/test/java/com/nvidia/cuvs/lucene/TestLuceneCompat.java` | `ba897c67180103d9c03322af4b456051590deb90` | `fa04413d7e5537ba8cd03242908d5306c5a7673d` |
| `java/cuvs-lucene/compat/leaf-reader-103-acceptdocs/src/test/java/com/nvidia/cuvs/lucene/TestLuceneCompat.java` | `dcd6544f4d1c8309eda64be1659ccfd3db232f1f` | `9bdc09a5865d21acb40a8812d867bdbc1293c592` |
| `java/cuvs-lucene/src/test/java/com/nvidia/cuvs/lucene/TestBackCompatIndices.java` | `203b57469d9122573c6bff0203e307a1aaf311f4` | `ee3cd6734e410a58aa08d703b7f944e210bfdf0c` |

## Reconstruction check (performed 2026-10-24, isolated directory, not a repo)

1. The four original files were read from PR 2702 head `23004d0f0de` through the GitHub contents API. Each Git blob matched `originalBlob`.
2. `git apply --check --whitespace=error-all combined-tests.patch` passed. `git apply --whitespace=error-all` then ran.
3. Each resulting file's blob matched `finalBlob`.
4. `sha256sum combined-tests.patch` returned `84688de57719d0600505e42f4a62bfbd8b49373486ee90f6c4d9efbeff73ee5c`.

A reviewer should repeat this check independently. Commands:

```
git -C <clone-of-NVIDIA/cuvs> fetch origin pull/2702/head
# create an empty dir, write the four originals from the head, then:
git apply --check --whitespace=error-all combined-tests.patch
git apply --whitespace=error-all combined-tests.patch
```

## Claim-to-evidence map (for the revision 1 first-contact draft)

| Public statement | Evidence |
| --- | --- |
| Three query cases plus the existing test, 4 methods on each of 4 modules, 0 failures, 0 skipped (16 executions) | Check A receipt, positive run item 4 |
| With the mask dropped on 10.2.2 and 10.4.0, the three new cases fail; the existing case passes | Check A receipt, mutation item 5 |
| The new cases pass on 10.2.2, 10.3.2, 10.4.0 and 10.5.1 | Check A receipt, item 4 |
| Deletes and sparse vectors upgrade case checks 183 live documents and 173 vectors on all four versions | Check B receipt, item 4 |
| JDK 21, Linux aarch64, no GPU | Both receipts, host and limits sections |

## Known weak points for the reviewer to judge

1. Mutation controls exist on 10.2.2 and 10.4.0 only. Nothing shows the three new cases fail on 10.3.2 or 10.5.1. The public text names only 10.2.2 and 10.4.0 for the failure claim.
2. The data-fault control for Check B also runs on 10.2.2 and 10.4.0 only.
3. Target. PR 2702 is an open draft that its author is splitting. PR 2867 (open, approved 2026-10-22) is based on main and contains only the Lucene 10.2 module. The tests cover 10.2 to 10.5, so PR 2702 is the only matching head. If PR 2867 lands first, PR 2702 must be rebased and the tests ported, with a new CPU run on the new head. The revision 1 comment says this.
4. Revision 1 comment says "I see that #2867 lands the Lucene 10.2 layout first." That is an expectation, not a fact: #2867 has approvals and a blocked merge state. The reviewer should judge whether to keep the sentence.
5. Round-3 compile matrix is derived from stdout, not a separate run (the receipt says so). Round-3 seed `18556C0D` appears in `final.json`; the receipt body does not state it.
6. The "reviewed by Nick Shanin" header is proposed. Nick has not reviewed any draft.
7. Public text was scanned for em and en dashes and internal terms. The scan found hits only in owner notes and the header line.

## Step 07 upstream check (performed 2026-10-24)

- apache/solr main is still `13c693d805f6541f7cec684cd27d28eef7b3b614`, the patch base. The patch still applies to that commit as checked against its file content.
- Both sentences the patch replaces are still present in main: "This is expected to be fixed in a subsequent Solr 10.x release." (`major-changes-in-solr-10.adoc`) and the matching sentence in `dense-vector-search.adoc`.
- Open apache/solr PRs that mention SOLR-18556, use the phrase "fixed in a subsequent Solr 10", or touch vector indexing in the ref guide: none.

## Trigger check for steps 05, 06 and 08 (performed 2026-10-24)

- Step 05 (upstream feedback): no reviews or comments on PR 2702 after 2026-10-07. PR 2867 has reviews from paul-aiyedun and cjnolet (2026-10-22), not directed at these tests. No trigger.
- Step 06 (release or GPU evidence): NVIDIA/cuvs latest release is v26.10.00 (2026-10-08). Maven latest for `com.nvidia.cuvs.lucene:cuvs-lucene` is 26.10.0. No newer candidate. No trigger.
- Step 08 (Solr adoption): needs a compatible released artifact and Nick's activation. Neither exists. No trigger.

## Limits of this kit

- It makes no verdict and changes no test bytes.
- It does not run the compile or JUnit suites. Those were run for the receipts only.
- No PR, comment, Jira, or fork write was made.
