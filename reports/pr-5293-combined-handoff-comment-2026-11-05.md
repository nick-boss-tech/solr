# PR 5293: combined Solr and Lucene handoff posted

- Public comment: https://github.com/apache/solr/pull/5293#issuecomment-6650656181
- Posted by `nick-boss-tech` at `2026-11-05T05:37:54Z`.
- Authority: Nick explicitly requested a simple language check and authorized posting if the draft looked sound. This released the prior draft-review hold. Publication does not rely on the standing clean-gate exception; the Solr branch remains a failing reproducer.
- Live board snapshot before language review: `08b0d99092059cf4a6e1f0b48a8bec06d55ffc7d`; immediately before adding this report: `b364ba3b7f85818dae66053793c29a29fd7e6467`. This report path was separately confirmed absent.
- PR remains open at `978e2b1e352c7c39a68f7b952e6ab12eabf80890`. Pre-publication conversation comments, reviews and inline comments were empty. Post-publication read found the offered comment exactly once, no reviews, and the unchanged PR head.
- Solr reproducer: `nick-boss-tech/solr:pr-5293-solr-18622-child-sort-missing`, exact remote head `769ca6e177d77acd3f1993a4a24bd3257ae243c0`. Ancestry from the live contributor head was rechecked before offering the pinned fast-forward command.
- Lucene repair: `nick-boss-tech/lucene:fix-block-join-missing-values`, exact remote head `55c3174bd154b5cc987d9b24e1c77043e23fe71e`, base `aa366ae209ae8296dd70d2835c6d47f8fc201972`. Both fork refs were independently refreshed before publication.

The comment offers both pinned diffs and repository-specific Git commands. It reports the two Solr failures, the 10.4/10.6 comparison, the outstanding Solr formatting-only correction and unrun broader Solr checks. It separately reports Lucene focused 10/10, full join 104 passed plus 3 existing nightly skips, four actual runtime fail-before failures, clean join tidy and successful root `check -x test`. Default-disabled Error Prone, unconfigured eclint, repository-wide runtime testing, maintenance backport and Solr integration limits are explicit. No new code or tests changed during language review, so no source gate was invalidated.

Evidence: `receipts/pr-5293-child-sort-missing-round-1.md`, including its 10.4 addendum; `receipts/lucene-block-join-missing-values-windows-round-1.md`; `gates/lucene-block-join-missing-values-windows-round-2.md`.

The main language change replaced "one outstanding tidy line join" with "one formatting-only correction"; several other phrases were simplified without changing results or scope. The required AI disclosure remains. The exact local body is `research/branch-reviews/2026-11-04-pr-batch/PR-5293-comment-draft.md`. A separate API read-back matched it after normalizing CRLF and trailing newlines only. Stored API response and post-comment PR state were archived locally as `PR-5293-comment-posted.json` and `PR-5293-post-comment-state.json`; the verifier is `verify-pr-5293-comment.ps1` in that same folder.

No Lucene PR, issue, or comment was created. This post is on the Solr PR only. No contributor branch, PR body, review resolution, approval or merge was changed. Await the author's response; a maintenance backport and patched-Solr integration would need their own exact-head proof.
