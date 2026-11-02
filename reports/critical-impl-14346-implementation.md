# SOLR-14346 implementation, Critical batch 2, unit 10 follow-up: DONE, branch gated GREEN and pushed

Fix: LbHttpSolrClient never revived a restarted server when the LB
endpoints carried no core and the client had no default collection.
The zombie check threw NullPointerException before sending any
request (Objects.requireNonNullElse with both arguments null), so
the server stayed a zombie forever. The check now picks the
collection with a null-tolerant choice and, when none is known,
probes /admin/info/system, which any live node answers, reviving
the server on a status 0 response. Behavior with a known
collection is unchanged.

Branch: solr-14346-submit on the fork, head
7a78b8cbdea19dcd906c8347b77d1c13a235ab06 (base 83d560030fde).
Two commits, both Nick Shanin <nick.boss.us@gmail.com>, no
trailers: 00784eb8f0 (the fix plus new SolrCloudTestCase
Solr14346ZombieCheckTest and changelog fragment) and 7a78b8cbde
(a @SuppressForbidden annotation on the test's zombie-map helper,
added after the first full gate failed only on
forbiddenApisTest).

Gate (runner ~/workspace/tools/14346-gate-runner.sh, output
~/workspace/tools/14346-gate-runner3.out, ended GATE RUNNER DONE,
GATE GREEN at the exact head, tree clean after): changelog YAML
rc=0; root tidy rc=0, 0 tracked files changed; Error Prone
compile BUILD SUCCESSFUL; pre-fix proof (production file reverted
to base) FAILED as expected, prefix XML 1 test, 1 failure, seed
14346C0FFEE14346, failure "restarted node 2 stayed a zombie",
production file restored; head focused test BUILD SUCCESSFUL,
head XML 1 test, 0 failures, same seed, fresh XML timestamps
2026-11-02T00:29:42Z (prefix) and 2026-11-02T00:31:12Z (head);
:solr:core:check -x test BUILD SUCCESSFUL. Proof pair complete.

History note for the record: the prior wake's head test run had
failed with the same "stayed a zombie" timeout and no admin probe
in the server log, which looked like a fix failure. A scratch
probe this run (invoking the private check directly, then
deleted) showed the fixed check reviving on its first call with
the admin probe hitting the server at status 0, so the earlier
failure was a stale or unrestored pre-fix run, not the fix; the
full gate above re-proved both legs at the exact head.

Claim: claims/critical-impl-14346-phone-agent.md, posted at
e1fe627e6b, marked DONE this run. No PR, no Jira/GitHub post.
Disk 63% used on /home/hatch, below the 85% guard.
