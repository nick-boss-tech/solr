# Comment draft: SOLR-4180

- Status: DRAFT (not posted). Posting needs the owner's go.
- Shape: scoping note (per comment-formula.md)
- Addressee: [~janhoy] — recent activity on Solr metrics handling; ticket is unassigned and its reporter is inactive.

[~janhoy], I checked the current metrics guide: Solr already reports node- and core-level request-handler counts and timing through the metrics API, but I could not verify that this covers the server-level statistics requested here from Jetty's `StatisticsHandler` ([current metrics guide|https://solr.apache.org/guide/solr/latest/deployment-guide/metrics-reporting.html]). Does this Wish still need a distinct Jetty-server view, and which specific measurements or consumer are missing from the current metrics API?

 * Keep it open as a scoped feature request if specific server-level measures are still required.
 * Close or supersede it if the current metrics surface already meets the multi-core use case; please identify the relevant metrics.

I recommend clarifying the missing measures before any implementation is proposed; I have not assumed that handler counts and timing are equivalent to Jetty's full server statistics.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
