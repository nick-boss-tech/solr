# Assignment: open the 28 update-processing PRs as drafts

Owner approval: given in the main chat on 2026-10-09. All 29 update branches are gated at the heads their PRs open from; receipts are in `receipts/`. SOLR-18505 is already out and is not part of this assignment.

## What to do

Open one draft pull request per ticket below, in this order (the order is the preferred merge order; all 28 open in this pass):

3657, 4841, 5065, 5505, 5754, 5887, 5939, 5941, 6045, 6065, 6973, 7022, 7504, 11475, 11483, 12245, 12703, 12864, 13265, 13696, 13943, 14262, 14718, 16356, 16655, 16673, 16910, 12705

For each ticket:

1. Read `receipts/SOLR-<ticket>.md` and note the gated head.
2. Check the live tip of `solr-<ticket>-submit` on the fork. If it does not equal the gated head, skip the ticket and report the mismatch; do not open from an ungated head.
3. Check whether a pull request already exists for that branch against apache/solr. If one does, skip and report its number.
4. Open the PR: repository apache/solr, base `main`, head `nick-boss-tech:solr-<ticket>-submit`, draft mode on. Title and body come from `pr-drafts/update-processing/SOLR-<ticket>.md` exactly as written (the drafts were brought up to the executed heads in c2a7e84d923 and already carry the AI header and footer). Do not edit the text, shorten it, or add anything.

## Rules

- Drafts stay drafts. Do not mark any of them ready, do not post comments, and do not change any description after opening.
- Do not edit, commit to, or push any `solr-*-submit` branch in this assignment.
- If the GitHub API refuses a creation (rate limit, validation error), stop after three consecutive refusals and report where you stopped; the main side will resume from there.

## Report

Write `reports/open-update-29-prs.md` with one line per ticket: the ticket, the PR number and URL, or SKIPPED with the reason (tip mismatch with both heads named, or the existing PR's number). Commit and push the report on pr-prepare when the pass is done or when you stop early.

## Amendment (main side, 2026-10-09, after the preflight report)

- The four receipts the preflight skipped (4841, 5754, 7022, 16673) are refreshed: each now names the live tip as its head and records the 2026-10-09 top-up verification (delta read file by file; 7022 also has an Error Prone compile and its focused test at the tip). All 28 tickets now pass the receipt rule.
- Titles: the drafts carry no title field. Use as the PR title: `SOLR-<ticket>: <title>` where `<title>` is the title line of the changelog fragment the branch adds under `changelog/unreleased/` on `solr-<ticket>-submit`. For SOLR-16673, its draft's labelled Title line is the same text; use it.
- Authorization: the owner approved these openings in the main chat on 2026-10-09 and will confirm in this session as well; treat the owner's word in this session as the go-ahead this amendment cannot supply on its own.
