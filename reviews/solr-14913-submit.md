# solr-14913-submit

- Branch: origin/solr-14913-submit
- Head: b80221f46d3c
- Base: upstream/main at 9d7cc2884e8a (merge-base b5c71bc5573c, 46 commits behind)
- Scope: 4 commits. `ExtendedDismaxQParser.java` (+43: `MatchNoDocsQuery` short-circuit in two alias entry points, `isValidAliasTarget`, `hasValidAliasTarget`, skip in `getQueries` and the multi-term variant), `TestExtendedDismaxParser.java` (+81: one new test, `testAliasingWithNonSchemaField`), changelog `SOLR-14913.yml` (+7)
- Verdict: Nearly (code reads correctly; the MatchNoDocs behaviour is an owner call, below)
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff and the code at the listed head.

## Delta check against earlier disposition

- Handoff: round 33 disposition at this head; the MatchNoDocs behaviour choice awaits owner ratification. Head unchanged: `origin/solr-14913-submit` is at `b80221f46d3c`, as listed.
- Not ratified: this review poses the MatchNoDocs choice and does not decide it.

## Verified code facts

- Helpers resolve: `schema` is an instance field of the parser (`ExtendedDismaxQParser.java`, around line 1763); `MagicFieldName.get` exists (`SolrQueryParserBase.java:178`); `Set`, `HashSet`, `MatchNoDocsQuery` are imported.
- `IndexSchema.getFieldTypeNoEx` checks explicit fields and then dynamic-field patterns (`IndexSchema.java:1458-1462`), so a dynamic-field target counts as usable.
- Both alias entry points get the same check: the single-term path (around line 1179) and the multi-term path (around line 1227). An alias with no fields (`a.fields.size() == 0`) is excluded, so the old `getQuery()` fallback is kept for that case.
- `hasValidAliasTarget` looks through nested aliases and uses `seen` to avoid cycles. `validateCyclicAliasing` still runs first in both entry points.
- The skip in `getQueries` and the multi-term variant (`continue` on an unusable target) only changes behaviour for targets that fail `isValidAliasTarget`.

## Findings (ranked)

1. **MEDIUM, verified. The branch turns a configuration error into a silent skip, and the all-missing case into "matches nothing".** An unusable `f.<alias>.qf` target is now skipped without an error. An alias whose targets are all unusable returns `MatchNoDocsQuery`. Base surfaces these as errors or a fallback (see finding 2). A typo in an alias target is therefore invisible to the caller. The changelog states the new behaviour as settled ("an alias with no usable target matches nothing"). Per the handoff, this is pending owner ratification. Owner call, below.

2. **MEDIUM, hypothesis. The two descriptions of base behaviour disagree.** The changelog says the old code "fail[s] the whole query" for a missing target. The test comment for the all-missing case says the unpatched parser "returned no documents, because the clause was silently dropped". Both cannot be exact. The first test's comment describes the base as "abort[ing] the parse into the escape+re-parse fallback". Base `getQueries` calls `getAliasedQuery` per target (`ExtendedDismaxQParser.java`, around lines 1177-1190 and 1394-1412). The exception path for an unknown non-alias field was not traced. The handoff's premise run against base (the Linux gate) settles this. Until then, neither description should be copied into the PR text.

3. **LOW, verified. Test does not pin the compound-query effect its comment describes.** The all-missing comment says "what changes is compound queries, where the dropped clause used to leave the remaining clauses to decide the result". The two all-missing assertions are both single-clause or two-term alias queries (`myalias:Zapp`, `myalias:(Zapp Brannigan)`). No assertion covers a compound query such as `myalias:Zapp OR name:Zapp`. If the owner keeps the behaviour, a compound case would pin the claim.

4. **LOW, verified. Multi-term fallback is pinned.** The first assertion checks `parsedquery` for `name:Zapp` and `name:Brannigan`. That catches a fallback to the default field, which the comment describes. Good.

5. **LOW, verified. Dynamic-field targets are treated as usable.** Via `getFieldTypeNoEx`. This is correct for dynamic fields, and no test covers it.

6. **LOW, verified. Cycle safety.** `hasValidAliasTarget` threads `seen` through nested aliases, and cyclic aliasing is already rejected by `validateCyclicAliasing`, which runs before the new check. No change needed.

## Owner call (posed, not decided)

**MatchNoDocs for an alias with no usable target.** Options the owner can ratify or reject:

- (a) Keep the branch: an alias whose targets are all unusable matches nothing, and unusable targets among usable ones are skipped silently. Changelog stays as written, once the base-behaviour question (finding 2) is settled.
- (b) Keep the skip but raise an error when no target is usable, so a configuration typo is reported instead of matching nothing.
- (c) Keep the old behaviour for alias targets and treat this as a separate ticket.

The branch implements (a). The review does not choose between them. Finding 1 is the reason the owner should decide before the PR text is written.

## Not checked

- Not compiled, formatted (spotless), or run. Pass and fail on base and head is a hypothesis.
- Base behaviour for an unknown non-alias field inside `getQueries` (finding 2).
- Compound-query behaviour (finding 3) has no test to read.
- Error Prone was not run.
- Nothing pushed or posted to GitHub or JIRA.
