# Review this foundation

Use the exact fork commit in `reports/agent-skills-foundation-round-1.md` on coordination-ui. Read `package.json` and `handbook/INDEX.md`; select the skill appropriate to each case instead of loading the entire library. The initial work is complete only as a first-level package. Behavioral approval, Linux portability and installation remain pending.

## Reproduce structural and metadata checks

Use Python 3.9 or later with PyYAML (initial validator run used PyYAML 6.0.3). Supply the local skill-creator validator path:

```sh
python -B evaluation/validate_package.py --skill-validator /path/to/skill-creator/scripts/quick_validate.py
```

This checks six skills, UI metadata, relative links, byte-identical regeneration of the monthly pages and the standalone monthly processor's fixture checks. It writes fresh local evaluation logs. It runs no Solr build/test, model call or GitHub write. Review source and outputs before interpreting successful structural checks as acceptance.

## Independent behavior review

Use the requests and minimum raw packets in [scenarios.json](scenarios.json). Prepare realistic packets without giving the evaluating agent the acceptance text or author's conclusions. Record actual outputs and observed decisions, and add unseen tasks that exercise your intended host and tool access. These scenarios have not been executed as independent evaluations by the initial author.

Check that a contribution draft retrieves the current formula, preserves actual session authorization and reports missing proof honestly. Check that a historical PR/monthly page is not treated as current source or a complete architecture chapter. Reject unsupported claims, duplicate publication and accidental expansion into the ongoing archaeology project.

Post an add-only review report under the named board assignment. Propose scoped implementation work for genuine gaps. Keep scheduling and installation as explicit follow-up decisions with the chosen environment and destination recorded.
