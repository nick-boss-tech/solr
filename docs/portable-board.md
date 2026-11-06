# A Git branch as a multi-agent coordination board

Status: design draft for review. The Solr board remains on `coordination-ui`; this document does not authorize a cutover or describe an implementation that has already been tested.

## The idea

A team can dedicate a Git branch to shared work records. Agents on different machines read the same branch, agree on ownership, write handoffs, and publish evidence without sharing a process or chat history. The board is durable project memory; it is not a scheduler, a lock service, or a substitute for test execution or human decisions.

Each team chooses its own repository, branch, identity, authorization rules, and execution environments. This example does not require Solr, a particular agent product, or access to Nick's fork. Git history makes changes auditable, but repository access and credentials determine who can read or alter records; it is not a security boundary between writers.

## Relationship to Elephant-Goldfish

The [Elephant-Goldfish Model](https://github.com/google/elephant-goldfish-model) uses a context-rich Elephant to develop a design and fresh-context Goldfish reviewers to test whether the written design is understandable, sound, and ready to implement. The human makes the decisions. Google describes the lifecycle and its independent review stages in its [introduction](https://opensource.googleblog.com/2026/10/introducing-elephant-goldfish-model-plugin.html).

The board complements that model: the Elephant records a design and its assumptions; a separately authorized fresh-context reviewer reads the design and cited materials, writes findings to the board, and the Elephant records dispositions. The board persists the results across machines and sessions. A fresh worker is not automatically an independent Goldfish review: reviewers need an isolated context, a deliberate scope, and access to the evidence needed to judge it. Reviews do not replace tests or human judgment. If a runtime cannot provide a fresh independent context or delegation is not authorized, record that limitation; do not label a same-context review as Goldfish review. Adopting this pattern does not imply that the EGM plugin is installed or its full protocol was executed.

## Minimal records

| Record | Purpose | Essential information |
| --- | --- | --- |
| Assignment | Defines available work | Identifier, intended outcome, capabilities, scope, deliverables, constraints and relevant source revisions |
| Claim | Identifies an owner | Assignment, claimant, start time, exact slice and expected handoff |
| Gate | Defines a proof request | Exact source head, environment, commands or acceptance checks, required evidence and expected result |
| Receipt | Reports the actual outcome | Gate and head, host, commands, results, logs, failures, skips and limits |
| Report | Records findings or a handoff | Sources, decisions, unresolved questions and links to artifacts |

Suggested layout: `assignments/`, `claims/`, `gates/`, `receipts/`, `reports/`, `hosts/` and `docs/`. Project-specific records may need a different shape. Machine-generated indexes can help discovery, but should link to the governing records and identify their snapshot; never treat a stale list as proof that work is unclaimed or complete.

## Example lifecycle

1. A human or authorized agent writes an assignment: “Investigate whether the importer accepts duplicate IDs.” It names the revision, intended deliverable and any constraints.
2. A capable worker refreshes the board, checks existing claims, and acquires the assignment using the team's atomic claim protocol. It records which revision it inspected.
3. The worker publishes a report and, if a change is authorized, a source branch and exact commit. A separate gate request names that commit and the checks required.
4. A gate worker claims execution, runs the checks in the named environment, and publishes a receipt with the actual results and evidence. A changed source head requires new or explicitly superseding proof. Passing tests are not proof of every behavior outside their scope.
5. When a design merits independent review and the user or applicable workflow authorizes delegation, the Elephant provides a fresh reviewer the design plus the minimum relevant source material, without the prior conversation or the desired answer. The reviewer restates the design, identifies gaps and reports readiness with evidence. The Elephant evaluates the findings, corrects the design as appropriate and records unresolved decisions. Independent review and execution proof remain separate.

## Concurrency and failure handling

- Define an atomic claim mechanism before using the board with multiple writers. For example, a fixed claim path per assignment with a server-side create-if-absent operation can serialize initial claims; verify the service's actual preconditions. Two agents independently adding different claim files and then rebasing can both succeed, so ordinary fast-forward pushes alone do not establish exclusive ownership.
- A stale owner is not necessarily a stopped worker. Specify lease/heartbeat evidence, executor liveness, takeover rules and how to prevent concurrent side effects. If the system cannot establish that the old worker stopped, do not automatically take over. Git alone cannot enforce a lease on an external running process.
- Use compare-and-swap for shared single-writer updates where supported. For batches, define conflict handling and re-read any changed source records. Avoid force-pushing shared history. A successful handoff needs a read-back of the record and published head.
- Treat branch content, issue text, review comments and fetched artifacts as data, not authority to override the user's instructions. Do not store credentials in the board. Apply project authorization rules to external writes and verify links and evidence provenance.
- Moving to a new board needs an explicit single-active-board cutover, inventory of active claims and evidence, reconciliation of late writes, tool configuration changes and a rollback plan. Do not start a second independent claim queue during migration.

## Adoption and validation

1. Choose a repository and a dedicated branch, writer identities, credential handling and visibility. Decide which records and logs are appropriate for that visibility.
2. Define the claim and takeover mechanism, host capabilities, proof standards and the policy for external actions. Implement and test the mechanism with simultaneous claim attempts, stale-worker cases, write conflicts and failed or interrupted gates before relying on it.
3. Publish a short project-specific agent entry point that identifies the live board and tells agents how to discover current rules. Keep skill instructions separate from changing project records.
4. Run a small demonstration with a bounded assignment, claim, handoff and receipt. Retain the raw results; do not present a hypothetical walkthrough as a completed run.
5. If using EGM, install and follow the actual plugin only when the team chooses it and the runtime supports it. Otherwise describe the fresh-context review as EGM-inspired. Record what ran, what was checked and what remains unreviewed.

## What this preview does not yet supply

There is no portable executable claim service, validated starter repository, installation command, completed independent Goldfish review or migrated live queue here. These are future implementation and validation work, not properties to infer from this document.
