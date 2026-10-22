# Claim: pool-owner-items-scrub-implementation-round-1, item 2 (SOLR-11650)

- Claimant host: vm5, matching `hosts/vm5.md`.
- Assignment: `assignments/pool-owner-items-scrub-implementation-round-1.md`, item 2.
- Branch: `solr-11650-submit`, head `e4f5e941cd`.
- Capability tags: implementation, gate.
- Status: CLAIMED.
- Started: 2026-10-22T15:12:00Z (UTC).

Correction to the decision (rung 4, recorded before any code change). The decided rule "the userinfo ends at the last `@` before the first `/`, `?` or `#`" cannot match a password with a raw `/` or `?`: no `@` comes before that character, so the rule finds no userinfo. Two facts settle the choice:
- `URLUtilTest.testRedactUserInfoFailsClosedOnUnparseableUrls` pins that an `@` in the path or query is not user-info (`http://localhost:8983/solr/a@b?x=y@z` stays as given). A rule that takes the last `@` in the URL would break that pin.
- A raw `/` or `?` in a password is not user-info under RFC 3986 at all: `java.net.URI` reads `http://user:pa/ss@host` as host `user`, port `pa`. Such a URL cannot connect, so its only exposure is in the error and log messages that print it.

Chosen behavior: `URLUtil.redactUserInfo` keeps its current rule and its pins. The `IndexFetcher` messages (malformed leaderUrl, forbidden, and the leaderUrl update log) show the URL redacted when `redactUserInfo` changes it, and show a withheld placeholder when the URL contains `@` and redaction leaves it unchanged. Rung 4; reason: the messages are where the credentials leak, and withholding keeps the pinned URLUtil behavior.

Heartbeat: 2026-10-22T15:12:00Z
