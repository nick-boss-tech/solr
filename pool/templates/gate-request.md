# Gate request: fill every field, then run pool/bin/request-gate.py on this file

Each line is `Field: value`. Replace every FILL value. Delete the Head, Title, or Gate ID line if you do not use it. The script refuses any value that still starts with FILL, and any em dash or en dash. Windows posts do not run tidy or Gradle; the Linux gate host runs tidy and any repair (WORKFLOW.md, Gate repairs).

Ticket: FILL the ticket key, for example SOLR-1234
Title: FILL a short title for the job heading (optional)
Gate ID: FILL the ticket key plus -phase-<slug> when the ticket has more than one gate (optional)
Branch: FILL the fork branch name, for example solr-1234-submit
Head: FILL the 40-character sha (optional; if given it must match the fork tip)
Base: FILL the sha the branch was cut from (upstream main or the release branch)
Module: FILL the Gradle module, for example :solr:core
Focused class: FILL the fully qualified test class the gate runs
Seed: FILL the hex seed for the focused run, for example 13059C0FFEE13059
Changelog: FILL the changelog fragment path on the branch, or none if the branch adds none
Diff scope: FILL the files touched, with insertions and deletions, against Base
Proof leg: FILL one sentence: what is reverted to base and what failure is expected, or none
Why: FILL one paragraph: what changed since the last receipt and why this gate is owed
Intended host: FILL a host name (optional; default any capable Linux host)
