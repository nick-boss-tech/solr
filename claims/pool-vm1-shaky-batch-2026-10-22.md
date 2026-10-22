# Claim: SHAKY slate batch, disposition and evidence round (vm1)

Status: CLAIMED by vm1, 2026-10-22, at Nick's direction ("take some assignments from newdev or blocker list, they are shaky").

Scope: the five SHAKY items from the critical-blocker slate spot-audit (goal hidden_files/critical-blocker-slate-2026-10-22.md). Per item, verify the doubt on current evidence and return a disposition or a prepared next step. No PR openings, no comments, no Jira posts, no branch pushes.

- SOLR-14200: re-verify on current main that the ticket's crash is gone (the record's settling claim), then a retire-or-hygiene disposition with the run evidence attached.
- SOLR-16155: read the current state of upstream PR #1151 (Jan Hoydal) against this ticket; disposition follows its state (merged: ours retires; stalled: the collision call goes to Nick with both scopes compared).
- SOLR-17433+17143: assemble the retire pack: the inconclusive-by-construction proof, the cache-half-only diff, and what retiring loses, for Nick's call.
- SOLR-18010: restore verifiability at the live tip 1b14ce9556 (what a fresh gate at the tip would need) and scope the narrow attempt Nick directed on 2026-10-21. PR #5118 stays dropped; nothing here resurrects it.
- SOLR-17297: cost the widen option (the SPI-consolidation fix for the reproduced inverse-direction orphaning) against shipping as-is, so the owner Choice from 2026-10-08 can be re-presented with a design and a price.

Deliverable: a batch report with per-item verdicts, filed in the goal workspace and summarized to Nick; coordination records updated per item as verdicts land.
