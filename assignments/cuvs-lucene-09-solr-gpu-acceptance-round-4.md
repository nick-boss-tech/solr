# Assignment: prove Solr cuVS integration on a GPU host and prepare closure, round 4

Status: HANDED (waiting for step 8's final head, concrete jobs and a compatible GPU host)

- Intended host: Linux gate host with an actual supported NVIDIA GPU and compatible native/cuVS environment. A CPU-only pool host cannot claim the execution slice.
- Capability tags: gate, records, plus GPU capability explicitly documented in the host record. A label alone is insufficient; preflight must confirm the device/runtime.
- Claim: follow the concrete gate job from step 8. Do not create a second claim for this routing assignment. Record environment and exact input identity there.
- Dependencies: activated Solr adoption patch at a verified fork SHA, released-artifact manifest, matching CPU build/formatting proof, and a fully specified GPU job. No placeholder gate may run.

## Preflight and execution

1. Resolve the latest gate, claim and receipt state. Confirm the branch head matches the job, no other host is executing that input, and no code/formatting repair is unrecorded. Capture JDK, OS/architecture, GPU model/availability, driver/CUDA and loaded native/cuVS versions with package checksums. Confirm prerequisites from the selected release rather than assuming any CUDA installation works.
2. Use the exact focused commands, test classes, seed and formatting tasks named by the concrete gate. Establish that TestCuVSCodecSupportIT or its current replacement actually executes the affected cagra_hnsw GPU construction path. Detect device/native availability and test assumption skips explicitly. A green Gradle invocation with skipped integration tests is not acceptance.
3. At minimum execute the original SOLR-18556 indexing/graph-serialization scenario using the released artifact and Solr configuration, then query/read the resulting index and exercise the reopen/merge behavior named in the job. Include additional codec modes only where the actual adoption diff or existing test contract requires them. Record test/method counts and nonzero execution of the required GPU case; retain raw logs and fresh test XML.
4. Where practical, establish the old pinned combination's expected failure on a separate safe baseline input. If that comparison cannot run, say why and retain the original verified linkage evidence; do not pretend a GPU baseline was reproduced. Never mutate an index needed by another host or reuse stale outputs as proof.
5. Investigate any compile/native/indexing/query failure before a repair. Distinguish environment refusal before tests from a test that executed and failed. A source/test/runtime repair requires its new identity and concrete replacement job; preserve failed evidence. Do not fix a failure by accepting skips, changing the scenario, weakening assertions or ignoring linkage errors.

## Completion and closure packet

Add the receipt specified by the concrete job only after all required CPU and GPU legs cover the final intended head and artifact. It must list actual commands, environment, runtime/log/XML paths, test counts, skipped/assumption counts, artifact identities and limits. Compare current remote/PR head with the proved head before any fixed claim. An upstream NVIDIA GPU run is corroboration, not a substitute for Solr integration proof.

Add `reports/solr-18556-final-readiness-round-4.md` and a single new Jira/update draft with verified revision, released version, user-visible behavior, actual GPU result and remaining limits. Reconcile step 7's earlier proposed-fix wording and existing comment drafts. Identify any final docs correction still needed as a small scoped change; do not silently move the validated branch head. Public Jira/GitHub posting and PR creation remain Nick's decision after he reviews the exact final text.

Acceptance is an exact-input non-skipping GPU result and a reviewable closure packet. If the GPU capability or environment is unavailable, leave the work HANDED/Proof Pending in a new status record, without writing GREEN or claiming SOLR-18556 fixed. Do not repeatedly claim the task just to restate that no GPU is available.

## Limits

No execution on Windows or CPU-only hosts, no CUDA/native workaround outside the job, no merge/self-approval, no force-push/public message without direction, and no artifact or source substitution. Existing cuvs-lucene CPU receipts remain separate and unchanged.
