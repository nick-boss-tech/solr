# Decision ladder (proposed revision, 2026-10-18)

Status: IN FORCE (approved by Nick, 2026-10-18T21:15:06ZZ). Written from the gate and review records of 2026-10-18 (SOLR-3243, SOLR-14913, SOLR-18549, SOLR-4841). `pool/cron-prompt.md` step 7 points here.

The ladder is for a decision inside a claim: a choice the claimant could make without the owner. Go down the rungs in order, and stop at the first rung that settles the question. Record the rung used in the claim.

## Rung 1: read the code and its history

- Read the current code, the code before the behavior changed, and the commit that changed it (`git log -S`, `git blame`, `git show <parent>:<file>`). Cite file, line and commit.
- Read the tests and specs that depend on the behavior. A test that pins a status code is evidence of a contract only if it was written to pin it, not if it came from the same change.
- Stops here when the history shows a regression, an accidental side effect, or a clear pre-existing contract.
- Example (SOLR-18549, 500 to 400): `c74142675e` (SOLR-16458) wrapped every exception. Before it, the endpoint returned 400 for the same case, and v1 still does. Settled at rung 1.

## Rung 2: run a probe or a mutation

- Only when rung 1 leaves two explanations open. Choose the variant that separates them, run it, and record the result in the claim.
- A new assertion is not counted as a proof of anything until a run shows it failing on a mutation or base. If no run does, say so in the receipt and label the assertion as a pin (see the lessons below).
- Example (SOLR-4841): removing the detection call makes the first assertion fail with `expected:<[sv]> but was:<[]>`. The assertion discriminates.

## Rung 3: the stated intent

- Read the ticket, the PR body's stated intent, and the owner's written decisions in the assignment. If the intent settles it, follow it and say in the claim which sentence settled it.
- Example (SOLR-18549, standalone `nodes=all`): the ticket's expected behavior says "answered by the node itself" for the three broadcast endpoints and 400 for `/admin/metrics`.

## Rung 4: a genuine choice between defensible behaviors

- Pick the option with the strongest evidence from rungs 1 to 3. Record the options, the one chosen, and the reasons with citations in the claim and in the deliverable.
- The choice goes into the PR body as a stated behavior, with its reason. It does not go into the PR body as a question if the choice is already settled inside the pool.
- Keep a question for the maintainer only when the choice is open for the maintainer: a public contract or a choice that the pool cannot settle from the evidence. Put it in the PR body under the Choice section, and say why it is open.
- A decision that changes the input of a gate (a new proof base, a new head) is recorded in the claim before the run. The run is then a new input, not a repeat.

## Rung 5: the owner

- Only when rung 4 cannot be settled inside the pool: the intent is ambiguous, the change widens the approved scope, or the decision commits Nick to something public (a JIRA post, a PR opening, a merge).
- Set the item to `NEEDS-OWNER` with one question. Do not put it in an OPEN item.

## Lessons from 2026-10-18 that the ladder now covers

- **A proof base can be synthetic** when the head tests call a new API (SOLR-18549: base = apache main plus the hook declaration returning the default). Say so in the job and the receipt.
- **A proof that passes is not a proof.** SOLR-14913: the first base removed a block the new assertion did not depend on, and the run passed 40 of 40. The compound assertion stayed unproven, and the receipt says so. A new assertion must be shown to fail on a mutation, or be labelled a pin.
- **Accepting an amended failure criterion needs a written reason and no rerun.** SOLR-3243: the three extra failures were the widening tests, which the base lacks by construction. The run was accepted under the amended criterion. It was not repeated.
- **Settled choices do not become PR questions.** SOLR-18549 PR draft: the 400 status question was settled at rung 4 and removed from the PR body. Only the open maintainer question stays.
- **A HANDED item is not a host restriction.** It goes to any pool Linux host with the item's tags once the decision it waits for is recorded in the item.
