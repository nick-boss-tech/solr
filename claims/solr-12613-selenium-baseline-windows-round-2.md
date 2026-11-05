# Claim: solr-12613-selenium-baseline-windows-round-2
- Claimant host: `windows`, matching `hosts/windows.md`.
- Status: ACTIVE.
- Job or assignment: `assignments/solr-12613-selenium-handoff-round-1.md`; execution job `gates/solr-12613-frontend-only-baseline-round-1.md`.
- Supersedes: `claims/solr-12613-selenium-handoff-windows-round-1.md` (HANDED). Nick explicitly authorized Windows Gradle execution in the current chat for this attempt, and supplied the prior Windows Selenium run record. That reopens the first browser slice only; it does not move tidy, core-neighbor, root-check, or full-receipt ownership from the Linux gate host.
- Slice: Windows Selenium positive tests and the three isolated callback-argument mutations for the frontend-only baseline at exact fork head `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e`. Preserve the gate's seed, JVM flags, generated-client provenance, and intended mutation assertion. Do not run tidy, core tests, root checks, lifecycle, request-targeting, or combined jobs in this claim.
- Capability tags used: `selenium`, `review`, `records`, `windows-check`.
- Started: 2026-11-05T07:18:20Z.
- Decision ladder: rung 3. The assignment explicitly permits Windows browser execution with an approved runner or exact-head artifacts. Nick's current instruction expressly overrides the local Windows Gradle restriction for this run. The assignment still reserves non-browser proof and the full receipt for Linux.
- Candidate: `nick-boss-tech/solr:solr-12613-frontend-only-baseline` at required head `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e`, refreshed against the live fork ref before claiming.
- Prior setup evidence: the local PR 5282 exploratory record shows Chrome `156.0.8078.48` and Selenium Manager ChromeDriver `156.0.8078.80` starting successfully on this host; it is setup evidence only, not SOLR-12613 proof.
- Planned evidence: per-command logs, fresh JUnit XML copied before each `cleanTest`, browser/JDK/driver versions, exact worktree HEAD/tree, test counts, screenshots/page-source artifacts where produced, and isolated mutation diffs plus the intended argument-count failures.
- Lease: active while browser runs and hourly host activity continue. Attempts: 1. No source commit, fork push, PR, Jira, or upstream write is authorized or planned.

