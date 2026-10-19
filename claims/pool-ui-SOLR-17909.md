# Claim: pool-ui-SOLR-17909

- Claimant host: vm2, matching a file in `hosts/`.
- Status: ACTIVE. Breadcrumb component under `assignments/pool-ui-tickets-round-1.md`.
- Job or assignment: `assignments/pool-ui-tickets-round-1.md`.
- Slice: SOLR-17909, a reusable breadcrumb component. The collections use is not in this claim.
- Capability tags used: ui, jvm-tests.

## Rewritten criteria

The JIRA criteria say the breadcrumb mirrors the Decompose childstack. The baseline removed Decompose, so the component takes its items from the caller instead. Criteria used here:

- The caller supplies an ordered list of labels. The last label is the current location.
- Each earlier label is clickable. A click reports its index through `onItemClick`.
- The current (last) label is shown but not clickable.
- The component has no dependency on any navigation library.

The JIRA ticket keeps its original text. Ask Nick to confirm these rewritten criteria before merge.

- Blast radius: not flagged. Layout and text only.
- Started: 2026-10-19T18:12:00Z (UTC).

Heartbeat: 2026-10-19T18:12:00Z
