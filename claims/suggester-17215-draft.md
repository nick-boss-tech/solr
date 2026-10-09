# Claim: suggester SOLR-17215 draft

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: the draft for SOLR-17215, released by `material/suggester-17215-receipt.md` (commit `2468ded8b35`). The receipt names the head `04d35df186ddeab079ed93fce15c6fc61554912a`, gate GREEN there (log `g17215-gate.log`), and the pre-fix proof. The draft follows the decision in `material/suggester-round-4-decisions.md` item 6: the clearer 503 error stays, the catch is narrowed so `AlreadyClosedException` is not reported as "not built", the 500 to 503 change is the draft's Choice, and the ref-guide note on non-replication is in the branch.

Head checked live with `git ls-remote` on 2026-10-09: `solr-17215-submit` at `04d35df186dd`, the named head. Matches.

Output: `pr-drafts/suggester/SOLR-17215.md`. A second subagent checks the draft's code facts at the head independently before the lead commits.

Not in scope: edits to any `solr-*-submit` branch or PR description, opening or commenting on pull requests, builds, Gradle, and test runs, and the remaining holds in the draft round report.
