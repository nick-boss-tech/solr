# Family: ping-stale

Status: DRAFT ONLY. Prepared from rev4 records of `material/harmonized-backlog/batches/h1-NNN-rev4.json`. H1 QA by a second host has not been published, so no text here is approved for use. Nothing is posted to Jira.

Eligibility (assignment rules): bugs are barred from the four closure families; Critical and Blocker are barred from all families; a key with no metadata row is not eligible.

## Eligible now: 11 tickets

Keys: SOLR-6678, SOLR-7947, SOLR-9492, SOLR-10706, SOLR-11776, SOLR-13853, SOLR-15571, SOLR-16062, SOLR-16792, SOLR-17376, SOLR-17747. Full records: rev4 batches, `family_eligible` = `ping-stale`. Each record cites its skip-queue entry (`queue-current.json`). The last-activity date comes from the metadata CSV `Updated` column (`material/jira-issues-csv/solr-jira-metadata-2026-10-18-1792393324869.csv`), which is the store source for dates.

## Template (Jira wiki markup, first person)

```
[~{REPORTER}] I am checking in on this ticket. Its last activity was on {LAST_ACTIVITY_DATE}. A review of the ticket text found no steps that could be tested, so it has stayed unconfirmed. {KEEP_OPEN_RESPONSE}

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
```

## Placeholders and fill rule

- `REPORTER`: Jira reporter field from the metadata CSV `Reporter` (or the Jira reporter if the CSV lacks it). Do not guess.
- `LAST_ACTIVITY_DATE`: metadata CSV `Updated`, date part only.
- `KEEP_OPEN_RESPONSE`: fixed text, "If it still happens on a supported Solr release, a reply with the version and steps will keep it open."
- A ticket is not fillable if its record lacks the skip-queue citation or the metadata row. Such tickets go back as conflicts.

## Worked example (SOLR-6678)

Record: `material/harmonized-backlog/batches/h1-001-rev4.json`, key SOLR-6678, disposition `ping-stale`, skip category "stale or no reproduction", skip citation `queue-current.json` key SOLR-6678 (audit-3, 2026-10-05, "no repro or too vague to test"). Metadata: Bug, Major, Updated 2020-10-16, summary "Collection/core reload is causing a memory leak".

```
[~{REPORTER}] I am checking in on this ticket. Its last activity was on 2020-10-16. A review of the ticket text found no steps that could be tested, so it has stayed unconfirmed. If it still happens on a supported Solr release, a reply with the version and steps will keep it open.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
```

`REPORTER` is filled from the Jira reporter, which I did not look up for this example, so it stays a placeholder here.

## Scope note

Eligible list above is complete for rev4. Re-run the rule if rev5 changes any record.
