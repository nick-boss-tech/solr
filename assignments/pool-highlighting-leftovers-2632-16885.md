# Pool assignment: Highlighting leftovers, SOLR-2632 pin draft and SOLR-16885 Jira comment text

Capability tags: `draft`. Staffing: up to 2. Claim path: claims/pool-highlighting-leftovers-2632-16885.md.

Background: both tickets were NO GATE dispositions held for the owner during Highlighting round 1. The owner has since set a decision default (2026-10-10): a decision defaults to the recorded recommendation, or to the harder, more thorough option where no single recommendation is on record. Applied here: SOLR-2632 gets the test-only pin draft (option (a) of the recorded dispositions), and SOLR-16885 gets the Jira comment text with the full premise evidence (the presented disposition). Openings and Jira posts themselves stay with the main side and the owner.

Deliverable 1: pr-drafts/highlighting/SOLR-2632.md, a complete PR draft in the pr-formula.md shape for a test-only pin: the premise did not reproduce on current main (bytecode-verified on Lucene 10.4.0 and 9.12.3, per receipts/SOLR-2632.md), the branch at 1d7018f3fd7b pins the current behavior with a test, and the text says plainly that no defect is claimed. Title from the branch changelog fragment, verified for accuracy. If a Lucene version is mentioned, mention the others (main and branch_10x pin 10.4.0; branch_9x pins 9.12.3).

Deliverable 2: material/SOLR-16885-jira-comment.md, the Jira comment text in Jira wiki markup (code spans {{...}}, links [label|url], bullets " * ", the AI header in its Jira form: the robot emoji, _AI text below_, _(posted on behalf of Nick Shanin)_), stating what was checked, that the premise is not grounded on current code (per receipts/SOLR-16885.md and the round 1 report), and that no change is proposed. The owner posts it; nothing is posted under this assignment.

Rules: WORKFLOW.md binds (claim before work, mark the claim DONE in the same push as the deliverables). No builds or test runs. No em dashes in authored text.
