# Figma node index, round 1

Status: COMPLETE WITH ONE NOT-FOUND NODE

- Assignment: `assignments/figma-node-index-round-1.md`.
- Claim: `claims/figma-node-index-round-1-windows.md`, published at `cf537361572d53921c2d411008a2cc1006e23db0`.
- Host: windows, Codex desktop chat `01a16d20-4980-72a3-aa76-6735fcac2de3`.
- Inspected: 2026-10-24, approximately 09:06-09:24 UTC. Nick completed sign-in himself in the open browser tab. View-only access was sufficient.
- Initial board snapshot: `39684fd106d9f20924044aa348ddba6f6058f21d`; closeout prepared from `fbd9577d805b738af387c0471d797a40a10099d6`.
- File: [Apache Solr Admin UI v2 Concept](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept).
- Ticket comparison uses the ask recorded in `reports/new-ui-index-query-components-round-1.md` and `reports/new-ui-collections-configsets-round-1.md`, plus this assignment. This is a design-reference inspection, not a new implementation assessment or a test of interactive behavior.

## Item 1: cited nodes

All 11 exact URLs were opened. Ten selected an identifiable node; `87-12957` did not. No nearby node has been substituted for that reference. In the sections below, a displayed dropdown value is recorded as a value, not as an exhaustive menu. These are design canvases, so clicking a drawn control does not establish application behavior.

Every node has a viewport capture at `material/figma/<node-id>.png`. Resolved nodes also have a readable Figma PNG export at `material/figma/render/<node-id>.png`. The exports are additional evidence, not browser screenshots. Large section captures are necessarily small in the browser pane; use their exports to read the text.

### 23-5124: Index & Query umbrella

[Exact node](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=23-5124).

Status: READ. Page: **Index & Query**. Selected section name, exactly as the inspector exposes it: **Index & Query (formerly Queries and Operations**. The name has no closing parenthesis. Section dimensions: 4,368 by 6,000 px. Its dark section wrapper has a 100 px radius; that is not the page-radius token.

The section contains an empty application shell, `/update`, `/query`, `/select` in medium and large layouts, `/stream`, and `/sql` scenes. Shared header fields are **Collection / Core**, value `gettingstarted`, and **Request Handler**, with a path and handler class. Examples include `/select` with `solr.SearchHandler`, `/update` with `solr.UpdateRequestHandler`, `/stream` with `solr.StreamHandler`, and `/sql` with `solr.SQLHandler`.

Visible content includes **Request Builder**, **Regular Request Form**, **JSON Query API**, **Stream Expression**, **SQL Query Statement**, **Response**, **Metadata**, **Request Preview**, **Clear All**, **Execute in Debug**, and **Execute**. Stream and JSON examples show **Create Daemon Process**, **ID**, and **Run Interval (ms)**, with `10000` displayed. Request Preview shows **URL**, **Query Parameters**, **Payload**, **Copy curl**, and **URL Encoded**. Response examples show status, Zookeeper connection, query time, max score, documents found, start, exactness, and raw/table icons. Empty responses say `Click "Execute" to see results here.`

The section also contains design notes titled **Jira References**, **New Features**, **Jira Bugs that are addressed**, and **Unreported Issues that are addressed**. They describe local query history, curl construction, query-builder validation and documentation, switching collection without losing input, and preserving input after execution. Those notes express design intent, not proof that the bugs are fixed.

Ticket match: consistent with SOLR-17848's umbrella. It is a collection of scenes and requirements, not one small form. The enclosing section and current sidebar use Index & Query; the contents frame still uses Queries & Operations.

Evidence: `material/figma/23-5124.png`, `material/figma/render/23-5124.png`.

### 137-12066: header and large /select page

[Exact node](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=137-12066).

Status: READ. Page: **Index & Query**. Frame: **Index & Query - /select (Large)**, 1,920 by 1,080 px.

Two outlined dropdowns sit next to each other above the content: **Collection / Core** with `gettingstarted`, and **Request Handler** with `/select` and `solr.SearchHandler` on the same line. The second field is wider. There are two icon actions at the upper right, including a history symbol; their drawn icons have no visible text labels. The left content column is the Regular Request Form; the right is **Response**, initially showing `Click "Execute" to see results here.` The long form is clipped by the bottom of this fixed-height scene.

The sidebar reads **Dashboard**, **Metrics**, **Cluster**, **Security**, **Configsets**, **Collections**, **Index & Query**, **Analysis**, **Environment**, **Logging**, **Thread Dump**, and **Logout**. The footer reads **Documentation**, **Solr Query Syntax**, **Issue Tracker**, **Community**, **Slack**, **IRC**, and **Support**. These shell labels recur on the other scene exports.

Ticket match: verifies SOLR-17849's two header dropdowns and the renamed area cited by SOLR-18493. This exact frame shows neither an expanded option list nor loading, empty, or disabled dropdown states. Those ticket requirements remain requirements; they cannot be inferred from this populated mockup.

Evidence: `material/figma/137-12066.png`, `material/figma/render/137-12066.png`.

### 156-4776: complete Regular Request Builder

[Exact node](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=156-4776).

Status: READ. Page: **Index & Query**, under its Components section. Component: **Regular Request Builder**, 864 by 1,632 px. The purple dashed component wrapper has a 5 px radius and 16 px padding; it is not the 16 px card token.

The visible field and action list is:

| Label | Displayed value or control |
| --- | --- |
| Request Builder | Heading |
| Regular Request Form | Subheading |
| Query (q) | `*:*`, a clear icon, and **Add Query** |
| Query Parser (defType) | Closed dropdown, `(default)` |
| Query Operator (q.op) | Closed dropdown, `OR` |
| Writer Type (wt) | Closed dropdown, `(default)` |
| Filter Query (fq) | `first_name:John`, `last_name:Doe`; clear and delete icons; **Add Filter Query** |
| Sort (sort), Order | `score`, `asc`; clear icon; **Add Sort** |
| Start | `0` |
| Rows | `10` |
| Field List | `first_name`, `last_name`, `address(zip_code,street)`; per-row clear icons |
| Field-list add action | **Add Filter List Element**, exactly as drawn |
| Paramsets | `first_name`, clear icon; another **Add Filter List Element** action |
| Additional Query Parameters | Inputs `key` and `value`; **Add Query Param** |
| Highlighting (hl) | Toggle, shown off |
| Faceting (facet) | Toggle, shown off |
| Spatial Options (spatial) | Toggle, shown off |
| Spellchecking (spellcheck) | Toggle, shown off |
| Footer actions | **Clear All**, **Execute in Debug**, **Execute** |

Ticket match: supplies the field inventory missing from the SOLR-17850 text, and visually matches abbreviated labels, repeatable entries, displayed defaults, clear-all, and both execution actions. The design does not prove required-field validation, execution semantics, toggle-expanded content, or every dropdown option. The repeated **Add Filter List Element** wording is recorded without silently correcting it. Request Preview belongs to the surrounding scenes and is outside this component and the ticket's recorded first scope.

Evidence: `material/figma/156-4776.png`, `material/figma/render/156-4776.png`.

### 14-3248: Configsets section

[Exact node](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=14-3248).

Status: READ. Page and section: **Configsets**. Section dimensions: 21,810 by 2,771 px. This link selects a wide section containing multiple screens, dialogs, reference notes, and component examples.

The top navigation is **Overview**, **Libraries**, **Files**, **Schema**, **Update Configuration**, **Index / Query**, **Request Handlers / Dispatchers**, **Search Components**. The shared selector is **Configset**, value `gettingstarted`. Scene actions include **Create Configset**, **Edit solrconfig.xml**, **Reset Changes** or **Discard Changes**, **Apply Changes**, and overflow menus.

The section inventory is:

- **Create Configset** dialog: **Configset Name**, **Base Configset** (`_default`), **Import Configset**, **Cancel**, **Create Configset**.
- **Import Configset** variants: **Choose zip file to upload**, `or drag file here`, **Drop file here**, **Selected File** (`mycollection.zip`), **Configset Name**, **Create Configset**, **Cancel**, **Import Configset**, including a disabled import button before selection.
- **Overview**: **Basic Information**, **Schema Information**, **Index Configuration**, **Update Handler Configuration**, **Query Configuration**, and a summary for **General**, **Query**, **Request**. Basic labels include Name, Base Configset, Lucene Match Version, Data Directory, Directory Factory, Codec Factory. Schema labels include Schema Version, Dynamic Fields, Field Guessing, Nested Child Documents, Languages, Is Published.
- **Libraries**: **Add Library**, **Filter library list**, and table headings **Directory / Path**, **Exp/Imp**, **Resolves To**, **Regex**, **Matching Files**. A red design overlay explicitly says this section may be removed in favor of SOLR-6681 and SOLR-16777. Do not treat Libraries as an unquestioned implementation requirement.
- **Files**: tree, selected `synonyms.txt`, numbered file text, **Upload File**, **Download File**, **File Metadata**, **Discard Changes**, **Save File**. See the exact large and medium references below.
- **Schema**: filter example `element:dynamic-field indexed:true`; table headings **Elmt**, **Name**, **Type / Class**, **Default Value**, **Copy To**, **Indexed**, **Stored**, **Multi-Value**, **Doc Values**, **Doc Value as Stored**, **Required**, **Tokenized**, with row detail chevrons. **Create Collection** and **Edit solrconfig.xml** appear above it.
- **Field Details**: a back link to `gettingstarted`, selectors **Field** and `description_t`, **Type** (`text_general`), **Default Value**, **Unique Key**, **Indexed**, **Stored**, **Multi-Valued**, **Doc Values**, **Use Doc Values as Stored**, **Required**, **Tokenized**, **Sort Missing Last**, **Uninvertible**, term-vector/position/offset/payload and omit flags, **Large**, **Copy to**, **Add Destination Field**, **Index Analyzers**, **Query Analyzers**, **Reset Changes**, **Save Changes**.
- **Update Configuration**: **Add Update Processor**, **Create Processor Chain**, **Edit solrconfig.xml**, a filter, and **Name**, **Class**, **Parameters** columns. Indented chains connect named processors such as `add-unknown-fields-to-the-schema`, `uuid`, `remove-blank`, `field-name-mutating`, `parse-boolean`, `parse-long`, `parse-double`, `parse-date`, and `dedupe`.
- Reference cards enumerate **Search Components**, **Request Handlers / Dispatchers**, **Query Configuration**, **Initial Parameters**, and the expanded/collapsed information cards. Some tabs have an enum or inventory without a completed scene in this section.

Ticket match: a useful integration reference for SOLR-17886's reusable components and the Configsets umbrella, not itself a reusable-component specification. It does not settle SOLR-18222's empty ticket description or SOLR-18223's server write contract. It shows where file-tree, tabs, and viewer components fit. Collections has its own page and is not the subject of this section.

Evidence: `material/figma/14-3248.png`, `material/figma/render/14-3248.png`. The large export uses a 256-color PNG to meet the per-file limit; all token values below come from the inspector.

### 13-3972: large ZooKeeper tree scene

[Exact node](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=13-3972).

Status: READ. Page: **Cluster**. Frame: **Cluster - Zookeeper - Overview (Large)**, 1,920 by 1,080 px.

Tabs are **Zookeeper**, **Nodes**, **Cores**. **Auto-Refresh** shows `30 seconds`. The left panel heading is **ZK Root Directory**, with a dropdown chevron. The tree has folder icons and disclosure chevrons for `collections`, `configs`, `live_nodes`, `node_roles`, `overseer`, `overseer_elect`, `zookeeper`, then file icons for `aliases.json` and `security.json`. Extensions are visibly retained.

The right column has **Zookeeper Overview** and **Zookeeper Details**, both expanded. Overview labels include Status (`green`), ZK connection string (`zoo:2181`), Ensemble size (`1`), Ensemble mode (`standalone`), Dynamic reconfig enabled (`false`). Details include Status, Client Port, Secure Client Port, ZK Server State, ZK Version, ZK Approximate Data Size, ZK ZNode Count, ZK Connections Alive, Data Directory, Data Log Directory, ZK Average Latency, ZK Max File Descriptors, ZK Watch Count, ZK Packets sent/received, Tick Time, Max Client Connections, Min/Max Session Timeout.

Ticket match: demonstrates SOLR-17887's IDE-style folder/file presentation and visible extensions. A static scene does not prove the single-click output, double-click output, expansion behavior, or selection model.

Evidence: `material/figma/13-3972.png`, `material/figma/render/13-3972.png`.

### 13-3977: medium ZooKeeper tree scene

[Exact node](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=13-3977).

Status: READ. Page: **Cluster**. Frame: **Cluster - Zookeeper (Medium)**, 1,280 by 720 px.

Same **Zookeeper / Nodes / Cores** tabs, `30 seconds` Auto-Refresh, **ZK Root Directory**, folders, files, and two expanded overview/detail cards as the large scene. The tree is narrower; the visible detail content ends at ZK Watch Count before the footer. No different file names or expanded menu options are shown.

Ticket match: the medium layout reference for SOLR-17887. It supplies sizing context, not extra behavioral acceptance criteria.

Evidence: `material/figma/13-3977.png`, `material/figma/render/13-3977.png`.

### 227-7661: large Configsets Files scene

[Exact node](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=227-7661).

Status: READ. Page: **Configsets**. Frame: **Configsets - Files (Large)**, 1,920 by 1,080 px.

The eight top tabs listed under 14-3248 are visible, **Files** highlighted. Below them: **Configset** (`gettingstarted`), **Upload File**, **Edit solrconfig.xml**, **Reset Changes**, **Apply Changes**, and overflow. The tree header is **Configset** with a dropdown and `/configs/gettingstarted`. A collapsed `lang` folder precedes `managed-schema.xml`, `protwords.txt`, `solrconfig.xml`, `stopwords.txt`, and selected `synonyms.txt`. Selected row: salmon background and a filled file icon.

Above the content: `synonyms.txt`, **Download File**, **File Metadata**, overflow, **Discard Changes**, **Save File**. The editor-like sample has a separate line-number gutter, lines 1-29, preserved blank lines, monospaced synonym text, and a highlighted current line. Examples include `aaafoo => aaabar`, `GB,gib,gigabyte,gigabytes`, and `Television, Televisions, TV, TVs`. The drawing depicts editing and save actions in the host scene.

Ticket match: a combined placement reference for SOLR-17887, SOLR-17896, and SOLR-17911. It confirms tab ordering and the tree/viewer arrangement. SOLR-17911's recorded scope is a plain-string read-only viewer with selection and scrolling; this broader screen's edit/save controls do not expand that ticket automatically. Selection and scrolling are not executable proof from a canvas.

Evidence: `material/figma/227-7661.png`, `material/figma/render/227-7661.png`.

### 227-9235: medium Configsets Files scene

[Exact node](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=227-9235).

Status: READ. Page: **Configsets**. Frame: **Configsets - Files (Medium)**, 1,280 by 720 px.

The narrower variant keeps the selected Configset, **Upload File**, **Reset Changes**, **Apply Changes**, and overflow. The top tabs extend beyond the right edge of the visible frame. The tree shows the same `lang` folder and five files, with `synonyms.txt` selected, and a shortened `/configs/gettin...` path. The file toolbar visibly retains **Download File**, overflow, **Discard Changes**, and **Save File**; the large scene's File Metadata and Edit solrconfig.xml text actions are not visible here. The numbered content starts at line 4 and ends at 29 in this example.

Ticket match: medium-width layout evidence for SOLR-17887. The clipped tabs/path and omitted text actions should not be replaced with invented menu contents.

Evidence: `material/figma/227-9235.png`, `material/figma/render/227-9235.png`.

### 87-12957: closed navigation reference

[Exact URL tried](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=87-12957).

Status: NOT FOUND. After loading, the URL retained this id, but Figma selected no item. The left panel was on Metrics, the canvas accessibility name was **Figma Design** without the selected-item suffix, and Properties showed global **Styles**, not a named node or its dimensions. The other ten references selected a named node. This is the observed failure to resolve the reference, not proof of why it disappeared.

No frame name, text, or closed-navigation layout is asserted for this id. SOLR-17896's closed-state design remains unverified through this link. The Configsets navigation in 227-7661 is independently readable but is not a replacement for this node.

Evidence: `material/figma/87-12957.png`, capturing the unresolved state. No native node render is available.

### 1997-7453: Collections breadcrumb scene

[Exact node](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=1997-7453).

Status: READ. Page: **Collections**. Frame: **Collections - Nested Grid View - Collections - techproducts (Medium)**, 1,280 by 720 px.

The top breadcrumb is **Collections**, a double-chevron separator, **techproducts**. A separate large **techproducts** heading appears below the action row. Upper-right actions: **Refresh**, **Switch to Table View**. Collection actions: **Rename**, **Reload**, **Reindex**, **Rebalance**, **Migrate**, **Delete**.

Cards are **Collection Details** (Configset, Replication Factor, Router, Z-Node Version, Schema None-Compliant, Creation Time) and **Collection Attributes** (`foo`, `autoAddReplicas`, `leaderPreference`, **Modify Attributes**). Right-hand **Shards** and **Replicas** cards use pie charts with PULL/TLOG/NRT and state legends. Other actions: **Show Shards**, **Backup Collection**, **Show Snapshots**.

Ticket match: gives SOLR-17909 a concrete breadcrumb in its optional Collections consumer. The static frame does not establish pop-to-index behavior, whether the last item is inert, or wrapping. Those remain governed by the ticket rather than guessed from styling.

Evidence: `material/figma/1997-7453.png`, `material/figma/render/1997-7453.png`.

### 319-20114: ZooKeeper file preview

[Exact node](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=319-20114).

Status: READ. Page: **Cluster**. Frame: **Cluster - Zookeeper - File Preview (Large)**, 1,920 by 1,080 px.

The shared **Zookeeper / Nodes / Cores** tabs, **Auto-Refresh** (`30 seconds`), and root tree are present. `security.json` is selected. **Zookeeper Overview** and **Zookeeper Details** are collapsed. A third card is titled **/security.json** with a **Close** text action. Its monospaced JSON is displayed with a separate line-number gutter, lines 1-23, and preserved indentation. No editing, syntax-coloring, or save controls are drawn in this preview. The design includes a sample credential value; that line is masked in both outgoing images and is not transcribed here.

Ticket match: a direct read-only visual reference for SOLR-17911, unlike the editable Configsets host scene. The image supports line-number and layout requirements, but cannot prove selection, escape handling, or horizontal/vertical scrolling.

Evidence: `material/figma/319-20114.png`, `material/figma/render/319-20114.png`, with the design-sample credential value masked.

## Item 2: table of contents and page list

Status: READ. On **Introduction**, selecting the **Table of Content** layer resolved node [394-288](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=394-288), 768 by 2,268 px. The visible entries, in order, are:

| Order | Visible name | Purpose described in the frame | Resolved reference in the assignment |
| --- | --- | --- | --- |
| 1 | Start | Connect to Solr and optionally supply credentials | None |
| 2 | Dashboard | Overview of current cluster and node state | None |
| 3 | Metrics | Data from the Metrics API | None; 87-12957 did not select a node |
| 4 | Cluster | ZooKeeper, nodes, and cores | 13-3972, 13-3977, 319-20114 |
| 5 | Security | Configure cluster security, users, and access | None |
| 6 | Configsets | Configsets, files, schema design, configuration | 14-3248, 227-7661, 227-9235 |
| 7 | Collections | Collections, replicas, and aliases | 1997-7453 |
| 8 | Queries & Operations | Requests, responses, and a playground | 23-5124, 137-12066, 156-4776, on the renamed page |
| 9 | Environment | Versions, JVM arguments, environment variables | None |
| 10 | Logging | Look up logs and logged problems | None |
| 11 | Thread Dump | Visualize thread-dump output | None |

The visible eighth heading uses **Queries & Operations**, with an ampersand. Its layer is named **Queries and Operations Page**. Record both that wording and **Index & Query**; no naming decision is made here. There is no Analysis card in this contents frame even though the current sidebar/page list includes Analysis. The final Thread Dump card is internally named **Logging Page**, another reason to read visible text instead of treating layer names as the page titles.

The current Figma page list, distinct from the contents frame, is: Introduction; Components; divider; Start; Dashboard; Metrics; Cluster; Security; Configsets; Collections; Index & Query; Analysis; Logging; Environment; Thread Dump; divider; Archive. Notice Logging precedes Environment in the page list but follows it in the contents frame.

Evidence: `material/figma/394-288.png`, `material/figma/render/394-288.png`.

## Item 3: design-token spot check

Status: VERIFIED WITH SCOPE CLARIFICATION. Two card samples are **Sign In Page** [394-295](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=394-295) and **Dashboard Page** [394-303](https://www.figma.com/design/VdbEfcWQ8mirFNquBzbPk2/Apache-Solr-Admin-UI-v2-Concept?node-id=394-303), with their text children. Shared page background, page radius, and headline were checked on their enclosing contents frame and headline. This distinguishes properties on the common parent from properties that actually belong to each card.

| Supplied token | Inspector observation | Result |
| --- | --- | --- |
| Page background #FFF8F6 | Table of Content 394-288, Schemes/Background, #FFF8F6 | Confirmed on shared parent |
| Text #271814 | Headline 394-289 and both descriptions 394-298 / 394-306, Schemes/On Background, #271814 | Confirmed for headline/body; card link titles instead use #4D4544, Schemes/On Surface Variant |
| Surface #FDF8F8 plus rgba(181,39,3,0.05) | Both cards use solr-theme-m3/surfaces/light/surface1, with #FDF8F8 and #B52703 at 5% | Confirmed on both cards |
| Headline Raleway 24px/32px | Contents headline 394-289, solr-theme-m3/headline/small, Raleway Regular 400, 24px, 32px, 0px letter spacing | Confirmed on shared headline |
| Title Roboto 22px/28px | Start title 394-296 and Dashboard title 394-305, solr-theme-m3/title/large, Roboto Regular 400, 22px, 28px, 0px letter spacing, underlined | Confirmed on both titles |
| Body Roboto 14px/20px, 0.25px letter spacing | Descriptions 394-298 and 394-306, solr-theme-m3/body/medium, Roboto Regular 400, 14px, 20px, 0.25px | Confirmed on both descriptions |
| Card radius 16px | Both card inspectors: 16px; each is 688 by 180px with a 16px gap | Confirmed on both cards |
| Page radius 24px | Contents parent 394-288: 24px, with 40px padding and 16px gap | Confirmed on shared parent |

These values match the supplied CSS for this contents frame, with the text-role qualification above. They are not evidence that every historical scene uses the same outer fill or radius. For example, 137-12066 and the Configsets/ZooKeeper scene frames expose #FFFFFF, while section wrappers expose #444444 and 100px radii. Do not bulk-replace those values based on this sample.

Evidence: `material/figma/tokens-start-card.png`, `material/figma/tokens-dashboard-card.png`; precise text and parent node ids are listed above. No whole CSS export is included.

## Item 4: image delivery and limits

Status: COMPLETE. There are 25 PNG files: 11 requested-node viewport captures, one contents-frame viewport capture, 11 native renders for the ten resolved requested nodes and the contents frame, and two inspector captures. Every PNG is below 1,000,000 bytes. Largest PNG: 741,402 bytes. Combined PNG size: 4,385,236 bytes.

`material/figma/manifest.json` records each path, byte count, dimensions, SHA-256, and any masking or palette conversion. The unavailable node has an unresolved-state screenshot, not a fabricated render. The Configsets section render was converted to a 256-color palette to stay below the limit. The file-preview sample credential value was masked in the outgoing screenshot and render. Other native renders retain their original pixels apart from lossless PNG optimization.

No Figma token or MCP was requested. No password, session cookie, login artifact, edit, comment, access request, or sharing change was made by this host. No Jira/GitHub comment was posted. No code changed and no Gradle or test queue was run. Menu options and behaviors not visible in these nodes remain explicitly unverified.

DONE: 2026-10-24. The report, image evidence, manifest, and new completion record land together. The original assignment and claim remain unchanged under the board's add-only workflow.
