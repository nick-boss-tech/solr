# Assignment: investigate HTTP client binary compatibility after SOLR-17968, round 1

Status: OPEN

- Tags: review, records, draft, premise-run.
- Intended host: any capable Linux host with JDK 17 for consumers, JDK 21 if a Solr build is needed, Git, artifact-resolution tooling and read access to Apache Jira/GitHub. Prefer the host with the existing vm2 evidence. One build/probe job at a time under the host lock.
- Owner request: investigate the earlier change found during SOLR-18325 and determine whether a separate ticket is warranted. Deliver findings and a concrete recommendation before any broader repair.
- Deliverables: `reports/solr-17968-http-abi-investigation-linux-round-1.md`, `receipts/SOLR-17968-http-abi-investigation-round-1.md`, and, if warranted, `jira/solr-17968-http-abi-followup-ticket-draft-round-1.md`. If a new ticket is unwarranted, explain the existing tracking or documented policy in the report instead of producing a redundant draft.

## Known evidence; start here

Read this assignment and related records from one live coordination-ui snapshot, check claims and host fit, then add your own scoped claim. Do not take over the completed SOLR-18325 gate or rewrite its receipt.

- `receipts/SOLR-18325-binary-attribution-round-1.md` and its Linux report: the same released-compiled Java 17 consumer bytes pass on released 10.0.0 and fail identically before HTTP on the pre-Jackson base and candidate. Class SHA-256: `027d21fdb1d79c133aab4e4bb4f109f9d17baa05b822c7457e1c9add44bdf18a`.
- `receipts/SOLR-18325-evidence-closeout-round-1.md`, its Linux report, `reports/solr-18325-http-compatibility-prerequisite-design-round-1.md`, and `reports/solr-18325-impact-conclusion-windows-round-1.md`.
- Raw evidence: `/workspace/gates/logs/vm2/solr18325-binattr/` and `/workspace/gates/logs/vm2/solr18325-closeout/`. Reuse verified outputs; do not repeat the completed three-runtime control or full artifact inventory without a changed question or missing evidence.
- Released 10.0.0 source: `6c6c48a6f78486130682ea9c1f7a2723af5a87be`. Pre-Jackson development base: `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`. Jackson candidate: fork `solr-18325-submit` at `8bfca8edd30acf11bdf9d6a733312e97f99ec78a`. Leave all three unchanged.
- Refactor: branch_10x `e5340a954b6700627b8826431a243c2e2759b0f3`, parent `df9941affcd76be0b009f23955c841f42c565926`; main counterpart `8d3f995c6ac1633be0850d7715b788f9876ca4f2`. Verify the parent before using it as a new proof input.
- Existing issue [SOLR-17968](https://issues.apache.org/jira/browse/SOLR-17968), Resolved on preliminary read; merged [Solr PR 3829](https://github.com/apache/solr/pull/3829). The PR explicitly renames HttpSolrClientBase to HttpSolrClient and nests HttpSolrClientBuilderBase. Its upgrade-note addition is under Solr 10.1. The observed binary consequence still needs intent/policy classification.
- Released HttpSolrClientBase has `@lucene.internal` at line 53. The released builder base has no such class marker in the file read. Do not infer an automatic compatibility exemption for ordinary user code from the internal marker: javac may put those old types into bytecode generated from ordinary builder calls.

## Questions to settle

### 1. Was this particular compatibility consequence accepted?

Read the actual Jira comments, PR reviews and inline discussions, refactor history, relevant release/upgrade notes, and the project's published SolrJ compatibility guidance. Cite exact source revisions/lines and links, distinguishing explicit policy from your inference. Separate Java source compatibility, Java binary linking, and client/server HTTP compatibility. Deliberately renaming a class does not by itself establish acceptance of a same-major consumer break. Silence in a PR does not establish rejection either.

### 2. Which published versions are affected, and is the issue still present?

Identify the first containing source branch/tag and independently check actual published artifacts where available. Distinguish development snapshots from released versions. Refresh upstream branch heads once and record them; check whether a subsequent fix, revert or compatibility bridge already exists. A historical candidate failure is not proof of a current upstream failure. Pin source SHAs, coordinates, artifact hashes, JDK and dependency graphs. Do not assume a 10.1 artifact is affected solely because an upgrade note mentions 10.1.

### 3. Can ordinary documented application code trigger the failure?

Extract a minimal consumer using only normal HttpJdkSolrClient.Builder timeout calls, with no direct source reference to the old internal base types. Compile once against released 10.0.0 at release 17; hash the class and use javap to show the emitted call descriptors. Run those exact bytes on the released positive control and the relevant published/development runtimes. No server is required to demonstrate a builder-linkage failure. Report whether recompiling that identical source against each newer runtime succeeds. This distinguishes an ordinary dependency replacement failure from a subclass/internal-API-only case. Preserve all classpath manifests and complete failure signatures.

Use existing 40-reference/whole-artifact comparisons for context. Do not turn the 105 HTTP members, 134 inherited class entries, 176 other members or 11 solr-api entries into counts of independent user defects. Classify the public call chain separately from direct internal subclassing. Unrelated API changes stay outside this investigation's repair scope.

### 4. What remains uncertain in the proposed repair?

The existing design is a stub-level feasibility result, not an implemented fix. Correct the acceptance specification in a new report: instantiate and exercise the old subclass fixture at runtime, preserve direct protected-field access by moving the released fields, and require the promised old classes/descriptors to resolve without exceptions for the missing top-level classes.

Characterize current and released JDK-builder withHttpClient dispatch, executor ownership and SSL-context copying with small external probes where possible. Test both a concrete JDK client reference and an old base-typed reference, explicit builder overrides, caller-provided versus internally created executors, and closing a derived client while the original remains usable. Use bounded probe timeouts and deterministic cleanup. Report actual baseline behavior before proposing an ownership change; do not label a changed behavior as compatibility preservation. A fresh probe may expose another issue; record and scope it separately rather than silently expanding this assignment.

### 5. Does this warrant a new ticket, an update to existing tracking, or no defect filing?

Search current Jira and GitHub for duplicates. Preliminary Windows searches found the original refactor issue, but no confirmed separate ticket for this exact builder-linkage regression. The broad class-name search returned 63 results with only 50 issues in its response; finish that search with bounded date partitions or narrower queries. Additional searches for SOLR-17968 references and compatibility/binary/NoSuchMethodError summaries did not identify a duplicate. Save queries and search date; avoid claiming exhaustive absence.

Recommend one outcome with evidence: a distinct compatibility defect, an already tracked defect, an intentional documented break needing clearer migration guidance, or an unresolved maintainer policy question. A new issue draft must contain the minimal reproducer, tested affected coordinates, expected versus observed behavior, exact descriptor/stack, JDK, artifact hashes, original issue/PR links, and the fact that Jackson is not required. Do not assert an affected release that was not checked. Keep implementation proposals separate from the observed defect.

## Boundaries and completion

This is an investigation and probe assignment. It does not require selecting the previous three production API design choices. Do not implement restoration/shims, alter the Jackson branch, change gate requirements, or undertake whole-artifact compatibility repair. Existing SOLR-18325 binaryHttp FAIL and INCOMPLETE remain until separately resolved.

Before drafting public text, read the live comment formula and Jira scope rules. Prepare the ticket draft if justified; publication is a separate final step after Nick can review the concrete finding. No public Jira/GitHub write is requested by this assignment.

The receipt must record each question's answer, exact inputs, links to raw logs/probe sources, commands/exits and remaining uncertainty. The report should give Nick a plain-language recommendation on the smallest justified next action. Do not request another broad gate merely to re-prove the unchanged failure. Keep existing mutant worktrees untouched and clean up only your own disposable resources.
