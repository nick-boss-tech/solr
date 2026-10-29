# SOLR-18600: dual-release compatibility direction, round 3 (Windows)

Status: DIRECTION RECORDED. This addendum supersedes the restore-versus-document framing in the round-2 recommendation and its local mailing-list draft. The existing fix remains held; this is not implementation or proof completion.

## Owner clarification and recommendation

Nick stated on 2026-10-29 that the RC3 vote is closed and proposed pursuing compatibility for callers compiled against both 10.0 and 10.1. Adopt that as the patch acceptance target. Stop framing a new message as an intervention in an open RC3 vote.

The concrete proposal is to repair the affected HTTP client API so unchanged 10.0-compiled AND unchanged 10.1-compiled consumers run on the same future patched version. Do not ask maintainers to choose which release's consumers the patch should break. Maintainers still decide acceptance, supported scope and release/backport placement. That distinction does not require delaying the technical design until a general policy is decided.

Compatibility here is forward upgrade compatibility into the repaired runtime. Running 10.1-compiled consumers on a 10.0 runtime is outside this target. This is also not a claim to restore every unrelated SolrJ or solr-api ABI change.

## Implementation direction to investigate

Preserve the 10.1 surface and add back the 10.0 surface, rather than replacing the former with the latter:

- Restore the legacy client/builder supertypes, but retain the newer nested BuilderBase's fluent descriptors using explicit forwarding/covariant overrides where required. The current move of methods without such overrides is the reproduced defect in the proposed restoration.
- Keep both old and new parameter overloads and constructor descriptors. A generic-bound change also changes erased constructor/getter descriptors and must be reviewed as an API change.
- Retain newer getter return types and investigate an inherited legacy declaration or compatibility interface that lets javac emit the old covariant-return bridge. Java source cannot directly overload a method solely by return type; simply changing the getter back or adding a differently named getter is insufficient. This bridge design is a candidate for a Linux compiler/descriptor probe, not a demonstrated implementation.
- Preserve released fields where they are in the agreed extension surface, and keep current request routing and ownership behavior. Restoring a base class is not permission to revert unrelated 10.1 behavior.

The named source findings and scope inventory remain in reports/vm2-solr-18600-premise-review-round-1-windows.md. The later addendum already reproduces loss of the 10.1 JDK timeout descriptor on the restoration variants. Cloud builder success does not establish Cloud getter, LB or ConcurrentUpdate compatibility.

## Proof required before offering the repair

Compile and hash fixtures once against released 10.0.0 and once against released 10.1.0. Each cohort must pass on its matching release as a positive control, then pass unchanged on the SAME repaired build. Retain the original 10.0-on-10.1 failure as the negative control.

Include JDK/Jetty fluent calls and bridges, old/new Cloud overloads and getters, LB constructors/getters/dispatch, provider constructors/getters, ConcurrentUpdate constructors/getters and the promised SolrRequest overload. Compare the supported descriptor set to both releases, including inherited members and generated bridges. Add ordinary current-source compilation controls and behavior/ownership checks. After a source repair is committed and pushed, a fresh exact-head Linux gate must cover that head. No Windows build or probe is authorized by this record.

The next discriminating step is a Linux source/compiler prototype for retaining both descriptor sets. Do not repeat the already established failure without a new design to test. This record does not launch a gate, reset vm2's scratch clone or claim a Linux execution slot.

## Communication

Present a concrete additive repair proposal with the two-cohort proof as its acceptance bar. Describe its design as proposed until built, and as verified only after the required runs pass. The remaining question is whether maintainers want to accept that scoped repair and where to ship it, not whether to trade one binary break for another.

Keep a 10.1 upgrade-note correction as a companion: it explains behavior of existing 10.1 jars and does not substitute for the repair. Do not imply that a future patched release retroactively changes 10.1.0.

The local dev-list draft now states this proposal instead of asking maintainers to choose between documentation and restoration. No upstream message, JIRA edit or PR was posted. Read at board snapshot 8f02e517e13c7de4756093760055e3bcfc3519ff; owner clarification is the source for the closed-vote status.
