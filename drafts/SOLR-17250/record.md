# Skip record: SOLR-17250

- Ticket: SOLR-17250, "Release wizard needs to set user/email for repo it checks out". JIRA status Open, resolution none, created 2024-04-23, last updated 2024-04-28, checked 2026-10-19 UTC.
- Verdict: pr-needed.
- Draft branch: solr-17250-draft-pr.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

The release wizard assumes the release manager has a global git user name and email. The reporter wants the wizard to ask for the identity and set it locally in the checkout it uses. Comments: janhoy (2024-04-23) suggests the wizard prompt for the email in its first section, and points out that it could assume <asfid@apache.org>; gus (2024-04-27) reports the failure when committing the Dockerfile after a vote ("Author identity unknown"), suggests the @apache.org address and the public name from the Whimsy profile, and reports the same failure at the website step.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `dev-tools/scripts/releaseWizard.yaml`: sixteen steps run `git commit` with no identity set (for example lines 456, 484, 509, 537, 695, 1176, 1429, 1456, 1485, 1512, 1609, 1743, 1757, 1782). None of them passes `-c user.name` or `-c user.email`, and no step runs `git config`.
- `dev-tools/scripts/releaseWizard.py`: no call to `git config` and no identity prompt. The only identity handling is the GPG key check (`CommitterPgp`, near line 1201), which checks the signing key and does not set git's author.
- `git grep -n "user.email\|user.name\|git config"` over `dev-tools`: the only match is `cherrypick.sh:202` (a read of `branch.<name>.remote`). No identity setup exists in the dev tools.

So a release manager with no global git identity hits git's "Author identity unknown" error at the first wizard commit. The code path is the `git commit` steps above with no identity.

## History

- The YAML steps and the wizard were not traced for the commit that added the first `git commit` step; the ticket does not depend on it.
- No commit on the fork names SOLR-17250 (empty `git log --grep` at the pinned head).

## Reproduction attempts

Not attempted. A reproduction would run a wizard commit step with a HOME that has no git identity (for example `HOME=<empty dir>` and no `/etc` identity) and see the "Author identity unknown" error. The code path argument is enough for the verdict: the commit steps have no identity, and the git behavior on a machine with no identity is the reported error.

## Gap analysis

The reported failure follows from the commit steps. The fix has a design choice that the ticket's comments leave open: prompt for the address in the first section, or assume `<asfid>@apache.org`, and whether to use the Whimsy public name. This record makes that choice (below), so the change does not wait on it.

## Verdict justification

pr-needed: "the code path shows it, and a change is needed." Minimum evidence: the commit steps with line numbers in `releaseWizard.yaml`, the absence of any identity setup in `releaseWizard.py` and the dev tools, and the change the PR would make. The PR's check: a dry run of one commit step under an empty HOME passes after the change and fails before it.

Recorded choice (rung 4): the wizard asks once, in its first section, for the release manager's Apache email address and public name, with the Apache address as the default suggestion (`<asfid>@apache.org`), and sets them with `git config --local` in the checkout it works in before any commit step. Reason: janhoy's and gus's comments both point at the Apache address and the public name; setting the identity per checkout (not globally) matches the ticket's concern about people who use other projects.

## Limits

This record does not run the wizard. It does not check the Whimsy profile lookup that gus suggests. It does not check the website or docker repositories' own identity settings.
