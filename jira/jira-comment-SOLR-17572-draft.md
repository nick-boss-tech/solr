[~malliaridis] I looked at SOLR-17572 (migrate Gradle files to Kotlin DSL). I have not changed anything. The numbers are read from apache/solr main at 79b23df58d on 2026-10-24.

 * 97 Groovy {{.gradle}} files, 12,204 lines in total: 56 under {{gradle/}}, 33 under {{solr/}}, 5 under {{build-tools/}}, and the root {{build.gradle}} and {{settings.gradle}} plus one file under {{platform/}}.
 * 78 {{apply from:}} calls and 183 {{ext}} uses. A mechanical port has to rewrite these by hand.
 * Three Kotlin DSL scripts already exist outside {{ui/}}: {{gradle/testing/coverage.gradle.kts}}, {{gradle/validation/dependency-analyze.gradle.kts}} and {{solr/webapp/js-client/build.gradle.kts}}. Per-file migration is already an accepted pattern here.

Does this evidence meet the bar you set for the ticket? That is the one question I am asking.

The options as I see them:
 * Option A: migrate all 97 files in one change. This is the "all together" option in the ticket.
 * Option B: migrate by slice, one PR per slice, starting with a small file.
 * Option C: close the ticket and leave the Groovy scripts as they are, since Kotlin DSL scripts already work alongside them.

My recommendation is Option B. A Kotlin script can still apply a Groovy file with {{apply(from = ...)}}, so each slice can land without a flag day. The plan has seven slices: the three small files {{build-tools/missing-doclet/build.gradle}} (44 lines), {{build-tools/missing-doclet/settings.gradle}} (31 lines) and {{build-tools/build-infra/settings.gradle}} (31 lines), then the two remaining {{build-tools/}} files, the root and {{platform/}} files, {{gradle/}} and {{solr/}}. If the direction is right, the first step is the port of {{build-tools/missing-doclet/build.gradle}}, checked by running that included build's {{build}} task.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
