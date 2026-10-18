# Skip-queue verdict rubric

Every skip ticket that gets a record ends with one verdict. The verdict decides the draft branch name: `solr-NNNN-draft-<status>`, where status is `close`, `pr`, or `open`.

| Verdict | Branch status | Use when |
| --- | --- | --- |
| close-fixed | close | A commit on main fixes the reported problem, and the fixed code is still on main. |
| close-obsolete | close | The feature, class or code the ticket is about has been removed, and a search of main finds no remaining use. |
| close-cannot-reproduce | close | Reproduction was attempted on the pinned head and did not show the problem, and the code path argument also rules it out. |
| close-answered | close | A support question that the reference guide or docs answer. Link the page. |
| pr-needed | pr | The problem reproduces on the pinned head, or the code path shows it, and a change is needed. The draft says we reproduced it. |
| keep-open | open | Evidence is inconclusive after the attempts listed in the record. The record says what is missing. |

## Minimum evidence per verdict

- Every verdict: the pinned head sha, the JIRA status and date checked, and the code paths read with file and line.
- close-fixed: the fixing commit sha, a check that it is an ancestor of the pinned head, and a check that the changed code is still present at the head.
- close-obsolete: the grep command and its empty result, run at the pinned head.
- close-cannot-reproduce: at least one reproduction attempt with its command, seed or input, result, and log path. The record states what the attempt does not cover. A code path argument alone is not enough.
- close-answered: the docs link and the sentence that answers the question.
- pr-needed: the reproduction (command, seed, failing output, log path) or the code-path argument with the line that causes the failure. Name the test or check the PR would add.
- keep-open: the attempts made and the missing piece (a repro input, a second environment, a fix commit that needs checking).

## Rules for every record

- Pin one head. Cite the fork's origin/main sha. If you cite upstream history, name the upstream commit as well.
- Reproduction is conclusive only if the same command was run twice with the same result, or the result matches the ticket's stated symptom exactly. Otherwise record it as inconclusive.
- When a reproduction is inconclusive, read the code and git history for the gap. Try one different variant that separates the explanations, then record it.
- Public text: the JIRA comment is plain language. No internal process words (claims, assignments, pool, host labels, agent names). No host names.
- Nothing is posted to JIRA from a record. The comment is a draft until someone posts it by hand.
