# New UI authentication: SOLR-17885 and SOLR-17845

Claim: `claims/new-ui-auth-round-1.md` (vm6). Assignment: `assignments/new-ui-auth-round-1.md`. Read-only: no code change, no Jira or GitHub comment, no PR.
Sources: Jira REST API (issues.apache.org, public reads, comments, changelog, remote links) on 2026-10-24; code on `origin/main` at `498a09aea9`; GitHub API for PR #3754 state. I did not build or run the `ui/` module.

## SOLR-17885: Add support for User Authentication to New UI

(a) Ask: a wrapper for the new UI's authentication sub-tasks. It closes "once sufficient authentication options are supported", meaning when no more sub-tasks are added. Reporter and assignee: Christos Malliaridis.

(b) Current state and evidence:
- Jira status is Reopened. The changelog shows Resolved as Fixed and then Reopened on 2025-09-30, the same day, after PR #3704 merged (commit f9f4547266). Christos's comment that day says #3704 "only addresses issues with the authentication and does not resolve this ticket, as it is a wrapper of multiple subtasks". That comment is the recorded reason for the reopen.
- Sub-tasks and related work:
  - SOLR-17659 (basic authentication): Resolved.
  - SOLR-17930 (MultiAuthPlugin compatibility, a "Required" link): Closed. Its title is "Improve MultiAuthPlugin compatibility with BasicAuthPlugin".
  - PR #3704 (basic auth fixes in the browser target): merged.
  - SOLR-17845 (OAuth): PR #3754 merged to `main` on 2026-02-22 (`d06531384d`), cherry-picked to `branch_10x` (`d0ff2b98c4`). The remaining open items are in (c) of SOLR-17845 below.
- The `pull-request-available` label on this ticket is stale, since #3704 is merged.

(c) Open questions:
- Whether the wrapper closes after the token-refresh slice, or stays open for the follow-ups Christos named on 2026-02-22: the custom URI scheme (`solr://callback`, "a separate issue") and the hardcoded Solr URL ("a separate issue"). The ticket text does not say which. This is a scope call for the ticket author.

(d) Verdict: NEEDS CHRISTOS. Question: "Should SOLR-17885 close when token refresh lands, or stay open for the custom URI scheme and the configurable Solr URL?"

## SOLR-17845: Implement authentication with OAuth in Admin UI

(a) Ask: OAuth 2.0 / OIDC in the new UI with five criteria: client ID configurable; Authorization Code Flow with PKCE; redirects to the browser and back; tokens refreshed when refresh tokens are issued; ID tokens used for user identity.

(b) Current state and evidence, per criterion:
- Client ID configurable: met, from server configuration rather than a hardcoded value. The client ID comes from the server's auth data (`ui/shared/src/commonMain/kotlin/org/apache/solr/ui/data/SolrAuthData.kt:29-30`, `ui/shared/src/commonMain/kotlin/org/apache/solr/ui/domain/OAuthData.kt:26`) and is sent as `client_id` at `ui/shared/src/commonMain/kotlin/org/apache/solr/ui/components/auth/store/OAuthStoreProvider.kt:88`.
- PKCE: met. A 128-character verifier is generated (`ui/shared/src/commonMain/kotlin/org/apache/solr/ui/components/auth/AuthUtils.kt:49`), and the challenge uses `code_challenge_method` `S256` (`OAuthStoreProvider.kt:82, 92-93`). `AuthUtilsTest.kt` covers the verifier and challenge.
- Redirects to the browser and back: met on both targets.
  - Desktop: an embedded Ktor server on `127.0.0.1:8088` takes the callback (`ui/shared/src/jvmMain/kotlin/org/apache/solr/ui/components/auth/AuthUtils.desktop.kt:24`, `ui/shared/src/jvmMain/kotlin/org/apache/solr/ui/components/auth/integration/PlatformOAuthStoreClient.kt:123`).
  - Web: a postMessage and event listener path (`ui/shared/src/wasmJsMain/kotlin/org/apache/solr/ui/components/auth/integration/PlatformOAuthStoreClient.kt:46`, `AuthUtils.wasmJs.kt:28`).
  - Deep links, which the ticket suggests, are not used. Desktop uses the local server instead.
- Token refresh: not met.
  - `ui/shared/src/commonMain/kotlin/org/apache/solr/ui/utils/HttpClientUtils.kt:107-108` has `refreshTokens { TODO("Not implemented yet") }`. The bearer client is the OAuth path's client (`HttpClientUtils.kt:71`).
  - The refresh token is stored (`ui/shared/src/commonMain/kotlin/org/apache/solr/ui/components/auth/store/OAuthStore.kt:57`, `HttpClientUtils.kt:75`) and never used.
  - No test exercises a refresh. `OAuthStoreProviderTest.kt:138-156` only checks that the refresh token is returned.
  - From the code, a 401 that triggers the Ktor refresh path would hit the TODO and throw `NotImplementedError`. I have not run this.
  - Christos's own comment on 2026-02-22 names this as missing "under this issue".
- ID tokens for user identity: not met. `ui/shared/src/commonMain/kotlin/org/apache/solr/ui/data/AuthorizationResponse.kt:35-36` parses `id_token`, and nothing reads it.
- Solr URL: `DEFAULT_SOLR_URL` is `http://127.0.0.1:8983/` (`ui/shared/src/commonMain/kotlin/org/apache/solr/ui/utils/Constants.kt:23`, and `ui/shared/src/commonMain/composeResources/values/strings.xml:71`). Christos named this as a separate issue.
- Small doc mismatch: the KDoc at `PlatformOAuthStoreClient.kt:115` says the server listens on port 3000, but the code uses 8088. Not blocking.
- Jira status: "Patch Available" since 2025-10-16, with no change since the PR merged on 2026-02-22. The ticket is stale.

(c) Open design questions:
- ID-token identity: which field of the ID token identifies the user, and where the UI shows it. Nothing on `main` displays a user identity, so this needs a design choice. Christos or a security reviewer should settle it.
- Refresh failure behavior: on refresh failure, does the UI send the user back to login, or show an error? The code does not say.

(d) Verdict: ALREADY DONE for the core flow (client ID, PKCE, redirect, merged in PR #3754). Two criteria remain open. Token refresh is the smallest slice and is bounded to `HttpClientUtils.kt`, but it changes token handling, so it needs Christos's review. Jira status change to Resolved or re-scope is an owner or author call.

## Closing: first UI contribution?

Neither ticket is a good first UI contribution for a newcomer. The remaining token refresh and ID-token identity work both touch how credentials are stored and sent, so a wrong step there carries real cost, and the reviewer should be Christos. A newcomer could start instead on the non-security items in the `new-ui-index-query-components` set, or on the small KDoc mismatch above, which is not a contribution by itself.

For the security-sensitive path, a reasonable first slice is the refresh implementation in `HttpClientUtils.kt`, after Christos confirms the refresh-failure behavior in (c).
