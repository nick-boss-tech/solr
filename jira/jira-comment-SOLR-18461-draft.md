# Jira comment draft: SOLR-18461

Status: DRAFT (not posted). Shape: status note, evidence first. Addressee: Eric Pugh (reporter). Prepared by vm6, 2026-10-27, from `report-18461.md` (pinned main `e59ce3c97d`). Posting needs Nick Shanin's explicit go.

---

I counted the curl examples in the Ref Guide at apache/solr main {{e59ce3c97d}}:

 * 463 {{curl}} command lines.
 * 232 name the {{techproducts}} collection, 38 name {{gettingstarted}}, and 11 name {{films}}.
 * 182 name none of these. Some are v2 calls with a collection-name placeholder ([task-management.adoc:120|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/solr-ref-guide/modules/deployment-guide/pages/task-management.adoc#L120]), and some are cluster or node calls with no collection.

So {{techproducts}} is already the most common dataset. The work left is the 182 unnamed lines, each of which needs its own collection choice, and a runner to execute them, which is not in the Ref Guide tree.

Eric, should the 182 unnamed lines be converted to {{techproducts}}, or only the ones that take a collection? And is the runner in scope for this ticket?

Possible states: close after converting the collection-taking lines; keep open for the runner; or split the runner into its own ticket. The recommendation is the first, with the runner split out, since the runner is a tooling decision.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
