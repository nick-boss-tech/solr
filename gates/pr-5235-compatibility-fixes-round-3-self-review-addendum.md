# Gate addendum: PR 5235 round 3 self-review correction

Status: OPEN
Tags: gate, premise-run, review, records
Intended host: the host claiming gates/pr-5235-compatibility-fixes-round-3.md

Supersedes that job's predicted failure for testGenericLegacyStreamsRetainAllDocuments only. This is the SAME execution job, not an additional gate or a request to repeat valid work. Name both files when claiming; all commands, test scope, original/fixed production pair, seed, checks and receipt/outcome targets remain those of the original job.

- Fixed head F remains 5f279c7aa44a993bd999be1d4dfc6ecab7942bc8.
- Original head O remains 9ff44cdd0b818c6f1296d69651732f794acd76b7.
- Seed remains 5235C0FFEE18351.
- Required receipt remains receipts/PR-5235-compatibility-fixes-round-3.md.
- Review details: reports/pr-5235-compatibility-fixes-round-3-self-review.md.

## Corrected original-leg expectation

The new generic embedded method should fail on O at client.request with SolrException BAD_REQUEST, message "missing content stream", BEFORE its document-count assertion. Original EmbeddedSolrServer returns no streams for the GenericSolrRequest subclass; SolrRequestParsers.java:208-210 leaves the request streams null for an empty list; ContentStreamHandlerBase.java:91-94 rejects the update. This is an intended missing-payload failure, not a harness failure. F must process the request and index exactly IDs 1 and 2.

The earlier prediction "indexes zero instead of two" was incorrect. Do not require that assertion to be reached, weaken the test, repair the original production leg, or restart valid execution because this exception occurs. Preserve the actual exception, stack and XML in the receipt. This generic case is additional coverage of the explicit embedded fallback; do not describe it as a fifth independently established B-to-O regression.

The confirmed unnamed ContentStreamUpdateRequest case still expects O to index one document, with F indexing both. All other API/body/hook/control outcomes and 18-method counts are unchanged. Expected O totals remain 12 failing methods and 6 passing controls; if XML distinguishes failures from errors, report both accurately. No source or test file changed, and there is no new branch head to gate.
