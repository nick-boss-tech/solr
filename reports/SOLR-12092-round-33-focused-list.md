# SOLR-12092 round 33 focused class list

Deliverable for `assignments/pool-solr-12092-round-33-focused-list-round-1.md` (vm1, 2026-10-18). Read-only search; nothing was rerun and no worktree was edited.

## Item 1: search for the round 33 logs

Status: DONE, both logs found on this host.

Paths searched:

- `/home/hatch/workspace/tools/` (this host's gate log store): FOUND.
- `/home/hatch/` (recursive name search): no other copies.
- `/tmp`: nothing (wiped on VM replacements, as expected).
- `/workspace` (the factory hosts' root): not present on this host; `/workspace/gates/` does not exist here.
- Filesystem root to depth 6: no other copy.
- Worktrees: `/home/hatch/workspace/solr-worktrees/solr-12092-gate` exists; its build test-results were not needed because the logs survive.

Paths found, all in `/home/hatch/workspace/tools/`:

- `g12092r33-gate.log`, sha256 e54b56c3e650428987211d0e879547f69f3b933407103fc453f40466ae3a77aa
- `g12092r33-check2.log`, sha256 8e1aed0e67c83783740cd1cd46ffd9758a91968892cd5158cf497ef74c0694b2 (the module check rerun; it carries no test tallies)
- Also present: `g12092r33-gate.sh` and `g12092r33-check2.sh` (the runner scripts), `g12092r33-prep.log`, `g12092r33-gh-premise.log`.

## Item 2: the nine focused classes at the head

Status: DONE. Source: the per-class tally lines in `g12092r33-gate.log` for the focused run at head `ca9573dabd385a38711bd60633973ae46b7a552e`, cross-checked against the class list in `g12092r33-gate.sh`. Head run seed: `12092C0FFEE002` (the premise leg used `12092C0FFEE001`).

| Class | Tests | Failures |
| --- | --- | --- |
| org.apache.solr.core.TestConfigSetImmutable | 3 | 0 |
| org.apache.solr.core.TestSolrConfigHandler | 8 | 0 |
| org.apache.solr.rest.TestRestManager | 4 | 0 |
| org.apache.solr.rest.schema.TestBulkSchemaAPI | 16 | 0 |
| org.apache.solr.rest.schema.analysis.TestManagedStopFilterFactory | 5 | 0 |
| org.apache.solr.rest.schema.analysis.TestManagedSynonymGraphFilterFactory | 4 | 0 |
| org.apache.solr.schema.TestUseDocValuesAsStored2 | 1 | 0 |
| org.apache.solr.search.TestExtendedDismaxParser | 39 | 0 |
| org.apache.solr.update.RootFieldTest | 2 | 0 |
| **Total** | **82** | **0** |

The three classes the round 33 GitHub dispatches did not confirm are TestConfigSetImmutable, TestUseDocValuesAsStored2, and RootFieldTest. The P5 gate job (`gates/SOLR-12092-edismax-p5.md`) can now name all nine.
