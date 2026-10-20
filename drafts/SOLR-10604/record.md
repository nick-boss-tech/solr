# Skip record: SOLR-10604

- Ticket: SOLR-10604, "ChaosMonkey doesn't expire session or cause connection losses by default". JIRA status read 2026-10-20T11:55:38Z (UTC): Open, resolution None, updated 2019-06-08.
- Verdict: keep-open (kept; re-read at the pinned head).
- Draft branch: solr-10604-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T11:55:38Z (UTC).

## Ticket reading

The ticket says ChaosMonkey's session-expiry and connection-loss options are read as the string `"false"` rather than null, so they are never randomized by default. The fix is small, and the reporter asks to see the effect on tests first. One 2017 comment says the tests did not yet beast fully.

## Code reading (pinned head)

- `solr/test-framework/src/java/org/apache/solr/cloud/ChaosMonkey.java:78-82`: `CONN_LOSS` and `EXP` default to `"false"`, with a NOTE that says this is intentional: "Remove the default value once we know tests pass reliably under those conditions."
- Lines 130-137: the randomized branch runs only when the value is null, so with the default string it never runs. The ticket's symptom is present by design.
- The question is therefore a test-reliability decision, not a code defect.

## History

- evidence.py at the pinned head: no commits naming SOLR-10604.

## Reproduction attempts

Not run. A reliability run would enable the two options (`solr.tests.cloud.cm.exp=` and `solr.tests.cloud.cm.connloss.enabled=`) across the SolrCloud tests with a fixed seed set; that run is the missing piece and is a multi-hour test campaign.

## Gap analysis

The default is deliberately off until the cloud tests pass under random session expiry and connection loss. Whether they do is unknown from this pass.

## Verdict justification

keep-open meets the rubric: the attempts made are the code-path reading and the NOTE; the missing piece is the reliability run under the enabled options. close-fixed and close-obsolete are not supported; close-answered is not supported since the NOTE records a deliberate choice, not a question with a doc answer.

## Limits

This record does not establish the test reliability. It does not check later ChaosMonkey changes beyond the pinned head.
