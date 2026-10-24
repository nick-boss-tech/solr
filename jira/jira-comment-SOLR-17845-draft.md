[~malliaridis] I checked SOLR-17845 against {{main}} at {{498a09aea9}} on 2026-10-24. The ticket still shows "Patch Available", but the OAuth work landed in [PR #3754|https://github.com/apache/solr/pull/3754] (merge commit {{d06531384d}}, 2026-02-22). The status looks stale. Two parts of the acceptance list are still open.

Met, read from the code:
 * Client ID comes from the server's auth data, not a hardcoded value: {{SolrAuthData.kt}} lines 29 to 30, and {{OAuthStoreProvider.kt}} line 88.
 * Authorization Code flow with PKCE and an {{S256}} challenge: {{OAuthStoreProvider.kt}} lines 82 to 93, and {{AuthUtils.kt}} line 49.
 * Redirects: desktop uses an embedded server on 127.0.0.1:8088 ({{AuthUtils.desktop.kt}} line 24). Web uses postMessage ({{PlatformOAuthStoreClient.kt}} in {{wasmJsMain}}, line 46).

Not met:
 * Token refresh. {{HttpClientUtils.kt}} lines 107 to 108 still read {{refreshTokens { TODO("Not implemented yet") }}}. The refresh token is stored and never used. Ktor 3.5.2 calls the refresh block on a 401 response, and Solr's JWT plugin returns 401 for an expired token ({{JWTAuthPlugin.java}} lines 518 to 529 and 692 to 696). So from the code, an expired access token should reach the TODO and throw {{NotImplementedError}}. I have not reproduced this against a running Solr.
 * ID token identity. {{AuthorizationResponse.kt}} lines 35 to 36 parse {{id_token}}, and nothing reads it. Nothing on {{main}} shows a user identity for an OAuth session.

No open PR covers either item (GitHub search on 2026-10-24).

Question for you: should SOLR-17845 move to Resolved, with token refresh and ID-token identity filed as new sub-tasks? Or should it stay open until those land? And should [SOLR-17885|https://issues.apache.org/jira/browse/SOLR-17885] close when token refresh lands, since it is the wrapper for these?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
