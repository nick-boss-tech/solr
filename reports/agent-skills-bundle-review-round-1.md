# Independent Solr skill foundation review, round 1

Status: STATIC AND STRUCTURAL REVIEW COMPLETE; BEHAVIORAL EXECUTION AND WINDOWS LOADING NOT VERIFIED. Package is not recommended for installation until the blocking compatibility and validator-scope issues below are repaired.

## Reviewed inputs and method

- Package: `nick-boss-tech/solr:factory-solr-skills-round-1`, exact commit `b30a0a77df5f3ae10191c814256245319f56cdd7`.
- Reviewed all six `SKILL.md` files, their bundled references/scripts/UI metadata, `package.json`, `handbook/INDEX.md`, `evaluation/REVIEW.md`, `evaluation/scenarios.json`, and the pinned implementation handoff.
- Read the six corresponding per-skill assignments, the live `pr-formula.md` and `comment-formula.md` from board snapshot `ed07cb930649addb4799e8e6c9de5bedc52ac406`, plus `START-HERE.md` and `WORKFLOW.md` for current authority and posting rules.
- Structural checks were run from a package-only export at the pinned commit using Python 3.12.3, PyYAML 6.0.3, and the local skill-creator validator. All six skills validated, 23 relative links passed, the monthly output reproduced byte-for-byte, population reconciled to 603, and all six monthly-index unit tests passed.
- I also ran the prescribed validator against a full-repository export and ran the monthly-index CLI under CPython 3.9.25. Both exposed reproducible defects described below.
- The scenario acceptance notes in `evaluation/scenarios.json` were available to me, so the scenario traces below are not blinded. This session has no Windows device and the package is not installed as active skills; I did not make live skill-loaded model calls. No scenario is claimed as a behavioral execution pass.
- No Solr Gradle build, package installation, scheduled job, gateway/API call, public comment, or source/board edit was performed during the review. The only board change requested is this add-only report.

## Findings requiring repair

### F1 — Major: the stated Python 3.9 minimum is not supported

`evaluation/REVIEW.md` line 7 says Python 3.9 or later. But `.agents/skills/solr-pr-map/scripts/monthly_index.py` line 182 calls `Path.write_text(..., newline='\n')`; the `newline` parameter was added to `Path.write_text` in Python 3.10 ([Python pathlib docs](https://docs.python.org/3/library/pathlib.html#pathlib.Path.write_text)). I installed CPython 3.9.25 in an isolated directory and ran the packaged snapshot command. It exited 1 before writing any output:

```text
TypeError: write_text() got an unexpected keyword argument 'newline'
```

`evaluation/validate_package.py` repeats the same unsupported argument at lines 56 and 73. This breaks the main monthly-index writer and the review harness on an advertised runtime. Minimal repair: use `Path.open('w', encoding='utf-8', newline='\n')` (or equivalent) in both scripts and add a Python 3.9 regression run. Alternatively, raise the documented minimum to 3.10 and test/declare that narrower support.

### F2 — Major: the link validator rejects unrelated repository Markdown

`evaluation/validate_package.py` sets the package root to the repository root at line 17, then recursively checks every `*.md` file below it at lines 35–42. On the full repository export at this exact commit, the prescribed command failed on the unrelated historical file `reports/bats-assertion-hardening-finish-round-1-phone-agent-2-supplement-2026-10-27.md`, whose log locator points outside the repository to `/workspace/gates/...`. The resulting error was `Broken/escaping package link`; it is unrelated to the 34-file skill/handbook/evaluation payload. The same validator passes when run from a package-only export, which is how I confirmed its package-specific checks.

Minimal repair: restrict link traversal to the package-owned `.agents/skills`, `handbook`, and `evaluation` Markdown files (or another explicit manifest), while still rejecting escaping links within those files. Then verify both the full-repository command and the package-only export.

### F3 — Minor: five review locators retain Windows separators

`handbook/months/snapshot.json` contains backslash-separated `source_review_locator` values for PRs 4594, 4596, 4597, 4655 and 4657 (for example, lines 308–309). They are described as locators into the scan project, but POSIX tooling will not treat those strings as normal relative paths. Normalize imported locators to `/` and add a fixture for Windows-style input. The generated Markdown currently omits these locators, so this is an operability issue rather than a false coverage count.

## Per-skill verdicts

These are instruction-quality verdicts from source review and scenario tracing, not runtime behavior verdicts.

| Skill | Verdict | Evidence and limits |
| --- | --- | --- |
| `solr-navigate` | PASS — static | `.agents/skills/solr-navigate/SKILL.md` requires a pinned target revision, applicable workspace instructions, bounded source tracing, nearest callers/tests and explicit gaps. Its source map labels entries as search pointers rather than verified architecture. It correctly limits navigation requests from broad archaeology or patches. Scenario traces for an unknown subsystem and a title-only architecture claim are covered by these instructions. |
| `solr-pr-map` | NEEDS REPAIR | The grouping contract is careful: merged records use UTC `mergedAt`; open/draft backlog uses UTC `createdAt`; stale heads and unmatched scan rows remain visible; output distinguishes source-review coverage from runtime proof. The 3.9 runtime defect in F1 blocks the advertised compatibility range. The five locator separators in F3 are a small data portability issue. |
| `solr-review` | PASS — static | The skill and worksheet separate correctness, architecture, reuse, compatibility/security and verification; require exact head/base and discussion/inline-thread refresh; and distinguish source evidence, hypothesis and executed proof. It warns that old green receipts do not cover moved heads and that review does not authorize edits. |
| `solr-proof` | PASS — static | It explicitly separates validator rejection, runtime assertion, mutation evidence, setup errors and coverage pins; requires exact pushed heads/required legs; and says changed code needs a new pushed head and superseding record. This supports the YAML-failure-before and compile-failure mutation scenarios. |
| `solr-contribute` | PASS — static | It fetches the current formula rather than copying an old one, requires actual issue/diff/proof inputs, preserves live session authority and disclosure, and forbids inventing proof or claiming Nick reviewed wording when he did not. The live formula confirms the skill’s instructions to draft first and not publish without applicable authorization. |
| `solr-maintain` | PASS — static | It refreshes PR state, heads, discussion, CI SHA and prior actions; explicitly recognizes incorporation, merge and moved-head changes; and directs workers to avoid duplicate offers. Its automation reference preserves the last good cursor on partial failure and includes check identity for same-head evidence. |

## Scenario review

I traced each supplied case through the relevant skill and reference. “Covered” means the instruction text specifies the required decision; it does not mean an independent model executed the case.

| Supplied case | Review trace / instruction coverage |
| --- | --- |
| `navigate-unknown` | Covered: pin the requested revision, use source-map entrypoints and nearest tests, return a brief with gaps, stop before unrelated scans. |
| `map-boundary` | Covered in the monthly contract and verified by script tests: merge month comes from UTC `mergedAt`; open/draft backlog uses UTC `createdAt`; stale scan heads are not current review coverage. |
| `review-partial` | Covered: refresh the current head and threads; a receipt on the previous head remains historical; list the actual assessed scope and unassessed remainder. |
| `proof-validator` | Covered: require validator failure-before and patched validation; do not manufacture a Python assertion failure or run Solr Gradle on a Windows host. |
| `proof-coverage` | Covered: a coverage pin is distinct from mutation proof; compile/setup failure does not establish the intended runtime assertion. |
| `contribute-no-proof` | Covered: use a provisional draft/handoff, do not copy stale counts or claim Nick reviewed it, and name the missing evidence. |
| `contribute-authority` | Covered by current-session authority and the live formula: a draft/review hold is not released by standing publication authority. |
| `maintain-merged` | Covered: reconcile the merged diff and current main before proposing follow-up commands. |
| `maintain-incorporated` | Covered: refresh discussion and prior actions; record incorporation and avoid a duplicate offer. |
| `automation-partial` | Covered by the automation contract: retain the last successful cursor, deduplicate unchanged events, include late check identity, and leave deployment pending cadence/host/destination choices. |

Additional unseen cases traced: (1) a navigation request based only on a PR title routes to source at the requested revision; (2) an unmatched scan-register row is surfaced rather than counted as reviewed; (3) a review with no fresh XML stays unproven even if source assertions look plausible; (4) a test startup error is recorded as setup failure, not fail-before proof; (5) a request to draft a public offer while holding publication for Nick remains a draft; and (6) a partial maintenance collection cannot advance the stored cursor. In each case the relevant instructions point to the safe decision, but these are reviewer traces, not executed agent outputs.

## Installation and follow-up recommendation

Do not install this package broadly yet. First fix F1 and F2, normalize the five locators in F3, and rerun the package validator in both a full repository checkout and a package-only export on Python 3.9 and 3.12. Then run the supplied scenarios and at least one fresh case per skill with a genuinely skill-loaded evaluator on Linux and Windows. This review did not prove Windows loading or behavioral acceptance. The package contains the expected repository-scoped `.agents/skills/<name>/SKILL.md` layout and no gateway/API-key dependency, but the instructions explicitly defer installation and scheduling; keep those decisions pending until the repairs and behavioral review are complete.