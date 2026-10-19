# PR draft: SOLR-18552 (OPENED as PR #5181, ready, 2026-10-18 at Nick's approval)

- Proposed title: SOLR-18552: Set MMapDirectory shared arena permits to 64
- Head: nick-boss-tech:solr-18552-submit at a5406c9fe531161df7b424b2a55143489b95166e. Base: branch_9x.
- Gate: LIGHT GATE GREEN (gates/SOLR-18552.md on coordination). Opened 2026-10-18: https://github.com/apache/solr/pull/5181 (base branch_9x, head cb32d0d9661, ready (two follow-up commits set the limit on the existing init line in both launch scripts). Body rewritten to the PR formula on 2026-10-18 at Nick's note (Jira link line, merge-base citations with anchors, dated Proof at the head, Changelog link line, AI assistance footer).).

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18552

## What happens today

**On the 9x line, Solr starts with Lucene's built-in shared arena permit limit of 1024.**

- branch_9x pins Lucene 9.12.3, whose MMapDirectory reads the system property `org.apache.lucene.store.MMapDirectory.sharedArenaMaxPermits` and falls back to 1024 when it is not set.
- Neither launch script sets it: at the merge base, [bin/solr](https://github.com/apache/solr/blob/3511867c3e9e3bc9f5977f3767d6d5276f751b6f/solr/bin/solr#L725) initializes its script options empty, and [bin/solr.cmd](https://github.com/apache/solr/blob/3511867c3e9e3bc9f5977f3767d6d5276f751b6f/solr/bin/solr.cmd#L792) does the same.
- Lucene 10 already defaults to 64, which is why the ticket scopes this change to the 9x line.

## What this change does

**Both launch scripts set the limit to 64 on the script options line.**

- [bin/solr](https://github.com/apache/solr/blob/cb32d0d966159edb237d581318af9bf96071c0d1/solr/bin/solr#L725-L726) initializes SCRIPT_SOLR_OPTS with `-Dorg.apache.lucene.store.MMapDirectory.sharedArenaMaxPermits=64`; [bin/solr.cmd](https://github.com/apache/solr/blob/cb32d0d966159edb237d581318af9bf96071c0d1/solr/bin/solr.cmd#L792-L793) does the same.
- SCRIPT_SOLR_OPTS is placed before the user's SOLR_OPTS on the launch line, so an operator who sets the property themselves still overrides this default.

## Proof

**The property and its current default were checked against the pinned Lucene jar; verified 2026-10-18 at head cb32d0d9661.**

- The property string in both scripts matches the constant read by `MMapDirectory.getSharedArenaMaxPermitsSysprop` in lucene-core 9.12.3, and the fallback in that class is 1024, so this change moves the effective default to 64 on this line.
- bin/solr passes a syntax check (bash -n) and the changelog entry parses.
- There is no test run for this change: it is a launch script default, and no packaging test asserts on the script options. The verification is by construction against the jar, stated plainly.

## Limits

**Two limits, both about reach.**

- The Windows script change is verified by inspection and parity with bin/solr, not by execution.
- The permit limit is consumed by Lucene's memory segment provider; where that provider is not in use, the setting has no effect.

Changelog: [changelog/unreleased/SOLR-18552-mmap-arena-permits.yml](https://github.com/apache/solr/blob/cb32d0d966159edb237d581318af9bf96071c0d1/changelog/unreleased/SOLR-18552-mmap-arena-permits.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
