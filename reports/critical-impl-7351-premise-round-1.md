# SOLR-7351 premise check: MoreLikeThis with date fields (round 1, vm4)

Verdict: the premise as written does NOT reproduce. The assignment expects an empty interesting-terms list on a TrieDateField. On apache/solr main `ee2f1bf2e0109956145d7fd995da63c4091a589f`, MLT on a date field throws an exception instead: `Invalid Date String:'1577836800000'`. The defect ("MLT does not work with date fields") reproduces, but with a different symptom than the assignment states. Per the assignment, a failed premise stops the work: no implementation and no branch. The owner is asked to decide the next step (see the question below).

Assignment: `assignments/critical-impl-7351-round-1.md`, step 1. Claim: `claims/critical-impl-7351-round-1-vm4.md`. Source triage: `reports/critical-implementation-triage-round-1.md`, rank 8.

## Setup

- Host vm4, JDK 21.0.12.1, `env -u NODE_ENV`.
- Standalone shallow clone of apache/solr main at `ee2f1bf2e0109956145d7fd995da63c4091a589f`.
- Build: `:solr:packaging:assemble` through `pool/bin/sweep-run.sh`, run `s7351-assemble-ee2f1bf-20261027T121031Z`, rc 0 (94 tasks). Logs in `logs/vm4/critical-impl-7351/`.
- Packaged Solr 11.0.0-SNAPSHOT on port 8987, collection `techproducts` from `sample_techproducts_configs`. The configset has no MLT handler, so `/mlt` was registered through the Config API (`add-requesthandler`, class `solr.MoreLikeThisHandler`, mlt defaults mintf=1, mindf=1, minwl=1).
- Documents: three new ids (m1, m2, m3) with `name` text (`alpha beta gamma delta`, `alpha beta gamma epsilon`, `alpha beta zeta eta`) and one date value each. Date fields: `last_modified` (`DatePointField`, pdate) and `tdate_mlt` (`TrieDateField`, added through the schema API).

## Results (`/mlt` on m1, `mlt.interestingTerms=list`)

| `mlt.fl` | Field type | Result |
| --- | --- | --- |
| `name` | text (control) | interesting terms `alpha`, `beta`, `gamma`, `delta` |
| `last_modified` | DatePointField | error: `Invalid Date String:'1577836800000'` |
| `tdate_mlt` | TrieDateField | error: `Invalid Date String:'1577836800000'` |

The error comes from `DateMathParser` (`solr/core/src/java/org/apache/solr/util/DateMathParser.java:234`). The MLT path turns the date value into epoch milliseconds and then parses that string as a date, which fails.

## Limits

- One main tip, one configset with schema and handler added for the run, three documents. This is not a sweep.
- The ticket's own request was not available to check; the premise used the assignment's shape.
- No fix, branch, test, or gate. No PR, JIRA, or GitHub action.

## Question for the owner

Should the next step be (a) a narrow fix to the MLT date-term path (the defect as observed: an exception, not an empty list), with the assignment's stop point unchanged, or (b) closing the triage item as a premise mismatch until the ticket's exact symptom is confirmed?
