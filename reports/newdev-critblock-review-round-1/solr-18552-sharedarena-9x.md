# Review: SOLR-18552, branch solr-18552-sharedarena-9x (unit 5, 9.x line)

Head reviewed: cd7cc695fb09fc8adba211992a85b5f76380b110 (ls-remote 2026-10-20T14:15Z). Base: upstream branch_9 at f4f2507177 (the 9.x base the critblock record names). Commit: cd7cc695fb, "SOLR-18552: default sharedArenaMaxPermits to 64 in bin/solr on 9.x". Diff: `solr/bin/solr` +1, `pr-draft/SOLR-18552.md` +22 (draft only).

## Pass 1

Review (2026-10-20, vm5):

- Change. One added line in `solr/bin/solr`, after the stack-size default: `SCRIPT_SOLR_OPTS+=("-Dorg.apache.lucene.store.MMapDirectory.sharedArenaMaxPermits=64")`. The array form matches the neighbouring line. User `SOLR_OPTS` comes after `SCRIPT_SOLR_OPTS` on the java command, so a user value still wins (same order as unit 4).
- Finding, major (owner question, scope): `solr/bin/solr.cmd` is not changed on this line. The main line (units 4) sets the default in both launchers. On 9.x, Windows users get no default. The draft says "bin/solr" only and does not say the Windows launcher is out of scope. Adding the cmd line would be a scope widening (the ladder's rung 5); not repaired here. Recommendation for the owner: add the matching `solr.cmd` line on 9.x, so both lines carry the same default.
- Finding, minor: the branch has no changelog file for SOLR-18552 (main has `changelog/unreleased/SOLR-18552-mmap-arena-permits.yml`). The gate's changelog parse step has nothing to parse on this head. A changelog entry is content, not a repair in the Gate repairs scope; recorded for the owner and for the regate step.
- Syntax. `bash -n` of `solr/bin/solr` at the head: ok. `git diff --check`: clean.
- Gate evidence from the draft: a 11.0 SNAPSHOT distribution gate (one JVM carries the property, control gives zero). Not reproduced here: the 9.x distribution does not build on vm5 (npm step), and the bats tag is vm1 only.

Repairs: none.

Regate: gates/SOLR-18552-9x-review-pass-1.md (partial: syntax and diff checks on vm5; the launcher and bats legs are for vm1).

## Pass 2

Review (2026-10-20T14:18Z, vm5, second pass on head cd7cc695fb09fc8adba211992a85b5f76380b110; ls-remote unchanged):

- Draft's gate claim against the tip. `pr-draft/SOLR-18552.md` cites a launcher gate: `bin/solr start -p 8995` from an 11.0 SNAPSHOT distribution with this bin/solr copied in, one JVM carries the property, the control gives zero. That run used an 11.0 distribution, not a 9.x one, so it is evidence for the launcher line's mechanics, not for the 9.x line itself. Finding, minor: the draft should say so, or the 9.x launcher leg must run on a 9.x distribution (vm1).
- The 9.x default. The draft says 1024 is the 9.x default. vm1 verified the 1024 fallback against the pinned lucene-core 9.12.3 jar on the main-line review (claims/pool-vm1-18552-light-gate.md); the same jar line applies to this branch. Consistent.
- Override order. `SCRIPT_SOLR_OPTS` precedes `SOLR_OPTS` (read at the line in pass 1); a user value still wins.
- Changelog. Still no file on this line (pass 1 finding stands).
- Owner question (rung 5, scope), posted in the claim: add the matching `solr.cmd` default on 9.x? Recommendation: yes, so both launchers carry the same default, as on main.

Repairs: none.

Regate: gates/SOLR-18552-9x-review-pass-2.md (partial).
