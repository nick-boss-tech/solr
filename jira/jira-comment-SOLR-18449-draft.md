# Jira comment draft: SOLR-18449

Status: DRAFT (not posted). Shape: staged ask. Addressee: Eric Pugh (reporter). Prepared by vm6, 2026-10-27, from `reports/solr-18449-test-sysprops-audit-round-1.md` (pinned main `e59ce3c97d`). Posting needs Nick Shanin's explicit go.

---

I went through the system properties that test code reads or sets at apache/solr main {{e59ce3c97d}}. Of 178 names that only test code uses, 32 are test-harness knobs, and only 2 of those already start with {{test.}}. The rest split across three prefixes:

 * {{solr.tests.}}: 15 names, for example {{solr.tests.numeric.dv}} ([TestJoin.java:49|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/core/src/test/org/apache/solr/TestJoin.java#L49]) and {{solr.tests.cloud.cm.enabled}} ([ChaosMonkey.java:77|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/test-framework/src/java/org/apache/solr/cloud/ChaosMonkey.java#L77]).
 * {{solr.test.}}: 10 names, for example {{solr.test.fieldLimit.warnOnly}} ([NumFieldLimitingUpdateRequestProcessorIntegrationTest.java:42|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/core/src/test/org/apache/solr/update/processor/NumFieldLimitingUpdateRequestProcessorIntegrationTest.java#L42]).
 * {{tests.}}: 5 names, for example {{tests.ssl}} ([RandomizeSSL.java:132|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/test-framework/src/java/org/apache/solr/util/RandomizeSSL.java#L132]).

The example in the ticket, {{test.solr.cloud.replica.autorecovery.skip.enabled}}, is not in the tree. The nearest name is {{solrcloud.skip.autorecovery}}, which main code reads ([ZkController.java:159|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/core/src/java/org/apache/solr/cloud/ZkController.java#L159]), so it is not test-only and I have left it out. Nothing test-oriented appears in the Ref Guide.

Eric, the ticket asks for the {{test.}} prefix. Which prefix should the test-only names standardize on: {{test.}} for all 30, or keep {{solr.tests.}} and rename the rest to match?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

Correction for Nick before posting: apache/solr PR #4913, "SOLR-18449 standard test properties naming", is open. This draft does not account for it, so check that PR's prefix choice before posting.
