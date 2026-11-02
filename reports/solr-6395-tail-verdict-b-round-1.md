# SOLR-6395 tail verdict, batch B claim 4: CORRECTION, do not close

- Assignment: `assignments/overseer-tail-verdicts-b-round-1.md`, Claim 4.
- Claim: `claims/solr-6395-tail-verdict-b-phone-agent.md`.
- Ticket: SOLR-6395, "If the overseer queue is large, then the cloud
  tree view (admin UI) hangs", Open, Major, reporter Timothy Potter,
  last updated 2017-02-16. Description: a backed-up overseer queue
  makes the tree view in the cloud panel almost unusable, presumably
  because the UI pulls all the overseer queue child nodes; better to
  lazily load child nodes when the parent znode tree element is
  opened. Stefan Matheis (2014-09-27) confirmed the tree view made a
  single request loading the whole tree, and asked whether one level
  could be requested at a time; Potter (2014-10-02) agreed that
  requesting znodes per level and pulling more as the user interacts
  is the best approach.
- Deliverable type: correction report, not a closure draft. The batch
  rules require this when a hang survives into the replacement UI or
  the old UI path is not actually retired, and both hold here: the
  old UI is still the shipped admin UI and still walks the whole
  tree eagerly, while the replacement UI carries no tree view at all.
  No Jira post, no close.

## Verdict

Do not close SOLR-6395 as superseded by the new UI. The census premise
("the old UI is being replaced by the new UI") does not retire this
ask, for two independent reasons, either of which alone blocks a
closure draft:

1. The old UI is still the shipped admin UI, and its tree view still
   loads the whole tree before rendering anything. The 2014 proposed
   fix, per-level lazy loading on expand, was never implemented.
2. The replacement UI does not carry a cloud tree view at all, so
   there is no replacement behaviour for this ask to have been
   superseded into. A user on the new UI cannot open a cloud tree;
   a user on the shipped UI still hits the eager walk.

## Current code, verified against the local main checkout at e55d2e7bcb

- The old AngularJS admin UI still ships: `solr/webapp/web/` with
  `js/angular/controllers/cloud.js` and the cloud panel template
  `partials/cloud.html`, whose tree pane is
  `<jstree class="tree" ... data="tree">`.
- The tree view's load path is eager and total. `treeSubController`
  in `cloud.js` calls `initTree()` on open, which runs
  `buildZkSubtree('/')`: for every node it issues one
  `ZookeeperReadV2.listNodes` call (`GET
  /api/cluster/zookeeper/children` + path, one level per call;
  server side `ZookeeperRead.listNodes` in
  `solr/core/src/java/org/apache/solr/handler/admin/ZookeeperRead.java`
  returns that level's children via `getChildren`), and recurses
  into every child that has children. Only when the whole nested
  array is built is `$scope.tree` assigned, and only then does the
  jstree directive refresh and render (`app.js` jstree directive).
  The code comment states this plainly: the client-side walk
  mirrors v1's `admin/zookeeper`, "which did this same walk
  server-side in one call".
- The v2 migration (commit b1cbbbf1a1, PR #4901, "Migrate Admin UI
  screens to v2 endpoints: Java Properties, Schema, and Cloud
  Zookeeper tree view") moved the walk from server to client but
  kept it eager: one HTTP request per znode instead of one call
  total. With a backed-up overseer queue, `/overseer/queue` alone
  contributes one request per queued item, plus the rest of the
  namespace, before the tree renders. That is the ticket's hang,
  unchanged in shape since 2014.
- No lazy loading exists on expand: nothing in `cloud.js` or the
  jstree directive fetches children when a node is opened. Node
  clicks only load the clicked node's detail pane
  (`showTreeLink`, via `listNodes` with `children: false` plus
  `readNode`), never its children.
- The new UI does not supersede the ask. `ui/` carries a Cluster
  view with a Zookeeper tab (`ClusterTab.Zookeeper` in
  `ClusterComponent.kt`, activated by default in
  `DefaultClusterComponent.kt`), but there is no tree-view
  implementation behind it: no Zookeeper child component renders
  a tree anywhere under `ui/shared`. Per `dev-docs/ui/
  testing-and-deployment.adoc`, the new UI is experimental and
  opt-in (`--module=new-ui`, `SOLR_UI_EXPERIMENTAL_ENABLED`),
  with wasmJs artifacts shipped from v10. It is a replacement in
  progress, not a shipped replacement, and the tree view is not
  among what it has replaced yet.

## What would actually settle the ticket

Either of these, neither of which exists today:

- Lazy loading in the shipped tree view: build only the root level
  at open, and fetch a node's children when it is expanded (the
  exact shape Potter agreed to in 2014). The v2 `listNodes`
  endpoint already returns one level at a time, so the server side
  needs no change; the work is client-side in `cloud.js` plus the
  jstree directive's lazy-load support.
- A tree view in the new UI that loads per level from the start,
  at which point the old UI's tree could be retired and this ticket
  closed as superseded.

## Limits

- Code read only, no build, per the batch rules; the hang was not
  re-executed against a live cluster with a large queue. The
  verdict rests on the load path's structure, which is unambiguous
  in the code: full recursive walk before first render.
- The local checkout head e55d2e7bcb dates to 2026-09-29; the tree
  controller's last change is the v2 migration above, and nothing
  in the batch record suggests a later lazy-load change. A re-check
  against current upstream main before any closure draft is cheap
  and worth doing, but it cannot turn this into a closure draft
  unless it finds lazy loading or a new-UI tree that this read
  missed.
- The ticket's own caveat stands: a backed-up overseer queue is a
  symptom of bigger issues. That does not weaken the UI ask, which
  is about not hanging the whole tree view when the queue is large.
