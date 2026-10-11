# Using the GitHub token from an agent (Windows review host)

For any agent that needs GitHub access on the Windows review host. Short version: **do not log in to gh. Use the wrapper.**

## Do not run `gh auth login`

`gh auth login` stores a credential in the GitHub CLI's own configuration. That is not needed here, and it is not allowed without Nick's direct go-ahead. If an interactive login seems to be the only way forward, stop and ask Nick. Nick can run it himself with the `!` prefix in the session.

## Use the wrapper for every gh call

From the workspace root, `C:\Users\shaninna\dev\Solr-issues`, run gh through the wrapper as a plain command:

```
.\research\gh.ps1 pr view <number> --repo apache/solr --json headRefOid,mergeable,mergeStateStatus
.\research\gh.ps1 pr checks <number> --repo apache/solr
.\research\gh.ps1 run list --repo apache/solr --limit 5
```

What the wrapper does: if the environment has no `GH_TOKEN`, it reads the token file once, keeps the value in that one process, and passes your arguments to gh. Nothing is written to disk, nothing is printed, and nothing is stored in gh's configuration.

Quote a JSON field list in single quotes if a comma-separated list fails to parse.

## Check that the token works

Use a call that returns only the login name:

```
.\research\gh.ps1 api user --jq .login
```

It should print the account name. If it prints an error about authentication (401, "bad credentials", "not logged in"), stop and report it to Nick. Do not try another token, do not paste anything into a command, and do not work around the error.

## Git pushes to the fork

Do not run `gh auth setup-git` yourself. Use one of the wrappers, which run it for you:

- For the `pr-prepare` branch: `.\research\push-pr-prepare.ps1 -Worktree <worktree path>`
- For a ticket worktree: `.\research\branch-work.ps1 push <ticket>`

## What is allowed without asking

Read-only gh calls: `pr view`, `pr checks`, `pr list`, `run list`, `run view`, `api` GETs, and `api user`.

## What needs Nick's explicit go-ahead first

Anything that changes state on GitHub: `pr comment`, `pr create`, `pr edit`, `pr close`, `pr reopen`, `pr review`, branch deletion, and any force push. Draft the text locally first, and post only after Nick authorizes that exact action.

## Rules

- Never echo the token, put it in a URL, commit it, paste it into a report, or write it to a file.
- Never open the token file to read it.
- If a command fails, report the exact error to Nick. Do not retry with other credentials.
- Other hosts (VM1, VM2) have their own wrapper paths. Ask Nick before using this doc on one of them.
