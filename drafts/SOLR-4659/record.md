# Skip record: SOLR-4659

- Ticket: SOLR-4659, "SolrResourceLoader should always get parent classloader explicitly (no default behavior)". JIRA status read 2026-10-20T15:25:43Z (UTC): Open, resolution None, updated 2013-04-01.
- Verdict: keep-open (kept; re-read at the pinned head). The null default is still present; removing it changes a constructor used across the codebase, which is a maintainer decision.
- Draft branch: solr-4659-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T15:25:43Z (UTC).

## Ticket reading

The resource loader should receive its parent classloader explicitly, with no default and no null. A 2013 comment asks for a constructor that requires the parent and throws on null.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/core/SolrResourceLoader.java:187-188`: `SolrResourceLoader(Path instanceDir)` passes `null` as the parent.
- Lines 196-207: the constructor `SolrResourceLoader(Path instanceDir, ClassLoader parent)` replaces a null parent with the class's own loader (`getClass().getClassLoader()`). So the default the ticket asks to remove is present.
- The other constructor at line 168 takes a parent explicitly.

## History

- evidence.py at the pinned head: no commits naming SOLR-4659.

## Reproduction attempts

Not applicable: the behavior is a constructor default, not a runtime failure.

## Gap analysis

The default is in place. Removing the one-argument constructor or throwing on null affects every caller of that constructor; the change needs a maintainer decision on the public constructor surface.

## Verdict justification

keep-open meets the rubric for an open design question: attempts made are the constructor reading; the missing piece is the maintainer decision on the constructor. close-fixed and close-obsolete are not supported: the default is present. pr-needed is not chosen since the constructor change touches callers.

## Limits

This record does not count the callers of the one-argument constructor. It does not change the constructor.
