# SOLR-3923 - hypothetical reproduction (nothing was compiled or run)

JIRA (2012, 3.5): edismax with `pf` on `((cat:string1) (Kitchen Sink))` built a bad phrase. The audit note said "fixed in 3.6.3",
but Shawn Heisey's last comment says the first form was fixed and a second form is still strange: with spaces around the
fielded clause, `(( special_cats:string1 ) (Kitchen Sink))` gives `catchall:"? ? kitchen sink"~3^2.0` (two holes).

## Root cause (read on `upstream/main`)
`splitIntoClauses` splits on whitespace, so `( special_cats:string1 )` yields three clauses: `(`, `special_cats:string1`, `)`.
The middle one has a field and is skipped by `addPhraseFieldQueries`; the two parens are bare words (`val` = `\(` / `\)`,
`raw` = `(` / `)`) and stay in `normalClauses`. They analyze to no tokens, which leaves position holes in the phrase.

## Change
`addPhraseFieldQueries` skips a bare clause whose raw text is only parens (new helper `isOnlyParens`). Parens attached to a word
(`(Kitchen`, `Sink)`) are unchanged. `TestExtendedDismaxParser` gets one assertion: `( id:s0 ) (zzzz xxxx)` with `pf=phrase_sw`
must contain `phrase_sw:"zzzz xxxx"` and no `?` in the parsed query.

## Guesses to verify first
- `phrase_sw` (schema12 `text_sw`, MockTokenizer + WDGF) may drop the paren without leaving a hole, so the assertion may pass without
  the fix. If so, switch the pf field to a type with a stop filter at query time (as `text_chars` was added for SOLR-3962).
- Clause `raw` is `(` for a lone paren (disallowUserField path, only colons are escaped) - read from the code, not run.
- `pf2`/`pf3` take the same `normalClauses` list, so they are covered; no separate assertion.

## Fail-before
Uncertain (see first guess). Enqueue with `-WithFailBefore`; a `NOT_PROVEN` verdict means the test field needs a stop filter.
