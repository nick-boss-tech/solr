---
name: solr-review
description: Review an Apache Solr PR or branch at a pinned revision for correctness, architecture, reuse, compatibility and evidence quality. Use for source assessment and follow-up review, not automatic implementation.
---

# Review a Solr revision

Read applicable workspace instructions and the existing research review guide when available. Refresh the PR identity/discussion, including inline threads, and fetch the actual reviewed head. Record the exact head, base and scope. Reuse earlier findings as history; they are not a current whole-PR verdict.

Trace behavior from its entrypoint to the relevant boundary. Assess correctness, architectural fit, maintenance/reuse, compatibility/security and verification separately using [the worksheet](references/review-worksheet.md). For an API/hook, inspect another real consumer. For a new fixture/helper, search existing infrastructure and explain the addition. Apply the project's threat model before describing a security finding.

Verify claims against the code and distinguish source-confirmed behavior, a hypothesis and executed proof. Test-file presence does not establish assertion quality. Review the actual gate and receipt: compare head, required legs, failures/skips and material execution conditions. A source-only review says so; choose runtime work only when authorized and on a capable host. Windows factory hosts do not run Solr Gradle or the retired test queue.

Return actionable findings with file/revision evidence, impact and a bounded repair or proof proposal. State what was assessed and what remains unassessed before an overall verdict. A request to review another contributor's PR authorizes assessment, not edits or publication; follow existing session authorization for follow-up work.
