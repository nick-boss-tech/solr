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
