# PR draft: SOLR-18552 (for Nick's approval; not opened)

- Proposed title: SOLR-18552: Set MMapDirectory shared arena permits to 64
- Head: nick-boss-tech:solr-18552-submit at a5406c9fe531161df7b424b2a55143489b95166e. Base: branch_9x.
- Gate: LIGHT GATE GREEN (gates/SOLR-18552.md on coordination). Opening needs Nick's approval.

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

## What happens today

**On the 9x line, Solr starts with Lucene's built-in shared arena permit limit of 1024.**

- branch_9x pins Lucene 9.12.3, whose MMapDirectory reads the system property `org.apache.lucene.store.MMapDirectory.sharedArenaMaxPermits` and falls back to 1024 when it is not set.
- Lucene 10 already defaults to 64, which is why the ticket scopes this change to the 9x line.

## What this change does

**The launch scripts set the limit to 64 as a script default.**

- [bin/solr](https://github.com/apache/solr/blob/a5406c9fe531161df7b424b2a55143489b95166e/solr/bin/solr) and [bin/solr.cmd](https://github.com/apache/solr/blob/a5406c9fe531161df7b424b2a55143489b95166e/solr/bin/solr.cmd) each add `-Dorg.apache.lucene.store.MMapDirectory.sharedArenaMaxPermits=64` to SCRIPT_SOLR_OPTS.
- SCRIPT_SOLR_OPTS is placed before the user's SOLR_OPTS on the launch line, so an operator who sets the property themselves still overrides this default.
- A changelog entry records the changed default.

## Proof

**The setting names the exact property the pinned Lucene reads, checked against the jar.**

- The property string in both scripts matches the constant read by `MMapDirectory.getSharedArenaMaxPermitsSysprop` in lucene-core 9.12.3, whose fallback default is 1024, so the change moves the effective default to 64 on this line.
- bin/solr passes a syntax check (bash -n), and the changelog entry parses.
- No packaging test asserts on the script options, so nothing else in the tree needs to change with this default.

## Limits

**Two limits, both about reach.**

- The Windows script change is verified by inspection and parity with bin/solr, not by execution.
- The permit limit is consumed by Lucene's memory segment provider; where that provider is not in use, the setting has no effect.

## Changelog

Set a lower shared arena permit limit for MMapDirectory

---

*AI-generated description, human-reviewed.*
