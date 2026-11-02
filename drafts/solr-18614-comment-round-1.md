# Draft: SOLR-18614 comment (Jira wiki markup) — not posted, needs Nick's approval

Target: https://issues.apache.org/jira/browse/SOLR-18614
Suggested link after posting (for whoever has rights): SOLR-18614 "duplicates" SOLR-18579.

---

This looks like the same root cause as SOLR-18579, which [PR #5214|https://github.com/apache/solr/pull/5214] addresses for container-level v2 APIs such as {{/api/node/health}}.

I reproduced the report with the released slim distributions in standalone mode on JDK 21, using BasicAuth with {{blockUnknown=false}}, the predefined {{health}} permission with {{role: null}}, and an {{all}} permission for an admin role:

||Build||/solr/admin/info/health (anonymous)||/api/node/health (anonymous)||
|10.0.0|200|200|
|10.1.0|200|401|
|10.1.0 with the path-based workaround from the description|200|200|
|PR #5214 head {{69ba9fe0e9a3}}|200|200|

With TRACE logging on {{RuleBasedAuthorizationPluginBase}}, the anonymous 10.1.0 request is checked in {{HttpSolrCall}} with no handler ("Request handler [null] is not a PermissionNameProvider, perm doesnt apply"), so {{all}} governs the request before Jersey's {{SolrRequestAuthorizer}} can match {{health}}. The endpoint moved to JAX-RS in SOLR-16458, which is in 10.1.0 but not 10.0.0. The PR's own tests ({{JaxRsPredefinedPermissionTest}}, {{JaxRsRemoteProxyAuthzTest}}) pass at that head.

Not checked: SolrCloud mode, and the PR base built from source (the classes involved are identical between the PR base and 10.1.0).

[~epugh], would it be reasonable to link this as a duplicate of SOLR-18579 and list it in the PR #5214 changelog entry?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
