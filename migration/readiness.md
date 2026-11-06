# Coordination v2 switchover readiness

Status: **NOT READY TO SWITCH**. `coordination-ui` remains the sole live board. This is a preparation snapshot; no live host has been redirected and no legacy job has been marked complete by this record.

Direction: start the new board fresh. Old work remains in the preserved source branch and may be considered individually; do not automatically carry forward its assignments, claims or gates. See the [work catalog](legacy-work-catalog.md) for source links and the [fresh-board transition plan](../MIGRATION.md).

## Snapshot and preservation

The [inventory](legacy-inventory-0d91325.json) describes all 3,618 files (50,088,435 bytes of blob contents) at `coordination-ui` commit `0d91325c0c618abb14f4be29884fc681cf07886b`: 205 assignment paths, 601 claim paths, 218 gate paths, 1,043 receipt paths and 874 report paths, including nested evidence. These are file counts, not counts of active jobs or passing tests. It also records all other roots and Git modes/object IDs. Titles, reported status and mentions are extracted from 2,258 bounded Markdown files and can be incomplete or ambiguous. Source content and raw evidence are accessible at the pinned source commit; the manifest alone is not a replacement for them.

The preceding source snapshot `ed07cb930649addb4799e8e6c9de5bedc52ac406` compared with this one yields one addition, `reports/agent-skills-bundle-review-round-1.md`, and no changed or removed paths. That agrees with `git diff --name-status --no-renames` for those exact commits. This is a check of the comparison tool, not proof that later source writes are absent.

The existing six-skill package's independent review at this source commit reports blocking Python-version and validator-scope defects and says behavioral execution and Windows loading remain unverified. Do not conflate this coordination skill with that package or claim it is ready to install.

## Writer audit and risks

- The source `pool/bin/post-file.sh` at the pinned commit reads the current remote SHA and includes it in a PUT even when `--base-sha` was omitted. Thus, an existing path can be updated without the caller having reviewed its current blob. Do not assume this helper enforces create-if-absent. The Windows `research/post-coordination-ui-file.ps1` is a separate implementation; its add-only behavior does not establish the behavior of every other host.
- The source `pool/bin/board-push.sh` retries after a failed push by rebuilding on a new tip with the same paths, without checking whether those paths were created or changed meanwhile. It can therefore replace a competing path on a later attempt. It also shares `FETCH_HEAD` in its checkout. Do not equate a fast-forward push with claim exclusivity.
- The preview's `tools/legacy_publish.py` requires absent paths for adds and explicit expected blob IDs for edits and rechecks the entire batch on each retry. Its local tests do not establish remote permissions, host adoption, a canonical legacy claim name or worker liveness. It cannot safely migrate old claims into a new naming scheme automatically.
- The Windows bootstrap, `env/agents.md`, poster and other wrappers explicitly name `coordination-ui`. The phone agent reports that its own tools accept an override while still defaulting to the live board; independently validate its report on that host. The source includes host records for `windows`, `phone-agent`, `phone-agent-2`, `vm1` through `vm6`, `pool-linux` and `cue-gate-sandbox`. A host file is not proof the host is online, idle or prepared.

## Cutover blockers

1. Identify old-board work still executing and coordinate its completion or an explicit handoff before switching. The inventory intentionally does not classify claims as live or stale; there is no need to classify or import every historical item. Preserve a reliable route to the source branch and evidence.
2. For each item deliberately carried over, verify the relevant source snapshot, owner, source head and evidence, and preserve its provenance. `coordination-v2` intentionally starts without the legacy records; its structured JSON index does not index them.
3. Agree and implement a canonical claim identity and a release/takeover protocol that prevents duplicate execution; expected-blob checks on arbitrary legacy filenames are not enough. Replace or safely adapt every writer, including retry logic and shared checkouts. Verify host-specific configuration and remote service behavior in isolated targets. Do not redirect production defaults during preview.
4. Obtain readiness and quiescence evidence for every active host and in-flight execution, including offline or late writers. Establish a safe, coordinated write freeze or equivalent single-writer handover. A heartbeat timeout alone does not prove a process stopped.
5. At cutover, review late changes on the final source snapshot and reconcile any in-flight or selectively carried work, verify historical access and single-board operation, update all bootstraps and writers, and record exact heads and a rollback procedure. Until those checks complete, no host should claim work on `coordination-v2`.

This document is not a cutover instruction and is not evidence that any host has acknowledged a freeze.
