# Directed assignment: phone-agent-2 Critical and closure-ready comment drafts (round 1)

Status: OPEN
Intended host: phone-agent-2 (directed by Nick Shanin, 2026-10-24)
Tags: draft, records

This file routes the drafting line of `assignments/critical-tickets-comments-round-1.md` to phone-agent-2, at the owner's direction. Other hosts: leave the Critical comment batches to phone-agent-2 while this directed assignment is open, unless she posts a release or the owner redirects. The stand-alone implementation triage (`assignments/critical-implementation-triage-round-1.md`) is not routed by this file and stays open to any host.

## Work

1. Claim Critical comment batches one at a time under the parent assignment, starting with batch 01, and deliver exactly what the parent specifies per batch: a report at `reports/critical-comments-round-1-batch-<NN>.md` with one record per ticket, and one draft per drafted ticket at `jira/jira-comment-<KEY>-draft.md`. Batch 01 is SOLR-4180, SOLR-6420, SOLR-5821, SOLR-7351, SOLR-7517, SOLR-5850, SOLR-6683, SOLR-8331, SOLR-8417, SOLR-7926. SOLR-5821 is in that batch; this host has worked it before (2026-10-23 review round), so build on that record instead of starting cold.
2. Closure-ready tickets get their drafts first within each batch. A ticket is closure-ready when its own record supports it: fixed in a shipped line but left open, overtaken by later history, a duplicate of a named original, not a bug on the ticket's own evidence, or stale after the reproduction check the record describes. Closure drafts are individual and evidence-cited, one per ticket, in the family shapes the owner has been shown; bulk text with a swapped key is not a draft.
3. Keep a running index at `reports/closure-ready-drafts-index-round-1.md`: every closure-ready ticket this line finds, Critical batches first, then harmonized backlog batches once their QA passes, with its draft path or the missing evidence that keeps it from being drafted.

## Rules

Nothing posts; every draft needs the owner's explicit go. Drafts follow `comment-formula.md` (Jira wiki markup, first person, footer disclosure). No routed review asks to Smiley. Security-sensitive tickets are flagged `security-route` in the batch report and get no public draft. If a ticket's evidence does not support closure, draft the stale-check or status text the parent assignment prescribes instead, or record the no-draft verdict with its reason code.
