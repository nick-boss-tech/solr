# Pool assignment: Figma nodes for SOLR-17914 (former and new create collection dialog)

Status: OPEN
Tags: selenium, records
Category: none (design reference for SOLR-17914 and its PR slices)
Intended host: windows. Claim under `claims/` before starting. The Windows host holds the Figma login; Linux hosts do not need to take this.
Posted by: vm4, 2026-10-27, at the direction of Nick Shanin.

## Why

SOLR-17914 cites Figma node `2119-22708` for the create collection dialog. That node is not in `material/figma/manifest.json`, and `reports/new-ui-figma-reconciliation-round-1.md` records "none cited" for SOLR-17914 in error. Christos Malliaridis then posted a newer, still work-in-progress design, node `2635-40394`, in Jira comment 18133018 (2026-10-27), and asked that it be used once complete. The PR slices for SOLR-17914 (advanced options, the Collections action, and the create request) cannot be scoped without both nodes.

Figma file: "Apache Solr Admin UI v2 Concept", file key `VdbEfcWQ8mirFNquBzbPk2`.

- Former design (cited by SOLR-17914): https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=2119-22708&t=yxFihPpG3r7FXACm-1
- New work-in-progress design (Christos, comment 18133018): https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=2635-40394&t=dPHtvnuoxAz2CBt9-4

## Items

1. **Read the former node `2119-22708`.** Record the frame name, the visible text (title, field labels, placeholders, button labels, error text, dropdown values), the controls, and whether the advanced options are shown. For advanced options, record each label exactly as displayed and whether they are shown by default or behind a toggle. Output: a section in the deliverable.

2. **Read the new node `2635-40394`.** Same fields as item 1. Also record whether it is marked work in progress, whether any frame is empty or placeholder, and every difference from the former node in the same list. Output: a section in the deliverable.

3. **Where the create action lives.** Open the Collections section node in the same file (the ticket SOLR-17912 cites node `87-12962`; the index in `reports/figma-node-index-round-1.md` records `87-12957` as not found). Record whether a create action is drawn there, where it sits, and which dialog it opens (former or new). If the Collections section is not drawn, say so. Output: a section in the deliverable.

4. **Error states.** From the two dialog nodes, record whether there is a drawn state for: the name-exists error, a failed create, and a general error. Record the exact text. If a state is not drawn, say so and do not infer it.

5. **Screenshots.** One viewport PNG per node under `material/figma/<node-id>.png` (for example `material/figma/2119-22708.png` and `material/figma/2635-40394.png`). Also save a readable export if Figma offers one, under `material/figma/render/<node-id>.png`, following the earlier round. Keep each under 1 MB. Add the new files to `material/figma/manifest.json` in the same push as the DONE line.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Figma login | default: Nick signs in on the Windows host; the host never types or stores a password or token | if the file asks for sign-in and Nick is not present, stop and record "blocked: sign-in" | Nick |
| Figma API token or MCP | decided: none. No token is created or stored in `.env`, and the Figma MCP server is not requested | none | Nick |
| Node not visible to the account | default: record "not accessible" for that node | do not request access; leave a note for Nick to ask Christos Malliaridis to share the file | Nick |
| Work-in-progress design | default: record it as WIP, do not treat its layout as final | none | Nick |
| Edits to the Figma file | decided: no edits, no comments, no sharing changes | view only | Nick |

No row may leave the choice to the claimant.

## Deliverable

`reports/figma-christos-17914-nodes-round-1.md`: one section per item, with its status, the node facts above, the screenshot paths under `material/figma/`, and any node marked "not found" or "not accessible". Add the deliverable, the screenshots, and the manifest entries in the same push as the DONE line.

## Limits

- Read only. No edits, comments, or sharing changes in Figma, and no Jira or GitHub comments.
- No secrets in any file. Do not write the Figma login, a token, or any cookie.
- Workers add files. Do not edit this assignment; the claim file records the state.
- Commit as Nick Shanin with no trailers. No em dashes in the authored text.
