# Report: comment formula update, round 1

Claimed and worked by the phone agent at the owner's direction, 2026-10-27. Board snapshot read: `beb52fe9e0e701fac0960cc53fbca5d5808a7a12` (the tip when the claim was posted). Sources: root `comment-formula.md` at that snapshot, `reports/ui-17572-design-plan-draft-round-1.md`, `jira/formula-review-2026-10-23.md`, and the posted comments cited below, read live from Jira and GitHub on 2026-10-27.

## Findings

### 1. A fourth shape exists in practice and the formula does not cover it: the handoff comment

Two comments posted 2026-10-27, both contributions into another maintainer's open PR, share an exact shape that matches none of the formula's three shapes:

- PR #5112 (Smiley's), comment https://github.com/apache/solr/pull/5112#issuecomment-6450355228
- PR #5241 (Pugh's), comment https://github.com/apache/solr/pull/5241#issuecomment-6435404327

Shared structure: GitHub disclosure header; a named mention of the PR author; the finding stated concretely first (a worked counterexample request in #5112, the user-visible regression in #5241); the prepared fix described in one short paragraph, built on top of the author's current head, with a compare link to the diff; the verification in a collapsed details block (tests at a named head and seed, the new tests failing on the author's original code, checks run, links to the full receipt); exact take-over commands under a "To take ..." lead-in; and the closing line "You're welcome to take the commits or adapt the changes." (in #5241, "these commits").

This shape inverts two ask-comment rules on purpose, and the reason is structural: the diff link the ask comment forbids is the content here, because the recipient owns the branch and the comment's whole point is to hand them finished, verified commits they can take with one merge command. There is no review ask and no sufficiency question, because taking the commits is the author's decision and needs no staged permission. The formula should name this shape so the next handoff comment is written to a rule instead of improvised, and so nobody mistakes it for an ask comment that broke the rules.

### 2. "One question" is really "one decision"; linked scope questions are one decision

The formula says the ask comment asks one question, about the evidence. The SOLR-17861 ask (Jira comment 18132574, posted 2026-10-26) asked two questions in one sentence: shall I open the checksum slice as a PR for this ticket, and are you content for the LICENSE and NOTICE question to move to SOLR-17970? The sponsor answered both fully and the work proceeded. The two questions are one decision (the slice boundary) seen from its two sides. The counter-specimen stands: SOLR-18470's first touch posed three unlinked questions including scope menus, and the 2026-10-23 formula review records it as the specimen the staged form was designed against. Recommendation: reword the rule to one decision per ask comment; two linked questions that state the same decision from both sides are permitted; unlinked question menus are not.

### 3. The design-plan conflict resolves by splitting the two questions the 17572 draft raised

The SOLR-17572 design-plan report records the conflict plainly: the underlying assignment wanted two questions asked of the sponsor, whether the slices should be tracked as sub-tasks and whether he would review the first slice, while the formula allows one evidence question and no review ask; the draft followed the formula and asked neither. The two questions are not the same kind. The tracking question (sub-tasks or not) decides how the work is organized and commits the sponsor to no labor; a plan cannot be filed sensibly without it. The review question is a labor commitment and is exactly what the staged ask defers until engagement. Recommendation: give plan comments (no evidence beat exists yet; the SOLR-18470 review reached the same conclusion about that specimen) their own short shape: the plan's scope and files lead, in the files-I-expect-to-touch form; the recommendation is stated; process and tracking questions the plan cannot proceed without may be asked; a review commitment may not. That resolves the named conflict without opening first-touch review asks anywhere.

### 4. Rules the examples confirm; no change recommended

- Disclosure split by platform: both handoff comments carry the GitHub header; the SOLR-17861, SOLR-18586 and SOLR-17861-harmonization Jira comments carry the footer. No example motivates a change.
- Status note shape: confirmed by the 2026-10-23 review set (SOLR-18240, 17988, 12833, 16450, 5754) and by the SOLR-17861 harmonization comment (18133180), which is a status note ending in the owner's decision question.
- Review replies: the SOLR-18586 reply (18133116) confirms the pattern of answering at the length the exchange sets, correcting the premise with the evidence, and putting the one open decision to the maintainer.
- Engaged-sponsor adjustment: no change. It stays distinct from the handoff shape: engaged-sponsor is Nick's own ticket with a sponsor who has welcomed the work; the handoff is someone else's PR.
- Markup rules and standing text rules: no change.

## Recommendation

Adopt the replacement text in `drafts/comment-formula-update-round-1.md`. Material changes, each tied to the findings above: (1) a new section, "The handoff comment (GitHub, into another maintainer's PR)", codifying the shape in finding 1 including its closing line; (2) the ask comment's question rule reworded from one question to one decision, with the linked-question permission from finding 2; (3) a new short section, "The plan comment", per finding 3, stating which questions a plan may ask and that a review commitment may not be among them. Everything else is carried over unchanged.
