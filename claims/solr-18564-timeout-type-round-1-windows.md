# SOLR-18564 timeout exception follow-up, Windows implementation round 1

Status: CLAIMED

- Host: windows; tags: implementation, review, records.
- Started: 2026-10-27T17:10:45Z.
- Source: Nick's direct instruction in this chat to implement the proposed typed timeout exception and hand it to the Linux gate, following sigram's PR 5192 comment https://github.com/apache/solr/pull/5192#issuecomment-6439747314.
- Reviewed board snapshot: `94e1d1c6c2528e70aec97c245ccd95992995a81b`. No implementation claim for this follow-up exists there. The older exact-head receipts cover `dd65b21b41dc1d008c51031b71f61964ee2ca169` only.
- Slice: introduce a specific unchecked executor termination timeout exception at the existing throw site, preserve the message and waiting behavior, narrow the CoreContainer catch, and strengthen the existing ExecutorUtil timeout test. Push a normal follow-up commit to the existing `corecontainer-shutdown-closes` PR branch. No history rewrite.
- Decision ladder: rung 3, explicit owner-approved scope; rung 1 confirms that ExecutorUtil currently has only a plain RuntimeException timeout throw and CoreContainer catches that broad type.
- Deliverables: pushed patch on the existing PR head; `gates/SOLR-18564-pr5192-timeout-type-round-1.md`; `reports/solr-18564-timeout-type-round-1-windows.md`.
- Windows performs static review only. No Gradle or local test queue. Status after push is Implemented, Proof Pending until a fresh Linux receipt covers the new head.
