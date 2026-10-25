# Comment family examples, round 2 (2026-10-24, spot-checked live)

Three more family examples, each sampled and spot-checked against live Jira on 2026-10-24 (status, fix version, last update, attachments, issue links read at sampling time). Like round 1, these are TEXT EXAMPLES for the owner's verdict on family shape; production texts get the full per-ticket verification in the housekeeping pipeline before any posting decision, and nothing here is approved to post.

## Example A: EOL ping, Bug type (SOLR-6534)

Spot check: Bug, Open, fixVersion 5.0, last updated 2014-09-19, no attachments, no issue links. Summary: "Multipolygon query problem with datelineRule=ccwRect". The Bug variant of the batch-1 ping: for a bug the question is reproduction on a supported release, not whether a feature is still wanted.

> Housekeeping check on an old ticket. This bug was filed against the 5.0 line, which has been end-of-life for years, and the ticket has not been updated since September 2014. The cleanup discussion on the dev list is about closing tickets that no longer apply so the open list reflects real work.
>
> Does this multipolygon dateline problem still reproduce on a supported Solr release? If it does, a note here with the version keeps the ticket alive and gives it a current target. If nobody is hitting it anymore, closing it as no longer applicable seems right, and it can always be reopened with a current reproduction.
>
> 🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

## Example B: closure-obsolete, Task type (SOLR-1760)

Spot check: Task, Open, no fix version, last updated 2012-06-09, no attachments, no issue links. Summary: "convert synonymsfilter to new tokenstream API". The production version of this text requires the code-history check the harmonization step specifies (that the conversion the task asked for has long since landed and the API era it names has itself passed); the example assumes that check passes and says what it relies on.

> Proposing this one for closure as obsolete. The task asked for the synonyms filter to be converted to the then-new tokenstream API; that conversion landed long ago and the codebase has moved through several API generations since. The ticket has been untouched since June 2012, with no fix version and nothing attached.
>
> If anyone sees remaining work here that is not already covered elsewhere, a pointer to it keeps this open. Otherwise it reads as completed by history rather than by a commit on this ticket.
>
> 🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

## Example C: ping-stale, Task type (SOLR-3727)

Spot check: Task, Open, no fix version, last updated 2012-08-13, no attachments, no issue links. Summary: "Re-enable tests inheriting from AbstractFullDistribZkTestBase".

> Housekeeping check on an old ticket. This task, re-enabling the tests that inherit from AbstractFullDistribZkTestBase, has been open since August 2012 with no fix version set, and the test infrastructure has been reworked heavily since then.
>
> Is the re-enablement still wanted in some current form? If the affected tests have since been re-enabled, replaced, or removed, closing this as overtaken by events seems right. A pointer to any test still disabled for the reason this ticket describes would be enough to keep it.
>
> 🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

## Sampling note

A closure-fixed Improvement example was attempted and the sampler's first hit (SOLR-13454) was excluded on sight: its status is Reopened and it carries an issue link, both hard exclusions under the pipeline criteria. The criteria doing that filtering at sampling time is the system working; a clean example for that family will come from the harmonized store rather than ad hoc sampling.
