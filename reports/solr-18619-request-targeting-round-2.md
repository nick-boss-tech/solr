# Selected-local pinning and request targeting, round 2

Status: Implemented, Proof Pending.

- Fork `pr-5110-followup-request-targeting`, new pushed/read-back head `67cc03dfbed1866216c8cda8c31a65beb2c5672e`, ordinary fast-forward from `4d8c0a5a94528514aea41c429bb5009fd82a9ffa`.
- UI baseline unchanged: `b45bae5da3a151c7b439ff4e42009c383b538fae` (main plus picker plus existing gated browser prerequisites). No contributor history was rewritten.
- Gate `gates/solr-18619-request-targeting-round-2.md` supersedes/withdraws round 1; receipt `receipts/SOLR-18619-request-targeting-round-2.md`.
- Source continuation claim `claims/solr-18619-request-targeting-windows-round-2.md` records the code/spec evidence and rung-1/3 decision.

The generated selected-node reads previously omitted a target when the selection equaled the node identified by system-info discovery. That is insufficient behind a load balancer: later requests may be routed elsewhere. NodeTarget.withNodes now sets the pending per-call header after local cloud identity and a selected node are known, including selected-local reads. It retains the readiness guard to avoid sending a stale persisted selection to a standalone node before mode discovery. Metadata/discovery calls that do not use withNodes and standalone cleared state remain untargeted. The existing synchronous plugin consumes the target when the generated request is constructed; no arbitrary asynchronous shared-state race is claimed.

One additional browser test inspects actual outgoing selected-local properties, threads and core-menu headers. It waits for requests independent of their target value and asserts the required header explicitly, so restoring only the old isRemote guard should fail an assertion while real APIs remain executable. The new gate adds this sixth isolated mutation and reruns every earlier positive/mutation requirement at the new head. NodeScreens now contains 11 methods; the C4 branch has seven new methods relative to its baseline. No load-balancer runtime reproduction or passing browser result is claimed on Windows.

The baseline-to-head file scope remains the same 12 files listed in the original C4 report/gate; only services.js and AdminUiNodeScreensTest.java changed in this amendment. Scoped diff and diff --check were reviewed, the ordinary commit names Nick Shanin with no trailer, and the full source patch is pushed. No public PR/comment/Jira write was made. The logging default/All nodes choice remains as recorded through the decision ladder and must remain in the unposted PR draft for maintainer review as Nick directed.

## Current handoff and draft amendments

Current heads are C2 `bd05d1ed39a005317f0a6655e4cd8fed078531b8`, C5 `96747864862bf716daf451e02a7c24194d41c267`, C3 `a331e7faf96c8c215f2d623f4865d4cbabca6492`, C4 `67cc03dfbed1866216c8cda8c31a65beb2c5672e`. The authored new-method inventory is now 20 across the four independent branches; it is not an executed-test count. C3 uses its round-2 gate/receipt. C2/C5 retain round-1 jobs.

For the eventual C4 PR draft, use [this baseline-to-current comparison](https://github.com/nick-boss-tech/solr/compare/b45bae5da3a151c7b439ff4e42009c383b538fae...67cc03dfbed1866216c8cda8c31a65beb2c5672e), say that selected-local reads are pinned as well, and keep the logging default question from the original draft. The draft remains AI-assisted and unposted, pending Nick's human review and actual Linux results. Do not reuse the old head or present source counts as test results.

C3 and C4 are independent patches: both are needed for the combined initialization and later-request targeting behavior. A composed candidate or a changed upstream merge must receive its own exact-head integration proof before being described as tested together. At the latest source recheck no required Linux receipt or execution claim existed. The next required work remains the named Linux gate proof and any repairs it establishes.
