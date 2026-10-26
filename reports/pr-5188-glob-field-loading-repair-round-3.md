# PR 5188 glob field loading, repair round 3 (vm6)

- Failed head: `53ad2ccacbf205333f9029ed9f6ebbc93da034f2` (round 2). Its pre-fix RTG leg failed on `unrelated_s should remain lazy`, the predicted assertion. Its pre-fix prefetch leg failed at the `source_s` response check, and the post-fix legs were not run.
- Cause, verified with lxml on the real response shape: the XML writer indents `arr` children, so the string-value of `<arr name="source_s">\n      <str>source</str>\n    </arr>` includes the surrounding whitespace. `[.='source']` matches nothing, while `[normalize-space(.)='source']` matches one node.
- Repair commit: `7ac2bc86fed51ed420f3b1a9559e167631e5a3c8` (`PR 5188: normalize whitespace in glob test response checks`), Nick Shanin, no trailers, fast-forward pushed; remote read back as this SHA.
- Scope: test files only, 11 insertions and 11 deletions. In the same three methods as round 2, `//doc/*[@name='X'][.='v']` became `//doc/*[@name='X'][normalize-space(.)='v']`. No production line changed.
- Pre-fix overlay for round 3: `e07455a` production plus `BasicFunctionalityTest.java` blob at F3 and `TestPseudoReturnFields.java` blob at F3.
