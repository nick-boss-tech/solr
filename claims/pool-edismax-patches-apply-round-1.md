# Claim: pool-edismax-patches-apply-round-1

- Claimant host: vm2, matching a file in `hosts/`.
- Status: DONE 2026-10-18T03:40Z. Deliverables below. Gate results are not yet in: each gate job is OPEN for a gate host.
- Job or assignment: `assignments/pool-edismax-patches-apply-round-1.md`.
- Slice: the whole assignment, patches P1 to P6, each claimed and recorded per patch.
- Capability tags used: implementation, gate (patch and post only; no build or test run on this host).
- Started: 2026-10-18T03:16:00Z (UTC).

Heartbeat: 2026-10-18T03:16:00Z

Pinned tips checked by ls-remote at claim time, all unchanged from the assignment: SOLR-3243 `1db99c13662`, SOLR-3729 `0fe7e503945`, SOLR-6009 `a41bb034a1f`, SOLR-6320 `cb710c96353`, SOLR-12092 `ca9573dabd3`, SOLR-14913 `b80221f46d3`.

Result: 2026-10-18 (this tick): P1 SOLR-3243 applied at `1db99c13662`, commit `a07674262e` (changelog title, new test `testNegatedUnfieldedOpenRangeExcludesEveryDocument`), pushed fast-forward. Gate job `gates/SOLR-3243-edismax-p1.md`. Receipt `receipts/SOLR-3243.md` names the head as awaiting its gate. No build run here.

Result: 2026-10-18 (this tick): P2 SOLR-3729 applied at `0fe7e503945`, commit `ec4173da5d` (comment-only: `isStandaloneMatchAll` javadoc, malformed-boost test comment), pushed fast-forward. The pre-push format check refused the first wording, so the javadoc was re-wrapped and the commit amended before the push; the amend was local and unpushed. Gate job `gates/SOLR-3729-edismax-p2.md`. Receipt updated. No build run here.

Result: 2026-10-18 (this tick): P3 SOLR-6009 applied at `a41bb034a1f`, commit `b7695a518c` (changelog title only), pushed fast-forward. Gate job `gates/SOLR-6009-edismax-p3.md`. Receipt updated. No build run here.

Result: 2026-10-18 (this tick): P4 SOLR-6320 applied at `cb710c963531`, commit `3a35d800a2` (test comments only; TODO lines removed), pushed fast-forward. Gate job `gates/SOLR-6320-edismax-p4.md`. Receipt updated. No build run here.

Result: 2026-10-18 (this tick): P5 SOLR-12092 applied at `ca9573dabd38`, commit `4243924c34` (changelog title and one guide sentence), pushed fast-forward. Gate job `gates/SOLR-12092-edismax-p5.md`. Receipt updated. No build run here.
- Skipped: removing the three `Co-Authored-By` trailers from `cde23f0a9d7`, `b989934d4ea`, `3a54f4f3fdf` (history rewrite; needs a force push, which this host does not do). Still present on the branch; owner item O3 stays open with Nick.
- Not done here: the review flag in `pr-drafts/edismax/SOLR-12092.md` and the draft's Proof and Changelog links. They are not on the coordination branch, and the assignment moves them when P5 is gated.

Result: 2026-10-18 (this tick): P6 SOLR-14913 applied at `b80221f46d`, commit `137dbace01` (changelog title and the compound-query assertion), pushed fast-forward after the formatter collapsed the new `req(...)` call onto one line. Gate job `gates/SOLR-14913-edismax-p6.md`. Receipt updated.

DONE: 2026-10-18 (this tick). Deliverables: `gates/SOLR-3243-edismax-p1.md`, `gates/SOLR-3729-edismax-p2.md`, `gates/SOLR-6009-edismax-p3.md`, `gates/SOLR-6320-edismax-p4.md`, `gates/SOLR-12092-edismax-p5.md`, `gates/SOLR-14913-edismax-p6.md`; receipt sections in `receipts/SOLR-3243.md`, `receipts/SOLR-3729.md`, `receipts/SOLR-6009.md`, `receipts/SOLR-6320.md`, `receipts/SOLR-12092.md`, `receipts/SOLR-14913.md`; fork heads `a07674262e`, `ec4173da5d`, `b7695a518c`, `3a35d800a2`, `4243924c34`, `137dbace01`.
