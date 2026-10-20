# Review: SOLR-18552, branch solr-18552-submit (unit 4, main line)

Head reviewed: cb32d0d966159edb237d581318af9bf96071c0d1 (ls-remote 2026-10-20T14:08Z). Base: upstream branch_9x at 3511867c3e (merge-base). PR #5181 is open on this head. Commits: a5406c9fe5 (set the permit default), 0652af46e4 (solr.cmd init line), cb32d0d966 (bin/solr init line).

## Pass 1

Review (2026-10-20, vm5):

- Scope. Three files: `solr/bin/solr` (+1 -1 on the init line), `solr/bin/solr.cmd` (+2 -1 on the init line), and a changelog YAML (type `changed`, link SOLR-18552). The change is confined to the launcher defaults.
- Behavior. The launcher adds `-Dorg.apache.lucene.store.MMapDirectory.sharedArenaMaxPermits=64` to `SCRIPT_SOLR_OPTS`, the same array later appended to with `+=`. The bash form matches the existing `SCRIPT_SOLR_OPTS+=(...)` lines (for example 914, 1271). The cmd form uses `set "..."`, the quoted form, which is correct for a value that contains `=`.
- Scope limit, recorded: embedded and custom-start users keep the Lucene default. The ticket asks for a Solr 9 default; a launcher default is the stated route in the receipt. Not repaired.
- User override. A property set in `SOLR_OPTS` lands on the same JVM command line; which value wins depends on order in the command. Nit, recorded, not repaired.
- Changelog. Parsed with PyYAML (`uv run --with pyyaml`): keys authors, links, title, type; type `changed`; link SOLR-18552. Valid.
- Syntax. `bash -n solr/bin/solr` (at the head) ok.
- Not run on this host: the launcher behavior check (a node started with the branch's `bin/solr` has the property on its JVM; the control launcher does not) and the bats launcher tests. Those need the bats tag, which vm1 carries (hosts/vm1.md). The launcher check is named as the NEARLY piece for vm1.

Repairs: none.

Regate: gates/SOLR-18552-review-pass-1.md (partial: syntax and changelog legs on vm5; launcher leg named for vm1).

## Pass 2

Review (2026-10-20T14:12Z, vm5, second pass on head cb32d0d966159edb237d581318af9bf96071c0d1; ls-remote recheck unchanged):

- Override order, bash. `solr/bin/solr` builds the java command as `... "${SCRIPT_SOLR_OPTS[@]}" ... "${SOLR_OPTS[@]}"`: the launcher default comes before any user `SOLR_OPTS`. For a repeated `-D`, the JVM takes the last value, so a user-set `sharedArenaMaxPermits` in `SOLR_OPTS` wins over the default. Verified by reading the command line at the head (line 1428). Not run.
- Override order, cmd. `solr/bin/solr.cmd` appends `%SCRIPT_SOLR_OPTS%` to `START_OPTS` (line 1203) before `%SOLR_OPTS%` (line 1219). Same result: the user value wins.
- Quoting, cmd. The default is set with `set "SCRIPT_SOLR_OPTS=..."`, and the later appends use the same `set "...%SCRIPT_SOLR_OPTS% ..."` form the file already uses at lines 797 to 938. The value contains `=`, which the quoted form handles. Consistent with the file; not run.
- Ordering with the stack-size line. `solr.cmd` line 1087 rebuilds `SCRIPT_SOLR_OPTS` as `%SOLR_JAVA_STACK_SIZE% %SCRIPT_SOLR_OPTS%`; the permit default stays in the value. No issue found.
- Still not run on vm5: launcher behavior and bats. Named piece for vm1 (gates/SOLR-18552-launcher-bats.md).

Repairs: none.

Regate: gates/SOLR-18552-review-pass-2.md (partial, same shape as pass 1).

## Pass 3

Review (2026-10-20T14:15Z, vm5, third pass on head cb32d0d966159edb237d581318af9bf96071c0d1; ls-remote unchanged):

- Final verdict on the change: correct as a launcher default. The property name and its value were checked by vm1 against the pinned lucene-core 9.12.3 jar on the earlier head a5406c9fe5 (claims/pool-vm1-18552-light-gate.md). The two later commits (0652af46e4 for solr.cmd, cb32d0d966 for bin/solr) only move the default onto the init line; reviewed here, no logic change between a5406c9fe5 and the head beyond that.
- Coverage gap, recorded: vm1's light gate ran on a5406c9fe5. The head cb32d0d966 has not had the launcher behavior check or the bats run. Those are the open piece for vm1 (gates/SOLR-18552-launcher-bats.md, OPEN).
- Not checkable on Linux: `solr.cmd` cannot be run here; its syntax is read only (quoted set form, consistent with the file).

Repairs: none.

Regate: gates/SOLR-18552-review-pass-3.md (partial).
