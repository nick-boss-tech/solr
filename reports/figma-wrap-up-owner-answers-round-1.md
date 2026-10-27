# Figma wrap-up: owner answers, round 1

- Assignment: `assignments/figma-wrap-up-round-1.md`, section 3. Recorded by the phone agent on 2026-10-26 from the owner's messages that day. Items the owner has not ruled on are marked as such; nothing below opens a PR or posts anywhere.

## Decided by the owner

- **O2, attribution line.** No "Generated with Claude Code" footer in PR bodies. The PR formula's AI header and responsibility line are the disclosure.
- **O6, batch head as gate evidence.** A combined batch run may be cited as a branch's gate evidence in its PR body, provided the body states plainly that the tests ran as part of a batch of related branches. Separate exact-head gates are preferred when time permits but are not required before opening. A failure surfacing later from the batch shape is acceptable to the owner. None of these PRs has been opened yet.

## Owner direction for today

- **O1, opening the PRs.** No UI PRs open today (2026-10-26). Future openings remain per-PR owner decisions, expected to return when Christos answers the posted asks; under the creation cap any new PR would be created as a draft.

## Closed by events

- **O3, Christos before PRs.** The staged asks went first: all ten comments posted on 2026-10-26 after Christos asked for the full set. The sequence now is to wait for his answers.
- **O4, the SOLR-17850 field-set question.** It is inside the posted comment 18132602. The older separate draft is not to be posted.
- **O9, who runs batch A.** Batch A already ran: vm6's gates plus vm4's combined check. No assignment is needed.

## Defaults standing, owner has not ruled

- **O5, splitting SOLR-17849.** The assignment default applies meanwhile: no split. Revisit if Christos answers the other asks and stalls on SOLR-17850, or asks for the header alone.
- **O7, GL-free test route.** The proposed default applies meanwhile: the pool script `material/ui-test-setup/run-ui-jvm-tests.sh` first, the vm4 Mesa route as fallback. The one confirming run waits for the next UI gate cycle or PR prep round.
- **O8, Jira actions.** Covered by the standing rule: no further Jira action on these tickets without the owner's go. The ten posted comments are the action taken.
