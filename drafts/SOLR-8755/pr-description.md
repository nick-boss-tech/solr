# SOLR-8755: Add test cases for org.apache.solr.parser.TokenMgrError

## What happens today

`org.apache.solr.parser.TokenMgrError` has no direct test. Its constructors, the lexical error message built from the lexer state, the `addEscapes` helper, and the reason constants are not covered.

## What this change does

Adds `TokenMgrErrorTest` with five tests:

- the message constructor keeps the message and the reason;
- a lexical error with a character and an after-prefix builds the message;
- the end-of-input branch reports the lexical state;
- `addEscapes` escapes tabs, quotes and backslashes, and uses unicode escapes for unprintable characters;
- the reason constants keep their values.

No source file changes.

Behavior stated openly: the lexical error message quotes the numeric code point, for example `'120' (120)`, not the character. The generated lexer passes an int, and `LexicalErr` formats it as a string. The test pins the current output. Changing the wording is a source change and is not part of this PR.

## Proof

Test-only change, so there is no base-versus-change run. Head 31bdbd2488 on 2026-10-19: `:solr:core:test --tests org.apache.solr.parser.TokenMgrErrorTest` gives 5 tests, 0 failures, 0 skipped (log `/workspace/gates/logs/vm4/solr-8755-test-3.log`, XML `/workspace/gates/logs/vm4/solr-8755-TEST-TokenMgrErrorTest.xml`). `:solr:core:spotlessJavaCheck` passes in the same run.

## Question for the maintainer

Should the lexical error message quote the character instead of its code point? That is a source change and would need its own test update.
