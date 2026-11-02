# Report: PR 5107 example start sequences, local diagnostic (phone agent)

Status: DONE as a diagnostic. The rerun sequences themselves are
BLOCKED on this host by an environment property (below); the run
produced one hard product finding instead. The pool assignment
`assignments/pr-5107-example-rerun-diagnostic-round-1.md` remains the
route for the sequences on a non-root host with normal networking.
No public post was made; Eric's thread stays unanswered per Nick's
direction.

- Head: F3 `e75405eff845eda0e0f4f252d447347e2206acbe`, dev
  distribution built from `~/workspace/solr-clones/5107-r3`
  (`:solr:packaging:dev`, after seeding the checksum-correct UI wasm
  zip; see the gate receipt addendum). This host is root, so starts
  used `--force`; diagnostic only, not product-test evidence.
- Logs: `~/workspace/tools/5107-rerun-logs/`, runner outputs
  `~/workspace/tools/5107-rerun-diagnostic{,2,3}.out`.

## Why no example start can run on this host

Every `bin/solr start -e <example>` aborts in RunExampleTool's port
check: `checkPortConflict` calls `isPortAvailable(8983)`, which probes
with `new Socket("localhost", port)` and treats a successful connect
as "port in use". On this VM a Java socket connect to localhost
succeeds on every port tested (8983, 8984, 9999, 80), while raw
sockets (Python, IPv4 and IPv6) are refused and nothing listens on
those ports. The sandbox's Java networking is intercepted (the
environment carries proxy variables for egress), so the probe always
reports the port as used and the tool throws "Port 8983 is already
being used by another process" before any node starts. All three
attempted starts (techproducts, films, techproducts again) failed
this way within seconds, with the port verified free at OS level.
This is a host property, not a branch behavior; the same probe shape
exists on main. No verdict on Eric's rerun questions can come from
this host.

## Hard finding: run_example does not accept --credentials in either parser

Before the port check, argument parsing runs, and there the
secure-setup starts failed for a product reason, reproduced manually
at F3 in both modes:

- Default mode: "Failed to parse command-line arguments due to:
  Unrecognized option: --credentials", followed by the run_example
  usage (which lists no credentials option).
- Picocli mode (SOLR_PICOCLI=true): rc=2, "Unknown option:
  '--credentials'", same absence in its usage.

Cause, from the code: `RunExampleTool.runImpl` reads the value at
line 534 via `cli.getOptionValue(CommonCLIOptions.CREDENTIALS_OPTION)`,
and `withCredentials` (line 635) forwards it into node starts, but
`RunExampleTool.getOptions()` never adds `CREDENTIALS_OPTION` to the
declared list. The omission is identical at Eric's head A
`7d193dac0f` (verified by reading getOptions() there), so the
half-wired state is his feature's, pre-existing our branch; the
branch does not touch RunExampleTool.java. Our repair commit F taught
bin/solr to forward the option, which exposes this next failure: as
things stand, `bin/solr start -e secure-setup --credentials u:p`
cannot complete in either parser mode, and the BATS secure-example
cases that start with valid credentials would fail the same way on
any host. The one-line shape of the fix is to declare the option in
getOptions(); whether to add it to the handoff branch is Nick's call,
so no fix was made under this diagnostic.

## State

- The dev distribution and logs stay on this host for re-checks.
- The pool assignment stands for the sequences; it now also asks the
  pool host to record the --credentials parse behavior it observes.
