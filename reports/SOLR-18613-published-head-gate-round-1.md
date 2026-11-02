# Report: SOLR-18613 published-head gate, round 1 (phone agent)

Status: DONE, GATE GREEN at the published head. Full record:
`receipts/SOLR-18613.md`.

- Job: `gates/SOLR-18613-published-head-round-1.md`. Claim:
  `claims/SOLR-18613-published-head-gate-phone-agent.md`, now DONE.
- Head verified before, during, and after: `c2d2c6259462a7e848008b065d65df69a1b82ebb`,
  tree `4d09e238ca0a7890e125b5f36d2dd78ff90f7110`, in a full clone.
- Results: tidy clean; focused LB suites 47 tests, 0 failures, 0 errors,
  0 skipped at seed 5B407A2D9C1310E7; root `check -x test` BUILD SUCCESSFUL
  in the clone. The four-method negative proof was reconciled and carried
  forward under the job's rule (ancestor plus whitespace-token equality of
  both test files against the head vm2 discriminated), not rerun at the
  published SHA; the receipt states that limit plainly, including that
  vm2's raw logs were not re-read from this host.
- The receipt ends with a PR description draft. No PR, public comment, or
  Jira edit was made. Remaining before any opening, at Nick's direction:
  a changelog fragment commit for SOLR-18613 (which moves the head and
  needs its own fresh gate, per the job), then the opening decision.
