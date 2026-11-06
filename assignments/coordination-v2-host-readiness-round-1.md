# Coordination v2: host readiness for a fresh-board cutover, round 1

Status: OPEN

Intended host: each active coordination pool host, each taking only its own host slice. Capability tags: review, records; no Gradle, browser, BATS or other build capability required.

## Owner direction and current state

Nick authorized preparation and a switch to the fresh `coordination-v2` board. The target preview is `nick-boss-tech/solr:coordination-v2` at `183ebf63db0d78b511651b1b5679bd4ccd58faee`. The old board remains `coordination-ui` until a separate verified cutover record says otherwise. This assignment is preparation, not the cutover; do not redirect a production writer or claim real work on v2 yet.

The fresh queue does not bulk-import old assignments or claims. Old work can be considered one item at a time, retaining the source head and evidence. A release record is not proof that an executor stopped. Do not stop, duplicate, discard, or silently reassign any in-flight execution.

## Per-host slice

Before work, refresh the live old-board snapshot and add a host-specific claim, `claims/coordination-v2-readiness-<host>-round-1.md`, naming this assignment, host, capability tags, exact preview head and start time. Do not claim another host's slice. Check for an existing claim or report for the same host first. The first valid claim for each host slice governs.

1. Inventory this host's actual board readers, write scripts, bootstraps, active skills and scheduled jobs. Identify each default board branch and any cached checkout or `FETCH_HEAD` usage. Identify unpushed local board commits and any running gate or other executor; do not print or copy credentials.
2. Review the preview's `START-HERE.md`, `WORKFLOW.md`, `tools/board.py`, and `tools/README.md`. Record whether this host has the required Git, Python, credentials, identity and execution environment. Test the v2 writer and applicable wrapper against a new isolated local bare repository or other explicitly isolated test target, never the live v2 queue. Check concurrent same-job claim attempts and release/successor behavior; report commands, results and limitations. Do not use Gradle.
3. Identify changes needed for this host to select the new live branch and use the canonical structured claim path, including how old jobs, existing processes and late writes will be handled. Do not activate changed defaults until a coordinated cutover. If the host cannot safely stop or observe a running process, record the uncertainty instead of declaring it idle.
4. Add `reports/coordination-v2-readiness-<host>-round-1.md` to the old board with exact observed source and preview heads, active or unknown work, old/new writer inventory, test evidence, remaining changes, and an explicit READY / NOT READY verdict for this host only. Link the claim. A ready report does not switch the board or authorize a gate takeover. An offline host needs a later acknowledgement or a separately verified fencing plan, not an assumed pass.

Windows has a prepared v2 PowerShell wrapper, tested against an isolated local repository. The phone-agent reported a branch override in `docs/phone-agent-workflow-fixes.md`; verify actual current behavior on that host rather than treating the historical report as a cutover acknowledgement. Hosts `vm1` and `phone-agent` are the same historical host; do not create duplicate slices for that alias. Generic pool host records describe capabilities, not necessarily distinct running hosts.

## Restrictions

No production defaults change, no old-board freeze, no public PR/Jira communication and no broad process termination are authorized by this preparatory assignment. Continue using `coordination-ui` for existing work. Any eventual cutover needs fresh source and target heads, resolved in-flight work, a single-active-board handover, verification and rollback plan.
