# Assignment: update processing, round 3 close-out

Close out final round 3. The round 3 report is
reports/update-processing-final-round-3.md; every answer and
the new material are in
material/update-processing-round-3-closeout.md. Where the
material records a decision, it is taken; do not re-pose it
as open.

Rules, as before: verify the live tip equals the named head
or hold the branch; Proof only from the named source; no PRs
opened, no comments, no submit-branch edits; draft format
per pr-formula.md. Claim first with a commit under claims/,
then drafts under pr-drafts/update-processing/, then a
report at reports/update-processing-round-3-closeout.md.

## Items

1. SOLR-7022 at db357868610b92709335129ebada4646ecf11e26:
   RELEASED. Update the held draft's Proof per material
   item 1 (gate at 6a233ab2fdb with counts; the two wording
   deltas named).
2. SOLR-16655 at 5e2317443f4116a9e97330b0d6ce175cf44b54df
   and SOLR-12705 at 8624b7c3238b: state the landing order
   from material item 2 in both drafts (16655 first, 12705
   rebases), replacing the current no-order wording.
3. SOLR-12245 at f325d5d0576e582d4488570eabdeabc7410a8a0f:
   name testDistribErrorMessageNamesTheHostOnce as the
   fail-before test in the Proof (material item 3).
4. SOLR-11475 at 0de48e492fd49a15f406b2a5500c337f92ef3d4d:
   add the corrected PeerSync count wording from material
   item 7.
5. SOLR-13696 at 1d0b8a0a73cd10f4968f57985af93b450c484aca:
   NEW draft from material item 9, with both Choices and
   the Limits lines as specified there.
6. No change: SOLR-6045 (length accepted, material item 4),
   SOLR-16673 (Limits line kept, item 5), SOLR-5941 (list
   confirmed, item 6). Verify the drafts already match and
   say so in the report; edit nothing on those three unless
   a draft contradicts the material.

## Joins when ready

SOLR-13943: its draft joins this close-out when a further
material addendum names the stacked head and its gate
receipt. If the addendum has not landed when the rest of
this close-out is done, report 13943 as waiting rather than
holding the close-out.
