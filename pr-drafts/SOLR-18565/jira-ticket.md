Summary: test_ssl.bats "use different hostname when not checking peer-name" fails on the ubuntu-26.04 runner image with a different curl error message

Description:

Test 88, "use different hostname when not checking peer-name", fails at solr/packaging/test/test_ssl.bats line 103. It is the only failing test in the bats job log for https://github.com/apache/solr/actions/runs/39688109570/job/119558270184, a run of pull request https://github.com/apache/solr/pull/5190. That PR does not change SSL code.

The assertion expects curl's output to contain "no alternative certificate subject name matches target host name". In this run curl exited with code 60 and printed:

    curl: (60) SSL: no alternative certificate subject name matches target hostname 'localhost'

The only difference is "hostname" where the test expects "host name". Earlier steps in the same test pass in this run, including starting Solr with SSL and running solr create and solr api over https.

The same test passed on the ubuntu-24.04 runner image in two recent runs of the same workflow: https://github.com/apache/solr/actions/runs/38613980754 (2026-10-14) and https://github.com/apache/solr/actions/runs/39499583835 (2026-10-21). Both show "ok 88 use different hostname when not checking peer-name". Those runs are on the SOLR-18234 branch, and their logs do not print curl's message, so the wording on that image is inferred from the passing assertion.

This is not a Solr regression. The error text comes from the operating system's curl.

Suggested fix: match the stable part of the message, for example "no alternative certificate subject name matches target host", or check for curl's exit code 60.

Reproduction: run the Solr Script Tests job on the ubuntu-26.04 runner image (20261012.163) with test_ssl.bats as it is at 78923482727b8530d26ad08db734712ab2dacb12. The expected failure is at line 103 with the message above.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
