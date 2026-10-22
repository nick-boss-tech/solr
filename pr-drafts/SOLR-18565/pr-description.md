🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18565

## What happens today

**Test 88 fails on the ubuntu-26.04 runner image because curl's error text changed.**

Test 88 ("use different hostname when not checking peer-name") is at [test_ssl.bats line 103](https://github.com/nick-boss-tech/solr/blob/7892348272/solr/packaging/test/test_ssl.bats#L103) on the base commit. It expects curl's output to contain "no alternative certificate subject name matches target host name". On ubuntu-26.04, curl exits with code 60 and prints "...matches target hostname 'localhost'". The word "hostname" does not match "host name", so the assertion fails.

This is the only failing test in the job log of [this run of PR #5190](https://github.com/apache/solr/actions/runs/39688109570/job/119558270184). Test 88 passed on ubuntu-24.04 in [run 38613980754](https://github.com/apache/solr/actions/runs/38613980754) and [run 39499583835](https://github.com/apache/solr/actions/runs/39499583835). Those runs are on the SOLR-18234 branch.

## What this change does

**The assertion matches only the part of the message that both wordings share.**

The expected text on [line 103](https://github.com/nick-boss-tech/solr/blob/2c747689c32bd17a6327ecd2b60999b972771269/solr/packaging/test/test_ssl.bats#L103) is shortened to "no alternative certificate subject name matches target host". The rest of the test is unchanged, and the check still asserts that verification fails. No product code changes.

## Proof

**The base fails in CI. The head result is pending.**

- Base: test 88 fails on ubuntu-26.04 in [run 39688109570](https://github.com/apache/solr/actions/runs/39688109570/job/119558270184). It is the only failing test in that job.
- Head: the Script Tests run on this PR is the check. Result: pending.
- Local check: the new expected text is a substring of the curl message in the failing run (string match).

## Limits

- The full bats suite runs only in this PR's CI.
- Only one failing run was read. Other PRs on the ubuntu-26.04 image were not checked.
- No other bats file uses the old wording (checked with a grep of `solr/packaging` at the base commit).

## Checklist

- [ ] I have reviewed the guidelines for How to Contribute and my code conforms to the standards described there to the best of my ability.
- [x] I have created a Jira issue and added the issue ID to my pull request title.
- [ ] I have given Solr maintainers access to contribute to my PR branch.
- [x] I have developed this patch against the `main` branch.
- [ ] I have run `./gradlew check`. Not run.
- [ ] I have added tests for my changes. Not applicable: this fixes an existing assertion.
- [ ] I have added documentation for the Reference Guide. Not applicable.
- [ ] I have added a changelog entry. Not added: test-only change, not user-facing.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
