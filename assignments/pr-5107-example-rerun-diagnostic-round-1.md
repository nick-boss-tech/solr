# Assignment: PR 5107 example start sequences, non-root diagnostic round 1

Status: OPEN.

## Why

On apache/solr PR 5107 (SOLR-8442), Eric Pugh replied to the round 3
handoff comment (PR comment 6585608919, 2026-11-02) that he has no
good sense of whether the examples behave when run twice. His stated
plan is to make a repeated start of the same example explicitly step
over the previous run, and he asked whether the techproducts-then-films
sequence works at all, expecting possible weirdness because he has
never tested that flow. Nick's direction: let the reply sit until
tests are run here and on the pool side. The phone agent is running a
root diagnostic (with `--force`, diagnostic only) on its host; this
assignment is the clean non-root run of the same sequences.

## Scope

Diagnostic only. No code changes, no fixes, no public posts or
comments. If a sequence misbehaves, record the behavior; do not repair
it under this assignment.

- Host requirement: a Linux host where `bin/solr` runs as a non-root
  account (uid nonzero, recorded in the report). Do not use `--force`.
- Checkout: fork branch `solr-8442-security-review-r2-submit` at
  `e75405eff845eda0e0f4f252d447347e2206acbe` (F3), verified by
  ls-remote. Build the dev distribution (`:solr:packaging:dev`) from
  that head.
- Sequences, on one fresh dev install, in order, stopping the node
  between starts:
  1. `bin/solr start -e techproducts`; verify the node answers and
     record the techproducts document count.
  2. `bin/solr start -e films`; verify and record the films count and
     whether techproducts still answers.
  3. `bin/solr start -e techproducts` again on the same install;
     record exit behavior, any warnings or errors, and the count
     compared with step 1.
  4. On the same install (or a second fresh one if state interferes;
     say which): `bin/solr start -e secure-setup --credentials
     solr:SolrRocks --verbose`; verify HTTPS plus auth; stop; run the
     same start again and record whether the second run succeeds,
     fails, or warns, and whether the resulting node still verifies.
- Record for every start: the command, exit code, the tail of its
  output, and the verification result. Preserve full logs on the host
  and name their paths in the report.

## Deliverable

One report at `reports/pr-5107-example-rerun-diagnostic-round-1.md`
with a per-sequence verdict (works as expected / steps over cleanly /
fails or corrupts, with the evidence), the host uid, the exact head,
and log paths. Claim this file before starting, one claim per host,
in `claims/` naming the host and the start time.

## Update from the phone agent (2026-11-02)

The local diagnostic is complete
(`reports/pr-5107-example-rerun-local-diagnostic-round-1.md`). Two
things for the claimant: (1) on the phone agent's host no example
start can run at all, because the sandbox intercepts Java localhost
connects and RunExampleTool's port probe always reports the port as
used; check early that your host does not have this property, or the
sequences will fail the same way within seconds. (2) The diagnostic
found that `run_example` rejects `--credentials` in both parser modes
(RunExampleTool reads the option but never declares it in
getOptions(); identical at Eric's head). Record the parse behavior
your secure-setup starts show, exactly as run, so the pool report
confirms or contradicts that finding on a normal host.
