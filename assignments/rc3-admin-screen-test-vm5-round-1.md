# Assignment: RC3 admin screen test on vm5, round 1

Status: OPEN
Intended host: vm5 (directed assignment, posted at the owner's direction, 2026-10-26 evening)
Capability tags: premise-run, records

## Background

On the Solr 10.1.0 RC3 vote thread, Joel Bernstein reported an admin screen error and Jan Høydahl answered that he could not reproduce it and suggested trying again on another machine or another browser. Joel later reported the problem no longer occurs for him, with no cause found. This assignment is the other-machine check, on a host whose environment differs from both of theirs: vm5 is aarch64 Linux and ran the RC3 smoke loop on JDK 21.0.12.1.

Joel's report, verbatim steps: unpack the tar.gz, enter the 10.1.0 directory, run `bin/solr start`, open a browser to localhost:8983/solr. The admin screen showed the error: "Your user does not have the necessary role(s) to perform this action." The same procedure on 10.0.0 did not show the error.

## Task

Use the official RC3 artifacts: https://dist.apache.org/repos/dist/dev/solr/solr-10.1.0-RC3-rev-749712b71940c9f96d824c533f32f5c56dcd3924

1. Unpack the RC3 tarball, start Solr standalone with `bin/solr start` on the stock configuration. No authentication setup, no configuration changes.
2. Open http://localhost:8983/solr in a browser or headless browser if the host has one. Record whether the role error above renders, whether the page and its assets load, and any console or network errors. If the host has no browser tooling, run the HTTP-level equivalent instead: fetch the admin page, every asset it references, and the API calls the UI makes on load, recording the status of each, and state plainly in the report that the check ran at HTTP level only.
3. Control: repeat the same procedure against the 10.0.0 release artifacts, because the original report is comparative.
4. Record the environment exactly: OS and architecture, JDK version, artifact revision, commands run, and the time each step ran.

## Deliverable

`reports/rc3-admin-screen-test-vm5-round-1.md` with one verdict, stated as it lands: reproduced, not reproduced at browser level, or not reproduced at HTTP level only. A not-reproduced result is a complete deliverable. Include the per-step evidence the verdict rests on, including any exceptions in solr.log.

## Timing

The RC3 vote closes 2026-10-27 11:00 UTC. A result before then is worth more; accuracy over speed, and no partial or interim posts.

## Limits

- No post to the dev list, no Jira comment, no GitHub comment. The owner decides what, if anything, is said publicly with the result.
- This is the admin screen test only. The RC3 smoke loop stays stood down; do not start new smoke runs.
- Add files only; do not edit this assignment.
