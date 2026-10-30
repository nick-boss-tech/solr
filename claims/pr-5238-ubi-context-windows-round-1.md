# Claim: adjacent UBI recording context fix, Windows round 1

- Status: CLAIMED
- Host: windows, Codex desktop
- Started: 2026-10-30 UTC
- Tags: implementation, review, records
- Source: Nick's direct request to implement a real adjacent UBI patch on top of the tested listener branch and hand it to a Linux gate host.
- Board snapshot inspected: `88b0ae9a62e04c270df656a0f9c69713939b0c8d`. No existing UBI context implementation claim was found in assignments, claims, or gates.
- Base: `c2c387064a83def34cece4163eccded28318caeb`, the tested test-only UBI adapter example. Preserve that branch unchanged.
- New branch: `nick-boss-tech/solr:solr-11994-ubi-context-submit`.
- Slice: import the two real UBI draft source prerequisites from `apache/solr` PR 2452 at `6402b4b528d3ec8daa134772abc84532380e3f7c`, separately commit that unfixed baseline, then isolate recording StreamContext per request and add a deterministic integration regression using the real component and logging stream.
- Deliverables: `reports/pr-5238-ubi-context-windows-round-1.md`, `gates/pr-5238-ubi-context-round-1.md`, and the pushed candidate head named in those records.
- Proof: no Gradle or Java execution on Windows. Linux must demonstrate an intended wrong-payload assertion on the imported unfixed baseline with the new test overlaid, then pass the repaired head, tidy, and check. This is not a demonstrated defect until that matched pair is complete.
- Decision: follow the direct request with a separate adjacent candidate; do not broaden the existing green test-only offer or claim this race is in the listener hook. Public offer waits for exact-head Linux proof.
