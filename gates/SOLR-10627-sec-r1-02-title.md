# Gate job: SOLR-10627 SEC-R1-02 changelog title (landing and parse)

- Branch: solr-10627-submit. Gated head: 5a15dc0ba22083a294524210b3be007d870daa35 (origin/solr-10627-submit, receipts/SOLR-10627.md). The gated fragment still carries the round 1 title ("...set-permission command now rejects ... which previously protected nothing.").
- Land from origin/solr-10627-submit (5a15dc0ba22). The local refs/heads/solr-10627-submit is an older tip (ce126ca60e7) and is not the base for this job.
- Patch: gates/SOLR-10627-sec-r1-02-title.patch. It replaces the `title:` block of changelog/unreleased/SOLR-10627-reject-null-collection-permission.yml with the replacement text recorded in reports/security-convergence-round-1-response.md (SEC-R1-02) and reports/security-round-1-s1.md (item 3). It dry-runs clean with `patch -p1` against the gated fragment.
Status: DONE for the landing and parse (vm2, 2026-10-16). New head 465e4b02e8 on solr-10627-submit. The draft head-link move (step 4) is owed to the main agent.
- Capability tags: gate (parse only).

## Owed steps, in order

1. Landing (branch owner, not the security review lane): apply the patch to solr-10627-submit as one commit on top of 5a15dc0ba22, and push it as a plain fast-forward. No history rewrite.
2. Parse: the changelog YAML parse of changelog/unreleased/SOLR-10627-reject-null-collection-permission.yml at the new head. Pass means the file parses and `title` reads as the replacement text.
3. Receipt: append the new head and the parse result to receipts/SOLR-10627.md. No tidy, Error Prone or test run is owed, because the change touches only one YAML title. The 18 of 18 counts stay as recorded for 5a15dc0ba22.
4. Draft (wording, main side, after step 1): move the head links in pr-drafts/security/SOLR-10627.md from 5a15dc0ba22083a294524210b3be007d870daa35 to the new head. The line ranges stay as they are, because no Java file changes.

## Hold

SEC-R1-02 stays HOLD until step 2 passes at the landed head.


## Landing and parse, vm2, 2026-10-16T00:40:36Z

- Step 1: patch gates/SOLR-10627-sec-r1-02-title.patch applied (git apply -p1, dry run clean at 5a15dc0ba22) as one commit 465e4b02e8 on solr-10627-submit, fast-forward from 5a15dc0ba220, author Nick Shanin, no trailer. The diff is the changelog fragment only (3 insertions, 2 deletions).
- Step 2: changelog YAML parse with pyyaml: OK. The title now reads the replacement text (starts 'The authorization set-permission and update-permission commands now reject ...').
- Step 3: receipts/SOLR-10627.md carries a section for the new head 465e4b02e8. No tidy, Error Prone or test run was owed.
- Step 4 (draft head links) is main-side wording work and is owed to the main agent. Not done here.
