# Jira comment draft: SOLR-18405

Status: DRAFT (not posted). Shape: status note, evidence first. Addressee: Eric Pugh (reporter). Prepared by vm6, 2026-10-27, from `report-18405.md` (pinned main `e59ce3c97d`). Posting needs Nick Shanin's explicit go.

---

I measured the debug and trace calls in {{solr/core/src/java}} at apache/solr main {{e59ce3c97d}}, to see how many would benefit from the lazy form:

 * 760 {{log.trace}} and {{log.debug}} calls, 14 of them commented out.
 * 253 are guarded by {{isTraceEnabled()}} or {{isDebugEnabled()}} within three lines.
 * 507 are unguarded. Most pass identifiers, fields or literals, which are cheap to evaluate.
 * The regex finds seven unguarded calls with method-call or concatenation arguments. Four of those are commented out or pass literals. {{PeerSync.java:684}} and {{TransactionLog.java:733}} pass cheap accessors. {{PeerSyncWithLeader.java:162}} calls {{msg()}}, but it sits behind a local {{debug}} flag.

So I did not find a hot-path call where the argument is clearly expensive. A regex cannot separate cheap accessors from expensive work, so this is a measurement, not an audit.

Eric, your comment says the lambda has a cost and should be limited to expensive calls. Given that none stands out here, should this ticket be closed, or do you want the rule (flagging new unguarded method calls at debug or trace) pursued as its own ticket?

Possible states: close with no code change; keep open for a targeted read of the hot-path searcher and update-processor calls; or split the rule into its own ticket. The recommendation is the first: the evidence does not show a regression to fix.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
