# solr-6009-submit

- Branch: origin/solr-6009-submit
- Head: a41bb034a1f4 (matches the listed head)
- Base: upstream/main at 9d7cc2884e8a (merge-base 14c7aac0d151, 45 commits behind)
- Scope: 6 commits, 3 files (+96). `ExtendedDismaxQParser.java` (+12: `QType.REGEXP` at line 962, `getRegexpQuery` override at lines 1163-1170, dispatch at line 1488), `TestExtendedDismaxParser.java` (+73, two tests), changelog `SOLR-6009-edismax-regexp.yml` (+11, type `fixed`)
- Verdict: Close (no defect found; the test covers the configurations round 28 asked for, and the fan-out cost is a note for the PR text)
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff and the code at the listed head.

## Delta check against earlier disposition

- Bulk review `research/branch-reviews/round-28/SOLR-6009-review.md` (verdict Close) was written at snapshot `b2976bdc7dc`, an ancestor of the head. The delta adds the test commit (`a41bb034a1f`) and changes the changelog. The code is unchanged since the snapshot.
- Round-28 F1 (the test uses only `qf=name`): addressed. Tests now cover multiple qf fields (`name title`), a fielded regex (`name:/.*apper/`), `sow=false`, and a field reachable only through a dynamic field (`t_regexprobe`).
- Round-28 F2 (fan-out cost): still a note for the PR text; see finding 2.
- Round-28 F3 (the changelog does not say that a bare regex now matches): addressed. The changelog says a bare regex "previously matched nothing (or errored)... now matches".
- Round-28 F4 (handoff doc added and removed): no handoff or TESTING file at head.

## Verified code facts

- `getRegexpQuery(String, String)` matches the base signature at `SolrQueryParserBase.java:1333`. The `@Override` and the `super.getRegexpQuery(field, val)` dispatch at line 1488 are consistent. Compilation is not checked.
- The only `switch (type)` in the file is in `getQuery()` (line 1445). Round 28 read only that dispatch. This review enumerated the `QType` uses, and there are no other switches.
- An exception from a field's query makes `getQuery()` return `null` (its catch at line 1496), so that field is dropped rather than failing the request.

## Findings (ranked)

1. **LOW, verified. Fan-out cost.** A regex clause now builds one `RegexpQuery` per qf field, the same as wildcard and prefix already do. A qf field whose type cannot take a regex is dropped by the catch above. Note this in the PR text. Pathological patterns are still bounded by Lucene's determinisation limit, as before.

2. **LOW, verified. Unknown-field fallback.** `nosuchfield_xyz:/.*apper/` makes `getAliasedQuery` throw `unknownField()`. That happens outside `getQuery()`'s catch, so the first parse fails and the escaped reparse matches nothing. The test's `numFound=0` agrees with this reading.

3. **LOW, hypothesis (consistency, not a defect here).** `addPhraseFieldQueries` still passes regex clause text (`\/\.\*apper\/`) to the pf analyzers. This is the SOLR-3962 class. The branch does not change pf, so this is not a 6009 defect.

4. **LOW, proof. Fixture reading.** In the test index, doc 44 ("The Zapper") is the only doc whose `name` or `title` holds a token ending in `apper`, which matches the `numFound=1` assertions. The dynamic-field test relies on the `t_*` dynamic field in `schema12.xml` (line 700) and on doc "snapper". Not run.

## Owner calls (not decided here)

- None blocking. Round 28 recorded none.

## Interactions with other branches

- None in code. The regex clause goes through the same alias fan-out as wildcards, and it shares no code with the other eDisMax branches.

## Not checked

- Not compiled, formatted, or run.
- Regex performance beyond Lucene's determinisation limit.
- Spotless and Error Prone were not run.
- No GitHub or JIRA writes.
