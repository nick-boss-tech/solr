# PR formula supplement: test adequacy and minimalism

Use this check during implementation and review, before preparing the PR
description. It supplements the [historical PR formula](https://github.com/nick-boss-tech/solr/blob/95e2574f5091fbf1425fd9bfc4cef98c746b42f6/pr-formula.md); current workspace instructions, the user's direction and the live board govern where they differ. This is an internal review aid, not a checklist to paste into a PR.

## Choose the smallest convincing proof

- State the behavior and failure mode to be demonstrated. Find the existing
  tests, release checks and CI jobs that exercise it, and verify what they
  actually run, when they run, and whether they would expose the bug.
- Prefer an existing test or a small extension at the real behavioral boundary
  when it gives sufficient coverage. Where practical, show the relevant failure
  on the old code and success with the change. For coverage-only work, new APIs
  and other cases where fail-before is not meaningful, describe what the check
  actually establishes instead of manufacturing a failure.
- Before adding a suite, fixture, helper or CI workflow, identify the distinct
  regression or meaningful benefit it adds, such as a hard-to-reach edge case,
  a safer isolation boundary or an earlier signal worth its cost. Weigh that
  against code size, duplication, flakiness, run time and maintenance. An
  existing end-to-end check may be enough; a focused unit test can still be
  justified when it covers a material gap. Do not use line count as a hard cap
  or remove necessary safety, compatibility or negative cases to make a diff
  smaller.
- Check what mocks replace. A mocked test can verify decisions and interactions,
  but cannot prove that real artifacts, processes, files, network paths or
  platforms work. Use a real boundary check when that behavior matters. A
  temporary diagnostic probe can help development without becoming a permanent
  suite; record its actual scope and do not present it as integration proof.
- Review the whole outbound change, not just the test: use existing helpers
  where suitable, keep assertions meaningful, and justify new abstractions and
  defensive branches. Separate adjacent work when it has an independent purpose
  and proof, unless the combined scope makes the fix clearer or is needed for
  correctness. Do not introduce additional test machinery merely to satisfy a
  formula or move a failure earlier.

In the PR's proof section, describe the relevant behavior and checks that
actually ran, at the tested revision. Do not imply that mocked, skipped,
historical or unrun checks prove the current integration. Explain a test design
tradeoff only when it helps the reviewer; the internal checklist stays out of
the public description.

## Example: PR 5310

The release smoketester already detected the missing slim example data. A
separate mocked smoketester suite aimed to catch mistakes earlier, but added a
large amount of test and CI machinery while replacing Solr subprocesses and
HTTP with mocks. In [the review](https://github.com/apache/solr/pull/5310#discussion_r4509289338),
David questioned whether that earlier signal justified the maintenance cost;
the suite and its dedicated workflow were removed. The lesson is to justify
the additional coverage against the real check, not to prohibit tests of test
infrastructure or to equate fewer lines with adequate proof.
