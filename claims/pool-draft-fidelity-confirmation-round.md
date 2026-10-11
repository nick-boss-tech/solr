# Claim: draft fidelity confirmation round

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review
- Assignment: `assignments/pool-draft-fidelity-confirmation-round.md`
- Date: 2026-10-11
- Staffing: 2 read-only subagents, where the assignment says 1, because the scheduled check asks for at least 2 and the scope is wide. Slice 1: every draft edited by the Group D and Group O settlements. Slice 2: at least a third of the drafts edited by the original application, rotated across categories.
- Scope: read-only. No draft edit; every disagreement is flagged. No build, test or gate. No PR, comment or Jira write.
- Deliverable: `reports/draft-fidelity-confirmation-round.md`: per draft, CONFIRMED or DRIFT with the exact passage and the record it disagrees with; drafts not checked listed as not checked.
- Heartbeat: 2026-10-12T00:10Z (claim taken); both slices reported.
- Status: DONE, 2026-10-11. Deliverable: `reports/draft-fidelity-confirmation-round.md`. DRIFT in seven drafts (SOLR-17987, 12556, 4362, 16914, 17029, 18368, 9396), each with the exact passage and the fix; the rest read correctly in place. Several readiness and record notes are listed. Nothing was edited, posted or built.
