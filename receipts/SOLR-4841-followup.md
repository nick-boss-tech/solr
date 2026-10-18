# SOLR-4841 follow-up receipt

Status: GATE PASSED on the branch head (vm4). Review rounds 2 and 3 are not run, so the assignment is not SATISFIED.

- Ticket: SOLR-4841 (the follow-up has no key of its own yet; see reports/SOLR-4841-followup-round-1.md).
- Branch: solr-4841-langid-followup-submit on https://github.com/nick-boss-tech/solr. Tip: bf1b01d0dc0ecb0455a19c2cb3276f5a1318d437. Base: apache/solr upstream main c8ab71430f.
- Module: :solr:modules:langid. Test-only; no production changes; no changelog fragment.
- Environment: vm4, Linux, JDK 21.0.12.1, Gradle 9.7.0 from the branch wrapper, `NODE_ENV` unset. Seed for focused tests 4841C0FFEE4841.

## Gate counts at the tip

- Tidy on the final tree: rc=0, zero changed paths after tidy. Log: /workspace/gates/logs/vm4/SOLR-4841-tidy.log, SOLR-4841-tidy-status.log.
- Error Prone compile (`-Pvalidation.errorprone=true`, main and test of :solr:modules:langid): rc=0. Log: SOLR-4841-errorprone.log.
- Focused, fresh JUnit XML at 2026-10-18T01:14:45Z: CustomLanguageIdentifierProcessingTest tests=1 failures=0 errors=0 skipped=0; CustomLanguageIdentifierTest tests=1 failures=0 errors=0 skipped=0. Log: SOLR-4841-focused.log.
- `:solr:modules:langid:check -x test`: rc=0 (BUILD SUCCESSFUL, 65 actionable tasks). Log: SOLR-4841-check.log.

## Pre-fix leg

Parent state, compileTestJava: rc=1, "DetectedLanguage(String,Double) is not public in DetectedLanguage". Log: /workspace/gates/logs/vm4/SOLR-4841-followup-prefix-compile.log.

## History at the branch

- 92567d75c4 failed tidy on import order. Superseded by bf1b01d0dc, a fast-forward commit with the sorted import. The gate restarted at bf1b01d0dc. The first gate run on 92567d75c4 was stopped and its logs were discarded.
