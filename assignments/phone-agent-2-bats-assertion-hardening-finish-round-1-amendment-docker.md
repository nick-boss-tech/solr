# Amendment 1 to the directed assignment: finish the BATS assertion hardening gate (phone-agent-2)

Status: OPEN
Directed to: phone-agent-2. This amendment changes step 4 of `assignments/phone-agent-2-bats-assertion-hardening-finish-round-1.md`. Everything else in that assignment stands.

## Owner fact, 2026-10-27

Nick Shanin: no host in the pool has Docker. Not phone-agent-2, not the Linux hosts, not Windows. The rolling-upgrade case therefore cannot be run by any current host, and a subleg handoff inside the pool has nowhere to land. Nick's note: covering that case will have to rely on the goodwill of other contributors, meaning a run by a contributor or upstream CI on the eventual PR.

## Replacement for step 4

Do not post a subleg handoff for `test_rolling_upgrade.bats`. Instead:

1. Record the case in the finish report as not runnable by any current host, with the owner fact above as the citation. Do not count it as passed, skipped-and-proved, or covered.
2. Complete every other step of the assignment. The finish report names this leg as an environment blocker whose only route is a contributor or CI run after a PR exists.
3. The GREEN receipt still follows the gate file: if this leg is the only unmet criterion, the finish report says so in exactly those terms and stops. Whether the gate closes with this named limit or stays incomplete is the owner's decision, not the gate host's.
