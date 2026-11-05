# Claim: separate eDisMax empty phrase cleanup, round 1 (Windows)

- Status: LIVE. Claimant: windows. Tags: implementation, review, records. Started: 2026-11-05 06:51:31 UTC.
- Authority: Nick's request to implement adjacent patches using the decision ladder after discussing Smiley's concerns on PR 4652.
- Ticket: SOLR-18314. This is an authorized preparatory split of existing work, not an unsolicited competing public PR.
- Parent: `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`. New fork branch `solr-18314-edismax-empty-phrase`; Windows worktree `wt/SOLR-18314-edismax-empty-phrase`.
- Board snapshot checked: `b841908e77c2959cfe6024c1590bf59300150d93`; no competing implementation claim for this split found.
- Decision ladder rungs 1 and 3: an empty optional phrase clause contributes no useful match/score but blocks the parser's match-all simplification. Smiley explicitly supports this half of PR 4652. Reuse the guards from Ian Springer's open PR 4652 at `c122384ba230eecd0a6f2ba13a15c02908c9f0fc`, crediting that source in the report and changelog. Preserve classic DisMax behavior and the meaningful multi-token analysis cases.
- Scope: ExtendedDismaxQParser, focused existing parser tests, and a name-only-author changelog fragment. No classic DisMax single-term scoring removal or compatibility switch.
- Deliver: scoped ordinary commit and verified fork push, report and exact-head Linux gate. Keep Implemented, Proof Pending. Publication strategy remains with Nick and the original contributor; this claim opens no PR and posts no public comment.
