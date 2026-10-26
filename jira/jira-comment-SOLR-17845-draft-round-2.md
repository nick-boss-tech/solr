# Jira comment draft: SOLR-17845, round 2 (rewrite)

Status: DRAFT (not posted). Rewrite of the round-1 status note at Nick Shanin's direction on 2026-10-26. Changes from round 1: the two open-item claims were re-verified against main at `0d6b2e32f5` on 2026-10-26 (the refresh TODO is still at `HttpClientUtils.kt` lines 107 to 108, and `idToken` still occurs only at its declaration in `AuthorizationResponse.kt` lines 35 to 36); the met-items list with line citations that were only verified at the older SHA was dropped; the text now acknowledges Christos's own 2026-02-22 comment that token refresh should be addressed under this issue; the SOLR-17885 question was dropped to keep one question. Addressee: Christos Malliaridis (reporter and assignee). Posting needs Nick Shanin's explicit go. Note: the ticket itself has not changed since 2026-02-22; the round-1 draft's caution was about its crash-path claim being code-read only, which the rewrite keeps explicit.

---

[~malliaridis] A status note on this ticket, checked against {{main}} at {{0d6b2e32f5}} today. The OAuth support landed in [PR #3754|https://github.com/apache/solr/pull/3754] in February, and the ticket still shows Patch Available with no fix version, which no longer describes it.

Of the acceptance list, two items are still open, read from the code:

 * Token refresh. {{HttpClientUtils.kt}} lines 107 to 108 still read {{refreshTokens { TODO("Not implemented yet") }}}. You noted when the PR merged that refresh should be addressed under this issue. From the code, an expired access token leads Ktor to call that block, so it would throw {{NotImplementedError}}; I have not reproduced this against a running Solr.
 * ID token identity. {{AuthorizationResponse.kt}} lines 35 to 36 parse {{id_token}}, and nothing else in the UI tree reads it, so an OAuth session still carries no user identity.

Do you want both items to stay under this ticket until they land, or should the ticket be resolved on the merged work, with refresh and identity split out as their own tickets?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
