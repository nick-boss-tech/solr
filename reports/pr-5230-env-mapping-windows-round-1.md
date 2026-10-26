# PR 5230 environment guidance corrections, Windows round 1

Status: IMPLEMENTED, PROOF PENDING. The Windows implementation claim's deliverables are complete with this report; Linux validation is a separate open gate.

## Landed patch

- Claim: `claims/pr-5230-env-mapping-windows-round-1.md`, posted at `5e5f13dff5ca099f805b01d632ede1aed8007dc6`.
- PR: https://github.com/apache/solr/pull/5230, SOLR-17988.
- Fork branch: `solr-17988-docs`.
- Previous head: `6d2f34fb52e53f716fc66f1254924f74521967c2`.
- Pushed head: `bf285189f74b775165fc53ba305d35dccf880a47`.
- Commit: `SOLR-17988: clarify exported environment aliases and UI controls`, Nick Shanin, no attribution trailers.
- A normal fork push moved the public PR head. Both the fork ref API and separate PR read returned the new SHA. No force push, upstream push, public comment or draft-status change.
- Follow-up diff: only `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc`, 14 additions and 7 deletions. The whole PR remains three guide pages, 36 additions and 6 deletions.

## Findings addressed and correction to the old premise

Review F1 and F2 are addressed in the guide. The ST1/ST3 shell-only inference in the earlier subtask report was incomplete. Absence of a script read does not establish that an exported variable is ignored by Java.

At Solr 10.0.0 release `6c6c48a6f78486130682ea9c1f7a2723af5a87be`:

1. `solr/solrj/src/java/org/apache/solr/common/util/EnvUtils.java:58-81` initializes from `System.getenv()`; conversion and alias handling are at lines 206-250.
2. `solr/solrj/src/resources/DeprecatedSystemPropertyMappings.properties:22` maps `solr.wait.for.zk` to `solr.cloud.wait.for.zk.seconds`; line 66 inverts `solr.admin.ui.disabled` into `solr.ui.enabled`. Thus the exported old names remain deprecated aliases with warnings. The current exported ZooKeeper name works through the default conversion.
3. `NodeConfig.loadNodeConfig` constructs `SolrResourceLoader` before consuming the ZooKeeper system property; the resource loader initializes EnvUtils. Direct `Integer.getInteger` is not evidence that conversion was bypassed.
4. `solr/core/src/java/org/apache/solr/servlet/LoadAdminUiServlet.java:42-54` reads `solr.ui.enabled`. Exported `SOLR_UI_ENABLED=false` therefore affects the classic UI as well as the script's new-module decision. `solr/bin/solr:318-325` omits the new module when either UI switch is false.
5. An unexported POSIX include assignment is not inherited by Java. The note now states this and offers `export` and `SOLR_OPTS` alternatives. The experimental-only switch is documented for disabling only the new module.

These are source-derived corrections; no new runtime result is claimed. The existing `EnvUtilsTest.testFlippingDisabledToEnabledPropertyName` also represents the inverted old UI alias. Earlier reports are preserved; this report corrects their premise additively.

## Authorized live body edit

The body now agrees with the guide, distinguishes historical proof from pending validation and describes the broader inventory as planned follow-up. This addresses D1, D2 and D3 from the local review. It retains the AI header/footer, contains no em/en dashes and is approximately 2,269 visible characters.

The body was updated at 2026-10-26T01:24:44Z. A separate read-back at 2026-10-26T01:27:25Z matched the submitted file, ignoring terminal CR/LF only. The head remained `bf285189f74b775165fc53ba305d35dccf880a47`; the PR remained draft, with no comments, inline comments or reviews. No public reply was posted.

## Verification and handoff

`git diff --check` passed, the implementation worktree is clean, and the one-file follow-up was reviewed before committing. No Gradle, Java tests or local queue were used on Windows. A new Java regression test is not needed for a documentation-only correction.

The historical Ref Guide build at `16d3005293d3c6a047277bc586b30e05b6b6ef11` predates the corrected wording. New exact-head proof is required:

- Gate: `gates/pr-5230-env-mapping-docs-round-1.md`.
- Gate commit: `7cef7a3c59acb5210f10019924819a7643f51c49`; content verified and reachable from the live board tip, which was the same SHA at verification.
- Required: detached tidy preflight; standalone-clone `buildLocalSite`, `checkSiteLinks`, `checkCliDocsUpToDate`; isolated exported-environment probes against the pinned 10.0.0 release; module/consumer checks and warning logs.
- Gating host writes `receipts/SOLR-17988.md`. Until then the corrected head is implemented with proof pending. Formatting/source repairs require a new pinned gate.

Board state and relevant claims were rechecked at `ef684195228ba2dde408d9feb252e8082d95c265`; the completed round-3 claim remains historical. This direct owner-authorized Windows follow-up does not take over an active Linux claim. Decision ladder rung 1 supplied the source evidence; remaining runtime proof is explicitly handed to the capable Linux pool.
