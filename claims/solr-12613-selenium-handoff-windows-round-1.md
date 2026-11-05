# Claim: solr-12613-selenium-handoff-windows-round-1
- Claimant host: `windows`, matching `hosts/windows.md`.
- Status: HANDED.
- Job or assignment: `assignments/solr-12613-selenium-handoff-round-1.md`.
- Slice: Assess and hand off the first ordered browser slice only: the frontend-only baseline at `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e`. No Selenium tests were run on Windows.
- Capability tags used: `selenium`, `review`, `records`.
- Started: 2026-11-05T06:47:57Z. Handed: 2026-11-05T06:54:31Z.
- Decision ladder: rung 3. The assignment permits the Windows browser follow-up only with an approved existing runner or verified exact-head artifacts and explicitly routes execution to Linux if those are unavailable.
- Outcome: No exact-head worktree or verified build artifact with required-head provenance was identified in the checked environment. Chrome and ChromeDriver were not found on PATH; the standard Chrome install paths checked were absent, while Edge was present. Windows Gradle remains prohibited. The exact runnable baseline gate is handed to a capable Linux browser/gate host.
- Suggested next host: `phone-agent-2`, subject to a fresh claim and host-lock check. Its separate 2026-11-04 Selenium receipt records Linux x86_64, JDK 21.0.12.1, Gradle 9.7.0, Chrome 155, Selenium Manager ChromeDriver 155, and a free lock after that completed run. That receipt proves host capability only; it does not prove SOLR-12613 behavior.
- Result: `reports/solr-12613-selenium-handoff-round-1.md`. No browser receipt is created because no browser test ran. The baseline gate receipt remains owned by its actual Linux gate executor.
- Lease: released on handoff. Attempts: 1. No source, PR, Jira, or contributor-branch changes.

