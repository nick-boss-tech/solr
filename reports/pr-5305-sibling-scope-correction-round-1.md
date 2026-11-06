# SOLR-18627 / PR 5305: correct the sibling-repair scope recommendation

Status: RECORDED. Add-only scope correction; runtime gate remains OPEN and source remains Implemented, Proof Pending.

- Applies to claim claims/pr-5305-ignored-fields-windows-round-1.md, report reports/pr-5305-ignored-fields-implementation-round-1.md, and gate gates/pr-5305-ignored-fields-round-1.md.
- Prepared branch: nick-boss-tech/solr:pr-5305-solr-18627-ignored-fields, head 219fee78bf7cae5039e50fe885108a1d4937ccd3.
- Contributor PR 5305 remains OPEN at 00292cd3729126194235e939afe38e351d850c70. Its live discussion was refreshed for this correction.

Nick pointed out that the author is aware of the sibling cases and deliberately fixes only the reported StrField case. The author explicitly states that scope in https://github.com/apache/solr/pull/5305#issuecomment-6679331149 and reiterates it in https://github.com/apache/solr/pull/5305#issuecomment-6684604464. Eric Pugh asks whether the sibling types share the problem; neither his conversation question nor his inline checker question requests expanding this PR or establishes that a broader contribution would be rejected.

The deeper review correctly identifies the sibling source paths, but its recommendation to include them in PR 5305 was too strong. A shared originating commit establishes a coherent follow-up, not a requirement to expand a deliberately narrow regression fix. Recommend keeping PR 5305 scoped to StrField and treating the prepared sibling branch as a separate follow-up candidate.

The existing gate may proceed as technical evidence for the prepared branch. Its required commands, expected failures, pinned source head and receipt requirements are unchanged. Superseding only the communication instruction at the end of the job: prepare a separate sibling-follow-up summary rather than a replacement body for PR 5305. A passing receipt does not settle contribution scope or authorize a public offer to expand PR 5305. The scope concern must be resolved with Nick before any such offer; do not apply the standing clean-gate comment exception while this concern remains.

The current branch retains contributor ancestry and is suitable as a tested continuation. It is not yet an independent PR package: opening it against current main would include the original StrField contribution. After PR 5305 lands, a separate submission should be prepared on the resulting main, carrying only the sibling repair and tests, with a separate changelog fragment and appropriate issue scope. Preserve the existing branch/history and test evidence; any differently based or changed submission head needs matching validation. Opening another PR or filing/updating JIRA still requires Nick's explicit authorization.

No source changes, branch rewrite, cancellation of technical gate execution, public comment, PR-body edit, new PR or JIRA write is part of this correction.
