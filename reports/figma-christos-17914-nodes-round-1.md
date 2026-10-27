# Figma: SOLR-17914 former and WIP create collection dialogs, round 1

Status: COMPLETE WITH DESIGN GAPS RECORDED

- Assignment: `assignments/figma-christos-17914-nodes-round-1.md`, introduced by `f6e1a4faf28f4ce0e8bee278de6855a022816948`.
- Claim: `claims/figma-christos-17914-nodes-round-1-windows.md`, published at `3eea9679b6a1081e6ae0b1c96f39a9ecb0dc085b`.
- Host: Windows Codex desktop. Inspected 2026-10-27, approximately 22:06-22:14 UTC. Existing view access worked; no new sign-in was needed.
- Initial board snapshot: `3c8dcb62969840122164ae28ffe051598c88d2ad`. Closeout prepared from `3eea9679b6a1081e6ae0b1c96f39a9ecb0dc085b`.
- File: [Apache Solr Admin UI v2 Concept](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept).

The earlier `reports/new-ui-figma-reconciliation-round-1.md` row saying SOLR-17914 cited no Figma node is incorrect. The ticket cites `2119-22708`; Christos's newer reference is `2635-40394`. Both are accessible and now captured. The old report remains unchanged under the add-only rule; this report supplies the correction.

## Item 1: former node 2119-22708

Status: READ. [Exact reference](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=2119-22708). Page: **Collections**. Inspector name: **Create Collection Dialog**, a component set, 951 by 1,521 px. The layer tree exposes exactly three variants: **collapsed**, **expanded**, and **WithPreview**.

This reference is a live component set, not a frozen picture of the former design. It now includes the new WithPreview variant as its third, bottom example. The first two examples are the former layouts. Do not count the third as an additional former layout or conclude that the two requested references are independent component sets.

| Former layout | Exact visible labels and values |
| --- | --- |
| collapsed | Heading **Create Collection**. **Collection Name**, blank text field with no placeholder. **Configset**, closed dropdown showing `_default`. **Show More Options**, down chevron. Footer **Cancel**, **Create Collection**. |
| expanded | Same heading, name, configset, and footer. **Shards Count** `1`; **Replication Factor** `1`; **Hide Less Options**, up chevron, exactly that wording; **Router Type**, closed dropdown showing **Composite ID**; **Router Field**, blank text field with no placeholder. |

Only name and configset are drawn in the collapsed variant. Shards Count, Replication Factor, Router Type, and Router Field are drawn in the expanded variant. The toggle sits between replication inputs and router inputs in that expanded drawing. This establishes the two drawn states, not an executed runtime toggle contract. The drawings contain no tooltip content, open dropdown menus, name-exists text, failed-create message, or general-error message.

Evidence: `material/figma/2119-22708.png` (viewport) and `material/figma/render/2119-22708.png` (complete native component-set export).

## Item 2: new node 2635-40394

Status: READ, WIP. [Exact reference](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=2635-40394). Page: **Collections**. Inspector: variant of **Create Collection Dialog**, property **Variant: WithPreview**, 919 by 673 px. It is the bottom variant of the component set above.

The frame is populated, not empty or a placeholder. There is no visible WIP badge or WIP text inside this node. WIP status comes from Christos's Jira comment 18133018 as recorded in the assignment, not from a badge in the drawing. Treat the layout as provisional.

| Area | Exact text and drawn control/value |
| --- | --- |
| Title | **Create Collection** |
| Name | **Name**, blank text field without a placeholder. Helper: `Letters, digits, _ and -`. |
| Configset | **Configset**, closed dropdown `_default (used by 3)`. Link **Preview configset**. |
| Size and Redundancy | Section label **Size and Redundancy**. **Shards**, minus/value/plus stepper showing `2`. Helper: **Split the index to large data over nodes.** |
| Replica counts | **Copies of each shard**. **NRT** `2`, **TLOG** `0`, **PULL** `1`, three separate inputs. Link **Which replica type do I need?** |
| Placement | Section label **Placement**. **Put replicas on**, closed dropdown showing **Any node, spread across zones (recommended)**. |
| Advanced | Down chevron and **Advanced: router, route field, properties, async**. Collapsed in this drawing; no expanded controls or field labels are drawn beneath it. Do not silently change the displayed `route field` wording to the former **Router Field**. |
| Footer | Separated by a horizontal border: **Cancel**, **Create Collection**, aligned right. |

The separate right-hand preview is titled **Placement** and reads:

- **6 replicas on 6 nodes in 3 zones**.
- **shard1** and **shard2**, each with **N N P** badges (two green N badges and one gray P badge).
- **Survives the loss of any 1 node. Queries can use 3 copies per shard; updates go to the 2 NRT copies.**
- **Estimated disk: unknown until documents are added.**

Differences from the two former drawings:

- Wider dialog with a main form and right-hand placement preview; the former examples are narrow single-panel forms.
- **Collection Name** becomes **Name**, with the naming helper added.
- Configset shows usage count and adds **Preview configset**.
- **Shards Count** becomes **Shards**, with minus/plus controls, value `2` instead of `1`, and a helper sentence.
- **Replication Factor** is replaced by individual NRT, TLOG, and PULL counts, plus a replica-type help link. These and Shards are visible while Advanced is collapsed.
- New **Size and Redundancy** and **Placement** sections, placement dropdown, and preview card.
- The former **Show More Options** / **Hide Less Options** text is replaced in this variant by the collapsed **Advanced: router, route field, properties, async** row. Former Router Type and Router Field controls are not exposed in this drawing. Properties and async appear only in that row, with no shown control type, defaults, or expanded layout.
- Footer gains a full-width separator; title and footer action wording remain **Create Collection**, **Cancel**, **Create Collection**.
- Neither former nor new drawing supplies any of the three requested error states or an open menu. No other dropdown choices, help content, validation behavior, or create-request semantics are established by these static drawings.

Evidence: `material/figma/2635-40394.png` and `material/figma/render/2635-40394.png`.

## Item 3: where the Collections create action lives

Status: READ; prototype dialog destination NOT ESTABLISHED.

The [cited 87-12962 URL](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=87-12962) opens the Collections page without selecting a frame. Properties shows global **Styles**, not node dimensions. Further inspection establishes this is a page reference: Figma's own Present action generates a Collections prototype URL with `page-id=87%3A12962`. It should not be described as an inaccessible file or a missing Collections page. The capture `material/figma/87-12962.png` preserves its unselected page state. There is no frame export for this page ID. The earlier round's different `87-12957` reference is not reclassified by this observation.

Using the visible Collections layer, the current section resolves independently to [14-3470](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=14-3470), inspector **Collections**, 7,768 by 7,481 px. It contains populated tree-table, empty-collection, alias, selection, and collection-health scenes. This section is additional evidence, not a replacement node silently assigned to the cited ID.

The populated [2635-33680](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=2635-33680) frame is **Collections - Tree Table (Large)**, 1,920 by 1,080 px. At the top of the content area, **Collections** and **Aliases** tabs are left-aligned. A filled orange **+ Create Collection** action is at the far upper right, followed by **Restore**. It sits above the quick-filter chips, filter bar, and table, rather than as the last collection row or card.

The section's empty-collection scenes instead show **You have no collections yet**, a three-step introduction, and an inline first card **Create a collection**, with **Name**, **Configuration**, and **Create**. Following cards are **Add documents** and **Search**. This is a separate drawn inline entry point; it does not show either requested dialog opening.

I opened the populated frame with Figma Present, fit it to the viewport, and clicked the drawn **Create Collection** button. The screen stayed at `node-id=2635-33680`, frame 15 / 31, and no overlay or dialog appeared. Screenshot: `material/figma/2635-33680-prototype-after-create.png`. Consequently neither former nor new dialog is established as the target by this prototype. Do not implement a specific link target on the claim that Figma proved it.

Section evidence: `material/figma/14-3470.png`, `material/figma/render/14-3470.png`. Readable action-placement evidence: `material/figma/2635-33680.png`, `material/figma/render/2635-33680.png`.

## Item 4: error states

Status: CHECKED, NOT DRAWN IN THESE NODES.

| Requested state | Former collapsed/expanded variants | New WithPreview variant |
| --- | --- | --- |
| Name already exists | Not drawn; no error text. | Not drawn; no error text. Naming helper is guidance, not an error. |
| Failed create | Not drawn; no error text. | Not drawn; no error text. |
| General error | Not drawn; no error text. | Not drawn; no error text. |

The parent component set's variant list is collapsed, expanded, WithPreview. No error variant is present there. These findings describe the inspected component set and exact new variant; they do not claim that no error design exists anywhere else in the file. Ticket acceptance criteria remain requirements even where the drawing is silent.

## Item 5: screenshots and manifest

Status: COMPLETE. Ten new PNGs land with this report, the manifest additions, and the separate DONE record. Five editor viewport captures cover the two requested dialog nodes, the cited page reference, the current section, and the populated action scene; a sixth captures the prototype after clicking create. Four native exports cover the two dialogs, the section, and the populated scene.

Every new PNG is below 1,000,000 bytes. Browser viewport captures are 1,280 by 720 px and were decoded from the browser's JPEG capture to PNG without resizing. Native exports retain their dimensions. The large Collections section export uses a 256-color palette to meet the size limit; the other three exports use lossless PNG optimization. `material/figma/manifest.json` preserves the earlier 25 records and adds these ten, with sizes, dimensions, SHA-256 hashes, and processing notes. No masking was needed for these nodes.

## Effect on the contribution slices

The former form reference is now verified. The new WIP design changes enough of the field inventory and layout that it should not be folded into the former-design form PR without an explicit scope choice. Christos's recorded offer allows the former design as temporary integration; reading the new node does not make it final.

The advanced-options slice still lacks an expanded new layout, control types/defaults for properties and async, menu choices, and drawn error states. The create-action placement is now concrete, but its prototype destination is unverified. Create-request success and failure behavior is not specified by these static nodes. Keep these gaps explicit when scoping those slices; do not infer them from the preview counts or control labels.

No code changed, no Gradle or Windows test queue ran, and no PR was opened. No Figma edit, comment, access request, sharing change, token creation, or MCP setup was made. Nothing was posted to Jira or as a GitHub comment.

DONE: 2026-10-27. Design-reference inspection delivered with the missing drawn states and unverified prototype target stated explicitly.
