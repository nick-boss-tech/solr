# Assignment: SOLR-18556 cuVS indexing breaks with Lucene 10.4, round 1

Status: OPEN

## Background

SOLR-18556 (Open, Major, affects 10.1, no fix version, created 2026-10-18) says GPU-accelerated indexing with the `cuvs` module breaks on Solr 10.1. The stack trace in the ticket is `java.lang.NoSuchMethodError` for `HnswGraph$NodesIterator.getSortedNodes` in `com.nvidia.cuvs.lucene.Lucene99AcceleratedHNSWVectorsWriter.writeGraph`. The ticket says the cuvs-lucene maintainers expect the fix in cuvs-lucene v26.12 (December 2026), supporting Lucene up to 10.5.

Facts checked on 2026-10-24:
- Solr 10.1 (`gradle/libs.versions.toml` at RC3 tip 749712b719) pins `cuvs-lucene` 25.12.0 and Lucene `apache-lucene` 10.4.0.
- The POM of every cuvs-lucene release on Maven Central (25.12.0, 26.02.0, 26.04.0, 26.06.0, 26.08.0, 26.10.0) declares `lucene-core`, `lucene-codecs` and `lucene-backward-codecs` 10.2.0. No released version declares 10.4. A pin bump alone is not a documented fix.
- The module is `solr/modules/cuvs`. Codec: `CuVSCodec.java`. The only test is `TestCuVSCodecSupportIT.java`.
- Docs that say a fix is expected: `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc` line 88 and `solr/solr-ref-guide/modules/query-guide/pages/dense-vector-search.adoc` line 1038.

## Work

1. JIRA comment draft, in `jira/SOLR-18556-comment-draft-round-1.md`. Drafts only: no post. Under the current flow the owner approves each JIRA comment before it is posted. The draft states the checked facts above, the Maven POM evidence, and the open options from item 3. It does not promise a fix date beyond what the ticket says.
2. Reproduction check. Determine whether the failure can be reproduced without a GPU. Read `TestCuVSCodecSupportIT` and the codec path to see which branch runs without a GPU. Record in the report whether a reproduction needs a GPU host. Do not run GPU-only code on a host without a GPU.
3. Options report, `reports/solr-18556-round-1.md`. For each option: what changes in Solr, what it fixes, what it risks, and the evidence (file and line). The options are at least: (a) document the known issue and leave the code alone; (b) a Solr-side guard in `CuVSCodec` that falls back to the default codec and logs a warning when the cuVS writer fails with `NoSuchMethodError`; (c) pin to a new cuvs-lucene release when one is built against Lucene 10.4, with the release date and POM evidence. Give a recommendation for each. Do not implement any option.
4. Check for a newer cuvs-lucene release each time this claim is picked up, and record the POM Lucene version for it.

## Decisions

- Decision 1: JIRA first. Only drafts go into `jira/`. Nothing is posted to JIRA or GitHub without the owner's approval.
- Decision 2: No code change in `solr/modules/cuvs` in this round. A code option is a public behavior change and needs the owner's decision (rung 5 of `material/decision-ladder.md`).
- Decision 3: No pin bump. The POM evidence says the Maven releases still target Lucene 10.2.0.
- Decision 4: If reproduction needs a GPU and no GPU host is available, record "not reproduced: GPU required" in the report and stop there.
- Decision 5: The gate is out of scope for this round. If the owner picks option (b) or (c), a gate assignment is posted separately and goes to a host that lists the `gate` tag.

## Capability tags

`review`, `draft`, `records`. No `implementation` and no `gate` in this round.

## Deliverables

- `jira/SOLR-18556-comment-draft-round-1.md`
- `reports/solr-18556-round-1.md`

## Deadline

None set by the owner. Propose delivery by Mon 2026-10-26 so the owner can decide before the 10.1 release discussion moves on.
