# Claim: pool-vm5-gate-backlog-14913-edismax-p6-r2

- Claimant host: vm5, matching a file in `hosts/`.
- Status: ACTIVE; mark DONE with the deliverable paths when the work lands.
- Job or assignment: `gates/SOLR-14913-edismax-p6.md` (HANDED; this claim takes it under the rule that HANDED is not a host restriction).
- Slice: step 3 with a new proof base, then steps 3 to 5 at the head.
- Capability tags used: gate.
- Started: 2026-10-18T20:14:57Z (UTC).

Decision (claimant, 2026-10-18T20:14:57ZZ): the 08:21Z proof base (`b5c71bc5573`) fails at line 960, the multi-term assertion, before the new compound assertion at line 1031 is reached. That assertion is served by `getAliasedMultiTermQuery`, which the proof base lacks. The new proof base is the head parser with only the all-invalid block in `getAliasedQuery` removed (the `if (a.fields.size() > 0 && !hasValidAliasTarget(...)) return new MatchNoDocsQuery();` block with the comment "an alias with no usable target matches nothing"). The multi-term block and the `hasValidAliasTarget` helper stay. This is a new input, so the step 3 rule allows a new run.

Expected: the multi-term assertion at line 960 passes, and the first failure is a single-term all-invalid assertion before line 1031, because the compound query at 1031 uses the same term path. That shows the term path depends on the block. It does not reach 1031 on its own, and the record will say so.

Then restore the head parser (verify the tree equals HEAD), and run steps 4 and 5 at `137dbace01`: clean, focused `TestExtendedDismaxParser` with seed `14913C0FFEE14913` (expect 40 of 40 from the fresh XML), then `:solr:core:check -x test`.

Heartbeat: 2026-10-18T20:14:57Z

Result (2026-10-18T20:26:42Z): proof leg with the all-invalid block removed from `getAliasedQuery` passed 40 of 40 (`proof.xml/`, seed `14913C0FFEE14913`). That block is not what the new assertions test: the fallback after it gives the same empty result. The proof base is therefore not discriminating, and this claim records that instead of counting it as a proof.

Decision (2026-10-18T20:26:42Z): second proof base. Keep the head parser except the term-path skip in `getQueries`, the `if (!isValidAliasTarget(f)) continue;` before `this.field = f; Query sub = getAliasedQuery();`. Without that skip an alias target missing from the schema reaches `getAliasedQuery` and the parse falls back to the escape-and-re-parse path, which is the behavior 78d8222742 removed. The multi-term skip stays, so the line 960 assertion is still served. Expected: the new compound assertion at line 1031 fails with a name:Zapp match, or an earlier single-term all-invalid assertion does. Whichever line fails first is recorded. Restore after the run, then re-run head only if the restore changes anything (it does not change the head tree).
