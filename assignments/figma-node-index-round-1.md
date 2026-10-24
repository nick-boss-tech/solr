# Pool assignment: Figma node index for the new UI tickets

Status: OPEN

- Capability tags: selenium, records.
- Category: none (design reference for the Index & Query, collections, configsets, and component tickets).
- Intended host: windows. Claim under `claims/` before starting (WORKFLOW.md). The Windows host holds the Figma login; Linux hosts do not need to take this.
- Posted by: vm4, 2026-10-24, at the direction of Nick Shanin.

Goal: read the Figma design nodes that the new UI Jira tickets cite, and write what each node shows, so the tickets can be claimed without guessing the layout. Today the reports in `reports/new-ui-*.md` mark every Figma-only requirement "not verified".

Figma file: "Apache Solr Admin UI v2 Concept", file key `VdbEfcWQ8mirFNquBzbPk2`. Open each node by its id, for example `https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=156-4776`.

## Items

1. **Read each cited node.** For every node in the table below, record the frame name, the visible text (titles, placeholders, button labels, column headers, dropdown options), the controls (fields, tables, tabs, lists, trees, breadcrumbs), and whether the node matches the ticket text. Start from the table. Output: section per node in the deliverable.

   | Node | Ticket(s) that cite it | Note |
   | --- | --- | --- |
   | 23-5124 | SOLR-17848 | Index & Query umbrella |
   | 137-12066 | SOLR-17849, SOLR-17850, SOLR-18493 | header dropdowns; also the Index & Query page |
   | 156-4776 | SOLR-17850 | SearchHandler form. The ticket has no field list; this node is the only source |
   | 14-3248 | SOLR-17886 | Configsets scene; collections umbrella |
   | 13-3972, 13-3977 | SOLR-17887 | file tree |
   | 227-7661 | SOLR-17887, SOLR-17896, SOLR-17911 | navigation tabs; file viewer |
   | 227-9235 | SOLR-17887 | file tree |
   | 87-12957 | SOLR-17896 | navigation tabs (closed) |
   | 1997-7453 | SOLR-17909 | breadcrumb |
   | 319-20114 | SOLR-17911 | file viewer |

   If a node id does not open, record "not found" with the URL you tried. Do not guess a nearby node.

2. **Read the table-of-contents frame.** Record the page names in the order the frame lists them, and any page that has no node in the table above: Start (sign-in page), Dashboard, Metrics, Cluster, Security, Configsets, Collections, Queries and Operations, Environment, Logging, Thread Dump. Record whether the frame calls the Index & Query area "Queries and Operations" (the Jira umbrella says "Index & Query"). Output: a section in the deliverable.

3. **Spot-check the design tokens.** The frame uses these values (copied from the design's CSS by Nick): page background `#FFF8F6`, text `#271814`, surface `#FDF8F8` with a 5% `rgba(181, 39, 3, 0.05)` overlay, headline Raleway 24px/32px, title Roboto 22px/28px, body Roboto 14px/20px with 0.25px letter spacing, card radius 16px and page radius 24px. Confirm or correct each value in the Figma inspector for two nodes of your choice, and record the result. Do not copy the whole CSS. Output: a short section in the deliverable.

4. **Save screenshots.** One PNG per node, under `material/figma/<node-id>.png` (for example `material/figma/156-4776.png`). Keep each under 1 MB. If a PNG cannot be saved, say so in the deliverable.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Figma login | default: Nick signs in on the Windows host; the host never types or stores a password or token | if the file asks for sign-in and Nick is not present, stop and record "blocked: sign-in" | Nick |
| Figma API token or MCP | decided: none. No token is created or stored in `.env`, and the Figma MCP server is not requested | none | Nick |
| Node not visible to the account | default: record "not accessible" for that node | do not request access; leave a note for Nick to ask Christos Malliaridis or Eric Pugh to share the file | Nick |
| Edits to the Figma file | decided: no edits, no comments, no sharing changes | view only | Nick |
| Naming of the Index & Query area | default: record both names and decide nothing | none | Nick |

No row may leave the choice to the claimant.

## Deliverable

`reports/figma-node-index-round-1.md`: one section per item, with its status, the node and page facts above, the screenshot paths under `material/figma/`, and any node marked "not found" or "not accessible". Add the deliverable and the screenshots in the same push as the DONE line.

## Limits

- Read only. No edits, comments, or sharing changes in Figma, and no Jira or GitHub comments.
- No secrets in any file. Do not write the Figma login, a token, or any cookie.
- Workers add files. Do not edit this assignment; the claim file records the state.
- Commit as Nick Shanin with no trailers. No em dashes in the authored text.
