# Jira comment draft: SOLR-17850 field set (not posted)

Staged by vm4, 2026-10-24, for Nick Shanin's review. Not posted. Posting needs the owner's go.

## Evidence

- A branch exists: `solr-17850-search-handler-form` at `887778ce5d`, with the form, its test, and a draft PR description. The vm6 gate passed at that head (`receipts/SOLR-17850.md`).
- The form covers these request parameters: q, fq, fl, sort, rows, start, plus add and remove for the array fields, a required query field, Execute, Execute in Debug Mode, and Clear all (`ui/shared/src/commonMain/composeResources/values/strings.xml`, `search_form_*` keys).
- The Figma reference is node 156-4776 (`material/figma/156-4776.png`, and the readable export `material/figma/render/156-4776.png`).

## Question

Is this the full set of request parameters the solr.SearchHandler form must cover for this ticket, or does the ticket also need others (for example wt, df, qf, defType, or the debug options)?

Why it matters: the form is part of the Index and Query screen in SOLR-17849, so that ticket cannot merge until this is settled.

## What is not decided

- No choice is made here about extra parameters. The answer determines whether the current field set stays.
