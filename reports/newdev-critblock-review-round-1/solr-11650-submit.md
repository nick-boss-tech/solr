# Review: SOLR-11650, branch solr-11650-submit (unit 7)

Head reviewed: e4f5e941cd8e88524196b03f2022261c2215c3ba (ls-remote 2026-10-20T14:46Z). Base: 14c7aac0d1. Commits: aa95e32a5e (redact replication credentials in response details), 5e0e449bfb (fail closed when redacting leaderUrl; handler test; changelog), e4f5e941cd (redact leaderUrl in IndexFetcher log and error messages). Diff: 7 files, +255/-5, including `URLUtil.redactUserInfo` in solrj.

## Pass 1

Review (2026-10-20, vm5):

- Change. `URLUtil.redactUserInfo` (solrj) replaces the password in a URL with `********`. `IndexFetcher` uses it in the malformed-URL error, the allow-list (FORBIDDEN) error, and the "Updated leaderUrl" log line. `ReplicationHandler` uses it for the `leaderUrl` detail.
- Finding, blocker (out of repair scope; HANDED BACK): redaction fails open for a password that contains a raw `/` or `?`. The regex is `^([A-Za-z][A-Za-z0-9+.-]*://)([^/?]*)(?=@)`, so the user-info group stops at the first `/` or `?`. Checked on the regex (python, same pattern): `http://user:pa/ss@host:8983/solr/c1` and `http://user:pa?ss@host:8983/solr/c1` come back unchanged, so `pa/ss` and `pa?ss` remain in the text. The javadoc says a raw `/`, `?` or `#` ends the authority and that such passwords reach a URL percent-encoded; the regex does not enforce that, and a password with a raw `/` or `?` is not percent-encoded in the input. In `IndexFetcher` the FORBIDDEN and malformed messages print the redacted URL, so the password fragment would be printed. Fixing it changes redaction behavior and the expected values in `URLUtilTest`, which is outside the Gate repairs scope. Route: HANDED BACK for implementation.
- Finding, minor: the javadoc lists `#` as an authority delimiter, the regex does not exclude `#`. `http://host#x@y/z` redacts to `http://********@y/z`, which hides the host. Display-only; no credential is exposed by this case.
- Ticket fit: the change covers the three places the ticket names (log line, malformed error, allow-list error) and the response detail. Correct in intent apart from the gap above.
- Changelog: parses (receipt); not rechecked this pass.

Gate (vm5, this pass, on e4f5e941cd):
- `:solr:core:compileJava :solr:core:compileTestJava :solr:solrj:compileJava` (offline): rc=0 (`/workspace/gates/logs/vm5/g11650-compile.log`).
- `IndexFetcherLeaderUrlRedactionTest` (core): tests=2, failures=0, errors=0, skipped=0 (`/workspace/gates/logs/vm5/g11650-test.log`).
- `URLUtilTest` (solrj): tests=18, failures=0, errors=0, skipped=0 (`/workspace/gates/logs/vm5/g11650-urlutil.log`).

Not run this pass: tidy, Error Prone, the module checks. The receipt's gate covers them on the head.

Outcome: HANDED BACK (implementation). The fix for the raw `/` and `?` case is for the implementer; this review does not repair it.
