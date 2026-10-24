# PR 5211 review comment draft

Status: DRAFT, NOT POSTED. Static review only. The implementation/proof handoff is `assignments/pr-5211-test-review-implementation-and-gate-round-1.md`. Its final comment must replace this interim wording with the actual incremental diff and verified results. Nick must review that concrete comment before posting.

---

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

@gerlowskija, one test detail at `6f72e0cac151`: `testStartupWaitsForLeftoverEphemeralNodesToDisappear` joins the deletion thread before checking that the entry is absent. If `checkForExistingEphemeralNode()` returns before deletion, the join still lets the worker delete the entry and the assertion passes.

I suggest checking that the entry is absent immediately after the method returns, with the join kept for cleanup. Coordinating the worker with the check would also make the test less dependent on the one-second sleep. The production wait logic looks sound from this review.
