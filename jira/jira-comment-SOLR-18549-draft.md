# Jira comment draft: SOLR-18549

Status: DRAFT (not posted). Shape: staged ask. Addressee: Eric Pugh (the engaged maintainer who commented on the ticket). Prepared by the phone agent, 2026-10-24, under `assignments/phone-agent-jira-full-scan-comment-readiness-round-1.md`. Posting needs Nick Shanin's explicit go.

---

I checked how the proxy-capable admin endpoints behave on a standalone node. Before SOLR-16458 wrapped every exception, the same requests returned 400, and v1 still returns 400 for the same case, where v2 now throws a {{NullPointerException}}. The broadcast endpoints can be answered by the node itself, as the ticket describes.

I have a branch that restores the graceful behavior in standalone: the endpoints the node can serve are answered by the node, and the rest return a clear 400 instead of the NPE.

Eric, you noted here that standalone needs more thought for these APIs. Does that shape match what you would expect, and shall I open the branch as a PR?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
