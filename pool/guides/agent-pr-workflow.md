# PR workflow for factory agents

This is the agent-facing version of the PR workflow. It is not sent to
human contributors. It states the git commands for the whole workflow
plus the factory rules that bind every step. A human contributor's
version would differ: agents work in fork worktrees under the queue
lock, commit as Nick Shanin, and never open a PR, flip a draft to
ready, or post a comment without the owner's explicit go.

## 1. Worktree setup

Agents do not clone. Use a worktree of the fork checkout, on the
branch the assignment names:

```sh
git -C ~/workspace/solr fetch fork
git -C ~/workspace/solr worktree add ~/workspace/solr-worktrees/<name> fork/<branch>
cd ~/workspace/solr-worktrees/<name>
```

Two files are gitignored and do not travel with the worktree. Copy
both or every Gradle invocation fails:

```sh
cp ~/workspace/solr/gradle.properties .
mkdir -p gradle/wrapper
cp ~/workspace/solr/gradle/wrapper/gradle-wrapper.jar gradle/wrapper/
```

Never run `./gradlew` anywhere on this VM. Use
`~/workspace/tools/solr-gradle.sh`, which sets the vendored JDK 21 and
Gradle 9.7.0 and routes through the local proxy relay. Never run two
Gradle builds at once: every build goes under
`~/workspace/tools/.solr-test-queue.lock`, and Gradle children must
close fd 9. Launch gate runners detached with `setsid nohup` so they
survive the launching shell.

## 2. Start a branch for one ticket

Branch from a fresh upstream main, never from local main, and never
commit directly on main:

```sh
git fetch upstream
git checkout -b solr-XXXXX-short-description upstream/main
```

One branch per ticket. For a backport, branch from the release branch
(`upstream/branch_10_1` or the relevant branch) and say so in the PR
draft. Branch names start with the ticket, then a short slug.

## 3. Make the change, commit it

Work in small, reviewable commits. Every commit message starts with
the ticket number and an imperative summary. Authorship is always
Nick Shanin, with no "Claude" in authors, committers, or trailers:

```sh
git status
git add <files you changed>
git -c user.name='Nick Shanin' -c user.email='nick.boss.us@gmail.com' commit -m "SOLR-XXXXX: Fix the thing in one sentence"
```

Rules that save review rounds:

- One logical change per commit. A refactor and a behavior change are
  two commits.
- Never commit generated files, build output, or IDE settings.
- Add a changelog fragment under `changelog/unreleased/`. Copy an
  existing fragment and adapt it; if the title contains a colon
  followed by a space, quote the whole title, or the changelog
  validation workflow fails on YAML parsing.
- New tests go in the same commit as the fix they cover, or the commit
  right before it if the test is meant to fail first.
- New tests extend `SolrTestCase` or `SolrCloudTestCase`, never
  `SolrTestCaseJ4` as a base.

Before committing, run tidy on the tree and review its diff. Tidy
prunes unused version-catalog entries as a side effect; restore
`gradle/libs.versions.toml` with `git checkout --` if tidy touched it
and the change is not yours. Tidy output is formatting only: if a
tidy diff touches anything beyond formatting, stop and surface it.

## 4. Gate the branch

A branch is not finished until the full hardening gate is green at
its exact head, and the gate's module check is sufficient (the owner
ruled the full `gradle check` is not required). The gate runner is
`~/workspace/tools/harden-branch.sh`. Gate evidence stands only on
the run's log file and fresh JUnit XML on disk. Never count failures
from a gate log without separating the pre-fix proof step from the
head run.

Verify Error Prone locally, since CI runs it with `-Werror` and local
builds skip it by default:

```sh
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Record the result in the branch receipt (`receipts/SOLR-XXXXX.md`
on the board). The PR description draft is the receipt's final
section, written when the gate goes green, so any host can open the
PR from the record alone.

## 5. Push the branch to the fork

```sh
git push -u origin solr-XXXXX-short-description
```

The `-u` sets the upstream tracking once; later pushes are just
`git push`. Pushes to Nick's own open PRs and branches are always
authorized. If Muse patches it, Muse pushes it, with
`--force-with-lease` where a rebase requires it.

## 6. Open the pull request: only on the owner's go

Openings need the owner's explicit approval each time, and so do
draft-to-ready flips and new comment posts. Approving a set of
recommendations does not approve posting a drafted comment presented
alongside them. Edits to Nick's own PR descriptions and comments
carry standing authorization, with one exception: a maintainer
actively commenting on that PR means leave the description alone and
check with Nick first.

When the go is given, open the PR against apache/solr `main`
(or the release branch for a backport) as a draft, with a description
written under `pr-formula.md`, in this order:

1. What the change does, in plain language.
2. Why it is needed, with the ticket link.
3. How it was tested, with the test class names, the head SHA, and
   the result, including that the new test fails without the fix.
4. Limits: anything the change deliberately does not do.
5. A choice to check, only when a real decision exists between
   defensible behaviors.

Public PR text carries no internal process vocabulary: no gate,
receipt, ledger, "fresh JUnit XML", "rc=0", or "pre-fix proof" as a
label. Proof sections state plainly what ran, at which head, what
passed, and that the new test fails without the fix.

## 7. Handle review

Reviewers comment on lines, on the PR, or with a formal review. For
each round, add fixup commits on top; do not rewrite history while a
review is in progress, so the reviewer can see what changed since
their last read:

```sh
git status
git add <files you changed>
git -c user.name='Nick Shanin' -c user.email='nick.boss.us@gmail.com' commit -m "SOLR-XXXXX: Address review: <what you changed>"
git push
```

Answer each comment thread, even if the answer is just that the fix
is in the latest commit. If a maintainer asks for a change, implement
it at the scope written; bring any objection to Nick first with the
design and the cost, and never present a preference as a constraint.
Review replies are brief, self-critical, and honest, and they follow
`comment-formula.md`: the GitHub header for comments longer than one
sentence, no "we" for Nick plus the agent, no internal process
vocabulary.

## 8. Stay current with main

While the PR is open, main moves. Rebase onto the latest main,
resolve conflicts, and push:

```sh
git fetch upstream
git rebase upstream/main
git push --force-with-lease
```

`--force-with-lease` is the only force push ever used, and only on
Nick's own PR branch. It refuses to run if someone else pushed to the
branch since the last fetch. Never force push to main, to a release
branch, or to anyone else's branch. Never rewrite a branch's history
for cosmetic commit-message defects. If a rebase goes badly,
`git rebase --abort` returns to where you started.

## 9. Approval freeze

Once a maintainer approves a PR, push nothing to it without the
owner's explicit go, even a change the reviewer suggested. An
approval freezes the tree in the approver's mind; a new commit after
approval tells them the target moved and resets whatever validation
they had planned against the approved head. The reviewer's own
suggestion can wait a day; the approval cannot be un-spent.

## 10. Finish

Most Solr committers squash-merge, so do not squash a branch
yourself unless the reviewer asks. If they do ask:

```sh
git rebase -i upstream/main
```

Mark every commit after the first as `squash`, save, edit the
combined message to `SOLR-XXXXX: <final summary>`, then
`git push --force-with-lease`.

After the PR merges, clean up:

```sh
git checkout main
git pull upstream main
git branch -d solr-XXXXX-short-description
git push origin --delete solr-XXXXX-short-description
git remote prune origin
```

Retiring a branch means labeling it retired in the records and
leaving the branch in place, never deleting it silently.

## 11. Common mistakes

- Pushing to `upstream` instead of `origin`. Check `git remote -v`
  and push to `origin`.
- Committing on main and then branching. Branch first, then commit.
- Amending a commit after review started. Add a new commit instead.
- Force pushing without `--lease`. A plain `--force` can discard
  someone else's push.
- Opening a PR from main. Always use a topic branch.
- Opening a PR, flipping a draft to ready, or posting a comment
  without the owner's explicit go for that act.
- Counting gate-log failures without separating the pre-fix proof
  step from the head run.
- Trusting a tool output over the log file and fresh JUnit XML on
  disk. Tool outputs can carry fabricated text; the log and the XML
  are the ground truth.
- Writing a handoff or assignment that mentions email. Only the
  phone agent has email access; write handoffs as plain instructions.
